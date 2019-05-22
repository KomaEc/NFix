package org.apache.commons.collections.set;

import java.util.Comparator;
import java.util.SortedSet;
import org.apache.commons.collections.Predicate;

public class PredicatedSortedSet extends PredicatedSet implements SortedSet {
   private static final long serialVersionUID = -9110948148132275052L;

   public static SortedSet decorate(SortedSet set, Predicate predicate) {
      return new PredicatedSortedSet(set, predicate);
   }

   protected PredicatedSortedSet(SortedSet set, Predicate predicate) {
      super(set, predicate);
   }

   private SortedSet getSortedSet() {
      return (SortedSet)this.getCollection();
   }

   public SortedSet subSet(Object fromElement, Object toElement) {
      SortedSet sub = this.getSortedSet().subSet(fromElement, toElement);
      return new PredicatedSortedSet(sub, super.predicate);
   }

   public SortedSet headSet(Object toElement) {
      SortedSet sub = this.getSortedSet().headSet(toElement);
      return new PredicatedSortedSet(sub, super.predicate);
   }

   public SortedSet tailSet(Object fromElement) {
      SortedSet sub = this.getSortedSet().tailSet(fromElement);
      return new PredicatedSortedSet(sub, super.predicate);
   }

   public Object first() {
      return this.getSortedSet().first();
   }

   public Object last() {
      return this.getSortedSet().last();
   }

   public Comparator comparator() {
      return this.getSortedSet().comparator();
   }
}
