package soot.JastAddJ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class Options {
   private Map options = new HashMap();
   private Map optionDescriptions = new HashMap();
   private HashSet files = new HashSet();

   public Collection files() {
      return this.files;
   }

   public void initOptions() {
      this.options = new HashMap();
      this.optionDescriptions = new HashMap();
      this.files = new HashSet();
   }

   public void addKeyOption(String name) {
      if (this.optionDescriptions.containsKey(name)) {
         throw new Error("Command line definition error: option description for " + name + " is multiply declared");
      } else {
         this.optionDescriptions.put(name, new Options.Option(name, false, false));
      }
   }

   public void addKeyValueOption(String name) {
      if (this.optionDescriptions.containsKey(name)) {
         throw new Error("Command line definition error: option description for " + name + " is multiply declared");
      } else {
         this.optionDescriptions.put(name, new Options.Option(name, true, false));
      }
   }

   public void addKeyCollectionOption(String name) {
      if (this.optionDescriptions.containsKey(name)) {
         throw new Error("Command line definition error: option description for " + name + " is multiply declared");
      } else {
         this.optionDescriptions.put(name, new Options.Option(name, true, true));
      }
   }

   public void addOptionDescription(String name, boolean value) {
      if (this.optionDescriptions.containsKey(name)) {
         throw new Error("Command line definition error: option description for " + name + " is multiply declared");
      } else {
         this.optionDescriptions.put(name, new Options.Option(name, value, false));
      }
   }

   public void addOptionDescription(String name, boolean value, boolean isCollection) {
      if (this.optionDescriptions.containsKey(name)) {
         throw new Error("Command line definition error: option description for " + name + " is multiply declared");
      } else {
         this.optionDescriptions.put(name, new Options.Option(name, value, isCollection));
      }
   }

   public void addOptions(String[] args) {
      for(int i = 0; i < args.length; ++i) {
         String arg = args[i];
         FileReader value;
         if (arg.startsWith("@")) {
            try {
               String fileName = arg.substring(1, arg.length());
               value = new FileReader(fileName);
               StreamTokenizer tokenizer = new StreamTokenizer(value);
               tokenizer.resetSyntax();
               tokenizer.whitespaceChars(32, 32);
               tokenizer.whitespaceChars(9, 9);
               tokenizer.whitespaceChars(12, 12);
               tokenizer.whitespaceChars(10, 10);
               tokenizer.whitespaceChars(13, 13);
               tokenizer.wordChars(33, 255);
               ArrayList list = new ArrayList();

               for(int next = tokenizer.nextToken(); next != -1; next = tokenizer.nextToken()) {
                  if (next == -3) {
                     list.add(tokenizer.sval);
                  }
               }

               String[] newArgs = new String[list.size()];
               int index = 0;

               for(Iterator iter = list.iterator(); iter.hasNext(); ++index) {
                  newArgs[index] = (String)iter.next();
               }

               this.addOptions(newArgs);
               value.close();
            } catch (FileNotFoundException var12) {
               System.err.println("File not found: " + arg.substring(1));
            } catch (IOException var13) {
               System.err.println("Exception: " + var13.getMessage());
            }
         } else if (arg.startsWith("-")) {
            if (!this.optionDescriptions.containsKey(arg)) {
               throw new Error("Command line argument error: option " + arg + " is not defined");
            }

            Options.Option o = (Options.Option)this.optionDescriptions.get(arg);
            if (!o.isCollection && this.options.containsKey(arg)) {
               throw new Error("Command line argument error: option " + arg + " is multiply defined");
            }

            String value;
            if (o.hasValue && !o.isCollection) {
               value = null;
               if (i + 1 > args.length - 1) {
                  throw new Error("Command line argument error: value missing for key " + arg);
               }

               value = args[i + 1];
               if (value.startsWith("-")) {
                  throw new Error("Command line argument error: value missing for key " + arg);
               }

               ++i;
               this.options.put(arg, value);
            } else if (o.hasValue && o.isCollection) {
               value = null;
               if (i + 1 > args.length - 1) {
                  throw new Error("Command line argument error: value missing for key " + arg);
               }

               value = args[i + 1];
               if (value.startsWith("-")) {
                  throw new Error("Command line argument error: value missing for key " + arg);
               }

               ++i;
               Collection c = (Collection)this.options.get(arg);
               if (c == null) {
                  c = new ArrayList();
               }

               ((Collection)c).add(value);
               this.options.put(arg, c);
            } else {
               this.options.put(arg, (Object)null);
            }
         } else {
            this.files.add(arg);
         }
      }

   }

   public boolean hasOption(String name) {
      return this.options.containsKey(name);
   }

   public void setOption(String name) {
      this.options.put(name, (Object)null);
   }

   public boolean hasValueForOption(String name) {
      return this.options.containsKey(name) && this.options.get(name) != null;
   }

   public String getValueForOption(String name) {
      if (!this.hasValueForOption(name)) {
         throw new Error("Command line argument error: key " + name + " does not have a value");
      } else {
         return (String)this.options.get(name);
      }
   }

   public void setValueForOption(String value, String option) {
      this.options.put(option, value);
   }

   public Collection getValueCollectionForOption(String name) {
      if (!this.hasValueForOption(name)) {
         throw new Error("Command line argument error: key " + name + " does not have a value");
      } else {
         return (Collection)this.options.get(name);
      }
   }

   public boolean verbose() {
      return this.hasOption("-verbose");
   }

   static class Option {
      public String name;
      public boolean hasValue;
      public boolean isCollection;

      public Option(String name, boolean hasValue, boolean isCollection) {
         this.name = name;
         this.hasValue = hasValue;
         this.isCollection = isCollection;
      }
   }
}
