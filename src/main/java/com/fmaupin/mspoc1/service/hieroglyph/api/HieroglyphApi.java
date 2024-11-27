package com.fmaupin.mspoc1.service.hieroglyph.api;

import java.util.List;
import java.util.Set;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;

/**
 * Interface pour couche service des hiéroglyphes
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
public interface HieroglyphApi {

    public List<String> getTokens(String sequence);

    public List<HieroglyphResult> mapTo(String sequence);

    public boolean isContainsKnownHieroglyphicStructure(String sequence);

    public String getMdCTransliteration(String sequence)
            throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException;

    public String getGardinerTransliteration(String mdcTransliteration);

    public List<HieroglyphResult> getHieroglyphLabelsFromSequence(String sequence, HieroglyphEnum... labels);

    public Set<HieroglyphEnum> getLabelsFromSign(String signId);

    public Set<String> getTransliterationFromSign(String signId);
}
