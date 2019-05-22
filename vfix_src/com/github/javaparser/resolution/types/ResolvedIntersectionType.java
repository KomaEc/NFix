package com.github.javaparser.resolution.types;

import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResolvedIntersectionType implements ResolvedType {
   private List<ResolvedType> elements;

   public ResolvedIntersectionType(Collection<ResolvedType> elements) {
      if (elements.size() < 2) {
         throw new IllegalArgumentException("An intersection type should have at least two elements. This has " + elements.size());
      } else {
         this.elements = new LinkedList(elements);
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ResolvedIntersectionType that = (ResolvedIntersectionType)o;
         return (new HashSet(this.elements)).equals(new HashSet(that.elements));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (new HashSet(this.elements)).hashCode();
   }

   public String describe() {
      return String.join(" & ", (Iterable)this.elements.stream().map(ResolvedType::describe).collect(Collectors.toList()));
   }

   public boolean isAssignableBy(ResolvedType other) {
      return this.elements.stream().allMatch((e) -> {
         return e.isAssignableBy(other);
      });
   }

   public ResolvedType replaceTypeVariables(ResolvedTypeParameterDeclaration tp, ResolvedType replaced, Map<ResolvedTypeParameterDeclaration, ResolvedType> inferredTypes) {
      List<ResolvedType> elementsReplaced = (List)this.elements.stream().map((e) -> {
         return e.replaceTypeVariables(tp, replaced, inferredTypes);
      }).collect(Collectors.toList());
      return elementsReplaced.equals(this.elements) ? this : new ResolvedIntersectionType(elementsReplaced);
   }
}
