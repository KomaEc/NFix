package soot.util;

public class IntegerNumberer implements Numberer<Long> {
   public void add(Long o) {
   }

   public long get(Long o) {
      return o == null ? 0L : o;
   }

   public Long get(long number) {
      return number == 0L ? null : new Long(number);
   }

   public int size() {
      throw new RuntimeException("IntegerNumberer does not implement the size() method.");
   }
}
