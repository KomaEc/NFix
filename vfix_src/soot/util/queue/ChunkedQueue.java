package soot.util.queue;

public final class ChunkedQueue<E> {
   static final Object NULL_CONST = new Object();
   static final Object DELETED_CONST = new Object();
   private static final int LENGTH = 60;
   private Object[] q = new Object[60];
   private int index = 0;

   public void add(E o) {
      if (o == null) {
         o = NULL_CONST;
      }

      if (this.index == 59) {
         Object[] temp = new Object[60];
         this.q[this.index] = temp;
         this.q = temp;
         this.index = 0;
      }

      this.q[this.index++] = o;
   }

   public QueueReader<E> reader() {
      return new QueueReader((Object[])this.q, this.index);
   }
}
