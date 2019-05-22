package difflib;

import java.util.List;

public class InsertDelta extends Delta {
   public InsertDelta(Chunk original, Chunk revised) {
      super(original, revised);
   }

   public void applyTo(List<Object> target) throws PatchFailedException {
      this.verify(target);
      int position = this.getOriginal().getPosition();
      List<?> lines = this.getRevised().getLines();

      for(int i = 0; i < lines.size(); ++i) {
         target.add(position + i, lines.get(i));
      }

   }

   public void restore(List<Object> target) {
      int position = this.getRevised().getPosition();
      int size = this.getRevised().size();

      for(int i = 0; i < size; ++i) {
         target.remove(position);
      }

   }

   public void verify(List<?> target) throws PatchFailedException {
      if (this.getOriginal().getPosition() > target.size()) {
         throw new PatchFailedException("Incorrect patch for delta: delta original position > target size");
      }
   }

   public Delta.TYPE getType() {
      return Delta.TYPE.INSERT;
   }

   public String toString() {
      return "[InsertDelta, position: " + this.getOriginal().getPosition() + ", lines: " + this.getRevised().getLines() + "]";
   }
}
