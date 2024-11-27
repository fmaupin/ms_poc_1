package com.fmaupin.mspoc1.core.algorithms.impl.steppers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import com.fmaupin.mspoc1.core.algorithms.Stepper;
import com.fmaupin.mspoc1.core.algorithms.impl.dto.RootData;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;

/**
 * Algorithme complément phonétique : retrouver les bilitères & trilitères dans
 * le pattern traité
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
public class FindBiliteralsAndTriliterals implements Stepper {

	private List<String> globalPattern;

	private List<RootData> root;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(List<?> input) {
		// pattern global
		globalPattern = (List<String>) input;

		// bilitères & trilitères dans pattern
		root = new ArrayList<>();

		return true;
	}

	@Override
	public List<?> play() {
		// rechercher bilitères & trilitères
		IntStream.range(0, globalPattern.size()).forEach(i -> {
			if (globalPattern.get(i).equals(HieroglyphEnum.BILITERAL.name())
					|| globalPattern.get(i).equals(HieroglyphEnum.TRILITERAL.name())) {
				root.add(new RootData(HieroglyphEnum.valueOf(globalPattern.get(i)), i));
			}
		});

		return root;
	}
}
