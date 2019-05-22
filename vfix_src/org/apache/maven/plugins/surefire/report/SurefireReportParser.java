package org.apache.maven.plugins.surefire.report;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.maven.reporting.MavenReportException;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.StringUtils;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.DirectoryScanner;
import org.xml.sax.SAXException;

public class SurefireReportParser {
   private static final String INCLUDES = "*.xml";
   private static final String EXCLUDES = "*.txt, testng-failed.xml, testng-failures.xml, testng-results.xml, failsafe-summary*.xml";
   private NumberFormat numberFormat = NumberFormat.getInstance();
   private List<File> reportsDirectories;
   private final List<ReportTestSuite> testSuites = new ArrayList();
   private static final int PCENT = 100;

   public SurefireReportParser() {
   }

   public SurefireReportParser(List<File> reportsDirectoriesFiles, Locale locale) {
      this.reportsDirectories = reportsDirectoriesFiles;
      this.setLocale(locale);
   }

   public List<ReportTestSuite> parseXMLReportFiles() throws MavenReportException {
      List<File> xmlReportFileList = new ArrayList();
      Iterator i$ = this.reportsDirectories.iterator();

      while(true) {
         File reportsDirectory;
         do {
            if (!i$.hasNext()) {
               TestSuiteXmlParser parser = new TestSuiteXmlParser();

               Collection suites;
               for(Iterator i$ = xmlReportFileList.iterator(); i$.hasNext(); this.testSuites.addAll(suites)) {
                  File aXmlReportFileList = (File)i$.next();

                  try {
                     suites = parser.parse(aXmlReportFileList.getAbsolutePath());
                  } catch (ParserConfigurationException var10) {
                     throw new MavenReportException("Error setting up parser for JUnit XML report", var10);
                  } catch (SAXException var11) {
                     throw new MavenReportException("Error parsing JUnit XML report " + aXmlReportFileList, var11);
                  } catch (IOException var12) {
                     throw new MavenReportException("Error reading JUnit XML report " + aXmlReportFileList, var12);
                  }
               }

               return this.testSuites;
            }

            reportsDirectory = (File)i$.next();
         } while(!reportsDirectory.exists());

         String[] xmlReportFiles = getIncludedFiles(reportsDirectory, "*.xml", "*.txt, testng-failed.xml, testng-failures.xml, testng-results.xml, failsafe-summary*.xml");
         String[] arr$ = xmlReportFiles;
         int len$ = xmlReportFiles.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String xmlReportFile = arr$[i$];
            File xmlReport = new File(reportsDirectory, xmlReportFile);
            xmlReportFileList.add(xmlReport);
         }
      }
   }

   protected String parseTestSuiteName(String lineString) {
      return lineString.substring(lineString.lastIndexOf(".") + 1, lineString.length());
   }

   protected String parseTestSuitePackageName(String lineString) {
      return lineString.substring(lineString.indexOf(":") + 2, lineString.lastIndexOf("."));
   }

   protected String parseTestCaseName(String lineString) {
      return lineString.substring(0, lineString.indexOf("("));
   }

   public Map<String, String> getSummary(List<ReportTestSuite> suites) {
      Map<String, String> totalSummary = new HashMap();
      int totalNumberOfTests = 0;
      int totalNumberOfErrors = 0;
      int totalNumberOfFailures = 0;
      int totalNumberOfSkipped = 0;
      float totalElapsedTime = 0.0F;

      ReportTestSuite suite;
      for(Iterator i$ = suites.iterator(); i$.hasNext(); totalElapsedTime += suite.getTimeElapsed()) {
         suite = (ReportTestSuite)i$.next();
         totalNumberOfTests += suite.getNumberOfTests();
         totalNumberOfErrors += suite.getNumberOfErrors();
         totalNumberOfFailures += suite.getNumberOfFailures();
         totalNumberOfSkipped += suite.getNumberOfSkipped();
      }

      String totalPercentage = this.computePercentage(totalNumberOfTests, totalNumberOfErrors, totalNumberOfFailures, totalNumberOfSkipped);
      totalSummary.put("totalTests", Integer.toString(totalNumberOfTests));
      totalSummary.put("totalErrors", Integer.toString(totalNumberOfErrors));
      totalSummary.put("totalFailures", Integer.toString(totalNumberOfFailures));
      totalSummary.put("totalSkipped", Integer.toString(totalNumberOfSkipped));
      totalSummary.put("totalElapsedTime", this.numberFormat.format((double)totalElapsedTime));
      totalSummary.put("totalPercentage", totalPercentage);
      return totalSummary;
   }

   public void setReportsDirectory(File reportsDirectory) {
      this.reportsDirectories = Collections.singletonList(reportsDirectory);
   }

   public final void setLocale(Locale locale) {
      this.numberFormat = NumberFormat.getInstance(locale);
   }

   public NumberFormat getNumberFormat() {
      return this.numberFormat;
   }

   public Map<String, List<ReportTestSuite>> getSuitesGroupByPackage(List<ReportTestSuite> testSuitesList) {
      Map<String, List<ReportTestSuite>> suitePackage = new HashMap();
      Iterator i$ = testSuitesList.iterator();

      while(i$.hasNext()) {
         ReportTestSuite suite = (ReportTestSuite)i$.next();
         List<ReportTestSuite> suiteList = new ArrayList();
         if (suitePackage.get(suite.getPackageName()) != null) {
            suiteList = (List)suitePackage.get(suite.getPackageName());
         }

         ((List)suiteList).add(suite);
         suitePackage.put(suite.getPackageName(), suiteList);
      }

      return suitePackage;
   }

   public String computePercentage(int tests, int errors, int failures, int skipped) {
      float percentage;
      if (tests == 0) {
         percentage = 0.0F;
      } else {
         percentage = (float)(tests - errors - failures - skipped) / (float)tests * 100.0F;
      }

      return this.numberFormat.format((double)percentage);
   }

   public List<ReportTestCase> getFailureDetails(List<ReportTestSuite> testSuitesList) {
      List<ReportTestCase> failureDetailList = new ArrayList();
      Iterator i$ = testSuitesList.iterator();

      while(true) {
         List testCaseList;
         do {
            if (!i$.hasNext()) {
               return failureDetailList;
            }

            ReportTestSuite suite = (ReportTestSuite)i$.next();
            testCaseList = suite.getTestCases();
         } while(testCaseList == null);

         Iterator i$ = testCaseList.iterator();

         while(i$.hasNext()) {
            ReportTestCase tCase = (ReportTestCase)i$.next();
            if (tCase.getFailure() != null) {
               failureDetailList.add(tCase);
            }
         }
      }
   }

   public static boolean hasReportFiles(File directory) {
      return directory != null && directory.isDirectory() && getIncludedFiles(directory, "*.xml", "*.txt, testng-failed.xml, testng-failures.xml, testng-results.xml, failsafe-summary*.xml").length > 0;
   }

   private static String[] getIncludedFiles(File directory, String includes, String excludes) {
      DirectoryScanner scanner = new DirectoryScanner();
      scanner.setBasedir(directory);
      scanner.setIncludes(StringUtils.split(includes, ","));
      scanner.setExcludes(StringUtils.split(excludes, ","));
      scanner.scan();
      return scanner.getIncludedFiles();
   }
}
