package org.apache.maven.plugin.surefire;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ExcludesArtifactFilter;
import org.apache.maven.artifact.resolver.filter.ScopeArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.surefire.booterclient.ChecksumCalculator;
import org.apache.maven.plugin.surefire.booterclient.ForkConfiguration;
import org.apache.maven.plugin.surefire.booterclient.ForkStarter;
import org.apache.maven.plugin.surefire.util.DependencyScanner;
import org.apache.maven.plugin.surefire.util.DirectoryScanner;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.surefire.booter.ClassLoaderConfiguration;
import org.apache.maven.surefire.booter.Classpath;
import org.apache.maven.surefire.booter.ClasspathConfiguration;
import org.apache.maven.surefire.booter.ProviderConfiguration;
import org.apache.maven.surefire.booter.StartupConfiguration;
import org.apache.maven.surefire.booter.SurefireBooterForkException;
import org.apache.maven.surefire.booter.SurefireExecutionException;
import org.apache.maven.surefire.booter.TypeEncodedValue;
import org.apache.maven.surefire.report.ReporterConfiguration;
import org.apache.maven.surefire.shade.org.apache.maven.shared.artifact.filter.PatternIncludesArtifactFilter;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.FileUtils;
import org.apache.maven.surefire.suite.RunResult;
import org.apache.maven.surefire.testset.DirectoryScannerParameters;
import org.apache.maven.surefire.testset.RunOrderParameters;
import org.apache.maven.surefire.testset.TestArtifactInfo;
import org.apache.maven.surefire.testset.TestRequest;
import org.apache.maven.surefire.testset.TestSetFailedException;
import org.apache.maven.surefire.util.DefaultScanResult;
import org.apache.maven.surefire.util.RunOrder;
import org.apache.maven.toolchain.Toolchain;
import org.apache.maven.toolchain.ToolchainManager;

public abstract class AbstractSurefireMojo extends AbstractMojo implements SurefireExecutionParameters {
   @Parameter(
      defaultValue = "${plugin}",
      readonly = true
   )
   protected PluginDescriptor pluginDescriptor;
   @Parameter(
      property = "skipTests",
      defaultValue = "false"
   )
   protected boolean skipTests;
   /** @deprecated */
   @Parameter(
      property = "maven.test.skip.exec"
   )
   protected boolean skipExec;
   @Parameter(
      property = "maven.test.skip",
      defaultValue = "false"
   )
   protected boolean skip;
   @Component
   protected MavenProject project;
   @Parameter(
      defaultValue = "${basedir}"
   )
   protected File basedir;
   @Parameter(
      defaultValue = "${project.build.testOutputDirectory}"
   )
   protected File testClassesDirectory;
   @Parameter(
      defaultValue = "${project.build.outputDirectory}"
   )
   protected File classesDirectory;
   @Parameter(
      property = "maven.test.dependency.excludes"
   )
   private String[] classpathDependencyExcludes;
   @Parameter(
      defaultValue = ""
   )
   private String classpathDependencyScopeExclude;
   @Parameter(
      property = "maven.test.additionalClasspath"
   )
   private String[] additionalClasspathElements;
   @Parameter(
      defaultValue = "${project.build.testSourceDirectory}",
      required = true
   )
   protected File testSourceDirectory;
   @Parameter
   protected File includesFile;
   @Parameter
   protected List<String> excludes;
   @Parameter
   protected File excludesFile;
   @Parameter(
      defaultValue = "${localRepository}",
      required = true,
      readonly = true
   )
   protected ArtifactRepository localRepository;
   /** @deprecated */
   @Parameter
   protected Properties systemProperties;
   @Parameter
   protected Map<String, String> systemPropertyVariables;
   @Parameter
   protected File systemPropertiesFile;
   @Parameter
   protected Properties properties;
   @Parameter(
      property = "plugin.artifactMap",
      required = true,
      readonly = true
   )
   protected Map<String, Artifact> pluginArtifactMap;
   @Parameter(
      property = "project.artifactMap",
      readonly = true,
      required = true
   )
   protected Map<String, Artifact> projectArtifactMap;
   @Parameter(
      property = "surefire.reportNameSuffix",
      defaultValue = ""
   )
   protected String reportNameSuffix;
   @Parameter(
      property = "maven.test.redirectTestOutputToFile",
      defaultValue = "false"
   )
   protected boolean redirectTestOutputToFile;
   @Parameter(
      property = "failIfNoTests"
   )
   protected Boolean failIfNoTests;
   @Parameter(
      property = "forkMode",
      defaultValue = "once"
   )
   protected String forkMode;
   @Parameter(
      property = "jvm"
   )
   protected String jvm;
   @Parameter(
      property = "argLine"
   )
   protected String argLine;
   @Parameter
   protected Map<String, String> environmentVariables = new HashMap();
   @Parameter(
      property = "basedir"
   )
   protected File workingDirectory;
   @Parameter(
      property = "childDelegation",
      defaultValue = "false"
   )
   protected boolean childDelegation;
   @Parameter(
      property = "groups"
   )
   protected String groups;
   @Parameter(
      property = "excludedGroups"
   )
   protected String excludedGroups;
   @Parameter
   protected File[] suiteXmlFiles;
   @Parameter(
      property = "junitArtifactName",
      defaultValue = "junit:junit"
   )
   protected String junitArtifactName;
   @Parameter(
      property = "testNGArtifactName",
      defaultValue = "org.testng:testng"
   )
   protected String testNGArtifactName;
   @Parameter(
      property = "threadCount"
   )
   protected int threadCount;
   @Parameter(
      property = "forkCount",
      defaultValue = "1"
   )
   private String forkCount;
   @Parameter(
      property = "reuseForks",
      defaultValue = "true"
   )
   private boolean reuseForks;
   @Parameter(
      property = "perCoreThreadCount",
      defaultValue = "true"
   )
   protected boolean perCoreThreadCount;
   @Parameter(
      property = "useUnlimitedThreads",
      defaultValue = "false"
   )
   protected boolean useUnlimitedThreads;
   @Parameter(
      property = "parallel"
   )
   protected String parallel;
   @Parameter(
      property = "parallelOptimized",
      defaultValue = "true"
   )
   protected boolean parallelOptimized;
   @Parameter(
      property = "threadCountSuites",
      defaultValue = "0"
   )
   protected int threadCountSuites;
   @Parameter(
      property = "threadCountClasses",
      defaultValue = "0"
   )
   protected int threadCountClasses;
   @Parameter(
      property = "threadCountMethods",
      defaultValue = "0"
   )
   protected int threadCountMethods;
   @Parameter(
      property = "trimStackTrace",
      defaultValue = "true"
   )
   protected boolean trimStackTrace;
   @Component
   protected ArtifactResolver artifactResolver;
   @Component
   protected ArtifactFactory artifactFactory;
   @Parameter(
      defaultValue = "${project.pluginArtifactRepositories}"
   )
   protected List<ArtifactRepository> remoteRepositories;
   @Component
   protected ArtifactMetadataSource metadataSource;
   @Parameter(
      property = "disableXmlReport",
      defaultValue = "false"
   )
   protected boolean disableXmlReport;
   @Parameter(
      property = "enableAssertions",
      defaultValue = "true"
   )
   protected boolean enableAssertions;
   @Component
   protected MavenSession session;
   @Parameter(
      property = "objectFactory"
   )
   protected String objectFactory;
   @Parameter(
      defaultValue = "${session.parallel}",
      readonly = true
   )
   protected Boolean parallelMavenExecution;
   @Parameter(
      defaultValue = "filesystem"
   )
   protected String runOrder;
   @Parameter(
      property = "dependenciesToScan"
   )
   private String[] dependenciesToScan;
   @Component
   protected ToolchainManager toolchainManager;
   private Artifact surefireBooterArtifact;
   private Toolchain toolchain;
   private int effectiveForkCount = -1;
   public static final String THREAD_NUMBER_PLACEHOLDER = "${surefire.threadNumber}";
   public static final String FORK_NUMBER_PLACEHOLDER = "${surefire.forkNumber}";
   private SurefireDependencyResolver dependencyResolver;

   protected abstract String getPluginName();

   public void execute() throws MojoExecutionException, MojoFailureException {
      this.setupStuff();
      if (this.verifyParameters() && !this.hasExecutedBefore()) {
         DefaultScanResult scan = this.scanForTestClasses();
         if (!this.isValidSuiteXmlFileConfig() && scan.isEmpty()) {
            if (this.getEffectiveFailIfNoTests()) {
               throw new MojoFailureException("No tests were executed!  (Set -DfailIfNoTests=false to ignore this error.)");
            }

            this.handleSummary(RunResult.noTestsRun(), (Exception)null);
            return;
         }

         this.logReportsDirectory();
         this.executeAfterPreconditionsChecked(scan);
      }

   }

   private void setupStuff() {
      this.createDependencyResolver();
      this.surefireBooterArtifact = this.getSurefireBooterArtifact();
      this.toolchain = this.getToolchain();
   }

   private DefaultScanResult scanForTestClasses() {
      DefaultScanResult scan = this.scanDirectories();
      DefaultScanResult scanDeps = this.scanDependencies();
      return scan.append(scanDeps);
   }

