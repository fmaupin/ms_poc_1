package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;
import com.fmaupin.mspoc1.service.CacheService;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphDbApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.PhonogramApi;

/**
 * Tests sur phonogrammes
 *
 * @author fmaupin, 13/08/2024
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
class PhonogramTests {

        private HieroglyphApi hieroglyphService;

        private PhonogramApi phonogramService;

        @BeforeEach
        void init(@Autowired HieroglyphApi hieroglyphService, @Autowired CacheService cacheService,
                        @Autowired HieroglyphDbApi service, @Autowired PhonogramApi phonogramService) {
                this.hieroglyphService = hieroglyphService;
                this.phonogramService = phonogramService;

                cacheService.clear();
                service.findAll();
        }

        @Test
        void testWithoutUniliterals() {
                String sequence = "M1 YY ZZ";

                List<HieroglyphResult> results = hieroglyphService.getHieroglyphLabelsFromSequence(sequence,
                                HieroglyphEnum.UNILITERAL);

                assertTrue(results.isEmpty(),
                                String.format("there must not have 'uniliteral' phonograms in the sequence : %s",
                                                sequence));

                assertThrows(IllegalArgumentException.class, () -> {
                        hieroglyphService.getHieroglyphLabelsFromSequence("", HieroglyphEnum.UNILITERAL);
                }, "An exception must be throwed");

                assertThrows(NullPointerException.class, () -> {
                        hieroglyphService.getHieroglyphLabelsFromSequence(null, HieroglyphEnum.UNILITERAL);
                }, "An exception must be throwed");
        }

        @Test
        void testWithUniliteralsWithInformation() {
                String sequence = "XX G1 D58 YY M17-M17";

                List<HieroglyphResult> results = hieroglyphService.getHieroglyphLabelsFromSequence(sequence,
                                HieroglyphEnum.UNILITERAL);

                assertEquals(3, results.size(),
                                String.format("there must have three 'uniliteral' phonograms in the sequence : %s",
                                                sequence));

                assertEquals(2, results.get(0).getIndexInSequence(), String.format(
                                "the index of the first 'uniliteral' phonogram in the sequence : %s must be equal to %s",
                                sequence, 2));

                assertEquals("G1", results.get(0).getSign().get(0),
                                String.format("the sign of the first 'uniliteral' phonogram in the sequence : %s must be equal to %s",
                                                sequence, "G1"));

                assertEquals("M17-M17", results.get(2).getSign().get(0),
                                String.format("the sign of the last 'uniliteral' phonogram in the sequence : %s must be equal to %s",
                                                sequence, "M17-M17"));
        }

        @Test
        void testWithoutBiliterals() {
                String sequence = "G1 M17";

                List<HieroglyphResult> results = hieroglyphService.getHieroglyphLabelsFromSequence(sequence,
                                HieroglyphEnum.BILITERAL);

                assertTrue(results.isEmpty(),
                                String.format("there must not have 'biliteral' phonograms in the sequence : %s",
                                                sequence));
        }

        @Test
        void testWithBiliterals() {
                String sequence = "G1 O29 V4 xx";

                List<HieroglyphResult> results = hieroglyphService.getHieroglyphLabelsFromSequence(sequence,
                                HieroglyphEnum.BILITERAL);

                assertEquals(2, results.size(),
                                String.format("there must have two 'biliteral' phonograms in the sequence : %s",
                                                sequence));

                assertEquals(3, results.get(1).getIndexInSequence(), String.format(
                                "the index of the second 'biliteral' phonogram in the sequence : %s must be equal to %s",
                                sequence, 3));
        }

        @Test
        void testWithoutTriliterals() {
                String sequence = "V4 yy O29";

                List<HieroglyphResult> results = hieroglyphService.getHieroglyphLabelsFromSequence(sequence,
                                HieroglyphEnum.TRILITERAL);

                assertTrue(results.isEmpty(),
                                String.format("there must not have 'triliteral' phonograms in the sequence : %s",
                                                sequence));
        }

        @Test
        void testWithTriliterals() {
                String sequence = "xx R8 X8 yy Aa11";

                List<HieroglyphResult> results = hieroglyphService.getHieroglyphLabelsFromSequence(sequence,
                                HieroglyphEnum.TRILITERAL);

                assertEquals(2, results.size(),
                                String.format("there must have two 'triliteral' phonograms in the sequence : %s",
                                                sequence));

                assertEquals(5, results.get(1).getIndexInSequence(),
                                String.format("the index of the second 'triliteral' phonogram in the sequence : %s must be equal to %s",
                                                sequence, 5));
        }

        @Test
        void testNumberOfPhoneticComplementsInSequence()
                        throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
                // xx | xx = 'vrai' complément phonétique
                // xx - xx = 'faux' complément phonétique
                // ... = pas de phonogrammes

                // pattern : uniliteral | biliteral | uniliteral ... uniliteral | biliteral |
                // uniliteral
                String sequence1 = "D36 O29 G1 R10 E10 D58 G29 G1";
                assertEquals(2, phonogramService.numberOfPhoneticComplements(sequence1));

                // pattern : uniliteral - biliteral ...
                String sequence2 = "D36 O29 XX YY";
                assertEquals(0, phonogramService.numberOfPhoneticComplements(sequence2));

                // pattern : ... biliteral | uniliteral ... uniliteral - biliteral
                String sequence3 = "XX O29 D36 YY D36 O29";

                assertEquals(1, phonogramService.numberOfPhoneticComplements(sequence3));

                // pattern : uniliteral | triliteral | uniliteral ... triliteral | uniliteral
                // ... triliteral - uniliteral
                String sequence4 = "D36 S42 G1 XX YY S39 X1 E1 E2 Aa20 D21";
                assertEquals(3, phonogramService.numberOfPhoneticComplements(sequence4));
        }

        @Test
        void testGetPhoneticComplementsInSequence()
                        throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
                Set<String> uTransliteration0 = new HashSet<>(Arrays.asList("c"));
                Set<String> uTransliteration0b = new HashSet<>(Arrays.asList("3"));

                List<Set<String>> uTransliteration0List = new ArrayList<>();
                uTransliteration0List.add(uTransliteration0);
                uTransliteration0List.add(uTransliteration0b);

                Set<String> uTransliteration1 = new HashSet<>(Arrays.asList("n"));
                List<Set<String>> uTransliteration1List = new ArrayList<>();
                uTransliteration1List.add(uTransliteration1);

                String sequence = "D36 O29 G1 O28 N35";
                List<PhoneticComplement> pc = phonogramService.getPhoneticComplements(sequence);

                assertEquals(uTransliteration0List, pc.get(0).getUTransliteration());

                assertEquals(uTransliteration1List, pc.get(1).getUTransliteration());
        }

        @Test
        void testSearchPhoneticComplementWithNoDataInput() {
                assertThrows(IllegalArgumentException.class,
                                () -> phonogramService.numberOfPhoneticComplements(""));
        }
}
