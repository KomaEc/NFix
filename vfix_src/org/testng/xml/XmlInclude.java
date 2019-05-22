package org.testng.xml;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.reporters.XMLStringBuffer;

public class XmlInclude implements Serializable {
   private static final long serialVersionUID = 1L;
   private String m_name;
   private List<Integer> m_invocationNumbers;
   private int m_index;
   private String m_description;
   private Map<String, String> m_parameters;
   private XmlClass m_xmlClass;

   public XmlInclude() {
      this.m_invocationNumbers = Lists.newArrayList();
      this.m_parameters = Maps.newHashMap();
   }

   public XmlInclude(String n) {
      this(n, Collections.emptyList(), 0);
   }

   public XmlInclude(String n, int index) {
      this(n, Collections.emptyList(), index);
   }

   public XmlInclude(String n, List<Integer> list, int index) {
      this.m_invocationNumbers = Lists.newArrayList();
      this.m_parameters = Maps.newHashMap();
      this.m_name = n;
      this.m_invocationNumbers = list;
      this.m_index = index;
   }

   public void setDescription(String description) {
      this.m_description = description;
   }

   public String getDescription() {
      return this.m_description;
   }

   public String getName() {
      return this.m_name;
   }

   public List<Integer> getInvocationNumbers() {
      return this.m_invocationNumbers;
   }

   public int getIndex() {
      return this.m_index;
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      Properties p = new Properties();
      p.setProperty("name", this.getName());
      List<Integer> invocationNumbers = this.getInvocationNumbers();
      if (invocationNumbers != null && invocationNumbers.size() > 0) {
         p.setProperty("invocation-numbers", XmlClass.listToString(invocationNumbers).toString());
      }

      if (!this.m_parameters.isEmpty()) {
         xsb.push("include", p);
         XmlUtils.dumpParameters(xsb, this.m_parameters);
         xsb.pop("include");
      } else {
         xsb.addEmptyElement("include", p);
      }

      return xsb.toXML();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.m_index;
      result = 31 * result + (this.m_invocationNumbers == null ? 0 : this.m_invocationNumbers.hashCode());
      result = 31 * result + (this.m_parameters == null ? 0 : this.m_parameters.hashCode());
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
         XmlInclude other = (XmlInclude)obj;
         if (this.m_invocationNumbers == null) {
            if (other.m_invocationNumbers != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_invocationNumbers.equals(other.m_invocationNumbers)) {
            return XmlSuite.f();
         }

         if (this.m_name == null) {
            if (other.m_name != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_name.equals(other.m_name)) {
            return XmlSuite.f();
         }

         if (this.m_parameters == null) {
            if (other.m_parameters != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_parameters.equals(other.m_parameters)) {
            return XmlSuite.f();
         }

         return true;
      }
   }

   public void addParameter(String name, String value) {
      this.m_parameters.put(name, value);
   }

   /** @deprecated */
   @Deprecated
   public Map<String, String> getParameters() {
      return this.getAllParameters();
   }

   public Map<String, String> getLocalParameters() {
      return this.m_parameters;
   }

   public Map<String, String> getAllParameters() {
      Map<String, String> result = Maps.newHashMap();
      if (this.m_xmlClass != null) {
         result.putAll(this.m_xmlClass.getAllParameters());
      }

      result.putAll(this.m_parameters);
      return result;
   }

   public void setXmlClass(XmlClass xmlClass) {
      this.m_xmlClass = xmlClass;
   }
}
