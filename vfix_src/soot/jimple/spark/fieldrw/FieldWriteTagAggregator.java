package soot.jimple.spark.fieldrw;

import soot.G;
import soot.Singletons;
import soot.tagkit.ImportantTagAggregator;
import soot.tagkit.Tag;

public class FieldWriteTagAggregator extends ImportantTagAggregator {
   public FieldWriteTagAggregator(Singletons.Global g) {
   }

   public static FieldWriteTagAggregator v() {
      return G.v().soot_jimple_spark_fieldrw_FieldWriteTagAggregator();
   }

   public boolean wantTag(Tag t) {
      return t instanceof FieldWriteTag;
   }

   public String aggregatedName() {
      return "FieldWrite";
   }
}
