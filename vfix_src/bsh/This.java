package bsh;

import java.io.Serializable;

public class This implements Serializable, Runnable {
   NameSpace namespace;
   transient Interpreter declaringInterpreter;

   static This getThis(NameSpace var0, Interpreter var1) {
      try {
         Class var2;
         if (Capabilities.canGenerateInterfaces()) {
            var2 = Class.forName("bsh.XThis");
         } else {
            if (!Capabilities.haveSwing()) {
               return new This(var0, var1);
            }

            var2 = Class.forName("bsh.JThis");
         }

         return (This)Reflect.constructObject(var2, new Object[]{var0, var1});
      } catch (Exception var3) {
         throw new InterpreterError("internal error 1 in This: " + var3);
      }
   }

   public Object getInterface(Class var1) throws UtilEvalError {
      if (var1.isInstance(this)) {
         return this;
      } else {
         throw new UtilEvalError("Dynamic proxy mechanism not available. Cannot construct interface type: " + var1);
      }
   }

   public Object getInterface(Class[] var1) throws UtilEvalError {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!var1[var2].isInstance(this)) {
            throw new UtilEvalError("Dynamic proxy mechanism not available. Cannot construct interface type: " + var1[var2]);
         }
      }

      return this;
   }

   protected This(NameSpace var1, Interpreter var2) {
      this.namespace = var1;
      this.declaringInterpreter = var2;
   }

   public NameSpace getNameSpace() {
      return this.namespace;
   }

   public String toString() {
      return "'this' reference to Bsh object: " + this.namespace;
   }

   public void run() {
      try {
         this.invokeMethod("run", new Object[0]);
      } catch (EvalError var2) {
         this.declaringInterpreter.error("Exception in runnable:" + var2);
      }

   }

   public Object invokeMethod(String var1, Object[] var2) throws EvalError {
      return this.invokeMethod(var1, var2, (Interpreter)null, (CallStack)null, (SimpleNode)null, false);
   }

   public Object invokeMethod(String var1, Object[] var2, Interpreter var3, CallStack var4, SimpleNode var5, boolean var6) throws EvalError {
      if (var2 != null) {
         Object[] var7 = new Object[var2.length];

         for(int var8 = 0; var8 < var2.length; ++var8) {
            var7[var8] = var2[var8] == null ? Primitive.NULL : var2[var8];
         }

         var2 = var7;
      }

      if (var3 == null) {
         var3 = this.declaringInterpreter;
      }

      if (var4 == null) {
         var4 = new CallStack(this.namespace);
      }

      if (var5 == null) {
         var5 = SimpleNode.JAVACODE;
      }

      Class[] var12 = Types.getTypes(var2);
      BshMethod var13 = null;

      try {
         var13 = this.namespace.getMethod(var1, var12, var6);
      } catch (UtilEvalError var11) {
      }

      if (var13 != null) {
         return var13.invoke(var2, var3, var4, var5);
      } else if (var1.equals("toString")) {
         return this.toString();
      } else if (var1.equals("hashCode")) {
         return new Integer(this.hashCode());
      } else if (var1.equals("equals")) {
         Object var9 = var2[0];
         return new Boolean(this == var9);
      } else {
         try {
            var13 = this.namespace.getMethod("invoke", new Class[]{null, null});
         } catch (UtilEvalError var10) {
         }

         if (var13 != null) {
            return var13.invoke(new Object[]{var1, var2}, var3, var4, var5);
         } else {
            throw new EvalError("Method " + StringUtil.methodString(var1, var12) + " not found in bsh scripted object: " + this.namespace.getName(), var5, var4);
         }
      }
   }

   public static void bind(This var0, NameSpace var1, Interpreter var2) {
      var0.namespace.setParent(var1);
      var0.declaringInterpreter = var2;
   }

   static boolean isExposedThisMethod(String var0) {
      return var0.equals("getClass") || var0.equals("invokeMethod") || var0.equals("getInterface") || var0.equals("wait") || var0.equals("notify") || var0.equals("notifyAll");
   }
}
