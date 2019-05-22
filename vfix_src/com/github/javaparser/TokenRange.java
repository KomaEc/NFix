package com.github.javaparser;

import com.github.javaparser.utils.Utils;
import java.util.Iterator;
import java.util.Optional;

public class TokenRange implements Iterable<JavaToken> {
   public static final TokenRange INVALID;
   private final JavaToken begin;
   private final JavaToken end;

   public TokenRange(JavaToken begin, JavaToken end) {
      this.begin = (JavaToken)Utils.assertNotNull(begin);
      this.end = (JavaToken)Utils.assertNotNull(end);
   }

   public JavaToken getBegin() {
      return this.begin;
   }

   public JavaToken getEnd() {
      return this.end;
   }

   public Optional<Range> toRange() {
      return this.begin.getRange().isPresent() && this.end.getRange().isPresent() ? Optional.of(new Range(((Range)this.begin.getRange().get()).begin, ((Range)this.end.getRange().get()).end)) : Optional.empty();
   }

   public TokenRange withBegin(JavaToken begin) {
      return new TokenRange((JavaToken)Utils.assertNotNull(begin), this.end);
   }

   public TokenRange withEnd(JavaToken end) {
      return new TokenRange(this.begin, (JavaToken)Utils.assertNotNull(end));
   }

   public String toString() {
      StringBuilder result = new StringBuilder();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         JavaToken t = (JavaToken)var2.next();
         result.append(t.getText());
      }

      return result.toString();
   }

   public Iterator<JavaToken> iterator() {
      return new Iterator<JavaToken>() {
         private boolean hasNext = true;
         private JavaToken current;

         {
            this.current = TokenRange.this.begin;
         }

         public boolean hasNext() {
            return this.hasNext;
         }

         public JavaToken next() {
            JavaToken retval = this.current;
            if (this.current == null) {
               throw new IllegalStateException("Attempting to move past end of range.");
            } else {
               if (this.current == TokenRange.this.end) {
                  this.hasNext = false;
               }

               this.current = (JavaToken)this.current.getNextToken().orElse((Object)null);
               if (this.current == null && this.hasNext) {
                  throw new IllegalStateException("End token is not linked to begin token.");
               } else {
                  return retval;
               }
            }
         }
      };
   }

   static {
      INVALID = new TokenRange(JavaToken.INVALID, JavaToken.INVALID);
   }
}
