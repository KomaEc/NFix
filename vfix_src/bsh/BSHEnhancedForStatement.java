package bsh;

class BSHEnhancedForStatement extends SimpleNode implements ParserConstants {
   String varName;

   BSHEnhancedForStatement(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      Class var3 = null;
      SimpleNode var4 = null;
      NameSpace var5 = var1.top();
      SimpleNode var6 = (SimpleNode)this.jjtGetChild(0);
      int var7 = this.jjtGetNumChildren();
      SimpleNode var8;
      if (var6 instanceof BSHType) {
         var3 = ((BSHType)var6).getType(var1, var2);
         var8 = (SimpleNode)this.jjtGetChild(1);
         if (var7 > 2) {
            var4 = (SimpleNode)this.jjtGetChild(2);
         }
      } else {
         var8 = var6;
         if (var7 > 1) {
            var4 = (SimpleNode)this.jjtGetChild(1);
         }
      }

      BlockNameSpace var9 = new BlockNameSpace(var5);
      var1.swap(var9);
      Object var10 = var8.eval(var1, var2);
      if (var10 == Primitive.NULL) {
         throw new EvalError("The collection, array, map, iterator, or enumeration portion of a for statement cannot be null.", this, var1);
      } else {
         CollectionManager var11 = CollectionManager.getCollectionManager();
         if (!var11.isBshIterable(var10)) {
            throw new EvalError("Can't iterate over type: " + var10.getClass(), this, var1);
         } else {
            BshIterator var12 = var11.getBshIterator(var10);
            Object var13 = Primitive.VOID;

            while(var12.hasNext()) {
               try {
                  if (var3 != null) {
                     var9.setTypedVariable(this.varName, var3, var12.next(), new Modifiers());
                  } else {
                     var9.setVariable(this.varName, var12.next(), false);
                  }
               } catch (UtilEvalError var16) {
                  throw var16.toEvalError("for loop iterator variable:" + this.varName, this, var1);
               }

               boolean var14 = false;
               if (var4 != null) {
                  Object var15 = var4.eval(var1, var2);
                  if (var15 instanceof ReturnControl) {
                     switch(((ReturnControl)var15).kind) {
                     case 12:
                        var14 = true;
                     case 19:
                     default:
                        break;
                     case 46:
                        var13 = var15;
                        var14 = true;
                     }
                  }
               }

               if (var14) {
                  break;
               }
            }

            var1.swap(var5);
            return var13;
         }
      }
   }
}
