package com.github.javaparser.ast;

import java.util.EnumSet;
import java.util.Set;

public enum Modifier {
   PUBLIC,
   PROTECTED,
   PRIVATE,
   ABSTRACT,
   STATIC,
   FINAL,
   TRANSIENT,
   VOLATILE,
   SYNCHRONIZED,
   NATIVE,
   STRICTFP,
   TRANSITIVE,
   DEFAULT;

   final String codeRepresentation = this.name().toLowerCase();

   public String asString() {
      return this.codeRepresentation;
   }

   public EnumSet<Modifier> toEnumSet() {
      return EnumSet.of(this);
   }

   public static AccessSpecifier getAccessSpecifier(Set<Modifier> modifiers) {
      if (modifiers.contains(PUBLIC)) {
         return AccessSpecifier.PUBLIC;
      } else if (modifiers.contains(PROTECTED)) {
         return AccessSpecifier.PROTECTED;
      } else {
         return modifiers.contains(PRIVATE) ? AccessSpecifier.PRIVATE : AccessSpecifier.DEFAULT;
      }
   }
}
