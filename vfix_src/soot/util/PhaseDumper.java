package soot.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Printer;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.SourceLocator;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalGraph;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

public class PhaseDumper {
   private static final Logger logger = LoggerFactory.getLogger(PhaseDumper.class);
   private List bodyDumpingPhases = null;
   private List cfgDumpingPhases = null;
   private final PhaseDumper.PhaseStack phaseStack = new PhaseDumper.PhaseStack();
   static final String allWildcard = "ALL";
   private boolean alreadyDumping = false;

   public PhaseDumper(Singletons.Global g) {
      if (!Options.v().dump_body().isEmpty()) {
         this.bodyDumpingPhases = Options.v().dump_body();
      }

      if (!Options.v().dump_cfg().isEmpty()) {
         this.cfgDumpingPhases = Options.v().dump_cfg();
      }

   }

   public static PhaseDumper v() {
      return G.v().soot_util_PhaseDumper();
   }

   private boolean isBodyDumpingPhase(String phaseName) {
      return this.bodyDumpingPhases != null && (this.bodyDumpingPhases.contains(phaseName) || this.bodyDumpingPhases.contains("ALL"));
   }

   private boolean isCFGDumpingPhase(String phaseName) {
      if (this.cfgDumpingPhases == null) {
         return false;
      } else if (this.cfgDumpingPhases.contains("ALL")) {
         return true;
      } else {
         while(!this.cfgDumpingPhases.contains(phaseName)) {
            int lastDot = phaseName.lastIndexOf(46);
            if (lastDot < 0) {
               return false;
            }

            phaseName = phaseName.substring(0, lastDot);
         }

         return true;
      }
   }

   private static File makeDirectoryIfMissing(Body b) throws IOException {
      StringBuffer buf = new StringBuffer(SourceLocator.v().getOutputDir());
      buf.append(File.separatorChar);
      String className = b.getMethod().getDeclaringClass().getName();
      buf.append(className);
      buf.append(File.separatorChar);
      buf.append(b.getMethod().getSubSignature().replace('<', '[').replace('>', ']'));
      File dir = new File(buf.toString());
      if (dir.exists()) {
         if (!dir.isDirectory()) {
            throw new IOException(dir.getPath() + " exists but is not a directory.");
         }
      } else if (!dir.mkdirs()) {
         throw new IOException("Unable to mkdirs " + dir.getPath());
      }

      return dir;
   }

   private static PrintWriter openBodyFile(Body b, String baseName) throws IOException {
      File dir = makeDirectoryIfMissing(b);
      String filePath = dir.toString() + File.separatorChar + baseName;
      return new PrintWriter(new FileOutputStream(filePath));
   }

   private static String nextGraphFileName(Body b, String baseName) throws IOException {
      File dir = makeDirectoryIfMissing(b);
      String prefix = dir.toString() + File.separatorChar + baseName;
      File file = null;
      int fileNumber = 0;

      do {
         file = new File(prefix + fileNumber + ".dot");
         ++fileNumber;
      } while(file.exists());

      return file.toString();
   }

