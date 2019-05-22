package soot.coffi;

class InnerClasses_attribute extends attribute_info {
   public int inner_classes_length;
   public inner_class_entry[] inner_classes;

   public String toString() {
      StringBuffer buffer = new StringBuffer();

      for(int i = 0; i < this.inner_classes_length; ++i) {
         buffer.append(this.inner_classes[i]);
         buffer.append('\n');
      }

      return buffer.toString();
   }
}
