package org.junit.runners.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.internal.MethodSorter;

public class TestClass implements Annotatable {
   private static final TestClass.FieldComparator FIELD_COMPARATOR = new TestClass.FieldComparator();
   private static final TestClass.MethodComparator METHOD_COMPARATOR = new TestClass.MethodComparator();
   private final Class<?> clazz;
   private final Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations;
   private final Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations;

   public TestClass(Class<?> clazz) {
      this.clazz = clazz;
      if (clazz != null && clazz.getConstructors().length > 1) {
         throw new IllegalArgumentException("Test class can only have one constructor");
      } else {
         Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations = new LinkedHashMap();
         Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations = new LinkedHashMap();
         this.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
         this.methodsForAnnotations = makeDeeplyUnmodifiable(methodsForAnnotations);
         this.fieldsForAnnotations = makeDeeplyUnmodifiable(fieldsForAnnotations);
      }
   }

   protected void scanAnnotatedMembers(Map<Class<? extends Annotation>, List<FrameworkMethod>> methodsForAnnotations, Map<Class<? extends Annotation>, List<FrameworkField>> fieldsForAnnotations) {
      Iterator i$ = getSuperClasses(this.clazz).iterator();

      while(i$.hasNext()) {
         Class<?> eachClass = (Class)i$.next();
         Method[] arr$ = MethodSorter.getDeclaredMethods(eachClass);
         int len$ = arr$.length;

         int i$;
         for(i$ = 0; i$ < len$; ++i$) {
            Method eachMethod = arr$[i$];
            addToAnnotationLists(new FrameworkMethod(eachMethod), methodsForAnnotations);
         }

         Field[] arr$ = getSortedDeclaredFields(eachClass);
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            Field eachField = arr$[i$];
            addToAnnotationLists(new FrameworkField(eachField), fieldsForAnnotations);
         }
      }

   }

   private static Field[] getSortedDeclaredFields(Class<?> clazz) {
      Field[] declaredFields = clazz.getDeclaredFields();
      Arrays.sort(declaredFields, FIELD_COMPARATOR);
      return declaredFields;
   }

   protected static <T extends FrameworkMember<T>> void addToAnnotationLists(T member, Map<Class<? extends Annotation>, List<T>> map) {
      Annotation[] arr$ = member.getAnnotations();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Annotation each = arr$[i$];
         Class<? extends Annotation> type = each.annotationType();
         List<T> members = getAnnotatedMembers(map, type, true);
         if (member.isShadowedBy(members)) {
            return;
         }

         if (runsTopToBottom(type)) {
            members.add(0, member);
         } else {
            members.add(member);
         }
      }

   }

   private static <T extends FrameworkMember<T>> Map<Class<? extends Annotation>, List<T>> makeDeeplyUnmodifiable(Map<Class<? extends Annotation>, List<T>> source) {
      LinkedHashMap<Class<? extends Annotation>, List<T>> copy = new LinkedHashMap();
      Iterator i$ = source.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Class<? extends Annotation>, List<T>> entry = (Entry)i$.next();
         copy.put(entry.getKey(), Collections.unmodifiableList((List)entry.getValue()));
      }

      return Collections.unmodifiableMap(copy);
   }

   public List<FrameworkMethod> getAnnotatedMethods() {
      List<FrameworkMethod> methods = this.collectValues(this.methodsForAnnotations);
      Collections.sort(methods, METHOD_COMPARATOR);
      return methods;
   }

   public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotationClass) {
      return Collections.unmodifiableList(getAnnotatedMembers(this.methodsForAnnotations, annotationClass, false));
   }

   public List<FrameworkField> getAnnotatedFields() {
      return this.collectValues(this.fieldsForAnnotations);
   }

   public List<FrameworkField> getAnnotatedFields(Class<? extends Annotation> annotationClass) {
      return Collections.unmodifiableList(getAnnotatedMembers(this.fieldsForAnnotations, annotationClass, false));
   }

   private <T> List<T> collectValues(Map<?, List<T>> map) {
      Set<T> values = new LinkedHashSet();
      Iterator i$ = map.values().iterator();

      while(i$.hasNext()) {
         List<T> additionalValues = (List)i$.next();
         values.addAll(additionalValues);
      }

      return new ArrayList(values);
   }

   private static <T> List<T> getAnnotatedMembers(Map<Class<? extends Annotation>, List<T>> map, Class<? extends Annotation> type, boolean fillIfAbsent) {
      if (!map.containsKey(type) && fillIfAbsent) {
         map.put(type, new ArrayList());
      }

      List<T> members = (List)map.get(type);
      return members == null ? Collections.emptyList() : members;
   }

   private static boolean runsTopToBottom(Class<? extends Annotation> annotation) {
      return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
   }

   private static List<Class<?>> getSuperClasses(Class<?> testClass) {
      ArrayList<Class<?>> results = new ArrayList();

      for(Class current = testClass; current != null; current = current.getSuperclass()) {
         results.add(current);
      }

      return results;
   }

   public Class<?> getJavaClass() {
      return this.clazz;
   }

   public String getName() {
      return this.clazz == null ? "null" : this.clazz.getName();
   }

   public Constructor<?> getOnlyConstructor() {
      Constructor<?>[] constructors = this.clazz.getConstructors();
      Assert.assertEquals(1L, (long)constructors.length);
      return constructors[0];
   }

   public Annotation[] getAnnotations() {
      return this.clazz == null ? new Annotation[0] : this.clazz.getAnnotations();
   }

   public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
      return this.clazz == null ? null : this.clazz.getAnnotation(annotationType);
   }

   public <T> List<T> getAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
      List<T> results = new ArrayList();
      Iterator i$ = this.getAnnotatedFields(annotationClass).iterator();

      while(i$.hasNext()) {
         FrameworkField each = (FrameworkField)i$.next();

         try {
            Object fieldValue = each.get(test);
            if (valueClass.isInstance(fieldValue)) {
               results.add(valueClass.cast(fieldValue));
            }
         } catch (IllegalAccessException var8) {
            throw new RuntimeException("How did getFields return a field we couldn't access?", var8);
         }
      }

      return results;
   }

   public <T> List<T> getAnnotatedMethodValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
      List<T> results = new ArrayList();
      Iterator i$ = this.getAnnotatedMethods(annotationClass).iterator();

      while(i$.hasNext()) {
         FrameworkMethod each = (FrameworkMethod)i$.next();

         try {
            if (valueClass.isAssignableFrom(each.getReturnType())) {
               Object fieldValue = each.invokeExplosively(test);
               results.add(valueClass.cast(fieldValue));
            }
         } catch (Throwable var8) {
            throw new RuntimeException("Exception in " + each.getName(), var8);
         }
      }

      return results;
   }

   public boolean isPublic() {
      return Modifier.isPublic(this.clazz.getModifiers());
   }

   public boolean isANonStaticInnerClass() {
      return this.clazz.isMemberClass() && !Modifier.isStatic(this.clazz.getModifiers());
   }

   public int hashCode() {
      return this.clazz == null ? 0 : this.clazz.hashCode();
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         TestClass other = (TestClass)obj;
         return this.clazz == other.clazz;
      }
   }

   private static class MethodComparator implements Comparator<FrameworkMethod> {
      private MethodComparator() {
      }

      public int compare(FrameworkMethod left, FrameworkMethod right) {
         return MethodSorter.NAME_ASCENDING.compare(left.getMethod(), right.getMethod());
      }

      // $FF: synthetic method
      MethodComparator(Object x0) {
         this();
      }
   }

   private static class FieldComparator implements Comparator<Field> {
      private FieldComparator() {
      }

      public int compare(Field left, Field right) {
         return left.getName().compareTo(right.getName());
      }

      // $FF: synthetic method
      FieldComparator(Object x0) {
         this();
      }
   }
}
