package soot.jimple.toolkits.annotation.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.Expr;
import soot.jimple.GotoStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NaiveSideEffectTester;
import soot.jimple.NewExpr;
import soot.jimple.Stmt;
import soot.tagkit.ColorTag;
import soot.tagkit.LoopInvariantTag;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.SmartLocalDefs;
import soot.toolkits.scalar.SmartLocalDefsPool;

public class LoopInvariantFinder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LoopInvariantFinder.class);
   private ArrayList constants;

   public LoopInvariantFinder(Singletons.Global g) {
   }

   public static LoopInvariantFinder v() {
      return G.v().soot_jimple_toolkits_annotation_logic_LoopInvariantFinder();
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      SmartLocalDefs sld = SmartLocalDefsPool.v().getSmartLocalDefsFor(b);
      UnitGraph g = sld.getGraph();
      NaiveSideEffectTester nset = new NaiveSideEffectTester();
      LoopFinder lf = new LoopFinder();
      lf.internalTransform(b, phaseName, options);
      Collection<Loop> loops = lf.loops();
      this.constants = new ArrayList();
      if (!loops.isEmpty()) {
         Iterator lIt = loops.iterator();

         while(lIt.hasNext()) {
            Loop loop = (Loop)lIt.next();
            Stmt header = loop.getHead();
            Collection<Stmt> loopStmts = loop.getLoopStatements();
            Iterator bIt = loopStmts.iterator();

            while(bIt.hasNext()) {
               Stmt tStmt = (Stmt)bIt.next();
               this.handleLoopBodyStmt(tStmt, nset, loopStmts);
            }
         }

      }
   }

   private void handleLoopBodyStmt(Stmt s, NaiveSideEffectTester nset, Collection<Stmt> loopStmts) {
      if (s instanceof DefinitionStmt) {
         DefinitionStmt ds = (DefinitionStmt)s;
         if (ds.getLeftOp() instanceof Local && ds.getRightOp() instanceof Constant) {
            if (!this.constants.contains(ds.getLeftOp())) {
               this.constants.add(ds.getLeftOp());
            } else {
               this.constants.remove(ds.getLeftOp());
            }
         }
      }

      if (!(s instanceof GotoStmt)) {
         if (!(s instanceof InvokeStmt)) {
            logger.debug("s : " + s + " use boxes: " + s.getUseBoxes() + " def boxes: " + s.getDefBoxes());
            Iterator useBoxesIt = s.getUseBoxes().iterator();
            boolean result = true;

            label94:
            while(useBoxesIt.hasNext()) {
               ValueBox vb = (ValueBox)useBoxesIt.next();
               Value v = vb.getValue();
               if (v instanceof NewExpr) {
                  result = false;
                  logger.debug("break uses: due to new expr");
                  break;
               }

               if (v instanceof InvokeExpr) {
                  result = false;
                  logger.debug("break uses: due to invoke expr");
                  break;
               }

               if (!(v instanceof Expr)) {
                  logger.debug("test: " + v + " of kind: " + v.getClass());
                  Iterator loopStmtsIt = loopStmts.iterator();

                  while(loopStmtsIt.hasNext()) {
                     Stmt next = (Stmt)loopStmtsIt.next();
                     if (nset.unitCanWriteTo(next, v) && !this.isConstant(next)) {
                        logger.debug("result = false unit can be written to by: " + next);
                        result = false;
                        break label94;
                     }
                  }
               }
            }

            Iterator defBoxesIt = s.getDefBoxes().iterator();

            label76:
            while(defBoxesIt.hasNext()) {
               ValueBox vb = (ValueBox)defBoxesIt.next();
               Value v = vb.getValue();
               if (v instanceof NewExpr) {
                  result = false;
                  logger.debug("break defs due to new");
                  break;
               }

               if (v instanceof InvokeExpr) {
                  result = false;
                  logger.debug("break defs due to invoke");
                  break;
               }

               if (!(v instanceof Expr)) {
                  logger.debug("test: " + v + " of kind: " + v.getClass());
                  Iterator loopStmtsIt = loopStmts.iterator();

                  while(loopStmtsIt.hasNext()) {
                     Stmt next = (Stmt)loopStmtsIt.next();
                     if (!next.equals(s) && nset.unitCanWriteTo(next, v) && !this.isConstant(next)) {
                        logger.debug("result false: unit can be written to by: " + next);
                        result = false;
                        break label76;
                     }
                  }
               }
            }

            logger.debug("stmt: " + s + " result: " + result);
            if (result) {
               s.addTag(new LoopInvariantTag("is loop invariant"));
               s.addTag(new ColorTag(0, "Loop Invariant Analysis"));
            }

         }
      }
   }

   private boolean isConstant(Stmt s) {
      if (s instanceof DefinitionStmt) {
         DefinitionStmt ds = (DefinitionStmt)s;
         if (this.constants.contains(ds.getLeftOp())) {
            return true;
         }
      }

      return false;
   }
}
