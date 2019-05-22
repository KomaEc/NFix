package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.LambdaArgumentTypePlaceholder;
import com.github.javaparser.symbolsolver.logic.AbstractClassDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;

public class JavassistClassDeclaration extends AbstractClassDeclaration {
   private CtClass ctClass;
   private TypeSolver typeSolver;
   private JavassistTypeDeclarationAdapter javassistTypeDeclarationAdapter;

   public JavassistClassDeclaration(CtClass ctClass, TypeSolver typeSolver) {
      if (ctClass == null) {
         throw new IllegalArgumentException();
      } else if (!ctClass.isInterface() && !ctClass.isAnnotation() && !ctClass.isPrimitive() && !ctClass.isEnum()) {
         this.ctClass = ctClass;
         this.typeSolver = typeSolver;
         this.javassistTypeDeclarationAdapter = new JavassistTypeDeclarationAdapter(ctClass, typeSolver);
      } else {
         throw new IllegalArgumentException("Trying to instantiate a JavassistClassDeclaration with something which is not a class: " + ctClass.toString());
      }
   }

   protected ResolvedReferenceType object() {
      return new ReferenceTypeImpl(this.typeSolver.solveType(Object.class.getCanonicalName()), this.typeSolver);
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return this.ctClass.hasAnnotation(canonicalName);
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return this.javassistTypeDeclarationAdapter.getDeclaredMethods();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.isAssignableBy((ResolvedType)(new ReferenceTypeImpl(other, this.typeSolver)));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavassistClassDeclaration that = (JavassistClassDeclaration)o;
         return this.ctClass.equals(that.ctClass);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.ctClass.hashCode();
   }

   public String getPackageName() {
      return this.ctClass.getPackageName();
   }

   public String getClassName() {
      String className = this.ctClass.getName().replace('$', '.');
      return this.getPackageName() != null ? className.substring(this.getPackageName().length() + 1, className.length()) : className;
   }

   public String getQualifiedName() {
      return this.ctClass.getName().replace('$', '.');
   }

