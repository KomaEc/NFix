package java_cup.anttask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java_cup.Main;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CUPTask extends Task {
   private String srcfile = null;
   private String parser = null;
   private String _package = null;
   private String symbols = null;
   private String destdir = null;
   private boolean _interface = false;
   private boolean nonterms = false;
   private String expect = null;
   private boolean compact_red = false;
   private boolean nowarn = false;
   private boolean nosummary = false;
   private boolean progress = false;
   private boolean dump_grammar = false;
   private boolean dump_states = false;
   private boolean dump_tables = false;
   private boolean dump = false;
   private boolean time = false;
   private boolean debug = false;
   private boolean debugsymbols = false;
   private boolean nopositions = false;
   private boolean xmlactions = false;
   private boolean genericlabels = false;
   private boolean locations = true;
   private boolean noscanner = false;
   private boolean force = false;
   private boolean quiet = false;

   public void execute() throws BuildException {
      List sc = new ArrayList();
      if (this.parser != null) {
         sc.add("-parser");
         sc.add(this.parser);
      } else {
         this.parser = "parser";
      }

      if (this._package != null) {
         sc.add("-package");
         sc.add(this._package);
      }

      if (this.symbols != null) {
         sc.add("-symbols");
         sc.add(this.symbols);
      } else {
         this.symbols = "sym";
      }

      if (this.expect != null) {
         sc.add("-expect");
         sc.add(this.expect);
      }

      if (this._interface) {
         sc.add("-interface");
      }

      if (this.nonterms) {
         sc.add("-nonterms");
      }

      if (this.compact_red) {
         sc.add("-compact_red");
      }

      if (this.nowarn) {
         sc.add("-nowarn");
      }

      if (this.nosummary) {
         sc.add("-nosummary");
      }

      if (this.progress) {
         sc.add("-progress");
      }

      if (this.dump_grammar) {
         sc.add("-dump_grammar");
      }

      if (this.dump_states) {
         sc.add("-dump_states");
      }

      if (this.dump_tables) {
         sc.add("-dump_tables");
      }

      if (this.dump) {
         sc.add("-dump");
      }

      if (this.time) {
         sc.add("-time");
      }

      if (this.debug) {
         sc.add("-debug");
      }

      if (this.debugsymbols) {
         sc.add("-debugsymbols");
      }

      if (this.nopositions) {
         sc.add("-nopositions");
      }

      if (this.locations) {
         sc.add("-locations");
      }

      if (this.genericlabels) {
         sc.add("-genericlabels");
      }

      if (this.xmlactions) {
         sc.add("-xmlactions");
      }

      if (this.noscanner) {
         sc.add("-noscanner");
      }

      if (!this.quiet) {
         this.log("This is CUP v0.11b beta 20140226");
      }

      if (!this.quiet) {
         this.log("Authors : Scott E. Hudson, Frank Flannery, Michael Petter and C. Scott Ananian");
      }

      if (!this.quiet) {
         this.log("Bugreports to petter@cs.tum.edu");
      }

      String packagename = this.inspect(this.srcfile);
      if (this.destdir == null) {
         this.destdir = System.getProperty("user.dir");
         if (!this.quiet) {
            this.log("No destination directory specified; using working directory: " + this.destdir);
         }
      }

      File dest = new File(this.destdir + packagename);
      if (!dest.exists()) {
         if (!this.quiet) {
            this.log("Destination directory didn't exist; creating new one: " + this.destdir + packagename);
         }

         dest.mkdirs();
         this.force = true;
      } else {
         if (this.force && !this.quiet) {
            this.log("anyway, this generation will be processed because of option force set to \"true\"");
         } else if (!this.quiet) {
            this.log("checking, whether this run is necessary");
         }

         File parserfile = new File(this.destdir + packagename, this.parser + ".java");
         File symfile = new File(this.destdir + packagename, this.symbols + ".java");
         File cupfile = new File(this.srcfile);
         if (parserfile.exists() && symfile.exists()) {
            if (!this.quiet) {
               this.log("Parserfile and symbolfile are existing");
            }
         } else {
            if (!this.quiet) {
               this.log("Either Parserfile or Symbolfile didn't exist");
            }

            this.force = true;
         }

         if (parserfile.lastModified() <= cupfile.lastModified()) {
            if (!this.quiet) {
               this.log("Parserfile " + parserfile + " isn't actual");
            }

            this.force = true;
         } else if (!this.quiet) {
            this.log("Parserfile " + parserfile + " is actual");
         }

         if (symfile.lastModified() <= cupfile.lastModified()) {
            if (!this.quiet) {
               this.log("Symbolfile " + symfile + " isn't actual");
            }

            this.force = true;
         } else if (!this.quiet) {
            this.log("Symbolfile" + symfile + " is actual");
         }

         if (!this.force) {
            if (!this.quiet) {
               this.log("skipping generation of " + this.srcfile);
            }

            if (!this.quiet) {
               this.log("use option force=\"true\" to override");
            }

            return;
         }
      }

      sc.add("-destdir");
      sc.add(dest.getAbsolutePath());
      if (this.srcfile == null) {
         throw new BuildException("Input file needed: Specify <cup srcfile=\"myfile.cup\"> ");
      } else if (!(new File(this.srcfile)).exists()) {
         throw new BuildException("Input file not found: srcfile=\"" + this.srcfile + "\" ");
      } else {
         sc.add(this.srcfile);
         String[] args = new String[sc.size()];

         for(int i = 0; i < args.length; ++i) {
            args[i] = (String)sc.get(i);
         }

         try {
            Main.main(args);
         } catch (Exception var7) {
            this.log("CUP error occured int CUP task: " + var7);
         }

      }
   }

   protected String inspect(String cupfile) {
      try {
         BufferedReader br = new BufferedReader(new FileReader(cupfile));

         while(br.ready()) {
            String line = br.readLine();
            if (line.startsWith("package") && line.indexOf(";") != -1) {
               String result = line.substring(8, line.indexOf(";"));
               result = result.replace('.', System.getProperty("file.separator").charAt(0));
               return System.getProperty("file.separator") + result;
            }
         }
      } catch (IOException var5) {
      }

      return "";
   }

   public boolean getQuiet() {
      return this.quiet;
   }

   public void setQuiet(boolean argquiet) {
      this.quiet = argquiet;
   }

   public boolean getForce() {
      return this.force;
   }

   public void setForce(boolean argforce) {
      this.force = argforce;
   }

   public String getPackage() {
      return this._package;
   }

   public void setPackage(String arg_package) {
      this._package = arg_package;
   }

   public String getDestdir() {
      return this.destdir;
   }

   public void setDestdir(String destdir) {
      this.destdir = destdir;
   }

   public boolean isInterface() {
      return this._interface;
   }

   public void setInterface(boolean arg_interface) {
      this._interface = arg_interface;
   }

   public String getSrcfile() {
      return this.srcfile;
   }

   public void setSrcfile(String newSrcfile) {
      this.srcfile = newSrcfile;
   }

   public String getParser() {
      return this.parser;
   }

   public void setParser(String argParser) {
      this.parser = argParser;
   }

   public String getSymbols() {
      return this.symbols;
   }

   public void setSymbols(String argSymbols) {
      this.symbols = argSymbols;
   }

   public boolean isNonterms() {
      return this.nonterms;
   }

   public void setNonterms(boolean argNonterms) {
      this.nonterms = argNonterms;
   }

   public String getExpect() {
      return this.expect;
   }

   public void setExpect(String argExpect) {
      this.expect = argExpect;
   }

   public boolean isCompact_red() {
      return this.compact_red;
   }

   public void setCompact_red(boolean argCompact_red) {
      this.compact_red = argCompact_red;
   }

   public boolean isNowarn() {
      return this.nowarn;
   }

   public void setNowarn(boolean argNowarn) {
      this.nowarn = argNowarn;
   }

   public boolean isNosummary() {
      return this.nosummary;
   }

   public void setNosummary(boolean argNosummary) {
      this.nosummary = argNosummary;
   }

   public boolean isProgress() {
      return this.progress;
   }

   public void setProgress(boolean argProgress) {
      this.progress = argProgress;
   }

   public boolean isDump_grammar() {
      return this.dump_grammar;
   }

   public void setDump_grammar(boolean argDump_grammar) {
      this.dump_grammar = argDump_grammar;
   }

   public boolean isDump_states() {
      return this.dump_states;
   }

   public void setDump_states(boolean argDump_states) {
      this.dump_states = argDump_states;
   }

   public boolean isDump_tables() {
      return this.dump_tables;
   }

   public void setDump_tables(boolean argDump_tables) {
      this.dump_tables = argDump_tables;
   }

   public boolean isDump() {
      return this.dump;
   }

   public void setDump(boolean argDump) {
      this.dump = argDump;
   }

   public boolean isTime() {
      return this.time;
   }

   public void setTime(boolean argTime) {
      this.time = argTime;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean argDebug) {
      this.debug = argDebug;
   }

   public boolean isDebugSymbols() {
      return this.debugsymbols;
   }

   public void setDebugSymbols(boolean argDebug) {
      this.debugsymbols = argDebug;
   }

   public boolean isNopositions() {
      return this.nopositions;
   }

   public void setNopositions(boolean argNopositions) {
      this.nopositions = argNopositions;
   }

   public boolean isLocations() {
      return this.locations;
   }

   public void setLocations(boolean argLocations) {
      this.locations = argLocations;
   }

   public boolean isNoscanner() {
      return this.noscanner;
   }

   public void setNoscanner(boolean argNoscanner) {
      this.noscanner = argNoscanner;
   }

   public boolean isXmlactions() {
      return this.xmlactions;
   }

   public void setXmlactions(boolean xmlactions) {
      this.xmlactions = xmlactions;
   }

   public boolean isGenericlabels() {
      return this.genericlabels;
   }

   public void setGenericlabels(boolean genericlabels) {
      this.genericlabels = genericlabels;
   }
}
