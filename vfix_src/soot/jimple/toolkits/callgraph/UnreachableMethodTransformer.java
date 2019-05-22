package soot.jimple.toolkits.callgraph;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StringConstant;

public class UnreachableMethodTransformer extends BodyTransformer {
   protected void internalTransform(Body b, String phaseName, Map options) {
      ReachableMethods reachableMethods = Scene.v().getReachableMethods();
      SootMethod method = b.getMethod();
      if (!reachableMethods.contains(method)) {
         JimpleBody body = (JimpleBody)method.getActiveBody();
         PatchingChain units = body.getUnits();
         List<Unit> list = new Vector();
         Local tmpRef = Jimple.v().newLocal("tmpRef", RefType.v("java.io.PrintStream"));
         body.getLocals().add(tmpRef);
         list.add(Jimple.v().newAssignStmt(tmpRef, Jimple.v().newStaticFieldRef(Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
         SootMethod toCall = Scene.v().getMethod("<java.lang.Thread: void dumpStack()>");
         list.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(toCall.makeRef())));
         toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
         list.add(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), (Value)StringConstant.v("Executing supposedly unreachable method:"))));
         list.add(Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef, toCall.makeRef(), (Value)StringConstant.v("\t" + method.getDeclaringClass().getName() + "." + method.getName()))));
         toCall = Scene.v().getMethod("<java.lang.System: void exit(int)>");
         list.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(toCall.makeRef(), (Value)IntConstant.v(1))));
         units.insertBefore((List)list, (Unit)units.getFirst());
      }
   }
}
