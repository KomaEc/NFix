package groovy.lang;

public class ParameterArray {
   private Object parameters;

   public ParameterArray(Object data) {
      this.parameters = this.packArray(data);
   }

   private Object packArray(Object object) {
      return object instanceof Object[] ? (Object[])((Object[])object) : object;
   }

   public Object get() {
      return this.parameters;
   }

   public String toString() {
      return this.parameters == null ? "<null parameter>" : this.parameters.toString();
   }
}
