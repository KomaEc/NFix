package polyglot.ext.jl.types;

import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class LocalInstance_c extends VarInstance_c implements LocalInstance {
   protected LocalInstance_c() {
   }

   public LocalInstance_c(TypeSystem ts, Position pos, Flags flags, Type type, String name) {
      super(ts, pos, flags, type, name);
   }

   public void setConstantValue(Object constantValue) {
      if (constantValue != null && !(constantValue instanceof Boolean) && !(constantValue instanceof Number) && !(constantValue instanceof Character) && !(constantValue instanceof String)) {
         throw new InternalCompilerError("Can only set constant value to a primitive or String.");
      } else {
         this.constantValue = constantValue;
         this.isConstant = true;
      }
   }

   public LocalInstance constantValue(Object constantValue) {
      if (this.constantValue != constantValue) {
         LocalInstance_c n = (LocalInstance_c)this.copy();
         n.setConstantValue(constantValue);
         return n;
      } else {
         return this;
      }
   }

   public LocalInstance flags(Flags flags) {
      if (!flags.equals(this.flags)) {
         LocalInstance_c n = (LocalInstance_c)this.copy();
         n.flags = flags;
         return n;
      } else {
         return this;
      }
   }

   public LocalInstance name(String name) {
      if ((name == null || name.equals(this.name)) && (name != null || name == this.name)) {
         return this;
      } else {
         LocalInstance_c n = (LocalInstance_c)this.copy();
         n.name = name;
         return n;
      }
   }

   public LocalInstance type(Type type) {
      if (this.type != type) {
         LocalInstance_c n = (LocalInstance_c)this.copy();
         n.type = type;
         return n;
      } else {
         return this;
      }
   }

   public boolean equalsImpl(TypeObject o) {
      if (o instanceof LocalInstance) {
         LocalInstance i = (LocalInstance)o;
         return super.equalsImpl(i);
      } else {
         return false;
      }
   }

   public String toString() {
      return "local " + this.flags.translate() + this.type + " " + this.name + (this.constantValue != null ? " = " + this.constantValue : "");
   }

   public boolean isCanonical() {
      return this.type.isCanonical();
   }
}
