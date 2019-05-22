package org.apache.commons.collections.set;

import java.util.Comparator;
import java.util.SortedSet;
import org.apache.commons.collections.Transformer;

public class TransformedSortedSet extends TransformedSet implements SortedSet {
   private static final long serialVersionUID = -1675486811351124386L;

   public static SortedSet decorate(SortedSet set, Transformer transformer) {
      return new TransformedSortedSet(set, transformer);
   }

   protected TransformedSortedSet(SortedSet set, Transformer transformer) {
      super(set, transformer);
   }

   protected SortedSet getSortedSet() {
      return (SortedSet)super.collection;
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

   public SortedSet subSet(Object fromElement, Object toElement) {
      SortedSet set = this.getSortedSet().subSet(fromElement, toElement);
      return new TransformedSortedSet(set, super.transformer);
   }

   public SortedSet headSet(Object toElement) {
      SortedSet set = this.getSortedSet().headSet(toElement);
      return new TransformedSortedSet(set, super.transformer);
   }

   public SortedSet tailSet(Object fromElement) {
      SortedSet set = this.getSortedSet().tailSet(fromElement);
      return new TransformedSortedSet(set, super.transformer);
   }
}
