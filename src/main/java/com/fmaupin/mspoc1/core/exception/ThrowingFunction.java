package com.fmaupin.mspoc1.core.exception;

import java.util.function.Function;
import static java.util.Objects.requireNonNull;

/**
 * wrapper qui prend en charge la gestion des exceptions lors appel fonction
 * dans expression lambda
 *
 * Fonction avec un seul paramètre et valeur de retour
 * 
 * https://www.baeldung.com/java-lambda-exceptions
 * 
 * @param <T> type paramètre fonction
 * 
 * @param <E> type exception 'checked' levée
 * 
 * @param <R> type valeur retournée
 * 
 * @author fmaupin, 04/08/2024
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
public interface ThrowingFunction<T, R, E extends Exception> {
    R apply(T arg) throws E;

    static <T, R> Function<T, R> wrapper(final ThrowingFunction<? super T, ? extends R, ?> function) {
        requireNonNull(function);

        return t -> {
            try {
                return function.apply(t);
            } catch (final Exception e) {
                throw new CustomRuntimeException(e);
            }
        };
    }
}
