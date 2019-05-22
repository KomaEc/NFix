package org.testng.internal.thread.graph;

import java.util.Iterator;
import java.util.List;
import org.testng.ISuite;
import org.testng.SuiteRunnerWorker;
import org.testng.collections.Lists;
import org.testng.internal.SuiteRunnerMap;

public class SuiteWorkerFactory implements IThreadWorkerFactory<ISuite> {
   private Integer m_verbose;
   private String m_defaultSuiteName;
   private SuiteRunnerMap m_suiteRunnerMap;

   public SuiteWorkerFactory(SuiteRunnerMap suiteRunnerMap, Integer verbose, String defaultSuiteName) {
      this.m_suiteRunnerMap = suiteRunnerMap;
      this.m_verbose = verbose;
      this.m_defaultSuiteName = defaultSuiteName;
   }

   public List<IWorker<ISuite>> createWorkers(List<ISuite> suites) {
      List<IWorker<ISuite>> suiteWorkers = Lists.newArrayList();
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suiteRunner = (ISuite)i$.next();
         SuiteRunnerWorker worker = new SuiteRunnerWorker(suiteRunner, this.m_suiteRunnerMap, this.m_verbose, this.m_defaultSuiteName);
         suiteWorkers.add(worker);
      }

      return suiteWorkers;
   }
}
