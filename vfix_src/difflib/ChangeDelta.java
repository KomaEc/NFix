package difflib;

import java.util.Iterator;
import java.util.List;

public class ChangeDelta extends Delta {
   public ChangeDelta(Chunk original, Chunk revised) {
      super(original, revised);
   }

   public void applyTo(List<Object> target) throws PatchFailedException {
      this.verify(target);
      int position = this.getOriginal().getPosition();
      int size = this.getOriginal().size();

      int i;
      for(i = 0; i < size; ++i) {
         target.remove(position);
      }

      i = 0;

      for(Iterator i$ = this.getRevised().getLines().iterator(); i$.hasNext(); ++i) {
         Object line = i$.next();
         target.add(position + i, line);
      }

   }

   public void restore(List<Object> target) {
      int position = this.getRevised().getPosition();
      int size = this.getRevised().size();

      int i;
      for(i = 0; i < size; ++i) {
         target.remove(position);
      }

      i = 0;

      for(Iterator i$ = this.getOriginal().getLines().iterator(); i$.hasNext(); ++i) {
         Object line = i$.next();
         target.add(position + i, line);
      }

   }

   public void verify(List<?> target) throws PatchFailedException {
      this.getOriginal().verify(target);
      if (this.getOriginal().getPosition() > target.size()) {
         throw new PatchFailedException("Incorrect patch for delta: delta original position > target size");
      }
   }

   public String toString() {
      return "[ChangeDelta, position: " + this.getOriginal().getPosition() + ", lines: " + this.getOriginal().getLines() + " to " + this.getRevised().getLines() + "]";
   }

   public Delta.TYPE getType() {
      return Delta.TYPE.CHANGE;
   }
}
