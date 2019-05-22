package soot.coffi;

public class LineNumberTable_attribute extends attribute_info {
   public int line_number_table_length;
   public line_number_table_entry[] line_number_table;

   public String toString() {
      String sv = "LineNumberTable : " + this.line_number_table_length + "\n";

      for(int i = 0; i < this.line_number_table_length; ++i) {
         sv = sv + "LineNumber(" + this.line_number_table[i].start_pc + ":" + this.line_number_table[i].start_inst + "," + this.line_number_table[i].line_number + ")";
         sv = sv + "\n";
      }

      return sv;
   }
}
