package pxb.android;

public class StringItem {
   public String data;
   public int dataOffset;
   public int index;

   public StringItem() {
   }

   public StringItem(String data) {
      this.data = data;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         StringItem other = (StringItem)obj;
         if (this.data == null) {
            if (other.data != null) {
               return false;
            }
         } else if (!this.data.equals(other.data)) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + (this.data == null ? 0 : this.data.hashCode());
      return result;
   }

   public String toString() {
      return String.format("S%04d %s", this.index, this.data);
   }
}
