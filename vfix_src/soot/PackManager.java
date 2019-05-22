package soot;

import heros.solver.CountingThreadPoolExecutor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.baf.Baf;
import soot.baf.BafASMBackend;
import soot.baf.BafBody;
import soot.baf.JasminClass;
import soot.baf.toolkits.base.LoadStoreOptimizer;
import soot.baf.toolkits.base.PeepholeOptimizer;
import soot.baf.toolkits.base.StoreChainOptimizer;
import soot.coffi.CFG;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.DavaBuildFile;
import soot.dava.DavaPrinter;
import soot.dava.DavaStaticBlockCleaner;
import soot.dava.toolkits.base.AST.interProcedural.InterProceduralAnalyses;
import soot.dava.toolkits.base.AST.transformations.RemoveEmptyBodyDefaultConstructor;
import soot.dava.toolkits.base.AST.transformations.VoidReturnRemover;
import soot.dava.toolkits.base.misc.PackageNamer;
import soot.dava.toolkits.base.misc.ThrowFinder;
import soot.grimp.Grimp;
import soot.grimp.toolkits.base.ConstructorFolder;
import soot.jimple.JimpleBody;
import soot.jimple.paddle.PaddleHook;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.spark.fieldrw.FieldTagAggregator;
import soot.jimple.spark.fieldrw.FieldTagger;
import soot.jimple.toolkits.annotation.AvailExprTagger;
import soot.jimple.toolkits.annotation.DominatorsTagger;
import soot.jimple.toolkits.annotation.LineNumberAdder;
import soot.jimple.toolkits.annotation.arraycheck.ArrayBoundsChecker;
import soot.jimple.toolkits.annotation.arraycheck.RectangularArrayFinder;
import soot.jimple.toolkits.annotation.callgraph.CallGraphGrapher;
import soot.jimple.toolkits.annotation.callgraph.CallGraphTagger;
import soot.jimple.toolkits.annotation.defs.ReachingDefsTagger;
import soot.jimple.toolkits.annotation.fields.UnreachableFieldsTagger;
import soot.jimple.toolkits.annotation.liveness.LiveVarsTagger;
import soot.jimple.toolkits.annotation.logic.LoopInvariantFinder;
import soot.jimple.toolkits.annotation.methods.UnreachableMethodsTagger;
import soot.jimple.toolkits.annotation.nullcheck.NullCheckEliminator;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerChecker;
import soot.jimple.toolkits.annotation.nullcheck.NullPointerColorer;
import soot.jimple.toolkits.annotation.parity.ParityTagger;
import soot.jimple.toolkits.annotation.profiling.ProfilingGenerator;
import soot.jimple.toolkits.annotation.purity.PurityAnalysis;
import soot.jimple.toolkits.annotation.qualifiers.TightestQualifiersTagger;
import soot.jimple.toolkits.annotation.tags.ArrayNullTagAggregator;
import soot.jimple.toolkits.base.Aggregator;
import soot.jimple.toolkits.base.RenameDuplicatedClasses;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraphPack;
import soot.jimple.toolkits.callgraph.UnreachableMethodTransformer;
import soot.jimple.toolkits.invoke.StaticInliner;
import soot.jimple.toolkits.invoke.StaticMethodBinder;
import soot.jimple.toolkits.pointer.CastCheckEliminatorDumper;
import soot.jimple.toolkits.pointer.DependenceTagAggregator;
import soot.jimple.toolkits.pointer.ParameterAliasTagger;
import soot.jimple.toolkits.pointer.SideEffectTagger;
import soot.jimple.toolkits.reflection.ConstantInvokeMethodBaseTransformer;
import soot.jimple.toolkits.scalar.CommonSubexpressionEliminator;
import soot.jimple.toolkits.scalar.ConditionalBranchFolder;
import soot.jimple.toolkits.scalar.ConstantPropagatorAndFolder;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.EmptySwitchEliminator;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.jimple.toolkits.scalar.UnconditionalBranchFolder;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
import soot.jimple.toolkits.scalar.pre.BusyCodeMotion;
import soot.jimple.toolkits.scalar.pre.LazyCodeMotion;
import soot.jimple.toolkits.thread.mhp.MhpTransformer;
import soot.jimple.toolkits.thread.synchronization.LockAllocator;
import soot.jimple.toolkits.typing.TypeAssigner;
import soot.options.Options;
import soot.shimple.Shimple;
import soot.shimple.ShimpleBody;
import soot.shimple.ShimpleTransformer;
import soot.shimple.toolkits.scalar.SConstantPropagatorAndFolder;
import soot.sootify.TemplatePrinter;
import soot.tagkit.InnerClassTagAggregator;
import soot.tagkit.LineNumberTagAggregator;
import soot.toDex.DexPrinter;
import soot.toolkits.exceptions.DuplicateCatchAllTrapRemover;
import soot.toolkits.exceptions.TrapTightener;
import soot.toolkits.graph.interaction.InteractionHandler;
import soot.toolkits.scalar.ConstantInitializerToTagTransformer;
import soot.toolkits.scalar.ConstantValueToInitializerTransformer;
import soot.toolkits.scalar.LocalPacker;
import soot.toolkits.scalar.LocalSplitter;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.Chain;
import soot.util.EscapedWriter;
import soot.util.JasminOutputStream;
import soot.util.PhaseDumper;
import soot.xml.TagCollector;
import soot.xml.XMLPrinter;

