package com.fmaupin.mspoc1.core.checker.rule;

import java.util.List;
import java.util.regex.Matcher;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.enumeration.ExpressionEnum;

/**
 * checker pour les expressions <AND>
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
public class AndChecker implements CheckerInputRule<String> {

	private static final int OPERANDS_NUMBER = 2;

	@Override
	public boolean isValid(List<String> exprs) {
		if (exprs == null || exprs.get(0).equals("")) {
			return false;
		}

		int idx = exprs.indexOf(ExpressionEnum.AND.toString());

		if (idx > 0) {
			try {
				// expression de type condition valide ?
				if (!isCondPattern(exprs.get(idx + 1))) {
					return false;
				}

				// vérification du format de chaque opérande dans l'expression de type condition
				// format opérande : <alias> + "." + <expression key> + ["[<index dans
				// expression key>]"] avec index > 0
				int opNumber = 1;
				for (String operand : exprs.get(idx + 1).split(Constants.RULE_COND_REGEX)) {
					if (!isValidOperand(operand, opNumber)) {
						return false;
					}

					opNumber++;
				}
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}

		return true;
	}

	/**
	 * expression de type condition ?
	 *
	 * @param expression : expression à analyser
	 * @return true / false
	 */
	private boolean isCondPattern(String expression) {
		// vérification si nous avons une expression de type condition
		Matcher matcher = Constants.RULE_COND_PATTERN.matcher(expression);

		// seulement deux opérandes sont permises
		String[] operands = expression.split(Constants.RULE_COND_REGEX);

		return matcher.find() && operands.length == OPERANDS_NUMBER;
	}

	/**
	 * l'opérande est-elle valide ?
	 *
	 * @param operand  : opérande à analyser
	 * @param opNumber : numéro opérande dans expression
	 * 
	 * @return true / false
	 */
	private boolean isValidOperand(String operand, int opNumber) {
		// vérification si préfixe expression
		Matcher matcher = Constants.RULE_BASIC_OP_PATTERN.matcher(operand);

		if (!matcher.find()) {
			return false;
		}

		// vérification format index si opérande est indexée
		if (opNumber == OPERANDS_NUMBER) {
			if (!operand.contains(Constants.RULE_INDICATOR_OPTION)) {
				return false;
			}

			return Constants.OPERAND_PATTERN.matcher(operand).find();
		}

		return true;
	}

}
