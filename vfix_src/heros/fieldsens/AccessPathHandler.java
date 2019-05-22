package heros.fieldsens;

import heros.fieldsens.structs.WrappedFact;

public class AccessPathHandler<Field, Fact, Stmt, Method> {
   private AccessPath<Field> accessPath;
   private Resolver<Field, Fact, Stmt, Method> resolver;
   private Debugger<Field, Fact, Stmt, Method> debugger;

   public AccessPathHandler(AccessPath<Field> accessPath, Resolver<Field, Fact, Stmt, Method> resolver, Debugger<Field, Fact, Stmt, Method> debugger) {
      this.accessPath = accessPath;
      this.resolver = resolver;
      this.debugger = debugger;
   }

   public boolean canRead(Field field) {
      return this.accessPath.canRead(field);
   }

   public boolean mayCanRead(Field field) {
      return this.accessPath.canRead(field) || this.accessPath.hasEmptyAccessPath() && !this.accessPath.isAccessInExclusions(field);
   }

   public boolean mayBeEmpty() {
      return this.accessPath.hasEmptyAccessPath();
   }

   public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
      return new FlowFunction.ConstrainedFact(new WrappedFact(fact, this.accessPath, this.resolver));
   }

   public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generateWithEmptyAccessPath(Fact fact, ZeroHandler<Field> zeroHandler) {
      return new FlowFunction.ConstrainedFact(new WrappedFact(fact, new AccessPath(), new ZeroCallEdgeResolver(this.resolver.analyzer, zeroHandler, this.debugger)));
   }

   public AccessPathHandler.ResultBuilder<Field, Fact, Stmt, Method> prepend(final Field field) {
      return new AccessPathHandler.ResultBuilder<Field, Fact, Stmt, Method>() {
         public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
            return new FlowFunction.ConstrainedFact(new WrappedFact(fact, AccessPathHandler.this.accessPath.prepend(field), AccessPathHandler.this.resolver));
         }
      };
   }

   public AccessPathHandler.ResultBuilder<Field, Fact, Stmt, Method> read(final Field field) {
      if (this.mayCanRead(field)) {
         return new AccessPathHandler.ResultBuilder<Field, Fact, Stmt, Method>() {
            public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
               return AccessPathHandler.this.canRead(field) ? new FlowFunction.ConstrainedFact(new WrappedFact(fact, AccessPathHandler.this.accessPath.removeFirst(), AccessPathHandler.this.resolver)) : new FlowFunction.ConstrainedFact(new WrappedFact(fact, new AccessPath(), AccessPathHandler.this.resolver), new FlowFunction.ReadFieldConstraint(field));
            }
         };
      } else {
         throw new IllegalArgumentException("Cannot read field " + field);
      }
   }

   public AccessPathHandler.ResultBuilder<Field, Fact, Stmt, Method> overwrite(final Field field) {
      if (this.mayBeEmpty()) {
         return new AccessPathHandler.ResultBuilder<Field, Fact, Stmt, Method>() {
            public FlowFunction.ConstrainedFact<Field, Fact, Stmt, Method> generate(Fact fact) {
               return AccessPathHandler.this.accessPath.isAccessInExclusions(field) ? new FlowFunction.ConstrainedFact(new WrappedFact(fact, AccessPathHandler.this.accessPath, AccessPathHandler.this.resolver)) : new FlowFunction.ConstrainedFact(new WrappedFact(fact, AccessPathHandler.this.accessPath.appendExcludedFieldReference(field), AccessPathHandler.this.resolver), new FlowFunction.WriteFieldConstraint(field));
            }
         };
      } else {
         throw new IllegalArgumentException("Cannot write field " + field);
      }
   }

   public interface ResultBuilder<FieldRef, FactAbstraction, Stmt, Method> {
      FlowFunction.ConstrainedFact<FieldRef, FactAbstraction, Stmt, Method> generate(FactAbstraction var1);
   }
}
