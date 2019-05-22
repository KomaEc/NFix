package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections.BoundedMap;

public class LRUMap extends AbstractLinkedMap implements BoundedMap, Serializable, Cloneable {
   private static final long serialVersionUID = -612114643488955218L;
   protected static final int DEFAULT_MAX_SIZE = 100;
   private transient int maxSize;
   private boolean scanUntilRemovable;

   public LRUMap() {
      this(100, 0.75F, false);
   }

   public LRUMap(int maxSize) {
      this(maxSize, 0.75F);
   }

   public LRUMap(int maxSize, boolean scanUntilRemovable) {
      this(maxSize, 0.75F, scanUntilRemovable);
   }

   public LRUMap(int maxSize, float loadFactor) {
      this(maxSize, loadFactor, false);
   }

   public LRUMap(int maxSize, float loadFactor, boolean scanUntilRemovable) {
      super(maxSize < 1 ? 16 : maxSize, loadFactor);
      if (maxSize < 1) {
         throw new IllegalArgumentException("LRUMap max size must be greater than 0");
      } else {
         this.maxSize = maxSize;
         this.scanUntilRemovable = scanUntilRemovable;
      }
   }

   public LRUMap(Map map) {
      this(map, false);
   }

   public LRUMap(Map map, boolean scanUntilRemovable) {
      this(map.size(), 0.75F, scanUntilRemovable);
      this.putAll(map);
   }

   public Object get(Object key) {
      AbstractLinkedMap.LinkEntry entry = (AbstractLinkedMap.LinkEntry)this.getEntry(key);
      if (entry == null) {
         return null;
      } else {
         this.moveToMRU(entry);
         return entry.getValue();
      }
   }

   protected void moveToMRU(AbstractLinkedMap.LinkEntry entry) {
      if (entry.after != super.header) {
         ++super.modCount;
         entry.before.after = entry.after;
         entry.after.before = entry.before;
         entry.after = super.header;
         entry.before = super.header.before;
         super.header.before.after = entry;
         super.header.before = entry;
      } else if (entry == super.header) {
         throw new IllegalStateException("Can't move header to MRU (please report this to commons-dev@jakarta.apache.org)");
      }

   }

   protected void updateEntry(AbstractHashedMap.HashEntry entry, Object newValue) {
      this.moveToMRU((AbstractLinkedMap.LinkEntry)entry);
      entry.setValue(newValue);
   }

   protected void addMapping(int hashIndex, int hashCode, Object key, Object value) {
      if (this.isFull()) {
         AbstractLinkedMap.LinkEntry reuse = super.header.after;
         boolean removeLRUEntry = false;
         if (!this.scanUntilRemovable) {
            removeLRUEntry = this.removeLRU(reuse);
         } else {
            while(reuse != super.header && reuse != null) {
               if (this.removeLRU(reuse)) {
                  removeLRUEntry = true;
                  break;
               }

               reuse = reuse.after;
            }

            if (reuse == null) {
               throw new IllegalStateException("Entry.after=null, header.after" + super.header.after + " header.before" + super.header.before + " key=" + key + " value=" + value + " size=" + super.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to commons-dev@jakarta.apache.org as a bug.");
            }
         }

         if (removeLRUEntry) {
            if (reuse == null) {
               throw new IllegalStateException("reuse=null, header.after=" + super.header.after + " header.before" + super.header.before + " key=" + key + " value=" + value + " size=" + super.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to commons-dev@jakarta.apache.org as a bug.");
            }

            this.reuseMapping(reuse, hashIndex, hashCode, key, value);
         } else {
            super.addMapping(hashIndex, hashCode, key, value);
         }
      } else {
         super.addMapping(hashIndex, hashCode, key, value);
      }

   }

   protected void reuseMapping(AbstractLinkedMap.LinkEntry entry, int hashIndex, int hashCode, Object key, Object value) {
      try {
         int removeIndex = this.hashIndex(entry.hashCode, super.data.length);
         AbstractHashedMap.HashEntry[] tmp = super.data;
         AbstractHashedMap.HashEntry loop = tmp[removeIndex];

         AbstractHashedMap.HashEntry previous;
         for(previous = null; loop != entry && loop != null; loop = loop.next) {
            previous = loop;
         }

         if (loop == null) {
            throw new IllegalStateException("Entry.next=null, data[removeIndex]=" + super.data[removeIndex] + " previous=" + previous + " key=" + key + " value=" + value + " size=" + super.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to commons-dev@jakarta.apache.org as a bug.");
         } else {
            ++super.modCount;
            this.removeEntry(entry, removeIndex, previous);
            this.reuseEntry(entry, hashIndex, hashCode, key, value);
            this.addEntry(entry, hashIndex);
         }
      } catch (NullPointerException var10) {
         throw new IllegalStateException("NPE, entry=" + entry + " entryIsHeader=" + (entry == super.header) + " key=" + key + " value=" + value + " size=" + super.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to commons-dev@jakarta.apache.org as a bug.");
      }
   }

   protected boolean removeLRU(AbstractLinkedMap.LinkEntry entry) {
      return true;
   }

   public boolean isFull() {
      return super.size >= this.maxSize;
   }

   public int maxSize() {
      return this.maxSize;
   }

   public boolean isScanUntilRemovable() {
      return this.scanUntilRemovable;
   }

   public Object clone() {
      return super.clone();
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      this.doWriteObject(out);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.doReadObject(in);
   }

   protected void doWriteObject(ObjectOutputStream out) throws IOException {
      out.writeInt(this.maxSize);
      super.doWriteObject(out);
   }

   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.maxSize = in.readInt();
      super.doReadObject(in);
   }
}
