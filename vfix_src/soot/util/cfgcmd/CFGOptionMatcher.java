package soot.util.cfgcmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.CompilationDeathException;

public class CFGOptionMatcher {
   private static final Logger logger = LoggerFactory.getLogger(CFGOptionMatcher.class);
   private CFGOptionMatcher.CFGOption[] options;

   public CFGOptionMatcher(CFGOptionMatcher.CFGOption[] options) {
      this.options = options;
   }

   public CFGOptionMatcher.CFGOption match(String quarry) throws CompilationDeathException {
      String uncasedQuarry = quarry.toLowerCase();
      int match = -1;

      for(int i = 0; i < this.options.length; ++i) {
         String uncasedName = this.options[i].name().toLowerCase();
         if (uncasedName.startsWith(uncasedQuarry)) {
            if (match != -1) {
               logger.debug("" + quarry + " is ambiguous; it matches " + this.options[match].name() + " and " + this.options[i].name());
               throw new CompilationDeathException(0, "Option parse error");
            }

            match = i;
         }
      }

      if (match == -1) {
         logger.debug("\"" + quarry + "\" does not match any value.");
         throw new CompilationDeathException(0, "Option parse error");
      } else {
         return this.options[match];
      }
   }

   public String help(int initialIndent, int rightMargin, int hangingIndent) {
      StringBuffer newLineBuf = new StringBuffer(2 + rightMargin);
      newLineBuf.append('\n');
      if (hangingIndent < 0) {
         hangingIndent = 0;
      }

      for(int i = 0; i < hangingIndent; ++i) {
         newLineBuf.append(' ');
      }

      String newLine = newLineBuf.toString();
      StringBuffer result = new StringBuffer();
      int lineLength = 0;

      int i;
      for(i = 0; i < initialIndent; ++i) {
         ++lineLength;
         result.append(' ');
      }

      for(i = 0; i < this.options.length; ++i) {
         if (i > 0) {
            result.append('|');
            ++lineLength;
         }

         String name = this.options[i].name();
         int nameLength = name.length();
         if (lineLength + nameLength > rightMargin) {
            result.append(newLine);
            lineLength = hangingIndent;
         }

         result.append(name);
         lineLength += nameLength;
      }

      return result.toString();
   }

   public abstract static class CFGOption {
      private final String name;

      protected CFGOption(String name) {
         this.name = name;
      }

      public String name() {
         return this.name;
      }
   }
}
