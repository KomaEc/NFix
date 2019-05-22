package polyglot.frontend;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.main.Options;
import polyglot.main.Report;
import polyglot.types.Context;
import polyglot.types.TypeSystem;
import polyglot.types.reflect.ClassFile;
import polyglot.util.CodeWriter;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.visit.DumpAst;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public abstract class AbstractExtensionInfo implements ExtensionInfo {
   protected Compiler compiler;
   private Options options;
   protected TypeSystem ts = null;
   protected NodeFactory nf = null;
   protected SourceLoader source_loader = null;
   protected TargetFactory target_factory = null;
   protected Stats stats;
   protected LinkedList worklist;
   protected Map jobs;
   protected static final Object COMPLETED_JOB = "COMPLETED JOB";
   protected Job currentJob;

   public Options getOptions() {
      if (this.options == null) {
         this.options = this.createOptions();
      }

      return this.options;
   }

   protected Options createOptions() {
      return new Options(this);
   }

   public Stats getStats() {
      if (this.stats == null) {
         this.stats = new Stats(this);
      }

      return this.stats;
   }

   public Compiler compiler() {
      return this.compiler;
   }

   public void initCompiler(Compiler compiler) {
      this.compiler = compiler;
      this.jobs = new HashMap();
      this.worklist = new LinkedList();
      compiler.addExtension(this);
      this.currentJob = null;
      this.typeSystem();
      this.nodeFactory();
      this.initTypeSystem();
   }

   protected abstract void initTypeSystem();

   public boolean runToCompletion() {
      boolean okay = true;

      while(okay && !this.worklist.isEmpty()) {
         SourceJob job = this.selectJobFromWorklist();
         if (Report.should_report((String)"frontend", 1)) {
            Report.report(1, "Running job " + job);
         }

         okay &= this.runAllPasses(job);
         if (job.completed()) {
            this.jobs.put(job.source(), COMPLETED_JOB);
            if (Report.should_report((String)"frontend", 1)) {
               Report.report(1, "Completed job " + job);
            }
         } else {
            if (Report.should_report((String)"frontend", 1)) {
               Report.report(1, "Failed to complete job " + job);
            }

            this.worklist.add(job);
         }
      }

      if (Report.should_report((String)"frontend", 1)) {
         Report.report(1, "Finished all passes -- " + (okay ? "okay" : "failed"));
      }

      return okay;
   }

   protected SourceJob selectJobFromWorklist() {
      return (SourceJob)this.worklist.remove(0);
   }

   public boolean readSource(FileSource source) {
      SourceJob job = this.addJob(source);
      if (job == null) {
         return true;
      } else {
         Pass.ID barrier;
         if (this.currentJob != null) {
            if (this.currentJob.sourceJob().lastBarrier() == null) {
               throw new InternalCompilerError("A Source Job which has not reached a barrier cannot read another source file.");
            }

            barrier = this.currentJob.sourceJob().lastBarrier().id();
         } else {
            barrier = Pass.FIRST_BARRIER;
         }

         return this.runToPass(job, (Pass.ID)barrier) && this.runToPass(job, (Pass.ID)Pass.FIRST_BARRIER);
      }
   }

   public boolean runAllPasses(Job job) {
      List pending = job.pendingPasses();
      if (!pending.isEmpty()) {
         Pass lastPass = (Pass)pending.get(pending.size() - 1);
         return this.runToPass(job, lastPass);
      } else {
         return true;
      }
   }

   public boolean runToPass(Job job, Pass.ID goal) {
      if (Report.should_report((String)"frontend", 1)) {
         Report.report(1, "Running " + job + " to pass named " + goal);
      }

      if (job.completed(goal)) {
         return true;
      } else {
         Pass pass = job.passByID(goal);
         return this.runToPass(job, pass);
      }
   }

   public boolean runToPass(Job job, Pass goal) {
      if (Report.should_report((String)"frontend", 1)) {
         Report.report(1, "Running " + job + " to pass " + goal);
      }

      while(!job.pendingPasses().isEmpty()) {
         Pass pass = (Pass)job.pendingPasses().get(0);

         try {
            this.runPass(job, pass);
         } catch (CyclicDependencyException var5) {
            job.finishPass(pass, false);
         }

         if (pass == goal) {
            break;
         }
      }

      if (job.completed() && Report.should_report((String)"frontend", 1)) {
         Report.report(1, "Job " + job + " completed");
      }

      return job.status();
   }

   protected void runPass(Job job, Pass pass) throws CyclicDependencyException {
      try {
         this.enforceInvariants(job, pass);
      } catch (CyclicDependencyException var7) {
         return;
      }

      if (this.getOptions().disable_passes.contains(pass.name())) {
         if (Report.should_report((String)"frontend", 1)) {
            Report.report(1, "Skipping pass " + pass);
         }

         job.finishPass(pass, true);
      } else {
         if (Report.should_report((String)"frontend", 1)) {
            Report.report(1, "Trying to run pass " + pass + " in " + job);
         }

         if (job.isRunning()) {
            throw new CyclicDependencyException(job + " cannot reach pass " + pass);
         } else {
            pass.resetTimers();
            boolean result = false;
            if (job.status()) {
               Job oldCurrentJob = this.currentJob;
               this.currentJob = job;
               Report.should_report.push(pass.name());
               Pass oldPass = oldCurrentJob != null ? oldCurrentJob.runningPass() : null;
               if (oldPass != null) {
                  oldPass.toggleTimers(true);
               }

               job.setRunningPass(pass);
               pass.toggleTimers(false);
               result = pass.run();
               pass.toggleTimers(false);
               job.setRunningPass((Pass)null);
               Report.should_report.pop();
               this.currentJob = oldCurrentJob;
               if (oldPass != null) {
                  oldPass.toggleTimers(true);
               }

               if (this.getOptions().print_ast.contains(pass.name())) {
                  System.err.println("----------------------------------------------------------------");
                  System.err.println("Pretty-printing AST for " + job + " after " + pass.name());
                  PrettyPrinter pp = new PrettyPrinter();
                  pp.printAst(job.ast(), new CodeWriter(System.err, 78));
               }

               if (this.getOptions().dump_ast.contains(pass.name())) {
                  System.err.println("----------------------------------------------------------------");
                  System.err.println("Dumping AST for " + job + " after " + pass.name());
                  NodeVisitor dumper = new DumpAst(new CodeWriter(System.err, 78));
                  NodeVisitor dumper = dumper.begin();
                  job.ast().visit(dumper);
                  dumper.finish();
               }
            }

            Stats stats = this.getStats();
            stats.accumPassTimes(pass.id(), pass.inclusiveTime(), pass.exclusiveTime());
            if (Report.should_report((String)"time", 2)) {
               Report.report(2, "Finished " + pass + " status=" + str(result) + " inclusive_time=" + pass.inclusiveTime() + " exclusive_time=" + pass.exclusiveTime());
            } else if (Report.should_report((String)"frontend", 1)) {
               Report.report(1, "Finished " + pass + " status=" + str(result));
            }

            job.finishPass(pass, result);
         }
      }
   }

   protected void enforceInvariants(Job job, Pass pass) throws CyclicDependencyException {
      SourceJob srcJob = job.sourceJob();
      if (srcJob != null) {
         BarrierPass lastBarrier = srcJob.lastBarrier();
         if (lastBarrier != null) {
            List allDependentSrcs = new ArrayList(srcJob.dependencies());
            Iterator i = allDependentSrcs.iterator();

            while(i.hasNext()) {
               Source s = (Source)i.next();
               Object o = this.jobs.get(s);
               if (o != COMPLETED_JOB) {
                  if (o == null) {
                     throw new InternalCompilerError("Unknown source " + s);
                  }

                  SourceJob sj = (SourceJob)o;
                  if (sj.pending(lastBarrier.id())) {
                     if (Report.should_report((String)"frontend", 3)) {
                        Report.report(3, "Running " + sj + " to " + lastBarrier.id() + " for " + srcJob);
                     }

                     this.runToPass(sj, (Pass.ID)lastBarrier.id());
                  }
               }
            }
         }

         if (pass instanceof GlobalBarrierPass) {
            LinkedList barrierWorklist = new LinkedList(this.jobs.values());

            while(true) {
               SourceJob sj;
               do {
                  do {
                     Object o;
                     do {
                        if (barrierWorklist.isEmpty()) {
                           return;
                        }

                        o = barrierWorklist.removeFirst();
                     } while(o == COMPLETED_JOB);

                     sj = (SourceJob)o;
                  } while(sj.completed(pass.id()));
               } while(sj.nextPass() == sj.passByID(pass.id()));

               Pass beforeGlobal = sj.getPreviousTo(pass.id());
               if (Report.should_report((String)"frontend", 3)) {
                  Report.report(3, "Running " + sj + " to " + beforeGlobal.id() + " for " + srcJob);
               }

               while(!sj.pendingPasses().isEmpty()) {
                  Pass p = (Pass)sj.pendingPasses().get(0);
                  this.runPass(sj, p);
                  if (p == beforeGlobal) {
                     break;
                  }
               }
            }
         }
      }
   }

   private static String str(boolean okay) {
      return okay ? "done" : "failed";
   }

   public String[] fileExtensions() {
      String[] sx = this.getOptions() == null ? null : this.getOptions().source_ext;
      if (sx == null) {
         sx = this.defaultFileExtensions();
      }

      return sx.length == 0 ? this.defaultFileExtensions() : sx;
   }

   public String[] defaultFileExtensions() {
      String ext = this.defaultFileExtension();
      return new String[]{ext};
   }

   public SourceLoader sourceLoader() {
      if (this.source_loader == null) {
         this.source_loader = new SourceLoader(this, this.getOptions().source_path);
      }

      return this.source_loader;
   }

   public TargetFactory targetFactory() {
      if (this.target_factory == null) {
         this.target_factory = new TargetFactory(this.getOptions().output_directory, this.getOptions().output_ext, this.getOptions().output_stdout);
      }

      return this.target_factory;
   }

   protected abstract TypeSystem createTypeSystem();

   public TypeSystem typeSystem() {
      if (this.ts == null) {
         this.ts = this.createTypeSystem();
      }

      return this.ts;
   }

   protected abstract NodeFactory createNodeFactory();

   public NodeFactory nodeFactory() {
      if (this.nf == null) {
         this.nf = this.createNodeFactory();
      }

      return this.nf;
   }

   public JobExt jobExt() {
      return null;
   }

   public void addDependencyToCurrentJob(Source s) {
      if (s != null) {
         if (this.currentJob != null) {
            Object o = this.jobs.get(s);
            if (o != COMPLETED_JOB) {
               if (Report.should_report((String)"frontend", 2)) {
                  Report.report(2, "Adding dependency from " + this.currentJob.source() + " to " + s);
               }

               this.currentJob.sourceJob().addDependency(s);
            }

         } else {
            throw new InternalCompilerError("No current job!");
         }
      }
   }

   public SourceJob addJob(Source source) {
      return this.addJob(source, (Node)null);
   }

   public SourceJob addJob(Source source, Node ast) {
      Object o = this.jobs.get(source);
      SourceJob job = null;
      if (o == COMPLETED_JOB) {
         return null;
      } else {
         if (o == null) {
            job = this.createSourceJob(source, ast);
            this.jobs.put(source, job);
            this.worklist.addLast(job);
            if (Report.should_report((String)"frontend", 3)) {
               Report.report(3, "Adding job for " + source + " at the " + "request of job " + this.currentJob);
            }
         } else {
            job = (SourceJob)o;
         }

         if (this.currentJob instanceof SourceJob) {
            ((SourceJob)this.currentJob).addDependency(source);
         }

         return job;
      }
   }

   protected SourceJob createSourceJob(Source source, Node ast) {
      return new SourceJob(this, this.jobExt(), source, ast);
   }

   protected Job createJob(Node ast, Context context, Job outer, Pass.ID begin, Pass.ID end) {
      return new InnerJob(this, this.jobExt(), ast, context, outer, begin, end);
   }

   public Job spawnJob(Context c, Node ast, Job outerJob, Pass.ID begin, Pass.ID end) {
      Job j = this.createJob(ast, c, outerJob, begin, end);
      if (Report.should_report((String)"frontend", 1)) {
         Report.report(1, this + " spawning " + j);
      }

      this.runAllPasses(j);
      return j;
   }

   public abstract Parser parser(Reader var1, FileSource var2, ErrorQueue var3);

   public void replacePass(List passes, Pass.ID id, List newPasses) {
      ListIterator i = passes.listIterator();

      Pass p;
      do {
         if (!i.hasNext()) {
            throw new InternalCompilerError("Pass " + id + " not found.");
         }

         p = (Pass)i.next();
      } while(p.id() != id);

      if (p instanceof BarrierPass) {
         throw new InternalCompilerError("Cannot replace a barrier pass.");
      } else {
         i.remove();
         Iterator j = newPasses.iterator();

         while(j.hasNext()) {
            i.add(j.next());
         }

      }
   }

   public void removePass(List passes, Pass.ID id) {
      ListIterator i = passes.listIterator();

      Pass p;
      do {
         if (!i.hasNext()) {
            throw new InternalCompilerError("Pass " + id + " not found.");
         }

         p = (Pass)i.next();
      } while(p.id() != id);

      if (p instanceof BarrierPass) {
         throw new InternalCompilerError("Cannot remove a barrier pass.");
      } else {
         i.remove();
      }
   }

   public void beforePass(List passes, Pass.ID id, List newPasses) {
      ListIterator i = passes.listIterator();

      Pass p;
      do {
         if (!i.hasNext()) {
            throw new InternalCompilerError("Pass " + id + " not found.");
         }

         p = (Pass)i.next();
      } while(p.id() != id);

      i.previous();
      Iterator j = newPasses.iterator();

      while(j.hasNext()) {
         i.add(j.next());
      }

   }

   public void afterPass(List passes, Pass.ID id, List newPasses) {
      ListIterator i = passes.listIterator();

      Pass p;
      do {
         if (!i.hasNext()) {
            throw new InternalCompilerError("Pass " + id + " not found.");
         }

         p = (Pass)i.next();
      } while(p.id() != id);

      Iterator j = newPasses.iterator();

      while(j.hasNext()) {
         i.add(j.next());
      }

   }

   public void replacePass(List passes, Pass.ID id, Pass pass) {
      this.replacePass(passes, id, Collections.singletonList(pass));
   }

   public void beforePass(List passes, Pass.ID id, Pass pass) {
      this.beforePass(passes, id, Collections.singletonList(pass));
   }

   public void afterPass(List passes, Pass.ID id, Pass pass) {
      this.afterPass(passes, id, Collections.singletonList(pass));
   }

   public abstract List passes(Job var1);

   public List passes(Job job, Pass.ID begin, Pass.ID end) {
      List l = this.passes(job);
      Pass p = null;
      Iterator i = l.iterator();

      while(i.hasNext()) {
         p = (Pass)i.next();
         if (begin == p.id()) {
            break;
         }

         if (!(p instanceof BarrierPass)) {
            i.remove();
         }
      }

      while(p.id() != end && i.hasNext()) {
         p = (Pass)i.next();
      }

      while(i.hasNext()) {
         p = (Pass)i.next();
         i.remove();
      }

      return l;
   }

   public String toString() {
      return this.getClass().getName() + " worklist=" + this.worklist;
   }

   public ClassFile createClassFile(File classFileSource, byte[] code) {
      return new ClassFile(classFileSource, code, this);
   }
}
