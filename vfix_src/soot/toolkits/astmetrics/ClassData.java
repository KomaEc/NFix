package soot.toolkits.astmetrics;

import java.util.ArrayList;
import java.util.Iterator;

public class ClassData {
   String className;
   ArrayList<MetricData> metricData;

   public ClassData(String name) {
      this.className = name;
      this.metricData = new ArrayList();
   }

   public String getClassName() {
      return this.className;
   }

   public boolean classNameEquals(String className) {
      return this.className.equals(className);
   }

   public void addMetric(MetricData data) {
      Iterator it = this.metricData.iterator();

      MetricData temp;
      do {
         if (!it.hasNext()) {
            this.metricData.add(data);
            return;
         }

         temp = (MetricData)it.next();
      } while(!temp.metricName.equals(data.metricName));

   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append("<Class>\n");
      b.append("<ClassName>" + this.className + "</ClassName>\n");
      Iterator it = this.metricData.iterator();

      while(it.hasNext()) {
         b.append(((MetricData)it.next()).toString());
      }

      b.append("</Class>");
      return b.toString();
   }
}
