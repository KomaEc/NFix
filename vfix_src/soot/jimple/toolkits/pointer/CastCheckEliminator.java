package soot.jimple.toolkits.pointer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.NeExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;

public class CastCheckEliminator extends ForwardBranchedFlowAnalysis<LocalTypeSet> {
   Map unitToKill = new HashMap();
   Map unitToGenFallThrough = new HashMap();
   Map unitToGenBranch = new HashMap();
   LocalTypeSet emptySet;

   public CastCheckEliminator(BriefUnitGraph cfg) {
      super(cfg);
      this.makeInitialSet();
      this.doAnalysis();
      this.tagCasts();
   }

   protected void tagCasts() {
      Iterator sIt = ((UnitGraph)this.graph).getBody().getUnits().iterator();

      while(sIt.hasNext()) {
         Stmt s = (Stmt)sIt.next();
         if (s instanceof AssignStmt) {
            AssignStmt as = (AssignStmt)s;
            Value rhs = as.getRightOp();
            if (rhs instanceof CastExpr) {
               CastExpr cast = (CastExpr)rhs;
               Type t = cast.getCastType();
               if (t instanceof RefType) {
                  if (cast.getOp() instanceof Local) {
                     Local l = (Local)cast.getOp();
                     LocalTypeSet set = (LocalTypeSet)this.getFlowBefore(s);
                     s.addTag(new CastCheckTag(set.get(set.indexOf(l, (RefType)t))));
                  } else {
                     NullConstant nc = (NullConstant)cast.getOp();
                     s.addTag(new CastCheckTag(true));
                  }
               }
            }
         }
      }

   }

   protected void makeInitialSet() {
      Collection<Local> locals = ((UnitGraph)this.graph).getBody().getLocals();
      List<Local> refLocals = new ArrayList();
      Iterator lIt = locals.iterator();

      while(lIt.hasNext()) {
         Local l = (Local)lIt.next();
         if (l.getType() instanceof RefType) {
            refLocals.add(l);
         }
      }

      List<Type> types = new ArrayList();
      Iterator sIt = ((UnitGraph)this.graph).getBody().getUnits().iterator();

      while(sIt.hasNext()) {
         Stmt s = (Stmt)sIt.next();
         if (s instanceof AssignStmt) {
            AssignStmt as = (AssignStmt)s;
            Value rhs = as.getRightOp();
            if (rhs instanceof CastExpr) {
               Type t = ((CastExpr)rhs).getCastType();
               if (t instanceof RefType && !types.contains(t)) {
                  types.add(t);
               }
            }
         }
      }

      this.emptySet = new LocalTypeSet(refLocals, types);
   }

   protected LocalTypeSet newInitialFlow() {
      LocalTypeSet ret = (LocalTypeSet)this.emptySet.clone();
      ret.setAllBits();
      return ret;
   }

   protected void flowThrough(LocalTypeSet in, Unit unit, List<LocalTypeSet> outFallValues, List<LocalTypeSet> outBranchValues) {
      LocalTypeSet out = (LocalTypeSet)in.clone();
      LocalTypeSet outBranch = out;
      Stmt stmt = (Stmt)unit;
      Iterator it = stmt.getDefBoxes().iterator();

      Value lhs;
      while(it.hasNext()) {
         ValueBox b = (ValueBox)it.next();
         lhs = b.getValue();
         if (lhs instanceof Local && lhs.getType() instanceof RefType) {
            out.killLocal((Local)lhs);
         }
      }

      if (stmt instanceof AssignStmt) {
         AssignStmt astmt = (AssignStmt)stmt;
         Value rhs = astmt.getRightOp();
         lhs = astmt.getLeftOp();
         if (lhs instanceof Local && rhs.getType() instanceof RefType) {
            Local l = (Local)lhs;
            if (rhs instanceof NewExpr) {
               out.localMustBeSubtypeOf(l, (RefType)rhs.getType());
            } else if (rhs instanceof CastExpr) {
               CastExpr cast = (CastExpr)rhs;
               Type castType = cast.getCastType();
               if (castType instanceof RefType && cast.getOp() instanceof Local) {
                  RefType refType = (RefType)castType;
                  Local opLocal = (Local)cast.getOp();
                  out.localCopy(l, opLocal);
                  out.localMustBeSubtypeOf(l, refType);
                  out.localMustBeSubtypeOf(opLocal, refType);
               }
            } else if (rhs instanceof Local) {
               out.localCopy(l, (Local)rhs);
            }
         }
      } else if (stmt instanceof IfStmt) {
         IfStmt ifstmt = (IfStmt)stmt;
         if (this.graph.getPredsOf(stmt).size() == 1) {
            Object predecessor = this.graph.getPredsOf(stmt).get(0);
            if (predecessor instanceof AssignStmt) {
               AssignStmt pred = (AssignStmt)predecessor;
               if (pred.getRightOp() instanceof InstanceOfExpr) {
                  InstanceOfExpr iofexpr = (InstanceOfExpr)pred.getRightOp();
                  if (iofexpr.getCheckType() instanceof RefType && iofexpr.getOp() instanceof Local) {
                     ConditionExpr c = (ConditionExpr)ifstmt.getCondition();
                     if (c.getOp1().equals(pred.getLeftOp()) && c.getOp2() instanceof IntConstant && ((IntConstant)c.getOp2()).value == 0) {
                        if (c instanceof NeExpr) {
                           outBranch = (LocalTypeSet)out.clone();
                           outBranch.localMustBeSubtypeOf((Local)iofexpr.getOp(), (RefType)iofexpr.getCheckType());
                        } else if (c instanceof EqExpr) {
                           outBranch = (LocalTypeSet)out.clone();
                           out.localMustBeSubtypeOf((Local)iofexpr.getOp(), (RefType)iofexpr.getCheckType());
                        }
                     }
                  }
               }
            }
         }
      }

      it = outFallValues.iterator();

      while(it.hasNext()) {
         this.copy(out, (LocalTypeSet)it.next());
      }

      it = outBranchValues.iterator();

      while(it.hasNext()) {
         this.copy(outBranch, (LocalTypeSet)it.next());
      }

   }

   protected void copy(LocalTypeSet s, LocalTypeSet d) {
      d.and(s);
      d.or(s);
   }

   protected void merge(LocalTypeSet in1, LocalTypeSet in2, LocalTypeSet o) {
      o.setAllBits();
      o.and(in1);
      o.and(in2);
   }

   protected LocalTypeSet entryInitialFlow() {
      LocalTypeSet ret = (LocalTypeSet)this.emptySet.clone();
      return ret;
   }
}
