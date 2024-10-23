package com.fmaupin.mspoc1.core.algorithms;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;

/**
 * gestionnaire des algorithmes
 *
 * @author fmaupin, 12/08/2024
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
public class AlgorithmManager<T, R> {

    private List<Algorithm<T, R>> algorithms;

    public AlgorithmManager() {
        algorithms = new ArrayList<>();
    }

    /**
     * enregistrer un algorithme
     * 
     * @param algorithm : algorithme à enregistrer
     * 
     * @return instance courante
     */
    public AlgorithmManager<T, R> register(Algorithm<T, R> algorithm) {
        requireNonNull(algorithm);

        algorithms.add(algorithm);

        return this;
    }

    /**
     * traitement de l'algorithme
     * 
     * @param feature   : algorithme à traiter
     * @param dataInput : données utilisées par l'algorithme
     * 
     * @return résultat du traitement
     * 
     * @throws AlgorithmNotFoundException
     * @throws InputAlgorithmException
     * @throws ExecuteAlgorithmException
     */
    public List<R> execute(AlgorithmEnum feature, List<T> dataInput)
            throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
        requireNonNull(dataInput);

        Algorithm<T, R> algorithm = getAlgorithm(feature);

        algorithm.input(dataInput);
        algorithm.execute();

        return algorithm.output();
    }

    /**
     * retourner l'algorithme
     * 
     * @param feature : algorithme à chercher
     * @return algorithme
     * 
     * @throws AlgorithmNotFoundException
     */
    public Algorithm<T, R> getAlgorithm(Enum<?> feature) throws AlgorithmNotFoundException {
        return algorithms.stream().filter(a -> a.getFeature().name().equals(feature.name())).findFirst()
                .orElseThrow(() -> new AlgorithmNotFoundException(feature));
    }
}
