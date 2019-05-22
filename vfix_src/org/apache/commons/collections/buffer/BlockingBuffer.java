package org.apache.commons.collections.buffer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.BufferUnderflowException;

public class BlockingBuffer extends SynchronizedBuffer {
   private static final long serialVersionUID = 1719328905017860541L;
   private final long timeout;

   public static Buffer decorate(Buffer buffer) {
      return new BlockingBuffer(buffer);
   }

   public static Buffer decorate(Buffer buffer, long timeoutMillis) {
      return new BlockingBuffer(buffer, timeoutMillis);
   }

   protected BlockingBuffer(Buffer buffer) {
      super(buffer);
      this.timeout = 0L;
   }

   protected BlockingBuffer(Buffer buffer, long timeoutMillis) {
      super(buffer);
      this.timeout = timeoutMillis < 0L ? 0L : timeoutMillis;
   }

   public boolean add(Object o) {
      Object var2 = super.lock;
      synchronized(var2) {
         boolean result = super.collection.add(o);
         super.lock.notifyAll();
         return result;
      }
   }

   public boolean addAll(Collection c) {
      Object var2 = super.lock;
      synchronized(var2) {
         boolean result = super.collection.addAll(c);
         super.lock.notifyAll();
         return result;
      }
   }

   public Object get() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2;
         while(super.collection.isEmpty()) {
            try {
               if (this.timeout > 0L) {
                  var2 = this.get(this.timeout);
                  return var2;
               }

               super.lock.wait();
            } catch (InterruptedException var5) {
               PrintWriter out = new PrintWriter(new StringWriter());
               var5.printStackTrace(out);
               throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
            }
         }

         var2 = this.getBuffer().get();
         return var2;
      }
   }

   public Object get(long timeout) {
      Object var3 = super.lock;
      synchronized(var3) {
         long expiration = System.currentTimeMillis() + timeout;
         long timeLeft = expiration - System.currentTimeMillis();

         while(timeLeft > 0L && super.collection.isEmpty()) {
            try {
               super.lock.wait(timeLeft);
               timeLeft = expiration - System.currentTimeMillis();
            } catch (InterruptedException var11) {
               PrintWriter out = new PrintWriter(new StringWriter());
               var11.printStackTrace(out);
               throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
            }
         }

         if (super.collection.isEmpty()) {
            throw new BufferUnderflowException("Timeout expired");
         } else {
            Object var8 = this.getBuffer().get();
            return var8;
         }
      }
   }

   public Object remove() {
      Object var1 = super.lock;
      synchronized(var1) {
         Object var2;
         while(super.collection.isEmpty()) {
            try {
               if (this.timeout > 0L) {
                  var2 = this.remove(this.timeout);
                  return var2;
               }

               super.lock.wait();
            } catch (InterruptedException var5) {
               PrintWriter out = new PrintWriter(new StringWriter());
               var5.printStackTrace(out);
               throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
            }
         }

         var2 = this.getBuffer().remove();
         return var2;
      }
   }

   public Object remove(long timeout) {
      Object var3 = super.lock;
      synchronized(var3) {
         long expiration = System.currentTimeMillis() + timeout;
         long timeLeft = expiration - System.currentTimeMillis();

         while(timeLeft > 0L && super.collection.isEmpty()) {
            try {
               super.lock.wait(timeLeft);
               timeLeft = expiration - System.currentTimeMillis();
            } catch (InterruptedException var11) {
               PrintWriter out = new PrintWriter(new StringWriter());
               var11.printStackTrace(out);
               throw new BufferUnderflowException("Caused by InterruptedException: " + out.toString());
            }
         }

         if (super.collection.isEmpty()) {
            throw new BufferUnderflowException("Timeout expired");
         } else {
            Object var8 = this.getBuffer().remove();
            return var8;
         }
      }
   }
}
