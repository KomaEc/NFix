package difflib;

public class DiffRow {
   private DiffRow.Tag tag;
   private String oldLine;
   private String newLine;

   public DiffRow(DiffRow.Tag tag, String oldLine, String newLine) {
      this.tag = tag;
      this.oldLine = oldLine;
      this.newLine = newLine;
   }

   public DiffRow.Tag getTag() {
      return this.tag;
   }

   public void setTag(DiffRow.Tag tag) {
      this.tag = tag;
   }

   public String getOldLine() {
      return this.oldLine;
   }

   public void setOldLine(String oldLine) {
      this.oldLine = oldLine;
   }

   public String getNewLine() {
      return this.newLine;
   }

   public void setNewLine(String newLine) {
      this.newLine = newLine;
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.newLine == null ? 0 : this.newLine.hashCode());
      result = 31 * result + (this.oldLine == null ? 0 : this.oldLine.hashCode());
      result = 31 * result + (this.tag == null ? 0 : this.tag.hashCode());
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
         DiffRow other = (DiffRow)obj;
         if (this.newLine == null) {
            if (other.newLine != null) {
               return false;
            }
         } else if (!this.newLine.equals(other.newLine)) {
            return false;
         }

         if (this.oldLine == null) {
            if (other.oldLine != null) {
               return false;
            }
         } else if (!this.oldLine.equals(other.oldLine)) {
            return false;
         }

         if (this.tag == null) {
            if (other.tag != null) {
               return false;
            }
         } else if (!this.tag.equals(other.tag)) {
            return false;
         }

         return true;
      }
   }

   public String toString() {
      return "[" + this.tag + "," + this.oldLine + "," + this.newLine + "]";
   }

   public static enum Tag {
      INSERT,
      DELETE,
      CHANGE,
      EQUAL;
   }
}
