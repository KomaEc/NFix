package bsh;

class BSHUnaryExpression extends SimpleNode implements ParserConstants {
   public int kind;
   public boolean postfix = false;

   BSHUnaryExpression(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      SimpleNode var3 = (SimpleNode)this.jjtGetChild(0);

      try {
         if (this.kind != 100 && this.kind != 101) {
            return this.unaryOperation(var3.eval(var1, var2), this.kind);
         } else {
            LHS var4 = ((BSHPrimaryExpression)var3).toLHS(var1, var2);
            return this.lhsUnaryOperation(var4, var2.getStrictJava());
         }
      } catch (UtilEvalError var5) {
         throw var5.toEvalError(this, var1);
      }
   }

   private Object lhsUnaryOperation(LHS var1, boolean var2) throws UtilEvalError {
      if (Interpreter.DEBUG) {
         Interpreter.debug("lhsUnaryOperation");
      }

      Object var3 = var1.getValue();
      Object var4 = this.unaryOperation(var3, this.kind);
      Object var5;
      if (this.postfix) {
         var5 = var3;
      } else {
         var5 = var4;
      }

      var1.assign(var4, var2);
      return var5;
   }

   private Object unaryOperation(Object var1, int var2) throws UtilEvalError {
      if (!(var1 instanceof Boolean) && !(var1 instanceof Character) && !(var1 instanceof Number)) {
         if (!(var1 instanceof Primitive)) {
            throw new UtilEvalError("Unary operation " + ParserConstants.tokenImage[var2] + " inappropriate for object");
         } else {
            return Primitive.unaryOperation((Primitive)var1, var2);
         }
      } else {
         return this.primitiveWrapperUnaryOperation(var1, var2);
      }
   }

   private Object primitiveWrapperUnaryOperation(Object var1, int var2) throws UtilEvalError {
      Class var3 = var1.getClass();
      Object var4 = Primitive.promoteToInteger(var1);
      if (var4 instanceof Boolean) {
         return new Boolean(Primitive.booleanUnaryOperation((Boolean)var4, var2));
      } else if (!(var4 instanceof Integer)) {
         if (var4 instanceof Long) {
            return new Long(Primitive.longUnaryOperation((Long)var4, var2));
         } else if (var4 instanceof Float) {
            return new Float(Primitive.floatUnaryOperation((Float)var4, var2));
         } else if (var4 instanceof Double) {
            return new Double(Primitive.doubleUnaryOperation((Double)var4, var2));
         } else {
            throw new InterpreterError("An error occurred.  Please call technical support.");
         }
      } else {
         int var5 = Primitive.intUnaryOperation((Integer)var4, var2);
         if (var2 == 100 || var2 == 101) {
            if (var3 == Byte.TYPE) {
               return new Byte((byte)var5);
            }

            if (var3 == Short.TYPE) {
               return new Short((short)var5);
            }

            if (var3 == Character.TYPE) {
               return new Character((char)var5);
            }
         }

         return new Integer(var5);
      }
   }
}
