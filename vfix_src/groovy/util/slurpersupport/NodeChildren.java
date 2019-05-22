package groovy.util.slurpersupport;

import groovy.lang.Buildable;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

class NodeChildren extends GPathResult {
   private int size;

   public NodeChildren(GPathResult parent, String name, String namespacePrefix, Map<String, String> namespaceTagHints) {
      super(parent, name, namespacePrefix, namespaceTagHints);
      this.size = -1;
   }

   public NodeChildren(GPathResult parent, String name, Map<String, String> namespaceTagHints) {
      this(parent, name, "*", namespaceTagHints);
   }

   public NodeChildren(GPathResult parent, Map<String, String> namespaceTagHints) {
      this(parent, "*", namespaceTagHints);
   }

   public Iterator childNodes() {
      return new Iterator() {
         private final Iterator iter;
         private Iterator childIter;

         {
            this.iter = NodeChildren.this.parent.childNodes();
            this.childIter = this.nextChildIter();
         }

         public boolean hasNext() {
            return this.childIter != null;
         }

         public Object next() {
            while(true) {
               if (this.childIter != null) {
                  Object var1;
                  try {
                     if (!this.childIter.hasNext()) {
                        continue;
                     }

                     var1 = this.childIter.next();
                  } finally {
                     if (!this.childIter.hasNext()) {
                        this.childIter = this.nextChildIter();
                     }

                  }

                  return var1;
               }

               return null;
            }
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         private Iterator nextChildIter() {
            while(this.iter.hasNext()) {
               Node node = (Node)this.iter.next();
               if (NodeChildren.this.name.equals(node.name()) || NodeChildren.this.name.equals("*")) {
                  Iterator result = node.childNodes();
                  if (result.hasNext() && ("*".equals(NodeChildren.this.namespacePrefix) || "".equals(NodeChildren.this.namespacePrefix) && "".equals(node.namespaceURI()) || node.namespaceURI().equals(NodeChildren.this.namespaceMap.get(NodeChildren.this.namespacePrefix)))) {
                     return result;
                  }
               }
            }

            return null;
         }
      };
   }

   public Iterator iterator() {
      return new Iterator() {
         final Iterator iter = NodeChildren.this.nodeIterator();

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public Object next() {
            return new NodeChild((Node)this.iter.next(), NodeChildren.this.parent, NodeChildren.this.namespaceTagHints);
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Iterator nodeIterator() {
      return (Iterator)("*".equals(this.name) ? this.parent.childNodes() : new NodeIterator(this.parent.childNodes()) {
         protected Object getNextNode(Iterator iter) {
            while(true) {
               if (iter.hasNext()) {
                  Node node = (Node)iter.next();
                  if (!NodeChildren.this.name.equals(node.name()) || !"*".equals(NodeChildren.this.namespacePrefix) && (!"".equals(NodeChildren.this.namespacePrefix) || !"".equals(node.namespaceURI())) && !node.namespaceURI().equals(NodeChildren.this.namespaceMap.get(NodeChildren.this.namespacePrefix))) {
                     continue;
                  }

                  return node;
               }

               return null;
            }
         }
      });
   }

   public GPathResult parents() {
      throw new GroovyRuntimeException("parents() not implemented yet");
   }

   public synchronized int size() {
      if (this.size == -1) {
         Iterator iter = this.iterator();

         for(this.size = 0; iter.hasNext(); ++this.size) {
            iter.next();
         }
      }

      return this.size;
   }

   public String text() {
      StringBuffer buf = new StringBuffer();
      Iterator iter = this.nodeIterator();

      while(iter.hasNext()) {
         buf.append(((Node)iter.next()).text());
      }

      return buf.toString();
   }

   public GPathResult find(Closure closure) {
      Iterator iter = this.iterator();

      Object node;
      do {
         if (!iter.hasNext()) {
            return new NoChildren(this, this.name, this.namespaceTagHints);
         }

         node = iter.next();
      } while(!DefaultTypeTransformation.castToBoolean(closure.call(new Object[]{node})));

      return (GPathResult)node;
   }

   public GPathResult findAll(Closure closure) {
      return new FilteredNodeChildren(this, closure, this.namespaceTagHints);
   }

   public void build(GroovyObject builder) {
      Iterator iter = this.nodeIterator();

      while(iter.hasNext()) {
         Object next = iter.next();
         if (next instanceof Buildable) {
            ((Buildable)next).build(builder);
         } else {
            ((Node)next).build(builder, this.namespaceMap, this.namespaceTagHints);
         }
      }

   }

   public Writer writeTo(Writer out) throws IOException {
      Iterator iter = this.nodeIterator();

      while(iter.hasNext()) {
         ((Node)iter.next()).writeTo(out);
      }

      return out;
   }

   protected void replaceNode(Closure newValue) {
      Iterator iter = this.iterator();

      while(iter.hasNext()) {
         NodeChild result = (NodeChild)iter.next();
         result.replaceNode(newValue);
      }

   }

   protected void replaceBody(Object newValue) {
      Iterator iter = this.iterator();

      while(iter.hasNext()) {
         NodeChild result = (NodeChild)iter.next();
         result.replaceBody(newValue);
      }

   }

   protected void appendNode(Object newValue) {
      Iterator iter = this.iterator();

      while(iter.hasNext()) {
         NodeChild result = (NodeChild)iter.next();
         result.appendNode(newValue);
      }

   }
}
