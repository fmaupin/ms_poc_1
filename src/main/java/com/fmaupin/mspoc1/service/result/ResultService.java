package com.fmaupin.mspoc1.service.result;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.model.enumeration.StatusEnum;
import com.fmaupin.mspoc1.model.message.InputMessage;
import com.fmaupin.mspoc1.model.message.ResultProcessMessage;
import com.fmaupin.mspoc1.service.logic.LogicService;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Couche service pour gestion des résultats issus traitement des messages
 * entrants
 *
 * @author fmaupin, 29/08/2023
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
@Service
@Slf4j
public class ResultService implements Result {

    @Value("${mspoc1.rabbitmq.out.exchange}")
    private String exchange;

    @Value("${mspoc1.rabbitmq.out.routingkey}")
    private String routingkey;

    private LogicService logicService;

    private RabbitTemplate rabbitTemplate;

    // gestionnaire d'exécution des tâches
    private ExecutorService executorService = Executors.newCachedThreadPool();

    // résultats traitement des messages entrants
    @Getter
    private List<ResultProcessMessage> resultList = new ArrayList<>();

    // queue ordonnée pour messages entrants
    @Getter
    private PriorityBlockingQueue<InputMessage> inQueue = new PriorityBlockingQueue<>();

    public ResultService(final LogicService logicService, final RabbitTemplate rabbitTemplate) {
        this.logicService = logicService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public synchronized void process(InputMessage poll)
            throws InterruptedException, ExecutionException {
        // message -> traitement couche métier -> récupération résultat
        CompletableFuture.supplyAsync(
                () -> logicService.run(poll.getMsg()),
                executorService).whenComplete((result, exception) -> {
                    if (exception != null) {
                        errorHandling(exception.getMessage());
                    } else {
                        updateResultObject(poll, result);
                    }
                });
    }

    /**
     * traiter et envoyer résultat message entrant
     * 
     * @param poll   : message entrant
     * @param result : résultat traitement message entrant
     */
    private synchronized void updateResultObject(InputMessage poll, String result) {
        OptionalInt indexFound = IntStream.range(0, resultList.size())
                .filter(mr -> resultList.get(mr).getMsg().equals(poll)).findFirst();

        if (indexFound.isPresent()) {
            int currentIndexItem = indexFound.getAsInt();

            // message COMPLETE
            setToComplete(currentIndexItem, poll, result);
        } else {
            errorHandling(String.format("result object for message %s not found", poll.toString()));
        }
    }

    @Override
    public synchronized void setToComplete(int index, InputMessage poll, String result) {
        resultList.get(index).setStatus(StatusEnum.COMPLETE);
        resultList.get(index).setResult(result);
        resultList.get(index).setProcessDate(new Timestamp(new Date().getTime()));

        log.info("message {} COMPLETE", poll);
    }

    @Override
    public synchronized void setToSend(int index, InputMessage poll) {
        resultList.get(index).setStatus(StatusEnum.SENDED);
        resultList.get(index).setSendDate(new Timestamp(new Date().getTime()));

        // push résultat traitement message vers queue sortante
        rabbitTemplate.convertAndSend(exchange, routingkey, resultList.get(index).getResult());

        log.info("message {} SENDED", poll);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(Constants.AWAIT_TERMINATION, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            errorHandling(e);

            executorService.shutdownNow();
        }

        log.info("ExecutorService shutdown");
    }

    @Override
    public void errorHandling(Exception e) {
        errorHandling(e.getMessage());
    }

    @Override
    public void errorHandling(String message) {
        log.error(message);

        Thread.currentThread().interrupt();
    }

    @Override
    public synchronized void addToQueue(String msg) {
        // horodatage message entrant
        InputMessage im = InputMessage.builder().msg(msg).consumeDate(new Timestamp(new Date().getTime())).build();

        // mise en queue du message pour traitement
        inQueue.add(im);

        // initialisation stockage résultat traitement message
        resultList.add(ResultProcessMessage.builder().msg(im).build());

        // conserver l'ordre initial des messages entrants
        Collections.sort(resultList);
    }

    @Override
    public synchronized void send() {
        IntStream
                .range(0, resultList.size())
                .filter(i -> resultList.get(i).getStatus() != StatusEnum.SENDED)
                .takeWhile(i -> resultList.get(i).getStatus() == StatusEnum.COMPLETE)
                .forEach(i -> setToSend(i, resultList.get(i).getMsg()));

        log.info("after sending messages -> {}", resultList.toString());
    }

    @Override
    public boolean isAnyMessagesToSend() {
        return !resultList.isEmpty()
                && resultList.stream().anyMatch(r -> r.getStatus() == StatusEnum.COMPLETE && r.getSendDate() == null);
    }

    @Override
    public synchronized void clean() {
        Predicate<ResultProcessMessage> isSended = item -> item.getStatus() == StatusEnum.SENDED;

        if (resultList.removeIf(isSended)) {
            log.info("after cleaning messages -> {}", resultList.toString());
        }
    }
}