   private DefaultScanResult scanDirectories() {
      return (new DirectoryScanner(this.getTestClassesDirectory(), this.getIncludeList(), this.getExcludeList(), this.getSpecificTests())).scan();
   }

   private DefaultScanResult scanDependencies() {
      if (this.getDependenciesToScan() == null) {
         return null;
      } else {
         try {
            return (new DependencyScanner(DependencyScanner.filter(this.project.getTestArtifacts(), Arrays.asList(this.getDependenciesToScan())), this.getIncludeList(), this.getExcludeList(), this.getSpecificTests())).scan();
         } catch (Exception var2) {
            throw new RuntimeException(var2);
         }
      }
   }

   boolean verifyParameters() throws MojoFailureException, MojoExecutionException {
      this.setProperties(new SurefireProperties(this.getProperties()));
      if (this.isSkipExecution()) {
         this.getLog().info((CharSequence)"Tests are skipped.");
         return false;
      } else {
         String jvmToUse = this.getJvm();
         if (this.toolchain != null) {
            this.getLog().info((CharSequence)("Toolchain in " + this.getPluginName() + "-plugin: " + this.toolchain));
            if (jvmToUse != null) {
               this.getLog().warn((CharSequence)("Toolchains are ignored, 'executable' parameter is set to " + jvmToUse));
            }
         }

         if (this.getTestClassesDirectory().exists() || this.getDependenciesToScan() != null && this.getDependenciesToScan().length != 0) {
            this.convertDeprecatedForkMode();
            this.ensureWorkingDirectoryExists();
            this.ensureParallelRunningCompatibility();
            this.ensureThreadCountWithPerThread();
            this.warnIfUselessUseSystemClassLoaderParameter();
            this.warnIfDefunctGroupsCombinations();
         } else {
            if (Boolean.TRUE.equals(this.getFailIfNoTests())) {
               throw new MojoFailureException("No tests to run!");
            }

            this.getLog().info((CharSequence)"No tests to run.");
         }

         return true;
      }
   }

   protected abstract boolean isSkipExecution();

   protected void executeAfterPreconditionsChecked(DefaultScanResult scanResult) throws MojoExecutionException, MojoFailureException {
      List<ProviderInfo> providers = this.createProviders();
      RunResult current = RunResult.noTestsRun();
      Exception firstForkException = null;
      Iterator i$ = providers.iterator();

      while(i$.hasNext()) {
         ProviderInfo provider = (ProviderInfo)i$.next();

         try {
            current = current.aggregate(this.executeProvider(provider, scanResult));
         } catch (SurefireBooterForkException var8) {
            if (firstForkException == null) {
               firstForkException = var8;
            }
         } catch (SurefireExecutionException var9) {
            if (firstForkException == null) {
               firstForkException = var9;
            }
         } catch (TestSetFailedException var10) {
            if (firstForkException == null) {
               firstForkException = var10;
            }
         }
      }

      if (firstForkException != null) {
         current = RunResult.failure(current, (Exception)firstForkException);
      }

      this.handleSummary(current, (Exception)firstForkException);
   }

   private void createDependencyResolver() {
      this.dependencyResolver = new SurefireDependencyResolver(this.getArtifactResolver(), this.getArtifactFactory(), this.getLog(), this.getLocalRepository(), this.getRemoteRepositories(), this.getMetadataSource(), this.getPluginName());
   }

   protected List<ProviderInfo> createProviders() throws MojoFailureException, MojoExecutionException {
      Artifact junitDepArtifact = this.getJunitDepArtifact();
      ProviderList wellKnownProviders = new ProviderList(new AbstractSurefireMojo.DynamicProviderInfo((String)null), new ProviderInfo[]{new AbstractSurefireMojo.TestNgProviderInfo(this.getTestNgArtifact()), new AbstractSurefireMojo.JUnitCoreProviderInfo(this.getJunitArtifact(), junitDepArtifact), new AbstractSurefireMojo.JUnit4ProviderInfo(this.getJunitArtifact(), junitDepArtifact), new AbstractSurefireMojo.JUnit3ProviderInfo()});
      return wellKnownProviders.resolve(this.getLog());
   }

   private SurefireProperties setupProperties() {
      SurefireProperties sysProps = null;

      try {
         sysProps = SurefireProperties.loadProperties(this.systemPropertiesFile);
      } catch (IOException var5) {
         String msg = "The system property file '" + this.systemPropertiesFile.getAbsolutePath() + "' can't be read.";
         if (this.getLog().isDebugEnabled()) {
            this.getLog().warn(msg, var5);
         } else {
            this.getLog().warn((CharSequence)msg);
         }
      }

      SurefireProperties result = SurefireProperties.calculateEffectiveProperties(this.getSystemProperties(), this.getSystemPropertyVariables(), this.getUserProperties(), sysProps);
      result.setProperty("basedir", (String)this.getBasedir().getAbsolutePath());
      result.setProperty("user.dir", (String)this.getWorkingDirectory().getAbsolutePath());
      result.setProperty("localRepository", (String)this.getLocalRepository().getBasedir());
      Iterator i$ = result.propertiesThatCannotBeSetASystemProperties().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         this.getLog().warn((CharSequence)(o + " cannot be set as system property, use <argLine>-D" + o + "=...<argLine> instead"));
      }

      if (this.getLog().isDebugEnabled()) {
         this.showToLog(result, this.getLog(), "system property");
      }

