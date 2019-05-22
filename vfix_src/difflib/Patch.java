package difflib;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Patch {
   private List<Delta> deltas = new LinkedList();

   public List<?> applyTo(List<?> target) throws PatchFailedException {
      List<Object> result = new LinkedList(target);
      ListIterator it = this.getDeltas().listIterator(this.deltas.size());

      while(it.hasPrevious()) {
         Delta delta = (Delta)it.previous();
         delta.applyTo(result);
      }

      return result;
   }

   public List<?> restore(List<?> target) {
      List<Object> result = new LinkedList(target);
      ListIterator it = this.getDeltas().listIterator(this.deltas.size());

      while(it.hasPrevious()) {
         Delta delta = (Delta)it.previous();
         delta.restore(result);
      }

      return result;
   }

   public void addDelta(Delta delta) {
      this.deltas.add(delta);
   }

   public List<Delta> getDeltas() {
      Collections.sort(this.deltas, DeltaComparator.INSTANCE);
      return this.deltas;
   }
}
