package com.fmaupin.mspoc1.core.algorithms.impl.steppers;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fmaupin.mspoc1.core.algorithms.Stepper;
import com.fmaupin.mspoc1.core.algorithms.impl.dto.Pattern;
import com.fmaupin.mspoc1.core.algorithms.impl.dto.RootData;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;

import javafx.util.Pair;

/**
 * Algorithme complément phonétique : retrouver groupes à valider
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
public class FindGroupsToValidate implements Stepper {

	private BiFunction<Integer, List<RootData>, Integer> computeMinIndex = (index, rData) -> {
		if (index == rData.get(0).getIndex()) {
			return 0;
		}

		OptionalInt idx = IntStream.range(0, rData.size()).filter(i -> rData.get(i).getIndex() == index).findFirst();

		return (rData.get(idx.getAsInt() - 1).getIndex() + 1);
	};

	private Predicate<Pattern> isOnlyOneRoot = data -> {
		int nb = 0;

		for (String p : data.getPatternValue().split("\\|")) {
			if (p.contains(HieroglyphEnum.BILITERAL.name()) || p.contains(HieroglyphEnum.TRILITERAL.name())) {
				nb++;
			}
		}

		return nb == 1;
	};

	private Pair<Integer, Integer> biliteralDepth;

	private Pair<Integer, Integer> triliteralDepth;

	private List<String> inputPatternItems;

	public FindGroupsToValidate(Pair<Integer, Integer> biliteralDepth, Pair<Integer, Integer> triliteralDepth,
			List<String> inputPatternItems) {
		this.biliteralDepth = biliteralDepth;
		this.triliteralDepth = triliteralDepth;

		this.inputPatternItems = inputPatternItems;
	}

	private List<RootData> root;

	private List<Pattern> groups;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(List<?> input) {
		// root = bilitères ou trilitère trouvés dans séquence
		root = (List<RootData>) input;

		// group = pattern candidat pour validation
		groups = new ArrayList<>();

		return true;
	}

	@Override
	public List<?> play() {
		root.forEach(r -> {
			int currentIndex = r.getIndex();

			// calculer amplitude minimale pour patterns
			int minIndex = computeMinIndex.apply(currentIndex, root);

			// pour chaque root générer tous les "patterns" possibles (en prenant en compte
			// l'amplitude)
			while (currentIndex >= minIndex) {
				generatePattern(r.getType(), currentIndex--).stream().forEach(groups::add);
			}
		});

		// supprimer les groupes inutiles
		groups.removeIf(g -> g.getPatternValue().contains(HieroglyphEnum.UNDEFINED.name()));

		return groups;
	}

	private List<Pattern> generatePattern(HieroglyphEnum root, int index) {
		List<Pattern> patterns = new ArrayList<>();

		int currentDepth = (root == HieroglyphEnum.BILITERAL) ? biliteralDepth.getValue() : triliteralDepth.getValue();

		int minDepth = (root == HieroglyphEnum.BILITERAL) ? biliteralDepth.getKey() : triliteralDepth.getKey();

		while (currentDepth >= minDepth) {
			int lastPosition = (index + currentDepth);

			if ((index + currentDepth) > (inputPatternItems.size() - 1)) {
				lastPosition = inputPatternItems.size();
				currentDepth = (inputPatternItems.size() - index) - 1;
			} else {
				currentDepth--;
			}

			patterns.add(new Pattern(String.join("|", inputPatternItems.subList(index, lastPosition)), index,
					lastPosition - 1));
		}

		// seulement les patterns avec un seul root
		return patterns.stream().filter(f -> isOnlyOneRoot.test(f)).collect(Collectors.toList());
	}

}
