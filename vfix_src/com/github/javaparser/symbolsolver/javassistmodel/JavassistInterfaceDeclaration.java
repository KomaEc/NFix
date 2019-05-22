package com.github.javaparser.symbolsolver.javassistmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedInterfaceDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class JavassistInterfaceDeclaration extends AbstractTypeDeclaration implements ResolvedInterfaceDeclaration {
   private CtClass ctClass;
   private TypeSolver typeSolver;
   private JavassistTypeDeclarationAdapter javassistTypeDeclarationAdapter;

   public String toString() {
      return "JavassistInterfaceDeclaration{ctClass=" + this.ctClass.getName() + ", typeSolver=" + this.typeSolver + '}';
   }

   public JavassistInterfaceDeclaration(CtClass ctClass, TypeSolver typeSolver) {
      if (!ctClass.isInterface()) {
         throw new IllegalArgumentException("Not an interface: " + ctClass.getName());
      } else {
         this.ctClass = ctClass;
         this.typeSolver = typeSolver;
         this.javassistTypeDeclarationAdapter = new JavassistTypeDeclarationAdapter(ctClass, typeSolver);
      }
   }

   public List<ResolvedReferenceType> getInterfacesExtended() {
      return (List)Arrays.stream(this.ctClass.getClassFile().getInterfaces()).map((i) -> {
         return this.typeSolver.solveType(i);
      }).map((i) -> {
         return new ReferenceTypeImpl(i, this.typeSolver);
      }).collect(Collectors.toList());
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

   /** @deprecated */
   @Deprecated
   public Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext, List<ResolvedType> typeParameterValues) {
      return JavassistUtils.getMethodUsage(this.ctClass, name, argumentsTypes, typeSolver, invokationContext);
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

      int var8;
      for(var8 = 0; var8 < var7; ++var8) {
         CtMethod method = var6[var8];
         boolean isSynthetic = method.getMethodInfo().getAttribute("Synthetic") != null;
         boolean isNotBridge = (method.getMethodInfo().getAccessFlags() & 64) == 0;
         if (method.getName().equals(name) && !isSynthetic && isNotBridge && staticOnlyCheck.test(method)) {
            candidates.add(new JavassistMethodDeclaration(method, this.typeSolver));
         }
      }

      try {
         CtClass superClass = this.ctClass.getSuperclass();
         if (superClass != null) {
            SymbolReference<ResolvedMethodDeclaration> ref = (new JavassistClassDeclaration(superClass, this.typeSolver)).solveMethod(name, argumentsTypes, staticOnly);
            if (ref.isSolved()) {
               candidates.add(ref.getCorrespondingDeclaration());
            }
         }
      } catch (NotFoundException var13) {
         throw new RuntimeException(var13);
      }

      try {
         CtClass[] var15 = this.ctClass.getInterfaces();
         var7 = var15.length;

         for(var8 = 0; var8 < var7; ++var8) {
            CtClass interfaze = var15[var8];
            SymbolReference<ResolvedMethodDeclaration> ref = (new JavassistInterfaceDeclaration(interfaze, this.typeSolver)).solveMethod(name, argumentsTypes, staticOnly);
            if (ref.isSolved()) {
               candidates.add(ref.getCorrespondingDeclaration());
            }
         }
      } catch (NotFoundException var12) {
         throw new RuntimeException(var12);
      }

      return MethodResolutionLogic.findMostApplicable(candidates, name, argumentsTypes, this.typeSolver);
   }

   public boolean isAssignableBy(ResolvedType type) {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      return this.javassistTypeDeclarationAdapter.getDeclaredFields();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      throw new UnsupportedOperationException();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      ArrayList ancestors = new ArrayList();

      try {
         CtClass[] var3 = this.ctClass.getInterfaces();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CtClass interfaze = var3[var5];

            try {
               ResolvedReferenceType superInterfaze = JavassistFactory.typeUsageFor(interfaze, this.typeSolver).asReferenceType();
               ancestors.add(superInterfaze);
            } catch (UnsolvedSymbolException var8) {
               if (!acceptIncompleteList) {
                  throw var8;
               }
            }
         }
      } catch (NotFoundException var9) {
         throw new RuntimeException(var9);
      }

      List<ResolvedReferenceType> ancestors = (List)ancestors.stream().filter((a) -> {
         return a.getQualifiedName() != Object.class.getCanonicalName();
      }).collect(Collectors.toList());
      ancestors.add(new ReferenceTypeImpl(this.typeSolver.solveType(Object.class.getCanonicalName()), this.typeSolver));
      return ancestors;
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return (Set)Arrays.stream(this.ctClass.getDeclaredMethods()).map((m) -> {
         return new JavassistMethodDeclaration(m, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return this.ctClass.hasAnnotation(canonicalName);
   }

   public String getName() {
      String[] nameElements = this.ctClass.getSimpleName().replace('$', '.').split("\\.");
      return nameElements[nameElements.length - 1];
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.javassistTypeDeclarationAdapter.getTypeParameters();
   }

   public AccessSpecifier accessSpecifier() {
      return JavassistFactory.modifiersToAccessLevel(this.ctClass.getModifiers());
   }

   public ResolvedInterfaceDeclaration asInterface() {
      return this;
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      CtField[] var3 = this.ctClass.getDeclaredFields();
      int var4 = var3.length;

      int var5;
      for(var5 = 0; var5 < var4; ++var5) {
         CtField field = var3[var5];
         if (field.getName().equals(name)) {
            return SymbolReference.solved(new JavassistFieldDeclaration(field, typeSolver));
         }
      }

      String[] interfaceFQNs = this.getInterfaceFQNs();
      String[] var10 = interfaceFQNs;
      var5 = interfaceFQNs.length;

      for(int var11 = 0; var11 < var5; ++var11) {
         String interfaceFQN = var10[var11];
         SymbolReference<? extends ResolvedValueDeclaration> interfaceRef = this.solveSymbolForFQN(name, typeSolver, interfaceFQN);
         if (interfaceRef.isSolved()) {
            return interfaceRef;
         }
      }

      return SymbolReference.unsolved(ResolvedValueDeclaration.class);
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

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return Collections.emptyList();
   }

   public Optional<ClassOrInterfaceDeclaration> toAst() {
      return Optional.empty();
   }
}
