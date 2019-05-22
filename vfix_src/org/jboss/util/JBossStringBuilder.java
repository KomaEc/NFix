package org.jboss.util;

import java.io.Serializable;
import java.util.Arrays;

public class JBossStringBuilder implements Serializable, CharSequence {
   private static final long serialVersionUID = 1874946609763446794L;
   protected char[] chars;
   protected int pos;

   public JBossStringBuilder() {
      this(16);
   }

   public JBossStringBuilder(int capacity) {
      this.chars = new char[capacity];
   }

   public JBossStringBuilder(String string) {
      this(string.length() + 16);
      this.append(string);
   }

   public JBossStringBuilder(CharSequence charSequence) {
      this(charSequence.length() + 16);
      this.append(charSequence);
   }

   public JBossStringBuilder(char[] ch) {
      this(ch, 0, ch.length);
   }

   public JBossStringBuilder(char[] ch, int start, int length) {
      this(length + 16);
      this.append(ch, start, length);
   }

   public JBossStringBuilder append(Object object) {
      return this.append(String.valueOf(object));
   }

   public JBossStringBuilder append(String string) {
      if (string == null) {
         string = "null";
      }

      int length = string.length();
      if (length == 0) {
         return this;
      } else {
         int afterAppend = this.pos + length;
         if (afterAppend > this.chars.length) {
            this.expandCapacity(afterAppend);
         }

         string.getChars(0, length, this.chars, this.pos);
         this.pos = afterAppend;
         return this;
      }
   }

   public JBossStringBuilder append(StringBuffer buffer) {
      if (buffer == null) {
         return this.append("null");
      } else {
         int length = buffer.length();
         if (length == 0) {
            return this;
         } else {
            int afterAppend = this.pos + length;
            if (afterAppend > this.chars.length) {
               this.expandCapacity(afterAppend);
            }

            buffer.getChars(0, length, this.chars, this.pos);
            this.pos = afterAppend;
            return this;
         }
      }
   }

   public JBossStringBuilder append(CharSequence charSequence) {
      if (charSequence == null) {
         return this.append("null");
      } else {
         int length = charSequence.length();
         return length == 0 ? this : this.append((CharSequence)charSequence, 0, charSequence.length());
      }
   }

   public JBossStringBuilder append(CharSequence charSequence, int start, int end) {
      if (charSequence == null) {
         return this.append("null");
      } else if (start >= 0 && end >= 0 && start <= end && start <= charSequence.length()) {
         int length = end - start;
         if (length == 0) {
            return this;
         } else {
            int afterAppend = this.pos + length;
            if (afterAppend > this.chars.length) {
               this.expandCapacity(afterAppend);
            }

            for(int i = start; i < end; ++i) {
               this.chars[this.pos++] = charSequence.charAt(i);
            }

            this.pos = afterAppend;
            return this;
         }
      } else {
         throw new IndexOutOfBoundsException("Invalid start=" + start + " end=" + end + " length=" + charSequence.length());
      }
   }

   public JBossStringBuilder append(char[] array) {
      if (array == null) {
         return this.append("null");
      } else if (array.length == 0) {
         return this;
      } else {
         String string = String.valueOf(array);
         return this.append(string);
      }
   }

   public JBossStringBuilder append(char[] array, int offset, int length) {
      if (array == null) {
         return this.append("null");
      } else {
         int arrayLength = array.length;
         if (offset >= 0 && length >= 0 && offset + length <= arrayLength) {
            if (length != 0 && arrayLength != 0) {
               String string = String.valueOf(array, offset, length);
               return this.append(string);
            } else {
               return this;
            }
         } else {
            throw new IndexOutOfBoundsException("Invalid offset=" + offset + " length=" + length + " array.length=" + arrayLength);
         }
      }
   }

   public JBossStringBuilder append(boolean primitive) {
      String string = String.valueOf(primitive);
      return this.append(string);
   }

   public JBossStringBuilder append(char primitive) {
      String string = String.valueOf(primitive);
      return this.append(string);
   }

   public JBossStringBuilder append(int primitive) {
      String string = String.valueOf(primitive);
      return this.append(string);
   }

   public JBossStringBuilder append(long primitive) {
      String string = String.valueOf(primitive);
      return this.append(string);
   }

