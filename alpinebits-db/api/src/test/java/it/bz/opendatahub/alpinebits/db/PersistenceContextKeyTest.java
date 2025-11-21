// SPDX-FileCopyrightText: NOI Techpark <digital@noi.bz.it>
//
// SPDX-License-Identifier: MPL-2.0

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package it.bz.opendatahub.alpinebits.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Test cases for {@link PersistenceContextKey} class.
 */
public class PersistenceContextKeyTest {

    @Test
    public void testContextKey_RequestId() {
        assertEquals(PersistenceContextKey.ENTITY_MANAGER_FACTORY.getType(), EntityManagerFactory.class);
    }

    @Test
    public void testContextKey_RequestUsername() {
        assertEquals(PersistenceContextKey.ENTITY_MANAGER.getType(), EntityManager.class);
    }

}