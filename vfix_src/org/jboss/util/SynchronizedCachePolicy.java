package org.jboss.util;

public final class SynchronizedCachePolicy implements CachePolicy {
   private final CachePolicy delegate;

   public SynchronizedCachePolicy(CachePolicy delegate) {
      this.delegate = delegate;
   }

   public synchronized Object get(Object key) {
      return this.delegate.get(key);
   }

   public synchronized Object peek(Object key) {
      return this.delegate.get(key);
   }

   public synchronized void insert(Object key, Object object) {
      this.delegate.insert(key, object);
   }

   public synchronized void remove(Object key) {
      this.delegate.remove(key);
   }

   public synchronized void flush() {
      this.delegate.flush();
   }

   public synchronized int size() {
      return this.delegate.size();
   }

   public synchronized void create() throws Exception {
      this.delegate.create();
   }

   public synchronized void start() throws Exception {
      this.delegate.start();
   }

   public synchronized void stop() {
      this.delegate.stop();
   }

   public synchronized void destroy() {
      this.delegate.destroy();
   }
}
