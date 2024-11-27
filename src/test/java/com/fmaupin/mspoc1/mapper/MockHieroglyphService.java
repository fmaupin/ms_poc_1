package com.fmaupin.mspoc1.mapper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;

/**
 * Configuration mockée pour test service de la gestion hiéroglyphes côté
 * applicatif
 *
 * @author fmaupin, 31/03/2024
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
@TestConfiguration
public class MockHieroglyphService {

    @Bean
    public HieroglyphApi hieroglyphService() {
        return new HieroglyphApi() {

            @Override
            public List<String> getTokens(String sequence) {
                throw new UnsupportedOperationException("Unimplemented method 'getTokens'");
            }

            @Override
            public List<HieroglyphResult> mapTo(String sequence) {
                throw new UnsupportedOperationException("Unimplemented method 'mapTo'");
            }

            @Override
            public boolean isContainsKnownHieroglyphicStructure(String sequence) {
                throw new UnsupportedOperationException("Unimplemented method 'isContainsKnownHieroglyphicStructure'");
            }

            @Override
            public String getMdCTransliteration(String sequence)
                    throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
                throw new UnsupportedOperationException("Unimplemented method 'getMdCTransliteration'");
            }

            @Override
            public String getGardinerTransliteration(String mdcTransliteration) {
                throw new UnsupportedOperationException("Unimplemented method 'getGardinerTransliteration'");
            }

            @Override
            public List<HieroglyphResult> getHieroglyphLabelsFromSequence(String sequence, HieroglyphEnum... labels) {
                throw new UnsupportedOperationException("Unimplemented method 'getHieroglyphLabelsFromSequence'");
            }

            @Override
            public Set<HieroglyphEnum> getLabelsFromSign(String signId) {
                return signId == "XX" ? new HashSet<>(Arrays.asList(HieroglyphEnum.UNDEFINED))
                        : new HashSet<>(Arrays.asList(HieroglyphEnum.UNILITERAL));
            }

            @Override
            public Set<String> getTransliterationFromSign(String signId) {
                return signId == "XX" ? new HashSet<>() : new HashSet<>(Arrays.asList("d_"));
            }

        };
    }
}
