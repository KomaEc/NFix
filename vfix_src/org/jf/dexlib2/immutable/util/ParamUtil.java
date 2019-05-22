package org.jf.dexlib2.immutable.util;

import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import javax.annotation.Nonnull;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;

public class ParamUtil {
   private static int findTypeEnd(@Nonnull String str, int index) {
      char c = str.charAt(index);
      switch(c) {
      case 'B':
      case 'C':
      case 'D':
      case 'F':
      case 'I':
      case 'J':
      case 'S':
      case 'Z':
         return index + 1;
      case 'E':
      case 'G':
      case 'H':
      case 'K':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      default:
         throw new IllegalArgumentException(String.format("Param string \"%s\" contains invalid type prefix: %s", str, Character.toString(c)));
      case 'L':
         while(str.charAt(index++) != ';') {
         }

         return index;
      case '[':
         while(str.charAt(index++) != '[') {
         }

         return findTypeEnd(str, index);
      }
   }

   @Nonnull
   public static Iterable<ImmutableMethodParameter> parseParamString(@Nonnull final String params) {
      return new Iterable<ImmutableMethodParameter>() {
         public Iterator<ImmutableMethodParameter> iterator() {
            return new Iterator<ImmutableMethodParameter>() {
               private int index = 0;

               public boolean hasNext() {
                  return this.index < params.length();
               }

               public ImmutableMethodParameter next() {
                  int end = ParamUtil.findTypeEnd(params, this.index);
                  String ret = params.substring(this.index, end);
                  this.index = end;
                  return new ImmutableMethodParameter(ret, (ImmutableSet)null, (String)null);
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }
}
