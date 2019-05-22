package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;

public class production {
   protected static Hashtable _all = new Hashtable();
   protected static int next_index;
   protected symbol_part _lhs;
   protected int _rhs_prec;
   protected int _rhs_assoc;
   protected production_part[] _rhs;
   protected int _rhs_length;
   protected action_part _action;
   protected int _index;
   protected int _num_reductions;
   protected boolean _nullable_known;
   protected boolean _nullable;
   protected terminal_set _first_set;

   public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, String action_str) throws internal_error {
      this._rhs_prec = -1;
      this._rhs_assoc = -1;
      this._num_reductions = 0;
      this._nullable_known = false;
      this._nullable = false;
      this._first_set = new terminal_set();
      int rightlen = rhs_l;
      if (rhs_l >= 0) {
         this._rhs_length = rhs_l;
      } else if (rhs_parts != null) {
         this._rhs_length = rhs_parts.length;
      } else {
         this._rhs_length = 0;
      }

      if (lhs_sym == null) {
         throw new internal_error("Attempt to construct a production with a null LHS");
      } else {
         if (rhs_l > 0) {
            if (rhs_parts[rhs_l - 1].is_action()) {
               rightlen = rhs_l - 1;
            } else {
               rightlen = rhs_l;
            }
         }

         String declare_str = this.declare_labels(rhs_parts, rightlen, action_str);
         if (action_str == null) {
            action_str = declare_str;
         } else {
            action_str = declare_str + action_str;
         }

         lhs_sym.note_use();
         this._lhs = new symbol_part(lhs_sym);
         this._rhs_length = this.merge_adjacent_actions(rhs_parts, this._rhs_length);
         action_part tail_action = this.strip_trailing_action(rhs_parts, this._rhs_length);
         if (tail_action != null) {
            --this._rhs_length;
         }

         this._rhs = new production_part[this._rhs_length];

         for(int i = 0; i < this._rhs_length; ++i) {
            this._rhs[i] = rhs_parts[i];
            if (!this._rhs[i].is_action()) {
               ((symbol_part)this._rhs[i]).the_symbol().note_use();
               if (((symbol_part)this._rhs[i]).the_symbol() instanceof terminal) {
                  this._rhs_prec = ((terminal)((symbol_part)this._rhs[i]).the_symbol()).precedence_num();
                  this._rhs_assoc = ((terminal)((symbol_part)this._rhs[i]).the_symbol()).precedence_side();
               }
            }
         }

         if (action_str == null) {
            action_str = "";
         }

         if (tail_action != null && tail_action.code_string() != null) {
            action_str = action_str + "\t\t" + tail_action.code_string();
         }

         this._action = new action_part(action_str);
         this.remove_embedded_actions();
         this._index = next_index++;
         _all.put(new Integer(this._index), this);
         lhs_sym.add_production(this);
      }
   }

   public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l) throws internal_error {
      this(lhs_sym, rhs_parts, rhs_l, (String)null);
   }

   public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, String action_str, int prec_num, int prec_side) throws internal_error {
      this(lhs_sym, rhs_parts, rhs_l, action_str);
      this.set_precedence_num(prec_num);
      this.set_precedence_side(prec_side);
   }

   public production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, int prec_num, int prec_side) throws internal_error {
      this(lhs_sym, rhs_parts, rhs_l, (String)null);
      this.set_precedence_num(prec_num);
      this.set_precedence_side(prec_side);
   }

   public static Enumeration all() {
      return _all.elements();
   }

   public static production find(int indx) {
      return (production)_all.get(new Integer(indx));
   }

   public static void clear() {
      _all.clear();
      next_index = 0;
   }

   public static int number() {
      return _all.size();
   }

   public symbol_part lhs() {
      return this._lhs;
   }

   public int precedence_num() {
      return this._rhs_prec;
   }

   public int precedence_side() {
      return this._rhs_assoc;
   }

   public void set_precedence_num(int prec_num) {
      this._rhs_prec = prec_num;
   }

   public void set_precedence_side(int prec_side) {
      this._rhs_assoc = prec_side;
   }

   public production_part rhs(int indx) throws internal_error {
      if (indx >= 0 && indx < this._rhs_length) {
         return this._rhs[indx];
      } else {
         throw new internal_error("Index out of range for right hand side of production");
      }
   }

   public int rhs_length() {
      return this._rhs_length;
   }

   public action_part action() {
      return this._action;
   }

   public int index() {
      return this._index;
   }

   public int num_reductions() {
      return this._num_reductions;
   }

   public void note_reduction_use() {
      ++this._num_reductions;
   }

   public boolean nullable_known() {
      return this._nullable_known;
   }

   public boolean nullable() {
      return this._nullable;
   }

   public terminal_set first_set() {
      return this._first_set;
   }

   protected static boolean is_id_start(char c) {
      return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
   }

   protected static boolean is_id_char(char c) {
      return is_id_start(c) || c >= '0' && c <= '9';
   }

   protected String make_declaration(String labelname, String stack_type, int offset) {
      String ret;
      if (emit.lr_values()) {
         if (!emit.locations()) {
            ret = "\t\tint " + labelname + "left = ((java_cup.runtime.Symbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + "-" + offset + ")") + ").left;\n" + "\t\tint " + labelname + "right = ((java_cup.runtime.Symbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + "-" + offset + ")") + ").right;\n";
         } else {
            ret = "\t\tLocation " + labelname + "xleft = ((java_cup.runtime.ComplexSymbolFactory.ComplexSymbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + "-" + offset + ")") + ").xleft;\n" + "\t\tLocation " + labelname + "xright = ((java_cup.runtime.ComplexSymbolFactory.ComplexSymbol)" + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + "-" + offset + ")") + ").xright;\n";
         }
      } else {
         ret = "";
      }

      return ret + "\t\t" + stack_type + " " + labelname + " = (" + stack_type + ")((" + "java_cup.runtime.Symbol) " + emit.pre("stack") + (offset == 0 ? ".peek()" : ".elementAt(" + emit.pre("top") + "-" + offset + ")") + ").value;\n";
   }

   protected String declare_labels(production_part[] rhs, int rhs_len, String final_action) {
      String declaration = "";

      for(int pos = 0; pos < rhs_len; ++pos) {
         if (!rhs[pos].is_action()) {
            symbol_part part = (symbol_part)rhs[pos];
            String label;
            if ((label = part.label()) != null || emit._xmlactions) {
               if (label == null) {
                  label = part.the_symbol().name() + pos;
               }

               declaration = declaration + this.make_declaration(label, part.the_symbol().stack_type(), rhs_len - pos - 1);
            }
         }
      }

      return declaration;
   }

   protected int merge_adjacent_actions(production_part[] rhs_parts, int len) {
      if (rhs_parts != null && len != 0) {
         int merge_cnt = 0;
         int to_loc = -1;

         for(int from_loc = 0; from_loc < len; ++from_loc) {
            if (to_loc < 0 || !rhs_parts[to_loc].is_action() || !rhs_parts[from_loc].is_action()) {
               ++to_loc;
               if (to_loc != from_loc) {
                  rhs_parts[to_loc] = null;
               }
            }

            if (to_loc != from_loc) {
               if (rhs_parts[to_loc] != null && rhs_parts[to_loc].is_action() && rhs_parts[from_loc].is_action()) {
                  rhs_parts[to_loc] = new action_part(((action_part)rhs_parts[to_loc]).code_string() + ((action_part)rhs_parts[from_loc]).code_string());
                  ++merge_cnt;
               } else {
                  rhs_parts[to_loc] = rhs_parts[from_loc];
               }
            }
         }

         return len - merge_cnt;
      } else {
         return 0;
      }
   }

   protected action_part strip_trailing_action(production_part[] rhs_parts, int len) {
      if (rhs_parts != null && len != 0) {
         if (rhs_parts[len - 1].is_action()) {
            action_part result = (action_part)rhs_parts[len - 1];
            rhs_parts[len - 1] = null;
            return result;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   protected void remove_embedded_actions() throws internal_error {
      int lastLocation = -1;

      for(int act_loc = 0; act_loc < this.rhs_length(); ++act_loc) {
         if (this.rhs(act_loc).is_action()) {
            String declare_str = this.declare_labels(this._rhs, act_loc, "");
            non_terminal new_nt = non_terminal.create_new((String)null, this.lhs().the_symbol().stack_type());
            new_nt.is_embedded_action = true;
            new action_production(this, new_nt, (production_part[])null, 0, declare_str + ((action_part)this.rhs(act_loc)).code_string(), lastLocation == -1 ? -1 : act_loc - lastLocation);
            this._rhs[act_loc] = new symbol_part(new_nt);
            lastLocation = act_loc;
         }
      }

   }

   public boolean check_nullable() throws internal_error {
      if (this.nullable_known()) {
         return this.nullable();
      } else if (this.rhs_length() == 0) {
         return this.set_nullable(true);
      } else {
         for(int pos = 0; pos < this.rhs_length(); ++pos) {
            production_part part = this.rhs(pos);
            if (!part.is_action()) {
               symbol sym = ((symbol_part)part).the_symbol();
               if (!sym.is_non_term()) {
                  return this.set_nullable(false);
               }

               if (!((non_terminal)sym).nullable()) {
                  return false;
               }
            }
         }

         return this.set_nullable(true);
      }
   }

   boolean set_nullable(boolean v) {
      this._nullable_known = true;
      this._nullable = v;
      return v;
   }

   public terminal_set check_first_set() throws internal_error {
      for(int part = 0; part < this.rhs_length(); ++part) {
         if (!this.rhs(part).is_action()) {
            symbol sym = ((symbol_part)this.rhs(part)).the_symbol();
            if (!sym.is_non_term()) {
               this._first_set.add((terminal)sym);
               break;
            }

            this._first_set.add(((non_terminal)sym).first_set());
            if (!((non_terminal)sym).nullable()) {
               break;
            }
         }
      }

      return this.first_set();
   }

   public boolean equals(production other) {
      if (other == null) {
         return false;
      } else {
         return other._index == this._index;
      }
   }

   public boolean equals(Object other) {
      return !(other instanceof production) ? false : this.equals((production)other);
   }

   public int hashCode() {
      return this._index * 13;
   }

   public String toString() {
      String result;
      try {
         result = "production [" + this.index() + "]: ";
         result = result + (this.lhs() != null ? this.lhs().toString() : "$$NULL-LHS$$");
         result = result + " :: = ";

         for(int i = 0; i < this.rhs_length(); ++i) {
            result = result + this.rhs(i) + " ";
         }

         result = result + ";";
         if (this.action() != null && this.action().code_string() != null) {
            result = result + " {" + this.action().code_string() + "}";
         }

         if (this.nullable_known()) {
            if (this.nullable()) {
               result = result + "[NULLABLE]";
            } else {
               result = result + "[NOT NULLABLE]";
            }
         }
      } catch (internal_error var3) {
         var3.crash();
         result = null;
      }

      return result;
   }

   public String to_simple_string() throws internal_error {
      String result = this.lhs() != null ? this.lhs().the_symbol().name() : "NULL_LHS";
      result = result + " ::= ";

      for(int i = 0; i < this.rhs_length(); ++i) {
         if (!this.rhs(i).is_action()) {
            result = result + ((symbol_part)this.rhs(i)).the_symbol().name() + " ";
         }
      }

      return result;
   }
}
