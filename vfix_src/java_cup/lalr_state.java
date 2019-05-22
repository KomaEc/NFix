package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

public class lalr_state {
   protected static Hashtable _all = new Hashtable();
   protected static Hashtable _all_kernels = new Hashtable();
   protected static int next_index = 0;
   protected lalr_item_set _items;
   protected lalr_transition _transitions = null;
   protected int _index;

   public lalr_state(lalr_item_set itms) throws internal_error {
      if (itms == null) {
         throw new internal_error("Attempt to construct an LALR state from a null item set");
      } else if (find_state(itms) != null) {
         throw new internal_error("Attempt to construct a duplicate LALR state");
      } else {
         this._index = next_index++;
         this._items = itms;
         _all.put(this._items, this);
      }
   }

   public static Enumeration all() {
      return _all.elements();
   }

   public static void clear() {
      _all.clear();
      _all_kernels.clear();
      next_index = 0;
   }

   public static int number() {
      return _all.size();
   }

   public static lalr_state find_state(lalr_item_set itms) {
      return itms == null ? null : (lalr_state)_all.get(itms);
   }

   public lalr_item_set items() {
      return this._items;
   }

   public lalr_transition transitions() {
      return this._transitions;
   }

   public int index() {
      return this._index;
   }

   protected static void dump_state(lalr_state st) throws internal_error {
      if (st == null) {
         System.out.println("NULL lalr_state");
      } else {
         System.out.println("lalr_state [" + st.index() + "] {");
         lalr_item_set itms = st.items();

         for(Enumeration e = itms.all(); e.hasMoreElements(); System.out.println("]")) {
            lalr_item itm = (lalr_item)e.nextElement();
            System.out.print("  [");
            System.out.print(itm.the_production().lhs().the_symbol().name());
            System.out.print(" ::= ");

            for(int i = 0; i < itm.the_production().rhs_length(); ++i) {
               if (i == itm.dot_pos()) {
                  System.out.print("(*) ");
               }

               production_part part = itm.the_production().rhs(i);
               if (part.is_action()) {
                  System.out.print("{action} ");
               } else {
                  System.out.print(((symbol_part)part).the_symbol().name() + " ");
               }
            }

            if (itm.dot_at_end()) {
               System.out.print("(*) ");
            }
         }

         System.out.println("}");
      }
   }

   protected static void propagate_all_lookaheads() throws internal_error {
      Enumeration st = all();

      while(st.hasMoreElements()) {
         ((lalr_state)st.nextElement()).propagate_lookaheads();
      }

   }

   public void add_transition(symbol on_sym, lalr_state to_st) throws internal_error {
      lalr_transition trans = new lalr_transition(on_sym, to_st, this._transitions);
      this._transitions = trans;
   }

   public static lalr_state build_machine(production start_prod) throws internal_error {
      Stack work_stack = new Stack();
      if (start_prod == null) {
         throw new internal_error("Attempt to build viable prefix recognizer using a null production");
      } else {
         lalr_item_set start_items = new lalr_item_set();
         lalr_item itm = new lalr_item(start_prod);
         itm.lookahead().add(terminal.EOF);
         start_items.add(itm);
         lalr_item_set kernel = new lalr_item_set(start_items);
         start_items.compute_closure();
         lalr_state start_state = new lalr_state(start_items);
         work_stack.push(start_state);
         _all_kernels.put(kernel, start_state);

         while(!work_stack.empty()) {
            lalr_state st = (lalr_state)work_stack.pop();
            symbol_set outgoing = new symbol_set();
            Enumeration i = st.items().all();

            symbol sym;
            while(i.hasMoreElements()) {
               itm = (lalr_item)i.nextElement();
               sym = itm.symbol_after_dot();
               if (sym != null) {
                  outgoing.add(sym);
               }
            }

            lalr_state new_st;
            for(Enumeration s = outgoing.all(); s.hasMoreElements(); st.add_transition(sym, new_st)) {
               sym = (symbol)s.nextElement();
               lalr_item_set linked_items = new lalr_item_set();
               lalr_item_set new_items = new lalr_item_set();
               i = st.items().all();

               while(i.hasMoreElements()) {
                  itm = (lalr_item)i.nextElement();
                  symbol sym2 = itm.symbol_after_dot();
                  if (sym.equals(sym2)) {
                     new_items.add(itm.shift());
                     linked_items.add(itm);
                  }
               }

               kernel = new lalr_item_set(new_items);
               new_st = (lalr_state)_all_kernels.get(kernel);
               if (new_st == null) {
                  new_items.compute_closure();
                  new_st = new lalr_state(new_items);
                  work_stack.push(new_st);
                  _all_kernels.put(kernel, new_st);
               } else {
                  Enumeration fix = linked_items.all();

                  while(fix.hasMoreElements()) {
                     lalr_item fix_itm = (lalr_item)fix.nextElement();

                     for(int l = 0; l < fix_itm.propagate_items().size(); ++l) {
                        lalr_item new_itm = (lalr_item)fix_itm.propagate_items().elementAt(l);
                        lalr_item existing = new_st.items().find(new_itm);
                        if (existing != null) {
                           fix_itm.propagate_items().setElementAt(existing, l);
                        }
                     }
                  }
               }
            }
         }

         propagate_all_lookaheads();
         return start_state;
      }
   }

