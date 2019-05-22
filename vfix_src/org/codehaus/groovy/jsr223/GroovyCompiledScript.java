package org.codehaus.groovy.jsr223;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class GroovyCompiledScript extends CompiledScript {
   private final GroovyScriptEngineImpl engine;
   private final Class clasz;

   public GroovyCompiledScript(GroovyScriptEngineImpl engine, Class clasz) {
      this.engine = engine;
      this.clasz = clasz;
   }

   public Object eval(ScriptContext context) throws ScriptException {
      return this.engine.eval(this.clasz, context);
   }

   public ScriptEngine getEngine() {
      return this.engine;
   }
}
