package org.codehaus.groovy.util;

public abstract class LazyReference<T> extends LockableObject {
   private static final ManagedReference INIT;
   private static final ManagedReference NULL_REFERENCE;
   private ManagedReference<T> reference;
   private final ReferenceBundle bundle;

   public LazyReference(ReferenceBundle bundle) {
      this.reference = INIT;
      this.bundle = bundle;
   }

   public T get() {
      ManagedReference<T> resRef = this.reference;
      if (resRef == INIT) {
         return this.getLocked(false);
      } else if (resRef == NULL_REFERENCE) {
         return null;
      } else {
         T res = resRef.get();
         return res == null ? this.getLocked(true) : res;
      }
   }

   private T getLocked(boolean force) {
      this.lock();

      Object var4;
      try {
         ManagedReference<T> resRef = this.reference;
         Object res;
         if (!force && resRef != INIT) {
            res = resRef.get();
            return res;
         }

         res = this.initValue();
         if (res == null) {
            this.reference = NULL_REFERENCE;
         } else {
            this.reference = new ManagedReference(this.bundle, res);
         }

         var4 = res;
      } finally {
         this.unlock();
      }

      return var4;
   }

   public void clear() {
      this.reference = INIT;
   }

   public abstract T initValue();

   public String toString() {
      T res = this.reference.get();
      return res == null ? "<null>" : res.toString();
   }

   static {
      INIT = new ManagedReference(ReferenceType.HARD, (ReferenceManager)null, (Object)null) {
      };
      NULL_REFERENCE = new ManagedReference(ReferenceType.HARD, (ReferenceManager)null, (Object)null) {
      };
   }
}
