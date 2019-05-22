package difflib;

import java.util.List;

public class DeleteDelta extends Delta {
   public DeleteDelta(Chunk original, Chunk revised) {
      super(original, revised);
   }

   public void applyTo(List<Object> target) throws PatchFailedException {
      this.verify(target);
      int position = this.getOriginal().getPosition();
      int size = this.getOriginal().size();

      for(int i = 0; i < size; ++i) {
         target.remove(position);
      }

   }

   public void restore(List<Object> target) {
      int position = this.getRevised().getPosition();
      List<?> lines = this.getOriginal().getLines();

      for(int i = 0; i < lines.size(); ++i) {
         target.add(position + i, lines.get(i));
      }

   }

   public Delta.TYPE getType() {
      return Delta.TYPE.DELETE;
   }

   public void verify(List<?> target) throws PatchFailedException {
      this.getOriginal().verify(target);
   }

   public String toString() {
      return "[DeleteDelta, position: " + this.getOriginal().getPosition() + ", lines: " + this.getOriginal().getLines() + "]";
   }
}
