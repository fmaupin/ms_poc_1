package com.fmaupin.mspoc1.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fmaupin.mspoc1.model.converter.HieroglyphEnumSetConverter;
import com.fmaupin.mspoc1.model.converter.StringListConverter;
import com.fmaupin.mspoc1.model.converter.StringSetConverter;
import com.fmaupin.mspoc1.model.enumeration.HieroglyphEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fmaupin.mspoc1.core.Constants;

/**
 * MODEL -> Hiéroglyphe
 *
 * @author fmaupin, 26/12/2022
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
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hieroglyph implements Serializable, Comparable<Hieroglyph> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // INFO : peut contenir des 'sign id' en doublons - ordre d'insertion des
    // sign ids est important
    @Column(nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> signid;

    @Column
    @Convert(converter = StringSetConverter.class)
    @Builder.Default
    private Set<String> transliteration = new HashSet<>();

    @Column
    @Convert(converter = HieroglyphEnumSetConverter.class)
    @Builder.Default
    private Set<HieroglyphEnum> label = new HashSet<>();

    @Override
    @Generated
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(signid).append(transliteration).append(label).toHashCode();
    }

    /**
     * @return la liste des signes concaténés sous forme de chaine
     */
    private String getStringSignid() {
        return String.join(Constants.SIGN_SPLIT_SEPARATOR, signid);
    }

    /**
     * @return la liste des translittérations concaténées sous forme de chaine
     */
    private String getStringTransliteration() {
        return String.join(Constants.TRANSLITERATION_SPLIT_SEPARATOR, transliteration);
    }

    /**
     * @return la liste des labels concaténés sous forme de chaine
     */
    private String getStringLabel() {
        return label.stream()
                .map(Enum::name)
                .collect(Collectors.joining(Constants.LABEL_SPLIT_SEPARATOR));
    }

    @Override
    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Hieroglyph)) {
            return false;
        }

        Hieroglyph otherHieroghyph = (Hieroglyph) o;

        return (compareTo(otherHieroghyph) == 0);
    }

    @Override
    @Generated
    public int compareTo(Hieroglyph other) {
        return Comparator.comparing(Hieroglyph::getStringSignid, Comparator.nullsFirst(String::compareTo))
                .thenComparing(Hieroglyph::getStringTransliteration, Comparator.nullsFirst(String::compareTo))
                .thenComparing(Hieroglyph::getStringLabel, Comparator.nullsFirst(String::compareTo))
                .compare(this, other);
    }

}
