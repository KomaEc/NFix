package soot.jimple.toolkits.scalar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.NullType;
import soot.RefLikeType;
import soot.Scene;
import soot.Singletons;
import soot.Timers;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.options.CPOptions;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.util.Chain;

public class CopyPropagator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(CopyPropagator.class);
   protected ThrowAnalysis throwAnalysis = null;
   protected boolean forceOmitExceptingUnitEdges = false;

   public CopyPropagator(Singletons.Global g) {
   }

   public CopyPropagator(ThrowAnalysis ta) {
      this.throwAnalysis = ta;
   }

   public CopyPropagator(ThrowAnalysis ta, boolean forceOmitExceptingUnitEdges) {
      this.throwAnalysis = ta;
      this.forceOmitExceptingUnitEdges = forceOmitExceptingUnitEdges;
   }

   public static CopyPropagator v() {
      return G.v().soot_jimple_toolkits_scalar_CopyPropagator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> opts) {
      CPOptions options = new CPOptions(opts);
      StmtBody stmtBody = (StmtBody)b;
      int fastCopyPropagationCount = 0;
      int slowCopyPropagationCount = 0;
      if (Options.v().verbose()) {
         logger.debug("[" + stmtBody.getMethod().getName() + "] Propagating copies...");
      }

      if (Options.v().time()) {
         Timers.v().propagatorTimer.start();
      }

      Chain<Unit> units = stmtBody.getUnits();
      Map<Local, Integer> localToDefCount = new HashMap();
      Iterator var10 = units.iterator();

      while(var10.hasNext()) {
         Unit u = (Unit)var10.next();
         Stmt s = (Stmt)u;
         if (s instanceof DefinitionStmt && ((DefinitionStmt)s).getLeftOp() instanceof Local) {
            Local l = (Local)((DefinitionStmt)s).getLeftOp();
            if (!localToDefCount.containsKey(l)) {
               localToDefCount.put(l, new Integer(1));
            } else {
               localToDefCount.put(l, new Integer((Integer)localToDefCount.get(l) + 1));
            }
         }
      }

      if (this.throwAnalysis == null) {
         this.throwAnalysis = Scene.v().getDefaultThrowAnalysis();
      }

      if (!this.forceOmitExceptingUnitEdges) {
         this.forceOmitExceptingUnitEdges = Options.v().omit_excepting_unit_edges();
      }

      UnitGraph graph = new ExceptionalUnitGraph(stmtBody, this.throwAnalysis, this.forceOmitExceptingUnitEdges);
      LocalDefs localDefs = LocalDefs.Factory.newLocalDefs((UnitGraph)graph);
      Iterator stmtIt = (new PseudoTopologicalOrderer()).newList(graph, false).iterator();

      label199:
      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         Iterator var14 = stmt.getUseBoxes().iterator();

         while(true) {
            while(true) {
               ValueBox useBox;
               Local l;
               List defsOfUse;
               boolean propagateDef;
               do {
                  do {
                     do {
                        if (!var14.hasNext()) {
                           continue label199;
                        }

                        useBox = (ValueBox)var14.next();
                     } while(!(useBox.getValue() instanceof Local));

                     l = (Local)useBox.getValue();
                  } while(!(l.getType() instanceof NullType) && (options.only_regular_locals() && l.getName().startsWith("$") || options.only_stack_locals() && !l.getName().startsWith("$")));

                  defsOfUse = localDefs.getDefsOfAt(l, stmt);
                  propagateDef = defsOfUse.size() == 1;
                  if (!propagateDef && defsOfUse.size() > 0) {
                     boolean agrees = true;
                     Constant constVal = null;

                     boolean defAgrees;
                     for(Iterator var21 = defsOfUse.iterator(); var21.hasNext(); agrees &= defAgrees) {
                        Unit defUnit = (Unit)var21.next();
                        defAgrees = false;
                        if (defUnit instanceof AssignStmt) {
                           AssignStmt assign = (AssignStmt)defUnit;
                           if (assign.getRightOp() instanceof Constant) {
                              if (constVal == null) {
                                 constVal = (Constant)assign.getRightOp();
                                 defAgrees = true;
                              } else if (constVal.equals(assign.getRightOp())) {
                                 defAgrees = true;
                              }
                           }
                        }
                     }

                     propagateDef = agrees;
                  }
               } while(!propagateDef);

               DefinitionStmt def = (DefinitionStmt)defsOfUse.get(0);
               if (def.getRightOp() instanceof Constant) {
                  if (useBox.canContainValue(def.getRightOp())) {
                     useBox.setValue(def.getRightOp());
                  }
               } else if (def.getRightOp() instanceof CastExpr) {
                  CastExpr ce = (CastExpr)def.getRightOp();
                  if (ce.getCastType() instanceof RefLikeType) {
                     boolean isConstNull = ce.getOp() instanceof IntConstant && ((IntConstant)ce.getOp()).value == 0;
                     isConstNull |= ce.getOp() instanceof LongConstant && ((LongConstant)ce.getOp()).value == 0L;
                     if (isConstNull && useBox.canContainValue(NullConstant.v())) {
                        useBox.setValue(NullConstant.v());
                     }
                  }
               } else if (def.getRightOp() instanceof Local) {
                  Local m = (Local)def.getRightOp();
                  if (l != m) {
                     Integer defCount = (Integer)localToDefCount.get(m);
                     if (defCount == null || defCount == 0) {
                        throw new RuntimeException("Variable " + m + " used without definition!");
                     }

                     if (defCount == 1) {
                        useBox.setValue(m);
                        ++fastCopyPropagationCount;
                     } else {
                        List<Unit> path = graph.getExtendedBasicBlockPathBetween(def, stmt);
                        if (path != null) {
                           Iterator<Unit> pathIt = path.iterator();
                           pathIt.next();
                           boolean isRedefined = false;

                           while(pathIt.hasNext()) {
                              Stmt s = (Stmt)pathIt.next();
                              if (stmt == s) {
                                 break;
                              }

                              if (s instanceof DefinitionStmt && ((DefinitionStmt)s).getLeftOp() == m) {
                                 isRedefined = true;
                                 break;
                              }
                           }

                           if (!isRedefined) {
                              useBox.setValue(m);
                              ++slowCopyPropagationCount;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + stmtBody.getMethod().getName() + "]     Propagated: " + fastCopyPropagationCount + " fast copies  " + slowCopyPropagationCount + " slow copies");
      }

      if (Options.v().time()) {
         Timers.v().propagatorTimer.end();
      }

   }
}
