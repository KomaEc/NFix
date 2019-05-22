package polyglot.ext.jl.ast;

import polyglot.ast.BooleanLit;
import polyglot.ast.Node;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class BooleanLit_c extends Lit_c implements BooleanLit {
   protected boolean value;

   public BooleanLit_c(Position pos, boolean value) {
      super(pos);
      this.value = value;
   }

   public boolean value() {
      return this.value;
   }

   public BooleanLit value(boolean value) {
      BooleanLit_c n = (BooleanLit_c)this.copy();
      n.value = value;
      return n;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      return this.type(tc.typeSystem().Boolean());
   }

   public String toString() {
      return String.valueOf(this.value);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(String.valueOf(this.value));
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(value " + this.value + ")");
      w.end();
   }

   public Object constantValue() {
      return this.value;
   }
}
