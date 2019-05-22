package soot.util.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class QueueReader<E> implements Iterator<E> {
   private E[] q;
   private int index;

   protected QueueReader(E[] q, int index) {
      this.q = q;
      this.index = index;
   }

   public E next() {
      Object ret = null;

      do {
         if (this.q[this.index] == null) {
            throw new NoSuchElementException();
         }

         if (this.index == this.q.length - 1) {
            this.q = (Object[])((Object[])this.q[this.index]);
            this.index = 0;
            if (this.q[this.index] == null) {
               throw new NoSuchElementException();
            }
         }

         ret = this.q[this.index];
         if (ret == ChunkedQueue.NULL_CONST) {
            ret = null;
         }

         ++this.index;
      } while(ret == ChunkedQueue.DELETED_CONST);

      return ret;
   }

   public boolean hasNext() {
      while(this.q[this.index] != null) {
         if (this.index == this.q.length - 1) {
            this.q = (Object[])((Object[])this.q[this.index]);
            this.index = 0;
            if (this.q[this.index] == null) {
               return false;
            }
         }

         if (this.q[this.index] != ChunkedQueue.DELETED_CONST) {
            return true;
         }

         ++this.index;
      }

      return false;
   }

   public void remove(E o) {
      int idx = 0;

      for(Object[] curQ = this.q; curQ[idx] != null; ++idx) {
         if (idx == curQ.length - 1) {
            curQ = (Object[])((Object[])curQ[idx]);
            idx = 0;
         }

         if (o.equals(curQ[idx])) {
            curQ[idx] = ChunkedQueue.DELETED_CONST;
         }
      }

   }

   public void remove() {
      this.q[this.index - 1] = ChunkedQueue.DELETED_CONST;
   }

   public QueueReader<E> clone() {
      return new QueueReader(this.q, this.index);
   }
}
