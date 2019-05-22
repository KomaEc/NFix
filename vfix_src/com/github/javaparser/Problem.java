package com.github.javaparser;

import com.github.javaparser.utils.Utils;
import java.util.Comparator;
import java.util.Optional;

public class Problem {
   private final String message;
   private final TokenRange location;
   private final Throwable cause;
   public static Comparator<Problem> PROBLEM_BY_BEGIN_POSITION = (a, b) -> {
      Optional<Position> aBegin = a.getLocation().flatMap((l) -> {
         return l.getBegin().getRange().map((r) -> {
            return r.begin;
         });
      });
      Optional<Position> bBegin = b.getLocation().flatMap((l) -> {
         return l.getBegin().getRange().map((r) -> {
            return r.begin;
         });
      });
      if (aBegin.isPresent() && bBegin.isPresent()) {
         return ((Position)aBegin.get()).compareTo((Position)bBegin.get());
      } else if (!a.getLocation().isPresent() && !b.getLocation().isPresent()) {
         return 0;
      } else {
         return a.getLocation().isPresent() ? 1 : -1;
      }
   };

   public Problem(String message, TokenRange location, Throwable cause) {
      Utils.assertNotNull(message);
      this.message = message;
      this.location = location;
      this.cause = cause;
   }

   public String toString() {
      StringBuilder str = new StringBuilder(this.getVerboseMessage());
      if (this.cause != null) {
         str.append(Utils.EOL).append("Problem stacktrace : ").append(Utils.EOL);

         for(int i = 0; i < this.cause.getStackTrace().length; ++i) {
            StackTraceElement ste = this.cause.getStackTrace()[i];
            str.append("  ").append(ste.toString());
            if (i + 1 != this.cause.getStackTrace().length) {
               str.append(Utils.EOL);
            }
         }
      }

      return str.toString();
   }

   public String getMessage() {
      return this.message;
   }

   public String getVerboseMessage() {
      return (String)this.getLocation().map((l) -> {
         return (String)l.getBegin().getRange().map((r) -> {
            return r.begin.toString();
         }).orElse("(line ?,col ?)") + " " + this.message;
      }).orElse(this.message);
   }

   public Optional<TokenRange> getLocation() {
      return Optional.ofNullable(this.location);
   }

   /** @deprecated */
   @Deprecated
   public Optional<TokenRange> getRange() {
      return this.getLocation();
   }

   public Optional<Throwable> getCause() {
      return Optional.ofNullable(this.cause);
   }
}
