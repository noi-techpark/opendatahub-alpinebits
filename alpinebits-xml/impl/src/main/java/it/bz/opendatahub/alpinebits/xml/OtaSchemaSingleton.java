/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml;

import javax.xml.validation.Schema;

/**
 * This class provides a singleton instance of the OTA 2015a XML Schema.
 */
public final class OtaSchemaSingleton {

    private OtaSchemaSingleton() {
        // Empty
    }

    public static Schema getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final Schema INSTANCE = XmlValidationSchemaProvider.buildXsdSchema("ota2015a-min.xsd");
    }

}