public class PackManager {
   private static final Logger logger = LoggerFactory.getLogger(PackManager.class);
   public static boolean DEBUG = false;
   private final Map<String, Pack> packNameToPack = new HashMap();
   private final List<Pack> packList = new LinkedList();
   private boolean onlyStandardPacks = false;
   private JarOutputStream jarFile = null;
   protected DexPrinter dexPrinter = null;

   public PackManager(Singletons.Global g) {
      PhaseOptions.v().setPackManager(this);
      this.init();
   }

   public static PackManager v() {
      return G.v().soot_PackManager();
   }

   public boolean onlyStandardPacks() {
      return this.onlyStandardPacks;
   }

   void notifyAddPack() {
      this.onlyStandardPacks = false;
   }

   private void init() {
      JimpleBodyPack p;
      this.addPack(p = new JimpleBodyPack());
      p.add(new Transform("jb.tt", TrapTightener.v()));
      p.add(new Transform("jb.dtr", DuplicateCatchAllTrapRemover.v()));
      p.add(new Transform("jb.ese", EmptySwitchEliminator.v()));
      p.add(new Transform("jb.ls", LocalSplitter.v()));
      p.add(new Transform("jb.a", Aggregator.v()));
      p.add(new Transform("jb.ule", UnusedLocalEliminator.v()));
      p.add(new Transform("jb.tr", TypeAssigner.v()));
      p.add(new Transform("jb.ulp", LocalPacker.v()));
      p.add(new Transform("jb.lns", LocalNameStandardizer.v()));
      p.add(new Transform("jb.cp", CopyPropagator.v()));
      p.add(new Transform("jb.dae", DeadAssignmentEliminator.v()));
      p.add(new Transform("jb.cp-ule", UnusedLocalEliminator.v()));
      p.add(new Transform("jb.lp", LocalPacker.v()));
      p.add(new Transform("jb.ne", NopEliminator.v()));
      p.add(new Transform("jb.uce", UnreachableCodeEliminator.v()));
      JavaToJimpleBodyPack p;
      this.addPack(p = new JavaToJimpleBodyPack());
      p.add(new Transform("jj.ls", LocalSplitter.v()));
      p.add(new Transform("jj.a", Aggregator.v()));
      p.add(new Transform("jj.ule", UnusedLocalEliminator.v()));
      p.add(new Transform("jj.ne", NopEliminator.v()));
      p.add(new Transform("jj.tr", TypeAssigner.v()));
      p.add(new Transform("jj.ulp", LocalPacker.v()));
      p.add(new Transform("jj.lns", LocalNameStandardizer.v()));
      p.add(new Transform("jj.cp", CopyPropagator.v()));
      p.add(new Transform("jj.dae", DeadAssignmentEliminator.v()));
      p.add(new Transform("jj.cp-ule", UnusedLocalEliminator.v()));
      p.add(new Transform("jj.lp", LocalPacker.v()));
      p.add(new Transform("jj.uce", UnreachableCodeEliminator.v()));
      ScenePack p;
      this.addPack(p = new ScenePack("wjpp"));
      p.add(new Transform("wjpp.cimbt", ConstantInvokeMethodBaseTransformer.v()));
      this.addPack(new ScenePack("wspp"));
      CallGraphPack p;
      this.addPack(p = new CallGraphPack("cg"));
      p.add(new Transform("cg.cha", CHATransformer.v()));
      p.add(new Transform("cg.spark", SparkTransformer.v()));
      p.add(new Transform("cg.paddle", PaddleHook.v()));
      this.addPack(new ScenePack("wstp"));
      this.addPack(new ScenePack("wsop"));
      this.addPack(p = new ScenePack("wjtp"));
      p.add(new Transform("wjtp.mhp", MhpTransformer.v()));
      p.add(new Transform("wjtp.tn", LockAllocator.v()));
      p.add(new Transform("wjtp.rdc", RenameDuplicatedClasses.v()));
      this.addPack(p = new ScenePack("wjop"));
      p.add(new Transform("wjop.smb", StaticMethodBinder.v()));
      p.add(new Transform("wjop.si", StaticInliner.v()));
      this.addPack(p = new ScenePack("wjap"));
      p.add(new Transform("wjap.ra", RectangularArrayFinder.v()));
      p.add(new Transform("wjap.umt", UnreachableMethodsTagger.v()));
      p.add(new Transform("wjap.uft", UnreachableFieldsTagger.v()));
      p.add(new Transform("wjap.tqt", TightestQualifiersTagger.v()));
      p.add(new Transform("wjap.cgg", CallGraphGrapher.v()));
      p.add(new Transform("wjap.purity", PurityAnalysis.v()));
      this.addPack(new BodyPack("shimple"));
      this.addPack(new BodyPack("stp"));
      BodyPack p;
      this.addPack(p = new BodyPack("sop"));
      p.add(new Transform("sop.cpf", SConstantPropagatorAndFolder.v()));
      this.addPack(new BodyPack("jtp"));
      this.addPack(p = new BodyPack("jop"));
      p.add(new Transform("jop.cse", CommonSubexpressionEliminator.v()));
      p.add(new Transform("jop.bcm", BusyCodeMotion.v()));
      p.add(new Transform("jop.lcm", LazyCodeMotion.v()));
      p.add(new Transform("jop.cp", CopyPropagator.v()));
      p.add(new Transform("jop.cpf", ConstantPropagatorAndFolder.v()));
      p.add(new Transform("jop.cbf", ConditionalBranchFolder.v()));
      p.add(new Transform("jop.dae", DeadAssignmentEliminator.v()));
      p.add(new Transform("jop.nce", new NullCheckEliminator()));
      p.add(new Transform("jop.uce1", UnreachableCodeEliminator.v()));
      p.add(new Transform("jop.ubf1", UnconditionalBranchFolder.v()));
      p.add(new Transform("jop.uce2", UnreachableCodeEliminator.v()));
      p.add(new Transform("jop.ubf2", UnconditionalBranchFolder.v()));
      p.add(new Transform("jop.ule", UnusedLocalEliminator.v()));
      this.addPack(p = new BodyPack("jap"));
      p.add(new Transform("jap.npc", NullPointerChecker.v()));
      p.add(new Transform("jap.npcolorer", NullPointerColorer.v()));
      p.add(new Transform("jap.abc", ArrayBoundsChecker.v()));
      p.add(new Transform("jap.profiling", ProfilingGenerator.v()));
      p.add(new Transform("jap.sea", SideEffectTagger.v()));
      p.add(new Transform("jap.fieldrw", FieldTagger.v()));
      p.add(new Transform("jap.cgtagger", CallGraphTagger.v()));
      p.add(new Transform("jap.parity", ParityTagger.v()));
      p.add(new Transform("jap.pat", ParameterAliasTagger.v()));
      p.add(new Transform("jap.rdtagger", ReachingDefsTagger.v()));
      p.add(new Transform("jap.lvtagger", LiveVarsTagger.v()));
      p.add(new Transform("jap.che", CastCheckEliminatorDumper.v()));
      p.add(new Transform("jap.umt", new UnreachableMethodTransformer()));
      p.add(new Transform("jap.lit", LoopInvariantFinder.v()));
      p.add(new Transform("jap.aet", AvailExprTagger.v()));
      p.add(new Transform("jap.dmt", DominatorsTagger.v()));
      this.addPack(p = new BodyPack("gb"));
      p.add(new Transform("gb.a1", Aggregator.v()));
      p.add(new Transform("gb.cf", ConstructorFolder.v()));
      p.add(new Transform("gb.a2", Aggregator.v()));
      p.add(new Transform("gb.ule", UnusedLocalEliminator.v()));
      this.addPack(new BodyPack("gop"));
      this.addPack(p = new BodyPack("bb"));
      p.add(new Transform("bb.lso", LoadStoreOptimizer.v()));
      p.add(new Transform("bb.pho", PeepholeOptimizer.v()));
      p.add(new Transform("bb.ule", UnusedLocalEliminator.v()));
      p.add(new Transform("bb.lp", LocalPacker.v()));
      p.add(new Transform("bb.sco", StoreChainOptimizer.v()));
      this.addPack(new BodyPack("bop"));
      this.addPack(p = new BodyPack("tag"));
      p.add(new Transform("tag.ln", LineNumberTagAggregator.v()));
      p.add(new Transform("tag.an", ArrayNullTagAggregator.v()));
      p.add(new Transform("tag.dep", DependenceTagAggregator.v()));
      p.add(new Transform("tag.fieldrw", FieldTagAggregator.v()));
      this.addPack(p = new BodyPack("db"));
      p.add(new Transform("db.transformations", (Transformer)null));
      p.add(new Transform("db.renamer", (Transformer)null));
      p.add(new Transform("db.deobfuscate", (Transformer)null));
      p.add(new Transform("db.force-recompile", (Transformer)null));
      this.onlyStandardPacks = true;
   }

