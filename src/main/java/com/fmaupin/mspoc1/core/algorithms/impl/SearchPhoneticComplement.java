package com.fmaupin.mspoc1.core.algorithms.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.algorithms.Algorithm;
import com.fmaupin.mspoc1.core.algorithms.impl.dto.Pattern;
import com.fmaupin.mspoc1.core.algorithms.impl.steppers.FindBiliteralsAndTriliterals;
import com.fmaupin.mspoc1.core.algorithms.impl.steppers.FindGroupsToValidate;
import com.fmaupin.mspoc1.core.algorithms.impl.steppers.RemoveOverlapPhoneticComplements;
import com.fmaupin.mspoc1.core.algorithms.impl.steppers.StepperManager;
import com.fmaupin.mspoc1.core.algorithms.impl.steppers.ValidateGroups;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.core.exception.RulesNotFoundException;
import com.fmaupin.mspoc1.model.Rule;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

import javafx.util.Pair;

/**
 * Algorithme pour la gestion des compléments phonétiques (phonogrammes)
 *
 * @author fmaupin, 12/08/2024
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
public class SearchPhoneticComplement implements Algorithm<String, PhoneticComplement> {

    private HieroglyphApi hieroglyphService;

    private RuleApi ruleService;

    private List<HieroglyphResult> input;

    private List<String> inputPatternItems;

    private List<Rule> rules;

    private Pair<Integer, Integer> biliteralDepth;

    private Pair<Integer, Integer> triliteralDepth;

    private List<PhoneticComplement> output;

    public SearchPhoneticComplement(final HieroglyphApi hieroglyphService, final RuleApi ruleService) {
        this.hieroglyphService = hieroglyphService;
        this.ruleService = ruleService;
    }

    @Override
    public AlgorithmEnum getFeature() {
        return AlgorithmEnum.PHONETIC_COMPLEMENTS;
    }

    @Override
    public void input(List<String> data) throws InputAlgorithmException {
        input = hieroglyphService.mapTo(data.get(0));

        // pattern des données
        String inputPattern = HieroglyphResult.getPattern(input);

        inputPatternItems = Arrays.asList(inputPattern.split(Constants.SEPARATOR_PATTERN_REGEX));

        try {
            // récupérer toutes les règles pour l'algorithme
            rules = ruleService.getAllRulesFromFeature(getFeature());

            // profondeur min/max pour règles / bilitères (ex profondeur '2' =
            // UNILITERAL|BILITERAL)
            biliteralDepth = Rule.getFilteredDepth(rules, HieroglyphEnum.BILITERAL);

            // profondeur min/max pour règles / trilitères (ex profondeur '3' =
            // UNILITERAL|TRILITERAL|UNILITERAL)
            triliteralDepth = Rule.getFilteredDepth(rules, HieroglyphEnum.TRILITERAL);
        } catch (RulesNotFoundException e) {
            throw new InputAlgorithmException(getFeature().name());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute() throws ExecuteAlgorithmException {
        output = new ArrayList<>();

        // l'algorithme est divisé en plusieurs étapes qui seront exécutées en mode
        // séquentiel ('pipe')
        // l'entrée d'une étape est la valeur retournées par la précédente étape
        StepperManager stepperManager = new StepperManager();

        stepperManager //
                .register(new FindBiliteralsAndTriliterals()) //
                .register(new FindGroupsToValidate(biliteralDepth, triliteralDepth, inputPatternItems)) //
                .register(new ValidateGroups(rules, input)) //
                .register(new RemoveOverlapPhoneticComplements());

        List<Pattern> groups = (List<Pattern>) stepperManager.playAll(inputPatternItems);

        // retourner tous les compléments phonétiques qui ont été trouvés
        output.addAll(groups.stream().map(Pattern::getPhoneticComplement).collect(Collectors.toList()));
    }

    @Override
    public List<PhoneticComplement> output() {
        return output;
    }
}
