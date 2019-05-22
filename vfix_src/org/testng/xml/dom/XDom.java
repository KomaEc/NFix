package org.testng.xml.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.testng.Assert;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.collections.Pair;
import org.testng.xml.XmlDefine;
import org.testng.xml.XmlGroups;
import org.testng.xml.XmlMethodSelector;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XDom {
   private Document m_document;
   private ITagFactory m_tagFactory;

   public XDom(ITagFactory tagFactory, Document document) throws XPathExpressionException, InstantiationException, IllegalAccessException {
      this.m_tagFactory = tagFactory;
      this.m_document = document;
   }

   public Object parse() throws XPathExpressionException, InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
      Object result = null;
      NodeList nodes = this.m_document.getChildNodes();

      for(int i = 0; i < nodes.getLength(); ++i) {
         Node item = nodes.item(i);
         if (item.getAttributes() != null) {
            String nodeName = item.getNodeName();
            System.out.println("Node name:" + nodeName);
            Class<?> c = this.m_tagFactory.getClassForTag(nodeName);
            if (c == null) {
               throw new RuntimeException("No class found for tag " + nodeName);
            }

            result = c.newInstance();
            this.populateAttributes(item, result);
            if (ITagSetter.class.isAssignableFrom(result.getClass())) {
               throw new RuntimeException("TAG SETTER");
            }

            this.populateChildren(item, result);
         }
      }

      return result;
   }

   public void populateChildren(Node root, Object result) throws InstantiationException, IllegalAccessException, XPathExpressionException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
      this.p("populateChildren: " + root.getLocalName());
      NodeList childNodes = root.getChildNodes();
      ListMultiMap<String, Object> children = Maps.newListMultiMap();

      for(int i = 0; i < childNodes.getLength(); ++i) {
         Node item = childNodes.item(i);
         if (item.getAttributes() != null) {
            String nodeName = item.getNodeName();
            if ("suite-files".equals(nodeName)) {
               System.out.println("BREAK");
            }

            Class<?> c = this.m_tagFactory.getClassForTag(nodeName);
            if (c == null) {
               System.out.println("Warning: No class found for tag " + nodeName);
               boolean foundSetter = this.invokeOnSetter(result, (Element)item, nodeName, (Object)null);
               System.out.println("  found setter:" + foundSetter);
            } else {
               Object object = this.instantiateElement(c, result);
               if (ITagSetter.class.isAssignableFrom(object.getClass())) {
                  System.out.println("Tag setter:" + result);
                  ((ITagSetter)object).setProperty(nodeName, result, item);
               } else {
                  children.put(nodeName, object);
                  this.populateAttributes(item, object);
                  this.populateContent(item, object);
               }

               this.invokeOnSetter(result, (Element)item, nodeName, object);
               this.populateChildren(item, object);
            }
         }
      }

   }

   private Object instantiateElement(Class<?> c, Object parent) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
      Object result = null;
      Method m = this.findMethodAnnotatedWith(c, ParentSetter.class);
      if (m != null) {
         result = c.newInstance();
         m.invoke(result, parent);
      } else {
         try {
            result = c.getConstructor(parent.getClass()).newInstance(parent);
         } catch (NoSuchMethodException var6) {
            result = c.newInstance();
         }
      }

      return result;
   }

   private Method findMethodAnnotatedWith(Class<?> c, Class<? extends Annotation> annotation) {
      Method[] arr$ = c.getMethods();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Method m = arr$[i$];
         if (m.getAnnotation(annotation) != null) {
            return m;
         }
      }

      return null;
   }

   private void populateContent(Node item, Object object) {
      for(int i = 0; i < item.getChildNodes().getLength(); ++i) {
         Node child = item.getChildNodes().item(i);
         if (child instanceof Text) {
            this.setText(object, (Text)child);
         }
      }

   }

   private void setText(Object bean, Text child) {
      List<Pair<Method, Wrapper>> pairs = Reflect.findMethodsWithAnnotation(bean.getClass(), TagContent.class, bean);
      Iterator i$ = pairs.iterator();

      while(i$.hasNext()) {
         Pair pair = (Pair)i$.next();

         try {
            ((Method)pair.first()).invoke(bean, child.getTextContent());
         } catch (InvocationTargetException | IllegalAccessException | DOMException | IllegalArgumentException var7) {
            var7.printStackTrace();
         }
      }

   }

   private boolean invokeOnSetter(Object object, Element element, String nodeName, Object bean) {
      Pair<Method, Wrapper> pair = Reflect.findSetterForTag(object.getClass(), nodeName, bean);
      List<Object[]> allParameters = null;
      if (pair != null) {
         Method m = (Method)pair.first();

         try {
            if (pair.second() != null) {
               allParameters = ((Wrapper)pair.second()).getParameters(element);
            } else {
               allParameters = Lists.newArrayList();
               allParameters.add(new Object[]{bean});
            }

            Iterator i$ = allParameters.iterator();

            while(i$.hasNext()) {
               Object[] p = (Object[])i$.next();
               m.invoke(object, p);
            }

            return true;
         } catch (IllegalArgumentException var10) {
            System.out.println("Parameters: " + allParameters);
            var10.printStackTrace();
         } catch (InvocationTargetException | IllegalAccessException var11) {
            var11.printStackTrace();
         }
      }

      return false;
   }

   private void populateAttributes(Node node, Object object) throws XPathExpressionException {
      for(int j = 0; j < node.getAttributes().getLength(); ++j) {
         Node item = node.getAttributes().item(j);
         this.setProperty(object, item.getLocalName(), item.getNodeValue());
      }

   }

   private void setProperty(Object object, String name, Object value) {
      Pair<Method, Wrapper> setter = Reflect.findSetterForTag(object.getClass(), name, value);
      if (setter != null) {
         Method foundMethod = (Method)setter.first();

         try {
            Class<?> type = foundMethod.getParameterTypes()[0];
            if (type != Boolean.class && type != Boolean.TYPE) {
               if (type != Integer.class && type != Integer.TYPE) {
                  foundMethod.invoke(object, value);
               } else {
                  foundMethod.invoke(object, Integer.parseInt(value.toString()));
               }
            } else {
               foundMethod.invoke(object, Boolean.parseBoolean(value.toString()));
            }
         } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException var7) {
            var7.printStackTrace();
         }
      } else {
         this.e("Couldn't find setter method for property" + name + " on " + object.getClass());
      }

   }

   private void p(String string) {
      System.out.println("[XDom] " + string);
   }

   private void e(String string) {
      System.out.println("[XDom] [Error] " + string);
   }

   public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      FileInputStream inputStream = new FileInputStream(new File(System.getProperty("user.home") + "/java/testng/src/test/resources/testng-all.xml"));
      Document doc = builder.parse((InputStream)inputStream);
      XmlSuite result = (XmlSuite)(new XDom(new TestNGTagFactory(), doc)).parse();
      test(result);
      System.out.println(result.toXml());
   }

   private static void test(XmlSuite s) {
      Assert.assertEquals("TestNG", s.getName());
      Assert.assertEquals((int)s.getDataProviderThreadCount(), (int)3);
      Assert.assertEquals((int)s.getThreadCount(), (int)2);
      List<XmlMethodSelector> suiteFiles = s.getMethodSelectors();
      Assert.assertEquals((int)suiteFiles.size(), (int)2);
      XmlMethodSelector s1 = (XmlMethodSelector)suiteFiles.get(0);
      Assert.assertEquals(s1.getLanguage(), "javascript");
      Assert.assertEquals(s1.getExpression(), "foo()");
      XmlMethodSelector s2 = (XmlMethodSelector)suiteFiles.get(1);
      Assert.assertEquals(s2.getClassName(), "SelectorClass");
      Assert.assertEquals((int)s2.getPriority(), (int)3);
      suiteFiles = s.getSuiteFiles();
      Assert.assertEquals((Collection)suiteFiles, (Collection)Arrays.asList("./junit-suite.xml"));
      Map<String, String> p = s.getParameters();
      Assert.assertEquals((int)p.size(), (int)2);
      Assert.assertEquals((String)p.get("suiteParameter"), "suiteParameterValue");
      Assert.assertEquals((String)p.get("first-name"), "Cedric");
      Assert.assertEquals((Collection)s.getIncludedGroups(), (Collection)Arrays.asList("includeThisGroup"));
      Assert.assertEquals((Collection)s.getExcludedGroups(), (Collection)Arrays.asList("excludeThisGroup"));
      XmlGroups groups = s.getGroups();
      List<XmlDefine> defines = groups.getDefines();
      Assert.assertEquals((int)defines.size(), (int)1);
      XmlDefine define = (XmlDefine)defines.get(0);
      Assert.assertEquals(define.getName(), "bigSuite");
      Assert.assertEquals((Collection)define.getIncludes(), (Collection)Arrays.asList("suite1", "suite2"));
      Assert.assertEquals((Collection)s.getPackageNames(), (Collection)Arrays.asList("com.example1", "com.example2"));
      Assert.assertEquals((Collection)s.getListeners(), (Collection)Arrays.asList("com.beust.Listener1", "com.beust.Listener2"));
      Assert.assertEquals((int)s.getTests().size(), (int)3);

      for(int i = 0; i < s.getTests().size(); ++i) {
         if ("Nopackage".equals(((XmlTest)s.getTests().get(i)).getName())) {
            testNoPackage((XmlTest)s.getTests().get(i));
         }
      }

   }

   private static void testNoPackage(XmlTest t) {
      Assert.assertEquals((int)t.getThreadCount(), (int)42);
      Assert.assertTrue(t.getAllowReturnValues());
      Map<String, List<String>> metaGroups = t.getMetaGroups();
      Assert.assertEquals((Collection)((Collection)metaGroups.get("evenodd")), (Collection)Arrays.asList("even", "odd"));
      Assert.assertEquals((Collection)t.getIncludedGroups(), (Collection)Arrays.asList("nopackage", "includeThisGroup"));
      Assert.assertEquals((Collection)t.getExcludedGroups(), (Collection)Arrays.asList("excludeThisGroup"));
      Map<String, String> dg = t.getXmlDependencyGroups();
      Assert.assertEquals((int)dg.size(), (int)2);
      Assert.assertEquals((String)dg.get("e"), "f");
      Assert.assertEquals((String)dg.get("g"), "h");
   }
}
