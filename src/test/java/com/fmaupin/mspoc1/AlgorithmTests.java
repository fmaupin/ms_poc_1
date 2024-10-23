package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fmaupin.mspoc1.core.CoreInitialization;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

/**
 * Tests sur algorithme
 *
 * @author fmaupin, 23/10/2024
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
@SpringBootTest
class AlgorithmTests {

    private HieroglyphApi hieroglyphService;

    private RuleApi ruleService;

    private enum TestEnum {
        UNDEFINED
    }

    @BeforeEach
    void init(@Autowired HieroglyphApi hieroglyphService, @Autowired RuleApi ruleService) {
        this.hieroglyphService = hieroglyphService;
        this.ruleService = ruleService;
    }

    @Test
    void testWithUnknownAlgorithm() {
        assertThrows(AlgorithmNotFoundException.class,
                () -> CoreInitialization.getInstance(hieroglyphService, ruleService).getAlgorithms()
                        .getAlgorithm(TestEnum.UNDEFINED));
    }

    @AfterEach
    void after() throws AlgorithmNotFoundException {
        CoreInitialization.getInstance(hieroglyphService, ruleService).getAlgorithms()
                .getAlgorithm(AlgorithmEnum.PHONETIC_COMPLEMENTS);
    }
}
