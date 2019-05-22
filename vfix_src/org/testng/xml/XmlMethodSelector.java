package org.testng.xml;

import java.util.Properties;
import org.testng.TestNGException;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.OnElement;

public class XmlMethodSelector {
   private String m_className;
   private int m_priority;
   private XmlScript m_script = new XmlScript();

   public void setClassName(String s) {
      this.m_className = s;
   }

   public String getClassName() {
      return this.m_className;
   }

   @OnElement(
      tag = "selector-class",
      attributes = {"name", "priority"}
   )
   public void setElement(String name, String priority) {
      this.setName(name);
      this.setPriority(Integer.parseInt(priority));
   }

   public void setName(String name) {
      this.m_className = name;
   }

   public void setScript(XmlScript script) {
      this.m_script = script;
   }

   public String getExpression() {
      return this.m_script.getScript();
   }

   public void setExpression(String expression) {
      this.m_script.setScript(expression);
   }

   public String getLanguage() {
      return this.m_script.getLanguage();
   }

   public void setLanguage(String language) {
      this.m_script.setLanguage(language);
   }

   public int getPriority() {
      return this.m_priority;
   }

   public void setPriority(int priority) {
      this.m_priority = priority;
   }

   private void ppp(String s) {
      System.out.println("[XmlMethodSelector] " + s);
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      xsb.push("method-selector");
      Properties clsProp;
      if (null != this.m_className) {
         clsProp = new Properties();
         clsProp.setProperty("name", this.getClassName());
         if (this.getPriority() != -1) {
            clsProp.setProperty("priority", String.valueOf(this.getPriority()));
         }

         xsb.addEmptyElement("selector-class", clsProp);
      } else {
         if (this.getLanguage() == null) {
            throw new TestNGException("Invalid Method Selector:  found neither class name nor language");
         }

         clsProp = new Properties();
         clsProp.setProperty("language", this.getLanguage());
         xsb.push("script", clsProp);
         xsb.addCDATA(this.getExpression());
         xsb.pop("script");
      }

      xsb.pop("method-selector");
      return xsb.toXML();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.m_className == null ? 0 : this.m_className.hashCode());
      result = 31 * result + (this.getExpression() == null ? 0 : this.getExpression().hashCode());
      result = 31 * result + (this.getLanguage() == null ? 0 : this.getLanguage().hashCode());
      result = 31 * result + this.m_priority;
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
         XmlMethodSelector other = (XmlMethodSelector)obj;
         if (this.m_className == null) {
            if (other.m_className != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_className.equals(other.m_className)) {
            return XmlSuite.f();
         }

         if (this.getExpression() == null) {
            if (other.getExpression() != null) {
               return XmlSuite.f();
            }
         } else if (!this.getExpression().equals(other.getExpression())) {
            return XmlSuite.f();
         }

         if (this.getLanguage() == null) {
            if (other.getLanguage() != null) {
               return XmlSuite.f();
            }
         } else if (!this.getLanguage().equals(other.getLanguage())) {
            return XmlSuite.f();
         }

         return this.m_priority != other.m_priority ? XmlSuite.f() : true;
      }
   }
}
