package com.fmaupin.mspoc1.core.exception;

import java.util.function.Consumer;
import static java.util.Objects.requireNonNull;

/**
 * wrapper qui prend en charge la gestion des exceptions pour toute expression
 * lambda qui ne retourne pas de résultat
 *
 * https://www.baeldung.com/java-lambda-exceptions
 * 
 * @author fmaupin, 29/12/2023
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
public interface ThrowingConsumer<T, E extends Exception> {

    public void accept(T t) throws E;

    static <T> Consumer<T> unchecked(ThrowingConsumer<? super T, ?> consumer) {
        requireNonNull(consumer);

        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw new CheckedException(e);
            }
        };
    }

}
