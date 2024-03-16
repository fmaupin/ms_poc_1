package com.fmaupin.mspoc1.core.layer;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.fmaupin.mspoc1.core.enumeration.LayerEnum;
import com.fmaupin.mspoc1.core.enumeration.LayerTypeEnum;

import lombok.Getter;

/**
 * Gestionnaire des "couches"
 *
 * @author fmaupin, 02/01/2024
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
public class LayerManager<T> {

    @Getter
    private List<Layer> layers;

    public LayerManager() {
        layers = new ArrayList<>();
    }

    public LayerManager<T> register(List<?> data, LayerTypeEnum type, LayerEnum name) {
        requireNonNull(data);

        layers.add(new Layer.LayerBuilder().type(type).name(name).data(data).build());

        return this;
    }

    /**
     * fusionner "couches"
     * 
     * @param layers   : "couches" sélectionnées
     * @param strategy : stratégie à utiliser pour fusionner les "couches"
     * 
     * @return "couches" fusionnées
     */
    public Layer merge(List<Layer> layers, Function<List<Layer>, Layer> strategy) {
        requireNonNull(layers);

        return strategy.apply(layers);
    }

    /**
     * supprimer les "couches" enregistrées
     */
    public void clear() {
        layers.clear();
    }

}
