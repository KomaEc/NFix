package polyglot.ast;

import java.util.List;
import polyglot.util.SubtypeSet;
import polyglot.visit.CFGBuilder;

public interface Term extends Node {
   Term entry();

   List acceptCFG(CFGBuilder var1, List var2);

   boolean reachable();

   Term reachable(boolean var1);

   SubtypeSet exceptions();

   Term exceptions(SubtypeSet var1);
}
