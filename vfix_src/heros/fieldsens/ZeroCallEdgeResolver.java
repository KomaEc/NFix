package heros.fieldsens;

public class ZeroCallEdgeResolver<Field, Fact, Stmt, Method> extends CallEdgeResolver<Field, Fact, Stmt, Method> {
   private ZeroHandler<Field> zeroHandler;

   public ZeroCallEdgeResolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, ZeroHandler<Field> zeroHandler, Debugger<Field, Fact, Stmt, Method> debugger) {
      super(analyzer, debugger);
      this.zeroHandler = zeroHandler;
   }

   ZeroCallEdgeResolver<Field, Fact, Stmt, Method> copyWithAnalyzer(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
      return new ZeroCallEdgeResolver(analyzer, this.zeroHandler, this.debugger);
   }

   public void resolve(FlowFunction.Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {
      if (this.zeroHandler.shouldGenerateAccessPath(constraint.applyToAccessPath(new AccessPath()))) {
         callback.interest(this.analyzer, this);
      }

   }

   public void interest(Resolver<Field, Fact, Stmt, Method> resolver) {
   }

   protected ZeroCallEdgeResolver<Field, Fact, Stmt, Method> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
      return this;
   }

   public String toString() {
      return "[0-Resolver" + super.toString() + "]";
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.zeroHandler == null ? 0 : this.zeroHandler.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         ZeroCallEdgeResolver other = (ZeroCallEdgeResolver)obj;
         if (this.zeroHandler == null) {
            if (other.zeroHandler != null) {
               return false;
            }
         } else if (!this.zeroHandler.equals(other.zeroHandler)) {
            return false;
         }

         return true;
      }
   }
}
