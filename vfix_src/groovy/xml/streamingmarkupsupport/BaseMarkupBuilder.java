package groovy.xml.streamingmarkupsupport;

import groovy.lang.Closure;
import groovy.lang.GroovyInterceptable;
import groovy.lang.GroovyObjectSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseMarkupBuilder extends Builder {
   public BaseMarkupBuilder(Map namespaceMethodMap) {
      super(namespaceMethodMap);
   }

   public Object bind(Closure root) {
      return new BaseMarkupBuilder.Document(root, this.namespaceMethodMap);
   }

   private static class Document extends Builder.Built implements GroovyInterceptable {
      private Object out;
      private final Map pendingNamespaces = new HashMap();
      private final Map namespaces = new HashMap();
      private final Map specialProperties = new HashMap();
      private String prefix = "";

      public Document(Closure root, Map namespaceMethodMap) {
         super(root, namespaceMethodMap);
         this.namespaces.put("xml", "http://www.w3.org/XML/1998/namespace");
         this.namespaces.put("mkp", "http://www.codehaus.org/Groovy/markup/keywords");
         this.specialProperties.put("out", new BaseMarkupBuilder.Document.OutputSink("out") {
            public Object leftShift(Object value) {
               return this.leftShift("yield", value);
            }
         });
         this.specialProperties.put("unescaped", new BaseMarkupBuilder.Document.OutputSink("unescaped") {
            public Object leftShift(Object value) {
               return this.leftShift("yieldUnescaped", value);
            }
         });
         this.specialProperties.put("namespaces", new BaseMarkupBuilder.Document.OutputSink("namespaces") {
            public Object leftShift(Object value) {
               return this.leftShift("declareNamespace", value);
            }
         });
         this.specialProperties.put("pi", new BaseMarkupBuilder.Document.OutputSink("pi") {
            public Object leftShift(Object value) {
               return this.leftShift("pi", value);
            }
         });
         this.specialProperties.put("comment", new BaseMarkupBuilder.Document.OutputSink("comment") {
            public Object leftShift(Object value) {
               return this.leftShift("comment", value);
            }
         });
      }

      public Object invokeMethod(String name, Object args) {
         Object[] arguments = (Object[])((Object[])args);
         Map attrs = Collections.EMPTY_MAP;
         Object body = null;

         for(int i = 0; i != arguments.length; ++i) {
            Object arg = arguments[i];
            if (arg instanceof Map) {
               attrs = (Map)arg;
            } else if (arg instanceof Closure) {
               Closure c = (Closure)arg;
               c.setDelegate(this);
               body = c.asWritable();
            } else {
               body = arg;
            }
         }

         Object uri;
         if (this.pendingNamespaces.containsKey(this.prefix)) {
            uri = this.pendingNamespaces.get(this.prefix);
         } else if (this.namespaces.containsKey(this.prefix)) {
            uri = this.namespaces.get(this.prefix);
         } else {
            uri = ":";
         }

         Object[] info = (Object[])((Object[])this.namespaceSpecificTags.get(uri));
         Map tagMap = (Map)info[2];
         Closure defaultTagClosure = (Closure)info[0];
         String prefix = this.prefix;
         this.prefix = "";
         if (tagMap.containsKey(name)) {
            return ((Closure)tagMap.get(name)).call(new Object[]{this, this.pendingNamespaces, this.namespaces, this.namespaceSpecificTags, prefix, attrs, body, this.out});
         } else {
            return defaultTagClosure.call(new Object[]{name, this, this.pendingNamespaces, this.namespaces, this.namespaceSpecificTags, prefix, attrs, body, this.out});
         }
      }

      public Object getProperty(String property) {
         Object special = this.specialProperties.get(property);
         if (special == null) {
            this.prefix = property;
            return this;
         } else {
            return special;
         }
      }

      public void setProperty(String property, Object newValue) {
         if ("trigger".equals(property)) {
            this.out = newValue;
            this.root.call((Object)this);
         } else {
            super.setProperty(property, newValue);
         }

      }

      private abstract class OutputSink extends GroovyObjectSupport {
         private final String name;

         public OutputSink(String name) {
            this.name = name;
         }

         public Object invokeMethod(String name, Object args) {
            Document.this.prefix = this.name;
            return Document.this.invokeMethod(name, args);
         }

         public abstract Object leftShift(Object var1);

         protected Object leftShift(String command, Object value) {
            Document.this.getProperty("mkp");
            Document.this.invokeMethod(command, new Object[]{value});
            return this;
         }
      }
   }
}
