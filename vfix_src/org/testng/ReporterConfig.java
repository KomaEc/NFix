package org.testng;

import java.util.Iterator;
import java.util.List;
import org.testng.collections.Lists;
import org.testng.internal.ClassHelper;
import org.testng.internal.PropertyUtils;
import org.testng.internal.Utils;

public class ReporterConfig {
   private String m_className;
   private List<ReporterConfig.Property> m_properties = Lists.newArrayList();

   public void addProperty(ReporterConfig.Property property) {
      this.m_properties.add(property);
   }

   public List<ReporterConfig.Property> getProperties() {
      return this.m_properties;
   }

   public String getClassName() {
      return this.m_className;
   }

   public void setClassName(String className) {
      this.m_className = className;
   }

   public String serialize() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.m_className);
      if (!this.m_properties.isEmpty()) {
         sb.append(":");

         for(int i = 0; i < this.m_properties.size(); ++i) {
            ReporterConfig.Property property = (ReporterConfig.Property)this.m_properties.get(i);
            sb.append(property.getName());
            sb.append("=");
            sb.append(property.getValue());
            if (i < this.m_properties.size() - 1) {
               sb.append(",");
            }
         }
      }

      return sb.toString();
   }

   public static ReporterConfig deserialize(String inputString) {
      ReporterConfig reporterConfig = null;
      if (!Utils.isStringEmpty(inputString)) {
         reporterConfig = new ReporterConfig();
         int clsNameEndIndex = inputString.indexOf(":");
         if (clsNameEndIndex == -1) {
            reporterConfig.setClassName(inputString);
         } else {
            reporterConfig.setClassName(inputString.substring(0, clsNameEndIndex));
            String propString = inputString.substring(clsNameEndIndex + 1, inputString.length());
            String[] props = propString.split(",");
            if (props != null && props.length > 0) {
               String[] arr$ = props;
               int len$ = props.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String prop = arr$[i$];
                  String[] propNameAndVal = prop.split("=");
                  if (propNameAndVal != null && propNameAndVal.length == 2) {
                     ReporterConfig.Property property = new ReporterConfig.Property();
                     property.setName(propNameAndVal[0]);
                     property.setValue(propNameAndVal[1]);
                     reporterConfig.addProperty(property);
                  }
               }
            }
         }
      }

      return reporterConfig;
   }

   public Object newReporterInstance() {
      Object result = null;
      Class reporterClass = ClassHelper.forName(this.m_className);
      if (reporterClass != null) {
         result = ClassHelper.newInstance(reporterClass);
         Iterator i$ = this.m_properties.iterator();

         while(i$.hasNext()) {
            ReporterConfig.Property property = (ReporterConfig.Property)i$.next();
            PropertyUtils.setProperty(result, property.getName(), property.getValue());
         }
      }

      return result;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("\nClass = " + this.m_className);
      Iterator i$ = this.m_properties.iterator();

      while(i$.hasNext()) {
         ReporterConfig.Property prop = (ReporterConfig.Property)i$.next();
         buf.append("\n\t " + prop.getName() + "=" + prop.getValue());
      }

      return buf.toString();
   }

   public static class Property {
      private String name;
      private String value;

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         this.value = value;
      }
   }
}
