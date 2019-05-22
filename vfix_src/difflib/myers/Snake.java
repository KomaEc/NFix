package difflib.myers;

public final class Snake extends PathNode {
   public Snake(int i, int j, PathNode prev) {
      super(i, j, prev);
   }

   public boolean isSnake() {
      return true;
   }
}
