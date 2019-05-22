package org.testng.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.Map.Entry;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.reporters.XMLStringBuffer;
import org.testng.xml.dom.ParentSetter;

public class XmlTest implements Serializable, Cloneable {
   private static final long serialVersionUID = 6533504325942417606L;
   public static int DEFAULT_TIMEOUT_MS = Integer.MAX_VALUE;
   private XmlSuite m_suite;
   private String m_name;
   private Integer m_verbose;
   private Boolean m_isJUnit;
   private int m_threadCount;
   private List<XmlClass> m_xmlClasses;
   private List<String> m_includedGroups;
   private List<String> m_excludedGroups;
   private Map<String, List<String>> m_metaGroups;
   private Map<String, String> m_parameters;
   private String m_parallel;
   private List<XmlMethodSelector> m_methodSelectors;
   private List<XmlPackage> m_xmlPackages;
   private String m_timeOut;
   private Boolean m_skipFailedInvocationCounts;
   private Map<String, List<Integer>> m_failedInvocationNumbers;
   private String m_preserveOrder;
   private int m_index;
   private Boolean m_groupByInstances;
   private Boolean m_allowReturnValues;
   private Map<String, String> m_xmlDependencyGroups;
   private XmlGroups m_xmlGroups;

   public XmlTest(XmlSuite suite, int index) {
      this.m_verbose = XmlSuite.DEFAULT_VERBOSE;
      this.m_isJUnit = XmlSuite.DEFAULT_JUNIT;
      this.m_threadCount = -1;
      this.m_xmlClasses = Lists.newArrayList();
      this.m_includedGroups = Lists.newArrayList();
      this.m_excludedGroups = Lists.newArrayList();
      this.m_metaGroups = Maps.newHashMap();
      this.m_parameters = Maps.newHashMap();
      this.m_methodSelectors = Lists.newArrayList();
      this.m_xmlPackages = Lists.newArrayList();
      this.m_skipFailedInvocationCounts = XmlSuite.DEFAULT_SKIP_FAILED_INVOCATION_COUNTS;
      this.m_failedInvocationNumbers = null;
      this.m_preserveOrder = XmlSuite.DEFAULT_PRESERVE_ORDER;
      this.m_allowReturnValues = null;
      this.m_xmlDependencyGroups = Maps.newHashMap();
      this.init(suite, index);
   }

   public XmlTest(XmlSuite suite) {
      this.m_verbose = XmlSuite.DEFAULT_VERBOSE;
      this.m_isJUnit = XmlSuite.DEFAULT_JUNIT;
      this.m_threadCount = -1;
      this.m_xmlClasses = Lists.newArrayList();
      this.m_includedGroups = Lists.newArrayList();
      this.m_excludedGroups = Lists.newArrayList();
      this.m_metaGroups = Maps.newHashMap();
      this.m_parameters = Maps.newHashMap();
      this.m_methodSelectors = Lists.newArrayList();
      this.m_xmlPackages = Lists.newArrayList();
      this.m_skipFailedInvocationCounts = XmlSuite.DEFAULT_SKIP_FAILED_INVOCATION_COUNTS;
      this.m_failedInvocationNumbers = null;
      this.m_preserveOrder = XmlSuite.DEFAULT_PRESERVE_ORDER;
      this.m_allowReturnValues = null;
      this.m_xmlDependencyGroups = Maps.newHashMap();
      this.init(suite, 0);
   }

   private void init(XmlSuite suite, int index) {
      this.m_suite = suite;
      this.m_suite.getTests().add(this);
      this.m_index = index;
      this.m_name = "Command line test " + UUID.randomUUID().toString();
   }

