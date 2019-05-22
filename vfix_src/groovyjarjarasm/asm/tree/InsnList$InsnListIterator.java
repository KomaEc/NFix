package groovyjarjarasm.asm.tree;

import java.util.ListIterator;
import java.util.NoSuchElementException;

final class InsnList$InsnListIterator implements ListIterator {
   AbstractInsnNode next;
   AbstractInsnNode prev;
   // $FF: synthetic field
   private final InsnList this$0;

   private InsnList$InsnListIterator(InsnList var1, int var2) {
      this.this$0 = var1;
      if (var2 == var1.size()) {
         this.next = null;
         this.prev = var1.getLast();
      } else {
         this.next = var1.get(var2);
         this.prev = this.next.prev;
      }

   }

   public boolean hasNext() {
      return this.next != null;
   }

   public Object next() {
      if (this.next == null) {
         throw new NoSuchElementException();
      } else {
         AbstractInsnNode var1 = this.next;
         this.prev = var1;
         this.next = var1.next;
         return var1;
      }
   }

   public void remove() {
      this.this$0.remove(this.prev);
      this.prev = this.prev.prev;
   }

   public boolean hasPrevious() {
      return this.prev != null;
   }

   public Object previous() {
      AbstractInsnNode var1 = this.prev;
      this.next = var1;
      this.prev = var1.prev;
      return var1;
   }

   public int nextIndex() {
      if (this.next == null) {
         return this.this$0.size();
      } else {
         if (InsnList.access$100(this.this$0) == null) {
            InsnList.access$102(this.this$0, this.this$0.toArray());
         }

         return this.next.index;
      }
   }

   public int previousIndex() {
      if (this.prev == null) {
         return -1;
      } else {
         if (InsnList.access$100(this.this$0) == null) {
            InsnList.access$102(this.this$0, this.this$0.toArray());
         }

         return this.prev.index;
      }
   }

   public void add(Object var1) {
      this.this$0.insertBefore(this.next, (AbstractInsnNode)var1);
      this.prev = (AbstractInsnNode)var1;
   }

   public void set(Object var1) {
      this.this$0.set(this.next.prev, (AbstractInsnNode)var1);
      this.prev = (AbstractInsnNode)var1;
   }

   // $FF: synthetic method
   InsnList$InsnListIterator(InsnList var1, int var2, InsnList$1 var3) {
      this(var1, var2);
   }
}
