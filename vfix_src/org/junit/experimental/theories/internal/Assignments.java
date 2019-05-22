package org.junit.experimental.theories.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.experimental.theories.ParameterSignature;
import org.junit.experimental.theories.ParameterSupplier;
import org.junit.experimental.theories.ParametersSuppliedBy;
import org.junit.experimental.theories.PotentialAssignment;
import org.junit.runners.model.TestClass;

public class Assignments {
   private final List<PotentialAssignment> assigned;
   private final List<ParameterSignature> unassigned;
   private final TestClass clazz;

   private Assignments(List<PotentialAssignment> assigned, List<ParameterSignature> unassigned, TestClass clazz) {
      this.unassigned = unassigned;
      this.assigned = assigned;
      this.clazz = clazz;
   }

   public static Assignments allUnassigned(Method testMethod, TestClass testClass) {
      List<ParameterSignature> signatures = ParameterSignature.signatures(testClass.getOnlyConstructor());
      signatures.addAll(ParameterSignature.signatures(testMethod));
      return new Assignments(new ArrayList(), signatures, testClass);
   }

   public boolean isComplete() {
      return this.unassigned.size() == 0;
   }

   public ParameterSignature nextUnassigned() {
      return (ParameterSignature)this.unassigned.get(0);
   }

   public Assignments assignNext(PotentialAssignment source) {
      List<PotentialAssignment> assigned = new ArrayList(this.assigned);
      assigned.add(source);
      return new Assignments(assigned, this.unassigned.subList(1, this.unassigned.size()), this.clazz);
   }

   public Object[] getActualValues(int start, int stop) throws PotentialAssignment.CouldNotGenerateValueException {
      Object[] values = new Object[stop - start];

      for(int i = start; i < stop; ++i) {
         values[i - start] = ((PotentialAssignment)this.assigned.get(i)).getValue();
      }

      return values;
   }

   public List<PotentialAssignment> potentialsForNextUnassigned() throws Throwable {
      ParameterSignature unassigned = this.nextUnassigned();
      List<PotentialAssignment> assignments = this.getSupplier(unassigned).getValueSources(unassigned);
      if (assignments.size() == 0) {
         assignments = this.generateAssignmentsFromTypeAlone(unassigned);
      }

      return assignments;
   }

   private List<PotentialAssignment> generateAssignmentsFromTypeAlone(ParameterSignature unassigned) {
      Class<?> paramType = unassigned.getType();
      if (paramType.isEnum()) {
         return (new EnumSupplier(paramType)).getValueSources(unassigned);
      } else {
         return !paramType.equals(Boolean.class) && !paramType.equals(Boolean.TYPE) ? Collections.emptyList() : (new BooleanSupplier()).getValueSources(unassigned);
      }
   }

   private ParameterSupplier getSupplier(ParameterSignature unassigned) throws Exception {
      ParametersSuppliedBy annotation = (ParametersSuppliedBy)unassigned.findDeepAnnotation(ParametersSuppliedBy.class);
      return (ParameterSupplier)(annotation != null ? this.buildParameterSupplierFromClass(annotation.value()) : new AllMembersSupplier(this.clazz));
   }

   private ParameterSupplier buildParameterSupplierFromClass(Class<? extends ParameterSupplier> cls) throws Exception {
      Constructor<?>[] supplierConstructors = cls.getConstructors();
      Constructor[] arr$ = supplierConstructors;
      int len$ = supplierConstructors.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Constructor<?> constructor = arr$[i$];
         Class<?>[] parameterTypes = constructor.getParameterTypes();
         if (parameterTypes.length == 1 && parameterTypes[0].equals(TestClass.class)) {
            return (ParameterSupplier)constructor.newInstance(this.clazz);
         }
      }

      return (ParameterSupplier)cls.newInstance();
   }

   public Object[] getConstructorArguments() throws PotentialAssignment.CouldNotGenerateValueException {
      return this.getActualValues(0, this.getConstructorParameterCount());
   }

   public Object[] getMethodArguments() throws PotentialAssignment.CouldNotGenerateValueException {
      return this.getActualValues(this.getConstructorParameterCount(), this.assigned.size());
   }

   public Object[] getAllArguments() throws PotentialAssignment.CouldNotGenerateValueException {
      return this.getActualValues(0, this.assigned.size());
   }

   private int getConstructorParameterCount() {
      List<ParameterSignature> signatures = ParameterSignature.signatures(this.clazz.getOnlyConstructor());
      int constructorParameterCount = signatures.size();
      return constructorParameterCount;
   }

   public Object[] getArgumentStrings(boolean nullsOk) throws PotentialAssignment.CouldNotGenerateValueException {
      Object[] values = new Object[this.assigned.size()];

      for(int i = 0; i < values.length; ++i) {
         values[i] = ((PotentialAssignment)this.assigned.get(i)).getDescription();
      }

      return values;
   }
}
