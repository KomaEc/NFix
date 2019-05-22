package polyglot.ext.jl.types;

import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.ReferenceType;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public class InitializerInstance_c extends TypeObject_c implements InitializerInstance {
   protected ClassType container;
   protected Flags flags;

   protected InitializerInstance_c() {
   }

   public InitializerInstance_c(TypeSystem ts, Position pos, ClassType container, Flags flags) {
      super(ts, pos);
      this.container = container;
      this.flags = flags;
   }

   public ReferenceType container() {
      return this.container;
   }

   public InitializerInstance container(ClassType container) {
      if (this.container != container) {
         InitializerInstance_c n = (InitializerInstance_c)this.copy();
         n.container = container;
         return n;
      } else {
         return this;
      }
   }

   public Flags flags() {
      return this.flags;
   }

   public InitializerInstance flags(Flags flags) {
      if (!flags.equals(this.flags)) {
         InitializerInstance_c n = (InitializerInstance_c)this.copy();
         n.flags = flags;
         return n;
      } else {
         return this;
      }
   }

   public int hashCode() {
      return this.container.hashCode() + this.flags.hashCode();
   }

   public boolean equalsImpl(TypeObject o) {
      if (!(o instanceof InitializerInstance)) {
         return false;
      } else {
         InitializerInstance i = (InitializerInstance)o;
         return this.flags.equals(i.flags()) && this.ts.equals(this.container, i.container());
      }
   }

   public String toString() {
      return this.flags.translate() + "initializer";
   }

   public boolean isCanonical() {
      return true;
   }
}
