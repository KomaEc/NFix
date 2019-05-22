package groovy.util;

import groovy.xml.QName;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.runtime.InvokerHelper;

public class XmlNodePrinter {
   protected final IndentPrinter out;
   private String quote;
   private boolean namespaceAware;
   private boolean preserveWhitespace;

   public XmlNodePrinter(PrintWriter out) {
      this(out, "  ");
   }

   public XmlNodePrinter(PrintWriter out, String indent) {
      this(out, indent, "\"");
   }

   public XmlNodePrinter(PrintWriter out, String indent, String quote) {
      this(new IndentPrinter(out, indent), quote);
   }

   public XmlNodePrinter(IndentPrinter out) {
      this(out, "\"");
   }

   public XmlNodePrinter(IndentPrinter out, String quote) {
      this.namespaceAware = true;
      this.preserveWhitespace = false;
      if (out == null) {
         throw new IllegalArgumentException("Argument 'IndentPrinter out' must not be null!");
      } else {
         this.out = out;
         this.quote = quote;
      }
   }

   public XmlNodePrinter() {
      this(new PrintWriter(new OutputStreamWriter(System.out)));
   }

   public void print(Node node) {
      this.print(node, new XmlNodePrinter.NamespaceContext());
   }

   public boolean isNamespaceAware() {
      return this.namespaceAware;
   }

   public void setNamespaceAware(boolean namespaceAware) {
      this.namespaceAware = namespaceAware;
   }

   public boolean isPreserveWhitespace() {
      return this.preserveWhitespace;
   }

   public void setPreserveWhitespace(boolean preserveWhitespace) {
      this.preserveWhitespace = preserveWhitespace;
   }

   public String getQuote() {
      return this.quote;
   }

   public void setQuote(String quote) {
      this.quote = quote;
   }

   protected void print(Node node, XmlNodePrinter.NamespaceContext ctx) {
      if (this.isEmptyElement(node)) {
         this.printLineBegin();
         this.out.print("<");
         this.out.print(this.getName(node));
         if (ctx != null) {
            this.printNamespace(node, ctx);
         }

         this.printNameAttributes(node.attributes(), ctx);
         this.out.print("/>");
         this.printLineEnd();
         this.out.flush();
      } else if (this.printSpecialNode(node)) {
         this.out.flush();
      } else {
         Object value = node.value();
         if (value instanceof List) {
            this.printName(node, ctx, true, this.isListOfSimple((List)value));
            this.printList((List)value, ctx);
            this.printName(node, ctx, false, this.isListOfSimple((List)value));
            this.out.flush();
         } else {
            this.printName(node, ctx, true, this.preserveWhitespace);
            this.printSimpleItemWithIndent(value);
            this.printName(node, ctx, false, this.preserveWhitespace);
            this.out.flush();
         }
      }
   }

   private boolean isListOfSimple(List value) {
      Iterator i$ = value.iterator();

      Object p;
      do {
         if (!i$.hasNext()) {
            return this.preserveWhitespace;
         }

         p = i$.next();
      } while(!(p instanceof Node));

      return false;
   }

   protected void printLineBegin() {
      this.out.printIndent();
   }

   protected void printLineEnd() {
      this.printLineEnd((String)null);
   }

   protected void printLineEnd(String comment) {
      if (comment != null) {
         this.out.print(" <!-- ");
         this.out.print(comment);
         this.out.print(" -->");
      }

      this.out.println();
      this.out.flush();
   }

   protected void printList(List list, XmlNodePrinter.NamespaceContext ctx) {
      this.out.incrementIndent();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         XmlNodePrinter.NamespaceContext context = new XmlNodePrinter.NamespaceContext(ctx);
         if (value instanceof Node) {
            this.print((Node)value, context);
         } else {
            this.printSimpleItem(value);
         }
      }

