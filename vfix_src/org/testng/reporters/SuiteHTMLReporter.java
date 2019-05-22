package org.testng.reporters;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.Reporter;
import org.testng.collections.Maps;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

public class SuiteHTMLReporter implements IReporter {
   public static final String METHODS_CHRONOLOGICAL = "methods.html";
   public static final String METHODS_ALPHABETICAL = "methods-alphabetical.html";
   public static final String GROUPS = "groups.html";
   public static final String CLASSES = "classes.html";
   public static final String REPORTER_OUTPUT = "reporter-output.html";
   public static final String METHODS_NOT_RUN = "methods-not-run.html";
   public static final String TESTNG_XML = "testng.xml.html";
   private Map<String, ITestClass> m_classes = Maps.newHashMap();
   private String m_outputDirectory;
   private static final String SP = "&nbsp;";
   private static final String SP2 = "&nbsp;&nbsp;&nbsp;&nbsp;";
   private static final String SP3 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
   private static final String SP4 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
   public static final String AFTER = "&lt;&lt;";
   public static final String BEFORE = "&gt;&gt;";

   public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
      this.m_outputDirectory = this.generateOutputDirectoryName(outputDirectory + File.separator + "old");

      try {
         HtmlHelper.generateStylesheet(outputDirectory);
      } catch (IOException var7) {
         var7.printStackTrace();
      }

      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         ISuite suite = (ISuite)i$.next();
         XmlSuite xmlSuite = suite.getXmlSuite();
         if (xmlSuite.getTests().size() != 0) {
            this.generateTableOfContents(xmlSuite, suite);
            this.generateSuites(xmlSuite, suite);
            this.generateIndex(xmlSuite, suite);
            this.generateMain(xmlSuite, suite);
            this.generateMethodsAndGroups(xmlSuite, suite);
            this.generateMethodsChronologically(xmlSuite, suite, "methods.html", false);
            this.generateMethodsChronologically(xmlSuite, suite, "methods-alphabetical.html", true);
            this.generateClasses(xmlSuite, suite);
            this.generateReporterOutput(xmlSuite, suite);
            this.generateExcludedMethodsReport(xmlSuite, suite);
            this.generateXmlFile(xmlSuite, suite);
         }
      }

      this.generateIndex(suites);
   }

   protected String generateOutputDirectoryName(String outputDirectory) {
      return outputDirectory;
   }

   private void generateXmlFile(XmlSuite xmlSuite, ISuite suite) {
      String content = xmlSuite.toXml().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll(" ", "&nbsp;").replaceAll("\n", "<br/>");
      StringBuffer sb = new StringBuffer("<html>");
      sb.append("<head><title>").append("testng.xml for ").append(xmlSuite.getName()).append("</title></head><body><tt>").append(content).append("</tt></body></html>");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "testng.xml.html", sb.toString());
   }

   private void generateIndex(List<ISuite> suites) {
      StringBuffer sb = new StringBuffer();
      String title = "Test results";
      sb.append("<html>\n<head><title>" + title + "</title>").append(HtmlHelper.getCssString(".")).append("</head><body>\n").append("<h2><p align='center'>").append(title).append("</p></h2>\n").append("<table border='1' width='100%' class='main-page'>").append("<tr><th>Suite</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>testng.xml</th></tr>\n");
      int totalFailedTests = 0;
      int totalPassedTests = 0;
      int totalSkippedTests = 0;
      StringBuffer suiteBuf = new StringBuffer();
      Iterator i$ = suites.iterator();

      while(true) {
         ISuite suite;
         do {
            if (!i$.hasNext()) {
               String cls = totalFailedTests > 0 ? "invocation-failed" : (totalPassedTests > 0 ? "invocation-passed" : "invocation-failed");
               sb.append("<tr align='center' class='").append(cls).append("'>").append("<td><em>Total</em></td>").append("<td><em>").append(totalPassedTests).append("</em></td>").append("<td><em>").append(totalFailedTests).append("</em></td>").append("<td><em>").append(totalSkippedTests).append("</em></td>").append("<td>&nbsp;</td>").append("</tr>\n");
               sb.append(suiteBuf);
               sb.append("</table>").append("</body></html>\n");
               Utils.writeFile(this.m_outputDirectory, "index.html", sb.toString());
               return;
            }

            suite = (ISuite)i$.next();
         } while(suite.getResults().size() == 0);

         String name = suite.getName();
         int failedTests = 0;
         int passedTests = 0;
         int skippedTests = 0;
         Map<String, ISuiteResult> results = suite.getResults();

         ITestContext context;
         for(Iterator i$ = results.values().iterator(); i$.hasNext(); totalSkippedTests += context.getSkippedTests().size()) {
            ISuiteResult result = (ISuiteResult)i$.next();
            context = result.getTestContext();
            failedTests += context.getFailedTests().size();
            totalFailedTests += context.getFailedTests().size();
            passedTests += context.getPassedTests().size();
            totalPassedTests += context.getPassedTests().size();
            skippedTests += context.getSkippedTests().size();
         }

         String cls = failedTests > 0 ? "invocation-failed" : (passedTests > 0 ? "invocation-passed" : "invocation-failed");
         suiteBuf.append("<tr align='center' class='").append(cls).append("'>").append("<td><a href='").append(name).append("/index.html'>").append(name).append("</a></td>\n");
         suiteBuf.append("<td>" + passedTests + "</td>").append("<td>" + failedTests + "</td>").append("<td>" + skippedTests + "</td>").append("<td><a href='").append(name).append("/").append("testng.xml.html").append("'>Link").append("</a></td>").append("</tr>");
      }
   }

   private void generateExcludedMethodsReport(XmlSuite xmlSuite, ISuite suite) {
      Collection<ITestNGMethod> excluded = suite.getExcludedMethods();
      StringBuffer sb2 = new StringBuffer("<h2>Methods that were not run</h2><table>\n");
      Iterator i$ = excluded.iterator();

      while(i$.hasNext()) {
         ITestNGMethod method = (ITestNGMethod)i$.next();
         Method m = method.getMethod();
         if (m != null) {
            sb2.append("<tr><td>").append(m.getDeclaringClass().getName() + "." + m.getName());
            String description = method.getDescription();
            if (Utils.isStringNotEmpty(description)) {
               sb2.append("<br/>").append("&nbsp;&nbsp;&nbsp;&nbsp;").append("<i>").append(description).append("</i>");
            }

            sb2.append("</td></tr>\n");
         }
      }

      sb2.append("</table>");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "methods-not-run.html", sb2.toString());
   }

   private void generateReporterOutput(XmlSuite xmlSuite, ISuite suite) {
      StringBuffer sb = new StringBuffer();
      sb.append("<h2>Reporter output</h2>").append("<table>");
      List<String> output = Reporter.getOutput();
      Iterator i$ = output.iterator();

      while(i$.hasNext()) {
         String line = (String)i$.next();
         sb.append("<tr><td>").append(line).append("</td></tr>\n");
      }

      sb.append("</table>");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "reporter-output.html", sb.toString());
   }

   private void generateClasses(XmlSuite xmlSuite, ISuite suite) {
      StringBuffer sb = new StringBuffer();
      sb.append("<table border='1'>\n").append("<tr>\n").append("<th>Class name</th>\n").append("<th>Method name</th>\n").append("<th>Groups</th>\n").append("</tr>");
      Iterator i$ = this.m_classes.values().iterator();

      while(i$.hasNext()) {
         ITestClass tc = (ITestClass)i$.next();
         sb.append(this.generateClass(tc));
      }

      sb.append("</table>\n");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "classes.html", sb.toString());
   }

   private String generateClass(ITestClass cls) {
      StringBuffer sb = new StringBuffer();
      sb.append("<tr>\n").append("<td>").append(cls.getRealClass().getName()).append("</td>\n").append("<td>&nbsp;</td>").append("<td>&nbsp;</td>").append("</tr>\n");
      String[] tags = new String[]{"@Test", "@BeforeClass", "@BeforeMethod", "@AfterMethod", "@AfterClass"};
      ITestNGMethod[][] methods = new ITestNGMethod[][]{cls.getTestMethods(), cls.getBeforeClassMethods(), cls.getBeforeTestMethods(), cls.getAfterTestMethods(), cls.getAfterClassMethods()};

      for(int i = 0; i < tags.length; ++i) {
         sb.append("<tr>\n").append("<td align='center' colspan='3'>").append(tags[i]).append("</td>\n").append("</tr>\n").append(this.dumpMethods(methods[i]));
      }

      String result = sb.toString();
      return result;
   }

   private String dumpMethods(ITestNGMethod[] testMethods) {
      StringBuffer sb = new StringBuffer();
      if (null != testMethods && testMethods.length != 0) {
         ITestNGMethod[] arr$ = testMethods;
         int len$ = testMethods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod m = arr$[i$];
            sb.append("<tr>\n");
            sb.append("<td>&nbsp;</td>\n").append("<td>").append(m.getMethodName()).append("</td>\n");
            String[] groups = m.getGroups();
            if (groups != null && groups.length > 0) {
               sb.append("<td>");
               String[] arr$ = groups;
               int len$ = groups.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String g = arr$[i$];
                  sb.append(g).append(" ");
               }

               sb.append("</td>\n");
            } else {
               sb.append("<td>&nbsp;</td>");
            }

            sb.append("</tr>\n");
         }

         String result = sb.toString();
         return result;
      } else {
         return "";
      }
   }

   private String dumpGroups(String[] groups) {
      StringBuffer sb = new StringBuffer();
      if (null != groups && groups.length > 0) {
         sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append("<em>[");
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String g = arr$[i$];
            sb.append(g).append(" ");
         }

         sb.append("]</em><br/>\n");
      }

      String result = sb.toString();
      return result;
   }

   private void generateMethodsChronologically(XmlSuite xmlSuite, ISuite suite, String outputFileName, boolean alphabetical) {
      StringBuffer sb = new StringBuffer();
      sb.append("<h2>Methods run, sorted chronologically</h2>");
      sb.append("<h3>&gt;&gt; means before, &lt;&lt; means after</h3><p/>");
      long startDate = -1L;
      sb.append("<br/><em>").append(suite.getName()).append("</em><p/>");
      sb.append("<small><i>(Hover the method name to see the test class name)</i></small><p/>\n");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), outputFileName, sb.toString());
      sb = null;
      Collection<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
      if (alphabetical) {
         Comparator<? super ITestNGMethod> alphabeticalComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
               IInvokedMethod m1 = (IInvokedMethod)o1;
               IInvokedMethod m2 = (IInvokedMethod)o2;
               return m1.getTestMethod().getMethodName().compareTo(m2.getTestMethod().getMethodName());
            }
         };
         Collections.sort((List)invokedMethods, alphabeticalComparator);
      }

      SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
      StringBuffer table = new StringBuffer();
      boolean addedHeader = false;
      Iterator i$ = invokedMethods.iterator();

      while(i$.hasNext()) {
         IInvokedMethod iim = (IInvokedMethod)i$.next();
         ITestNGMethod tm = iim.getTestMethod();
         table.setLength(0);
         if (!addedHeader) {
            table.append("<table border=\"1\">\n").append("<tr>").append("<th>Time</th>").append("<th>Delta (ms)</th>").append("<th>Suite<br>configuration</th>").append("<th>Test<br>configuration</th>").append("<th>Class<br>configuration</th>").append("<th>Groups<br>configuration</th>").append("<th>Method<br>configuration</th>").append("<th>Test<br>method</th>").append("<th>Thread</th>").append("<th>Instances</th>").append("</tr>\n");
            addedHeader = true;
         }

         String methodName = tm.toString();
         boolean bc = tm.isBeforeClassConfiguration();
         boolean ac = tm.isAfterClassConfiguration();
         boolean bt = tm.isBeforeTestConfiguration();
         boolean at = tm.isAfterTestConfiguration();
         boolean bs = tm.isBeforeSuiteConfiguration();
         boolean as = tm.isAfterSuiteConfiguration();
         boolean bg = tm.isBeforeGroupsConfiguration();
         boolean ag = tm.isAfterGroupsConfiguration();
         boolean setUp = tm.isBeforeMethodConfiguration();
         boolean tearDown = tm.isAfterMethodConfiguration();
         boolean isClassConfiguration = bc || ac;
         boolean isGroupsConfiguration = bg || ag;
         boolean isTestConfiguration = bt || at;
         boolean isSuiteConfiguration = bs || as;
         boolean isSetupOrTearDown = setUp || tearDown;
         String configurationClassMethod = isClassConfiguration ? (bc ? "&gt;&gt;" : "&lt;&lt;") + methodName : "&nbsp;";
         String configurationTestMethod = isTestConfiguration ? (bt ? "&gt;&gt;" : "&lt;&lt;") + methodName : "&nbsp;";
         String configurationGroupsMethod = isGroupsConfiguration ? (bg ? "&gt;&gt;" : "&lt;&lt;") + methodName : "&nbsp;";
         String configurationSuiteMethod = isSuiteConfiguration ? (bs ? "&gt;&gt;" : "&lt;&lt;") + methodName : "&nbsp;";
         String setUpOrTearDownMethod = isSetupOrTearDown ? (setUp ? "&gt;&gt;" : "&lt;&lt;") + methodName : "&nbsp;";
         String testMethod = tm.isTest() ? methodName : "&nbsp;";
         StringBuffer instances = new StringBuffer();
         long[] arr$ = tm.getInstanceHashCodes();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            long o = arr$[i$];
            instances.append(o).append(" ");
         }

         if (startDate == -1L) {
            startDate = iim.getDate();
         }

         String date = format.format(iim.getDate());
         table.append("<tr bgcolor=\"" + this.createColor(tm) + "\">").append("  <td>").append(date).append("</td> ").append("  <td>").append(iim.getDate() - startDate).append("</td> ").append(this.td(configurationSuiteMethod)).append(this.td(configurationTestMethod)).append(this.td(configurationClassMethod)).append(this.td(configurationGroupsMethod)).append(this.td(setUpOrTearDownMethod)).append(this.td(testMethod)).append("  <td>").append(tm.getId()).append("</td> ").append("  <td>").append(instances).append("</td> ").append("</tr>\n");
         Utils.appendToFile(this.getOutputDirectory(xmlSuite), outputFileName, table.toString());
      }

      Utils.appendToFile(this.getOutputDirectory(xmlSuite), outputFileName, "</table>\n");
   }

   private String createColor(ITestNGMethod tm) {
      long color = tm.getRealClass() != null ? (long)(tm.getRealClass().hashCode() & 16777215) : 16777215L;
      long[] rgb = new long[]{(color & 16711680L) >> 16 & 255L, (color & 65280L) >> 8 & 255L, color & 255L};

      for(int i = 0; i < rgb.length; ++i) {
         if (rgb[i] < 96L) {
            rgb[i] += 96L;
         }
      }

      long adjustedColor = rgb[0] << 16 | rgb[1] << 8 | rgb[2];
      String result = Long.toHexString(adjustedColor);
      return result;
   }

   private String td(String s) {
      StringBuffer result = new StringBuffer();
      String prefix = "";
      if (s.startsWith("&gt;&gt;")) {
         prefix = "&gt;&gt;";
      } else if (s.startsWith("&lt;&lt;")) {
         prefix = "&lt;&lt;";
      }

      if (!s.equals("&nbsp;")) {
         result.append("<td title=\"").append(s).append("\">");
         int open = s.lastIndexOf("(");
         int start = s.substring(0, open).lastIndexOf(".");
         if (start >= 0) {
            result.append(prefix + s.substring(start + 1, open));
         } else {
            result.append(prefix + s);
         }

         result.append("</td> \n");
      } else {
         result.append("<td>").append("&nbsp;").append("</td>");
      }

      return result.toString();
   }

   private void ppp(String s) {
      System.out.println("[SuiteHTMLReporter] " + s);
   }

   private void generateMethodsAndGroups(XmlSuite xmlSuite, ISuite suite) {
      StringBuffer sb = new StringBuffer();
      Map<String, Collection<ITestNGMethod>> groups = suite.getMethodsByGroups();
      sb.append("<h2>Groups used for this test run</h2>");
      if (groups.size() > 0) {
         sb.append("<table border=\"1\">\n").append("<tr> <td align=\"center\"><b>Group name</b></td>").append("<td align=\"center\"><b>Methods</b></td></tr>");
         String[] groupNames = (String[])groups.keySet().toArray(new String[groups.size()]);
         Arrays.sort(groupNames);
         String[] arr$ = groupNames;
         int len$ = groupNames.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String group = arr$[i$];
            Collection<ITestNGMethod> methods = (Collection)groups.get(group);
            sb.append("<tr><td>").append(group).append("</td>");
            StringBuffer methodNames = new StringBuffer();
            Map<ITestNGMethod, ITestNGMethod> uniqueMethods = Maps.newHashMap();
            Iterator i$ = methods.iterator();

            ITestNGMethod tm;
            while(i$.hasNext()) {
               tm = (ITestNGMethod)i$.next();
               uniqueMethods.put(tm, tm);
            }

            i$ = uniqueMethods.values().iterator();

            while(i$.hasNext()) {
               tm = (ITestNGMethod)i$.next();
               methodNames.append(tm.toString()).append("<br/>");
            }

            sb.append("<td>" + methodNames.toString() + "</td></tr>\n");
         }

         sb.append("</table>\n");
      }

      Utils.writeFile(this.getOutputDirectory(xmlSuite), "groups.html", sb.toString());
   }

   private void generateIndex(XmlSuite xmlSuite, ISuite sr) {
      StringBuffer index = (new StringBuffer()).append("<html><head><title>Results for " + sr.getName() + "</title></head>\n").append("<frameset cols=\"26%,74%\">\n").append("<frame src=\"toc.html\" name=\"navFrame\">\n").append("<frame src=\"main.html\" name=\"mainFrame\">\n").append("</frameset>\n").append("</html>\n");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "index.html", index.toString());
   }

   private String makeTitle(ISuite suite) {
      return "Results for<br/><em>" + suite.getName() + "</em>";
   }

   private void generateMain(XmlSuite xmlSuite, ISuite sr) {
      StringBuffer index = (new StringBuffer()).append("<html><head><title>Results for " + sr.getName() + "</title></head>\n").append("<body>Select a result on the left-hand pane.</body>").append("</html>\n");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "main.html", index.toString());
   }

   private void generateTableOfContents(XmlSuite xmlSuite, ISuite suite) {
      StringBuffer tableOfContents = new StringBuffer();
      Map<String, ISuiteResult> suiteResults = suite.getResults();
      int groupCount = suite.getMethodsByGroups().size();
      int methodCount = 0;
      Iterator i$ = suiteResults.values().iterator();

      while(i$.hasNext()) {
         ISuiteResult sr = (ISuiteResult)i$.next();
         ITestNGMethod[] methods = sr.getTestContext().getAllTestMethods();
         methodCount += Utils.calculateInvokedMethodCount(methods);
         ITestNGMethod[] arr$ = methods;
         int len$ = methods.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ITestNGMethod tm = arr$[i$];
            ITestClass tc = tm.getTestClass();
            this.m_classes.put(tc.getRealClass().getName(), tc);
         }
      }

      String name = "Results for " + suite.getName();
      tableOfContents.append("<html>\n").append("<head>\n").append("<title>" + name + "</title>\n").append(HtmlHelper.getCssString()).append("</head>\n");
      tableOfContents.append("<body>\n").append("<h3><p align=\"center\">" + this.makeTitle(suite) + "</p></h3>\n").append("<table border='1' width='100%'>\n").append("<tr valign='top'>\n").append("<td>").append(suiteResults.size()).append(" ").append(this.pluralize(suiteResults.size(), "test")).append("</td>\n").append("<td>").append("<a target='mainFrame' href='").append("classes.html").append("'>").append(this.m_classes.size() + " " + this.pluralize(this.m_classes.size(), "class")).append("</a>").append("</td>\n").append("<td>" + methodCount + " " + this.pluralize(methodCount, "method") + ":<br/>\n").append("&nbsp;&nbsp;<a target='mainFrame' href='").append("methods.html").append("'>").append("chronological</a><br/>\n").append("&nbsp;&nbsp;<a target='mainFrame' href='").append("methods-alphabetical.html").append("'>").append("alphabetical</a><br/>\n").append("&nbsp;&nbsp;<a target='mainFrame' href='").append("methods-not-run.html").append("'>not run (" + suite.getExcludedMethods().size() + ")</a>").append("</td>\n").append("</tr>\n").append("<tr>\n").append("<td><a target='mainFrame' href='").append("groups.html").append("'>").append(groupCount + this.pluralize(groupCount, " group") + "</a></td>\n").append("<td><a target='mainFrame' href='").append("reporter-output.html").append("'>reporter output</a></td>\n").append("<td><a target='mainFrame' href='").append("testng.xml.html").append("'>testng.xml</a></td>\n").append("</tr>").append("</table>");
      Map<String, ISuiteResult> redResults = Maps.newHashMap();
      Map<String, ISuiteResult> yellowResults = Maps.newHashMap();
      Map<String, ISuiteResult> greenResults = Maps.newHashMap();
      Iterator i$ = suiteResults.entrySet().iterator();

      int len$;
      int i$;
      while(i$.hasNext()) {
         Entry<String, ISuiteResult> entry = (Entry)i$.next();
         String suiteName = (String)entry.getKey();
         ISuiteResult sr = (ISuiteResult)entry.getValue();
         ITestContext tc = sr.getTestContext();
         len$ = tc.getFailedTests().size();
         i$ = tc.getSkippedTests().size();
         int passed = tc.getPassedTests().size();
         if (len$ > 0) {
            redResults.put(suiteName, sr);
         } else if (i$ > 0) {
            yellowResults.put(suiteName, sr);
         } else if (passed > 0) {
            greenResults.put(suiteName, sr);
         } else {
            redResults.put(suiteName, sr);
         }
      }

      ISuiteResult[][] results = new ISuiteResult[][]{this.sortResults(redResults.values()), this.sortResults(yellowResults.values()), this.sortResults(greenResults.values())};
      String[] colors = new String[]{"failed", "skipped", "passed"};

      for(int i = 0; i < colors.length; ++i) {
         ISuiteResult[] r = results[i];
         ISuiteResult[] arr$ = r;
         len$ = r.length;

         for(i$ = 0; i$ < len$; ++i$) {
            ISuiteResult sr = arr$[i$];
            String suiteName = sr.getTestContext().getName();
            this.generateSuiteResult(suiteName, sr, colors[i], tableOfContents, this.m_outputDirectory);
         }
      }

      tableOfContents.append("</body></html>");
      Utils.writeFile(this.getOutputDirectory(xmlSuite), "toc.html", tableOfContents.toString());
   }

   private String pluralize(int count, String singular) {
      return count > 1 ? (singular.endsWith("s") ? singular + "es" : singular + "s") : singular;
   }

   private String getOutputDirectory(XmlSuite xmlSuite) {
      return this.m_outputDirectory + File.separatorChar + xmlSuite.getName();
   }

   private ISuiteResult[] sortResults(Collection<ISuiteResult> r) {
      ISuiteResult[] result = (ISuiteResult[])r.toArray(new ISuiteResult[r.size()]);
      Arrays.sort(result);
      return result;
   }

   private void generateSuiteResult(String suiteName, ISuiteResult sr, String cssClass, StringBuffer tableOfContents, String outputDirectory) {
      ITestContext tc = sr.getTestContext();
      int passed = tc.getPassedTests().size();
      int failed = tc.getFailedTests().size();
      int skipped = tc.getSkippedTests().size();
      String baseFile = tc.getName();
      tableOfContents.append("\n<table width='100%' class='test-").append(cssClass).append("'>\n").append("<tr><td>\n").append("<table style='width: 100%'><tr>").append("<td valign='top'>").append(suiteName).append(" (").append(passed).append("/").append(failed).append("/").append(skipped).append(")").append("</td>").append("<td valign='top' align='right'>\n").append("  <a href='" + baseFile + ".html' target='mainFrame'>Results</a>\n").append("</td>").append("</tr></table>\n").append("</td></tr><p/>\n");
      tableOfContents.append("</table>\n");
   }

   private void generateSuites(XmlSuite xmlSuite, ISuite suite) {
      Map<String, ISuiteResult> suiteResults = suite.getResults();
      Iterator i$ = suiteResults.values().iterator();

      while(i$.hasNext()) {
         ISuiteResult sr = (ISuiteResult)i$.next();
         ITestContext testContext = sr.getTestContext();
         StringBuffer sb = new StringBuffer();
         Iterator i$ = suiteResults.values().iterator();

         while(i$.hasNext()) {
            ISuiteResult suiteResult = (ISuiteResult)i$.next();
            sb.append(suiteResult.toString());
         }

         Utils.writeFile(this.getOutputDirectory(xmlSuite), testContext.getName() + ".properties", sb.toString());
      }

   }
}
