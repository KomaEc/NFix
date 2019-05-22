package org.apache.tools.ant.filters;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.tools.ant.BuildException;

public final class ClassConstants extends BaseFilterReader implements ChainableReader {
   private String queuedData = null;
   private static final String JAVA_CLASS_HELPER = "org.apache.tools.ant.filters.util.JavaClassHelper";
   // $FF: synthetic field
   static Class array$B;

   public ClassConstants() {
   }

   public ClassConstants(Reader in) {
      super(in);
   }

   public int read() throws IOException {
      int ch = -1;
      if (this.queuedData != null && this.queuedData.length() == 0) {
         this.queuedData = null;
      }

      if (this.queuedData != null) {
         ch = this.queuedData.charAt(0);
         this.queuedData = this.queuedData.substring(1);
         if (this.queuedData.length() == 0) {
            this.queuedData = null;
         }
      } else {
         String clazz = this.readFully();
         if (clazz == null) {
            ch = -1;
         } else {
            byte[] bytes = clazz.getBytes("ISO-8859-1");

            try {
               Class javaClassHelper = Class.forName("org.apache.tools.ant.filters.util.JavaClassHelper");
               if (javaClassHelper != null) {
                  Class[] params = new Class[]{array$B == null ? (array$B = class$("[B")) : array$B};
                  Method getConstants = javaClassHelper.getMethod("getConstants", params);
                  Object[] args = new Object[]{bytes};
                  StringBuffer sb = (StringBuffer)getConstants.invoke((Object)null, args);
                  if (sb.length() > 0) {
                     this.queuedData = sb.toString();
                     return this.read();
                  }
               }
            } catch (NoClassDefFoundError var9) {
               throw var9;
            } catch (RuntimeException var10) {
               throw var10;
            } catch (InvocationTargetException var11) {
               Throwable t = var11.getTargetException();
               if (t instanceof NoClassDefFoundError) {
                  throw (NoClassDefFoundError)t;
               }

               if (t instanceof RuntimeException) {
                  throw (RuntimeException)t;
               }

               throw new BuildException(t);
            } catch (Exception var12) {
               throw new BuildException(var12);
            }
         }
      }

      return ch;
   }

   public Reader chain(Reader rdr) {
      ClassConstants newFilter = new ClassConstants(rdr);
      return newFilter;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
