package com.fmaupin.mspoc1.core;

import lombok.NoArgsConstructor;
import lombok.AccessLevel;

/**
 * Constantes globales à l'application
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String BASE_PACKAGE = "com.fmaupin.mspoc1";

    public static final String SIGN_SPLIT_SEPARATOR = "-";

    public static final String TRANSLITERATION_SPLIT_SEPARATOR = ",";

    public static final String LABEL_SPLIT_SEPARATOR = ",";

    public static final Integer AWAIT_TERMINATION = 1000;

    public static final String NO_CACHE = "internal error : no cache %s unvailable";
}
