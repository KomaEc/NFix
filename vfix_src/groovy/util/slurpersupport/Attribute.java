package groovy.util.slurpersupport;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class Attribute extends GPathResult {
   private final String value;

   public Attribute(String name, String value, GPathResult parent, String namespacePrefix, Map<String, String> namespaceTagHints) {
      super(parent, name, namespacePrefix, namespaceTagHints);
      this.value = value;
   }

   public String name() {
      return this.name.substring(1);
   }

   public int size() {
      return 1;
   }

   public String text() {
      return this.value;
   }

   public GPathResult parents() {
      throw new GroovyRuntimeException("parents() not implemented yet");
   }

   public Iterator childNodes() {
      throw new GroovyRuntimeException("can't call childNodes() in the attribute " + this.name);
   }

   public Iterator iterator() {
      return this.nodeIterator();
   }

   public GPathResult find(Closure closure) {
      return (GPathResult)(DefaultTypeTransformation.castToBoolean(closure.call(new Object[]{this})) ? this : new NoChildren(this, "", this.namespaceTagHints));
   }

   public GPathResult findAll(Closure closure) {
      return this.find(closure);
   }

   public Iterator nodeIterator() {
      return new Iterator() {
         private boolean hasNext = true;

         public boolean hasNext() {
            return this.hasNext;
         }

         public Object next() {
            Attribute var1;
            try {
               var1 = this.hasNext ? Attribute.this : null;
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

   public Writer writeTo(Writer out) throws IOException {
      out.write(this.value);
      return out;
   }

   public void build(GroovyObject builder) {
      builder.getProperty("mkp");
      builder.invokeMethod("yield", new Object[]{this.value});
   }

   protected void replaceNode(Closure newValue) {
   }

   protected void replaceBody(Object newValue) {
   }

   protected void appendNode(Object newValue) {
   }
}
