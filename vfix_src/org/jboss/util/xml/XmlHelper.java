package org.jboss.util.xml;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlHelper {
   public static void write(Writer out, Document dom) throws Exception {
      (new DOMWriter(out)).setPrettyprint(true).print(dom);
   }

   public static Iterator getChildrenByTagName(Element element, String tagName) {
      if (element == null) {
         return null;
      } else {
         NodeList children = element.getChildNodes();
         ArrayList goodChildren = new ArrayList();

         for(int i = 0; i < children.getLength(); ++i) {
            Node currentChild = children.item(i);
            if (currentChild.getNodeType() == 1 && ((Element)currentChild).getTagName().equals(tagName)) {
               goodChildren.add(currentChild);
            }
         }

         return goodChildren.iterator();
      }
   }

   public static Element getUniqueChild(Element element, String tagName) throws Exception {
      Iterator goodChildren = getChildrenByTagName(element, tagName);
      if (goodChildren != null && goodChildren.hasNext()) {
         Element child = (Element)goodChildren.next();
         if (goodChildren.hasNext()) {
            throw new Exception("expected only one " + tagName + " tag");
         } else {
            return child;
         }
      } else {
         throw new Exception("expected one " + tagName + " tag");
      }
   }

   public static Element getOptionalChild(Element element, String tagName) throws Exception {
      return getOptionalChild(element, tagName, (Element)null);
   }

   public static Element getOptionalChild(Element element, String tagName, Element defaultElement) throws Exception {
      Iterator goodChildren = getChildrenByTagName(element, tagName);
      if (goodChildren != null && goodChildren.hasNext()) {
         Element child = (Element)goodChildren.next();
         if (goodChildren.hasNext()) {
            throw new Exception("expected only one " + tagName + " tag");
         } else {
            return child;
         }
      } else {
         return defaultElement;
      }
   }

   public static String getElementContent(Element element) throws Exception {
      return getElementContent(element, (String)null);
   }

   public static String getElementContent(Element element, String defaultStr) throws Exception {
      if (element == null) {
         return defaultStr;
      } else {
         NodeList children = element.getChildNodes();
         String result = "";

         for(int i = 0; i < children.getLength(); ++i) {
            if (children.item(i).getNodeType() != 3 && children.item(i).getNodeType() != 4) {
               if (children.item(i).getNodeType() == 8) {
               }
            } else {
               result = result + children.item(i).getNodeValue();
            }
         }

         return result.trim();
      }
   }

   public static String getUniqueChildContent(Element element, String tagName) throws Exception {
      return getElementContent(getUniqueChild(element, tagName));
   }

   public static String getOptionalChildContent(Element element, String tagName) throws Exception {
      return getElementContent(getOptionalChild(element, tagName));
   }

   public static boolean getOptionalChildBooleanContent(Element element, String name) throws Exception {
      Element child = getOptionalChild(element, name);
      if (child == null) {
         return false;
      } else {
         String value = getElementContent(child).toLowerCase();
         return value.equals("true") || value.equals("yes");
      }
   }
}
