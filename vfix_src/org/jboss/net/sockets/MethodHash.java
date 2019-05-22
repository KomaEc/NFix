package org.jboss.net.sockets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class MethodHash {
   static Map hashMap = new WeakHashMap();

   public static Map getInterfaceHashes(Class intf) {
      Method[] methods = intf.getMethods();
      HashMap map = new HashMap();

      for(int i = 0; i < methods.length; ++i) {
         Method method = methods[i];
         Class[] parameterTypes = method.getParameterTypes();
         String methodDesc = method.getName() + "(";

         for(int j = 0; j < parameterTypes.length; ++j) {
            methodDesc = methodDesc + getTypeString(parameterTypes[j]);
         }

         methodDesc = methodDesc + ")" + getTypeString(method.getReturnType());

         try {
            long hash = 0L;
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(512);
            MessageDigest messagedigest = MessageDigest.getInstance("SHA");
            DataOutputStream dataoutputstream = new DataOutputStream(new DigestOutputStream(bytearrayoutputstream, messagedigest));
            dataoutputstream.writeUTF(methodDesc);
            dataoutputstream.flush();
            byte[] abyte0 = messagedigest.digest();

            for(int j = 0; j < Math.min(8, abyte0.length); ++j) {
               hash += (long)(abyte0[j] & 255) << j * 8;
            }

            map.put(method.toString(), new Long(hash));
         } catch (Exception var14) {
            var14.printStackTrace();
         }
      }

      return map;
   }

   static String getTypeString(Class cl) {
      if (cl == Byte.TYPE) {
         return "B";
      } else if (cl == Character.TYPE) {
         return "C";
      } else if (cl == Double.TYPE) {
         return "D";
      } else if (cl == Float.TYPE) {
         return "F";
      } else if (cl == Integer.TYPE) {
         return "I";
      } else if (cl == Long.TYPE) {
         return "J";
      } else if (cl == Short.TYPE) {
         return "S";
      } else if (cl == Boolean.TYPE) {
         return "Z";
      } else if (cl == Void.TYPE) {
         return "V";
      } else {
         return cl.isArray() ? "[" + getTypeString(cl.getComponentType()) : "L" + cl.getName().replace('.', '/') + ";";
      }
   }

   public static long calculateHash(Method method) {
      Map methodHashes = (Map)hashMap.get(method.getDeclaringClass());
      if (methodHashes == null) {
         methodHashes = getInterfaceHashes(method.getDeclaringClass());
         WeakHashMap newHashMap = new WeakHashMap();
         newHashMap.putAll(hashMap);
         newHashMap.put(method.getDeclaringClass(), methodHashes);
         hashMap = newHashMap;
      }

      return (Long)methodHashes.get(method.toString());
   }
}
