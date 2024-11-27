package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fmaupin.mspoc1.core.algorithms.impl.SearchPhoneticComplement;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

/**
 * Tests sur algorithme complément phonétique
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
class SearchPhoneticComplementTests {

    private SearchPhoneticComplement algorithm;

    @BeforeEach
    void init(@Autowired HieroglyphApi hieroglyphService, @Autowired RuleApi ruleService) {

        ruleService.getAllRules().clear();

        algorithm = new SearchPhoneticComplement(hieroglyphService, ruleService);
    }

    @Test
    void testWithNoRules() {
        String sequence = "D36 O29 G1 R10 E10 D58 G29 G1";

        assertThrows(InputAlgorithmException.class,
                () -> algorithm.input(new ArrayList<String>(Arrays.asList(sequence.split(" ")))));
    }

}
