package com.fmaupin.mspoc1;

import static java.lang.annotation.ElementType.METHOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.util.Optional.ofNullable;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fmaupin.mspoc1.model.Hieroglyph;
import com.fmaupin.mspoc1.model.enumeration.HieroglyphEnum;
import com.fmaupin.mspoc1.service.CacheService;
import com.fmaupin.mspoc1.service.HieroglyphService;

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
class CacheTests {

	@Autowired
	private CacheService cacheService;

	private List<Hieroglyph> hieroglyphs;

	private Map<HieroglyphEnum, Integer> labelsCount;

	private static final String HIEROGLYPH_ID = "G1";

	// annotation customisée -> pas de chargement de data
	@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
	@Target({ METHOD })
	@interface SkipInit {
	}

	@BeforeEach
	public void init(@Autowired HieroglyphService service, TestInfo testInfo)
			throws NoSuchMethodException, SecurityException {
		cacheService.clear();

		SkipInit skipInit = testInfo.getTestMethod().get().getAnnotation(SkipInit.class);

		// charger les data uniquement pour les tests n'ayant pas d'annotation @SkipInit
		if (skipInit == null) {
			hieroglyphs = service.findAll();

			// pré-comptage des hiéroglyphes par label
			labelsCount = hieroglyphs.stream()
					.flatMap(h -> h.getLabel().stream())
					.collect(Collectors.groupingBy(Function.identity(),
							Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
		}
	}

	@Test
	@SkipInit
	void testHieroglyphsNotYetCached() {
		assertEquals(Optional.empty(), cacheService.getHieroglyph(List.of(HIEROGLYPH_ID)));
	}

	@Test
	void testHieroglyphsCached() {
		Optional<Hieroglyph> hieroglyph = ofNullable(
				hieroglyphs.stream().filter(h -> h.getSignid().contains(HIEROGLYPH_ID)).findFirst().get());

		assertEquals(hieroglyph, cacheService.getHieroglyph(List.of(HIEROGLYPH_ID)));
	}

	@Test
	void testHieroglyphByLabelCached() {
		assertEquals(labelsCount.get(HieroglyphEnum.UNILITERAL),
				cacheService.getHieroglyphsFromLabel(HieroglyphEnum.UNILITERAL).size());

		assertEquals(labelsCount.get(HieroglyphEnum.BILITERAL),
				cacheService.getHieroglyphsFromLabel(HieroglyphEnum.BILITERAL).size());

		assertEquals(labelsCount.get(HieroglyphEnum.TRILITERAL),
				cacheService.getHieroglyphsFromLabel(HieroglyphEnum.TRILITERAL).size());
	}
}
