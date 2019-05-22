package polyglot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import polyglot.ast.Node;
import polyglot.types.Context;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;

public abstract class Job {
   protected JobExt ext;
   protected ExtensionInfo lang;
   protected Node ast;
   protected ArrayList passes;
   protected int nextPass;
   protected Pass runningPass;
   protected boolean status;
   protected Map passMap;
   protected int initialErrorCount;
   protected boolean reportedErrors;

   public Job(ExtensionInfo lang, JobExt ext, Node ast) {
      this.lang = lang;
      this.ext = ext;
      this.ast = ast;
      this.passes = null;
      this.passMap = null;
      this.nextPass = 0;
      this.runningPass = null;
      this.status = true;
      this.initialErrorCount = 0;
      this.reportedErrors = false;
   }

   public JobExt ext() {
      return this.ext;
   }

   public BarrierPass lastBarrier() {
      for(int i = this.nextPass - 1; i >= 0; --i) {
         Pass pass = (Pass)this.passes.get(i);
         if (pass instanceof BarrierPass) {
            return (BarrierPass)pass;
         }
      }

      return null;
   }

   public void setRunningPass(Pass pass) {
      if (pass != null) {
         this.initialErrorCount = this.compiler().errorQueue().errorCount();
      } else {
         int errorCount = this.compiler().errorQueue().errorCount();
         if (errorCount > this.initialErrorCount) {
            this.reportedErrors = true;
         }
      }

      this.runningPass = pass;
   }

   public boolean isRunning() {
      return this.runningPass != null;
   }

   public Pass runningPass() {
      return this.runningPass;
   }

   public Node ast() {
      return this.ast;
   }

   public void ast(Node ast) {
      this.ast = ast;
   }

   public boolean reportedErrors() {
      return this.reportedErrors;
   }

   public void dump(CodeWriter cw) {
      if (this.ast != null) {
         this.ast.dump(cw);
      }

   }

   public Context context() {
      return null;
   }

   public abstract SourceJob sourceJob();

   public Source source() {
      return this.sourceJob().source();
   }

   public boolean userSpecified() {
      return this.source().userSpecified();
   }

   protected abstract List getPasses();

   public final List passes() {
      if (this.passes == null) {
         this.init();
      }

      return this.passes;
   }

   private Map passMap() {
      if (this.passMap == null) {
         this.init();
      }

      return this.passMap;
   }

   protected void init() {
      this.passes = new ArrayList(this.getPasses());
      this.passMap = new HashMap();

      for(int i = 0; i < this.passes.size(); ++i) {
         Pass pass = (Pass)this.passes.get(i);
         this.passMap.put(pass.id(), new Integer(i));
      }

   }

   public boolean completed() {
      return this.pendingPasses().isEmpty();
   }

   public List completedPasses() {
      return this.passes().subList(0, this.nextPass);
   }

   public List pendingPasses() {
      return this.passes().subList(this.nextPass, this.passes.size());
   }

   public boolean completed(Pass.ID id) {
      Integer i = (Integer)this.passMap().get(id);
      return i != null && i < this.nextPass;
   }

   public boolean pending(Pass.ID id) {
      Integer i = (Integer)this.passMap().get(id);
      return i != null && i >= this.nextPass;
   }

   public Pass passByID(Pass.ID id) {
      Integer i = (Integer)this.passMap().get(id);
      if (i != null) {
         return (Pass)this.passes().get(i);
      } else {
         throw new InternalCompilerError("No pass named \"" + id + "\".");
      }
   }

   public Pass getPreviousTo(Pass.ID id) {
      Integer i = (Integer)this.passMap().get(id);
      if (i != null) {
         return i == 0 ? null : (Pass)this.passes().get(i - 1);
      } else {
         throw new InternalCompilerError("No pass named \"" + id + "\".");
      }
   }

   public Pass nextPass() {
      if (this.nextPass < this.passes().size()) {
         Pass pass = (Pass)this.passes().get(this.nextPass);
         return pass;
      } else {
         return null;
      }
   }

   public boolean status() {
      return this.status;
   }

   public void finishPass(Pass p, boolean okay) {
      List passes = this.passes();
      this.status &= okay;

      for(int i = this.nextPass; i < passes.size(); ++i) {
         Pass pass = (Pass)passes.get(i);
         if (pass == p) {
            this.nextPass = i + 1;
            return;
         }
      }

      throw new InternalCompilerError("Pass " + p + " was not a pending " + "pass.");
   }

   public ExtensionInfo extensionInfo() {
      return this.lang;
   }

   public Compiler compiler() {
      return this.lang.compiler();
   }

   public Job spawn(Context c, Node ast, Pass.ID begin, Pass.ID end) {
      return this.lang.spawnJob(c, ast, this, begin, end);
   }
}
