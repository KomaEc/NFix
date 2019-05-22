package org.testng.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.testng.collections.CollectionUtils;
import org.testng.collections.Lists;
import org.testng.reporters.XMLStringBuffer;

public class XmlMethodSelectors {
   private List<XmlMethodSelector> m_methodSelectors = Lists.newArrayList();

   public List<XmlMethodSelector> getMethodSelectors() {
      return this.m_methodSelectors;
   }

   public void setMethodSelector(XmlMethodSelector xms) {
      this.m_methodSelectors.add(xms);
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      if (CollectionUtils.hasElements((Collection)this.m_methodSelectors)) {
         xsb.push("method-selectors");
         Iterator i$ = this.m_methodSelectors.iterator();

         while(i$.hasNext()) {
            XmlMethodSelector selector = (XmlMethodSelector)i$.next();
            xsb.getStringBuffer().append(selector.toXml(indent + "  "));
         }

         xsb.pop("method-selectors");
      }

      return xsb.toXML();
   }
}
