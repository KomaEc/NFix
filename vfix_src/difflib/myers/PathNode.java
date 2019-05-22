package difflib.myers;

public abstract class PathNode {
   public final int i;
   public final int j;
   public final PathNode prev;

   public PathNode(int i, int j, PathNode prev) {
      this.i = i;
      this.j = j;
      this.prev = prev;
   }

   public abstract boolean isSnake();

   public boolean isBootstrap() {
      return this.i < 0 || this.j < 0;
   }

   public final PathNode previousSnake() {
      if (this.isBootstrap()) {
         return null;
      } else {
         return !this.isSnake() && this.prev != null ? this.prev.previousSnake() : this;
      }
   }

   public String toString() {
      StringBuffer buf = new StringBuffer("[");

      for(PathNode node = this; node != null; node = node.prev) {
         buf.append("(");
         buf.append(Integer.toString(node.i));
         buf.append(",");
         buf.append(Integer.toString(node.j));
         buf.append(")");
      }

      buf.append("]");
      return buf.toString();
   }
}
