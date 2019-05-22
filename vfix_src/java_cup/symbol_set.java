package java_cup;

import java.util.Enumeration;
import java.util.Hashtable;

public class symbol_set {
   protected Hashtable _all = new Hashtable(11);

   public symbol_set() {
   }

   public symbol_set(symbol_set other) throws internal_error {
      this.not_null(other);
      this._all = (Hashtable)other._all.clone();
   }

   public Enumeration all() {
      return this._all.elements();
   }

   public int size() {
      return this._all.size();
   }

   protected void not_null(Object obj) throws internal_error {
      if (obj == null) {
         throw new internal_error("Null object used in set operation");
      }
   }

   public boolean contains(symbol sym) {
      return this._all.containsKey(sym.name());
   }

   public boolean is_subset_of(symbol_set other) throws internal_error {
      this.not_null(other);
      Enumeration e = this.all();

      while(e.hasMoreElements()) {
         if (!other.contains((symbol)e.nextElement())) {
            return false;
         }
      }

      return true;
   }

   public boolean is_superset_of(symbol_set other) throws internal_error {
      this.not_null(other);
      return other.is_subset_of(this);
   }

   public boolean add(symbol sym) throws internal_error {
      this.not_null(sym);
      Object previous = this._all.put(sym.name(), sym);
      return previous == null;
   }

   public void remove(symbol sym) throws internal_error {
      this.not_null(sym);
      this._all.remove(sym.name());
   }

   public boolean add(symbol_set other) throws internal_error {
      boolean result = false;
      this.not_null(other);

      for(Enumeration e = other.all(); e.hasMoreElements(); result = this.add((symbol)e.nextElement()) || result) {
      }

      return result;
   }

   public void remove(symbol_set other) throws internal_error {
      this.not_null(other);
      Enumeration e = other.all();

      while(e.hasMoreElements()) {
         this.remove((symbol)e.nextElement());
      }

   }

   public boolean equals(symbol_set other) {
      if (other != null && other.size() == this.size()) {
         try {
            return this.is_subset_of(other);
         } catch (internal_error var3) {
            var3.crash();
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean equals(Object other) {
      return !(other instanceof symbol_set) ? false : this.equals((symbol_set)other);
   }

   public int hashCode() {
      int result = 0;
      Enumeration e = this.all();

      for(int cnt = 0; e.hasMoreElements() && cnt < 5; ++cnt) {
         result ^= ((symbol)e.nextElement()).hashCode();
      }

      return result;
   }

   public String toString() {
      String result = "{";
      boolean comma_flag = false;

      for(Enumeration e = this.all(); e.hasMoreElements(); result = result + ((symbol)e.nextElement()).name()) {
         if (comma_flag) {
            result = result + ", ";
         } else {
            comma_flag = true;
         }
      }

      result = result + "}";
      return result;
   }
}
