package soot.jimple.toolkits.scalar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.ValueBox;
import soot.jimple.DefinitionStmt;
import soot.jimple.Stmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.BackwardFlowAnalysis;
import soot.toolkits.scalar.FlowSet;

public class CommonPrecedingEqualValueAnalysis extends BackwardFlowAnalysis {
   private static final Logger logger = LoggerFactory.getLogger(CommonPrecedingEqualValueAnalysis.class);
   Map unitToAliasSet = null;
   Stmt s = null;

   public CommonPrecedingEqualValueAnalysis(UnitGraph g) {
      super(g);
   }

   public List getCommonAncestorValuesOf(Map unitToAliasSet, Stmt s) {
      this.unitToAliasSet = unitToAliasSet;
      this.s = s;
      this.doAnalysis();
      FlowSet fs = (FlowSet)this.getFlowAfter(s);
      List ancestorList = new ArrayList(fs.size());
      Iterator var5 = fs.iterator();

      while(var5.hasNext()) {
         Object o = var5.next();
         ancestorList.add(o);
      }

      return ancestorList;
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

      if (this.unitToAliasSet.keySet().contains(stmt)) {
         out.clear();
         List aliases = (List)this.unitToAliasSet.get(stmt);
         Iterator aliasIt = aliases.iterator();

         while(aliasIt.hasNext()) {
            out.add(aliasIt.next());
         }
      } else if (stmt instanceof DefinitionStmt) {
         Iterator newDefsIt = newDefs.iterator();

         while(newDefsIt.hasNext()) {
            out.remove(newDefsIt.next());
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
