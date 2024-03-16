package com.fmaupin.mspoc1.core.checker.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;

/**
 * checker pour l'intégrité des alias
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
public class AliasIntegrityChecker implements CheckerInputRule<Map<ExpressionEnum, Object>> {

	private List<Boolean> valid = new ArrayList<>();

	private String valueRef = null;

	@Override
	public boolean isValid(List<Map<ExpressionEnum, Object>> exprs) {
		List<Map<ExpressionEnum, Object>> clonedList = new ArrayList<>(exprs);

		clonedList.sort((Map<ExpressionEnum, Object> e1, Map<ExpressionEnum, Object> e2) -> e1
				.get(ExpressionEnum.KEYWORD).toString().compareTo(e2.get(ExpressionEnum.KEYWORD).toString()));

		clonedList.forEach(e -> {
			// récupére valeur alias d'une expression de type BILITERAL ou TRILITERAL
			if (e.get(ExpressionEnum.KEYWORD).toString().equals(HieroglyphEnum.BILITERAL.toString())
					|| e.get(ExpressionEnum.KEYWORD).toString().equals(HieroglyphEnum.TRILITERAL.toString())) {
				valueRef = e.get(ExpressionEnum.AS).toString();
			} else {
				// vérification si alias est utilisé dans une expression
				valid.add(e.get(ExpressionEnum.AND).toString().contains(valueRef + "."));
			}
		});

		return valid.stream().allMatch(v -> true);
	}

}
