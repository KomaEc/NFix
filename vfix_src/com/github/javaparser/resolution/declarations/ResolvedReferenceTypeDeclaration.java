package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.AccessSpecifier;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface ResolvedReferenceTypeDeclaration extends ResolvedTypeDeclaration, ResolvedTypeParametrizable {
   default ResolvedReferenceTypeDeclaration asReferenceType() {
      return this;
   }

   default List<ResolvedReferenceType> getAncestors() {
      return this.getAncestors(false);
   }

   List<ResolvedReferenceType> getAncestors(boolean acceptIncompleteList);

   default List<ResolvedReferenceType> getAllAncestors() {
      List<ResolvedReferenceType> ancestors = new ArrayList();
      if (!Object.class.getCanonicalName().equals(this.getQualifiedName())) {
         Iterator var2 = this.getAncestors().iterator();

         while(var2.hasNext()) {
            ResolvedReferenceType ancestor = (ResolvedReferenceType)var2.next();
            ancestors.add(ancestor);
            Iterator var4 = ancestor.getAllAncestors().iterator();

            while(var4.hasNext()) {
               ResolvedReferenceType inheritedAncestor = (ResolvedReferenceType)var4.next();
               if (!ancestors.contains(inheritedAncestor)) {
                  ancestors.add(inheritedAncestor);
               }
            }
         }
      }

      return ancestors;
   }

   default ResolvedFieldDeclaration getField(String name) {
      Optional<ResolvedFieldDeclaration> field = this.getAllFields().stream().filter((f) -> {
         return f.getName().equals(name);
      }).findFirst();
      if (field.isPresent()) {
         return (ResolvedFieldDeclaration)field.get();
      } else {
         throw new UnsolvedSymbolException("Field not found: " + name);
      }
   }

   default ResolvedFieldDeclaration getVisibleField(String name) {
      Optional<ResolvedFieldDeclaration> field = this.getVisibleFields().stream().filter((f) -> {
         return f.getName().equals(name);
      }).findFirst();
      if (field.isPresent()) {
         return (ResolvedFieldDeclaration)field.get();
      } else {
         throw new IllegalArgumentException();
      }
   }

   default boolean hasField(String name) {
      return this.getAllFields().stream().filter((f) -> {
         return f.getName().equals(name);
      }).findFirst().isPresent();
   }

   default boolean hasVisibleField(String name) {
      return this.getVisibleFields().stream().filter((f) -> {
         return f.getName().equals(name);
      }).findFirst().isPresent();
   }

   List<ResolvedFieldDeclaration> getAllFields();

   default List<ResolvedFieldDeclaration> getVisibleFields() {
      return (List)this.getAllFields().stream().filter((f) -> {
         return f.declaringType().equals(this) || f.accessSpecifier() != AccessSpecifier.PRIVATE;
      }).collect(Collectors.toList());
   }

   default List<ResolvedFieldDeclaration> getAllNonStaticFields() {
      return (List)this.getAllFields().stream().filter((it) -> {
         return !it.isStatic();
      }).collect(Collectors.toList());
   }

   default List<ResolvedFieldDeclaration> getAllStaticFields() {
      return (List)this.getAllFields().stream().filter((it) -> {
         return it.isStatic();
      }).collect(Collectors.toList());
   }

   default List<ResolvedFieldDeclaration> getDeclaredFields() {
      return (List)this.getAllFields().stream().filter((it) -> {
         return it.declaringType().getQualifiedName().equals(this.getQualifiedName());
      }).collect(Collectors.toList());
   }

   Set<ResolvedMethodDeclaration> getDeclaredMethods();

   Set<MethodUsage> getAllMethods();

   boolean isAssignableBy(ResolvedType type);

   default boolean canBeAssignedTo(ResolvedReferenceTypeDeclaration other) {
      return other.isAssignableBy(this);
   }

   boolean isAssignableBy(ResolvedReferenceTypeDeclaration other);

   boolean hasDirectlyAnnotation(String qualifiedName);

   default boolean hasAnnotation(String qualifiedName) {
      return this.hasDirectlyAnnotation(qualifiedName) ? true : this.getAllAncestors().stream().anyMatch((it) -> {
         return it.asReferenceType().getTypeDeclaration().hasDirectlyAnnotation(qualifiedName);
      });
   }

   boolean isFunctionalInterface();

   default Optional<ResolvedTypeParameterDeclaration> findTypeParameter(String name) {
      Iterator var2 = this.getTypeParameters().iterator();

      ResolvedTypeParameterDeclaration tp;
      do {
         if (!var2.hasNext()) {
            if (this.containerType().isPresent()) {
               return ((ResolvedReferenceTypeDeclaration)this.containerType().get()).findTypeParameter(name);
            }

            return Optional.empty();
         }

         tp = (ResolvedTypeParameterDeclaration)var2.next();
      } while(!tp.getName().equals(name));

      return Optional.of(tp);
   }

   List<ResolvedConstructorDeclaration> getConstructors();
}
