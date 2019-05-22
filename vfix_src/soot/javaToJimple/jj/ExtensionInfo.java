package soot.javaToJimple.jj;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.frontend.Source;
import polyglot.main.Options;
import polyglot.types.TypeSystem;
import soot.javaToJimple.jj.ast.JjNodeFactory_c;
import soot.javaToJimple.jj.types.JjTypeSystem_c;

public class ExtensionInfo extends polyglot.ext.jl.ExtensionInfo {
   private HashMap<Source, Job> sourceJobMap;

   public String defaultFileExtension() {
      return "jj";
   }

   public String compilerName() {
      return "jjc";
   }

   protected NodeFactory createNodeFactory() {
      return new JjNodeFactory_c();
   }

   protected TypeSystem createTypeSystem() {
      return new JjTypeSystem_c();
   }

   public List passes(Job job) {
      List passes = super.passes(job);
      return passes;
   }

   public HashMap<Source, Job> sourceJobMap() {
      return this.sourceJobMap;
   }

   public void sourceJobMap(HashMap<Source, Job> map) {
      this.sourceJobMap = map;
   }

   protected Options createOptions() {
      return new Options(this) {
         public String constructFullClasspath() {
            String cp = super.constructFullClasspath();
            cp = cp + File.pathSeparator + soot.options.Options.v().soot_classpath();
            return cp;
         }
      };
   }

   static {
      new Topics();
   }
}
