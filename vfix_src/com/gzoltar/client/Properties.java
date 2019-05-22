package com.gzoltar.client;

import com.gzoltar.client.statistics.StatisticsVariables;
import com.gzoltar.instrumentation.Logger;
import com.gzoltar.instrumentation.components.Component;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Properties implements Serializable {
   private static final long serialVersionUID = -498501012949009748L;
   @Properties.Parameter(
      key = "loglevel",
      group = "Execution",
      description = "Verbosity level of logger"
   )
   public static Logger.LogLevel LOGLEVEL;
   @Properties.Parameter(
      key = "timelimit",
      group = "Execution",
      description = "Maximum time (seconds) allowed to execute GZoltar (-1 allows infinite time)"
   )
   public static Integer TIMELIMIT;
   @Properties.Parameter(
      key = "test_timeout",
      group = "Execution",
      description = "Maximum time (seconds) allowed to execute a test class or a test case depending on the test strategy used. (-1 allows infinite time)"
   )
   public static Integer TEST_TIMEOUT;
   @Properties.Parameter(
      key = "number_of_test_runs_in_parallel",
      group = "Execution",
      description = "Number of tests that can be executed in parallel. (-1 to use all available processors)"
   )
   public static Integer NUMBER_OF_TEST_RUNS_IN_PARALLEL;
   @Properties.Parameter(
      key = "enable_assertions_for_gzoltar",
      group = "Execution",
      description = "Checks assertions when running GZoltar"
   )
   public static Boolean ENABLE_ASSERTIONS_FOR_GZOLTAR;
   @Properties.Parameter(
      key = "configurationID",
      group = "Execution",
      description = "Configuration ID of the experiment"
   )
   public static String CONFIGURATIONID;
   @Properties.Parameter(
      key = "is_running_a_gzoltar_test",
      group = "Runtime",
      description = "Specify that a GZoltar test is running. To be used only for debugging/testing purposes"
   )
   public static volatile Boolean IS_RUNNING_A_GZOLTAR_TEST;
   @Properties.Parameter(
      key = "max_client_memory",
      group = "Execution",
      description = "Maximum memory (in MB) allocated for the GZ-client"
   )
   public static Integer MAX_CLIENT_MEMORY;
   @Properties.Parameter(
      key = "max_perm_size",
      group = "Runtime",
      description = "MaxPermSize (in MB) for the client process"
   )
   public static Integer MAX_PERM_SIZE;
   @Properties.Parameter(
      key = "show_progress_bar",
      group = "Execution",
      description = "If enable, shows a progress bar on console"
   )
   public static Boolean SHOW_PROGRESS_BAR;
   @Properties.Parameter(
      key = "gzoltar_classloader",
      group = "Execution",
      description = "If enable, the classloader used to load each class is the GZoltar classloader"
   )
   public static Boolean GZOLTAR_CLASSLOADER;
   @Properties.Parameter(
      key = "gzoltar_context_classloader",
      group = "Execution",
      description = "If enable, the context classloader used to run the test suite(s) / case(s) is the GZoltar classloader"
   )
   public static Boolean GZOLTAR_CONTEXT_CLASSLOADER;
   @Properties.Parameter(
      key = "strategy",
      group = "Diagnose",
      description = "Diagnose strategy"
   )
   public static Properties.Strategy STRATEGY;
   @Properties.Parameter(
      key = "mutation_strategy",
      group = "Diagnose",
      description = "Mutation strategy"
   )
   public static Properties.MutationStrategy MUTATION_STRATEGY;
   @Properties.Parameter(
      key = "coefficients",
      group = "Diagnose",
      description = "Diagnose coefficients"
   )
   public static Properties.Coefficient[] COEFFICIENTS;
   @Properties.Parameter(
      key = "candidate_generation_algorithm",
      group = "Diagnose",
      description = "Candidage Generation Algorithm"
   )
   public static Properties.CandidateGeneration CANDIDATE_GENERATION_ALGORITHM;
   @Properties.Parameter(
      key = "granularity",
      group = "Diagnose",
      description = "Granularity level of instrumentation"
   )
   public static Component.Granularity GRANULARITY;
   @Properties.Parameter(
      key = "unique_line_numbers",
      group = "Diagnose",
      description = "When true, instrumentation takes into account whether there are multiple bytcode instructions for a single line number"
   )
   public static Boolean UNIQUE_LINE_NUMBERS;
   @Properties.Parameter(
      key = "star",
      group = "Diagnose",
      description = "Star value used on DStar suspiciousness formula"
   )
   public static Integer STAR;
   @Properties.Parameter(
      key = "mutation_backend",
      group = "Mutation",
      description = "Mutation framework"
   )
   public static Properties.MutationBackend MUTATION_BACKEND;
   @Properties.Parameter(
      key = "major_javac",
      group = "Mutation",
      description = "Javac executable of Major framework"
   )
   public static String MAJOR_JAVAC;
   @Properties.Parameter(
      key = "mutants_dir",
      group = "Mutation",
      description = "Directory to where mutants will be exported"
   )
   public static String MUTANTS_DIR;
   @Properties.Parameter(
      key = "mutant_dir",
      group = "Mutation",
      description = "Mutant directory"
   )
   public static String MUTANT_DIR;
   @Properties.Parameter(
      key = "mutants_log",
      group = "Mutation",
      description = "File with information about the generated mutants"
   )
   public static String MUTANTS_LOG;
   @Properties.Parameter(
      key = "mutants",
      group = "Mutation",
      description = "Mutants ID"
   )
   public static Integer MUTANTS;
   @Properties.Parameter(
      key = "only_run_tests_that_cover_component",
      group = "Mutation",
      description = "If enable, only test cases that cover a component 'c' are executed on mutants of 'c'"
   )
   public static Boolean ONLY_RUN_TESTS_THAT_COVER_COMPONENT;
   @Properties.Parameter(
      key = "minimization_strategy",
      group = "Minimization",
      description = "Minimization strategy"
   )
   public static Properties.MinimizationStrategy MINIMIZATION_STRATEGY;
   @Properties.Parameter(
      key = "minion_exec",
      group = "Minimization",
      description = "Path to the Minion's executable"
   )
   public static String MINION_EXEC;
   @Properties.Parameter(
      key = "minion_num_solutions",
      group = "Minimization",
      description = "Maximum limit of solutions (-1 enables 'all solutions')"
   )
   public static Integer MINION_NUM_SOLUTIONS;
   @Properties.Parameter(
      key = "minion_timeout",
      group = "Minimization",
      description = "Maximum time (in seconds) allowed to execute MINION (-1 allows infinite time)"
   )
   public static Integer MINION_TIMEOUT;
   @Properties.Parameter(
      key = "gzoltar_data_dir",
      group = "Output",
      description = "Directory name where all output of GZoltar is saved"
   )
   public static String GZOLTAR_DATA_DIR;
   @Properties.Parameter(
      key = "print_matrix",
      group = "Output",
      description = "If enable coverage matrix is returned to a file"
   )
   public static Boolean PRINT_MATRIX;
   @Properties.Parameter(
      key = "matrix_file",
      group = "Output",
      description = "Coverage matrix file name"
   )
   public static String MATRIX_FILE;
   @Properties.Parameter(
      key = "extra_matrix_file",
      group = "Output",
      description = "Coverage of a previous generated matrix"
   )
   public static String EXTRA_MATRIX_FILE;
   @Properties.Parameter(
      key = "filter_spectra",
      group = "Output",
      description = "Filter Spectra"
   )
   public static Properties.FilterSpectra FILTER_SPECTRA;
   @Properties.Parameter(
      key = "normalize_spectra",
      group = "Output",
      description = "If enable, the suspiciousness values are normalized"
   )
   public static Boolean NORMALIZE_SPECTRA;
   @Properties.Parameter(
      key = "print_spectra",
      group = "Output",
      description = "If enable spectra data is returned to a file"
   )
   public static Boolean PRINT_SPECTRA;
   @Properties.Parameter(
      key = "spectra_file",
      group = "Output",
      description = "Spectra file name"
   )
   public static String SPECTRA_FILE;
   @Properties.Parameter(
      key = "serialize_spectra",
      group = "Output",
      description = "If enable, a spectra object is serialized to a file"
   )
   public static Boolean SERIALIZE_SPECTRA;
   @Properties.Parameter(
      key = "verbose_spectra",
      group = "Output",
      description = "When enable a verbose spectra will be printed"
   )
   public static Boolean VERBOSE_SPECTRA;
   @Properties.Parameter(
      key = "INCLUDE_SUSPICIOUSNESS_VALUE",
      group = "Output",
      description = "When enable a verbose spectra will be printed"
   )
   public static Boolean INCLUDE_SUSPICIOUSNESS_VALUE;
   @Properties.Parameter(
      key = "output_variables",
      group = "Output",
      description = "Statistics variables to be saved. Can define more than one variable by using a ':' separated list"
   )
   public static StatisticsVariables[] OUTPUT_VARIABLES;
   @Properties.Parameter(
      key = "classes_file",
      group = "Output",
      description = "File in which the list of classes is stored"
   )
   public static String CLASSES_FILE;
   @Properties.Parameter(
      key = "tests_file",
      group = "Output",
      description = "File in which the list of test-classes is stored"
   )
   public static String TESTS_FILE;
   @Properties.Parameter(
      key = "statistics_file",
      group = "Output",
      description = "Statistics CSV file name"
   )
   public static String STATISTICS_FILE;
   @Properties.Parameter(
      key = "statistics_backend",
      group = "Output",
      description = "Statistics backend"
   )
   public static Properties.StatisticsBackend STATISTICS_BACKEND;
   @Properties.Parameter(
      key = "projectID",
      group = "Project Under Test",
      description = "Name of the project under test"
   )
   public static String PROJECTID;
   @Properties.Parameter(
      key = "sourcedir",
      group = "Project Under Test",
      description = "Source directory"
   )
   public static String SOURCEDIR;
   @Properties.Parameter(
      key = "classesDir",
      group = "Project Under Test",
      description = "Path of classes directory"
   )
   public static String CLASSESDIR;
   @Properties.Parameter(
      key = "testsDir",
      group = "Project Under Test",
      description = "Path of tests directory"
   )
   public static String TESTSDIR;
   @Properties.Parameter(
      key = "deps",
      group = "Project Under Test",
      description = "All depedencies (e.g., jar files) of the project under test"
   )
   public static String[] DEPS;
   @Properties.Parameter(
      key = "project_cp",
      group = "Project Under Test",
      description = "Classpath of the Project Under Test (i.e., classes directory, test-classes directory, jar files, or class files"
   )
   public static String PROJECT_CP;
   @Properties.Parameter(
      key = "native_libraries",
      group = "Project Under Test",
      description = "Native libraries (i.e., *.so, *.dll, *.jnilib) of the project under test"
   )
   public static String[] NATIVE_LIBRARIES;
   @Properties.Parameter(
      key = "targetclasses",
      group = "Project Under Test",
      description = "Target class(es) to diagnose. A fully qualifying needs to be provided, e.g. org.foo.SomeClass. Can define more than one class by using a ':' separated list"
   )
   public static String[] TARGETCLASSES;
   @Properties.Parameter(
      key = "targetpackages",
      group = "Project Under Test",
      description = "Target package(es) to diagnose. Can define more than one package by using a ':' separated list"
   )
   public static String[] TARGETPACKAGES;
   @Properties.Parameter(
      key = "testclasses",
      group = "Project Under Test",
      description = "Test class(es) to execute. A fully qualifying needs to be provided, e.g. org.foo.TestSomeClass. Can define more than one test by using a ':' separated list"
   )
   public static String[] TESTCLASSES;
   @Properties.Parameter(
      key = "testmethods",
      group = "Project Under Test",
      description = "Test method(s) to execute (by default all test methods within a test class are execute). A fully qualifying needs to be provided, e.g. org.foo.TestSomeClass#test00. Can define more than one test by using a ':' separated list"
   )
   public static String[] TESTMETHODS;
   @Properties.Parameter(
      key = "testpackages",
      group = "Project Under Test",
      description = "Test package(es) to execute. Can define more than one test package by using a ':' separated list"
   )
   public static String[] TESTPACKAGES;
   @Properties.Parameter(
      key = "include_deprecated_classes",
      group = "Project Under Test",
      description = "If enable deprecated classes will also be considered"
   )
   public static Boolean INCLUDE_DEPRECATED_CLASSES;
   @Properties.Parameter(
      key = "include_deprecated_methods",
      group = "Project Under Test",
      description = "If enable deprecated methods will also be considered"
   )
   public static Boolean INCLUDE_DEPRECATED_METHODS;
   @Properties.Parameter(
      key = "include_local_classes",
      group = "Project Under Test",
      description = "If enable inner/anonymous classes will also be included"
   )
   public static Boolean INCLUDE_LOCAL_CLASSES;
   @Properties.Parameter(
      key = "faulty_components",
      group = "Project Under Test",
      description = "Faulty components ID (format <class canonical name>{<method name#line number. Can define more than one faulty component by using a ':' separated list"
   )
   public static String FAULTY_COMPONENTS;
   @Properties.Parameter(
      key = "instrument_test_classes",
      group = "Project Under Test",
      description = "If enable, coverage of test classes will also be considered"
   )
   public static Boolean INSTRUMENT_TEST_CLASSES;
   @Properties.Parameter(
      key = "search_for_classes_on_jar_files",
      group = "Project Under Test",
      description = ""
   )
   public static Boolean SEARCH_FOR_CLASSES_ON_JAR_FILES;
   private static Properties instance;
   private static Map<String, Field> parameters;
   private static Map<String, Object> defaultParameters;

   private Properties() {
   }

   public static Properties getInstance() {
      if (instance == null) {
         instance = new Properties();
      }

      return instance;
   }

   private static void initializeAllParameters() {
      Field[] var0;
      int var1 = (var0 = Properties.class.getFields()).length;

      for(int var2 = 0; var2 < var1; ++var2) {
         Field var3;
         if ((var3 = var0[var2]).isAnnotationPresent(Properties.Parameter.class)) {
            Properties.Parameter var4 = (Properties.Parameter)var3.getAnnotation(Properties.Parameter.class);
            parameters.put(var4.key().toUpperCase(), var3);

            try {
               defaultParameters.put(var4.key().toUpperCase(), var3.get((Object)null));
            } catch (IllegalAccessException | IllegalArgumentException var5) {
               Logger.getInstance().err(var5.getMessage(), var5);
            }
         }
      }

   }

   public static Map<String, String> getAllParameters() throws IllegalArgumentException, IllegalAccessException {
      HashMap var0 = new HashMap();

      String var2;
      StringBuffer var4;
      for(Iterator var1 = parameters.keySet().iterator(); var1.hasNext(); var0.put(var2, var4.toString())) {
         var2 = (String)var1.next();
         Field var3 = (Field)parameters.get(var2.toUpperCase());
         var4 = new StringBuffer();
         Object var7;
         if ((var7 = var3.get((Object)null)) != null && var7.getClass().isArray()) {
            int var5 = Array.getLength(var7);

            for(int var6 = 0; var6 < var5; ++var6) {
               if (var6 > 0) {
                  var4.append(":");
               }

               var4.append(Array.get(var7, var6));
            }
         } else {
            var4.append(var7);
         }
      }

      return var0;
   }

   public void resetToDefaults() {
      Iterator var1 = parameters.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         Field var3 = (Field)parameters.get(var2.toUpperCase());

         try {
            Object var4 = defaultParameters.get(var2.toUpperCase());
            StringBuffer var5 = new StringBuffer();
            if (var4 != null && var4.getClass().isArray()) {
               int var6 = Array.getLength(var4);

               for(int var7 = 0; var7 < var6; ++var7) {
                  if (var7 > 0) {
                     var5.append(":");
                  }

                  var5.append(Array.get(var4, var7));
               }
            } else {
               var5.append(var4);
            }

            this.setValue(var3, var5.toString());
         } catch (IllegalAccessException | IllegalArgumentException var8) {
            Logger.getInstance().err("Failed to reset property field " + var2, var8);
         }
      }

   }

   public void setValue(Field var1, String var2) throws IllegalArgumentException, IllegalAccessException {
      if (var1.getType().isEnum()) {
         var1.set(this, Enum.valueOf(var1.getType(), var2.toUpperCase()));
      } else {
         if (var1.getType().isArray()) {
            if (var2.equals("null")) {
               var1.set(this, (Object)null);
               return;
            }

            if (var1.getType().isAssignableFrom(String[].class)) {
               var1.set(this, var2.split(":"));
               return;
            }

            int var4;
            int var5;
            int var6;
            String var7;
            String[] var8;
            if (var1.getType().isAssignableFrom(Properties.Coefficient[].class)) {
               Properties.Coefficient[] var9 = new Properties.Coefficient[(var8 = var2.split(":")).length];
               var4 = 0;
               var5 = (var8 = var8).length;

               for(var6 = 0; var6 < var5; ++var6) {
                  var7 = var8[var6];
                  var9[var4++] = (Properties.Coefficient)Enum.valueOf(Properties.Coefficient.class, var7.toUpperCase());
               }

               var1.set(this, var9);
               return;
            }

            if (var1.getType().isAssignableFrom(StatisticsVariables[].class)) {
               StatisticsVariables[] var3 = new StatisticsVariables[(var8 = var2.split(":")).length];
               var4 = 0;
               var5 = (var8 = var8).length;

               for(var6 = 0; var6 < var5; ++var6) {
                  var7 = var8[var6];
                  var3[var4++] = (StatisticsVariables)Enum.valueOf(StatisticsVariables.class, var7.toUpperCase());
               }

               var1.set(this, var3);
               return;
            }
         } else {
            if (var1.getType().equals(Integer.class)) {
               var1.set(this, Integer.parseInt(var2));
               return;
            }

            if (var1.getType().equals(Boolean.class)) {
               var1.set(this, Boolean.parseBoolean(var2));
               return;
            }

            if (var2.equals("null")) {
               var1.set(this, (Object)null);
               return;
            }

            var1.set(this, var2);
         }

      }
   }

   public void setValue(String var1, String var2) throws IllegalArgumentException, IllegalAccessException {
      if (!parameters.containsKey(var1.toUpperCase())) {
         throw new IllegalArgumentException();
      } else {
         Field var3 = (Field)parameters.get(var1.toUpperCase());
         this.setValue(var3, var2);
      }
   }

   public void setValues(Map<String, String> var1) throws IllegalArgumentException, IllegalAccessException {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!parameters.containsKey(var3.toUpperCase())) {
            throw new IllegalArgumentException();
         }

         Field var4 = (Field)parameters.get(var3.toUpperCase());
         this.setValue(var4, (String)var1.get(var3));
      }

   }

   static {
      LOGLEVEL = Logger.LogLevel.INFO;
      TIMELIMIT = 600;
      TEST_TIMEOUT = 60;
      NUMBER_OF_TEST_RUNS_IN_PARALLEL = -1;
      ENABLE_ASSERTIONS_FOR_GZOLTAR = true;
      CONFIGURATIONID = null;
      IS_RUNNING_A_GZOLTAR_TEST = false;
      MAX_CLIENT_MEMORY = 1024;
      MAX_PERM_SIZE = 256;
      SHOW_PROGRESS_BAR = true;
      GZOLTAR_CLASSLOADER = true;
      GZOLTAR_CONTEXT_CLASSLOADER = true;
      STRATEGY = Properties.Strategy.COVERAGE;
      MUTATION_STRATEGY = Properties.MutationStrategy.METALLAXIS;
      COEFFICIENTS = new Properties.Coefficient[]{Properties.Coefficient.OCHIAI};
      CANDIDATE_GENERATION_ALGORITHM = Properties.CandidateGeneration.STACCATO;
      GRANULARITY = Component.Granularity.STATEMENT;
      UNIQUE_LINE_NUMBERS = true;
      STAR = 2;
      MUTATION_BACKEND = Properties.MutationBackend.MAJOR;
      MAJOR_JAVAC = null;
      MUTANTS_DIR = "mutants";
      MUTANT_DIR = null;
      MUTANTS_LOG = "mutants.log";
      MUTANTS = -1;
      ONLY_RUN_TESTS_THAT_COVER_COMPONENT = true;
      MINIMIZATION_STRATEGY = Properties.MinimizationStrategy.GREEDY;
      MINION_EXEC = null;
      MINION_NUM_SOLUTIONS = -1;
      MINION_TIMEOUT = 60;
      GZOLTAR_DATA_DIR = "gzoltar-data";
      PRINT_MATRIX = true;
      MATRIX_FILE = "matrix";
      EXTRA_MATRIX_FILE = null;
      FILTER_SPECTRA = Properties.FilterSpectra.TEST_CASES_WITHOUT_ANY_COVERAGE;
      NORMALIZE_SPECTRA = true;
      PRINT_SPECTRA = true;
      SPECTRA_FILE = "spectra";
      SERIALIZE_SPECTRA = false;
      VERBOSE_SPECTRA = true;
      INCLUDE_SUSPICIOUSNESS_VALUE = true;
      OUTPUT_VARIABLES = new StatisticsVariables[]{StatisticsVariables.NUMBER_OF_CLASSES, StatisticsVariables.NUMBER_OF_TEST_CLASSES, StatisticsVariables.NUMBER_OF_UNIT_TEST_CASES, StatisticsVariables.NUMBER_OF_COMPONENTS, StatisticsVariables.AMBIGUITY_GROUPS, StatisticsVariables.INFORMATION_GAIN, StatisticsVariables.RHO};
      CLASSES_FILE = "classes";
      TESTS_FILE = "tests";
      STATISTICS_FILE = "statistics.csv";
      STATISTICS_BACKEND = Properties.StatisticsBackend.CSV;
      PROJECTID = null;
      SOURCEDIR = null;
      CLASSESDIR = null;
      TESTSDIR = null;
      DEPS = null;
      PROJECT_CP = null;
      NATIVE_LIBRARIES = null;
      TARGETCLASSES = new String[]{"*"};
      TARGETPACKAGES = null;
      TESTCLASSES = new String[]{"*"};
      TESTMETHODS = null;
      TESTPACKAGES = null;
      INCLUDE_DEPRECATED_CLASSES = true;
      INCLUDE_DEPRECATED_METHODS = true;
      INCLUDE_LOCAL_CLASSES = true;
      FAULTY_COMPONENTS = null;
      INSTRUMENT_TEST_CLASSES = false;
      SEARCH_FOR_CLASSES_ON_JAR_FILES = false;
      instance = null;
      parameters = new HashMap();
      defaultParameters = new HashMap();
      initializeAllParameters();
   }

   public static enum StatisticsBackend {
      NONE,
      CONSOLE,
      CSV;
   }

   public static enum FilterSpectra {
      NONE,
      TEST_CASES_WITHOUT_ANY_COVERAGE,
      COMPONENTS_TRIGGERED_BY_FAILING_TEST_CASES;
   }

   public static enum MinimizationStrategy {
      CSP,
      GREEDY;
   }

   public static enum MutationBackend {
      MAJOR,
      PIT,
      CUSTOM;
   }

   public static enum CandidateGeneration {
      STACCATO;
   }

   public static enum Coefficient {
      OCHIAI,
      OCHIAI2,
      TARANTULA,
      JACCARD,
      SBI,
      KULCZYNSKI2,
      SORENSEN_DICE,
      ANDERBERG,
      SIMPLE_MATCHING,
      ROGERS_TANIMOTO,
      RUSSEL_RAO,
      DSTAR,
      OPT,
      BARINEL,
      BARINEL_SR,
      IDEAL,
      NAISH1;
   }

   public static enum MutationStrategy {
      METALLAXIS,
      MUSE;
   }

   public static enum Strategy {
      COVERAGE,
      MUTATION;
   }

   @Retention(RetentionPolicy.RUNTIME)
   @Target({ElementType.FIELD})
   public @interface Parameter {
      String key();

      String group() default "Parameter";

      String description();
   }
}
