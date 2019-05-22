package org.testng.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.testng.collections.CollectionUtils;
import org.testng.collections.Lists;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.OnElement;

public class XmlRun {
   private List<String> m_excludes = Lists.newArrayList();
   private List<String> m_includes = Lists.newArrayList();

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      boolean hasElements = CollectionUtils.hasElements((Collection)this.m_excludes) || CollectionUtils.hasElements((Collection)this.m_includes);
      if (hasElements) {
         xsb.push("run");
      }

      Iterator i$ = this.m_includes.iterator();

      String s;
      while(i$.hasNext()) {
         s = (String)i$.next();
         xsb.addEmptyElement("include", "name", s);
      }

      i$ = this.m_excludes.iterator();

      while(i$.hasNext()) {
         s = (String)i$.next();
         xsb.addEmptyElement("exclude", "name", s);
      }

      if (hasElements) {
         xsb.pop("run");
      }

      return xsb.toXML();
   }

   public List<String> getExcludes() {
      return this.m_excludes;
   }

   @OnElement(
      tag = "exclude",
      attributes = {"name"}
   )
   public void onExclude(String name) {
      this.m_excludes.add(name);
   }

   public List<String> getIncludes() {
      return this.m_includes;
   }

   @OnElement(
      tag = "include",
      attributes = {"name"}
   )
   public void onInclude(String name) {
      this.m_includes.add(name);
   }
}
