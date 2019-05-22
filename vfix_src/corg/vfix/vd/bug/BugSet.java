package corg.vfix.vd.bug;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class BugSet {
   private static ArrayList<BugRecord> bugRecords = new ArrayList();

   public static void addBug(BugRecord c) {
      bugRecords.add(c);
   }

   public static void printAll() {
      String record = genRecordHead();
      System.out.println(record);
      Iterator var2 = bugRecords.iterator();

      while(var2.hasNext()) {
         BugRecord c = (BugRecord)var2.next();
         print(c);
      }

   }

   public static void printAllLocationStat() {
      String record = genLocationStatRecordHead();
      System.out.println(record);
      Iterator var2 = bugRecords.iterator();

      while(var2.hasNext()) {
         BugRecord c = (BugRecord)var2.next();
         printLocationStat(c);
      }

   }

   private static void printOperationStat(BugRecord br) {
      String record = genOperationStatRecord(br);
      System.out.println(record);
   }

   private static void printLocationStat(BugRecord br) {
      String record = genLocationStatRecord(br);
      System.out.println(record);
   }

   public static String genLocationStatRecord(BugRecord br) {
      double t = (double)br.numOfTotalExecuted;
      double n = (double)br.numOfVfgNodes;
      double l = (double)br.numOfVfgExecuted;
      double nt = t == 0.0D ? 0.0D : n / t;
      double ln = n == 0.0D ? 0.0D : l / n;
      double lt = t == 0.0D ? 0.0D : l / t;
      return genLocationStatRecord(br.getBugID(), t, n, l, nt, ln, lt);
   }

   public static void printAllOperationStat() {
      String record = genOperationStatRecordHead();
      System.out.println(record);
      Iterator var2 = bugRecords.iterator();

      while(var2.hasNext()) {
         BugRecord c = (BugRecord)var2.next();
         printOperationStat(c);
      }

   }

   private static String genOperationStatRecordHead() {
      String bugID = String.format("%1$-18s", "Bug ID");
      String OPS = String.format("%1$-12s", "Operation");
      String OPS_CUR = String.format("%1$-15s", "#Operation");
      String OPS_TOTAL = String.format("%1$-10s", "#Total");
      String OPS_AVERAGE = String.format("%1$-10s", "#Average");
      return bugID + OPS + OPS_CUR + OPS_TOTAL + OPS_AVERAGE;
   }

   public static String genOperationStatRecord(BugRecord br) {
      String ops = br.getFinalOperation();
      int ops_cur = br.getCurOperationNum();
      int ops_total = br.getTotalOperations();
      double ops_average = br.getAverageOperations();
      String bugID = String.format("%1$-18s", br.getBugID());
      String OPS = String.format("%1$-12s", ops);
      String OPS_CUR = String.format("%1$-15s", ops_cur);
      String OPS_TOTAL = String.format("%1$-10s", ops_total);
      String OPS_AVERAGE = String.format("%1$-10s", ops_average);
      return bugID + OPS + OPS_CUR + OPS_TOTAL + OPS_AVERAGE;
   }

   private static String genLocationStatRecordHead() {
      String bugID = String.format("%1$-18s", "Bug ID");
      String totalLs = String.format("%1$-12s", "#total(T)");
      String totalVFGLs = String.format("%1$-16s", "#vfg_nodes(N)");
      String executedVFGLs = String.format("%1$-25s", "#selected locations(L)");
      String nt = String.format("%1$-9s", "N/T");
      String ln = String.format("%1$-9s", "L/N");
      String lt = String.format("%1$-9s", "L/T");
      return bugID + totalLs + totalVFGLs + executedVFGLs + nt + ln + lt;
   }

   private static String genLocationStatRecord(String id, double total, double total_vfg, double executed_vfg, double NT, double LN, double LT) {
      String bugID = String.format("%1$-18s", id);
      String totalLs = String.format("%1$-12s", total);
      String totalVFGLs = String.format("%1$-16s", total_vfg);
      String executedVFGLs = String.format("%1$-25s", executed_vfg);
      NumberFormat format = NumberFormat.getPercentInstance();
      format.setMaximumFractionDigits(2);
      String nt = String.format("%1$-9s", format.format(NT));
      String ln = String.format("%1$-9s", format.format(LN));
      String lt = String.format("%1$-9s", format.format(LT));
      return bugID + totalLs + totalVFGLs + executedVFGLs + nt + ln + lt;
   }

   public static String genRecordHead() {
      String bugID = String.format("%1$-18s", "Bug ID");
      String nthPatch = String.format("%1$-25s", "N-th patch passing test");
      String numOfPatch = String.format("%1$-10s", "#Patches");
      String time = String.format("%1$-10s", "Time(s)");
      return bugID + nthPatch + numOfPatch + time;
   }

   public static String genRecord(BugRecord c) {
      return genRecord(c.getBugID(), c.getNthPatch(), c.getNumOfPatch(), c.getTime());
   }

   public static String genRecord(String id, int nth, int num, double t) {
      String bugID = String.format("%1$-18s", id);
      String nthPatch = String.format("%1$-25s", nth);
      String numOfPatch = String.format("%1$-10s", num);
      String time = String.format("%1$-10s", t);
      return bugID + nthPatch + numOfPatch + time;
   }

   public static void print(BugRecord c) {
      String record = genRecord(c);
      System.out.println(record);
   }

   public static void main(String[] args) {
      BugRecord br = new BugRecord("chart", 4);
      br.numOfTotalExecuted = 15;
      br.numOfVfgExecuted = 2;
      br.numOfVfgNodes = 7;
      addBug(br);
      printAllLocationStat();
   }
}
