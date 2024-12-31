package com.fmaupin.mspoc1;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.messaging.handler.annotation.Header;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.exception.ThreadInterruptedException;
import com.fmaupin.mspoc1.model.message.InputMessage;
import com.fmaupin.mspoc1.service.CacheService;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphDbApi;
import com.fmaupin.mspoc1.service.result.ResultService;
import com.rabbitmq.client.Channel;

import jakarta.annotation.PreDestroy;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

/**
 * point d'entrée de l'application
 *
 * @author fmaupin, 26/12/2022
 *
 * @since 0.0.1-SNAPSHOT
 *
 *        mspoc1 is free software; you can redistribute it and/or
 *        modify it under the terms of the GNU Lesser General Public License as
 *        published by the Free Software Foundation; either version 3 of the
 *        License, or (at your option) any later version.
 *
 *        mspoc1 is distributed in the hope that it will be
 *        useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 *        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *        Lesser General Public License for more details.
 *
 *        You should have received a copy of the GNU Lesser General Public
 *        License along with this program; if not, write to the Free Software
 *        Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *        02110-1301, USA.
 */
@SpringBootApplication
@EntityScan({ Constants.BASE_PACKAGE })
@EnableCaching
@Slf4j
@Generated
public class Application implements CommandLineRunner {

	private ResultService resultService;
	private HieroglyphDbApi hieroglyphDbService;
	private CacheService cacheService;

	// contenu dernier message entrant
	private String previousMessage = "";

	public Application(final ResultService resultService, final HieroglyphDbApi hieroglyphDbService,
			final CacheService cacheService) {
		this.resultService = resultService;
		this.hieroglyphDbService = hieroglyphDbService;
		this.cacheService = cacheService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {
		cacheService.clear();

		log.info("Reading hieroglyphs at startup...");
		hieroglyphDbService.findAll();

		Thread polling = new Thread(() -> {
			while (true) {
				try {
					// récupération message entrant
					InputMessage poll = resultService.getInQueue().take();

					// traitement message
					log.info("Polled : " + poll.toString());

					resultService.process(poll);
				} catch (InterruptedException | AmqpException | ExecutionException e) {
					Thread.currentThread().interrupt();
					throw new ThreadInterruptedException(e);
				}
			}
		});

		polling.start();

		Thread sending = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);

					if (resultService.isAnyMessagesToSend()) {
						resultService.send();
					} else {
						resultService.clean();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					throw new ThreadInterruptedException(e);
				}
			}
		});

		sending.start();

	}

	@RabbitListener(queues = "#{inQueue}", ackMode = "MANUAL")
	public void listen(String in, Channel channel,
			@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		// acquitter le message
		channel.basicAck(tag, false);

		// on ne traite pas 2 fois le même message séquentiellement !
		if (!in.equals(this.previousMessage)) {
			// mise en queue du message pour traitement
			resultService.addToQueue(in);

			this.previousMessage = in;
		} else {
			log.info("This message has already been processed");
		}
	}

	@PreDestroy
	public void preDestroy() {
		resultService.shutdown();
	}
}
