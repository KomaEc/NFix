package polyglot.types;

import polyglot.util.Position;

public class NoMemberException extends SemanticException {
   private int kind;
   public static final int METHOD = 1;
   public static final int CONSTRUCTOR = 2;
   public static final int FIELD = 3;

   public NoMemberException(int kind, String s) {
      super(s);
      this.kind = kind;
   }

   public NoMemberException(int kind, String s, Position position) {
      super(s, position);
      this.kind = kind;
   }

   public int getKind() {
      return this.kind;
   }

   public String getKindStr() {
      switch(this.kind) {
      case 1:
         return "method";
      case 2:
         return "constructor";
      case 3:
         return "field";
      default:
         return "unknown!!!";
      }
   }
}
