package heros.solver;

public class PathEdge<N, D> {
   protected final N target;
   protected final D dSource;
   protected final D dTarget;
   protected final int hashCode;

   public PathEdge(D dSource, N target, D dTarget) {
      this.target = target;
      this.dSource = dSource;
      this.dTarget = dTarget;
      int prime = true;
      int result = 1;
      int result = 31 * result + (dSource == null ? 0 : dSource.hashCode());
      result = 31 * result + (dTarget == null ? 0 : dTarget.hashCode());
      result = 31 * result + (target == null ? 0 : target.hashCode());
      this.hashCode = result;
   }

   public N getTarget() {
      return this.target;
   }

   public D factAtSource() {
      return this.dSource;
   }

   public D factAtTarget() {
      return this.dTarget;
   }

   public int hashCode() {
      return this.hashCode;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         PathEdge other = (PathEdge)obj;
         if (this.dSource == null) {
            if (other.dSource != null) {
               return false;
            }
         } else if (!this.dSource.equals(other.dSource)) {
            return false;
         }

         if (this.dTarget == null) {
            if (other.dTarget != null) {
               return false;
            }
         } else if (!this.dTarget.equals(other.dTarget)) {
            return false;
         }

         if (this.target == null) {
            if (other.target != null) {
               return false;
            }
         } else if (!this.target.equals(other.target)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      StringBuffer result = new StringBuffer();
      result.append("<");
      result.append(this.dSource);
      result.append("> -> <");
      result.append(this.target.toString());
      result.append(",");
      result.append(this.dTarget);
      result.append(">");
      return result.toString();
   }
}
