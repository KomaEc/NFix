package org.testng.xml;

import java.util.List;
import org.testng.collections.ListMultiMap;
import org.testng.collections.Maps;

public class Parameters {
   private ListMultiMap<String, String> m_localParameters = Maps.newListMultiMap();
   private ListMultiMap<String, String> m_allParameters = Maps.newListMultiMap();

   public List<String> getLocalParameter(String name) {
      return (List)this.m_localParameters.get(name);
   }

   public List<String> getAllValues(String name) {
      return (List)this.m_allParameters.get(name);
   }

   public List<String> getValue(String name) {
      return (List)this.m_localParameters.get(name);
   }

   public void addLocalParameter(String name, String value) {
      this.m_localParameters.put(name, value);
      this.m_allParameters.put(name, value);
   }

   public void addAllParameter(String name, String value) {
      this.m_allParameters.put(name, value);
   }
}
