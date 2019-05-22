package java_cup;

public class parse_action_row {
   protected static int _size = 0;
   protected static int[] reduction_count = null;
   public parse_action[] under_term;
   public int default_reduce;

   public parse_action_row() {
      if (_size <= 0) {
         _size = terminal.number();
      }

      this.under_term = new parse_action[size()];

      for(int i = 0; i < _size; ++i) {
         this.under_term[i] = new parse_action();
      }

   }

   public static int size() {
      return _size;
   }

   public static void clear() {
      _size = 0;
      reduction_count = null;
   }

   public void compute_default() {
      if (reduction_count == null) {
         reduction_count = new int[production.number()];
      }

      int i;
      for(i = 0; i < production.number(); ++i) {
         reduction_count[i] = 0;
      }

      int max_prod = -1;
      int max_red = 0;

      for(i = 0; i < size(); ++i) {
         if (this.under_term[i].kind() == 2) {
            int prod = ((reduce_action)this.under_term[i]).reduce_with().index();
            int var10002 = reduction_count[prod]++;
            if (reduction_count[prod] > max_red) {
               max_red = reduction_count[prod];
               max_prod = prod;
            }
         }
      }

      this.default_reduce = max_prod;
   }
}
