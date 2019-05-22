package groovy.xml;

import groovy.util.BuilderSupport;
import groovy.util.IndentPrinter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MarkupBuilder extends BuilderSupport {
   private IndentPrinter out;
   private boolean nospace;
   private int state;
   private boolean nodeIsEmpty;
   private boolean useDoubleQuotes;
   private boolean omitNullAttributes;
   private boolean omitEmptyAttributes;

   public MarkupBuilder() {
      this(new IndentPrinter());
   }

   public MarkupBuilder(PrintWriter pw) {
      this(new IndentPrinter(pw));
   }

   public MarkupBuilder(Writer writer) {
      this(new IndentPrinter(new PrintWriter(writer)));
   }

   public MarkupBuilder(IndentPrinter out) {
      this.nodeIsEmpty = true;
      this.useDoubleQuotes = false;
      this.omitNullAttributes = false;
      this.omitEmptyAttributes = false;
      this.out = out;
   }

   public boolean getDoubleQuotes() {
      return this.useDoubleQuotes;
   }

   public void setDoubleQuotes(boolean useDoubleQuotes) {
      this.useDoubleQuotes = useDoubleQuotes;
   }

   public boolean isOmitNullAttributes() {
      return this.omitNullAttributes;
   }

   public void setOmitNullAttributes(boolean omitNullAttributes) {
      this.omitNullAttributes = omitNullAttributes;
   }

   public boolean isOmitEmptyAttributes() {
      return this.omitEmptyAttributes;
   }

   public void setOmitEmptyAttributes(boolean omitEmptyAttributes) {
      this.omitEmptyAttributes = omitEmptyAttributes;
   }

   protected IndentPrinter getPrinter() {
      return this.out;
   }

   protected void setParent(Object parent, Object child) {
   }

   public Object getMkp() {
      return new MarkupBuilderHelper(this);
   }

   void pi(Map<String, Map<String, Object>> args) {
      Iterator<Entry<String, Map<String, Object>>> iterator = args.entrySet().iterator();
      if (iterator.hasNext()) {
         Entry<String, Map<String, Object>> mapEntry = (Entry)iterator.next();
         this.createNode("?" + (String)mapEntry.getKey(), (Map)((Map)mapEntry.getValue()));
         this.state = 2;
         this.out.println("?>");
      }

   }

   void yield(String value, boolean escaping) {
      if (this.state == 1) {
         this.state = 2;
         this.nodeIsEmpty = false;
         this.out.print(">");
      }

      if (this.state == 2 || this.state == 3) {
         this.out.print(escaping ? this.escapeElementContent(value) : value);
      }

   }

   protected Object createNode(Object name) {
      Object theName = this.getName(name);
      this.toState(1, theName);
      this.nodeIsEmpty = true;
      return theName;
   }

   protected Object createNode(Object name, Object value) {
      Object theName = this.getName(name);
      if (value == null) {
         return this.createNode(theName);
      } else {
         this.toState(2, theName);
         this.nodeIsEmpty = false;
         this.out.print(">");
         this.out.print(this.escapeElementContent(value.toString()));
         return theName;
      }
   }

   protected Object createNode(Object name, Map attributes, Object value) {
      Object theName = this.getName(name);
      this.toState(1, theName);
      Iterator i$ = attributes.entrySet().iterator();

      while(i$.hasNext()) {
         Object p = i$.next();
         Entry entry = (Entry)p;
         Object attributeValue = entry.getValue();
         boolean skipNull = attributeValue == null && this.omitNullAttributes;
         boolean skipEmpty = attributeValue != null && this.omitEmptyAttributes && attributeValue.toString().length() == 0;
         if (!skipNull && !skipEmpty) {
            this.out.print(" ");
            this.print(entry.getKey().toString());
            this.out.print(this.useDoubleQuotes ? "=\"" : "='");
            this.print(attributeValue == null ? "" : this.escapeAttributeValue(attributeValue.toString()));
            this.out.print(this.useDoubleQuotes ? "\"" : "'");
         }
      }

      if (value != null) {
         this.yield(value.toString(), true);
      } else {
         this.nodeIsEmpty = true;
      }

      return theName;
   }

   protected Object createNode(Object name, Map attributes) {
      return this.createNode(name, attributes, (Object)null);
   }

   protected void nodeCompleted(Object parent, Object node) {
      this.toState(3, node);
      this.out.flush();
   }

   protected void print(Object node) {
      this.out.print(node == null ? "null" : node.toString());
   }

   protected Object getName(String methodName) {
      return super.getName(methodName);
   }

   /** @deprecated */
   protected String transformValue(String value) {
      if (value.matches(".*&.*")) {
         value = value.replaceAll("&", "&amp;");
      }

      if (value.matches(".*\\'.*")) {
         value = value.replaceAll("'", "&apos;");
      }

      if (value.matches(".*<.*")) {
         value = value.replaceAll("<", "&lt;");
      }

      if (value.matches(".*>.*")) {
         value = value.replaceAll(">", "&gt;");
      }

      return value;
   }

   private String escapeAttributeValue(String value) {
      return this.escapeXmlValue(value, true);
   }

   private String escapeElementContent(String value) {
      return this.escapeXmlValue(value, false);
   }

   private String escapeXmlValue(String value, boolean isAttrValue) {
      if (value == null) {
         throw new IllegalArgumentException();
      } else {
         StringBuilder sb = null;
         int i = 0;

         for(int len = value.length(); i < len; ++i) {
            char ch = value.charAt(i);
            String replacement = this.checkForReplacement(isAttrValue, ch);
            if (replacement != null) {
               if (sb == null) {
                  sb = new StringBuilder((int)(1.1D * (double)len));
                  sb.append(value.substring(0, i));
               }

               sb.append(replacement);
            } else if (sb != null) {
               sb.append(ch);
            }
         }

         return sb == null ? value : sb.toString();
      }
   }

   private String checkForReplacement(boolean isAttrValue, char ch) {
      switch(ch) {
      case '"':
         if (isAttrValue && this.useDoubleQuotes) {
            return "&quot;";
         }
         break;
      case '&':
         return "&amp;";
      case '\'':
         if (isAttrValue && !this.useDoubleQuotes) {
            return "&apos;";
         }
         break;
      case '<':
         return "&lt;";
      case '>':
         return "&gt;";
      }

      return null;
   }

   private void toState(int next, Object name) {
      label54:
      switch(this.state) {
      case 0:
         switch(next) {
         case 1:
         case 2:
            this.out.print("<");
            this.print(name);
            break label54;
         case 3:
            throw new Error();
         default:
            break label54;
         }
      case 1:
         switch(next) {
         case 1:
         case 2:
            this.out.print(">");
            if (this.nospace) {
               this.nospace = false;
            } else {
               this.out.println();
               this.out.incrementIndent();
               this.out.printIndent();
            }

            this.out.print("<");
            this.print(name);
            break label54;
         case 3:
            if (this.nodeIsEmpty) {
               this.out.print(" />");
            }
         default:
            break label54;
         }
      case 2:
         switch(next) {
         case 1:
         case 2:
            if (!this.nodeIsEmpty) {
               this.out.println();
               this.out.incrementIndent();
               this.out.printIndent();
            }

            this.out.print("<");
            this.print(name);
            break label54;
         case 3:
            this.out.print("</");
            this.print(name);
            this.out.print(">");
         default:
            break label54;
         }
      case 3:
         switch(next) {
         case 1:
         case 2:
            if (this.nospace) {
               this.nospace = false;
            } else {
               this.out.println();
               this.out.printIndent();
            }

            this.out.print("<");
            this.print(name);
            break;
         case 3:
            if (this.nospace) {
               this.nospace = false;
            } else {
               this.out.println();
               this.out.decrementIndent();
               this.out.printIndent();
            }

            this.out.print("</");
            this.print(name);
            this.out.print(">");
         }
      }

      this.state = next;
   }

   private Object getName(Object name) {
      return name instanceof QName ? ((QName)name).getQualifiedName() : name;
   }
}
