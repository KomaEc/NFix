package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.FieldDictionary;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.PureJavaReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ReflectionProvider;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.CustomObjectOutputStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.DependencyInjectionFactory;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.PresortedMap;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.PresortedSet;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.AttributedString;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class JVM implements Caching {
   private ReflectionProvider reflectionProvider;
   private static final boolean isAWTAvailable;
   private static final boolean isSwingAvailable;
   private static final boolean isSQLAvailable;
   private static final boolean canAllocateWithUnsafe;
   private static final boolean canWriteWithUnsafe;
   private static final boolean optimizedTreeSetAddAll;
   private static final boolean optimizedTreeMapPutAll;
   private static final boolean canParseUTCDateFormat;
   private static final boolean canParseISO8601TimeZoneInDateFormat;
   private static final boolean canCreateDerivedObjectOutputStream;
   private static final String vendor = System.getProperty("java.vm.vendor");
   private static final float majorJavaVersion = getMajorJavaVersion();
   private static final float DEFAULT_JAVA_VERSION = 1.4F;
   private static final boolean reverseFieldOrder = false;
   private static final Class reflectionProviderType;

   private static final float getMajorJavaVersion() {
      try {
         return isAndroid() ? 1.5F : Float.parseFloat(System.getProperty("java.specification.version"));
      } catch (NumberFormatException var1) {
         return 1.4F;
      }
   }

   /** @deprecated */
   public static boolean is14() {
      return majorJavaVersion >= 1.4F;
   }

   /** @deprecated */
   public static boolean is15() {
      return majorJavaVersion >= 1.5F;
   }

   /** @deprecated */
   public static boolean is16() {
      return majorJavaVersion >= 1.6F;
   }

   public static boolean is17() {
      return majorJavaVersion >= 1.7F;
   }

   public static boolean is18() {
      return majorJavaVersion >= 1.8F;
   }

   public static boolean is19() {
      return majorJavaVersion >= 1.9F;
   }

   private static boolean isIBM() {
      return vendor.indexOf("IBM") != -1;
   }

   private static boolean isAndroid() {
      return vendor.indexOf("Android") != -1;
   }

   public static Class loadClassForName(String name) {
      return loadClassForName(name, true);
   }

   /** @deprecated */
   public Class loadClass(String name) {
      return loadClassForName(name, true);
   }

   public static Class loadClassForName(String name, boolean initialize) {
      try {
         Class clazz = Class.forName(name, initialize, JVM.class.getClassLoader());
         return clazz;
      } catch (LinkageError var3) {
         return null;
      } catch (ClassNotFoundException var4) {
         return null;
      }
   }

   /** @deprecated */
   public Class loadClass(String name, boolean initialize) {
      return loadClassForName(name, initialize);
   }

   public static ReflectionProvider newReflectionProvider() {
      return (ReflectionProvider)DependencyInjectionFactory.newInstance(reflectionProviderType, (Object[])null);
   }

   public static ReflectionProvider newReflectionProvider(FieldDictionary dictionary) {
      return (ReflectionProvider)DependencyInjectionFactory.newInstance(reflectionProviderType, new Object[]{dictionary});
   }

   public static Class getStaxInputFactory() throws ClassNotFoundException {
      if (is16()) {
         return isIBM() ? Class.forName("com.ibm.xml.xlxp.api.stax.XMLInputFactoryImpl") : Class.forName("com.sun.xml.internal.stream.XMLInputFactoryImpl");
      } else {
         return null;
      }
   }

   public static Class getStaxOutputFactory() throws ClassNotFoundException {
      if (is16()) {
         return isIBM() ? Class.forName("com.ibm.xml.xlxp.api.stax.XMLOutputFactoryImpl") : Class.forName("com.sun.xml.internal.stream.XMLOutputFactoryImpl");
      } else {
         return null;
      }
   }

   /** @deprecated */
   public synchronized ReflectionProvider bestReflectionProvider() {
      if (this.reflectionProvider == null) {
         this.reflectionProvider = newReflectionProvider();
      }

      return this.reflectionProvider;
   }

   private static boolean canUseSunUnsafeReflectionProvider() {
      return canAllocateWithUnsafe && is14();
   }

   private static boolean canUseSunLimitedUnsafeReflectionProvider() {
      return canWriteWithUnsafe;
   }

   /** @deprecated */
   public static boolean reverseFieldDefinition() {
      return false;
   }

   public static boolean isAWTAvailable() {
      return isAWTAvailable;
   }

   /** @deprecated */
   public boolean supportsAWT() {
      return isAWTAvailable;
   }

   public static boolean isSwingAvailable() {
      return isSwingAvailable;
   }

   /** @deprecated */
   public boolean supportsSwing() {
      return isSwingAvailable;
   }

   public static boolean isSQLAvailable() {
      return isSQLAvailable;
   }

   /** @deprecated */
   public boolean supportsSQL() {
      return isSQLAvailable;
   }

   public static boolean hasOptimizedTreeSetAddAll() {
      return optimizedTreeSetAddAll;
   }

   public static boolean hasOptimizedTreeMapPutAll() {
      return optimizedTreeMapPutAll;
   }

   public static boolean canParseUTCDateFormat() {
      return canParseUTCDateFormat;
   }

   public static boolean canParseISO8601TimeZoneInDateFormat() {
      return canParseISO8601TimeZoneInDateFormat;
   }

   public static boolean canCreateDerivedObjectOutputStream() {
      return canCreateDerivedObjectOutputStream;
   }

   /** @deprecated */
   public void flushCache() {
   }

   public static void main(String[] args) {
      boolean reverseJDK = false;
      Field[] fields = AttributedString.class.getDeclaredFields();

      for(int i = 0; i < fields.length; ++i) {
         if (fields[i].getName().equals("text")) {
            reverseJDK = i > 3;
            break;
         }
      }

      boolean reverseLocal = false;
      fields = JVM.Test.class.getDeclaredFields();

      for(int i = 0; i < fields.length; ++i) {
         if (fields[i].getName().equals("o")) {
            reverseLocal = i > 3;
            break;
         }
      }

      String staxInputFactory = null;

      try {
         staxInputFactory = getStaxInputFactory().getName();
      } catch (ClassNotFoundException var9) {
         staxInputFactory = var9.getMessage();
      } catch (NullPointerException var10) {
      }

      String staxOutputFactory = null;

      try {
         staxOutputFactory = getStaxOutputFactory().getName();
      } catch (ClassNotFoundException var7) {
         staxOutputFactory = var7.getMessage();
      } catch (NullPointerException var8) {
      }

      System.out.println("XStream JVM diagnostics");
      System.out.println("java.specification.version: " + System.getProperty("java.specification.version"));
      System.out.println("java.specification.vendor: " + System.getProperty("java.specification.vendor"));
      System.out.println("java.specification.name: " + System.getProperty("java.specification.name"));
      System.out.println("java.vm.vendor: " + vendor);
      System.out.println("java.vendor: " + System.getProperty("java.vendor"));
      System.out.println("java.vm.name: " + System.getProperty("java.vm.name"));
      System.out.println("Version: " + majorJavaVersion);
      System.out.println("XStream support for enhanced Mode: " + canUseSunUnsafeReflectionProvider());
      System.out.println("XStream support for reduced Mode: " + canUseSunLimitedUnsafeReflectionProvider());
      System.out.println("Supports AWT: " + isAWTAvailable());
      System.out.println("Supports Swing: " + isSwingAvailable());
      System.out.println("Supports SQL: " + isSQLAvailable());
      System.out.println("Java Beans EventHandler present: " + (loadClassForName("java.beans.EventHandler") != null));
      System.out.println("Standard StAX XMLInputFactory: " + staxInputFactory);
      System.out.println("Standard StAX XMLOutputFactory: " + staxOutputFactory);
      System.out.println("Optimized TreeSet.addAll: " + hasOptimizedTreeSetAddAll());
      System.out.println("Optimized TreeMap.putAll: " + hasOptimizedTreeMapPutAll());
      System.out.println("Can parse UTC date format: " + canParseUTCDateFormat());
      System.out.println("Can create derive ObjectOutputStream: " + canCreateDerivedObjectOutputStream());
      System.out.println("Reverse field order detected for JDK: " + reverseJDK);
      System.out.println("Reverse field order detected (only if JVM class itself has been compiled): " + reverseLocal);
   }

   static {
      boolean test = true;
      Object unsafe = null;

      Class type;
      try {
         type = Class.forName("sun.misc.Unsafe");
         Field unsafeField = type.getDeclaredField("theUnsafe");
         unsafeField.setAccessible(true);
         unsafe = unsafeField.get((Object)null);
         Method allocateInstance = type.getDeclaredMethod("allocateInstance", Class.class);
         allocateInstance.setAccessible(true);
         test = allocateInstance.invoke(unsafe, JVM.Test.class) != null;
      } catch (Exception var17) {
         test = false;
      } catch (Error var18) {
         test = false;
      }

      canAllocateWithUnsafe = test;
      test = false;
      type = PureJavaReflectionProvider.class;
      if (canUseSunUnsafeReflectionProvider()) {
         Class cls = loadClassForName("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.SunUnsafeReflectionProvider");
         if (cls != null) {
            try {
               ReflectionProvider provider = (ReflectionProvider)DependencyInjectionFactory.newInstance(cls, (Object[])null);
               JVM.Test t = (JVM.Test)provider.newInstance(JVM.Test.class);

               try {
                  provider.writeField(t, "o", "object", JVM.Test.class);
                  provider.writeField(t, "c", new Character('c'), JVM.Test.class);
                  provider.writeField(t, "b", new Byte((byte)1), JVM.Test.class);
                  provider.writeField(t, "s", new Short((short)1), JVM.Test.class);
                  provider.writeField(t, "i", new Integer(1), JVM.Test.class);
                  provider.writeField(t, "l", new Long(1L), JVM.Test.class);
                  provider.writeField(t, "f", new Float(1.0F), JVM.Test.class);
                  provider.writeField(t, "d", new Double(1.0D), JVM.Test.class);
                  provider.writeField(t, "bool", Boolean.TRUE, JVM.Test.class);
                  test = true;
               } catch (IncompatibleClassChangeError var14) {
                  cls = null;
               } catch (ObjectAccessException var15) {
                  cls = null;
               }

               if (cls == null) {
                  cls = loadClassForName("com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.SunLimitedUnsafeReflectionProvider");
               }

               type = cls;
            } catch (ObjectAccessException var16) {
            }
         }
      }

      reflectionProviderType = type;
      canWriteWithUnsafe = test;
      Comparator comparator = new Comparator() {
         public int compare(Object o1, Object o2) {
            throw new RuntimeException();
         }
      };
      SortedMap map = new PresortedMap(comparator);
      map.put("one", (Object)null);
      map.put("two", (Object)null);

      try {
         (new TreeMap(comparator)).putAll(map);
         test = true;
      } catch (RuntimeException var13) {
         test = false;
      }

      optimizedTreeMapPutAll = test;
      SortedSet set = new PresortedSet(comparator);
      set.addAll(map.keySet());

      try {
         (new TreeSet(comparator)).addAll(set);
         test = true;
      } catch (RuntimeException var12) {
         test = false;
      }

      optimizedTreeSetAddAll = test;

      try {
         (new SimpleDateFormat("z")).parse("UTC");
         test = true;
      } catch (ParseException var11) {
         test = false;
      }

      canParseUTCDateFormat = test;

      try {
         (new SimpleDateFormat("X")).parse("Z");
         test = true;
      } catch (ParseException var9) {
         test = false;
      } catch (IllegalArgumentException var10) {
         test = false;
      }

      canParseISO8601TimeZoneInDateFormat = test;

      try {
         test = new CustomObjectOutputStream((CustomObjectOutputStream.StreamCallback)null) != null;
      } catch (RuntimeException var7) {
         test = false;
      } catch (IOException var8) {
         test = false;
      }

      canCreateDerivedObjectOutputStream = test;
      isAWTAvailable = loadClassForName("java.awt.Color", false) != null;
      isSwingAvailable = loadClassForName("javax.swing.LookAndFeel", false) != null;
      isSQLAvailable = loadClassForName("java.sql.Date") != null;
   }

   static class Test {
      private Object o;
      private char c;
      private byte b;
      private short s;
      private int i;
      private long l;
      private float f;
      private double d;
      private boolean bool;

      Test() {
         throw new UnsupportedOperationException();
      }
   }
}
