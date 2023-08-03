package com.fmaupin.mspoc1.service;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.fmaupin.mspoc1.model.Hieroglyph;
import com.fmaupin.mspoc1.repository.HieroglyphRepository;

/**
 * Couche service pour la gestion des hiéroglyphes
 *
 * @author fmaupin, 28/12/2022
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
public class HieroglyphService {

    private HieroglyphRepository hieroglyphRepository;

    public HieroglyphService(final HieroglyphRepository hieroglyphRepository) {
        this.hieroglyphRepository = hieroglyphRepository;
    }

    /**
     * @return liste de tous les objets "hiéroglyphes"
     */
    @Cacheable(value = "all_hieroglyphs", key = "'all_hieroglyphs'")
    public List<Hieroglyph> findAll() {
        return hieroglyphRepository.findAll();
    }

}
