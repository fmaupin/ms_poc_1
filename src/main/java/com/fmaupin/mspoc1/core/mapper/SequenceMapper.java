package com.fmaupin.mspoc1.core.mapper;

import java.util.Arrays;
import java.util.List;

import com.fmaupin.mspoc1.core.Constants;

/**
 * Mapper une séquence hiéroglyphique (codification Gardiner) vers une liste
 * de signes
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
public class SequenceMapper implements MapObjectToListMapper<String, String> {

    @Override
    public List<String> mapTo(String from) {
        return Arrays.asList(from.replace(Constants.CARRIAGE_RETURN_REGEX, "").split(Constants.TOKEN_REGEX));
    }

}
