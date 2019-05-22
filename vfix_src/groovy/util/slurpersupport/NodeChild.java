package groovy.util.slurpersupport;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class NodeChild extends GPathResult {
   private final Node node;

   public NodeChild(Node node, GPathResult parent, String namespacePrefix, Map<String, String> namespaceTagHints) {
      super(parent, node.name(), namespacePrefix, namespaceTagHints);
      this.node = node;
   }

   public NodeChild(Node node, GPathResult parent, Map<String, String> namespaceTagHints) {
      this(node, parent, "*", namespaceTagHints);
   }

   public int size() {
      return 1;
   }

   public String text() {
      return this.node.text();
   }

   public String namespaceURI() {
      return this.node.namespaceURI();
   }

   public GPathResult parents() {
      throw new GroovyRuntimeException("parents() not implemented yet");
   }

   public Iterator iterator() {
      return new Iterator() {
         private boolean hasNext = true;

         public boolean hasNext() {
            return this.hasNext;
         }

         public Object next() {
            NodeChild var1;
            try {
               var1 = this.hasNext ? NodeChild.this : null;
            } finally {
               this.hasNext = false;
            }

            return var1;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Iterator nodeIterator() {
      return new Iterator() {
         private boolean hasNext = true;

         public boolean hasNext() {
            return this.hasNext;
         }

         public Object next() {
            Node var1;
            try {
               var1 = this.hasNext ? NodeChild.this.node : null;
            } finally {
               this.hasNext = false;
            }

            return var1;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Object getAt(int index) {
      if (index == 0) {
         return this.node;
      } else {
         throw new ArrayIndexOutOfBoundsException(index);
      }
   }

   public Map attributes() {
      return this.node.attributes();
   }

   public Iterator childNodes() {
      return this.node.childNodes();
   }

   public GPathResult find(Closure closure) {
      return (GPathResult)(DefaultTypeTransformation.castToBoolean(closure.call(new Object[]{this.node})) ? this : new NoChildren(this, "", this.namespaceTagHints));
   }

   public GPathResult findAll(Closure closure) {
      return this.find(closure);
   }

   public void build(GroovyObject builder) {
      this.node.build(builder, this.namespaceMap, this.namespaceTagHints);
   }

   public Writer writeTo(Writer out) throws IOException {
      return this.node.writeTo(out);
   }

   protected void replaceNode(Closure newValue) {
      this.node.replaceNode(newValue, this);
   }

   protected void replaceBody(Object newValue) {
      this.node.replaceBody(newValue);
   }

   protected void appendNode(Object newValue) {
      this.node.appendNode(newValue, this);
   }
}
