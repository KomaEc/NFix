package soot.tagkit;

import java.util.Collections;
import java.util.List;
import soot.UnitBox;

public class GenericAttribute implements Attribute {
   private final String mName;
   private byte[] mValue;

   public GenericAttribute(String name, byte[] value) {
      if (value == null) {
         value = new byte[0];
      }

      this.mName = name;
      this.mValue = value;
   }

   public String getName() {
      return this.mName;
   }

   public byte[] getValue() {
      return this.mValue;
   }

   public String toString() {
      return this.mName + " " + Base64.encode(this.mValue).toString();
   }

   public void setValue(byte[] value) {
      this.mValue = value;
   }

   public List<UnitBox> getUnitBoxes() {
      return Collections.emptyList();
   }
}
