package com.github.javaparser.resolution.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ResolvedUnionType implements ResolvedType {
   private List<ResolvedType> elements;

   public ResolvedUnionType(List<ResolvedType> elements) {
      if (elements.size() < 2) {
         throw new IllegalArgumentException("An union type should have at least two elements. This has " + elements.size());
      } else {
         this.elements = new LinkedList(elements);
      }
   }

   public Optional<ResolvedReferenceType> getCommonAncestor() {
      Optional<List<ResolvedReferenceType>> reduce = this.elements.stream().map(ResolvedType::asReferenceType).map(ResolvedReferenceType::getAllAncestors).reduce((a, b) -> {
         ArrayList<ResolvedReferenceType> common = new ArrayList(a);
         common.retainAll(b);
         return common;
      });
      return ((List)reduce.orElse(new ArrayList())).stream().findFirst();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ResolvedUnionType that = (ResolvedUnionType)o;
         return (new HashSet(this.elements)).equals(new HashSet(that.elements));
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (new HashSet(this.elements)).hashCode();
   }

   public String describe() {
      return String.join(" | ", (Iterable)this.elements.stream().map(ResolvedType::describe).collect(Collectors.toList()));
   }

   public boolean isAssignableBy(ResolvedType other) {
      return this.elements.stream().allMatch((e) -> {
         return e.isAssignableBy(other);
      });
   }

   public boolean isUnionType() {
      return true;
   }

   public ResolvedUnionType asUnionType() {
      return this;
   }
}
