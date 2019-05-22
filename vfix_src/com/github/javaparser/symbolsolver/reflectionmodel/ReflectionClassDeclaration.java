package com.github.javaparser.symbolsolver.reflectionmodel;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
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
import com.github.javaparser.symbolsolver.javaparsermodel.contexts.ContextHelper;
import com.github.javaparser.symbolsolver.logic.AbstractClassDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.comparators.MethodComparator;
import com.github.javaparser.symbolsolver.resolution.MethodResolutionLogic;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReflectionClassDeclaration extends AbstractClassDeclaration {
   private Class<?> clazz;
   private TypeSolver typeSolver;
   private ReflectionClassAdapter reflectionClassAdapter;

   public ReflectionClassDeclaration(Class<?> clazz, TypeSolver typeSolver) {
      if (clazz == null) {
         throw new IllegalArgumentException("Class should not be null");
      } else if (clazz.isInterface()) {
         throw new IllegalArgumentException("Class should not be an interface");
      } else if (clazz.isPrimitive()) {
         throw new IllegalArgumentException("Class should not represent a primitive class");
      } else if (clazz.isArray()) {
         throw new IllegalArgumentException("Class should not be an array");
      } else if (clazz.isEnum()) {
         throw new IllegalArgumentException("Class should not be an enum");
      } else {
         this.clazz = clazz;
         this.typeSolver = typeSolver;
         this.reflectionClassAdapter = new ReflectionClassAdapter(clazz, typeSolver, this);
      }
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      return this.reflectionClassAdapter.getDeclaredMethods();
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      return this.reflectionClassAdapter.getAncestors();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ReflectionClassDeclaration that = (ReflectionClassDeclaration)o;
         return this.clazz.getCanonicalName().equals(that.clazz.getCanonicalName());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.clazz.hashCode();
   }

   public String getPackageName() {
      return this.clazz.getPackage() != null ? this.clazz.getPackage().getName() : null;
   }

   public String getClassName() {
      String canonicalName = this.clazz.getCanonicalName();
      return canonicalName != null && this.getPackageName() != null ? canonicalName.substring(this.getPackageName().length() + 1, canonicalName.length()) : null;
   }

   public String getQualifiedName() {
      return this.clazz.getCanonicalName();
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<ResolvedMethodDeclaration> solveMethod(String name, List<ResolvedType> argumentsTypes, boolean staticOnly) {
      List<ResolvedMethodDeclaration> methods = new ArrayList();
      Predicate<Method> staticFilter = (m) -> {
         return !staticOnly || staticOnly && Modifier.isStatic(m.getModifiers());
      };
      Iterator var6 = ((List)Arrays.stream(this.clazz.getDeclaredMethods()).filter((m) -> {
         return m.getName().equals(name);
      }).filter(staticFilter).sorted(new MethodComparator()).collect(Collectors.toList())).iterator();

      while(var6.hasNext()) {
         Method method = (Method)var6.next();
         if (!method.isBridge() && !method.isSynthetic()) {
            ResolvedMethodDeclaration methodDeclaration = new ReflectionMethodDeclaration(method, this.typeSolver);
            methods.add(methodDeclaration);
         }
      }

      if (this.getSuperClass() != null) {
         ResolvedClassDeclaration superClass = (ResolvedClassDeclaration)this.getSuperClass().getTypeDeclaration();
         SymbolReference<ResolvedMethodDeclaration> ref = MethodResolutionLogic.solveMethodInType(superClass, name, argumentsTypes, staticOnly, this.typeSolver);
         if (ref.isSolved()) {
            methods.add(ref.getCorrespondingDeclaration());
         }
      }

      var6 = this.getInterfaces().iterator();

      while(var6.hasNext()) {
         ResolvedReferenceType interfaceDeclaration = (ResolvedReferenceType)var6.next();
         SymbolReference<ResolvedMethodDeclaration> ref = MethodResolutionLogic.solveMethodInType(interfaceDeclaration.getTypeDeclaration(), name, argumentsTypes, staticOnly, this.typeSolver);
         if (ref.isSolved()) {
            methods.add(ref.getCorrespondingDeclaration());
         }
      }

      if (methods.isEmpty()) {
         return SymbolReference.unsolved(ResolvedMethodDeclaration.class);
      } else {
         return MethodResolutionLogic.findMostApplicable(methods, name, argumentsTypes, this.typeSolver);
      }
   }

   public String toString() {
      return "ReflectionClassDeclaration{clazz=" + this.getId() + '}';
   }

   public ResolvedType getUsage(Node node) {
      return new ReferenceTypeImpl(this, this.typeSolver);
   }

   public Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> argumentsTypes, TypeSolver typeSolver, Context invokationContext, List<ResolvedType> typeParameterValues) {
      List<MethodUsage> methods = new ArrayList();
      Iterator var7 = ((List)Arrays.stream(this.clazz.getDeclaredMethods()).filter((m) -> {
         return m.getName().equals(name);
      }).sorted(new MethodComparator()).collect(Collectors.toList())).iterator();

      while(true) {
         Method method;
         do {
            do {
               if (!var7.hasNext()) {
                  if (this.getSuperClass() != null) {
                     ResolvedClassDeclaration superClass = (ResolvedClassDeclaration)this.getSuperClass().getTypeDeclaration();
                     Optional<MethodUsage> ref = ContextHelper.solveMethodAsUsage(superClass, name, argumentsTypes, typeSolver, invokationContext, typeParameterValues);
                     if (ref.isPresent()) {
                        methods.add(ref.get());
                     }
                  }

                  var7 = this.getInterfaces().iterator();

                  while(var7.hasNext()) {
                     ResolvedReferenceType interfaceDeclaration = (ResolvedReferenceType)var7.next();
                     Optional<MethodUsage> ref = ContextHelper.solveMethodAsUsage(interfaceDeclaration.getTypeDeclaration(), name, argumentsTypes, typeSolver, invokationContext, typeParameterValues);
                     if (ref.isPresent()) {
                        methods.add(ref.get());
                     }
                  }

                  Optional<MethodUsage> ref = MethodResolutionLogic.findMostApplicableUsage(methods, name, argumentsTypes, typeSolver);
                  return ref;
               }

               method = (Method)var7.next();
            } while(method.isBridge());
         } while(method.isSynthetic());

         ResolvedMethodDeclaration methodDeclaration = new ReflectionMethodDeclaration(method, typeSolver);
         MethodUsage methodUsage = new MethodUsage(methodDeclaration);

         for(int i = 0; i < this.getTypeParameters().size() && i < typeParameterValues.size(); ++i) {
            ResolvedTypeParameterDeclaration tpToReplace = (ResolvedTypeParameterDeclaration)this.getTypeParameters().get(i);
            ResolvedType newValue = (ResolvedType)typeParameterValues.get(i);
            methodUsage = methodUsage.replaceTypeParameter(tpToReplace, newValue);
         }

         methods.add(methodUsage);
      }
   }

   public boolean canBeAssignedTo(ResolvedReferenceTypeDeclaration other) {
      if (other instanceof LambdaArgumentTypePlaceholder) {
         return this.isFunctionalInterface();
      } else if (other.getQualifiedName().equals(this.getQualifiedName())) {
         return true;
      } else if (this.clazz.getSuperclass() != null && (new ReflectionClassDeclaration(this.clazz.getSuperclass(), this.typeSolver)).canBeAssignedTo(other)) {
         return true;
      } else {
         Class[] var2 = this.clazz.getInterfaces();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Class<?> interfaze = var2[var4];
            if ((new ReflectionInterfaceDeclaration(interfaze, this.typeSolver)).canBeAssignedTo(other)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isAssignableBy(ResolvedType type) {
      return this.reflectionClassAdapter.isAssignableBy(type);
   }

   public boolean isTypeParameter() {
      return false;
   }

   public ResolvedFieldDeclaration getField(String name) {
      return this.reflectionClassAdapter.getField(name);
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      return this.reflectionClassAdapter.getAllFields();
   }

   /** @deprecated */
   @Deprecated
   public SymbolReference<? extends ResolvedValueDeclaration> solveSymbol(String name, TypeSolver typeSolver) {
      Field[] var3 = this.clazz.getFields();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Field field = var3[var5];
         if (field.getName().equals(name)) {
            return SymbolReference.solved(new ReflectionFieldDeclaration(field, typeSolver));
         }
      }

      return SymbolReference.unsolved(ResolvedValueDeclaration.class);
   }

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return this.reflectionClassAdapter.hasDirectlyAnnotation(canonicalName);
   }

   public boolean hasField(String name) {
      return this.reflectionClassAdapter.hasField(name);
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.isAssignableBy((ResolvedType)(new ReferenceTypeImpl(other, this.typeSolver)));
   }

   public String getName() {
      return this.clazz.getSimpleName();
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
      return !this.clazz.isInterface();
   }

   public ReferenceTypeImpl getSuperClass() {
      return this.reflectionClassAdapter.getSuperClass();
   }

   public List<ResolvedReferenceType> getInterfaces() {
      return this.reflectionClassAdapter.getInterfaces();
   }

   public boolean isInterface() {
      return this.clazz.isInterface();
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return this.reflectionClassAdapter.getTypeParameters();
   }

   public AccessSpecifier accessSpecifier() {
      return ReflectionFactory.modifiersToAccessLevel(this.clazz.getModifiers());
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return this.reflectionClassAdapter.getConstructors();
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.reflectionClassAdapter.containerType();
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      return (Set)Arrays.stream(this.clazz.getDeclaredClasses()).map((ic) -> {
         return ReflectionFactory.typeDeclarationFor(ic, this.typeSolver);
      }).collect(Collectors.toSet());
   }

   public Optional<Node> toAst() {
      return Optional.empty();
   }

   protected ResolvedReferenceType object() {
      return new ReferenceTypeImpl(this.typeSolver.solveType(Object.class.getCanonicalName()), this.typeSolver);
   }
}