   private void addPack(Pack p) {
      if (this.packNameToPack.containsKey(p.getPhaseName())) {
         throw new RuntimeException("Duplicate pack " + p.getPhaseName());
      } else {
         this.packNameToPack.put(p.getPhaseName(), p);
         this.packList.add(p);
      }
   }

   public boolean hasPack(String phaseName) {
      return this.getPhase(phaseName) != null;
   }

   public Pack getPack(String phaseName) {
      Pack p = (Pack)this.packNameToPack.get(phaseName);
      return p;
   }

   public boolean hasPhase(String phaseName) {
      return this.getPhase(phaseName) != null;
   }

   public HasPhaseOptions getPhase(String phaseName) {
      int index = phaseName.indexOf(".");
      if (index < 0) {
         return this.getPack(phaseName);
      } else {
         String packName = phaseName.substring(0, index);
         return !this.hasPack(packName) ? null : this.getPack(packName).get(phaseName);
      }
   }

   public Transform getTransform(String phaseName) {
      return (Transform)this.getPhase(phaseName);
   }

   public Collection<Pack> allPacks() {
      return Collections.unmodifiableList(this.packList);
   }

   public void runPacks() {
      if (Options.v().oaat()) {
         this.runPacksForOneClassAtATime();
      } else {
         this.runPacksNormally();
      }

   }

