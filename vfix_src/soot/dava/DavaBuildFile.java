package soot.dava;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class DavaBuildFile {
   public static void generate(PrintWriter out, ArrayList<String> decompiledClasses) {
      out.print("<project default=\"compile\" name=\"Build file for decompiled code\">\n");
      out.print("\t<description>\n");
      out.print("  This is the build file produced by Dava for the decompiled code.\n");
      out.print("  New features like (formatting using jalopy etc) will be added to this build file\n");
      out.print("</description>\n");
      out.print("<!-- properties for project directories -->\n");
      out.print("<property name=\"srcDir\" location=\"src\"/>\n");
      out.print("<property name=\"classesDir\" location=\"classes\"/>\n");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("\t<!--  ========== Compile Target ================= -->\n");
      out.print("\t<target name=\"compile\" description=\"Compile .java files\">\n");
      out.print("\t<javac srcdir=\"${srcDir}\" destdir=\"${classesDir}\">\n");
      out.print("\t  <classpath>\n");
      out.print("\t\t <pathelement location=\"${junitJar}\"/>\n");
      out.print("\t  </classpath>\n");
      out.print("\t </javac>\n");
      out.print("\t</target>\n");
      out.print("\t<!--  ==========AST METRICS FOR DECOMPILED CODE================= -->\n");
      out.print("<target name=\"ast-metrics\" description=\"Compute the ast metrics\">\n");
      out.print("   <exec executable=\"java\" dir=\"src\">\n");
      out.print("\t\t<arg value=\"-Xmx400m\" />\n");
      out.print("\t\t<arg value=\"soot.Main\" />\n");
      out.print("\t\t<arg value=\"-ast-metrics\" />\n");
      out.print("\t\t<arg value=\"--src-prec\" />\n");
      out.print("\t\t<arg value=\"java\" />\n");

      String temp;
      for(Iterator it = decompiledClasses.iterator(); it.hasNext(); out.print("\t\t<arg value=\"" + temp + "\" />\n")) {
         temp = (String)it.next();
         if (temp.endsWith(".java")) {
            temp = temp.substring(0, temp.length() - 5);
         }
      }

      out.print("");
      out.print("\t  </exec>\n");
      out.print("\t</target>\n");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("");
      out.print("</project>");
   }
}
