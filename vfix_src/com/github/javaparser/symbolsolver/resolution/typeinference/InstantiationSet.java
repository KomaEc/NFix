package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.resolution.types.ResolvedType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InstantiationSet {
   private List<Instantiation> instantiations = new LinkedList();
   private static final InstantiationSet EMPTY = new InstantiationSet();

   public boolean allInferenceVariablesAreResolved(BoundSet boundSet) {
      throw new UnsupportedOperationException();
   }

   public static InstantiationSet empty() {
      return EMPTY;
   }

   private InstantiationSet() {
   }

   public InstantiationSet withInstantiation(Instantiation instantiation) {
      InstantiationSet newInstance = new InstantiationSet();
      newInstance.instantiations.addAll(this.instantiations);
      newInstance.instantiations.add(instantiation);
      return newInstance;
   }

   public boolean isEmpty() {
      return this.instantiations.isEmpty();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         InstantiationSet that = (InstantiationSet)o;
         return this.instantiations.equals(that.instantiations);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.instantiations.hashCode();
   }

   public String toString() {
      return "InstantiationSet{instantiations=" + this.instantiations + '}';
   }

   public ResolvedType apply(ResolvedType type) {
      Instantiation instantiation;
      for(Iterator var2 = this.instantiations.iterator(); var2.hasNext(); type = type.replaceTypeVariables(instantiation.getInferenceVariable().getTypeParameterDeclaration(), instantiation.getProperType())) {
         instantiation = (Instantiation)var2.next();
      }

      return type;
   }
}
