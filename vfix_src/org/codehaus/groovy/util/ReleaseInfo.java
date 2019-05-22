package org.codehaus.groovy.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

public class ReleaseInfo {
   private static final Properties RELEASE_INFO = new Properties();
   private static final String RELEASE_INFO_FILE = "META-INF/groovy-release-info.properties";
   private static final String KEY_IMPLEMENTATION_VERSION = "ImplementationVersion";
   private static final String KEY_BUNDLE_VERSION = "BundleVersion";
   private static final String KEY_BUILD_DATE = "BuildDate";
   private static final String KEY_BUILD_TIME = "BuildTime";

   public static String getVersion() {
      return get("ImplementationVersion");
   }

   public static Properties getAllProperties() {
      return RELEASE_INFO;
   }

   private static String get(String propName) {
      String propValue = RELEASE_INFO.getProperty(propName);
      return propValue == null ? "" : propValue;
   }

   static {
      URL url = null;
      ClassLoader cl = ReleaseInfo.class.getClassLoader();
      if (cl == null) {
         cl = ClassLoader.getSystemClassLoader();
      }

      if (cl instanceof URLClassLoader) {
         url = ((URLClassLoader)cl).findResource("META-INF/groovy-release-info.properties");
      } else {
         url = cl.getResource("META-INF/groovy-release-info.properties");
      }

      if (url != null) {
         try {
            InputStream is = url.openStream();
            if (is != null) {
               RELEASE_INFO.load(is);
            }
         } catch (IOException var3) {
         }
      }

   }
}
