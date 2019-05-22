package org.testng.reporters;

import java.io.Writer;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Pattern;
import org.testng.internal.Nullable;

public class XMLStringBuffer {
   private static final String EOL = System.getProperty("line.separator", "\n");
   private static final String DEFAULT_INDENT_INCREMENT = "  ";
   private IBuffer m_buffer;
   private final Stack<Tag> m_tagStack = new Stack();
   private String m_currentIndent = "";
   private static final Pattern INVALID_XML_CHARS = Pattern.compile("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\ud800\udc00-\udbff\udfff]");

   public XMLStringBuffer() {
      this.init(Buffer.create(), "", "1.0", "UTF-8");
   }

   public XMLStringBuffer(String start) {
      this.init(Buffer.create(), start);
   }

   public XMLStringBuffer(IBuffer buffer, String start) {
      this.init(buffer, start);
   }

   private void init(IBuffer buffer, String start) {
      this.init(buffer, start, (String)null, (String)null);
   }

   private void init(IBuffer buffer, String start, @Nullable String version, @Nullable String encoding) {
      this.m_buffer = buffer;
      this.m_currentIndent = start;
      if (version != null) {
         this.setXmlDetails(version, encoding);
      }

   }

   public void setXmlDetails(String v, String enc) {
      if (this.m_buffer.toString().length() != 0) {
         throw new IllegalStateException("Buffer should be empty: '" + this.m_buffer.toString() + "'");
      } else {
         this.m_buffer.append("<?xml version=\"" + v + "\" encoding=\"" + enc + "\"?>").append(EOL);
      }
   }

   public void setDocType(String docType) {
      this.m_buffer.append("<!DOCTYPE " + docType + ">" + EOL);
   }

   public void push(String tagName, @Nullable String schema, @Nullable Properties attributes) {
      XMLUtils.xmlOpen(this.m_buffer, this.m_currentIndent, tagName + schema, attributes);
      this.m_tagStack.push(new Tag(this.m_currentIndent, tagName, attributes));
      this.m_currentIndent = this.m_currentIndent + "  ";
   }

   public void push(String tagName, @Nullable String schema) {
      this.push(tagName, schema, (Properties)null);
   }

   public void push(String tagName, @Nullable Properties attributes) {
      this.push(tagName, "", attributes);
   }

   public void push(String tagName, String... attributes) {
      this.push(tagName, this.createProperties(attributes));
   }

   private Properties createProperties(String[] attributes) {
      Properties result = new Properties();
      if (attributes == null) {
         return result;
      } else if (attributes.length % 2 != 0) {
         throw new IllegalArgumentException("Arguments 'attributes' length must be even. Actual: " + attributes.length);
      } else {
         for(int i = 0; i < attributes.length; i += 2) {
            result.put(attributes[i], attributes[i + 1]);
         }

         return result;
      }
   }

   public void push(String tagName) {
      this.push(tagName, "");
   }

   public void pop() {
      this.pop((String)null);
   }

   public void pop(String tagName) {
      this.m_currentIndent = this.m_currentIndent.substring("  ".length());
      Tag t = (Tag)this.m_tagStack.pop();
      if (null != tagName && !tagName.equals(t.tagName)) {
         throw new AssertionError("Popping the wrong tag: " + t.tagName + " but expected " + tagName);
      } else {
         XMLUtils.xmlClose(this.m_buffer, this.m_currentIndent, t.tagName, XMLUtils.extractComment(tagName, t.properties));
      }
   }

   public void addRequired(String tagName, @Nullable String value) {
      this.addRequired(tagName, value, (Properties)null);
   }

   public void addRequired(String tagName, @Nullable String value, @Nullable Properties attributes) {
      XMLUtils.xmlRequired(this.m_buffer, this.m_currentIndent, tagName, value, attributes);
   }

   public void addRequired(String tagName, @Nullable String value, String... attributes) {
      this.addRequired(tagName, value, this.createProperties(attributes));
   }

