package soot.jbco.util;

public class StringTrie {
   private char[] startChars = new char[0];
   private StringTrie[] tries = new StringTrie[0];

   public void add(char[] chars, int index) {
      if (chars.length != index) {
         if (this.startChars.length == 0) {
            this.startChars = new char[1];
            this.startChars[0] = chars[0];
            this.tries = new StringTrie[1];
            this.tries[0].add(chars, index++);
         } else {
            int i = this.findStart(chars[index], 0, this.startChars.length - 1);
            if (i >= 0) {
               this.tries[i].add(chars, index++);
            } else {
               i = this.addChar(chars[index]);
               this.tries[i].add(chars, index++);
            }
         }

      }
   }

   private int addChar(char c) {
      int oldLength = this.startChars.length;
      int i = this.findSpot(c, 0, oldLength - 1);
      char[] tmp = (char[])this.startChars.clone();
      StringTrie[] t = (StringTrie[])this.tries.clone();
      this.startChars = new char[oldLength + 1];
      this.tries = new StringTrie[oldLength + 1];
      if (i > 0) {
         System.arraycopy(tmp, 0, this.startChars, 0, i);
         System.arraycopy(t, 0, this.tries, 0, i);
      }

      if (i < oldLength) {
         System.arraycopy(tmp, i, this.startChars, i + 1, oldLength - i);
         System.arraycopy(t, i, this.tries, i + 1, oldLength - i);
      }

      this.startChars[i] = c;
      this.tries[i] = new StringTrie();
      return i;
   }

   private int findSpot(char c, int first, int last) {
      int diff = last - first;
      if (diff == 1) {
         return c < this.startChars[first] ? first : (c < this.startChars[last] ? last : last + 1);
      } else {
         diff /= 2;
         return this.startChars[first + diff] < c ? this.findSpot(c, first + diff, last) : this.findSpot(c, first, last - diff);
      }
   }

   public boolean contains(char[] chars, int index) {
      if (chars.length == index) {
         return true;
      } else if (this.startChars.length == 0) {
         return false;
      } else {
         int i = this.findStart(chars[index], 0, this.startChars.length - 1);
         return i >= 0 ? this.tries[i].contains(chars, index++) : false;
      }
   }

   private int findStart(char c, int first, int last) {
      int diff = last - first;
      if (diff <= 1) {
         return c == this.startChars[first] ? first : (c == this.startChars[last] ? last : -1);
      } else {
         diff /= 2;
         return this.startChars[first + diff] <= c ? this.findStart(c, first + diff, last) : this.findStart(c, first, last - diff);
      }
   }
}
