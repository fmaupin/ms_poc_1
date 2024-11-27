package com.fmaupin.mspoc1.core;

import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

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

    // global
    public static final String BASE_PACKAGE = "com.fmaupin.mspoc1";

    public static final String OPERAND_OPTIONAL_REGEX = "\\[[1-9]\\d*\\]";

    public static final Pattern OPERAND_PATTERN = Pattern.compile(OPERAND_OPTIONAL_REGEX);

    // séparateurs
    public static final String SIGN_SPLIT_SEPARATOR = "-";

    public static final String TRANSLITERATION_SPLIT_SEPARATOR = ",";

    public static final String LABEL_SPLIT_SEPARATOR = ",";

    public static final String SEPARATOR_PATTERN_REGEX = "\\|";

    // service executor
    public static final Integer AWAIT_TERMINATION = 1000;

    // cache
    public static final String NO_CACHE = "internal error : no cache %s unavailable";

    // paramètres
    public static final String INCORRECT_VALUE = "parameter-is-incorrect";

    public static final String PATTERN_ERROR_WITH_PARAMETER = "%s : %s";

    public static final String SEQUENCE_PARAMETER = "sequence";

    public static final String DATA_PARAMETER = "data";

    public static final String SIGNID_PARAMETER = "sign-id";

    public static final String SEQUENCE_PARAMETER_ERROR = String.format("%s: %s", INCORRECT_VALUE, SEQUENCE_PARAMETER);

    public static final String SIGNID_PARAMETER_ERROR = String.format("%s: %s", INCORRECT_VALUE, SIGNID_PARAMETER);

    // fichiers json
    public static final String JSON_FILE = "rules.json";

    public static final String FILE_NOT_FOUND = "file-not-found";

    public static final String READ_DATA = "read-data";

    // mappers
    public static final String TOKEN_REGEX = "[\\s]";

    public static final String CARRIAGE_RETURN_REGEX = "\\r?\\n";

    // règles - checkers
    public static final String RULE_KEY_EXPRESSION = "transliteration";

    public static final String ALIAS_REGEX = "[A-Za-z]{1,}[0-9]{0,}+";

    public static final String RULE_COND_REGEX = "=";

    public static final Pattern RULE_COND_PATTERN = Pattern.compile(RULE_COND_REGEX);

    public static final String RULE_OPERAND_REGEX = "[a-z]+\\d*\\." + RULE_KEY_EXPRESSION;

    public static final Pattern RULE_BASIC_OP_PATTERN = Pattern.compile(RULE_OPERAND_REGEX);

    public static final Pattern RULE_OPERAND_PATTERN = Pattern.compile(OPERAND_OPTIONAL_REGEX);

    public static final String RULE_INDICATOR_OPTION = "[";
}
