package org.testng.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.collections.CollectionUtils;
import org.testng.collections.Maps;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.OnElement;

public class XmlDependencies {
   private Map<String, String> m_xmlDependencyGroups = Maps.newHashMap();

   @OnElement(
      tag = "group",
      attributes = {"name", "depends-on"}
   )
   public void onGroup(String name, String dependsOn) {
      this.m_xmlDependencyGroups.put(name, dependsOn);
   }

   public Map<String, String> getDependencies() {
      return this.m_xmlDependencyGroups;
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      boolean hasElements = CollectionUtils.hasElements(this.m_xmlDependencyGroups);
      if (hasElements) {
         xsb.push("dependencies");
      }

      Iterator i$ = this.m_xmlDependencyGroups.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, String> entry = (Entry)i$.next();
         xsb.addEmptyElement("include", "name", (String)entry.getKey(), "depends-on", (String)entry.getValue());
      }

      if (hasElements) {
         xsb.pop("dependencies");
      }

      return xsb.toXML();
   }
}
