package groovy.xml;

import groovy.util.BuilderSupport;
import groovy.util.NodeBuilder;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.runtime.InvokerHelper;

public class NamespaceBuilderSupport extends BuilderSupport {
   private boolean autoPrefix;
   private Map<String, String> nsMap;
   private BuilderSupport builder;

   public NamespaceBuilderSupport(BuilderSupport builder) {
      super(builder);
      this.nsMap = new HashMap();
      this.builder = builder;
   }

   public NamespaceBuilderSupport(BuilderSupport builder, String uri) {
      this(builder, uri, "");
   }

   public NamespaceBuilderSupport(BuilderSupport builder, String uri, String prefix) {
      this(builder, uri, prefix, true);
   }

   public NamespaceBuilderSupport(BuilderSupport builder, String uri, String prefix, boolean autoPrefix) {
      this(builder);
      this.nsMap.put(prefix, uri);
      this.autoPrefix = autoPrefix;
   }

   public NamespaceBuilderSupport(BuilderSupport builder, Map nsMap) {
      this(builder);
      this.nsMap = nsMap;
   }

   public NamespaceBuilderSupport namespace(String namespaceURI) {
      this.nsMap.put("", namespaceURI);
      return this;
   }

   public NamespaceBuilderSupport namespace(String namespaceURI, String prefix) {
      this.nsMap.put(prefix, namespaceURI);
      return this;
   }

   public NamespaceBuilderSupport declareNamespace(Map nsMap) {
      this.nsMap = nsMap;
      return this;
   }

   protected Object getCurrent() {
      return this.builder instanceof NodeBuilder ? InvokerHelper.invokeMethod(this.builder, "getCurrent", (Object)null) : super.getCurrent();
   }

   protected void setCurrent(Object current) {
      if (this.builder instanceof NodeBuilder) {
         InvokerHelper.invokeMethod(this.builder, "setCurrent", current);
      } else {
         super.setCurrent(current);
      }

   }

   protected void setParent(Object parent, Object child) {
   }

   protected Object getName(String methodName) {
      String prefix = this.autoPrefix ? (String)this.nsMap.keySet().iterator().next() : "";
      String localPart = methodName;
      int idx = methodName.indexOf(58);
      if (idx > 0) {
         prefix = methodName.substring(0, idx);
         localPart = methodName.substring(idx + 1);
      }

      String namespaceURI = (String)this.nsMap.get(prefix);
      if (namespaceURI == null) {
         namespaceURI = "";
         prefix = "";
      }

      return new QName(namespaceURI, localPart, prefix);
   }

   protected Object createNode(Object name) {
      return name;
   }

   protected Object createNode(Object name, Object value) {
      return name;
   }

   protected Object createNode(Object name, Map attributes) {
      return name;
   }

   protected Object createNode(Object name, Map attributes, Object value) {
      return name;
   }
}
