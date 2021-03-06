package org.activityinfo.client.dispatch.remote.cache;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.MockEventBus;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.DTOs;
import org.junit.Assert;
import org.junit.Test;

public class AdminEntityCacheTest {

    @Test
    public void testRootLevelCache() {

        CacheManager proxyMgr = new CacheManager(new MockEventBus());

        new AdminEntityCache(proxyMgr);

        proxyMgr.notifyListenersOfSuccess(new GetAdminEntities(1),
            DTOs.getProvinces());

        CacheResult<ListResult<AdminEntityDTO>> proxyResult = proxyMgr
            .execute(new GetAdminEntities(1));

        Assert.assertTrue(proxyResult.isCouldExecute());
        Assert.assertEquals(2, proxyResult.getResult().getData().size());
    }
}
