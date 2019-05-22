package corg.vfix.sa.vfg.build;

import corg.vfix.fl.stack.StackTrace;
import corg.vfix.pg.java.element.TmpExprMaps;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.testng.internal.collections.Pair;
import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AnyNewExpr;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.UnopExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.JInvokeStmt;

public class NPELocator {
   private static String nullName = null;

   public static void init(String name) {
      nullName = name;
   }

   public static String getNullName() {
      return nullName == null ? "null" : nullName;
   }

   public static void locateNPE() throws Exception {
      if (nullName == null) {
         System.out.println("NPELocator init fail...");
      } else {
         System.out.println("ClsName: " + StackTrace.getTailClsName());
         SootClass sClass = Scene.v().getSootClass(StackTrace.getTailClsName());
         if (sClass == null) {
            System.out.println("SootClass " + StackTrace.getTailClsName() + " not found!!!!");
         } else {
            SootMethod sMethod = getMtdByNumber(sClass, StackTrace.getTailLineNumber());
            if (sMethod != null && sMethod.getName().equals(StackTrace.getTailMtdName())) {
               JimpleBody body = (JimpleBody)sMethod.getActiveBody();
               PatchingChain<Unit> units = body.getUnits();
               ArrayList<Pair<Stmt, Value>> pairs = new ArrayList();
               Iterator var6 = units.iterator();

               while(true) {
                  Unit u;
                  Stmt stmt;
                  do {
                     do {
                        if (!var6.hasNext()) {
                           setStackTrace(pairs, sMethod);
                           StackTrace.setNullCls(sClass);
                           StackTrace.setNullMtd(sMethod);
                           return;
                        }

                        u = (Unit)var6.next();
                        stmt = (Stmt)u;
                     } while(stmt.getJavaSourceStartLineNumber() != StackTrace.getTailLineNumber());
                  } while(!(u instanceof JInvokeStmt) && !(u instanceof AssignStmt));

                  Value base = getBase(stmt);
                  if (base != null) {
                     pairs.add(new Pair(stmt, base));
                  }
               }
            } else {
               System.out.println(StackTrace.getTailMtdName() + " not found");
            }
         }
      }
   }

   private static void setStackTrace(ArrayList<Pair<Stmt, Value>> pairs, SootMethod sMethod) throws Exception {
      if (pairs.size() <= 0) {
         System.out.println("null pointer exception not found");
         throw new Exception();
      } else {
         Pair pair;
         if (pairs.size() == 1) {
            pair = (Pair)pairs.get(0);
            Value base = (Value)pair.second();
            Stmt stmt = (Stmt)pair.first();
            StackTrace.setNullStmt(stmt);
            StackTrace.setNullPointer(base);
            System.out.println("NPE located");
         } else {
            System.out.println("multiple candidate pointers are found");
            Iterator var3 = pairs.iterator();

            while(var3.hasNext()) {
               pair = (Pair)var3.next();
               Value base = (Value)pair.second();
               Stmt stmt = (Stmt)pair.first();
               String baseName = "";
               System.out.println("Candidate Base: " + base);
               if (base instanceof Local && TmpExprMaps.isTmpLocal((Local)base)) {
                  baseName = TmpExprMaps.getTmpName(sMethod, (Local)base).toString();
               } else {
                  baseName = base.toString();
               }

               if (TmpExprMaps.hasVarNum(baseName)) {
                  baseName = TmpExprMaps.removeVarNum(baseName);
               }

               if (baseName.equals(nullName)) {
                  StackTrace.setNullStmt(stmt);
                  StackTrace.setNullPointer(base);
                  System.out.println("NPE located");
                  System.out.println("Stmt: " + stmt);
                  System.out.println("Base: " + base);
                  return;
               }

               if (baseName.contains("this.")) {
                  String removedThisName = baseName.replace("this.", "");
                  if (removedThisName.equals(nullName)) {
                     StackTrace.setNullStmt(stmt);
                     StackTrace.setNullPointer(base);
                     System.out.println("NPE located");
                     System.out.println("Stmt: " + stmt);
                     System.out.println("Base: " + base);
                  }
               } else {
                  System.out.println("baseName: " + baseName);
                  System.out.println("nullName: " + nullName);
               }
            }

            if (StackTrace.getNullStmt() == null) {
               throw new Exception();
            }
         }
      }
   }

   public static SootMethod getMtdByNumber(SootClass sClass, int lineNumber) throws Exception {
      List<SootMethod> mtds = sClass.getMethods();
      mtds.sort(new Comparator<SootMethod>() {
         public int compare(SootMethod o1, SootMethod o2) {
            int l1 = o1.getJavaSourceStartLineNumber();
            int l2 = o2.getJavaSourceStartLineNumber();
            if (l1 == l2) {
               return 0;
            } else {
               return l1 > l2 ? -1 : 1;
            }
         }
      });
      Iterator var4 = mtds.iterator();

      while(var4.hasNext()) {
         SootMethod mtd = (SootMethod)var4.next();
         if (mtd.getJavaSourceStartLineNumber() <= lineNumber) {
            return mtd;
         }
      }

      System.out.println("Method not found!");
      System.out.println("in Class: " + sClass.getName());
      System.out.println("in Methods: " + mtds);
      return null;
   }

