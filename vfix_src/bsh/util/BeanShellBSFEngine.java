package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.InterpreterError;
import bsh.Primitive;
import bsh.TargetError;
import bsh.This;
import java.util.Vector;
import org.apache.bsf.BSFDeclaredBean;
import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;
import org.apache.bsf.util.BSFEngineImpl;

public class BeanShellBSFEngine extends BSFEngineImpl {
   Interpreter interpreter;
   boolean installedApplyMethod;
   static final String bsfApplyMethod = "_bsfApply( _bsfNames, _bsfArgs, _bsfText ) {for(i=0;i<_bsfNames.length;i++)this.namespace.setVariable(_bsfNames[i], _bsfArgs[i],false);return this.interpreter.eval(_bsfText, this.namespace);}";

   public void initialize(BSFManager var1, String var2, Vector var3) throws BSFException {
      super.initialize(var1, var2, var3);
      this.interpreter = new Interpreter();

      try {
         this.interpreter.set("bsf", var1);
      } catch (EvalError var6) {
         throw new BSFException("bsh internal error: " + var6.toString());
      }

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         BSFDeclaredBean var5 = (BSFDeclaredBean)var3.get(var4);
         this.declareBean(var5);
      }

   }

   public void setDebug(boolean var1) {
      Interpreter var10000 = this.interpreter;
      Interpreter.DEBUG = var1;
   }

   public Object call(Object var1, String var2, Object[] var3) throws BSFException {
      if (var1 == null) {
         try {
            var1 = this.interpreter.get("global");
         } catch (EvalError var10) {
            throw new BSFException("bsh internal error: " + var10.toString());
         }
      }

      if (var1 instanceof This) {
         try {
            Object var4 = ((This)var1).invokeMethod(var2, var3);
            return Primitive.unwrap(var4);
         } catch (InterpreterError var7) {
            throw new BSFException("BeanShell interpreter internal error: " + var7);
         } catch (TargetError var8) {
            throw new BSFException("The application script threw an exception: " + var8.getTarget());
         } catch (EvalError var9) {
            throw new BSFException("BeanShell script error: " + var9);
         }
      } else {
         throw new BSFException("Cannot invoke method: " + var2 + ". Object: " + var1 + " is not a BeanShell scripted object.");
      }
   }

   public Object apply(String var1, int var2, int var3, Object var4, Vector var5, Vector var6) throws BSFException {
      if (var5.size() != var6.size()) {
         throw new BSFException("number of params/names mismatch");
      } else if (!(var4 instanceof String)) {
         throw new BSFException("apply: functino body must be a string");
      } else {
         String[] var7 = new String[var5.size()];
         var5.copyInto(var7);
         Object[] var8 = new Object[var6.size()];
         var6.copyInto(var8);

         try {
            if (!this.installedApplyMethod) {
               this.interpreter.eval("_bsfApply( _bsfNames, _bsfArgs, _bsfText ) {for(i=0;i<_bsfNames.length;i++)this.namespace.setVariable(_bsfNames[i], _bsfArgs[i],false);return this.interpreter.eval(_bsfText, this.namespace);}");
               this.installedApplyMethod = true;
            }

            This var9 = (This)this.interpreter.get("global");
            Object var10 = var9.invokeMethod("_bsfApply", new Object[]{var7, var8, (String)var4});
            return Primitive.unwrap(var10);
         } catch (InterpreterError var12) {
            throw new BSFException("BeanShell interpreter internal error: " + var12 + this.sourceInfo(var1, var2, var3));
         } catch (TargetError var13) {
            throw new BSFException("The application script threw an exception: " + var13.getTarget() + this.sourceInfo(var1, var2, var3));
         } catch (EvalError var14) {
            throw new BSFException("BeanShell script error: " + var14 + this.sourceInfo(var1, var2, var3));
         }
      }
   }

   public Object eval(String var1, int var2, int var3, Object var4) throws BSFException {
      if (!(var4 instanceof String)) {
         throw new BSFException("BeanShell expression must be a string");
      } else {
         try {
            return this.interpreter.eval((String)var4);
         } catch (InterpreterError var8) {
            throw new BSFException("BeanShell interpreter internal error: " + var8 + this.sourceInfo(var1, var2, var3));
         } catch (TargetError var9) {
            throw new BSFException("The application script threw an exception: " + var9.getTarget() + this.sourceInfo(var1, var2, var3));
         } catch (EvalError var10) {
            throw new BSFException("BeanShell script error: " + var10 + this.sourceInfo(var1, var2, var3));
         }
      }
   }

   public void exec(String var1, int var2, int var3, Object var4) throws BSFException {
      this.eval(var1, var2, var3, var4);
   }

   public void declareBean(BSFDeclaredBean var1) throws BSFException {
      try {
         this.interpreter.set(var1.name, var1.bean);
      } catch (EvalError var3) {
         throw new BSFException("error declaring bean: " + var1.name + " : " + var3.toString());
      }
   }

   public void undeclareBean(BSFDeclaredBean var1) throws BSFException {
      try {
         this.interpreter.unset(var1.name);
      } catch (EvalError var3) {
         throw new BSFException("bsh internal error: " + var3.toString());
      }
   }

   public void terminate() {
   }

   private String sourceInfo(String var1, int var2, int var3) {
      return " BSF info: " + var1 + " at line: " + var2 + " column: columnNo";
   }
}
