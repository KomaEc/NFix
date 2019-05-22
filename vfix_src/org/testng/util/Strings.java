package org.testng.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

public class Strings {
   private static List<String> ESCAPE_HTML_LIST = Lists.newArrayList((Object[])("&", "&amp;", "<", "&lt;", ">", "&gt;"));
   private static final Map<String, String> ESCAPE_HTML_MAP = Maps.newLinkedHashMap();

   public static boolean isNullOrEmpty(String string) {
      return string == null || string.length() == 0;
   }

   public static String escapeHtml(String text) {
      String result = text;

      Entry entry;
      for(Iterator i$ = ESCAPE_HTML_MAP.entrySet().iterator(); i$.hasNext(); result = result.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue())) {
         entry = (Entry)i$.next();
      }

      return result;
   }

   public static void main(String[] args) {
      System.out.println(escapeHtml("10 < 20 && 30 > 20"));
   }

   static {
      for(int i = 0; i < ESCAPE_HTML_LIST.size(); i += 2) {
         ESCAPE_HTML_MAP.put(ESCAPE_HTML_LIST.get(i), ESCAPE_HTML_LIST.get(i + 1));
      }

   }
}
