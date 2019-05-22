package org.jboss.util.xml;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMWriter {
   private PrintWriter out;
   private boolean canonical;
   private boolean prettyprint;
   private boolean writeXMLDeclaration;
   private boolean ignoreWhitespace;
   private String charsetName;
   private int prettyIndent;
   private boolean wroteXMLDeclaration;
   private Node rootNode;
   private boolean completeNamespaces = true;
   private String currentDefaultNamespace;

   public DOMWriter(Writer w) {
      this.out = new PrintWriter(w);
   }

   public DOMWriter(Writer w, String charsetName) {
      this.out = new PrintWriter(w);
      this.charsetName = charsetName;
      this.writeXMLDeclaration = true;
   }

   public DOMWriter(OutputStream stream) {
      try {
         this.out = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
      } catch (UnsupportedEncodingException var3) {
      }

   }

   public DOMWriter(OutputStream stream, String charsetName) {
      try {
         this.out = new PrintWriter(new OutputStreamWriter(stream, charsetName));
         this.charsetName = charsetName;
         this.writeXMLDeclaration = true;
      } catch (UnsupportedEncodingException var4) {
         throw new IllegalArgumentException("Unsupported encoding: " + charsetName);
      }
   }

   public static String printNode(Node node, boolean prettyprint) {
      StringWriter strw = new StringWriter();
      (new DOMWriter(strw)).setPrettyprint(prettyprint).print(node);
      return strw.toString();
   }

   public boolean isCanonical() {
      return this.canonical;
   }

   public DOMWriter setCanonical(boolean canonical) {
      this.canonical = canonical;
      return this;
   }

   public boolean isIgnoreWhitespace() {
      return this.ignoreWhitespace;
   }

   public DOMWriter setIgnoreWhitespace(boolean ignoreWhitespace) {
      this.ignoreWhitespace = ignoreWhitespace;
      return this;
   }

   public DOMWriter setCompleteNamespaces(boolean complete) {
      this.completeNamespaces = complete;
      return this;
   }

   public boolean isPrettyprint() {
      return this.prettyprint;
   }

   public DOMWriter setPrettyprint(boolean prettyprint) {
      this.prettyprint = prettyprint;
      return this;
   }

   public boolean isWriteXMLDeclaration() {
      return this.writeXMLDeclaration;
   }

   public DOMWriter setWriteXMLDeclaration(boolean flag) {
      this.writeXMLDeclaration = flag;
      return this;
   }

   public void print(Node node) {
      if (this.prettyprint && this.ignoreWhitespace) {
         throw new IllegalStateException("Cannot pretty print and ignore whitespace");
      } else {
         this.rootNode = node;
         this.printInternal(node, false);
      }
   }

   private void printInternal(Node node, boolean indentEndMarker) {
      if (node != null) {
         if (!this.wroteXMLDeclaration && this.writeXMLDeclaration && !this.canonical) {
            this.out.print("<?xml version='1.0'");
            if (this.charsetName != null) {
               this.out.print(" encoding='" + this.charsetName + "'");
            }

            this.out.print("?>");
            if (this.prettyprint) {
               this.out.println();
            }

            this.wroteXMLDeclaration = true;
         }

         int type = node.getNodeType();
         boolean hasChildNodes = node.getChildNodes().getLength() > 0;
         String nodeName = node.getNodeName();
         NodeList children;
         int len;
         int i;
         String data;
         switch(type) {
         case 1:
            Element element = (Element)node;
            if (this.prettyprint) {
               for(len = 0; len < this.prettyIndent; ++len) {
                  this.out.print(' ');
               }

               ++this.prettyIndent;
            }

            this.out.print('<');
            this.out.print(nodeName);
            Map nsMap = new HashMap();
            String elPrefix = node.getPrefix();
            String elNamespaceURI = node.getNamespaceURI();
            if (elPrefix != null) {
               String nsURI = this.getNamespaceURI(elPrefix, element, this.rootNode);
               nsMap.put(elPrefix, nsURI);
            }

            Attr[] attrs = this.sortAttributes(node.getAttributes());

            String atPrefix;
            for(int i = 0; i < attrs.length; ++i) {
               Attr attr = attrs[i];
               atPrefix = attr.getPrefix();
               String atName = attr.getNodeName();
               String atValue = normalize(attr.getNodeValue(), this.canonical);
               if (atName.equals("xmlns")) {
                  this.currentDefaultNamespace = atValue;
               }

               if (atPrefix != null && !atPrefix.equals("xmlns") && !atPrefix.equals("xml")) {
                  String nsURI = this.getNamespaceURI(atPrefix, element, this.rootNode);
                  nsMap.put(atPrefix, nsURI);
                  if (atName.equals(atPrefix + ":type") && atValue.indexOf(":") > 0) {
                     if (nsURI == null) {
                        nsURI = this.getNamespaceURI(atPrefix, element, (Node)null);
                     }

                     if ("http://www.w3.org/2001/XMLSchema-instance".equals(nsURI)) {
                        String typePrefix = atValue.substring(0, atValue.indexOf(":"));
                        String typeURI = this.getNamespaceURI(typePrefix, element, this.rootNode);
                        nsMap.put(typePrefix, typeURI);
                     }
                  }
               }

               this.out.print(" " + atName + "='" + atValue + "'");
            }

            if (this.completeNamespaces) {
               Iterator itPrefix = nsMap.keySet().iterator();

               while(itPrefix.hasNext()) {
                  String prefix = (String)itPrefix.next();
                  atPrefix = (String)nsMap.get(prefix);
                  if (atPrefix == null) {
                     atPrefix = this.getNamespaceURI(prefix, element, (Node)null);
                     this.out.print(" xmlns:" + prefix + "='" + atPrefix + "'");
                  }
               }
            }

            if (elPrefix == null && elNamespaceURI != null) {
               String defaultNamespace = element.getAttribute("xmlns");
               if (defaultNamespace.length() == 0 && !elNamespaceURI.equals(this.currentDefaultNamespace)) {
                  this.out.print(" xmlns='" + elNamespaceURI + "'");
                  this.currentDefaultNamespace = elNamespaceURI;
               }
            }

            if (hasChildNodes) {
               this.out.print('>');
            }

            indentEndMarker = this.isEndMarkerIndented(node);
            if (indentEndMarker) {
               this.out.print('\n');
            }

            NodeList childNodes = node.getChildNodes();
            int len = childNodes.getLength();

            for(int i = 0; i < len; ++i) {
               Node childNode = childNodes.item(i);
               this.printInternal(childNode, false);
            }
         case 2:
         case 6:
         default:
            break;
         case 3:
            data = normalize(node.getNodeValue(), this.canonical);
            if (data.trim().length() > 0) {
               this.out.print(data);
            } else if (!this.prettyprint && !this.ignoreWhitespace) {
               this.out.print(data);
            }
            break;
         case 4:
            if (this.canonical) {
               this.out.print(normalize(node.getNodeValue(), this.canonical));
            } else {
               this.out.print("<![CDATA[");
               this.out.print(node.getNodeValue());
               this.out.print("]]>");
            }
            break;
         case 5:
            if (this.canonical) {
               children = node.getChildNodes();
               if (children != null) {
                  len = children.getLength();

                  for(int i = 0; i < len; ++i) {
                     this.printInternal(children.item(i), false);
                  }
               }
            } else {
               this.out.print('&');
               this.out.print(nodeName);
               this.out.print(';');
            }
            break;
         case 7:
            this.out.print("<?");
            this.out.print(nodeName);
            data = node.getNodeValue();
            if (data != null && data.length() > 0) {
               this.out.print(' ');
               this.out.print(data);
            }

            this.out.print("?>");
            break;
         case 8:
            for(i = 0; i < this.prettyIndent; ++i) {
               this.out.print(' ');
            }

            this.out.print("<!--");
            data = node.getNodeValue();
            if (data != null) {
               this.out.print(data);
            }

            this.out.print("-->");
            if (this.prettyprint) {
               this.out.print('\n');
            }
            break;
         case 9:
            children = node.getChildNodes();

            for(len = 0; len < children.getLength(); ++len) {
               this.printInternal(children.item(len), false);
            }

            this.out.flush();
         }

         if (type == 1) {
            if (this.prettyprint) {
               --this.prettyIndent;
            }

            if (!hasChildNodes) {
               this.out.print("/>");
            } else {
               if (indentEndMarker) {
                  for(i = 0; i < this.prettyIndent; ++i) {
                     this.out.print(' ');
                  }
               }

               this.out.print("</");
               this.out.print(nodeName);
               this.out.print('>');
            }

            if (this.prettyIndent > 0) {
               this.out.print('\n');
            }
         }

         this.out.flush();
      }
   }

   private String getNamespaceURI(String prefix, Element element, Node stopNode) {
      Node parent = element.getParentNode();
      String nsURI = element.getAttribute("xmlns:" + prefix);
      if (nsURI.length() == 0 && element != stopNode && parent instanceof Element) {
         return this.getNamespaceURI(prefix, (Element)parent, stopNode);
      } else {
         return nsURI.length() > 0 ? nsURI : null;
      }
   }

   private boolean isEndMarkerIndented(Node node) {
      if (this.prettyprint) {
         NodeList childNodes = node.getChildNodes();
         int len = childNodes.getLength();

         for(int i = 0; i < len; ++i) {
            Node children = childNodes.item(i);
            if (children.getNodeType() == 1) {
               return true;
            }
         }
      }

      return false;
   }

   private Attr[] sortAttributes(NamedNodeMap attrs) {
      int len = attrs != null ? attrs.getLength() : 0;
      Attr[] array = new Attr[len];

      int i;
      for(i = 0; i < len; ++i) {
         array[i] = (Attr)attrs.item(i);
      }

      for(i = 0; i < len - 1; ++i) {
         String name = array[i].getNodeName();
         int index = i;

         for(int j = i + 1; j < len; ++j) {
            String curName = array[j].getNodeName();
            if (curName.compareTo(name) < 0) {
               name = curName;
               index = j;
            }
         }

         if (index != i) {
            Attr temp = array[i];
            array[i] = array[index];
            array[index] = temp;
         }
      }

      return array;
   }

   public static String normalize(String s, boolean canonical) {
      StringBuffer str = new StringBuffer();
      int len = s != null ? s.length() : 0;

      for(int i = 0; i < len; ++i) {
         char ch = s.charAt(i);
         switch(ch) {
         case '\n':
         case '\r':
            if (canonical) {
               str.append("&#");
               str.append(Integer.toString(ch));
               str.append(';');
               break;
            }
         default:
            str.append(ch);
            break;
         case '"':
            str.append("&quot;");
            break;
         case '&':
            str.append("&amp;");
            break;
         case '\'':
            str.append("&apos;");
            break;
         case '<':
            str.append("&lt;");
            break;
         case '>':
            str.append("&gt;");
         }
      }

      return str.toString();
   }
}
