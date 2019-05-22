package com.gzoltar.shaded.org.apache.commons.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

/** @deprecated */
@Deprecated
public abstract class Parser implements CommandLineParser {
   protected CommandLine cmd;
   private Options options;
   private List requiredOptions;

   protected void setOptions(Options options) {
      this.options = options;
      this.requiredOptions = new ArrayList(options.getRequiredOptions());
   }

   protected Options getOptions() {
      return this.options;
   }

   protected List getRequiredOptions() {
      return this.requiredOptions;
   }

   protected abstract String[] flatten(Options var1, String[] var2, boolean var3) throws ParseException;

   public CommandLine parse(Options options, String[] arguments) throws ParseException {
      return this.parse(options, arguments, (Properties)null, false);
   }

   public CommandLine parse(Options options, String[] arguments, Properties properties) throws ParseException {
      return this.parse(options, arguments, properties, false);
   }

   public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
      return this.parse(options, arguments, (Properties)null, stopAtNonOption);
   }

   public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException {
      Iterator var5 = options.helpOptions().iterator();

      while(var5.hasNext()) {
         Option opt = (Option)var5.next();
         opt.clearValues();
      }

      var5 = options.getOptionGroups().iterator();

      while(var5.hasNext()) {
         OptionGroup group = (OptionGroup)var5.next();
         group.setSelected((Option)null);
      }

      this.setOptions(options);
      this.cmd = new CommandLine();
      boolean eatTheRest = false;
      if (arguments == null) {
         arguments = new String[0];
      }

      List<String> tokenList = Arrays.asList(this.flatten(this.getOptions(), arguments, stopAtNonOption));
      ListIterator iterator = tokenList.listIterator();

      while(true) {
         do {
            if (!iterator.hasNext()) {
               this.processProperties(properties);
               this.checkRequiredOptions();
               return this.cmd;
            }

            String t = (String)iterator.next();
            if ("--".equals(t)) {
               eatTheRest = true;
            } else if ("-".equals(t)) {
               if (stopAtNonOption) {
                  eatTheRest = true;
               } else {
                  this.cmd.addArg(t);
               }
            } else if (t.startsWith("-")) {
               if (stopAtNonOption && !this.getOptions().hasOption(t)) {
                  eatTheRest = true;
                  this.cmd.addArg(t);
               } else {
                  this.processOption(t, iterator);
               }
            } else {
               this.cmd.addArg(t);
               if (stopAtNonOption) {
                  eatTheRest = true;
               }
            }
         } while(!eatTheRest);

         while(iterator.hasNext()) {
            String str = (String)iterator.next();
            if (!"--".equals(str)) {
               this.cmd.addArg(str);
            }
         }
      }
   }

   protected void processProperties(Properties properties) throws ParseException {
      if (properties != null) {
         Enumeration e = properties.propertyNames();

         while(true) {
            Option opt;
            String value;
            do {
               String option;
               boolean selected;
               do {
                  do {
                     if (!e.hasMoreElements()) {
                        return;
                     }

                     option = e.nextElement().toString();
                     opt = this.options.getOption(option);
                     if (opt == null) {
                        throw new UnrecognizedOptionException("Default option wasn't defined", option);
                     }

                     OptionGroup group = this.options.getOptionGroup(opt);
                     selected = group != null && group.getSelected() != null;
                  } while(this.cmd.hasOption(option));
               } while(selected);

               value = properties.getProperty(option);
               if (opt.hasArg()) {
                  if (opt.getValues() == null || opt.getValues().length == 0) {
                     try {
                        opt.addValueForProcessing(value);
                     } catch (RuntimeException var9) {
                     }
                  }
                  break;
               }
            } while(!"yes".equalsIgnoreCase(value) && !"true".equalsIgnoreCase(value) && !"1".equalsIgnoreCase(value));

            this.cmd.addOption(opt);
            this.updateRequiredOptions(opt);
         }
      }
   }

   protected void checkRequiredOptions() throws MissingOptionException {
      if (!this.getRequiredOptions().isEmpty()) {
         throw new MissingOptionException(this.getRequiredOptions());
      }
   }

   public void processArgs(Option opt, ListIterator<String> iter) throws ParseException {
      while(true) {
         if (iter.hasNext()) {
            String str = (String)iter.next();
            if (this.getOptions().hasOption(str) && str.startsWith("-")) {
               iter.previous();
            } else {
               try {
                  opt.addValueForProcessing(Util.stripLeadingAndTrailingQuotes(str));
                  continue;
               } catch (RuntimeException var5) {
                  iter.previous();
               }
            }
         }

         if (opt.getValues() == null && !opt.hasOptionalArg()) {
            throw new MissingArgumentException(opt);
         }

         return;
      }
   }

   protected void processOption(String arg, ListIterator<String> iter) throws ParseException {
      boolean hasOption = this.getOptions().hasOption(arg);
      if (!hasOption) {
         throw new UnrecognizedOptionException("Unrecognized option: " + arg, arg);
      } else {
         Option opt = (Option)this.getOptions().getOption(arg).clone();
         this.updateRequiredOptions(opt);
         if (opt.hasArg()) {
            this.processArgs(opt, iter);
         }

         this.cmd.addOption(opt);
      }
   }

   private void updateRequiredOptions(Option opt) throws ParseException {
      if (opt.isRequired()) {
         this.getRequiredOptions().remove(opt.getKey());
      }

      if (this.getOptions().getOptionGroup(opt) != null) {
         OptionGroup group = this.getOptions().getOptionGroup(opt);
         if (group.isRequired()) {
            this.getRequiredOptions().remove(group);
         }

         group.setSelected(opt);
      }

   }
}
