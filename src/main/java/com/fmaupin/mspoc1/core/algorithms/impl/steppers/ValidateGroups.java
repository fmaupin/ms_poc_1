package com.fmaupin.mspoc1.core.algorithms.impl.steppers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.algorithms.Stepper;
import com.fmaupin.mspoc1.core.algorithms.impl.dto.Pattern;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.model.Rule;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;

import javafx.util.Pair;

/**
 * Algorithme complément phonétique : valider les groupes ('vrais' compléments
 * phonétiques)
 * 
 * @author fmaupin, 12/08/2024
 *
 * @since 0.0.1-SNAPSHOT
 * 
 *        mspoc1 is free software; you can redistribute it and/or modify it
 *        under
 *        the terms of the GNU Lesser General Public License as published by the
 *        Free Software Foundation; either version 3 of the License, or (at your
 *        option) any later version.
 * 
 *        mspoc1 is distributed in the hope that it will be useful, but WITHOUT
 *        ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *        FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 *        License for more details.
 * 
 *        You should have received a copy of the GNU Lesser General Public
 *        License along with this program; if not, write to the Free Software
 *        Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *        02110-1301, USA.
 */
public class ValidateGroups implements Stepper {

	private List<Rule> rules;

	private List<HieroglyphResult> input;

	private BiFunction<List<String>, Integer, Pair<Set<String>, List<Set<String>>>> getTransliteration = (pattern,
			i) -> {
		List<Integer> btIdx = Rule.getIndexKeywords(pattern, HieroglyphEnum.BILITERAL, HieroglyphEnum.TRILITERAL);

		List<Integer> uIdx = Rule.getIndexKeywords(pattern, HieroglyphEnum.UNILITERAL);
		List<Set<String>> uList = new ArrayList<>();

		uIdx.forEach(u -> uList.add(input.get(i + u).getTransliteration()));

		return new Pair<>(input.get(i + btIdx.get(0)).getTransliteration(), uList);
	};

	public ValidateGroups(List<Rule> rules, List<HieroglyphResult> input) {
		this.rules = rules;
		this.input = input;
	}

	private List<Pattern> groups;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(List<?> input) {
		groups = (List<Pattern>) input;

		return true;
	}

	@Override
	public List<?> play() {
		// valider chaque groupe...
		groups.forEach(g -> {
			List<Rule> linkedRules = Rule.getAllRules(rules, g.getPatternValue());

			for (Rule linkedRule : linkedRules) {
				PhoneticComplement pc = new PhoneticComplement(g.getStartIndex(),
						(g.getEndIndex() - g.getStartIndex()) + 1, linkedRule);

				// ajouter translittération pour phonogrammes pour évaluation
				List<String> pattern = Arrays.asList(g.getPatternValue().split(Constants.SEPARATOR_PATTERN_REGEX));

				Pair<Set<String>, List<Set<String>>> transliteration = getTransliteration.apply(pattern,
						g.getStartIndex());

				pc.setBtTransliteration(transliteration.getKey());
				pc.setUTransliteration(transliteration.getValue());

				// si complément phonétique réel => valider groupe
				if (pc.isReal()) {
					g.setValid(true);
					g.setPhoneticComplement(pc);
				}
			}
		});

		// supprimer les groupes invalides
		groups.removeIf(g -> !g.isValid());

		return groups;
	}
}
