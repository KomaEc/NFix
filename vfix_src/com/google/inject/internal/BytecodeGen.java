package com.google.inject.internal;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Logger;

public final class BytecodeGen {
   static final Logger logger = Logger.getLogger(BytecodeGen.class.getName());
   static final ClassLoader GUICE_CLASS_LOADER = canonicalize(BytecodeGen.class.getClassLoader());
   static final String GUICE_INTERNAL_PACKAGE = BytecodeGen.class.getName().replaceFirst("\\.internal\\..*$", ".internal");
   private static final String CGLIB_PACKAGE = " ";
   private static final LoadingCache<ClassLoader, ClassLoader> CLASS_LOADER_CACHE;

   private static ClassLoader canonicalize(ClassLoader classLoader) {
      return classLoader != null ? classLoader : BytecodeGen.SystemBridgeHolder.SYSTEM_BRIDGE.getParent();
   }

   public static ClassLoader getClassLoader(Class<?> type) {
      return getClassLoader(type, type.getClassLoader());
   }

   private static ClassLoader getClassLoader(Class<?> type, ClassLoader delegate) {
      if (InternalFlags.getCustomClassLoadingOption() == InternalFlags.CustomClassLoadingOption.OFF) {
         return delegate;
      } else if (type.getName().startsWith("java.")) {
         return GUICE_CLASS_LOADER;
      } else {
         delegate = canonicalize(delegate);
         if (delegate != GUICE_CLASS_LOADER && !(delegate instanceof BytecodeGen.BridgeClassLoader)) {
            if (BytecodeGen.Visibility.forType(type) == BytecodeGen.Visibility.PUBLIC) {
               return (ClassLoader)(delegate != BytecodeGen.SystemBridgeHolder.SYSTEM_BRIDGE.getParent() ? (ClassLoader)CLASS_LOADER_CACHE.getUnchecked(delegate) : BytecodeGen.SystemBridgeHolder.SYSTEM_BRIDGE);
            } else {
               return delegate;
            }
         } else {
            return delegate;
         }
      }
   }

   static {
      CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().weakKeys().weakValues();
      if (InternalFlags.getCustomClassLoadingOption() == InternalFlags.CustomClassLoadingOption.OFF) {
         builder.maximumSize(0L);
      }

      CLASS_LOADER_CACHE = builder.build(new CacheLoader<ClassLoader, ClassLoader>() {
         public ClassLoader load(final ClassLoader typeClassLoader) {
            Logger var10000 = BytecodeGen.logger;
            String var2 = String.valueOf(String.valueOf(typeClassLoader));
            var10000.fine((new StringBuilder(34 + var2.length())).append("Creating a bridge ClassLoader for ").append(var2).toString());
            return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
               public ClassLoader run() {
                  return new BytecodeGen.BridgeClassLoader(typeClassLoader);
               }
            });
         }
      });
   }

   private static class BridgeClassLoader extends ClassLoader {
      BridgeClassLoader() {
      }

      BridgeClassLoader(ClassLoader usersClassLoader) {
         super(usersClassLoader);
      }

      protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
         if (name.startsWith("sun.reflect")) {
            return BytecodeGen.SystemBridgeHolder.SYSTEM_BRIDGE.classicLoadClass(name, resolve);
         } else {
            if (name.startsWith(BytecodeGen.GUICE_INTERNAL_PACKAGE) || name.startsWith(" ")) {
               if (null == BytecodeGen.GUICE_CLASS_LOADER) {
                  return BytecodeGen.SystemBridgeHolder.SYSTEM_BRIDGE.classicLoadClass(name, resolve);
               }

               try {
                  Class<?> clazz = BytecodeGen.GUICE_CLASS_LOADER.loadClass(name);
                  if (resolve) {
                     this.resolveClass(clazz);
                  }

                  return clazz;
               } catch (Throwable var4) {
               }
            }

            return this.classicLoadClass(name, resolve);
         }
      }

      Class<?> classicLoadClass(String name, boolean resolve) throws ClassNotFoundException {
         return super.loadClass(name, resolve);
      }
   }

   public static enum Visibility {
      PUBLIC {
         public BytecodeGen.Visibility and(BytecodeGen.Visibility that) {
            return that;
         }
      },
      SAME_PACKAGE {
         public BytecodeGen.Visibility and(BytecodeGen.Visibility that) {
            return this;
         }
      };

      private Visibility() {
      }

      public static BytecodeGen.Visibility forMember(Member member) {
         if ((member.getModifiers() & 5) == 0) {
            return SAME_PACKAGE;
         } else {
            Class[] parameterTypes;
            if (member instanceof Constructor) {
               parameterTypes = ((Constructor)member).getParameterTypes();
            } else {
               Method method = (Method)member;
               if (forType(method.getReturnType()) == SAME_PACKAGE) {
                  return SAME_PACKAGE;
               }

               parameterTypes = method.getParameterTypes();
            }

            Class[] arr$ = parameterTypes;
            int len$ = parameterTypes.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Class<?> type = arr$[i$];
               if (forType(type) == SAME_PACKAGE) {
                  return SAME_PACKAGE;
               }
            }

            return PUBLIC;
         }
      }

      public static BytecodeGen.Visibility forType(Class<?> type) {
         return (type.getModifiers() & 5) != 0 ? PUBLIC : SAME_PACKAGE;
      }

      public abstract BytecodeGen.Visibility and(BytecodeGen.Visibility var1);

      // $FF: synthetic method
      Visibility(Object x2) {
         this();
      }
   }

   private static class SystemBridgeHolder {
      static final BytecodeGen.BridgeClassLoader SYSTEM_BRIDGE = new BytecodeGen.BridgeClassLoader();
   }
}
