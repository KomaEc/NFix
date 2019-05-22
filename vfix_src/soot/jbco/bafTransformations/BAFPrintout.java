package soot.jbco.bafTransformations;

import java.util.Map;
import java.util.Stack;
import soot.Body;
import soot.BodyTransformer;
import soot.Type;
import soot.Unit;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Debugger;

public class BAFPrintout extends BodyTransformer implements IJbcoTransform {
   public static String name = "bb.printout";
   static boolean stack = false;

   public void outputSummary() {
   }

   public String[] getDependencies() {
      return new String[0];
   }

   public String getName() {
      return name;
   }

   public BAFPrintout() {
   }

   public BAFPrintout(String newname, boolean print_stack) {
      name = newname;
      stack = print_stack;
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      System.out.println("\n" + b.getMethod().getSignature());
      if (stack) {
         Map<Unit, Stack<Type>> stacks = null;
         Map b2j = (Map)Main.methods2Baf2JLocals.get(b.getMethod());

         try {
            if (b2j == null) {
               stacks = StackTypeHeightCalculator.calculateStackHeights(b);
            } else {
               stacks = StackTypeHeightCalculator.calculateStackHeights(b, b2j);
            }

            StackTypeHeightCalculator.printStack(b.getUnits(), stacks, true);
         } catch (Exception var7) {
            System.out.println("\n**************Exception calculating height " + var7 + ", printing plain bytecode now\n\n");
            Debugger.printUnits(b, "  FINAL");
         }
      } else {
         Debugger.printUnits(b, "  FINAL");
      }

      System.out.println();
   }
}
