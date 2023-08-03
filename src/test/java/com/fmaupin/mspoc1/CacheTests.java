package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.fmaupin.mspoc1.core.cache.CacheService;
import com.fmaupin.mspoc1.model.Hieroglyph;
import com.fmaupin.mspoc1.model.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.repository.HieroglyphRepository;
import com.fmaupin.mspoc1.service.HieroglyphService;

import lombok.extern.slf4j.Slf4j;

/**
 * Tests sur cache données
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
@SpringBootTest
@Slf4j
@TestMethodOrder(OrderAnnotation.class)
class CacheTests {

	@Autowired
	private HieroglyphService service;

	@Autowired
	private HieroglyphRepository repository;

	@Autowired
	private CacheService cacheService;

	void newEntries() {
		// entry 1
		List<String> signid = new ArrayList<>();
		signid.add("G1");

		HashSet<String> transliteration = new HashSet<>();
		transliteration.add("A");

		HashSet<HieroglyphEnum> label = new HashSet<>();
		label.add(HieroglyphEnum.UNILITERAL);

		repository.save(Hieroglyph.builder().id(Long.valueOf(1)).signid(
				signid).transliteration(transliteration).label(label).build());

		// entry 2
		signid = new ArrayList<>();
		signid.add("M17");

		transliteration = new HashSet<>();
		transliteration.add("i");

		label = new HashSet<>();
		label.add(HieroglyphEnum.UNILITERAL);

		repository.save(Hieroglyph.builder().id(Long.valueOf(2)).signid(
				signid).transliteration(transliteration).label(label).build());

	}

	@Test
	@Order(1)
	void testHieroglyphsNotYetCached() {
		assertEquals(Optional.empty(), cacheService.getHieroglyph(List.of("G1")));
	}

	@Test
	@Order(2)
	void testHieroglyphsCached() {
		newEntries();

		List<Hieroglyph> hieroglyphs = service.findAll();

		for (Hieroglyph hieroglyph : hieroglyphs) {
			log.info(hieroglyph.toString());
		}

		Optional<Hieroglyph> hieroglyph = ofNullable(hieroglyphs.get(0));

		assertEquals(hieroglyph, cacheService.getHieroglyph(List.of("G1")));
	}

	/*
	 * @Test
	 * 
	 * @Order(3)
	 * void testHieroglyphCached() {
	 * newEntries();
	 * 
	 * }
	 */
}
