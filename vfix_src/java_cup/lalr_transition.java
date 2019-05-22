package java_cup;

public class lalr_transition {
   protected symbol _on_symbol;
   protected lalr_state _to_state;
   protected lalr_transition _next;

   public lalr_transition(symbol on_sym, lalr_state to_st, lalr_transition nxt) throws internal_error {
      if (on_sym == null) {
         throw new internal_error("Attempt to create transition on null symbol");
      } else if (to_st == null) {
         throw new internal_error("Attempt to create transition to null state");
      } else {
         this._on_symbol = on_sym;
         this._to_state = to_st;
         this._next = nxt;
      }
   }

   public lalr_transition(symbol on_sym, lalr_state to_st) throws internal_error {
      this(on_sym, to_st, (lalr_transition)null);
   }

   public symbol on_symbol() {
      return this._on_symbol;
   }

   public lalr_state to_state() {
      return this._to_state;
   }

   public lalr_transition next() {
      return this._next;
   }

   public String toString() {
      String result = "transition on " + this.on_symbol().name() + " to state [";
      result = result + this._to_state.index();
      result = result + "]";
      return result;
   }
}
