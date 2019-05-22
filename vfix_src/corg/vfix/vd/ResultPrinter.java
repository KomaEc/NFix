package corg.vfix.vd;

import corg.vfix.pg.FileOperationLib;
import corg.vfix.vd.bug.BugRecord;
import corg.vfix.vd.bug.BugSet;

public class ResultPrinter {
   private static String resultPath;

   public static void init(String outputPath) {
      resultPath = outputPath;
   }

   public static void outputResult(String result) {
      String resultFile = resultPath + "/mainResult.txt";
      FileOperationLib.createDir(resultFile);
      FileOperationLib.printToFile(resultFile, result);
   }

   public static void outputOperationResult(String result) {
      String resultFile = resultPath + "/operation.txt";
      FileOperationLib.createDir(resultFile);
      FileOperationLib.printToFile(resultFile, result);
   }

   public static void outputLocationResult(String result) {
      String resultFile = resultPath + "/location.txt";
      FileOperationLib.createDir(resultFile);
      FileOperationLib.printToFile(resultFile, result);
   }

   public static void outputAllResult(BugRecord br) {
      outputResult(br);
      outputOperationResult(br);
      outputLocationResult(br);
   }

   public static void outputResult(BugRecord br) {
      String result = BugSet.genRecord(br);
      outputResult(result);
   }

   public static void outputOperationResult(BugRecord br) {
      String result = BugSet.genOperationStatRecord(br);
      outputOperationResult(result);
   }

   public static void outputLocationResult(BugRecord br) {
      String result = BugSet.genLocationStatRecord(br);
      outputLocationResult(result);
   }
}
