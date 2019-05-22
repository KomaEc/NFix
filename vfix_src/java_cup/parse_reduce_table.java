package java_cup;

public class parse_reduce_table {
   protected int _num_states = lalr_state.number();
   public parse_reduce_row[] under_state;

   public parse_reduce_table() {
      this.under_state = new parse_reduce_row[this._num_states];

      for(int i = 0; i < this._num_states; ++i) {
         this.under_state[i] = new parse_reduce_row();
      }

   }

   public int num_states() {
      return this._num_states;
   }

   public String toString() {
      String result = "-------- REDUCE_TABLE --------\n";

      for(int row = 0; row < this.num_states(); ++row) {
         result = result + "From state #" + row + "\n";
         int cnt = 0;

         for(int col = 0; col < parse_reduce_row.size(); ++col) {
            lalr_state goto_st = this.under_state[row].under_non_term[col];
            if (goto_st != null) {
               result = result + " [non term " + col + "->";
               result = result + "state " + goto_st.index() + "]";
               ++cnt;
               if (cnt == 3) {
                  result = result + "\n";
                  cnt = 0;
               }
            }
         }

         if (cnt != 0) {
            result = result + "\n";
         }
      }

      result = result + "-----------------------------";
      return result;
   }
}
