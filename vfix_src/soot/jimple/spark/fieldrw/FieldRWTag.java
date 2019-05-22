package soot.jimple.spark.fieldrw;

import java.util.Iterator;
import java.util.Set;
import soot.SootField;
import soot.tagkit.Tag;

public abstract class FieldRWTag implements Tag {
   String fieldNames = new String();

   FieldRWTag(Set<SootField> fields) {
      StringBuffer sb = new StringBuffer();
      boolean first = true;
      Iterator var4 = fields.iterator();

      while(var4.hasNext()) {
         SootField field = (SootField)var4.next();
         if (!first) {
            sb.append("%");
         }

         first = false;
         sb.append(field.getDeclaringClass().getName());
         sb.append(":");
         sb.append(field.getName());
      }

      this.fieldNames = sb.toString();
   }

   public abstract String getName();

   public byte[] getValue() {
      byte[] bytes = this.fieldNames.getBytes();
      byte[] ret = new byte[bytes.length + 2];
      ret[0] = (byte)(bytes.length / 256);
      ret[1] = (byte)(bytes.length % 256);
      System.arraycopy(bytes, 0, ret, 2, bytes.length);
      return ret;
   }

   public String toString() {
      return this.getName() + this.fieldNames;
   }
}
