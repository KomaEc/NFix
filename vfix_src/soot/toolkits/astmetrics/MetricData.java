package soot.toolkits.astmetrics;

public class MetricData {
   String metricName;
   Object value;

   public MetricData(String name, Object val) {
      this.metricName = name;
      this.value = val;
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append("<Metric>\n");
      b.append("  <MetricName>" + this.metricName + "</MetricName>\n");
      b.append("  <Value>" + this.value.toString() + "</Value>\n");
      b.append("</Metric>\n");
      return b.toString();
   }
}
