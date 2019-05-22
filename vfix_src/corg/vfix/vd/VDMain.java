package corg.vfix.vd;

import corg.vfix.Main;
import corg.vfix.pg.FileOperationLib;
import corg.vfix.pg.jimple.OperationLib;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VDMain {
   private static String dataset_path;
   private static String test_class;
   private static String work_dir;
   private static String deps;
   public static int[] chartID = new int[]{2, 4, 14, 15, 16, 25, 26};
   public static int[] langID = new int[]{20, 33, 39, 47, 57};
   public static int[] mathID = new int[]{4, 70, 79};
   public static int[] felixID = new int[]{4960, 5464};
   public static int[] slingID = new int[]{4982, 6487};
   public static int[] collectionsID = new int[]{39, 360};
   public static int[] pdfboxID = new int[]{2266, 2477, 2812, 2948, 2951, 2965, 2995, 3479, 3572};

   public static void init(String testClass, String d) {
      test_class = testClass;
      work_dir = System.getProperty("user.dir");
      dataset_path = work_dir + "/dataset";
      deps = d;
   }

   private static boolean runOneTest(String command) throws Exception {
      String result = "";

      try {
         Process pr = Runtime.getRuntime().exec(command);
         BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
         String line = null;

         while((line = input.readLine()) != null) {
            System.out.println(line);
            if (line.length() > 0) {
               result = line;
            }
         }
      } catch (Exception var5) {
         System.out.println(var5.toString());
         var5.printStackTrace();
      }

      return !result.contains("Failures:") ? true : isPassed(result);
   }

   private static boolean isPassed(String str) {
      int index = str.indexOf("Failures:");
      char failNumber = str.charAt(index + 10);
      return '0' == failNumber;
   }

   public static void main(String[] args) throws Exception {
      runOneProjectTests("chart", chartID);
      runOneProjectTests("lang", langID);
      runOneProjectTests("felix", felixID);
      runOneProjectTests("sling", slingID);
      runOneProjectTests("collections", collectionsID);
      runOneProjectTests("pdfbox", pdfboxID);
   }

   private static void runOneProjectTests(String project, int[] projectIDs) throws Exception {
      int failCount = 0;
      int passCount = 0;
      int[] var7 = projectIDs;
      int var6 = projectIDs.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         int id = var7[var5];
         if (runOneTest(project, id)) {
            ++passCount;
            System.out.println("******PASS******");
         } else {
            ++failCount;
            System.out.println("******FAIL******");
         }
      }

      System.out.println(project + " #bugs: " + projectIDs.length + " passed: " + passCount + " failed: " + failCount);
   }

   public static void main(String project, int id, String runtime_id) throws Exception {
      int patchNum = OperationLib.getPatchID();
      OperationLib.clearPatchID();

      for(int i = 1; i <= patchNum; ++i) {
         boolean passed = runOneTest(project, id, runtime_id, i);
         if (passed) {
            System.out.println("No." + i + " patch (" + patchNum + " patches) passes the test");
            Main.bug.setPatchPass(i);
            return;
         }
      }

      System.out.println("no patch passes the test");
      Main.bug.setPatchPass(-1);
   }

   public static boolean runOneTest(String project, int id, String runtime_id, int patch_id) throws Exception {
      FileOperationLib.copyDataToTmp(project, id);
      FileOperationLib.copyPatchToTmp(project, id, runtime_id, patch_id);
      String projectDir = System.getProperty("user.dir") + "/";
      String file_config = projectDir + "dataset/" + project + "-" + id + "/config/config";
      configParser(file_config);
      init(test_class, deps);
      String tmpPath = work_dir + "/tmp/" + project + "-" + id;
      String classPath = tmpPath + "/target/classes/";
      String testPath = tmpPath + "/target/test-classes/";
      String libPath = work_dir + "/lib";
      String junitJar = libPath + "/junit-4.11.jar";
      String hamcrestJar = libPath + "/hamcrest-core-1.3.jar";
      String depsPath = generateDeps(classPath, deps);
      String command = "java -cp " + junitJar + ":" + hamcrestJar + ":" + classPath + ":" + testPath + ":" + depsPath + " org.junit.runner.JUnitCore " + test_class;
      return runOneTest(command);
   }

   private static boolean runOneTest(String project, int id) throws Exception {
      String projectDir = System.getProperty("user.dir") + "/";
      String file_config = projectDir + "dataset/" + project + "-" + id + "/config/config";
      configParser(file_config);
      init(test_class, deps);
      String classPath = dataset_path + "/" + project + "-" + id + "/target/classes/";
      String testPath = dataset_path + "/" + project + "-" + id + "/target/test-classes/";
      String libPath = work_dir + "/lib";
      String junitJar = libPath + "/junit-4.11.jar";
      String hamcrestJar = libPath + "/hamcrest-core-1.3.jar";
      String depsPath = generateDeps(classPath, deps);
      String command = "java -cp " + junitJar + ":" + hamcrestJar + ":" + classPath + ":" + testPath + ":" + depsPath + " org.junit.runner.JUnitCore " + test_class;
      System.out.println("command: " + command);
      return runOneTest(command);
   }

   private static void configParser(String file_config) throws IOException {
      File file = new File(file_config);
      BufferedReader br = new BufferedReader(new FileReader(file));

      String s;
      while((s = br.readLine()) != null) {
         String str = s.trim();
         String[] tokens = str.split("=");
         if ("test".equals(tokens[0])) {
            test_class = tokens[1];
         } else if ("deps".equals(tokens[0])) {
            deps = tokens[1];
         }
      }

      br.close();
   }

   private static String generateDeps(String classesDir, String dependency) {
      String deps = "";
      if (dependency != null && !"".equals(dependency)) {
         String[] ds = dependency.split(":");
         String[] var7 = ds;
         int var6 = ds.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String d = var7[var5];
            deps = deps + classesDir + d + ":";
         }

         if (deps.length() > 0) {
            deps = deps.substring(0, deps.length() - 1);
         }

         return deps;
      } else {
         return deps;
      }
   }
}