   public void addOptional(String tagName, @Nullable String value, @Nullable Properties attributes) {
      if (value != null) {
         XMLUtils.xmlOptional(this.m_buffer, this.m_currentIndent, tagName, value, attributes);
      }

   }

   public void addOptional(String tagName, @Nullable String value, String... attributes) {
      if (value != null) {
         XMLUtils.xmlOptional(this.m_buffer, this.m_currentIndent, tagName, value, this.createProperties(attributes));
      }

   }

   public void addOptional(String tagName, @Nullable String value) {
      this.addOptional(tagName, value, (Properties)null);
   }

   public void addOptional(String tagName, @Nullable Boolean value, @Nullable Properties attributes) {
      if (null != value) {
         XMLUtils.xmlOptional(this.m_buffer, this.m_currentIndent, tagName, value.toString(), attributes);
      }

   }

   public void addOptional(String tagName, @Nullable Boolean value) {
      this.addOptional(tagName, (Boolean)value, (Properties)null);
   }

   public void addEmptyElement(String tagName) {
      this.addEmptyElement(tagName, (Properties)null);
   }

   public void addEmptyElement(String tagName, @Nullable Properties attributes) {
      this.m_buffer.append(this.m_currentIndent).append("<").append(tagName);
      XMLUtils.appendAttributes(this.m_buffer, attributes);
      this.m_buffer.append("/>").append(EOL);
   }

   public void addEmptyElement(String tagName, String... attributes) {
      this.addEmptyElement(tagName, this.createProperties(attributes));
   }

   public void addComment(String comment) {
      this.m_buffer.append(this.m_currentIndent).append("<!-- " + comment.replaceAll("[-]{2,}", "-") + " -->\n");
   }

   public void addString(String s) {
      this.m_buffer.append(s);
   }

   private static void ppp(String s) {
      System.out.println("[XMLStringBuffer] " + s);
   }

   public void addCDATA(String content) {
      if (content == null) {
         content = "null";
      }

      if (content.contains("]]>")) {
         String[] subStrings = content.split("]]>");
         this.m_buffer.append(this.m_currentIndent).append("<![CDATA[").append(subStrings[0]).append("]]]]>");

         for(int i = 1; i < subStrings.length - 1; ++i) {
            this.m_buffer.append("<![CDATA[>").append(subStrings[i]).append("]]]]>");
         }

         this.m_buffer.append("<![CDATA[>").append(subStrings[subStrings.length - 1]).append("]]>");
         if (content.endsWith("]]>")) {
            this.m_buffer.append("<![CDATA[]]]]>").append("<![CDATA[>]]>");
         }

         this.m_buffer.append(EOL);
      } else {
         this.m_buffer.append(this.m_currentIndent).append("<![CDATA[").append(content).append("]]>" + EOL);
      }

   }

   public IBuffer getStringBuffer() {
      return this.m_buffer;
   }

   public String toXML() {
      return INVALID_XML_CHARS.matcher(this.m_buffer.toString()).replaceAll("");
   }

   public static void main(String[] argv) {
      IBuffer result = Buffer.create();
      XMLStringBuffer sb = new XMLStringBuffer(result, "");
      sb.push("family");
      Properties p = new Properties();
      p.setProperty("prop1", "value1");
      p.setProperty("prop2", "value2");
      sb.addRequired("cedric", "true", p);
      sb.addRequired("alois", "true");
      sb.addOptional("anne-marie", (String)null);
      sb.pop();
      System.out.println(result.toString());

      assert ("<family>" + EOL + "<cedric>true</cedric>" + EOL + "<alois>true</alois>" + EOL + "</family>" + EOL).equals(result.toString());

   }

   public String getCurrentIndent() {
      return this.m_currentIndent;
   }

   public void toWriter(Writer fw) {
      this.m_buffer.toWriter(fw);
   }
}
