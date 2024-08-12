package com.fmaupin.mspoc1.model.converter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.Generated;
import static java.util.Objects.requireNonNull;

/**
 * Converter générique : String <-> Set<Enum>
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
public abstract class GenericEnumSetConverter<E extends Enum<E>> implements AttributeConverter<Set<E>, String> {

    protected final Class<E> clazz;

    private static final String SPLIT_SEPARATOR = ",";

    protected GenericEnumSetConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    @Override
    @Generated
    public String convertToDatabaseColumn(final Set<E> list) {
        if (list.isEmpty()) {
            return null;
        }

        return list.stream()
                .map(Enum::name)
                .collect(Collectors.joining(SPLIT_SEPARATOR));
    }

    @Override
    public Set<E> convertToEntityAttribute(final String joined) {
        requireNonNull(joined);

        List<E> values = Stream.of(joined.split(SPLIT_SEPARATOR))
                .map(e -> Enum.valueOf(clazz, e))
                .collect(Collectors.toList());

        return Set.copyOf(values);
    }

}