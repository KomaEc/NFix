package soot.coffi;

class local_variable_table_entry {
   public int start_pc;
   public int length;
   public int name_index;
   public int descriptor_index;
   public int index;

   public String toString() {
      return "start: " + this.start_pc + "length: " + this.length + "name_index: " + this.name_index + "descriptor_index: " + this.descriptor_index + "index: " + this.index;
   }
}
