package org.testng.xml;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.testng.collections.Lists;
import org.testng.internal.PackageUtils;
import org.testng.internal.Utils;
import org.testng.reporters.XMLStringBuffer;

public class XmlPackage implements Serializable {
   private static final long serialVersionUID = 1996341670354923204L;
   private String m_name;
   private List<String> m_include = Lists.newArrayList();
   private List<String> m_exclude = Lists.newArrayList();
   private List<XmlClass> m_xmlClasses = null;

   public XmlPackage() {
   }

   public XmlPackage(String name) {
      this.m_name = name;
   }

   public List<String> getExclude() {
      return this.m_exclude;
   }

   public void setExclude(List<String> exclude) {
      this.m_exclude = exclude;
   }

   public List<String> getInclude() {
      return this.m_include;
   }

   public void setInclude(List<String> include) {
      this.m_include = include;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String name) {
      this.m_name = name;
   }

   public List<XmlClass> getXmlClasses() {
      if (null == this.m_xmlClasses) {
         this.m_xmlClasses = this.initializeXmlClasses();
      }

      return this.m_xmlClasses;
   }

   private List<XmlClass> initializeXmlClasses() {
      List result = Lists.newArrayList();

      try {
         String[] classes = PackageUtils.findClassesInPackage(this.m_name, this.m_include, this.m_exclude);
         int index = 0;
         String[] arr$ = classes;
         int len$ = classes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String className = arr$[i$];
            result.add(new XmlClass(className, index++, false));
         }
      } catch (IOException var8) {
         Utils.log("XmlPackage", 1, var8.getMessage());
      }

      return result;
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      Properties p = new Properties();
      p.setProperty("name", this.getName());
      if (this.getInclude().isEmpty() && this.getExclude().isEmpty()) {
         xsb.addEmptyElement("package", p);
      } else {
         xsb.push("package", p);
         Iterator i$ = this.getInclude().iterator();

         String m;
         Properties excludeProp;
         while(i$.hasNext()) {
            m = (String)i$.next();
            excludeProp = new Properties();
            excludeProp.setProperty("name", m);
            xsb.addEmptyElement("include", excludeProp);
         }

         i$ = this.getExclude().iterator();

         while(i$.hasNext()) {
            m = (String)i$.next();
            excludeProp = new Properties();
            excludeProp.setProperty("name", m);
            xsb.addEmptyElement("exclude", excludeProp);
         }

         xsb.pop("package");
      }

      return xsb.toXML();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.m_exclude == null ? 0 : this.m_exclude.hashCode());
      result = 31 * result + (this.m_include == null ? 0 : this.m_include.hashCode());
      result = 31 * result + (this.m_name == null ? 0 : this.m_name.hashCode());
      result = 31 * result + (this.m_xmlClasses == null ? 0 : this.m_xmlClasses.hashCode());
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
         XmlPackage other = (XmlPackage)obj;
         if (this.m_exclude == null) {
            if (other.m_exclude != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_exclude.equals(other.m_exclude)) {
            return XmlSuite.f();
         }

         if (this.m_include == null) {
            if (other.m_include != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_include.equals(other.m_include)) {
            return XmlSuite.f();
         }

         if (this.m_name == null) {
            if (other.m_name != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_name.equals(other.m_name)) {
            return XmlSuite.f();
         }

         if (this.m_xmlClasses == null) {
            if (other.m_xmlClasses != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_xmlClasses.equals(other.m_xmlClasses)) {
            return XmlSuite.f();
         }

         return true;
      }
   }
}