   private void runPacksForOneClassAtATime() {
      if (Options.v().src_prec() == 1 && Options.v().keep_line_number()) {
         LineNumberAdder lineNumAdder = LineNumberAdder.v();
         lineNumAdder.internalTransform("", (Map)null);
      }

      this.setupJAR();
      Iterator var11 = Options.v().process_dir().iterator();

      while(var11.hasNext()) {
         String path = (String)var11.next();
         Iterator var3 = SourceLocator.v().getClassesUnder(path).iterator();

         String cl;
         SootClass clazz;
         while(var3.hasNext()) {
            cl = (String)var3.next();
            clazz = Scene.v().forceResolve(cl, 2);
            clazz.setApplicationClass();
         }

         var3 = SourceLocator.v().getClassesUnder(path).iterator();

         while(var3.hasNext()) {
            cl = (String)var3.next();
            clazz = null;
            ClassSource source = SourceLocator.v().getClassSource(cl);

            try {
               if (source == null) {
                  throw new RuntimeException("Could not locate class source");
               }

               clazz = Scene.v().getSootClass(cl);
               clazz.setResolvingLevel(3);
               source.resolve(clazz);
            } finally {
               if (source != null) {
                  source.close();
               }

            }

            Iterator var7 = Scene.v().getApplicationClasses().iterator();

            while(var7.hasNext()) {
               SootClass sc = (SootClass)var7.next();
               if (Options.v().validate()) {
                  sc.validate();
               }

               if (!sc.isPhantom) {
                  ConstantInitializerToTagTransformer.v().transformClass(sc, true);
               }
            }

            this.runBodyPacks(clazz);
            this.writeClass(clazz);
            if (!Options.v().no_writeout_body_releasing()) {
               this.releaseBodies(clazz);
            }
         }
      }

      this.tearDownJAR();
      this.handleInnerClasses();
   }

   private void runPacksNormally() {
      if (Options.v().src_prec() == 1 && Options.v().keep_line_number()) {
         LineNumberAdder lineNumAdder = LineNumberAdder.v();
         lineNumAdder.internalTransform("", (Map)null);
      }

      if (Options.v().whole_program() || Options.v().whole_shimple()) {
         this.runWholeProgramPacks();
      }

      this.retrieveAllBodies();
      Iterator var3 = Scene.v().getApplicationClasses().iterator();

      while(var3.hasNext()) {
         SootClass sc = (SootClass)var3.next();
         if (Options.v().validate()) {
            sc.validate();
         }

         if (!sc.isPhantom) {
            ConstantInitializerToTagTransformer.v().transformClass(sc, true);
         }
      }

      if (soot.jbco.Main.metrics) {
         this.coffiMetrics();
         System.exit(0);
      }

      this.preProcessDAVA();
      if (Options.v().interactive_mode()) {
         if (InteractionHandler.v().getInteractionListener() == null) {
            logger.debug("Cannot run in interactive mode. No listeners available. Continuing in regular mode.");
            Options.v().set_interactive_mode(false);
         } else {
            logger.debug("Running in interactive mode.");
         }
      }

      this.runBodyPacks();
      this.handleInnerClasses();
   }

   public void coffiMetrics() {
      int tV = 0;
      int tE = 0;
      int hM = 0;
      double aM = 0.0D;
      HashMap<SootMethod, int[]> hashVem = CFG.methodsToVEM;
      Iterator it = hashVem.keySet().iterator();

      while(it.hasNext()) {
         int[] vem = (int[])hashVem.get(it.next());
         tV += vem[0];
         tE += vem[1];
         aM += (double)vem[2];
         if (vem[2] > hM) {
            hM = vem[2];
         }
      }

      if (hashVem.size() > 0) {
         aM /= (double)hashVem.size();
      }

      logger.debug("Vertices, Edges, Avg Degree, Highest Deg:    " + tV + "  " + tE + "  " + aM + "  " + hM);
   }

