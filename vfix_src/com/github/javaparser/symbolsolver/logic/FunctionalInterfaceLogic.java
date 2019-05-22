package com.github.javaparser.symbolsolver.logic;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class FunctionalInterfaceLogic {
   private static List<String> OBJECT_METHODS_SIGNATURES = (List)Arrays.stream(Object.class.getDeclaredMethods()).map((method) -> {
      return getSignature(method);
   }).collect(Collectors.toList());

   private FunctionalInterfaceLogic() {
   }

   public static Optional<MethodUsage> getFunctionalMethod(ResolvedType type) {
      return type.isReferenceType() && type.asReferenceType().getTypeDeclaration().isInterface() ? getFunctionalMethod(type.asReferenceType().getTypeDeclaration()) : Optional.empty();
   }

   public static Optional<MethodUsage> getFunctionalMethod(ResolvedReferenceTypeDeclaration typeDeclaration) {
      Set<MethodUsage> methods = (Set)typeDeclaration.getAllMethods().stream().filter((m) -> {
         return m.getDeclaration().isAbstract();
      }).filter((m) -> {
         return !declaredOnObject(m);
      }).collect(Collectors.toSet());
      return methods.size() == 1 ? Optional.of(methods.iterator().next()) : Optional.empty();
   }

   public static boolean isFunctionalInterfaceType(ResolvedType type) {
      return getFunctionalMethod(type).isPresent();
   }

   private static String getSignature(Method m) {
      return String.format("%s(%s)", m.getName(), String.join(", ", (Iterable)Arrays.stream(m.getParameters()).map((p) -> {
         return toSignature(p);
      }).collect(Collectors.toList())));
   }

   private static String toSignature(Parameter p) {
      return p.getType().getCanonicalName();
   }

   private static boolean declaredOnObject(MethodUsage m) {
      return OBJECT_METHODS_SIGNATURES.contains(m.getDeclaration().getSignature());
   }
}
