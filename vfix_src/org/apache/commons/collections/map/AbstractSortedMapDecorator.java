package org.apache.commons.collections.map;

import java.util.Comparator;
import java.util.SortedMap;

public abstract class AbstractSortedMapDecorator extends AbstractMapDecorator implements SortedMap {
   protected AbstractSortedMapDecorator() {
   }

   public AbstractSortedMapDecorator(SortedMap map) {
      super(map);
   }

   protected SortedMap getSortedMap() {
      return (SortedMap)super.map;
   }

   public Comparator comparator() {
      return this.getSortedMap().comparator();
   }

   public Object firstKey() {
      return this.getSortedMap().firstKey();
   }

   public SortedMap headMap(Object toKey) {
      return this.getSortedMap().headMap(toKey);
   }

   public Object lastKey() {
      return this.getSortedMap().lastKey();
   }

   public SortedMap subMap(Object fromKey, Object toKey) {
      return this.getSortedMap().subMap(fromKey, toKey);
   }

   public SortedMap tailMap(Object fromKey) {
      return this.getSortedMap().tailMap(fromKey);
   }
}