   public void runBodyPacks() {
      this.runBodyPacks(this.reachableClasses());
   }

   public JarOutputStream getJarFile() {
      return this.jarFile;
   }

   public void writeOutput() {
      this.setupJAR();
      if (Options.v().verbose()) {
         PhaseDumper.v().dumpBefore("output");
      }

      if (Options.v().output_format() == 15) {
         this.postProcessDAVA();
      } else if (Options.v().output_format() != 10 && Options.v().output_format() != 11) {
         this.writeOutput(this.reachableClasses());
         this.tearDownJAR();
      } else {
         this.writeDexOutput();
      }

      this.postProcessXML(this.reachableClasses());
      if (!Options.v().no_writeout_body_releasing()) {
         this.releaseBodies(this.reachableClasses());
      }

      if (Options.v().verbose()) {
         PhaseDumper.v().dumpAfter("output");
      }

   }

   protected void writeDexOutput() {
      this.dexPrinter = new DexPrinter();
      this.writeOutput(this.reachableClasses());
      this.dexPrinter.print();
      this.dexPrinter = null;
   }

   private void setupJAR() {
      if (Options.v().output_jar()) {
         String outFileName = SourceLocator.v().getOutputJarName();

         try {
            this.jarFile = new JarOutputStream(new FileOutputStream(outFileName));
         } catch (IOException var3) {
            throw new CompilationDeathException("Cannot open output Jar file " + outFileName);
         }
      } else {
         this.jarFile = null;
      }

   }

   private void runWholeProgramPacks() {
      if (Options.v().whole_shimple()) {
         ShimpleTransformer.v().transform();
         this.getPack("wspp").apply();
         this.getPack("cg").apply();
         this.getPack("wstp").apply();
         this.getPack("wsop").apply();
      } else {
         this.getPack("wjpp").apply();
         this.getPack("cg").apply();
         this.getPack("wjtp").apply();
         this.getPack("wjop").apply();
         this.getPack("wjap").apply();
      }

      PaddleHook.v().finishPhases();
   }

   private void preProcessDAVA() {
      if (Options.v().output_format() == 15) {
         Map<String, String> options = PhaseOptions.v().getPhaseOptions("db");
         boolean isSourceJavac = PhaseOptions.getBoolean(options, "source-is-javac");
         if (!isSourceJavac) {
            if (DEBUG) {
               System.out.println("Source is not Javac hence invoking ThrowFinder");
            }

            ThrowFinder.v().find();
         } else if (DEBUG) {
            System.out.println("Source is javac hence we dont need to invoke ThrowFinder");
         }

         PackageNamer.v().fixNames();
      }

   }

   private void runBodyPacks(Iterator<SootClass> classes) {
      int threadNum = Runtime.getRuntime().availableProcessors();
      CountingThreadPoolExecutor executor = new CountingThreadPoolExecutor(threadNum, threadNum, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());

      while(classes.hasNext()) {
         final SootClass c = (SootClass)classes.next();
         executor.execute(new Runnable() {
            public void run() {
               PackManager.this.runBodyPacks(c);
            }
         });
      }

      try {
         executor.awaitCompletion();
         executor.shutdown();
      } catch (InterruptedException var5) {
         throw new RuntimeException("Could not wait for pack threads to finish: " + var5.getMessage(), var5);
      }

      if (executor.getException() != null) {
         if (executor.getException() instanceof RuntimeException) {
            throw (RuntimeException)executor.getException();
         } else {
            throw new RuntimeException(executor.getException());
         }
      }
   }

   private void handleInnerClasses() {
      InnerClassTagAggregator agg = InnerClassTagAggregator.v();
      agg.internalTransform("", (Map)null);
   }

   protected void writeOutput(Iterator<SootClass> classes) {
      int threadNum = Options.v().output_format() == 14 && this.jarFile == null ? Runtime.getRuntime().availableProcessors() : 1;
      CountingThreadPoolExecutor executor = new CountingThreadPoolExecutor(threadNum, threadNum, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());

      while(classes.hasNext()) {
         final SootClass c = (SootClass)classes.next();
         executor.execute(new Runnable() {
            public void run() {
               PackManager.this.writeClass(c);
            }
         });
      }

      try {
         executor.awaitCompletion();
         executor.shutdown();
      } catch (InterruptedException var5) {
         throw new RuntimeException("Could not wait for writer threads to finish: " + var5.getMessage(), var5);
      }

      if (executor.getException() != null) {
         if (executor.getException() instanceof RuntimeException) {
            throw (RuntimeException)executor.getException();
         } else {
            throw new RuntimeException(executor.getException());
         }
      }
   }

