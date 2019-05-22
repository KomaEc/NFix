package org.testng.xml.dom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomUtil {
   private XPath m_xpath;
   private Document m_document;

   public DomUtil(Document doc) {
      XPathFactory xpathFactory = XPathFactory.newInstance();
      this.m_xpath = xpathFactory.newXPath();
      this.m_document = doc;
   }

   public void populate(final XmlSuite xmlSuite) throws XPathExpressionException {
      NodeList nodes = this.m_document.getChildNodes();
      final Map<String, String> parameters = Maps.newHashMap();

      for(int i = 0; i < nodes.getLength(); ++i) {
         Node item1 = nodes.item(i);
         Map<String, DomUtil.NodeProcessor> map = Maps.newHashMap();
         map.put("parameter", new DomUtil.NodeProcessor() {
            public void process(Node node) {
               Element e = (Element)node;
               parameters.put(e.getAttribute("name"), e.getAttribute("value"));
            }
         });
         map.put("test", new DomUtil.NodeProcessor() {
            public void process(Node node) {
               XmlTest xmlTest = new XmlTest(xmlSuite);
               DomUtil.this.populateTest(xmlTest, node);
            }
         });
         map.put("suite-files", new DomUtil.NodeProcessor() {
            public void process(Node node) {
               NodeList item2Children = node.getChildNodes();
               List<String> suiteFiles = Lists.newArrayList();

               for(int k = 0; k < item2Children.getLength(); ++k) {
                  Node item3 = item2Children.item(k);
                  if (item3 instanceof Element) {
                     Element e = (Element)item3;
                     if ("suite-file".equals(item3.getNodeName())) {
                        suiteFiles.add(e.getAttribute("path"));
                     }
                  }
               }

               xmlSuite.setSuiteFiles(suiteFiles);
            }
         });
         this.parseNodeAndChildren("suite", item1, xmlSuite, map);
      }

      xmlSuite.setParameters(parameters);
   }

   private void parseNodeAndChildren(String name, Node root, Object object, Map<String, DomUtil.NodeProcessor> processors) throws XPathExpressionException {
      if (name.equals(root.getNodeName()) && root.getAttributes() != null) {
         this.populateAttributes(root, object);
         NodeList children = root.getChildNodes();

         for(int j = 0; j < children.getLength(); ++j) {
            Node item2 = children.item(j);
            String nodeName = item2.getNodeName();
            DomUtil.NodeProcessor proc = (DomUtil.NodeProcessor)processors.get(nodeName);
            if (proc != null) {
               proc.process(item2);
            } else if (!nodeName.startsWith("#")) {
               throw new RuntimeException("No processor found for " + nodeName);
            }
         }
      }

   }

   public static Iterator<Node> findChildren(Node node, String name) {
      List<Node> result = Lists.newArrayList();
      NodeList children = node.getChildNodes();

      for(int i = 0; i < children.getLength(); ++i) {
         Node n = children.item(i);
         if (name.equals(n.getNodeName())) {
            result.add(n);
         }
      }

      return result.iterator();
   }

   private void populateTest(XmlTest xmlTest, Node item) {
      Map<String, String> testParameters = Maps.newHashMap();
      this.populateAttributes(item, xmlTest);
      NodeList itemChildren = item.getChildNodes();

      for(int k = 0; k < itemChildren.getLength(); ++k) {
         Node item2 = itemChildren.item(k);
         if ("parameter".equals(item2.getNodeName())) {
            Element e = (Element)item2;
            testParameters.put(e.getAttribute("name"), e.getAttribute("value"));
         } else {
            NodeList item2Children;
            if ("classes".equals(item2.getNodeName())) {
               item2Children = item2.getChildNodes();

               for(int l = 0; l < item2Children.getLength(); ++l) {
                  Node item4 = item2Children.item(l);
                  if ("class".equals(item4.getNodeName())) {
                     XmlClass xmlClass = new XmlClass();
                     this.populateAttributes(item4, xmlClass);
                     xmlTest.getClasses().add(xmlClass);
                  }
               }
            } else if ("groups".equals(item2.getNodeName())) {
               item2Children = item2.getChildNodes();
               List<String> includes = Lists.newArrayList();
               List<String> excludes = Lists.newArrayList();

               for(int l = 0; l < item2Children.getLength(); ++l) {
                  Node item3 = item2Children.item(l);
                  NodeList item3Children;
                  int m;
                  Node item4;
                  if ("run".equals(item3.getNodeName())) {
                     item3Children = item3.getChildNodes();

                     for(m = 0; m < item3Children.getLength(); ++m) {
                        item4 = item3Children.item(m);
                        if ("include".equals(item4.getNodeName())) {
                           includes.add(((Element)item4).getAttribute("name"));
                        } else if ("exclude".equals(item4.getNodeName())) {
                           excludes.add(((Element)item4).getAttribute("name"));
                        }
                     }
                  } else if ("dependencies".equals(item3.getNodeName())) {
                     item3Children = item3.getChildNodes();

                     for(m = 0; m < item3Children.getLength(); ++m) {
                        item4 = item3Children.item(m);
                        if ("group".equals(item4.getNodeName())) {
                           Element e = (Element)item4;
                           xmlTest.addXmlDependencyGroup(e.getAttribute("name"), e.getAttribute("depends-on"));
                        }
                     }
                  } else if ("define".equals(item3.getNodeName())) {
                     this.xmlDefine(xmlTest, item3);
                  }
               }

               xmlTest.setIncludedGroups(includes);
               xmlTest.setExcludedGroups(excludes);
            }
         }
      }

      xmlTest.setParameters(testParameters);
   }

   private void xmlDefine(XmlTest xmlTest, Node item) {
      NodeList item3Children = item.getChildNodes();
      List<String> groups = Lists.newArrayList();

      for(int m = 0; m < item3Children.getLength(); ++m) {
         Node item4 = item3Children.item(m);
         if ("include".equals(item4.getNodeName())) {
            Element e = (Element)item4;
            groups.add(e.getAttribute("name"));
         }
      }

      xmlTest.addMetaGroup(((Element)item).getAttribute("name"), groups);
   }

   private void populateAttributes(Node node, Object object) {
      for(int j = 0; j < node.getAttributes().getLength(); ++j) {
         Node item = node.getAttributes().item(j);
         this.p(node.getAttributes().item(j).toString());
         this.setProperty(object, item.getLocalName(), item.getNodeValue());
      }

   }

   private void setProperty(Object object, String name, Object value) {
      String methodName = this.toCamelCaseSetter(name);
      Method foundMethod = null;
      Method[] arr$ = object.getClass().getDeclaredMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         if (m.getName().equals(methodName)) {
            foundMethod = m;
            break;
         }
      }

      if (foundMethod == null) {
         this.p("Warning: couldn't find setter method " + methodName);
      } else {
         try {
            this.p("Invoking " + methodName + " with " + value);
            Class<?> type = foundMethod.getParameterTypes()[0];
            if (type != Boolean.class && type != Boolean.TYPE) {
               if (type != Integer.class && type != Integer.TYPE) {
                  foundMethod.invoke(object, value.toString());
               } else {
                  foundMethod.invoke(object, Integer.parseInt(value.toString()));
               }
            } else {
               foundMethod.invoke(object, Boolean.parseBoolean(value.toString()));
            }
         } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException var10) {
            var10.printStackTrace();
         }
      }

   }

   private void p(String string) {
   }

   private String toCamelCaseSetter(String name) {
      StringBuilder result = new StringBuilder("set" + name.substring(0, 1).toUpperCase());

      for(int i = 1; i < name.length(); ++i) {
         if (name.charAt(i) == '-') {
            result.append(Character.toUpperCase(name.charAt(i + 1)));
            ++i;
         } else {
            result.append(name.charAt(i));
         }
      }

      return result.toString();
   }

   public interface NodeProcessor {
      void process(Node var1);
   }
}
