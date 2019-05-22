package org.junit.experimental.theories.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class SpecificDataPointsSupplier extends AllMembersSupplier {
   public SpecificDataPointsSupplier(TestClass testClass) {
      super(testClass);
   }

   protected Collection<Field> getSingleDataPointFields(ParameterSignature sig) {
      Collection<Field> fields = super.getSingleDataPointFields(sig);
      String requestedName = ((FromDataPoints)sig.getAnnotation(FromDataPoints.class)).value();
      List<Field> fieldsWithMatchingNames = new ArrayList();
      Iterator i$ = fields.iterator();

      while(i$.hasNext()) {
         Field field = (Field)i$.next();
         String[] fieldNames = ((DataPoint)field.getAnnotation(DataPoint.class)).value();
         if (Arrays.asList(fieldNames).contains(requestedName)) {
            fieldsWithMatchingNames.add(field);
         }
      }

      return fieldsWithMatchingNames;
   }

   protected Collection<Field> getDataPointsFields(ParameterSignature sig) {
      Collection<Field> fields = super.getDataPointsFields(sig);
      String requestedName = ((FromDataPoints)sig.getAnnotation(FromDataPoints.class)).value();
      List<Field> fieldsWithMatchingNames = new ArrayList();
      Iterator i$ = fields.iterator();

      while(i$.hasNext()) {
         Field field = (Field)i$.next();
         String[] fieldNames = ((DataPoints)field.getAnnotation(DataPoints.class)).value();
         if (Arrays.asList(fieldNames).contains(requestedName)) {
            fieldsWithMatchingNames.add(field);
         }
      }

      return fieldsWithMatchingNames;
   }

   protected Collection<FrameworkMethod> getSingleDataPointMethods(ParameterSignature sig) {
      Collection<FrameworkMethod> methods = super.getSingleDataPointMethods(sig);
      String requestedName = ((FromDataPoints)sig.getAnnotation(FromDataPoints.class)).value();
      List<FrameworkMethod> methodsWithMatchingNames = new ArrayList();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         FrameworkMethod method = (FrameworkMethod)i$.next();
         String[] methodNames = ((DataPoint)method.getAnnotation(DataPoint.class)).value();
         if (Arrays.asList(methodNames).contains(requestedName)) {
            methodsWithMatchingNames.add(method);
         }
      }

      return methodsWithMatchingNames;
   }

   protected Collection<FrameworkMethod> getDataPointsMethods(ParameterSignature sig) {
      Collection<FrameworkMethod> methods = super.getDataPointsMethods(sig);
      String requestedName = ((FromDataPoints)sig.getAnnotation(FromDataPoints.class)).value();
      List<FrameworkMethod> methodsWithMatchingNames = new ArrayList();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         FrameworkMethod method = (FrameworkMethod)i$.next();
         String[] methodNames = ((DataPoints)method.getAnnotation(DataPoints.class)).value();
         if (Arrays.asList(methodNames).contains(requestedName)) {
            methodsWithMatchingNames.add(method);
         }
      }

      return methodsWithMatchingNames;
   }
}
