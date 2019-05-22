package soot.jimple.spark.ondemand.genericutil;

import java.util.Iterator;

public class IteratorMapper<T, U> implements Iterator<U> {
   private final Mapper<T, U> mapper;
   private final Iterator<T> delegate;

   public IteratorMapper(Mapper<T, U> mapper, Iterator<T> delegate) {
      this.mapper = mapper;
      this.delegate = delegate;
   }

   public boolean hasNext() {
      return this.delegate.hasNext();
   }

   public U next() {
      return this.mapper.map(this.delegate.next());
   }

   public void remove() {
      this.delegate.remove();
   }
}
