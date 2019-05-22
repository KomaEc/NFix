package org.testng.xml;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.collections.Objects;
import org.testng.internal.ClassHelper;
import org.testng.reporters.XMLStringBuffer;

public class XmlClass implements Serializable, Cloneable {
   private static final long serialVersionUID = 8885360896966149897L;
   private List<XmlInclude> m_includedMethods = Lists.newArrayList();
   private List<String> m_excludedMethods = Lists.newArrayList();
   private String m_name = null;
   private Class m_class = null;
   private int m_index;
   private boolean m_loadClasses = true;
   private Map<String, String> m_parameters = Maps.newHashMap();
   private XmlTest m_xmlTest;

   public XmlClass() {
      this.init("", (Class)null, 0, false);
   }

   public XmlClass(String name) {
      this.init(name, (Class)null, 0);
   }

   public XmlClass(String name, boolean loadClasses) {
      this.init(name, (Class)null, 0, loadClasses);
   }

   public XmlClass(Class cls) {
      this.init(cls.getName(), cls, 0, true);
   }

   public XmlClass(Class cls, boolean loadClasses) {
      this.init(cls.getName(), cls, 0, loadClasses);
   }

   public XmlClass(String className, int index) {
      this.init(className, (Class)null, index, true);
   }

   public XmlClass(String className, int index, boolean loadClasses) {
      this.init(className, (Class)null, index, loadClasses);
   }

   private void init(String className, Class cls, int index) {
      this.init(className, cls, index, true);
   }

   private void init(String className, Class cls, int index, boolean resolveClass) {
      this.m_name = className;
      this.m_class = cls;
      this.m_index = index;
      if (null == this.m_class && resolveClass) {
         this.loadClass();
      }

   }

   private void loadClass() {
      this.m_class = ClassHelper.forName(this.m_name);
      if (null == this.m_class) {
         throw new TestNGException("Cannot find class in classpath: " + this.m_name);
      }
   }

   public Class getSupportClass() {
      if (this.m_class == null) {
         this.loadClass();
      }

      return this.m_class;
   }

   public void setClass(Class className) {
      this.m_class = className;
   }

   public List<String> getExcludedMethods() {
      return this.m_excludedMethods;
   }

   public void setExcludedMethods(List<String> excludedMethods) {
      this.m_excludedMethods = excludedMethods;
   }

   public List<XmlInclude> getIncludedMethods() {
      return this.m_includedMethods;
   }

   public void setIncludedMethods(List<XmlInclude> includedMethods) {
      this.m_includedMethods = includedMethods;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String name) {
      this.m_name = name;
   }

   public boolean loadClasses() {
      return this.m_loadClasses;
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("class", this.m_name).toString();
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      Properties prop = new Properties();
      prop.setProperty("name", this.getName());
      boolean hasMethods = !this.m_includedMethods.isEmpty() || !this.m_excludedMethods.isEmpty();
      boolean hasParameters = !this.m_parameters.isEmpty();
      if (!hasParameters && !hasMethods) {
         xsb.addEmptyElement("class", prop);
      } else {
         xsb.push("class", prop);
         XmlUtils.dumpParameters(xsb, this.m_parameters);
         if (hasMethods) {
            xsb.push("methods");
            Iterator i$ = this.getIncludedMethods().iterator();

            while(i$.hasNext()) {
               XmlInclude m = (XmlInclude)i$.next();
               xsb.getStringBuffer().append(m.toXml(indent + "    "));
            }

            i$ = this.getExcludedMethods().iterator();

            while(i$.hasNext()) {
               String m = (String)i$.next();
               Properties p = new Properties();
               p.setProperty("name", m);
               xsb.addEmptyElement("exclude", p);
            }

            xsb.pop("methods");
         }

         xsb.pop("class");
      }

      return xsb.toXML();
   }

   public static String listToString(List<Integer> invocationNumbers) {
      StringBuilder result = new StringBuilder();
      int i = 0;

      Integer n;
      for(Iterator i$ = invocationNumbers.iterator(); i$.hasNext(); result.append(n)) {
         n = (Integer)i$.next();
         if (i++ > 0) {
            result.append(" ");
         }
      }

      return result.toString();
   }

   public Object clone() {
      XmlClass result = new XmlClass(this.getName(), this.getIndex(), this.loadClasses());
      result.setExcludedMethods(this.getExcludedMethods());
      result.setIncludedMethods(this.getIncludedMethods());
      return result;
   }

   public int getIndex() {
      return this.m_index;
   }

   public void setIndex(int index) {
      this.m_index = index;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.m_class == null ? 0 : this.m_class.hashCode());
      result = 31 * result + (this.m_loadClasses ? 1 : 0);
      result = 31 * result + (this.m_excludedMethods == null ? 0 : this.m_excludedMethods.hashCode());
      result = 31 * result + (this.m_includedMethods == null ? 0 : this.m_includedMethods.hashCode());
      result = 31 * result + this.m_index;
      result = 31 * result + (this.m_name == null ? 0 : this.m_name.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return XmlSuite.f();
      } else if (this.getClass() != obj.getClass()) {
         return XmlSuite.f();
      } else {
         XmlClass other = (XmlClass)obj;
         if (other.m_loadClasses != this.m_loadClasses) {
            return XmlSuite.f();
         } else if (!this.m_excludedMethods.equals(other.m_excludedMethods)) {
            return XmlSuite.f();
         } else {
            if (this.m_includedMethods == null) {
               if (other.m_includedMethods != null) {
                  return XmlSuite.f();
               }
            } else if (!this.m_includedMethods.equals(other.m_includedMethods)) {
               return XmlSuite.f();
            }

            if (this.m_name == null) {
               if (other.m_name != null) {
                  return XmlSuite.f();
               }
            } else if (!this.m_name.equals(other.m_name)) {
               return XmlSuite.f();
            }

            return true;
         }
      }
   }

   public void setParameters(Map<String, String> parameters) {
      this.m_parameters.clear();
      this.m_parameters.putAll(parameters);
   }

   public Map<String, String> getAllParameters() {
      Map<String, String> result = Maps.newHashMap();
      Map<String, String> parameters = this.m_xmlTest.getLocalParameters();
      Iterator i$ = parameters.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, String> parameter = (Entry)i$.next();
         result.put(parameter.getKey(), parameter.getValue());
      }

      i$ = this.m_parameters.keySet().iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         result.put(key, this.m_parameters.get(key));
      }

      return result;
   }

   public Map<String, String> getLocalParameters() {
      return this.m_parameters;
   }

   /** @deprecated */
   @Deprecated
   public Map<String, String> getParameters() {
      return this.getAllParameters();
   }

   public void setXmlTest(XmlTest test) {
      this.m_xmlTest = test;
   }
}
