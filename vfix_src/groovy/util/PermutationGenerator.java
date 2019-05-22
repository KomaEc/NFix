package groovy.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PermutationGenerator<E> implements Iterator<List<E>> {
   private int[] a;
   private BigInteger numLeft;
   private BigInteger total;
   private List<E> items;

   public PermutationGenerator(Collection<E> items) {
      this.items = new ArrayList(items);
      int n = items.size();
      if (n < 1) {
         throw new IllegalArgumentException("At least one item required");
      } else {
         this.a = new int[n];
         this.total = getFactorial(n);
         this.reset();
      }
   }

   public void reset() {
      for(int i = 0; i < this.a.length; this.a[i] = i++) {
      }

      this.numLeft = new BigInteger(this.total.toString());
   }

   public BigInteger getTotal() {
      return this.total;
   }

   public boolean hasNext() {
      return this.numLeft.compareTo(BigInteger.ZERO) == 1;
   }

   private static BigInteger getFactorial(int n) {
      BigInteger fact = BigInteger.ONE;

      for(int i = n; i > 1; --i) {
         fact = fact.multiply(new BigInteger(Integer.toString(i)));
      }

      return fact;
   }

   public List<E> next() {
      if (this.numLeft.equals(this.total)) {
         this.numLeft = this.numLeft.subtract(BigInteger.ONE);
         return this.items;
      } else {
         int j;
         for(j = this.a.length - 2; this.a[j] > this.a[j + 1]; --j) {
         }

         int k;
         for(k = this.a.length - 1; this.a[j] > this.a[k]; --k) {
         }

         int temp = this.a[k];
         this.a[k] = this.a[j];
         this.a[j] = temp;
         int r = this.a.length - 1;

         for(int s = j + 1; r > s; ++s) {
            temp = this.a[s];
            this.a[s] = this.a[r];
            this.a[r] = temp;
            --r;
         }

         this.numLeft = this.numLeft.subtract(BigInteger.ONE);
         List<E> ans = new ArrayList(this.a.length);
         int[] arr$ = this.a;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int index = arr$[i$];
            ans.add(this.items.get(index));
         }

         return ans;
      }
   }

   public void remove() {
      throw new UnsupportedOperationException("remove() not allowed for PermutationGenerator");
   }
}
