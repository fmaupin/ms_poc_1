package com.fmaupin.mspoc1.service.hieroglyph.api;

import java.util.List;

import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphDb;

/**
 * Interface pour couche service pour la gestion des données hiéroglyphiques en
 * base de données
 *
 * @author fmaupin, 02/11/2023
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
@FunctionalInterface
public interface HieroglyphDbApi {

    public List<HieroglyphDb> findAll();

}
