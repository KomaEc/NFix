package soot.jbco.bafTransformations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;

public class Jimple2BafLocalBuilder extends BodyTransformer implements IJbcoTransform {
   public static String[] dependancies = new String[]{"jtp.jbco_jl", "bb.jbco_j2bl", "bb.lp"};
   public static String name = "bb.jbco_j2bl";
   private static boolean runOnce = false;

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (Main.methods2JLocals.size() == 0) {
         if (!runOnce) {
            runOnce = true;
            out.println("[Jimple2BafLocalBuilder]:: Jimple Local Lists have not been built");
            out.println("                           Skipping Jimple To Baf Builder\n");
         }

      } else {
         Collection<Local> bLocals = b.getLocals();
         HashMap<Local, Local> bafToJLocals = new HashMap();
         Iterator jlocIt = ((List)Main.methods2JLocals.get(b.getMethod())).iterator();

         while(true) {
            while(jlocIt.hasNext()) {
               Local jl = (Local)jlocIt.next();
               Iterator blocIt = bLocals.iterator();

               while(blocIt.hasNext()) {
                  Local bl = (Local)blocIt.next();
                  if (bl.getName().equals(jl.getName())) {
                     bafToJLocals.put(bl, jl);
                     break;
                  }
               }
            }

            Main.methods2Baf2JLocals.put(b.getMethod(), bafToJLocals);
            return;
         }
      }
   }
}
