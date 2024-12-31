package com.fmaupin.mspoc1.service.hieroglyph;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.CoreInitialization;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.enumeration.GTransliterationEnum;
import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.core.enumeration.LayerEnum;
import com.fmaupin.mspoc1.core.enumeration.LayerTypeEnum;
import com.fmaupin.mspoc1.core.exception.AlgorithmNotFoundException;
import com.fmaupin.mspoc1.core.exception.ExecuteAlgorithmException;
import com.fmaupin.mspoc1.core.exception.InputAlgorithmException;
import com.fmaupin.mspoc1.core.exception.LayerMergeException;
import com.fmaupin.mspoc1.core.exception.ThrowingFunction;
import com.fmaupin.mspoc1.core.layer.Layer;
import com.fmaupin.mspoc1.core.layer.LayerManager;
import com.fmaupin.mspoc1.core.mapper.HieroglyphMapper;
import com.fmaupin.mspoc1.core.mapper.SequenceMapper;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.model.hieroglyph.PhoneticComplement;
import com.fmaupin.mspoc1.service.CacheService;
import com.fmaupin.mspoc1.service.hieroglyph.api.HieroglyphApi;
import com.fmaupin.mspoc1.service.hieroglyph.api.RuleApi;

import lombok.extern.slf4j.Slf4j;

