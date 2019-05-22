package difflib;

import java.io.Serializable;
import java.util.Comparator;

public class DeltaComparator implements Comparator<Delta>, Serializable {
   private static final long serialVersionUID = 1L;
   public static final Comparator<Delta> INSTANCE = new DeltaComparator();

   private DeltaComparator() {
   }

   public int compare(Delta a, Delta b) {
      int posA = a.getOriginal().getPosition();
      int posB = b.getOriginal().getPosition();
      if (posA > posB) {
         return 1;
      } else {
         return posA < posB ? -1 : 0;
      }
   }
}
