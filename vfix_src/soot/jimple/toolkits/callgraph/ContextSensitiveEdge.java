package soot.jimple.toolkits.callgraph;

import soot.Context;
import soot.Kind;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;

public interface ContextSensitiveEdge {
   Context srcCtxt();

   SootMethod src();

   Unit srcUnit();

   Stmt srcStmt();

   Context tgtCtxt();

   SootMethod tgt();

   Kind kind();
}
