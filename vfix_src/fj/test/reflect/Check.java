package fj.test.reflect;

import fj.Bottom;
import fj.F;
import fj.Function;
import fj.P;
import fj.P2;
import fj.P3;
import fj.data.Array;
import fj.data.List;
import fj.data.Option;
import fj.test.CheckResult;
import fj.test.Property;
import fj.test.Rand;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Check {
   private Check() {
      throw new UnsupportedOperationException();
   }

   public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, String... categories) {
      return check(c, Rand.standard, categories);
   }

   public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, List<String> categories) {
      return check(c, Rand.standard, (String[])categories.toArray().array(String[].class));
   }

   public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, final Rand r, final String... categories) {
      return List.join(c.map(new F<Class<T>, List<P2<String, CheckResult>>>() {
         public List<P2<String, CheckResult>> f(Class<T> c) {
            return Check.check(c, r, categories);
         }
      }));
   }

   public static <T> List<P2<String, CheckResult>> check(List<Class<T>> c, Rand r, List<String> categories) {
      return check(c, r, (String[])categories.toArray().array(String[].class));
   }

   public static <T> List<P2<String, CheckResult>> check(Class<T> c, String... categories) {
      return check(c, Rand.standard, categories);
   }

   public static <T> List<P2<String, CheckResult>> check(Class<T> c, List<String> categories) {
      return check(c, Rand.standard, (String[])categories.toArray().array(String[].class));
   }

   public static <T> List<P2<String, CheckResult>> check(Class<T> c, final Rand r, final String... categories) {
      return List.join(fj.Class.clas(c).inheritance().map(new F<fj.Class<? super T>, List<P3<Property, String, Option<CheckParams>>>>() {
         public List<P3<Property, String, Option<CheckParams>>> f(fj.Class<? super T> c) {
            return Check.properties(c.clas(), categories);
         }
      })).map(new F<P3<Property, String, Option<CheckParams>>, P2<String, CheckResult>>() {
         public P2<String, CheckResult> f(P3<Property, String, Option<CheckParams>> p) {
            if (((Option)p._3()).isSome()) {
               CheckParams ps = (CheckParams)((Option)p._3()).some();
               return P.p(p._2(), ((Property)p._1()).check(r, ps.minSuccessful(), ps.maxDiscarded(), ps.minSize(), ps.maxSize()));
            } else {
               return P.p(p._2(), ((Property)p._1()).check(r));
            }
         }
      });
   }

   public static <T> List<P2<String, CheckResult>> check(Class<T> c, Rand r, List<String> categories) {
      return check(c, r, (String[])categories.toArray().array(String[].class));
   }

   public static <U, T extends U> List<P3<Property, String, Option<CheckParams>>> properties(Class<T> c, String... categories) {
      Array<P3<Property, String, Option<CheckParams>>> propFields = properties(Array.array((Object[])c.getDeclaredFields()).map(new F<Field, Check.PropertyMember>() {
         public Check.PropertyMember f(final Field f) {
            return new Check.PropertyMember() {
               public Class<?> type() {
                  return f.getType();
               }

               public AnnotatedElement element() {
                  return f;
               }

               public String name() {
                  return f.getName();
               }

               public int modifiers() {
                  return f.getModifiers();
               }

               public <X> Property invoke(X x) throws IllegalAccessException {
                  f.setAccessible(true);
                  return (Property)f.get(x);
               }

               public boolean isProperty() {
                  return true;
               }
            };
         }
      }), c, categories);
      Array<P3<Property, String, Option<CheckParams>>> propMethods = properties(Array.array((Object[])c.getDeclaredMethods()).map(new F<Method, Check.PropertyMember>() {
         public Check.PropertyMember f(final Method m) {
            return new Check.PropertyMember() {
               public Class<?> type() {
                  return m.getReturnType();
               }

               public AnnotatedElement element() {
                  return m;
               }

               public String name() {
                  return m.getName();
               }

               public int modifiers() {
                  return m.getModifiers();
               }

               public <X> Property invoke(X x) throws Exception {
                  m.setAccessible(true);
                  return (Property)m.invoke(x);
               }

               public boolean isProperty() {
                  return m.getParameterTypes().length == 0;
               }
            };
         }
      }), c, categories);
      return propFields.append(propMethods).toList();
   }

   private static <T> Array<P3<Property, String, Option<CheckParams>>> properties(Array<Check.PropertyMember> ms, final Class<T> declaringClass, final String... categories) {
      final Option<T> t = emptyCtor(declaringClass).map(new F<Constructor<T>, T>() {
         public T f(Constructor<T> ctor) {
            try {
               ctor.setAccessible(true);
               return ctor.newInstance();
            } catch (Exception var3) {
               throw Bottom.error(var3.toString());
            }
         }
      });
      final F<AnnotatedElement, F<String, Boolean>> p = new F<AnnotatedElement, F<String, Boolean>>() {
         public F<String, Boolean> f(final AnnotatedElement e) {
            return new F<String, Boolean>() {
               public Boolean f(final String s) {
                  F<Category, Boolean> p = new F<Category, Boolean>() {
                     public Boolean f(Category c) {
                        return Array.array((Object[])c.value()).exists(new F<String, Boolean>() {
                           public Boolean f(String cs) {
                              return cs.equals(s);
                           }
                        });
                     }
                  };
                  List<Boolean> bss = Option.somes(List.list(Option.fromNull(e.getAnnotation(Category.class)).map(p), Option.fromNull(declaringClass.getAnnotation(Category.class)).map(p)));
                  return bss.exists(Function.identity());
               }
            };
         }
      };
      final F<Name, String> nameS = new F<Name, String>() {
         public String f(Name name) {
            return name.value();
         }
      };
      return ms.filter(new F<Check.PropertyMember, Boolean>() {
         public Boolean f(Check.PropertyMember m) {
            return m.isProperty() && m.type() == Property.class && !m.element().isAnnotationPresent(NoCheck.class) && !declaringClass.isAnnotationPresent(NoCheck.class) && (categories.length == 0 || Array.array((Object[])categories).exists((F)p.f(m.element()))) && (t.isSome() || Modifier.isStatic(m.modifiers()));
         }
      }).map(new F<Check.PropertyMember, P3<Property, String, Option<CheckParams>>>() {
         public P3<Property, String, Option<CheckParams>> f(Check.PropertyMember m) {
            try {
               Option<CheckParams> params = Option.fromNull(m.element().getAnnotation(CheckParams.class)).orElse(Option.fromNull(declaringClass.getAnnotation(CheckParams.class)));
               String name = (String)Option.fromNull(m.element().getAnnotation(Name.class)).map(nameS).orSome((Object)m.name());
               return P.p(m.invoke(t.orSome(P.p((Object)null))), name, params);
            } catch (Exception var4) {
               throw Bottom.error(var4.toString());
            }
         }
      });
   }

   private static <T> Option<Constructor<T>> emptyCtor(Class<T> c) {
      Option ctor;
      try {
         ctor = Option.some(c.getDeclaredConstructor());
      } catch (NoSuchMethodException var3) {
         ctor = Option.none();
      }

      return ctor;
   }

   private interface PropertyMember {
      Class<?> type();

      AnnotatedElement element();

      String name();

      int modifiers();

      <X> Property invoke(X var1) throws Exception;

      boolean isProperty();
   }
}
