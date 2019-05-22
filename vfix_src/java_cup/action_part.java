package java_cup;

public class action_part extends production_part {
   protected String _code_string;

   public action_part(String code_str) {
      super((String)null);
      this._code_string = code_str;
   }

   public String code_string() {
      return this._code_string;
   }

   public void set_code_string(String new_str) {
      this._code_string = new_str;
   }

   public boolean is_action() {
      return true;
   }

   public boolean equals(action_part other) {
      return other != null && super.equals((production_part)other) && other.code_string().equals(this.code_string());
   }

   public boolean equals(Object other) {
      return !(other instanceof action_part) ? false : this.equals((action_part)other);
   }

   public int hashCode() {
      return super.hashCode() ^ (this.code_string() == null ? 0 : this.code_string().hashCode());
   }

   public String toString() {
      return super.toString() + "{" + this.code_string() + "}";
   }
}
