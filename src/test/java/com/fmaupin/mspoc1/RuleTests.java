package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.CustomRuntimeException;
import com.fmaupin.mspoc1.core.json.JsonLoader;
import com.fmaupin.mspoc1.model.Rule;

import javafx.util.Pair;

/**
 * Tests sur règles pour compléments phonétiques
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
class RuleTests {

    private static final String JSON_FILE = "rules.json";

    private static List<Rule> result;

    private static JsonLoader<Rule> jsonLoader = new JsonLoader<>(new TypeReference<List<Rule>>() {
    });

    private static int ruleMinDepth = 2;

    private static int ruleMaxDepth = 4;

    private static int rule0Depth = 3;

    private static int ruleBiliteralMinDepth = 2;

    private static int ruleBiliteralMaxDepth = 3;

    @BeforeAll
    static void init() throws IOException {
        result = jsonLoader.loadList(JSON_FILE);
    }

    @Test
    void testGetAllRules() {
        List<Rule> output = result.stream().filter(r -> r.getKeywordPattern().equals("UNILITERAL"))
                .collect(Collectors.toList());

        assertEquals(output, Rule.getAllRules(result, "UNILITERAL"));
    }

    @Test
    void testGetPattern() {
        List<Pair<Rule, String>> output1 = new ArrayList<>();

        List<Pair<Rule, String>> output2 = new ArrayList<>();

        result.stream().forEach(r -> output1.add(new Pair<>(r, r.getKeywordPattern())));

        result.stream().filter(r -> r.getInput().size() == ruleMinDepth)
                .forEach(r -> output2.add(new Pair<>(r, r.getKeywordPattern())));

        assertEquals(output1, Rule.getPattern(result));
        assertEquals(output2, Rule.getPattern(result, ruleMinDepth));
    }

    @Test
    void testGetDepth() {
        assertEquals(new Pair<>(ruleMinDepth, ruleMaxDepth), Rule.getDepth(result));

        Predicate<Rule> filter = r -> r.getName().equals("COMPLEMENT_PHONETIC_B1");

        assertEquals(new Pair<>(rule0Depth, rule0Depth), Rule.getDepth(result, filter));

        assertEquals(new Pair<>(ruleBiliteralMinDepth, ruleBiliteralMaxDepth),
                Rule.getFilteredDepth(result, HieroglyphEnum.BILITERAL));
    }

    @Test
    void testGetkeywords() {
        List<HieroglyphEnum> output1 = Arrays.asList(HieroglyphEnum.UNILITERAL, HieroglyphEnum.BILITERAL,
                HieroglyphEnum.UNILITERAL);

        List<Integer> output2 = Arrays.asList(0, 2);

        assertEquals(output1, result.get(0).getKeywords());

        assertEquals(output2, result.get(0).getIndexKeywords(HieroglyphEnum.UNILITERAL));

        assertEquals(output2, Rule.getIndexKeywords(Arrays.asList("UNILITERAL", "BILITERAL", "UNILITERAL"),
                HieroglyphEnum.UNILITERAL));
    }

    @Test
    void testGetValueFromExpression() {
        String output = "u1.transliteration=b.transliteration[1]";

        assertEquals(output, result.get(0).getValueFromExpression(0, ExpressionEnum.AND));
    }

    @Test
    void testSetInput() {
        Rule rule = result.get(0);

        List<String> update = new ArrayList<>();

        update.add("UNILITERAL AS u1 AND xxx");

        assertThrows(CustomRuntimeException.class,
                () -> rule.setInput(update));
    }
}
