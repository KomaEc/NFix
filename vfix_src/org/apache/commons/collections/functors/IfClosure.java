package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;

public class IfClosure implements Closure, Serializable {
   private static final long serialVersionUID = 3518477308466486130L;
   private final Predicate iPredicate;
   private final Closure iTrueClosure;
   private final Closure iFalseClosure;

   public static Closure getInstance(Predicate predicate, Closure trueClosure) {
      return getInstance(predicate, trueClosure, NOPClosure.INSTANCE);
   }

   public static Closure getInstance(Predicate predicate, Closure trueClosure, Closure falseClosure) {
      if (predicate == null) {
         throw new IllegalArgumentException("Predicate must not be null");
      } else if (trueClosure != null && falseClosure != null) {
         return new IfClosure(predicate, trueClosure, falseClosure);
      } else {
         throw new IllegalArgumentException("Closures must not be null");
      }
   }

   public IfClosure(Predicate predicate, Closure trueClosure) {
      this(predicate, trueClosure, NOPClosure.INSTANCE);
   }

   public IfClosure(Predicate predicate, Closure trueClosure, Closure falseClosure) {
      this.iPredicate = predicate;
      this.iTrueClosure = trueClosure;
      this.iFalseClosure = falseClosure;
   }

   public void execute(Object input) {
      if (this.iPredicate.evaluate(input)) {
         this.iTrueClosure.execute(input);
      } else {
         this.iFalseClosure.execute(input);
      }

   }

   public Predicate getPredicate() {
      return this.iPredicate;
   }

   public Closure getTrueClosure() {
      return this.iTrueClosure;
   }

   public Closure getFalseClosure() {
      return this.iFalseClosure;
   }
}
