package corg.vfix.fl.spectrum;

import java.util.ArrayList;
import java.util.Iterator;
import soot.SootMethod;
import soot.Unit;

public class FLClass {
   private String clsName;
   private ArrayList<FLStmt> stmts = new ArrayList();

   FLClass(String name) {
      this.clsName = name;
   }

   public void addStmt(FLStmt flStmt) {
      if (!this.stmts.contains(flStmt)) {
         this.stmts.add(flStmt);
      }

   }

   public void print() {
      System.out.println("ClassName: " + this.clsName);
      Iterator var2 = this.stmts.iterator();

      while(var2.hasNext()) {
         FLStmt flStmt = (FLStmt)var2.next();
         flStmt.print();
      }

      System.out.println();
   }

   public String getName() {
      return this.clsName;
   }

   public ArrayList<FLStmt> getFLStmts() {
      return this.stmts;
   }

   public boolean query(int lineNumber) {
      Iterator var3 = this.stmts.iterator();

      while(var3.hasNext()) {
         FLStmt stmt = (FLStmt)var3.next();
         if (stmt.getLineNumber() == lineNumber) {
            if (stmt.getSuspicious() > 0.0D) {
               return true;
            }

            return false;
         }
      }

      return false;
   }

   public boolean query(SootMethod mtd, FLClass flCls) {
      Iterator var4 = mtd.getActiveBody().getUnits().iterator();

      while(var4.hasNext()) {
         Unit stmt = (Unit)var4.next();
         int lineNumber = stmt.getJavaSourceStartLineNumber();
         if (flCls.query(lineNumber)) {
            return true;
         }
      }

      return false;
   }
}
