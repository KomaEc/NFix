package org.apache.maven.surefire.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.plugin.surefire.runorder.RunEntryStatisticsMap;
import org.apache.maven.surefire.testset.RunOrderParameters;

public class DefaultRunOrderCalculator implements RunOrderCalculator {
   private final Comparator<Class> sortOrder;
   private final RunOrder[] runOrder;
   private final RunOrderParameters runOrderParameters;
   private final int threadCount;

   public DefaultRunOrderCalculator(RunOrderParameters runOrderParameters, int threadCount) {
      this.runOrderParameters = runOrderParameters;
      this.threadCount = threadCount;
      this.runOrder = runOrderParameters.getRunOrder();
      this.sortOrder = this.runOrder.length > 0 ? this.getSortOrderComparator(this.runOrder[0]) : null;
   }

   public TestsToRun orderTestClasses(TestsToRun scannedClasses) {
      List<Class> result = new ArrayList(500);
      Iterator i$ = scannedClasses.iterator();

      while(i$.hasNext()) {
         Class scannedClass = (Class)i$.next();
         result.add(scannedClass);
      }

      this.orderTestClasses(result, this.runOrder.length != 0 ? this.runOrder[0] : null);
      return new TestsToRun(result);
   }

   private void orderTestClasses(List<Class> testClasses, RunOrder runOrder) {
      if (RunOrder.RANDOM.equals(runOrder)) {
         Collections.shuffle(testClasses);
      } else {
         RunEntryStatisticsMap runEntryStatisticsMap;
         List prioritized;
         if (RunOrder.FAILEDFIRST.equals(runOrder)) {
            runEntryStatisticsMap = RunEntryStatisticsMap.fromFile(this.runOrderParameters.getRunStatisticsFile());
            prioritized = runEntryStatisticsMap.getPrioritizedTestsByFailureFirst(testClasses);
            testClasses.clear();
            testClasses.addAll(prioritized);
         } else if (RunOrder.BALANCED.equals(runOrder)) {
            runEntryStatisticsMap = RunEntryStatisticsMap.fromFile(this.runOrderParameters.getRunStatisticsFile());
            prioritized = runEntryStatisticsMap.getPrioritizedTestsClassRunTime(testClasses, this.threadCount);
            testClasses.clear();
            testClasses.addAll(prioritized);
         } else if (this.sortOrder != null) {
            Collections.sort(testClasses, this.sortOrder);
         }
      }

   }

   private Comparator<Class> getSortOrderComparator(RunOrder runOrder) {
      if (RunOrder.ALPHABETICAL.equals(runOrder)) {
         return this.getAlphabeticalComparator();
      } else if (RunOrder.REVERSE_ALPHABETICAL.equals(runOrder)) {
         return this.getReverseAlphabeticalComparator();
      } else if (RunOrder.HOURLY.equals(runOrder)) {
         int hour = Calendar.getInstance().get(11);
         return hour % 2 == 0 ? this.getAlphabeticalComparator() : this.getReverseAlphabeticalComparator();
      } else {
         return null;
      }
   }

   private Comparator<Class> getReverseAlphabeticalComparator() {
      return new Comparator<Class>() {
         public int compare(Class o1, Class o2) {
            return o2.getName().compareTo(o1.getName());
         }
      };
   }

   private Comparator<Class> getAlphabeticalComparator() {
      return new Comparator<Class>() {
         public int compare(Class o1, Class o2) {
            return o1.getName().compareTo(o2.getName());
         }
      };
   }
}
