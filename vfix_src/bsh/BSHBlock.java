package bsh;

class BSHBlock extends SimpleNode {
   public boolean isSynchronized = false;

   BSHBlock(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      return this.eval(var1, var2, false);
   }

   public Object eval(CallStack var1, Interpreter var2, boolean var3) throws EvalError {
      Object var4 = null;
      if (this.isSynchronized) {
         SimpleNode var5 = (SimpleNode)this.jjtGetChild(0);
         var4 = var5.eval(var1, var2);
      }

      Object var9;
      if (this.isSynchronized) {
         synchronized(var4) {
            var9 = this.evalBlock(var1, var2, var3, (BSHBlock.NodeFilter)null);
         }
      } else {
         var9 = this.evalBlock(var1, var2, var3, (BSHBlock.NodeFilter)null);
      }

      return var9;
   }

   Object evalBlock(CallStack var1, Interpreter var2, boolean var3, BSHBlock.NodeFilter var4) throws EvalError {
      Object var5 = Primitive.VOID;
      NameSpace var6 = null;
      if (!var3) {
         var6 = var1.top();
         BlockNameSpace var7 = new BlockNameSpace(var6);
         var1.swap(var7);
      }

      int var16 = this.isSynchronized ? 1 : 0;
      int var8 = this.jjtGetNumChildren();

      try {
         for(int var9 = var16; var9 < var8; ++var9) {
            SimpleNode var10 = (SimpleNode)this.jjtGetChild(var9);
            if ((var4 == null || var4.isVisible(var10)) && var10 instanceof BSHClassDeclaration) {
               var10.eval(var1, var2);
            }
         }

         for(int var17 = var16; var17 < var8; ++var17) {
            SimpleNode var11 = (SimpleNode)this.jjtGetChild(var17);
            if (!(var11 instanceof BSHClassDeclaration) && (var4 == null || var4.isVisible(var11))) {
               var5 = var11.eval(var1, var2);
               if (var5 instanceof ReturnControl) {
                  return var5;
               }
            }
         }

         return var5;
      } finally {
         if (!var3) {
            var1.swap(var6);
         }

      }
   }

   public interface NodeFilter {
      boolean isVisible(SimpleNode var1);
   }
}
