package groovy.lang;

public interface GroovyObject {
   Object invokeMethod(String var1, Object var2);

   Object getProperty(String var1);

   void setProperty(String var1, Object var2);

   MetaClass getMetaClass();

   void setMetaClass(MetaClass var1);
}
