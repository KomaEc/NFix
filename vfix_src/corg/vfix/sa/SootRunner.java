package corg.vfix.sa;

import java.io.File;
import soot.Main;
import soot.options.Options;

public class SootRunner {
   private static String mainClass;
   private static String classPath;

   public static void init(String cp, String mc) {
      classPath = cp;
      mainClass = mc;
   }

   public static void main() {
      String javaLibs = String.join(File.separator, ".", "jre", "jre1.6.0_45", "lib", "rt.jar");
      Options.v().setPhaseOption("jb", "use-original-names:true");
      String[] sootArgs = new String[]{"-cp", classPath + File.pathSeparator + javaLibs, "-app", "-w", "-keep-line-number", "-no-writeout-body-releasing", "-p", "jb", "use-original-names:true", "cg.spark", "enabled:true,string-constants:true", "jap.npc", "enabled:true", "-allow-phantom-refs", "-src-prec", "apk", "-f", "J", "-main-class", mainClass, mainClass};
      System.out.println();
      System.out.println("soot args: classpath: " + classPath + "  mainclass: " + mainClass);
      Main.main(sootArgs);
      System.out.println();
   }
}
