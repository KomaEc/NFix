package com.gzoltar.shaded.org.pitest.util;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import java.util.Collection;
import java.util.regex.Pattern;

public class Glob implements Predicate<String> {
   private final Pattern regex;

   public Glob(String glob) {
      this.regex = Pattern.compile(convertGlobToRegex(glob));
   }

   public boolean matches(CharSequence seq) {
      return this.regex.matcher(seq).matches();
   }

   public static F<String, Predicate<String>> toGlobPredicate() {
      return new F<String, Predicate<String>>() {
         public Glob apply(String glob) {
            return new Glob(glob);
         }
      };
   }

   public static Collection<Predicate<String>> toGlobPredicates(Collection<String> globs) {
      return FCollection.map(globs, toGlobPredicate());
   }

   private static String convertGlobToRegex(String glob) {
      StringBuilder out = new StringBuilder("^");

      for(int i = 0; i < glob.length(); ++i) {
         char c = glob.charAt(i);
         switch(c) {
         case '$':
            out.append("\\$");
            break;
         case '*':
            out.append(".*");
            break;
         case '.':
            out.append("\\.");
            break;
         case '?':
            out.append('.');
            break;
         case '\\':
            out.append("\\\\");
            break;
         default:
            out.append(c);
         }
      }

      out.append('$');
      return out.toString();
   }

   public Boolean apply(String value) {
      return this.matches(value);
   }

   public String toString() {
      return this.regex.pattern();
   }
}