   public JBossStringBuilder append(float primitive) {
      String string = String.valueOf(primitive);
      return this.append(string);
   }

   public JBossStringBuilder append(double primitive) {
      String string = String.valueOf(primitive);
      return this.append(string);
   }

   public JBossStringBuilder delete(int start, int end) {
      if (start >= 0 && start <= this.pos && start <= end && end <= this.pos) {
         if (start == end) {
            return this;
         } else {
            int removed = end - start;
            System.arraycopy(this.chars, start + removed, this.chars, start, this.pos - end);
            this.pos -= removed;
            return this;
         }
      } else {
         throw new IndexOutOfBoundsException("Invalid start=" + start + " end=" + end + " length=" + this.pos);
      }
   }

   public JBossStringBuilder deleteCharAt(int index) {
      return this.delete(index, 1);
   }

   public JBossStringBuilder replace(int start, int end, String string) {
      this.delete(start, end);
      return this.insert(start, string);
   }

   public JBossStringBuilder insert(int index, char[] string) {
      return this.insert(index, (char[])string, 0, string.length);
   }

   public JBossStringBuilder insert(int index, char[] string, int offset, int len) {
      int stringLength = string.length;
      if (index >= 0 && index <= this.pos && offset >= 0 && len >= 0 && offset + len <= string.length) {
         if (len == 0) {
            return this;
         } else {
            int afterAppend = this.pos + len;
            if (afterAppend > this.chars.length) {
               this.expandCapacity(afterAppend);
            }

            System.arraycopy(this.chars, index, this.chars, index + stringLength, this.pos - index);
            System.arraycopy(string, offset, this.chars, index, len);
            this.pos = afterAppend;
            return this;
         }
      } else {
         throw new IndexOutOfBoundsException("Invalid index=" + index + " offset=" + offset + " len=" + len + " string.length=" + stringLength + " length=" + this.pos);
      }
   }

   public JBossStringBuilder insert(int offset, Object object) {
      return object == null ? this.insert(offset, "null") : this.insert(offset, String.valueOf(object));
   }

   public JBossStringBuilder insert(int offset, String string) {
      if (offset >= 0 && offset <= this.pos) {
         if (string == null) {
            string = "null";
         }

         int stringLength = string.length();
         int afterAppend = this.pos + stringLength;
         if (afterAppend > this.chars.length) {
            this.expandCapacity(afterAppend);
         }

         System.arraycopy(this.chars, offset, this.chars, offset + stringLength, this.pos - offset);
         string.getChars(0, stringLength, this.chars, offset);
         this.pos = afterAppend;
         return this;
      } else {
         throw new IndexOutOfBoundsException("Invalid offset=" + offset + " length=" + this.pos);
      }
   }

   public JBossStringBuilder insert(int offset, CharSequence charSequence) {
      return charSequence == null ? this.insert(offset, "null") : this.insert(offset, (CharSequence)charSequence, 0, charSequence.length());
   }

   public JBossStringBuilder insert(int offset, CharSequence charSequence, int start, int end) {
      if (charSequence == null) {
         charSequence = "null";
      }

      int sequenceLength = ((CharSequence)charSequence).length();
      if (offset >= 0 && offset <= this.pos && start >= 0 && end >= 0 && start <= sequenceLength && end <= sequenceLength && start <= end) {
         int len = end - start;
         if (len == 0) {
            return this;
         } else {
            int afterAppend = this.pos + len;
            if (afterAppend > this.chars.length) {
               this.expandCapacity(afterAppend);
            }

            System.arraycopy(this.chars, offset, this.chars, offset + sequenceLength, this.pos - offset);

            for(int i = start; i < end; ++i) {
               this.chars[offset++] = ((CharSequence)charSequence).charAt(i);
            }

            this.pos = afterAppend;
            return this;
         }
      } else {
         throw new IndexOutOfBoundsException("Invalid offset=" + offset + " start=" + start + " end=" + end + " sequence.length()=" + sequenceLength + " length=" + this.pos);
      }
   }

   public JBossStringBuilder insert(int offset, boolean primitive) {
      return this.insert(offset, String.valueOf(primitive));
   }

   public JBossStringBuilder insert(int offset, char primitive) {
      return this.insert(offset, String.valueOf(primitive));
   }

