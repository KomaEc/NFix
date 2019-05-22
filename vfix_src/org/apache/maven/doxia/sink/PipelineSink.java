package org.apache.maven.doxia.sink;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.List;

public class PipelineSink implements InvocationHandler {
   private List pipeline;

   public PipelineSink(List pipeline) {
      this.pipeline = pipeline;
   }

   public void addSink(Sink sink) {
      this.pipeline.add(sink);
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      Iterator it = this.pipeline.iterator();

      while(it.hasNext()) {
         Sink sink = (Sink)it.next();
         method.invoke(sink, args);
      }

      return null;
   }

   public static Sink newInstance(List pipeline) {
      return (Sink)Proxy.newProxyInstance(PipelineSink.class.getClassLoader(), new Class[]{Sink.class}, new PipelineSink(pipeline));
   }
}