   protected void propagate_lookaheads() throws internal_error {
      Enumeration itm = this.items().all();

      while(itm.hasMoreElements()) {
         ((lalr_item)itm.nextElement()).propagate_lookaheads((terminal_set)null);
      }

   }

   public void build_table_entries(parse_action_table act_table, parse_reduce_table reduce_table) throws internal_error {
      terminal_set conflict_set = new terminal_set();
      parse_action_row our_act_row = act_table.under_state[this.index()];
      parse_reduce_row our_red_row = reduce_table.under_state[this.index()];
      Enumeration i = this.items().all();

      while(true) {
         lalr_item itm;
         do {
            if (!i.hasMoreElements()) {
               for(lalr_transition trans = this.transitions(); trans != null; trans = trans.next()) {
                  symbol sym = trans.on_symbol();
                  if (!sym.is_non_term()) {
                     parse_action act = new shift_action(trans.to_state());
                     if (our_act_row.under_term[sym.index()].kind() == 0) {
                        our_act_row.under_term[sym.index()] = act;
                     } else {
                        production p = ((reduce_action)our_act_row.under_term[sym.index()]).reduce_with();
                        if (!this.fix_with_precedence(p, sym.index(), our_act_row, act)) {
                           our_act_row.under_term[sym.index()] = act;
                           conflict_set.add(terminal.find(sym.index()));
                        }
                     }
                  } else {
                     our_red_row.under_non_term[sym.index()] = trans.to_state();
                  }
               }

               if (!conflict_set.empty()) {
                  this.report_conflicts(conflict_set);
               }

               return;
            }

            itm = (lalr_item)i.nextElement();
         } while(!itm.dot_at_end());

         parse_action act = new reduce_action(itm.the_production());

         for(int t = 0; t < terminal.number(); ++t) {
            if (itm.lookahead().contains(t)) {
               if (our_act_row.under_term[t].kind() == 0) {
                  our_act_row.under_term[t] = act;
               } else {
                  terminal term = terminal.find(t);
                  parse_action other_act = our_act_row.under_term[t];
                  if (other_act.kind() != 1 && other_act.kind() != 3) {
                     if (itm.the_production().index() < ((reduce_action)other_act).reduce_with().index()) {
                        our_act_row.under_term[t] = act;
                     }
                  } else if (this.fix_with_precedence(itm.the_production(), t, our_act_row, act)) {
                     term = null;
                  }

                  if (term != null) {
                     conflict_set.add(term);
                  }
               }
            }
         }
      }
   }

   protected boolean fix_with_precedence(production p, int term_index, parse_action_row table_row, parse_action act) throws internal_error {
      terminal term = terminal.find(term_index);
      if (p.precedence_num() > -1) {
         if (p.precedence_num() > term.precedence_num()) {
            table_row.under_term[term_index] = this.insert_reduce(table_row.under_term[term_index], act);
            return true;
         } else if (p.precedence_num() < term.precedence_num()) {
            table_row.under_term[term_index] = this.insert_shift(table_row.under_term[term_index], act);
            return true;
         } else if (term.precedence_side() == 1) {
            table_row.under_term[term_index] = this.insert_shift(table_row.under_term[term_index], act);
            return true;
         } else if (term.precedence_side() == 0) {
            table_row.under_term[term_index] = this.insert_reduce(table_row.under_term[term_index], act);
            return true;
         } else if (term.precedence_side() == 2) {
            table_row.under_term[term_index] = new nonassoc_action();
            return true;
         } else {
            throw new internal_error("Unable to resolve conflict correctly");
         }
      } else if (term.precedence_num() > -1) {
         table_row.under_term[term_index] = this.insert_shift(table_row.under_term[term_index], act);
         return true;
      } else {
         return false;
      }
   }

