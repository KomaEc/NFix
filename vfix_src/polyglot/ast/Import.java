package polyglot.ast;

import polyglot.util.Enum;

public interface Import extends Node {
   Import.Kind CLASS = new Import.Kind("class");
   Import.Kind PACKAGE = new Import.Kind("package");

   String name();

   Import name(String var1);

   Import.Kind kind();

   Import kind(Import.Kind var1);

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
