package com.fmaupin.mspoc1.core.checker.rule;

import java.util.List;

import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;

/**
 * checker pour les mot-clés
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
public class KeywordChecker implements CheckerInputRule<String> {

	@Override
	public boolean isValid(List<String> exprs) {
		if (exprs == null || exprs.isEmpty()) {
			return false;
		}

		// le premier token d'une règle doit être un mot clé
		try {
			HieroglyphEnum.valueOf(exprs.get(0));
		} catch (IllegalArgumentException e) {
			return false;
		}

		// nous devons avoir une expression <AS>
		return exprs.stream().anyMatch(e -> e.contains(ExpressionEnum.AS.toString()));
	}

}
