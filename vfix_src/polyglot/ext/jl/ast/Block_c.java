package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;

public class Block_c extends AbstractBlock_c implements Block {
   public Block_c(Position pos, List statements) {
      super(pos, statements);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("{");
      w.allowBreak(4, " ");
      super.prettyPrint(w, tr);
      w.allowBreak(0, " ");
      w.write("}");
   }
}
