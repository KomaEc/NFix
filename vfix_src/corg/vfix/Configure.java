package corg.vfix;

import corg.vfix.fl.GzoltarRunner;
import corg.vfix.parser.SpectraParser;
import corg.vfix.parser.TraceParser;
import corg.vfix.pg.java.JavaOperationLib;
import corg.vfix.pg.jimple.OperationLib;
import corg.vfix.sa.SootRunner;
import corg.vfix.sa.StaticSlicer;
import corg.vfix.sa.plot.CGPlot;
import corg.vfix.sa.plot.CongestionPlot;
import corg.vfix.sa.plot.DDGPlot;
import corg.vfix.sa.plot.VFGPlot;
import corg.vfix.sa.vfg.build.NPELocator;
import corg.vfix.vd.ResultPrinter;
import corg.vfix.vd.VDMain;
import corg.vfix.vd.bug.BugRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Configure {
   private static String projectDir;
   private static String classPath;
   private static String testPath;
   private static String vfgPath;
   private static String ddgPath;
   private static String cgPath;
   private static String patchPath;
   private static String resultPath;
   private static String deps;
   private static String mainClass;
   private static String testClass;
   private static String project;
   private static String id;
   private static String file_log;
   private static String file_config;
   private static String file_spectra;
   private static String file_tgtcls;
   private static String file_src;
   private static String nullObj;

   public static void init(String pid, int bid, String runtime_id) throws IOException {
      Main.bug = new BugRecord(pid, bid);
      init(pid, Integer.toString(bid), runtime_id);
   }

   public static void init(String pid, String bid, String runtime_id) throws IOException {
      System.out.println("init...");
      projectDir = System.getProperty("user.dir") + "/";
      System.out.println("Working Directory: " + projectDir);
      project = pid;
      id = bid;
      file_log = projectDir + "dataset/" + project + "-" + id + "/config/log";
      file_config = projectDir + "dataset/" + project + "-" + id + "/config/config";
      file_tgtcls = projectDir + "dataset/" + project + "-" + id + "/config/target";
      file_spectra = projectDir + "dataset/" + project + "-" + id + "/gzoltar-data/spectra";
      file_src = projectDir + "dataset/" + project + "-" + id + "/source";
      String projectID = pid + "-" + bid;
      vfgPath = projectDir + "output/" + runtime_id + "/" + projectID + "/vfgOutput";
      ddgPath = projectDir + "output/" + runtime_id + "/" + projectID + "/ddgOutput";
      cgPath = projectDir + "output/" + runtime_id + "/" + projectID + "/cgOutput";
      patchPath = projectDir + "output/" + runtime_id + "/" + projectID + "/patchOutput";
      resultPath = projectDir + "output/" + runtime_id + "/" + projectID + "/result";
      classPath = projectDir + "dataset/" + project + "-" + id + "/target/classes";
      testPath = projectDir + "dataset/" + project + "-" + id + "/target/test-classes";
      configParser();
      System.out.println("mainClass: " + mainClass);
      System.out.println("testClass: " + testClass);
      System.out.println("Bug ID: " + project + "-" + id);
      System.out.println();
      System.out.println("dataset checking...");
      if (!checkDataset()) {
         System.exit(0);
      }

      System.out.println("dataset loading...");
      loadDataset();
   }

   private static void loadDataset() throws IOException {
      System.out.println("SootRunner init...");
      SootRunner.init(classPath, mainClass);
      System.out.println("TraceParser init...");
      TraceParser.init(file_log);
      System.out.println("StaticSlicer init...");
      StaticSlicer.init(file_tgtcls);
      System.out.println("Validator init");
      VDMain.init(testClass, deps);
      System.out.println("GzoltarRunner init...");
      GzoltarRunner.init(classPath, testPath, testClass, deps);
      System.out.println("SpectraParser init...");
      SpectraParser.init(file_spectra);
      System.out.println("NPELocator init...");
      NPELocator.init(nullObj);
      System.out.println("VFGPlot init...");
      VFGPlot.init(vfgPath);
      System.out.println("CongestionPlot init...");
      CongestionPlot.init(vfgPath);
      System.out.println("CGPlot init...");
      CGPlot.init(cgPath);
      System.out.println("DDGPlot init...");
      DDGPlot.init(ddgPath);
      System.out.println("JimpleTransformer init...");
      OperationLib.init(patchPath);
      System.out.println("JavaTransformer init...");
      JavaOperationLib.init(file_src);
      System.out.println("ResultPrinter init...");
      ResultPrinter.init(resultPath);
   }

   private static boolean checkDataset() {
      File logFile = new File(file_log);
      File mainFile = new File(file_config);
      if (!logFile.exists()) {
         System.out.println(file_log + " does not exist...");
         return false;
      } else if (!mainFile.exists()) {
         System.out.println(file_config + " does not exist...");
         return false;
      } else {
         return true;
      }
   }

   private static void configParser() throws IOException {
      File file = new File(file_config);
      BufferedReader br = new BufferedReader(new FileReader(file));

      String s;
      while((s = br.readLine()) != null) {
         String str = s.trim();
         String[] tokens = str.split("=");
         if ("main".equals(tokens[0])) {
            mainClass = tokens[1];
         } else if ("test".equals(tokens[0])) {
            testClass = tokens[1];
         } else if ("nullpointer".equals(tokens[0])) {
            nullObj = tokens[1];
         } else if ("deps".equals(tokens[0])) {
            deps = tokens[1];
         }
      }

      br.close();
   }

   public static String getCurrentTimeUsingDate() {
      Date date = new Date();
      String strDateFormat = "MM-dd-HH-mm-ss";
      DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
      return dateFormat.format(date);
   }
}
