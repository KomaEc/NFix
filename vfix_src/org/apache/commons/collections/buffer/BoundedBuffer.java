package org.apache.commons.collections.buffer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.BoundedCollection;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferOverflowException;
import org.apache.commons.collections.BufferUnderflowException;
import org.apache.commons.collections.iterators.AbstractIteratorDecorator;

public class BoundedBuffer extends SynchronizedBuffer implements BoundedCollection {
   private static final long serialVersionUID = 1536432911093974264L;
   private final int maximumSize;
   private final long timeout;

   public static BoundedBuffer decorate(Buffer buffer, int maximumSize) {
      return new BoundedBuffer(buffer, maximumSize, 0L);
   }

   public static BoundedBuffer decorate(Buffer buffer, int maximumSize, long timeout) {
      return new BoundedBuffer(buffer, maximumSize, timeout);
   }

   protected BoundedBuffer(Buffer buffer, int maximumSize, long timeout) {
      super(buffer);
      if (maximumSize < 1) {
         throw new IllegalArgumentException();
      } else {
         this.maximumSize = maximumSize;
         this.timeout = timeout;
      }
   }

   public Object remove() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object returnValue = this.getBuffer().remove();
         super.lock.notifyAll();
         return returnValue;
      }
   }

   public boolean add(Object o) {
      Object var2 = super.lock;
      synchronized(var2) {
         this.timeoutWait(1);
         boolean var3 = this.getBuffer().add(o);
         return var3;
      }
   }

   public boolean addAll(Collection c) {
      Object var2 = super.lock;
      synchronized(var2) {
         this.timeoutWait(c.size());
         boolean var3 = this.getBuffer().addAll(c);
         return var3;
      }
   }

   public Iterator iterator() {
      return new BoundedBuffer.NotifyingIterator(super.collection.iterator());
   }

   private void timeoutWait(int nAdditions) {
      if (nAdditions > this.maximumSize) {
         throw new BufferOverflowException("Buffer size cannot exceed " + this.maximumSize);
      } else if (this.timeout <= 0L) {
         if (this.getBuffer().size() + nAdditions > this.maximumSize) {
            throw new BufferOverflowException("Buffer size cannot exceed " + this.maximumSize);
         }
      } else {
         long expiration = System.currentTimeMillis() + this.timeout;
         long timeLeft = expiration - System.currentTimeMillis();

         while(timeLeft > 0L && this.getBuffer().size() + nAdditions > this.maximumSize) {
            try {
               super.lock.wait(timeLeft);
               timeLeft = expiration - System.currentTimeMillis();
            } catch (InterruptedException var8) {
               PrintWriter out = new PrintWriter(new StringWriter());
               var8.printStackTrace(out);
               throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
            }
         }

         if (this.getBuffer().size() + nAdditions > this.maximumSize) {
            throw new BufferOverflowException("Timeout expired");
         }
      }
   }

   public boolean isFull() {
      return this.size() == this.maxSize();
   }

   public int maxSize() {
      return this.maximumSize;
   }

   // $FF: synthetic method
   static Object access$001(BoundedBuffer x0) {
      return x0.lock;
   }

   // $FF: synthetic method
   static Object access$101(BoundedBuffer x0) {
      return x0.lock;
   }

   private class NotifyingIterator extends AbstractIteratorDecorator {
      public NotifyingIterator(Iterator it) {
         super(it);
      }

      public void remove() {
         Object var1 = BoundedBuffer.access$001(BoundedBuffer.this);
         synchronized(var1) {
            super.iterator.remove();
            BoundedBuffer.access$101(BoundedBuffer.this).notifyAll();
         }
      }
   }
}
