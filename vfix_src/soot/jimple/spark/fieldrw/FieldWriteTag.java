package soot.jimple.spark.fieldrw;

import java.util.Set;
import soot.SootField;

public class FieldWriteTag extends FieldRWTag {
   FieldWriteTag(Set<SootField> fields) {
      super(fields);
   }

   public String getName() {
      return "FieldWriteTag";
   }
}
