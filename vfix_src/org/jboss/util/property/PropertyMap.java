package org.jboss.util.property;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.jboss.util.NullArgumentException;

public class PropertyMap extends Properties {
   private static final long serialVersionUID = 8747802918099008518L;
   public static final String PROPERTY_NAME_SEPARATOR = ".";
   public static final String[] EMPTY_ARRAY_PROPERTY = new String[0];
   protected transient List unboundListeners;
   protected transient Map boundListeners;
   private transient Map jndiMap;
   private static final Object NULL_VALUE = new Object();

   public PropertyMap(Properties defaults) {
      super(defaults);
      this.init();
   }

   public PropertyMap() {
      this((Properties)null);
   }

   private void init() {
      this.unboundListeners = Collections.synchronizedList(new ArrayList());
      this.boundListeners = Collections.synchronizedMap(new HashMap());
      this.jndiMap = new HashMap();
      PrivilegedAction action = new PrivilegedAction() {
         public Object run() {
            Object value = System.getProperty("java.naming.provider.url");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.provider.url", value);
            value = System.getProperty("java.naming.factory.initial");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.factory.initial", value);
            value = System.getProperty("java.naming.factory.object");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.factory.object", value);
            value = System.getProperty("java.naming.factory.url.pkgs");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.factory.url.pkgs", value);
            value = System.getProperty("java.naming.factory.state");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.factory.state", value);
            value = System.getProperty("java.naming.dns.url");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.dns.url", value);
            value = System.getProperty("java.naming.factory.control");
            if (value == null) {
               value = PropertyMap.NULL_VALUE;
            }

            PropertyMap.this.jndiMap.put("java.naming.factory.control", value);
            return null;
         }
      };
      AccessController.doPrivileged(action);
   }

   private void updateJndiCache(String name, String value) {
      if (name != null) {
         boolean isJndiProperty = name.equals("java.naming.provider.url") || name.equals("java.naming.factory.initial") || name.equals("java.naming.factory.object") || name.equals("java.naming.factory.url.pkgs") || name.equals("java.naming.factory.state") || name.equals("java.naming.dns.url") || name.equals("java.naming.factory.control");
         if (isJndiProperty) {
            this.jndiMap.put(name, value);
         }

      }
   }

