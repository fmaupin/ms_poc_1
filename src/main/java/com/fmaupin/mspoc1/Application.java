package com.fmaupin.mspoc1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import com.fmaupin.mspoc1.core.Constants;

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
public class Application {

	@Value("${mspoc1.rabbitmq.out.exchange}")
	private String exchange;

	@Value("${mspoc1.rabbitmq.out.routingkey}")
	private String routingkey;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/* consommateur de messages */
	@RabbitListener(queues = "#{inQueue}")
	public void listen(String in) {
		log.info("Message read : " + in);

		// envoi d'un message de test
		rabbitTemplate.convertAndSend(exchange, routingkey, in);
	}

}
