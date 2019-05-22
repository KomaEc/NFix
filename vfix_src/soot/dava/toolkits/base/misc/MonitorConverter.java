package soot.dava.toolkits.base.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import soot.G;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.VoidType;
import soot.dava.DavaBody;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.javaRep.DStaticInvokeExpr;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.grimp.internal.GInvokeStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.MonitorStmt;

public class MonitorConverter {
   private final SootMethod v;
   private final SootMethod enter;
   private final SootMethod exit;

   public MonitorConverter(Singletons.Global g) {
      SootClass davaMonitor = new SootClass("soot.dava.toolkits.base.DavaMonitor.DavaMonitor", 1);
      davaMonitor.setSuperclass(Scene.v().loadClassAndSupport("java.lang.Object"));
      LinkedList objectSingleton = new LinkedList();
      objectSingleton.add(RefType.v("java.lang.Object"));
      this.v = Scene.v().makeSootMethod("v", new LinkedList(), RefType.v("soot.dava.toolkits.base.DavaMonitor.DavaMonitor"), 9);
      this.enter = Scene.v().makeSootMethod("enter", objectSingleton, VoidType.v(), 33);
      this.exit = Scene.v().makeSootMethod("exit", objectSingleton, VoidType.v(), 33);
      davaMonitor.addMethod(this.v);
      davaMonitor.addMethod(this.enter);
      davaMonitor.addMethod(this.exit);
      Scene.v().addClass(davaMonitor);
   }

   public static MonitorConverter v() {
      return G.v().soot_dava_toolkits_base_misc_MonitorConverter();
   }

   public void convert(DavaBody body) {
      Iterator var2 = body.get_MonitorFacts().iterator();

      while(var2.hasNext()) {
         AugmentedStmt mas = (AugmentedStmt)var2.next();
         MonitorStmt ms = (MonitorStmt)mas.get_Stmt();
         body.addToImportList("soot.dava.toolkits.base.DavaMonitor.DavaMonitor");
         ArrayList arg = new ArrayList();
         arg.add(ms.getOp());
         if (ms instanceof EnterMonitorStmt) {
            mas.set_Stmt(new GInvokeStmt(new DVirtualInvokeExpr(new DStaticInvokeExpr(this.v.makeRef(), new ArrayList()), this.enter.makeRef(), arg, new HashSet())));
         } else {
            mas.set_Stmt(new GInvokeStmt(new DVirtualInvokeExpr(new DStaticInvokeExpr(this.v.makeRef(), new ArrayList()), this.exit.makeRef(), arg, new HashSet())));
         }
      }

   }
}
