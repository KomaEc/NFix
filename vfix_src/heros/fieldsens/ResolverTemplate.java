package heros.fieldsens;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class ResolverTemplate<Field, Fact, Stmt, Method, Incoming> extends Resolver<Field, Fact, Stmt, Method> {
   private boolean recursionLock = false;
   protected Set<Incoming> incomingEdges = Sets.newHashSet();
   private ResolverTemplate<Field, Fact, Stmt, Method, Incoming> parent;
   private Map<AccessPath<Field>, ResolverTemplate<Field, Fact, Stmt, Method, Incoming>> nestedResolvers = Maps.newHashMap();
   private Map<AccessPath<Field>, ResolverTemplate<Field, Fact, Stmt, Method, Incoming>> allResolversInExclHierarchy;
   protected AccessPath<Field> resolvedAccessPath;
   protected Debugger<Field, Fact, Stmt, Method> debugger;

   public ResolverTemplate(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, AccessPath<Field> resolvedAccessPath, ResolverTemplate<Field, Fact, Stmt, Method, Incoming> parent, Debugger<Field, Fact, Stmt, Method> debugger) {
      super(analyzer);
      this.resolvedAccessPath = resolvedAccessPath;
      this.parent = parent;
      this.debugger = debugger;
      if (parent != null && !resolvedAccessPath.getExclusions().isEmpty()) {
         this.allResolversInExclHierarchy = parent.allResolversInExclHierarchy;
      } else {
         this.allResolversInExclHierarchy = Maps.newHashMap();
      }

      debugger.newResolver(analyzer, this);
   }

   protected boolean isLocked() {
      if (this.recursionLock) {
         return true;
      } else {
         return this.parent == null ? false : this.parent.isLocked();
      }
   }

   protected void lock() {
      this.recursionLock = true;
   }

   protected void unlock() {
      this.recursionLock = false;
   }

   protected abstract AccessPath<Field> getAccessPathOf(Incoming var1);

   public void addIncoming(Incoming inc) {
      if (this.resolvedAccessPath.isPrefixOf(this.getAccessPathOf(inc)) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX) {
         this.log("Incoming Edge: " + inc);
         if (!this.incomingEdges.add(inc)) {
            return;
         }

         this.interest(this);
         Iterator var2 = Lists.newLinkedList(this.nestedResolvers.values()).iterator();

         while(var2.hasNext()) {
            ResolverTemplate<Field, Fact, Stmt, Method, Incoming> nestedResolver = (ResolverTemplate)var2.next();
            nestedResolver.addIncoming(inc);
         }

         this.processIncomingGuaranteedPrefix(inc);
      } else if (this.getAccessPathOf(inc).isPrefixOf(this.resolvedAccessPath).atLeast(AccessPath.PrefixTestResult.POTENTIAL_PREFIX)) {
         this.processIncomingPotentialPrefix(inc);
      }

   }

   protected abstract void processIncomingPotentialPrefix(Incoming var1);

   protected abstract void processIncomingGuaranteedPrefix(Incoming var1);

   public void resolve(FlowFunction.Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {
      this.log("Resolve: " + constraint);
      this.debugger.askedToResolve(this, constraint);
      if (constraint.canBeAppliedTo(this.resolvedAccessPath) && !this.isLocked()) {
         AccessPath<Field> newAccPath = constraint.applyToAccessPath(this.resolvedAccessPath);
         ResolverTemplate<Field, Fact, Stmt, Method, Incoming> nestedResolver = this.getOrCreateNestedResolver(newAccPath);

         assert nestedResolver.resolvedAccessPath.equals(constraint.applyToAccessPath(this.resolvedAccessPath));

         nestedResolver.registerCallback(callback);
      }

   }

   protected ResolverTemplate<Field, Fact, Stmt, Method, Incoming> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
      if (this.resolvedAccessPath.equals(newAccPath)) {
         return this;
      } else if (this.nestedResolvers.containsKey(newAccPath)) {
         return (ResolverTemplate)this.nestedResolvers.get(newAccPath);
      } else {
         assert this.resolvedAccessPath.getDeltaTo(newAccPath).accesses.length <= 1;

         if (this.allResolversInExclHierarchy.containsKey(newAccPath)) {
            return (ResolverTemplate)this.allResolversInExclHierarchy.get(newAccPath);
         } else {
            ResolverTemplate<Field, Fact, Stmt, Method, Incoming> nestedResolver = this.createNestedResolver(newAccPath);
            if (!this.resolvedAccessPath.getExclusions().isEmpty() || !newAccPath.getExclusions().isEmpty()) {
               this.allResolversInExclHierarchy.put(newAccPath, nestedResolver);
            }

            this.nestedResolvers.put(newAccPath, nestedResolver);
            Iterator var3 = Lists.newLinkedList(this.incomingEdges).iterator();

            while(var3.hasNext()) {
               Incoming inc = var3.next();
               nestedResolver.addIncoming(inc);
            }

            return nestedResolver;
         }
      }
   }

   protected abstract ResolverTemplate<Field, Fact, Stmt, Method, Incoming> createNestedResolver(AccessPath<Field> var1);
}
