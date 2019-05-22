package org.testng;

import org.testng.collections.Objects;
import org.testng.xml.XmlSuite;

class SuiteResult implements ISuiteResult, Comparable {
   private static final long serialVersionUID = 6778513869858860756L;
   private String m_propertyFileName = null;
   private XmlSuite m_suite = null;
   private ITestContext m_testContext = null;

   protected SuiteResult(XmlSuite suite, ITestContext tr) {
      this.m_suite = suite;
      this.m_testContext = tr;
   }

   public String getPropertyFileName() {
      return this.m_propertyFileName;
   }

   public ITestContext getTestContext() {
      return this.m_testContext;
   }

   public XmlSuite getSuite() {
      return this.m_suite;
   }

   public int compareTo(Object o) {
      int result = 0;

      try {
         SuiteResult other = (SuiteResult)o;
         String n1 = this.getTestContext().getName();
         String n2 = other.getTestContext().getName();
         result = n1.compareTo(n2);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return result;
   }

   public String toString() {
      return Objects.toStringHelper(this.getClass()).add("context", this.getTestContext().getName()).toString();
   }
}
