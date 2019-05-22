package groovyjarjarcommonscli;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class CommandLine implements Serializable {
   private static final long serialVersionUID = 1L;
   private List args = new LinkedList();
   private List options = new ArrayList();

   CommandLine() {
   }

   public boolean hasOption(String opt) {
      return this.options.contains(this.resolveOption(opt));
   }

   public boolean hasOption(char opt) {
      return this.hasOption(String.valueOf(opt));
   }

   /** @deprecated */
   public Object getOptionObject(String opt) {
      try {
         return this.getParsedOptionValue(opt);
      } catch (ParseException var3) {
         System.err.println("Exception found converting " + opt + " to desired type: " + var3.getMessage());
         return null;
      }
   }

   public Object getParsedOptionValue(String opt) throws ParseException {
      String res = this.getOptionValue(opt);
      Option option = this.resolveOption(opt);
      if (option == null) {
         return null;
      } else {
         Object type = option.getType();
         return res == null ? null : TypeHandler.createValue(res, type);
      }
   }

   public Object getOptionObject(char opt) {
      return this.getOptionObject(String.valueOf(opt));
   }

   public String getOptionValue(String opt) {
      String[] values = this.getOptionValues(opt);
      return values == null ? null : values[0];
   }

   public String getOptionValue(char opt) {
      return this.getOptionValue(String.valueOf(opt));
   }

   public String[] getOptionValues(String opt) {
      List values = new ArrayList();
      Iterator it = this.options.iterator();

      while(true) {
         Option option;
         do {
            if (!it.hasNext()) {
               return values.isEmpty() ? null : (String[])((String[])values.toArray(new String[values.size()]));
            }

            option = (Option)it.next();
         } while(!opt.equals(option.getOpt()) && !opt.equals(option.getLongOpt()));

         values.addAll(option.getValuesList());
      }
   }

   private Option resolveOption(String opt) {
      opt = Util.stripLeadingHyphens(opt);
      Iterator it = this.options.iterator();

      Option option;
      do {
         if (!it.hasNext()) {
            return null;
         }

         option = (Option)it.next();
         if (opt.equals(option.getOpt())) {
            return option;
         }
      } while(!opt.equals(option.getLongOpt()));

      return option;
   }

   public String[] getOptionValues(char opt) {
      return this.getOptionValues(String.valueOf(opt));
   }

   public String getOptionValue(String opt, String defaultValue) {
      String answer = this.getOptionValue(opt);
      return answer != null ? answer : defaultValue;
   }

   public String getOptionValue(char opt, String defaultValue) {
      return this.getOptionValue(String.valueOf(opt), defaultValue);
   }

   public Properties getOptionProperties(String opt) {
      Properties props = new Properties();
      Iterator it = this.options.iterator();

      while(true) {
         Option option;
         do {
            if (!it.hasNext()) {
               return props;
            }

            option = (Option)it.next();
         } while(!opt.equals(option.getOpt()) && !opt.equals(option.getLongOpt()));

         List values = option.getValuesList();
         if (values.size() >= 2) {
            props.put(values.get(0), values.get(1));
         } else if (values.size() == 1) {
            props.put(values.get(0), "true");
         }
      }
   }

   public String[] getArgs() {
      String[] answer = new String[this.args.size()];
      this.args.toArray(answer);
      return answer;
   }

   public List getArgList() {
      return this.args;
   }

   void addArg(String arg) {
      this.args.add(arg);
   }

   void addOption(Option opt) {
      this.options.add(opt);
   }

   public Iterator iterator() {
      return this.options.iterator();
   }

   public Option[] getOptions() {
      Collection processed = this.options;
      Option[] optionsArray = new Option[processed.size()];
      return (Option[])((Option[])processed.toArray(optionsArray));
   }
}
