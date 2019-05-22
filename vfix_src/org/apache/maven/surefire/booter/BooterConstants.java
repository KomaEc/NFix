package org.apache.maven.surefire.booter;

public interface BooterConstants {
   String SPECIFIC_TEST_PROPERTY_PREFIX = "specificTest";
   String INCLUDES_PROPERTY_PREFIX = "includes";
   String EXCLUDES_PROPERTY_PREFIX = "excludes";
   String USESYSTEMCLASSLOADER = "useSystemClassLoader";
   String USEMANIFESTONLYJAR = "useManifestOnlyJar";
   String FAILIFNOTESTS = "failIfNoTests";
   String ISTRIMSTACKTRACE = "isTrimStackTrace";
   String REPORTSDIRECTORY = "reportsDirectory";
   String TESTARTIFACT_VERSION = "testFwJarVersion";
   String TESTARTIFACT_CLASSIFIER = "testFwJarClassifier";
   String REQUESTEDTEST = "requestedTest";
   String REQUESTEDTESTMETHOD = "requestedTestMethod";
   String SOURCE_DIRECTORY = "testSuiteDefinitionTestSourceDirectory";
   String TEST_CLASSES_DIRECTORY = "testClassesDirectory";
   String RUN_ORDER = "runOrder";
   String RUN_STATISTICS_FILE = "runStatisticsFile";
   String TEST_SUITE_XML_FILES = "testSuiteXmlFiles";
   String PROVIDER_CONFIGURATION = "providerConfiguration";
   String FORKTESTSET = "forkTestSet";
   String FORKTESTSET_PREFER_TESTS_FROM_IN_STREAM = "preferTestsFromInStream";
}
