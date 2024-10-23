package com.fmaupin.mspoc1.core;

import com.fmaupin.mspoc1.core.algorithms.AlgorithmManager;
import com.fmaupin.mspoc1.core.algorithms.impl.SearchPhoneticComplement;
import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Initialisation micro-service / core
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
@Slf4j
public class CoreInitialization {

    private static CoreInitialization instance;

    @Getter
    private AlgorithmManager<String, PhoneticComplement> algorithms = new AlgorithmManager<>();

    private CoreInitialization(final HieroglyphApi hieroglyphService, final RuleApi ruleService) {
        // enregistrement des algorithmes
        SearchPhoneticComplement algorithm = new SearchPhoneticComplement(hieroglyphService, ruleService);

        algorithms.register(algorithm);

        log.info("register algorithms OK");
    }

    public static synchronized CoreInitialization getInstance(final HieroglyphApi hieroglyphService,
            final RuleApi ruleService) {
        if (instance == null) {
            instance = new CoreInitialization(hieroglyphService, ruleService);
        }

        return instance;
    }
}
