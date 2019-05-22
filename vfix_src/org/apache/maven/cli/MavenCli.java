package org.apache.maven.cli;

import com.gzoltar.shaded.org.apache.commons.cli.CommandLine;
import com.gzoltar.shaded.org.apache.commons.cli.CommandLineParser;
import com.gzoltar.shaded.org.apache.commons.cli.GnuParser;
import com.gzoltar.shaded.org.apache.commons.cli.HelpFormatter;
import com.gzoltar.shaded.org.apache.commons.cli.OptionBuilder;
import com.gzoltar.shaded.org.apache.commons.cli.Options;
import com.gzoltar.shaded.org.apache.commons.cli.ParseException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.apache.maven.Maven;
import org.apache.maven.SettingsConfigurationException;
import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryFactory;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.ArtifactRepositoryLayout;
import org.apache.maven.execution.DefaultMavenExecutionRequest;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.monitor.event.DefaultEventDispatcher;
import org.apache.maven.monitor.event.DefaultEventMonitor;
import org.apache.maven.monitor.event.EventDispatcher;
import org.apache.maven.plugin.Mojo;
import org.apache.maven.profiles.DefaultProfileManager;
import org.apache.maven.profiles.ProfileManager;
import org.apache.maven.reactor.MavenExecutionException;
import org.apache.maven.settings.MavenSettingsBuilder;
import org.apache.maven.settings.RuntimeInfo;
import org.apache.maven.settings.Settings;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLifecycleException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.embed.Embedder;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.LoggerManager;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class MavenCli {
   /** @deprecated */
   public static final String OS_NAME;
   /** @deprecated */
   public static final String OS_ARCH;
   /** @deprecated */
   public static final String OS_VERSION;
   private static Embedder embedder;

   public static int main(String[] args, ClassWorld classWorld) {
      MavenCli.CLIManager cliManager = new MavenCli.CLIManager();

      CommandLine commandLine;
      try {
         commandLine = cliManager.parse(args);
      } catch (ParseException var34) {
         System.err.println("Unable to parse command line options: " + var34.getMessage());
         cliManager.displayHelp();
         return 1;
      }

      if ("1.4".compareTo(System.getProperty("java.specification.version")) > 0) {
         System.err.println("Sorry, but JDK 1.4 or above is required to execute Maven. You appear to be using Java:");
         System.err.println("java version \"" + System.getProperty("java.version", "<unknown java version>") + "\"");
         System.err.println(System.getProperty("java.runtime.name", "<unknown runtime name>") + " (build " + System.getProperty("java.runtime.version", "<unknown runtime version>") + ")");
         System.err.println(System.getProperty("java.vm.name", "<unknown vm name>") + " (build " + System.getProperty("java.vm.version", "<unknown vm version>") + ", " + System.getProperty("java.vm.info", "<unknown vm info>") + ")");
         return 1;
      } else {
         boolean debug = commandLine.hasOption('X');
         boolean showErrors = debug || commandLine.hasOption('e');
         if (showErrors) {
            System.out.println("+ Error stacktraces are turned on.");
         }

         if (commandLine.hasOption('h')) {
            cliManager.displayHelp();
            return 0;
         } else if (commandLine.hasOption('v')) {
            showVersion();
            return 0;
         } else {
            if (debug) {
               showVersion();
            }

            EventDispatcher eventDispatcher = new DefaultEventDispatcher();
            embedder = new Embedder();

            try {
               embedder.start(classWorld);
            } catch (PlexusContainerException var33) {
               showFatalError("Unable to start the embedded plexus container", var33, showErrors);
               return 1;
            }

            Properties executionProperties = new Properties();
            Properties userProperties = new Properties();
            populateProperties(commandLine, executionProperties, userProperties);

            Settings settings;
            try {
               settings = buildSettings(commandLine);
            } catch (SettingsConfigurationException var31) {
               showError("Error reading settings.xml: " + var31.getMessage(), var31, showErrors);
               return 1;
            } catch (ComponentLookupException var32) {
               showFatalError("Unable to read settings.xml", var32, showErrors);
               return 1;
            }

            Maven maven = null;
            MavenExecutionRequest request = null;
            LoggerManager loggerManager = null;

            label221: {
               byte var14;
               try {
                  loggerManager = (LoggerManager)embedder.lookup(LoggerManager.ROLE);
                  if (debug) {
                     loggerManager.setThreshold(0);
                  } else if (commandLine.hasOption('q')) {
                     loggerManager.setThreshold(3);
                  }

                  ProfileManager profileManager = new DefaultProfileManager(embedder.getContainer(), executionProperties);
                  if (commandLine.hasOption('P')) {
                     String profilesLine = commandLine.getOptionValue('P');
                     StringTokenizer profileTokens = new StringTokenizer(profilesLine, ",");

                     while(profileTokens.hasMoreTokens()) {
                        String profileAction = profileTokens.nextToken().trim();
                        if (profileAction.startsWith("-")) {
                           profileManager.explicitlyDeactivate(profileAction.substring(1));
                        } else if (profileAction.startsWith("+")) {
                           profileManager.explicitlyActivate(profileAction.substring(1));
                        } else {
                           profileManager.explicitlyActivate(profileAction);
                        }
                     }
                  }

                  request = createRequest(commandLine, settings, eventDispatcher, loggerManager, profileManager, executionProperties, userProperties, showErrors);
                  setProjectFileOptions(commandLine, request);
                  maven = createMavenInstance(settings.isInteractiveMode());
                  break label221;
               } catch (ComponentLookupException var35) {
                  showFatalError("Unable to configure the Maven application", var35, showErrors);
                  var14 = 1;
               } finally {
                  if (loggerManager != null) {
                     try {
                        embedder.release(loggerManager);
                     } catch (ComponentLifecycleException var29) {
                        showFatalError("Error releasing logging manager", var29, showErrors);
                     }
                  }

               }

               return var14;
            }

            try {
               maven.execute(request);
               return 0;
            } catch (MavenExecutionException var30) {
               return 1;
            }
         }
      }
   }

   private static Settings buildSettings(CommandLine commandLine) throws ComponentLookupException, SettingsConfigurationException {
      String userSettingsPath = null;
      if (commandLine.hasOption('s')) {
         userSettingsPath = commandLine.getOptionValue('s');
      }

      Settings settings = null;
      MavenSettingsBuilder settingsBuilder = (MavenSettingsBuilder)embedder.lookup(MavenSettingsBuilder.ROLE);

      try {
         if (userSettingsPath != null) {
            File userSettingsFile = new File(userSettingsPath);
            if (userSettingsFile.exists() && !userSettingsFile.isDirectory()) {
               settings = settingsBuilder.buildSettings(userSettingsFile);
            } else {
               System.out.println("WARNING: Alternate user settings file: " + userSettingsPath + " is invalid. Using default path.");
            }
         }

         if (settings == null) {
            settings = settingsBuilder.buildSettings();
         }
      } catch (IOException var5) {
         throw new SettingsConfigurationException("Error reading settings file", var5);
      } catch (XmlPullParserException var6) {
         throw new SettingsConfigurationException(var6.getMessage(), var6.getDetail(), var6.getLineNumber(), var6.getColumnNumber());
      }

      if (commandLine.hasOption('B')) {
         settings.setInteractiveMode(false);
      }

      if (commandLine.hasOption("npr")) {
         settings.setUsePluginRegistry(false);
      }

      settings.setRuntimeInfo(createRuntimeInfo(commandLine, settings));
      return settings;
   }

   private static RuntimeInfo createRuntimeInfo(CommandLine commandLine, Settings settings) {
      RuntimeInfo runtimeInfo = new RuntimeInfo(settings);
      if (!commandLine.hasOption("cpu") && !commandLine.hasOption("up")) {
         if (commandLine.hasOption("npu")) {
            runtimeInfo.setPluginUpdateOverride(Boolean.FALSE);
         }
      } else {
         runtimeInfo.setPluginUpdateOverride(Boolean.TRUE);
      }

      return runtimeInfo;
   }

   private static void showFatalError(String message, Exception e, boolean show) {
      System.err.println("FATAL ERROR: " + message);
      if (show) {
         System.err.println("Error stacktrace:");
         e.printStackTrace();
      } else {
         System.err.println("For more information, run with the -e flag");
      }

   }

   private static void showError(String message, Exception e, boolean show) {
      System.err.println(message);
      if (show) {
         System.err.println("Error stacktrace:");
         e.printStackTrace();
      }

   }

   private static MavenExecutionRequest createRequest(CommandLine commandLine, Settings settings, EventDispatcher eventDispatcher, LoggerManager loggerManager, ProfileManager profileManager, Properties executionProperties, Properties userProperties, boolean showErrors) throws ComponentLookupException {
      ArtifactRepository localRepository = createLocalRepository(embedder, settings, commandLine);
      File userDir = new File(System.getProperty("user.dir"));
      MavenExecutionRequest request = new DefaultMavenExecutionRequest(localRepository, settings, eventDispatcher, commandLine.getArgList(), userDir.getPath(), profileManager, executionProperties, userProperties, showErrors);
      Logger logger = loggerManager.getLoggerForComponent(Mojo.ROLE);
      if (logger != null) {
         request.addEventMonitor(new DefaultEventMonitor(logger));
      }

      if (commandLine.hasOption('N')) {
         request.setRecursive(false);
      }

      if (commandLine.hasOption("ff")) {
         request.setFailureBehavior("fail-fast");
      } else if (commandLine.hasOption("fae")) {
         request.setFailureBehavior("fail-at-end");
      } else if (commandLine.hasOption("fn")) {
         request.setFailureBehavior("fail-never");
      }

      return request;
   }

   private static void setProjectFileOptions(CommandLine commandLine, MavenExecutionRequest request) {
      if (commandLine.hasOption('r')) {
         request.setReactorActive(true);
      } else if (commandLine.hasOption('f')) {
         request.setPomFile(commandLine.getOptionValue('f'));
      }

   }

   private static Maven createMavenInstance(boolean interactive) throws ComponentLookupException {
      WagonManager wagonManager = (WagonManager)embedder.lookup(WagonManager.ROLE);
      if (interactive) {
         wagonManager.setDownloadMonitor(new ConsoleDownloadMonitor());
      } else {
         wagonManager.setDownloadMonitor(new BatchModeDownloadMonitor());
      }

      wagonManager.setInteractive(interactive);
      return (Maven)embedder.lookup(Maven.ROLE);
   }

   private static ArtifactRepository createLocalRepository(Embedder embedder, Settings settings, CommandLine commandLine) throws ComponentLookupException {
      ArtifactRepositoryLayout repositoryLayout = (ArtifactRepositoryLayout)embedder.lookup(ArtifactRepositoryLayout.ROLE, "default");
      ArtifactRepositoryFactory artifactRepositoryFactory = (ArtifactRepositoryFactory)embedder.lookup(ArtifactRepositoryFactory.ROLE);
      String url = settings.getLocalRepository();
      if (!url.startsWith("file:")) {
         url = "file://" + url;
      }

      ArtifactRepository localRepository = new DefaultArtifactRepository("local", url, repositoryLayout);
      boolean snapshotPolicySet = false;
      if (commandLine.hasOption('o')) {
         settings.setOffline(true);
         snapshotPolicySet = true;
      }

      if (!snapshotPolicySet && commandLine.hasOption('U')) {
         artifactRepositoryFactory.setGlobalUpdatePolicy("always");
      }

      if (commandLine.hasOption('C')) {
         System.out.println("+ Enabling strict checksum verification on all artifact downloads.");
         artifactRepositoryFactory.setGlobalChecksumPolicy("fail");
      } else if (commandLine.hasOption('c')) {
         System.out.println("+ Disabling strict checksum verification on all artifact downloads.");
         artifactRepositoryFactory.setGlobalChecksumPolicy("warn");
      }

      return localRepository;
   }

   private static void showVersion() {
      try {
         Properties properties = new Properties();
         InputStream resourceAsStream = MavenCli.class.getClassLoader().getResourceAsStream("META-INF/maven/org.apache.maven/maven-core/pom.properties");
         if (resourceAsStream != null) {
            properties.load(resourceAsStream);
            if (properties.getProperty("builtOn") != null) {
               System.out.println("Maven version: " + properties.getProperty("version", "unknown") + " built on " + properties.getProperty("builtOn"));
            } else {
               System.out.println("Maven version: " + properties.getProperty("version", "unknown"));
            }
         } else {
            System.out.println("Maven version: unknown");
         }

         System.out.println("Java version: " + System.getProperty("java.version", "<unknown java version>"));
         System.out.println("OS name: \"" + Os.OS_NAME + "\" version: \"" + Os.OS_VERSION + "\" arch: \"" + Os.OS_ARCH + "\" Family: \"" + Os.OS_FAMILY + "\"");
      } catch (IOException var2) {
         System.err.println("Unable determine version from JAR file: " + var2.getMessage());
      }

   }

   static void populateProperties(CommandLine commandLine, Properties executionProperties, Properties userProperties) {
      try {
         Properties envVars = CommandLineUtils.getSystemEnvVars();
         Iterator i = envVars.entrySet().iterator();

         while(i.hasNext()) {
            Entry e = (Entry)i.next();
            executionProperties.setProperty("env." + e.getKey().toString(), e.getValue().toString());
         }
      } catch (IOException var6) {
         System.err.println("Error getting environment vars for profile activation: " + var6);
      }

      if (commandLine.hasOption('D')) {
         String[] defStrs = commandLine.getOptionValues('D');
         if (defStrs != null) {
            for(int i = 0; i < defStrs.length; ++i) {
               setCliProperty(defStrs[i], userProperties);
            }
         }

         executionProperties.putAll(userProperties);
      }

      executionProperties.putAll(System.getProperties());
   }

   private static void setCliProperty(String property, Properties requestProperties) {
      int i = property.indexOf("=");
      String name;
      String value;
      if (i <= 0) {
         name = property.trim();
         value = "true";
      } else {
         name = property.substring(0, i).trim();
         value = property.substring(i + 1).trim();
      }

      requestProperties.setProperty(name, value);
      System.setProperty(name, value);
   }

   static {
      OS_NAME = Os.OS_NAME;
      OS_ARCH = Os.OS_ARCH;
      OS_VERSION = Os.OS_VERSION;
   }

   static class CLIManager {
      public static final char ALTERNATE_POM_FILE = 'f';
      public static final char BATCH_MODE = 'B';
      public static final char SET_SYSTEM_PROPERTY = 'D';
      public static final char OFFLINE = 'o';
      public static final char REACTOR = 'r';
      public static final char QUIET = 'q';
      public static final char DEBUG = 'X';
      public static final char ERRORS = 'e';
      public static final char HELP = 'h';
      public static final char VERSION = 'v';
      private Options options = new Options();
      public static final char NON_RECURSIVE = 'N';
      public static final char UPDATE_SNAPSHOTS = 'U';
      public static final char ACTIVATE_PROFILES = 'P';
      public static final String FORCE_PLUGIN_UPDATES = "cpu";
      public static final String FORCE_PLUGIN_UPDATES2 = "up";
      public static final String SUPPRESS_PLUGIN_UPDATES = "npu";
      public static final String SUPPRESS_PLUGIN_REGISTRY = "npr";
      public static final char CHECKSUM_FAILURE_POLICY = 'C';
      public static final char CHECKSUM_WARNING_POLICY = 'c';
      private static final char ALTERNATE_USER_SETTINGS = 's';
      private static final String FAIL_FAST = "ff";
      private static final String FAIL_AT_END = "fae";
      private static final String FAIL_NEVER = "fn";

      public CLIManager() {
         Options var10000 = this.options;
         OptionBuilder.withLongOpt("file");
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("Force the use of an alternate POM file.");
         var10000.addOption(OptionBuilder.create('f'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("define");
         OptionBuilder.hasArg();
         OptionBuilder.withDescription("Define a system property");
         var10000.addOption(OptionBuilder.create('D'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("offline");
         OptionBuilder.withDescription("Work offline");
         var10000.addOption(OptionBuilder.create('o'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("help");
         OptionBuilder.withDescription("Display help information");
         var10000.addOption(OptionBuilder.create('h'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("version");
         OptionBuilder.withDescription("Display version information");
         var10000.addOption(OptionBuilder.create('v'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("quiet");
         OptionBuilder.withDescription("Quiet output - only show errors");
         var10000.addOption(OptionBuilder.create('q'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("debug");
         OptionBuilder.withDescription("Produce execution debug output");
         var10000.addOption(OptionBuilder.create('X'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("errors");
         OptionBuilder.withDescription("Produce execution error messages");
         var10000.addOption(OptionBuilder.create('e'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("reactor");
         OptionBuilder.withDescription("Execute goals for project found in the reactor");
         var10000.addOption(OptionBuilder.create('r'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("non-recursive");
         OptionBuilder.withDescription("Do not recurse into sub-projects");
         var10000.addOption(OptionBuilder.create('N'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("update-snapshots");
         OptionBuilder.withDescription("Forces a check for updated releases and snapshots on remote repositories");
         var10000.addOption(OptionBuilder.create('U'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("activate-profiles");
         OptionBuilder.withDescription("Comma-delimited list of profiles to activate");
         OptionBuilder.hasArg();
         var10000.addOption(OptionBuilder.create('P'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("batch-mode");
         OptionBuilder.withDescription("Run in non-interactive (batch) mode");
         var10000.addOption(OptionBuilder.create('B'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("check-plugin-updates");
         OptionBuilder.withDescription("Force upToDate check for any relevant registered plugins");
         var10000.addOption(OptionBuilder.create("cpu"));
         var10000 = this.options;
         OptionBuilder.withLongOpt("update-plugins");
         OptionBuilder.withDescription("Synonym for cpu");
         var10000.addOption(OptionBuilder.create("up"));
         var10000 = this.options;
         OptionBuilder.withLongOpt("no-plugin-updates");
         OptionBuilder.withDescription("Suppress upToDate check for any relevant registered plugins");
         var10000.addOption(OptionBuilder.create("npu"));
         var10000 = this.options;
         OptionBuilder.withLongOpt("no-plugin-registry");
         OptionBuilder.withDescription("Don't use ~/.m2/plugin-registry.xml for plugin versions");
         var10000.addOption(OptionBuilder.create("npr"));
         var10000 = this.options;
         OptionBuilder.withLongOpt("strict-checksums");
         OptionBuilder.withDescription("Fail the build if checksums don't match");
         var10000.addOption(OptionBuilder.create('C'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("lax-checksums");
         OptionBuilder.withDescription("Warn if checksums don't match");
         var10000.addOption(OptionBuilder.create('c'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("settings");
         OptionBuilder.withDescription("Alternate path for the user settings file");
         OptionBuilder.hasArg();
         var10000.addOption(OptionBuilder.create('s'));
         var10000 = this.options;
         OptionBuilder.withLongOpt("fail-fast");
         OptionBuilder.withDescription("Stop at first failure in reactorized builds");
         var10000.addOption(OptionBuilder.create("ff"));
         var10000 = this.options;
         OptionBuilder.withLongOpt("fail-at-end");
         OptionBuilder.withDescription("Only fail the build afterwards; allow all non-impacted builds to continue");
         var10000.addOption(OptionBuilder.create("fae"));
         var10000 = this.options;
         OptionBuilder.withLongOpt("fail-never");
         OptionBuilder.withDescription("NEVER fail the build, regardless of project result");
         var10000.addOption(OptionBuilder.create("fn"));
      }

      public CommandLine parse(String[] args) throws ParseException {
         String[] cleanArgs = this.cleanArgs(args);
         CommandLineParser parser = new GnuParser();
         return parser.parse(this.options, cleanArgs);
      }

      private String[] cleanArgs(String[] args) {
         List cleaned = new ArrayList();
         StringBuffer currentArg = null;

         int cleanedSz;
         String arg;
         for(cleanedSz = 0; cleanedSz < args.length; ++cleanedSz) {
            arg = args[cleanedSz];
            boolean addedToBuffer = false;
            if (arg.startsWith("\"")) {
               if (currentArg != null) {
                  cleaned.add(currentArg.toString());
               }

               currentArg = new StringBuffer(arg.substring(1));
               addedToBuffer = true;
            }

            if (arg.endsWith("\"")) {
               String cleanArgPart = arg.substring(0, arg.length() - 1);
               if (currentArg != null) {
                  if (addedToBuffer) {
                     currentArg.setLength(currentArg.length() - 1);
                  } else {
                     currentArg.append(' ').append(cleanArgPart);
                  }

                  cleaned.add(currentArg.toString());
               } else {
                  cleaned.add(cleanArgPart);
               }

               currentArg = null;
            } else if (!addedToBuffer) {
               if (currentArg != null) {
                  currentArg.append(' ').append(arg);
               } else {
                  cleaned.add(arg);
               }
            }
         }

         if (currentArg != null) {
            cleaned.add(currentArg.toString());
         }

         cleanedSz = cleaned.size();
         arg = null;
         String[] cleanArgs;
         if (cleanedSz == 0) {
            cleanArgs = args;
         } else {
            cleanArgs = (String[])((String[])cleaned.toArray(new String[cleanedSz]));
         }

         return cleanArgs;
      }

      public void displayHelp() {
         System.out.println();
         HelpFormatter formatter = new HelpFormatter();
         formatter.printHelp("mvn [options] [<goal(s)>] [<phase(s)>]", "\nOptions:", this.options, "\n");
      }
   }
}
