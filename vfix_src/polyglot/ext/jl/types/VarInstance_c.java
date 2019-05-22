package polyglot.ext.jl.types;

import polyglot.types.Flags;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.Position;

public abstract class VarInstance_c extends TypeObject_c implements VarInstance {
   protected Flags flags;
   protected Type type;
   protected String name;
   protected Object constantValue;
   protected boolean isConstant;

   protected VarInstance_c() {
   }

   public VarInstance_c(TypeSystem ts, Position pos, Flags flags, Type type, String name) {
      super(ts, pos);
      this.flags = flags;
      this.type = type;
      this.name = name;
   }

   public boolean isConstant() {
      return this.isConstant;
   }

   public Object constantValue() {
      return this.constantValue;
   }

   public Flags flags() {
      return this.flags;
   }

   public Type type() {
      return this.type;
   }

   public String name() {
      return this.name;
   }

   public int hashCode() {
      return this.flags.hashCode() + this.name.hashCode();
   }

   public boolean equalsImpl(TypeObject o) {
      if (!(o instanceof VarInstance)) {
         return false;
      } else {
         VarInstance i = (VarInstance)o;
         return this.flags.equals(i.flags()) && this.ts.equals(this.type, i.type()) && this.name.equals(i.name());
      }
   }

   public boolean isCanonical() {
      return true;
   }

   public void setType(Type type) {
      this.type = type;
   }

   public void setFlags(Flags flags) {
      this.flags = flags;
   }
}
