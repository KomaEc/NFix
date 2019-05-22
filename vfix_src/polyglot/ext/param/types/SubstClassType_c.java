package polyglot.ext.param.types;

import java.util.Iterator;
import java.util.List;
import polyglot.ext.jl.types.ClassType_c;
import polyglot.types.ClassType;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.util.Position;

public class SubstClassType_c extends ClassType_c implements SubstType {
   protected ClassType base;
   protected Subst subst;

   public SubstClassType_c(ParamTypeSystem ts, Position pos, ClassType base, Subst subst) {
      super(ts, pos);
      this.base = base;
      this.subst = subst;
   }

   public Iterator entries() {
      return this.subst.entries();
   }

   public Type base() {
      return this.base;
   }

   public Subst subst() {
      return this.subst;
   }

   public Type superType() {
      return this.subst.substType(this.base.superType());
   }

   public List interfaces() {
      return this.subst.substTypeList(this.base.interfaces());
   }

   public List fields() {
      return this.subst.substFieldList(this.base.fields());
   }

   public List methods() {
      return this.subst.substMethodList(this.base.methods());
   }

   public List constructors() {
      return this.subst.substConstructorList(this.base.constructors());
   }

   public List memberClasses() {
      return this.subst.substTypeList(this.base.memberClasses());
   }

   public ClassType outer() {
      return (ClassType)this.subst.substType(this.base.outer());
   }

   public ClassType.Kind kind() {
      return this.base.kind();
   }

   public boolean inStaticContext() {
      return this.base.inStaticContext();
   }

   public String fullName() {
      return this.base.fullName();
   }

   public String name() {
      return this.base.name();
   }

   public Package package_() {
      return this.base.package_();
   }

   public Flags flags() {
      return this.base.flags();
   }

   public String translate(Resolver c) {
      return this.base.translate(c);
   }

   public boolean equalsImpl(TypeObject t) {
      if (!(t instanceof SubstType)) {
         return false;
      } else {
         SubstType x = (SubstType)t;
         return this.base.equals(x.base()) && this.subst.equals(x.subst());
      }
   }

   public int hashCode() {
      return this.base.hashCode() ^ this.subst.hashCode();
   }

   public String toString() {
      return this.base.toString() + this.subst.toString();
   }
}
