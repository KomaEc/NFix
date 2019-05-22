package org.testng.reporters.jq;

import java.util.Iterator;
import java.util.Set;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.collections.Maps;
import org.testng.collections.SetMultiMap;
import org.testng.reporters.XMLStringBuffer;

public class IgnoredMethodsPanel extends BaseMultiSuitePanel {
   public IgnoredMethodsPanel(Model model) {
      super(model);
   }

   public String getPrefix() {
      return "ignored-methods-";
   }

   public String getHeader(ISuite suite) {
      return pluralize(suite.getExcludedMethods().size(), "ignored method");
   }

   public String getContent(ISuite suite, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      SetMultiMap<Class<?>, ITestNGMethod> map = Maps.newSetMultiMap();
      Iterator i$ = suite.getExcludedMethods().iterator();

      while(i$.hasNext()) {
         ITestNGMethod method = (ITestNGMethod)i$.next();
         map.put(method.getTestClass().getRealClass(), method);
      }

      i$ = map.keySet().iterator();

      while(i$.hasNext()) {
         Class<?> c = (Class)i$.next();
         xsb.push("div", "class", "ignored-class-div");
         xsb.addRequired("span", c.getName(), "class", "ignored-class-name");
         xsb.push("div", "class", "ignored-methods-div");
         Iterator i$ = ((Set)map.get(c)).iterator();

         while(i$.hasNext()) {
            ITestNGMethod m = (ITestNGMethod)i$.next();
            xsb.addRequired("span", m.getMethodName(), "class", "ignored-method-name");
            xsb.addEmptyElement("br");
         }

         xsb.pop("div");
         xsb.pop("div");
      }

      return xsb.toXML();
   }

   public String getNavigatorLink(ISuite suite) {
      return "Ignored methods";
   }
}
