package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.List;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Flags;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;

public class ConstructorInstance_c extends ProcedureInstance_c implements ConstructorInstance {
   protected ConstructorInstance_c() {
   }

   public ConstructorInstance_c(TypeSystem ts, Position pos, ClassType container, Flags flags, List formalTypes, List excTypes) {
      super(ts, pos, container, flags, formalTypes, excTypes);
   }

   public ConstructorInstance flags(Flags flags) {
      if (!flags.equals(this.flags)) {
         ConstructorInstance_c n = (ConstructorInstance_c)this.copy();
         n.flags = flags;
         return n;
      } else {
         return this;
      }
   }

   public ConstructorInstance formalTypes(List l) {
      if (!CollectionUtil.equals(this.formalTypes, l)) {
         ConstructorInstance_c n = (ConstructorInstance_c)this.copy();
         n.formalTypes = new ArrayList(l);
         return n;
      } else {
         return this;
      }
   }

   public ConstructorInstance throwTypes(List l) {
      if (!CollectionUtil.equals(this.excTypes, l)) {
         ConstructorInstance_c n = (ConstructorInstance_c)this.copy();
         n.excTypes = new ArrayList(l);
         return n;
      } else {
         return this;
      }
   }

   public ConstructorInstance container(ClassType container) {
      if (this.container != container) {
         ConstructorInstance_c n = (ConstructorInstance_c)this.copy();
         n.container = container;
         return n;
      } else {
         return this;
      }
   }

   public String toString() {
      return this.designator() + " " + this.flags.translate() + this.signature();
   }

   public String signature() {
      return this.container + "(" + TypeSystem_c.listToString(this.formalTypes) + ")";
   }

   public String designator() {
      return "constructor";
   }

   public boolean equalsImpl(TypeObject o) {
      return !(o instanceof ConstructorInstance) ? false : super.equalsImpl(o);
   }

   public boolean isCanonical() {
      return this.container.isCanonical() && this.listIsCanonical(this.formalTypes) && this.listIsCanonical(this.excTypes);
   }
}
