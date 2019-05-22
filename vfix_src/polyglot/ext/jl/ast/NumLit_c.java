package polyglot.ext.jl.ast;

import polyglot.ast.NumLit;
import polyglot.util.CodeWriter;
import polyglot.util.Position;

public abstract class NumLit_c extends Lit_c implements NumLit {
   protected long value;

   public NumLit_c(Position pos, long value) {
      super(pos);
      this.value = value;
   }

   public long longValue() {
      return this.value;
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(value " + this.value + ")");
      w.end();
   }
}
