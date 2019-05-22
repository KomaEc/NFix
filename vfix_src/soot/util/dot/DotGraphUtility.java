package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DotGraphUtility {
   private static final Logger logger = LoggerFactory.getLogger(DotGraphUtility.class);

   public static String replaceQuotes(String original) {
      byte[] ord = original.getBytes();
      int quotes = 0;
      boolean escapeActive = false;
      byte[] newsrc = ord;
      int i = ord.length;

      int j;
      for(j = 0; j < i; ++j) {
         byte element = newsrc[j];
         switch(element) {
         case 34:
            ++quotes;
            if (escapeActive) {
               ++quotes;
            }
         default:
            escapeActive = false;
            break;
         case 92:
            escapeActive = true;
         }
      }

      if (quotes == 0) {
         return original;
      } else {
         newsrc = new byte[ord.length + quotes];
         i = 0;
         j = 0;

         for(int n = ord.length; i < n; ++j) {
            if (ord[i] == 34) {
               if (i > 0 && ord[i - 1] == 92) {
                  newsrc[j++] = 92;
               }

               newsrc[j++] = 92;
            }

            newsrc[j] = ord[i];
            ++i;
         }

         return new String(newsrc);
      }
   }

   public static String replaceReturns(String original) {
      byte[] ord = original.getBytes();
      int quotes = 0;
      byte[] newsrc = ord;
      int i = ord.length;

      int j;
      for(j = 0; j < i; ++j) {
         byte element = newsrc[j];
         if (element == 10) {
            ++quotes;
         }
      }

      if (quotes == 0) {
         return original;
      } else {
         newsrc = new byte[ord.length + quotes];
         i = 0;
         j = 0;

         for(int n = ord.length; i < n; ++j) {
            if (ord[i] == 10) {
               newsrc[j++] = 92;
               newsrc[j] = 110;
            } else {
               newsrc[j] = ord[i];
            }

            ++i;
         }

         return new String(newsrc);
      }
   }

   public static void renderLine(OutputStream out, String content, int indent) throws IOException {
      for(int i = 0; i < indent; ++i) {
         out.write(32);
      }

      content = content + "\n";
      out.write(content.getBytes());
   }
}
