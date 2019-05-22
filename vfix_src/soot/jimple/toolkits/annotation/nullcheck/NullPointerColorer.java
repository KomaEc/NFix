package soot.jimple.toolkits.annotation.nullcheck;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.RefLikeType;
import soot.Singletons;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.tagkit.ColorTag;
import soot.tagkit.KeyTag;
import soot.tagkit.StringTag;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.FlowSet;

public class NullPointerColorer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(NullPointerColorer.class);

   public NullPointerColorer(Singletons.Global g) {
   }

   public static NullPointerColorer v() {
      return G.v().soot_jimple_toolkits_annotation_nullcheck_NullPointerColorer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      BranchedRefVarsAnalysis analysis = new BranchedRefVarsAnalysis(new ExceptionalUnitGraph(b));
      Iterator it = b.getUnits().iterator();

      while(it.hasNext()) {
         Stmt s = (Stmt)it.next();
         Iterator<ValueBox> usesIt = s.getUseBoxes().iterator();
         FlowSet beforeSet = (FlowSet)analysis.getFlowBefore(s);

         while(usesIt.hasNext()) {
            ValueBox vBox = (ValueBox)usesIt.next();
            this.addColorTags(vBox, beforeSet, s, analysis);
         }

         Iterator<ValueBox> defsIt = s.getDefBoxes().iterator();
         FlowSet afterSet = (FlowSet)analysis.getFallFlowAfter(s);

         while(defsIt.hasNext()) {
            ValueBox vBox = (ValueBox)defsIt.next();
            this.addColorTags(vBox, afterSet, s, analysis);
         }
      }

      Iterator<Tag> keysIt = b.getMethod().getDeclaringClass().getTags().iterator();
      boolean keysAdded = false;

      while(keysIt.hasNext()) {
         Tag next = (Tag)keysIt.next();
         if (next instanceof KeyTag && ((KeyTag)next).analysisType().equals("NullCheckTag")) {
            keysAdded = true;
         }
      }

      if (!keysAdded) {
         b.getMethod().getDeclaringClass().addTag(new KeyTag(0, "Nullness: Null", "NullCheckTag"));
         b.getMethod().getDeclaringClass().addTag(new KeyTag(1, "Nullness: Not Null", "NullCheckTag"));
         b.getMethod().getDeclaringClass().addTag(new KeyTag(3, "Nullness: Nullness Unknown", "NullCheckTag"));
      }

   }

   private void addColorTags(ValueBox vBox, FlowSet set, Stmt s, BranchedRefVarsAnalysis analysis) {
      Value val = vBox.getValue();
      if (val.getType() instanceof RefLikeType) {
         int vInfo = analysis.anyRefInfo(val, set);
         switch(vInfo) {
         case 0:
            s.addTag(new StringTag(val + ": Nullness Unknown", "NullCheckTag"));
            vBox.addTag(new ColorTag(3, "NullCheckTag"));
            break;
         case 1:
            s.addTag(new StringTag(val + ": Null", "NullCheckTag"));
            vBox.addTag(new ColorTag(0, "NullCheckTag"));
            break;
         case 2:
            s.addTag(new StringTag(val + ": NonNull", "NullCheckTag"));
            vBox.addTag(new ColorTag(1, "NullCheckTag"));
            break;
         case 99:
            s.addTag(new StringTag(val + ": Nullness Unknown", "NullCheckTag"));
            vBox.addTag(new ColorTag(3, "NullCheckTag"));
         }
      }

   }
}
