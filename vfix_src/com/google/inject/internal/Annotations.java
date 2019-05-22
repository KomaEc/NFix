package com.google.inject.internal;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.inject.BindingAnnotation;
import com.google.inject.Key;
import com.google.inject.ScopeAnnotation;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.Classes;
import com.google.inject.name.Names;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Scope;

public class Annotations {
   private static final LoadingCache<Class<? extends Annotation>, Annotation> cache = CacheBuilder.newBuilder().weakKeys().build(new CacheLoader<Class<? extends Annotation>, Annotation>() {
      public Annotation load(Class<? extends Annotation> input) {
         return Annotations.generateAnnotationImpl(input);
      }
   });
   private static final Joiner.MapJoiner JOINER = Joiner.on(", ").withKeyValueSeparator("=");
   private static final Function<Object, String> DEEP_TO_STRING_FN = new Function<Object, String>() {
      public String apply(Object arg) {
         String s = Arrays.deepToString(new Object[]{arg});
         return s.substring(1, s.length() - 1);
      }
   };
   private static final Annotations.AnnotationChecker scopeChecker = new Annotations.AnnotationChecker(Arrays.asList(ScopeAnnotation.class, Scope.class));
   private static final Annotations.AnnotationChecker bindingAnnotationChecker = new Annotations.AnnotationChecker(Arrays.asList(BindingAnnotation.class, Qualifier.class));

   public static boolean isMarker(Class<? extends Annotation> annotationType) {
      return annotationType.getDeclaredMethods().length == 0;
   }

   public static boolean isAllDefaultMethods(Class<? extends Annotation> annotationType) {
      boolean hasMethods = false;
      Method[] arr$ = annotationType.getDeclaredMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         hasMethods = true;
         if (m.getDefaultValue() == null) {
            return false;
         }
      }

