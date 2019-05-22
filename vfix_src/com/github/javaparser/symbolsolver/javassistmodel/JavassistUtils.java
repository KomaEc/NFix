package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParametrizable;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.ResolvedTypeVariable;
import com.github.javaparser.resolution.types.ResolvedVoidType;
import com.github.javaparser.resolution.types.ResolvedWildcard;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.SignatureAttribute;

class JavassistUtils {
   static Optional<MethodUsage> getMethodUsage(CtClass ctClass, String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext) {
      CtMethod[] var5 = ctClass.getDeclaredMethods();
      int var6 = var5.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         CtMethod method = var5[var7];
         if (method.getName().equals(name)) {
            MethodUsage methodUsage = new MethodUsage(new JavassistMethodDeclaration(method, typeSolver));
            if (argumentsTypes.size() >= methodUsage.getNoParams()) {
               try {
                  if (method.getGenericSignature() != null) {
                     SignatureAttribute.MethodSignature methodSignature = SignatureAttribute.toMethodSignature(method.getGenericSignature());
                     List<ResolvedType> parametersOfReturnType = parseTypeParameters(methodSignature.getReturnType().toString(), typeSolver, invokationContext);
                     ResolvedType newReturnType = methodUsage.returnType();
                     if (newReturnType.isReferenceType() && parametersOfReturnType.size() > 0) {
                        newReturnType = newReturnType.asReferenceType().transformTypeParameters((tp) -> {
                           return (ResolvedType)parametersOfReturnType.remove(0);
                        });
                     }

                     methodUsage = methodUsage.replaceReturnType(newReturnType);
                  }

                  return Optional.of(methodUsage);
               } catch (BadBytecode var13) {
                  throw new RuntimeException(var13);
               }
            }
         }
      }

      try {
         CtClass superClass = ctClass.getSuperclass();
         if (superClass != null) {
            Optional<MethodUsage> ref = (new JavassistClassDeclaration(superClass, typeSolver)).solveMethodAsUsage(name, argumentsTypes, typeSolver, invokationContext, (List)null);
            if (ref.isPresent()) {
               return ref;
            }
         }
      } catch (NotFoundException var15) {
         throw new RuntimeException(var15);
      }

      try {
         CtClass[] var17 = ctClass.getInterfaces();
         var6 = var17.length;

         for(var7 = 0; var7 < var6; ++var7) {
            CtClass interfaze = var17[var7];
            Optional<MethodUsage> ref = (new JavassistInterfaceDeclaration(interfaze, typeSolver)).solveMethodAsUsage(name, argumentsTypes, typeSolver, invokationContext, (List)null);
            if (ref.isPresent()) {
               return ref;
            }
         }
      } catch (NotFoundException var14) {
         throw new RuntimeException(var14);
      }

