package com.github.javaparser.symbolsolver.javaparsermodel.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedConstructorDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumConstantDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedEnumDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedParameterDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedArrayType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.resolution.types.parametrization.ResolvedTypeParametersMap;
import com.github.javaparser.symbolsolver.core.resolution.Context;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFactory;
import com.github.javaparser.symbolsolver.logic.AbstractTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.SymbolReference;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceTypeImpl;
import com.github.javaparser.symbolsolver.reflectionmodel.ReflectionFactory;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaParserEnumDeclaration extends AbstractTypeDeclaration implements ResolvedEnumDeclaration {
   private TypeSolver typeSolver;
   private EnumDeclaration wrappedNode;
   private JavaParserTypeAdapter<EnumDeclaration> javaParserTypeAdapter;

   public JavaParserEnumDeclaration(EnumDeclaration wrappedNode, TypeSolver typeSolver) {
      this.wrappedNode = wrappedNode;
      this.typeSolver = typeSolver;
      this.javaParserTypeAdapter = new JavaParserTypeAdapter(wrappedNode, typeSolver);
   }

   public String toString() {
      return "JavaParserEnumDeclaration{wrappedNode=" + this.wrappedNode + '}';
   }

   public Set<ResolvedMethodDeclaration> getDeclaredMethods() {
      Set<ResolvedMethodDeclaration> methods = new HashSet();
      Iterator var2 = this.wrappedNode.getMembers().iterator();

      while(var2.hasNext()) {
         BodyDeclaration<?> member = (BodyDeclaration)var2.next();
         if (member instanceof MethodDeclaration) {
            methods.add(new JavaParserMethodDeclaration((MethodDeclaration)member, this.typeSolver));
         }
      }

      return methods;
   }

   public Context getContext() {
      return JavaParserFactory.getContext(this.wrappedNode, this.typeSolver);
   }

   public String getName() {
      return this.wrappedNode.getName().getId();
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

   public boolean hasDirectlyAnnotation(String canonicalName) {
      return AstResolutionUtils.hasDirectlyAnnotation(this.wrappedNode, this.typeSolver, canonicalName);
   }

   public boolean canBeAssignedTo(ResolvedReferenceTypeDeclaration other) {
      String otherName = other.getQualifiedName();
      if (otherName.equals(this.getQualifiedName())) {
         return true;
      } else if (otherName.equals(Enum.class.getCanonicalName())) {
         return true;
      } else if (otherName.equals(Comparable.class.getCanonicalName())) {
         return true;
      } else if (otherName.equals(Serializable.class.getCanonicalName())) {
         return true;
      } else {
         return otherName.equals(Object.class.getCanonicalName());
      }
   }

   public boolean isClass() {
      return false;
   }

   public boolean isInterface() {
      return false;
   }

   public String getPackageName() {
      return this.javaParserTypeAdapter.getPackageName();
   }

   public String getClassName() {
      return this.javaParserTypeAdapter.getClassName();
   }

   public String getQualifiedName() {
      return this.javaParserTypeAdapter.getQualifiedName();
   }

   public boolean isAssignableBy(ResolvedReferenceTypeDeclaration other) {
      return this.javaParserTypeAdapter.isAssignableBy(other);
   }

   public boolean isAssignableBy(ResolvedType type) {
      return this.javaParserTypeAdapter.isAssignableBy(type);
   }

   public boolean isTypeParameter() {
      return false;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         JavaParserEnumDeclaration that = (JavaParserEnumDeclaration)o;
         return this.wrappedNode.equals(that.wrappedNode);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrappedNode.hashCode();
   }

   /** @deprecated */
   @Deprecated
   public Optional<MethodUsage> solveMethodAsUsage(String name, List<ResolvedType> parameterTypes, TypeSolver typeSolver, Context invokationContext, List<ResolvedType> typeParameterValues) {
      return name.equals("values") && parameterTypes.isEmpty() ? Optional.of((new JavaParserEnumDeclaration.ValuesMethod(this, typeSolver)).getUsage((Node)null)) : this.getContext().solveMethodAsUsage(name, parameterTypes, typeSolver);
   }

   public List<ResolvedFieldDeclaration> getAllFields() {
      List<ResolvedFieldDeclaration> fields = this.javaParserTypeAdapter.getFieldsForDeclaredVariables();
      this.getAncestors().forEach((a) -> {
         fields.addAll(a.getAllFieldsVisibleToInheritors());
      });
      this.wrappedNode.getMembers().stream().filter((m) -> {
         return m instanceof FieldDeclaration;
      }).forEach((m) -> {
         FieldDeclaration fd = (FieldDeclaration)m;
         fd.getVariables().forEach((v) -> {
            fields.add(new JavaParserFieldDeclaration(v, this.typeSolver));
         });
      });
      return fields;
   }

   public List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList) {
      List<ResolvedReferenceType> ancestors = new ArrayList();
      ResolvedReferenceType enumClass = ReflectionFactory.typeUsageFor(Enum.class, this.typeSolver).asReferenceType();
      ResolvedTypeParameterDeclaration eTypeParameter = (ResolvedTypeParameterDeclaration)enumClass.getTypeDeclaration().getTypeParameters().get(0);
      enumClass = enumClass.deriveTypeParameters((new ResolvedTypeParametersMap.Builder()).setValue(eTypeParameter, new ReferenceTypeImpl(this, this.typeSolver)).build());
      ancestors.add(enumClass);
      if (this.wrappedNode.getImplementedTypes() != null) {
         Iterator var5 = this.wrappedNode.getImplementedTypes().iterator();

         while(var5.hasNext()) {
            ClassOrInterfaceType implementedType = (ClassOrInterfaceType)var5.next();
            SymbolReference<ResolvedTypeDeclaration> implementedDeclRef = (new SymbolSolver(this.typeSolver)).solveTypeInType(this, implementedType.getName().getId());
            if (!implementedDeclRef.isSolved() && !acceptIncompleteList) {
               throw new UnsolvedSymbolException(implementedType.getName().getId());
            }

            ancestors.add(new ReferenceTypeImpl((ResolvedReferenceTypeDeclaration)implementedDeclRef.getCorrespondingDeclaration(), this.typeSolver));
         }
      }

      return ancestors;
   }

   public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
      return Collections.emptyList();
   }

   public EnumDeclaration getWrappedNode() {
      return this.wrappedNode;
   }

   public List<ResolvedEnumConstantDeclaration> getEnumConstants() {
      return (List)this.wrappedNode.getEntries().stream().map((entry) -> {
         return new JavaParserEnumConstantDeclaration(entry, this.typeSolver);
      }).collect(Collectors.toList());
   }

   public AccessSpecifier accessSpecifier() {
      throw new UnsupportedOperationException();
   }

   public Set<ResolvedReferenceTypeDeclaration> internalTypes() {
      Set<ResolvedReferenceTypeDeclaration> res = new HashSet();
      Iterator var2 = this.wrappedNode.getMembers().iterator();

      while(var2.hasNext()) {
         BodyDeclaration<?> member = (BodyDeclaration)var2.next();
         if (member instanceof TypeDeclaration) {
            res.add(JavaParserFacade.get(this.typeSolver).getTypeDeclaration((TypeDeclaration)member));
         }
      }

      return res;
   }

   public Optional<ResolvedReferenceTypeDeclaration> containerType() {
      return this.javaParserTypeAdapter.containerType();
   }

   public List<ResolvedConstructorDeclaration> getConstructors() {
      return AstResolutionUtils.getConstructors(this.wrappedNode, this.typeSolver, this);
   }

   public static class ValuesMethod implements ResolvedMethodDeclaration {
      private JavaParserEnumDeclaration enumDeclaration;
      private TypeSolver typeSolver;

      public ValuesMethod(JavaParserEnumDeclaration enumDeclaration, TypeSolver typeSolver) {
         this.enumDeclaration = enumDeclaration;
         this.typeSolver = typeSolver;
      }

      public ResolvedReferenceTypeDeclaration declaringType() {
         return this.enumDeclaration;
      }

      public ResolvedType getReturnType() {
         return new ResolvedArrayType(new ReferenceTypeImpl(this.enumDeclaration, this.typeSolver));
      }

      public int getNumberOfParams() {
         return 0;
      }

      public ResolvedParameterDeclaration getParam(int i) {
         throw new UnsupportedOperationException();
      }

      public MethodUsage getUsage(Node node) {
         throw new UnsupportedOperationException();
      }

      public MethodUsage resolveTypeVariables(Context context, List<ResolvedType> parameterTypes) {
         return new MethodUsage(this);
      }

      public boolean isAbstract() {
         throw new UnsupportedOperationException();
      }

      public boolean isDefaultMethod() {
         return false;
      }

      public boolean isStatic() {
         return false;
      }

      public String getName() {
         return "values";
      }

      public List<ResolvedTypeParameterDeclaration> getTypeParameters() {
         return Collections.emptyList();
      }

      public AccessSpecifier accessSpecifier() {
         return AstResolutionUtils.toAccessLevel(this.enumDeclaration.getWrappedNode().getModifiers());
      }

      public int getNumberOfSpecifiedExceptions() {
         return 0;
      }

      public ResolvedType getSpecifiedException(int index) {
         throw new UnsupportedOperationException("The values method of an enum does not throw any exception");
      }

      public Optional<MethodDeclaration> toAst() {
         return Optional.empty();
      }
   }
}
