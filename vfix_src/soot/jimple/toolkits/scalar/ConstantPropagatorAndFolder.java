package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.NullConstant;
import soot.jimple.NumericConstant;
import soot.jimple.StringConstant;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.Orderer;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;

public class ConstantPropagatorAndFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(ConstantPropagatorAndFolder.class);

   public ConstantPropagatorAndFolder(Singletons.Global g) {
   }

   public static ConstantPropagatorAndFolder v() {
      return G.v().soot_jimple_toolkits_scalar_ConstantPropagatorAndFolder();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int numFolded = 0;
      int numPropagated = 0;
      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "] Propagating and folding constants...");
      }

      UnitGraph g = new ExceptionalUnitGraph(b);
      LocalDefs localDefs = LocalDefs.Factory.newLocalDefs((UnitGraph)g);
      Orderer<Unit> orderer = new PseudoTopologicalOrderer();
      Iterator var9 = orderer.newList(g, false).iterator();

      label72:
      while(var9.hasNext()) {
         Unit u = (Unit)var9.next();
         Iterator var11 = u.getUseBoxes().iterator();

         while(true) {
            while(true) {
               ValueBox useBox;
               List defsOfUse;
               do {
                  Value value;
                  do {
                     if (!var11.hasNext()) {
                        var11 = u.getUseBoxes().iterator();

                        while(var11.hasNext()) {
                           useBox = (ValueBox)var11.next();
                           value = useBox.getValue();
                           if (!(value instanceof Constant) && Evaluator.isValueConstantValued(value)) {
                              Value constValue = Evaluator.getConstantValueOf(value);
                              if (useBox.canContainValue(constValue)) {
                                 useBox.setValue(constValue);
                                 ++numFolded;
                              }
                           }
                        }
                        continue label72;
                     }

                     useBox = (ValueBox)var11.next();
                     value = useBox.getValue();
                  } while(!(value instanceof Local));

                  Local local = (Local)value;
                  defsOfUse = localDefs.getDefsOfAt(local, u);
               } while(defsOfUse.size() != 1);

               DefinitionStmt defStmt = (DefinitionStmt)defsOfUse.get(0);
               Value rhs = defStmt.getRightOp();
               if (!(rhs instanceof NumericConstant) && !(rhs instanceof StringConstant) && !(rhs instanceof NullConstant)) {
                  if (rhs instanceof CastExpr) {
                     CastExpr ce = (CastExpr)rhs;
                     if (ce.getCastType() instanceof RefType && ce.getOp() instanceof NullConstant) {
                        defStmt.getRightOpBox().setValue(NullConstant.v());
                        ++numPropagated;
                     }
                  }
               } else if (useBox.canContainValue(rhs)) {
                  useBox.setValue(rhs);
                  ++numPropagated;
               }
            }
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + b.getMethod().getName() + "]     Propagated: " + numPropagated + ", Folded:  " + numFolded);
      }

   }
}
