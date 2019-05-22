package heros.fieldsens.structs;

import heros.fieldsens.AccessPath;
import heros.fieldsens.Resolver;

public class ReturnEdge<Field, Fact, Stmt, Method> {
   public final Fact incFact;
   public final Resolver<Field, Fact, Stmt, Method> resolverAtCaller;
   public final AccessPath.Delta<Field> callDelta;
   public final AccessPath<Field> incAccessPath;
   public final Resolver<Field, Fact, Stmt, Method> incResolver;
   public final AccessPath.Delta<Field> usedAccessPathOfIncResolver;

   public ReturnEdge(WrappedFact<Field, Fact, Stmt, Method> fact, Resolver<Field, Fact, Stmt, Method> resolverAtCaller, AccessPath.Delta<Field> callDelta) {
      this(fact.getFact(), fact.getAccessPath(), fact.getResolver(), resolverAtCaller, callDelta, AccessPath.Delta.empty());
   }

   private ReturnEdge(Fact incFact, AccessPath<Field> incAccessPath, Resolver<Field, Fact, Stmt, Method> incResolver, Resolver<Field, Fact, Stmt, Method> resolverAtCaller, AccessPath.Delta<Field> callDelta, AccessPath.Delta<Field> usedAccessPathOfIncResolver) {
      this.incFact = incFact;
      this.incAccessPath = incAccessPath;
      this.incResolver = incResolver;
      this.resolverAtCaller = resolverAtCaller;
      this.callDelta = callDelta;
      this.usedAccessPathOfIncResolver = usedAccessPathOfIncResolver;
   }

   public ReturnEdge<Field, Fact, Stmt, Method> copyWithIncomingResolver(Resolver<Field, Fact, Stmt, Method> incResolver, AccessPath.Delta<Field> usedAccessPathOfIncResolver) {
      return new ReturnEdge(this.incFact, this.incAccessPath, incResolver, this.resolverAtCaller, this.callDelta, usedAccessPathOfIncResolver);
   }

   public ReturnEdge<Field, Fact, Stmt, Method> copyWithResolverAtCaller(Resolver<Field, Fact, Stmt, Method> resolverAtCaller, AccessPath.Delta<Field> usedAccessPathOfIncResolver) {
      return new ReturnEdge(this.incFact, this.incAccessPath, (Resolver)null, resolverAtCaller, this.callDelta, usedAccessPathOfIncResolver);
   }

   public String toString() {
      return String.format("IncFact: %s%s, Delta: %s, IncResolver: <%s:%s>, ResolverAtCallSite: %s", this.incFact, this.incAccessPath, this.callDelta, this.usedAccessPathOfIncResolver, this.incResolver, this.resolverAtCaller);
   }
}
