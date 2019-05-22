package difflib.myers;

import difflib.ChangeDelta;
import difflib.Chunk;
import difflib.DeleteDelta;
import difflib.Delta;
import difflib.DiffAlgorithm;
import difflib.InsertDelta;
import difflib.Patch;
import java.lang.reflect.Array;
import java.util.List;

public class MyersDiff implements DiffAlgorithm {
   public Patch diff(List<?> original, List<?> revised) {
      return this.diff(original.toArray(), revised.toArray());
   }

   public Patch diff(Object[] orig, Object[] rev) {
      try {
         PathNode path = buildPath(orig, rev);
         return buildRevision(path, orig, rev);
      } catch (DifferentiationFailedException var5) {
         var5.printStackTrace();
         return new Patch();
      }
   }

   public static PathNode buildPath(Object[] orig, Object[] rev) throws DifferentiationFailedException {
      if (orig == null) {
         throw new IllegalArgumentException("original sequence is null");
      } else if (rev == null) {
         throw new IllegalArgumentException("revised sequence is null");
      } else {
         int N = orig.length;
         int M = rev.length;
         int MAX = N + M + 1;
         int size = 1 + 2 * MAX;
         int middle = size / 2;
         PathNode[] diagonal = new PathNode[size];
         diagonal[middle + 1] = new Snake(0, -1, (PathNode)null);

         for(int d = 0; d < MAX; ++d) {
            for(int k = -d; k <= d; k += 2) {
               int kmiddle = middle + k;
               int kplus = kmiddle + 1;
               int kminus = kmiddle - 1;
               PathNode prev = null;
               int i;
               if (k != -d && (k == d || diagonal[kminus].i >= diagonal[kplus].i)) {
                  i = diagonal[kminus].i + 1;
                  prev = diagonal[kminus];
               } else {
                  i = diagonal[kplus].i;
                  prev = diagonal[kplus];
               }

               diagonal[kminus] = null;
               int j = i - k;

               Object node;
               for(node = new DiffNode(i, j, prev); i < N && j < M && orig[i].equals(rev[j]); ++j) {
                  ++i;
               }

               if (i > ((PathNode)node).i) {
                  node = new Snake(i, j, (PathNode)node);
               }

               diagonal[kmiddle] = (PathNode)node;
               if (i >= N && j >= M) {
                  return diagonal[kmiddle];
               }
            }

            diagonal[middle + d - 1] = null;
         }

         throw new DifferentiationFailedException("could not find a diff path");
      }
   }

   public static Patch buildRevision(PathNode path, Object[] orig, Object[] rev) {
      if (path == null) {
         throw new IllegalArgumentException("path is null");
      } else if (orig == null) {
         throw new IllegalArgumentException("original sequence is null");
      } else if (rev == null) {
         throw new IllegalArgumentException("revised sequence is null");
      } else {
         Patch patch = new Patch();
         if (path.isSnake()) {
            path = path.prev;
         }

         while(path != null && path.prev != null && path.prev.j >= 0) {
            if (path.isSnake()) {
               throw new IllegalStateException("bad diffpath: found snake when looking for diff");
            }

            int i = path.i;
            int j = path.j;
            path = path.prev;
            int ianchor = path.i;
            int janchor = path.j;
            Chunk original = new Chunk(ianchor, copyOfRange(orig, ianchor, i));
            Chunk revised = new Chunk(janchor, copyOfRange(rev, janchor, j));
            Delta delta = null;
            if (original.size() == 0 && revised.size() != 0) {
               delta = new InsertDelta(original, revised);
            } else if (original.size() > 0 && revised.size() == 0) {
               delta = new DeleteDelta(original, revised);
            } else {
               delta = new ChangeDelta(original, revised);
            }

            patch.addDelta((Delta)delta);
            if (path.isSnake()) {
               path = path.prev;
            }
         }

         return patch;
      }
   }

   public static <T> T[] copyOfRange(T[] original, int from, int to) {
      return copyOfRange(original, from, to, original.getClass());
   }

   public static <T, U> T[] copyOfRange(U[] original, int from, int to, Class<? extends T[]> newType) {
      int newLength = to - from;
      if (newLength < 0) {
         throw new IllegalArgumentException(from + " > " + to);
      } else {
         T[] copy = newType == Object[].class ? (Object[])(new Object[newLength]) : (Object[])((Object[])Array.newInstance(newType.getComponentType(), newLength));
         System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
         return copy;
      }
   }
}
