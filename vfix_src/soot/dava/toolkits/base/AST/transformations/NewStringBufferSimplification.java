package soot.dava.toolkits.base.AST.transformations;

import java.util.ArrayList;
import java.util.Iterator;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.javaRep.DNewInvokeExpr;
import soot.dava.internal.javaRep.DVirtualInvokeExpr;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.grimp.internal.GAddExpr;

public class NewStringBufferSimplification extends DepthFirstAdapter {
   public static boolean DEBUG = false;

   public NewStringBufferSimplification() {
   }

   public NewStringBufferSimplification(boolean verbose) {
      super(verbose);
   }

   public void inExprOrRefValueBox(ValueBox argBox) {
      if (DEBUG) {
         System.out.println("ValBox is: " + argBox.toString());
      }

      Value tempArgValue = argBox.getValue();
      if (DEBUG) {
         System.out.println("arg value is: " + tempArgValue);
      }

      if (!(tempArgValue instanceof DVirtualInvokeExpr)) {
         if (DEBUG) {
            System.out.println("Not a DVirtualInvokeExpr" + tempArgValue.getClass());
         }

      } else {
         if (DEBUG) {
            System.out.println("arg value is a virtual invokeExpr");
         }

         DVirtualInvokeExpr vInvokeExpr = (DVirtualInvokeExpr)tempArgValue;

         try {
            if (!vInvokeExpr.getMethod().toString().equals("<java.lang.StringBuffer: java.lang.String toString()>")) {
               return;
            }
         } catch (Exception var9) {
            return;
         }

         if (DEBUG) {
            System.out.println("Ends in toString()");
         }

         Value base = vInvokeExpr.getBase();

         ArrayList args;
         for(args = new ArrayList(); base instanceof DVirtualInvokeExpr; base = ((DVirtualInvokeExpr)base).getBase()) {
            DVirtualInvokeExpr tempV = (DVirtualInvokeExpr)base;
            if (DEBUG) {
               System.out.println("base method is " + tempV.getMethod());
            }

            if (!tempV.getMethod().toString().startsWith("<java.lang.StringBuffer: java.lang.StringBuffer append")) {
               if (DEBUG) {
                  System.out.println("Found a virtual invoke which is not a append" + tempV.getMethod());
               }

               return;
            }

            args.add(0, tempV.getArg(0));
         }

         if (base instanceof DNewInvokeExpr) {
            if (DEBUG) {
               System.out.println("New expr is " + ((DNewInvokeExpr)base).getMethod());
            }

            if (((DNewInvokeExpr)base).getMethod().toString().equals("<java.lang.StringBuffer: void <init>()>")) {
               if (DEBUG) {
                  System.out.println("Found a new StringBuffer.append list in it");
               }

               Iterator it = args.iterator();
               Object newVal = null;

               while(it.hasNext()) {
                  Value temp = (Value)it.next();
                  if (newVal == null) {
                     newVal = temp;
                  } else {
                     newVal = new GAddExpr((Value)newVal, temp);
                  }
               }

               if (DEBUG) {
                  System.out.println("New expression for System.out.println is" + newVal);
               }

               argBox.setValue((Value)newVal);
            }
         }
      }
   }
}
