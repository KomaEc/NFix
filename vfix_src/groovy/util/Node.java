package groovy.util;

import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.xml.QName;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.runtime.InvokerHelper;

public class Node implements Serializable {
   private static final long serialVersionUID = 4121134753270542643L;
   private Node parent;
   private Object name;
   private Map attributes;
   private Object value;

   public Node(Node parent, Object name) {
      this(parent, name, (Object)(new NodeList()));
   }

   public Node(Node parent, Object name, Object value) {
      this(parent, name, new HashMap(), value);
   }

   public Node(Node parent, Object name, Map attributes) {
      this(parent, name, attributes, new NodeList());
   }

   public Node(Node parent, Object name, Map attributes, Object value) {
      this.parent = parent;
      this.name = name;
      this.attributes = attributes;
      this.value = value;
      if (parent != null) {
         this.getParentList(parent).add(this);
      }

   }

   private List getParentList(Node parent) {
      Object parentValue = parent.value();
      Object parentList;
      if (parentValue instanceof List) {
         parentList = (List)parentValue;
      } else {
         parentList = new NodeList();
         ((List)parentList).add(parentValue);
         parent.setValue(parentList);
      }

      return (List)parentList;
   }

   public boolean append(Node child) {
      child.parent = this;
      return this.getParentList(this).add(child);
   }

   public boolean remove(Node child) {
      child.parent = null;
      return this.getParentList(this).remove(child);
   }

   public Node appendNode(Object name, Map attributes) {
      return new Node(this, name, attributes);
   }

   public Node appendNode(Object name) {
      return new Node(this, name);
   }

   public Node appendNode(Object name, Object value) {
      return new Node(this, name, value);
   }

   public Node appendNode(Object name, Map attributes, Object value) {
      return new Node(this, name, attributes, value);
   }

   public Node replaceNode(Closure c) {
      this.getParentList(this.parent).remove(this);
      NodeBuilder b = new NodeBuilder();
      Node newNode = (Node)b.invokeMethod("dummyNode", c);
      List<Node> children = newNode.children();
      Node result = this;

      Node child;
      for(Iterator i$ = children.iterator(); i$.hasNext(); result = this.parent.appendNode(child.name(), child.attributes(), child.value())) {
         child = (Node)i$.next();
      }

      return result;
   }

   public void plus(Closure c) {
      List<Node> list = this.parent().children();
      int afterIndex = list.indexOf(this);
      List<Node> leftOvers = new ArrayList(list.subList(afterIndex + 1, list.size()));
      list.subList(afterIndex + 1, list.size()).clear();
      NodeBuilder b = new NodeBuilder();
      Node newNode = (Node)b.invokeMethod("dummyNode", c);
      List<Node> children = newNode.children();
      Iterator i$ = children.iterator();

      Node child;
      while(i$.hasNext()) {
         child = (Node)i$.next();
         this.parent.appendNode(child.name(), child.attributes(), child.value());
      }

      i$ = leftOvers.iterator();

      while(i$.hasNext()) {
         child = (Node)i$.next();
         this.parent().children().add(child);
      }

   }

   protected static void setMetaClass(MetaClass metaClass, Class nodeClass) {
      MetaClass newMetaClass = new DelegatingMetaClass(metaClass) {
         public Object getAttribute(Object object, String attribute) {
            Node n = (Node)object;
            return n.get("@" + attribute);
         }

         public void setAttribute(Object object, String attribute, Object newValue) {
            Node n = (Node)object;
            n.attributes().put(attribute, newValue);
         }

         public Object getProperty(Object object, String property) {
            if (object instanceof Node) {
               Node n = (Node)object;
               return n.get(property);
            } else {
               return super.getProperty(object, property);
            }
         }

         public void setProperty(Object object, String property, Object newValue) {
            if (property.startsWith("@")) {
               String attribute = property.substring(1);
               Node n = (Node)object;
               n.attributes().put(attribute, newValue);
            } else {
               this.delegate.setProperty(object, property, newValue);
            }
         }
      };
      GroovySystem.getMetaClassRegistry().setMetaClass(nodeClass, newMetaClass);
   }

