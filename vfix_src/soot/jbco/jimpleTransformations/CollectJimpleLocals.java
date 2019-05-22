package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;

public class CollectJimpleLocals extends BodyTransformer implements IJbcoTransform {
   public static String[] dependancies = new String[]{"jtp.jbco_jl"};
   public static String name = "jtp.jbco_jl";

   public void outputSummary() {
   }

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      Main.methods2JLocals.put(body.getMethod(), new ArrayList(body.getLocals()));
   }
}
