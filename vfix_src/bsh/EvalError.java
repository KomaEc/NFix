package bsh;

public class EvalError extends Exception {
   SimpleNode node;
   String message;
   CallStack callstack;

   public EvalError(String var1, SimpleNode var2, CallStack var3) {
      this.setMessage(var1);
      this.node = var2;
      if (var3 != null) {
         this.callstack = var3.copy();
      }

   }

   public String toString() {
      String var1;
      if (this.node != null) {
         var1 = " : at Line: " + this.node.getLineNumber() + " : in file: " + this.node.getSourceFile() + " : " + this.node.getText();
      } else {
         var1 = ": <at unknown location>";
      }

      if (this.callstack != null) {
         var1 = var1 + "\n" + this.getScriptStackTrace();
      }

      return this.getMessage() + var1;
   }

   public void reThrow(String var1) throws EvalError {
      this.prependMessage(var1);
      throw this;
   }

   SimpleNode getNode() {
      return this.node;
   }

   void setNode(SimpleNode var1) {
      this.node = var1;
   }

   public String getErrorText() {
      return this.node != null ? this.node.getText() : "<unknown error>";
   }

   public int getErrorLineNumber() {
      return this.node != null ? this.node.getLineNumber() : -1;
   }

   public String getErrorSourceFile() {
      return this.node != null ? this.node.getSourceFile() : "<unknown file>";
   }

   public String getScriptStackTrace() {
      if (this.callstack == null) {
         return "<Unknown>";
      } else {
         String var1 = "";
         CallStack var2 = this.callstack.copy();

         while(var2.depth() > 0) {
            NameSpace var3 = var2.pop();
            SimpleNode var4 = var3.getNode();
            if (var3.isMethod) {
               var1 = var1 + "\nCalled from method: " + var3.getName();
               if (var4 != null) {
                  var1 = var1 + " : at Line: " + var4.getLineNumber() + " : in file: " + var4.getSourceFile() + " : " + var4.getText();
               }
            }
         }

         return var1;
      }
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   protected void prependMessage(String var1) {
      if (var1 != null) {
         if (this.message == null) {
            this.message = var1;
         } else {
            this.message = var1 + " : " + this.message;
         }

      }
   }
}
