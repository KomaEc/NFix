package soot.coffi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

final class BBQ {
   private final ArrayList<BasicBlock> q = new ArrayList();

   public void push(BasicBlock b) {
      if (!b.inq) {
         b.inq = true;
         this.q.add(b);
      }

   }

   public BasicBlock pull() throws NoSuchElementException {
      if (this.q.size() == 0) {
         throw new NoSuchElementException("Pull from empty BBQ");
      } else {
         BasicBlock b = (BasicBlock)this.q.get(0);
         this.q.remove(0);
         b.inq = false;
         return b;
      }
   }

   public boolean contains(BasicBlock b) {
      return b.inq;
   }

   public int size() {
      return this.q.size();
   }

   public boolean isEmpty() {
      return this.q.isEmpty();
   }

   public void clear() {
      BasicBlock basicBlock;
      for(Iterator var2 = this.q.iterator(); var2.hasNext(); basicBlock.inq = false) {
         basicBlock = (BasicBlock)var2.next();
      }

      this.q.clear();
   }
}
