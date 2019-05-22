package com.gzoltar.shaded.org.jacoco.core.runtime;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

public final class AgentOptions {
   public static final String DESTFILE = "destfile";
   public static final String DEFAULT_DESTFILE = "jacoco.exec";
   public static final String APPEND = "append";
   public static final String INCLUDES = "includes";
   public static final String EXCLUDES = "excludes";
   public static final String EXCLCLASSLOADER = "exclclassloader";
   public static final String INCLBOOTSTRAPCLASSES = "inclbootstrapclasses";
   public static final String INCLNOLOCATIONCLASSES = "inclnolocationclasses";
   public static final String SESSIONID = "sessionid";
   public static final String DUMPONEXIT = "dumponexit";
   public static final String OUTPUT = "output";
   private static final Pattern OPTION_SPLIT = Pattern.compile(",(?=[a-zA-Z0-9_\\-]+=)");
   public static final String ADDRESS = "address";
   public static final String DEFAULT_ADDRESS = null;
   public static final String PORT = "port";
   public static final int DEFAULT_PORT = 6300;
   public static final String CLASSDUMPDIR = "classdumpdir";
   public static final String JMX = "jmx";
   private static final Collection<String> VALID_OPTIONS = Arrays.asList("destfile", "append", "includes", "excludes", "exclclassloader", "inclbootstrapclasses", "inclnolocationclasses", "sessionid", "dumponexit", "output", "address", "port", "classdumpdir", "jmx");
   private final Map<String, String> options;

   public AgentOptions() {
      this.options = new HashMap();
   }

   public AgentOptions(String optionstr) {
      this();
      if (optionstr != null && optionstr.length() > 0) {
         String[] arr$ = OPTION_SPLIT.split(optionstr);
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String entry = arr$[i$];
            int pos = entry.indexOf(61);
            if (pos == -1) {
               throw new IllegalArgumentException(String.format("Invalid agent option syntax \"%s\".", optionstr));
            }

            String key = entry.substring(0, pos);
            if (!VALID_OPTIONS.contains(key)) {
               throw new IllegalArgumentException(String.format("Unknown agent option \"%s\".", key));
            }

            String value = entry.substring(pos + 1);
            this.setOption(key, value);
         }

         this.validateAll();
      }

   }

   public AgentOptions(Properties properties) {
      this();
      Iterator i$ = VALID_OPTIONS.iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         String value = properties.getProperty(key);
         if (value != null) {
            this.setOption(key, value);
         }
      }

   }

   private void validateAll() {
      this.validatePort(this.getPort());
      this.getOutput();
   }

   private void validatePort(int port) {
      if (port < 0) {
         throw new IllegalArgumentException("port must be positive");
      }
   }

   public String getDestfile() {
      return this.getOption("destfile", "jacoco.exec");
   }

   public void setDestfile(String destfile) {
      this.setOption("destfile", destfile);
   }

   public boolean getAppend() {
      return this.getOption("append", true);
   }

   public void setAppend(boolean append) {
      this.setOption("append", append);
   }

   public String getIncludes() {
      return this.getOption("includes", "*");
   }

   public void setIncludes(String includes) {
      this.setOption("includes", includes);
   }

   public String getExcludes() {
      return this.getOption("excludes", "");
   }

   public void setExcludes(String excludes) {
      this.setOption("excludes", excludes);
   }

   public String getExclClassloader() {
      return this.getOption("exclclassloader", "sun.reflect.DelegatingClassLoader");
   }

   public void setExclClassloader(String expression) {
      this.setOption("exclclassloader", expression);
   }

   public boolean getInclBootstrapClasses() {
      return this.getOption("inclbootstrapclasses", false);
   }

   public void setInclBootstrapClasses(boolean include) {
      this.setOption("inclbootstrapclasses", include);
   }

   public boolean getInclNoLocationClasses() {
      return this.getOption("inclnolocationclasses", false);
   }

   public void setInclNoLocationClasses(boolean include) {
      this.setOption("inclnolocationclasses", include);
   }

   public String getSessionId() {
      return this.getOption("sessionid", (String)null);
   }

   public void setSessionId(String id) {
      this.setOption("sessionid", id);
   }

   public boolean getDumpOnExit() {
      return this.getOption("dumponexit", true);
   }

   public void setDumpOnExit(boolean dumpOnExit) {
      this.setOption("dumponexit", dumpOnExit);
   }

   public int getPort() {
      return this.getOption("port", 6300);
   }

   public void setPort(int port) {
      this.validatePort(port);
      this.setOption("port", port);
   }

   public String getAddress() {
      return this.getOption("address", DEFAULT_ADDRESS);
   }

   public void setAddress(String address) {
      this.setOption("address", address);
   }

   public AgentOptions.OutputMode getOutput() {
      String value = (String)this.options.get("output");
      return value == null ? AgentOptions.OutputMode.file : AgentOptions.OutputMode.valueOf(value);
   }

   public void setOutput(String output) {
      this.setOutput(AgentOptions.OutputMode.valueOf(output));
   }

   public void setOutput(AgentOptions.OutputMode output) {
      this.setOption("output", output.name());
   }

   public String getClassDumpDir() {
      return this.getOption("classdumpdir", (String)null);
   }

   public void setClassDumpDir(String location) {
      this.setOption("classdumpdir", location);
   }

   public boolean getJmx() {
      return this.getOption("jmx", false);
   }

   public void setJmx(boolean jmx) {
      this.setOption("jmx", jmx);
   }

   private void setOption(String key, int value) {
      this.setOption(key, Integer.toString(value));
   }

   private void setOption(String key, boolean value) {
      this.setOption(key, Boolean.toString(value));
   }

   private void setOption(String key, String value) {
      this.options.put(key, value);
   }

   private String getOption(String key, String defaultValue) {
      String value = (String)this.options.get(key);
      return value == null ? defaultValue : value;
   }

   private boolean getOption(String key, boolean defaultValue) {
      String value = (String)this.options.get(key);
      return value == null ? defaultValue : Boolean.parseBoolean(value);
   }

   private int getOption(String key, int defaultValue) {
      String value = (String)this.options.get(key);
      return value == null ? defaultValue : Integer.parseInt(value);
   }

   public String getVMArgument(File agentJarFile) {
      return String.format("-javaagent:%s=%s", agentJarFile, this);
   }

   public String getQuotedVMArgument(File agentJarFile) {
      return CommandLineSupport.quote(this.getVMArgument(agentJarFile));
   }

   public String prependVMArguments(String arguments, File agentJarFile) {
      List<String> args = CommandLineSupport.split(arguments);
      String plainAgent = String.format("-javaagent:%s", agentJarFile);
      Iterator i = args.iterator();

      while(i.hasNext()) {
         if (((String)i.next()).startsWith(plainAgent)) {
            i.remove();
         }
      }

      args.add(0, this.getVMArgument(agentJarFile));
      return CommandLineSupport.quote(args);
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      Iterator i$ = VALID_OPTIONS.iterator();

      while(i$.hasNext()) {
         String key = (String)i$.next();
         String value = (String)this.options.get(key);
         if (value != null) {
            if (sb.length() > 0) {
               sb.append(',');
            }

            sb.append(key).append('=').append(value);
         }
      }

      return sb.toString();
   }

   public static enum OutputMode {
      file,
      tcpserver,
      tcpclient,
      none;
   }
}
