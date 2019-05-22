package heros.fieldsens;

public interface FlowFunctions<Stmt, FieldRef, F, Method> {
   FlowFunction<FieldRef, F, Stmt, Method> getNormalFlowFunction(Stmt var1);

   FlowFunction<FieldRef, F, Stmt, Method> getCallFlowFunction(Stmt var1, Method var2);

   FlowFunction<FieldRef, F, Stmt, Method> getReturnFlowFunction(Stmt var1, Method var2, Stmt var3, Stmt var4);

   FlowFunction<FieldRef, F, Stmt, Method> getCallToReturnFlowFunction(Stmt var1, Stmt var2);
}
