package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Empty;
import polyglot.ast.Term;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.PrettyPrinter;

public class Empty_c extends Stmt_c implements Empty {
   public Empty_c(Position pos) {
      super(pos);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(";");
   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      return succs;
   }

   public String toString() {
      return ";";
   }
}