   private static Value getBaseInvokeStmt(InvokeStmt invokeStmt) throws Exception {
      InvokeExpr invokeExpr = invokeStmt.getInvokeExpr();
      if (invokeExpr instanceof VirtualInvokeExpr) {
         return getBaseVirtualInvokeExpr((VirtualInvokeExpr)invokeExpr);
      } else if (invokeExpr instanceof SpecialInvokeExpr) {
         return getBaseSpecialInvokeExpr((SpecialInvokeExpr)invokeExpr);
      } else {
         System.out.println("cannot handle " + invokeStmt + " in NPE locator");
         throw new Exception();
      }
   }

   private static Value getBaseSpecialInvokeExpr(SpecialInvokeExpr invokeExpr) {
      return invokeExpr.getBase();
   }

   private static Value getBaseVirtualInvokeExpr(VirtualInvokeExpr vInvokeExpr) {
      return vInvokeExpr.getBase();
   }

   private static Value getBaseInstanceFieldRef(InstanceFieldRef ifieldRef) {
      return ifieldRef.getBase();
   }

   private static Value getBaseFieldRef(FieldRef fieldRef) throws Exception {
      if (fieldRef instanceof StaticFieldRef) {
         System.out.println("cannot handle StaticFieldRef in NPE locator");
         return null;
      } else if (fieldRef instanceof InstanceFieldRef) {
         return getBaseInstanceFieldRef((InstanceFieldRef)fieldRef);
      } else {
         System.out.println("cannot handle " + fieldRef + " in NPE locator");
         throw new Exception();
      }
   }

   private static Value getBaseInstanceInvokeExpr(InstanceInvokeExpr instanceInvokeExpr) {
      return instanceInvokeExpr.getBase();
   }

   private static Value getBaseInvokeExpr(InvokeExpr invokeExpr) throws Exception {
      if (invokeExpr instanceof InstanceInvokeExpr) {
         return getBaseInstanceInvokeExpr((InstanceInvokeExpr)invokeExpr);
      } else {
         if (invokeExpr instanceof StaticInvokeExpr) {
            System.out.println("cannot handle StaticInvokeExpr in NPE locator");
         } else if (invokeExpr instanceof DynamicInvokeExpr) {
            System.out.println("cannot handle DynamicInvokeExpr in NPE locator");
         } else {
            System.out.println("cannot handle InvokeExpr in NPE locator");
         }

         return null;
      }
   }

   private static Value getBaseCastExpr(CastExpr castExpr) {
      return castExpr.getOp();
   }

   private static Value getBaseUnopExpr(UnopExpr unopExpr) {
      return unopExpr.getOp();
   }

   private static Value getBaseAssignStmt(AssignStmt assignStmt) throws Exception {
      Value rightOp = assignStmt.getRightOp();
      if (rightOp instanceof FieldRef) {
         return getBaseFieldRef((FieldRef)rightOp);
      } else if (rightOp instanceof InvokeExpr) {
         return getBaseInvokeExpr((InvokeExpr)rightOp);
      } else if (rightOp instanceof UnopExpr) {
         return getBaseUnopExpr((UnopExpr)rightOp);
      } else {
         if (rightOp instanceof AnyNewExpr) {
            System.out.println("Type: AnyNewExpr");
         } else if (rightOp instanceof BinopExpr) {
            System.out.println("Type: BinopExpr");
         } else {
            if (rightOp instanceof CastExpr) {
               return getBaseCastExpr((CastExpr)rightOp);
            }

            if (rightOp instanceof InstanceOfExpr) {
               System.out.println("Type: InstanceOfExpr");
            } else if (rightOp instanceof NewArrayExpr) {
               System.out.println("Type: NewArrayExpr");
            } else if (rightOp instanceof NewExpr) {
               System.out.println("Type: NewExpr");
            } else if (rightOp instanceof NewMultiArrayExpr) {
               System.out.println("Type: NewMultiArrayExpr");
            }
         }

         return null;
      }
   }

   private static Value getBase(Stmt stmt) throws Exception {
      System.out.println("Stmt:" + stmt);
      if (stmt instanceof InvokeStmt) {
         return getBaseInvokeStmt((InvokeStmt)stmt);
      } else if (stmt instanceof AssignStmt) {
         return getBaseAssignStmt((AssignStmt)stmt);
      } else {
         System.out.println("cannot handle " + stmt + " in NPE locator");
         throw new Exception();
      }
   }
}
