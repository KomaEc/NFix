package polyglot.ext.param.types;

import java.util.Iterator;
import java.util.List;
import polyglot.ext.jl.types.TypeObject_c;
import polyglot.types.ClassType;
import polyglot.types.Package;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.Position;

public abstract class PClass_c extends TypeObject_c implements PClass {
   protected PClass_c() {
   }

   public PClass_c(TypeSystem ts) {
      this(ts, (Position)null);
   }

   public PClass_c(TypeSystem ts, Position pos) {
      super(ts, pos);
   }

   public ClassType instantiate(Position pos, List actuals) throws SemanticException {
      ParamTypeSystem pts = (ParamTypeSystem)this.typeSystem();
      return pts.instantiate(pos, this, actuals);
   }

   public boolean isCanonical() {
      if (!this.clazz().isCanonical()) {
         return false;
      } else {
         Iterator i = this.formals().iterator();

         Param p;
         do {
            if (!i.hasNext()) {
               return true;
            }

            p = (Param)i.next();
         } while(p.isCanonical());

         return false;
      }
   }

   public String name() {
      return this.clazz().name();
   }

   public String fullName() {
      return this.clazz().fullName();
   }

   public Package package_() {
      return this.clazz().package_();
   }
}
