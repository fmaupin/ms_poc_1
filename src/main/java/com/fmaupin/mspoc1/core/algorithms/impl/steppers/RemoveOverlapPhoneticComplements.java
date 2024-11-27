package com.fmaupin.mspoc1.core.algorithms.impl.steppers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fmaupin.mspoc1.core.algorithms.Stepper;
import com.fmaupin.mspoc1.core.algorithms.impl.dto.Pattern;

import javafx.util.Pair;

/**
 * Algorithme complément phonétique : valider les groupes (supprimer les
 * compléments phonétiques qui se chevauchent)
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
public class RemoveOverlapPhoneticComplements implements Stepper {

	private List<Pattern> groups;

	private List<Integer> idxsToRemove;

	private List<Pair<Integer, Integer>> previousList;

	private Predicate<List<Pattern>> isOverlapPhoneticComplements = gr -> {
		Map<Integer, Long> countMapEndIndex = gr.stream()
				.collect(Collectors.groupingBy(Pattern::getEndIndex, Collectors.counting()));

		Map<Integer, Long> countMapStartIndex = gr.stream()
				.collect(Collectors.groupingBy(Pattern::getStartIndex, Collectors.counting()));

		return !countMapEndIndex.values().stream().filter(v -> v > 1).collect(Collectors.toList()).isEmpty()
				|| !countMapStartIndex.values().stream().filter(v -> v > 1).collect(Collectors.toList()).isEmpty();
	};

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(List<?> input) {
		groups = (List<Pattern>) input;

		Collections.sort(groups);

		// chevauchement de compléments phonétiques ?
		return isOverlapPhoneticComplements.test(groups);
	}

	@Override
	public List<?> play() {
		// chevauchement = plusieurs groupes avec le même startIndex ou le même EndIndex
		// dans ce cas nous prenons seulement un groupe (avec la grosse profondeur)

		// ex profondeur 2 = "BILITERAL|UNILITERAL"
		addPreviousList(0);

		// liste des groupes inutiles
		idxsToRemove = new ArrayList<>();

		for (int i = 1; i < groups.size(); i++) {
			if (groups.get(i).getEndIndex().equals(groups.get(i - 1).getEndIndex())
					|| groups.get(i).getStartIndex() == groups.get(i - 1).getStartIndex()) {
				previousList.add(new Pair<>(i, groups.get(i).getPhoneticComplement().getLength()));
			} else {
				// prendre le pattern avec la plus grosse profondeur
				if (previousList.size() > 1) {
					takePatternWithBiggestDepth();
				}

				addPreviousList(i);
			}
		}

		// prendre le pattern avec la plus grosse profondeur
		if (previousList.size() > 1) {
			takePatternWithBiggestDepth();
		}

		// supprimer les compléments phonétiques inutiles
		Collections.sort(idxsToRemove, Collections.reverseOrder());

		idxsToRemove.forEach(idx -> groups.remove(idx.intValue()));

		return groups;
	}

	private void addPreviousList(int i) {
		previousList = new ArrayList<>();

		previousList.add(new Pair<>(i, groups.get(i).getPhoneticComplement().getLength()));
	}

	private void takePatternWithBiggestDepth() {
		Integer max = Collections
				.max(previousList.stream().map(Pair<Integer, Integer>::getValue).collect(Collectors.toList()));

		previousList.stream().filter(p -> !p.getValue().equals(max)).map(Pair<Integer, Integer>::getKey)
				.forEach(idxsToRemove::add);
	}
}
