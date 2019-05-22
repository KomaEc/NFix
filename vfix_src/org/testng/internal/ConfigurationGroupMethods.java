package org.testng.internal;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.testng.ITestNGMethod;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class ConfigurationGroupMethods implements Serializable {
   private static final long serialVersionUID = 1660798519864898480L;
   private final Map<String, List<ITestNGMethod>> m_beforeGroupsMethods;
   private final Map<String, List<ITestNGMethod>> m_afterGroupsMethods;
   private final ITestNGMethod[] m_allMethods;
   private Map<String, List<ITestNGMethod>> m_afterGroupsMap = null;

   public ConfigurationGroupMethods(ITestNGMethod[] allMethods, Map<String, List<ITestNGMethod>> beforeGroupsMethods, Map<String, List<ITestNGMethod>> afterGroupsMethods) {
      this.m_allMethods = allMethods;
      this.m_beforeGroupsMethods = beforeGroupsMethods;
      this.m_afterGroupsMethods = afterGroupsMethods;
   }

   public Map<String, List<ITestNGMethod>> getBeforeGroupsMethods() {
      return this.m_beforeGroupsMethods;
   }

   public Map<String, List<ITestNGMethod>> getAfterGroupsMethods() {
      return this.m_afterGroupsMethods;
   }

   public synchronized boolean isLastMethodForGroup(String group, ITestNGMethod method) {
      int invocationCount = method.getCurrentInvocationCount();
      if (invocationCount < method.getInvocationCount() * method.getParameterInvocationCount()) {
         return false;
      } else {
         if (this.m_afterGroupsMap == null) {
            this.m_afterGroupsMap = this.initializeAfterGroupsMap();
         }

         List<ITestNGMethod> methodsInGroup = (List)this.m_afterGroupsMap.get(group);
         if (null != methodsInGroup && !methodsInGroup.isEmpty()) {
            methodsInGroup.remove(method);
            return methodsInGroup.isEmpty();
         } else {
            return false;
         }
      }
   }

   private synchronized Map<String, List<ITestNGMethod>> initializeAfterGroupsMap() {
      Map<String, List<ITestNGMethod>> result = Maps.newHashMap();
      ITestNGMethod[] arr$ = this.m_allMethods;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ITestNGMethod m = arr$[i$];
         String[] groups = m.getGroups();
         String[] arr$ = groups;
         int len$ = groups.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String g = arr$[i$];
            List<ITestNGMethod> methodsInGroup = (List)result.get(g);
            if (null == methodsInGroup) {
               methodsInGroup = Lists.newArrayList();
               result.put(g, methodsInGroup);
            }

            methodsInGroup.add(m);
         }
      }

      return result;
   }

   public synchronized void removeBeforeMethod(String group, ITestNGMethod method) {
      List<ITestNGMethod> methods = (List)this.m_beforeGroupsMethods.get(group);
      if (methods != null) {
         Object success = methods.remove(method);
         if (success == null) {
            this.log("Couldn't remove beforeGroups method " + method + " for group " + group);
         }
      } else {
         this.log("Couldn't find any beforeGroups method for group " + group);
      }

   }

   private void log(String string) {
      Utils.log("ConfigurationGroupMethods", 2, string);
   }

   public synchronized Map<String, List<ITestNGMethod>> getBeforeGroupsMap() {
      return this.m_beforeGroupsMethods;
   }

   public synchronized Map<String, List<ITestNGMethod>> getAfterGroupsMap() {
      return this.m_afterGroupsMethods;
   }

   public synchronized void removeBeforeGroups(String[] groups) {
      String[] arr$ = groups;
      int len$ = groups.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String group = arr$[i$];
         this.m_beforeGroupsMethods.remove(group);
      }

   }

   public synchronized void removeAfterGroups(Collection<String> groups) {
      Iterator i$ = groups.iterator();

      while(i$.hasNext()) {
         String group = (String)i$.next();
         this.m_afterGroupsMethods.remove(group);
      }

   }
}
