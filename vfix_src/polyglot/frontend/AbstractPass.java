package polyglot.frontend;

public abstract class AbstractPass implements Pass {
   protected Pass.ID id;
   protected long exclusive_time = 0L;
   protected long inclusive_time = 0L;

   public AbstractPass(Pass.ID id) {
      this.id = id;
   }

   public Pass.ID id() {
      return this.id;
   }

   public String name() {
      return this.id.toString();
   }

   public abstract boolean run();

   public void toggleTimers(boolean exclusive_only) {
      if (!exclusive_only) {
         this.inclusive_time = System.currentTimeMillis() - this.inclusive_time;
      }

      this.exclusive_time = System.currentTimeMillis() - this.exclusive_time;
   }

   public void resetTimers() {
      this.inclusive_time = 0L;
      this.exclusive_time = 0L;
   }

   public long exclusiveTime() {
      return this.exclusive_time;
   }

   public long inclusiveTime() {
      return this.inclusive_time;
   }

   public String toString() {
      return this.getClass().getName() + ":" + this.id;
   }
}
