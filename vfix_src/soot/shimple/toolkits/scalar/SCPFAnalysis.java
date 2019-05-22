package soot.shimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Local;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardBranchedFlowAnalysis;
import soot.toolkits.scalar.Pair;

class SCPFAnalysis extends ForwardBranchedFlowAnalysis {
   protected FlowSet emptySet = new ArraySparseSet();
   protected Map<Local, Constant> localToConstant;
   protected Map<Stmt, GotoStmt> stmtToReplacement = new HashMap();
   protected List<IfStmt> deadStmts = new ArrayList();

   public Map<Local, Constant> getResults() {
      return this.localToConstant;
   }

   public List<IfStmt> getDeadStmts() {
      return this.deadStmts;
   }

   public Map<Stmt, GotoStmt> getStmtsToReplace() {
      return this.stmtToReplacement;
   }

   public SCPFAnalysis(UnitGraph graph) {
      super(graph);
      Collection<Local> locals = graph.getBody().getLocals();
      Iterator<Local> localsIt = locals.iterator();
      this.localToConstant = new HashMap(graph.size() * 2 + 1, 0.7F);

      while(localsIt.hasNext()) {
         Local local = (Local)localsIt.next();
         this.localToConstant.put(local, SEvaluator.TopConstant.v());
      }

      this.doAnalysis();
   }

   protected boolean treatTrapHandlersAsEntries() {
      return true;
   }

   protected Object entryInitialFlow() {
      FlowSet entrySet = this.emptySet.emptySet();
      entrySet.add(SEvaluator.TopConstant.v());
      return entrySet;
   }

   protected Object newInitialFlow() {
      return this.emptySet.emptySet();
   }

   protected void merge(Object in1, Object in2, Object out) {
      FlowSet fin1 = (FlowSet)in1;
      FlowSet fin2 = (FlowSet)in2;
      FlowSet fout = (FlowSet)out;
      fin1.union(fin2, fout);
   }

   protected void copy(Object source, Object dest) {
      FlowSet fource = (FlowSet)source;
      FlowSet fest = (FlowSet)dest;
      fource.copy(fest);
   }

