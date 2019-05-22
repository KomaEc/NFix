package polyglot.util;

public class IdentityKey {
   Object obj;

   public IdentityKey(Object obj) {
      this.obj = obj;
   }

   public Object object() {
      return this.obj;
   }

   public int hashCode() {
      return System.identityHashCode(this.obj);
   }

   public boolean equals(Object other) {
      return other instanceof IdentityKey && ((IdentityKey)other).obj == this.obj;
   }

   public String toString() {
      return "Id(" + this.obj + ")";
   }
}
