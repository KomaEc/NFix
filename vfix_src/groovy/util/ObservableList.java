package groovy.util;

import groovy.lang.Closure;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableList implements List {
   private List delegate;
   private PropertyChangeSupport pcs;
   private Closure test;

   public ObservableList() {
      this(new ArrayList(), (Closure)null);
   }

   public ObservableList(List delegate) {
      this(delegate, (Closure)null);
   }

   public ObservableList(Closure test) {
      this(new ArrayList(), test);
   }

   public ObservableList(List delegate, Closure test) {
      this.delegate = delegate;
      this.test = test;
      this.pcs = new PropertyChangeSupport(this);
   }

   public void add(int index, Object element) {
      this.delegate.add(index, element);
      if (this.test != null) {
         Object result = this.test.call(element);
         if (result != null && result instanceof Boolean && (Boolean)result) {
            this.pcs.firePropertyChange(new ObservableList.ElementAddedEvent(this, element, index));
         }
      } else {
         this.pcs.firePropertyChange(new ObservableList.ElementAddedEvent(this, element, index));
      }

   }

   public boolean add(Object o) {
      boolean success = this.delegate.add(o);
      if (success) {
         if (this.test != null) {
            Object result = this.test.call(o);
            if (result != null && result instanceof Boolean && (Boolean)result) {
               this.pcs.firePropertyChange(new ObservableList.ElementAddedEvent(this, o, this.size() - 1));
            }
         } else {
            this.pcs.firePropertyChange(new ObservableList.ElementAddedEvent(this, o, this.size() - 1));
         }
      }

      return success;
   }

   public boolean addAll(Collection c) {
      int index = this.size() - 1;
      index = index < 0 ? 0 : index;
      boolean success = this.delegate.addAll(c);
      if (success && c != null) {
         List values = new ArrayList();
         Iterator i = c.iterator();

         while(i.hasNext()) {
            Object element = i.next();
            if (this.test != null) {
               Object result = this.test.call(element);
               if (result != null && result instanceof Boolean && (Boolean)result) {
                  values.add(element);
               }
            } else {
               values.add(element);
            }
         }

         if (values.size() > 0) {
            this.pcs.firePropertyChange(new ObservableList.MultiElementAddedEvent(this, index, values));
         }
      }

      return success;
   }

   public boolean addAll(int index, Collection c) {
      boolean success = this.delegate.addAll(index, c);
      if (success && c != null) {
         List values = new ArrayList();
         Iterator i = c.iterator();

         while(i.hasNext()) {
            Object element = i.next();
            if (this.test != null) {
               Object result = this.test.call(element);
               if (result != null && result instanceof Boolean && (Boolean)result) {
                  values.add(element);
               }
            } else {
               values.add(element);
            }
         }

         if (values.size() > 0) {
            this.pcs.firePropertyChange(new ObservableList.MultiElementAddedEvent(this, index, values));
         }
      }

      return success;
   }

   public void clear() {
      List values = new ArrayList();
      values.addAll(this.delegate);
      this.delegate.clear();
      if (!values.isEmpty()) {
         this.pcs.firePropertyChange(new ObservableList.ElementClearedEvent(this, values));
      }

   }

   public boolean contains(Object o) {
      return this.delegate.contains(o);
   }

   public boolean containsAll(Collection c) {
      return this.delegate.containsAll(c);
   }

   public boolean equals(Object o) {
      return this.delegate.equals(o);
   }

   public Object get(int index) {
      return this.delegate.get(index);
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public int indexOf(Object o) {
      return this.delegate.indexOf(o);
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Iterator iterator() {
      return new ObservableList.ObservableIterator(this.delegate.iterator());
   }

   public int lastIndexOf(Object o) {
      return this.delegate.lastIndexOf(o);
   }

   public ListIterator listIterator() {
      return new ObservableList.ObservableListIterator(this.delegate.listIterator(), 0);
   }

   public ListIterator listIterator(int index) {
      return new ObservableList.ObservableListIterator(this.delegate.listIterator(index), index);
   }

   public Object remove(int index) {
      Object element = this.delegate.remove(index);
      this.pcs.firePropertyChange(new ObservableList.ElementRemovedEvent(this, element, index));
      return element;
   }

   public boolean remove(Object o) {
      int index = this.delegate.indexOf(o);
      boolean success = this.delegate.remove(o);
      if (success) {
         this.pcs.firePropertyChange(new ObservableList.ElementRemovedEvent(this, o, index));
      }

      return success;
   }

   public boolean removeAll(Collection c) {
      if (c == null) {
         return false;
      } else {
         List values = new ArrayList();
         if (c != null) {
            Iterator i = c.iterator();

            while(i.hasNext()) {
               Object element = i.next();
               if (this.delegate.contains(element)) {
                  values.add(element);
               }
            }
         }

         boolean success = this.delegate.removeAll(c);
         if (success && !values.isEmpty()) {
            this.pcs.firePropertyChange(new ObservableList.MultiElementRemovedEvent(this, values));
         }

         return success;
      }
   }

   public boolean retainAll(Collection c) {
      if (c == null) {
         return false;
      } else {
         List values = new ArrayList();
         if (c != null) {
            Iterator i = this.delegate.iterator();

            while(i.hasNext()) {
               Object element = i.next();
               if (!c.contains(element)) {
                  values.add(element);
               }
            }
         }

         boolean success = this.delegate.retainAll(c);
         if (success && !values.isEmpty()) {
            this.pcs.firePropertyChange(new ObservableList.MultiElementRemovedEvent(this, values));
         }

         return success;
      }
   }

   public Object set(int index, Object element) {
      Object oldValue = this.delegate.set(index, element);
      if (this.test != null) {
         Object result = this.test.call(element);
         if (result != null && result instanceof Boolean && (Boolean)result) {
            this.pcs.firePropertyChange(new ObservableList.ElementUpdatedEvent(this, oldValue, element, index));
         }
      } else {
         this.pcs.firePropertyChange(new ObservableList.ElementUpdatedEvent(this, oldValue, element, index));
      }

      return oldValue;
   }

   public int size() {
      return this.delegate.size();
   }

   public List subList(int fromIndex, int toIndex) {
      return this.delegate.subList(fromIndex, toIndex);
   }

   public Object[] toArray() {
      return this.delegate.toArray();
   }

   public Object[] toArray(Object[] a) {
      return this.delegate.toArray(a);
   }

   public void addPropertyChangeListener(PropertyChangeListener listener) {
      this.pcs.addPropertyChangeListener(listener);
   }

   public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      this.pcs.addPropertyChangeListener(propertyName, listener);
   }

   public PropertyChangeListener[] getPropertyChangeListeners() {
      return this.pcs.getPropertyChangeListeners();
   }

   public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
      return this.pcs.getPropertyChangeListeners(propertyName);
   }

   public void removePropertyChangeListener(PropertyChangeListener listener) {
      this.pcs.removePropertyChangeListener(listener);
   }

   public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
      this.pcs.removePropertyChangeListener(propertyName, listener);
   }

   public boolean hasListeners(String propertyName) {
      return this.pcs.hasListeners(propertyName);
   }

   public static class MultiElementRemovedEvent extends ObservableList.ElementEvent {
      private List values = new ArrayList();

      public MultiElementRemovedEvent(Object source, List values) {
         super(source, OLDVALUE, NEWVALUE, 4, 0);
         if (values != null) {
            this.values.addAll(values);
         }

      }

      public List getValues() {
         return Collections.unmodifiableList(this.values);
      }
   }

   public static class MultiElementAddedEvent extends ObservableList.ElementEvent {
      private List values = new ArrayList();

      public MultiElementAddedEvent(Object source, int index, List values) {
         super(source, OLDVALUE, NEWVALUE, 4, index);
         if (values != null) {
            this.values.addAll(values);
         }

      }

      public List getValues() {
         return Collections.unmodifiableList(this.values);
      }
   }

   public static class ElementClearedEvent extends ObservableList.ElementEvent {
      private List values = new ArrayList();

      public ElementClearedEvent(Object source, List values) {
         super(source, OLDVALUE, NEWVALUE, 3, 0);
         if (values != null) {
            this.values.addAll(values);
         }

      }

      public List getValues() {
         return Collections.unmodifiableList(this.values);
      }
   }

   public static class ElementRemovedEvent extends ObservableList.ElementEvent {
      public ElementRemovedEvent(Object source, Object newValue, int index) {
         super(source, (Object)null, newValue, index, 2);
      }
   }

   public static class ElementUpdatedEvent extends ObservableList.ElementEvent {
      public ElementUpdatedEvent(Object source, Object oldValue, Object newValue, int index) {
         super(source, oldValue, newValue, index, 1);
      }
   }

   public static class ElementAddedEvent extends ObservableList.ElementEvent {
      public ElementAddedEvent(Object source, Object newValue, int index) {
         super(source, (Object)null, newValue, index, 0);
      }
   }

   public abstract static class ElementEvent extends PropertyChangeEvent {
      public static final int ADDED = 0;
      public static final int UPDATED = 1;
      public static final int REMOVED = 2;
      public static final int CLEARED = 3;
      public static final int MULTI_ADD = 4;
      public static final int MULTI_REMOVE = 5;
      private static final String PROPERTY_NAME = "groovy_util_ObservableList__element";
      protected static final Object OLDVALUE = new Object();
      protected static final Object NEWVALUE = new Object();
      private int type;
      private int index;

      public ElementEvent(Object source, Object oldValue, Object newValue, int index, int type) {
         super(source, "groovy_util_ObservableList__element", oldValue, newValue);
         switch(type) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
            this.type = type;
            break;
         default:
            this.type = 1;
         }

         this.index = index;
      }

      public int getIndex() {
         return this.index;
      }

      public int getType() {
         return this.type;
      }

      public String getTypeAsString() {
         switch(this.type) {
         case 0:
            return "ADDED";
         case 1:
            return "UPDATED";
         case 2:
            return "REMOVED";
         case 3:
            return "CLEARED";
         case 4:
            return "MULTI_ADD";
         case 5:
            return "MULTI_REMOVE";
         default:
            return "UPDATED";
         }
      }
   }

   private class ObservableListIterator extends ObservableList.ObservableIterator implements ListIterator {
      public ObservableListIterator(ListIterator iterDelegate, int index) {
         super(iterDelegate);
         this.cursor = index;
      }

      public ListIterator getListIterator() {
         return (ListIterator)this.getDelegate();
      }

      public void add(Object o) {
         ObservableList.this.add(o);
         ++this.cursor;
      }

      public boolean hasPrevious() {
         return this.getListIterator().hasPrevious();
      }

      public int nextIndex() {
         return this.getListIterator().nextIndex();
      }

      public Object previous() {
         return this.getListIterator().previous();
      }

      public int previousIndex() {
         return this.getListIterator().previousIndex();
      }

      public void set(Object o) {
         ObservableList.this.set(this.cursor, o);
      }
   }

   private class ObservableIterator implements Iterator {
      private Iterator iterDelegate;
      protected int cursor = 0;

      public ObservableIterator(Iterator iterDelegate) {
         this.iterDelegate = iterDelegate;
      }

      public Iterator getDelegate() {
         return this.iterDelegate;
      }

      public boolean hasNext() {
         return this.iterDelegate.hasNext();
      }

      public Object next() {
         ++this.cursor;
         return this.iterDelegate.next();
      }

      public void remove() {
         ObservableList.this.remove(this.cursor--);
      }
   }
}
