package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.EquivalentValue;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class EqualLocalsAnalysis extends ForwardFlowAnalysis {
   Local l = null;
   Stmt s = null;

   public EqualLocalsAnalysis(UnitGraph g) {
      super(g);
   }

   public List getCopiesOfAt(Local l, Stmt s) {
      this.l = l;
      this.s = s;
      this.doAnalysis();
      FlowSet fs = (FlowSet)this.getFlowBefore(s);
      List aliasList = new ArrayList(fs.size());
      Iterator var5 = fs.iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         aliasList.add(o);
      }

      return aliasList.contains(new EquivalentValue(l)) ? aliasList : new ArrayList();
   }

   protected void merge(Object in1, Object in2, Object out) {
      FlowSet inSet1 = (FlowSet)in1;
      FlowSet inSet2 = (FlowSet)in2;
      FlowSet outSet = (FlowSet)out;
      inSet1.intersection(inSet2, outSet);
   }

   protected void flowThrough(Object inValue, Object unit, Object outValue) {
      FlowSet in = (FlowSet)inValue;
      FlowSet out = (FlowSet)outValue;
      Stmt stmt = (Stmt)unit;
      in.copy(out);
      List<EquivalentValue> newDefs = new ArrayList();
      Iterator newDefBoxesIt = stmt.getDefBoxes().iterator();

      while(newDefBoxesIt.hasNext()) {
         newDefs.add(new EquivalentValue(((ValueBox)newDefBoxesIt.next()).getValue()));
      }

      if (newDefs.contains(new EquivalentValue(this.l))) {
         List<Object> existingDefStmts = new ArrayList();
         Iterator outIt = out.iterator();

         while(outIt.hasNext()) {
            Object o = outIt.next();
            if (o instanceof Stmt) {
               existingDefStmts.add(o);
            }
         }

         out.clear();
         Iterator newDefsIt = newDefs.iterator();

         while(newDefsIt.hasNext()) {
            out.add(newDefsIt.next());
         }

         if (stmt instanceof DefinitionStmt && !stmt.containsInvokeExpr() && !(stmt instanceof IdentityStmt)) {
            out.add(new EquivalentValue(((DefinitionStmt)stmt).getRightOp()));
         }

         Iterator existingDefIt = existingDefStmts.iterator();

         while(true) {
            while(true) {
               Stmt s;
               ArrayList sNewDefs;
               do {
                  if (!existingDefIt.hasNext()) {
                     return;
                  }

                  s = (Stmt)existingDefIt.next();
                  sNewDefs = new ArrayList();
                  Iterator sNewDefBoxesIt = s.getDefBoxes().iterator();

                  while(sNewDefBoxesIt.hasNext()) {
                     sNewDefs.add(((ValueBox)sNewDefBoxesIt.next()).getValue());
                  }
               } while(!(s instanceof DefinitionStmt));

               Iterator sNewDefsIt;
               if (out.contains(new EquivalentValue(((DefinitionStmt)s).getRightOp()))) {
                  sNewDefsIt = sNewDefs.iterator();

                  while(sNewDefsIt.hasNext()) {
                     out.add(new EquivalentValue((Value)sNewDefsIt.next()));
                  }
               } else {
                  sNewDefsIt = sNewDefs.iterator();

                  while(sNewDefsIt.hasNext()) {
                     out.remove(new EquivalentValue((Value)sNewDefsIt.next()));
                  }
               }
            }
         }
      } else if (stmt instanceof DefinitionStmt) {
         if (out.contains(new EquivalentValue(this.l))) {
            Iterator newDefsIt;
            if (out.contains(new EquivalentValue(((DefinitionStmt)stmt).getRightOp()))) {
               newDefsIt = newDefs.iterator();

               while(newDefsIt.hasNext()) {
                  out.add(newDefsIt.next());
               }
            } else {
               newDefsIt = newDefs.iterator();

               while(newDefsIt.hasNext()) {
                  out.remove(newDefsIt.next());
               }
            }
         } else {
            out.add(stmt);
         }
      }

   }

   protected void copy(Object source, Object dest) {
      FlowSet sourceSet = (FlowSet)source;
      FlowSet destSet = (FlowSet)dest;
      sourceSet.copy(destSet);
   }

   protected Object entryInitialFlow() {
      return new ArraySparseSet();
   }

   protected Object newInitialFlow() {
      return new ArraySparseSet();
   }
}
