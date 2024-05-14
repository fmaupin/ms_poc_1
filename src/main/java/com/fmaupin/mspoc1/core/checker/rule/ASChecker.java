package com.fmaupin.mspoc1.core.checker.rule;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;

/**
 * checker pour les expressions <AS>
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
public class ASChecker implements CheckerInputRule<String> {

	private Pattern pattern = Pattern.compile(Constants.ALIAS_REGEX);

	@Override
	public boolean isValid(List<String> exprs) {
		if (exprs == null || exprs.isEmpty()) {
			return false;
		}

		int idx = exprs.indexOf(ExpressionEnum.AS.toString());

		if (idx > 0) {
			try {
				// vérification si un alias est fourni et si son format est conforme
				Matcher matcher = pattern.matcher(exprs.get(idx + 1));

				return matcher.find();
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}

		return true;
	}

}
