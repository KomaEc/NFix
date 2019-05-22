package edu.emory.mathcs.backport.java.util.concurrent.atomic;

public class AtomicStampedReference {
   private final AtomicReference atomicRef;

   public AtomicStampedReference(Object initialRef, int initialStamp) {
      this.atomicRef = new AtomicReference(new AtomicStampedReference.ReferenceIntegerPair(initialRef, initialStamp));
   }

   public Object getReference() {
      return this.getPair().reference;
   }

   public int getStamp() {
      return this.getPair().integer;
   }

   public Object get(int[] stampHolder) {
      AtomicStampedReference.ReferenceIntegerPair p = this.getPair();
      stampHolder[0] = p.integer;
      return p.reference;
   }

   public boolean weakCompareAndSet(Object expectedReference, Object newReference, int expectedStamp, int newStamp) {
      AtomicStampedReference.ReferenceIntegerPair current = this.getPair();
      return expectedReference == current.reference && expectedStamp == current.integer && (newReference == current.reference && newStamp == current.integer || this.atomicRef.weakCompareAndSet(current, new AtomicStampedReference.ReferenceIntegerPair(newReference, newStamp)));
   }

   public boolean compareAndSet(Object expectedReference, Object newReference, int expectedStamp, int newStamp) {
      AtomicStampedReference.ReferenceIntegerPair current = this.getPair();
      return expectedReference == current.reference && expectedStamp == current.integer && (newReference == current.reference && newStamp == current.integer || this.atomicRef.compareAndSet(current, new AtomicStampedReference.ReferenceIntegerPair(newReference, newStamp)));
   }

   public void set(Object newReference, int newStamp) {
      AtomicStampedReference.ReferenceIntegerPair current = this.getPair();
      if (newReference != current.reference || newStamp != current.integer) {
         this.atomicRef.set(new AtomicStampedReference.ReferenceIntegerPair(newReference, newStamp));
      }

   }

   public boolean attemptStamp(Object expectedReference, int newStamp) {
      AtomicStampedReference.ReferenceIntegerPair current = this.getPair();
      return expectedReference == current.reference && (newStamp == current.integer || this.atomicRef.compareAndSet(current, new AtomicStampedReference.ReferenceIntegerPair(expectedReference, newStamp)));
   }

   private AtomicStampedReference.ReferenceIntegerPair getPair() {
      return (AtomicStampedReference.ReferenceIntegerPair)this.atomicRef.get();
   }

   private static class ReferenceIntegerPair {
      private final Object reference;
      private final int integer;

      ReferenceIntegerPair(Object r, int i) {
         this.reference = r;
         this.integer = i;
      }
   }
}
