package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.FactoryTransformer;

public class LazyMap extends AbstractMapDecorator implements Map, Serializable {
   private static final long serialVersionUID = 7990956402564206740L;
   protected final Transformer factory;

   public static Map decorate(Map map, Factory factory) {
      return new LazyMap(map, factory);
   }

   public static Map decorate(Map map, Transformer factory) {
      return new LazyMap(map, factory);
   }

   protected LazyMap(Map map, Factory factory) {
      super(map);
      if (factory == null) {
         throw new IllegalArgumentException("Factory must not be null");
      } else {
         this.factory = FactoryTransformer.getInstance(factory);
      }
   }

   protected LazyMap(Map map, Transformer factory) {
      super(map);
      if (factory == null) {
         throw new IllegalArgumentException("Factory must not be null");
      } else {
         this.factory = factory;
      }
   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      out.writeObject(super.map);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      super.map = (Map)in.readObject();
   }

   public Object get(Object key) {
      if (!super.map.containsKey(key)) {
         Object value = this.factory.transform(key);
         super.map.put(key, value);
         return value;
      } else {
         return super.map.get(key);
      }
   }
}
