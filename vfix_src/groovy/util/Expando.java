package groovy.util;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.MetaExpandoProperty;
import groovy.lang.MissingPropertyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Expando extends GroovyObjectSupport {
   private Map expandoProperties;

   public Expando() {
   }

   public Expando(Map expandoProperties) {
      this.expandoProperties = expandoProperties;
   }

   public Map getProperties() {
      if (this.expandoProperties == null) {
         this.expandoProperties = this.createMap();
      }

      return this.expandoProperties;
   }

   public List getMetaPropertyValues() {
      List ret = new ArrayList();
      Iterator i$ = this.getProperties().entrySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         Entry entry = (Entry)o;
         ret.add(new MetaExpandoProperty(entry));
      }

      return ret;
   }

   public Object getProperty(String property) {
      Object result = this.getProperties().get(property);
      if (result != null) {
         return result;
      } else {
         try {
            return super.getProperty(property);
         } catch (MissingPropertyException var4) {
            return null;
         }
      }
   }

   public void setProperty(String property, Object newValue) {
      this.getProperties().put(property, newValue);
   }

   public Object invokeMethod(String name, Object args) {
      try {
         return super.invokeMethod(name, args);
      } catch (GroovyRuntimeException var6) {
         Object value = this.getProperty(name);
         if (value instanceof Closure) {
            Closure closure = (Closure)value;
            closure = (Closure)closure.clone();
            closure.setDelegate(this);
            return closure.call((Object[])((Object[])args));
         } else {
            throw var6;
         }
      }
   }

   public String toString() {
      Object method = this.getProperties().get("toString");
      if (method != null && method instanceof Closure) {
         Closure closure = (Closure)method;
         closure.setDelegate(this);
         return closure.call().toString();
      } else {
         return this.expandoProperties.toString();
      }
   }

   public boolean equals(Object obj) {
      Object method = this.getProperties().get("equals");
      if (method != null && method instanceof Closure) {
         Closure closure = (Closure)method;
         closure.setDelegate(this);
         Boolean ret = (Boolean)closure.call(obj);
         return ret;
      } else {
         return super.equals(obj);
      }
   }

   public int hashCode() {
      Object method = this.getProperties().get("hashCode");
      if (method != null && method instanceof Closure) {
         Closure closure = (Closure)method;
         closure.setDelegate(this);
         Integer ret = (Integer)closure.call();
         return ret;
      } else {
         return super.hashCode();
      }
   }

   protected Map createMap() {
      return new HashMap();
   }
}
