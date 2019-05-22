package java_cup;

import java.util.Enumeration;

public class parse_action_table {
   protected int _num_states = lalr_state.number();
   public parse_action_row[] under_state;

   public parse_action_table() {
      this.under_state = new parse_action_row[this._num_states];

      for(int i = 0; i < this._num_states; ++i) {
         this.under_state[i] = new parse_action_row();
      }

   }

   public int num_states() {
      return this._num_states;
   }

   public void check_reductions() throws internal_error {
      for(int row = 0; row < this.num_states(); ++row) {
         for(int col = 0; col < parse_action_row.size(); ++col) {
            parse_action act = this.under_state[row].under_term[col];
            if (act != null && act.kind() == 2) {
               ((reduce_action)act).reduce_with().note_reduction_use();
            }
         }
      }

      Enumeration p = production.all();

      while(p.hasMoreElements()) {
         production prod = (production)p.nextElement();
         if (prod.num_reductions() == 0 && !emit.nowarn) {
            ErrorManager.getManager().emit_warning("*** Production \"" + prod.to_simple_string() + "\" never reduced");
         }
      }

   }

   public String toString() {
      String result = "-------- ACTION_TABLE --------\n";

      for(int row = 0; row < this.num_states(); ++row) {
         result = result + "From state #" + row + "\n";
         int cnt = 0;

         for(int col = 0; col < parse_action_row.size(); ++col) {
            if (this.under_state[row].under_term[col].kind() != 0) {
               result = result + " [term " + col + ":" + this.under_state[row].under_term[col] + "]";
               ++cnt;
               if (cnt == 2) {
                  result = result + "\n";
                  cnt = 0;
               }
            }
         }

         if (cnt != 0) {
            result = result + "\n";
         }
      }

      result = result + "------------------------------";
      return result;
   }
}
