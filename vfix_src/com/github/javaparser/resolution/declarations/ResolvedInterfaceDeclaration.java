package com.github.javaparser.resolution.declarations;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface ResolvedInterfaceDeclaration extends ResolvedReferenceTypeDeclaration, ResolvedTypeParametrizable, HasAccessSpecifier, AssociableToAST<ClassOrInterfaceDeclaration> {
   default boolean isInterface() {
      return true;
   }

   List<ResolvedReferenceType> getInterfacesExtended();

   default List<ResolvedReferenceType> getAllInterfacesExtended() {
      List<ResolvedReferenceType> interfaces = new ArrayList();
      Iterator var2 = this.getInterfacesExtended().iterator();

      while(var2.hasNext()) {
         ResolvedReferenceType interfaceDeclaration = (ResolvedReferenceType)var2.next();
         interfaces.add(interfaceDeclaration);
         interfaces.addAll(interfaceDeclaration.getAllInterfacesAncestors());
      }

      return interfaces;
   }
}
