package com.github.javaparser.ast;

public enum AccessSpecifier {
   PUBLIC("public"),
   PRIVATE("private"),
   PROTECTED("protected"),
   DEFAULT("");

   private String codeRepresenation;

   private AccessSpecifier(String codeRepresentation) {
      this.codeRepresenation = codeRepresentation;
   }

   public String asString() {
      return this.codeRepresenation;
   }
}
