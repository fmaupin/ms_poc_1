package com.fmaupin.mspoc1.service.logic;

import java.util.Random;

import org.springframework.stereotype.Service;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

/**
 * Couche service pour implémentation métier
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

    private Random random = new Random();

    @Override
    public String run(String message) {
        long lowerLimit = 1000L;
        long upperLimit = 10000L;

        long processingTime = random.nextLong(lowerLimit, upperLimit);

        try {
            Thread.sleep(processingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Thread {} - processing message {} -> processing time {}", Thread.currentThread().getName(), message,
                processingTime);

        return message;
    }

}
