package org.codehaus.plexus;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.codehaus.plexus.context.Context;

public abstract class PlexusTestCase extends TestCase {
   protected PlexusContainer container;
   /** @deprecated */
   protected String basedir;
   private static String basedirPath;

   public PlexusTestCase() {
   }

   /** @deprecated */
   public PlexusTestCase(String testName) {
      super(testName);
   }

   protected void setUp() throws Exception {
      InputStream configuration = null;

      try {
         configuration = this.getCustomConfiguration();
         if (configuration == null) {
            configuration = this.getConfiguration();
         }
      } catch (Exception var4) {
         System.out.println("Error with configuration:");
         System.out.println("configuration = " + configuration);
         Assert.fail(var4.getMessage());
      }

      this.basedir = getBasedir();
      this.container = this.createContainerInstance();
      this.container.addContextValue("basedir", getBasedir());
      this.customizeContext();
      this.customizeContext(this.getContext());
      boolean hasPlexusHome = this.getContext().contains("plexus.home");
      if (!hasPlexusHome) {
         File f = getTestFile("target/plexus-home");
         if (!f.isDirectory()) {
            f.mkdir();
         }

         this.getContext().put("plexus.home", f.getAbsolutePath());
      }

      if (configuration != null) {
         this.container.setConfigurationResource(new InputStreamReader(configuration));
      }

      this.container.initialize();
      this.container.start();
   }

   protected PlexusContainer createContainerInstance() {
      return new DefaultPlexusContainer();
   }

   private Context getContext() {
      return this.container.getContext();
   }

   protected void customizeContext() throws Exception {
   }

   protected void customizeContext(Context context) throws Exception {
   }

   protected InputStream getCustomConfiguration() throws Exception {
      return null;
   }

   protected void tearDown() throws Exception {
      this.container.dispose();
      this.container = null;
   }

   protected PlexusContainer getContainer() {
      return this.container;
   }

   protected InputStream getConfiguration() throws Exception {
      return this.getConfiguration((String)null);
   }

   protected InputStream getConfiguration(String subname) throws Exception {
      String className = this.getClass().getName();
      String base = className.substring(className.lastIndexOf(".") + 1);
      String config = null;
      if (subname != null && !subname.equals("")) {
         config = base + "-" + subname + ".xml";
      } else {
         config = base + ".xml";
      }

      InputStream configStream = this.getResourceAsStream(config);
      return configStream;
   }

   protected InputStream getResourceAsStream(String resource) {
      return this.getClass().getResourceAsStream(resource);
   }

   protected ClassLoader getClassLoader() {
      return this.getClass().getClassLoader();
   }

   protected Object lookup(String componentKey) throws Exception {
      return this.getContainer().lookup(componentKey);
   }

   protected Object lookup(String role, String id) throws Exception {
      return this.getContainer().lookup(role, id);
   }

   protected void release(Object component) throws Exception {
      this.getContainer().release(component);
   }

   public static File getTestFile(String path) {
      return new File(getBasedir(), path);
   }

   public static File getTestFile(String basedir, String path) {
      File basedirFile = new File(basedir);
      if (!basedirFile.isAbsolute()) {
         basedirFile = getTestFile(basedir);
      }

      return new File(basedirFile, path);
   }

   public static String getTestPath(String path) {
      return getTestFile(path).getAbsolutePath();
   }

   public static String getTestPath(String basedir, String path) {
      return getTestFile(basedir, path).getAbsolutePath();
   }

   public static String getBasedir() {
      if (basedirPath != null) {
         return basedirPath;
      } else {
         basedirPath = System.getProperty("basedir");
         if (basedirPath == null) {
            basedirPath = (new File("")).getAbsolutePath();
         }

         return basedirPath;
      }
   }
}