   public XmlTest() {
      this.m_verbose = XmlSuite.DEFAULT_VERBOSE;
      this.m_isJUnit = XmlSuite.DEFAULT_JUNIT;
      this.m_threadCount = -1;
      this.m_xmlClasses = Lists.newArrayList();
      this.m_includedGroups = Lists.newArrayList();
      this.m_excludedGroups = Lists.newArrayList();
      this.m_metaGroups = Maps.newHashMap();
      this.m_parameters = Maps.newHashMap();
      this.m_methodSelectors = Lists.newArrayList();
      this.m_xmlPackages = Lists.newArrayList();
      this.m_skipFailedInvocationCounts = XmlSuite.DEFAULT_SKIP_FAILED_INVOCATION_COUNTS;
      this.m_failedInvocationNumbers = null;
      this.m_preserveOrder = XmlSuite.DEFAULT_PRESERVE_ORDER;
      this.m_allowReturnValues = null;
      this.m_xmlDependencyGroups = Maps.newHashMap();
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

   public void setPackages(List<XmlPackage> p) {
      this.setXmlPackages(p);
   }

   public List<XmlMethodSelector> getMethodSelectors() {
      return this.m_methodSelectors;
   }

   public void setMethodSelectors(List<XmlMethodSelector> methodSelectors) {
      this.m_methodSelectors = Lists.newArrayList((Collection)methodSelectors);
   }

   public XmlSuite getSuite() {
      return this.m_suite;
   }

   public List<String> getIncludedGroups() {
      List result;
      if (this.m_xmlGroups != null) {
         result = this.m_xmlGroups.getRun().getIncludes();
         result.addAll(this.m_suite.getIncludedGroups());
      } else {
         result = Lists.newArrayList((Collection)this.m_includedGroups);
         result.addAll(this.m_suite.getIncludedGroups());
      }

      return result;
   }

   /** @deprecated */
   @Deprecated
   public void setClassNames(List<XmlClass> classes) {
      this.m_xmlClasses = classes;
   }

   public List<XmlClass> getXmlClasses() {
      return this.m_xmlClasses;
   }

   public List<XmlClass> getClasses() {
      return this.getXmlClasses();
   }

   public void setClasses(List<XmlClass> c) {
      this.setXmlClasses(c);
   }

   public void setXmlClasses(List<XmlClass> classes) {
      this.m_xmlClasses = classes;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String name) {
      this.m_name = name;
   }

   public void setVerbose(int v) {
      this.m_verbose = v;
   }

   public int getThreadCount() {
      return this.m_threadCount > 0 ? this.m_threadCount : this.getSuite().getThreadCount();
   }

   public void setThreadCount(int threadCount) {
      this.m_threadCount = threadCount;
   }

   public void setIncludedGroups(List<String> g) {
      this.m_includedGroups = g;
   }

   public void setExcludedGroups(List<String> g) {
      this.m_excludedGroups = g;
   }

   public List<String> getExcludedGroups() {
      List<String> result = new ArrayList(this.m_excludedGroups);
      result.addAll(this.m_suite.getExcludedGroups());
      return result;
   }

   public void addIncludedGroup(String g) {
      this.m_includedGroups.add(g);
   }

   public void addExcludedGroup(String g) {
      this.m_excludedGroups.add(g);
   }

   public int getVerbose() {
      Integer result = this.m_verbose;
      if (null == result || XmlSuite.DEFAULT_VERBOSE.equals(this.m_verbose)) {
         result = this.m_suite.getVerbose();
      }

      return null != result ? result : 1;
   }

   public boolean getGroupByInstances() {
      Boolean result = this.m_groupByInstances;
      if (result == null || XmlSuite.DEFAULT_GROUP_BY_INSTANCES.equals(this.m_groupByInstances)) {
         result = this.m_suite.getGroupByInstances();
      }

      return result != null ? result : XmlSuite.DEFAULT_GROUP_BY_INSTANCES;
   }

   public void setGroupByInstances(boolean f) {
      this.m_groupByInstances = f;
   }

   public boolean isJUnit() {
      Boolean result = this.m_isJUnit;
      if (null == result || XmlSuite.DEFAULT_JUNIT.equals(result)) {
         result = this.m_suite.isJUnit();
      }

      return result;
   }

   public void setJUnit(boolean isJUnit) {
      this.m_isJUnit = isJUnit;
   }

   public void setJunit(boolean isJUnit) {
      this.setJUnit(isJUnit);
   }

   public void setSkipFailedInvocationCounts(boolean skip) {
      this.m_skipFailedInvocationCounts = skip;
   }

   public boolean skipFailedInvocationCounts() {
      Boolean result = this.m_skipFailedInvocationCounts;
      if (null == result) {
         result = this.m_suite.skipFailedInvocationCounts();
      }

      return result;
   }

   public void addMetaGroup(String name, List<String> metaGroup) {
      this.m_metaGroups.put(name, metaGroup);
   }

   public void setMetaGroups(Map<String, List<String>> metaGroups) {
      this.m_metaGroups = metaGroups;
   }

   public Map<String, List<String>> getMetaGroups() {
      if (this.m_xmlGroups == null) {
         return this.m_metaGroups;
      } else {
         Map<String, List<String>> result = Maps.newHashMap();
         List<XmlDefine> defines = this.m_xmlGroups.getDefines();
         Iterator i$ = defines.iterator();

         while(i$.hasNext()) {
            XmlDefine xd = (XmlDefine)i$.next();
            result.put(xd.getName(), xd.getIncludes());
         }

         return result;
      }
   }

   public void setParameters(Map<String, String> parameters) {
      this.m_parameters = parameters;
   }

   public void addParameter(String key, String value) {
      this.m_parameters.put(key, value);
   }

   public String getParameter(String name) {
      String result = (String)this.m_parameters.get(name);
      if (null == result) {
         result = this.m_suite.getParameter(name);
      }

      return result;
   }

   public Map<String, String> getAllParameters() {
      Map<String, String> result = Maps.newHashMap();
      Map<String, String> parameters = this.getSuite().getParameters();
      Iterator i$ = parameters.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, String> parameter = (Entry)i$.next();
         result.put(parameter.getKey(), parameter.getValue());
      }

      i$ = this.m_parameters.keySet().iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         result.put(key, this.m_parameters.get(key));
      }

