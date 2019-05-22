package org.jf.util;

import java.util.Comparator;
import java.util.List;

public class LinearSearch {
   public static <T> int linearSearch(List<? extends T> list, Comparator<T> comparator, T key, int initialGuess) {
      int guess = initialGuess;
      if (initialGuess >= list.size()) {
         guess = list.size() - 1;
      }

      int comparison = comparator.compare(list.get(guess), key);
      if (comparison == 0) {
         return guess;
      } else if (comparison < 0) {
         ++guess;

         while(guess < list.size()) {
            comparison = comparator.compare(list.get(guess), key);
            if (comparison == 0) {
               return guess;
            }

            if (comparison > 0) {
               return -(guess + 1);
            }

            ++guess;
         }

         return -(list.size() + 1);
      } else {
         --guess;

         while(guess >= 0) {
            comparison = comparator.compare(list.get(guess), key);
            if (comparison == 0) {
               return guess;
            }

            if (comparison < 0) {
               return -(guess + 2);
            }

            --guess;
         }

         return -1;
      }
   }
}
