package polyglot.ext.jl.types;

import polyglot.types.Package;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;

public class Package_c extends TypeObject_c implements Package {
   protected Package prefix;
   protected String name;
   protected String fullname;

   protected Package_c() {
      this.fullname = null;
   }

   public Package_c(TypeSystem ts) {
      this(ts, (Package)null, (String)null);
   }

   public Package_c(TypeSystem ts, String name) {
      this(ts, (Package)null, name);
   }

   public Package_c(TypeSystem ts, Package prefix, String name) {
      super(ts);
      this.fullname = null;
      this.prefix = prefix;
      this.name = name;
   }

   public boolean isType() {
      return false;
   }

   public boolean isPackage() {
      return true;
   }

   public Type toType() {
      return null;
   }

   public Package toPackage() {
      return this;
   }

   public Package prefix() {
      return this.prefix;
   }

   public String name() {
      return this.name;
   }

   public String translate(Resolver c) {
      return this.prefix() == null ? this.name() : this.prefix().translate(c) + "." + this.name();
   }

   public String fullName() {
      if (this.fullname == null) {
         this.fullname = this.prefix() == null ? this.name : this.prefix().fullName() + "." + this.name;
      }

      return this.fullname;
   }

   public String toString() {
      return this.prefix() == null ? this.name : this.prefix().toString() + "." + this.name;
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equalsImpl(TypeObject o) {
      if (o instanceof Package) {
         Package p = (Package)o;
         if (p == null) {
            return false;
         } else if (this.prefix() == null) {
            return p.prefix() == null && this.name().equals(p.name());
         } else {
            return this.prefix().equals(p.prefix()) && this.name().equals(p.name());
         }
      } else {
         return false;
      }
   }

   public boolean isCanonical() {
      return true;
   }
}
