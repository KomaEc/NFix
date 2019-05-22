package soot.jimple.spark.ondemand.genericutil;

import java.util.Arrays;

public final class DisjointSets {
   private int[] array;

   public DisjointSets(int numElements) {
      this.array = new int[numElements];
      Arrays.fill(this.array, -1);
   }

   public void union(int root1, int root2) {
      assert this.array[root1] < 0;

      assert this.array[root2] < 0;

      assert root1 != root2;

      int[] var10000;
      if (this.array[root2] < this.array[root1]) {
         var10000 = this.array;
         var10000[root2] += this.array[root1];
         this.array[root1] = root2;
      } else {
         var10000 = this.array;
         var10000[root1] += this.array[root2];
         this.array[root2] = root1;
      }

   }

   public int find(int x) {
      if (this.array[x] < 0) {
         return x;
      } else {
         this.array[x] = this.find(this.array[x]);
         return this.array[x];
      }
   }
}
