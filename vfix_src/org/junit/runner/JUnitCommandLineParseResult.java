package org.junit.runner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.internal.Classes;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.InitializationError;

class JUnitCommandLineParseResult {
   private final List<String> filterSpecs = new ArrayList();
   private final List<Class<?>> classes = new ArrayList();
   private final List<Throwable> parserErrors = new ArrayList();

   public List<String> getFilterSpecs() {
      return Collections.unmodifiableList(this.filterSpecs);
   }

   public List<Class<?>> getClasses() {
      return Collections.unmodifiableList(this.classes);
   }

   public static JUnitCommandLineParseResult parse(String[] args) {
      JUnitCommandLineParseResult result = new JUnitCommandLineParseResult();
      result.parseArgs(args);
      return result;
   }

   private void parseArgs(String[] args) {
      this.parseParameters(this.parseOptions(args));
   }

   String[] parseOptions(String... args) {
      for(int i = 0; i != args.length; ++i) {
         String arg = args[i];
         if (arg.equals("--")) {
            return this.copyArray(args, i + 1, args.length);
         }

         if (!arg.startsWith("--")) {
            return this.copyArray(args, i, args.length);
         }

         if (!arg.startsWith("--filter=") && !arg.equals("--filter")) {
            this.parserErrors.add(new JUnitCommandLineParseResult.CommandLineParserError("JUnit knows nothing about the " + arg + " option"));
         } else {
            String filterSpec;
            if (arg.equals("--filter")) {
               ++i;
               if (i >= args.length) {
                  this.parserErrors.add(new JUnitCommandLineParseResult.CommandLineParserError(arg + " value not specified"));
                  return new String[0];
               }

               filterSpec = args[i];
            } else {
               filterSpec = arg.substring(arg.indexOf(61) + 1);
            }

            this.filterSpecs.add(filterSpec);
         }
      }

      return new String[0];
   }

   private String[] copyArray(String[] args, int from, int to) {
      ArrayList<String> result = new ArrayList();

      for(int j = from; j != to; ++j) {
         result.add(args[j]);
      }

      return (String[])result.toArray(new String[result.size()]);
   }

   void parseParameters(String[] args) {
      String[] arr$ = args;
      int len$ = args.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String arg = arr$[i$];

         try {
            this.classes.add(Classes.getClass(arg));
         } catch (ClassNotFoundException var7) {
            this.parserErrors.add(new IllegalArgumentException("Could not find class [" + arg + "]", var7));
         }
      }

   }

   private Request errorReport(Throwable cause) {
      return Request.errorReport(JUnitCommandLineParseResult.class, cause);
   }

   public Request createRequest(Computer computer) {
      if (this.parserErrors.isEmpty()) {
         Request request = Request.classes(computer, (Class[])this.classes.toArray(new Class[this.classes.size()]));
         return this.applyFilterSpecs(request);
      } else {
         return this.errorReport(new InitializationError(this.parserErrors));
      }
   }

   private Request applyFilterSpecs(Request request) {
      try {
         Filter filter;
         for(Iterator i$ = this.filterSpecs.iterator(); i$.hasNext(); request = request.filterWith(filter)) {
            String filterSpec = (String)i$.next();
            filter = FilterFactories.createFilterFromFilterSpec(request, filterSpec);
         }

         return request;
      } catch (FilterFactory.FilterNotCreatedException var5) {
         return this.errorReport(var5);
      }
   }

   public static class CommandLineParserError extends Exception {
      private static final long serialVersionUID = 1L;

      public CommandLineParserError(String message) {
         super(message);
      }
   }
}
