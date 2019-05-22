package soot.jimple.toolkits.callgraph;

import soot.SootMethod;
import soot.jimple.Stmt;

public interface ReflectionModel {
   void methodInvoke(SootMethod var1, Stmt var2);

   void classNewInstance(SootMethod var1, Stmt var2);

   void contructorNewInstance(SootMethod var1, Stmt var2);

   void classForName(SootMethod var1, Stmt var2);
}
