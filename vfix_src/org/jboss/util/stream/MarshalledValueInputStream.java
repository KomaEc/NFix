package org.jboss.util.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MarshalledValueInputStream extends ObjectInputStream {
   private static final Map<String, Class> primClasses = new HashMap(8, 1.0F);

   public MarshalledValueInputStream(InputStream is) throws IOException {
      super(is);
   }

   protected Class<?> resolveClass(ObjectStreamClass v) throws IOException, ClassNotFoundException {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      String className = v.getName();

      try {
         return Class.forName(className, false, loader);
      } catch (ClassNotFoundException var6) {
         Class cl = (Class)primClasses.get(className);
         if (cl == null) {
            throw var6;
         } else {
            return cl;
         }
      }
   }

   protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Class<?>[] ifaceClasses = new Class[interfaces.length];

      for(int i = 0; i < interfaces.length; ++i) {
         ifaceClasses[i] = loader.loadClass(interfaces[i]);
      }

      return Proxy.getProxyClass(loader, ifaceClasses);
   }

   static {
      primClasses.put("boolean", Boolean.TYPE);
      primClasses.put("byte", Byte.TYPE);
      primClasses.put("char", Character.TYPE);
      primClasses.put("short", Short.TYPE);
      primClasses.put("int", Integer.TYPE);
      primClasses.put("long", Long.TYPE);
      primClasses.put("float", Float.TYPE);
      primClasses.put("double", Double.TYPE);
      primClasses.put("void", Void.TYPE);
   }
}
