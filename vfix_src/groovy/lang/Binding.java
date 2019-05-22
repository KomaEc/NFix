package groovy.lang;

import java.util.LinkedHashMap;
import java.util.Map;

public class Binding extends GroovyObjectSupport {
   private Map variables;

   public Binding() {
   }

   public Binding(Map variables) {
      this.variables = variables;
   }

   public Binding(String[] args) {
      this();
      this.setVariable("args", args);
   }

   public Object getVariable(String name) {
      if (this.variables == null) {
         throw new MissingPropertyException(name, this.getClass());
      } else {
         Object result = this.variables.get(name);
         if (result == null && !this.variables.containsKey(name)) {
            throw new MissingPropertyException(name, this.getClass());
         } else {
            return result;
         }
      }
   }

   public void setVariable(String name, Object value) {
      if (this.variables == null) {
         this.variables = new LinkedHashMap();
      }

      this.variables.put(name, value);
   }

   public Map getVariables() {
      if (this.variables == null) {
         this.variables = new LinkedHashMap();
      }

      return this.variables;
   }

   public Object getProperty(String property) {
      try {
         return super.getProperty(property);
      } catch (MissingPropertyException var3) {
         return this.getVariable(property);
      }
   }

   public void setProperty(String property, Object newValue) {
      try {
         super.setProperty(property, newValue);
      } catch (MissingPropertyException var4) {
         this.setVariable(property, newValue);
      }

   }
}
