package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;

/**
 * Tests sur les hiéroglyphes
 *
 * @author fmaupin, 08/08/2024
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
class HieroglyphTests {

    private static Set<HieroglyphEnum> mockUniliteral;

    @BeforeAll
    static void init() {
        mockUniliteral = new HashSet<>();
        mockUniliteral.add(HieroglyphEnum.UNILITERAL);
    }

    @Test
    void testIfHieroglyphTypeExists() {
        boolean exists = HieroglyphEnum.isKeyExists("UNILITERAL");
        assertEquals(Boolean.TRUE, exists);

        exists = HieroglyphEnum.isKeyExists("XXXXXX");
        assertEquals(Boolean.FALSE, exists);
    }

    @Test
    void testIndexsInSequenceInHieroglyphResult() {
        Set<String> mockTransliterationFromSign = new HashSet<>(Arrays.asList("d_"));
        assertEquals("{->1->2}", HieroglyphResult.getIndexsInSequence(getHieroResultList(mockTransliterationFromSign)));
    }

    @Test
    void testPatternInHieroglyphResult() {
        Set<String> mockTransliterationFromSign = new HashSet<>(Arrays.asList("d_"));
        assertEquals("UNILITERAL|UNILITERAL",
                HieroglyphResult.getPattern(getHieroResultList(mockTransliterationFromSign)));
    }

    @Test
    void testTransliterationInHieroglyphResult() {
        Set<String> mockTransliterationFromSign = new HashSet<>(Arrays.asList("d_"));
        assertEquals("d_ d_", HieroglyphResult.getTransliteration(getHieroResultList(mockTransliterationFromSign)));

        mockTransliterationFromSign = new HashSet<>();
        assertEquals("", HieroglyphResult.getTransliteration(getHieroResultList(mockTransliterationFromSign)));
    }

    private List<HieroglyphResult> getHieroResultList(Set<String> mockTransliterationFromSign) {
        return Arrays.asList(
                new HieroglyphResult(Arrays.asList("G1"), mockTransliterationFromSign,
                        mockUniliteral, 1),
                new HieroglyphResult(Arrays.asList("M17"), mockTransliterationFromSign,
                        mockUniliteral, 2));
    }
}
