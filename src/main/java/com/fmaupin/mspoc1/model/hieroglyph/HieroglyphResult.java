package com.fmaupin.mspoc1.model.hieroglyph;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * MODEL -> Informations détaillées sur hiéroglyphe dans une séquence
 *
 * @author fmaupin, 02/11/2023
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
@Slf4j
public class HieroglyphResult extends Hieroglyph {

    // index du signe dans la séquence (index 1..N)
    private final int indexInSequence;

    public HieroglyphResult(List<String> sign, Set<String> transliteration, Set<HieroglyphEnum> label,
            int indexInSequence) {
        super(sign, transliteration, label);

        this.indexInSequence = indexInSequence;
    }

    /**
     * retourne translittération à partir d'une séquence
     * 
     * @param sequence : liste des informations sur signes de la séquence
     * 
     * @return translittération
     */
    public static String getTransliteration(List<HieroglyphResult> sequence) {
        StringBuilder builder = new StringBuilder();

        sequence.stream().forEach(s -> {
            if (Objects.nonNull(s.getTransliteration()) && !s.getTransliteration().isEmpty()) {
                builder.append(String.join("|", s.getTransliteration()));
                builder.append(" ");
            }
        });

        String tr = builder.toString();

        return tr.length() > 0 ? tr.substring(0, tr.length() - 1) : tr;
    }

    /**
     * retourne les entités utilisées dans la séquence
     * 
     * @param sequence : séquence à analyser
     * 
     * @return entités (format - entité | entité ...)
     */
    public static String getPattern(List<HieroglyphResult> sequence) {
        StringBuilder entity = new StringBuilder();

        for (HieroglyphResult s : sequence) {
            entity.append(s.getLabel().stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(Constants.LABEL_SPLIT_SEPARATOR)));
            entity.append("|");
        }

        entity.deleteCharAt(entity.length() - 1);

        log.info("pattern = {}", entity);

        return entity.toString();
    }

    /**
     * retourne les indexs des signes dans la séquence
     * 
     * @param results : liste des informations sur signes de la séquence
     * 
     * @return indexs (format string)
     */
    public static String getIndexsInSequence(List<HieroglyphResult> results) {
        StringBuilder ret = new StringBuilder();

        ret.append("{");

        for (HieroglyphResult resultDTO : results) {
            ret.append("->" + resultDTO.getIndexInSequence());
        }

        ret.append("}");

        return ret.toString();
    }

}