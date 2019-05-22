package soot.jimple.toolkits.ide.libsumm;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;

public class Main {
   static int yes = 0;
   static int no = 0;

   public static void main(String[] args) {
      PackManager.v().getPack("jtp").add(new Transform("jtp.fixedie", new BodyTransformer() {
         protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
            Iterator var4 = b.getUnits().iterator();

            while(var4.hasNext()) {
               Unit u = (Unit)var4.next();
               Stmt s = (Stmt)u;
               if (s.containsInvokeExpr()) {
                  InvokeExpr ie = s.getInvokeExpr();
                  if (FixedMethods.isFixed(ie)) {
                     System.err.println("+++ " + ie);
                     ++Main.yes;
                  } else {
                     System.err.println(" -  " + ie);
                     ++Main.no;
                  }
               }
            }

         }
      }));
      soot.Main.main(args);
      System.err.println("+++ " + yes);
      System.err.println(" -  " + no);
   }
}
