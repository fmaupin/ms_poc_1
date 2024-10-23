package com.fmaupin.mspoc1.core.exception;

import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;

/**
 * Exception si règle introuvable
 * 
 * @author fmaupin, 12/08/2024
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
public class RulesNotFoundException extends GenericException {

    public RulesNotFoundException(AlgorithmEnum feature) {
        super("rules-not-found", feature.name());
    }
}