   private void tearDownJAR() {
      try {
         if (this.jarFile != null) {
            this.jarFile.close();
         }

      } catch (IOException var2) {
         throw new CompilationDeathException("Error closing output jar: " + var2);
      }
   }

   private void releaseBodies(Iterator<SootClass> classes) {
      while(classes.hasNext()) {
         this.releaseBodies((SootClass)classes.next());
      }

   }

   private Iterator<SootClass> reachableClasses() {
      return Scene.v().getApplicationClasses().snapshotIterator();
   }

   private void postProcessDAVA() {
      Chain<SootClass> appClasses = Scene.v().getApplicationClasses();
      Map<String, String> options = PhaseOptions.v().getPhaseOptions("db.transformations");
      boolean transformations = PhaseOptions.getBoolean(options, "enabled");
      Iterator var4 = appClasses.iterator();

      while(var4.hasNext()) {
         SootClass s = (SootClass)var4.next();
         String fileName = SourceLocator.v().getFileNameFor(s, Options.v().output_format());
         DavaStaticBlockCleaner.v().staticBlockInlining(s);
         VoidReturnRemover.cleanClass(s);
         RemoveEmptyBodyDefaultConstructor.checkAndRemoveDefault(s);
         logger.debug("Analyzing " + fileName + "... ");
         Iterator var7 = s.getMethods().iterator();

         while(var7.hasNext()) {
            SootMethod m = (SootMethod)var7.next();
            if (m.hasActiveBody()) {
               DavaBody body = (DavaBody)m.getActiveBody();
               if (transformations) {
                  body.analyzeAST();
               } else {
                  body.applyBugFixes();
               }
            }
         }
      }

      if (transformations) {
         InterProceduralAnalyses.applyInterProceduralAnalyses();
      }

      this.outputDava();
   }

   private void outputDava() {
      Chain<SootClass> appClasses = Scene.v().getApplicationClasses();
      String pathForBuild = null;
      ArrayList<String> decompiledClasses = new ArrayList();
      Iterator classIt = appClasses.iterator();

      PrintWriter writerOut;
      while(classIt.hasNext()) {
         SootClass s = (SootClass)classIt.next();
         OutputStream streamOut = null;
         writerOut = null;
         String fileName = SourceLocator.v().getFileNameFor(s, Options.v().output_format());
         decompiledClasses.add(fileName.substring(fileName.lastIndexOf(47) + 1));
         if (pathForBuild == null) {
            pathForBuild = fileName.substring(0, fileName.lastIndexOf(47) + 1);
         }

         if (Options.v().gzip()) {
            fileName = fileName + ".gz";
         }

         try {
            if (this.jarFile != null) {
               JarEntry entry = new JarEntry(fileName.replace('\\', '/'));
               this.jarFile.putNextEntry(entry);
               streamOut = this.jarFile;
            } else {
               streamOut = new FileOutputStream(fileName);
            }

            if (Options.v().gzip()) {
               streamOut = new GZIPOutputStream((OutputStream)streamOut);
            }

            writerOut = new PrintWriter(new OutputStreamWriter((OutputStream)streamOut));
         } catch (IOException var12) {
            throw new CompilationDeathException("Cannot output file " + fileName, var12);
         }

         logger.debug("Generating " + fileName + "... ");
         G.v().out.flush();
         DavaPrinter.v().printTo(s, writerOut);
         G.v().out.flush();

         try {
            writerOut.flush();
            if (this.jarFile == null) {
               ((OutputStream)streamOut).close();
            }
         } catch (IOException var11) {
            throw new CompilationDeathException("Cannot close output file " + fileName);
         }
      }

      if (pathForBuild != null) {
         if (pathForBuild.endsWith("src/")) {
            pathForBuild = pathForBuild.substring(0, pathForBuild.length() - 4);
         }

         String fileName = pathForBuild + "build.xml";

         try {
            OutputStream streamOut = new FileOutputStream(fileName);
            writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
            DavaBuildFile.generate(writerOut, decompiledClasses);
            writerOut.flush();
            streamOut.close();
         } catch (IOException var10) {
            throw new CompilationDeathException("Cannot output file " + fileName, var10);
         }
      }

   }

