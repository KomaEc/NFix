package groovy.util.slurpersupport;

import groovy.lang.Buildable;
import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.GString;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.IntRange;
import groovy.lang.MetaClass;
import groovy.lang.Range;
import groovy.lang.Writable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

public abstract class GPathResult extends GroovyObjectSupport implements Writable, Buildable {
   protected final GPathResult parent;
   protected final String name;
   protected final String namespacePrefix;
   protected final Map namespaceMap = new HashMap();
   protected final Map<String, String> namespaceTagHints;

   public GPathResult(GPathResult parent, String name, String namespacePrefix, Map<String, String> namespaceTagHints) {
      if (parent == null) {
         this.parent = this;
         this.namespaceMap.put("xml", "http://www.w3.org/XML/1998/namespace");
      } else {
         this.parent = parent;
         this.namespaceMap.putAll(parent.namespaceMap);
      }

      this.name = name;
      this.namespacePrefix = namespacePrefix;
      this.namespaceTagHints = namespaceTagHints;
      this.setMetaClass(this.getMetaClass());
   }

   public void setMetaClass(MetaClass metaClass) {
      MetaClass newMetaClass = new DelegatingMetaClass(metaClass) {
         public Object getAttribute(Object object, String attribute) {
            return GPathResult.this.getProperty("@" + attribute);
         }

         public void setAttribute(Object object, String attribute, Object newValue) {
            GPathResult.this.setProperty("@" + attribute, newValue);
         }
      };
      super.setMetaClass(newMetaClass);
   }

   public Object getProperty(String property) {
      if ("..".equals(property)) {
         return this.parent();
      } else if ("*".equals(property)) {
         return this.children();
      } else if ("**".equals(property)) {
         return this.depthFirst();
      } else {
         int i;
         if (property.startsWith("@")) {
            if (property.indexOf(":") != -1) {
               i = property.indexOf(":");
               return new Attributes(this, "@" + property.substring(i + 1), property.substring(1, i), this.namespaceTagHints);
            } else {
               return new Attributes(this, property, this.namespaceTagHints);
            }
         } else if (property.indexOf(":") != -1) {
            i = property.indexOf(":");
            return new NodeChildren(this, property.substring(i + 1), property.substring(0, i), this.namespaceTagHints);
         } else {
            return new NodeChildren(this, property, this.namespaceTagHints);
         }
      }
   }

   public void setProperty(String property, Object newValue) {
      if (property.startsWith("@")) {
         if (newValue instanceof String || newValue instanceof GString) {
            Iterator iter = this.iterator();

            while(iter.hasNext()) {
               NodeChild child = (NodeChild)iter.next();
               child.attributes().put(property.substring(1), newValue);
            }
         }
      } else {
         GPathResult result = new NodeChildren(this, property, this.namespaceTagHints);
         if (newValue instanceof Map) {
            Iterator i$ = ((Map)newValue).entrySet().iterator();

            while(i$.hasNext()) {
               Object o = i$.next();
               Entry entry = (Entry)o;
               result.setProperty("@" + entry.getKey(), entry.getValue());
            }
         } else if (newValue instanceof Closure) {
            result.replaceNode((Closure)newValue);
         } else {
            result.replaceBody(newValue);
         }
      }

   }

   public Object leftShift(Object newValue) {
      this.appendNode(newValue);
      return this;
   }

   public Object plus(final Object newValue) {
      this.replaceNode(new Closure(this) {
         public void doCall(Object[] args) {
            GroovyObject delegate = (GroovyObject)this.getDelegate();
            delegate.getProperty("mkp");
            delegate.invokeMethod("yield", args);
            delegate.getProperty("mkp");
            delegate.invokeMethod("yield", new Object[]{newValue});
         }
      });
      return this;
   }

   protected abstract void replaceNode(Closure var1);

   protected abstract void replaceBody(Object var1);

   protected abstract void appendNode(Object var1);

   public String name() {
      return this.name;
   }

   public GPathResult parent() {
      return this.parent;
   }

   public GPathResult children() {
      return new NodeChildren(this, this.namespaceTagHints);
   }

   public String lookupNamespace(String prefix) {
      return (String)this.namespaceTagHints.get(prefix);
   }

   public String toString() {
      return this.text();
   }

   public Integer toInteger() {
      return DefaultGroovyMethods.toInteger(this.text());
   }

   public Long toLong() {
      return DefaultGroovyMethods.toLong(this.text());
   }

   public Float toFloat() {
      return DefaultGroovyMethods.toFloat(this.text());
   }

   public Double toDouble() {
      return DefaultGroovyMethods.toDouble(this.text());
   }

   public BigDecimal toBigDecimal() {
      return DefaultGroovyMethods.toBigDecimal(this.text());
   }

   public BigInteger toBigInteger() {
      return DefaultGroovyMethods.toBigInteger(this.text());
   }

   public URL toURL() throws MalformedURLException {
      return DefaultGroovyMethods.toURL(this.text());
   }

   public URI toURI() throws URISyntaxException {
      return DefaultGroovyMethods.toURI(this.text());
   }

