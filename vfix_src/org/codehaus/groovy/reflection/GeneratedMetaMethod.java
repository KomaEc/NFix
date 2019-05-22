package org.codehaus.groovy.reflection;

import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaMethod;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class GeneratedMetaMethod extends MetaMethod {
   private final String name;
   private final CachedClass declaringClass;
   private final Class returnType;

   public GeneratedMetaMethod(String name, CachedClass declaringClass, Class returnType, Class[] parameters) {
      this.name = name;
      this.declaringClass = declaringClass;
      this.returnType = returnType;
      this.nativeParamTypes = parameters;
   }

   public int getModifiers() {
      return 1;
   }

   public String getName() {
      return this.name;
   }

   public Class getReturnType() {
      return this.returnType;
   }

   public CachedClass getDeclaringClass() {
      return this.declaringClass;
   }

   public static class DgmMethodRecord implements Serializable {
      public String className;
      public String methodName;
      public Class returnType;
      public Class[] parameters;
      private static final Class[] PRIMITIVE_CLASSES;

      public static void saveDgmInfo(List<GeneratedMetaMethod.DgmMethodRecord> records, String file) throws IOException {
         DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
         Map<String, Integer> classes = new LinkedHashMap();
         int nextClassId = 0;

         for(int i = 0; i < PRIMITIVE_CLASSES.length; ++i) {
            classes.put(PRIMITIVE_CLASSES[i].getName(), nextClassId++);
         }

         Iterator i$ = records.iterator();

         GeneratedMetaMethod.DgmMethodRecord record;
         Integer key;
         while(i$.hasNext()) {
            record = (GeneratedMetaMethod.DgmMethodRecord)i$.next();
            String name = record.returnType.getName();
            key = (Integer)classes.get(name);
            if (key == null) {
               key = nextClassId++;
               classes.put(name, key);
            }

            for(int i = 0; i < record.parameters.length; ++i) {
               name = record.parameters[i].getName();
               key = (Integer)classes.get(name);
               if (key == null) {
                  key = nextClassId++;
                  classes.put(name, key);
               }
            }
         }

         i$ = classes.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, Integer> stringIntegerEntry = (Entry)i$.next();
            out.writeUTF((String)stringIntegerEntry.getKey());
            out.writeInt((Integer)stringIntegerEntry.getValue());
         }

         out.writeUTF("");
         out.writeInt(records.size());
         i$ = records.iterator();

         while(i$.hasNext()) {
            record = (GeneratedMetaMethod.DgmMethodRecord)i$.next();
            out.writeUTF(record.className);
            out.writeUTF(record.methodName);
            out.writeInt((Integer)classes.get(record.returnType.getName()));
            out.writeInt(record.parameters.length);

            for(int i = 0; i < record.parameters.length; ++i) {
               key = (Integer)classes.get(record.parameters[i].getName());
               out.writeInt(key);
            }
         }

         out.close();
      }

      public static List<GeneratedMetaMethod.DgmMethodRecord> loadDgmInfo() throws IOException, ClassNotFoundException {
         ClassLoader loader = GeneratedMetaMethod.DgmMethodRecord.class.getClassLoader();
         DataInputStream in = new DataInputStream(new BufferedInputStream(loader.getResourceAsStream("META-INF/dgminfo")));
         Map<Integer, Class> classes = new HashMap();

         int i;
         for(i = 0; i < PRIMITIVE_CLASSES.length; ++i) {
            classes.put(i, PRIMITIVE_CLASSES[i]);
         }

         i = 0;

         while(true) {
            String name;
            int key;
            do {
               name = in.readUTF();
               if (name.length() == 0) {
                  int size = in.readInt();
                  List<GeneratedMetaMethod.DgmMethodRecord> res = new ArrayList(size);

                  for(int i = 0; i != size; ++i) {
                     boolean skipRecord = false;
                     GeneratedMetaMethod.DgmMethodRecord record = new GeneratedMetaMethod.DgmMethodRecord();
                     record.className = in.readUTF();
                     record.methodName = in.readUTF();
                     record.returnType = (Class)classes.get(in.readInt());
                     if (record.returnType == null) {
                        skipRecord = true;
                     }

                     int psize = in.readInt();
                     record.parameters = new Class[psize];

                     for(int j = 0; j < record.parameters.length; ++j) {
                        record.parameters[j] = (Class)classes.get(in.readInt());
                        if (record.parameters[j] == null) {
                           skipRecord = true;
                        }
                     }

                     if (!skipRecord) {
                        res.add(record);
                     }
                  }

                  in.close();
                  return res;
               }

               key = in.readInt();
            } while(i++ < PRIMITIVE_CLASSES.length);

            Class cls = null;

            try {
               cls = loader.loadClass(name);
            } catch (ClassNotFoundException var11) {
               continue;
            }

            classes.put(key, cls);
         }
      }

      static {
         PRIMITIVE_CLASSES = new Class[]{Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Double.TYPE, Float.TYPE, Void.TYPE, boolean[].class, char[].class, byte[].class, short[].class, int[].class, long[].class, double[].class, float[].class, Object[].class, String[].class, Class[].class, Byte[].class};
      }
   }

   public static class Proxy extends GeneratedMetaMethod {
      private volatile MetaMethod proxy;
      private final String className;

      public Proxy(String className, String name, CachedClass declaringClass, Class returnType, Class[] parameters) {
         super(name, declaringClass, returnType, parameters);
         this.className = className;
      }

      public boolean isValidMethod(Class[] arguments) {
         return this.proxy().isValidMethod(arguments);
      }

      public Object doMethodInvoke(Object object, Object[] argumentArray) {
         return this.proxy().doMethodInvoke(object, argumentArray);
      }

      public Object invoke(Object object, Object[] arguments) {
         return this.proxy().invoke(object, arguments);
      }

      public final synchronized MetaMethod proxy() {
         if (this.proxy == null) {
            this.createProxy();
         }

         return this.proxy;
      }

      private void createProxy() {
         try {
            Class<?> aClass = this.getClass().getClassLoader().loadClass(this.className.replace('/', '.'));
            Constructor<?> constructor = aClass.getConstructor(String.class, CachedClass.class, Class.class, Class[].class);
            this.proxy = (MetaMethod)constructor.newInstance(this.getName(), this.getDeclaringClass(), this.getReturnType(), this.getNativeParameterTypes());
         } catch (Throwable var3) {
            var3.printStackTrace();
            throw new GroovyRuntimeException("Failed to create DGM method proxy : " + var3, var3);
         }
      }
   }
}
