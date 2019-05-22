package com.gzoltar.shaded.org.pitest.reloc.xstream.core.util;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

public class DependencyInjectionFactory {
   public static Object newInstance(Class type, Object[] dependencies) {
      return newInstance(type, dependencies, (BitSet)null);
   }

   public static Object newInstance(Class type, Object[] dependencies, BitSet usedDependencies) {
      if (dependencies != null && dependencies.length > 63) {
         throw new IllegalArgumentException("More than 63 arguments are not supported");
      } else {
         Constructor bestMatchingCtor = null;
         ArrayList matchingDependencies = new ArrayList();
         List possibleMatchingDependencies = null;
         long usedDeps = 0L;
         long possibleUsedDeps = 0L;
         if (dependencies != null && dependencies.length > 0) {
            Constructor[] ctors = type.getConstructors();
            if (ctors.length > 1) {
               Arrays.sort(ctors, new Comparator() {
                  public int compare(Object o1, Object o2) {
                     return ((Constructor)o2).getParameterTypes().length - ((Constructor)o1).getParameterTypes().length;
                  }
               });
            }

            DependencyInjectionFactory.TypedValue[] typedDependencies = new DependencyInjectionFactory.TypedValue[dependencies.length];

            for(int i = 0; i < dependencies.length; ++i) {
               Object dependency = dependencies[i];
               Class depType = dependency.getClass();
               if (depType.isPrimitive()) {
                  depType = Primitives.box(depType);
               } else if (depType == TypedNull.class) {
                  depType = ((TypedNull)dependency).getType();
                  dependency = null;
               }

               typedDependencies[i] = new DependencyInjectionFactory.TypedValue(depType, dependency);
            }

            Constructor possibleCtor = null;
            int arity = Integer.MAX_VALUE;

            for(int i = 0; bestMatchingCtor == null && i < ctors.length; ++i) {
               Constructor constructor = ctors[i];
               Class[] parameterTypes = constructor.getParameterTypes();
               if (parameterTypes.length <= dependencies.length) {
                  if (parameterTypes.length == 0) {
                     if (possibleCtor == null) {
                        bestMatchingCtor = constructor;
                     }
                     break;
                  }

                  if (arity > parameterTypes.length) {
                     if (possibleCtor != null) {
                        continue;
                     }

                     arity = parameterTypes.length;
                  }

                  int j;
                  for(j = 0; j < parameterTypes.length; ++j) {
                     if (parameterTypes[j].isPrimitive()) {
                        parameterTypes[j] = Primitives.box(parameterTypes[j]);
                     }
                  }

                  matchingDependencies.clear();
                  usedDeps = 0L;
                  j = 0;

                  for(int k = 0; j < parameterTypes.length && parameterTypes.length + k - j <= typedDependencies.length; ++k) {
                     if (parameterTypes[j].isAssignableFrom(typedDependencies[k].type)) {
                        matchingDependencies.add(typedDependencies[k].value);
                        usedDeps |= 1L << k;
                        ++j;
                        if (j == parameterTypes.length) {
                           bestMatchingCtor = constructor;
                           break;
                        }
                     }
                  }

                  if (bestMatchingCtor == null) {
                     boolean possible = true;
                     DependencyInjectionFactory.TypedValue[] deps = new DependencyInjectionFactory.TypedValue[typedDependencies.length];
                     System.arraycopy(typedDependencies, 0, deps, 0, deps.length);
                     matchingDependencies.clear();
                     usedDeps = 0L;

                     for(int j = 0; j < parameterTypes.length; ++j) {
                        int assignable = -1;

                        for(int k = 0; k < deps.length; ++k) {
                           if (deps[k] != null) {
                              if (deps[k].type == parameterTypes[j]) {
                                 assignable = k;
                                 break;
                              }

                              if (parameterTypes[j].isAssignableFrom(deps[k].type) && (assignable < 0 || deps[assignable].type != deps[k].type && deps[assignable].type.isAssignableFrom(deps[k].type))) {
                                 assignable = k;
                              }
                           }
                        }

                        if (assignable < 0) {
                           possible = false;
                           break;
                        }

                        matchingDependencies.add(deps[assignable].value);
                        usedDeps |= 1L << assignable;
                        deps[assignable] = null;
                     }

                     if (possible && (possibleCtor == null || usedDeps < possibleUsedDeps)) {
                        possibleCtor = constructor;
                        possibleMatchingDependencies = (List)matchingDependencies.clone();
                        possibleUsedDeps = usedDeps;
                     }
                  }
               }
            }

            if (bestMatchingCtor == null) {
               if (possibleCtor == null) {
                  usedDeps = 0L;
                  throw new ObjectAccessException("Cannot construct " + type.getName() + ", none of the dependencies match any constructor's parameters");
               }

               bestMatchingCtor = possibleCtor;
               matchingDependencies.clear();
               matchingDependencies.addAll(possibleMatchingDependencies);
               usedDeps = possibleUsedDeps;
            }
         }

         try {
            Object instance;
            if (bestMatchingCtor == null) {
               instance = type.newInstance();
            } else {
               instance = bestMatchingCtor.newInstance(matchingDependencies.toArray());
            }

            if (usedDependencies != null) {
               usedDependencies.clear();
               int i = 0;

               for(long l = 1L; l < usedDeps; ++i) {
                  if ((usedDeps & l) > 0L) {
                     usedDependencies.set(i);
                  }

                  l <<= 1;
               }
            }

            return instance;
         } catch (InstantiationException var24) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var24);
         } catch (IllegalAccessException var25) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var25);
         } catch (InvocationTargetException var26) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var26);
         } catch (SecurityException var27) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var27);
         } catch (ExceptionInInitializerError var28) {
            throw new ObjectAccessException("Cannot construct " + type.getName(), var28);
         }
      }
   }

   private static class TypedValue {
      final Class type;
      final Object value;

      public TypedValue(Class type, Object value) {
         this.type = type;
         this.value = value;
      }

      public String toString() {
         return this.type.getName() + ":" + this.value;
      }
   }
}
