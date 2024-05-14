package com.fmaupin.mspoc1.model.hieroglyph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.model.Rule;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * MODEL -> complément phonétique
 *
 * @author fmaupin, 03/11/2023
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
@ToString
@RequiredArgsConstructor
public class PhoneticComplement {

    private static final String IDX_TRANSLITERATION_REGEX = "[\\[\\]]";

    private final int startIndex;

    private final int length;

    private final Rule rule;

    @Setter
    private Set<String> btTransliteration;

    @Setter
    private List<Set<String>> uTransliteration;

    /**
     * complément phonétique réel ?
     *
     * @return true / false
     */
    public boolean isReal() {
        int idxIndexKeywordsU = 0;

        List<Boolean> uniliteralTransliterationOK = new ArrayList<>();

        List<Integer> indexKeywords = rule.getIndexKeywords(HieroglyphEnum.UNILITERAL);

        // pour chaque 'uniliteral' dans la règle...
        for (int index : indexKeywords) {
            // récupérer index dans la translittération 'biliteral / triliteral' mentionnée
            // par règle 'uniliteral'
            // (e.g "xx.transliteration=yy.transliteration[1]")
            String value = (String) rule.getValueFromExpression(index, ExpressionEnum.AND);

            int idxInBT = Integer.valueOf(Constants.OPERAND_PATTERN.matcher(value).results()
                    .collect(Collectors.toList()).get(0).group(0).replaceAll(IDX_TRANSLITERATION_REGEX, "")) - 1;

            // translittération uniliteral = translittération[idxInBT] biliteral/triliteral
            // ?
            for (String u : uTransliteration.get(idxIndexKeywordsU)) {
                int nbUFound = 0;

                for (String bt : btTransliteration) {
                    if (bt.substring(idxInBT, idxInBT + 1).equals(u)) {
                        nbUFound++;
                    }
                }

                // oui
                if (nbUFound == uTransliteration.get(idxIndexKeywordsU).size()) {
                    uniliteralTransliterationOK.add(true);
                    break;
                }
            }

            idxIndexKeywordsU++;
        }

        return indexKeywords.size() == uniliteralTransliterationOK.size();
    }

}
