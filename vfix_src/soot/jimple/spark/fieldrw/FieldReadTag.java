package soot.jimple.spark.fieldrw;

import java.util.Set;
import soot.SootField;

public class FieldReadTag extends FieldRWTag {
   FieldReadTag(Set<SootField> fields) {
      super(fields);
   }

   public String getName() {
      return "FieldReadTag";
   }
}
