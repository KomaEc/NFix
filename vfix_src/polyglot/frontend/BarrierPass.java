package polyglot.frontend;

import polyglot.main.Report;

public class BarrierPass extends AbstractPass {
   Job job;

   public BarrierPass(Pass.ID id, Job job) {
      super(id);
      this.job = job;
   }

   public boolean run() {
      if (Report.should_report((String)"frontend", 1)) {
         Report.report(1, this.job + " at barrier " + this.id);
      }

      if (Report.should_report((String)"frontend", 2)) {
         Report.report(2, "dependencies of " + this.job.sourceJob() + " = " + this.job.sourceJob().dependencies());
      }

      return true;
   }
}
