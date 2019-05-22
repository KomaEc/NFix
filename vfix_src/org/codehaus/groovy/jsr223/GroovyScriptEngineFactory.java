package org.codehaus.groovy.jsr223;

import groovy.lang.GroovySystem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class GroovyScriptEngineFactory implements ScriptEngineFactory {
   private static final String VERSION = "2.0";
   private static final String SHORT_NAME = "groovy";
   private static final String LANGUAGE_NAME = "Groovy";
   private static final List<String> NAMES;
   private static final List<String> EXTENSIONS;
   private static final List<String> MIME_TYPES;

   public String getEngineName() {
      return "Groovy Scripting Engine";
   }

   public String getEngineVersion() {
      return "2.0";
   }

   public String getLanguageName() {
      return "Groovy";
   }

   public String getLanguageVersion() {
      return GroovySystem.getVersion();
   }

   public List<String> getExtensions() {
      return EXTENSIONS;
   }

   public List<String> getMimeTypes() {
      return MIME_TYPES;
   }

   public List<String> getNames() {
      return NAMES;
   }

   public Object getParameter(String key) {
      if ("javax.script.name".equals(key)) {
         return "groovy";
      } else if ("javax.script.engine".equals(key)) {
         return this.getEngineName();
      } else if ("javax.script.engine_version".equals(key)) {
         return "2.0";
      } else if ("javax.script.language".equals(key)) {
         return "Groovy";
      } else if ("javax.script.language_version".equals(key)) {
         return GroovySystem.getVersion();
      } else if ("THREADING".equals(key)) {
         return "MULTITHREADED";
      } else {
         throw new IllegalArgumentException("Invalid key");
      }
   }

   public ScriptEngine getScriptEngine() {
      return new GroovyScriptEngineImpl();
   }

   public String getMethodCallSyntax(String obj, String method, String... args) {
      String ret = obj + "." + method + "(";
      int len = args.length;
      if (len == 0) {
         ret = ret + ")";
         return ret;
      } else {
         for(int i = 0; i < len; ++i) {
            ret = ret + args[i];
            if (i != len - 1) {
               ret = ret + ",";
            } else {
               ret = ret + ")";
            }
         }

         return ret;
      }
   }

   public String getOutputStatement(String toDisplay) {
      StringBuilder buf = new StringBuilder();
      buf.append("println(\"");
      int len = toDisplay.length();

      for(int i = 0; i < len; ++i) {
         char ch = toDisplay.charAt(i);
         switch(ch) {
         case '"':
            buf.append("\\\"");
            break;
         case '\\':
            buf.append("\\\\");
            break;
         default:
            buf.append(ch);
         }
      }

      buf.append("\")");
      return buf.toString();
   }

   public String getProgram(String... statements) {
      StringBuilder ret = new StringBuilder();
      int len = statements.length;

      for(int i = 0; i < len; ++i) {
         ret.append(statements[i]);
         ret.append('\n');
      }

      return ret.toString();
   }

   static {
      List<String> n = new ArrayList(2);
      n.add("groovy");
      n.add("Groovy");
      NAMES = Collections.unmodifiableList(n);
      n = new ArrayList(1);
      n.add("groovy");
      EXTENSIONS = Collections.unmodifiableList(n);
      n = new ArrayList(1);
      n.add("application/x-groovy");
      MIME_TYPES = Collections.unmodifiableList(n);
   }
}
