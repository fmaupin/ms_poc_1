package com.fmaupin.mspoc1.service.logic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.stereotype.Service;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.enumeration.OutputQueueMessageEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.PhonogramApi;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

/**
 * Couche service pour implémentation détection et traitement des phonogrammes à
 * partir d'un message dans queue entrante
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
@Generated
public class LogicService implements Logic {

        private HieroglyphApi hieroglyphService;

        private PhonogramApi phonogramService;

        public LogicService(final HieroglyphApi hieroglyphService, final PhonogramApi phonogramService) {
                this.hieroglyphService = hieroglyphService;
                this.phonogramService = phonogramService;
        }

        @Override
        public String run(String message) {
                // seulement les messages qui contiennent une séquence hiéroglyphique sont pris
                // en compte
                if (hieroglyphService.isContainsKnownHieroglyphicStructure(message)) {
                        try {
                                Instant start = Instant.now();

                                JSONObject result = new JSONObject();

                                result.put(OutputQueueMessageEnum.MESSAGE.getMessage(), message);

                                // informations sur phonogrammes
                                result.put(OutputQueueMessageEnum.UNILITERAL.getMessage(),
                                                getJsonObject(message, HieroglyphEnum.UNILITERAL));
                                result.put(OutputQueueMessageEnum.BILITERAL.getMessage(),
                                                getJsonObject(message, HieroglyphEnum.BILITERAL));
                                result.put(OutputQueueMessageEnum.TRILITERAL.getMessage(),
                                                getJsonObject(message, HieroglyphEnum.TRILITERAL));

                                result.put(OutputQueueMessageEnum.NUMBER_OF_PHONETIC_COMPLEMENTS.getMessage(),
                                                phonogramService.numberOfPhoneticComplements(message));

                                // translittération message
                                String mdcTransliteration = hieroglyphService.getMdCTransliteration(message);
                                result.put(OutputQueueMessageEnum.TRANSLITERATION.getMessage(),
                                                hieroglyphService.getGardinerTransliteration(mdcTransliteration));

                                Instant end = Instant.now();

                                log.info("Thread {} - processing message : {} -> processing time : {} ms",
                                                Thread.currentThread().getName(),
                                                message,
                                                ChronoUnit.MILLIS.between(start, end));

                                log.info("Result sended : {}", result.toString());

                                return result.toString();
                        } catch (JSONException | AlgorithmNotFoundException | InputAlgorithmException
                                        | ExecuteAlgorithmException e) {
                                log.error(e.getMessage());
                        }
                }

                log.info("not hieroglyphic sequence => no result");

                return "";
        }

        private JSONObject getJsonObject(String message, HieroglyphEnum type) {
                List<HieroglyphResult> resultsU = hieroglyphService.getHieroglyphLabelsFromSequence(message,
                                type);
                JSONObject result = new JSONObject();

                result.put(OutputQueueMessageEnum.NUMBER.getMessage(), resultsU.size());

                result.put(OutputQueueMessageEnum.LOCATION.getMessage(),
                                HieroglyphResult.getIndexsInSequence(resultsU));

                return result;
        }
}
