package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;

public class Interners$InternerBuilder {
   private final MapMaker mapMaker;
   private boolean strong;

   private Interners$InternerBuilder() {
      this.mapMaker = new MapMaker();
      this.strong = true;
   }

   public Interners$InternerBuilder strong() {
      this.strong = true;
      return this;
   }

   @GwtIncompatible("java.lang.ref.WeakReference")
   public Interners$InternerBuilder weak() {
      this.strong = false;
      return this;
   }

   public Interners$InternerBuilder concurrencyLevel(int concurrencyLevel) {
      this.mapMaker.concurrencyLevel(concurrencyLevel);
      return this;
   }

   public <E> Interner<E> build() {
      if (!this.strong) {
         this.mapMaker.weakKeys();
      }

      return new Interners$InternerImpl(this.mapMaker);
   }

   // $FF: synthetic method
   Interners$InternerBuilder(Object x0) {
      this();
   }
}
