package soot.jimple.toolkits.reflection;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;

public class ConstantInvokeMethodBaseTransformer extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(ConstantInvokeMethodBaseTransformer.class);
   private static final String INVOKE_SIG = "<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>";

   public ConstantInvokeMethodBaseTransformer(Singletons.Global g) {
   }

   public static ConstantInvokeMethodBaseTransformer v() {
      return G.v().soot_jimple_toolkits_reflection_ConstantInvokeMethodBaseTransformer();
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      boolean verbose = options.containsKey("verbose");
      Iterator var4 = Scene.v().getApplicationClasses().iterator();

      while(true) {
         SootClass sootClass;
         do {
            if (!var4.hasNext()) {
               return;
            }

            sootClass = (SootClass)var4.next();
         } while(sootClass.resolvingLevel() < 3);

         Iterator var6 = sootClass.getMethods().iterator();

         while(var6.hasNext()) {
            SootMethod sootMethod = (SootMethod)var6.next();
            Body body = sootMethod.retrieveActiveBody();
            Iterator iterator = body.getUnits().snapshotIterator();

            while(iterator.hasNext()) {
               Stmt u = (Stmt)iterator.next();
               if (u.containsInvokeExpr()) {
                  InvokeExpr invokeExpr = u.getInvokeExpr();
                  if (invokeExpr.getMethod().getSignature().equals("<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>") && invokeExpr.getArg(0) instanceof StringConstant) {
                     StringConstant constant = (StringConstant)invokeExpr.getArg(0);
                     Local newLocal = Jimple.v().newLocal("sc" + body.getLocalCount(), constant.getType());
                     body.getLocals().add(newLocal);
                     body.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(newLocal, constant), (Unit)u);
                     invokeExpr.setArg(0, newLocal);
                     if (verbose) {
                        logger.debug("Replaced constant base object of Method.invoke() by local in: " + sootMethod.toString());
                     }
                  }
               }
            }
         }
      }
   }
}
