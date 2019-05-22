package soot.jimple.spark.fieldrw;

import soot.G;
import soot.Singletons;
import soot.tagkit.ImportantTagAggregator;
import soot.tagkit.Tag;

public class FieldReadTagAggregator extends ImportantTagAggregator {
   public FieldReadTagAggregator(Singletons.Global g) {
   }

   public static FieldReadTagAggregator v() {
      return G.v().soot_jimple_spark_fieldrw_FieldReadTagAggregator();
   }

   public boolean wantTag(Tag t) {
      return t instanceof FieldReadTag;
   }

   public String aggregatedName() {
      return "FieldRead";
   }
}
