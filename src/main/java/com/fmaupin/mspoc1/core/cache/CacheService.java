package com.fmaupin.mspoc1.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.fmaupin.mspoc1.model.Hieroglyph;
import static java.util.Optional.ofNullable;

/**
 * Couche service pour la gestion des caches
 * 
 * @author fmaupin, 15/01/2023
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
public class CacheService {

    private static final String ALL_HIEROGLYPHS_CACHE_ALIAS = "all_hieroglyphs";

    private static final String ALL_HIEROGLYPHS_CACHE_KEY = "all_hieroglyphs";

    @Autowired
    private CacheManager cacheManager;

    /**
     * @param signid : peut être composé de plusieurs signid (codification Gardiner)
     *               séparé par "-"
     * 
     * @return objet hiéroglyph ou pas
     */
    @SuppressWarnings("unchecked")
    public Optional<Hieroglyph> getHieroglyph(List<String> signid) {
        Optional<Cache> cache = ofNullable(
                cacheManager.getCache(ALL_HIEROGLYPHS_CACHE_ALIAS));

        if (cache.isPresent()) {
            Optional<ArrayList<Hieroglyph>> cachedEntries = ofNullable(
                    cache.get().get(ALL_HIEROGLYPHS_CACHE_KEY, ArrayList.class));

            if (cachedEntries.isPresent()) {
                return cachedEntries.get().stream().filter(h -> h.getSignid().equals(signid)).findAny();
            }
        }

        return Optional.empty();
    }
}
