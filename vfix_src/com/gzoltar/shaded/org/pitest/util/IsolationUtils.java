package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.XStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.CompactWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.WeakHashMap;

public abstract class IsolationUtils {
   private static final XStream XSTREAM_INSTANCE = new XStream(new PitXmlDriver());
   private static final WeakHashMap<ClassLoader, XStream> CACHE = new WeakHashMap();
   private static final ClassLoaderDetectionStrategy LOADER_DETECTION_STRATEGY = new ClassLoaderDetectionStrategy() {
      public boolean fromDifferentLoader(Class<?> clazz, ClassLoader loader) {
         return IsolationUtils.fromIncompatibleLoader(clazz, loader);
      }
   };

   public static ClassLoaderDetectionStrategy loaderDetectionStrategy() {
      return LOADER_DETECTION_STRATEGY;
   }

   public static ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public static Object cloneForLoader(Object o, ClassLoader loader) {
      try {
         String xml = toXml(o);
         XStream foreignXstream = getXStreamForLoader(loader);
         return foreignXstream.fromXML(xml);
      } catch (Exception var4) {
         throw Unchecked.translateCheckedException(var4);
      }
   }

   private static XStream getXStreamForLoader(ClassLoader loader) {
      XStream foreginXstream = (XStream)CACHE.get(loader);
      if (foreginXstream == null) {
         foreginXstream = new XStream(new PitXmlDriver());
         foreginXstream.setClassLoader(loader);
         synchronized(CACHE) {
            CACHE.put(loader, foreginXstream);
         }
      }

      return foreginXstream;
   }

   private static boolean fromIncompatibleLoader(Class<?> clazz, ClassLoader loader) {
      ClassLoader target = clazz.getClassLoader();
      if (target != bootClassLoader() && loader != bootClassLoader()) {
         while(target != bootClassLoader()) {
            if (target == loader) {
               return false;
            }

            target = target.getParent();
         }

         return true;
      } else {
         return false;
      }
   }

   public static Class<?> convertForClassLoader(ClassLoader loader, String name) {
      try {
         return Class.forName(name, false, loader);
      } catch (ClassNotFoundException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public static Class<?> convertForClassLoader(ClassLoader loader, Class<?> clazz) {
      return clazz.getClassLoader() != loader ? convertForClassLoader(loader, clazz.getName()) : clazz;
   }

   public static String toXml(Object o) {
      Writer writer = new StringWriter();
      XSTREAM_INSTANCE.marshal(o, new CompactWriter(writer));
      return writer.toString();
   }

   public static Object fromXml(String xml) {
      return XSTREAM_INSTANCE.fromXML(xml);
   }

   public static Object clone(Object object) {
      return cloneForLoader(object, getContextClassLoader());
   }

   public static ClassLoader bootClassLoader() {
      return Object.class.getClassLoader();
   }
}
