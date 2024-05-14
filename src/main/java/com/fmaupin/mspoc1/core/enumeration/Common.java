package com.fmaupin.mspoc1.core.enumeration;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Boite à outils pour les énumérations
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
public interface Common {

	/**
	 * récupération valeur de la clé dans énumération
	 * 
	 * @param name : clé
	 * @param e    : énumération
	 * @return null -> clé n'existe pas ou valeur
	 */
	static <E extends Enum<E>> E getValue(String name, Class<E> e) {
		Map<String, E> lookup = EnumSet.allOf(e).stream().collect(Collectors.toMap(Enum::name, Function.identity()));

		return lookup.get(name);
	}
}
