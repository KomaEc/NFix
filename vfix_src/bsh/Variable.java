package bsh;

import java.io.Serializable;

public class Variable implements Serializable {
   static final int DECLARATION = 0;
   static final int ASSIGNMENT = 1;
   String name;
   Class type;
   String typeDescriptor;
   Object value;
   Modifiers modifiers;
   LHS lhs;

   Variable(String var1, Class var2, LHS var3) {
      this.type = null;
      this.name = var1;
      this.lhs = var3;
      this.type = var2;
   }

   Variable(String var1, Object var2, Modifiers var3) throws UtilEvalError {
      this(var1, (Class)null, var2, var3);
   }

   Variable(String var1, String var2, Object var3, Modifiers var4) throws UtilEvalError {
      this(var1, (Class)null, var3, var4);
      this.typeDescriptor = var2;
   }

   Variable(String var1, Class var2, Object var3, Modifiers var4) throws UtilEvalError {
      this.type = null;
      this.name = var1;
      this.type = var2;
      this.modifiers = var4;
      this.setValue(var3, 0);
   }

   public void setValue(Object var1, int var2) throws UtilEvalError {
      if (this.hasModifier("final") && this.value != null) {
         throw new UtilEvalError("Final variable, can't re-assign.");
      } else {
         if (var1 == null) {
            var1 = Primitive.getDefaultValue(this.type);
         }

         if (this.lhs != null) {
            this.lhs.assign(var1, false);
         } else {
            if (this.type != null) {
               var1 = Types.castObject(var1, this.type, var2 == 0 ? 0 : 1);
            }

            this.value = var1;
         }
      }
   }

   Object getValue() throws UtilEvalError {
      return this.lhs != null ? this.lhs.getValue() : this.value;
   }

   public Class getType() {
      return this.type;
   }

   public String getTypeDescriptor() {
      return this.typeDescriptor;
   }

   public Modifiers getModifiers() {
      return this.modifiers;
   }

   public String getName() {
      return this.name;
   }

   public boolean hasModifier(String var1) {
      return this.modifiers != null && this.modifiers.hasModifier(var1);
   }

   public String toString() {
      return "Variable: " + super.toString() + " " + this.name + ", type:" + this.type + ", value:" + this.value + ", lhs = " + this.lhs;
   }
}
