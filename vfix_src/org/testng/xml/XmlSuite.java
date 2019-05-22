package org.testng.xml;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import org.testng.ITestObjectFactory;
import org.testng.TestNG;
import org.testng.collections.CollectionUtils;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.internal.Utils;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.OnElement;
import org.testng.xml.dom.OnElementList;
import org.testng.xml.dom.Tag;

public class XmlSuite implements Serializable, Cloneable {
   public static final String PARALLEL_TESTS = "tests";
   public static final String PARALLEL_METHODS = "methods";
   public static final String PARALLEL_CLASSES = "classes";
   public static final String PARALLEL_INSTANCES = "instances";
   public static final String PARALLEL_NONE = "none";
   public static Set<String> PARALLEL_MODES = new HashSet<String>() {
      {
         this.add("tests");
         this.add("methods");
         this.add("classes");
         this.add("instances");
         this.add("none");
         this.add("true");
         this.add("false");
      }
   };
   public static final String SKIP = "skip";
   public static final String CONTINUE = "continue";
   private String m_test;
   private static final String DEFAULT_SUITE_NAME = "Default Suite";
   private String m_name = "Default Suite";
   public static Integer DEFAULT_VERBOSE = 1;
   private Integer m_verbose = null;
   public static String DEFAULT_PARALLEL = "false";
   private String m_parallel;
   private String m_parentModule;
   private String m_guiceStage;
   public static String DEFAULT_CONFIG_FAILURE_POLICY = "skip";
   private String m_configFailurePolicy;
   public static Boolean DEFAULT_JUNIT;
   private Boolean m_isJUnit;
   public static Boolean DEFAULT_MIXED;
   private Boolean m_isMixed;
   public static Boolean DEFAULT_SKIP_FAILED_INVOCATION_COUNTS;
   private Boolean m_skipFailedInvocationCounts;
   public static Integer DEFAULT_THREAD_COUNT;
   private int m_threadCount;
   public static final Integer DEFAULT_DATA_PROVIDER_THREAD_COUNT;
   private int m_dataProviderThreadCount;
   public static final Boolean DEFAULT_GROUP_BY_INSTANCES;
   private Boolean m_groupByInstances;
   public static Boolean DEFAULT_ALLOW_RETURN_VALUES;
   private Boolean m_allowReturnValues;
   private List<XmlPackage> m_xmlPackages;
   private String m_expression;
   private List<XmlMethodSelector> m_methodSelectors;
   private List<XmlTest> m_tests;
   private Map<String, String> m_parameters;
   private String m_fileName;
   private String m_timeOut;
   private List<XmlSuite> m_childSuites;
   private XmlSuite m_parentSuite;
   private List<String> m_suiteFiles;
   private ITestObjectFactory m_objectFactory;
   private List<String> m_listeners;
   private static final long serialVersionUID = 4999962288272750226L;
   public static String DEFAULT_PRESERVE_ORDER;
   private String m_preserveOrder;
   private List<String> m_includedGroups;
   private List<String> m_excludedGroups;
   private XmlMethodSelectors m_xmlMethodSelectors;
   private XmlGroups m_xmlGroups;

   public XmlSuite() {
      this.m_parallel = DEFAULT_PARALLEL;
      this.m_parentModule = "";
      this.m_guiceStage = "";
      this.m_configFailurePolicy = DEFAULT_CONFIG_FAILURE_POLICY;
      this.m_isJUnit = DEFAULT_JUNIT;
      this.m_isMixed = DEFAULT_MIXED;
      this.m_skipFailedInvocationCounts = DEFAULT_SKIP_FAILED_INVOCATION_COUNTS;
      this.m_threadCount = DEFAULT_THREAD_COUNT;
      this.m_dataProviderThreadCount = DEFAULT_DATA_PROVIDER_THREAD_COUNT;
      this.m_groupByInstances = DEFAULT_GROUP_BY_INSTANCES;
      this.m_allowReturnValues = DEFAULT_ALLOW_RETURN_VALUES;
      this.m_xmlPackages = Lists.newArrayList();
      this.m_expression = null;
      this.m_methodSelectors = Lists.newArrayList();
      this.m_tests = Lists.newArrayList();
      this.m_parameters = Maps.newHashMap();
      this.m_childSuites = Lists.newArrayList();
      this.m_suiteFiles = Lists.newArrayList();
      this.m_listeners = Lists.newArrayList();
      this.m_preserveOrder = DEFAULT_PRESERVE_ORDER;
      this.m_includedGroups = Lists.newArrayList();
      this.m_excludedGroups = Lists.newArrayList();
   }

