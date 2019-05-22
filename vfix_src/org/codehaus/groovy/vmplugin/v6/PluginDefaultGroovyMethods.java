package org.codehaus.groovy.vmplugin.v6;

import groovy.lang.Binding;
import java.io.Reader;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport;

public class PluginDefaultGroovyMethods extends DefaultGroovyMethodsSupport {
   public static Object eval(ScriptEngine self, String script, Binding binding) throws ScriptException {
      storeBindingVars(self, binding);
      Object result = self.eval(script);
      retrieveBindingVars(self, binding);
      return result;
   }

   public static Object eval(ScriptEngine self, Reader reader, Binding binding) throws ScriptException {
      storeBindingVars(self, binding);
      Object result = self.eval(reader);
      retrieveBindingVars(self, binding);
      return result;
   }

   private static void retrieveBindingVars(ScriptEngine self, Binding binding) {
      Set<Entry<String, Object>> returnVars = self.getBindings(100).entrySet();
      Iterator i$ = returnVars.iterator();

      while(i$.hasNext()) {
         Entry<String, Object> me = (Entry)i$.next();
         binding.setVariable((String)me.getKey(), me.getValue());
      }

   }

   private static void storeBindingVars(ScriptEngine self, Binding binding) {
      Set<Entry> vars = binding.getVariables().entrySet();
      Iterator i$ = vars.iterator();

      while(i$.hasNext()) {
         Entry me = (Entry)i$.next();
         self.put(me.getKey().toString(), me.getValue());
      }

   }
}
