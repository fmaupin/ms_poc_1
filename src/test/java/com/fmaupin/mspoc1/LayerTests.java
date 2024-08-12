package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.fmaupin.mspoc1.core.enumeration.LayerEnum;
import com.fmaupin.mspoc1.core.enumeration.LayerTypeEnum;
import com.fmaupin.mspoc1.core.exception.CustomRuntimeException;
import com.fmaupin.mspoc1.core.exception.LayerMergeException;
import com.fmaupin.mspoc1.core.exception.ThrowingFunction;
import com.fmaupin.mspoc1.core.layer.Layer;
import com.fmaupin.mspoc1.core.layer.LayerManager;
import com.fmaupin.mspoc1.core.mapper.HieroglyphMapper;
import com.fmaupin.mspoc1.core.mapper.SequenceMapper;
import com.fmaupin.mspoc1.mapper.MockPhonogramService;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphResult;
import com.fmaupin.mspoc1.service.hieroglyph.api.PhonogramApi;

/**
 * Tests sur layers
 *
 * @author fmaupin, 03/08/2024
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
@SpringBootTest
@Import(MockPhonogramService.class)
class LayerTests {

    private SequenceMapper sequenceMapper;

    private HieroglyphMapper hieroglyphMapper;

    private LayerManager<String> layerManager = new LayerManager<>();

    private final String sequence = "G1 M17";

    @BeforeEach
    void init(@Autowired PhonogramApi phonogramService) {
        sequenceMapper = new SequenceMapper();

        hieroglyphMapper = new HieroglyphMapper(phonogramService);

        layerManager.clear();

        layerManager.register(hieroglyphMapper.mapTo(sequenceMapper.mapTo(sequence)), LayerTypeEnum.BASIC,
                LayerEnum.SIGN);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testMerge() {
        Layer merge = layerManager.merge(layerManager.getLayers(),
                ThrowingFunction.wrapper(this::mergeStrategyForTransliteration));

        String result = HieroglyphResult
                .getTransliteration((List<HieroglyphResult>) merge.getData());

        assertEquals("d_ d_", result);
    }

    @Test
    void testBuildLayer() {
        List<String> sequenceList = Arrays.asList("G1", "M17");

        Layer layer = Layer.builder().name(LayerEnum.SIGN).type(LayerTypeEnum.BASIC).data(sequenceList).build();

        assertEquals(LayerEnum.SIGN, layer.getName());
        assertEquals(LayerTypeEnum.BASIC, layer.getType());
        assertEquals(sequenceList, layer.getData());
    }

    @Test
    void testExceptionLayer() {
        layerManager.clear();

        layerManager.register(hieroglyphMapper.mapTo(sequenceMapper.mapTo(sequence)), LayerTypeEnum.BASIC,
                LayerEnum.PHONETIC_COMPLEMENT);

        try {
            layerManager.merge(layerManager.getLayers(),
                    ThrowingFunction.wrapper(this::mergeStrategyForTransliteration));
        } catch (CustomRuntimeException e) {
            assertTrue(Boolean.TRUE);
        }
    }

    private Layer mergeStrategyForTransliteration(List<Layer> layers) throws LayerMergeException {
        return layers.stream().filter(l -> l.getName() == LayerEnum.SIGN).findAny()
                .orElseThrow(() -> new LayerMergeException(LayerEnum.SIGN.name()));
    }
}
