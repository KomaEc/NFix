package groovy.util;

import groovy.lang.Closure;
import groovy.lang.DelegatingMetaClass;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import groovy.xml.QName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class NodeList extends ArrayList {
   public NodeList() {
   }

   public NodeList(Collection collection) {
      super(collection);
   }

   public NodeList(int size) {
      super(size);
   }

   protected static void setMetaClass(Class nodelistClass, MetaClass metaClass) {
      MetaClass newMetaClass = new DelegatingMetaClass(metaClass) {
         public Object getAttribute(Object object, String attribute) {
            NodeList nl = (NodeList)object;
            Iterator it = nl.iterator();
            ArrayList result = new ArrayList();

            while(it.hasNext()) {
               Node node = (Node)it.next();
               result.add(node.attributes().get(attribute));
            }

            return result;
         }

         public void setAttribute(Object object, String attribute, Object newValue) {
            Iterator i$ = ((NodeList)object).iterator();

            while(i$.hasNext()) {
               Object o = i$.next();
               Node node = (Node)o;
               node.attributes().put(attribute, newValue);
            }

         }

         public Object getProperty(Object object, String property) {
            if (object instanceof NodeList) {
               NodeList nl = (NodeList)object;
               return nl.getAt(property);
            } else {
               return super.getProperty(object, property);
            }
         }
      };
      GroovySystem.getMetaClassRegistry().setMetaClass(nodelistClass, newMetaClass);
   }

   public NodeList getAt(String name) {
      NodeList answer = new NodeList();
      Iterator i$ = this.iterator();

      while(i$.hasNext()) {
         Object child = i$.next();
         if (child instanceof Node) {
            Node childNode = (Node)child;
            Object temp = childNode.get(name);
            if (temp instanceof Collection) {
               answer.addAll((Collection)temp);
            } else {
               answer.add(temp);
            }
         }
      }

      return answer;
   }

   public NodeList getAt(QName name) {
      NodeList answer = new NodeList();
      Iterator i$ = this.iterator();

      while(i$.hasNext()) {
         Object child = i$.next();
         if (child instanceof Node) {
            Node childNode = (Node)child;
            NodeList temp = childNode.getAt(name);
            answer.addAll(temp);
         }
      }

      return answer;
   }

   public String text() {
      String previousText = null;
      StringBuffer buffer = null;
      Iterator i$ = this.iterator();

      while(i$.hasNext()) {
         Object child = i$.next();
         String text = null;
         if (child instanceof String) {
            text = (String)child;
         } else if (child instanceof Node) {
            text = ((Node)child).text();
         }

         if (text != null) {
            if (previousText == null) {
               previousText = text;
            } else {
               if (buffer == null) {
                  buffer = new StringBuffer();
                  buffer.append(previousText);
               }

               buffer.append(text);
            }
         }
      }

      if (buffer != null) {
         return buffer.toString();
      } else if (previousText != null) {
         return previousText;
      } else {
         return "";
      }
   }

   public Node replaceNode(Closure c) {
      if (this.size() > 0 && this.size() <= 1) {
         return ((Node)this.get(0)).replaceNode(c);
      } else {
         throw new GroovyRuntimeException("replaceNode() can only be used to replace a single node.");
      }
   }

   public void plus(Closure c) {
      Iterator i$ = this.iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         ((Node)o).plus(c);
      }

   }

   static {
      setMetaClass(NodeList.class, GroovySystem.getMetaClassRegistry().getMetaClass(NodeList.class));
   }
}
