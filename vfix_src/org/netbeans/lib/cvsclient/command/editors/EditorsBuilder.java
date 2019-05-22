package org.netbeans.lib.cvsclient.command.editors;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.command.Builder;
import org.netbeans.lib.cvsclient.event.EventManager;
import org.netbeans.lib.cvsclient.event.FileInfoEvent;

public class EditorsBuilder implements Builder {
   private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd hh:mm:ss yyyy");
   private final EventManager eventManager;
   private String editorsFileName = null;

   EditorsBuilder(EventManager var1) {
      this.eventManager = var1;
   }

   public void parseLine(String var1, boolean var2) {
      if (!var2) {
         this.parseLine(var1);
      }

   }

   public void parseEnhancedMessage(String var1, Object var2) {
   }

   public void outputDone() {
   }

   private boolean parseLine(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, "\t");
      if (!var2.hasMoreTokens()) {
         return false;
      } else {
         if (!var1.startsWith("\t")) {
            this.editorsFileName = var2.nextToken();
            if (!var2.hasMoreTokens()) {
               return false;
            }
         } else if (this.editorsFileName == null) {
            return false;
         }

         String var3 = var2.nextToken();
         if (!var2.hasMoreTokens()) {
            return false;
         } else {
            String var4 = var2.nextToken();
            if (!var2.hasMoreTokens()) {
               return false;
            } else {
               String var5 = var2.nextToken();
               if (!var2.hasMoreTokens()) {
                  return false;
               } else {
                  String var6 = var2.nextToken();

                  try {
                     EditorsFileInfoContainer var7 = this.parseEntries(var6, this.editorsFileName, var3, var4, var5);
                     FileInfoEvent var8 = new FileInfoEvent(this, var7);
                     this.eventManager.fireCVSEvent(var8);
                     return true;
                  } catch (ParseException var9) {
                     return false;
                  }
               }
            }
         }
      }
   }

   private EditorsFileInfoContainer parseEntries(String var1, String var2, String var3, String var4, String var5) throws ParseException {
      int var6 = var2.lastIndexOf(47);
      if (var6 >= 0) {
         var2 = var2.substring(var6 + 1);
      }

      Date var7 = this.parseDate(var4);
      File var8 = new File(var1, var2);
      return new EditorsFileInfoContainer(var8, var3, var7, var5);
   }

   private Date parseDate(String var1) throws ParseException {
      int var2 = Math.max(var1.indexOf(32), 0);
      int var3 = Math.min(var1.lastIndexOf(32), var1.length());
      var1 = var1.substring(var2, var3).trim();
      return DATE_FORMAT.parse(var1);
   }
}