   public Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext, List<ResolvedType> typeParameterValues) {
      return JavassistUtils.getMethodUsage(this.ctClass, name, argumentsTypes, typeSolver, invokationContext);
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      CtField[] var3 = this.ctClass.getDeclaredFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         CtField field = var3[var5];
         if (field.getName().equals(name)) {
            return SymbolReference.solved(new JavassistFieldDeclaration(field, typeSolver));
         }
      }

      String superclassFQN = this.getSuperclassFQN();
      SymbolReference<? extends ResolvedValueDeclaration> ref = this.solveSymbolForFQN(name, typeSolver, superclassFQN);
      if (ref.isSolved()) {
         return ref;
      } else {
         String[] interfaceFQNs = this.getInterfaceFQNs();
         String[] var14 = interfaceFQNs;
         int var7 = interfaceFQNs.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String interfaceFQN = var14[var8];
            SymbolReference<? extends ResolvedValueDeclaration> interfaceRef = this.solveSymbolForFQN(name, typeSolver, interfaceFQN);
            if (interfaceRef.isSolved()) {
               return interfaceRef;
            }
         }

         return SymbolReference.unsolved(ResolvedValueDeclaration.class);
      }
   }

   private SymbolReference<? extends ResolvedValueDeclaration> solveSymbolForFQN(String symbolName, TypeSolver typeSolver, String fqn) {
      if (fqn == null) {
         return SymbolReference.unsolved(ResolvedValueDeclaration.class);
      } else {
         ResolvedReferenceTypeDeclaration fqnTypeDeclaration = typeSolver.solveType(fqn);
         return (new SymbolSolver(typeSolver)).solveSymbolInType(fqnTypeDeclaration, symbolName);
      }
   }

   private String[] getInterfaceFQNs() {
      return this.ctClass.getClassFile().getInterfaces();
   }

   private String getSuperclassFQN() {
      return this.ctClass.getClassFile().getSuperclass();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      ArrayList ancestors = new ArrayList();

      try {
         ResolvedReferenceType superClass = this.getSuperClass();
         if (superClass != null) {
            ancestors.add(superClass);
         }
      } catch (UnsolvedSymbolException var5) {
         if (!acceptIncompleteList) {
            throw var5;
         }
      }

      try {
         ancestors.addAll(this.getInterfaces());
      } catch (UnsolvedSymbolException var4) {
         if (!acceptIncompleteList) {
            throw var4;
         }
      }

      return ancestors;
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly) {
      List<ResolvedMethodDeclaration> candidates = new ArrayList();
      Predicate<CtMethod> staticOnlyCheck = (m) -> {
         return !staticOnly || staticOnly && Modifier.isStatic(m.getModifiers());
      };
      CtMethod[] var6 = this.ctClass.getDeclaredMethods();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         CtMethod method = var6[var8];
         boolean isSynthetic = method.getMethodInfo().getAttribute("Synthetic") != null;
         boolean isNotBridge = (method.getMethodInfo().getAccessFlags() & 64) == 0;
         if (method.getName().equals(name) && !isSynthetic && isNotBridge && staticOnlyCheck.test(method)) {
            candidates.add(new JavassistMethodDeclaration(method, this.typeSolver));
         }
      }

      SymbolReference<ResolvedMethodDeclaration> superClassMethodRef = MethodResolutionLogic.solveMethodInType(this.getSuperClass().getTypeDeclaration(), name, argumentsTypes, staticOnly, this.typeSolver);
      if (superClassMethodRef.isSolved()) {
         candidates.add(superClassMethodRef.getCorrespondingDeclaration());
      }

      Iterator var13 = this.getInterfaces().iterator();

      while(var13.hasNext()) {
         ResolvedReferenceType interfaceRef = (ResolvedReferenceType)var13.next();
         SymbolReference<ResolvedMethodDeclaration> interfaceMethodRef = MethodResolutionLogic.solveMethodInType(interfaceRef.getTypeDeclaration(), name, argumentsTypes, staticOnly, this.typeSolver);
         if (interfaceMethodRef.isSolved()) {
            candidates.add(interfaceMethodRef.getCorrespondingDeclaration());
         }
      }

      return MethodResolutionLogic.findMostApplicable(candidates, name, argumentsTypes, this.typeSolver);
   }

   public ResolvedType getUsage(Node node) {
      return new ReferenceTypeImpl(this, this.typeSolver);
   }

   public boolean isAssignableBy(ResolvedType type) {
      if (type.isNull()) {
         return true;
      } else if (type instanceof LambdaArgumentTypePlaceholder) {
         return this.isFunctionalInterface();
      } else if (type.describe().equals(this.getQualifiedName())) {
         return true;
      } else {
         try {
            if (this.ctClass.getSuperclass() != null && (new JavassistClassDeclaration(this.ctClass.getSuperclass(), this.typeSolver)).isAssignableBy(type)) {
               return true;
            } else {
               CtClass[] var2 = this.ctClass.getInterfaces();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  CtClass interfaze = var2[var4];
                  if ((new JavassistInterfaceDeclaration(interfaze, this.typeSolver)).isAssignableBy(type)) {
                     return true;
                  }
               }

               return false;
            }
         } catch (NotFoundException var6) {
            throw new RuntimeException(var6);
         }
      }
   }

   public boolean isTypeParameter() {
      return false;
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      return this.javassistTypeDeclarationAdapter.getDeclaredFields();
   }

   public String getName() {
      String[] nameElements = this.ctClass.getSimpleName().replace('$', '.').split("\\.");
      return nameElements[nameElements.length - 1];
   }

   public boolean isField() {
      return false;
   }

   public boolean isParameter() {
      return false;
   }

   public boolean isType() {
      return true;
   }

   public boolean isClass() {
      return !this.ctClass.isInterface();
   }

   public ResolvedReferenceType getSuperClass() {
      try {
         if (this.ctClass.getClassFile().getSuperclass() == null) {
            return new ReferenceTypeImpl(this.typeSolver.solveType(Object.class.getCanonicalName()), this.typeSolver);
         } else if (this.ctClass.getGenericSignature() == null) {
            return new ReferenceTypeImpl(this.typeSolver.solveType(JavassistUtils.internalNameToCanonicalName(this.ctClass.getClassFile().getSuperclass())), this.typeSolver);
         } else {
            SignatureAttribute.ClassSignature classSignature = SignatureAttribute.toClassSignature(this.ctClass.getGenericSignature());
            return JavassistUtils.signatureTypeToType(classSignature.getSuperClass(), this.typeSolver, this).asReferenceType();
         }
      } catch (BadBytecode var2) {
         throw new RuntimeException(var2);
      }
   }

   public List<ResolvedReferenceType> getInterfaces() {
      try {
         if (this.ctClass.getGenericSignature() == null) {
            return (List)Arrays.stream(this.ctClass.getClassFile().getInterfaces()).map((i) -> {
               return this.typeSolver.solveType(JavassistUtils.internalNameToCanonicalName(i));
            }).map((i) -> {
               return new ReferenceTypeImpl(i, this.typeSolver);
            }).collect(Collectors.toList());
         } else {
            SignatureAttribute.ClassSignature classSignature = SignatureAttribute.toClassSignature(this.ctClass.getGenericSignature());
            return (List)Arrays.stream(classSignature.getInterfaces()).map((i) -> {
               return JavassistUtils.signatureTypeToType(i, this.typeSolver, this).asReferenceType();
            }).collect(Collectors.toList());
         }
      } catch (BadBytecode var2) {
         throw new RuntimeException(var2);
      }
   }

   public boolean isInterface() {
      return this.ctClass.isInterface();
   }

   public String toString() {
      return "JavassistClassDeclaration {" + this.ctClass.getName() + '}';
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.javassistTypeDeclarationAdapter.getTypeParameters();
   }

   public AccessSpecifier accessSpecifier() {
      return JavassistFactory.modifiersToAccessLevel(this.ctClass.getModifiers());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return this.javassistTypeDeclarationAdapter.getConstructors();
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.javassistTypeDeclarationAdapter.containerType();
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      try {
         return (Set)Arrays.stream(this.ctClass.getDeclaredClasses()).map((itype) -> {
            return JavassistFactory.toTypeDeclaration(itype, this.typeSolver);
         }).collect(Collectors.toSet());
      } catch (NotFoundException var2) {
         throw new RuntimeException(var2);
      }
   }

   public ResolvedReferenceTypeDeclaration getInternalType(String name) {
      Optional<ResolvedReferenceTypeDeclaration> type = this.internalTypes().stream().filter((f) -> {
         return f.getName().endsWith(name);
      }).findFirst();
      return (ResolvedReferenceTypeDeclaration)type.orElseThrow(() -> {
         return new UnsolvedSymbolException("Internal type not found: " + name);
      });
   }

   public boolean hasInternalType(String name) {
      return this.internalTypes().stream().anyMatch((f) -> {
         return f.getName().endsWith(name);
      });
   }

   public Optional<Node> toAst() {
      return Optional.empty();
   }
}
