package polyglot.ext.jl.types;

import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.ReferenceType;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class FieldInstance_c extends VarInstance_c implements FieldInstance {
   protected ReferenceType container;

   protected FieldInstance_c() {
   }

   public FieldInstance_c(TypeSystem ts, Position pos, ReferenceType container, Flags flags, Type type, String name) {
      super(ts, pos, flags, type, name);
      this.container = container;
   }

   public ReferenceType container() {
      return this.container;
   }

   public void setConstantValue(Object constantValue) {
      if (constantValue != null && !(constantValue instanceof Boolean) && !(constantValue instanceof Number) && !(constantValue instanceof Character) && !(constantValue instanceof String)) {
         throw new InternalCompilerError("Can only set constant value to a primitive or String.");
      } else {
         this.constantValue = constantValue;
         this.isConstant = true;
      }
   }

   public FieldInstance constantValue(Object constantValue) {
      if (this.constantValue != constantValue) {
         FieldInstance_c n = (FieldInstance_c)this.copy();
         n.setConstantValue(constantValue);
         return n;
      } else {
         return this;
      }
   }

   public FieldInstance container(ReferenceType container) {
      if (this.container != container) {
         FieldInstance_c n = (FieldInstance_c)this.copy();
         n.container = container;
         return n;
      } else {
         return this;
      }
   }

   public FieldInstance flags(Flags flags) {
      if (!flags.equals(this.flags)) {
         FieldInstance_c n = (FieldInstance_c)this.copy();
         n.flags = flags;
         return n;
      } else {
         return this;
      }
   }

   public FieldInstance name(String name) {
      if ((name == null || name.equals(this.name)) && (name != null || name == this.name)) {
         return this;
      } else {
         FieldInstance_c n = (FieldInstance_c)this.copy();
         n.name = name;
         return n;
      }
   }

   public FieldInstance type(Type type) {
      if (this.type != type) {
         FieldInstance_c n = (FieldInstance_c)this.copy();
         n.type = type;
         return n;
      } else {
         return this;
      }
   }

   public boolean equalsImpl(TypeObject o) {
      if (!(o instanceof FieldInstance)) {
         return false;
      } else {
         FieldInstance i = (FieldInstance)o;
         return super.equalsImpl(i) && this.ts.equals(this.container, i.container());
      }
   }

   public String toString() {
      Object v = this.constantValue;
      if (v instanceof String) {
         String s = (String)v;
         if (s.length() > 8) {
            s = s.substring(0, 8) + "...";
         }

         v = "\"" + s + "\"";
      }

      return "field " + this.flags.translate() + this.type + " " + this.name + (v != null ? " = " + v : "");
   }

   public boolean isCanonical() {
      return this.container.isCanonical() && this.type.isCanonical();
   }
}
