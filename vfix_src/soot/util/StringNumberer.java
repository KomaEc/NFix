package soot.util;

import heros.ThreadSafe;
import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class StringNumberer extends ArrayNumberer<NumberedString> {
   private Map<String, NumberedString> stringToNumbered = new HashMap(1024);

   public synchronized NumberedString findOrAdd(String s) {
      NumberedString ret = (NumberedString)this.stringToNumbered.get(s);
      if (ret == null) {
         ret = new NumberedString(s);
         this.stringToNumbered.put(s, ret);
         this.add(ret);
      }

      return ret;
   }

   public NumberedString find(String s) {
      return (NumberedString)this.stringToNumbered.get(s);
   }
}
