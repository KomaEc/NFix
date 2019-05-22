package org.jboss.util.collection;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class CollectionsUtil {
   public static List list(Enumeration e) {
      ArrayList result = new ArrayList();

      while(e.hasMoreElements()) {
         result.add(e.nextElement());
      }

      return result;
   }
}
