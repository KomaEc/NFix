package java_cup;

public class parse_reduce_row {
   protected static int _size = 0;
   public lalr_state[] under_non_term;

   public parse_reduce_row() {
      if (_size <= 0) {
         _size = non_terminal.number();
      }

      this.under_non_term = new lalr_state[size()];
   }

   public static int size() {
      return _size;
   }

   public static void clear() {
      _size = 0;
   }
}
