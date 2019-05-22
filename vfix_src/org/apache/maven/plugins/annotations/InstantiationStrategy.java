package org.apache.maven.plugins.annotations;

public enum InstantiationStrategy {
   PER_LOOKUP("per-lookup"),
   SINGLETON("singleton"),
   KEEP_ALIVE("keep-alive"),
   POOLABLE("poolable");

   private final String id;

   private InstantiationStrategy(String id) {
      this.id = id;
   }

   public String id() {
      return this.id;
   }
}
