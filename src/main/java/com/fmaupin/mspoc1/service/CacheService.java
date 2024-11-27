package com.fmaupin.mspoc1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.fmaupin.mspoc1.core.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.model.hieroglyph.HieroglyphDb;

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
@SuppressWarnings("unchecked")
public class CacheService {

    private static final String ALL_HIEROGLYPHS_CACHE_ALIAS = "all_hieroglyphs";

    private static final String ALL_HIEROGLYPHS_CACHE_KEY = "all_hieroglyphs";

    private Optional<Cache> cache;

    public CacheService(final CacheManager cacheManager) {
        cache = ofNullable(
                cacheManager.getCache(ALL_HIEROGLYPHS_CACHE_ALIAS));
    }

    /**
     * retourne un objet 'Hieroglyph'
     * 
     * @param signid : peut être composé de plusieurs signid (codification Gardiner)
     *               séparé par "-"
     * 
     * @return objet 'Hieroglyph' ou pas
     */
    public Optional<HieroglyphDb> getHieroglyph(List<String> signid) {
        Optional<ArrayList<HieroglyphDb>> cachedEntries = ofNullable(
                cache.get().get(ALL_HIEROGLYPHS_CACHE_KEY, ArrayList.class));

        if (cachedEntries.isPresent()) {
            return cachedEntries.get().stream().filter(h -> h.getSignid().equals(signid)).findFirst();
        }

        return Optional.empty();
    }

    /**
     * retourne une liste d'objets 'Hieroglyph'
     * 
     * @param label : type de hiéroglyphe
     * 
     * @return liste d'objets 'Hieroglyph'
     */
    public List<HieroglyphDb> getHieroglyphsFromLabel(HieroglyphEnum label) {
        List<HieroglyphDb> result = new ArrayList<>();

        Optional<ArrayList<HieroglyphDb>> cachedEntries = ofNullable(
                cache.get().get(ALL_HIEROGLYPHS_CACHE_KEY, ArrayList.class));

        if (cachedEntries.isPresent()) {
            result = cachedEntries.get().stream().filter(h -> h.getLabel().contains(label))
                    .collect(Collectors.toList());
        }

        return result;
    }

    public List<HieroglyphDb> getAll() {
        List<HieroglyphDb> result = new ArrayList<>();

        Optional<ArrayList<HieroglyphDb>> cachedEntries = ofNullable(
                cache.get().get(ALL_HIEROGLYPHS_CACHE_KEY, ArrayList.class));

        if (cachedEntries.isPresent()) {
            result = cachedEntries.get().stream().collect(Collectors.toList());
        }

        return result;
    }

    /**
     * vider le cache
     */
    public void clear() {
        if (cache.isPresent()) {
            cache.get().evictIfPresent(ALL_HIEROGLYPHS_CACHE_KEY);
        }
    }
}
