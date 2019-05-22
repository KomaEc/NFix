package polyglot.ext.jl.ast;

import polyglot.ast.PackageNode;
import polyglot.types.Package;
import polyglot.types.Qualifier;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;

public class PackageNode_c extends Node_c implements PackageNode {
   protected Package package_;

   public PackageNode_c(Position pos, Package package_) {
      super(pos);
      this.package_ = package_;
   }

   public Qualifier qualifier() {
      return this.package_;
   }

   public Package package_() {
      return this.package_;
   }

   public PackageNode package_(Package package_) {
      PackageNode_c n = (PackageNode_c)this.copy();
      n.package_ = package_;
      return n;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.package_ == null) {
         w.write("<unknown-package>");
      } else {
         w.write(this.package_.toString());
      }

   }

   public void translate(CodeWriter w, Translator tr) {
      w.write(tr.typeSystem().translatePackage(tr.context(), this.package_));
   }

   public String toString() {
      return this.package_.toString();
   }
}
