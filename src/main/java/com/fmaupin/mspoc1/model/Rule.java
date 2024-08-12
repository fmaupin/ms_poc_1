package com.fmaupin.mspoc1.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fmaupin.mspoc1.core.checker.rule.AliasIntegrityChecker;
import com.fmaupin.mspoc1.core.checker.rule.CheckerInputRuleManager;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.CheckInputRuleException;
import com.fmaupin.mspoc1.core.exception.ThrowingConsumer;
import com.fmaupin.mspoc1.core.mapper.InputRuleMapper;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * MODEL -> règle
 *
 * @author fmaupin, 17/12/2023
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
@ToString(includeFieldNames = true)
@Slf4j
public class Rule {

    @JsonIgnore
    private InputRuleMapper inputRuleMapper = new InputRuleMapper();

    @JsonIgnore
    private CheckerInputRuleManager<Map<ExpressionEnum, Object>> checkerInputRuleManager = new CheckerInputRuleManager<>();

    @JsonIgnore
    private static int minDepth;

    @JsonIgnore
    private static int maxDepth;

    private String name;

    private AlgorithmEnum feature;

    @JsonSetter("feature")
    public void setFeature(String feature) {
        this.feature = AlgorithmEnum.valueOf(feature.toUpperCase(Locale.getDefault()));
    }

    private List<Map<ExpressionEnum, Object>> input;

    @JsonSetter("input")
    public void setInput(List<String> input) throws CheckInputRuleException {
        this.input = new ArrayList<>();

        input.forEach(ThrowingConsumer.wrapper(i -> {
            // vérifier et convertir règles en liste d'expressions
            Map<ExpressionEnum, Object> exp = inputRuleMapper.mapTo(i);
            this.input.add(exp);
        }));

        // vérifier toutes les règles
        checkerInputRuleManager.apply(this.input);
    }

    private List<String> output;

    public Rule() {
        checkerInputRuleManager.register(new AliasIntegrityChecker());
    }

    /**
     * rechercher des règles
     * 
     * @param rules   : liste des règles
     * @param pattern : pattern utilisé pour la recherche
     * 
     * @return règle(s)
     */
    public static List<Rule> getAllRules(List<Rule> rules, String pattern) {
        List<Rule> matchedRules = new ArrayList<>();

        rules.stream().filter(r -> r.getKeywordPattern().equals(pattern)).forEach(matchedRules::add);

        return matchedRules;
    }

    /**
     * retourne la liste des patterns pour les règles
     * 
     * @param rules : liste des règles
     * 
     * @param depth : nombre de lignes pour les règles
     * 
     * @return liste des patterns (règle / pattern)
     */
    public static List<Pair<Rule, String>> getPattern(List<Rule> rules, int... depth) {
        List<Pair<Rule, String>> patterns = new ArrayList<>();

        if (depth.length == 0) {
            rules.forEach(r -> patterns.add(new Pair<>(r, r.getKeywordPattern())));
        } else {
            rules.stream().filter(r -> r.getInput().size() == depth[0])
                    .forEach(r -> patterns.add(new Pair<>(r, r.getKeywordPattern())));
        }

        return patterns.stream().distinct().collect(Collectors.toList());
    }

    /**
     * retourne le nombre de lignes minimum et maximum
     * 
     * @param rules : liste des règles
     * 
     * @return min/max
     */
    public static Pair<Integer, Integer> getDepth(List<Rule> rules) {
        minDepth = 99;
        maxDepth = 0;

        rules.forEach(r -> {
            if (r.getInput().size() < minDepth) {
                minDepth = r.getInput().size();
            }

            if (r.getInput().size() > maxDepth) {
                maxDepth = r.getInput().size();
            }
        });

        return new Pair<>(minDepth, maxDepth);
    }

    /**
     * retourne la "profondeur" (nombre de lignes input)
     * 
     * @param rules  : liste de règles
     * 
     * @param filter : filtre à appliquer sur les règles
     * 
     * @return min/max pour nombre de lignes input
     */
    public static Pair<Integer, Integer> getDepth(List<Rule> rules, Predicate<Rule> filter) {
        return getDepth(rules.stream().filter(filter::test).collect(Collectors.toList()));
    }

    /**
     * retourne la "profondeur" (nombre de lignes input)
     * 
     * @param rules : liste des règles
     * 
     * @param root  : type de hiéroglyphe utilisé pour filtrer les règles
     * 
     * @return min/max pour nombre de lignes input
     */
    public static Pair<Integer, Integer> getFilteredDepth(List<Rule> rules, HieroglyphEnum root) {
        Predicate<Rule> p = t -> t.getOutput().stream().anyMatch(o -> o.equals(root.name()));

        return Rule.getDepth(rules, p);
    }

    /**
     * retourne le pattern d'une règle basé sur le mot-clé de chaque input
     * 
     * @return pattern
     */
    public String getKeywordPattern() {
        StringBuilder builder = new StringBuilder();

        input.stream().forEach(i -> {
            HieroglyphEnum keyword = (HieroglyphEnum) i.get(ExpressionEnum.KEYWORD);

            if (builder.length() > 0) {
                builder.append("|");
            }

            builder.append(keyword.name());
        });

        return builder.toString();
    }

    /**
     * @return liste des mots-clés (types hiéroglyphes)
     */
    public List<HieroglyphEnum> getKeywords() {
        List<HieroglyphEnum> keywords = new ArrayList<>();

        input.stream().forEach(i -> keywords.add((HieroglyphEnum) i.get(ExpressionEnum.KEYWORD)));

        return keywords;
    }

    /**
     * retourne la liste des indexs des mots-clés (types hiéroglyphes) par filtre
     * sur types de hiéroglyphes
     * 
     * @param main   : mot-clé principal
     * @param others : autres mots-clés
     * 
     * @return liste des indexs
     */
    public List<Integer> getIndexKeywords(HieroglyphEnum main, HieroglyphEnum... others) {
        List<HieroglyphEnum> obj = new ArrayList<>();
        obj.add(main);

        Arrays.asList(others).stream().forEach(obj::add);

        List<Integer> idx = new ArrayList<>();
        List<HieroglyphEnum> keywords = getKeywords();

        IntStream.range(0, keywords.size()).forEach(i -> {
            if (obj.contains(keywords.get(i))) {
                idx.add(i);
            }
        });

        return idx;
    }

    /**
     * retourne la liste des indexs des mots-clés (types hiéroglyphes) par pattern
     * et par types de hiéroglyphes
     * 
     * @param pattern : pattern règle
     * @param keyword : types de hiéroglyphes
     * 
     * @return liste des indexs
     */
    public static List<Integer> getIndexKeywords(List<String> pattern, HieroglyphEnum... keyword) {
        List<HieroglyphEnum> obj = new ArrayList<>();
        Arrays.asList(keyword).stream().forEach(obj::add);

        List<Integer> idx = new ArrayList<>();

        IntStream.range(0, pattern.size()).forEach(i -> {
            if (obj.toString().contains(pattern.get(i))) {
                idx.add(i);
            }
        });

        return idx;
    }

    /**
     * retourne valeur pour chaque règle value et expression 'key'
     * 
     * @param indexInput : index des règles input
     * @param key        : expression key
     * 
     * @return valeur
     */
    public Object getValueFromExpression(int indexInput, ExpressionEnum key) {
        return this.input.get(indexInput).get(key);
    }
}