   public String getFileName() {
      return this.m_fileName;
   }

   public void setFileName(String fileName) {
      this.m_fileName = fileName;
   }

   public String getParallel() {
      return this.m_parallel;
   }

   public String getParentModule() {
      return this.m_parentModule;
   }

   public String getGuiceStage() {
      return this.m_guiceStage;
   }

   public ITestObjectFactory getObjectFactory() {
      return this.m_objectFactory;
   }

   public void setObjectFactory(ITestObjectFactory objectFactory) {
      this.m_objectFactory = objectFactory;
   }

   public void setParallel(String parallel) {
      this.m_parallel = parallel;
   }

   public void setParentModule(String parentModule) {
      this.m_parentModule = parentModule;
   }

   public void setGuiceStage(String guiceStage) {
      this.m_guiceStage = guiceStage;
   }

   public void setConfigFailurePolicy(String configFailurePolicy) {
      this.m_configFailurePolicy = configFailurePolicy;
   }

   public String getConfigFailurePolicy() {
      return this.m_configFailurePolicy;
   }

   public Integer getVerbose() {
      return this.m_verbose != null ? this.m_verbose : TestNG.DEFAULT_VERBOSE;
   }

   public void setVerbose(Integer verbose) {
      this.m_verbose = verbose;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String name) {
      this.m_name = name;
   }

   public String getTest() {
      return this.m_test;
   }

   public List<XmlTest> getTests() {
      return this.m_tests;
   }

   public void setTests(List<XmlTest> tests) {
      this.m_tests = tests;
   }

   public List<XmlMethodSelector> getMethodSelectors() {
      return this.m_xmlMethodSelectors != null ? this.m_xmlMethodSelectors.getMethodSelectors() : this.m_methodSelectors;
   }

   public void setMethodSelectors(List<XmlMethodSelector> methodSelectors) {
      this.m_methodSelectors = Lists.newArrayList((Collection)methodSelectors);
   }

   private void updateParameters() {
      if (this.m_parentSuite != null) {
         Set<String> keySet = this.m_parentSuite.getParameters().keySet();
         Iterator i$ = keySet.iterator();

         while(i$.hasNext()) {
            String name = (String)i$.next();
            if (!this.m_parameters.containsKey(name)) {
               this.m_parameters.put(name, this.m_parentSuite.getParameter(name));
            }
         }
      }

   }

   public void setParameters(Map<String, String> parameters) {
      this.m_parameters = parameters;
      this.updateParameters();
   }

   public Map<String, String> getParameters() {
      return this.m_parameters;
   }

   public Map<String, String> getAllParameters() {
      Map<String, String> result = Maps.newHashMap();
      Iterator i$ = this.m_parameters.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, String> entry = (Entry)i$.next();
         result.put(entry.getKey(), entry.getValue());
      }

      i$ = this.getTests().iterator();

