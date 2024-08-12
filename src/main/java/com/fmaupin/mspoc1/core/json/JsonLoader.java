package com.fmaupin.mspoc1.core.json;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmaupin.mspoc1.core.Constants;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;

/**
 * gestionnaire des chargements des fichiers JSON
 *
 * @author fmaupin, 01/01/2024
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
@Slf4j
public class JsonLoader<T> {

    private ObjectMapper mapper;

    private TypeReference<List<T>> typeReference;

    public JsonLoader(TypeReference<List<T>> typeReference) {
        mapper = new ObjectMapper();

        this.typeReference = typeReference;
    }

    /**
     * @return liste d'objets extraits à partir d'un fichier JSON
     * 
     * @throws IOException
     */
    public List<T> loadList(String jsonFile) throws IOException {
        if (jsonFile == null || "".equals(jsonFile)) {
            throw new IllegalArgumentException(String.format("%s : %s", Constants.INCORRECT_VALUE, "jsonFile"));
        }

        String data = IOUtils.toString(getFileFromResources(jsonFile), StandardCharsets.UTF_8);

        log.info(Constants.READ_DATA, jsonFile);

        return mapper.readValue(data, typeReference);
    }

    /**
     * lire contenu fichier (dans classpath, dossier 'resources')
     * 
     * @param fileName : nom du fichier JSON
     * 
     * @return inputStream
     */
    public static InputStream getFileFromResources(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        InputStream resource = classLoader.getResourceAsStream(fileName);

        if (resource == null) {
            throw new IllegalArgumentException(String.format("%s : %s", Constants.FILE_NOT_FOUND, fileName));
        }

        return resource;
    }

}