package com.fmaupin.mspoc1.service.hieroglyph;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.exception.RulesNotFoundException;
import com.fmaupin.mspoc1.core.json.JsonLoader;
import com.fmaupin.mspoc1.model.Rule;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

/**
 * Couche service pour la gestion des règles
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
@Service
public class RuleServiceImpl implements RuleApi {

    private static final String JSON_FILE = "rules.json";

    @Getter
    @Setter(AccessLevel.NONE)
    private List<Rule> rules;

    public RuleServiceImpl() throws IOException {
        // charger toutes les règles !
        JsonLoader<Rule> jsonLoader = new JsonLoader<>(new TypeReference<List<Rule>>() {
        });

        rules = jsonLoader.loadList(JSON_FILE);
    }

    @Override
    public List<Rule> getAllRulesFromFeature(AlgorithmEnum feature) throws RulesNotFoundException {
        requireNonNull(feature);

        return Optional.of(rules.stream().filter(r -> r.getFeature() == feature).collect(Collectors.toList()))
                .orElseThrow(() -> new RulesNotFoundException(feature));
    }
}
