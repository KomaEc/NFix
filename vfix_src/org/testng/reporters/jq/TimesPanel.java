package org.testng.reporters.jq;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.ISuite;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.collections.Maps;
import org.testng.reporters.XMLStringBuffer;

public class TimesPanel extends BaseMultiSuitePanel {
   private Map<String, Long> m_totalTime = Maps.newHashMap();

   public TimesPanel(Model model) {
      super(model);
   }

   public String getPrefix() {
      return "times-";
   }

   public String getHeader(ISuite suite) {
      return "Times for " + suite.getName();
   }

   private String js(ISuite suite) {
      String functionName = "tableData_" + suiteToTag(suite);
      StringBuilder result = new StringBuilder("suiteTableInitFunctions.push('" + functionName + "');\n" + "function " + functionName + "() {\n" + "var data = new google.visualization.DataTable();\n" + "data.addColumn('number', 'Number');\n" + "data.addColumn('string', 'Method');\n" + "data.addColumn('string', 'Class');\n" + "data.addColumn('number', 'Time (ms)');\n");
      List<ITestResult> allTestResults = this.getModel().getAllTestResults(suite);
      result.append("data.addRows(" + allTestResults.size() + ");\n");
      Collections.sort(allTestResults, new Comparator<ITestResult>() {
         public int compare(ITestResult o1, ITestResult o2) {
            long t1 = o1.getEndMillis() - o1.getStartMillis();
            long t2 = o2.getEndMillis() - o2.getStartMillis();
            return (int)(t2 - t1);
         }
      });
      int index = 0;

      for(Iterator i$ = allTestResults.iterator(); i$.hasNext(); ++index) {
         ITestResult tr = (ITestResult)i$.next();
         ITestNGMethod m = tr.getMethod();
         long time = tr.getEndMillis() - tr.getStartMillis();
         result.append("data.setCell(" + index + ", " + "0, " + index + ")\n").append("data.setCell(" + index + ", " + "1, '" + m.getMethodName() + "')\n").append("data.setCell(" + index + ", " + "2, '" + m.getTestClass().getName() + "')\n").append("data.setCell(" + index + ", " + "3, " + time + ");\n");
         Long total = (Long)this.m_totalTime.get(suite.getName());
         if (total == null) {
            total = 0L;
         }

         this.m_totalTime.put(suite.getName(), total + time);
      }

      result.append("window.suiteTableData['" + suiteToTag(suite) + "']" + "= { tableData: data, tableDiv: 'times-div-" + suiteToTag(suite) + "'}\n" + "return data;\n" + "}\n");
      return result.toString();
   }

   public String getContent(ISuite suite, XMLStringBuffer main) {
      XMLStringBuffer xsb = new XMLStringBuffer(main.getCurrentIndent());
      xsb.push("div", "class", "times-div");
      xsb.push("script", "type", "text/javascript");
      xsb.addString(this.js(suite));
      xsb.pop("script");
      Long time = (Long)this.m_totalTime.get(suite.getName());
      if (time != null) {
         xsb.addRequired("span", String.format("Total running time: %s", this.prettyDuration(time)), "class", "suite-total-time");
      }

      xsb.push("div", "id", "times-div-" + suiteToTag(suite));
      xsb.pop("div");
      xsb.pop("div");
      return xsb.toXML();
   }

   private String prettyDuration(long totalTime) {
      String result;
      if (totalTime < 1000L) {
         result = totalTime + " ms";
      } else if (totalTime < 60000L) {
         result = totalTime / 1000L + " seconds";
      } else if (totalTime < 3600000L) {
         result = totalTime / 1000L / 60L + " minutes";
      } else {
         result = totalTime / 1000L / 60L / 60L + " hours";
      }

      return result;
   }

   public String getNavigatorLink(ISuite suite) {
      return "Times";
   }
}
