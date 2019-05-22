package corg.vfix.fl.spectrum;

import corg.vfix.sa.vfg.VFGNode;
import java.util.ArrayList;
import java.util.Iterator;
import soot.SootMethod;
import soot.Unit;

public class FLSpectrum {
   private static ArrayList<FLClass> clss = new ArrayList();

   public static void clearAll() {
      clss = new ArrayList();
   }

   public static void addClass(FLClass flCls) {
      if (!clss.contains(flCls)) {
         clss.add(flCls);
      }

   }

   public static FLClass getClassByName(String clsName) {
      Iterator var2 = clss.iterator();

      FLClass cls;
      while(var2.hasNext()) {
         cls = (FLClass)var2.next();
         if (clsName.equals(cls.getName())) {
            return cls;
         }
      }

      cls = new FLClass(clsName);
      addClass(cls);
      return cls;
   }

   public static void print() {
      System.out.println("Spectrum");
      Iterator var1 = clss.iterator();

      while(var1.hasNext()) {
         FLClass flCls = (FLClass)var1.next();
         flCls.print();
      }

      System.out.println();
   }

   public static boolean query(SootMethod mtd) {
      if (mtd == null) {
         return false;
      } else {
         Iterator var2 = clss.iterator();

         while(var2.hasNext()) {
            FLClass cls = (FLClass)var2.next();
            if (cls.getName().equals(mtd.getDeclaringClass().getName())) {
               return cls.query(mtd, cls);
            }
         }

         return false;
      }
   }

   public static boolean query(VFGNode vfgNode) {
      Iterator var2 = clss.iterator();

      while(var2.hasNext()) {
         FLClass cls = (FLClass)var2.next();
         if (cls.getName().equals(vfgNode.getClsName())) {
            return cls.query(vfgNode.getLineNumber());
         }
      }

      return false;
   }

   public static boolean query(Unit unit, SootMethod mtd) {
      Iterator var3 = clss.iterator();

      while(var3.hasNext()) {
         FLClass cls = (FLClass)var3.next();
         if (cls.getName().equals(mtd.getDeclaringClass().getName())) {
            return cls.query(unit.getJavaSourceStartLineNumber());
         }
      }

      return false;
   }

   public static int getTotalNumOfExecuted() {
      int count = 0;
      Iterator var2 = clss.iterator();

      while(var2.hasNext()) {
         FLClass cls = (FLClass)var2.next();
         ArrayList<FLStmt> stmts = cls.getFLStmts();
         Iterator var5 = stmts.iterator();

         while(var5.hasNext()) {
            FLStmt stmt = (FLStmt)var5.next();
            if (stmt.getSuspicious() > 0.0D) {
               ++count;
            }
         }
      }

      return count;
   }
}