   private void runBodyPacks(SootClass c) {
      int format = Options.v().output_format();
      if (format == 15) {
         logger.debug((String)"Decompiling {}...", (Object)c.getName());
         G.v().SootMethodAddedByDava = false;
      } else {
         logger.debug((String)"Transforming {}...", (Object)c.getName());
      }

      boolean produceBaf = false;
      boolean produceGrimp = false;
      boolean produceDava = false;
      boolean produceJimple = true;
      boolean produceShimple = false;
      switch(format) {
      case 1:
      case 2:
      case 9:
      case 10:
      case 11:
      case 12:
      case 16:
         break;
      case 3:
      case 4:
         produceShimple = true;
         produceJimple = false;
         break;
      case 5:
      case 6:
         produceBaf = true;
         break;
      case 13:
      case 14:
      case 17:
         produceGrimp = Options.v().via_grimp();
         produceBaf = !produceGrimp;
         break;
      case 15:
         produceDava = true;
      case 7:
      case 8:
         produceGrimp = true;
         break;
      default:
         throw new RuntimeException();
      }

      TagCollector tc = new TagCollector();
      boolean wholeShimple = Options.v().whole_shimple();
      if (Options.v().via_shimple()) {
         produceShimple = true;
      }

      ArrayList<SootMethod> methodsCopy = new ArrayList(c.getMethods());
      Iterator var11 = methodsCopy.iterator();

      SootMethod m;
      while(var11.hasNext()) {
         m = (SootMethod)var11.next();
         if (DEBUG && m.getExceptions().size() != 0) {
            System.out.println("PackManager printing out jimple body exceptions for method " + m.toString() + " " + m.getExceptions().toString());
         }

         if (m.isConcrete()) {
            if (produceShimple || wholeShimple) {
               ShimpleBody sBody = null;
               Body body = m.retrieveActiveBody();
               if (body instanceof ShimpleBody) {
                  sBody = (ShimpleBody)body;
                  if (!sBody.isSSA()) {
                     sBody.rebuild();
                  }
               } else {
                  sBody = Shimple.v().newBody(body);
               }

               m.setActiveBody(sBody);
               v().getPack("stp").apply(sBody);
               v().getPack("sop").apply(sBody);
               if (produceJimple || wholeShimple && !produceShimple) {
                  m.setActiveBody(sBody.toJimpleBody());
               }
            }

            if (produceJimple) {
               Body body = m.retrieveActiveBody();
               CopyPropagator.v().transform(body);
               ConditionalBranchFolder.v().transform(body);
               UnreachableCodeEliminator.v().transform(body);
               DeadAssignmentEliminator.v().transform(body);
               UnusedLocalEliminator.v().transform(body);
               v().getPack("jtp").apply(body);
               if (Options.v().validate()) {
                  body.validate();
               }

               v().getPack("jop").apply(body);
               v().getPack("jap").apply(body);
               if (Options.v().xml_attributes() && Options.v().output_format() != 1) {
                  tc.collectBodyTags(body);
               }
            }

            if (produceGrimp) {
               m.setActiveBody(Grimp.v().newBody(m.getActiveBody(), "gb"));
               v().getPack("gop").apply(m.getActiveBody());
            } else if (produceBaf) {
               m.setActiveBody(this.convertJimpleBodyToBaf(m));
            }
         }
      }

      if (Options.v().xml_attributes() && Options.v().output_format() != 1) {
         this.processXMLForClass(c, tc);
      }

      if (produceDava) {
         var11 = c.getMethods().iterator();

         while(var11.hasNext()) {
            m = (SootMethod)var11.next();
            if (m.isConcrete()) {
               m.setActiveBody(Dava.v().newBody(m.getActiveBody()));
            }
         }

         if (G.v().SootMethodAddedByDava) {
            ArrayList<SootMethod> sootMethodsAdded = G.v().SootMethodsAdded;
            Iterator it = sootMethodsAdded.iterator();

            while(it.hasNext()) {
               c.addMethod((SootMethod)it.next());
            }

            G.v().SootMethodsAdded = new ArrayList();
            G.v().SootMethodAddedByDava = false;
         }
      }

   }

   public BafBody convertJimpleBodyToBaf(SootMethod m) {
      JimpleBody body = (JimpleBody)m.getActiveBody().clone();
      BafBody bafBody = Baf.v().newBody((Body)body);
      v().getPack("bop").apply(bafBody);
      v().getPack("tag").apply(bafBody);
      if (Options.v().validate()) {
         bafBody.validate();
      }

      return bafBody;
   }

