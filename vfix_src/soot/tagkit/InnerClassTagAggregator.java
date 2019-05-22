package soot.tagkit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import soot.G;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;

public class InnerClassTagAggregator extends SceneTransformer {
   public InnerClassTagAggregator(Singletons.Global g) {
   }

   public static InnerClassTagAggregator v() {
      return G.v().soot_tagkit_InnerClassTagAggregator();
   }

   public String aggregatedName() {
      return "InnerClasses";
   }

   public void internalTransform(String phaseName, Map<String, String> options) {
      Iterator it = Scene.v().getApplicationClasses().iterator();

      while(it.hasNext()) {
         ArrayList<InnerClassTag> list = new ArrayList();
         SootClass nextSc = (SootClass)it.next();
         Iterator var6 = nextSc.getTags().iterator();

         while(var6.hasNext()) {
            Tag t = (Tag)var6.next();
            if (t instanceof InnerClassTag) {
               list.add((InnerClassTag)t);
            }
         }

         if (!list.isEmpty()) {
            nextSc.addTag(new InnerClassAttribute(list));
         }
      }

   }
}
