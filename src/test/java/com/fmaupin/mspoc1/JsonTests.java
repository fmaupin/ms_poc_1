package com.fmaupin.mspoc1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fmaupin.mspoc1.core.Constants;
import com.fmaupin.mspoc1.core.enumeration.AlgorithmEnum;
import com.fmaupin.mspoc1.core.json.JsonLoader;
import com.fmaupin.mspoc1.model.Rule;

/**
 * Tests sur fichiers JSON
 *
 * @author fmaupin, 09/04/2024
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
class JsonTests {

    // exemple d'une règle pour la gestion des compléments phonétiques
    private String jsonContentExample = """
            [
                {
                    "name": "COMPLEMENT_PHONETIC_B1",
                    "feature": "PHONETIC_COMPLEMENTS",
                    "input": [
                        "UNILITERAL AS u1 AND u1.transliteration=b.transliteration[1]",
                        "BILITERAL AS b",
                        "UNILITERAL AS u2 AND u2.transliteration=b.transliteration[2]"
                    ],
                    "output": [
                        "BILITERAL"
                    ]
                }
            ]
            """;

    private JsonLoader<Rule> jsonLoader = new JsonLoader<>(new TypeReference<List<Rule>>() {
    });

    // données paramétrées
    private static List<Arguments> provideValuesForLoadWithError() {
        return List.of(Arguments.of((Object) null), Arguments.of(""));
    }

    @ParameterizedTest
    @MethodSource("provideValuesForLoadWithError")
    void testLoadListWithFileNameError(String jsonFile) {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            jsonLoader.loadList(jsonFile);
        });

        assertTrue(thrown.getMessage().contains(String.format("%s : %s", Constants.INCORRECT_VALUE, "jsonFile")));
    }

    @SuppressWarnings("rawtypes")
    @Test
    void testLoadListWithValidFile() throws IOException {
        String fileName = "valid.json";

        try (MockedStatic<JsonLoader> mocked = Mockito.mockStatic(JsonLoader.class)) {
            mocked.when(() -> JsonLoader.getFileFromResources(fileName))
                    .thenReturn(new ByteArrayInputStream(jsonContentExample.getBytes()));

            List<Rule> result = jsonLoader.loadList(fileName);

            assertNotNull(result);
            assertEquals(1, result.size());

            assertEquals("COMPLEMENT_PHONETIC_B1", result.get(0).getName());
            assertEquals(AlgorithmEnum.PHONETIC_COMPLEMENTS, result.get(0).getFeature());

            assertEquals(3, result.get(0).getInput().size());
            assertEquals(1, result.get(0).getOutput().size());
        }
    }

    @Test
    void testGetFileFromResourcesWithNonExistentFile() {
        String fileName = "non_existent.json";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            JsonLoader.getFileFromResources(fileName);
        });

        assertTrue(thrown.getMessage().contains(String.format("%s : %s", Constants.FILE_NOT_FOUND, fileName)));
    }

    @Test
    void testGetFileFromResourcesWithExistentFile() {
        String fileName = "rules.json";

        InputStream result = JsonLoader.getFileFromResources(fileName);

        assertNotNull(result);
    }
}
