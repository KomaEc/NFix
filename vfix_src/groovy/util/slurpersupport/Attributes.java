package groovy.util.slurpersupport;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class Attributes extends NodeChildren {
   final String attributeName;

   public Attributes(GPathResult parent, String name, String namespacePrefix, Map<String, String> namespaceTagHints) {
      super(parent, name, namespacePrefix, namespaceTagHints);
      this.attributeName = this.name.substring(1);
   }

   public Attributes(GPathResult parent, String name, Map<String, String> namespaceTagHints) {
      this(parent, name, "*", namespaceTagHints);
   }

   public String name() {
      return this.name.substring(1);
   }

   public Iterator childNodes() {
      throw new GroovyRuntimeException("Can't get the child nodes on a GPath expression selecting attributes: ...." + this.parent.name() + "." + this.name() + ".childNodes()");
   }

   public Iterator iterator() {
      return new NodeIterator(this.nodeIterator()) {
         protected Object getNextNode(Iterator iter) {
            while(true) {
               if (iter.hasNext()) {
                  Object next = iter.next();
                  if (next instanceof Attribute) {
                     return next;
                  }

                  String value = (String)((Node)next).attributes().get(Attributes.this.attributeName);
                  if (value == null) {
                     continue;
                  }

                  return new Attribute(Attributes.this.name, value, new NodeChild((Node)next, Attributes.this.parent.parent, "", Attributes.this.namespaceTagHints), "", Attributes.this.namespaceTagHints);
               }

               return null;
            }
         }
      };
   }

   public Iterator nodeIterator() {
      return this.parent.nodeIterator();
   }

   public GPathResult parents() {
      return super.parents();
   }

   public String text() {
      StringBuffer buf = new StringBuffer();
      Iterator iter = this.iterator();

      while(iter.hasNext()) {
         buf.append(iter.next());
      }

      return buf.toString();
   }

   public List list() {
      Iterator iter = this.iterator();
      ArrayList result = new ArrayList();

      while(iter.hasNext()) {
         result.add(iter.next());
      }

      return result;
   }

   public GPathResult findAll(Closure closure) {
      return new FilteredAttributes(this, closure, this.namespaceTagHints);
   }

   public Writer writeTo(Writer out) throws IOException {
      out.write(this.text());
      return out;
   }

   public void build(GroovyObject builder) {
      builder.getProperty("mkp");
      builder.invokeMethod("yield", new Object[]{this.text()});
   }
}
