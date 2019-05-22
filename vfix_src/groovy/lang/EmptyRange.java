package groovy.lang;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.codehaus.groovy.runtime.InvokerHelper;

public class EmptyRange extends AbstractList implements Range {
   protected Comparable at;

   public EmptyRange(Comparable at) {
      this.at = at;
   }

   public Comparable getFrom() {
      return this.at;
   }

   public Comparable getTo() {
      return this.at;
   }

   public boolean isReverse() {
      return false;
   }

   public boolean containsWithinBounds(Object o) {
      return false;
   }

   public String inspect() {
      return InvokerHelper.inspect(this.at) + "..<" + InvokerHelper.inspect(this.at);
   }

   public String toString() {
      return null == this.at ? "null..<null" : this.at.toString() + "..<" + this.at.toString();
   }

   public int size() {
      return 0;
   }

   public Object get(int index) {
      throw new IndexOutOfBoundsException("can't get values from Empty Ranges");
   }

   public boolean add(Object o) {
      throw new UnsupportedOperationException("cannot add to Empty Ranges");
   }

   public boolean addAll(int index, Collection c) {
      throw new UnsupportedOperationException("cannot add to Empty Ranges");
   }

   public boolean addAll(Collection c) {
      throw new UnsupportedOperationException("cannot add to Empty Ranges");
   }

   public boolean remove(Object o) {
      throw new UnsupportedOperationException("cannot remove from Empty Ranges");
   }

   public Object remove(int index) {
      throw new UnsupportedOperationException("cannot remove from Empty Ranges");
   }

   public boolean removeAll(Collection c) {
      throw new UnsupportedOperationException("cannot remove from Empty Ranges");
   }

   public boolean retainAll(Collection c) {
      throw new UnsupportedOperationException("cannot retainAll in Empty Ranges");
   }

   public Object set(int index, Object element) {
      throw new UnsupportedOperationException("cannot set in Empty Ranges");
   }

   public void step(int step, Closure closure) {
   }

   public List step(int step) {
      return new ArrayList();
   }
}