   private static void deleteOldGraphFiles(Body b, final String phaseName) {
      try {
         File dir = makeDirectoryIfMissing(b);
         File[] toDelete = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
               return name.startsWith(phaseName) && name.endsWith(".dot");
            }
         });
         if (toDelete != null) {
            File[] var4 = toDelete;
            int var5 = toDelete.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               File element = var4[var6];
               element.delete();
            }
         }
      } catch (IOException var8) {
         logger.debug("PhaseDumper.dumpBody() caught: " + var8.toString());
         logger.error((String)var8.getMessage(), (Throwable)var8);
      }

   }

   public void dumpBody(Body b, String baseName) {
      try {
         this.alreadyDumping = true;
         PrintWriter out = openBodyFile(b, baseName);
         Printer.v().setOption(1);
         Printer.v().printTo(b, out);
         out.close();
      } catch (IOException var7) {
         logger.debug("PhaseDumper.dumpBody() caught: " + var7.toString());
         logger.error((String)var7.getMessage(), (Throwable)var7);
      } finally {
         this.alreadyDumping = false;
      }

   }

   private void dumpAllBodies(String baseName, boolean deleteGraphFiles) {
      List<SootClass> classes = Scene.v().getClasses(3);
      Iterator var4 = classes.iterator();

      while(var4.hasNext()) {
         SootClass cls = (SootClass)var4.next();
         Iterator m = cls.getMethods().iterator();

         while(m.hasNext()) {
            SootMethod method = (SootMethod)m.next();
            if (method.hasActiveBody()) {
               Body body = method.getActiveBody();
               if (deleteGraphFiles) {
                  deleteOldGraphFiles(body, baseName);
               }

               this.dumpBody(body, baseName);
            }
         }
      }

   }

   public void dumpBefore(Body b, String phaseName) {
      this.phaseStack.push(phaseName);
      if (this.isBodyDumpingPhase(phaseName)) {
         deleteOldGraphFiles(b, phaseName);
         this.dumpBody(b, phaseName + ".in");
      }

   }

   public void dumpAfter(Body b, String phaseName) {
      String poppedPhaseName = this.phaseStack.pop();
      if (poppedPhaseName != phaseName) {
         throw new IllegalArgumentException("dumpAfter(" + phaseName + ") when poppedPhaseName == " + poppedPhaseName);
      } else {
         if (this.isBodyDumpingPhase(phaseName)) {
            this.dumpBody(b, phaseName + ".out");
         }

      }
   }

   public void dumpBefore(String phaseName) {
      this.phaseStack.push(phaseName);
      if (this.isBodyDumpingPhase(phaseName)) {
         this.dumpAllBodies(phaseName + ".in", true);
      }

   }

   public void dumpAfter(String phaseName) {
      String poppedPhaseName = this.phaseStack.pop();
      if (poppedPhaseName != phaseName) {
         throw new IllegalArgumentException("dumpAfter(" + phaseName + ") when poppedPhaseName == " + poppedPhaseName);
      } else {
         if (this.isBodyDumpingPhase(phaseName)) {
            this.dumpAllBodies(phaseName + ".out", false);
         }

      }
   }

   public void dumpGraph(DirectedGraph g, Body b) {
      if (!this.alreadyDumping) {
         try {
            this.alreadyDumping = true;
            String phaseName = this.phaseStack.currentPhase();
            if (this.isCFGDumpingPhase(phaseName)) {
               try {
                  String outputFile = nextGraphFileName(b, phaseName + "-" + this.getClassIdent(g) + "-");
                  DotGraph dotGraph = (new CFGToDotGraph()).drawCFG(g, b);
                  dotGraph.plot(outputFile);
               } catch (IOException var9) {
                  logger.debug("PhaseDumper.dumpBody() caught: " + var9.toString());
                  logger.error((String)var9.getMessage(), (Throwable)var9);
               }
            }
         } finally {
            this.alreadyDumping = false;
         }

      }
   }

   public void dumpGraph(ExceptionalGraph g) {
      if (!this.alreadyDumping) {
         try {
            this.alreadyDumping = true;
            String phaseName = this.phaseStack.currentPhase();
            if (this.isCFGDumpingPhase(phaseName)) {
               try {
                  String outputFile = nextGraphFileName(g.getBody(), phaseName + "-" + this.getClassIdent(g) + "-");
                  CFGToDotGraph drawer = new CFGToDotGraph();
                  drawer.setShowExceptions(Options.v().show_exception_dests());
                  DotGraph dotGraph = drawer.drawCFG(g);
                  dotGraph.plot(outputFile);
               } catch (IOException var9) {
                  logger.debug("PhaseDumper.dumpBody() caught: " + var9.toString());
                  logger.error((String)var9.getMessage(), (Throwable)var9);
               }
            }
         } finally {
            this.alreadyDumping = false;
         }

      }
   }

   private String getClassIdent(Object obj) {
      String qualifiedName = obj.getClass().getName();
      int lastDotIndex = qualifiedName.lastIndexOf(46);
      return qualifiedName.substring(lastDotIndex + 1);
   }

   public void printCurrentStackTrace() {
      try {
         throw new IOException("FAKE");
      } catch (IOException var2) {
         logger.error((String)var2.getMessage(), (Throwable)var2);
      }
   }

   private class PhaseStack extends ArrayList {
      private static final int initialCapacity = 4;
      static final String EMPTY_STACK_PHASE_NAME = "NOPHASE";

      PhaseStack() {
         super(4);
      }

      boolean empty() {
         return this.size() == 0;
      }

      String currentPhase() {
         return this.size() <= 0 ? "NOPHASE" : (String)this.get(this.size() - 1);
      }

      String pop() {
         return (String)this.remove(this.size() - 1);
      }

      String push(String phaseName) {
         this.add(phaseName);
         return phaseName;
      }
   }
}
