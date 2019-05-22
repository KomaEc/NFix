package com.gzoltar.shaded.org.pitest.functional;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public abstract class Option<T> implements FunctionalIterable<T> {
   private static final Option.None NONE = new Option.None();

   private Option() {
   }

   public abstract T value();

   public abstract T getOrElse(T var1);

   public abstract boolean hasSome();

   public boolean contains(F<T, Boolean> predicate) {
      return FCollection.contains(this, predicate);
   }

   public FunctionalList<T> filter(F<T, Boolean> predicate) {
      return FCollection.filter(this, predicate);
   }

   public <B> FunctionalList<B> flatMap(F<T, ? extends Iterable<B>> f) {
      return FCollection.flatMap(this, f);
   }

   public void forEach(SideEffect1<T> e) {
      FCollection.forEach(this, e);
   }

   public <B> FunctionalList<B> map(F<T, B> f) {
      return FCollection.map(this, f);
   }

   public <B> void mapTo(F<T, B> f, Collection<? super B> bs) {
      FCollection.mapTo(this, f, bs);
   }

   public static <T> Option<T> some(T value) {
      return (Option)(value == null ? NONE : new Option.Some(value));
   }

   public static <T> Option.None<T> none() {
      return NONE;
   }

   public boolean hasNone() {
      return !this.hasSome();
   }

   // $FF: synthetic method
   Option(Object x0) {
      this();
   }

   public static final class Some<T> extends Option<T> {
      private final T value;

      private Some(T value) {
         super(null);
         this.value = value;
      }

      public T value() {
         return this.value;
      }

      public Iterator<T> iterator() {
         return Collections.singleton(this.value).iterator();
      }

      public T getOrElse(T defaultValue) {
         return this.value;
      }

      public boolean hasSome() {
         return true;
      }

      public int hashCode() {
         int prime = true;
         int result = 1;
         int result = 31 * result + (this.value == null ? 0 : this.value.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            Option.Some other = (Option.Some)obj;
            if (this.value == null) {
               if (other.value != null) {
                  return false;
               }
            } else if (!this.value.equals(other.value)) {
               return false;
            }

            return true;
         }
      }

      public String toString() {
         return "Some(" + this.value + ")";
      }

      // $FF: synthetic method
      Some(Object x0, Object x1) {
         this(x0);
      }
   }

   public static final class None<T> extends Option<T> {
      private None() {
         super(null);
      }

      public Iterator<T> iterator() {
         return Collections.emptySet().iterator();
      }

      public T value() {
         throw new UnsupportedOperationException("Tried to retrieve value but had None.");
      }

      public T getOrElse(T defaultValue) {
         return defaultValue;
      }

      public boolean hasSome() {
         return false;
      }

      // $FF: synthetic method
      None(Object x0) {
         this();
      }
   }
}
