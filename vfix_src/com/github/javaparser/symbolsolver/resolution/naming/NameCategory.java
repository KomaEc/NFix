package com.github.javaparser.symbolsolver.resolution.naming;

public enum NameCategory {
   MODULE_NAME(false),
   PACKAGE_NAME(false),
   TYPE_NAME(false),
   EXPRESSION_NAME(false),
   METHOD_NAME(false),
   PACKAGE_OR_TYPE_NAME(true),
   AMBIGUOUS_NAME(true),
   COMPILATION_ERROR(false);

   private boolean needDisambiguation;

   private NameCategory(boolean needDisambiguation) {
      this.needDisambiguation = needDisambiguation;
   }

   public boolean isNeedingDisambiguation() {
      return this.needDisambiguation;
   }

   public boolean isNameAcceptable(String name) {
      return this != TYPE_NAME || !name.equals("var");
   }

   public boolean isValid() {
      return this != COMPILATION_ERROR;
   }
}
