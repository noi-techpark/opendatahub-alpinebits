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
import it.bz.opendatahub.alpinebits.examples.inventory.middleware.InventoryPullMiddleware;
import it.bz.opendatahub.alpinebits.middleware.Middleware;
import jakarta.xml.bind.JAXBException;

import java.util.Arrays;

/**
 * Utility class to build a {@link InventoryPullMiddleware}.
 */
public final class InventoryPullMiddlewareBuilder {

    private InventoryPullMiddlewareBuilder() {
        // Empty
    }

    public static Middleware buildInventoryPullMiddleware() throws JAXBException {
        return ComposingMiddlewareBuilder.compose(Arrays.asList(
                XmlMiddlewareBuilder.buildXmlToObjectConvertingMiddleware(InventoryPullMiddleware.OTA_INVENTORY_PULL_REQUEST),
                XmlMiddlewareBuilder.buildObjectToXmlConvertingMiddleware(InventoryPullMiddleware.OTA_INVENTORY_PULL_RESPONSE),
                new InventoryPullMiddleware()
        ));
    }
}
