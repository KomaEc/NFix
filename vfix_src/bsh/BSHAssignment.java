package bsh;

class BSHAssignment extends SimpleNode implements ParserConstants {
   public int operator;

   BSHAssignment(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      BSHPrimaryExpression var3 = (BSHPrimaryExpression)this.jjtGetChild(0);
      if (var3 == null) {
         throw new InterpreterError("Error, null LHSnode");
      } else {
         boolean var4 = var2.getStrictJava();
         LHS var5 = var3.toLHS(var1, var2);
         if (var5 == null) {
            throw new InterpreterError("Error, null LHS");
         } else {
            Object var6 = null;
            if (this.operator != 81) {
               try {
                  var6 = var5.getValue();
               } catch (UtilEvalError var11) {
                  throw var11.toEvalError(this, var1);
               }
            }

            SimpleNode var7 = (SimpleNode)this.jjtGetChild(1);
            Object var8 = var7.eval(var1, var2);
            if (var8 == Primitive.VOID) {
               throw new EvalError("Void assignment.", this, var1);
            } else {
               try {
                  switch(this.operator) {
                  case 81:
                     return var5.assign(var8, var4);
                  case 82:
                  case 83:
                  case 84:
                  case 85:
                  case 86:
                  case 87:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 101:
                  case 102:
                  case 103:
                  case 104:
                  case 105:
                  case 106:
                  case 107:
                  case 108:
                  case 109:
                  case 110:
                  case 111:
                  case 112:
                  case 113:
                  case 114:
                  case 115:
                  case 116:
                  case 117:
                  default:
                     throw new InterpreterError("unimplemented operator in assignment BSH");
                  case 118:
                     return var5.assign(this.operation(var6, var8, 102), var4);
                  case 119:
                     return var5.assign(this.operation(var6, var8, 103), var4);
                  case 120:
                     return var5.assign(this.operation(var6, var8, 104), var4);
                  case 121:
                     return var5.assign(this.operation(var6, var8, 105), var4);
                  case 122:
                  case 123:
                     return var5.assign(this.operation(var6, var8, 106), var4);
                  case 124:
                  case 125:
                     return var5.assign(this.operation(var6, var8, 108), var4);
                  case 126:
                     return var5.assign(this.operation(var6, var8, 110), var4);
                  case 127:
                     return var5.assign(this.operation(var6, var8, 111), var4);
                  case 128:
                  case 129:
                     return var5.assign(this.operation(var6, var8, 112), var4);
                  case 130:
                  case 131:
                     return var5.assign(this.operation(var6, var8, 114), var4);
                  case 132:
                  case 133:
                     return var5.assign(this.operation(var6, var8, 116), var4);
                  }
               } catch (UtilEvalError var10) {
                  throw var10.toEvalError(this, var1);
               }
            }
         }
      }
   }

   private Object operation(Object var1, Object var2, int var3) throws UtilEvalError {
      if (var1 instanceof String && var2 != Primitive.VOID) {
         if (var3 != 102) {
            throw new UtilEvalError("Use of non + operator with String LHS");
         } else {
            return (String)var1 + var2;
         }
      } else {
         if (var1 instanceof Primitive || var2 instanceof Primitive) {
            if (var1 == Primitive.VOID || var2 == Primitive.VOID) {
               throw new UtilEvalError("Illegal use of undefined object or 'void' literal");
            }

            if (var1 == Primitive.NULL || var2 == Primitive.NULL) {
               throw new UtilEvalError("Illegal use of null object or 'null' literal");
            }
         }

         if (!(var1 instanceof Boolean) && !(var1 instanceof Character) && !(var1 instanceof Number) && !(var1 instanceof Primitive) || !(var2 instanceof Boolean) && !(var2 instanceof Character) && !(var2 instanceof Number) && !(var2 instanceof Primitive)) {
            throw new UtilEvalError("Non primitive value in operator: " + var1.getClass() + " " + ParserConstants.tokenImage[var3] + " " + var2.getClass());
         } else {
            return Primitive.binaryOperation(var1, var2, var3);
         }
      }
   }
}
