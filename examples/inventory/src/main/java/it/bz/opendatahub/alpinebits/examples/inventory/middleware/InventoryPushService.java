// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.examples.inventory.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ErrorType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.ErrorsType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.MessageAcknowledgementType;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAHotelDescriptiveContentNotifRS;
import it.bz.opendatahub.alpinebits.xml.schema.ota.SuccessType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * This service handles the DB persistence for
 * {@link InventoryPushMiddleware}.
 */
public class InventoryPushService {

    private static final Logger LOG = LoggerFactory.getLogger(InventoryPushService.class);

    private final ObjectMapper om;

    public InventoryPushService() {
        this.om = new ObjectMapper();
        this.om.enable(SerializationFeature.INDENT_OUTPUT);
        this.om.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Log the given {@link OTAHotelDescriptiveContentNotifRQ} instance.
     *
     * @param otaHotelDescriptiveContentNotifRQ Log this instance.
     * @return A {@link OTAHotelDescriptiveContentNotifRS} instance containing either a success
     * message (if no error happened), or an error.
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public OTAHotelDescriptiveContentNotifRS logInventoryBasic(OTAHotelDescriptiveContentNotifRQ otaHotelDescriptiveContentNotifRQ) {
        try {
            // Convert to JSON
            String json = om.writeValueAsString(otaHotelDescriptiveContentNotifRQ);

            // Execute business logic Log result / invoke
            LOG.info("----GOT Inventory/Basic REQUEST-----");
            LOG.info("{}", json);
            LOG.info("----END OF Inventory/Basic REQUEST-----");

            // Return success
            return buildSuccess();
        } catch (Exception e) {
            return buildError(e);
        }
    }

    /**
     * Log the given {@link OTAHotelDescriptiveContentNotifRQ} instance.
     *
     * @param otaHotelDescriptiveContentNotifRQ Log this instance.
     * @return A {@link OTAHotelDescriptiveContentNotifRS} instance containing either a success
     * message (if no error happened), or an error.
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    public OTAHotelDescriptiveContentNotifRS logInventoryHotelInfo(OTAHotelDescriptiveContentNotifRQ otaHotelDescriptiveContentNotifRQ) {
        try {
            // Convert to JSON
            String json = om.writeValueAsString(otaHotelDescriptiveContentNotifRQ);

            // Execute business logic Log result / invoke
            LOG.info("----GOT Inventory/HotelInfo REQUEST-----");
            LOG.info("{}", json);
            LOG.info("----END OF Inventory/HotelInfo REQUEST-----");

            // Return success
            return buildSuccess();
        } catch (Exception e) {
            return buildError(e);
        }
    }

    private OTAHotelDescriptiveContentNotifRS buildSuccess() {
        MessageAcknowledgementType mat = new MessageAcknowledgementType();
        mat.setSuccess(new SuccessType());
        mat.setVersion(BigDecimal.ONE);
        return new OTAHotelDescriptiveContentNotifRS(mat);
    }

    private OTAHotelDescriptiveContentNotifRS buildError(Exception e) {
        ErrorType errorType = new ErrorType();
        errorType.setValue(e.getMessage());
        errorType.setCode("450");
        errorType.setType("13");

        ErrorsType errorsType = new ErrorsType();
        errorsType.getErrors().add(errorType);
        MessageAcknowledgementType mat = new MessageAcknowledgementType();
        mat.setErrors(errorsType);
        mat.setVersion(BigDecimal.ONE);
        return new OTAHotelDescriptiveContentNotifRS(mat);
    }

}