      return hasMethods;
   }

   public static <T extends Annotation> T generateAnnotation(Class<T> annotationType) {
      Preconditions.checkState(isAllDefaultMethods(annotationType), "%s is not all default methods", annotationType);
      return (Annotation)cache.getUnchecked(annotationType);
   }

   private static <T extends Annotation> T generateAnnotationImpl(final Class<T> annotationType) {
      final Map<String, Object> members = resolveMembers(annotationType);
      return (Annotation)annotationType.cast(Proxy.newProxyInstance(annotationType.getClassLoader(), new Class[]{annotationType}, new InvocationHandler() {
         public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            String name = method.getName();
            if (name.equals("annotationType")) {
               return annotationType;
            } else if (name.equals("toString")) {
               return Annotations.annotationToString(annotationType, members);
            } else if (name.equals("hashCode")) {
               return Annotations.annotationHashCode(annotationType, members);
            } else {
               return name.equals("equals") ? Annotations.annotationEquals(annotationType, members, args[0]) : members.get(name);
            }
         }
      }));
   }

   private static ImmutableMap<String, Object> resolveMembers(Class<? extends Annotation> annotationType) {
      ImmutableMap.Builder<String, Object> result = ImmutableMap.builder();
      Method[] arr$ = annotationType.getDeclaredMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method method = arr$[i$];
         result.put(method.getName(), method.getDefaultValue());
      }

      return result.build();
   }

   private static boolean annotationEquals(Class<? extends Annotation> type, Map<String, Object> members, Object other) throws Exception {
      if (!type.isInstance(other)) {
         return false;
      } else {
         Method[] arr$ = type.getDeclaredMethods();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Method method = arr$[i$];
            String name = method.getName();
            if (!Arrays.deepEquals(new Object[]{method.invoke(other)}, new Object[]{members.get(name)})) {
               return false;
            }
         }

         return true;
      }
   }

   private static int annotationHashCode(Class<? extends Annotation> type, Map<String, Object> members) throws Exception {
      int result = 0;
      Method[] arr$ = type.getDeclaredMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method method = arr$[i$];
         String name = method.getName();
         Object value = members.get(name);
         result += 127 * name.hashCode() ^ Arrays.deepHashCode(new Object[]{value}) - 31;
      }

      return result;
   }

   private static String annotationToString(Class<? extends Annotation> type, Map<String, Object> members) throws Exception {
      StringBuilder sb = (new StringBuilder()).append("@").append(type.getName()).append("(");
      JOINER.appendTo(sb, Maps.transformValues(members, DEEP_TO_STRING_FN));
      return sb.append(")").toString();
   }

   public static boolean isRetainedAtRuntime(Class<? extends Annotation> annotationType) {
      Retention retention = (Retention)annotationType.getAnnotation(Retention.class);
      return retention != null && retention.value() == RetentionPolicy.RUNTIME;
   }

   public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Class<?> implementation) {
      return findScopeAnnotation(errors, implementation.getAnnotations());
   }

   public static Class<? extends Annotation> findScopeAnnotation(Errors errors, Annotation[] annotations) {
      Class<? extends Annotation> found = null;
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         Class<? extends Annotation> annotationType = annotation.annotationType();
         if (isScopeAnnotation(annotationType)) {
            if (found != null) {
               errors.duplicateScopeAnnotations(found, annotationType);
            } else {
               found = annotationType;
            }
         }
      }

      return found;
   }

   static boolean containsComponentAnnotation(Annotation[] annotations) {
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         if (annotation.annotationType().getSimpleName().equals("Component")) {
            return true;
         }
      }

      return false;
   }

   public static boolean isScopeAnnotation(Class<? extends Annotation> annotationType) {
      return scopeChecker.hasAnnotations(annotationType);
   }

   public static void checkForMisplacedScopeAnnotations(Class<?> type, Object source, Errors errors) {
      if (!Classes.isConcrete(type)) {
         Class<? extends Annotation> scopeAnnotation = findScopeAnnotation(errors, type);
         if (scopeAnnotation != null && !containsComponentAnnotation(type.getAnnotations())) {
            errors.withSource(type).scopeAnnotationOnAbstractType(scopeAnnotation, type, source);
         }

      }
   }

   public static Key<?> getKey(TypeLiteral<?> type, Member member, Annotation[] annotations, Errors errors) throws ErrorsException {
      int numErrorsBefore = errors.size();
      Annotation found = findBindingAnnotation(errors, member, annotations);
      errors.throwIfNewErrors(numErrorsBefore);
      return found == null ? Key.get(type) : Key.get(type, found);
   }

   public static Annotation findBindingAnnotation(Errors errors, Member member, Annotation[] annotations) {
      Annotation found = null;
      Annotation[] arr$ = annotations;
      int len$ = annotations.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation annotation = arr$[i$];
         Class<? extends Annotation> annotationType = annotation.annotationType();
         if (isBindingAnnotation(annotationType)) {
            if (found != null) {
               errors.duplicateBindingAnnotations(member, found.annotationType(), annotationType);
            } else {
               found = annotation;
            }
         }
      }

      return found;
   }

   public static boolean isBindingAnnotation(Class<? extends Annotation> annotationType) {
      return bindingAnnotationChecker.hasAnnotations(annotationType);
   }

   public static Annotation canonicalizeIfNamed(Annotation annotation) {
      return (Annotation)(annotation instanceof Named ? Names.named(((Named)annotation).value()) : annotation);
   }

   public static Class<? extends Annotation> canonicalizeIfNamed(Class<? extends Annotation> annotationType) {
      return annotationType == Named.class ? com.google.inject.name.Named.class : annotationType;
   }

   static class AnnotationChecker {
      private final Collection<Class<? extends Annotation>> annotationTypes;
      private CacheLoader<Class<? extends Annotation>, Boolean> hasAnnotations = new CacheLoader<Class<? extends Annotation>, Boolean>() {
         public Boolean load(Class<? extends Annotation> annotationType) {
            Annotation[] arr$ = annotationType.getAnnotations();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Annotation annotation = arr$[i$];
               if (AnnotationChecker.this.annotationTypes.contains(annotation.annotationType())) {
                  return true;
               }
            }

            return false;
         }
      };
      final LoadingCache<Class<? extends Annotation>, Boolean> cache;

      AnnotationChecker(Collection<Class<? extends Annotation>> annotationTypes) {
         this.cache = CacheBuilder.newBuilder().weakKeys().build(this.hasAnnotations);
         this.annotationTypes = annotationTypes;
      }

      boolean hasAnnotations(Class<? extends Annotation> annotated) {
         return (Boolean)this.cache.getUnchecked(annotated);
      }
   }
}
