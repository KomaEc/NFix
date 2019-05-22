package soot.jimple.toolkits.typing.fast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class QueuedSet<E> {
   private Set<E> hs;
   private LinkedList<E> ll;

   public QueuedSet() {
      this.hs = new HashSet();
      this.ll = new LinkedList();
   }

   public QueuedSet(List<E> os) {
      this();
      Iterator var2 = os.iterator();

      while(var2.hasNext()) {
         E o = var2.next();
         this.ll.addLast(o);
         this.hs.add(o);
      }

   }

   public QueuedSet(QueuedSet<E> qs) {
      this((List)qs.ll);
   }

   public boolean isEmpty() {
      return this.ll.isEmpty();
   }

   public boolean addLast(E o) {
      boolean r = this.hs.contains(o);
      if (!r) {
         this.ll.addLast(o);
         this.hs.add(o);
      }

      return r;
   }

   public int addLast(List<E> os) {
      int r = 0;
      Iterator var3 = os.iterator();

      while(var3.hasNext()) {
         E o = var3.next();
         if (this.addLast(o)) {
            ++r;
         }
      }

      return r;
   }

   public E removeFirst() {
      E r = this.ll.removeFirst();
      this.hs.remove(r);
      return r;
   }
}
