package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DOMElementWriter {
   private static final String NS = "ns";
   private boolean xmlDeclaration;
   private DOMElementWriter.XmlNamespacePolicy namespacePolicy;
   private HashMap nsPrefixMap;
   private int nextPrefix;
   private HashMap nsURIByElement;
   private static String lSep = System.getProperty("line.separator");
   protected String[] knownEntities;

   public DOMElementWriter() {
      this.xmlDeclaration = true;
      this.namespacePolicy = DOMElementWriter.XmlNamespacePolicy.IGNORE;
      this.nsPrefixMap = new HashMap();
      this.nextPrefix = 0;
      this.nsURIByElement = new HashMap();
      this.knownEntities = new String[]{"gt", "amp", "lt", "apos", "quot"};
   }

   public DOMElementWriter(boolean xmlDeclaration) {
      this(xmlDeclaration, DOMElementWriter.XmlNamespacePolicy.IGNORE);
   }

   public DOMElementWriter(boolean xmlDeclaration, DOMElementWriter.XmlNamespacePolicy namespacePolicy) {
      this.xmlDeclaration = true;
      this.namespacePolicy = DOMElementWriter.XmlNamespacePolicy.IGNORE;
      this.nsPrefixMap = new HashMap();
      this.nextPrefix = 0;
      this.nsURIByElement = new HashMap();
      this.knownEntities = new String[]{"gt", "amp", "lt", "apos", "quot"};
      this.xmlDeclaration = xmlDeclaration;
      this.namespacePolicy = namespacePolicy;
   }

   public void write(Element root, OutputStream out) throws IOException {
      Writer wri = new OutputStreamWriter(out, "UTF8");
      this.writeXMLDeclaration(wri);
      this.write(root, wri, 0, "  ");
      wri.flush();
   }

   public void writeXMLDeclaration(Writer wri) throws IOException {
      if (this.xmlDeclaration) {
         wri.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      }

   }

   public void write(Element element, Writer out, int indent, String indentWith) throws IOException {
      NodeList children = element.getChildNodes();
      boolean hasChildren = children.getLength() > 0;
      boolean hasChildElements = false;
      this.openElement(element, out, indent, indentWith, hasChildren);
      if (hasChildren) {
         for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            switch(child.getNodeType()) {
            case 1:
               hasChildElements = true;
               if (i == 0) {
                  out.write(lSep);
               }

               this.write((Element)child, out, indent + 1, indentWith);
            case 2:
            case 6:
            default:
               break;
            case 3:
               out.write(this.encode(child.getNodeValue()));
               break;
            case 4:
               out.write("<![CDATA[");
               out.write(this.encodedata(((Text)child).getData()));
               out.write("]]>");
               break;
            case 5:
               out.write(38);
               out.write(child.getNodeName());
               out.write(59);
               break;
            case 7:
               out.write("<?");
               out.write(child.getNodeName());
               String data = child.getNodeValue();
               if (data != null && data.length() > 0) {
                  out.write(32);
                  out.write(data);
               }

               out.write("?>");
               break;
            case 8:
               out.write("<!--");
               out.write(this.encode(child.getNodeValue()));
               out.write("-->");
            }
         }

         this.closeElement(element, out, indent, indentWith, hasChildElements);
      }

   }

   public void openElement(Element element, Writer out, int indent, String indentWith) throws IOException {
      this.openElement(element, out, indent, indentWith, true);
   }

   public void openElement(Element element, Writer out, int indent, String indentWith, boolean hasChildren) throws IOException {
      for(int i = 0; i < indent; ++i) {
         out.write(indentWith);
      }

      out.write("<");
      if (this.namespacePolicy.qualifyElements) {
         String uri = getNamespaceURI(element);
         String prefix = (String)this.nsPrefixMap.get(uri);
         if (prefix == null) {
            if (this.nsPrefixMap.isEmpty()) {
               prefix = "";
            } else {
               prefix = "ns" + this.nextPrefix++;
            }

            this.nsPrefixMap.put(uri, prefix);
            this.addNSDefinition(element, uri);
         }

         if (!"".equals(prefix)) {
            out.write(prefix);
            out.write(":");
         }
      }

      out.write(element.getTagName());
      NamedNodeMap attrs = element.getAttributes();

      String uri;
      String prefix;
      for(int i = 0; i < attrs.getLength(); ++i) {
         Attr attr = (Attr)attrs.item(i);
         out.write(" ");
         if (this.namespacePolicy.qualifyAttributes) {
            uri = getNamespaceURI(attr);
            prefix = (String)this.nsPrefixMap.get(uri);
            if (prefix == null) {
               prefix = "ns" + this.nextPrefix++;
               this.nsPrefixMap.put(uri, prefix);
               this.addNSDefinition(element, uri);
            }

            out.write(prefix);
            out.write(":");
         }

         out.write(attr.getName());
         out.write("=\"");
         out.write(this.encode(attr.getValue()));
         out.write("\"");
      }

      ArrayList al = (ArrayList)this.nsURIByElement.get(element);
      if (al != null) {
         Iterator iter = al.iterator();

         while(iter.hasNext()) {
            uri = (String)iter.next();
            prefix = (String)this.nsPrefixMap.get(uri);
            out.write(" xmlns");
            if (!"".equals(prefix)) {
               out.write(":");
               out.write(prefix);
            }

            out.write("=\"");
            out.write(uri);
            out.write("\"");
         }
      }

      if (hasChildren) {
         out.write(">");
      } else {
         this.removeNSDefinitions(element);
         out.write(" />");
         out.write(lSep);
         out.flush();
      }

   }

   public void closeElement(Element element, Writer out, int indent, String indentWith, boolean hasChildren) throws IOException {
      if (hasChildren) {
         for(int i = 0; i < indent; ++i) {
            out.write(indentWith);
         }
      }

      out.write("</");
      if (this.namespacePolicy.qualifyElements) {
         String uri = getNamespaceURI(element);
         String prefix = (String)this.nsPrefixMap.get(uri);
         if (prefix != null && !"".equals(prefix)) {
            out.write(prefix);
            out.write(":");
         }

         this.removeNSDefinitions(element);
      }

      out.write(element.getTagName());
      out.write(">");
      out.write(lSep);
      out.flush();
   }

   public String encode(String value) {
      StringBuffer sb = new StringBuffer();
      int len = value.length();

      for(int i = 0; i < len; ++i) {
         char c = value.charAt(i);
         switch(c) {
         case '"':
            sb.append("&quot;");
            break;
         case '&':
            int nextSemi = value.indexOf(";", i);
            if (nextSemi >= 0 && this.isReference(value.substring(i, nextSemi + 1))) {
               sb.append('&');
               break;
            }

            sb.append("&amp;");
            break;
         case '\'':
            sb.append("&apos;");
            break;
         case '<':
            sb.append("&lt;");
            break;
         case '>':
            sb.append("&gt;");
            break;
         default:
            if (this.isLegalCharacter(c)) {
               sb.append(c);
            }
         }
      }

      return sb.substring(0);
   }

   public String encodedata(String value) {
      StringBuffer sb = new StringBuffer();
      int len = value.length();

      for(int i = 0; i < len; ++i) {
         char c = value.charAt(i);
         if (this.isLegalCharacter(c)) {
            sb.append(c);
         }
      }

      String result = sb.substring(0);

      for(int cdEnd = result.indexOf("]]>"); cdEnd != -1; cdEnd = result.indexOf("]]>")) {
         sb.setLength(cdEnd);
         sb.append("&#x5d;&#x5d;&gt;").append(result.substring(cdEnd + 3));
         result = sb.substring(0);
      }

      return result;
   }

   public boolean isReference(String ent) {
      if (ent.charAt(0) == '&' && ent.endsWith(";")) {
         if (ent.charAt(1) == '#') {
            if (ent.charAt(2) == 'x') {
               try {
                  Integer.parseInt(ent.substring(3, ent.length() - 1), 16);
                  return true;
               } catch (NumberFormatException var4) {
                  return false;
               }
            } else {
               try {
                  Integer.parseInt(ent.substring(2, ent.length() - 1));
                  return true;
               } catch (NumberFormatException var5) {
                  return false;
               }
            }
         } else {
            String name = ent.substring(1, ent.length() - 1);

            for(int i = 0; i < this.knownEntities.length; ++i) {
               if (name.equals(this.knownEntities[i])) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isLegalCharacter(char c) {
      if (c != '\t' && c != '\n' && c != '\r') {
         if (c < ' ') {
            return false;
         } else if (c <= '\ud7ff') {
            return true;
         } else if (c < '\ue000') {
            return false;
         } else {
            return c <= '�';
         }
      } else {
         return true;
      }
   }

   private void removeNSDefinitions(Element element) {
      ArrayList al = (ArrayList)this.nsURIByElement.get(element);
      if (al != null) {
         Iterator iter = al.iterator();

         while(iter.hasNext()) {
            this.nsPrefixMap.remove(iter.next());
         }

         this.nsURIByElement.remove(element);
      }

   }

   private void addNSDefinition(Element element, String uri) {
      ArrayList al = (ArrayList)this.nsURIByElement.get(element);
      if (al == null) {
         al = new ArrayList();
         this.nsURIByElement.put(element, al);
      }

      al.add(uri);
   }

   private static String getNamespaceURI(Node n) {
      String uri = n.getNamespaceURI();
      if (uri == null) {
         uri = "";
      }

      return uri;
   }

   public static class XmlNamespacePolicy {
      private boolean qualifyElements;
      private boolean qualifyAttributes;
      public static final DOMElementWriter.XmlNamespacePolicy IGNORE = new DOMElementWriter.XmlNamespacePolicy(false, false);
      public static final DOMElementWriter.XmlNamespacePolicy ONLY_QUALIFY_ELEMENTS = new DOMElementWriter.XmlNamespacePolicy(true, false);
      public static final DOMElementWriter.XmlNamespacePolicy QUALIFY_ALL = new DOMElementWriter.XmlNamespacePolicy(true, true);

      public XmlNamespacePolicy(boolean qualifyElements, boolean qualifyAttributes) {
         this.qualifyElements = qualifyElements;
         this.qualifyAttributes = qualifyAttributes;
      }
   }
}
