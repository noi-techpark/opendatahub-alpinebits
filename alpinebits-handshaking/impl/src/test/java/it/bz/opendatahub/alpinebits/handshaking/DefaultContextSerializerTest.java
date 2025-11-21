// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.handshaking;

import it.bz.opendatahub.alpinebits.common.constants.AlpineBitsVersion;
import it.bz.opendatahub.alpinebits.common.context.RequestContextKey;
import it.bz.opendatahub.alpinebits.common.context.ResponseContextKeys;
import it.bz.opendatahub.alpinebits.middleware.Context;
import it.bz.opendatahub.alpinebits.middleware.RequiredContextKeyMissingException;
import it.bz.opendatahub.alpinebits.middleware.impl.SimpleContext;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRQ;
import it.bz.opendatahub.alpinebits.xml.schema.ota.OTAPingRS;
import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * Test cases for {@link DefaultContextSerializer} class.
 */
public class DefaultContextSerializerTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFromContext_ShouldThrowIfContextIsNull() {
        new DefaultContextSerializer(AlpineBitsVersion.V_2018_10).fromContext(null);
    }

    @Test(expectedExceptions = RequiredContextKeyMissingException.class)
    public void testFromContext_ShouldThrowIfContextIsEmpty() {
        new DefaultContextSerializer(AlpineBitsVersion.V_2018_10).fromContext(new SimpleContext());
    }

    @Test
    public void testFromContext_ShouldReturnValidData() {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("Handshake-OTA_PingRQ.xml");
        Context ctx = new SimpleContext();
        ctx.put(RequestContextKey.REQUEST_CONTENT_STREAM, is);

        OTAPingRQ otaPingRQ = new DefaultContextSerializer(AlpineBitsVersion.V_2018_10).fromContext(ctx);
        assertNotNull(otaPingRQ);
        assertNotNull(otaPingRQ.getEchoData());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testToContext_ShouldThrowIfContextIsNull() {
        new DefaultContextSerializer(AlpineBitsVersion.V_2018_10).toContext(null, null);
    }

    @Test
    public void testToContext_ShouldPutDataIntoContext() {
        Context ctx = new SimpleContext();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        ctx.put(ResponseContextKeys.RESPONSE_CONTENT_STREAM, responseStream);

        OTAPingRS otaPingRS = OTAPingRSBuilder.build("a", "");

        new DefaultContextSerializer(AlpineBitsVersion.V_2018_10).toContext(ctx, otaPingRS);

        String result = responseStream.toString(StandardCharsets.UTF_8);

        assertFalse(result.isEmpty());
    }
}