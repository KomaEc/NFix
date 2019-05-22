package org.apache.maven.plugin.surefire.booterclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.apache.maven.plugin.surefire.booterclient.lazytestprovider.OutputStreamFlushableCommandline;
import org.apache.maven.plugin.surefire.util.Relocator;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.booter.ForkedBooter;
import org.apache.maven.surefire.booter.StartupConfiguration;
import org.apache.maven.surefire.booter.SurefireBooterForkException;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.surefire.util.UrlUtils;

public class ForkConfiguration {
   public static final String FORK_ONCE = "once";
   public static final String FORK_ALWAYS = "always";
   public static final String FORK_NEVER = "never";
   public static final String FORK_PERTHREAD = "perthread";
   private final int forkCount;
   private final boolean reuseForks;
   private final Classpath bootClasspathConfiguration;
   private final String jvmExecutable;
   private Properties modelProperties;
   private final String argLine;
   private final Map<String, String> environmentVariables;
   private final File workingDirectory;
   private final File tempDirectory;
   private final boolean debug;
   private final String debugLine;

   public ForkConfiguration(Classpath bootClasspathConfiguration, File tmpDir, String debugLine, String jvmExecutable, File workingDirectory, Properties modelProperties, String argLine, Map<String, String> environmentVariables, boolean debugEnabled, int forkCount, boolean reuseForks) {
      this.bootClasspathConfiguration = bootClasspathConfiguration;
      this.tempDirectory = tmpDir;
      this.debugLine = debugLine;
      this.jvmExecutable = jvmExecutable;
      this.workingDirectory = workingDirectory;
      this.modelProperties = modelProperties;
      this.argLine = argLine;
      this.environmentVariables = environmentVariables;
      this.debug = debugEnabled;
      this.forkCount = forkCount;
      this.reuseForks = reuseForks;
   }

   public Classpath getBootClasspath() {
      return this.bootClasspathConfiguration;
   }

   public static String getEffectiveForkMode(String forkMode) {
      if ("pertest".equalsIgnoreCase(forkMode)) {
         return "always";
      } else if ("none".equalsIgnoreCase(forkMode)) {
         return "never";
      } else if (!forkMode.equals("never") && !forkMode.equals("once") && !forkMode.equals("always") && !forkMode.equals("perthread")) {
         throw new IllegalArgumentException("Fork mode " + forkMode + " is not a legal value");
      } else {
         return forkMode;
      }
   }

   public OutputStreamFlushableCommandline createCommandLine(List<String> classPath, StartupConfiguration startupConfiguration, int threadNumber) throws SurefireBooterForkException {
      return this.createCommandLine(classPath, startupConfiguration.getClassLoaderConfiguration().isManifestOnlyJarRequestedAndUsable(), startupConfiguration.isShadefire(), startupConfiguration.isProviderMainClass() ? startupConfiguration.getActualClassName() : ForkedBooter.class.getName(), threadNumber);
   }

   OutputStreamFlushableCommandline createCommandLine(List<String> classPath, boolean useJar, boolean shadefire, String providerThatHasMainMethod, int threadNumber) throws SurefireBooterForkException {
      OutputStreamFlushableCommandline cli = new OutputStreamFlushableCommandline();
      cli.setExecutable(this.jvmExecutable);
      if (this.argLine != null) {
         cli.createArg().setLine(this.replaceThreadNumberPlaceholder(this.stripNewLines(this.replacePropertyExpressions(this.argLine)), threadNumber));
      }

      if (this.environmentVariables != null) {
         Iterator i$ = this.environmentVariables.keySet().iterator();

         while(i$.hasNext()) {
            String key = (String)i$.next();
            String value = (String)this.environmentVariables.get(key);
            cli.addEnvironment(key, value);
         }
      }

      if (this.getDebugLine() != null && !"".equals(this.getDebugLine())) {
         cli.createArg().setLine(this.getDebugLine());
      }

      if (useJar) {
         File jarFile;
         try {
            jarFile = this.createJar(classPath, providerThatHasMainMethod);
         } catch (IOException var10) {
            throw new SurefireBooterForkException("Error creating archive file", var10);
         }

         cli.createArg().setValue("-jar");
         cli.createArg().setValue(jarFile.getAbsolutePath());
      } else {
         cli.addEnvironment("CLASSPATH", StringUtils.join(classPath.iterator(), File.pathSeparator));
         String forkedBooter = providerThatHasMainMethod != null ? providerThatHasMainMethod : ForkedBooter.class.getName();
         cli.createArg().setValue(shadefire ? (new Relocator()).relocate(forkedBooter) : forkedBooter);
      }

      cli.setWorkingDirectory(this.workingDirectory.getAbsolutePath());
      return cli;
   }

   private String replaceThreadNumberPlaceholder(String argLine, int threadNumber) {
      return argLine.replace("${surefire.threadNumber}", String.valueOf(threadNumber)).replace("${surefire.forkNumber}", String.valueOf(threadNumber));
   }

   private String replacePropertyExpressions(String argLine) {
      if (argLine == null) {
         return null;
      } else {
         Enumeration e = this.modelProperties.propertyNames();

         while(e.hasMoreElements()) {
            String key = e.nextElement().toString();
            String field = "@{" + key + "}";
            if (argLine.contains(field)) {
               argLine = argLine.replace(field, this.modelProperties.getProperty(key, ""));
            }
         }

         return argLine;
      }
   }

   private File createJar(List<String> classPath, String startClassName) throws IOException {
      File file = File.createTempFile("surefirebooter", ".jar", this.tempDirectory);
      if (!this.debug) {
         file.deleteOnExit();
      }

      FileOutputStream fos = new FileOutputStream(file);
      JarOutputStream jos = new JarOutputStream(fos);
      jos.setLevel(0);
      JarEntry je = new JarEntry("META-INF/MANIFEST.MF");
      jos.putNextEntry(je);
      Manifest man = new Manifest();
      String cp = "";

      String el;
      for(Iterator i$ = classPath.iterator(); i$.hasNext(); cp = cp + UrlUtils.getURL(new File(el)).toExternalForm() + " ") {
         el = (String)i$.next();
      }

      man.getMainAttributes().putValue("Manifest-Version", "1.0");
      man.getMainAttributes().putValue("Class-Path", cp.trim());
      man.getMainAttributes().putValue("Main-Class", startClassName);
      man.write(jos);
      jos.close();
      return file;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public String stripNewLines(String argline) {
      return argline.replace("\n", " ").replace("\r", " ");
   }

   public String getDebugLine() {
      return this.debugLine;
   }

   public File getTempDirectory() {
      return this.tempDirectory;
   }

   public int getForkCount() {
      return this.forkCount;
   }

   public boolean isReuseForks() {
      return this.reuseForks;
   }
}
