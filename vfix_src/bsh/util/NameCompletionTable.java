package bsh.util;

import bsh.NameSource;
import bsh.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class NameCompletionTable extends ArrayList implements NameCompletion {
   NameCompletionTable table;
   List sources;

   public void add(NameCompletionTable var1) {
      if (this.table != null) {
         throw new RuntimeException("Unimplemented usage error");
      } else {
         this.table = var1;
      }
   }

   public void add(NameSource var1) {
      if (this.sources == null) {
         this.sources = new ArrayList();
      }

      this.sources.add(var1);
   }

   protected void getMatchingNames(String var1, List var2) {
      for(int var3 = 0; var3 < this.size(); ++var3) {
         String var4 = (String)this.get(var3);
         if (var4.startsWith(var1)) {
            var2.add(var4);
         }
      }

      if (this.table != null) {
         this.table.getMatchingNames(var1, var2);
      }

      if (this.sources != null) {
         for(int var8 = 0; var8 < this.sources.size(); ++var8) {
            NameSource var5 = (NameSource)this.sources.get(var8);
            String[] var6 = var5.getAllNames();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var6[var7].startsWith(var1)) {
                  var2.add(var6[var7]);
               }
            }
         }
      }

   }

   public String[] completeName(String var1) {
      ArrayList var2 = new ArrayList();
      this.getMatchingNames(var1, var2);
      if (var2.size() == 0) {
         return new String[0];
      } else {
         String var3 = (String)var2.get(0);

         for(int var4 = 1; var4 < var2.size() && var3.length() > 0; ++var4) {
            var3 = StringUtil.maxCommonPrefix(var3, (String)var2.get(var4));
            if (var3.equals(var1)) {
               break;
            }
         }

         return var3.length() > var1.length() ? new String[]{var3} : (String[])var2.toArray(new String[0]);
      }
   }
}
