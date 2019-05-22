package org.codehaus.groovy.ant;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.Project;

public class AntProjectPropertiesDelegate extends Hashtable {
   private Project project;

   public AntProjectPropertiesDelegate(Project project) {
      this.project = project;
   }

   public synchronized int hashCode() {
      return this.project.getProperties().hashCode();
   }

   public synchronized int size() {
      return this.project.getProperties().size();
   }

   public synchronized void clear() {
      throw new UnsupportedOperationException("Impossible to clear the project properties.");
   }

   public synchronized boolean isEmpty() {
      return this.project.getProperties().isEmpty();
   }

   public synchronized Object clone() {
      return this.project.getProperties().clone();
   }

   public synchronized boolean contains(Object value) {
      return this.project.getProperties().contains(value);
   }

   public synchronized boolean containsKey(Object key) {
      return this.project.getProperties().containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.project.getProperties().containsValue(value);
   }

   public synchronized boolean equals(Object o) {
      return this.project.getProperties().equals(o);
   }

   public synchronized String toString() {
      return this.project.getProperties().toString();
   }

   public Collection values() {
      return this.project.getProperties().values();
   }

   public synchronized Enumeration elements() {
      return this.project.getProperties().elements();
   }

   public synchronized Enumeration keys() {
      return this.project.getProperties().keys();
   }

   public AntProjectPropertiesDelegate(Map t) {
      super(t);
   }

   public synchronized void putAll(Map t) {
      Set keySet = t.keySet();
      Iterator iterator = keySet.iterator();

      while(iterator.hasNext()) {
         Object key = iterator.next();
         Object value = t.get(key);
         this.put(key, value);
      }

   }

   public Set entrySet() {
      return this.project.getProperties().entrySet();
   }

   public Set keySet() {
      return this.project.getProperties().keySet();
   }

   public synchronized Object get(Object key) {
      return this.project.getProperties().get(key);
   }

   public synchronized Object remove(Object key) {
      throw new UnsupportedOperationException("Impossible to remove a property from the project properties.");
   }

   public synchronized Object put(Object key, Object value) {
      Object oldValue = null;
      if (this.containsKey(key)) {
         oldValue = this.get(key);
      }

      this.project.setProperty(key.toString(), value.toString());
      return oldValue;
   }
}
