package soot.util;

import java.util.ArrayList;

public class StationaryArrayList<T> extends ArrayList<T> {
   public int hashCode() {
      return System.identityHashCode(this);
   }

   public boolean equals(Object other) {
      return this == other;
   }
}
