package heros.fieldsens;

public interface InterestCallback<Field, Fact, Stmt, Method> {
   void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> var1, Resolver<Field, Fact, Stmt, Method> var2);

   void canBeResolvedEmpty();
}
