// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.xml.xmladapter;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Test cases for {@link LocalDateAdapter} class.
 */
public class LocalDateAdapterTest {

    @Test
    public void testUnmarshal() {
        LocalDate localDate = LocalDate.now();
        LocalDateAdapter localDateAdapter = new LocalDateAdapter();
        LocalDate unmarshalledLocalDate = localDateAdapter.unmarshal(localDate.toString());
        assertEquals(unmarshalledLocalDate, localDate);

    }

    @Test
    public void testMarshal() {
        LocalDate localDate = LocalDate.now();
        LocalDateAdapter localDateAdapter = new LocalDateAdapter();
        String date = localDateAdapter.marshal(localDate);
        assertEquals(date, localDate.toString());
    }

    @Test
    public void testMarshal_LocalDateIsNull() {
        LocalDateAdapter localDateAdapter = new LocalDateAdapter();
        String date = localDateAdapter.marshal(null);
        assertNull(date);
    }
}