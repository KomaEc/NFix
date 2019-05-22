package com.gzoltar.shaded.org.apache.commons.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/** @deprecated */
@Deprecated
public class PosixParser extends Parser {
   private final List<String> tokens = new ArrayList();
   private boolean eatTheRest;
   private Option currentOption;
   private Options options;

   private void init() {
      this.eatTheRest = false;
      this.tokens.clear();
   }

   protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
      this.init();
      this.options = options;

      for(Iterator iter = Arrays.asList(arguments).iterator(); iter.hasNext(); this.gobble(iter)) {
         String token = (String)iter.next();
         if (!"-".equals(token) && !"--".equals(token)) {
            if (token.startsWith("--")) {
               int pos = token.indexOf(61);
               String opt = pos == -1 ? token : token.substring(0, pos);
               List<String> matchingOpts = options.getMatchingOptions(opt);
               if (matchingOpts.isEmpty()) {
                  this.processNonOptionToken(token, stopAtNonOption);
               } else {
                  if (matchingOpts.size() > 1) {
                     throw new AmbiguousOptionException(opt, matchingOpts);
                  }

                  this.currentOption = options.getOption((String)matchingOpts.get(0));
                  this.tokens.add("--" + this.currentOption.getLongOpt());
                  if (pos != -1) {
                     this.tokens.add(token.substring(pos + 1));
                  }
               }
            } else if (token.startsWith("-")) {
               if (token.length() != 2 && !options.hasOption(token)) {
                  if (!options.getMatchingOptions(token).isEmpty()) {
                     List<String> matchingOpts = options.getMatchingOptions(token);
                     if (matchingOpts.size() > 1) {
                        throw new AmbiguousOptionException(token, matchingOpts);
                     }

                     Option opt = options.getOption((String)matchingOpts.get(0));
                     this.processOptionToken("-" + opt.getLongOpt(), stopAtNonOption);
                  } else {
                     this.burstToken(token, stopAtNonOption);
                  }
               } else {
                  this.processOptionToken(token, stopAtNonOption);
               }
            } else {
               this.processNonOptionToken(token, stopAtNonOption);
            }
         } else {
            this.tokens.add(token);
         }
      }

      return (String[])this.tokens.toArray(new String[this.tokens.size()]);
   }

   private void gobble(Iterator<String> iter) {
      if (this.eatTheRest) {
         while(iter.hasNext()) {
            this.tokens.add(iter.next());
         }
      }

   }

   private void processNonOptionToken(String value, boolean stopAtNonOption) {
      if (stopAtNonOption && (this.currentOption == null || !this.currentOption.hasArg())) {
         this.eatTheRest = true;
         this.tokens.add("--");
      }

      this.tokens.add(value);
   }

   private void processOptionToken(String token, boolean stopAtNonOption) {
      if (stopAtNonOption && !this.options.hasOption(token)) {
         this.eatTheRest = true;
      }

      if (this.options.hasOption(token)) {
         this.currentOption = this.options.getOption(token);
      }

      this.tokens.add(token);
   }

   protected void burstToken(String token, boolean stopAtNonOption) {
      for(int i = 1; i < token.length(); ++i) {
         String ch = String.valueOf(token.charAt(i));
         if (!this.options.hasOption(ch)) {
            if (stopAtNonOption) {
               this.processNonOptionToken(token.substring(i), true);
            } else {
               this.tokens.add(token);
            }
            break;
         }

         this.tokens.add("-" + ch);
         this.currentOption = this.options.getOption(ch);
         if (this.currentOption.hasArg() && token.length() != i + 1) {
            this.tokens.add(token.substring(i + 1));
            break;
         }
      }

   }
}
