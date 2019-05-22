package org.netbeans.lib.cvsclient.response;

import java.io.EOFException;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.netbeans.lib.cvsclient.command.KeywordSubstitutionOptions;
import org.netbeans.lib.cvsclient.util.LoggedDataInputStream;
import org.netbeans.lib.cvsclient.util.SimpleStringPattern;
import org.netbeans.lib.cvsclient.util.StringPattern;

public class WrapperSendResponse implements Response {
   public static Map parseWrappers(String var0) {
      StringTokenizer var1 = new StringTokenizer(var0);
      SimpleStringPattern var2 = new SimpleStringPattern(var1.nextToken());
      Object var3 = null;

      while(var1.hasMoreTokens()) {
         String var4 = var1.nextToken();
         String var5 = var1.nextToken();
         if (var4.equals("-k")) {
            int var6 = var5.indexOf(39);
            int var7 = var5.lastIndexOf(39);
            if (var6 >= 0 && var7 >= 0) {
               var5 = var5.substring(var6 + 1, var7);
            }

            KeywordSubstitutionOptions var8 = KeywordSubstitutionOptions.findKeywordSubstOption(var5);
            if (var3 == null) {
               if (!var1.hasMoreTokens()) {
                  var3 = Collections.singletonMap(var2, var8);
               } else {
                  var3 = new LinkedHashMap();
                  ((Map)var3).put(var2, var8);
               }
            } else {
               ((Map)var3).put(var2, var8);
            }
         }
      }

      return (Map)var3;
   }

   public void process(LoggedDataInputStream var1, ResponseServices var2) throws ResponseException {
      try {
         String var3 = var1.readLine();
         Map var4 = parseWrappers(var3);
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            StringPattern var6 = (StringPattern)var5.next();
            KeywordSubstitutionOptions var7 = (KeywordSubstitutionOptions)var4.get(var6);
            var2.addWrapper(var6, var7);
         }

      } catch (EOFException var8) {
         throw new ResponseException(var8, ResponseException.getLocalMessage("CommandException.EndOfFile", (Object[])null));
      } catch (IOException var9) {
         throw new ResponseException(var9);
      } catch (NoSuchElementException var10) {
         throw new ResponseException(var10);
      }
   }

   public boolean isTerminalResponse() {
      return false;
   }
}
