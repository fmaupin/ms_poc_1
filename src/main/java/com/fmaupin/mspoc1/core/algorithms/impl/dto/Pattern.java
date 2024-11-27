package com.fmaupin.mspoc1.core.algorithms.impl.dto;

import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Objet de type 'Data Transfert Object' (DTO) pour pattern
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
@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Pattern implements Comparable<Pattern> {

	private final String patternValue;

	private final int startIndex;

	private final Integer endIndex;

	private boolean valid = false;

	private PhoneticComplement phoneticComplement = null;

	@Override
	public int compareTo(Pattern o) {
		return endIndex.compareTo(o.getEndIndex());
	}
}