package soot.jimple.toolkits.annotation.nullcheck;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.PhaseOptions;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LengthExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.annotation.tags.NullCheckTag;
import soot.options.Options;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;
import soot.util.Chain;

public class NullPointerChecker extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(NullPointerChecker.class);
   private boolean isProfiling = false;
   private boolean enableOther = true;

   public NullPointerChecker(Singletons.Global g) {
   }

   public static NullPointerChecker v() {
      return G.v().soot_jimple_toolkits_annotation_nullcheck_NullPointerChecker();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      this.isProfiling = PhaseOptions.getBoolean(options, "profiling");
      this.enableOther = !PhaseOptions.getBoolean(options, "onlyarrayref");
      Date start = new Date();
      if (Options.v().verbose()) {
         logger.debug("[npc] Null pointer check for " + body.getMethod().getName() + " started on " + start);
      }

      BranchedRefVarsAnalysis analysis = new BranchedRefVarsAnalysis(new ExceptionalUnitGraph(body));
      SootClass counterClass = null;
      SootMethod increase = null;
      if (this.isProfiling) {
         counterClass = Scene.v().loadClassAndSupport("MultiCounter");
         increase = counterClass.getMethod("void increase(int)");
      }

      Chain<Unit> units = body.getUnits();
      Iterator stmtIt = units.snapshotIterator();

      while(stmtIt.hasNext()) {
         Stmt s = (Stmt)stmtIt.next();
         Value obj = null;
         if (s.containsArrayRef()) {
            ArrayRef aref = s.getArrayRef();
            obj = aref.getBase();
         } else if (this.enableOther) {
            if (s instanceof ThrowStmt) {
               obj = ((ThrowStmt)s).getOp();
            } else if (s instanceof MonitorStmt) {
               obj = ((MonitorStmt)s).getOp();
            } else {
               Iterator boxIt = s.getDefBoxes().iterator();

               ValueBox vBox;
               Value v;
               while(boxIt.hasNext()) {
                  vBox = (ValueBox)boxIt.next();
                  v = vBox.getValue();
                  if (v instanceof InstanceFieldRef) {
                     obj = ((InstanceFieldRef)v).getBase();
                     break;
                  }

                  if (v instanceof InstanceInvokeExpr) {
                     obj = ((InstanceInvokeExpr)v).getBase();
                     break;
                  }

                  if (v instanceof LengthExpr) {
                     obj = ((LengthExpr)v).getOp();
                     break;
                  }
               }

               boxIt = s.getUseBoxes().iterator();

               while(boxIt.hasNext()) {
                  vBox = (ValueBox)boxIt.next();
                  v = vBox.getValue();
                  if (v instanceof InstanceFieldRef) {
                     obj = ((InstanceFieldRef)v).getBase();
                     break;
                  }

                  if (v instanceof InstanceInvokeExpr) {
                     obj = ((InstanceInvokeExpr)v).getBase();
                     break;
                  }

                  if (v instanceof LengthExpr) {
                     obj = ((LengthExpr)v).getOp();
                     break;
                  }
               }
            }
         }

         if (obj != null) {
            FlowSet beforeSet = (FlowSet)analysis.getFlowBefore(s);
            int vInfo = analysis.anyRefInfo(obj, beforeSet);
            boolean needCheck = vInfo != 2;
            if (this.isProfiling) {
               int whichCounter = 5;
               if (!needCheck) {
                  whichCounter = 6;
               }

               units.insertBefore((Object)Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(increase.makeRef(), (Value)IntConstant.v(whichCounter))), s);
            }

            Tag nullTag = new NullCheckTag(needCheck);
            s.addTag(nullTag);
         }
      }

      Date finish = new Date();
      if (Options.v().verbose()) {
         long runtime = finish.getTime() - start.getTime();
         long mins = runtime / 60000L;
         long secs = runtime % 60000L / 1000L;
         logger.debug("[npc] Null pointer checker finished. It took " + mins + " mins and " + secs + " secs.");
      }

   }
}
