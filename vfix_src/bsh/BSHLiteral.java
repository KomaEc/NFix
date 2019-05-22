package bsh;

class BSHLiteral extends SimpleNode {
   public Object value;

   BSHLiteral(int var1) {
      super(var1);
   }

   public Object eval(CallStack var1, Interpreter var2) throws EvalError {
      if (this.value == null) {
         throw new InterpreterError("Null in bsh literal: " + this.value);
      } else {
         return this.value;
      }
   }

   private char getEscapeChar(char var1) {
      switch(var1) {
      case '"':
      case '\'':
      case '\\':
      default:
         break;
      case 'b':
         var1 = '\b';
         break;
      case 'f':
         var1 = '\f';
         break;
      case 'n':
         var1 = '\n';
         break;
      case 'r':
         var1 = '\r';
         break;
      case 't':
         var1 = '\t';
      }

      return var1;
   }

   public void charSetup(String var1) {
      char var2 = var1.charAt(0);
      if (var2 == '\\') {
         var2 = var1.charAt(1);
         if (Character.isDigit(var2)) {
            var2 = (char)Integer.parseInt(var1.substring(1), 8);
         } else {
            var2 = this.getEscapeChar(var2);
         }
      }

      this.value = new Primitive(new Character(var2));
   }

   void stringSetup(String var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         char var4 = var1.charAt(var3);
         if (var4 == '\\') {
            ++var3;
            var4 = var1.charAt(var3);
            if (!Character.isDigit(var4)) {
               var4 = this.getEscapeChar(var4);
            } else {
               int var5;
               for(var5 = var3; var5 < var3 + 2 && Character.isDigit(var1.charAt(var5 + 1)); ++var5) {
               }

               var4 = (char)Integer.parseInt(var1.substring(var3, var5 + 1), 8);
               var3 = var5;
            }
         }

         var2.append(var4);
      }

      this.value = var2.toString().intern();
   }
}
