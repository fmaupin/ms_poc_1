package com.fmaupin.mspoc1.core.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.service.hieroglyph.api.PhonogramApi;

/**
 * Mapper une liste de signes (codification Gardiner) vers une liste d'objets
 *
 * @author fmaupin, 29/12/2023
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
public class HieroglyphMapper implements MapListToListMapper<String, HieroglyphResult> {

    private int idx; // NOSONAR

    private PhonogramApi phonogramService;

    public HieroglyphMapper(final PhonogramApi phonogramService) {
        this.phonogramService = phonogramService;
    }

    @Override
    public List<HieroglyphResult> mapTo(List<String> from) {
        List<HieroglyphResult> map = new ArrayList<>();
        idx = 0;

        from.forEach(token -> {
            Set<HieroglyphEnum> phonogramtypes = phonogramService.getTypeFromSign(token);

            if (HieroglyphEnum.isPhonogram(phonogramtypes)) {
                map.add(new HieroglyphResult(Arrays.asList(token), phonogramService.getTransliterationFromSign(token),
                        phonogramtypes, (idx + 1)));
            } else {
                Set<HieroglyphEnum> undefined = new HashSet<>();
                undefined.add(HieroglyphEnum.UNDEFINED);

                map.add(new HieroglyphResult(Arrays.asList(token), new HashSet<>(),
                        undefined, (idx + 1)));
            }

            idx++;
        });

        return map;
    }
}
