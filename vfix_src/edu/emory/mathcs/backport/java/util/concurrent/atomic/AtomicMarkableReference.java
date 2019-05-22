package edu.emory.mathcs.backport.java.util.concurrent.atomic;

public class AtomicMarkableReference {
   private final AtomicReference atomicRef;

   public AtomicMarkableReference(Object initialRef, boolean initialMark) {
      this.atomicRef = new AtomicReference(new AtomicMarkableReference.ReferenceBooleanPair(initialRef, initialMark));
   }

   private AtomicMarkableReference.ReferenceBooleanPair getPair() {
      return (AtomicMarkableReference.ReferenceBooleanPair)this.atomicRef.get();
   }

   public Object getReference() {
      return this.getPair().reference;
   }

   public boolean isMarked() {
      return this.getPair().bit;
   }

   public Object get(boolean[] markHolder) {
      AtomicMarkableReference.ReferenceBooleanPair p = this.getPair();
      markHolder[0] = p.bit;
      return p.reference;
   }

   public boolean weakCompareAndSet(Object expectedReference, Object newReference, boolean expectedMark, boolean newMark) {
      AtomicMarkableReference.ReferenceBooleanPair current = this.getPair();
      return expectedReference == current.reference && expectedMark == current.bit && (newReference == current.reference && newMark == current.bit || this.atomicRef.weakCompareAndSet(current, new AtomicMarkableReference.ReferenceBooleanPair(newReference, newMark)));
   }

   public boolean compareAndSet(Object expectedReference, Object newReference, boolean expectedMark, boolean newMark) {
      AtomicMarkableReference.ReferenceBooleanPair current = this.getPair();
      return expectedReference == current.reference && expectedMark == current.bit && (newReference == current.reference && newMark == current.bit || this.atomicRef.compareAndSet(current, new AtomicMarkableReference.ReferenceBooleanPair(newReference, newMark)));
   }

   public void set(Object newReference, boolean newMark) {
      AtomicMarkableReference.ReferenceBooleanPair current = this.getPair();
      if (newReference != current.reference || newMark != current.bit) {
         this.atomicRef.set(new AtomicMarkableReference.ReferenceBooleanPair(newReference, newMark));
      }

   }

   public boolean attemptMark(Object expectedReference, boolean newMark) {
      AtomicMarkableReference.ReferenceBooleanPair current = this.getPair();
      return expectedReference == current.reference && (newMark == current.bit || this.atomicRef.compareAndSet(current, new AtomicMarkableReference.ReferenceBooleanPair(expectedReference, newMark)));
   }

   private static class ReferenceBooleanPair {
      private final Object reference;
      private final boolean bit;

      ReferenceBooleanPair(Object r, boolean i) {
         this.reference = r;
         this.bit = i;
      }
   }
}