      return Optional.empty();
   }

   private static List<ResolvedType> parseTypeParameters(String signature, TypeSolver typeSolver, Context invokationContext) {
      if (signature.contains("<")) {
         signature = signature.substring(signature.indexOf(60) + 1);
         if (!signature.endsWith(">")) {
            throw new IllegalArgumentException();
         } else {
            signature = signature.substring(0, signature.length() - 1);
            if (signature.contains(",")) {
               throw new UnsupportedOperationException();
            } else if (signature.startsWith("?")) {
               List<ResolvedType> types = new ArrayList();
               types.add(ResolvedWildcard.UNBOUNDED);
               return types;
            } else {
               List<ResolvedType> typeParameters = parseTypeParameters(signature, typeSolver, invokationContext);
               if (signature.contains("<")) {
                  signature = signature.substring(0, signature.indexOf(60));
               }

               if (signature.contains(">")) {
                  throw new UnsupportedOperationException();
               } else {
                  ResolvedType type = (new SymbolSolver(typeSolver)).solveTypeUsage(signature, invokationContext);
                  if (type.isReferenceType() && typeParameters.size() > 0) {
                     type = type.asReferenceType().transformTypeParameters((tp) -> {
                        return (ResolvedType)typeParameters.remove(0);
                     });
                  }

                  List<ResolvedType> types = new ArrayList();
                  types.add(type);
                  return types;
               }
            }
         }
      } else {
         return Collections.emptyList();
      }
   }

   static ResolvedType signatureTypeToType(SignatureAttribute.Type signatureType, TypeSolver typeSolver, ResolvedTypeParametrizable typeParametrizable) {
      if (signatureType instanceof SignatureAttribute.ClassType) {
         SignatureAttribute.ClassType classType = (SignatureAttribute.ClassType)signatureType;
         List<ResolvedType> typeArguments = classType.getTypeArguments() == null ? Collections.emptyList() : (List)Arrays.stream(classType.getTypeArguments()).map((ta) -> {
            return typeArgumentToType(ta, typeSolver, typeParametrizable);
         }).collect(Collectors.toList());
         String typeName = classType.getDeclaringClass() != null ? classType.getDeclaringClass().getName() + "." + classType.getName() : classType.getName();
         ResolvedReferenceTypeDeclaration typeDeclaration = typeSolver.solveType(removeTypeArguments(internalNameToCanonicalName(typeName)));
         return new ReferenceTypeImpl(typeDeclaration, typeArguments, typeSolver);
      } else if (signatureType instanceof SignatureAttribute.TypeVariable) {
         SignatureAttribute.TypeVariable typeVariableSignature = (SignatureAttribute.TypeVariable)signatureType;
         Optional<ResolvedTypeParameterDeclaration> typeParameterDeclarationOpt = typeParametrizable.findTypeParameter(typeVariableSignature.getName());
         if (!typeParameterDeclarationOpt.isPresent()) {
            throw new UnsolvedSymbolException("Unable to solve TypeVariable " + typeVariableSignature);
         } else {
            ResolvedTypeParameterDeclaration typeParameterDeclaration = (ResolvedTypeParameterDeclaration)typeParameterDeclarationOpt.get();
            return new ResolvedTypeVariable(typeParameterDeclaration);
         }
      } else if (signatureType instanceof SignatureAttribute.ArrayType) {
         SignatureAttribute.ArrayType arrayType = (SignatureAttribute.ArrayType)signatureType;
         return new ResolvedArrayType(signatureTypeToType(arrayType.getComponentType(), typeSolver, typeParametrizable));
      } else if (signatureType instanceof SignatureAttribute.BaseType) {
         SignatureAttribute.BaseType baseType = (SignatureAttribute.BaseType)signatureType;
         return baseType.toString().equals("void") ? ResolvedVoidType.INSTANCE : ResolvedPrimitiveType.byName(baseType.toString());
      } else {
         throw new RuntimeException(signatureType.getClass().getCanonicalName());
      }
   }

   private static String removeTypeArguments(String typeName) {
      return typeName.contains("<") ? typeName.substring(0, typeName.indexOf(60)) : typeName;
   }

   static String internalNameToCanonicalName(String typeName) {
      return typeName.replaceAll("\\$", ".");
   }

   private static ResolvedType objectTypeArgumentToType(SignatureAttribute.ObjectType typeArgument, TypeSolver typeSolver, ResolvedTypeParametrizable typeParametrizable) {
      if (typeArgument instanceof SignatureAttribute.ArrayType) {
         return signatureTypeToType(((SignatureAttribute.ArrayType)typeArgument).getComponentType(), typeSolver, typeParametrizable);
      } else {
         String typeName = typeArgument.jvmTypeName();
         return getGenericParameterByName(typeName, typeParametrizable, typeSolver);
      }
   }

   private static ResolvedType getGenericParameterByName(String typeName, ResolvedTypeParametrizable typeParametrizable, TypeSolver typeSolver) {
      Optional<ResolvedType> type = typeParametrizable.findTypeParameter(typeName).map(ResolvedTypeVariable::new);
      return (ResolvedType)type.orElseGet(() -> {
         return new ReferenceTypeImpl(typeSolver.solveType(removeTypeArguments(internalNameToCanonicalName(typeName))), typeSolver);
      });
   }

   private static ResolvedType typeArgumentToType(SignatureAttribute.TypeArgument typeArgument, TypeSolver typeSolver, ResolvedTypeParametrizable typeParametrizable) {
      if (typeArgument.isWildcard()) {
         if (typeArgument.getType() == null) {
            return ResolvedWildcard.UNBOUNDED;
         } else if (typeArgument.getKind() == '+') {
            return ResolvedWildcard.extendsBound(objectTypeArgumentToType(typeArgument.getType(), typeSolver, typeParametrizable));
         } else if (typeArgument.getKind() == '-') {
            return ResolvedWildcard.superBound(objectTypeArgumentToType(typeArgument.getType(), typeSolver, typeParametrizable));
         } else {
            throw new UnsupportedOperationException();
         }
      } else {
         return objectTypeArgumentToType(typeArgument.getType(), typeSolver, typeParametrizable);
      }
   }

   static Optional<String> extractParameterName(CtBehavior method, int paramNumber) {
      MethodInfo methodInfo = method.getMethodInfo();
      CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
      if (codeAttribute != null) {
         LocalVariableAttribute attr = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
         if (attr != null) {
            int pos = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
            return Optional.ofNullable(attr.variableName(paramNumber + pos));
         }
      }

      return Optional.empty();
   }
}
