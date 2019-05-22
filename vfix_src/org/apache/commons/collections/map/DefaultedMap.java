package org.apache.commons.collections.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.Factory;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.FactoryTransformer;

public class DefaultedMap extends AbstractMapDecorator implements Map, Serializable {
   private static final long serialVersionUID = 19698628745827L;
   protected final Object value;

   public static Map decorate(Map map, Object defaultValue) {
      if (defaultValue instanceof Transformer) {
         defaultValue = ConstantTransformer.getInstance(defaultValue);
      }

      return new DefaultedMap(map, defaultValue);
   }

   public static Map decorate(Map map, Factory factory) {
      if (factory == null) {
         throw new IllegalArgumentException("Factory must not be null");
      } else {
         return new DefaultedMap(map, FactoryTransformer.getInstance(factory));
      }
   }

   public static Map decorate(Map map, Transformer factory) {
      if (factory == null) {
         throw new IllegalArgumentException("Transformer must not be null");
      } else {
         return new DefaultedMap(map, factory);
      }
   }

   public DefaultedMap(Object defaultValue) {
      super(new HashMap());
      if (defaultValue instanceof Transformer) {
         defaultValue = ConstantTransformer.getInstance(defaultValue);
      }

      this.value = defaultValue;
   }

   protected DefaultedMap(Map map, Object value) {
      super(map);
      this.value = value;
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
         return this.value instanceof Transformer ? ((Transformer)this.value).transform(key) : this.value;
      } else {
         return super.map.get(key);
      }
   }
}
