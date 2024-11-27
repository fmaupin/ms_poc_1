package com.fmaupin.mspoc1.core.algorithms.impl.steppers;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.fmaupin.mspoc1.core.algorithms.Stepper;

import lombok.extern.slf4j.Slf4j;

/**
 * Gestionnaire des steppers (étapes traitement algorithme)
 * 
 * @author fmaupin, 12/08/2024
 *
 * @since 0.0.1-SNAPSHOT
 * 
 *        mspoc1 is free software; you can redistribute it and/or modify it
 *        under
 *        the terms of the GNU Lesser General Public License as published by the
 *        Free Software Foundation; either version 3 of the License, or (at your
 *        option) any later version.
 * 
 *        mspoc1 is distributed in the hope that it will be useful, but WITHOUT
 *        ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *        FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *        License for more details.
 * 
 *        You should have received a copy of the GNU Lesser General Public
 *        License along with this program; if not, write to the Free Software
 *        Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *        02110-1301, USA.
 */
@Slf4j
public class StepperManager {

    private List<Stepper> steppers;

    public StepperManager() {
        steppers = new ArrayList<>();
    }

    /**
     * enregister l'étape de traitement
     * 
     * @param stepper : étape à enregistrer
     * 
     * @return instance courante
     */
    public StepperManager register(Stepper stepper) {
        requireNonNull(stepper);

        steppers.add(stepper);

        return this;
    }

    /**
     * exécuter toutes les étapes (mode 'pipe') d'un algorithme
     * 
     * @param initialData : données pour la première étape à injecter
     * 
     * @return données finales
     */
    public List<?> playAll(List<?> initialData) { // NOSONAR
        List<?> pipedValue = initialData;

        boolean toBreak = false;

        for (Stepper stepper : steppers) {
            if (pipedValue.isEmpty() || !stepper.init(pipedValue)) {
                toBreak = true;
            }

            if (!toBreak) {
                pipedValue = stepper.play();

                if (pipedValue.isEmpty()) {
                    toBreak = true;
                }

                if (!toBreak) {
                    log.info("step = {}", stepper.getName() + " => OK");
                }
            }

            if (toBreak) {
                log.info("step = {}", stepper.getName() + " => BREAKING");
                break;
            }
        }

        return pipedValue;
    }
}