   public JBossStringBuilder insert(int offset, int primitive) {
      return this.insert(offset, String.valueOf(primitive));
   }

   public JBossStringBuilder insert(int offset, long primitive) {
      return this.insert(offset, String.valueOf(primitive));
   }

   public JBossStringBuilder insert(int offset, float primitive) {
      return this.insert(offset, String.valueOf(primitive));
   }

   public JBossStringBuilder insert(int offset, double primitive) {
      return this.insert(offset, String.valueOf(primitive));
   }

   public int indexOf(String string) {
      return this.indexOf(string, 0);
   }

   public int indexOf(String string, int fromIndex) {
      return this.toString().indexOf(string, fromIndex);
   }

   public int indexOf(char ch) {
      return this.indexOf(ch, 0);
   }

   public int indexOf(char ch, int fromIndex) {
      return this.toString().indexOf(ch, fromIndex);
   }

   public int lastIndexOf(String string) {
      return this.lastIndexOf(string, this.pos);
   }

   public int lastIndexOf(String string, int fromIndex) {
      return this.toString().lastIndexOf(string, fromIndex);
   }

   public int lastIndexOf(char ch) {
      return this.lastIndexOf(ch, this.pos);
   }

   public int lastIndexOf(char ch, int fromIndex) {
      return this.toString().lastIndexOf(ch, fromIndex);
   }

   public JBossStringBuilder reverse() {
      char[] tmp = new char[this.pos];

      for(int n = 0; n < this.pos; ++n) {
         tmp[n] = this.chars[this.pos - n - 1];
      }

      this.chars = tmp;
      return this;
   }

   public String toString() {
      return new String(this.chars, 0, this.pos);
   }

   public int length() {
      return this.pos;
   }

   public int capacity() {
      return this.chars.length;
   }

   public void ensureCapacity(int minimum) {
      if (minimum >= 0 && minimum >= this.chars.length) {
         this.expandCapacity(minimum);
      }
   }

   public void trimToSize() {
      char[] trimmed = new char[this.pos];
      System.arraycopy(this.chars, 0, trimmed, 0, this.pos);
      this.chars = trimmed;
   }

   public void setLength(int newLength) {
      if (newLength < 0) {
         throw new StringIndexOutOfBoundsException(newLength);
      } else {
         if (newLength > this.chars.length) {
            this.expandCapacity(newLength);
         }

         Arrays.fill(this.chars, newLength, this.chars.length, '\u0000');
         this.pos = newLength;
      }
   }

   public char charAt(int index) {
      return this.chars[index];
   }

   public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) {
      if (srcBegin >= 0 && dstBegin >= 0 && srcBegin <= srcEnd && srcEnd <= this.pos && dstBegin + srcEnd - srcBegin <= dst.length) {
         int len = srcEnd - srcBegin;
         if (len != 0) {
            System.arraycopy(this.chars, srcBegin, dst, dstBegin, len);
         }
      } else {
         throw new IndexOutOfBoundsException("Invalid srcBegin=" + srcBegin + " srcEnd=" + srcEnd + " dstBegin=" + dstBegin + " dst.length=" + dst.length + " length=" + this.pos);
      }
   }

   public void setCharAt(int index, char ch) {
      if (index >= 0 && index <= this.pos) {
         this.chars[index] = ch;
      } else {
         throw new IndexOutOfBoundsException("Invalid index=" + index + " length=" + this.pos);
      }
   }

   public String substring(int start) {
      return this.substring(start, this.pos);
   }

   public CharSequence subSequence(int start, int end) {
      return this.substring(start, end);
   }

   public String substring(int start, int end) {
      if (start >= 0 && end >= 0 && start <= end && end <= this.pos) {
         return new String(this.chars, start, end - start);
      } else {
         throw new IndexOutOfBoundsException("Invalid start=" + start + " end=" + end + " length=" + this.pos);
      }
   }

   protected void expandCapacity(int minimum) {
      int newSize = this.chars.length * 2;
      if (minimum > newSize) {
         newSize = minimum;
      }

      char[] newChars = new char[newSize];
      System.arraycopy(this.chars, 0, newChars, 0, this.pos);
      this.chars = newChars;
   }
}
