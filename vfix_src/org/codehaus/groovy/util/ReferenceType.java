package org.codehaus.groovy.util;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public enum ReferenceType {
   SOFT {
      protected <T, V extends Finalizable> Reference<T, V> createReference(T value, V handler, ReferenceQueue queue) {
         return new ReferenceType.SoftRef(value, handler, queue);
      }
   },
   WEAK {
      protected <T, V extends Finalizable> Reference<T, V> createReference(T value, V handler, ReferenceQueue queue) {
         return new ReferenceType.WeakRef(value, handler, queue);
      }
   },
   PHANTOM {
      protected <T, V extends Finalizable> Reference<T, V> createReference(T value, V handler, ReferenceQueue queue) {
         return new ReferenceType.PhantomRef(value, handler, queue);
      }
   },
   HARD {
      protected <T, V extends Finalizable> Reference<T, V> createReference(T value, V handler, ReferenceQueue queue) {
         return new ReferenceType.HardRef(value, handler, queue);
      }
   };

   private ReferenceType() {
   }

   protected abstract <T, V extends Finalizable> Reference<T, V> createReference(T var1, V var2, ReferenceQueue var3);

   // $FF: synthetic method
   ReferenceType(Object x2) {
      this();
   }

   private static class HardRef<TT, V extends Finalizable> implements Reference<TT, V> {
      private TT ref;
      private final V handler;

      public HardRef(TT referent, V handler, ReferenceQueue<? super TT> q) {
         this.ref = referent;
         this.handler = handler;
      }

      public V getHandler() {
         return this.handler;
      }

      public TT get() {
         return this.ref;
      }

      public void clear() {
         this.ref = null;
      }
   }

   private static class PhantomRef<TT, V extends Finalizable> extends PhantomReference<TT> implements Reference<TT, V> {
      private final V handler;

      public PhantomRef(TT referent, V handler, ReferenceQueue<? super TT> q) {
         super(referent, q);
         this.handler = handler;
      }

      public V getHandler() {
         return this.handler;
      }
   }

   private static class WeakRef<TT, V extends Finalizable> extends WeakReference<TT> implements Reference<TT, V> {
      private final V handler;

      public WeakRef(TT referent, V handler, ReferenceQueue<? super TT> q) {
         super(referent, q);
         this.handler = handler;
      }

      public V getHandler() {
         return this.handler;
      }
   }

   private static class SoftRef<TT, V extends Finalizable> extends SoftReference<TT> implements Reference<TT, V> {
      private final V handler;

      public SoftRef(TT referent, V handler, ReferenceQueue<? super TT> q) {
         super(referent, q);
         this.handler = handler;
      }

      public V getHandler() {
         return this.handler;
      }
   }
}
