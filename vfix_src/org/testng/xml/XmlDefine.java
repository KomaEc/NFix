package org.testng.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.testng.collections.CollectionUtils;
import org.testng.collections.Lists;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.OnElement;

public class XmlDefine {
   private String m_name;
   private List<String> m_includes = Lists.newArrayList();

   public void setName(String name) {
      this.m_name = name;
   }

   public String getName() {
      return this.m_name;
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      boolean hasElements = CollectionUtils.hasElements((Collection)this.m_includes);
      if (hasElements) {
         xsb.push("define", "name", this.m_name);
      }

      Iterator i$ = this.m_includes.iterator();

      while(i$.hasNext()) {
         String s = (String)i$.next();
         xsb.addEmptyElement("include", "name", s);
      }

      if (hasElements) {
         xsb.pop("define");
      }

      return xsb.toXML();
   }

   @OnElement(
      tag = "include",
      attributes = {"name"}
   )
   public void onElement(String name) {
      this.m_includes.add(name);
   }

   public List<String> getIncludes() {
      return this.m_includes;
   }
}
