package difflib;

import java.util.List;

public abstract class Delta {
   private Chunk original;
   private Chunk revised;

   public Delta(Chunk original, Chunk revised) {
      this.original = original;
      this.revised = revised;
   }

   public abstract void verify(List<?> var1) throws PatchFailedException;

   public abstract void applyTo(List<Object> var1) throws PatchFailedException;

   public abstract void restore(List<Object> var1);

   public abstract Delta.TYPE getType();

   public Chunk getOriginal() {
      return this.original;
   }

   public void setOriginal(Chunk original) {
      this.original = original;
   }

   public Chunk getRevised() {
      return this.revised;
   }

   public void setRevised(Chunk revised) {
      this.revised = revised;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.original == null ? 0 : this.original.hashCode());
      result = 31 * result + (this.revised == null ? 0 : this.revised.hashCode());
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
         Delta other = (Delta)obj;
         if (this.original == null) {
            if (other.original != null) {
               return false;
            }
         } else if (!this.original.equals(other.original)) {
            return false;
         }

         if (this.revised == null) {
            if (other.revised != null) {
               return false;
            }
         } else if (!this.revised.equals(other.revised)) {
            return false;
         }

         return true;
      }
   }

   public static enum TYPE {
      CHANGE,
      DELETE,
      INSERT;
   }
}
