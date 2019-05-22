package com.gzoltar.shaded.org.pitest.testapi.foreignclassloader;

import com.gzoltar.shaded.org.pitest.functional.SideEffect2;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import java.util.Iterator;
import java.util.List;

public class Events {
   public static void applyEvents(List<String> encodedEvents, ResultCollector rc, Description description) {
      Iterator i$ = encodedEvents.iterator();

      while(i$.hasNext()) {
         String each = (String)i$.next();
         SideEffect2<ResultCollector, Description> event = (SideEffect2)IsolationUtils.fromXml(each);
         event.apply(rc, description);
      }

   }
}
