package org.testng.internal;

import java.util.Collection;
import java.util.Map;
import org.testng.ISuite;
import org.testng.TestNGException;
import org.testng.collections.Maps;
import org.testng.xml.XmlSuite;

public class SuiteRunnerMap {
   private Map<String, ISuite> m_map = Maps.newHashMap();

   public void put(XmlSuite xmlSuite, ISuite suite) {
      String name = xmlSuite.getName();
      if (this.m_map.containsKey(name)) {
         throw new TestNGException("SuiteRunnerMap already have runner for suite " + name);
      } else {
         this.m_map.put(name, suite);
      }
   }

   public ISuite get(XmlSuite xmlSuite) {
      return (ISuite)this.m_map.get(xmlSuite.getName());
   }

   public Collection<ISuite> values() {
      return this.m_map.values();
   }
}
