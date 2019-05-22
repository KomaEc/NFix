package bsh;

import java.lang.reflect.InvocationTargetException;

public abstract class ClassGenerator {
   private static ClassGenerator cg;

   public static ClassGenerator getClassGenerator() throws UtilEvalError {
      if (cg == null) {
         try {
            Class var0 = Class.forName("bsh.ClassGeneratorImpl");
            cg = (ClassGenerator)var0.newInstance();
         } catch (Exception var1) {
            throw new Capabilities.Unavailable("ClassGenerator unavailable: " + var1);
         }
      }

      return cg;
   }

   public abstract Class generateClass(String var1, Modifiers var2, Class[] var3, Class var4, BSHBlock var5, boolean var6, CallStack var7, Interpreter var8) throws EvalError;

   public abstract Object invokeSuperclassMethod(BshClassManager var1, Object var2, String var3, Object[] var4) throws UtilEvalError, ReflectError, InvocationTargetException;

   public abstract void setInstanceNameSpaceParent(Object var1, String var2, NameSpace var3);
}
