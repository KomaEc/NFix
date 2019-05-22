package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.SwitchBlock;
import polyglot.types.Context;
import polyglot.util.Position;

public class SwitchBlock_c extends AbstractBlock_c implements SwitchBlock {
   public SwitchBlock_c(Position pos, List statements) {
      super(pos, statements);
   }

   public Context enterScope(Context c) {
      return c;
   }
}
