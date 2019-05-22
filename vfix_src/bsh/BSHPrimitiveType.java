package bsh;

class BSHPrimitiveType extends SimpleNode {
   public Class type;

   BSHPrimitiveType(int var1) {
      super(var1);
   }

   public Class getType() {
      return this.type;
   }
}
