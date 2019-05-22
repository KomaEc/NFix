package org.jboss.util.property;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import org.jboss.util.NullArgumentException;

public class PropertyGroup extends PropertyMap {
   private static final long serialVersionUID = -2557641199743063159L;
   protected final String basename;

   public PropertyGroup(String basename, Properties container) {
      super(container);
      if (basename == null) {
         throw new NullArgumentException("basename");
      } else {
         this.basename = basename;
      }
   }

   public final String getBaseName() {
      return this.basename;
   }

   private String makePropertyName(String suffix) {
      return this.basename + "." + suffix;
   }

   private String makePropertyName(Object suffix) {
      return this.makePropertyName(String.valueOf(suffix));
   }

   public boolean containsKey(Object name) {
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         return super.containsKey(this.makePropertyName(name));
      }
   }

   public Object put(Object name, Object value) {
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         return super.put(this.makePropertyName(name), value);
      }
   }

   public Object get(Object name) {
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         return super.get(this.makePropertyName(name));
      }
   }

   public Object remove(Object name) {
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         return super.remove(this.makePropertyName(name));
      }
   }

   public Set entrySet() {
      final Set superSet = super.entrySet(true);
      return new AbstractSet() {
         private boolean isInGroup(Entry entry) {
            String key = (String)entry.getKey();
            return key.startsWith(PropertyGroup.this.basename);
         }

         public int size() {
            Iterator iter = superSet.iterator();
            int count = 0;

            while(iter.hasNext()) {
               Entry entry = (Entry)iter.next();
               if (this.isInGroup(entry)) {
                  ++count;
               }
            }

            return count;
         }

         public Iterator iterator() {
            return new Iterator() {
               private Iterator iter = superSet.iterator();
               private Object next;

               public boolean hasNext() {
                  if (this.next != null) {
                     return true;
                  } else {
                     while(this.next == null && this.iter.hasNext()) {
                        Entry entry = (Entry)this.iter.next();
                        if (isInGroup(entry)) {
                           this.next = entry;
                           return true;
                        }
                     }

                     return false;
                  }
               }

               public Object next() {
                  if (this.next == null) {
                     throw new NoSuchElementException();
                  } else {
                     Object obj = this.next;
                     this.next = null;
                     return obj;
                  }
               }

               public void remove() {
                  this.iter.remove();
               }
            };
         }
      };
   }

   protected void addPropertyListener(BoundPropertyListener listener) {
      String name = this.makePropertyName(listener.getPropertyName());
      List list = (List)this.boundListeners.get(name);
      if (list == null) {
         list = new ArrayList();
         this.boundListeners.put(name, list);
      }

      if (!((List)list).contains(listener)) {
         ((List)list).add(listener);
         listener.propertyBound(this);
      }

   }

   protected boolean removePropertyListener(BoundPropertyListener listener) {
      String name = this.makePropertyName(listener.getPropertyName());
      List list = (List)this.boundListeners.get(name);
      boolean removed = false;
      if (list != null) {
         removed = list.remove(listener);
         if (removed) {
            listener.propertyUnbound(this);
         }
      }

      return removed;
   }
}
