package corg.vfix.fl;

import com.gzoltar.master.GZoltar;
import corg.vfix.parser.SpectraParser;
import corg.vfix.sa.StaticSlicer;
import java.io.File;
import java.io.IOException;

public class GzoltarRunner {
   private static String[] gzoltarArgs;

   public static void init(String classesDir, String testsDir, String testClasses, String dependency) throws IOException {
      String gzoltarDataDir = classesDir + "/../../gzoltar-data";
      String targetClasses = StaticSlicer.getAppClasses();
      String deps = generateDeps(classesDir, dependency);
      System.out.println(deps);
      if (deps.length() <= 0) {
         gzoltarArgs = new String[]{"-DclassesDir=" + classesDir, "-DtestsDir=" + testsDir, "-Dtargetclasses=" + targetClasses, "-Dtestclasses=" + testClasses, "-Dgzoltar_data_dir=" + gzoltarDataDir, "-diagnose"};
      } else {
         gzoltarArgs = new String[]{"-DclassesDir=" + classesDir, "-DtestsDir=" + testsDir, "-Dtargetclasses=" + targetClasses, "-Dtestclasses=" + testClasses, "-Dgzoltar_data_dir=" + gzoltarDataDir, "-Ddeps=" + deps, "-diagnose"};
      }

   }

   private static String generateDeps(String classesDir, String dependency) {
      String deps = "";
      if (dependency != null && !"".equals(dependency)) {
         String[] ds = dependency.split(":");
         String[] var7 = ds;
         int var6 = ds.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String d = var7[var5];
            deps = deps + classesDir + d + ":";
         }

         if (deps.length() > 0) {
            deps = deps.substring(0, deps.length() - 1);
         }

         return deps;
      } else {
         return deps;
      }
   }

   public static void main() {
      File file = new File(SpectraParser.getSpectraFile());
      if (!file.exists()) {
         GZoltar.main(gzoltarArgs);
      } else {
         System.out.println("Gzoltar results exist, skip...");
      }

   }
}
