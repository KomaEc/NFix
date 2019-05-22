package bsh;

class BSHForStatement extends SimpleNode implements ParserConstants {
   public boolean hasForInit;
   public boolean hasExpression;
   public boolean hasForUpdate;
   private SimpleNode forInit;
   private SimpleNode expression;
   private SimpleNode forUpdate;
   private SimpleNode statement;
   private boolean parsed;

   BSHForStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      int var3 = 0;
      if (this.hasForInit) {
         this.forInit = (SimpleNode)this.jjtGetChild(var3++);
      }

      if (this.hasExpression) {
         this.expression = (SimpleNode)this.jjtGetChild(var3++);
      }

      if (this.hasForUpdate) {
         this.forUpdate = (SimpleNode)this.jjtGetChild(var3++);
      }

      if (var3 < this.jjtGetNumChildren()) {
         this.statement = (SimpleNode)this.jjtGetChild(var3);
      }

      NameSpace var4 = var1.top();
      BlockNameSpace var5 = new BlockNameSpace(var4);
      var1.swap(var5);
      if (this.hasForInit) {
         this.forInit.eval(var1, var2);
      }

      Object var6 = Primitive.VOID;

      while(true) {
         boolean var7;
         if (this.hasExpression) {
            var7 = BSHIfStatement.evaluateCondition(this.expression, var1, var2);
            if (!var7) {
               break;
            }
         }

         var7 = false;
         if (this.statement != null) {
            Object var8 = this.statement.eval(var1, var2);
            if (var8 instanceof ReturnControl) {
               switch(((ReturnControl)var8).kind) {
               case 12:
                  var7 = true;
               case 19:
               default:
                  break;
               case 46:
                  var6 = var8;
                  var7 = true;
               }
            }
         }

         if (var7) {
            break;
         }

         if (this.hasForUpdate) {
            this.forUpdate.eval(var1, var2);
         }
      }

      var1.swap(var4);
      return var6;
   }
}
