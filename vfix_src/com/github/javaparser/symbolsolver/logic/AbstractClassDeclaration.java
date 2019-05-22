package com.github.javaparser.symbolsolver.logic;

import com.github.javaparser.resolution.declarations.ResolvedClassDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractClassDeclaration extends AbstractTypeDeclaration implements ResolvedClassDeclaration {
   public boolean hasName() {
      return this.getQualifiedName() != null;
   }

   public final List<ResolvedReferenceType> getAllSuperClasses() {
      List<ResolvedReferenceType> superclasses = new ArrayList();
      ResolvedReferenceType superClass = this.getSuperClass();
      if (superClass != null) {
         superclasses.add(superClass);
         superclasses.addAll(superClass.getAllClassesAncestors());
      }

      if (superclasses.removeIf((s) -> {
         return s.getQualifiedName().equals(Object.class.getCanonicalName());
      })) {
         superclasses.add(this.object());
      }

      return superclasses;
   }

   public final List<ResolvedReferenceType> getAllInterfaces() {
      List<ResolvedReferenceType> interfaces = new ArrayList();
      Iterator var2 = this.getInterfaces().iterator();

      while(var2.hasNext()) {
         ResolvedReferenceType interfaceDeclaration = (ResolvedReferenceType)var2.next();
         interfaces.add(interfaceDeclaration);
         interfaces.addAll(interfaceDeclaration.getAllInterfacesAncestors());
      }

      ResolvedReferenceType superClass = this.getSuperClass();
      if (superClass != null) {
         interfaces.addAll(superClass.getAllInterfacesAncestors());
      }

      return interfaces;
   }

   public final ResolvedClassDeclaration asClass() {
      return this;
   }

   protected abstract ResolvedReferenceType object();
}