   public Object put(Object name, Object value) {
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         boolean add = !this.containsKey(name);
         Object prev = super.put(name, value);
         PropertyEvent event = new PropertyEvent(this, name.toString(), value.toString());
         if (add) {
            this.firePropertyAdded(event);
         } else {
            this.firePropertyChanged(event);
         }

         return prev;
      }
   }

   public Object remove(Object name) {
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         boolean contains = this.containsKey(name);
         Object value = null;
         if (contains) {
            value = super.remove(name);
            if (this.defaults != null) {
               Object obj = this.defaults.remove(name);
               if (value == null) {
                  value = obj;
               }
            }

            this.jndiMap.remove(name);
            PropertyEvent event = new PropertyEvent(this, name.toString(), value.toString());
            this.firePropertyRemoved(event);
         }

         return value;
      }
   }

   public Set keySet(boolean includeDefaults) {
      if (includeDefaults) {
         Set set = new HashSet();
         set.addAll(this.defaults.keySet());
         set.addAll(super.keySet());
         return Collections.synchronizedSet(set);
      } else {
         return super.keySet();
      }
   }

   public Set entrySet(boolean includeDefaults) {
      if (includeDefaults) {
         Set set = new HashSet();
         set.addAll(this.defaults.entrySet());
         set.addAll(super.entrySet());
         return Collections.synchronizedSet(set);
      } else {
         return super.entrySet();
      }
   }

   public void addPropertyListener(PropertyListener listener) {
      if (listener == null) {
         throw new NullArgumentException("listener");
      } else {
         if (listener instanceof BoundPropertyListener) {
            this.addPropertyListener((BoundPropertyListener)listener);
         } else if (!this.unboundListeners.contains(listener)) {
            this.unboundListeners.add(listener);
         }

      }
   }

   protected void addPropertyListener(BoundPropertyListener listener) {
      String name = listener.getPropertyName();
      List list = (List)this.boundListeners.get(name);
      if (list == null) {
         list = Collections.synchronizedList(new ArrayList());
         this.boundListeners.put(name, list);
      }

      if (!list.contains(listener)) {
         list.add(listener);
         listener.propertyBound(this);
      }

   }

   public void addPropertyListeners(PropertyListener[] listeners) {
      if (listeners == null) {
         throw new NullArgumentException("listeners");
      } else {
         for(int i = 0; i < listeners.length; ++i) {
            this.addPropertyListener(listeners[i]);
         }

      }
   }

   public boolean removePropertyListener(PropertyListener listener) {
      if (listener == null) {
         throw new NullArgumentException("listener");
      } else {
         boolean removed = false;
         if (listener instanceof BoundPropertyListener) {
            removed = this.removePropertyListener((BoundPropertyListener)listener);
         } else {
            removed = this.unboundListeners.remove(listener);
         }

         return removed;
      }
   }

   protected boolean removePropertyListener(BoundPropertyListener listener) {
      String name = listener.getPropertyName();
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

   private void firePropertyAdded(List list, PropertyEvent event) {
      if (list != null) {
         int size = list.size();

         for(int i = 0; i < size; ++i) {
            PropertyListener listener = (PropertyListener)list.get(i);
            listener.propertyAdded(event);
         }

      }
   }

   protected void firePropertyAdded(PropertyEvent event) {
      if (this.boundListeners != null) {
         List list = (List)this.boundListeners.get(event.getPropertyName());
         if (list != null) {
            this.firePropertyAdded(list, event);
         }
      }

      this.firePropertyAdded(this.unboundListeners, event);
   }

   private void firePropertyRemoved(List list, PropertyEvent event) {
      if (list != null) {
         int size = list.size();

         for(int i = 0; i < size; ++i) {
            PropertyListener listener = (PropertyListener)list.get(i);
            listener.propertyRemoved(event);
         }

      }
   }

   protected void firePropertyRemoved(PropertyEvent event) {
      if (this.boundListeners != null) {
         List list = (List)this.boundListeners.get(event.getPropertyName());
         if (list != null) {
            this.firePropertyRemoved(list, event);
         }
      }

      this.firePropertyRemoved(this.unboundListeners, event);
   }

   private void firePropertyChanged(List list, PropertyEvent event) {
      if (list != null) {
         int size = list.size();

         for(int i = 0; i < size; ++i) {
            PropertyListener listener = (PropertyListener)list.get(i);
            listener.propertyChanged(event);
         }

      }
   }

   protected void firePropertyChanged(PropertyEvent event) {
      if (this.boundListeners != null) {
         List list = (List)this.boundListeners.get(event.getPropertyName());
         if (list != null) {
            this.firePropertyChanged(list, event);
         }
      }

      this.firePropertyChanged(this.unboundListeners, event);
   }

   protected String makePrefixedPropertyName(String base, String prefix) {
      if (prefix != null) {
         StringBuffer buff = new StringBuffer(base);
         if (prefix != null) {
            buff.insert(0, ".");
            buff.insert(0, prefix);
         }

         return buff.toString();
      } else {
         return base;
      }
   }

   public void load(String prefix, Map map) throws PropertyException {
      if (map == null) {
         throw new NullArgumentException("map");
      } else {
         Iterator iter = map.keySet().iterator();

         while(iter.hasNext()) {
            String key = String.valueOf(iter.next());
            String name = this.makePrefixedPropertyName(key, prefix);
            String value = String.valueOf(map.get(name));
            this.setProperty(name, value);
         }

      }
   }

   public void load(Map map) throws PropertyException {
      this.load((String)null, map);
   }

   public void load(PropertyReader reader) throws PropertyException, IOException {
      if (reader == null) {
         throw new NullArgumentException("reader");
      } else {
         this.load(reader.readProperties());
      }
   }

   public void load(String className) throws PropertyException, IOException {
      if (className == null) {
         throw new NullArgumentException("className");
      } else {
         PropertyReader reader = null;

         try {
            Class type = Class.forName(className);
            reader = (PropertyReader)type.newInstance();
         } catch (Exception var4) {
            throw new PropertyException(var4);
         }

         this.load(reader);
      }
   }

   public synchronized Object setProperty(String name, String value) {
      this.updateJndiCache(name, value);
      return this.put(name, value);
   }

   public String getProperty(String name) {
      Object value = this.jndiMap.get(name);
      if (value != null) {
         return value == NULL_VALUE ? null : (String)value;
      } else {
         return super.getProperty(name);
      }
   }

   public String removeProperty(String name) {
      return (String)this.remove(name);
   }

   protected String makeIndexPropertyName(String base, int index) {
      return base + "." + index;
   }

   public String[] getArrayProperty(String base, String[] defaultValues) {
      if (base == null) {
         throw new NullArgumentException("base");
      } else {
         List list = new LinkedList();
         int i = 0;

         while(true) {
            String name = this.makeIndexPropertyName(base, i);
            String value = this.getProperty(name);
            if (value != null) {
               list.add(value);
            } else if (i >= 0) {
               String[] values = defaultValues;
               if (list.size() != 0) {
                  values = (String[])((String[])list.toArray(new String[list.size()]));
               }

               return values;
            }

            ++i;
         }
      }
   }

   public String[] getArrayProperty(String name) {
      return this.getArrayProperty(name, EMPTY_ARRAY_PROPERTY);
   }

   public Iterator names() {
      return this.keySet().iterator();
   }

   public boolean containsProperty(String name) {
      return this.containsKey(name);
   }

   public PropertyGroup getPropertyGroup(String basename) {
      return new PropertyGroup(basename, this);
   }

   public PropertyGroup getPropertyGroup(String basename, int index) {
      String name = this.makeIndexPropertyName(basename, index);
      return this.getPropertyGroup(name);
   }

   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
      this.init();
      stream.defaultReadObject();
   }

   private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
   }
}
