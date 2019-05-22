package org.apache.commons.collections.map;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.iterators.AbstractIteratorDecorator;
import org.apache.commons.collections.keyvalue.AbstractMapEntryDecorator;
import org.apache.commons.collections.set.AbstractSetDecorator;

abstract class AbstractInputCheckedMapDecorator extends AbstractMapDecorator {
   protected AbstractInputCheckedMapDecorator() {
   }

   protected AbstractInputCheckedMapDecorator(Map map) {
      super(map);
   }

   protected abstract Object checkSetValue(Object var1);

   protected boolean isSetValueChecking() {
      return true;
   }

   public Set entrySet() {
      return (Set)(this.isSetValueChecking() ? new AbstractInputCheckedMapDecorator.EntrySet(super.map.entrySet(), this) : super.map.entrySet());
   }

   static class MapEntry extends AbstractMapEntryDecorator {
      private final AbstractInputCheckedMapDecorator parent;

      protected MapEntry(Entry entry, AbstractInputCheckedMapDecorator parent) {
         super(entry);
         this.parent = parent;
      }

      public Object setValue(Object value) {
         value = this.parent.checkSetValue(value);
         return super.entry.setValue(value);
      }
   }

   static class EntrySetIterator extends AbstractIteratorDecorator {
      private final AbstractInputCheckedMapDecorator parent;

      protected EntrySetIterator(Iterator iterator, AbstractInputCheckedMapDecorator parent) {
         super(iterator);
         this.parent = parent;
      }

      public Object next() {
         Entry entry = (Entry)super.iterator.next();
         return new AbstractInputCheckedMapDecorator.MapEntry(entry, this.parent);
      }
   }

   static class EntrySet extends AbstractSetDecorator {
      private final AbstractInputCheckedMapDecorator parent;

      protected EntrySet(Set set, AbstractInputCheckedMapDecorator parent) {
         super(set);
         this.parent = parent;
      }

      public Iterator iterator() {
         return new AbstractInputCheckedMapDecorator.EntrySetIterator(super.collection.iterator(), this.parent);
      }

      public Object[] toArray() {
         Object[] array = super.collection.toArray();

         for(int i = 0; i < array.length; ++i) {
            array[i] = new AbstractInputCheckedMapDecorator.MapEntry((Entry)array[i], this.parent);
         }

         return array;
      }

      public Object[] toArray(Object[] array) {
         Object[] result = array;
         if (array.length > 0) {
            result = (Object[])Array.newInstance(array.getClass().getComponentType(), 0);
         }

         result = super.collection.toArray(result);

         for(int i = 0; i < result.length; ++i) {
            result[i] = new AbstractInputCheckedMapDecorator.MapEntry((Entry)result[i], this.parent);
         }

         if (result.length > array.length) {
            return result;
         } else {
            System.arraycopy(result, 0, array, 0, result.length);
            if (array.length > result.length) {
               array[result.length] = null;
            }

            return array;
         }
      }
   }
}
