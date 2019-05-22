package groovy.util;

import groovy.lang.Closure;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class ObservableMap implements Map {
   private Map delegate;
   private PropertyChangeSupport pcs;
   private Closure test;

   public ObservableMap() {
      this(new LinkedHashMap(), (Closure)null);
   }

   public ObservableMap(Closure test) {
      this(new LinkedHashMap(), test);
   }

   public ObservableMap(Map delegate) {
      this(delegate, (Closure)null);
   }

   public ObservableMap(Map delegate, Closure test) {
      this.delegate = delegate;
      this.test = test;
      this.pcs = new PropertyChangeSupport(this);
   }

   public void clear() {
      Map values = new HashMap();
      if (!this.delegate.isEmpty()) {
         values.putAll(this.delegate);
      }

      this.delegate.clear();
      if (values != null) {
         this.pcs.firePropertyChange(new ObservableMap.PropertyClearedEvent(this, values));
      }

   }

   public boolean containsKey(Object key) {
      return this.delegate.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.delegate.containsValue(value);
   }

   public Set entrySet() {
      return this.delegate.entrySet();
   }

   public boolean equals(Object o) {
      return this.delegate.equals(o);
   }

   public Object get(Object key) {
      return this.delegate.get(key);
   }

   public int hashCode() {
      return this.delegate.hashCode();
   }

   public boolean isEmpty() {
      return this.delegate.isEmpty();
   }

   public Set keySet() {
      return this.delegate.keySet();
   }

   public Object put(Object key, Object value) {
      Object oldValue = null;
      boolean newKey = !this.delegate.containsKey(key);
      if (this.test != null) {
         oldValue = this.delegate.put(key, value);
         Object result = null;
         if (this.test.getMaximumNumberOfParameters() == 2) {
            result = this.test.call(new Object[]{key, value});
         } else {
            result = this.test.call(value);
         }

         if (result != null && result instanceof Boolean && (Boolean)result) {
            if (newKey) {
               this.pcs.firePropertyChange(new ObservableMap.PropertyAddedEvent(this, String.valueOf(key), value));
            } else if (oldValue != value) {
               this.pcs.firePropertyChange(new ObservableMap.PropertyUpdatedEvent(this, String.valueOf(key), oldValue, value));
            }
         }
      } else {
         oldValue = this.delegate.put(key, value);
         if (newKey) {
            this.pcs.firePropertyChange(new ObservableMap.PropertyAddedEvent(this, String.valueOf(key), value));
         } else if (oldValue != value) {
            this.pcs.firePropertyChange(new ObservableMap.PropertyUpdatedEvent(this, String.valueOf(key), oldValue, value));
         }
      }

      return oldValue;
   }

   public void putAll(Map map) {
      if (map != null) {
         List events = new ArrayList();
         Iterator entries = map.entrySet().iterator();

         while(entries.hasNext()) {
            Entry entry = (Entry)entries.next();
            String key = String.valueOf(entry.getKey());
            Object newValue = entry.getValue();
            Object oldValue = null;
            boolean newKey = !this.delegate.containsKey(key);
            if (this.test != null) {
               oldValue = this.delegate.put(key, newValue);
               Object result = null;
               if (this.test.getMaximumNumberOfParameters() == 2) {
                  result = this.test.call(new Object[]{key, newValue});
               } else {
                  result = this.test.call(newValue);
               }

               if (result != null && result instanceof Boolean && (Boolean)result) {
                  if (newKey) {
                     events.add(new ObservableMap.PropertyAddedEvent(this, key, newValue));
                  } else if (oldValue != newValue) {
                     events.add(new ObservableMap.PropertyUpdatedEvent(this, key, oldValue, newValue));
                  }
               }
            } else {
               oldValue = this.delegate.put(key, newValue);
               if (newKey) {
                  events.add(new ObservableMap.PropertyAddedEvent(this, key, newValue));
               } else if (oldValue != newValue) {
                  events.add(new ObservableMap.PropertyUpdatedEvent(this, key, oldValue, newValue));
               }
            }
         }

         if (events.size() > 0) {
            this.pcs.firePropertyChange(new ObservableMap.MultiPropertyEvent(this, (ObservableMap.PropertyEvent[])((ObservableMap.PropertyEvent[])events.toArray(new ObservableMap.PropertyEvent[events.size()]))));
         }
      }

   }

   public Object remove(Object key) {
      Object result = this.delegate.remove(key);
      if (key != null) {
         this.pcs.firePropertyChange(new ObservableMap.PropertyRemovedEvent(this, String.valueOf(key), result));
      }

      return result;
   }

   public int size() {
      return this.delegate.size();
   }

   public Collection values() {
      return this.delegate.values();
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

   public static class MultiPropertyEvent extends ObservableMap.PropertyEvent {
      public static final String MULTI_PROPERTY = "groovy_util_ObservableMap_MultiPropertyEvent_MULTI";
      private ObservableMap.PropertyEvent[] events = new ObservableMap.PropertyEvent[0];

      public MultiPropertyEvent(Object source, ObservableMap.PropertyEvent[] events) {
         super(source, "groovy_util_ObservableMap_MultiPropertyEvent_MULTI", OLDVALUE, NEWVALUE, 4);
         if (events != null && events.length > 0) {
            this.events = new ObservableMap.PropertyEvent[events.length];
            System.arraycopy(events, 0, this.events, 0, events.length);
         }

      }

      public ObservableMap.PropertyEvent[] getEvents() {
         ObservableMap.PropertyEvent[] copy = new ObservableMap.PropertyEvent[this.events.length];
         System.arraycopy(this.events, 0, copy, 0, this.events.length);
         return copy;
      }
   }

   public static class PropertyClearedEvent extends ObservableMap.PropertyEvent {
      public static final String CLEAR_PROPERTY = "groovy_util_ObservableMap_PropertyClearedEvent_CLEAR";
      private Map values = new HashMap();

      public PropertyClearedEvent(Object source, Map values) {
         super(source, "groovy_util_ObservableMap_PropertyClearedEvent_CLEAR", OLDVALUE, NEWVALUE, 3);
         if (values != null) {
            this.values.putAll(values);
         }

      }

      public Map getValues() {
         return Collections.unmodifiableMap(this.values);
      }
   }

   public static class PropertyRemovedEvent extends ObservableMap.PropertyEvent {
      public PropertyRemovedEvent(Object source, String propertyName, Object oldValue) {
         super(source, propertyName, oldValue, (Object)null, 2);
      }
   }

   public static class PropertyUpdatedEvent extends ObservableMap.PropertyEvent {
      public PropertyUpdatedEvent(Object source, String propertyName, Object oldValue, Object newValue) {
         super(source, propertyName, oldValue, newValue, 1);
      }
   }

   public static class PropertyAddedEvent extends ObservableMap.PropertyEvent {
      public PropertyAddedEvent(Object source, String propertyName, Object newValue) {
         super(source, propertyName, (Object)null, newValue, 0);
      }
   }

   public abstract static class PropertyEvent extends PropertyChangeEvent {
      public static final int ADDED = 0;
      public static final int UPDATED = 1;
      public static final int REMOVED = 2;
      public static final int CLEARED = 3;
      public static final int MULTI = 4;
      protected static final Object OLDVALUE = new Object();
      protected static final Object NEWVALUE = new Object();
      private int type;

      public PropertyEvent(Object source, String propertyName, Object oldValue, Object newValue, int type) {
         super(source, propertyName, oldValue, newValue);
         switch(type) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
            this.type = type;
            break;
         default:
            this.type = 1;
         }

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
            return "MULTI";
         default:
            return "UPDATED";
         }
      }
   }
}
