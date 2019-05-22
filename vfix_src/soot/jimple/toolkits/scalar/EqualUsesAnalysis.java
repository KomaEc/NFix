package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

/** @deprecated */
@Deprecated
public class EqualUsesAnalysis extends ForwardFlowAnalysis<Unit, FlowSet> {
   private static final Logger logger = LoggerFactory.getLogger(EqualUsesAnalysis.class);
   Map<Stmt, Local> stmtToLocal;
   Set<Stmt> useStmts = null;
   Collection<Local> useLocals = null;
   List boundaryStmts = null;
   List<Stmt> redefStmts = null;
   Map<Stmt, List> firstUseToAliasSet = null;
   EqualLocalsAnalysis el;

   public EqualUsesAnalysis(UnitGraph g) {
      super(g);
      this.el = new EqualLocalsAnalysis(g);
   }

   public boolean areEqualUses(Stmt firstStmt, Local firstLocal, Stmt secondStmt, Local secondLocal) {
      Map<Stmt, Local> stmtToLocal = new HashMap();
      stmtToLocal.put(firstStmt, firstLocal);
      stmtToLocal.put(secondStmt, secondLocal);
      return this.areEqualUses(stmtToLocal, new ArrayList());
   }

   public boolean areEqualUses(Stmt firstStmt, Local firstLocal, Stmt secondStmt, Local secondLocal, List boundaryStmts) {
      Map<Stmt, Local> stmtToLocal = new HashMap();
      stmtToLocal.put(firstStmt, firstLocal);
      stmtToLocal.put(secondStmt, secondLocal);
      return this.areEqualUses(stmtToLocal, boundaryStmts);
   }

   public boolean areEqualUses(Map<Stmt, Local> stmtToLocal) {
      return this.areEqualUses(stmtToLocal, new ArrayList());
   }

   public boolean areEqualUses(Map<Stmt, Local> stmtToLocal, List boundaryStmts) {
      this.stmtToLocal = stmtToLocal;
      this.useStmts = stmtToLocal.keySet();
      this.useLocals = stmtToLocal.values();
      this.boundaryStmts = boundaryStmts;
      this.redefStmts = new ArrayList();
      this.firstUseToAliasSet = new HashMap();
      this.doAnalysis();
      Iterator useIt = this.useStmts.iterator();

      Unit u;
      List aliases;
      do {
         if (!useIt.hasNext()) {
            return true;
         }

         u = (Unit)useIt.next();
         FlowSet fs = (FlowSet)this.getFlowBefore(u);
         Iterator redefIt = this.redefStmts.iterator();

         while(redefIt.hasNext()) {
            if (fs.contains(redefIt.next())) {
               return false;
            }
         }

         aliases = null;
         Iterator fsIt = fs.iterator();

         while(fsIt.hasNext()) {
            Object o = fsIt.next();
            if (o instanceof List) {
               aliases = (List)o;
            }
         }
      } while(aliases == null || aliases.contains(new EquivalentValue((Value)stmtToLocal.get(u))));

      return false;
   }

   public Map<Stmt, List> getFirstUseToAliasSet() {
      return this.firstUseToAliasSet;
   }

   protected void merge(FlowSet inSet1, FlowSet inSet2, FlowSet outSet) {
      inSet1.union(inSet2, outSet);
      List aliases1 = null;
      List aliases2 = null;
      Iterator outIt = outSet.iterator();

      while(outIt.hasNext()) {
         Object o = outIt.next();
         if (o instanceof List) {
            if (aliases1 == null) {
               aliases1 = (List)o;
            } else {
               aliases2 = (List)o;
            }
         }
      }

      if (aliases1 != null && aliases2 != null) {
         outSet.remove(aliases2);
         Iterator aliasIt = aliases1.iterator();

         while(aliasIt.hasNext()) {
            Object o = aliasIt.next();
            if (!aliases2.contains(o)) {
               aliasIt.remove();
            }
         }
      }

   }

   protected void flowThrough(FlowSet in, Unit unit, FlowSet out) {
      Stmt stmt = (Stmt)unit;
      in.copy(out);
      List<Value> newDefs = new ArrayList();
      Iterator newDefBoxesIt = stmt.getDefBoxes().iterator();

      while(newDefBoxesIt.hasNext()) {
         newDefs.add(((ValueBox)newDefBoxesIt.next()).getValue());
      }

      Iterator useLocalsIt = this.useLocals.iterator();

      while(true) {
         Local l;
         Iterator outIt;
         Object o;
         do {
            if (!useLocalsIt.hasNext()) {
               if (this.redefStmts.contains(stmt)) {
                  out.add(stmt);
               }

               if (this.boundaryStmts.contains(stmt)) {
                  out.clear();
               }

               if (this.useStmts.contains(stmt)) {
                  if (out.size() == 0) {
                     l = (Local)this.stmtToLocal.get(stmt);
                     List aliasList = this.el.getCopiesOfAt(l, stmt);
                     if (aliasList.size() == 0) {
                        aliasList.add(l);
                     }

                     List newAliasList = new ArrayList();
                     newAliasList.addAll(aliasList);
                     this.firstUseToAliasSet.put(stmt, newAliasList);
                     out.add(aliasList);
                  }

                  out.add(stmt);
               }

               if (stmt instanceof DefinitionStmt) {
                  List<EquivalentValue> aliases = null;
                  outIt = out.iterator();

                  while(outIt.hasNext()) {
                     o = outIt.next();
                     if (o instanceof List) {
                        aliases = (List)o;
                     }
                  }

                  if (aliases != null) {
                     Iterator newDefsIt;
                     if (aliases.contains(new EquivalentValue(((DefinitionStmt)stmt).getRightOp()))) {
                        newDefsIt = newDefs.iterator();

                        while(newDefsIt.hasNext()) {
                           aliases.add(new EquivalentValue((Value)newDefsIt.next()));
                        }
                     } else {
                        newDefsIt = newDefs.iterator();

                        while(newDefsIt.hasNext()) {
                           aliases.remove(new EquivalentValue((Value)newDefsIt.next()));
                        }
                     }
                  }
               }

               return;
            }

            l = (Local)useLocalsIt.next();
         } while(!newDefs.contains(l));

         outIt = out.iterator();

         while(outIt.hasNext()) {
            o = outIt.next();
            if (o instanceof Stmt) {
               Stmt s = (Stmt)o;
               if (this.stmtToLocal.get(s) == l) {
                  this.redefStmts.add(stmt);
               }
            }
         }
      }
   }

   protected void copy(FlowSet source, FlowSet dest) {
      source.copy(dest);
   }

   protected FlowSet entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected FlowSet newInitialFlow() {
      return new ArraySparseSet();
   }
}
