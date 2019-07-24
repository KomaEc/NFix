/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.felix.scr.impl.runtime;

import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mockito.Mockito;
import org.osgi.dto.DTO;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.dto.BundleDTO;
import org.osgi.framework.dto.ServiceReferenceDTO;
import org.junit.Test;

public class ServiceComponentRuntimeImplTest extends TestCase
{

    @Test
    public void testNullBundleServiceReferenceDTO() throws Exception
    {
        ServiceReference<?> sr = Mockito.mock(ServiceReference.class);
        Mockito.when(sr.getProperty(Constants.SERVICE_ID)).thenReturn(327L);
        Mockito.when(sr.getPropertyKeys()).thenReturn(new String[] {});

        ServiceComponentRuntimeImpl scr = new ServiceComponentRuntimeImpl(null, null);
        Method m = scr.getClass().getDeclaredMethod("serviceReferenceToDTO", ServiceReference.class);
        m.setAccessible(true);
        ServiceReferenceDTO dto = (ServiceReferenceDTO) m.invoke(scr, sr);
        assertEquals(-1, dto.bundle);
    }

    private void equalsToString(Object o, ServiceComponentRuntimeImpl scr)
    {
        assertEquals(String.valueOf(o), scr.convert(o));
    }

    private void same(Object o, ServiceComponentRuntimeImpl scr)
    {
        assertSame(o, scr.convert(o));
    }

}