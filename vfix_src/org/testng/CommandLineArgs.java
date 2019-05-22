package org.testng;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.CommaParameterSplitter;
import java.util.ArrayList;
import java.util.List;
import org.testng.collections.Lists;

public class CommandLineArgs {
   @Parameter(
      description = "The XML suite files to run"
   )
   public List<String> suiteFiles = Lists.newArrayList();
   public static final String LOG = "-log";
   public static final String VERBOSE = "-verbose";
   @Parameter(
      names = {"-log", "-verbose"},
      description = "Level of verbosity"
   )
   public Integer verbose;
   public static final String GROUPS = "-groups";
   @Parameter(
      names = {"-groups"},
      description = "Comma-separated list of group names to be run"
   )
   public String groups;
   public static final String EXCLUDED_GROUPS = "-excludegroups";
   @Parameter(
      names = {"-excludegroups"},
      description = "Comma-separated list of group names to  exclude"
   )
   public String excludedGroups;
   public static final String OUTPUT_DIRECTORY = "-d";
   @Parameter(
      names = {"-d"},
      description = "Output directory"
   )
   public String outputDirectory;
   public static final String JUNIT = "-junit";
   @Parameter(
      names = {"-junit"},
      description = "JUnit mode"
   )
   public Boolean junit;
   public static final String MIXED = "-mixed";
   @Parameter(
      names = {"-mixed"},
      description = "Mixed mode - autodetect the type of current test and run it with appropriate runner"
   )
   public Boolean mixed;
   public static final String LISTENER = "-listener";
   @Parameter(
      names = {"-listener"},
      description = "List of .class files or list of class names implementing ITestListener or ISuiteListener"
   )
   public String listener;
   public static final String METHOD_SELECTORS = "-methodselectors";
   @Parameter(
      names = {"-methodselectors"},
      description = "List of .class files or list of class names implementing IMethodSelector"
   )
   public String methodSelectors;
   public static final String OBJECT_FACTORY = "-objectfactory";
   @Parameter(
      names = {"-objectfactory"},
      description = "List of .class files or list of class names implementing ITestRunnerFactory"
   )
   public String objectFactory;
   public static final String PARALLEL = "-parallel";
   @Parameter(
      names = {"-parallel"},
      description = "Parallel mode (methods, tests or classes)"
   )
   public String parallelMode;
   public static final String CONFIG_FAILURE_POLICY = "-configfailurepolicy";
   @Parameter(
      names = {"-configfailurepolicy"},
      description = "Configuration failure policy (skip or continue)"
   )
   public String configFailurePolicy;
   public static final String THREAD_COUNT = "-threadcount";
   @Parameter(
      names = {"-threadcount"},
      description = "Number of threads to use when running tests in parallel"
   )
   public Integer threadCount;
   public static final String DATA_PROVIDER_THREAD_COUNT = "-dataproviderthreadcount";
   @Parameter(
      names = {"-dataproviderthreadcount"},
      description = "Number of threads to use when running data providers"
   )
   public Integer dataProviderThreadCount;
   public static final String SUITE_NAME = "-suitename";
   @Parameter(
      names = {"-suitename"},
      description = "Default name of test suite, if not specified in suite definition file or source code"
   )
   public String suiteName;
   public static final String TEST_NAME = "-testname";
   @Parameter(
      names = {"-testname"},
      description = "Default name of test, if not specified in suitedefinition file or source code"
   )
   public String testName;
   public static final String REPORTER = "-reporter";
   @Parameter(
      names = {"-reporter"},
      description = "Extended configuration for custom report listener"
   )
   public String reporter;
   public static final String USE_DEFAULT_LISTENERS = "-usedefaultlisteners";
   @Parameter(
      names = {"-usedefaultlisteners"},
      description = "Whether to use the default listeners"
   )
   public String useDefaultListeners;
   public static final String SKIP_FAILED_INVOCATION_COUNTS = "-skipfailedinvocationcounts";
   @Parameter(
      names = {"-skipfailedinvocationcounts"},
      hidden = true
   )
   public Boolean skipFailedInvocationCounts;
   public static final String TEST_CLASS = "-testclass";
   @Parameter(
      names = {"-testclass"},
      description = "The list of test classes"
   )
   public String testClass;
   public static final String TEST_NAMES = "-testnames";
   @Parameter(
      names = {"-testnames"},
      description = "The list of test names to run"
   )
   public String testNames;
   public static final String TEST_JAR = "-testjar";
   @Parameter(
      names = {"-testjar"},
      description = "A jar file containing the tests"
   )
   public String testJar;
   public static final String XML_PATH_IN_JAR = "-xmlpathinjar";
   public static final String XML_PATH_IN_JAR_DEFAULT = "testng.xml";
   @Parameter(
      names = {"-xmlpathinjar"},
      description = "The full path to the xml file inside the jar file (only valid if -testjar was specified)"
   )
   public String xmlPathInJar;
   public static final String TEST_RUNNER_FACTORY = "-testrunfactory";
   @Parameter(
      names = {"-testrunfactory", "-testRunFactory"},
      description = "The factory used to create tests"
   )
   public String testRunnerFactory;
   public static final String PORT = "-port";
   @Parameter(
      names = {"-port"},
      description = "The port"
   )
   public Integer port;
   public static final String HOST = "-host";
   @Parameter(
      names = {"-host"},
      description = "The host",
      hidden = true
   )
   public String host;
   public static final String MASTER = "-master";
   @Parameter(
      names = {"-master"},
      description = "Host where the master is",
      hidden = true
   )
   public String master;
   public static final String SLAVE = "-slave";
   @Parameter(
      names = {"-slave"},
      description = "Host where the slave is",
      hidden = true
   )
   public String slave;
   public static final String METHODS = "-methods";
   @Parameter(
      names = {"-methods"},
      description = "Comma separated of test methods",
      splitter = CommaParameterSplitter.class
   )
   public List<String> commandLineMethods;
   public static final String SUITE_THREAD_POOL_SIZE = "-suitethreadpoolsize";
   public static final Integer SUITE_THREAD_POOL_SIZE_DEFAULT = 1;
   @Parameter(
      names = {"-suitethreadpoolsize"},
      description = "Size of the thread pool to use to run suites"
   )
   public Integer suiteThreadPoolSize;
   public static final String RANDOMIZE_SUITES = "-randomizesuites";
   @Parameter(
      names = {"-randomizesuites"},
      hidden = true,
      description = "Whether to run suites in same order as specified in XML or not"
   )
   public Boolean randomizeSuites;
   public static final String DEBUG = "-debug";
   @Parameter(
      names = {"-debug"},
      hidden = true,
      description = "Used to debug TestNG"
   )
   public Boolean debug;

   public CommandLineArgs() {
      this.junit = Boolean.FALSE;
      this.mixed = Boolean.FALSE;
      this.useDefaultListeners = "true";
      this.xmlPathInJar = "testng.xml";
      this.commandLineMethods = new ArrayList();
      this.suiteThreadPoolSize = SUITE_THREAD_POOL_SIZE_DEFAULT;
      this.randomizeSuites = Boolean.FALSE;
      this.debug = Boolean.FALSE;
   }
}
