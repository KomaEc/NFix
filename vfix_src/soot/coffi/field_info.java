package soot.coffi;

public class field_info {
   public int access_flags;
   public int name_index;
   public int descriptor_index;
   public int attributes_count;
   public attribute_info[] attributes;

   public String toName(cp_info[] constant_pool) {
      CONSTANT_Utf8_info ci = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index]);
      return ci.convert();
   }

   public String prototype(cp_info[] constant_pool) {
      CONSTANT_Utf8_info cm = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.name_index]);
      CONSTANT_Utf8_info dm = (CONSTANT_Utf8_info)((CONSTANT_Utf8_info)constant_pool[this.descriptor_index]);
      String s = ClassFile.access_string(this.access_flags, " ");
      if (s.compareTo("") != 0) {
         s = s + " ";
      }

      return s + ClassFile.parseDesc(dm.convert(), "") + " " + cm.convert();
   }

   public ConstantValue_attribute findConstantValue_attribute() {
      for(int i = 0; i < this.attributes_count; ++i) {
         if (this.attributes[i] instanceof ConstantValue_attribute) {
            return (ConstantValue_attribute)((ConstantValue_attribute)this.attributes[i]);
         }
      }

      return null;
   }
}
