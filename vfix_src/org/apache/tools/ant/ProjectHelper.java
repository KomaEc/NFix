package org.apache.tools.ant;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import org.apache.tools.ant.helper.ProjectHelper2;
import org.apache.tools.ant.util.LoaderUtils;
import org.xml.sax.AttributeList;

public class ProjectHelper {
   public static final String ANT_CORE_URI = "antlib:org.apache.tools.ant";
   public static final String ANT_CURRENT_URI = "ant:current";
   public static final String ANTLIB_URI = "antlib:";
   public static final String ANT_TYPE = "ant-type";
   public static final String HELPER_PROPERTY = "org.apache.tools.ant.ProjectHelper";
   public static final String SERVICE_ID = "META-INF/services/org.apache.tools.ant.ProjectHelper";
   public static final String PROJECTHELPER_REFERENCE = "ant.projectHelper";
   private Vector importStack = new Vector();

   public static void configureProject(Project project, File buildFile) throws BuildException {
      ProjectHelper helper = getProjectHelper();
      project.addReference("ant.projectHelper", helper);
      helper.parse(project, buildFile);
   }

   public Vector getImportStack() {
      return this.importStack;
   }

   public void parse(Project project, Object source) throws BuildException {
      throw new BuildException("ProjectHelper.parse() must be implemented in a helper plugin " + this.getClass().getName());
   }

   public static ProjectHelper getProjectHelper() throws BuildException {
      ProjectHelper helper = null;
      String helperClass = System.getProperty("org.apache.tools.ant.ProjectHelper");

      try {
         if (helperClass != null) {
            helper = newHelper(helperClass);
         }
      } catch (SecurityException var9) {
         System.out.println("Unable to load ProjectHelper class \"" + helperClass + " specified in system property " + "org.apache.tools.ant.ProjectHelper");
      }

      if (helper == null) {
         try {
            ClassLoader classLoader = LoaderUtils.getContextClassLoader();
            InputStream is = null;
            if (classLoader != null) {
               is = classLoader.getResourceAsStream("META-INF/services/org.apache.tools.ant.ProjectHelper");
            }

            if (is == null) {
               is = ClassLoader.getSystemResourceAsStream("META-INF/services/org.apache.tools.ant.ProjectHelper");
            }

            if (is != null) {
               InputStreamReader isr;
               try {
                  isr = new InputStreamReader(is, "UTF-8");
               } catch (UnsupportedEncodingException var7) {
                  isr = new InputStreamReader(is);
               }

               BufferedReader rd = new BufferedReader(isr);
               String helperClassName = rd.readLine();
               rd.close();
               if (helperClassName != null && !"".equals(helperClassName)) {
                  helper = newHelper(helperClassName);
               }
            }
         } catch (Exception var8) {
            System.out.println("Unable to load ProjectHelper from service \"META-INF/services/org.apache.tools.ant.ProjectHelper");
         }
      }

      return (ProjectHelper)(helper != null ? helper : new ProjectHelper2());
   }

   private static ProjectHelper newHelper(String helperClass) throws BuildException {
      ClassLoader classLoader = LoaderUtils.getContextClassLoader();

      try {
         Class clazz = null;
         if (classLoader != null) {
            try {
               clazz = classLoader.loadClass(helperClass);
            } catch (ClassNotFoundException var4) {
            }
         }

         if (clazz == null) {
            clazz = Class.forName(helperClass);
         }

         return (ProjectHelper)clazz.newInstance();
      } catch (Exception var5) {
         throw new BuildException(var5);
      }
   }

   /** @deprecated */
   public static ClassLoader getContextClassLoader() {
      return !LoaderUtils.isContextLoaderAvailable() ? null : LoaderUtils.getContextClassLoader();
   }

   /** @deprecated */
   public static void configure(Object target, AttributeList attrs, Project project) throws BuildException {
      if (target instanceof TypeAdapter) {
         target = ((TypeAdapter)target).getProxy();
      }

      IntrospectionHelper ih = IntrospectionHelper.getHelper(project, target.getClass());

      for(int i = 0; i < attrs.getLength(); ++i) {
         String value = replaceProperties(project, attrs.getValue(i), project.getProperties());

         try {
            ih.setAttribute(project, target, attrs.getName(i).toLowerCase(Locale.US), value);
         } catch (BuildException var7) {
            if (!attrs.getName(i).equals("id")) {
               throw var7;
            }
         }
      }

   }

   public static void addText(Project project, Object target, char[] buf, int start, int count) throws BuildException {
      addText(project, target, new String(buf, start, count));
   }

   public static void addText(Project project, Object target, String text) throws BuildException {
      if (text != null) {
         if (target instanceof TypeAdapter) {
            target = ((TypeAdapter)target).getProxy();
         }

         IntrospectionHelper.getHelper(project, target.getClass()).addText(project, target, text);
      }
   }

   public static void storeChild(Project project, Object parent, Object child, String tag) {
      IntrospectionHelper ih = IntrospectionHelper.getHelper(project, parent.getClass());
      ih.storeElement(project, parent, child, tag);
   }

   /** @deprecated */
   public static String replaceProperties(Project project, String value) throws BuildException {
      return project.replaceProperties(value);
   }

   /** @deprecated */
   public static String replaceProperties(Project project, String value, Hashtable keys) throws BuildException {
      PropertyHelper ph = PropertyHelper.getPropertyHelper(project);
      return ph.replaceProperties((String)null, value, keys);
   }

   /** @deprecated */
   public static void parsePropertyString(String value, Vector fragments, Vector propertyRefs) throws BuildException {
      PropertyHelper.parsePropertyStringDefault(value, fragments, propertyRefs);
   }

   public static String genComponentName(String uri, String name) {
      return uri != null && !uri.equals("") && !uri.equals("antlib:org.apache.tools.ant") ? uri + ":" + name : name;
   }

   public static String extractUriFromComponentName(String componentName) {
      if (componentName == null) {
         return "";
      } else {
         int index = componentName.lastIndexOf(58);
         return index == -1 ? "" : componentName.substring(0, index);
      }
   }

   public static String extractNameFromComponentName(String componentName) {
      int index = componentName.lastIndexOf(58);
      return index == -1 ? componentName : componentName.substring(index + 1);
   }

   public static BuildException addLocationToBuildException(BuildException ex, Location newLocation) {
      if (ex.getLocation() != null && ex.getMessage() != null) {
         String errorMessage = "The following error occurred while executing this line:" + System.getProperty("line.separator") + ex.getLocation().toString() + ex.getMessage();
         return newLocation == null ? new BuildException(errorMessage, ex) : new BuildException(errorMessage, ex, newLocation);
      } else {
         return ex;
      }
   }
}
