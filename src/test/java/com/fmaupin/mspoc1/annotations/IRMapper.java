package com.fmaupin.mspoc1.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation pour testing input rule mapper
 *
 * @author fmaupin, 02/04/2024
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
@Documented
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface IRMapper {
}
