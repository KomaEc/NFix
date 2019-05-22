package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InferenceVariable implements ResolvedType {
   private static int unnamedInstantiated = 0;
   private String name;
   private ResolvedTypeParameterDeclaration typeParameterDeclaration;

   public static List<InferenceVariable> instantiate(List<ResolvedTypeParameterDeclaration> typeParameterDeclarations) {
      List<InferenceVariable> inferenceVariables = new LinkedList();
      Iterator var2 = typeParameterDeclarations.iterator();

      while(var2.hasNext()) {
         ResolvedTypeParameterDeclaration tp = (ResolvedTypeParameterDeclaration)var2.next();
         inferenceVariables.add(unnamed(tp));
      }

      return inferenceVariables;
   }

   public static InferenceVariable unnamed(ResolvedTypeParameterDeclaration typeParameterDeclaration) {
      return new InferenceVariable("__unnamed__" + unnamedInstantiated++, typeParameterDeclaration);
   }

   public InferenceVariable(String name, ResolvedTypeParameterDeclaration typeParameterDeclaration) {
      this.name = name;
      this.typeParameterDeclaration = typeParameterDeclaration;
   }

   public String describe() {
      return this.name;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         InferenceVariable that = (InferenceVariable)o;
         if (!this.name.equals(that.name)) {
            return false;
         } else {
            return this.typeParameterDeclaration != null ? this.typeParameterDeclaration.equals(that.typeParameterDeclaration) : that.typeParameterDeclaration == null;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.name.hashCode();
      result = 31 * result + (this.typeParameterDeclaration != null ? this.typeParameterDeclaration.hashCode() : 0);
      return result;
   }

   public boolean isAssignableBy(ResolvedType other) {
      if (other.equals(this)) {
         return true;
      } else {
         throw new UnsupportedOperationException("We are unable to determine the assignability of an inference variable without knowing the bounds and constraints");
      }
   }

   public ResolvedTypeParameterDeclaration getTypeParameterDeclaration() {
      if (this.typeParameterDeclaration == null) {
         throw new IllegalStateException("The type parameter declaration was not specified");
      } else {
         return this.typeParameterDeclaration;
      }
   }

   public String toString() {
      return "InferenceVariable{name='" + this.name + '\'' + ", typeParameterDeclaration=" + this.typeParameterDeclaration + '}';
   }

   public boolean mention(List<ResolvedTypeParameterDeclaration> typeParameters) {
      return false;
   }
}
