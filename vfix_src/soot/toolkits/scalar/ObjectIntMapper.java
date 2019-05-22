package soot.toolkits.scalar;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class ObjectIntMapper<E> {
   private Vector<E> intToObjects;
   private int counter;
   private Map<E, Integer> objectToInts;

   public ObjectIntMapper() {
      this.intToObjects = new Vector();
      this.objectToInts = new HashMap();
      this.counter = 0;
   }

   public ObjectIntMapper(FlowUniverse<E> flowUniverse) {
      this(flowUniverse.iterator(), flowUniverse.size());
   }

   public ObjectIntMapper(Collection<E> collection) {
      this(collection.iterator(), collection.size());
   }

   private ObjectIntMapper(Iterator<E> it, int initSize) {
      this.intToObjects = new Vector(initSize);
      this.objectToInts = new HashMap(initSize);
      this.counter = 0;

      while(it.hasNext()) {
         this.add(it.next());
      }

   }

   public int add(E o) {
      this.objectToInts.put(o, this.counter);
      this.intToObjects.add(o);
      return this.counter++;
   }

   public int getInt(E o) {
      Integer i = (Integer)this.objectToInts.get(o);
      return i != null ? i : this.add(o);
   }

   public E getObject(int i) {
      return this.intToObjects.get(i);
   }

   public boolean contains(Object o) {
      return this.objectToInts.containsKey(o);
   }

   public int size() {
      return this.counter;
   }
}
