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
package org.apache.felix.resolver.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;


import org.apache.felix.resolver.ResolverImpl;

import org.junit.Test;
import org.mockito.Mockito;

import org.osgi.resource.Capability;

import org.osgi.resource.Resource;


public class ResolverTest
{

    @Test
    public void testPackageSources() throws Exception {
        Method m = ResolverImpl.class.getDeclaredMethod("getPackageSources",
                Capability.class, Map.class);
        m.setAccessible(true);

        Capability cap = Mockito.mock(Capability.class);
        assertEquals(Collections.emptySet(),
                m.invoke(null, cap, new HashMap<Resource, ResolverImpl.Packages>()));

        Capability cap2 = Mockito.mock(Capability.class);
        Resource res2 = Mockito.mock(Resource.class);
        Mockito.when(cap2.getResource()).thenReturn(res2);
        Map<Resource, ResolverImpl.Packages> map2 = new HashMap<Resource, ResolverImpl.Packages>();
        map2.put(res2, new ResolverImpl.Packages(res2));
        assertEquals(Collections.emptySet(), m.invoke(null, cap2, map2));

        Capability cap3 = Mockito.mock(Capability.class);
        Resource res3 = Mockito.mock(Resource.class);
        Mockito.when(cap3.getResource()).thenReturn(res3);
        Map<Resource, ResolverImpl.Packages> map3 = new HashMap<Resource, ResolverImpl.Packages>();
        ResolverImpl.Packages pkgs3 = new ResolverImpl.Packages(res3);
        Set<Capability> srcCaps3 = Collections.singleton(Mockito.mock(Capability.class));
        Map<Capability, Set<Capability>> srcMap3 = Collections.singletonMap(
                cap3, srcCaps3);
        pkgs3.m_sources.putAll(srcMap3);
        map3.put(res3, pkgs3);
        assertEquals(srcCaps3, m.invoke(null, cap3, map3));

    }


}
