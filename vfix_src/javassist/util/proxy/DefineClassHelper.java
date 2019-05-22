package javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;
import javassist.CannotCompileException;
import javassist.bytecode.ClassFile;

public class DefineClassHelper {
   private static final DefineClassHelper.SecuredPrivileged privileged;

   public static Class<?> toClass(String className, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
      try {
         return privileged.defineClass(className, bcode, 0, bcode.length, loader, domain);
      } catch (RuntimeException var5) {
         throw var5;
      } catch (ClassFormatError var6) {
         throw new CannotCompileException(var6.getCause());
      } catch (Exception var7) {
         throw new CannotCompileException(var7);
      }
   }

   static Class<?> toPublicClass(String className, byte[] bcode) throws CannotCompileException {
      try {
         Lookup lookup = MethodHandles.lookup();
         lookup = lookup.dropLookupMode(2);
         return lookup.defineClass(bcode);
      } catch (Throwable var3) {
         throw new CannotCompileException(var3);
      }
   }

   private DefineClassHelper() {
   }

   static {
      privileged = ClassFile.MAJOR_VERSION > 54 ? DefineClassHelper.SecuredPrivileged.JAVA_OTHER : (ClassFile.MAJOR_VERSION >= 53 ? DefineClassHelper.SecuredPrivileged.JAVA_9 : (ClassFile.MAJOR_VERSION >= 51 ? DefineClassHelper.SecuredPrivileged.JAVA_7 : DefineClassHelper.SecuredPrivileged.JAVA_OTHER));
   }

   private static enum SecuredPrivileged {
      JAVA_9 {
         private final Object stack;
         private final Method getCallerClass;
         private final null.ReferencedUnsafe sunMiscUnsafe;

         {
            Class stackWalkerClass = null;

            try {
               stackWalkerClass = Class.forName("java.lang.StackWalker");
            } catch (ClassNotFoundException var6) {
            }

            if (stackWalkerClass != null) {
               try {
                  Class<?> optionClass = Class.forName("java.lang.StackWalker$Option");
                  this.stack = stackWalkerClass.getMethod("getInstance", optionClass).invoke((Object)null, optionClass.getEnumConstants()[0]);
                  this.getCallerClass = stackWalkerClass.getMethod("getCallerClass");
               } catch (Throwable var5) {
                  throw new RuntimeException("cannot initialize", var5);
               }
            } else {
               this.stack = null;
               this.getCallerClass = null;
            }

            this.sunMiscUnsafe = this.getReferencedUnsafe();
         }

         private final null.ReferencedUnsafe getReferencedUnsafe() {
            try {
               if (null != DefineClassHelper.SecuredPrivileged.JAVA_9 && this.getCallerClass.invoke(this.stack) != this.getClass()) {
                  throw new IllegalAccessError("Access denied for caller.");
               }
            } catch (Exception var5) {
               throw new RuntimeException("cannot initialize", var5);
            }

            try {
               SecurityActions.TheUnsafe usf = SecurityActions.getSunMiscUnsafeAnonymously();
               List<Method> defineClassMethod = (List)usf.methods.get("defineClass");
               if (null == defineClassMethod) {
                  return null;
               } else {
                  MethodHandle meth = MethodHandles.lookup().unreflect((Method)defineClassMethod.get(0));
                  return new null.ReferencedUnsafe(usf, meth);
               }
            } catch (Throwable var4) {
               throw new RuntimeException("cannot initialize", var4);
            }
         }

         public Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            try {
               if (this.getCallerClass.invoke(this.stack) != DefineClassHelper.class) {
                  throw new IllegalAccessError("Access denied for caller.");
               }
            } catch (Exception var8) {
               throw new RuntimeException("cannot initialize", var8);
            }

            return this.sunMiscUnsafe.defineClass(name, b, off, len, loader, protectionDomain);
         }

         final class ReferencedUnsafe {
            private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;
            private final MethodHandle defineClass;

            ReferencedUnsafe(SecurityActions.TheUnsafe usf, MethodHandle meth) {
               this.sunMiscUnsafeTheUnsafe = usf;
               this.defineClass = meth;
            }

            Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
               try {
                  if (getCallerClass.invoke(stack) != DefineClassHelper.SecuredPrivileged.JAVA_9.getClass()) {
                     throw new IllegalAccessError("Access denied for caller.");
                  }
               } catch (Exception var9) {
                  throw new RuntimeException("cannot initialize", var9);
               }

               try {
                  return (Class)this.defineClass.invokeWithArguments(this.sunMiscUnsafeTheUnsafe.theUnsafe, name, b, off, len, loader, protectionDomain);
               } catch (Throwable var8) {
                  if (var8 instanceof RuntimeException) {
                     throw (RuntimeException)var8;
                  } else if (var8 instanceof ClassFormatError) {
                     throw (ClassFormatError)var8;
                  } else {
                     throw new ClassFormatError(var8.getMessage());
                  }
               }
            }
         }
      },
      JAVA_7 {
         private final SecurityActions stack;
         private final MethodHandle defineClass;

         {
            this.stack = SecurityActions.stack;
            this.defineClass = this.getDefineClassMethodHandle();
         }

         private final MethodHandle getDefineClassMethodHandle() {
            if (null != DefineClassHelper.SecuredPrivileged.JAVA_7 && this.stack.getCallerClass() != this.getClass()) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
               } catch (NoSuchMethodException var2) {
                  throw new RuntimeException("cannot initialize", var2);
               }
            }
         }

         protected Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            if (this.stack.getCallerClass() != DefineClassHelper.class) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  return (Class)this.defineClass.invokeWithArguments(loader, name, b, off, len, protectionDomain);
               } catch (Throwable var8) {
                  if (var8 instanceof RuntimeException) {
                     throw (RuntimeException)var8;
                  } else if (var8 instanceof ClassFormatError) {
                     throw (ClassFormatError)var8;
                  } else {
                     throw new ClassFormatError(var8.getMessage());
                  }
               }
            }
         }
      },
      JAVA_OTHER {
         private final Method defineClass = this.getDefineClassMethod();
         private final SecurityActions stack;

         {
            this.stack = SecurityActions.stack;
         }

         private final Method getDefineClassMethod() {
            if (null != DefineClassHelper.SecuredPrivileged.JAVA_OTHER && this.stack.getCallerClass() != this.getClass()) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               try {
                  return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
               } catch (NoSuchMethodException var2) {
                  throw new RuntimeException("cannot initialize", var2);
               }
            }
         }

         protected Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            if (this.stack.getCallerClass() != DefineClassHelper.class) {
               throw new IllegalAccessError("Access denied for caller.");
            } else {
               Class var7;
               try {
                  SecurityActions.setAccessible(this.defineClass, true);
                  var7 = (Class)this.defineClass.invoke(loader, name, b, off, len, protectionDomain);
               } catch (Throwable var11) {
                  if (var11 instanceof ClassFormatError) {
                     throw (ClassFormatError)var11;
                  }

                  if (var11 instanceof RuntimeException) {
                     throw (RuntimeException)var11;
                  }

                  throw new ClassFormatError(var11.getMessage());
               } finally {
                  SecurityActions.setAccessible(this.defineClass, false);
               }

               return var7;
            }
         }
      };

      private SecuredPrivileged() {
      }

      protected abstract Class<?> defineClass(String var1, byte[] var2, int var3, int var4, ClassLoader var5, ProtectionDomain var6) throws ClassFormatError;

      // $FF: synthetic method
      SecuredPrivileged(Object x2) {
         this();
      }
   }
}
