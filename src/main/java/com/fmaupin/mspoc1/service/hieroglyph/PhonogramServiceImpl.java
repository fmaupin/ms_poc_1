package com.fmaupin.mspoc1.service.hieroglyph;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.CoreInitialization;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.PhonogramApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

/**
 * Couche service pour la gestion des phonogrammes
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
public class PhonogramServiceImpl implements PhonogramApi {

    private HieroglyphApi hieroglyphService;

    private RuleApi ruleService;

    public PhonogramServiceImpl(final HieroglyphApi hieroglyphService, final RuleApi ruleService) {
        this.hieroglyphService = hieroglyphService;
        this.ruleService = ruleService;
    }

    @Override
    public int numberOfPhoneticComplements(String sequence)
            throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        return getPhoneticComplements(sequence).size();
    }

    @Override
    public List<PhoneticComplement> getPhoneticComplements(String sequence)
            throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        return CoreInitialization.getInstance(hieroglyphService, ruleService).getAlgorithms()
                .execute(AlgorithmEnum.PHONETIC_COMPLEMENTS, Arrays.asList(sequence));
    }
}
