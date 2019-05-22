package bsh;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

public class ExternalNameSpace extends NameSpace {
   private Map externalMap;

   public ExternalNameSpace() {
      this((NameSpace)null, "External Map Namespace", (Map)null);
   }

   public ExternalNameSpace(NameSpace var1, String var2, Map var3) {
      super(var1, var2);
      if (var3 == null) {
         var3 = new HashMap();
      }

      this.externalMap = (Map)var3;
   }

   public Map getMap() {
      return this.externalMap;
   }

   public void setMap(Map var1) {
      this.externalMap = null;
      this.clear();
      this.externalMap = var1;
   }

   void setVariable(String var1, Object var2, boolean var3, boolean var4) throws UtilEvalError {
      super.setVariable(var1, var2, var3, var4);
      this.putExternalMap(var1, var2);
   }

   public void unsetVariable(String var1) {
      super.unsetVariable(var1);
      this.externalMap.remove(var1);
   }

   public String[] getVariableNames() {
      HashSet var1 = new HashSet();
      String[] var2 = super.getVariableNames();
      var1.addAll(Arrays.asList(var2));
      var1.addAll(this.externalMap.keySet());
      return (String[])var1.toArray(new String[0]);
   }

   protected Variable getVariableImpl(String var1, boolean var2) throws UtilEvalError {
      Object var3 = this.externalMap.get(var1);
      Variable var4;
      if (var3 == null) {
         super.unsetVariable(var1);
         var4 = super.getVariableImpl(var1, var2);
      } else {
         Variable var5 = super.getVariableImpl(var1, false);
         if (var5 == null) {
            var4 = new Variable(var1, (Class)null, var3, (Modifiers)null);
         } else {
            var4 = var5;
         }
      }

      return var4;
   }

   public Variable[] getDeclaredVariables() {
      return super.getDeclaredVariables();
   }

   public void setTypedVariable(String var1, Class var2, Object var3, Modifiers var4) throws UtilEvalError {
      super.setTypedVariable(var1, var2, var3, var4);
      this.putExternalMap(var1, var3);
   }

   public void setMethod(String var1, BshMethod var2) throws UtilEvalError {
      super.setMethod(var1, var2);
   }

   public BshMethod getMethod(String var1, Class[] var2, boolean var3) throws UtilEvalError {
      return super.getMethod(var1, var2, var3);
   }

   protected void getAllNamesAux(Vector var1) {
      super.getAllNamesAux(var1);
   }

   public void clear() {
      super.clear();
      this.externalMap.clear();
   }

   protected void putExternalMap(String var1, Object var2) {
      if (var2 instanceof Variable) {
         try {
            var2 = this.unwrapVariable((Variable)var2);
         } catch (UtilEvalError var4) {
            throw new InterpreterError("unexpected UtilEvalError");
         }
      }

      if (var2 instanceof Primitive) {
         var2 = Primitive.unwrap((Object)((Primitive)var2));
      }

      this.externalMap.put(var1, var2);
   }
}
