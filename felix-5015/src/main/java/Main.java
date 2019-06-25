import org.mockito.Mockito;

import org.osgi.resource.Capability;

import org.osgi.resource.Resource;
import java.lang.reflect.Method;

import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.Set;


import org.apache.felix.resolver.ResolverImpl;

public class Main {

    public void mytest() throws Exception {
        Capability cap = Mockito.mock(Capability.class);
        Set<Capability> set = ResolverImpl.getPackageSources(cap, new HashMap<Resource, ResolverImpl.Packages>());
        if (set.isEmpty()) {
            System.out.print("good");
        }
    }

    public void testPackageSources() throws Exception {
        Method m = ResolverImpl.class.getDeclaredMethod("getPackageSources",
                Capability.class, Map.class);
        m.setAccessible(true);

        Capability cap = Mockito.mock(Capability.class);
        if (Collections.emptySet() == m.invoke(null, cap, new HashMap<Resource, ResolverImpl.Packages>())) {
            System.out.println("good");
        }

        Capability cap2 = Mockito.mock(Capability.class);
        Resource res2 = Mockito.mock(Resource.class);
        Mockito.when(cap2.getResource()).thenReturn(res2);
        Map<Resource, ResolverImpl.Packages> map2 = new HashMap<Resource, ResolverImpl.Packages>();
        map2.put(res2, new ResolverImpl.Packages(res2));
        if(Collections.emptySet() == m.invoke(null, cap2, map2)) {
            System.out.println("good");
        }

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
        if(srcCaps3 == m.invoke(null, cap3, map3)) {
            System.out.println("good");
        }

    }
    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.mytest();
        run.testPackageSources();
    }
}