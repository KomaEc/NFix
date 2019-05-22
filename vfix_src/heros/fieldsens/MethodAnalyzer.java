package heros.fieldsens;

import heros.fieldsens.structs.WrappedFactAtStatement;

public interface MethodAnalyzer<Field, Fact, Stmt, Method> {
   void addIncomingEdge(CallEdge<Field, Fact, Stmt, Method> var1);

   void addInitialSeed(Stmt var1, Fact var2);

   void addUnbalancedReturnFlow(WrappedFactAtStatement<Field, Fact, Stmt, Method> var1, Stmt var2);
}
