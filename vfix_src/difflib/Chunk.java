package difflib;

import java.util.Arrays;
import java.util.List;

public class Chunk {
   private final int position;
   private List<?> lines;

   public Chunk(int position, List<?> lines) {
      this.position = position;
      this.lines = lines;
   }

   public Chunk(int position, Object[] lines) {
      this.position = position;
      this.lines = Arrays.asList(lines);
   }

   public void verify(List<?> target) throws PatchFailedException {
      if (this.last() > target.size()) {
         throw new PatchFailedException("Incorrect Chunk: the position of chunk > target size");
      } else {
         for(int i = 0; i < this.size(); ++i) {
            if (!target.get(this.position + i).equals(this.lines.get(i))) {
               throw new PatchFailedException("Incorrect Chunk: the chunk content doesn't match the target");
            }
         }

      }
   }

   public int getPosition() {
      return this.position;
   }

   public void setLines(List<?> lines) {
      this.lines = lines;
   }

   public List<?> getLines() {
      return this.lines;
   }

   public int size() {
      return this.lines.size();
   }

   public int last() {
      return this.getPosition() + this.size() - 1;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.lines == null ? 0 : this.lines.hashCode());
      result = 31 * result + this.position;
      result = 31 * result + this.size();
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         Chunk other = (Chunk)obj;
         if (this.lines == null) {
            if (other.lines != null) {
               return false;
            }
         } else if (!this.lines.equals(other.lines)) {
            return false;
         }

         return this.position == other.position;
      }
   }

   public String toString() {
      return "[position: " + this.position + ", size: " + this.size() + ", lines: " + this.lines + "]";
   }
}
