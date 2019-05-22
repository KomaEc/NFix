package soot.jimple.spark.sets;

import soot.Type;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.util.BitVector;

public class SharedListSet extends PointsToSetInternal {
   private PAG pag;
   private SharedListSet.ListNode data = null;

   public SharedListSet(Type type, PAG pag) {
      super(type);
      this.pag = pag;
   }

   public static final P2SetFactory getFactory() {
      return new P2SetFactory() {
         public final PointsToSetInternal newSet(Type type, PAG pag) {
            return new SharedListSet(type, pag);
         }
      };
   }

   public boolean contains(Node n) {
      for(SharedListSet.ListNode i = this.data; i != null; i = i.next) {
         if (i.elem == n) {
            return true;
         }
      }

      return false;
   }

   public boolean isEmpty() {
      return this.data == null;
   }

   public boolean forall(P2SetVisitor v) {
      for(SharedListSet.ListNode i = this.data; i != null; i = i.next) {
         v.visit(i.elem);
      }

      return v.getReturnValue();
   }

   private SharedListSet.ListNode advanceExclude(SharedListSet.ListNode exclude, SharedListSet.ListNode other) {
      for(int otherNum = other.elem.getNumber(); exclude != null && exclude.elem.getNumber() < otherNum; exclude = exclude.next) {
      }

      return exclude;
   }

   private boolean excluded(SharedListSet.ListNode exclude, SharedListSet.ListNode other, BitVector mask) {
      return exclude != null && other.elem == exclude.elem || mask != null && !mask.get(other.elem.getNumber());
   }

   private SharedListSet.ListNode union(SharedListSet.ListNode first, SharedListSet.ListNode other, SharedListSet.ListNode exclude, BitVector mask, boolean detachChildren) {
      if (first == null) {
         if (other == null) {
            return null;
         } else if (exclude == null && mask == null) {
            return this.makeNode(other.elem, other.next);
         } else {
            exclude = this.advanceExclude(exclude, other);
            return this.excluded(exclude, other, mask) ? this.union(first, other.next, exclude, mask, detachChildren) : this.makeNode(other.elem, this.union(first, other.next, exclude, mask, detachChildren));
         }
      } else if (other == null) {
         return first;
      } else if (first == other) {
         return first;
      } else {
         SharedListSet.ListNode retVal;
         if (first.elem.getNumber() > other.elem.getNumber()) {
            exclude = this.advanceExclude(exclude, other);
            if (this.excluded(exclude, other, mask)) {
               retVal = this.union(first, other.next, exclude, mask, detachChildren);
            } else {
               retVal = this.makeNode(other.elem, this.union(first, other.next, exclude, mask, detachChildren));
            }
         } else {
            if (first.refCount > 1L) {
               detachChildren = false;
            }

            if (first.elem == other.elem) {
               other = other.next;
            }

            retVal = this.makeNode(first.elem, this.union(first.next, other, exclude, mask, detachChildren));
            if (detachChildren && first != retVal && first.next != null) {
               first.next.decRefCount();
            }
         }

         return retVal;
      }
   }

   private boolean addOrAddAll(SharedListSet.ListNode first, SharedListSet.ListNode other, SharedListSet.ListNode exclude, BitVector mask) {
      SharedListSet.ListNode result = this.union(first, other, exclude, mask, true);
      if (result == this.data) {
         return false;
      } else {
         result.incRefCount();
         if (this.data != null) {
            this.data.decRefCount();
         }

         this.data = result;
         return true;
      }
   }

   public boolean add(Node n) {
      SharedListSet.ListNode other = this.makeNode(n, (SharedListSet.ListNode)null);
      other.incRefCount();
      boolean added = this.addOrAddAll(this.data, other, (SharedListSet.ListNode)null, (BitVector)null);
      other.decRefCount();
      return added;
   }

   public boolean addAll(PointsToSetInternal other, PointsToSetInternal exclude) {
      if (other == null) {
         return false;
      } else if (other instanceof SharedListSet && (exclude == null || exclude instanceof SharedListSet)) {
         SharedListSet realOther = (SharedListSet)other;
         SharedListSet realExclude = (SharedListSet)exclude;
         BitVector mask = this.getBitMask(realOther, this.pag);
         SharedListSet.ListNode excludeData = realExclude == null ? null : realExclude.data;
         return this.addOrAddAll(this.data, realOther.data, excludeData, mask);
      } else {
         return super.addAll(other, exclude);
      }
   }

   private SharedListSet.ListNode makeNode(Node elem, SharedListSet.ListNode next) {
      SharedListSet.Pair p = new SharedListSet.Pair(elem, next);
      SharedListSet.ListNode retVal = (SharedListSet.ListNode)AllSharedListNodes.v().allNodes.get(p);
      if (retVal == null) {
         retVal = new SharedListSet.ListNode(elem, next);
         if (next != null) {
            next.incRefCount();
         }

         AllSharedListNodes.v().allNodes.put(p, retVal);
      }

      return retVal;
   }

   public class ListNode {
      private Node elem;
      private SharedListSet.ListNode next = null;
      public long refCount;

      public ListNode(Node elem, SharedListSet.ListNode next) {
         this.elem = elem;
         this.next = next;
         this.refCount = 0L;
      }

      public void incRefCount() {
         ++this.refCount;
      }

      public void decRefCount() {
         if (--this.refCount == 0L) {
            AllSharedListNodes.v().allNodes.remove(SharedListSet.this.new Pair(this.elem, this.next));
         }

      }
   }

   public class Pair {
      public Node first;
      public SharedListSet.ListNode second;

      public Pair(Node first, SharedListSet.ListNode second) {
         this.first = first;
         this.second = second;
      }

      public int hashCode() {
         return this.second == null ? this.first.hashCode() : this.first.hashCode() + this.second.hashCode();
      }

      public boolean equals(Object other) {
         if (!(other instanceof SharedListSet.Pair)) {
            return false;
         } else {
            SharedListSet.Pair o = (SharedListSet.Pair)other;
            return (this.first == null && o.first == null || this.first == o.first) && (this.second == null && o.second == null || this.second == o.second);
         }
      }
   }
}
