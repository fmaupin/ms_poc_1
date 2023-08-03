package com.fmaupin.mspoc1.model.message;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fmaupin.mspoc1.model.enumeration.StatusEnum;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * MODEL -> résultat traitement d'un message
 *
 * @author fmaupin, 29/08/2023
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
@Builder
@ToString
public class ResultProcessMessage implements Comparable<ResultProcessMessage> {

    private InputMessage msg;

    @Builder.Default
    private StatusEnum status = StatusEnum.IN_PROGRESS;

    private String result;

    private Timestamp processDate;

    private Timestamp sendDate;

    @Override
    @Generated
    public int hashCode() {
        return new HashCodeBuilder().append(msg.getConsumeDate()).append(status).append(result).append(processDate)
                .append(sendDate)
                .toHashCode();
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof ResultProcessMessage)) {
            return false;
        }

        ResultProcessMessage otherInputMessage = (ResultProcessMessage) o;

        return (compareTo(otherInputMessage) == 0);
    }

    @Override
    @Generated
    public int compareTo(ResultProcessMessage other) {
        // ATTENTION : tri sur date de consommation des messages -> conserver ordre
        // initial des messages
        return msg.getConsumeDate().compareTo(other.getMsg().getConsumeDate());
    }

}