      return result;
   }

   public Map<String, String> getLocalParameters() {
      return this.m_parameters;
   }

   /** @deprecated */
   @Deprecated
   public Map<String, String> getParameters() {
      return this.getAllParameters();
   }

   public Map<String, String> getTestParameters() {
      return this.m_parameters;
   }

   public void setParallel(String parallel) {
      this.m_parallel = parallel;
   }

   public String getParallel() {
      String result = null;
      if (null == this.m_parallel && !XmlSuite.DEFAULT_PARALLEL.equals(this.m_parallel)) {
         result = this.m_suite.getParallel();
      } else {
         result = this.m_parallel;
      }

      return result;
   }

   public String getTimeOut() {
      String result = null;
      if (null != this.m_timeOut) {
         result = this.m_timeOut;
      } else {
         result = this.m_suite.getTimeOut();
      }

      return result;
   }

   public long getTimeOut(long def) {
      long result = def;
      if (this.getTimeOut() != null) {
         result = new Long(this.getTimeOut());
      }

      return result;
   }

   public void setTimeOut(long timeOut) {
      this.m_timeOut = Long.toString(timeOut);
   }

   private void setTimeOut(String timeOut) {
      this.m_timeOut = timeOut;
   }

   public void setExpression(String expression) {
      this.setBeanShellExpression(expression);
   }

   public void setBeanShellExpression(String expression) {
      List<XmlMethodSelector> selectors = this.getMethodSelectors();
      if (selectors.size() > 0) {
         ((XmlMethodSelector)selectors.get(0)).setExpression(expression);
      } else if (expression != null) {
         XmlMethodSelector xms = new XmlMethodSelector();
         xms.setExpression(expression);
         xms.setLanguage("BeanShell");
         this.getMethodSelectors().add(xms);
      }

   }

   public String getExpression() {
      List<XmlMethodSelector> selectors = this.getMethodSelectors();
      return selectors.size() > 0 ? ((XmlMethodSelector)selectors.get(0)).getExpression() : null;
   }

   public String toXml(String indent) {
      XMLStringBuffer xsb = new XMLStringBuffer(indent);
      Properties p = new Properties();
      p.setProperty("name", this.getName());
      if (this.m_isJUnit != null) {
         XmlUtils.setProperty(p, "junit", this.m_isJUnit.toString(), XmlSuite.DEFAULT_JUNIT.toString());
      }

      if (this.m_parallel != null) {
         XmlUtils.setProperty(p, "parallel", this.m_parallel, XmlSuite.DEFAULT_PARALLEL);
      }

      if (this.m_verbose != null) {
         XmlUtils.setProperty(p, "verbose", this.m_verbose.toString(), XmlSuite.DEFAULT_VERBOSE.toString());
      }

      if (null != this.m_timeOut) {
         p.setProperty("time-out", this.m_timeOut.toString());
      }

      if (this.m_preserveOrder != null && !XmlSuite.DEFAULT_PRESERVE_ORDER.equals(this.m_preserveOrder)) {
         p.setProperty("preserve-order", this.m_preserveOrder.toString());
      }

      if (this.m_threadCount != -1) {
         p.setProperty("thread-count", Integer.toString(this.m_threadCount));
      }

      if (this.m_groupByInstances != null) {
         XmlUtils.setProperty(p, "group-by-instances", String.valueOf(this.getGroupByInstances()), XmlSuite.DEFAULT_GROUP_BY_INSTANCES.toString());
      }

      xsb.push("test", p);
      Iterator i$;
      if (null != this.getMethodSelectors() && !this.getMethodSelectors().isEmpty()) {
         xsb.push("method-selectors");
         i$ = this.getMethodSelectors().iterator();

         while(i$.hasNext()) {
            XmlMethodSelector selector = (XmlMethodSelector)i$.next();
            xsb.getStringBuffer().append(selector.toXml(indent + "    "));
         }

         xsb.pop("method-selectors");
      }

      XmlUtils.dumpParameters(xsb, this.m_parameters);
      if (!this.m_metaGroups.isEmpty() || !this.m_includedGroups.isEmpty() || !this.m_excludedGroups.isEmpty() || !this.m_xmlDependencyGroups.isEmpty()) {
         xsb.push("groups");
         i$ = this.m_metaGroups.keySet().iterator();

         Properties excludeProps;
         String excludeGroupName;
         while(i$.hasNext()) {
            excludeGroupName = (String)i$.next();
            excludeProps = new Properties();
            excludeProps.setProperty("name", excludeGroupName);
            xsb.push("define", excludeProps);
            Iterator i$ = ((List)this.m_metaGroups.get(excludeGroupName)).iterator();

            while(i$.hasNext()) {
               String groupName = (String)i$.next();
               Properties includeProps = new Properties();
               includeProps.setProperty("name", groupName);
               xsb.addEmptyElement("include", includeProps);
            }

            xsb.pop("define");
         }

         if (!this.m_includedGroups.isEmpty() || !this.m_excludedGroups.isEmpty()) {
            xsb.push("run");
            i$ = this.m_includedGroups.iterator();

            while(i$.hasNext()) {
               excludeGroupName = (String)i$.next();
               excludeProps = new Properties();
               excludeProps.setProperty("name", excludeGroupName);
               xsb.addEmptyElement("include", excludeProps);
            }

            i$ = this.m_excludedGroups.iterator();

            while(i$.hasNext()) {
               excludeGroupName = (String)i$.next();
               excludeProps = new Properties();
               excludeProps.setProperty("name", excludeGroupName);
               xsb.addEmptyElement("exclude", excludeProps);
            }

            xsb.pop("run");
         }

         if (this.m_xmlDependencyGroups != null && !this.m_xmlDependencyGroups.isEmpty()) {
            xsb.push("dependencies");
            i$ = this.m_xmlDependencyGroups.entrySet().iterator();

            while(i$.hasNext()) {
               Entry<String, String> entry = (Entry)i$.next();
               xsb.addEmptyElement("group", "name", (String)entry.getKey(), "depends-on", (String)entry.getValue());
            }

            xsb.pop("dependencies");
         }

         xsb.pop("groups");
      }

      if (null != this.m_xmlPackages && !this.m_xmlPackages.isEmpty()) {
         xsb.push("packages");
         i$ = this.m_xmlPackages.iterator();

         while(i$.hasNext()) {
            XmlPackage pack = (XmlPackage)i$.next();
            xsb.getStringBuffer().append(pack.toXml("      "));
         }

         xsb.pop("packages");
      }

      if (null != this.getXmlClasses() && !this.getXmlClasses().isEmpty()) {
         xsb.push("classes");
         i$ = this.getXmlClasses().iterator();

         while(i$.hasNext()) {
            XmlClass cls = (XmlClass)i$.next();
            xsb.getStringBuffer().append(cls.toXml(indent + "    "));
         }

         xsb.pop("classes");
      }

      xsb.pop("test");
      return xsb.toXML();
   }

   public String toString() {
      StringBuffer result = (new StringBuffer("[Test: \"" + this.m_name + "\"")).append(" verbose:" + this.m_verbose);
      result.append("[parameters:");
      Iterator i$ = this.m_parameters.keySet().iterator();

      String g;
      while(i$.hasNext()) {
         g = (String)i$.next();
         String v = (String)this.m_parameters.get(g);
         result.append(g + "=>" + v);
      }

      result.append("]");
      result.append("[metagroups:");
      i$ = this.m_metaGroups.keySet().iterator();

      while(i$.hasNext()) {
         g = (String)i$.next();
         List<String> mg = (List)this.m_metaGroups.get(g);
         result.append(g).append("=");
         Iterator i$ = mg.iterator();

         while(i$.hasNext()) {
            String n = (String)i$.next();
            result.append(n).append(",");
         }
      }

      result.append("] ");
      result.append("[included: ");
      i$ = this.m_includedGroups.iterator();

      while(i$.hasNext()) {
         g = (String)i$.next();
         result.append(g).append(" ");
      }

      result.append("]");
      result.append("[excluded: ");
      i$ = this.m_excludedGroups.iterator();

      while(i$.hasNext()) {
         g = (String)i$.next();
         result.append(g).append("");
      }

      result.append("] ");
      result.append(" classes:");
      i$ = this.m_xmlClasses.iterator();

      while(i$.hasNext()) {
         XmlClass cl = (XmlClass)i$.next();
         result.append(cl).append(" ");
      }

      result.append(" packages:");
      i$ = this.m_xmlPackages.iterator();

      while(i$.hasNext()) {
         XmlPackage p = (XmlPackage)i$.next();
         result.append(p).append(" ");
      }

      result.append("] ");
      return result.toString();
   }

   static void ppp(String s) {
      System.out.println("[XmlTest] " + s);
   }

   public Object clone() {
      XmlTest result = new XmlTest(this.getSuite());
      result.setName(this.getName());
      result.setIncludedGroups(this.getIncludedGroups());
      result.setExcludedGroups(this.getExcludedGroups());
      result.setJUnit(this.isJUnit());
      result.setParallel(this.getParallel());
      result.setVerbose(this.getVerbose());
      result.setParameters(this.getLocalParameters());
      result.setXmlPackages(this.getXmlPackages());
      result.setTimeOut(this.getTimeOut());
      Map<String, List<String>> metagroups = this.getMetaGroups();
      Iterator i$ = metagroups.entrySet().iterator();

      while(i$.hasNext()) {
         Entry<String, List<String>> group = (Entry)i$.next();
         result.addMetaGroup((String)group.getKey(), (List)group.getValue());
      }

      return result;
   }

   public List<Integer> getInvocationNumbers(String method) {
      if (this.m_failedInvocationNumbers == null) {
         this.m_failedInvocationNumbers = Maps.newHashMap();
         Iterator i$ = this.getXmlClasses().iterator();

         while(i$.hasNext()) {
            XmlClass c = (XmlClass)i$.next();
            Iterator i$ = c.getIncludedMethods().iterator();

            while(i$.hasNext()) {
               XmlInclude xi = (XmlInclude)i$.next();
               List<Integer> invocationNumbers = xi.getInvocationNumbers();
               if (invocationNumbers.size() > 0) {
                  String methodName = c.getName() + "." + xi.getName();
                  this.m_failedInvocationNumbers.put(methodName, invocationNumbers);
               }
            }
         }
      }

      List<Integer> result = (List)this.m_failedInvocationNumbers.get(method);
      return result == null ? Lists.newArrayList() : result;
   }

   public void setPreserveOrder(String preserveOrder) {
      this.m_preserveOrder = preserveOrder;
   }

   public String getPreserveOrder() {
      String result = this.m_preserveOrder;
      if (result == null || XmlSuite.DEFAULT_PRESERVE_ORDER.equals(this.m_preserveOrder)) {
         result = this.m_suite.getPreserveOrder();
      }

      return result;
   }

   public void setSuite(XmlSuite result) {
      this.m_suite = result;
   }

   public Boolean getAllowReturnValues() {
      return this.m_allowReturnValues != null ? this.m_allowReturnValues : this.getSuite().getAllowReturnValues();
   }

   public void setAllowReturnValues(Boolean allowReturnValues) {
      this.m_allowReturnValues = allowReturnValues;
   }

   public int getIndex() {
      return this.m_index;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.m_excludedGroups == null ? 0 : this.m_excludedGroups.hashCode());
      result = 31 * result + (this.m_failedInvocationNumbers == null ? 0 : this.m_failedInvocationNumbers.hashCode());
      result = 31 * result + (this.m_includedGroups == null ? 0 : this.m_includedGroups.hashCode());
      result = 31 * result + (this.m_isJUnit == null ? 0 : this.m_isJUnit.hashCode());
      result = 31 * result + (this.m_metaGroups == null ? 0 : this.m_metaGroups.hashCode());
      result = 31 * result + (this.m_methodSelectors == null ? 0 : this.m_methodSelectors.hashCode());
      result = 31 * result + (this.m_name == null ? 0 : this.m_name.hashCode());
      result = 31 * result + (this.m_parallel == null ? 0 : this.m_parallel.hashCode());
      result = 31 * result + (this.m_parameters == null ? 0 : this.m_parameters.hashCode());
      result = 31 * result + (this.m_preserveOrder == null ? 0 : this.m_preserveOrder.hashCode());
      result = 31 * result + (this.m_skipFailedInvocationCounts == null ? 0 : this.m_skipFailedInvocationCounts.hashCode());
      result = 31 * result + this.m_threadCount;
      result = 31 * result + (this.m_timeOut == null ? 0 : this.m_timeOut.hashCode());
      result = 31 * result + (this.m_verbose == null ? 0 : this.m_verbose.hashCode());
      result = 31 * result + (this.m_xmlClasses == null ? 0 : this.m_xmlClasses.hashCode());
      result = 31 * result + (this.m_xmlPackages == null ? 0 : this.m_xmlPackages.hashCode());
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return XmlSuite.f();
      } else if (this.getClass() != obj.getClass()) {
         return XmlSuite.f();
      } else {
         XmlTest other = (XmlTest)obj;
         if (this.m_excludedGroups == null) {
            if (other.m_excludedGroups != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_excludedGroups.equals(other.m_excludedGroups)) {
            return XmlSuite.f();
         }

         if (this.m_failedInvocationNumbers == null) {
            if (other.m_failedInvocationNumbers != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_failedInvocationNumbers.equals(other.m_failedInvocationNumbers)) {
            return XmlSuite.f();
         }

         if (this.m_includedGroups == null) {
            if (other.m_includedGroups != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_includedGroups.equals(other.m_includedGroups)) {
            return XmlSuite.f();
         }

         if (this.m_isJUnit == null) {
            if (other.m_isJUnit != null && !other.m_isJUnit.equals(XmlSuite.DEFAULT_JUNIT)) {
               return XmlSuite.f();
            }
         } else if (!this.m_isJUnit.equals(other.m_isJUnit)) {
            return XmlSuite.f();
         }

         if (this.m_metaGroups == null) {
            if (other.m_metaGroups != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_metaGroups.equals(other.m_metaGroups)) {
            return XmlSuite.f();
         }

         if (this.m_methodSelectors == null) {
            if (other.m_methodSelectors != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_methodSelectors.equals(other.m_methodSelectors)) {
            return XmlSuite.f();
         }

         if (this.m_name == null) {
            if (other.m_name != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_name.equals(other.m_name)) {
            return XmlSuite.f();
         }

         if (this.m_parallel == null) {
            if (other.m_parallel != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_parallel.equals(other.m_parallel)) {
            return XmlSuite.f();
         }

         if (this.m_parameters == null) {
            if (other.m_parameters != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_parameters.equals(other.m_parameters)) {
            return XmlSuite.f();
         }

         if (this.m_preserveOrder == null) {
            if (other.m_preserveOrder != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_preserveOrder.equals(other.m_preserveOrder)) {
            return XmlSuite.f();
         }

         if (this.m_skipFailedInvocationCounts == null) {
            if (other.m_skipFailedInvocationCounts != null) {
               return XmlSuite.f();
            }
         } else if (!this.m_skipFailedInvocationCounts.equals(other.m_skipFailedInvocationCounts)) {
            return XmlSuite.f();
         }

         if (this.m_threadCount != other.m_threadCount) {
            return XmlSuite.f();
         } else {
            if (this.m_timeOut == null) {
               if (other.m_timeOut != null) {
                  return XmlSuite.f();
               }
            } else if (!this.m_timeOut.equals(other.m_timeOut)) {
               return XmlSuite.f();
            }

            if (this.m_verbose == null) {
               if (other.m_verbose != null) {
                  return XmlSuite.f();
               }
            } else if (!this.m_verbose.equals(other.m_verbose)) {
               return XmlSuite.f();
            }

            if (this.m_xmlClasses == null) {
               if (other.m_xmlClasses != null) {
                  return XmlSuite.f();
               }
            } else if (!this.m_xmlClasses.equals(other.m_xmlClasses)) {
               return XmlSuite.f();
            }

            if (this.m_xmlPackages == null) {
               if (other.m_xmlPackages != null) {
                  return XmlSuite.f();
               }
            } else if (!this.m_xmlPackages.equals(other.m_xmlPackages)) {
               return XmlSuite.f();
            }

            return true;
         }
      }
   }

   public void addXmlDependencyGroup(String group, String dependsOn) {
      if (!this.m_xmlDependencyGroups.containsKey(group)) {
         this.m_xmlDependencyGroups.put(group, dependsOn);
      } else {
         throw new TestNGException("Duplicate group dependency found for group \"" + group + "\"" + ", use a space-separated list of groups in the \"depends-on\" attribute");
      }
   }

   public Map<String, String> getXmlDependencyGroups() {
      if (this.m_xmlGroups == null) {
         return this.m_xmlDependencyGroups;
      } else {
         Map<String, String> result = Maps.newHashMap();
         List<XmlDependencies> deps = this.m_xmlGroups.getDependencies();
         Iterator i$ = deps.iterator();

         while(i$.hasNext()) {
            XmlDependencies d = (XmlDependencies)i$.next();
            result.putAll(d.getDependencies());
         }

         return result;
      }
   }

   @ParentSetter
   public void setXmlSuite(XmlSuite suite) {
      this.m_suite = suite;
   }

   public void setGroups(XmlGroups xmlGroups) {
      this.m_xmlGroups = xmlGroups;
   }
}
