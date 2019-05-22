package javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import javassist.CannotCompileException;
import javassist.bytecode.ClassFile;

public class DefinePackageHelper {
   private static final DefinePackageHelper.SecuredPrivileged privileged;

   public static void definePackage(String className, ClassLoader loader) throws CannotCompileException {
      try {
         privileged.definePackage(loader, className, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
      } catch (IllegalArgumentException var3) {
      } catch (Exception var4) {
         throw new CannotCompileException(var4);
      }
   }

   private DefinePackageHelper() {
   }

   static {
      privileged = ClassFile.MAJOR_VERSION >= 53 ? DefinePackageHelper.SecuredPrivileged.JAVA_9 : (ClassFile.MAJOR_VERSION >= 51 ? DefinePackageHelper.SecuredPrivileged.JAVA_7 : DefinePackageHelper.SecuredPrivileged.JAVA_OTHER);
   }

   private static enum SecuredPrivileged {
      JAVA_9 {
         protected Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
            throw new RuntimeException("define package has been disabled for jigsaw");
         }
      },
      JAVA_7 {
         private final SecurityActions stack;
         private final MethodHandle definePackage;

         {
            this.stack = SecurityActions.stack;
            this.definePackage = this.getDefinePackageMethodHandle();
         }

         private MethodHandle getDefinePackageMethodHandle() {
            if (this.stack.getCallerClass() != this.getClass()) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  return SecurityActions.getMethodHandle(ClassLoader.class, "definePackage", new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class});
               } catch (NoSuchMethodException var2) {
                  throw new RuntimeException("cannot initialize", var2);
               }
            }
         }

         protected Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
            if (this.stack.getCallerClass() != DefinePackageHelper.class) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  return (Package)this.definePackage.invokeWithArguments(loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
               } catch (Throwable var11) {
                  if (var11 instanceof IllegalArgumentException) {
                     throw (IllegalArgumentException)var11;
                  } else if (var11 instanceof RuntimeException) {
                     throw (RuntimeException)var11;
                  } else {
                     return null;
                  }
               }
            }
         }
      },
      JAVA_OTHER {
         private final SecurityActions stack;
         private final Method definePackage;

         {
            this.stack = SecurityActions.stack;
            this.definePackage = this.getDefinePackageMethod();
         }

         private Method getDefinePackageMethod() {
            if (this.stack.getCallerClass() != this.getClass()) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  return SecurityActions.getDeclaredMethod(ClassLoader.class, "definePackage", new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class});
               } catch (NoSuchMethodException var2) {
                  throw new RuntimeException("cannot initialize", var2);
               }
            }
         }

         protected Package definePackage(ClassLoader loader, String name, String specTitle, String specVersion, String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase) throws IllegalArgumentException {
            if (this.stack.getCallerClass() != DefinePackageHelper.class) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  try {
                     this.definePackage.setAccessible(true);
                     Package var10 = (Package)this.definePackage.invoke(loader, name, specTitle, specVersion, specVendor, implTitle, implVersion, implVendor, sealBase);
                     return var10;
                  } catch (Throwable var15) {
                     if (var15 instanceof InvocationTargetException) {
                        Throwable t = ((InvocationTargetException)var15).getTargetException();
                        if (t instanceof IllegalArgumentException) {
                           throw (IllegalArgumentException)t;
                        }
                     }
                  }

                  if (var15 instanceof RuntimeException) {
                     throw (RuntimeException)var15;
                  }
               } finally {
                  this.definePackage.setAccessible(false);
               }

               return null;
            }
         }
      };

      private SecuredPrivileged() {
      }

      protected abstract Package definePackage(ClassLoader var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, URL var9) throws IllegalArgumentException;

      // $FF: synthetic method
      SecuredPrivileged(Object x2) {
         this();
      }
   }
}
