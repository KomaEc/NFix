package org.codehaus.groovy.tools.shell.util;

import groovy.lang.Closure;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

public class SimpleCompletor extends com.gzoltar.shaded.jline.SimpleCompletor {
   public SimpleCompletor(String[] candidates) {
      super(candidates);
   }

   public SimpleCompletor() {
      this(new String[0]);
   }

   public SimpleCompletor(Closure loader) {
      this();

      assert loader != null;

      Object obj = loader.call();
      List list = null;
      if (obj instanceof List) {
         list = (List)obj;
      }

      if (list == null) {
         throw new IllegalStateException("The loader closure did not return a list of candicates; found: " + obj);
      } else {
         Iterator iter = list.iterator();

         while(iter.hasNext()) {
            this.add(String.valueOf(iter.next()));
         }

      }
   }

   public void add(String candidate) {
      this.addCandidateString(candidate);
   }

   public Object leftShift(String s) {
      this.add(s);
      return null;
   }

   public int complete(String buffer, int cursor, List clist) {
      String start = buffer == null ? "" : buffer;
      SortedSet matches = this.getCandidates().tailSet(start);

      String can;
      for(Iterator i = matches.iterator(); i.hasNext(); clist.add(can)) {
         can = (String)i.next();
         if (!can.startsWith(start)) {
            break;
         }

         String delim = this.getDelimiter();
         if (delim != null) {
            int index = can.indexOf(delim, cursor);
            if (index != -1) {
               can = can.substring(0, index + 1);
            }
         }
      }

      if (clist.size() == 1) {
         clist.set(0, (String)clist.get(0) + " ");
      }

      return clist.size() == 0 ? -1 : 0;
   }
}
