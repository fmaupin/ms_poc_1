package com.fmaupin.mspoc1.core.enumeration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * Enumération pour les translittérations selon Gardiner
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
public enum GTransliterationEnum {

    A(String.valueOf((char) 0xA722)), //
    i(String.valueOf((char) 0x1EC9)), // NOSONAR
    a(String.valueOf((char) 0xA724)), // NOSONAR
    H(String.valueOf((char) 0x1E25)), //
    x(String.valueOf((char) 0x1E2B)), // NOSONAR
    X(String.valueOf((char) 0x1E96)), //
    S(String.valueOf((char) 0x0161)), //
    q(String.valueOf((char) 0x1E33)), // NOSONAR
    T(String.valueOf((char) 0x1E6F)), //
    D(String.valueOf((char) 0x1E0F));

    @Getter
    private String unicodeValue;

    GTransliterationEnum(String value) {
        this.unicodeValue = value;
    }

    private static Map<String, GTransliterationEnum> lookup = Arrays.stream(values())
            .collect(Collectors.toMap(Enum::name, Function.identity()));

    public static GTransliterationEnum getByName(String name) {
        return lookup.get(name);
    }
}
