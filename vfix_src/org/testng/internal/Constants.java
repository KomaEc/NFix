package org.testng.internal;

import java.util.Map;
import java.util.Properties;
import org.testng.collections.Maps;

public class Constants {
   private static final String NAMESPACE = "testng";
   public static final String PROP_OUTPUT_DIR = "testng.outputDir";
   private static final TestNGProperty[] COMMAND_LINE_PARAMETERS = new TestNGProperty[]{new TestNGProperty("-d", "testng.outputDir", "Directory where the result files will be created.", "test-output")};
   private static final Map<String, TestNGProperty> m_propertiesByName = Maps.newHashMap();

   private static TestNGProperty getProperty(String propertyName) {
      TestNGProperty result = (TestNGProperty)m_propertiesByName.get(propertyName);

      assert null != result : "Unknown property : " + propertyName;

      return result;
   }

   public static String getPropertyValue(Properties p, String propertyName) {
      TestNGProperty r = getProperty(propertyName);

      assert null != r : "Unknown property : " + propertyName;

      String result = p.getProperty(r.getName());
      return result;
   }

   public static boolean getBooleanPropertyValue(Properties properties, String propertyName) {
      TestNGProperty p = getProperty(propertyName);
      String r = properties.getProperty(propertyName, p.getDefault());
      boolean result = "true".equalsIgnoreCase(r);
      return Boolean.valueOf(result);
   }

   public static int getIntegerPropertyValue(Properties properties, String propertyName) {
      TestNGProperty p = getProperty(propertyName);
      String r = properties.getProperty(propertyName, p.getDefault());
      int result = Integer.parseInt(r);
      return result;
   }

   public static String getDefaultValueFor(String propertyName) {
      TestNGProperty p = getProperty(propertyName);
      return p.getDefault();
   }

   public static String displayStatus(int status) {
      if (3 == status) {
         return "SKIP";
      } else if (1 == status) {
         return "SUCCESS";
      } else {
         return 2 == status ? "FAILURE" : "UNKNOWN_STATUS";
      }
   }

   static {
      TestNGProperty[] arr$ = COMMAND_LINE_PARAMETERS;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         TestNGProperty element = arr$[i$];
         m_propertiesByName.put(element.getName(), element);
      }

   }
}
