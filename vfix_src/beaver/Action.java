package beaver;

public abstract class Action {
   public static final Action NONE = new Action() {
      public Symbol reduce(Symbol[] args, int offset) {
         return new Symbol((Object)null);
      }
   };
   public static final Action RETURN = new Action() {
      public Symbol reduce(Symbol[] args, int offset) {
         return args[offset + 1];
      }
   };

   public abstract Symbol reduce(Symbol[] var1, int var2);
}