   protected void flowThrough(Object in, Unit s, List fallOut, List branchOuts) {
      FlowSet fin = ((FlowSet)in).clone();
      if (!fin.isEmpty()) {
         Pair pair = this.processDefinitionStmt(s);
         if (pair != null) {
            fin.add(pair);
         }

         if (!s.branches() && s.fallsThrough()) {
            Iterator fallOutIt = fallOut.iterator();

            while(fallOutIt.hasNext()) {
               FlowSet fallSet = (FlowSet)fallOutIt.next();
               fallSet.union(fin);
            }

         } else {
            boolean conservative = true;
            boolean fall = false;
            boolean branch = false;
            FlowSet oneBranch = null;
            Value keyV;
            Constant keyC;
            IntConstant branchBox;
            GotoStmt gotoStmt;
            if (s instanceof IfStmt) {
               IfStmt ifStmt = (IfStmt)s;
               keyV = ifStmt.getCondition();
               keyC = SEvaluator.getFuzzyConstantValueOf(keyV, this.localToConstant);
               if (keyC instanceof SEvaluator.BottomConstant) {
                  this.deadStmts.remove(ifStmt);
                  this.stmtToReplacement.remove(ifStmt);
               } else {
                  if (keyC instanceof SEvaluator.TopConstant) {
                     return;
                  }

                  conservative = false;
                  Constant trueC = IntConstant.v(1);
                  branchBox = IntConstant.v(0);
                  if (keyC.equals(trueC)) {
                     branch = true;
                     gotoStmt = Jimple.v().newGotoStmt(ifStmt.getTargetBox());
                     this.stmtToReplacement.put(ifStmt, gotoStmt);
                  }

                  if (keyC.equals(branchBox)) {
                     fall = true;
                     this.deadStmts.add(ifStmt);
                  }
               }
            }

            int index;
            if (s instanceof TableSwitchStmt) {
               TableSwitchStmt table = (TableSwitchStmt)s;
               keyV = table.getKey();
               keyC = SEvaluator.getFuzzyConstantValueOf(keyV, this.localToConstant);
               if (keyC instanceof SEvaluator.BottomConstant) {
                  this.stmtToReplacement.remove(table);
               } else {
                  if (keyC instanceof SEvaluator.TopConstant) {
                     return;
                  }

                  if (keyC instanceof IntConstant) {
                     conservative = false;
                     index = ((IntConstant)keyC).value;
                     int low = table.getLowIndex();
                     int high = table.getHighIndex();
                     int index = index - low;
                     UnitBox branchBox = null;
                     if (index >= 0 && index <= high) {
                        branchBox = table.getTargetBox(index);
                     } else {
                        branchBox = table.getDefaultTargetBox();
                     }

                     GotoStmt gotoStmt = Jimple.v().newGotoStmt(branchBox);
                     this.stmtToReplacement.put(table, gotoStmt);
                     List unitBoxes = table.getUnitBoxes();
                     int setIndex = unitBoxes.indexOf(branchBox);
                     oneBranch = (FlowSet)branchOuts.get(setIndex);
                  }
               }
            }

            if (s instanceof LookupSwitchStmt) {
               LookupSwitchStmt lookup = (LookupSwitchStmt)s;
               keyV = lookup.getKey();
               keyC = SEvaluator.getFuzzyConstantValueOf(keyV, this.localToConstant);
               if (keyC instanceof SEvaluator.BottomConstant) {
                  this.stmtToReplacement.remove(lookup);
               } else {
                  if (keyC instanceof SEvaluator.TopConstant) {
                     return;
                  }

                  if (keyC instanceof IntConstant) {
                     conservative = false;
                     index = lookup.getLookupValues().indexOf(keyC);
                     branchBox = null;
                     UnitBox branchBox;
                     if (index == -1) {
                        branchBox = lookup.getDefaultTargetBox();
                     } else {
                        branchBox = lookup.getTargetBox(index);
                     }

                     gotoStmt = Jimple.v().newGotoStmt(branchBox);
                     this.stmtToReplacement.put(lookup, gotoStmt);
                     List unitBoxes = lookup.getUnitBoxes();
                     int setIndex = unitBoxes.indexOf(branchBox);
                     oneBranch = (FlowSet)branchOuts.get(setIndex);
                  }
               }
            }

            if (conservative) {
               fall = s.fallsThrough();
               branch = s.branches();
            }

            Iterator branchOutsIt;
            FlowSet branchSet;
            if (fall) {
               branchOutsIt = fallOut.iterator();

               while(branchOutsIt.hasNext()) {
                  branchSet = (FlowSet)branchOutsIt.next();
                  branchSet.union(fin);
               }
            }

            if (branch) {
               branchOutsIt = branchOuts.iterator();

               while(branchOutsIt.hasNext()) {
                  branchSet = (FlowSet)branchOutsIt.next();
                  branchSet.union(fin);
               }
            }

            if (oneBranch != null) {
               oneBranch.union(fin);
            }

         }
      }
   }

   protected Pair processDefinitionStmt(Unit u) {
      if (!(u instanceof DefinitionStmt)) {
         return null;
      } else {
         DefinitionStmt dStmt = (DefinitionStmt)u;
         Value rightOp = dStmt.getLeftOp();
         if (!(rightOp instanceof Local)) {
            return null;
         } else {
            Local local = (Local)rightOp;
            rightOp = dStmt.getRightOp();
            Constant constant = SEvaluator.getFuzzyConstantValueOf(rightOp, this.localToConstant);
            return !this.merge(local, constant) ? null : new Pair(u, this.localToConstant.get(local));
         }
      }
   }

   protected boolean merge(Local local, Constant constant) {
      Constant current = (Constant)this.localToConstant.get(local);
      if (current instanceof SEvaluator.BottomConstant) {
         return false;
      } else if (current instanceof SEvaluator.TopConstant) {
         this.localToConstant.put(local, constant);
         return true;
      } else if (current.equals(constant)) {
         return false;
      } else {
         this.localToConstant.put(local, SEvaluator.BottomConstant.v());
         return true;
      }
   }
}