      while(i$.hasNext()) {
         XmlTest test = (XmlTest)i$.next();
         Map<String, String> tp = test.getLocalParameters();
         Iterator i$ = tp.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, String> entry = (Entry)i$.next();
            result.put(entry.getKey(), entry.getValue());
         }
      }

      return result;
   }

   public String getParameter(String name) {
      return (String)this.m_parameters.get(name);
   }

   public int getThreadCount() {
      return this.m_threadCount;
   }

   public void setThreadCount(int threadCount) {
      this.m_threadCount = threadCount;
   }

   public Boolean isJUnit() {
      return this.m_isJUnit;
   }

   public void setJUnit(Boolean isJUnit) {
      this.m_isJUnit = isJUnit;
   }

   public void setJunit(Boolean j) {
      this.setJUnit(j);
   }

   public Boolean skipFailedInvocationCounts() {
      return this.m_skipFailedInvocationCounts;
   }

   public void setSkipFailedInvocationCounts(boolean skip) {
      this.m_skipFailedInvocationCounts = skip;
   }

   public void setXmlPackages(List<XmlPackage> packages) {
      this.m_xmlPackages = Lists.newArrayList((Collection)packages);
   }

   public List<XmlPackage> getXmlPackages() {
      return this.m_xmlPackages;
   }

   public List<XmlPackage> getPackages() {
      return this.getXmlPackages();
   }

   @Tag(
      name = "method-selectors"
   )
   public void setMethodSelectors(XmlMethodSelectors xms) {
      this.m_xmlMethodSelectors = xms;
   }

   public void setPackages(List<XmlPackage> packages) {
      this.setXmlPackages(packages);
   }

   public String toXml() {
      XMLStringBuffer xsb = new XMLStringBuffer();
      xsb.setDocType("suite SYSTEM \"http://testng.org/testng-1.0.dtd\"");
      Properties p = new Properties();
      p.setProperty("name", this.getName());
      if (this.getVerbose() != null) {
         XmlUtils.setProperty(p, "verbose", this.getVerbose().toString(), DEFAULT_VERBOSE.toString());
      }

      String parallel = this.getParallel();
      if (Utils.isStringNotEmpty(parallel) && !DEFAULT_PARALLEL.equals(parallel)) {
         p.setProperty("parallel", parallel);
      }

      XmlUtils.setProperty(p, "group-by-instances", String.valueOf(this.getGroupByInstances()), DEFAULT_GROUP_BY_INSTANCES.toString());
      XmlUtils.setProperty(p, "configfailurepolicy", this.getConfigFailurePolicy(), DEFAULT_CONFIG_FAILURE_POLICY);
      XmlUtils.setProperty(p, "thread-count", String.valueOf(this.getThreadCount()), DEFAULT_THREAD_COUNT.toString());
      XmlUtils.setProperty(p, "data-provider-thread-count", String.valueOf(this.getDataProviderThreadCount()), DEFAULT_DATA_PROVIDER_THREAD_COUNT.toString());
      if (!DEFAULT_JUNIT.equals(this.m_isJUnit)) {
         p.setProperty("junit", this.m_isJUnit != null ? this.m_isJUnit.toString() : "false");
      }

      XmlUtils.setProperty(p, "skipfailedinvocationcounts", this.m_skipFailedInvocationCounts.toString(), DEFAULT_SKIP_FAILED_INVOCATION_COUNTS.toString());
      if (null != this.m_objectFactory) {
         p.setProperty("object-factory", this.m_objectFactory.getClass().getName());
      }

      if (Utils.isStringNotEmpty(this.m_parentModule)) {
         p.setProperty("parent-module", this.getParentModule());
      }

      if (Utils.isStringNotEmpty(this.m_guiceStage)) {
         p.setProperty("guice-stage", this.getGuiceStage());
      }

      XmlUtils.setProperty(p, "allow-return-values", String.valueOf(this.getAllowReturnValues()), DEFAULT_ALLOW_RETURN_VALUES.toString());
      xsb.push("suite", p);
      XmlUtils.dumpParameters(xsb, this.m_parameters);
      Iterator i$;
      if (CollectionUtils.hasElements((Collection)this.m_listeners)) {
         xsb.push("listeners");
         i$ = this.m_listeners.iterator();

         while(i$.hasNext()) {
            String listenerName = (String)i$.next();
            Properties listenerProps = new Properties();
            listenerProps.setProperty("class-name", listenerName);
            xsb.addEmptyElement("listener", listenerProps);
         }

         xsb.pop("listeners");
      }

      if (CollectionUtils.hasElements((Collection)this.getXmlPackages())) {
         xsb.push("packages");
         i$ = this.getXmlPackages().iterator();

         while(i$.hasNext()) {
            XmlPackage pack = (XmlPackage)i$.next();
            xsb.getStringBuffer().append(pack.toXml("    "));
         }

         xsb.pop("packages");
      }

      if (this.getXmlMethodSelectors() != null) {
         xsb.getStringBuffer().append(this.getXmlMethodSelectors().toXml("  "));
      } else if (CollectionUtils.hasElements((Collection)this.getMethodSelectors())) {
         xsb.push("method-selectors");
         i$ = this.getMethodSelectors().iterator();

         while(i$.hasNext()) {
            XmlMethodSelector selector = (XmlMethodSelector)i$.next();
            xsb.getStringBuffer().append(selector.toXml("  "));
         }

         xsb.pop("method-selectors");
      }

      List<String> suiteFiles = this.getSuiteFiles();
      if (suiteFiles.size() > 0) {
         xsb.push("suite-files");
         Iterator i$ = suiteFiles.iterator();

         while(i$.hasNext()) {
            String sf = (String)i$.next();
            Properties prop = new Properties();
            prop.setProperty("path", sf);
            xsb.addEmptyElement("suite-file", prop);
         }

         xsb.pop("suite-files");
      }

      List<String> included = this.getIncludedGroups();
      List<String> excluded = this.getExcludedGroups();
      Iterator i$;
      if (CollectionUtils.hasElements((Collection)included) || CollectionUtils.hasElements((Collection)excluded)) {
         xsb.push("groups");
         xsb.push("run");
         i$ = included.iterator();

         String g;
         while(i$.hasNext()) {
            g = (String)i$.next();
            xsb.addEmptyElement("include", "name", g);
         }

         i$ = excluded.iterator();

         while(i$.hasNext()) {
            g = (String)i$.next();
            xsb.addEmptyElement("exclude", "name", g);
         }

         xsb.pop("run");
         xsb.pop("groups");
      }

      if (this.m_xmlGroups != null) {
         xsb.getStringBuffer().append(this.m_xmlGroups.toXml("  "));
      }

      i$ = this.getTests().iterator();

      while(i$.hasNext()) {
         XmlTest test = (XmlTest)i$.next();
         xsb.getStringBuffer().append(test.toXml("  "));
      }

      xsb.pop("suite");
      return xsb.toXML();
   }

   @Tag(
      name = "method-selectors"
   )
   public void setXmlMethodSelectors(XmlMethodSelectors xms) {
      this.m_xmlMethodSelectors = xms;
   }

   private XmlMethodSelectors getXmlMethodSelectors() {
      return this.m_xmlMethodSelectors;
   }

   public String toString() {
      StringBuffer result = (new StringBuffer("[Suite: \"")).append(this.m_name).append("\" ");
      Iterator i$ = this.m_tests.iterator();

      while(i$.hasNext()) {
         XmlTest t = (XmlTest)i$.next();
         result.append("  ").append(t.toString()).append(' ');
      }

      i$ = this.m_methodSelectors.iterator();

      while(i$.hasNext()) {
         XmlMethodSelector ms = (XmlMethodSelector)i$.next();
         result.append(" methodSelector:" + ms);
      }

      result.append(']');
      return result.toString();
   }

   private static void ppp(String s) {
      System.out.println("[XmlSuite] " + s);
   }

   public Object clone() {
      XmlSuite result = new XmlSuite();
      result.setName(this.getName());
      result.setListeners(this.getListeners());
      result.setParallel(this.getParallel());
      result.setParentModule(this.getParentModule());
      result.setGuiceStage(this.getGuiceStage());
      result.setConfigFailurePolicy(this.getConfigFailurePolicy());
      result.setThreadCount(this.getThreadCount());
      result.setDataProviderThreadCount(this.getDataProviderThreadCount());
      result.setParameters(this.getAllParameters());
      result.setVerbose(this.getVerbose());
      result.setXmlPackages(this.getXmlPackages());
      result.setMethodSelectors(this.getMethodSelectors());
      result.setJUnit(this.isJUnit());
      result.setSkipFailedInvocationCounts(this.skipFailedInvocationCounts());
      result.setObjectFactory(this.getObjectFactory());
      result.setAllowReturnValues(this.getAllowReturnValues());
      result.setTimeOut(this.getTimeOut());
      return result;
   }

   public void setTimeOut(String timeOut) {
      this.m_timeOut = timeOut;
   }

   public String getTimeOut() {
      return this.m_timeOut;
   }

   public long getTimeOut(long def) {
      long result = def;
      if (this.m_timeOut != null) {
         result = new Long(this.m_timeOut);
      }

      return result;
   }

   public void setSuiteFiles(List<String> files) {
      this.m_suiteFiles = files;
   }

   public List<String> getSuiteFiles() {
      return this.m_suiteFiles;
   }

   public void setListeners(List<String> listeners) {
      this.m_listeners = listeners;
   }

   public List<String> getListeners() {
      if (this.m_parentSuite != null) {
         List<String> listeners = this.m_parentSuite.getListeners();
         Iterator i$ = listeners.iterator();

         while(i$.hasNext()) {
            String listener = (String)i$.next();
            if (!this.m_listeners.contains(listener)) {
               this.m_listeners.add(listener);
            }
         }
      }

      return this.m_listeners;
   }

   public void setDataProviderThreadCount(int count) {
      this.m_dataProviderThreadCount = count;
   }

   public int getDataProviderThreadCount() {
      String s = System.getProperty("dataproviderthreadcount");
      if (s != null) {
         try {
            int nthreads = Integer.parseInt(s);
            return nthreads;
         } catch (NumberFormatException var3) {
            System.err.println("Parsing System property 'dataproviderthreadcount': " + var3);
         }
      }

      return this.m_dataProviderThreadCount;
   }

   public void setParentSuite(XmlSuite parentSuite) {
      this.m_parentSuite = parentSuite;
      this.updateParameters();
   }

   public XmlSuite getParentSuite() {
      return this.m_parentSuite;
   }

   public List<XmlSuite> getChildSuites() {
      return this.m_childSuites;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.m_configFailurePolicy == null ? 0 : this.m_configFailurePolicy.hashCode());
      result = 31 * result + this.m_dataProviderThreadCount;
      result = 31 * result + (this.m_expression == null ? 0 : this.m_expression.hashCode());
      result = 31 * result + (this.m_fileName == null ? 0 : this.m_fileName.hashCode());
      result = 31 * result + (this.m_isJUnit == null ? 0 : this.m_isJUnit.hashCode());
      result = 31 * result + (this.m_listeners == null ? 0 : this.m_listeners.hashCode());
      result = 31 * result + (this.m_methodSelectors == null ? 0 : this.m_methodSelectors.hashCode());
      result = 31 * result + (this.m_name == null ? 0 : this.m_name.hashCode());
      result = 31 * result + (this.m_objectFactory == null ? 0 : this.m_objectFactory.hashCode());
      result = 31 * result + (this.m_parallel == null ? 0 : this.m_parallel.hashCode());
      result = 31 * result + (this.m_skipFailedInvocationCounts == null ? 0 : this.m_skipFailedInvocationCounts.hashCode());
      result = 31 * result + (this.m_suiteFiles == null ? 0 : this.m_suiteFiles.hashCode());
      result = 31 * result + (this.m_test == null ? 0 : this.m_test.hashCode());
      result = 31 * result + (this.m_tests == null ? 0 : this.m_tests.hashCode());
      result = 31 * result + this.m_threadCount;
      result = 31 * result + (this.m_timeOut == null ? 0 : this.m_timeOut.hashCode());
      result = 31 * result + (this.m_verbose == null ? 0 : this.m_verbose.hashCode());
      result = 31 * result + (this.m_xmlPackages == null ? 0 : this.m_xmlPackages.hashCode());
      return result;
   }

   static boolean f() {
      return false;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return f();
      } else if (this.getClass() != obj.getClass()) {
         return f();
      } else {
         XmlSuite other = (XmlSuite)obj;
         if (this.m_configFailurePolicy == null) {
            if (other.m_configFailurePolicy != null) {
               return f();
            }
         } else if (!this.m_configFailurePolicy.equals(other.m_configFailurePolicy)) {
            return f();
         }

         if (this.m_dataProviderThreadCount != other.m_dataProviderThreadCount) {
            return f();
         } else {
            if (this.m_expression == null) {
               if (other.m_expression != null) {
                  return f();
               }
            } else if (!this.m_expression.equals(other.m_expression)) {
               return f();
            }

            if (this.m_isJUnit == null) {
               if (other.m_isJUnit != null) {
                  return f();
               }
            } else if (!this.m_isJUnit.equals(other.m_isJUnit)) {
               return f();
            }

            if (this.m_listeners == null) {
               if (other.m_listeners != null) {
                  return f();
               }
            } else if (!this.m_listeners.equals(other.m_listeners)) {
               return f();
            }

            if (this.m_methodSelectors == null) {
               if (other.m_methodSelectors != null) {
                  return f();
               }
            } else if (!this.m_methodSelectors.equals(other.m_methodSelectors)) {
               return f();
            }

            if (this.m_name == null) {
               if (other.m_name != null) {
                  return f();
               }
            } else if (!this.m_name.equals(other.m_name)) {
               return f();
            }

            if (this.m_objectFactory == null) {
               if (other.m_objectFactory != null) {
                  return f();
               }
            } else if (!this.m_objectFactory.equals(other.m_objectFactory)) {
               return f();
            }

            if (this.m_parallel == null) {
               if (other.m_parallel != null) {
                  return f();
               }
            } else if (!this.m_parallel.equals(other.m_parallel)) {
               return f();
            }

            if (this.m_skipFailedInvocationCounts == null) {
               if (other.m_skipFailedInvocationCounts != null) {
                  return f();
               }
            } else if (!this.m_skipFailedInvocationCounts.equals(other.m_skipFailedInvocationCounts)) {
               return f();
            }

            if (this.m_suiteFiles == null) {
               if (other.m_suiteFiles != null) {
                  return f();
               }
            } else if (!this.m_suiteFiles.equals(other.m_suiteFiles)) {
               return f();
            }

            if (this.m_test == null) {
               if (other.m_test != null) {
                  return f();
               }
            } else if (!this.m_test.equals(other.m_test)) {
               return f();
            }

            if (this.m_tests == null) {
               if (other.m_tests != null) {
                  return f();
               }
            } else if (!this.m_tests.equals(other.m_tests)) {
               return f();
            }

            if (this.m_threadCount != other.m_threadCount) {
               return f();
            } else {
               if (this.m_timeOut == null) {
                  if (other.m_timeOut != null) {
                     return f();
                  }
               } else if (!this.m_timeOut.equals(other.m_timeOut)) {
                  return f();
               }

               if (this.m_verbose == null) {
                  if (other.m_verbose != null) {
                     return f();
                  }
               } else if (!this.m_verbose.equals(other.m_verbose)) {
                  return f();
               }

               if (this.m_xmlPackages == null) {
                  if (other.m_xmlPackages != null) {
                     return f();
                  }
               } else if (!this.m_xmlPackages.equals(other.m_xmlPackages)) {
                  return f();
               }

               return true;
            }
         }
      }
   }

   private boolean eq(String o1, String o2, String def) {
      boolean result = false;
      if (o1 == null && o2 == null) {
         result = true;
      } else if (o1 != null) {
         result = o1.equals(o2) || def.equals(o1) && o2 == null;
      } else if (o2 != null) {
         result = o2.equals(o1) || def.equals(o2) && o1 == null;
      }

      return result;
   }

   public void setPreserveOrder(String f) {
      this.m_preserveOrder = f;
   }

   public String getPreserveOrder() {
      return this.m_preserveOrder;
   }

   public List<String> getIncludedGroups() {
      return this.m_xmlGroups != null ? this.m_xmlGroups.getRun().getIncludes() : this.m_includedGroups;
   }

   public void addIncludedGroup(String g) {
      this.m_includedGroups.add(g);
   }

   public void setIncludedGroups(List<String> g) {
      this.m_includedGroups = g;
   }

   public void setExcludedGroups(List<String> g) {
      this.m_excludedGroups = g;
   }

   public List<String> getExcludedGroups() {
      return this.m_xmlGroups != null ? this.m_xmlGroups.getRun().getExcludes() : this.m_excludedGroups;
   }

   public void addExcludedGroup(String g) {
      this.m_excludedGroups.add(g);
   }

   public Boolean getGroupByInstances() {
      return this.m_groupByInstances;
   }

   public void setGroupByInstances(boolean f) {
      this.m_groupByInstances = f;
   }

   public void addListener(String listener) {
      this.m_listeners.add(listener);
   }

   public Boolean getAllowReturnValues() {
      return this.m_allowReturnValues;
   }

   public void setAllowReturnValues(Boolean allowReturnValues) {
      this.m_allowReturnValues = allowReturnValues;
   }

   public void setGroups(XmlGroups xmlGroups) {
      this.m_xmlGroups = xmlGroups;
   }

   @OnElement(
      tag = "parameter",
      attributes = {"name", "value"}
   )
   public void onParameterElement(String name, String value) {
      this.getParameters().put(name, value);
   }

   @OnElementList(
      tag = "listeners",
      attributes = {"class-name"}
   )
   public void onListenerElement(String className) {
      this.addListener(className);
   }

   @OnElementList(
      tag = "suite-files",
      attributes = {"path"}
   )
   public void onSuiteFilesElement(String path) {
      this.getSuiteFiles().add(path);
   }

   @OnElementList(
      tag = "packages",
      attributes = {"name"}
   )
   public void onPackagesElement(String name) {
      this.getPackages().add(new XmlPackage(name));
   }

   public void onMethodSelectorElement(String language, String name, String priority) {
      System.out.println("Language:" + language);
   }

   public XmlGroups getGroups() {
      return this.m_xmlGroups;
   }

   public void addTest(XmlTest test) {
      this.getTests().add(test);
   }

   public Collection<String> getPackageNames() {
      List<String> result = Lists.newArrayList();
      Iterator i$ = this.getPackages().iterator();

      while(i$.hasNext()) {
         XmlPackage p = (XmlPackage)i$.next();
         result.add(p.getName());
      }

      return result;
   }

   public static boolean isParallel(String parallel) {
      return PARALLEL_MODES.contains(parallel) && !"false".equals(parallel);
   }

   static {
      DEFAULT_JUNIT = Boolean.FALSE;
      DEFAULT_MIXED = Boolean.FALSE;
      DEFAULT_SKIP_FAILED_INVOCATION_COUNTS = Boolean.FALSE;
      DEFAULT_THREAD_COUNT = 5;
      DEFAULT_DATA_PROVIDER_THREAD_COUNT = 10;
      DEFAULT_GROUP_BY_INSTANCES = false;
      DEFAULT_ALLOW_RETURN_VALUES = Boolean.FALSE;
      DEFAULT_PRESERVE_ORDER = "true";
   }
}
