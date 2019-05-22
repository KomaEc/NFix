package soot.jimple.toolkits.ide.icfg;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.DontSynchronize;
import heros.SynchronizedBy;
import heros.solver.IDESolver;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.Stmt;
import soot.toolkits.exceptions.UnitThrowAnalysis;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;

public abstract class AbstractJimpleBasedICFG implements BiDiInterproceduralCFG<Unit, SootMethod> {
   protected final boolean enableExceptions;
   @DontSynchronize("written by single thread; read afterwards")
   protected final Map<Unit, Body> unitToOwner;
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<Body, DirectedGraph<Unit>> bodyToUnitGraph;
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<SootMethod, List<Value>> methodToParameterRefs;
   @SynchronizedBy("by use of synchronized LoadingCache class")
   protected final LoadingCache<SootMethod, Set<Unit>> methodToCallsFromWithin;

   public AbstractJimpleBasedICFG() {
      this(true);
   }

   public AbstractJimpleBasedICFG(boolean enableExceptions) {
      this.unitToOwner = new HashMap();
      this.bodyToUnitGraph = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<Body, DirectedGraph<Unit>>() {
         public DirectedGraph<Unit> load(Body body) throws Exception {
            return AbstractJimpleBasedICFG.this.makeGraph(body);
         }
      });
      this.methodToParameterRefs = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, List<Value>>() {
         public List<Value> load(SootMethod m) throws Exception {
            return m.getActiveBody().getParameterRefs();
         }
      });
      this.methodToCallsFromWithin = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, Set<Unit>>() {
         public Set<Unit> load(SootMethod m) throws Exception {
            Set<Unit> res = null;
            Iterator var3 = m.getActiveBody().getUnits().iterator();

            while(var3.hasNext()) {
               Unit u = (Unit)var3.next();
               if (AbstractJimpleBasedICFG.this.isCallStmt(u)) {
                  if (res == null) {
                     res = new LinkedHashSet();
                  }

                  res.add(u);
               }
            }

            return (Set)(res == null ? Collections.emptySet() : res);
         }
      });
      this.enableExceptions = enableExceptions;
   }

   public SootMethod getMethodOf(Unit u) {
      assert this.unitToOwner.containsKey(u) : "Statement " + u + " not in unit-to-owner mapping";

      Body b = (Body)this.unitToOwner.get(u);
      return b == null ? null : b.getMethod();
   }

   public List<Unit> getSuccsOf(Unit u) {
      Body body = (Body)this.unitToOwner.get(u);
      if (body == null) {
         return Collections.emptyList();
      } else {
         DirectedGraph<Unit> unitGraph = this.getOrCreateUnitGraph(body);
         return unitGraph.getSuccsOf(u);
      }
   }

   public DirectedGraph<Unit> getOrCreateUnitGraph(SootMethod m) {
      return this.getOrCreateUnitGraph(m.getActiveBody());
   }

   public DirectedGraph<Unit> getOrCreateUnitGraph(Body body) {
      return (DirectedGraph)this.bodyToUnitGraph.getUnchecked(body);
   }

   protected DirectedGraph<Unit> makeGraph(Body body) {
      return (DirectedGraph)(this.enableExceptions ? new ExceptionalUnitGraph(body, UnitThrowAnalysis.v(), true) : new BriefUnitGraph(body));
   }

   public boolean isExitStmt(Unit u) {
      Body body = (Body)this.unitToOwner.get(u);
      DirectedGraph<Unit> unitGraph = this.getOrCreateUnitGraph(body);
      return unitGraph.getTails().contains(u);
   }

   public boolean isStartPoint(Unit u) {
      Body body = (Body)this.unitToOwner.get(u);
      DirectedGraph<Unit> unitGraph = this.getOrCreateUnitGraph(body);
      return unitGraph.getHeads().contains(u);
   }

   public boolean isFallThroughSuccessor(Unit u, Unit succ) {
      assert this.getSuccsOf(u).contains(succ);

      if (!u.fallsThrough()) {
         return false;
      } else {
         Body body = (Body)this.unitToOwner.get(u);
         return body.getUnits().getSuccOf(u) == succ;
      }
   }

   public boolean isBranchTarget(Unit u, Unit succ) {
      assert this.getSuccsOf(u).contains(succ);

      if (!u.branches()) {
         return false;
      } else {
         Iterator var3 = u.getUnitBoxes().iterator();

         UnitBox ub;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            ub = (UnitBox)var3.next();
         } while(ub.getUnit() != succ);

         return true;
      }
   }

   public List<Value> getParameterRefs(SootMethod m) {
      return (List)this.methodToParameterRefs.getUnchecked(m);
   }

   public Collection<Unit> getStartPointsOf(SootMethod m) {
      if (m.hasActiveBody()) {
         Body body = m.getActiveBody();
         DirectedGraph<Unit> unitGraph = this.getOrCreateUnitGraph(body);
         return unitGraph.getHeads();
      } else {
         return Collections.emptySet();
      }
   }

   public boolean isCallStmt(Unit u) {
      return ((Stmt)u).containsInvokeExpr();
   }

   public Set<Unit> allNonCallStartNodes() {
      Set<Unit> res = new LinkedHashSet(this.unitToOwner.keySet());
      Iterator iter = res.iterator();

      while(true) {
         Unit u;
         do {
            if (!iter.hasNext()) {
               return res;
            }

            u = (Unit)iter.next();
         } while(!this.isStartPoint(u) && !this.isCallStmt(u));

         iter.remove();
      }
   }

   public Set<Unit> allNonCallEndNodes() {
      Set<Unit> res = new LinkedHashSet(this.unitToOwner.keySet());
      Iterator iter = res.iterator();

      while(true) {
         Unit u;
         do {
            if (!iter.hasNext()) {
               return res;
            }

            u = (Unit)iter.next();
         } while(!this.isExitStmt(u) && !this.isCallStmt(u));

         iter.remove();
      }
   }

   public Collection<Unit> getReturnSitesOfCallAt(Unit u) {
      return this.getSuccsOf(u);
   }

   public Set<Unit> getCallsFromWithin(SootMethod m) {
      return (Set)this.methodToCallsFromWithin.getUnchecked(m);
   }

   public List<Unit> getPredsOf(Unit u) {
      assert u != null;

      Body body = (Body)this.unitToOwner.get(u);
      DirectedGraph<Unit> unitGraph = this.getOrCreateUnitGraph(body);
      return unitGraph.getPredsOf(u);
   }

   public Collection<Unit> getEndPointsOf(SootMethod m) {
      if (m.hasActiveBody()) {
         Body body = m.getActiveBody();
         DirectedGraph<Unit> unitGraph = this.getOrCreateUnitGraph(body);
         return unitGraph.getTails();
      } else {
         return Collections.emptySet();
      }
   }

   public List<Unit> getPredsOfCallAt(Unit u) {
      return this.getPredsOf(u);
   }

   public boolean isReturnSite(Unit n) {
      Iterator var2 = this.getPredsOf(n).iterator();

      Unit pred;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         pred = (Unit)var2.next();
      } while(!this.isCallStmt(pred));

      return true;
   }

   public boolean isReachable(Unit u) {
      return this.unitToOwner.containsKey(u);
   }
}
