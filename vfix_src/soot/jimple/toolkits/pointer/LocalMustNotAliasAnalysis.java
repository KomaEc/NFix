package soot.jimple.toolkits.pointer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.RefLikeType;
import soot.RefType;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.NewExpr;
import soot.jimple.Stmt;
import soot.jimple.internal.AbstractNewExpr;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class LocalMustNotAliasAnalysis extends ForwardFlowAnalysis<Unit, HashMap<Local, Set<NewExpr>>> {
   protected static final NewExpr UNKNOWN = new AbstractNewExpr() {
      public String toString() {
         return "UNKNOWN";
      }

      public Object clone() {
         return this;
      }
   };
   protected Set<Local> locals;

   public LocalMustNotAliasAnalysis(UnitGraph g) {
      this(g, g.getBody());
   }

   public LocalMustNotAliasAnalysis(DirectedGraph<Unit> directedGraph, Body b) {
      super(directedGraph);
      this.locals = new HashSet();
      this.locals.addAll(b.getLocals());
      Iterator var3 = b.getLocals().iterator();

      while(var3.hasNext()) {
         Local l = (Local)var3.next();
         if (l.getType() instanceof RefLikeType) {
            this.locals.add(l);
         }
      }

      this.doAnalysis();
   }

   protected void merge(HashMap<Local, Set<NewExpr>> in1, HashMap<Local, Set<NewExpr>> in2, HashMap<Local, Set<NewExpr>> o) {
      Iterator var4 = this.locals.iterator();

      while(true) {
         while(var4.hasNext()) {
            Local l = (Local)var4.next();
            Set<NewExpr> l1 = (Set)in1.get(l);
            Set<NewExpr> l2 = (Set)in2.get(l);
            Set<NewExpr> out = (Set)o.get(l);
            out.clear();
            if (!l1.contains(UNKNOWN) && !l2.contains(UNKNOWN)) {
               out.addAll(l1);
               out.addAll(l2);
            } else {
               out.add(UNKNOWN);
            }
         }

         return;
      }
   }

   protected void flowThrough(HashMap<Local, Set<NewExpr>> in, Unit unit, HashMap<Local, Set<NewExpr>> out) {
      Stmt s = (Stmt)unit;
      out.clear();
      out.putAll(in);
      if (s instanceof DefinitionStmt) {
         DefinitionStmt ds = (DefinitionStmt)s;
         Value lhs = ds.getLeftOp();
         Value rhs = ds.getRightOp();
         if (lhs instanceof Local) {
            HashSet<NewExpr> lv = new HashSet();
            out.put((Local)lhs, lv);
            if (rhs instanceof NewExpr) {
               lv.add((NewExpr)rhs);
            } else if (rhs instanceof Local) {
               lv.addAll((Collection)in.get(rhs));
            } else {
               lv.add(UNKNOWN);
            }
         }
      }

   }

   protected void copy(HashMap<Local, Set<NewExpr>> source, HashMap<Local, Set<NewExpr>> dest) {
      dest.putAll(source);
   }

   protected HashMap<Local, Set<NewExpr>> entryInitialFlow() {
      HashMap<Local, Set<NewExpr>> m = new HashMap();
      Iterator var2 = this.locals.iterator();

      while(var2.hasNext()) {
         Local l = (Local)var2.next();
         HashSet<NewExpr> s = new HashSet();
         s.add(UNKNOWN);
         m.put(l, s);
      }

      return m;
   }

   protected HashMap<Local, Set<NewExpr>> newInitialFlow() {
      HashMap<Local, Set<NewExpr>> m = new HashMap();
      Iterator var2 = this.locals.iterator();

      while(var2.hasNext()) {
         Local l = (Local)var2.next();
         HashSet<NewExpr> s = new HashSet();
         m.put(l, s);
      }

      return m;
   }

   public boolean hasInfoOn(Local l, Stmt s) {
      HashMap<Local, Set<NewExpr>> flowBefore = (HashMap)this.getFlowBefore(s);
      if (flowBefore == null) {
         return false;
      } else {
         Set<NewExpr> info = (Set)flowBefore.get(l);
         return info != null && !info.contains(UNKNOWN);
      }
   }

   public boolean notMayAlias(Local l1, Stmt s1, Local l2, Stmt s2) {
      Set<NewExpr> l1n = (Set)((HashMap)this.getFlowBefore(s1)).get(l1);
      Set<NewExpr> l2n = (Set)((HashMap)this.getFlowBefore(s2)).get(l2);
      if (!l1n.contains(UNKNOWN) && !l2n.contains(UNKNOWN)) {
         Set<NewExpr> n = new HashSet();
         n.addAll(l1n);
         n.retainAll(l2n);
         return n.isEmpty();
      } else {
         return false;
      }
   }

   public RefType concreteType(Local l, Stmt s) {
      HashMap<Local, Set<NewExpr>> flowBefore = (HashMap)this.getFlowBefore(s);
      Set<NewExpr> set = (Set)flowBefore.get(l);
      if (set.size() != 1) {
         return null;
      } else {
         NewExpr singleNewExpr = (NewExpr)set.iterator().next();
         return singleNewExpr == UNKNOWN ? null : (RefType)singleNewExpr.getType();
      }
   }
}
