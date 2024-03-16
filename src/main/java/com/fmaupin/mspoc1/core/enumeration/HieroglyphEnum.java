package com.fmaupin.mspoc1.core.enumeration;

import java.util.Set;

/**
 * Enumération pour les types de hiéroglyphes
 *
 * @author fmaupin, 28/12/2022
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
public enum HieroglyphEnum {
	// ==> phonogrammes
	UNILITERAL,
	BILITERAL,
	TRILITERAL,

	UNDEFINED;

	/**
	 * type de hiéroglyphique est-il un phonogramme ?
	 * 
	 * @param type: type de hiéroglyphe
	 * @return true / false
	 */
	public static boolean isPhonogram(Set<HieroglyphEnum> type) {
		return type.contains(UNILITERAL) || type.contains(BILITERAL) || type.contains(TRILITERAL);
	}

	/**
	 * vérifier si clé existe dans énumération des types de hiéroglyphes ?
	 * 
	 * @param key: clé
	 * @return true / false
	 */
	public static boolean isKeyExists(String key) {
		return Common.getValue(key, HieroglyphEnum.class) != null;
	}
}
