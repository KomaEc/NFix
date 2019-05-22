package groovy.util.slurpersupport;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyRuntimeException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

public class NoChildren extends GPathResult {
   public NoChildren(GPathResult parent, String name, Map<String, String> namespaceTagHints) {
      super(parent, name, "*", namespaceTagHints);
   }

   public int size() {
      return 0;
   }

   public String text() {
      return "";
   }

   public GPathResult parents() {
      throw new GroovyRuntimeException("parents() not implemented yet");
   }

   public Iterator childNodes() {
      return this.iterator();
   }

   public Iterator iterator() {
      return new Iterator() {
         public boolean hasNext() {
            return false;
         }

         public Object next() {
            return null;
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public GPathResult find(Closure closure) {
      return this;
   }

   public GPathResult findAll(Closure closure) {
      return this;
   }

   public Iterator nodeIterator() {
      return this.iterator();
   }

   public Writer writeTo(Writer out) throws IOException {
      return out;
   }

   public void build(GroovyObject builder) {
   }

   protected void replaceNode(Closure newValue) {
   }

   protected void replaceBody(Object newValue) {
   }

   protected void appendNode(Object newValue) {
   }

   public boolean asBoolean() {
      return false;
   }
}
