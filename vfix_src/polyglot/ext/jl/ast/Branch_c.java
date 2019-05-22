package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Branch;
import polyglot.ast.Term;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.PrettyPrinter;

public class Branch_c extends Stmt_c implements Branch {
   protected Branch.Kind kind;
   protected String label;

   public Branch_c(Position pos, Branch.Kind kind, String label) {
      super(pos);
      this.kind = kind;
      this.label = label;
   }

   public Branch.Kind kind() {
      return this.kind;
   }

   public Branch kind(Branch.Kind kind) {
      Branch_c n = (Branch_c)this.copy();
      n.kind = kind;
      return n;
   }

   public String label() {
      return this.label;
   }

   public Branch label(String label) {
      Branch_c n = (Branch_c)this.copy();
      n.label = label;
      return n;
   }

   public String toString() {
      return this.kind.toString() + (this.label != null ? " " + this.label : "");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.kind.toString());
      if (this.label != null) {
         w.write(" " + this.label);
      }

      w.write(";");
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitBranchTarget(this);
      return Collections.EMPTY_LIST;
   }
}
