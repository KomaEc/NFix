package org.apache.maven.plugin.surefire.runorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.surefire.report.ReportEntry;

public class RunEntryStatisticsMap {
   private final Map<String, RunEntryStatistics> runEntryStatistics;
   private static final Pattern PARENS = Pattern.compile("^[^\\(\\)]+\\(([^\\\\(\\\\)]+)\\)$");

   public RunEntryStatisticsMap(Map<String, RunEntryStatistics> runEntryStatistics) {
      this.runEntryStatistics = Collections.synchronizedMap(runEntryStatistics);
   }

   public RunEntryStatisticsMap() {
      this(new HashMap());
   }

   public static RunEntryStatisticsMap fromFile(File file) {
      if (file.exists()) {
         try {
            FileReader fileReader = new FileReader(file);
            return fromReader(fileReader);
         } catch (FileNotFoundException var2) {
            throw new RuntimeException(var2);
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      } else {
         return new RunEntryStatisticsMap();
      }
   }

   static RunEntryStatisticsMap fromReader(Reader fileReader) throws IOException {
      Map<String, RunEntryStatistics> result = new HashMap();
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
         if (!line.startsWith("#")) {
            RunEntryStatistics stats = RunEntryStatistics.fromString(line);
            result.put(stats.getTestName(), stats);
         }
      }

      return new RunEntryStatisticsMap(result);
   }

   public void serialize(File file) throws FileNotFoundException {
      FileOutputStream fos = new FileOutputStream(file);
      PrintWriter printWriter = new PrintWriter(fos);
      List<RunEntryStatistics> items = new ArrayList(this.runEntryStatistics.values());
      Collections.sort(items, new RunEntryStatisticsMap.RunCountComparator());
      Iterator i$ = items.iterator();

      while(i$.hasNext()) {
         RunEntryStatistics item1 = (RunEntryStatistics)i$.next();
         printWriter.println(item1.getAsString());
      }

      printWriter.close();
   }

   public RunEntryStatistics findOrCreate(ReportEntry reportEntry) {
      RunEntryStatistics item = (RunEntryStatistics)this.runEntryStatistics.get(reportEntry.getName());
      return item != null ? item : RunEntryStatistics.fromReportEntry(reportEntry);
   }

   public RunEntryStatistics createNextGeneration(ReportEntry reportEntry) {
      RunEntryStatistics newItem = this.findOrCreate(reportEntry);
      Integer elapsed = reportEntry.getElapsed();
      return newItem.nextGeneration(elapsed != null ? elapsed : 0);
   }

   public RunEntryStatistics createNextGenerationFailure(ReportEntry reportEntry) {
      RunEntryStatistics newItem = this.findOrCreate(reportEntry);
      Integer elapsed = reportEntry.getElapsed();
      return newItem.nextGenerationFailure(elapsed != null ? elapsed : 0);
   }

   public void add(RunEntryStatistics item) {
      this.runEntryStatistics.put(item.getTestName(), item);
   }

   public List<Class> getPrioritizedTestsClassRunTime(List testsToRun, int threadCount) {
      List<PrioritizedTest> prioritizedTests = this.getPrioritizedTests(testsToRun, new RunEntryStatisticsMap.TestRuntimeComparator());
      ThreadedExecutionScheduler threadedExecutionScheduler = new ThreadedExecutionScheduler(threadCount);
      Iterator i$ = prioritizedTests.iterator();

      while(i$.hasNext()) {
         Object prioritizedTest1 = (PrioritizedTest)i$.next();
         threadedExecutionScheduler.addTest((PrioritizedTest)prioritizedTest1);
      }

      return threadedExecutionScheduler.getResult();
   }

   public List<Class> getPrioritizedTestsByFailureFirst(List testsToRun) {
      List prioritizedTests = this.getPrioritizedTests(testsToRun, new RunEntryStatisticsMap.LeastFailureComparator());
      return this.transformToClasses(prioritizedTests);
   }

   private List<PrioritizedTest> getPrioritizedTests(List testsToRun, Comparator<Priority> priorityComparator) {
      Map classPriorities = this.getPriorities(priorityComparator);
      List<PrioritizedTest> tests = new ArrayList();
      Iterator i$ = testsToRun.iterator();

      while(i$.hasNext()) {
         Object aTestsToRun = i$.next();
         Class clazz = (Class)aTestsToRun;
         Priority pri = (Priority)classPriorities.get(clazz.getName());
         if (pri == null) {
            pri = Priority.newTestClassPriority(clazz.getName());
         }

         PrioritizedTest prioritizedTest = new PrioritizedTest(clazz, pri);
         tests.add(prioritizedTest);
      }

      Collections.sort(tests, new RunEntryStatisticsMap.PrioritizedTestComparator());
      return tests;
   }

   private List<Class> transformToClasses(List tests) {
      List<Class> result = new ArrayList();
      Iterator i$ = tests.iterator();

      while(i$.hasNext()) {
         Object test = i$.next();
         result.add(((PrioritizedTest)test).getClazz());
      }

      return result;
   }

   public Map getPriorities(Comparator<Priority> priorityComparator) {
      Map<String, Priority> priorities = new HashMap();
      Iterator i$ = this.runEntryStatistics.keySet().iterator();

      Priority pri;
      while(i$.hasNext()) {
         Object o = (String)i$.next();
         String testNames = (String)o;
         String clazzName = this.extractClassName(testNames);
         pri = (Priority)priorities.get(clazzName);
         if (pri == null) {
            pri = new Priority(clazzName);
            priorities.put(clazzName, pri);
         }

         RunEntryStatistics itemStat = (RunEntryStatistics)this.runEntryStatistics.get(testNames);
         pri.addItem(itemStat);
      }

      List<Priority> items = new ArrayList(priorities.values());
      Collections.sort(items, priorityComparator);
      Map<String, Priority> result = new HashMap();
      int i = 0;
      Iterator i$ = items.iterator();

      while(i$.hasNext()) {
         pri = (Priority)i$.next();
         pri.setPriority(i++);
         result.put(pri.getClassName(), pri);
      }

      return result;
   }

   String extractClassName(String displayName) {
      Matcher m = PARENS.matcher(displayName);
      return !m.find() ? displayName : m.group(1);
   }

   class LeastFailureComparator implements Comparator<Priority> {
      public int compare(Priority o, Priority o1) {
         return o.getMinSuccessRate() - o1.getMinSuccessRate();
      }
   }

   class TestRuntimeComparator implements Comparator<Priority> {
      public int compare(Priority o, Priority o1) {
         return o1.getTotalRuntime() - o.getTotalRuntime();
      }
   }

   class PrioritizedTestComparator implements Comparator<PrioritizedTest> {
      public int compare(PrioritizedTest o, PrioritizedTest o1) {
         return o.getPriority() - o1.getPriority();
      }
   }

   class RunCountComparator implements Comparator<RunEntryStatistics> {
      public int compare(RunEntryStatistics o, RunEntryStatistics o1) {
         int runtime = o.getSuccessfulBuilds() - o1.getSuccessfulBuilds();
         return runtime == 0 ? o.getRunTime() - o1.getRunTime() : runtime;
      }
   }
}
