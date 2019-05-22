package org.jf.dexlib2.analysis.reflection;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.analysis.reflection.util.ReflectionUtils;
import org.jf.dexlib2.base.reference.BaseTypeReference;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;

public class ReflectionClassDef extends BaseTypeReference implements ClassDef {
   private final Class cls;
   private static final int DIRECT_MODIFIERS = 10;

   public ReflectionClassDef(Class cls) {
      this.cls = cls;
   }

   public int getAccessFlags() {
      return this.cls.getModifiers();
   }

   @Nullable
   public String getSuperclass() {
      if (Modifier.isInterface(this.cls.getModifiers())) {
         return "Ljava/lang/Object;";
      } else {
         Class superClass = this.cls.getSuperclass();
         return superClass == null ? null : ReflectionUtils.javaToDexName(superClass.getName());
      }
   }

   @Nonnull
   public List<String> getInterfaces() {
      return ImmutableList.copyOf(Iterators.transform(Iterators.forArray(this.cls.getInterfaces()), new Function<Class, String>() {
         @Nullable
         public String apply(@Nullable Class input) {
            return input == null ? null : ReflectionUtils.javaToDexName(input.getName());
         }
      }));
   }

   @Nullable
   public String getSourceFile() {
      return null;
   }

   @Nonnull
   public Set<? extends Annotation> getAnnotations() {
      return ImmutableSet.of();
   }

   @Nonnull
   public Iterable<? extends Field> getStaticFields() {
      return new Iterable<Field>() {
         @Nonnull
         public Iterator<Field> iterator() {
            Iterator<java.lang.reflect.Field> staticFields = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredFields()), (Predicate)(new Predicate<java.lang.reflect.Field>() {
               public boolean apply(@Nullable java.lang.reflect.Field input) {
                  return input != null && Modifier.isStatic(input.getModifiers());
               }
            }));
            return Iterators.transform(staticFields, new Function<java.lang.reflect.Field, Field>() {
               @Nullable
               public Field apply(@Nullable java.lang.reflect.Field input) {
                  return new ReflectionField(input);
               }
            });
         }
      };
   }

   @Nonnull
   public Iterable<? extends Field> getInstanceFields() {
      return new Iterable<Field>() {
         @Nonnull
         public Iterator<Field> iterator() {
            Iterator<java.lang.reflect.Field> staticFields = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredFields()), (Predicate)(new Predicate<java.lang.reflect.Field>() {
               public boolean apply(@Nullable java.lang.reflect.Field input) {
                  return input != null && !Modifier.isStatic(input.getModifiers());
               }
            }));
            return Iterators.transform(staticFields, new Function<java.lang.reflect.Field, Field>() {
               @Nullable
               public Field apply(@Nullable java.lang.reflect.Field input) {
                  return new ReflectionField(input);
               }
            });
         }
      };
   }

   @Nonnull
   public Set<? extends Field> getFields() {
      return new AbstractSet<Field>() {
         @Nonnull
         public Iterator<Field> iterator() {
            return Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredFields()), new Function<java.lang.reflect.Field, Field>() {
               @Nullable
               public Field apply(@Nullable java.lang.reflect.Field input) {
                  return new ReflectionField(input);
               }
            });
         }

         public int size() {
            return ReflectionClassDef.this.cls.getDeclaredFields().length;
         }
      };
   }

   @Nonnull
   public Iterable<? extends Method> getDirectMethods() {
      return new Iterable<Method>() {
         @Nonnull
         public Iterator<Method> iterator() {
            Iterator<Method> constructorIterator = Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredConstructors()), new Function<Constructor, Method>() {
               @Nullable
               public Method apply(@Nullable Constructor input) {
                  return new ReflectionConstructor(input);
               }
            });
            Iterator<java.lang.reflect.Method> directMethods = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredMethods()), (Predicate)(new Predicate<java.lang.reflect.Method>() {
               public boolean apply(@Nullable java.lang.reflect.Method input) {
                  return input != null && (input.getModifiers() & 10) != 0;
               }
            }));
            Iterator<Method> methodIterator = Iterators.transform(directMethods, new Function<java.lang.reflect.Method, Method>() {
               @Nullable
               public Method apply(@Nullable java.lang.reflect.Method input) {
                  return new ReflectionMethod(input);
               }
            });
            return Iterators.concat(constructorIterator, methodIterator);
         }
      };
   }

   @Nonnull
   public Iterable<? extends Method> getVirtualMethods() {
      return new Iterable<Method>() {
         @Nonnull
         public Iterator<Method> iterator() {
            Iterator<java.lang.reflect.Method> directMethods = Iterators.filter(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredMethods()), (Predicate)(new Predicate<java.lang.reflect.Method>() {
               public boolean apply(@Nullable java.lang.reflect.Method input) {
                  return input != null && (input.getModifiers() & 10) == 0;
               }
            }));
            return Iterators.transform(directMethods, new Function<java.lang.reflect.Method, Method>() {
               @Nullable
               public Method apply(@Nullable java.lang.reflect.Method input) {
                  return new ReflectionMethod(input);
               }
            });
         }
      };
   }

   @Nonnull
   public Set<? extends Method> getMethods() {
      return new AbstractSet<Method>() {
         @Nonnull
         public Iterator<Method> iterator() {
            Iterator<Method> constructorIterator = Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredConstructors()), new Function<Constructor, Method>() {
               @Nullable
               public Method apply(@Nullable Constructor input) {
                  return new ReflectionConstructor(input);
               }
            });
            Iterator<Method> methodIterator = Iterators.transform(Iterators.forArray(ReflectionClassDef.this.cls.getDeclaredMethods()), new Function<java.lang.reflect.Method, Method>() {
               @Nullable
               public Method apply(@Nullable java.lang.reflect.Method input) {
                  return new ReflectionMethod(input);
               }
            });
            return Iterators.concat(constructorIterator, methodIterator);
         }

         public int size() {
            return ReflectionClassDef.this.cls.getDeclaredMethods().length + ReflectionClassDef.this.cls.getDeclaredConstructors().length;
         }
      };
   }

   @Nonnull
   public String getType() {
      return ReflectionUtils.javaToDexName(this.cls.getName());
   }
}
