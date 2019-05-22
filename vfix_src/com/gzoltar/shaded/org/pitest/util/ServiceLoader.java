package com.gzoltar.shaded.org.pitest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

public abstract class ServiceLoader {
   public static <S> Collection<S> load(Class<S> ifc) {
      return load(ifc, Thread.currentThread().getContextClassLoader());
   }

   public static <S> Collection<S> load(Class<S> ifc, ClassLoader loader) {
      try {
         return loadImpl(ifc, loader);
      } catch (IOException var3) {
         throw new PitError("Error creating service " + ifc.getName(), var3);
      }
   }

   private static <S> Collection<S> loadImpl(Class<S> ifc, ClassLoader loader) throws IOException {
      Enumeration<URL> e = loader.getResources("META-INF/services/" + ifc.getName());
      ArrayList services = new ArrayList();

      while(e.hasMoreElements()) {
         URL url = (URL)e.nextElement();
         InputStream is = url.openStream();

         try {
            createServicesFromStream(ifc, loader, services, is);
         } finally {
            is.close();
         }
      }

      return services;
   }

   private static <S> void createServicesFromStream(Class<S> ifc, ClassLoader loader, Collection<S> services, InputStream is) throws IOException {
      BufferedReader r = new BufferedReader(new InputStreamReader(is, "UTF-8"));

      while(true) {
         String line = r.readLine();
         if (line == null) {
            return;
         }

         int comment = line.indexOf(35);
         if (comment >= 0) {
            line = line.substring(0, comment);
         }

         String name = line.trim();
         if (name.length() != 0) {
            services.add(createService(name, ifc, loader));
         }
      }
   }

   private static <S> S createService(String name, Class<S> ifc, ClassLoader loader) {
      try {
         Class<?> clz = Class.forName(name, true, loader);
         Class<? extends S> impl = clz.asSubclass(ifc);
         Constructor<? extends S> ctor = impl.getConstructor();
         return ctor.newInstance();
      } catch (Exception var6) {
         throw new PitError("Error creating service " + ifc.getName(), var6);
      }
   }
}
