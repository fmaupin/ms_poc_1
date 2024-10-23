package com.fmaupin.mspoc1.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.fmaupin.mspoc1.core.checker.rule.ASChecker;
import com.fmaupin.mspoc1.core.checker.rule.AndChecker;
import com.fmaupin.mspoc1.core.checker.rule.KeywordChecker;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.CheckInputRuleException;
import com.fmaupin.mspoc1.core.mapper.HieroglyphMapper;
import com.fmaupin.mspoc1.core.mapper.InputRuleMapper;
import com.fmaupin.mspoc1.core.mapper.SequenceMapper;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.annotations.HMapper;
import com.fmaupin.mspoc1.annotations.IRMapper;
import com.fmaupin.mspoc1.annotations.SMapper;

/**
 * Tests sur mappers
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
@SpringBootTest
@Import(MockHieroglyphService.class)
class MapperTests {

        HieroglyphMapper hieroglyphMapper;

        InputRuleMapper inputRuleMapper;

        SequenceMapper sequenceMapper;

        // données paramétrées
        private static List<Arguments> provideObjectsForHieroglyphMapper() {
                Set<HieroglyphEnum> mockUniliteral = new HashSet<>();
                mockUniliteral.add(HieroglyphEnum.UNILITERAL);

                Set<String> mockTransliterationFromSign = new HashSet<>(Arrays.asList("d_"));

                Set<HieroglyphEnum> mockUndefined = new HashSet<>();
                mockUndefined.add(HieroglyphEnum.UNDEFINED);

                return List.of(
                                Arguments.of(Arrays.asList("G1", "M17", "D36"), Arrays.asList(
                                                new HieroglyphResult(Arrays.asList("G1"), mockTransliterationFromSign,
                                                                mockUniliteral, 1),
                                                new HieroglyphResult(Arrays.asList("M17"), mockTransliterationFromSign,
                                                                mockUniliteral, 2),
                                                new HieroglyphResult(Arrays.asList("D36"), mockTransliterationFromSign,
                                                                mockUniliteral, 3))),
                                Arguments.of(Arrays.asList("XX"), Arrays
                                                .asList(new HieroglyphResult(Arrays.asList("XX"), new HashSet<>(),
                                                                mockUndefined, 1))));
        }

        @BeforeEach
        void init(@Autowired HieroglyphApi hieroglyphService, TestInfo testInfo) {
                HMapper hieroglyphMapperTest = testInfo.getTestMethod().get().getAnnotation(HMapper.class);

                IRMapper inputRuleMapperTest = testInfo.getTestMethod().get().getAnnotation(IRMapper.class);

                SMapper sequenceMapperTest = testInfo.getTestMethod().get().getAnnotation(SMapper.class);

                if (hieroglyphMapperTest != null) {
                        hieroglyphMapper = new HieroglyphMapper(hieroglyphService);
                }

                if (inputRuleMapperTest != null) {
                        inputRuleMapper = new InputRuleMapper();
                }

                if (sequenceMapperTest != null) {
                        sequenceMapper = new SequenceMapper();
                }
        }

        @ParameterizedTest
        @MethodSource("provideObjectsForHieroglyphMapper")
        @HMapper
        void testHieroglyphMapper(List<String> input, List<HieroglyphResult> output) {
                List<HieroglyphResult> results = hieroglyphMapper.mapTo(input);

                IntStream.range(0, results.size())
                                .forEach(idx -> assertEquals(output.get(idx).toString(), results.get(idx).toString()));
        }

        @Test
        @IRMapper
        void testInputRuleMapperWithSuccess() throws CheckInputRuleException {
                String input = "UNILITERAL AS u1 AND u1.transliteration=b.transliteration[1]";
                Map<ExpressionEnum, Object> output = new HashMap<>();

                output.put(ExpressionEnum.KEYWORD, HieroglyphEnum.valueOf("UNILITERAL"));
                output.put(ExpressionEnum.AS, "u1");
                output.put(ExpressionEnum.AND, "u1.transliteration=b.transliteration[1]");

                assertEquals(output, inputRuleMapper.mapTo(input));
        }

        @Test
        @IRMapper
        void testAsChecker() throws CheckInputRuleException {
                inputRuleMapper.getCheckerInputRuleManager().removeAllcheckers().register(new ASChecker());

                checkEmptyOrNullInput();

                String inputWithoutAs = "UNILITERAL XX";

                Map<ExpressionEnum, Object> output = new HashMap<>();

                output.put(ExpressionEnum.KEYWORD, HieroglyphEnum.valueOf("UNILITERAL"));

                assertEquals(output, inputRuleMapper.mapTo(inputWithoutAs));

                String incompleteInput = "UNILITERAL AS";

                assertThrows(CheckInputRuleException.class, () -> inputRuleMapper.mapTo(incompleteInput));
        }

        @Test
        @IRMapper
        void testKeywordChecker() {
                inputRuleMapper.getCheckerInputRuleManager().removeAllcheckers().register(new KeywordChecker());

                checkEmptyOrNullInput();

                String noKeywordInput = "XXXXXXX";

                assertThrows(CheckInputRuleException.class, () -> inputRuleMapper.mapTo(noKeywordInput));
        }

        @Test
        @IRMapper
        void testAndChecker() {
                inputRuleMapper.getCheckerInputRuleManager().removeAllcheckers().register(new AndChecker());

                checkEmptyOrNullInput();

                String incompleteInput = "UNILITERAL AS u1 AND";

                assertThrows(CheckInputRuleException.class, () -> inputRuleMapper.mapTo(incompleteInput));

                String inputWithNoSecondOperand = "UNILITERAL AS u1 AND u1.transliteration=";

                assertThrows(CheckInputRuleException.class, () -> inputRuleMapper.mapTo(inputWithNoSecondOperand));

                String inputWithNoIndexInSecondOperand = "UNILITERAL AS u1 AND u1.transliteration=b.transliteration[]";

                assertThrows(CheckInputRuleException.class,
                                () -> inputRuleMapper.mapTo(inputWithNoIndexInSecondOperand));

                String inputWithIncompleteSecondOperand = "UNILITERAL AS u1 AND u1.transliteration=b.transliteration";

                assertThrows(CheckInputRuleException.class,
                                () -> inputRuleMapper.mapTo(inputWithIncompleteSecondOperand));

                String inputWithNoPrefix = "UNILITERAL AS u1 AND transliteration=b.transliteration[1]";

                assertThrows(CheckInputRuleException.class,
                                () -> inputRuleMapper.mapTo(inputWithNoPrefix));
        }

        @Test
        @SMapper
        void testSequenceMapper() {
                String input = "G1 A1 B1";
                List<String> output = Arrays.asList("G1", "A1", "B1");

                assertEquals(output, sequenceMapper.mapTo(input));
        }

        private void checkEmptyOrNullInput() {
                assertThrows(CheckInputRuleException.class, () -> inputRuleMapper.mapTo(""));

                assertThrows(NullPointerException.class, () -> inputRuleMapper.mapTo(null));
        }

}
