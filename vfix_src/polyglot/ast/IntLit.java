package polyglot.ast;

import polyglot.util.Enum;

public interface IntLit extends NumLit {
   IntLit.Kind INT = new IntLit.Kind("int");
   IntLit.Kind LONG = new IntLit.Kind("long");

   long value();

   IntLit value(long var1);

   IntLit.Kind kind();

   IntLit kind(IntLit.Kind var1);

   boolean boundary();

   String positiveToString();

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
