package org.jboss.util;

import java.util.Comparator;

public class Heap {
   private Comparator m_comparator;
   private int m_count;
   private Object[] m_nodes;

   public Heap() {
      this((Comparator)null);
   }

   public Heap(Comparator comparator) {
      this.m_comparator = comparator;
      this.clear();
   }

   public void insert(Object obj) {
      int length = this.m_nodes.length;
      if (this.m_count == length) {
         Object[] newNodes = new Object[length + length];
         System.arraycopy(this.m_nodes, 0, newNodes, 0, length);
         this.m_nodes = newNodes;
      }

      int par;
      int k;
      for(k = this.m_count; k > 0; k = par) {
         par = this.parent(k);
         if (this.compare(obj, this.m_nodes[par]) >= 0) {
            break;
         }

         this.m_nodes[k] = this.m_nodes[par];
      }

      this.m_nodes[k] = obj;
      ++this.m_count;
   }

   public Object extract() {
      if (this.m_count < 1) {
         return null;
      } else {
         int length = this.m_nodes.length >> 1;
         if (length > 5 && this.m_count < length >> 1) {
            Object[] newNodes = new Object[length];
            System.arraycopy(this.m_nodes, 0, newNodes, 0, length);
            this.m_nodes = newNodes;
         }

         int k = 0;
         Object ret = this.m_nodes[k];
         --this.m_count;
         Object last = this.m_nodes[this.m_count];

         while(true) {
            int l = this.left(k);
            if (l >= this.m_count) {
               break;
            }

            int r = this.right(k);
            int child = r < this.m_count && this.compare(this.m_nodes[l], this.m_nodes[r]) >= 0 ? r : l;
            if (this.compare(last, this.m_nodes[child]) <= 0) {
               break;
            }

            this.m_nodes[k] = this.m_nodes[child];
            k = child;
         }

         this.m_nodes[k] = last;
         this.m_nodes[this.m_count] = null;
         return ret;
      }
   }

   public Object peek() {
      return this.m_count < 1 ? null : this.m_nodes[0];
   }

   public void clear() {
      this.m_count = 0;
      this.m_nodes = new Object[10];
   }

   protected int compare(Object o1, Object o2) {
      if (this.m_comparator != null) {
         return this.m_comparator.compare(o1, o2);
      } else if (o1 == null) {
         return o2 == null ? 0 : -((Comparable)o2).compareTo(o1);
      } else {
         return ((Comparable)o1).compareTo(o2);
      }
   }

   protected int parent(int index) {
      return index - 1 >> 1;
   }

   protected int left(int index) {
      return index + index + 1;
   }

   protected int right(int index) {
      return index + index + 2;
   }
}
