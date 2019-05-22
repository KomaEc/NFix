package soot;

import java.util.Iterator;
import java.util.Map;
import soot.options.Options;
import soot.util.Chain;
import soot.util.HashChain;

public abstract class Pack implements HasPhaseOptions, Iterable<Transform> {
   private String name;
   Chain<Transform> opts = new HashChain();

   public String getPhaseName() {
      return this.name;
   }

   public Pack(String name) {
      this.name = name;
   }

   public Iterator<Transform> iterator() {
      return this.opts.iterator();
   }

   public void add(Transform t) {
      if (!t.getPhaseName().startsWith(this.getPhaseName() + ".")) {
         throw new RuntimeException("Transforms in pack '" + this.getPhaseName() + "' must have a phase name that starts with '" + this.getPhaseName() + ".'.");
      } else {
         PhaseOptions.v().getPM().notifyAddPack();
         if (this.get(t.getPhaseName()) != null) {
            throw new RuntimeException("Phase " + t.getPhaseName() + " already in pack");
         } else {
            this.opts.add(t);
         }
      }
   }

   public void insertAfter(Transform t, String phaseName) {
      PhaseOptions.v().getPM().notifyAddPack();
      Iterator var3 = this.opts.iterator();

      Transform tr;
      do {
         if (!var3.hasNext()) {
            throw new RuntimeException("phase " + phaseName + " not found!");
         }

         tr = (Transform)var3.next();
      } while(!tr.getPhaseName().equals(phaseName));

      this.opts.insertAfter((Object)t, tr);
   }

   public void insertBefore(Transform t, String phaseName) {
      PhaseOptions.v().getPM().notifyAddPack();
      Iterator var3 = this.opts.iterator();

      Transform tr;
      do {
         if (!var3.hasNext()) {
            throw new RuntimeException("phase " + phaseName + " not found!");
         }

         tr = (Transform)var3.next();
      } while(!tr.getPhaseName().equals(phaseName));

      this.opts.insertBefore((Object)t, tr);
   }

   public Transform get(String phaseName) {
      Iterator var2 = this.opts.iterator();

      Transform tr;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         tr = (Transform)var2.next();
      } while(!tr.getPhaseName().equals(phaseName));

      return tr;
   }

   public boolean remove(String phaseName) {
      Iterator var2 = this.opts.iterator();

      Transform tr;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         tr = (Transform)var2.next();
      } while(!tr.getPhaseName().equals(phaseName));

      this.opts.remove(tr);
      return true;
   }

   protected void internalApply() {
      throw new RuntimeException("wrong type of pack");
   }

   protected void internalApply(Body b) {
      throw new RuntimeException("wrong type of pack");
   }

   public final void apply() {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions((HasPhaseOptions)this);
      if (PhaseOptions.getBoolean(options, "enabled")) {
         this.internalApply();
      }
   }

   public final void apply(Body b) {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions((HasPhaseOptions)this);
      if (PhaseOptions.getBoolean(options, "enabled")) {
         this.internalApply(b);
      }
   }

   public String getDeclaredOptions() {
      return Options.getDeclaredOptionsForPhase(this.getPhaseName());
   }

   public String getDefaultOptions() {
      return Options.getDefaultOptionsForPhase(this.getPhaseName());
   }
}