      return result;
   }

   public void showToLog(SurefireProperties props, Log log, String setting) {
      Iterator i$ = props.getStringKeySet().iterator();

      while(i$.hasNext()) {
         Object key = i$.next();
         String value = props.getProperty((String)key);
         log.debug((CharSequence)("Setting " + setting + " [" + key + "]=[" + value + "]"));
      }

   }

   private RunResult executeProvider(ProviderInfo provider, DefaultScanResult scanResult) throws MojoExecutionException, MojoFailureException, SurefireExecutionException, SurefireBooterForkException, TestSetFailedException {
      SurefireProperties effectiveProperties = this.setupProperties();
      ClassLoaderConfiguration classLoaderConfiguration = this.getClassLoaderConfiguration(this.isForking());
      RunOrderParameters runOrderParameters = new RunOrderParameters(this.getRunOrder(), this.getStatisticsFileName(this.getConfigChecksum()));
      RunResult result;
      if (this.isNotForking()) {
         createCopyAndReplaceForkNumPlaceholder(effectiveProperties, 1).copyToSystemProperties();
         InPluginVMSurefireStarter surefireStarter = this.createInprocessStarter(provider, classLoaderConfiguration, runOrderParameters);
         result = surefireStarter.runSuitesInProcess(scanResult);
      } else {
         ForkConfiguration forkConfiguration = this.getForkConfiguration();
         if (this.getLog().isDebugEnabled()) {
            this.showMap(this.getEnvironmentVariables(), "environment variable");
         }

         Properties originalSystemProperties = (Properties)System.getProperties().clone();

         try {
            ForkStarter forkStarter = this.createForkStarter(provider, forkConfiguration, classLoaderConfiguration, runOrderParameters, this.getLog());
            result = forkStarter.run(effectiveProperties, scanResult);
         } finally {
            System.setProperties(originalSystemProperties);
            this.cleanupForkConfiguration(forkConfiguration);
         }
      }

      return result;
   }

   public static SurefireProperties createCopyAndReplaceForkNumPlaceholder(SurefireProperties effectiveSystemProperties, int threadNumber) {
      SurefireProperties filteredProperties = new SurefireProperties(effectiveSystemProperties);
      String threadNumberString = String.valueOf(threadNumber);
      Iterator i$ = effectiveSystemProperties.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<Object, Object> entry = (Entry)i$.next();
         if (entry.getValue() instanceof String) {
            String value = (String)entry.getValue();
            value = value.replace("${surefire.threadNumber}", threadNumberString);
            value = value.replace("${surefire.forkNumber}", threadNumberString);
            filteredProperties.put(entry.getKey(), value);
         }
      }

      return filteredProperties;
   }

   protected void cleanupForkConfiguration(ForkConfiguration forkConfiguration) {
      if (!this.getLog().isDebugEnabled() && forkConfiguration != null) {
         File tempDirectory = forkConfiguration.getTempDirectory();

         try {
            FileUtils.deleteDirectory(tempDirectory);
         } catch (IOException var4) {
            this.getLog().warn((CharSequence)("Could not delete temp direcotry " + tempDirectory + " because " + var4.getMessage()));
         }
      }

   }

   protected abstract void handleSummary(RunResult var1, Exception var2) throws MojoExecutionException, MojoFailureException;

   protected void logReportsDirectory() {
      this.getLog().info((CharSequence)(StringUtils.capitalizeFirstLetter(this.getPluginName()) + " report directory: " + this.getReportsDirectory()));
   }

   final Toolchain getToolchain() {
      Toolchain tc = null;
      if (this.getToolchainManager() != null) {
         tc = this.getToolchainManager().getToolchainFromBuildContext("jdk", this.getSession());
      }

      return tc;
   }

   private void convertTestNGParameters() throws MojoExecutionException {
      if (this.getParallel() != null) {
         this.getProperties().setProperty("parallel", this.getParallel());
      }

      this.convertGroupParameters();
      if (this.getThreadCount() > 0) {
         this.getProperties().setProperty("threadcount", Integer.toString(this.getThreadCount()));
      }

      if (this.getObjectFactory() != null) {
         this.getProperties().setProperty("objectfactory", this.getObjectFactory());
      }

      if (this.getTestClassesDirectory() != null) {
         this.getProperties().setProperty("testng.test.classpath", this.getTestClassesDirectory().getAbsolutePath());
      }

      Artifact testNgArtifact = this.getTestNgArtifact();
      if (testNgArtifact != null) {
         DefaultArtifactVersion defaultArtifactVersion = new DefaultArtifactVersion(testNgArtifact.getVersion());
         this.getProperties().setProperty("testng.configurator", getConfiguratorName(defaultArtifactVersion));
      }

   }

   private static String getConfiguratorName(ArtifactVersion version) throws MojoExecutionException {
      try {
         VersionRange range = VersionRange.createFromVersionSpec("[4.7,5.1]");
         if (range.containsVersion(version)) {
            return "org.apache.maven.surefire.testng.conf.TestNG4751Configurator";
         } else {
            range = VersionRange.createFromVersionSpec("[5.2]");
            if (range.containsVersion(version)) {
               return "org.apache.maven.surefire.testng.conf.TestNG52Configurator";
            } else {
               range = VersionRange.createFromVersionSpec("[5.3,6.4]");
               if (range.containsVersion(version)) {
                  return "org.apache.maven.surefire.testng.conf.TestNGMapConfigurator";
               } else {
                  range = VersionRange.createFromVersionSpec("[6.5,)");
                  if (range.containsVersion(version)) {
                     return "org.apache.maven.surefire.testng.conf.TestNG652Configurator";
                  } else {
                     throw new MojoExecutionException("Unknown TestNG version " + version);
                  }
               }
            }
         }
      } catch (InvalidVersionSpecificationException var2) {
         throw new MojoExecutionException("Bug in plugin. Please report it with the attached stacktrace", var2);
      }
   }

   private void convertGroupParameters() {
      if (this.getExcludedGroups() != null) {
         this.getProperties().setProperty("excludegroups", this.getExcludedGroups());
      }

      if (this.getGroups() != null) {
         this.getProperties().setProperty("groups", this.getGroups());
      }

   }

   protected boolean isAnyConcurrencySelected() {
      return this.getParallel() != null && this.getParallel().trim().length() > 0;
   }

   protected boolean isAnyGroupsSelected() {
      return this.getGroups() != null || this.getExcludedGroups() != null;
   }

   private void convertJunitCoreParameters() throws MojoExecutionException {
      checkThreadCountEntity(this.getThreadCountSuites(), "suites");
      checkThreadCountEntity(this.getThreadCountClasses(), "classes");
      checkThreadCountEntity(this.getThreadCountMethods(), "methods");
      String usedParallel = this.getParallel() != null ? this.getParallel() : "none";
      if (!"none".equals(usedParallel)) {
         this.checkNonForkedThreads(this.parallel);
      }

      String usedThreadCount = Integer.toString(this.getThreadCount());
      this.getProperties().setProperty("parallel", usedParallel);
      this.getProperties().setProperty("threadcount", usedThreadCount);
      this.getProperties().setProperty("perCoreThreadCount", Boolean.toString(this.getPerCoreThreadCount()));
      this.getProperties().setProperty("useUnlimitedThreads", Boolean.toString(this.getUseUnlimitedThreads()));
      this.getProperties().setProperty("threadcountsuites", Integer.toString(this.getThreadCountSuites()));
      this.getProperties().setProperty("threadcountclasses", Integer.toString(this.getThreadCountClasses()));
      this.getProperties().setProperty("threadcountmethods", Integer.toString(this.getThreadCountMethods()));
      this.getProperties().setProperty("paralleltimeout", Double.toString(this.getParallelTestsTimeoutInSeconds()));
      this.getProperties().setProperty("paralleltimeoutforced", Double.toString(this.getParallelTestsTimeoutForcedInSeconds()));
      this.getProperties().setProperty("paralleloptimization", Boolean.toString(this.isParallelOptimized()));
      String message = "parallel='" + usedParallel + '\'' + ", perCoreThreadCount=" + this.getPerCoreThreadCount() + ", threadCount=" + usedThreadCount + ", useUnlimitedThreads=" + this.getUseUnlimitedThreads() + ", threadCountSuites=" + this.getThreadCountSuites() + ", threadCountClasses=" + this.getThreadCountClasses() + ", threadCountMethods=" + this.getThreadCountMethods() + ", parallelOptimized=" + this.isParallelOptimized();
      this.getLog().info((CharSequence)message);
   }

   private void checkNonForkedThreads(String parallel) throws MojoExecutionException {
      if ("suites".equals(parallel)) {
         if (!this.getUseUnlimitedThreads() && !(this.getThreadCount() > 0 ^ this.getThreadCountSuites() > 0)) {
            throw new MojoExecutionException("Use threadCount or threadCountSuites > 0 or useUnlimitedThreads=true for parallel='suites'");
         }

         this.setThreadCountClasses(0);
         this.setThreadCountMethods(0);
      } else if ("classes".equals(parallel)) {
         if (!this.getUseUnlimitedThreads() && !(this.getThreadCount() > 0 ^ this.getThreadCountClasses() > 0)) {
            throw new MojoExecutionException("Use threadCount or threadCountClasses > 0 or useUnlimitedThreads=true for parallel='classes'");
         }

         this.setThreadCountSuites(0);
         this.setThreadCountMethods(0);
      } else if ("methods".equals(parallel)) {
         if (!this.getUseUnlimitedThreads() && !(this.getThreadCount() > 0 ^ this.getThreadCountMethods() > 0)) {
            throw new MojoExecutionException("Use threadCount or threadCountMethods > 0 or useUnlimitedThreads=true for parallel='methods'");
         }

         this.setThreadCountSuites(0);
         this.setThreadCountClasses(0);
      } else if ("suitesAndClasses".equals(parallel)) {
         if (!this.getUseUnlimitedThreads() && !this.onlyThreadCount() && (this.getThreadCountSuites() <= 0 || this.getThreadCountClasses() <= 0 || this.getThreadCount() != 0 || this.getThreadCountMethods() != 0) && (this.getThreadCount() <= 0 || this.getThreadCountSuites() <= 0 || this.getThreadCountClasses() <= 0 || this.getThreadCountMethods() != 0) && (this.getThreadCount() <= 0 || this.getThreadCountSuites() <= 0 || this.getThreadCount() <= this.getThreadCountSuites() || this.getThreadCountClasses() != 0 || this.getThreadCountMethods() != 0)) {
            throw new MojoExecutionException("Use useUnlimitedThreads=true, or only threadCount > 0, or (threadCountSuites > 0 and threadCountClasses > 0), or (threadCount > 0 and threadCountSuites > 0 and threadCountClasses > 0) or (threadCount > 0 and threadCountSuites > 0 and threadCount > threadCountSuites) for parallel='suitesAndClasses' or 'both'");
         }

         this.setThreadCountMethods(0);
      } else if ("suitesAndMethods".equals(parallel)) {
         if (!this.getUseUnlimitedThreads() && !this.onlyThreadCount() && (this.getThreadCountSuites() <= 0 || this.getThreadCountMethods() <= 0 || this.getThreadCount() != 0 || this.getThreadCountClasses() != 0) && (this.getThreadCount() <= 0 || this.getThreadCountSuites() <= 0 || this.getThreadCountMethods() <= 0 || this.getThreadCountClasses() != 0) && (this.getThreadCount() <= 0 || this.getThreadCountSuites() <= 0 || this.getThreadCount() <= this.getThreadCountSuites() || this.getThreadCountClasses() != 0 || this.getThreadCountMethods() != 0)) {
            throw new MojoExecutionException("Use useUnlimitedThreads=true, or only threadCount > 0, or (threadCountSuites > 0 and threadCountMethods > 0), or (threadCount > 0 and threadCountSuites > 0 and threadCountMethods > 0), or (threadCount > 0 and threadCountSuites > 0 and threadCount > threadCountSuites) for parallel='suitesAndMethods'");
         }

         this.setThreadCountClasses(0);
      } else if (!"both".equals(parallel) && !"classesAndMethods".equals(parallel)) {
         if (!"all".equals(parallel)) {
            throw new MojoExecutionException("Illegal parallel='" + parallel + "'");
         }

         if (!this.getUseUnlimitedThreads() && !this.onlyThreadCount() && (this.getThreadCountSuites() <= 0 || this.getThreadCountClasses() <= 0 || this.getThreadCountMethods() <= 0) && (this.getThreadCount() <= 0 || this.getThreadCountSuites() <= 0 || this.getThreadCountClasses() <= 0 || this.getThreadCountMethods() != 0 || this.getThreadCount() <= this.getThreadCountSuites() + this.getThreadCountClasses())) {
            throw new MojoExecutionException("Use useUnlimitedThreads=true, or only threadCount > 0, or (threadCountSuites > 0 and threadCountClasses > 0 and threadCountMethods > 0), or every thread-count is specified, or (threadCount > 0 and threadCountSuites > 0 and threadCountClasses > 0 and threadCount > threadCountSuites + threadCountClasses) for parallel='all'");
         }
      } else {
         if (!this.getUseUnlimitedThreads() && !this.onlyThreadCount() && (this.getThreadCountClasses() <= 0 || this.getThreadCountMethods() <= 0 || this.getThreadCount() != 0 || this.getThreadCountSuites() != 0) && (this.getThreadCount() <= 0 || this.getThreadCountClasses() <= 0 || this.getThreadCountMethods() <= 0 || this.getThreadCountSuites() != 0) && (this.getThreadCount() <= 0 || this.getThreadCountClasses() <= 0 || this.getThreadCount() <= this.getThreadCountClasses() || this.getThreadCountSuites() != 0 || this.getThreadCountMethods() != 0)) {
            throw new MojoExecutionException("Use useUnlimitedThreads=true, or only threadCount > 0, or (threadCountClasses > 0 and threadCountMethods > 0), or (threadCount > 0 and threadCountClasses > 0 and threadCountMethods > 0), or (threadCount > 0 and threadCountClasses > 0 and threadCount > threadCountClasses) for parallel='both' or parallel='classesAndMethods'");
         }

         this.setThreadCountSuites(0);
      }

   }

   private boolean onlyThreadCount() {
      return this.getThreadCount() > 0 && this.getThreadCountSuites() == 0 && this.getThreadCountClasses() == 0 && this.getThreadCountMethods() == 0;
   }

   private static void checkThreadCountEntity(int count, String entity) throws MojoExecutionException {
      if (count < 0) {
         throw new MojoExecutionException("parallel maven execution does not allow negative thread-count" + entity);
      }
   }

   private boolean isJunit47Compatible(Artifact artifact) {
      return this.dependencyResolver.isWithinVersionSpec(artifact, "[4.7,)");
   }

   private boolean isAnyJunit4(Artifact artifact) {
      return this.dependencyResolver.isWithinVersionSpec(artifact, "[4.0,)");
   }

   static boolean isForkModeNever(String forkMode) {
      return "never".equals(forkMode);
   }

   boolean isForking() {
      return 0 < this.getEffectiveForkCount();
   }

   String getEffectiveForkMode() {
      String forkMode1 = this.getForkMode();
      return this.toolchain != null && isForkModeNever(forkMode1) ? "once" : ForkConfiguration.getEffectiveForkMode(forkMode1);
   }

   private List<RunOrder> getRunOrders() {
      String runOrderString = this.getRunOrder();
      RunOrder[] runOrder = runOrderString == null ? RunOrder.DEFAULT : RunOrder.valueOfMulti(runOrderString);
      return Arrays.asList(runOrder);
   }

   private boolean requiresRunHistory() {
      List<RunOrder> runOrders = this.getRunOrders();
      return runOrders.contains(RunOrder.BALANCED) || runOrders.contains(RunOrder.FAILEDFIRST);
   }

   private boolean getEffectiveFailIfNoTests() {
      if (this.isSpecificTestSpecified()) {
         if (this.getFailIfNoSpecifiedTests() != null) {
            return this.getFailIfNoSpecifiedTests();
         } else {
            return this.getFailIfNoTests() != null ? this.getFailIfNoTests() : true;
         }
      } else {
         return this.getFailIfNoTests() != null && this.getFailIfNoTests();
      }
   }

   private ProviderConfiguration createProviderConfiguration(RunOrderParameters runOrderParameters) throws MojoExecutionException, MojoFailureException {
      ReporterConfiguration reporterConfiguration = new ReporterConfiguration(this.getReportsDirectory(), this.isTrimStackTrace());
      Artifact testNgArtifact = this.getTestNgArtifact();
      DirectoryScannerParameters directoryScannerParameters = null;
      boolean isTestNg = testNgArtifact != null;
      TestArtifactInfo testNg = isTestNg ? new TestArtifactInfo(testNgArtifact.getVersion(), testNgArtifact.getClassifier()) : null;
      List<File> testXml = this.getSuiteXmlFiles() != null ? Arrays.asList(this.getSuiteXmlFiles()) : null;
      TestRequest testSuiteDefinition = new TestRequest(testXml, this.getTestSourceDirectory(), this.getTest(), this.getTestMethod());
      boolean failIfNoTests;
      if (this.isValidSuiteXmlFileConfig() && this.getTest() == null) {
         failIfNoTests = this.getFailIfNoTests() != null && this.getFailIfNoTests();
         if (!isTestNg) {
            throw new MojoExecutionException("suiteXmlFiles is configured, but there is no TestNG dependency");
         }
      } else {
         if (this.isSpecificTestSpecified()) {
            failIfNoTests = this.getEffectiveFailIfNoTests();
            this.setFailIfNoTests(failIfNoTests);
         } else {
            failIfNoTests = this.getFailIfNoTests() != null && this.getFailIfNoTests();
         }

         List<String> includes = this.getIncludeList();
         List<String> excludes = this.getExcludeList();
         List<String> specificTests = this.getSpecificTests();
         directoryScannerParameters = new DirectoryScannerParameters(this.getTestClassesDirectory(), includes, excludes, specificTests, failIfNoTests, this.getRunOrder());
      }

      Properties providerProperties = this.getProperties();
      return new ProviderConfiguration(directoryScannerParameters, runOrderParameters, failIfNoTests, reporterConfiguration, testNg, testSuiteDefinition, providerProperties, (TypeEncodedValue)null, false);
   }

   public String getStatisticsFileName(String configurationHash) {
      return this.getReportsDirectory().getParentFile().getParentFile() + File.separator + ".surefire-" + configurationHash;
   }

   StartupConfiguration createStartupConfiguration(ProviderInfo provider, ClassLoaderConfiguration classLoaderConfiguration) throws MojoExecutionException, MojoFailureException {
      try {
         provider.addProviderProperties();
         String providerName = provider.getProviderName();
         Classpath providerClasspath = ClasspathCache.getCachedClassPath(providerName);
         if (providerClasspath == null) {
            providerClasspath = provider.getProviderClasspath();
            ClasspathCache.setCachedClasspath(providerName, providerClasspath);
         }

         Artifact surefireArtifact = this.getCommonArtifact();
         Classpath inprocClassPath = providerClasspath.addClassPathElementUrl(surefireArtifact.getFile().getAbsolutePath()).addClassPathElementUrl(this.getApiArtifact().getFile().getAbsolutePath());
         Classpath testClasspath = this.generateTestClasspath();
         this.getLog().debug((CharSequence)testClasspath.getLogMessage("test"));
         this.getLog().debug((CharSequence)providerClasspath.getLogMessage("provider"));
         this.getLog().debug((CharSequence)testClasspath.getCompactLogMessage("test(compact)"));
         this.getLog().debug((CharSequence)providerClasspath.getCompactLogMessage("provider(compact)"));
         ClasspathConfiguration classpathConfiguration = new ClasspathConfiguration(testClasspath, providerClasspath, inprocClassPath, this.effectiveIsEnableAssertions(), this.isChildDelegation());
         return new StartupConfiguration(providerName, classpathConfiguration, classLoaderConfiguration, this.isForking(), false);
      } catch (ArtifactResolutionException var9) {
         throw new MojoExecutionException("Unable to generate classpath: " + var9, var9);
      } catch (ArtifactNotFoundException var10) {
         throw new MojoExecutionException("Unable to generate classpath: " + var10, var10);
      } catch (InvalidVersionSpecificationException var11) {
         throw new MojoExecutionException("Unable to generate classpath: " + var11, var11);
      }
   }

   private Artifact getCommonArtifact() {
      return (Artifact)this.getPluginArtifactMap().get("org.apache.maven.surefire:maven-surefire-common");
   }

   private Artifact getApiArtifact() {
      return (Artifact)this.getPluginArtifactMap().get("org.apache.maven.surefire:surefire-api");
   }

   private StartupReportConfiguration getStartupReportConfiguration(String configChecksum) {
      return new StartupReportConfiguration(this.isUseFile(), this.isPrintSummary(), this.getReportFormat(), this.isRedirectTestOutputToFile(), this.isDisableXmlReport(), this.getReportsDirectory(), this.isTrimStackTrace(), this.getReportNameSuffix(), configChecksum, this.requiresRunHistory());
   }

   private boolean isSpecificTestSpecified() {
      return this.getTest() != null;
   }

   private boolean isValidSuiteXmlFileConfig() {
      return this.getSuiteXmlFiles() != null && this.getSuiteXmlFiles().length > 0;
   }

   @Nonnull
   private List<String> readListFromFile(@Nonnull File file) {
      this.getLog().debug((CharSequence)("Reading list from: " + file));
      if (!file.exists()) {
         throw new RuntimeException("Failed to load list from file: " + file);
      } else {
         List list;
         try {
            list = FileUtils.loadFile(file);
         } catch (IOException var5) {
            throw new RuntimeException("Failed to load list from file: " + file, var5);
         }

         if (this.getLog().isDebugEnabled()) {
            this.getLog().debug((CharSequence)"List contents:");
            Iterator i$ = list.iterator();

            while(i$.hasNext()) {
               String entry = (String)i$.next();
               this.getLog().debug((CharSequence)("  " + entry));
            }
         }

         return list;
      }
   }

   private void maybeAppendList(List<String> base, List<String> list) {
      if (list != null) {
         base.addAll(list);
      }

   }

   @Nonnull
   private List<String> getExcludeList() {
      List<String> excludes = null;
      if (this.isSpecificTestSpecified()) {
         excludes = new ArrayList();
      } else {
         if (this.getExcludesFile() != null) {
            excludes = this.readListFromFile(this.getExcludesFile());
         }

         if (excludes == null) {
            excludes = this.getExcludes();
         } else {
            this.maybeAppendList((List)excludes, this.getExcludes());
         }

         if (excludes == null || ((List)excludes).size() == 0) {
            excludes = Arrays.asList("**/*$*");
         }
      }

      return this.filterNulls((List)excludes);
   }

   private List<String> getIncludeList() {
      List<String> includes = null;
      if (this.isSpecificTestSpecified() && !this.isMultipleExecutionBlocksDetected()) {
         includes = this.getSpecificTests();
      } else {
         if (this.getIncludesFile() != null) {
            includes = this.readListFromFile(this.getIncludesFile());
         }

         if (includes == null) {
            includes = this.getIncludes();
         } else {
            this.maybeAppendList(includes, this.getIncludes());
         }
      }

      if (includes == null || includes.size() == 0) {
         includes = Arrays.asList(this.getDefaultIncludes());
      }

      return this.filterNulls(includes);
   }

   @Nonnull
   private List<String> filterNulls(@Nonnull List<String> toFilter) {
      List<String> result = new ArrayList(toFilter.size());
      Iterator i$ = toFilter.iterator();

      while(i$.hasNext()) {
         String item = (String)i$.next();
         if (item != null) {
            result.add(item);
         }
      }

      return result;
   }

   private boolean isMultipleExecutionBlocksDetected() {
      MavenProject project = this.getProject();
      if (project != null) {
         String key = this.getPluginDescriptor().getPluginLookupKey();
         Plugin plugin = (Plugin)project.getBuild().getPluginsAsMap().get(key);
         if (plugin != null) {
            List executions = plugin.getExecutions();
            return executions != null && executions.size() > 1;
         }
      }

      return false;
   }

   private List<String> getSpecificTests() {
      if (!this.isSpecificTestSpecified()) {
         return Collections.emptyList();
      } else {
         List<String> specificTests = new ArrayList();
         String[] testRegexes = StringUtils.split(this.getTest(), ",");
         String[] arr$ = testRegexes;
         int len$ = testRegexes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String testRegexe = arr$[i$];
            String testRegex = testRegexe;
            if (testRegexe.endsWith(".java")) {
               testRegex = testRegexe.substring(0, testRegexe.length() - 5);
            }

            testRegex = testRegex.replace('.', '/');
            specificTests.add("**/" + testRegex + ".java");
         }

         return specificTests;
      }
   }

   private Artifact getTestNgArtifact() throws MojoExecutionException {
      Artifact artifact = (Artifact)this.getProjectArtifactMap().get(this.getTestNGArtifactName());
      if (artifact != null) {
         VersionRange range = this.createVersionRange();
         if (!range.containsVersion(new DefaultArtifactVersion(artifact.getVersion()))) {
            throw new MojoExecutionException("TestNG support requires version 4.7 or above. You have declared version " + artifact.getVersion());
         }
      }

      return artifact;
   }

   private VersionRange createVersionRange() {
      try {
         return VersionRange.createFromVersionSpec("[4.7,)");
      } catch (InvalidVersionSpecificationException var2) {
         throw new RuntimeException(var2);
      }
   }

   private Artifact getJunitArtifact() {
      return (Artifact)this.getProjectArtifactMap().get(this.getJunitArtifactName());
   }

   private Artifact getJunitDepArtifact() {
      return (Artifact)this.getProjectArtifactMap().get("junit:junit-dep");
   }

   protected ForkStarter createForkStarter(ProviderInfo provider, ForkConfiguration forkConfiguration, ClassLoaderConfiguration classLoaderConfiguration, RunOrderParameters runOrderParameters, Log log) throws MojoExecutionException, MojoFailureException {
      StartupConfiguration startupConfiguration = this.createStartupConfiguration(provider, classLoaderConfiguration);
      String configChecksum = this.getConfigChecksum();
      StartupReportConfiguration startupReportConfiguration = this.getStartupReportConfiguration(configChecksum);
      ProviderConfiguration providerConfiguration = this.createProviderConfiguration(runOrderParameters);
      return new ForkStarter(providerConfiguration, startupConfiguration, forkConfiguration, this.getForkedProcessTimeoutInSeconds(), startupReportConfiguration, log);
   }

   protected InPluginVMSurefireStarter createInprocessStarter(ProviderInfo provider, ClassLoaderConfiguration classLoaderConfiguration, RunOrderParameters runOrderParameters) throws MojoExecutionException, MojoFailureException {
      StartupConfiguration startupConfiguration = this.createStartupConfiguration(provider, classLoaderConfiguration);
      String configChecksum = this.getConfigChecksum();
      StartupReportConfiguration startupReportConfiguration = this.getStartupReportConfiguration(configChecksum);
      ProviderConfiguration providerConfiguration = this.createProviderConfiguration(runOrderParameters);
      return new InPluginVMSurefireStarter(startupConfiguration, providerConfiguration, startupReportConfiguration);
   }

   protected ForkConfiguration getForkConfiguration() {
      File tmpDir = this.getSurefireTempDir();
      tmpDir.mkdirs();
      Artifact shadeFire = (Artifact)this.getPluginArtifactMap().get("org.apache.maven.surefire:surefire-shadefire");
      Classpath bootClasspathConfiguration = this.getArtifactClasspath(shadeFire != null ? shadeFire : this.surefireBooterArtifact);
      return new ForkConfiguration(bootClasspathConfiguration, tmpDir, this.getEffectiveDebugForkedProcess(), this.getEffectiveJvm(), this.getWorkingDirectory() != null ? this.getWorkingDirectory() : this.getBasedir(), this.getProject().getModel().getProperties(), this.getArgLine(), this.getEnvironmentVariables(), this.getLog().isDebugEnabled(), this.getEffectiveForkCount(), this.reuseForks);
   }

   private void convertDeprecatedForkMode() {
      String effectiveForkMode = this.getEffectiveForkMode();
      if ("perthread".equals(effectiveForkMode)) {
         this.forkCount = String.valueOf(this.threadCount);
      } else if ("never".equals(effectiveForkMode)) {
         this.forkCount = "0";
      } else if ("always".equals(effectiveForkMode)) {
         this.forkCount = "1";
         this.reuseForks = false;
      }

      if (!"once".equals(this.getForkMode())) {
         this.getLog().warn((CharSequence)"The parameter forkMode is deprecated since version 2.14. Use forkCount and reuseForks instead.");
      }

   }

   protected int getEffectiveForkCount() {
      if (this.effectiveForkCount < 0) {
         try {
            this.effectiveForkCount = this.convertWithCoreCount(this.forkCount);
         } catch (NumberFormatException var2) {
         }

         if (this.effectiveForkCount < 0) {
            throw new IllegalArgumentException("Fork count " + this.forkCount.trim() + " is not a legal value.");
         }
      }

      return this.effectiveForkCount;
   }

   protected int convertWithCoreCount(String count) {
      String trimmed = count.trim();
      if (trimmed.endsWith("C")) {
         double multiplier = Double.parseDouble(trimmed.substring(0, trimmed.length() - 1));
         double calculated = multiplier * (double)Runtime.getRuntime().availableProcessors();
         return calculated > 0.0D ? Math.max((int)calculated, 1) : 0;
      } else {
         return Integer.parseInt(trimmed);
      }
   }

   private String getEffectiveDebugForkedProcess() {
      String debugForkedProcess = this.getDebugForkedProcess();
      return "true".equals(debugForkedProcess) ? "-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005" : debugForkedProcess;
   }

   private String getEffectiveJvm() {
      String jvmToUse = this.getJvm();
      if (this.toolchain != null && jvmToUse == null) {
         jvmToUse = this.toolchain.findTool("java");
      }

      if (StringUtils.isEmpty(jvmToUse)) {
         jvmToUse = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
         this.getLog().debug((CharSequence)("Using JVM: " + jvmToUse));
      }

      return jvmToUse;
   }

   private Artifact getSurefireBooterArtifact() {
      Artifact artifact = (Artifact)this.getPluginArtifactMap().get("org.apache.maven.surefire:surefire-booter");
      if (artifact == null) {
         throw new RuntimeException("Unable to locate surefire-booter in the list of plugin artifacts");
      } else {
         artifact.isSnapshot();
         return artifact;
      }
   }

   private File getSurefireTempDir() {
      return new File(this.getReportsDirectory().getParentFile(), "surefire");
   }

   private String getConfigChecksum() {
      ChecksumCalculator checksum = new ChecksumCalculator();
      checksum.add(this.getPluginName());
      checksum.add(this.isSkipTests());
      checksum.add(this.isSkipExec());
      checksum.add(this.isSkip());
      checksum.add(this.getTestClassesDirectory());
      checksum.add(this.getClassesDirectory());
      checksum.add((Object[])this.getClasspathDependencyExcludes());
      checksum.add(this.getClasspathDependencyScopeExclude());
      checksum.add((Object[])this.getAdditionalClasspathElements());
      checksum.add(this.getReportsDirectory());
      checksum.add(this.getTestSourceDirectory());
      checksum.add(this.getTest());
      checksum.add(this.getIncludes());
      checksum.add(this.getExcludes());
      checksum.add(this.getLocalRepository());
      checksum.add((Map)this.getSystemProperties());
      checksum.add(this.getSystemPropertyVariables());
      checksum.add(this.getSystemPropertiesFile());
      checksum.add((Map)this.getProperties());
      checksum.add(this.isPrintSummary());
      checksum.add(this.getReportFormat());
      checksum.add(this.getReportNameSuffix());
      checksum.add(this.isUseFile());
      checksum.add(this.isRedirectTestOutputToFile());
      checksum.add(this.getForkMode());
      checksum.add(this.getForkCount());
      checksum.add(this.isReuseForks());
      checksum.add(this.getJvm());
      checksum.add(this.getArgLine());
      checksum.add(this.getDebugForkedProcess());
      checksum.add(this.getForkedProcessTimeoutInSeconds());
      checksum.add(this.getParallelTestsTimeoutInSeconds());
      checksum.add(this.getParallelTestsTimeoutForcedInSeconds());
      checksum.add(this.getEnvironmentVariables());
      checksum.add(this.getWorkingDirectory());
      checksum.add(this.isChildDelegation());
      checksum.add(this.getGroups());
      checksum.add(this.getExcludedGroups());
      checksum.add((Object[])this.getSuiteXmlFiles());
      checksum.add(this.getJunitArtifact());
      checksum.add(this.getTestNGArtifactName());
      checksum.add(this.getThreadCount());
      checksum.add(this.getThreadCountSuites());
      checksum.add(this.getThreadCountClasses());
      checksum.add(this.getThreadCountMethods());
      checksum.add(this.getPerCoreThreadCount());
      checksum.add(this.getUseUnlimitedThreads());
      checksum.add(this.getParallel());
      checksum.add(this.isParallelOptimized());
      checksum.add(this.isTrimStackTrace());
      checksum.add(this.getRemoteRepositories());
      checksum.add(this.isDisableXmlReport());
      checksum.add(this.isUseSystemClassLoader());
      checksum.add(this.isUseManifestOnlyJar());
      checksum.add(this.isEnableAssertions());
      checksum.add(this.getObjectFactory());
      checksum.add(this.getFailIfNoTests());
      checksum.add(this.getRunOrder());
      checksum.add((Object[])this.getDependenciesToScan());
      this.addPluginSpecificChecksumItems(checksum);
      return checksum.getSha1();
   }

   protected void addPluginSpecificChecksumItems(ChecksumCalculator checksum) {
   }

   protected boolean hasExecutedBefore() {
      String configChecksum = this.getConfigChecksum();
      Map<String, String> pluginContext = this.getPluginContext();
      if (pluginContext.containsKey(configChecksum)) {
         this.getLog().info((CharSequence)"Skipping execution of surefire because it has already been run for this configuration");
         return true;
      } else {
         pluginContext.put(configChecksum, configChecksum);
         return false;
      }
   }

   protected ClassLoaderConfiguration getClassLoaderConfiguration(boolean isForking) {
      return isForking ? new ClassLoaderConfiguration(this.isUseSystemClassLoader(), this.isUseManifestOnlyJar()) : new ClassLoaderConfiguration(false, false);
   }

   protected abstract String[] getDefaultIncludes();

   Classpath generateTestClasspath() throws InvalidVersionSpecificationException, MojoFailureException, ArtifactResolutionException, ArtifactNotFoundException, MojoExecutionException {
      List<String> classpath = new ArrayList(2 + this.getProject().getArtifacts().size());
      classpath.add(this.getTestClassesDirectory().getAbsolutePath());
      classpath.add(this.getClassesDirectory().getAbsolutePath());
      Set<Artifact> classpathArtifacts = this.getProject().getArtifacts();
      if (this.getClasspathDependencyScopeExclude() != null && !this.getClasspathDependencyScopeExclude().equals("")) {
         ArtifactFilter dependencyFilter = new ScopeArtifactFilter(this.getClasspathDependencyScopeExclude());
         classpathArtifacts = this.filterArtifacts(classpathArtifacts, dependencyFilter);
      }

      if (this.getClasspathDependencyExcludes() != null) {
         ArtifactFilter dependencyFilter = new PatternIncludesArtifactFilter(Arrays.asList(this.getClasspathDependencyExcludes()));
         classpathArtifacts = this.filterArtifacts(classpathArtifacts, dependencyFilter);
      }

      Iterator i$ = classpathArtifacts.iterator();

      while(i$.hasNext()) {
         Artifact artifact = (Artifact)i$.next();
         if (artifact.getArtifactHandler().isAddedToClasspath()) {
            File file = artifact.getFile();
            if (file != null) {
               classpath.add(file.getPath());
            }
         }
      }

      if (this.getAdditionalClasspathElements() != null) {
         String[] arr$ = this.getAdditionalClasspathElements();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String classpathElement = arr$[i$];
            if (classpathElement != null) {
               classpath.add(classpathElement);
            }
         }
      }

      if (this.getTestNgArtifact() != null) {
         this.addTestNgUtilsArtifacts(classpath);
      }

      return new Classpath(classpath);
   }

   void addTestNgUtilsArtifacts(List<String> classpath) throws ArtifactResolutionException, ArtifactNotFoundException {
      Artifact surefireArtifact = (Artifact)this.getPluginArtifactMap().get("org.apache.maven.surefire:surefire-booter");
      String surefireVersion = surefireArtifact.getBaseVersion();
      Artifact[] extraTestNgArtifacts = new Artifact[]{this.getArtifactFactory().createArtifact("org.apache.maven.surefire", "surefire-testng-utils", surefireVersion, "runtime", "jar"), this.getArtifactFactory().createArtifact("org.apache.maven.surefire", "surefire-grouper", surefireVersion, "runtime", "jar")};
      Artifact[] arr$ = extraTestNgArtifacts;
      int len$ = extraTestNgArtifacts.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Artifact artifact = arr$[i$];
         this.getArtifactResolver().resolve(artifact, this.getRemoteRepositories(), this.getLocalRepository());
         String path = artifact.getFile().getPath();
         classpath.add(path);
      }

   }

   private Set<Artifact> filterArtifacts(Set<Artifact> artifacts, ArtifactFilter filter) {
      Set<Artifact> filteredArtifacts = new LinkedHashSet();
      Iterator i$ = artifacts.iterator();

      while(i$.hasNext()) {
         Artifact artifact = (Artifact)i$.next();
         if (!filter.include(artifact)) {
            filteredArtifacts.add(artifact);
         }
      }

      return filteredArtifacts;
   }

   private void showMap(Map<?, ?> map, String setting) {
      Iterator i$ = map.keySet().iterator();

      while(i$.hasNext()) {
         Object o = i$.next();
         String key = (String)o;
         String value = (String)map.get(key);
         this.getLog().debug((CharSequence)("Setting " + setting + " [" + key + "]=[" + value + "]"));
      }

   }

   private ArtifactResolutionResult resolveArtifact(Artifact filteredArtifact, Artifact providerArtifact) {
      ArtifactFilter filter = null;
      if (filteredArtifact != null) {
         filter = new ExcludesArtifactFilter(Collections.singletonList(filteredArtifact.getGroupId() + ":" + filteredArtifact.getArtifactId()));
      }

      Artifact originatingArtifact = this.getArtifactFactory().createBuildArtifact("dummy", "dummy", "1.0", "jar");

      try {
         return this.getArtifactResolver().resolveTransitively(Collections.singleton(providerArtifact), originatingArtifact, (ArtifactRepository)this.getLocalRepository(), (List)this.getRemoteRepositories(), (ArtifactMetadataSource)this.getMetadataSource(), (ArtifactFilter)filter);
      } catch (ArtifactResolutionException var6) {
         throw new RuntimeException(var6);
      } catch (ArtifactNotFoundException var7) {
         throw new RuntimeException(var7);
      }
   }

   private Classpath getArtifactClasspath(Artifact surefireArtifact) {
      Classpath existing = ClasspathCache.getCachedClassPath(surefireArtifact.getArtifactId());
      if (existing == null) {
         ArtifactResolutionResult result = this.resolveArtifact((Artifact)null, surefireArtifact);
         List<String> items = new ArrayList();
         Iterator i$ = result.getArtifacts().iterator();

         while(i$.hasNext()) {
            Object o = i$.next();
            Artifact artifact = (Artifact)o;
            this.getLog().debug((CharSequence)("Adding to " + this.getPluginName() + " booter test classpath: " + artifact.getFile().getAbsolutePath() + " Scope: " + artifact.getScope()));
            items.add(artifact.getFile().getAbsolutePath());
         }

         existing = new Classpath(items);
         ClasspathCache.setCachedClasspath(surefireArtifact.getArtifactId(), existing);
      }

      return existing;
   }

   private Properties getUserProperties() {
      Properties props = null;

      try {
         Method getUserProperties = this.getSession().getClass().getMethod("getUserProperties");
         props = (Properties)getUserProperties.invoke(this.getSession());
      } catch (Exception var4) {
         String msg = "Build uses Maven 2.0.x, cannot propagate system properties from command line to tests (cf. SUREFIRE-121)";
         if (this.getLog().isDebugEnabled()) {
            this.getLog().warn(msg, var4);
         } else {
            this.getLog().warn((CharSequence)msg);
         }
      }

      if (props == null) {
         props = new Properties();
      }

      return props;
   }

   void ensureWorkingDirectoryExists() throws MojoFailureException {
      if (this.getWorkingDirectory() == null) {
         throw new MojoFailureException("workingDirectory cannot be null");
      } else if (!this.getWorkingDirectory().exists() && !this.getWorkingDirectory().mkdirs()) {
         throw new MojoFailureException("Cannot create workingDirectory " + this.getWorkingDirectory());
      } else if (!this.getWorkingDirectory().isDirectory()) {
         throw new MojoFailureException("workingDirectory " + this.getWorkingDirectory() + " exists and is not a directory");
      }
   }

   void ensureParallelRunningCompatibility() throws MojoFailureException {
      if (this.isMavenParallel() && this.isNotForking()) {
         throw new MojoFailureException("parallel maven execution is not compatible with surefire forkCount 0");
      }
   }

   void ensureThreadCountWithPerThread() throws MojoFailureException {
      if ("perthread".equals(this.getEffectiveForkMode()) && this.getThreadCount() < 1) {
         throw new MojoFailureException("Fork mode perthread requires a thread count");
      }
   }

   void warnIfUselessUseSystemClassLoaderParameter() {
      if (this.isUseSystemClassLoader() && this.isNotForking()) {
         this.getLog().warn((CharSequence)"useSystemClassloader setting has no effect when not forking");
      }

   }

   private boolean isNotForking() {
      return !this.isForking();
   }

   void warnIfDefunctGroupsCombinations() throws MojoFailureException, MojoExecutionException {
      if (this.isAnyGroupsSelected()) {
         if (this.getTestNgArtifact() == null) {
            Artifact junitArtifact = this.getJunitArtifact();
            boolean junit47Compatible = this.isJunit47Compatible(junitArtifact);
            if (!junit47Compatible) {
               if (junitArtifact != null) {
                  throw new MojoFailureException("groups/excludedGroups are specified but JUnit version on classpath is too old to support groups. Check your dependency:tree to see if your project is picking up an old junit version");
               } else {
                  throw new MojoFailureException("groups/excludedGroups require TestNG or JUnit48+ on project test classpath");
               }
            }
         }
      }
   }

   public abstract List<String> getIncludes();

   public File getIncludesFile() {
      return this.includesFile;
   }

   public abstract void setIncludes(List<String> var1);

   public List<String> getExcludes() {
      return this.excludes;
   }

   public File getExcludesFile() {
      return this.excludesFile;
   }

   public void setExcludes(List<String> excludes) {
      this.excludes = excludes;
   }

   public ArtifactRepository getLocalRepository() {
      return this.localRepository;
   }

   public void setLocalRepository(ArtifactRepository localRepository) {
      this.localRepository = localRepository;
   }

   public Properties getSystemProperties() {
      return this.systemProperties;
   }

   public void setSystemProperties(Properties systemProperties) {
      this.systemProperties = systemProperties;
   }

   public Map<String, String> getSystemPropertyVariables() {
      return this.systemPropertyVariables;
   }

   public void setSystemPropertyVariables(Map<String, String> systemPropertyVariables) {
      this.systemPropertyVariables = systemPropertyVariables;
   }

   public File getSystemPropertiesFile() {
      return this.systemPropertiesFile;
   }

   public void setSystemPropertiesFile(File systemPropertiesFile) {
      this.systemPropertiesFile = systemPropertiesFile;
   }

   public Properties getProperties() {
      return this.properties;
   }

   public void setProperties(Properties properties) {
      this.properties = properties;
   }

   public Map<String, Artifact> getPluginArtifactMap() {
      return this.pluginArtifactMap;
   }

   public void setPluginArtifactMap(Map<String, Artifact> pluginArtifactMap) {
      this.pluginArtifactMap = pluginArtifactMap;
   }

   public Map<String, Artifact> getProjectArtifactMap() {
      return this.projectArtifactMap;
   }

   public void setProjectArtifactMap(Map<String, Artifact> projectArtifactMap) {
      this.projectArtifactMap = projectArtifactMap;
   }

   public String getReportNameSuffix() {
      return this.reportNameSuffix;
   }

   public void setReportNameSuffix(String reportNameSuffix) {
      this.reportNameSuffix = reportNameSuffix;
   }

   public boolean isRedirectTestOutputToFile() {
      return this.redirectTestOutputToFile;
   }

   public void setRedirectTestOutputToFile(boolean redirectTestOutputToFile) {
      this.redirectTestOutputToFile = redirectTestOutputToFile;
   }

   public Boolean getFailIfNoTests() {
      return this.failIfNoTests;
   }

   public void setFailIfNoTests(Boolean failIfNoTests) {
      this.failIfNoTests = failIfNoTests;
   }

   public String getForkMode() {
      return this.forkMode;
   }

   public void setForkMode(String forkMode) {
      this.forkMode = forkMode;
   }

   public String getJvm() {
      return this.jvm;
   }

   public String getArgLine() {
      return this.argLine;
   }

   public void setArgLine(String argLine) {
      this.argLine = argLine;
   }

   public Map<String, String> getEnvironmentVariables() {
      return this.environmentVariables;
   }

   public void setEnvironmentVariables(Map<String, String> environmentVariables) {
      this.environmentVariables = environmentVariables;
   }

   public File getWorkingDirectory() {
      return this.workingDirectory;
   }

   public void setWorkingDirectory(File workingDirectory) {
      this.workingDirectory = workingDirectory;
   }

   public boolean isChildDelegation() {
      return this.childDelegation;
   }

   public void setChildDelegation(boolean childDelegation) {
      this.childDelegation = childDelegation;
   }

   public String getGroups() {
      return this.groups;
   }

   public void setGroups(String groups) {
      this.groups = groups;
   }

   public String getExcludedGroups() {
      return this.excludedGroups;
   }

   public void setExcludedGroups(String excludedGroups) {
      this.excludedGroups = excludedGroups;
   }

   public File[] getSuiteXmlFiles() {
      return this.suiteXmlFiles;
   }

   public void setSuiteXmlFiles(File[] suiteXmlFiles) {
      this.suiteXmlFiles = suiteXmlFiles;
   }

   public String getJunitArtifactName() {
      return this.junitArtifactName;
   }

   public void setJunitArtifactName(String junitArtifactName) {
      this.junitArtifactName = junitArtifactName;
   }

   public String getTestNGArtifactName() {
      return this.testNGArtifactName;
   }

   public void setTestNGArtifactName(String testNGArtifactName) {
      this.testNGArtifactName = testNGArtifactName;
   }

   public int getThreadCount() {
      return this.threadCount;
   }

   public void setThreadCount(int threadCount) {
      this.threadCount = threadCount;
   }

   public boolean getPerCoreThreadCount() {
      return this.perCoreThreadCount;
   }

   public void setPerCoreThreadCount(boolean perCoreThreadCount) {
      this.perCoreThreadCount = perCoreThreadCount;
   }

   public boolean getUseUnlimitedThreads() {
      return this.useUnlimitedThreads;
   }

   public void setUseUnlimitedThreads(boolean useUnlimitedThreads) {
      this.useUnlimitedThreads = useUnlimitedThreads;
   }

   public String getParallel() {
      return this.parallel;
   }

   public void setParallel(String parallel) {
      this.parallel = parallel;
   }

   public boolean isParallelOptimized() {
      return this.parallelOptimized;
   }

   public void setParallelOptimized(boolean parallelOptimized) {
      this.parallelOptimized = parallelOptimized;
   }

   public int getThreadCountSuites() {
      return this.threadCountSuites;
   }

   public void setThreadCountSuites(int threadCountSuites) {
      this.threadCountSuites = threadCountSuites;
   }

   public int getThreadCountClasses() {
      return this.threadCountClasses;
   }

   public void setThreadCountClasses(int threadCountClasses) {
      this.threadCountClasses = threadCountClasses;
   }

   public int getThreadCountMethods() {
      return this.threadCountMethods;
   }

   public void setThreadCountMethods(int threadCountMethods) {
      this.threadCountMethods = threadCountMethods;
   }

   public boolean isTrimStackTrace() {
      return this.trimStackTrace;
   }

   public void setTrimStackTrace(boolean trimStackTrace) {
      this.trimStackTrace = trimStackTrace;
   }

   public ArtifactResolver getArtifactResolver() {
      return this.artifactResolver;
   }

   public void setArtifactResolver(ArtifactResolver artifactResolver) {
      this.artifactResolver = artifactResolver;
   }

   public ArtifactFactory getArtifactFactory() {
      return this.artifactFactory;
   }

   public void setArtifactFactory(ArtifactFactory artifactFactory) {
      this.artifactFactory = artifactFactory;
   }

   public List<ArtifactRepository> getRemoteRepositories() {
      return this.remoteRepositories;
   }

   public void setRemoteRepositories(List<ArtifactRepository> remoteRepositories) {
      this.remoteRepositories = remoteRepositories;
   }

   public ArtifactMetadataSource getMetadataSource() {
      return this.metadataSource;
   }

   public void setMetadataSource(ArtifactMetadataSource metadataSource) {
      this.metadataSource = metadataSource;
   }

   public boolean isDisableXmlReport() {
      return this.disableXmlReport;
   }

   public void setDisableXmlReport(boolean disableXmlReport) {
      this.disableXmlReport = disableXmlReport;
   }

   public boolean isEnableAssertions() {
      return this.enableAssertions;
   }

   public boolean effectiveIsEnableAssertions() {
      if (this.getArgLine() != null) {
         List<String> args = Arrays.asList(this.getArgLine().split(" "));
         if (args.contains("-da") || args.contains("-disableassertions")) {
            return false;
         }
      }

      return this.isEnableAssertions();
   }

   public void setEnableAssertions(boolean enableAssertions) {
      this.enableAssertions = enableAssertions;
   }

   public MavenSession getSession() {
      return this.session;
   }

   public void setSession(MavenSession session) {
      this.session = session;
   }

   public String getObjectFactory() {
      return this.objectFactory;
   }

   public void setObjectFactory(String objectFactory) {
      this.objectFactory = objectFactory;
   }

   public ToolchainManager getToolchainManager() {
      return this.toolchainManager;
   }

   public void setToolchainManager(ToolchainManager toolchainManager) {
      this.toolchainManager = toolchainManager;
   }

   public boolean isMavenParallel() {
      return this.parallelMavenExecution != null && this.parallelMavenExecution;
   }

   public String getRunOrder() {
      return this.runOrder;
   }

   public void setRunOrder(String runOrder) {
      this.runOrder = runOrder;
   }

   public String[] getDependenciesToScan() {
      return this.dependenciesToScan;
   }

   public void setDependenciesToScan(String[] dependenciesToScan) {
      this.dependenciesToScan = dependenciesToScan;
   }

   public PluginDescriptor getPluginDescriptor() {
      return this.pluginDescriptor;
   }

   public MavenProject getProject() {
      return this.project;
   }

   public void setProject(MavenProject project) {
      this.project = project;
   }

   public File getTestSourceDirectory() {
      return this.testSourceDirectory;
   }

   public void setTestSourceDirectory(File testSourceDirectory) {
      this.testSourceDirectory = testSourceDirectory;
   }

   public String getForkCount() {
      return this.forkCount;
   }

   public boolean isReuseForks() {
      return this.reuseForks;
   }

   public String[] getAdditionalClasspathElements() {
      return this.additionalClasspathElements;
   }

   public void setAdditionalClasspathElements(String[] additionalClasspathElements) {
      this.additionalClasspathElements = additionalClasspathElements;
   }

   public String[] getClasspathDependencyExcludes() {
      return this.classpathDependencyExcludes;
   }

   public void setClasspathDependencyExcludes(String[] classpathDependencyExcludes) {
      this.classpathDependencyExcludes = classpathDependencyExcludes;
   }

   public String getClasspathDependencyScopeExclude() {
      return this.classpathDependencyScopeExclude;
   }

   public void setClasspathDependencyScopeExclude(String classpathDependencyScopeExclude) {
      this.classpathDependencyScopeExclude = classpathDependencyScopeExclude;
   }

   public class DynamicProviderInfo implements ConfigurableProviderInfo {
      final String providerName;

      DynamicProviderInfo(String providerName) {
         this.providerName = providerName;
      }

      public ProviderInfo instantiate(String providerName) {
         return AbstractSurefireMojo.this.new DynamicProviderInfo(providerName);
      }

      @Nonnull
      public String getProviderName() {
         return this.providerName;
      }

      public boolean isApplicable() {
         return true;
      }

      public void addProviderProperties() throws MojoExecutionException {
         AbstractSurefireMojo.this.convertJunitCoreParameters();
         AbstractSurefireMojo.this.convertTestNGParameters();
      }

      public Classpath getProviderClasspath() throws ArtifactResolutionException, ArtifactNotFoundException {
         Map<String, Artifact> pluginArtifactMap = AbstractSurefireMojo.this.getPluginArtifactMap();
         Artifact plugin = (Artifact)pluginArtifactMap.get("org.apache.maven.plugins:maven-surefire-plugin");
         return AbstractSurefireMojo.this.dependencyResolver.addProviderToClasspath(pluginArtifactMap, plugin);
      }
   }

   class JUnitCoreProviderInfo implements ProviderInfo {
      private final Artifact junitArtifact;
      private final Artifact junitDepArtifact;

      JUnitCoreProviderInfo(Artifact junitArtifact, Artifact junitDepArtifact) {
         this.junitArtifact = junitArtifact;
         this.junitDepArtifact = junitDepArtifact;
      }

      @Nonnull
      public String getProviderName() {
         return "org.apache.maven.surefire.junitcore.JUnitCoreProvider";
      }

      private boolean is47CompatibleJunitDep() {
         return this.junitDepArtifact != null && AbstractSurefireMojo.this.isJunit47Compatible(this.junitDepArtifact);
      }

      public boolean isApplicable() {
         boolean isJunitArtifact47 = AbstractSurefireMojo.this.isAnyJunit4(this.junitArtifact) && AbstractSurefireMojo.this.isJunit47Compatible(this.junitArtifact);
         boolean isAny47ProvidersForcers = AbstractSurefireMojo.this.isAnyConcurrencySelected() || AbstractSurefireMojo.this.isAnyGroupsSelected();
         return isAny47ProvidersForcers && (isJunitArtifact47 || this.is47CompatibleJunitDep());
      }

      public void addProviderProperties() throws MojoExecutionException {
         AbstractSurefireMojo.this.convertJunitCoreParameters();
         AbstractSurefireMojo.this.convertGroupParameters();
      }

      public Classpath getProviderClasspath() throws ArtifactResolutionException, ArtifactNotFoundException {
         return AbstractSurefireMojo.this.dependencyResolver.getProviderClasspath("surefire-junit47", AbstractSurefireMojo.this.surefireBooterArtifact.getBaseVersion(), (Artifact)null);
      }
   }

   class JUnit4ProviderInfo implements ProviderInfo {
      private final Artifact junitArtifact;
      private final Artifact junitDepArtifact;

      JUnit4ProviderInfo(Artifact junitArtifact, Artifact junitDepArtifact) {
         this.junitArtifact = junitArtifact;
         this.junitDepArtifact = junitDepArtifact;
      }

      @Nonnull
      public String getProviderName() {
         return "org.apache.maven.surefire.junit4.JUnit4Provider";
      }

      public boolean isApplicable() {
         return this.junitDepArtifact != null || AbstractSurefireMojo.this.isAnyJunit4(this.junitArtifact);
      }

      public void addProviderProperties() throws MojoExecutionException {
      }

      public Classpath getProviderClasspath() throws ArtifactResolutionException, ArtifactNotFoundException {
         return AbstractSurefireMojo.this.dependencyResolver.getProviderClasspath("surefire-junit4", AbstractSurefireMojo.this.surefireBooterArtifact.getBaseVersion(), (Artifact)null);
      }
   }

   class JUnit3ProviderInfo implements ProviderInfo {
      @Nonnull
      public String getProviderName() {
         return "org.apache.maven.surefire.junit.JUnit3Provider";
      }

      public boolean isApplicable() {
         return true;
      }

      public void addProviderProperties() throws MojoExecutionException {
      }

      public Classpath getProviderClasspath() throws ArtifactResolutionException, ArtifactNotFoundException {
         return AbstractSurefireMojo.this.dependencyResolver.getProviderClasspath("surefire-junit3", AbstractSurefireMojo.this.surefireBooterArtifact.getBaseVersion(), (Artifact)null);
      }
   }

   class TestNgProviderInfo implements ProviderInfo {
      private final Artifact testNgArtifact;

      TestNgProviderInfo(Artifact testNgArtifact) {
         this.testNgArtifact = testNgArtifact;
      }

      @Nonnull
      public String getProviderName() {
         return "org.apache.maven.surefire.testng.TestNGProvider";
      }

      public boolean isApplicable() {
         return this.testNgArtifact != null;
      }

      public void addProviderProperties() throws MojoExecutionException {
         AbstractSurefireMojo.this.convertTestNGParameters();
      }

      public Classpath getProviderClasspath() throws ArtifactResolutionException, ArtifactNotFoundException {
         Artifact surefireArtifact = (Artifact)AbstractSurefireMojo.this.getPluginArtifactMap().get("org.apache.maven.surefire:surefire-booter");
         return AbstractSurefireMojo.this.dependencyResolver.getProviderClasspath("surefire-testng", surefireArtifact.getBaseVersion(), this.testNgArtifact);
      }
   }
}
