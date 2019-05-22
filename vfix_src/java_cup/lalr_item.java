package java_cup;

import java.util.Stack;

public class lalr_item extends lr_item_core {
   protected terminal_set _lookahead;
   protected Stack _propagate_items;
   protected boolean needs_propagation;

   public lalr_item(production prod, int pos, terminal_set look) throws internal_error {
      super(prod, pos);
      this._lookahead = look;
      this._propagate_items = new Stack();
      this.needs_propagation = true;
   }

   public lalr_item(production prod, terminal_set look) throws internal_error {
      this(prod, 0, look);
   }

   public lalr_item(production prod) throws internal_error {
      this(prod, 0, new terminal_set());
   }

   public terminal_set lookahead() {
      return this._lookahead;
   }

   public Stack propagate_items() {
      return this._propagate_items;
   }

   public void add_propagate(lalr_item prop_to) {
      this._propagate_items.push(prop_to);
      this.needs_propagation = true;
   }

   public void propagate_lookaheads(terminal_set incoming) throws internal_error {
      boolean change = false;
      if (this.needs_propagation || incoming != null && !incoming.empty()) {
         if (incoming != null) {
            change = this.lookahead().add(incoming);
         }

         if (change || this.needs_propagation) {
            this.needs_propagation = false;

            for(int i = 0; i < this.propagate_items().size(); ++i) {
               ((lalr_item)this.propagate_items().elementAt(i)).propagate_lookaheads(this.lookahead());
            }
         }

      }
   }

   public lalr_item shift() throws internal_error {
      if (this.dot_at_end()) {
         throw new internal_error("Attempt to shift past end of an lalr_item");
      } else {
         lalr_item result = new lalr_item(this.the_production(), this.dot_pos() + 1, new terminal_set(this.lookahead()));
         this.add_propagate(result);
         return result;
      }
   }

   public terminal_set calc_lookahead(terminal_set lookahead_after) throws internal_error {
      if (this.dot_at_end()) {
         throw new internal_error("Attempt to calculate a lookahead set with a completed item");
      } else {
         terminal_set result = new terminal_set();

         for(int pos = this.dot_pos() + 1; pos < this.the_production().rhs_length(); ++pos) {
            production_part part = this.the_production().rhs(pos);
            if (!part.is_action()) {
               symbol sym = ((symbol_part)part).the_symbol();
               if (!sym.is_non_term()) {
                  result.add((terminal)sym);
                  return result;
               }

               result.add(((non_terminal)sym).first_set());
               if (!((non_terminal)sym).nullable()) {
                  return result;
               }
            }
         }

         result.add(lookahead_after);
         return result;
      }
   }

   public boolean lookahead_visible() throws internal_error {
      if (this.dot_at_end()) {
         return true;
      } else {
         for(int pos = this.dot_pos() + 1; pos < this.the_production().rhs_length(); ++pos) {
            production_part part = this.the_production().rhs(pos);
            if (!part.is_action()) {
               symbol sym = ((symbol_part)part).the_symbol();
               if (!sym.is_non_term()) {
                  return false;
               }

               if (!((non_terminal)sym).nullable()) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public boolean equals(lalr_item other) {
      return other == null ? false : super.equals((lr_item_core)other);
   }

   public boolean equals(Object other) {
      return !(other instanceof lalr_item) ? false : this.equals((lalr_item)other);
   }

   public int hashCode() {
      return super.hashCode();
   }

   public String toString() {
      String result = "";
      result = result + "[";
      result = result + super.toString();
      result = result + ", ";
      if (this.lookahead() != null) {
         result = result + "{";

         for(int t = 0; t < terminal.number(); ++t) {
            if (this.lookahead().contains(t)) {
               result = result + terminal.find(t).name() + " ";
            }
         }

         result = result + "}";
      } else {
         result = result + "NULL LOOKAHEAD!!";
      }

      result = result + "]";
      return result;
   }
}
