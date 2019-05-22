package polyglot.types;

import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;

public class PackageContextResolver implements Resolver {
   protected Package p;
   protected TypeSystem ts;
   protected Resolver cr;

   public PackageContextResolver(TypeSystem ts, Package p, Resolver cr) {
      this.ts = ts;
      this.p = p;
      this.cr = cr;
   }

   public Package package_() {
      return this.p;
   }

   public Resolver outer() {
      return this.cr;
   }

   public Named find(String name) throws SemanticException {
      if (!StringUtil.isNameShort(name)) {
         throw new InternalCompilerError("Cannot lookup qualified name " + name);
      } else if (this.cr == null) {
         return this.ts.createPackage(this.p, name);
      } else {
         try {
            return this.cr.find(this.p.fullName() + "." + name);
         } catch (NoClassException var3) {
            if (!var3.getClassName().equals(this.p.fullName() + "." + name)) {
               throw var3;
            } else {
               return this.ts.createPackage(this.p, name);
            }
         }
      }
   }

   public String toString() {
      return "(package-context " + this.p.toString() + ")";
   }
}
