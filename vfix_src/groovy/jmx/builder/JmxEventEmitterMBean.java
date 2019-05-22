package groovy.jmx.builder;

public interface JmxEventEmitterMBean {
   String getEvent();

   void setEvent(String var1);

   long send(Object var1);
}
