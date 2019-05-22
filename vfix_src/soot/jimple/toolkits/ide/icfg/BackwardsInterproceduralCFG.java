package soot.jimple.toolkits.ide.icfg;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.toolkits.graph.DirectedGraph;

public class BackwardsInterproceduralCFG implements BiDiInterproceduralCFG<Unit, SootMethod> {
   protected final BiDiInterproceduralCFG<Unit, SootMethod> delegate;

   public BackwardsInterproceduralCFG(BiDiInterproceduralCFG<Unit, SootMethod> fwICFG) {
      this.delegate = fwICFG;
   }

   public List<Unit> getSuccsOf(Unit n) {
      return this.delegate.getPredsOf(n);
   }

   public Collection<Unit> getStartPointsOf(SootMethod m) {
      return this.delegate.getEndPointsOf(m);
   }

   public List<Unit> getReturnSitesOfCallAt(Unit n) {
      return this.delegate.getPredsOfCallAt(n);
   }

   public boolean isExitStmt(Unit stmt) {
      return this.delegate.isStartPoint(stmt);
   }

   public boolean isStartPoint(Unit stmt) {
      return this.delegate.isExitStmt(stmt);
   }

   public Set<Unit> allNonCallStartNodes() {
      return this.delegate.allNonCallEndNodes();
   }

   public List<Unit> getPredsOf(Unit u) {
      return this.delegate.getSuccsOf(u);
   }

   public Collection<Unit> getEndPointsOf(SootMethod m) {
      return this.delegate.getStartPointsOf(m);
   }

   public List<Unit> getPredsOfCallAt(Unit u) {
      return this.delegate.getSuccsOf(u);
   }

   public Set<Unit> allNonCallEndNodes() {
      return this.delegate.allNonCallStartNodes();
   }

   public SootMethod getMethodOf(Unit n) {
      return (SootMethod)this.delegate.getMethodOf(n);
   }

   public Collection<SootMethod> getCalleesOfCallAt(Unit n) {
      return this.delegate.getCalleesOfCallAt(n);
   }

   public Collection<Unit> getCallersOf(SootMethod m) {
      return this.delegate.getCallersOf(m);
   }

   public Set<Unit> getCallsFromWithin(SootMethod m) {
      return this.delegate.getCallsFromWithin(m);
   }

   public boolean isCallStmt(Unit stmt) {
      return this.delegate.isCallStmt(stmt);
   }

   public DirectedGraph<Unit> getOrCreateUnitGraph(SootMethod m) {
      return this.delegate.getOrCreateUnitGraph(m);
   }

   public List<Value> getParameterRefs(SootMethod m) {
      return this.delegate.getParameterRefs(m);
   }

   public boolean isFallThroughSuccessor(Unit stmt, Unit succ) {
      throw new UnsupportedOperationException("not implemented because semantics unclear");
   }

   public boolean isBranchTarget(Unit stmt, Unit succ) {
      throw new UnsupportedOperationException("not implemented because semantics unclear");
   }

   public boolean isReturnSite(Unit n) {
      Iterator var2 = this.getSuccsOf(n).iterator();

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
      return this.delegate.isReachable(u);
   }
}