   public String text() {
      if (this.value instanceof String) {
         return (String)this.value;
      } else {
         if (this.value instanceof Collection) {
            Collection coll = (Collection)this.value;
            String previousText = null;
            StringBuffer buffer = null;
            Iterator iter = coll.iterator();

            while(iter.hasNext()) {
               Object child = iter.next();
               if (child instanceof String) {
                  String childText = (String)child;
                  if (previousText == null) {
                     previousText = childText;
                  } else {
                     if (buffer == null) {
                        buffer = new StringBuffer();
                        buffer.append(previousText);
                     }

                     buffer.append(childText);
                  }
               }
            }

            if (buffer != null) {
               return buffer.toString();
            }

            if (previousText != null) {
               return previousText;
            }
         }

         return "";
      }
   }

   public Iterator iterator() {
      return this.children().iterator();
   }

   public List children() {
      if (this.value == null) {
         return new NodeList();
      } else if (this.value instanceof List) {
         return (List)this.value;
      } else {
         List result = new NodeList();
         result.add(this.value);
         return result;
      }
   }

   public Map attributes() {
      return this.attributes;
   }

   public Object attribute(Object key) {
      return this.attributes != null ? this.attributes.get(key) : null;
   }

   public Object name() {
      return this.name;
   }

   public Object value() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Node parent() {
      return this.parent;
   }

   public Object get(String key) {
      if (key != null && key.charAt(0) == '@') {
         String attributeName = key.substring(1);
         return this.attributes().get(attributeName);
      } else if ("..".equals(key)) {
         return this.parent();
      } else if ("*".equals(key)) {
         return this.children();
      } else {
         return "**".equals(key) ? this.depthFirst() : this.getByName(key);
      }
   }

   public NodeList getAt(QName name) {
      NodeList answer = new NodeList();
      Iterator iter = this.children().iterator();

      while(iter.hasNext()) {
         Object child = iter.next();
         if (child instanceof Node) {
            Node childNode = (Node)child;
            Object childNodeName = childNode.name();
            if (name.matches(childNodeName)) {
               answer.add(childNode);
            }
         }
      }

      return answer;
   }

   private NodeList getByName(String name) {
      NodeList answer = new NodeList();
      Iterator iter = this.children().iterator();

      while(iter.hasNext()) {
         Object child = iter.next();
         if (child instanceof Node) {
            Node childNode = (Node)child;
            Object childNodeName = childNode.name();
            if (childNodeName instanceof QName) {
               QName qn = (QName)childNodeName;
               if (qn.matches(name)) {
                  answer.add(childNode);
               }
            } else if (name.equals(childNodeName)) {
               answer.add(childNode);
            }
         }
      }

      return answer;
   }

   public List depthFirst() {
      List answer = new NodeList();
      answer.add(this);
      answer.addAll(this.depthFirstRest());
      return answer;
   }

   private List depthFirstRest() {
      List answer = new NodeList();
      Iterator iter = InvokerHelper.asIterator(this.value);

      while(iter.hasNext()) {
         Object child = iter.next();
         if (child instanceof Node) {
            Node childNode = (Node)child;
            List children = childNode.depthFirstRest();
            answer.add(childNode);
            answer.addAll(children);
         }
      }

      return answer;
   }

   public List breadthFirst() {
      List answer = new NodeList();
      answer.add(this);
      answer.addAll(this.breadthFirstRest());
      return answer;
   }

   private List breadthFirstRest() {
      List answer = new NodeList();
      Object nextLevelChildren = this.getDirectChildren();

      while(!((List)nextLevelChildren).isEmpty()) {
         List working = new NodeList((Collection)nextLevelChildren);
         nextLevelChildren = new NodeList();
         Iterator iter = working.iterator();

         while(iter.hasNext()) {
            Node childNode = (Node)iter.next();
            answer.add(childNode);
            List children = childNode.getDirectChildren();
            ((List)nextLevelChildren).addAll(children);
         }
      }

      return answer;
   }

   private List getDirectChildren() {
      List answer = new NodeList();
      Iterator iter = InvokerHelper.asIterator(this.value);

      while(iter.hasNext()) {
         Object child = iter.next();
         if (child instanceof Node) {
            Node childNode = (Node)child;
            answer.add(childNode);
         }
      }

      return answer;
   }

   public String toString() {
      return this.name + "[attributes=" + this.attributes + "; value=" + this.value + "]";
   }

   public void print(PrintWriter out) {
      (new NodePrinter(out)).print(this);
   }

   static {
      setMetaClass(GroovySystem.getMetaClassRegistry().getMetaClass(Node.class), Node.class);
   }
}