   protected parse_action insert_action(parse_action a1, parse_action a2, int act_type) throws internal_error {
      if (a1.kind() == act_type && a2.kind() == act_type) {
         throw new internal_error("Conflict resolution of bogus actions");
      } else if (a1.kind() == act_type) {
         return a1;
      } else if (a2.kind() == act_type) {
         return a2;
      } else {
         throw new internal_error("Conflict resolution of bogus actions");
      }
   }

   protected parse_action insert_shift(parse_action a1, parse_action a2) throws internal_error {
      return this.insert_action(a1, a2, 1);
   }

   protected parse_action insert_reduce(parse_action a1, parse_action a2) throws internal_error {
      return this.insert_action(a1, a2, 2);
   }

   protected void report_conflicts(terminal_set conflict_set) throws internal_error {
      Enumeration itms = this.items().all();

      while(true) {
         lalr_item itm;
         do {
            if (!itms.hasMoreElements()) {
               return;
            }

            itm = (lalr_item)itms.nextElement();
         } while(!itm.dot_at_end());

         boolean after_itm = false;
         Enumeration comps = this.items().all();

         while(comps.hasMoreElements()) {
            lalr_item compare = (lalr_item)comps.nextElement();
            if (itm == compare) {
               after_itm = true;
            }

            if (itm != compare && compare.dot_at_end() && after_itm && compare.lookahead().intersects(itm.lookahead())) {
               this.report_reduce_reduce(itm, compare);
            }
         }

         terminal_set lookahead = itm.lookahead();

         for(int t = 0; t < terminal.number(); ++t) {
            if (conflict_set.contains(t) && lookahead.contains(t)) {
               this.report_shift_reduce(itm, t);
            }
         }
      }
   }

   protected void report_reduce_reduce(lalr_item itm1, lalr_item itm2) throws internal_error {
      boolean comma_flag = false;
      String message = "*** Reduce/Reduce conflict found in state #" + this.index() + "\n" + "  between " + itm1.to_simple_string() + "\n" + "  and     " + itm2.to_simple_string() + "\n" + "  under symbols: {";

      for(int t = 0; t < terminal.number(); ++t) {
         if (itm1.lookahead().contains(t) && itm2.lookahead().contains(t)) {
            if (comma_flag) {
               message = message + ", ";
            } else {
               comma_flag = true;
            }

            message = message + terminal.find(t).name();
         }
      }

      message = message + "}\n  Resolved in favor of ";
      if (itm1.the_production().index() < itm2.the_production().index()) {
         message = message + "the first production.\n";
      } else {
         message = message + "the second production.\n";
      }

      ++emit.num_conflicts;
      ErrorManager.getManager().emit_warning(message);
   }

   protected void report_shift_reduce(lalr_item red_itm, int conflict_sym) throws internal_error {
      String message = "*** Shift/Reduce conflict found in state #" + this.index() + "\n" + "  between " + red_itm.to_simple_string() + "\n";
      int relevancecounter = 0;
      Enumeration itms = this.items().all();

      while(itms.hasMoreElements()) {
         lalr_item itm = (lalr_item)itms.nextElement();
         if (itm != red_itm && !itm.dot_at_end()) {
            symbol shift_sym = itm.symbol_after_dot();
            if (!shift_sym.is_non_term() && shift_sym.index() == conflict_sym) {
               ++relevancecounter;
               message = message + "  and     " + itm.to_simple_string() + "\n";
            }
         }
      }

      message = message + "  under symbol " + terminal.find(conflict_sym).name() + "\n" + "  Resolved in favor of shifting.\n";
      if (relevancecounter != 0) {
         ++emit.num_conflicts;
         ErrorManager.getManager().emit_warning(message);
      }
   }

   public boolean equals(lalr_state other) {
      return other != null && this.items().equals(other.items());
   }

   public boolean equals(Object other) {
      return !(other instanceof lalr_state) ? false : this.equals((lalr_state)other);
   }

   public int hashCode() {
      return this.items().hashCode();
   }

   public String toString() {
      String result = "lalr_state [" + this.index() + "]: " + this._items + "\n";

      for(lalr_transition tr = this.transitions(); tr != null; tr = tr.next()) {
         result = result + tr;
         result = result + "\n";
      }

      return result;
   }
}