   protected void writeClass(SootClass c) {
      if (Options.v().output_format() == 1 && !c.isPhantom) {
         ConstantValueToInitializerTransformer.v().transformClass(c);
      }

      int format = Options.v().output_format();
      if (format != 12) {
         if (format != 15) {
            if (format != 10 && format != 11) {
               OutputStream streamOut = null;
               PrintWriter writerOut = null;
               String fileName = SourceLocator.v().getFileNameFor(c, format);
               if (Options.v().gzip()) {
                  fileName = fileName + ".gz";
               }

               try {
                  if (this.jarFile != null) {
                     fileName = fileName.replace("\\", "/");
                     JarEntry entry = new JarEntry(fileName);
                     entry.setMethod(8);
                     this.jarFile.putNextEntry(entry);
                     streamOut = this.jarFile;
                  } else {
                     (new File(fileName)).getParentFile().mkdirs();
                     streamOut = new FileOutputStream(fileName);
                  }

                  if (Options.v().gzip()) {
                     streamOut = new GZIPOutputStream((OutputStream)streamOut);
                  }

                  if (format == 14 && Options.v().jasmin_backend()) {
                     streamOut = new JasminOutputStream((OutputStream)streamOut);
                  }

                  writerOut = new PrintWriter(new OutputStreamWriter((OutputStream)streamOut));
                  logger.debug("Writing to " + fileName);
               } catch (IOException var9) {
                  throw new CompilationDeathException("Cannot output file " + fileName, var9);
               }

               if (Options.v().xml_attributes()) {
                  Printer.v().setOption(16);
               }

               int java_version = Options.v().java_version();
               switch(format) {
               case 1:
               case 3:
               case 5:
               case 7:
                  writerOut = new PrintWriter(new EscapedWriter(new OutputStreamWriter((OutputStream)streamOut)));
                  Printer.v().printTo(c, writerOut);
                  break;
               case 2:
               case 4:
               case 6:
               case 8:
                  Printer.v().setOption(1);
                  Printer.v().printTo(c, writerOut);
                  break;
               case 9:
                  writerOut = new PrintWriter(new EscapedWriter(new OutputStreamWriter((OutputStream)streamOut)));
                  XMLPrinter.v().printJimpleStyleTo(c, writerOut);
                  break;
               case 10:
               case 11:
               case 12:
               case 15:
               default:
                  throw new RuntimeException();
               case 14:
                  if (!Options.v().jasmin_backend()) {
                     (new BafASMBackend(c, java_version)).generateClassFile((OutputStream)streamOut);
                     break;
                  }
               case 13:
                  if (c.containsBafBody()) {
                     (new JasminClass(c)).print(writerOut);
                  } else {
                     (new soot.jimple.JasminClass(c)).print(writerOut);
                  }
                  break;
               case 16:
                  writerOut = new PrintWriter(new OutputStreamWriter((OutputStream)streamOut));
                  TemplatePrinter.v().printTo(c, writerOut);
                  break;
               case 17:
                  (new BafASMBackend(c, java_version)).generateTextualRepresentation(writerOut);
               }

               try {
                  writerOut.flush();
                  if (this.jarFile == null) {
                     ((OutputStream)streamOut).close();
                     writerOut.close();
                  } else {
                     this.jarFile.closeEntry();
                  }

               } catch (IOException var8) {
                  throw new CompilationDeathException("Cannot close output file " + fileName);
               }
            } else {
               this.dexPrinter.add(c);
            }
         }
      }
   }

   private void postProcessXML(Iterator<SootClass> classes) {
      if (Options.v().xml_attributes()) {
         if (Options.v().output_format() == 1) {
            while(classes.hasNext()) {
               SootClass c = (SootClass)classes.next();
               this.processXMLForClass(c);
            }

         }
      }
   }

   private void processXMLForClass(SootClass c, TagCollector tc) {
      int ofmt = Options.v().output_format();
      int format = ofmt != 12 ? ofmt : 1;
      String fileName = SourceLocator.v().getFileNameFor(c, format);
      XMLAttributesPrinter xap = new XMLAttributesPrinter(fileName, SourceLocator.v().getOutputDir());
      xap.printAttrs(c, tc);
   }

   private void processXMLForClass(SootClass c) {
      int format = Options.v().output_format();
      String fileName = SourceLocator.v().getFileNameFor(c, format);
      XMLAttributesPrinter xap = new XMLAttributesPrinter(fileName, SourceLocator.v().getOutputDir());
      xap.printAttrs(c);
   }

   private void releaseBodies(SootClass cl) {
      Iterator methodIt = cl.methodIterator();

      while(methodIt.hasNext()) {
         SootMethod m = (SootMethod)methodIt.next();
         if (m.hasActiveBody()) {
            m.releaseActiveBody();
         }
      }

   }

   private void retrieveAllBodies() {
      int threadNum = Options.v().coffi() ? 1 : Runtime.getRuntime().availableProcessors();
      CountingThreadPoolExecutor executor = new CountingThreadPoolExecutor(threadNum, threadNum, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
      Iterator clIt = this.reachableClasses();

      while(clIt.hasNext()) {
         SootClass cl = (SootClass)clIt.next();
         Iterator methodIt = cl.getMethods().iterator();

         while(methodIt.hasNext()) {
            final SootMethod m = (SootMethod)methodIt.next();
            if (m.isConcrete()) {
               executor.execute(new Runnable() {
                  public void run() {
                     m.retrieveActiveBody();
                  }
               });
            }
         }
      }

      try {
         executor.awaitCompletion();
         executor.shutdown();
      } catch (InterruptedException var7) {
         throw new RuntimeException("Could not wait for loader threads to finish: " + var7.getMessage(), var7);
      }

      if (executor.getException() != null) {
         if (executor.getException() instanceof RuntimeException) {
            throw (RuntimeException)executor.getException();
         } else {
            throw new RuntimeException(executor.getException());
         }
      }
   }
}
