package polyglot.ext.jl.ast;

import polyglot.ast.Node;
import polyglot.ast.NullLit;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class NullLit_c extends Lit_c implements NullLit {
   public NullLit_c(Position pos) {
      super(pos);
   }

   public Node typeCheck(TypeChecker tc) {
      return this.type(tc.typeSystem().Null());
   }

   public Object objValue() {
      return null;
   }

   public String toString() {
      return "null";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("null");
   }

   public Object constantValue() {
      return null;
   }
}
