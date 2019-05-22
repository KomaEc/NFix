package soot.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Main;
import soot.PackManager;
import soot.PhaseOptions;
import soot.SootMethod;
import soot.SourceLocator;
import soot.Transform;
import soot.Unit;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.AltClassLoader;
import soot.util.cfgcmd.CFGGraphType;
import soot.util.cfgcmd.CFGIntermediateRep;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

public class CFGViewer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(CFGViewer.class);
   private static final String packToJoin = "jtp";
   private static final String phaseSubname = "printcfg";
   private static final String phaseFullname = "jtp.printcfg";
   private static final String altClassPathOptionName = "alt-class-path";
   private static final String graphTypeOptionName = "graph-type";
   private static final String defaultGraph = "BriefUnitGraph";
   private static final String irOptionName = "ir";
   private static final String defaultIR = "jimple";
   private static final String multipageOptionName = "multipages";
   private static final String briefLabelOptionName = "brief";
   private CFGGraphType graphtype;
   private CFGIntermediateRep ir;
   private CFGToDotGraph drawer;
   private Map<String, String> methodsToPrint;

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      this.initialize(options);
      SootMethod meth = b.getMethod();
      if (this.methodsToPrint == null || meth.getDeclaringClass().getName() == this.methodsToPrint.get(meth.getName())) {
         Body body = this.ir.getBody((JimpleBody)b);
         this.print_cfg(body);
      }

   }

   public static void main(String[] args) {
      CFGViewer viewer = new CFGViewer();
      Transform printTransform = new Transform("jtp.printcfg", viewer);
      printTransform.setDeclaredOptions("enabled alt-class-path graph-type ir multipages brief ");
      printTransform.setDefaultOptions("enabled alt-class-path: graph-type:BriefUnitGraph ir:jimple multipages:false  brief:false ");
      PackManager.v().getPack("jtp").add(printTransform);
      args = viewer.parse_options(args);
      if (args.length == 0) {
         usage();
      } else {
         Main.main(args);
      }

   }

   private static void usage() {
      logger.debug("Usage:\n   java soot.util.CFGViewer [soot options] [CFGViewer options] [class[:method]]...\n\n   CFGViewer options:\n      (When specifying the value for an '=' option, you only\n       need to type enough characters to specify the choice\n       unambiguously, and case is ignored.)\n\n       --alt-classpath PATH :\n                specifies the classpath from which to load classes\n                that implement graph types whose names begin with 'Alt'.\n       --graph={" + CFGGraphType.help(0, 70, "                ".length()) + "} :\n                show the specified type of graph.\n                Defaults to " + "BriefUnitGraph" + ".\n       --ir={" + CFGIntermediateRep.help(0, 70, "                ".length()) + "} :\n                create the CFG from the specified intermediate\n                representation. Defaults to " + "jimple" + ".\n       --brief :\n                label nodes with the unit or block index,\n                instead of the text of their statements.\n       --multipages :\n                produce dot file output for multiple 8.5x11\" pages.\n                By default, a single page is produced.\n       --help :\n                print this message.\n\n   Particularly relevant soot options (see \"soot --help\" for details):\n       --soot-class-path PATH\n       --show-exception-dests\n       --throw-analysis {pedantic|unit}\n       --omit-excepting-unit-edges\n       --trim-cfgs\n");
   }

   private String[] parse_options(String[] args) {
      List<String> sootArgs = new ArrayList(args.length);
      int i = 0;

      for(int n = args.length; i < n; ++i) {
         if (!args[i].equals("--alt-classpath") && !args[i].equals("--alt-class-path")) {
            if (args[i].startsWith("--graph=")) {
               sootArgs.add("-p");
               sootArgs.add("jtp.printcfg");
               sootArgs.add("graph-type:" + args[i].substring("--graph=".length()));
            } else if (args[i].startsWith("--ir=")) {
               sootArgs.add("-p");
               sootArgs.add("jtp.printcfg");
               sootArgs.add("ir:" + args[i].substring("--ir=".length()));
            } else if (args[i].equals("--brief")) {
               sootArgs.add("-p");
               sootArgs.add("jtp.printcfg");
               sootArgs.add("brief:true");
            } else if (args[i].equals("--multipages")) {
               sootArgs.add("-p");
               sootArgs.add("jtp.printcfg");
               sootArgs.add("multipages:true");
            } else {
               if (args[i].equals("--help")) {
                  return new String[0];
               }

               if (!args[i].equals("--soot-class-path") && !args[i].equals("-soot-class-path") && !args[i].equals("--soot-classpath") && !args[i].equals("-soot-classpath") && !args[i].equals("--process-dir") && !args[i].equals("-process-dir") && !args[i].equals("--android-jars") && !args[i].equals("-android-jars") && !args[i].equals("--force-android-jar") && !args[i].equals("-force-android-jar")) {
                  if (!args[i].equals("-p") && !args[i].equals("--phase-option") && !args[i].equals("-phase-option")) {
                     int smpos = args[i].indexOf(58);
                     if (smpos == -1) {
                        sootArgs.add(args[i]);
                     } else {
                        String clsname = args[i].substring(0, smpos);
                        sootArgs.add(clsname);
                        String methname = args[i].substring(smpos + 1);
                        if (this.methodsToPrint == null) {
                           this.methodsToPrint = new HashMap();
                        }

                        this.methodsToPrint.put(methname, clsname);
                     }
                  } else {
                     sootArgs.add(args[i]);
                     ++i;
                     sootArgs.add(args[i]);
                     ++i;
                     sootArgs.add(args[i]);
                  }
               } else {
                  sootArgs.add(args[i]);
                  ++i;
                  sootArgs.add(args[i]);
               }
            }
         } else {
            sootArgs.add("-p");
            sootArgs.add("jtp.printcfg");
            StringBuilder var10001 = (new StringBuilder()).append("alt-class-path:");
            ++i;
            sootArgs.add(var10001.append(args[i]).toString());
         }
      }

      String[] sootArgsArray = new String[sootArgs.size()];
      return (String[])((String[])sootArgs.toArray(sootArgsArray));
   }

   private void initialize(Map<String, String> options) {
      if (this.drawer == null) {
         this.drawer = new CFGToDotGraph();
         this.drawer.setBriefLabels(PhaseOptions.getBoolean(options, "brief"));
         this.drawer.setOnePage(!PhaseOptions.getBoolean(options, "multipages"));
         this.drawer.setUnexceptionalControlFlowAttr("color", "black");
         this.drawer.setExceptionalControlFlowAttr("color", "red");
         this.drawer.setExceptionEdgeAttr("color", "lightgray");
         this.drawer.setShowExceptions(Options.v().show_exception_dests());
         this.ir = CFGIntermediateRep.getIR(PhaseOptions.getString(options, "ir"));
         this.graphtype = CFGGraphType.getGraphType(PhaseOptions.getString(options, "graph-type"));
         AltClassLoader.v().setAltClassPath(PhaseOptions.getString(options, "alt-class-path"));
         AltClassLoader.v().setAltClasses(new String[]{"soot.toolkits.graph.ArrayRefBlockGraph", "soot.toolkits.graph.Block", "soot.toolkits.graph.Block$AllMapTo", "soot.toolkits.graph.BlockGraph", "soot.toolkits.graph.BriefBlockGraph", "soot.toolkits.graph.BriefUnitGraph", "soot.toolkits.graph.CompleteBlockGraph", "soot.toolkits.graph.CompleteUnitGraph", "soot.toolkits.graph.TrapUnitGraph", "soot.toolkits.graph.UnitGraph", "soot.toolkits.graph.ZonedBlockGraph"});
      }

   }

   protected void print_cfg(Body body) {
      DirectedGraph<Unit> graph = this.graphtype.buildGraph(body);
      DotGraph canvas = this.graphtype.drawGraph(this.drawer, graph, body);
      String methodname = body.getMethod().getSubSignature();
      String classname = body.getMethod().getDeclaringClass().getName().replaceAll("\\$", "\\.");
      String filename = SourceLocator.v().getOutputDir();
      if (filename.length() > 0) {
         filename = filename + File.separator;
      }

      filename = filename + classname + " " + methodname.replace(File.separatorChar, '.') + ".dot";
      logger.debug("Generate dot file in " + filename);
      canvas.plot(filename);
   }
}
