package com.fmaupin.mspoc1.core.checker.rule;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.fmaupin.mspoc1.core.exception.CheckInputRuleException;

import lombok.Getter;

/**
 * manager des checkers pour règles
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
public class CheckerInputRuleManager<T> {

	private List<CheckerInputRule<T>> checkers;

	public CheckerInputRuleManager() {
		checkers = new ArrayList<>();
	}

	/**
	 * enregistrer checker
	 * 
	 * @param checker : checker à enregistrer
	 * 
	 * @return instance courante
	 */
	public CheckerInputRuleManager<T> register(CheckerInputRule<T> checker) {
		requireNonNull(checker);

		checkers.add(checker);

		return this;
	}

	/**
	 * appliquer checkers
	 * 
	 * @param exprs : liste des expressions pour règles
	 * 
	 * @throws CheckInputRuleException
	 */
	public void apply(List<T> exprs) throws CheckInputRuleException {
		requireNonNull(exprs);

		for (CheckerInputRule<T> checker : checkers) {
			if (!checker.isValid(exprs)) {
				throw new CheckInputRuleException(checker.getName());
			}
		}
	}

}