   public Boolean toBoolean() {
      return DefaultGroovyMethods.toBoolean(this.text());
   }

   public GPathResult declareNamespace(Map newNamespaceMapping) {
      this.namespaceMap.putAll(newNamespaceMapping);
      return this;
   }

   public boolean equals(Object obj) {
      return this.text().equals(obj.toString());
   }

   public Object getAt(int index) {
      int adjustedIndex;
      if (index < 0) {
         List list = this.list();
         adjustedIndex = index + list.size();
         if (adjustedIndex >= 0 && adjustedIndex < list.size()) {
            return list.get(adjustedIndex);
         }
      } else {
         Iterator iter = this.iterator();
         adjustedIndex = 0;

         while(iter.hasNext()) {
            if (adjustedIndex++ == index) {
               return iter.next();
            }

            iter.next();
         }
      }

      return new NoChildren(this, this.name, this.namespaceTagHints);
   }

   public Object getAt(IntRange range) {
      return DefaultGroovyMethods.getAt((List)this.list(), (Range)range);
   }

   public void putAt(int index, Object newValue) {
      GPathResult result = (GPathResult)this.getAt(index);
      if (newValue instanceof Closure) {
         result.replaceNode((Closure)newValue);
      } else {
         result.replaceBody(newValue);
      }

   }

   public Iterator depthFirst() {
      return new Iterator() {
         private final List list = new LinkedList();
         private final Stack stack = new Stack();
         private Iterator iter = GPathResult.this.iterator();
         private GPathResult next = this.getNextByDepth();

         public boolean hasNext() {
            return this.next != null;
         }

         public Object next() {
            GPathResult var1;
            try {
               var1 = this.next;
            } finally {
               this.next = this.getNextByDepth();
            }

            return var1;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         private GPathResult getNextByDepth() {
            GPathResult result;
            while(this.iter.hasNext()) {
               result = (GPathResult)this.iter.next();
               this.list.add(result);
               this.stack.push(this.iter);
               this.iter = result.children().iterator();
            }

            if (this.list.isEmpty()) {
               return null;
            } else {
               result = (GPathResult)this.list.get(0);
               this.list.remove(0);
               this.iter = (Iterator)this.stack.pop();
               return result;
            }
         }
      };
   }

   public Iterator breadthFirst() {
      return new Iterator() {
         private final List list = new LinkedList();
         private Iterator iter = GPathResult.this.iterator();
         private GPathResult next = this.getNextByBreadth();

         public boolean hasNext() {
            return this.next != null;
         }

         public Object next() {
            GPathResult var1;
            try {
               var1 = this.next;
            } finally {
               this.next = this.getNextByBreadth();
            }

            return var1;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         private GPathResult getNextByBreadth() {
            ArrayList children = new ArrayList();

            while(true) {
               GPathResult result;
               while(this.iter.hasNext() || !children.isEmpty()) {
                  if (this.iter.hasNext()) {
                     result = (GPathResult)this.iter.next();
                     this.list.add(result);
                     this.list.add(this.iter);
                     children.add(result.children());
                  } else {
                     List nextLevel = new ArrayList();
                     Iterator i$ = children.iterator();

                     while(i$.hasNext()) {
                        Object child = i$.next();
                        GPathResult next = (GPathResult)child;
                        Iterator iterator = next.iterator();

                        while(iterator.hasNext()) {
                           nextLevel.add(iterator.next());
                        }
                     }

                     this.iter = nextLevel.iterator();
                     children = new ArrayList();
                  }
               }

               if (this.list.isEmpty()) {
                  return null;
               }

               result = (GPathResult)this.list.get(0);
               this.list.remove(0);
               this.iter = (Iterator)this.list.get(0);
               this.list.remove(0);
               return result;
            }
         }
      };
   }

   public List list() {
      Iterator iter = this.nodeIterator();
      LinkedList result = new LinkedList();

      while(iter.hasNext()) {
         result.add(new NodeChild((Node)iter.next(), this.parent, this.namespacePrefix, this.namespaceTagHints));
      }

      return result;
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public Closure getBody() {
      return new Closure(this.parent(), this) {
         public void doCall(Object[] args) {
            GroovyObject delegate = (GroovyObject)this.getDelegate();
            GPathResult thisObject = (GPathResult)this.getThisObject();
            Node node = (Node)thisObject.getAt(0);
            List children = node.children();
            Iterator i$ = children.iterator();

            while(i$.hasNext()) {
               Object child = i$.next();
               delegate.getProperty("mkp");
               if (child instanceof Node) {
                  delegate.invokeMethod("yield", new Object[]{new NodeChild((Node)child, thisObject, "*", (Map)null)});
               } else {
                  delegate.invokeMethod("yield", new Object[]{child});
               }
            }

         }
      };
   }

   public abstract int size();

   public abstract String text();

   public abstract GPathResult parents();

   public abstract Iterator childNodes();

   public abstract Iterator iterator();

   public abstract GPathResult find(Closure var1);

   public abstract GPathResult findAll(Closure var1);

   public abstract Iterator nodeIterator();
}
