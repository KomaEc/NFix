package com.gzoltar.shaded.org.pitest.coverage;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.F2;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.util.Log;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class CoverageData implements CoverageDatabase {
   private static final Logger LOG = Log.getLogger();
   private final Map<BlockLocation, Set<TestInfo>> blockCoverage = new LinkedHashMap();
   private final Map<BlockLocation, Set<Integer>> blocksToLines = new LinkedHashMap();
   private final Map<ClassName, Map<ClassLine, Set<TestInfo>>> lineCoverage = new LinkedHashMap();
   private final Map<String, Collection<ClassInfo>> classesForFile;
   private final CodeSource code;
   private final LineMap lm;
   private boolean hasFailedTest = false;

   public CoverageData(CodeSource code, LineMap lm) {
      this.code = code;
      this.lm = lm;
      this.classesForFile = FCollection.bucket(this.code.getCode(), keyFromClassInfo());
   }

   public Collection<TestInfo> getTestsForClassLine(ClassLine classLine) {
      Collection<TestInfo> result = (Collection)this.getTestsForClassName(classLine.getClassName()).get(classLine);
      return (Collection)(result == null ? Collections.emptyList() : result);
   }

   public boolean allTestsGreen() {
      return !this.hasFailedTest;
   }

   public Collection<ClassInfo> getClassInfo(Collection<ClassName> classes) {
      return this.code.getClassInfo(classes);
   }

   public int getNumberOfCoveredLines(Collection<ClassName> mutatedClass) {
      return (Integer)FCollection.fold(this.numberCoveredLines(), 0, mutatedClass);
   }

   public Collection<TestInfo> getTestsForClass(ClassName clazz) {
      Set<TestInfo> tis = new TreeSet(new TestInfoNameComparator());
      tis.addAll(FCollection.filter(this.blockCoverage.entrySet(), this.isFor(clazz)).flatMap(this.toTests()));
      return tis;
   }

   public void calculateClassCoverage(CoverageResult cr) {
      this.checkForFailedTest(cr);
      TestInfo ti = this.createTestInfo(cr.getTestUnitDescription(), cr.getExecutionTime(), cr.getNumberOfCoveredBlocks());
      Iterator i$ = cr.getCoverage().iterator();

      while(i$.hasNext()) {
         BlockLocation each = (BlockLocation)i$.next();
         this.addTestsToBlockMap(ti, each);
      }

   }

   private void addTestsToBlockMap(TestInfo ti, BlockLocation each) {
      Set<TestInfo> tests = (Set)this.blockCoverage.get(each);
      if (tests == null) {
         tests = new TreeSet(new TestInfoNameComparator());
         this.blockCoverage.put(each, tests);
      }

      ((Set)tests).add(ti);
   }

   public BigInteger getCoverageIdForClass(ClassName clazz) {
      Map<ClassLine, Set<TestInfo>> coverage = this.getTestsForClassName(clazz);
      return coverage.isEmpty() ? BigInteger.ZERO : this.generateCoverageNumber(coverage);
   }

   public List<BlockCoverage> createCoverage() {
      return FCollection.map(this.blockCoverage.entrySet(), toBlockCoverage());
   }

   private static F<Entry<BlockLocation, Set<TestInfo>>, BlockCoverage> toBlockCoverage() {
      return new F<Entry<BlockLocation, Set<TestInfo>>, BlockCoverage>() {
         public BlockCoverage apply(Entry<BlockLocation, Set<TestInfo>> a) {
            return new BlockCoverage((BlockLocation)a.getKey(), FCollection.map((Iterable)a.getValue(), TestInfo.toName()));
         }
      };
   }

   public Collection<ClassInfo> getClassesForFile(String sourceFile, String packageName) {
      Collection<ClassInfo> value = (Collection)this.getClassesForFileCache().get(keyFromSourceAndPackage(sourceFile, packageName));
      return (Collection)(value == null ? Collections.emptyList() : value);
   }

   private Map<String, Collection<ClassInfo>> getClassesForFileCache() {
      return this.classesForFile;
   }

   public CoverageSummary createSummary() {
      return new CoverageSummary(this.numberOfLines(), this.coveredLines());
   }

   private BigInteger generateCoverageNumber(Map<ClassLine, Set<TestInfo>> coverage) {
      BigInteger coverageNumber = BigInteger.ZERO;
      Set<ClassName> testClasses = new HashSet();
      FCollection.flatMapTo(coverage.values(), this.testsToClassName(), testClasses);

      ClassInfo each;
      for(Iterator i$ = this.code.getClassInfo(testClasses).iterator(); i$.hasNext(); coverageNumber = coverageNumber.add(each.getDeepHash())) {
         each = (ClassInfo)i$.next();
      }

      return coverageNumber;
   }

   private F<Set<TestInfo>, Iterable<ClassName>> testsToClassName() {
      return new F<Set<TestInfo>, Iterable<ClassName>>() {
         public Iterable<ClassName> apply(Set<TestInfo> a) {
            return FCollection.map(a, TestInfo.toDefiningClassName());
         }
      };
   }

   private static F<ClassInfo, String> keyFromClassInfo() {
      return new F<ClassInfo, String>() {
         public String apply(ClassInfo c) {
            return CoverageData.keyFromSourceAndPackage(c.getSourceFileName(), c.getName().getPackage().asJavaName());
         }
      };
   }

   private static String keyFromSourceAndPackage(String sourceFile, String packageName) {
      return packageName + " " + sourceFile;
   }

   private Collection<ClassName> allClasses() {
      return this.code.getCodeUnderTestNames();
   }

   private int numberOfLines() {
      return (Integer)FCollection.fold(this.numberLines(), 0, this.code.getClassInfo(this.allClasses()));
   }

   private int coveredLines() {
      return (Integer)FCollection.fold(this.numberCoveredLines(), 0, this.allClasses());
   }

   private F2<Integer, ClassInfo, Integer> numberLines() {
      return new F2<Integer, ClassInfo, Integer>() {
         public Integer apply(Integer a, ClassInfo clazz) {
            return a + clazz.getNumberOfCodeLines();
         }
      };
   }

   private void checkForFailedTest(CoverageResult cr) {
      if (!cr.isGreenTest()) {
         this.recordTestFailure();
         LOG.warning(cr.getTestUnitDescription() + " did not pass without mutation.");
      }

   }

   private TestInfo createTestInfo(Description description, int executionTime, int linesCovered) {
      Option<ClassName> testee = this.code.findTestee(description.getFirstTestClass());
      return new TestInfo(description.getFirstTestClass(), description.getQualifiedName(), executionTime, testee, linesCovered);
   }

   private F2<Integer, ClassName, Integer> numberCoveredLines() {
      return new F2<Integer, ClassName, Integer>() {
         public Integer apply(Integer a, ClassName clazz) {
            return a + CoverageData.this.getNumberOfCoveredLines(clazz);
         }
      };
   }

   private int getNumberOfCoveredLines(ClassName clazz) {
      Map<ClassLine, Set<TestInfo>> map = this.getTestsForClassName(clazz);
      return map != null ? map.size() : 0;
   }

   private Map<ClassLine, Set<TestInfo>> getTestsForClassName(ClassName clazz) {
      Map<ClassLine, Set<TestInfo>> map = (Map)this.lineCoverage.get(clazz);
      return map != null ? map : this.convertBlockCoverageToLineCoverageForClass(clazz);
   }

   private Map<ClassLine, Set<TestInfo>> convertBlockCoverageToLineCoverageForClass(ClassName clazz) {
      List<Entry<BlockLocation, Set<TestInfo>>> tests = FCollection.filter(this.blockCoverage.entrySet(), this.isFor(clazz));
      Map<ClassLine, Set<TestInfo>> linesToTests = new LinkedHashMap(0);
      Iterator i$ = tests.iterator();

      while(i$.hasNext()) {
         Entry<BlockLocation, Set<TestInfo>> each = (Entry)i$.next();
         Iterator i$ = this.getLinesForBlock((BlockLocation)each.getKey()).iterator();

         while(i$.hasNext()) {
            int line = (Integer)i$.next();
            Set<TestInfo> tis = this.getLineTestSet(clazz, linesToTests, each, line);
            tis.addAll((Collection)each.getValue());
         }
      }

      this.lineCoverage.put(clazz, linesToTests);
      return linesToTests;
   }

   private Set<TestInfo> getLineTestSet(ClassName clazz, Map<ClassLine, Set<TestInfo>> linesToTests, Entry<BlockLocation, Set<TestInfo>> each, int line) {
      ClassLine cl = new ClassLine(clazz, line);
      Set<TestInfo> tis = (Set)linesToTests.get(cl);
      if (tis == null) {
         tis = new TreeSet(new TestInfoNameComparator());
         ((Set)tis).addAll((Collection)each.getValue());
         linesToTests.put(new ClassLine(clazz, line), tis);
      }

      return (Set)tis;
   }

   private Set<Integer> getLinesForBlock(BlockLocation bl) {
      Set<Integer> lines = (Set)this.blocksToLines.get(bl);
      if (lines == null) {
         this.calculateLinesForBlocks(bl.getLocation().getClassName());
         lines = (Set)this.blocksToLines.get(bl);
         if (lines == null) {
            lines = Collections.emptySet();
         }
      }

      return lines;
   }

   private void calculateLinesForBlocks(ClassName className) {
      Map<BlockLocation, Set<Integer>> lines = this.lm.mapLines(className);
      this.blocksToLines.putAll(lines);
   }

   private void recordTestFailure() {
      this.hasFailedTest = true;
   }

   private F<Entry<BlockLocation, Set<TestInfo>>, Iterable<TestInfo>> toTests() {
      return new F<Entry<BlockLocation, Set<TestInfo>>, Iterable<TestInfo>>() {
         public Iterable<TestInfo> apply(Entry<BlockLocation, Set<TestInfo>> a) {
            return (Iterable)a.getValue();
         }
      };
   }

   private Predicate<Entry<BlockLocation, Set<TestInfo>>> isFor(final ClassName clazz) {
      return new Predicate<Entry<BlockLocation, Set<TestInfo>>>() {
         public Boolean apply(Entry<BlockLocation, Set<TestInfo>> a) {
            return ((BlockLocation)a.getKey()).isFor(clazz);
         }
      };
   }
}