      this.out.decrementIndent();
   }

   protected void printSimpleItem(Object value) {
      if (!this.preserveWhitespace) {
         this.printLineBegin();
      }

      this.printEscaped(InvokerHelper.toString(value), false);
      if (!this.preserveWhitespace) {
         this.printLineEnd();
      }

   }

   protected void printName(Node node, XmlNodePrinter.NamespaceContext ctx, boolean begin, boolean preserve) {
      if (node == null) {
         throw new NullPointerException("Node must not be null.");
      } else {
         Object name = node.name();
         if (name == null) {
            throw new NullPointerException("Name must not be null.");
         } else {
            if (!preserve || begin) {
               this.printLineBegin();
            }

            this.out.print("<");
            if (!begin) {
               this.out.print("/");
            }

            this.out.print(this.getName(node));
            if (ctx != null) {
               this.printNamespace(node, ctx);
            }

            if (begin) {
               this.printNameAttributes(node.attributes(), ctx);
            }

            this.out.print(">");
            if (!preserve || !begin) {
               this.printLineEnd();
            }

         }
      }
   }

   protected boolean printSpecialNode(Node node) {
      return false;
   }

   protected void printNamespace(Object object, XmlNodePrinter.NamespaceContext ctx) {
      if (this.namespaceAware) {
         if (object instanceof Node) {
            this.printNamespace(((Node)object).name(), ctx);
         } else if (object instanceof QName) {
            QName qname = (QName)object;
            String namespaceUri = qname.getNamespaceURI();
            if (namespaceUri != null) {
               String prefix = qname.getPrefix();
               if (!ctx.isPrefixRegistered(prefix, namespaceUri)) {
                  ctx.registerNamespacePrefix(prefix, namespaceUri);
                  this.out.print(" ");
                  this.out.print("xmlns");
                  if (prefix.length() > 0) {
                     this.out.print(":");
                     this.out.print(prefix);
                  }

                  this.out.print("=" + this.quote);
                  this.out.print(namespaceUri);
                  this.out.print(this.quote);
               }
            }
         }
      }

   }

   protected void printNameAttributes(Map attributes, XmlNodePrinter.NamespaceContext ctx) {
      if (attributes != null && !attributes.isEmpty()) {
         Iterator i$ = attributes.entrySet().iterator();

         while(i$.hasNext()) {
            Object p = i$.next();
            Entry entry = (Entry)p;
            this.out.print(" ");
            this.out.print(this.getName(entry.getKey()));
            this.out.print("=");
            Object value = entry.getValue();
            this.out.print(this.quote);
            if (value instanceof String) {
               this.printEscaped((String)value, true);
            } else {
               this.printEscaped(InvokerHelper.toString(value), true);
            }

            this.out.print(this.quote);
            this.printNamespace(entry.getKey(), ctx);
         }

      }
   }

   private boolean isEmptyElement(Node node) {
      if (node == null) {
         throw new IllegalArgumentException("Node must not be null!");
      } else if (!node.children().isEmpty()) {
         return false;
      } else {
         return node.text().length() == 0;
      }
   }

   private String getName(Object object) {
      if (object instanceof String) {
         return (String)object;
      } else if (object instanceof QName) {
         QName qname = (QName)object;
         return !this.namespaceAware ? qname.getLocalPart() : qname.getQualifiedName();
      } else if (object instanceof Node) {
         Object name = ((Node)object).name();
         return this.getName(name);
      } else {
         return object.toString();
      }
   }

   private void printSimpleItemWithIndent(Object value) {
      if (!this.preserveWhitespace) {
         this.out.incrementIndent();
      }

      this.printSimpleItem(value);
      if (!this.preserveWhitespace) {
         this.out.decrementIndent();
      }

   }

   private void printEscaped(String s, boolean isAttributeValue) {
      for(int i = 0; i < s.length(); ++i) {
         char c = s.charAt(i);
         switch(c) {
         case '\n':
            if (isAttributeValue) {
               this.out.print("&#10;");
            } else {
               this.out.print(c);
            }
            break;
         case '\r':
            if (isAttributeValue) {
               this.out.print("&#13;");
            } else {
               this.out.print(c);
            }
            break;
         case '"':
            if (this.quote.equals("\"")) {
               this.out.print("&quot;");
            } else {
               this.out.print(c);
            }
            break;
         case '&':
            this.out.print("&amp;");
            break;
         case '\'':
            if (this.quote.equals("'")) {
               this.out.print("&apos;");
            } else {
               this.out.print(c);
            }
            break;
         case '<':
            this.out.print("&lt;");
            break;
         case '>':
            this.out.print("&gt;");
            break;
         default:
            this.out.print(c);
         }
      }

   }

   protected class NamespaceContext {
      private final Map<String, String> namespaceMap;

      public NamespaceContext() {
         this.namespaceMap = new HashMap();
      }

      public NamespaceContext(XmlNodePrinter.NamespaceContext context) {
         this();
         this.namespaceMap.putAll(context.namespaceMap);
      }

      public boolean isPrefixRegistered(String prefix, String uri) {
         return this.namespaceMap.containsKey(prefix) && ((String)this.namespaceMap.get(prefix)).equals(uri);
      }

      public void registerNamespacePrefix(String prefix, String uri) {
         if (!this.isPrefixRegistered(prefix, uri)) {
            this.namespaceMap.put(prefix, uri);
         }

      }

      public String getNamespace(String prefix) {
         Object uri = this.namespaceMap.get(prefix);
         return uri == null ? null : uri.toString();
      }
   }
}
