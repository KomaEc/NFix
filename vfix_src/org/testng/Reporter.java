package org.testng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.testng.collections.Lists;
import org.testng.collections.Maps;
import org.testng.util.Strings;

public class Reporter {
   private static ThreadLocal<ITestResult> m_currentTestResult = new InheritableThreadLocal();
   private static List<String> m_output = new Vector();
   private static Map<Integer, List<Integer>> m_methodOutputMap = Maps.newHashMap();
   private static boolean m_escapeHtml = false;
   private static ThreadLocal<List<String>> m_orphanedOutput = new InheritableThreadLocal();

   public static void setCurrentTestResult(ITestResult m) {
      m_currentTestResult.set(m);
   }

   public static List<String> getOutput() {
      return m_output;
   }

   public static void clear() {
      m_methodOutputMap.clear();
      m_output.clear();
   }

   public static void setEscapeHtml(boolean escapeHtml) {
      m_escapeHtml = escapeHtml;
   }

   private static synchronized void log(String s, ITestResult m) {
      if (m_escapeHtml) {
         s = Strings.escapeHtml(s);
      }

      if (m == null) {
         if (m_orphanedOutput.get() == null) {
            m_orphanedOutput.set(new ArrayList());
         }

         ((List)m_orphanedOutput.get()).add(s);
      } else {
         int n = getOutput().size();
         List<Integer> lines = (List)m_methodOutputMap.get(m.hashCode());
         if (lines == null) {
            lines = Lists.newArrayList();
            m_methodOutputMap.put(m.hashCode(), lines);
         }

         if (m_orphanedOutput.get() != null) {
            n += ((List)m_orphanedOutput.get()).size();
            getOutput().addAll((Collection)m_orphanedOutput.get());
            m_orphanedOutput.remove();
         }

         lines.add(n);
         getOutput().add(s);
      }
   }

   public static void log(String s) {
      log(s, getCurrentTestResult());
   }

   public static void log(String s, int level, boolean logToStandardOut) {
      if (TestRunner.getVerbose() >= level) {
         log(s, getCurrentTestResult());
         if (logToStandardOut) {
            System.out.println(s);
         }
      }

   }

   public static void log(String s, boolean logToStandardOut) {
      log(s, getCurrentTestResult());
      if (logToStandardOut) {
         System.out.println(s);
      }

   }

   public static void log(String s, int level) {
      if (TestRunner.getVerbose() >= level) {
         log(s, getCurrentTestResult());
      }

   }

   public static ITestResult getCurrentTestResult() {
      return (ITestResult)m_currentTestResult.get();
   }

   public static synchronized List<String> getOutput(ITestResult tr) {
      List<String> result = Lists.newArrayList();
      if (tr == null) {
         return result;
      } else {
         List<Integer> lines = (List)m_methodOutputMap.get(tr.hashCode());
         if (lines != null) {
            Iterator i$ = lines.iterator();

            while(i$.hasNext()) {
               Integer n = (Integer)i$.next();
               result.add(getOutput().get(n));
            }
         }

         return result;
      }
   }
}
