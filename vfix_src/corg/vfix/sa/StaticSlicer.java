package corg.vfix.sa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import soot.Scene;
import soot.SootClass;
import soot.util.Chain;

public class StaticSlicer {
   private static String file_target;
   private static String app_classes;

   public static void init(String ft) {
      file_target = ft;
   }

   public static void main() throws IOException {
      app_classes = getAppClasses();
      System.out.println("App Classes:\n" + app_classes);
   }

   public static String getAppClasses() throws IOException {
      String str = "";
      File file = new File(file_target);
      if (file.exists()) {
         BufferedReader br = new BufferedReader(new FileReader(file));
         str = br.readLine().trim();
         br.close();
         return str;
      } else {
         SootRunner.main();
         Chain<SootClass> appCls = Scene.v().getApplicationClasses();
         if (appCls == null) {
            return str;
         } else {
            Object[] classes = appCls.toArray();

            for(int i = 0; i < classes.length; ++i) {
               String appclass = classes[i].toString();
               if (!"jap.npc".equals(appclass) && !"enabled:true".equals(appclass) && !"cg.spark".equals(appclass) && !"enabled:true,string-constants:true".equals(appclass)) {
                  str = str + classes[i].toString() + ":";
               }
            }

            outputAppCls(str);
            return str;
         }
      }
   }

   private static void outputAppCls(String str) throws IOException {
      File file = new File(file_target);
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write(str);
      writer.flush();
      writer.close();
   }
}
