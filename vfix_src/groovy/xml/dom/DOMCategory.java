package groovy.xml.dom;

import groovy.lang.Closure;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.IntRange;
import groovy.xml.DOMBuilder;
import groovy.xml.QName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.XmlGroovyMethods;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class DOMCategory {
   private static boolean trimWhitespace = true;

   public static Object get(Element element, String elementName) {
      return xgetAt(element, elementName);
   }

   public static Object get(NodeList nodeList, String elementName) {
      return nodeList instanceof Element ? xgetAt((Element)nodeList, elementName) : xgetAt(nodeList, elementName);
   }

   public static Object get(NamedNodeMap nodeMap, String elementName) {
      return xgetAt(nodeMap, elementName);
   }

   private static Object xgetAt(Element element, String elementName) {
      if ("..".equals(elementName)) {
         return parent(element);
      } else if ("**".equals(elementName)) {
         return depthFirst(element);
      } else {
         return elementName.startsWith("@") ? element.getAttribute(elementName.substring(1)) : getChildElements(element, elementName);
      }
   }

   private static Object xgetAt(NodeList nodeList, String elementName) {
      List results = new ArrayList();

      for(int i = 0; i < nodeList.getLength(); ++i) {
         Node node = nodeList.item(i);
         if (node instanceof Element) {
            addResult(results, get((Element)node, elementName));
         }
      }

      if (elementName.startsWith("@")) {
         return results;
      } else {
         return new DOMCategory.NodeListsHolder(results);
      }
   }

   public static NamedNodeMap attributes(Element element) {
      return element.getAttributes();
   }

   private static String xgetAt(NamedNodeMap namedNodeMap, String elementName) {
      Attr a = (Attr)namedNodeMap.getNamedItem(elementName);
      return a.getValue();
   }

   public static int size(NamedNodeMap namedNodeMap) {
      return namedNodeMap.getLength();
   }

   public static Node getAt(Node o, int i) {
      return nodeGetAt(o, i);
   }

   public static Node getAt(DOMCategory.NodeListsHolder o, int i) {
      return nodeGetAt(o, i);
   }

   public static Node getAt(DOMCategory.NodesHolder o, int i) {
      return nodeGetAt(o, i);
   }

   public static NodeList getAt(Node o, IntRange r) {
      return nodesGetAt(o, r);
   }

   public static NodeList getAt(DOMCategory.NodeListsHolder o, IntRange r) {
      return nodesGetAt(o, r);
   }

   public static NodeList getAt(DOMCategory.NodesHolder o, IntRange r) {
      return nodesGetAt(o, r);
   }

   private static Node nodeGetAt(Object o, int i) {
      if (o instanceof Element) {
         Node n = xgetAt((Element)o, i);
         if (n != null) {
            return n;
         }
      }

      return o instanceof NodeList ? xgetAt((NodeList)o, i) : null;
   }

   private static NodeList nodesGetAt(Object o, IntRange r) {
      if (o instanceof Element) {
         NodeList n = xgetAt((Element)o, r);
         if (n != null) {
            return n;
         }
      }

      return o instanceof NodeList ? xgetAt((NodeList)o, r) : null;
   }

   private static Node xgetAt(Element element, int i) {
      if (hasChildElements(element, "*")) {
         NodeList nodeList = getChildElements(element, "*");
         return xgetAt(nodeList, i);
      } else {
         return null;
      }
   }

   private static Node xgetAt(NodeList nodeList, int i) {
      if (i < 0) {
         i += nodeList.getLength();
      }

      return i >= 0 && i < nodeList.getLength() ? nodeList.item(i) : null;
   }

   private static NodeList xgetAt(Element element, IntRange r) {
      if (hasChildElements(element, "*")) {
         NodeList nodeList = getChildElements(element, "*");
         return xgetAt(nodeList, r);
      } else {
         return null;
      }
   }

   private static NodeList xgetAt(NodeList nodeList, IntRange r) {
      int from = r.getFromInt();
      int to = r.getToInt();
      if (from == to) {
         return new DOMCategory.NodesHolder(Arrays.asList(xgetAt(nodeList, from)));
      } else {
         if (from < 0) {
            from += nodeList.getLength();
         }

         if (to < 0) {
            to += nodeList.getLength();
         }

         if (from > to) {
            r = r.isReverse() ? new IntRange(to, from) : new IntRange(from, to);
            from = r.getFromInt();
            to = r.getToInt();
         }

         List<Node> nodes = new ArrayList(to - from + 1);
         int i;
         if (r.isReverse()) {
            for(i = to; i >= from; --i) {
               nodes.add(nodeList.item(i));
            }
         } else {
            for(i = from; i <= to; ++i) {
               nodes.add(nodeList.item(i));
            }
         }

         return new DOMCategory.NodesHolder(nodes);
      }
   }

   public static String name(Element element) {
      return element.getNodeName();
   }

   public static Node parent(Node node) {
      return node.getParentNode();
   }

   public static String text(Node node) {
      if (node.getNodeType() == 3) {
         return node.getNodeValue();
      } else {
         return node.hasChildNodes() ? text(node.getChildNodes()) : "";
      }
   }

   public static String text(NodeList nodeList) {
      StringBuffer sb = new StringBuffer();

      for(int i = 0; i < nodeList.getLength(); ++i) {
         sb.append(text(nodeList.item(i)));
      }

      return sb.toString();
   }

   public static List list(NodeList self) {
      List answer = new ArrayList();
      Iterator it = XmlGroovyMethods.iterator(self);

      while(it.hasNext()) {
         answer.add(it.next());
      }

      return answer;
   }

   public static NodeList depthFirst(Element self) {
      List result = new ArrayList();
      result.add(createNodeList(self));
      result.add(self.getElementsByTagName("*"));
      return new DOMCategory.NodeListsHolder(result);
   }

   public static void setValue(Element self, String value) {
      self.getFirstChild().setNodeValue(value);
   }

   public static void putAt(Element self, String property, Object value) {
      if (property.startsWith("@")) {
         String attributeName = property.substring(1);
         Document doc = self.getOwnerDocument();
         Attr newAttr = doc.createAttribute(attributeName);
         newAttr.setValue(value.toString());
         self.setAttributeNode(newAttr);
      } else {
         InvokerHelper.setProperty(self, property, value);
      }
   }

   public static Element appendNode(Element self, Object name) {
      return appendNode(self, name, (String)null);
   }

   public static Element appendNode(Element self, Object name, Map attributes) {
      return appendNode(self, name, attributes, (String)null);
   }

   public static Element appendNode(Element self, Object name, String value) {
      Document doc = self.getOwnerDocument();
      Element newChild;
      if (name instanceof QName) {
         QName qn = (QName)name;
         newChild = doc.createElementNS(qn.getNamespaceURI(), qn.getQualifiedName());
      } else {
         newChild = doc.createElement(name.toString());
      }

      if (value != null) {
         Text text = doc.createTextNode(value);
         newChild.appendChild(text);
      }

      self.appendChild(newChild);
      return newChild;
   }

   public static Element appendNode(Element self, Object name, Map attributes, String value) {
      Element result = appendNode(self, name, value);
      Iterator i$ = attributes.entrySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         Entry e = (Entry)o;
         putAt(result, "@" + e.getKey().toString(), e.getValue());
      }

      return result;
   }

   public static Element replaceNode(DOMCategory.NodesHolder self, Closure c) {
      if (self.getLength() > 0 && self.getLength() <= 1) {
         return replaceNode((Element)self.item(0), c);
      } else {
         throw new GroovyRuntimeException("replaceNode() can only be used to replace a single element.");
      }
   }

   public static Element replaceNode(Element self, Closure c) {
      DOMBuilder b = new DOMBuilder(self.getOwnerDocument());
      Element newNode = (Element)b.invokeMethod("rootNode", c);

      Node n;
      for(n = newNode.getFirstChild(); n != null && n.getNodeType() != 1; n = n.getNextSibling()) {
      }

      if (n == null) {
         throw new GroovyRuntimeException("Replacement node must be an element.");
      } else {
         newNode = (Element)n;
         self.getParentNode().replaceChild(newNode, self);
         return newNode;
      }
   }

   public static void plus(Element self, Closure c) {
      Node parent = self.getParentNode();
      Node beforeNode = self.getNextSibling();
      DOMBuilder b = new DOMBuilder(self.getOwnerDocument());
      Element newNodes = (Element)b.invokeMethod("rootNode", c);
      Iterator iter = XmlGroovyMethods.iterator(children(newNodes));

      while(iter.hasNext()) {
         parent.insertBefore((Node)iter.next(), beforeNode);
      }

   }

   public static void plus(NodeList self, Closure c) {
      for(int i = 0; i < self.getLength(); ++i) {
         plus((Element)self.item(i), c);
      }

   }

   private static NodeList createNodeList(Element self) {
      List first = new ArrayList();
      first.add(self);
      return new DOMCategory.NodesHolder(first);
   }

   public static NodeList breadthFirst(Element self) {
      List result = new ArrayList();

      for(NodeList thisLevel = createNodeList(self); thisLevel.getLength() > 0; thisLevel = getNextLevel(thisLevel)) {
         result.add(thisLevel);
      }

      return new DOMCategory.NodeListsHolder(result);
   }

   private static NodeList getNextLevel(NodeList thisLevel) {
      List result = new ArrayList();

      for(int i = 0; i < thisLevel.getLength(); ++i) {
         Node n = thisLevel.item(i);
         if (n instanceof Element) {
            result.add(getChildElements((Element)n, "*"));
         }
      }

      return new DOMCategory.NodeListsHolder(result);
   }

   public static NodeList children(Element self) {
      return getChildElements(self, "*");
   }

   private static boolean hasChildElements(Element self, String elementName) {
      return getChildElements(self, elementName).getLength() > 0;
   }

   private static NodeList getChildElements(Element self, String elementName) {
      List result = new ArrayList();
      NodeList nodeList = self.getChildNodes();

      for(int i = 0; i < nodeList.getLength(); ++i) {
         Node node = nodeList.item(i);
         if (node.getNodeType() == 1) {
            Element child = (Element)node;
            if ("*".equals(elementName) || child.getTagName().equals(elementName)) {
               result.add(child);
            }
         } else if (node.getNodeType() == 3) {
            String value = node.getNodeValue();
            if (trimWhitespace) {
               value = value.trim();
            }

            if ("*".equals(elementName) && value.length() > 0) {
               node.setNodeValue(value);
               result.add(node);
            }
         }
      }

      return new DOMCategory.NodesHolder(result);
   }

   public static String toString(Object o) {
      if (o instanceof Node && ((Node)o).getNodeType() == 3) {
         return ((Node)o).getNodeValue();
      } else {
         return o instanceof NodeList ? toString((NodeList)o) : o.toString();
      }
   }

   public static Object xpath(Node self, String expression, javax.xml.namespace.QName returnType) {
      XPath xpath = XPathFactory.newInstance().newXPath();

      try {
         return xpath.evaluate(expression, self, returnType);
      } catch (XPathExpressionException var5) {
         throw new GroovyRuntimeException(var5);
      }
   }

   public static String xpath(Node self, String expression) {
      XPath xpath = XPathFactory.newInstance().newXPath();

      try {
         return xpath.evaluate(expression, self);
      } catch (XPathExpressionException var4) {
         throw new GroovyRuntimeException(var4);
      }
   }

   private static String toString(NodeList self) {
      StringBuffer sb = new StringBuffer();
      sb.append("[");

      for(Iterator it = XmlGroovyMethods.iterator(self); it.hasNext(); sb.append(it.next().toString())) {
         if (sb.length() > 1) {
            sb.append(", ");
         }
      }

      sb.append("]");
      return sb.toString();
   }

   public static int size(NodeList self) {
      return self.getLength();
   }

   public static boolean isEmpty(NodeList self) {
      return size(self) == 0;
   }

   private static void addResult(List results, Object result) {
      if (result != null) {
         if (result instanceof Collection) {
            results.addAll((Collection)result);
         } else {
            results.add(result);
         }
      }

   }

   private static final class NodesHolder implements NodeList {
      private List nodes;

      private NodesHolder(List nodes) {
         this.nodes = nodes;
      }

      public int getLength() {
         return this.nodes.size();
      }

      public Node item(int index) {
         return index >= 0 && index < this.getLength() ? (Node)this.nodes.get(index) : null;
      }

      // $FF: synthetic method
      NodesHolder(List x0, Object x1) {
         this(x0);
      }
   }

   private static final class NodeListsHolder implements NodeList {
      private List nodeLists;

      private NodeListsHolder(List nodeLists) {
         this.nodeLists = nodeLists;
      }

      public int getLength() {
         int length = 0;

         for(int i = 0; i < this.nodeLists.size(); ++i) {
            NodeList nl = (NodeList)this.nodeLists.get(i);
            length += nl.getLength();
         }

         return length;
      }

      public Node item(int index) {
         int relativeIndex = index;

         for(int i = 0; i < this.nodeLists.size(); ++i) {
            NodeList nl = (NodeList)this.nodeLists.get(i);
            if (relativeIndex < nl.getLength()) {
               return nl.item(relativeIndex);
            }

            relativeIndex -= nl.getLength();
         }

         return null;
      }

      public String toString() {
         return DOMCategory.toString((NodeList)this);
      }

      // $FF: synthetic method
      NodeListsHolder(List x0, Object x1) {
         this(x0);
      }
   }
}
