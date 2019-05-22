package com.github.javaparser.symbolsolver.model.resolution;

import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import com.github.javaparser.resolution.types.ResolvedType;

public class Value {
   private ResolvedType type;
   private String name;

   public Value(ResolvedType type, String name) {
      this.type = type;
      this.name = name;
   }

   public static Value from(ResolvedValueDeclaration decl) {
      ResolvedType type = decl.getType();
      return new Value(type, decl.getName());
   }

   public String toString() {
      return "Value{typeUsage=" + this.type + ", name='" + this.name + '\'' + '}';
   }

   public String getName() {
      return this.name;
   }

   public ResolvedType getType() {
      return this.type;
   }
}
