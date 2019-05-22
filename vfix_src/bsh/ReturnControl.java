package bsh;

class ReturnControl implements ParserConstants {
   public int kind;
   public Object value;
   public SimpleNode returnPoint;

   public ReturnControl(int var1, Object var2, SimpleNode var3) {
      this.kind = var1;
      this.value = var2;
      this.returnPoint = var3;
   }
}
