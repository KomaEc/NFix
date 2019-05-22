package org.junit.runner;

public final class FilterFactoryParams {
   private final Description topLevelDescription;
   private final String args;

   public FilterFactoryParams(Description topLevelDescription, String args) {
      if (args != null && topLevelDescription != null) {
         this.topLevelDescription = topLevelDescription;
         this.args = args;
      } else {
         throw new NullPointerException();
      }
   }

   public String getArgs() {
      return this.args;
   }

   public Description getTopLevelDescription() {
      return this.topLevelDescription;
   }
}
