package bsh;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

public class XThis extends This {
   Hashtable interfaces;
   InvocationHandler invocationHandler = new XThis.Handler();
   // $FF: synthetic field
   static Class class$java$lang$Object;

   public XThis(NameSpace var1, Interpreter var2) {
      super(var1, var2);
   }

   public String toString() {
      return "'this' reference (XThis) to Bsh object: " + this.namespace;
   }

   public Object getInterface(Class var1) {
      return this.getInterface(new Class[]{var1});
   }

   public Object getInterface(Class[] var1) {
      if (this.interfaces == null) {
         this.interfaces = new Hashtable();
      }

      int var2 = 21;

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2 *= var1[var3].hashCode() + 3;
      }

      Integer var4 = new Integer(var2);
      Object var5 = this.interfaces.get(var4);
      if (var5 == null) {
         ClassLoader var6 = var1[0].getClassLoader();
         var5 = Proxy.newProxyInstance(var6, var1, this.invocationHandler);
         this.interfaces.put(var4, var5);
      }

      return var5;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   class Handler implements InvocationHandler, Serializable {
      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         try {
            return this.invokeImpl(var1, var2, var3);
         } catch (TargetError var6) {
            throw var6.getTarget();
         } catch (EvalError var7) {
            if (Interpreter.DEBUG) {
               Interpreter.debug("EvalError in scripted interface: " + XThis.this.toString() + ": " + var7);
            }

            throw var7;
         }
      }

      public Object invokeImpl(Object var1, Method var2, Object[] var3) throws EvalError {
         String var4 = var2.getName();
         new CallStack(XThis.this.namespace);
         BshMethod var6 = null;

         try {
            var6 = XThis.this.namespace.getMethod("equals", new Class[]{XThis.class$java$lang$Object == null ? (XThis.class$java$lang$Object = XThis.class$("java.lang.Object")) : XThis.class$java$lang$Object});
         } catch (UtilEvalError var12) {
         }

         if (var4.equals("equals") && var6 == null) {
            Object var13 = var3[0];
            return new Boolean(var1 == var13);
         } else {
            BshMethod var7 = null;

            try {
               var7 = XThis.this.namespace.getMethod("toString", new Class[0]);
            } catch (UtilEvalError var11) {
            }

            Class[] var8;
            if (var4.equals("toString") && var7 == null) {
               var8 = var1.getClass().getInterfaces();
               StringBuffer var9 = new StringBuffer(XThis.this.toString() + "\nimplements:");

               for(int var10 = 0; var10 < var8.length; ++var10) {
                  var9.append(" " + var8[var10].getName() + (var8.length > 1 ? "," : ""));
               }

               return var9.toString();
            } else {
               var8 = var2.getParameterTypes();
               return Primitive.unwrap(XThis.this.invokeMethod(var4, Primitive.wrap(var3, var8)));
            }
         }
      }
   }
}
