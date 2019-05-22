package soot.coffi;

class local_variable_type_table_entry {
   public int start_pc;
   public int length;
   public int name_index;
   public int signature_index;
   public int index;

   public String toString() {
      return "start: " + this.start_pc + "length: " + this.length + "name_index: " + this.name_index + "signature_index: " + this.signature_index + "index: " + this.index;
   }
}
