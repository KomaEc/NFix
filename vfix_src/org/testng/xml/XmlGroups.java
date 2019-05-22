package org.testng.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.testng.collections.CollectionUtils;
import org.testng.collections.Lists;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.Tag;

public class XmlGroups {
   private List<XmlDefine> m_defines = Lists.newArrayList();
   private XmlRun m_run;
   private List<XmlDependencies> m_dependencies = Lists.newArrayList();

   public List<XmlDefine> getDefines() {
      return this.m_defines;
   }

   @Tag(
      name = "define"
   )
   public void addDefine(XmlDefine define) {
      this.getDefines().add(define);
   }

   public void setDefines(List<XmlDefine> defines) {
      this.m_defines = defines;
   }

   public XmlRun getRun() {
      return this.m_run;
   }

   public void setRun(XmlRun run) {
      this.m_run = run;
   }

   public List<XmlDependencies> getDependencies() {
      return this.m_dependencies;
   }

   @Tag(
      name = "dependencies"
   )
   public void setXmlDependencies(XmlDependencies dependencies) {
      this.m_dependencies.add(dependencies);
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      String indent2 = indent + "  ";
      boolean hasGroups = CollectionUtils.hasElements((Collection)this.m_defines) || this.m_run != null || CollectionUtils.hasElements((Collection)this.m_dependencies);
      if (hasGroups) {
         xsb.push("groups");
      }

      Iterator i$ = this.m_defines.iterator();

      while(i$.hasNext()) {
         XmlDefine d = (XmlDefine)i$.next();
         xsb.getStringBuffer().append(d.toXml(indent2));
      }

      xsb.getStringBuffer().append(this.m_run.toXml(indent2));
      i$ = this.m_dependencies.iterator();

      while(i$.hasNext()) {
         XmlDependencies d = (XmlDependencies)i$.next();
         xsb.getStringBuffer().append(d.toXml(indent2));
      }

      if (hasGroups) {
         xsb.pop("groups");
      }

      return xsb.toXML();
   }
}