/**
 * Couche service pour la gestion des hiéroglyphes côté applicatif
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
@Slf4j
public class HieroglyphServiceImpl extends CommonService implements HieroglyphApi {

    private SequenceMapper sequenceMapper = new SequenceMapper();

    private HieroglyphMapper hieroglyphMapper;

    private LayerManager<String> layerManager = new LayerManager<>();

    private RuleApi ruleService;

    private CacheService cacheService;

    private BiFunction<String, List<HieroglyphResult>, Integer> getIndexOfBiliteralOrTriliteral = (
            transliteration, data) -> {
        int index = 0;

        for (int i = 0; i < data.size(); i++) {
            String tr = String.join(",", data.get(i).getTransliteration());

            if (transliteration.equals(tr)) {
                index = i;
                break;
            }
        }

        return index;
    };

    public HieroglyphServiceImpl(final RuleApi ruleService, final CacheService cacheService) {
        this.ruleService = ruleService;

        hieroglyphMapper = new HieroglyphMapper(this);
        this.cacheService = cacheService;
    }

    @Override
    public List<String> getTokens(String sequence) {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        return sequenceMapper.mapTo(sequence);
    }

    @Override
    public List<HieroglyphResult> mapTo(String sequence) {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        return hieroglyphMapper.mapTo(getTokens(sequence));
    }

    @Override
    public boolean isContainsKnownHieroglyphicStructure(String sequence) {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        // la seule structure supportée par le micro-service est la gestion des
        // phonogrammes !
        Predicate<String> p = t -> {
            boolean result = cacheService.getAll().stream()
                    .filter(h -> HieroglyphEnum.isPhonogram(h.getLabel()))
                    .anyMatch(ph -> ph.getSignid().contains(t));
            log.info("Checking token : " + t + " -> " + result);
            return result;
        };

        return sequenceMapper.mapTo(sequence).stream().anyMatch(p);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getMdCTransliteration(String sequence)
            throws AlgorithmNotFoundException, InputAlgorithmException, ExecuteAlgorithmException {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        // enregistrer couches ('SIGN' et 'PHONETIC_COMPLEMENT')
        layerManager.clear();

        layerManager.register(mapTo(sequence), LayerTypeEnum.BASIC, LayerEnum.SIGN) //
                .register(
                        CoreInitialization.getInstance(this, ruleService).getAlgorithms()
                                .execute(AlgorithmEnum.PHONETIC_COMPLEMENTS, Arrays.asList(sequence)),
                        LayerTypeEnum.BASIC, LayerEnum.PHONETIC_COMPLEMENT);

        // merge des couches
        Layer merge = layerManager.merge(layerManager.getLayers(),
                ThrowingFunction.wrapper(this::mergeStrategyForTransliteration));

        // retourner translittération à partir du merge des couches
        return HieroglyphResult
                .getTransliteration((List<HieroglyphResult>) merge.getData());
    }

    @Override
    public String getGardinerTransliteration(String mdcTransliteration) {
        requireNonNull(mdcTransliteration);

        if ("".equals(mdcTransliteration)) {
            return "";
        }

        StringBuilder seq = new StringBuilder();

        for (char c : mdcTransliteration.toCharArray()) {
            GTransliterationEnum name = GTransliterationEnum.getByName(String.valueOf(c));

            seq.append(Objects.nonNull(name) ? name.getUnicodeValue() : String.valueOf(c));
        }

        return seq.toString();
    }

    @Override
    public List<HieroglyphResult> getHieroglyphLabelsFromSequence(String sequence, HieroglyphEnum... labels) {
        requireNonNull(sequence);

        if ("".equals(sequence)) {
            throw new IllegalArgumentException(Constants.SEQUENCE_PARAMETER_ERROR);
        }

        if (labels.length == 0) {
            throw new IllegalArgumentException(
                    String.format("%s: %s", Constants.INCORRECT_VALUE, "at least one label"));
        }

        return super.getHieroglyphsByLabels(sequence, cacheService.getAll(), labels);
    }

    @Override
    public Set<HieroglyphEnum> getLabelsFromSign(String signId) {
        requireNonNull(signId);

        if ("".equals(signId)) {
            throw new IllegalArgumentException(Constants.SIGNID_PARAMETER_ERROR);
        }

        return CommonService.getLabelsFromSign(signId, cacheService.getAll());
    }

    @Override
    public Set<String> getTransliterationFromSign(String signId) {
        requireNonNull(signId);

        if ("".equals(signId)) {
            throw new IllegalArgumentException(Constants.SIGNID_PARAMETER_ERROR);
        }

        return super.getTransliterationFromSign(signId, cacheService.getAll());
    }

    /**
     * strategie pour le merge des couches
     *
     * @param layers : couches à merger
     *
     * @return couche composite
     *
     * @throws LayerMergeException
     */
    @SuppressWarnings("unchecked")
    private Layer mergeStrategyForTransliteration(List<Layer> layers) throws LayerMergeException {
        Layer signsLayer = layers.stream().filter(l -> l.getName() == LayerEnum.SIGN).findAny()
                .orElseThrow(() -> new LayerMergeException(LayerEnum.SIGN.name()));

        Optional<Layer> pcLayer = layers.stream().filter(l -> l.getName() == LayerEnum.PHONETIC_COMPLEMENT).findAny();

        // pas de compléments phonétiques => translittération basée sur la couche des
        // signes (SIGN)
        if (!pcLayer.isPresent()) {
            return signsLayer;
        }

        // compléments phonétiques => merge objets HieroglyphResult
        Layer composite = Layer.builder().type(LayerTypeEnum.COMPOSITE).name(LayerEnum.SIGN_AND_PHONETIC).build();

        List<HieroglyphResult> data = new ArrayList<>();
        data.addAll((List<HieroglyphResult>) signsLayer.getData());

        List<Integer> idxToRemove = new ArrayList<>();

        IntStream.iterate(pcLayer.get().getData().size() - 1, i -> i - 1).limit(pcLayer.get().getData().size())
                .forEach(pc -> {
                    PhoneticComplement currentPc = ((PhoneticComplement) pcLayer.get().getData().get(pc));

                    // find the HieroglyphServiceRequestResult index matching to biliteral or
                    // triliteral in current phonetic complement
                    int fromIndex = currentPc.getStartIndex();
                    int toIndex = fromIndex + currentPc.getLength() - 1;

                    int idx = getIndexOfBiliteralOrTriliteral.apply(String.join(",", currentPc.getBtTransliteration()),
                            data.subList(fromIndex, toIndex)) + fromIndex;

                    // update the data list
                    for (int i = toIndex; i >= fromIndex; i--) {
                        if (i != idx) {
                            idxToRemove.add(i);
                        }
                    }
                });

        Collections.sort(idxToRemove, Collections.reverseOrder());

        idxToRemove.forEach(i -> data.remove(i.intValue()));

        composite.setData(data);

        return composite;
    }
}
