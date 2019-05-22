package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;

public class non_terminal extends symbol {
   protected static Hashtable _all = new Hashtable();
   protected static Hashtable _all_by_index = new Hashtable();
   protected static int next_index = 0;
   protected static int next_nt = 0;
   public static final non_terminal START_nt = new non_terminal("$START");
   public boolean is_embedded_action;
   protected Hashtable _productions;
   protected boolean _nullable;
   protected terminal_set _first_set;

   public non_terminal(String nm, String tp) {
      super(nm, tp);
      this.is_embedded_action = false;
      this._productions = new Hashtable(11);
      this._first_set = new terminal_set();
      Object conflict = _all.put(nm, this);
      if (conflict != null) {
         (new internal_error("Duplicate non-terminal (" + nm + ") created")).crash();
      }

      this._index = next_index++;
      _all_by_index.put(new Integer(this._index), this);
   }

   public non_terminal(String nm) {
      this(nm, (String)null);
   }

   public static void clear() {
      _all.clear();
      _all_by_index.clear();
      next_index = 0;
      next_nt = 0;
   }

   public static Enumeration all() {
      return _all.elements();
   }

   public static non_terminal find(String with_name) {
      return with_name == null ? null : (non_terminal)_all.get(with_name);
   }

   public static non_terminal find(int indx) {
      Integer the_indx = new Integer(indx);
      return (non_terminal)_all_by_index.get(the_indx);
   }

   public static int number() {
      return _all.size();
   }

   static non_terminal create_new(String prefix) throws internal_error {
      return create_new(prefix, (String)null);
   }

   static non_terminal create_new() throws internal_error {
      return create_new((String)null);
   }

   static non_terminal create_new(String prefix, String type) throws internal_error {
      if (prefix == null) {
         prefix = "NT$";
      }

      return new non_terminal(prefix + next_nt++, type);
   }

   public static void compute_nullability() throws internal_error {
      boolean change = true;

      Enumeration e;
      while(change) {
         change = false;
         e = all();

         while(e.hasMoreElements()) {
            non_terminal nt = (non_terminal)e.nextElement();
            if (!nt.nullable() && nt.looks_nullable()) {
               nt._nullable = true;
               change = true;
            }
         }
      }

      e = production.all();

      while(e.hasMoreElements()) {
         production prod = (production)e.nextElement();
         prod.set_nullable(prod.check_nullable());
      }

   }

   public static void compute_first_sets() throws internal_error {
      boolean change = true;

      while(change) {
         change = false;
         Enumeration n = all();

         while(n.hasMoreElements()) {
            non_terminal nt = (non_terminal)n.nextElement();
            Enumeration p = nt.productions();

            while(p.hasMoreElements()) {
               production prod = (production)p.nextElement();
               terminal_set prod_first = prod.check_first_set();
               if (!prod_first.is_subset_of(nt._first_set)) {
                  change = true;
                  nt._first_set.add(prod_first);
               }
            }
         }
      }

   }

   public Enumeration productions() {
      return this._productions.elements();
   }

   public int num_productions() {
      return this._productions.size();
   }

   public void add_production(production prod) throws internal_error {
      if (prod != null && prod.lhs() != null && prod.lhs().the_symbol() == this) {
         this._productions.put(prod, prod);
      } else {
         throw new internal_error("Attempt to add invalid production to non terminal production table");
      }
   }

   public boolean nullable() {
      return this._nullable;
   }

   public terminal_set first_set() {
      return this._first_set;
   }

   public boolean is_non_term() {
      return true;
   }

   protected boolean looks_nullable() throws internal_error {
      Enumeration e = this.productions();

      while(e.hasMoreElements()) {
         if (((production)e.nextElement()).check_nullable()) {
            return true;
         }
      }

      return false;
   }

   public String toString() {
      return super.toString() + "[" + this.index() + "]" + (this.nullable() ? "*" : "");
   }
}
