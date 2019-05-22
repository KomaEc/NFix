package groovy.lang;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BenchmarkInterceptor implements Interceptor {
   protected Map calls = new LinkedHashMap();

   public Map getCalls() {
      return this.calls;
   }

   public void reset() {
      this.calls = new HashMap();
   }

   public Object beforeInvoke(Object object, String methodName, Object[] arguments) {
      if (!this.calls.containsKey(methodName)) {
         this.calls.put(methodName, new LinkedList());
      }

      ((List)this.calls.get(methodName)).add(new Long(System.currentTimeMillis()));
      return null;
   }

   public Object afterInvoke(Object object, String methodName, Object[] arguments, Object result) {
      ((List)this.calls.get(methodName)).add(new Long(System.currentTimeMillis()));
      return result;
   }

   public boolean doInvoke() {
      return true;
   }

   public List statistic() {
      List result = new LinkedList();

      Object[] line;
      int accTime;
      for(Iterator iter = this.calls.keySet().iterator(); iter.hasNext(); line[2] = new Long((long)accTime)) {
         line = new Object[3];
         result.add(line);
         line[0] = iter.next();
         List times = (List)this.calls.get(line[0]);
         line[1] = new Integer(times.size() / 2);
         accTime = 0;

         Long start;
         Long end;
         for(Iterator it = times.iterator(); it.hasNext(); accTime = (int)((long)accTime + (end - start))) {
            start = (Long)it.next();
            end = (Long)it.next();
         }
      }

      return result;
   }
}
