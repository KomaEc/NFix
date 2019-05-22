package heros.solver;

import java.util.Arrays;

public interface JoinHandlingNode<T> {
   boolean handleJoin(T var1);

   JoinHandlingNode.JoinKey createJoinKey();

   void setCallingContext(T var1);

   public static class JoinKey {
      private Object[] elements;

      public JoinKey(Object... elements) {
         this.elements = elements;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + Arrays.hashCode(this.elements);
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            JoinHandlingNode.JoinKey other = (JoinHandlingNode.JoinKey)obj;
            return Arrays.equals(this.elements, other.elements);
         }
      }
   }
}
