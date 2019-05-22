package soot.options;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import soot.HasPhaseOptions;
import soot.Pack;
import soot.PackManager;
import soot.PhaseOptions;
import soot.plugins.internal.PluginLoader;

abstract class OptionsBase {
   private final Deque<String> options = new LinkedList();
   protected LinkedList<String> classes = new LinkedList();

   private String pad(int initial, String opts, int tab, String desc) {
      StringBuffer b = new StringBuffer();

      int i;
      for(i = 0; i < initial; ++i) {
         b.append(" ");
      }

      b.append(opts);
      if (tab <= opts.length()) {
         b.append("\n");
         i = 0;
      } else {
         i = opts.length() + initial;
      }

      while(i <= tab) {
         b.append(" ");
         ++i;
      }

      String s;
      for(StringTokenizer t = new StringTokenizer(desc); t.hasMoreTokens(); i += s.length() + 1) {
         s = t.nextToken();
         if (i + s.length() > 78) {
            b.append("\n");

            for(i = 0; i <= tab; ++i) {
               b.append(" ");
            }
         }

         b.append(s);
         b.append(" ");
      }

      b.append("\n");
      return b.toString();
   }

   protected String padOpt(String opts, String desc) {
      return this.pad(1, opts, 30, desc);
   }

   protected String padVal(String vals, String desc) {
      return this.pad(4, vals, 32, desc);
   }

   protected String getPhaseUsage() {
      StringBuffer b = new StringBuffer();
      b.append("\nPhases and phase options:\n");
      Iterator var2 = PackManager.v().allPacks().iterator();

      while(var2.hasNext()) {
         Pack p = (Pack)var2.next();
         b.append(this.padOpt(p.getPhaseName(), p.getDeclaredOptions()));
         Iterator phIt = p.iterator();

         while(phIt.hasNext()) {
            HasPhaseOptions ph = (HasPhaseOptions)phIt.next();
            b.append(this.padVal(ph.getPhaseName(), ph.getDeclaredOptions()));
         }
      }

      return b.toString();
   }

   protected void pushOption(String option) {
      this.options.push(option);
   }

   protected boolean hasMoreOptions() {
      return !this.options.isEmpty();
   }

   protected String nextOption() {
      return (String)this.options.removeFirst();
   }

   public LinkedList<String> classes() {
      return this.classes;
   }

   public boolean setPhaseOption(String phase, String option) {
      return PhaseOptions.v().processPhaseOptions(phase, option);
   }

   protected boolean loadPluginConfiguration(String file) {
      return PluginLoader.load(file);
   }
}
