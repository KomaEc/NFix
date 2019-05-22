package org.junit.experimental.theories.internal;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.Assume;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.PotentialAssignment;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class AllMembersSupplier extends ParameterSupplier {
   private final TestClass clazz;

   public AllMembersSupplier(TestClass type) {
      this.clazz = type;
   }

   public List<PotentialAssignment> getValueSources(ParameterSignature sig) throws Throwable {
      List<PotentialAssignment> list = new ArrayList();
      this.addSinglePointFields(sig, list);
      this.addMultiPointFields(sig, list);
      this.addSinglePointMethods(sig, list);
      this.addMultiPointMethods(sig, list);
      return list;
   }

   private void addMultiPointMethods(ParameterSignature sig, List<PotentialAssignment> list) throws Throwable {
      Iterator i$ = this.getDataPointsMethods(sig).iterator();

      while(true) {
         FrameworkMethod dataPointsMethod;
         Class returnType;
         do {
            if (!i$.hasNext()) {
               return;
            }

            dataPointsMethod = (FrameworkMethod)i$.next();
            returnType = dataPointsMethod.getReturnType();
         } while((!returnType.isArray() || !sig.canPotentiallyAcceptType(returnType.getComponentType())) && !Iterable.class.isAssignableFrom(returnType));

         try {
            this.addDataPointsValues(returnType, sig, dataPointsMethod.getName(), list, dataPointsMethod.invokeExplosively((Object)null));
         } catch (Throwable var8) {
            DataPoints annotation = (DataPoints)dataPointsMethod.getAnnotation(DataPoints.class);
            if (annotation != null && isAssignableToAnyOf(annotation.ignoredExceptions(), var8)) {
               return;
            }

            throw var8;
         }
      }
   }

   private void addSinglePointMethods(ParameterSignature sig, List<PotentialAssignment> list) {
      Iterator i$ = this.getSingleDataPointMethods(sig).iterator();

      while(i$.hasNext()) {
         FrameworkMethod dataPointMethod = (FrameworkMethod)i$.next();
         if (sig.canAcceptType(dataPointMethod.getType())) {
            list.add(new AllMembersSupplier.MethodParameterValue(dataPointMethod));
         }
      }

   }

   private void addMultiPointFields(ParameterSignature sig, List<PotentialAssignment> list) {
      Iterator i$ = this.getDataPointsFields(sig).iterator();

      while(i$.hasNext()) {
         Field field = (Field)i$.next();
         Class<?> type = field.getType();
         this.addDataPointsValues(type, sig, field.getName(), list, this.getStaticFieldValue(field));
      }

   }

   private void addSinglePointFields(ParameterSignature sig, List<PotentialAssignment> list) {
      Iterator i$ = this.getSingleDataPointFields(sig).iterator();

      while(i$.hasNext()) {
         Field field = (Field)i$.next();
         Object value = this.getStaticFieldValue(field);
         if (sig.canAcceptValue(value)) {
            list.add(PotentialAssignment.forValue(field.getName(), value));
         }
      }

   }

   private void addDataPointsValues(Class<?> type, ParameterSignature sig, String name, List<PotentialAssignment> list, Object value) {
      if (type.isArray()) {
         this.addArrayValues(sig, name, list, value);
      } else if (Iterable.class.isAssignableFrom(type)) {
         this.addIterableValues(sig, name, list, (Iterable)value);
      }

   }

   private void addArrayValues(ParameterSignature sig, String name, List<PotentialAssignment> list, Object array) {
      for(int i = 0; i < Array.getLength(array); ++i) {
         Object value = Array.get(array, i);
         if (sig.canAcceptValue(value)) {
            list.add(PotentialAssignment.forValue(name + "[" + i + "]", value));
         }
      }

   }

   private void addIterableValues(ParameterSignature sig, String name, List<PotentialAssignment> list, Iterable<?> iterable) {
      Iterator<?> iterator = iterable.iterator();

      for(int i = 0; iterator.hasNext(); ++i) {
         Object value = iterator.next();
         if (sig.canAcceptValue(value)) {
            list.add(PotentialAssignment.forValue(name + "[" + i + "]", value));
         }
      }

   }

   private Object getStaticFieldValue(Field field) {
      try {
         return field.get((Object)null);
      } catch (IllegalArgumentException var3) {
         throw new RuntimeException("unexpected: field from getClass doesn't exist on object");
      } catch (IllegalAccessException var4) {
         throw new RuntimeException("unexpected: getFields returned an inaccessible field");
      }
   }

   private static boolean isAssignableToAnyOf(Class<?>[] typeArray, Object target) {
      Class[] arr$ = typeArray;
      int len$ = typeArray.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Class<?> type = arr$[i$];
         if (type.isAssignableFrom(target.getClass())) {
            return true;
         }
      }

      return false;
   }

   protected Collection<FrameworkMethod> getDataPointsMethods(ParameterSignature sig) {
      return this.clazz.getAnnotatedMethods(DataPoints.class);
   }

   protected Collection<Field> getSingleDataPointFields(ParameterSignature sig) {
      List<FrameworkField> fields = this.clazz.getAnnotatedFields(DataPoint.class);
      Collection<Field> validFields = new ArrayList();
      Iterator i$ = fields.iterator();

      while(i$.hasNext()) {
         FrameworkField frameworkField = (FrameworkField)i$.next();
         validFields.add(frameworkField.getField());
      }

      return validFields;
   }

   protected Collection<Field> getDataPointsFields(ParameterSignature sig) {
      List<FrameworkField> fields = this.clazz.getAnnotatedFields(DataPoints.class);
      Collection<Field> validFields = new ArrayList();
      Iterator i$ = fields.iterator();

      while(i$.hasNext()) {
         FrameworkField frameworkField = (FrameworkField)i$.next();
         validFields.add(frameworkField.getField());
      }

      return validFields;
   }

   protected Collection<FrameworkMethod> getSingleDataPointMethods(ParameterSignature sig) {
      return this.clazz.getAnnotatedMethods(DataPoint.class);
   }

   static class MethodParameterValue extends PotentialAssignment {
      private final FrameworkMethod method;

      private MethodParameterValue(FrameworkMethod dataPointMethod) {
         this.method = dataPointMethod;
      }

      public Object getValue() throws PotentialAssignment.CouldNotGenerateValueException {
         try {
            return this.method.invokeExplosively((Object)null);
         } catch (IllegalArgumentException var3) {
            throw new RuntimeException("unexpected: argument length is checked");
         } catch (IllegalAccessException var4) {
            throw new RuntimeException("unexpected: getMethods returned an inaccessible method");
         } catch (Throwable var5) {
            DataPoint annotation = (DataPoint)this.method.getAnnotation(DataPoint.class);
            Assume.assumeTrue(annotation == null || !AllMembersSupplier.isAssignableToAnyOf(annotation.ignoredExceptions(), var5));
            throw new PotentialAssignment.CouldNotGenerateValueException(var5);
         }
      }

      public String getDescription() throws PotentialAssignment.CouldNotGenerateValueException {
         return this.method.getName();
      }

      // $FF: synthetic method
      MethodParameterValue(FrameworkMethod x0, Object x1) {
         this(x0);
      }
   }
}
