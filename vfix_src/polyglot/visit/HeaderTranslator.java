package polyglot.visit;

import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.frontend.TargetFactory;
import polyglot.types.TypeSystem;

public class HeaderTranslator extends Translator {
   public HeaderTranslator(Job job, TypeSystem ts, NodeFactory nf, TargetFactory tf) {
      super(job, ts, nf, tf);
   }

   public HeaderTranslator(Translator t) {
      super(t.job(), t.typeSystem(), t.nodeFactory(), t.targetFactory());
      this.context = t.context;
      this.appendSemicolon = t.appendSemicolon();
   }
}
