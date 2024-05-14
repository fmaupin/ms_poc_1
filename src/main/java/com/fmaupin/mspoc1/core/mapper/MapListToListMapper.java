package com.fmaupin.mspoc1.core.mapper;

import java.util.List;

/**
 * Interface pour mapper une liste d'objets vers une autre liste d'objets
 * 
 * @author fmaupin, 29/12/2023
 *
 * @param <F> : liste d'objets source
 * @param <T> : liste d'objets destination
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
public interface MapListToListMapper<F, T> {

    List<T> mapTo(List<F> from);

}
