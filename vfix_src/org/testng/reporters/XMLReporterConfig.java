package org.testng.reporters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class XMLReporterConfig {
   public static final String TAG_TEST = "test";
   public static final String TAG_TEST_METHOD = "test-method";
   public static final String TAG_EXCEPTION = "exception";
   public static final String TAG_MESSAGE = "message";
   public static final String TAG_SHORT_STACKTRACE = "short-stacktrace";
   public static final String TAG_FULL_STACKTRACE = "full-stacktrace";
   public static final String TAG_TESTNG_RESULTS = "testng-results";
   public static final String TAG_SUITE = "suite";
   public static final String TAG_GROUPS = "groups";
   public static final String TAG_GROUP = "group";
   public static final String TAG_CLASS = "class";
   public static final String TAG_METHOD = "method";
   public static final String TAG_PARAMS = "params";
   public static final String TAG_PARAM = "param";
   public static final String TAG_PARAM_VALUE = "value";
   public static final String TAG_REPORTER_OUTPUT = "reporter-output";
   public static final String TAG_LINE = "line";
   public static final String TAG_ATTRIBUTES = "attributes";
   public static final String TAG_ATTRIBUTE = "attribute";
   public static final String ATTR_URL = "url";
   public static final String ATTR_NAME = "name";
   public static final String ATTR_STATUS = "status";
   public static final String ATTR_DESC = "description";
   public static final String ATTR_METHOD_SIG = "signature";
   public static final String ATTR_GROUPS = "groups";
   public static final String ATTR_CLASS = "class";
   public static final String ATTR_TEST_INSTANCE_NAME = "test-instance-name";
   public static final String ATTR_INDEX = "index";
   public static final String ATTR_IS_NULL = "is-null";
   public static final String ATTR_PACKAGE = "package";
   public static final String ATTR_STARTED_AT = "started-at";
   public static final String ATTR_FINISHED_AT = "finished-at";
   public static final String ATTR_DURATION_MS = "duration-ms";
   public static final String ATTR_IS_CONFIG = "is-config";
   public static final String ATTR_DEPENDS_ON_METHODS = "depends-on-methods";
   public static final String ATTR_DEPENDS_ON_GROUPS = "depends-on-groups";
   public static final String ATTR_DATA_PROVIDER = "data-provider";
   public static final String TEST_PASSED = "PASS";
   public static final String TEST_FAILED = "FAIL";
   public static final String TEST_SKIPPED = "SKIP";
   private static Map<String, Integer> STATUSES = new HashMap<String, Integer>() {
      {
         this.put("PASS", 1);
         this.put("FAIL", 2);
         this.put("SKIP", 3);
      }
   };
   public static final int FF_LEVEL_NONE = 1;
   public static final int FF_LEVEL_SUITE = 2;
   public static final int FF_LEVEL_SUITE_RESULT = 3;
   public static final int STACKTRACE_NONE = 0;
   public static final int STACKTRACE_SHORT = 1;
   public static final int STACKTRACE_FULL = 2;
   public static final int STACKTRACE_BOTH = 3;
   static final String FMT_DEFAULT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
   private int fileFragmentationLevel = 1;
   private int stackTraceOutputMethod = 2;
   private String outputDirectory;
   private boolean generateGroupsAttribute = false;
   private boolean splitClassAndPackageNames = false;
   private boolean generateDependsOnMethods = true;
   private boolean generateDependsOnGroups = true;
   private boolean generateTestResultAttributes = false;
   private static String timestampFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";

   public static Integer getStatus(String status) {
      return (Integer)STATUSES.get(status);
   }

   public static long convertDate(String date) {
      SimpleDateFormat format = new SimpleDateFormat(getTimestampFormat());

      try {
         long result = format.parse(date).getTime();
         return result;
      } catch (ParseException var5) {
         return -1L;
      }
   }

   public int getFileFragmentationLevel() {
      return this.fileFragmentationLevel;
   }

   public void setFileFragmentationLevel(int fileFragmentationLevel) {
      this.fileFragmentationLevel = fileFragmentationLevel;
   }

   public int getStackTraceOutputMethod() {
      return this.stackTraceOutputMethod;
   }

   public void setStackTraceOutputMethod(int stackTraceOutputMethod) {
      this.stackTraceOutputMethod = stackTraceOutputMethod;
   }

   public String getOutputDirectory() {
      return this.outputDirectory;
   }

   public void setOutputDirectory(String outputDirectory) {
      this.outputDirectory = outputDirectory;
   }

   public boolean isGenerateGroupsAttribute() {
      return this.generateGroupsAttribute;
   }

   public void setGenerateGroupsAttribute(boolean generateGroupsAttribute) {
      this.generateGroupsAttribute = generateGroupsAttribute;
   }

   public boolean isSplitClassAndPackageNames() {
      return this.splitClassAndPackageNames;
   }

   public void setSplitClassAndPackageNames(boolean splitClassAndPackageNames) {
      this.splitClassAndPackageNames = splitClassAndPackageNames;
   }

   public static String getTimestampFormat() {
      return timestampFormat;
   }

   public void setTimestampFormat(String timestampFormat) {
      XMLReporterConfig.timestampFormat = timestampFormat;
   }

   public boolean isGenerateDependsOnMethods() {
      return this.generateDependsOnMethods;
   }

   public void setGenerateDependsOnMethods(boolean generateDependsOnMethods) {
      this.generateDependsOnMethods = generateDependsOnMethods;
   }

   public boolean isGenerateDependsOnGroups() {
      return this.generateDependsOnGroups;
   }

   public void setGenerateDependsOnGroups(boolean generateDependsOnGroups) {
      this.generateDependsOnGroups = generateDependsOnGroups;
   }

   public void setGenerateTestResultAttributes(boolean generateTestResultAttributes) {
      this.generateTestResultAttributes = generateTestResultAttributes;
   }

   public boolean isGenerateTestResultAttributes() {
      return this.generateTestResultAttributes;
   }
}
