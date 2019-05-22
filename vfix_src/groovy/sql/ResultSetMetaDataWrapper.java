package groovy.sql;

import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;
import groovy.lang.ReadOnlyPropertyException;
import java.sql.ResultSetMetaData;
import org.codehaus.groovy.runtime.InvokerHelper;

public class ResultSetMetaDataWrapper extends GroovyObjectSupport {
   private ResultSetMetaData target;
   private int index;

   public ResultSetMetaDataWrapper(ResultSetMetaData target, int index) {
      this.target = target;
      this.index = index;
   }

   private Object[] getIndexedArgs(Object[] originalArgs) {
      Object[] result = new Object[originalArgs.length + 1];
      result[0] = this.index;
      int i = 0;

      for(int originalArgsLength = originalArgs.length; i < originalArgsLength; ++i) {
         result[i + 1] = originalArgs[i];
      }

      return result;
   }

   public Object invokeMethod(String name, Object args) {
      Object[] indexedArgs = this.getIndexedArgs((Object[])((Object[])args));
      return InvokerHelper.invokeMethod(this.target, name, indexedArgs);
   }

   private String getPropertyGetterName(String prop) {
      if (prop != null && prop.length() >= 1) {
         return "get" + prop.substring(0, 1).toUpperCase() + prop.substring(1);
      } else {
         throw new MissingPropertyException(prop, this.target.getClass());
      }
   }

   public Object getProperty(String property) {
      return this.invokeMethod(this.getPropertyGetterName(property), new Object[0]);
   }

   public void setProperty(String property, Object newValue) {
      throw new ReadOnlyPropertyException(property, this.target.getClass());
   }
}
