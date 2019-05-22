package org.apache.commons.validator;

import org.apache.oro.text.perl.Perl5Util;

public class ISBNValidator {
   private static final String SEP = "(\\-|\\s)";
   private static final String GROUP = "(\\d{1,5})";
   private static final String PUBLISHER = "(\\d{1,7})";
   private static final String TITLE = "(\\d{1,6})";
   private static final String CHECK = "([0-9X])";
   private static final String ISBN_PATTERN = "/^(\\d{1,5})(\\-|\\s)(\\d{1,7})(\\-|\\s)(\\d{1,6})(\\-|\\s)([0-9X])$/";

   public boolean isValid(String isbn) {
      if (isbn != null && isbn.length() >= 10 && isbn.length() <= 13) {
         if (this.isFormatted(isbn) && !this.isValidPattern(isbn)) {
            return false;
         } else {
            isbn = this.clean(isbn);
            if (isbn.length() != 10) {
               return false;
            } else {
               return this.sum(isbn) % 11 == 0;
            }
         }
      } else {
         return false;
      }
   }

   private int sum(String isbn) {
      int total = 0;

      for(int i = 0; i < 9; ++i) {
         int weight = 10 - i;
         total += weight * this.toInt(isbn.charAt(i));
      }

      total += this.toInt(isbn.charAt(9));
      return total;
   }

   private String clean(String isbn) {
      StringBuffer buf = new StringBuffer(10);

      for(int i = 0; i < isbn.length(); ++i) {
         char digit = isbn.charAt(i);
         if (Character.isDigit(digit) || digit == 'X') {
            buf.append(digit);
         }
      }

      return buf.toString();
   }

   private int toInt(char ch) {
      return ch == 'X' ? 10 : Character.getNumericValue(ch);
   }

   private boolean isFormatted(String isbn) {
      return isbn.indexOf(45) != -1 || isbn.indexOf(32) != -1;
   }

   private boolean isValidPattern(String isbn) {
      return (new Perl5Util()).match("/^(\\d{1,5})(\\-|\\s)(\\d{1,7})(\\-|\\s)(\\d{1,6})(\\-|\\s)([0-9X])$/", isbn);
   }
}
