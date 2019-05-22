package com.github.javaparser.resolution;

public class UnsolvedSymbolException extends RuntimeException {
   private String name;
   private String context;
   private Throwable cause;

   public UnsolvedSymbolException(String name) {
      this(name, (String)null, (Throwable)null);
   }

   public UnsolvedSymbolException(String name, String context) {
      this(name, context, (Throwable)null);
   }

   public UnsolvedSymbolException(String name, Throwable cause) {
      this(name, (String)null, cause);
   }

   public UnsolvedSymbolException(String name, String context, Throwable cause) {
      super("Unsolved symbol" + (context != null ? " in " + context : "") + " : " + name, cause);
      this.name = name;
      this.context = context;
      this.cause = cause;
   }

   public String getName() {
      return this.name;
   }

   public String toString() {
      return "UnsolvedSymbolException{context='" + this.context + "', name='" + this.name + "', cause='" + this.cause + "'}";
   }
}
