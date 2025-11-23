// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.bz.opendatahub.alpinebits.handshaking.dto.HandshakingData;

/**
 * This class provides methods to convert {@link HandshakingData}
 * to / from JSON.
 */
public class JsonSerializer {

    private final ObjectMapper om;

    public JsonSerializer() {
        this.om = new ObjectMapper();
        // Don't include empty sets in serialization, since this is against the specification
        this.om.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * Convert a string to an {@link HandshakingData} instance.
     * <p>
     * The input string is expected to be a valid JSON representation of a HandshakingData object.
     * <p>
     * In case of any conversion error, a {@link HandshakingDataConversionException} is thrown.
     *
     * @param handshakingDataAsJson Convert this string to an {@link HandshakingData} instance.
     * @return An instance of {@link HandshakingData} from the given input string.
     * @throws HandshakingDataConversionException if the input string could not be converted to
     * a {@link HandshakingData} instance.
     * @throws IllegalArgumentException if the input string is null.
     */
    public HandshakingData fromJson(String handshakingDataAsJson) {
        if (handshakingDataAsJson == null) {
            throw new IllegalArgumentException("The Handshaking data JSON must not be null");
        }
        try {
            return this.om.readValue(handshakingDataAsJson, HandshakingData.class);
        } catch (JsonMappingException e) {
            throw new HandshakingDataConversionException(
                    "Error while mapping the Handshaking data JSON to object",
                    HandshakingDataConversionException.CLIENT_ERROR,
                    e
            );
        } catch (JsonProcessingException e) {
            throw new HandshakingDataConversionException(
                    "Error while processing the Handshaking data JSON",
                    HandshakingDataConversionException.CLIENT_ERROR,
                    e
            );
        }
    }

    /**
     * Convert an {@link HandshakingData} instance to a JSON string.
     * <p>
     * In case of any conversion error, a {@link HandshakingDataConversionException} is thrown.
     *
     * @param handshakingData Convert this {@link HandshakingData} instance to a JSON string
     * @return The JSON representation of the given {@link HandshakingData}.
     * @throws IllegalArgumentException if handshakingData is null.
     */
    public String toJson(HandshakingData handshakingData) {
        if (handshakingData == null) {
            throw new IllegalArgumentException("The Handshaking data to convert to JSON must not be null");
        }
        try {
            return this.om.writeValueAsString(handshakingData);
        } catch (JsonProcessingException e) {
            throw new HandshakingDataConversionException(
                    "Error while producing the Handshaking data from JSON",
                    HandshakingDataConversionException.SERVER_ERROR,
                    e
            );
        }
    }

}
