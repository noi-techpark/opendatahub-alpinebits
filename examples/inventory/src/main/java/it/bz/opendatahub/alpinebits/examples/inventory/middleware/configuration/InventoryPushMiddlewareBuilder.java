// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.examples.inventory.middleware.configuration;

import it.bz.opendatahub.alpinebits.common.utils.middleware.ComposingMiddlewareBuilder;
import it.bz.opendatahub.alpinebits.examples.inventory.middleware.InventoryPushMiddleware;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import jakarta.xml.bind.JAXBException;

import java.util.Arrays;

/**
 * Utility class to build a {@link InventoryPushMiddleware}.
 */
public final class InventoryPushMiddlewareBuilder {

    private InventoryPushMiddlewareBuilder() {
        // Empty
    }

    public static Middleware buildInventoryPushMiddleware() throws JAXBException {
        return ComposingMiddlewareBuilder.compose(Arrays.asList(
                XmlMiddlewareBuilder.buildXmlToObjectConvertingMiddleware(InventoryPushMiddleware.OTA_INVENTORY_PUSH_REQUEST),
                XmlMiddlewareBuilder.buildObjectToXmlConvertingMiddleware(InventoryPushMiddleware.OTA_INVENTORY_PUSH_RESPONSE),
                new InventoryPushMiddleware()
        ));
    }
}
