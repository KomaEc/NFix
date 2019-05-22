package com.github.javaparser.symbolsolver.logic;

import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AbstractTypeDeclaration implements ResolvedReferenceTypeDeclaration {
   public final Set<MethodUsage> getAllMethods() {
      Set<MethodUsage> methods = new HashSet();
      Set<String> methodsSignatures = new HashSet();
      Iterator var3 = this.getDeclaredMethods().iterator();

      while(var3.hasNext()) {
         ResolvedMethodDeclaration methodDeclaration = (ResolvedMethodDeclaration)var3.next();
         methods.add(new MethodUsage(methodDeclaration));
         methodsSignatures.add(methodDeclaration.getSignature());
      }

      var3 = this.getAllAncestors().iterator();

      while(var3.hasNext()) {
         ResolvedReferenceType ancestor = (ResolvedReferenceType)var3.next();
         Iterator var5 = ancestor.getDeclaredMethods().iterator();

         while(var5.hasNext()) {
            MethodUsage mu = (MethodUsage)var5.next();
            String signature = mu.getDeclaration().getSignature();
            if (!methodsSignatures.contains(signature)) {
               methodsSignatures.add(signature);
               methods.add(mu);
            }
         }
      }

      return methods;
   }

   public final boolean isFunctionalInterface() {
      return FunctionalInterfaceLogic.getFunctionalMethod((ResolvedReferenceTypeDeclaration)this).isPresent();
   }
}
