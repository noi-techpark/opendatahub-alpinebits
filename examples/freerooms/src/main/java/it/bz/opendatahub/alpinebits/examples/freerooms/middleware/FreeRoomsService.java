// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.examples.freerooms.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ErrorType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ErrorsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.MessageAcknowledgementType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelAvailNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelAvailNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.SuccessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * This service handles the DB persistence for
 * {@link FreeRoomsMiddleware}.
 */
public class FreeRoomsService {

    private static final Logger LOG = LoggerFactory.getLogger(FreeRoomsService.class);

    private final ObjectMapper om;

    public FreeRoomsService() {
        this.om = new ObjectMapper();
        this.om.enable(SerializationFeature.INDENT_OUTPUT);
        this.om.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Log the given {@link OTAHotelAvailNotifRQ} instance.
     *
     * @param otaHotelAvailNotifRQ Log this instance.
     * @return A {@link OTAHotelAvailNotifRS} instance containing either a success
     * message (if no error happened), or an error.
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public OTAHotelAvailNotifRS logFreeRooms(OTAHotelAvailNotifRQ otaHotelAvailNotifRQ) {
        try {
            // Convert to JSON
            String json = om.writeValueAsString(otaHotelAvailNotifRQ);

            // Execute business logic Log result / invoke
            LOG.info("----GOT FreeRooms REQUEST-----");
            LOG.info("{}", json);
            LOG.info("----END OF FreeRooms REQUEST-----");

            // Return success
            return buildSuccess();
        } catch (Exception e) {
            return buildError(e);
        }
    }

    private OTAHotelAvailNotifRS buildSuccess() {
        MessageAcknowledgementType mat = new MessageAcknowledgementType();
        mat.setSuccess(new SuccessType());
        mat.setVersion(BigDecimal.ONE);
        return new OTAHotelAvailNotifRS(mat);
    }

    private OTAHotelAvailNotifRS buildError(Exception e) {
        ErrorType errorType = new ErrorType();
        errorType.setValue(e.getMessage());
        errorType.setCode("450");
        errorType.setType("13");

        ErrorsType errorsType = new ErrorsType();
        errorsType.getErrors().add(errorType);
        MessageAcknowledgementType mat = new MessageAcknowledgementType();
        mat.setErrors(errorsType);
        mat.setVersion(BigDecimal.ONE);
        return new OTAHotelAvailNotifRS(mat);
    }
}
