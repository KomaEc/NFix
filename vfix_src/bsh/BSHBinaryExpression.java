package bsh;

class BSHBinaryExpression extends SimpleNode implements ParserConstants {
   public int kind;
   // $FF: synthetic field
   static Class class$bsh$Primitive;

   BSHBinaryExpression(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      Object var3 = ((SimpleNode)this.jjtGetChild(0)).eval(var1, var2);
      if (this.kind == 35) {
         if (var3 == Primitive.NULL) {
            return new Primitive(false);
         } else {
            Class var11 = ((BSHType)this.jjtGetChild(1)).getType(var1, var2);
            if (var3 instanceof Primitive) {
               return var11 == (class$bsh$Primitive == null ? (class$bsh$Primitive = class$("bsh.Primitive")) : class$bsh$Primitive) ? new Primitive(true) : new Primitive(false);
            } else {
               boolean var9 = Types.isJavaBaseAssignable(var11, var3.getClass());
               return new Primitive(var9);
            }
         }
      } else {
         Object var4;
         if (this.kind == 98 || this.kind == 99) {
            var4 = var3;
            if (this.isPrimitiveValue(var3)) {
               var4 = ((Primitive)var3).getValue();
            }

            if (var4 instanceof Boolean && !(Boolean)var4) {
               return new Primitive(false);
            }
         }

         if (this.kind == 96 || this.kind == 97) {
            var4 = var3;
            if (this.isPrimitiveValue(var3)) {
               var4 = ((Primitive)var3).getValue();
            }

            if (var4 instanceof Boolean && (Boolean)var4) {
               return new Primitive(true);
            }
         }

         boolean var10 = this.isWrapper(var3);
         Object var5 = ((SimpleNode)this.jjtGetChild(1)).eval(var1, var2);
         boolean var6 = this.isWrapper(var5);
         if (!var10 && !this.isPrimitiveValue(var3) || !var6 && !this.isPrimitiveValue(var5) || var10 && var6 && this.kind == 90) {
            switch(this.kind) {
            case 90:
               return new Primitive(var3 == var5);
            case 95:
               return new Primitive(var3 != var5);
            case 102:
               if (var3 instanceof String || var5 instanceof String) {
                  return var3.toString() + var5.toString();
               }
            default:
               if (var3 instanceof Primitive || var5 instanceof Primitive) {
                  if (var3 == Primitive.VOID || var5 == Primitive.VOID) {
                     throw new EvalError("illegal use of undefined variable, class, or 'void' literal", this, var1);
                  }

                  if (var3 == Primitive.NULL || var5 == Primitive.NULL) {
                     throw new EvalError("illegal use of null value or 'null' literal", this, var1);
                  }
               }

               throw new EvalError("Operator: '" + ParserConstants.tokenImage[this.kind] + "' inappropriate for objects", this, var1);
            }
         } else {
            try {
               return Primitive.binaryOperation(var3, var5, this.kind);
            } catch (UtilEvalError var8) {
               throw var8.toEvalError(this, var1);
            }
         }
      }
   }

   private boolean isPrimitiveValue(Object var1) {
      return var1 instanceof Primitive && var1 != Primitive.VOID && var1 != Primitive.NULL;
   }

   private boolean isWrapper(Object var1) {
      return var1 instanceof Boolean || var1 instanceof Character || var1 instanceof Number;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
