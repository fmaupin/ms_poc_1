package com.fmaupin.mspoc1.model.converter;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.NoArgsConstructor;

/**
 * Converter : String <-> Set<String>
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
@Converter
@NoArgsConstructor
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    private static final String SPLIT_SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(final Set<String> list) {
        if (list.isEmpty()) {
            return null;
        }

        return String.join(SPLIT_SEPARATOR, list);
    }

    @Override
    public Set<String> convertToEntityAttribute(final String joined) {
        if (StringUtils.isBlank(joined)) {
            return Collections.emptySet();
        }

        List<String> values = Stream.of(joined.split(SPLIT_SEPARATOR))
                .collect(Collectors.toList());

        return Set.copyOf(values);
    }

}