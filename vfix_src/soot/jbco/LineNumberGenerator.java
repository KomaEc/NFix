package soot.jbco;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.PackManager;
import soot.PatchingChain;
import soot.Transform;
import soot.Unit;
import soot.tagkit.LineNumberTag;

public class LineNumberGenerator {
   LineNumberGenerator.BafLineNumberer bln = new LineNumberGenerator.BafLineNumberer();

   public static void main(String[] argv) {
      PackManager.v().getPack("jtp").add(new Transform("jtp.lnprinter", (new LineNumberGenerator()).bln));
      PackManager.v().getPack("bb").add(new Transform("bb.lnprinter", (new LineNumberGenerator()).bln));
      soot.Main.main(argv);
   }

   class BafLineNumberer extends BodyTransformer {
      protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
         System.out.println("Printing Line Numbers for: " + b.getMethod().getSignature());
         PatchingChain<Unit> units = b.getUnits();
         Iterator it = units.iterator();

         while(it.hasNext()) {
            Unit u = (Unit)it.next();
            if (u.hasTag("LineNumberTag")) {
               LineNumberTag tag = (LineNumberTag)u.getTag("LineNumberTag");
               System.out.println(u + " has Line Number: " + tag.getLineNumber());
            } else {
               System.out.println(u + " has no Line Number");
            }
         }

         System.out.println("\n");
      }
   }
}
