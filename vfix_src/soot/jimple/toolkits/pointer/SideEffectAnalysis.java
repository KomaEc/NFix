package soot.jimple.toolkits.pointer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;

public class SideEffectAnalysis {
   PointsToAnalysis pa;
   CallGraph cg;
   Map<SootMethod, MethodRWSet> methodToNTReadSet;
   Map<SootMethod, MethodRWSet> methodToNTWriteSet;
   int rwsetcount;
   TransitiveTargets tt;

   public void findNTRWSets(SootMethod method) {
      if (!this.methodToNTReadSet.containsKey(method) || !this.methodToNTWriteSet.containsKey(method)) {
         MethodRWSet read = null;
         MethodRWSet write = null;
         Iterator sIt = method.retrieveActiveBody().getUnits().iterator();

         while(sIt.hasNext()) {
            Stmt s = (Stmt)sIt.next();
            RWSet ntr = this.ntReadSet(method, s);
            if (ntr != null) {
               if (read == null) {
                  read = new MethodRWSet();
               }

               read.union(ntr);
            }

            RWSet ntw = this.ntWriteSet(method, s);
            if (ntw != null) {
               if (write == null) {
                  write = new MethodRWSet();
               }

               write.union(ntw);
            }
         }

         this.methodToNTReadSet.put(method, read);
         this.methodToNTWriteSet.put(method, write);
      }
   }

   public RWSet nonTransitiveReadSet(SootMethod method) {
      this.findNTRWSets(method);
      return (RWSet)this.methodToNTReadSet.get(method);
   }

   public RWSet nonTransitiveWriteSet(SootMethod method) {
      this.findNTRWSets(method);
      return (RWSet)this.methodToNTWriteSet.get(method);
   }

   private SideEffectAnalysis() {
      this.methodToNTReadSet = new HashMap();
      this.methodToNTWriteSet = new HashMap();
      this.rwsetcount = 0;
      if (G.v().Union_factory == null) {
         G.v().Union_factory = new UnionFactory() {
            public Union newUnion() {
               return FullObjectSet.v();
            }
         };
      }

   }

   public SideEffectAnalysis(PointsToAnalysis pa, CallGraph cg) {
      this();
      this.pa = pa;
      this.cg = cg;
      this.tt = new TransitiveTargets(cg);
   }

   public SideEffectAnalysis(PointsToAnalysis pa, CallGraph cg, Filter filter) {
      this();
      this.pa = pa;
      this.cg = cg;
      this.tt = new TransitiveTargets(cg, filter);
   }

   private RWSet ntReadSet(SootMethod method, Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value r = a.getRightOp();
         return this.addValue(r, method, stmt);
      } else {
         return null;
      }
   }

   public RWSet readSet(SootMethod method, Stmt stmt) {
      RWSet ret = null;
      Iterator targets = this.tt.iterator((Unit)stmt);

      while(targets.hasNext()) {
         SootMethod target = (SootMethod)targets.next();
         if (target.isNative()) {
            if (ret == null) {
               ret = new SiteRWSet();
            }

            ret.setCallsNative();
         } else if (target.isConcrete()) {
            RWSet ntr = this.nonTransitiveReadSet(target);
            if (ntr != null) {
               if (ret == null) {
                  ret = new SiteRWSet();
               }

               ret.union(ntr);
            }
         }
      }

      if (ret == null) {
         return this.ntReadSet(method, stmt);
      } else {
         ret.union(this.ntReadSet(method, stmt));
         return ret;
      }
   }

   private RWSet ntWriteSet(SootMethod method, Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value l = a.getLeftOp();
         return this.addValue(l, method, stmt);
      } else {
         return null;
      }
   }

   public RWSet writeSet(SootMethod method, Stmt stmt) {
      RWSet ret = null;
      Iterator targets = this.tt.iterator((Unit)stmt);

      while(targets.hasNext()) {
         SootMethod target = (SootMethod)targets.next();
         if (target.isNative()) {
            if (ret == null) {
               ret = new SiteRWSet();
            }

            ret.setCallsNative();
         } else if (target.isConcrete()) {
            RWSet ntw = this.nonTransitiveWriteSet(target);
            if (ntw != null) {
               if (ret == null) {
                  ret = new SiteRWSet();
               }

               ret.union(ntw);
            }
         }
      }

      if (ret == null) {
         return this.ntWriteSet(method, stmt);
      } else {
         ret.union(this.ntWriteSet(method, stmt));
         return ret;
      }
   }

   protected RWSet addValue(Value v, SootMethod m, Stmt s) {
      RWSet ret = null;
      PointsToSet base;
      if (v instanceof InstanceFieldRef) {
         InstanceFieldRef ifr = (InstanceFieldRef)v;
         base = this.pa.reachingObjects((Local)ifr.getBase());
         ret = new StmtRWSet();
         ret.addFieldRef(base, ifr.getField());
      } else if (v instanceof StaticFieldRef) {
         StaticFieldRef sfr = (StaticFieldRef)v;
         ret = new StmtRWSet();
         ret.addGlobal(sfr.getField());
      } else if (v instanceof ArrayRef) {
         ArrayRef ar = (ArrayRef)v;
         base = this.pa.reachingObjects((Local)ar.getBase());
         ret = new StmtRWSet();
         ret.addFieldRef(base, "ARRAY_ELEMENTS_NODE");
      }

      return ret;
   }

   public String toString() {
      return "SideEffectAnalysis: PA=" + this.pa + " CG=" + this.cg;
   }
}
