package corg.vfix;

import corg.vfix.fl.FLMain;
import corg.vfix.pg.PGMain;
import corg.vfix.sa.SAMain;
import corg.vfix.vd.ResultPrinter;
import corg.vfix.vd.VDMain;
import corg.vfix.vd.bug.BugRecord;
import corg.vfix.vd.bug.BugSet;

public class Main {
   public static int[] chartID = new int[]{2, 4, 14, 15, 16, 25, 26};
   public static int[] langID = new int[]{20, 33, 39, 47, 57};
   public static int[] mathID = new int[]{4, 70, 79};
   public static int[] felixID = new int[]{4960, 5464};
   public static int[] slingID = new int[]{4982, 6487};
   public static int[] collectionsID = new int[]{39, 360};
   public static int[] pdfboxID = new int[]{2266, 2477, 2812, 2948, 2951, 2965, 2995, 3479, 3572};
   public static BugRecord bug;

   public static void main(String[] args) throws Exception {
      if (args.length == 2) {
         runOne(args[0], args[1]);
      } else if (args.length > 2) {
         runOne(args[0], args[1], args[2]);
      }

   }

   public static void runOne(String project, String id) throws Exception {
      runOne(project, Integer.valueOf(id));
   }

   public static void runOne(String project, int id) throws Exception {
      runOne(project, id, Configure.getCurrentTimeUsingDate());
   }

   public static void run(String project, int id) throws Exception {
      runOne(project, id, Configure.getCurrentTimeUsingDate());
   }

   public static void runOne(String project, String id, String runtime_id) throws Exception {
      runOne(project, Integer.valueOf(id), runtime_id);
   }

   public static void runOne(String project, int id, String runtime_id) throws Exception {
      long start = System.currentTimeMillis();
      System.out.println("\n");
      System.out.println("########Fixing " + project + "-" + id + "########");
      Configure.init(project, id, runtime_id);
      FLMain.main();
      SAMain.main();
      PGMain.main(SAMain.nodes);
      VDMain.main(project, id, runtime_id);
      long end = System.currentTimeMillis();
      bug.setTime((double)((end - start) / 1000L));
      System.out.println("Total Time: " + (end - start) / 1000L + " seconds");
      BugSet.addBug(bug);
      BugSet.printAll();
      BugSet.printAllLocationStat();
      BugSet.printAllOperationStat();
      ResultPrinter.outputAllResult(bug);
   }
}
