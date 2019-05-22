package java_cup;

public abstract class symbol {
   protected String _name;
   protected String _stack_type;
   protected int _use_count;
   protected int _index;

   public symbol(String nm, String tp) {
      this._use_count = 0;
      if (nm == null) {
         nm = "";
      }

      if (tp == null) {
         tp = "Object";
      }

      this._name = nm;
      this._stack_type = tp;
   }

   public symbol(String nm) {
      this(nm, (String)null);
   }

   public String name() {
      return this._name;
   }

   public String stack_type() {
      return this._stack_type;
   }

   public int use_count() {
      return this._use_count;
   }

   public void note_use() {
      ++this._use_count;
   }

   public int index() {
      return this._index;
   }

   public abstract boolean is_non_term();

   public String toString() {
      return this.name();
   }
}
