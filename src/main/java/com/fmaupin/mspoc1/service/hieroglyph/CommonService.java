package com.fmaupin.mspoc1.service.hieroglyph;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.mapper.SequenceMapper;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphDb;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Outils pour la couche service de la gestion des hiéroglyphes
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
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CommonService {

    private SequenceMapper sequenceMapper = new SequenceMapper();

    /**
     * récupérer certains hiéroglyphes dans séquence selon labels
     *
     * @param sequence : séquence courante
     * @param list     : Liste des hiéroglyphes
     * @param types    : labels des hiéroglyphes à filtrer
     *
     * @return liste des hiéroglyphes
     */
    protected List<HieroglyphResult> getHieroglyphsByLabels(String sequence,
            List<HieroglyphDb> list, HieroglyphEnum... filteredLabels) {
        requireNonNull(sequence);
        requireNonNull(list);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        if (list.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s: %s", Constants.INCORRECT_VALUE, "list is empty"));
        }

        if (Arrays.asList(filteredLabels).stream().anyMatch(p -> !HieroglyphEnum.isKeyExists(p.toString()))) {
            throw new IllegalArgumentException(String.format("%s: %s", Constants.INCORRECT_VALUE, "types"));
        }

        List<HieroglyphResult> results = new ArrayList<>();

        // split séquence en tokens
        List<String> tokens = sequenceMapper.mapTo(sequence);

        // labels des hiéroglyphes trouvés dans séquence
        IntStream.range(0, tokens.size()).forEach(i -> {
            Set<HieroglyphEnum> labels = getLabelsFromSign(tokens.get(i), list);

            if (isTypesContainType(Arrays.asList(filteredLabels), labels)) {
                results.add(new HieroglyphResult(Arrays.asList(tokens.get(i)),
                        getTransliterationFromSign(tokens.get(i), list), labels, (i + 1)));
            }
        });

        return results;
    }

    /**
     * est-ce que les labels du signe traité contient au moins un label à filtrer ?
     * 
     * @param filteredLabels : labels à filtrer
     * @param labels         : labels du signe courant
     * 
     * @return true / false
     */
    private boolean isTypesContainType(List<HieroglyphEnum> filteredLabels, Set<HieroglyphEnum> labels) {
        boolean ret = false;

        for (HieroglyphEnum t : filteredLabels) {
            Optional<HieroglyphEnum> found = labels.stream()
                    .filter(h -> h.equals(t))
                    .findFirst();

            if (found.isPresent()) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    /**
     * retourner label(s) du signe
     *
     * @param signId : signe courant
     * @param list   : liste des hiéroglyphes
     *
     * @return label(s) ou HieroglyphEnum.UNDEFINED
     */
    protected static Set<HieroglyphEnum> getLabelsFromSign(String signId, List<HieroglyphDb> list) {
        requireNonNull(signId);

        Optional<HieroglyphDb> item = list.stream().filter(s -> s.getSignid().contains(signId)).findFirst();

        Set<HieroglyphEnum> undefined = new HashSet<>();
        undefined.add(HieroglyphEnum.UNDEFINED);

        return item.isPresent() ? item.get().getLabel() : undefined;
    }

    /**
     * retourner translittération du signe
     *
     * @param signId : signe courant
     * @param list   : liste des hiéroglyphes
     *
     * @return translittération ou liste vide
     */
    protected Set<String> getTransliterationFromSign(String signId, List<HieroglyphDb> list) {
        requireNonNull(signId);

        Optional<HieroglyphDb> item = list.stream().filter(s -> s.getSignid().contains(signId)).findFirst();

        return item.isPresent() ? item.get().getTransliteration() : new HashSet<>();
    }
}
