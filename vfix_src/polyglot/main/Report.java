package polyglot.main;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class Report {
   public static final Collection topics = new HashSet();
   public static final Stack should_report = new Stack();
   protected static final Map reportTopics = new HashMap();
   static boolean noReporting = true;
   public static final String cfg = "cfg";
   public static final String context = "context";
   public static final String dataflow = "dataflow";
   public static final String errors = "errors";
   public static final String frontend = "frontend";
   public static final String imports = "imports";
   public static final String loader = "loader";
   public static final String resolver = "resolver";
   public static final String serialize = "serialize";
   public static final String time = "time";
   public static final String types = "types";
   public static final String visit = "visit";
   public static final String verbose = "verbose";
   public static final String debug = "debug";

   public static boolean should_report(String topic, int level) {
      return noReporting ? false : should_report((Collection)Collections.singletonList(topic), level);
   }

   public static boolean should_report(String[] topics, int level) {
      return noReporting ? false : should_report((Collection)Arrays.asList(topics), level);
   }

   public static boolean should_report(Collection topics, int level) {
      if (noReporting) {
         return false;
      } else {
         Iterator i = should_report.iterator();

         String topic;
         while(i.hasNext()) {
            topic = (String)i.next();
            if (level(topic) >= level) {
               return true;
            }
         }

         if (topics != null) {
            i = topics.iterator();

            while(i.hasNext()) {
               topic = (String)i.next();
               if (level(topic) >= level) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public static void addTopic(String topic, int level) {
      Integer i = (Integer)reportTopics.get(topic);
      if (i == null || i < level) {
         reportTopics.put(topic, new Integer(level));
      }

      noReporting = false;
   }

   protected static int level(String name) {
      Object i = reportTopics.get(name);
      return i == null ? 0 : (Integer)i;
   }

   public static void report(int level, String message) {
      for(int j = 1; j < level; ++j) {
         System.err.print("  ");
      }

      System.err.println(message);
   }

   static {
      topics.add("cfg");
      topics.add("context");
      topics.add("dataflow");
      topics.add("errors");
      topics.add("frontend");
      topics.add("imports");
      topics.add("loader");
      topics.add("resolver");
      topics.add("serialize");
      topics.add("time");
      topics.add("types");
      topics.add("visit");
      topics.add("verbose");
      topics.add("debug");
      should_report.push("verbose");
   }
}
