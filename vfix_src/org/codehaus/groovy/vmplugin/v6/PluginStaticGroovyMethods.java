package org.codehaus.groovy.vmplugin.v6;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.codehaus.groovy.runtime.DefaultGroovyMethodsSupport;

public class PluginStaticGroovyMethods extends DefaultGroovyMethodsSupport {
   public static ScriptEngine $static_propertyMissing(ScriptEngineManager self, String languageShortName) {
      ScriptEngineManager manager = new ScriptEngineManager();
      return manager.getEngineByName(languageShortName);
   }
}
