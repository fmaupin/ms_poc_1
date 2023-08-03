package com.fmaupin.mspoc1.model.message;

import java.sql.Timestamp;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * MODEL -> message consommé
 *
 * @author fmaupin, 27/08/2023
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
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true, includeFieldNames = true)
public class InputMessage extends Message implements Comparable<InputMessage> {

    private Timestamp consumeDate;

    @Override
    @Generated
    public int hashCode() {
        return new HashCodeBuilder().append(consumeDate).toHashCode();
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof InputMessage)) {
            return false;
        }

        InputMessage otherInputMessage = (InputMessage) o;

        return (compareTo(otherInputMessage) == 0);
    }

    @Override
    @Generated
    public int compareTo(InputMessage other) {
        return consumeDate.compareTo(other.getConsumeDate());
    }
}
