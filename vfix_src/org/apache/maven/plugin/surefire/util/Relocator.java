package org.apache.maven.plugin.surefire.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Relocator {
   @Nullable
   private final String relocation;
   private static final String relocationBase = "org.apache.maven.surefire.";

   public Relocator(@Nullable String relocation) {
      this.relocation = relocation;
   }

   public Relocator() {
      this.relocation = "shadefire";
   }

   @Nullable
   private String getRelocation() {
      return this.relocation;
   }

   @Nonnull
   public String relocate(@Nonnull String className) {
      if (this.relocation == null) {
         return className;
      } else if (className.contains(this.relocation)) {
         return className;
      } else {
         String rest = className.substring("org.apache.maven.surefire.".length());
         String s = "org.apache.maven.surefire." + this.getRelocation() + ".";
         return s + rest;
      }
   }
}
