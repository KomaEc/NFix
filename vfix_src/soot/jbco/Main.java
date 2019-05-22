package soot.jbco;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Local;
import soot.Pack;
import soot.PackManager;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Transformer;
import soot.jbco.bafTransformations.AddJSRs;
import soot.jbco.bafTransformations.BAFCounter;
import soot.jbco.bafTransformations.BAFPrintout;
import soot.jbco.bafTransformations.BafLineNumberer;
import soot.jbco.bafTransformations.ConstructorConfuser;
import soot.jbco.bafTransformations.FindDuplicateSequences;
import soot.jbco.bafTransformations.FixUndefinedLocals;
import soot.jbco.bafTransformations.IfNullToTryCatch;
import soot.jbco.bafTransformations.IndirectIfJumpsToCaughtGotos;
import soot.jbco.bafTransformations.Jimple2BafLocalBuilder;
import soot.jbco.bafTransformations.LocalsToBitField;
import soot.jbco.bafTransformations.MoveLoadsAboveIfs;
import soot.jbco.bafTransformations.RemoveRedundantPushStores;
import soot.jbco.bafTransformations.TryCatchCombiner;
import soot.jbco.bafTransformations.UpdateConstantsToFields;
import soot.jbco.bafTransformations.WrapSwitchesInTrys;
import soot.jbco.jimpleTransformations.AddSwitches;
import soot.jbco.jimpleTransformations.ArithmeticTransformer;
import soot.jbco.jimpleTransformations.BuildIntermediateAppClasses;
import soot.jbco.jimpleTransformations.ClassRenamer;
import soot.jbco.jimpleTransformations.CollectConstants;
import soot.jbco.jimpleTransformations.CollectJimpleLocals;
import soot.jbco.jimpleTransformations.FieldRenamer;
import soot.jbco.jimpleTransformations.GotoInstrumenter;
import soot.jbco.jimpleTransformations.LibraryMethodWrappersBuilder;
import soot.jbco.jimpleTransformations.MethodRenamer;
import soot.tagkit.LineNumberTagAggregator;

public class Main {
   private static final Logger logger = LoggerFactory.getLogger(Main.class);
   public static boolean jbcoDebug = false;
   public static boolean jbcoSummary = true;
   public static boolean jbcoVerbose = false;
   public static boolean metrics = false;
   public static Map<String, Integer> transformsToWeights = new ConcurrentHashMap();
   public static Map<String, Map<Object, Integer>> transformsToMethodsToWeights = new ConcurrentHashMap();
   public static Map method2Locals2REALTypes = new ConcurrentHashMap();
   public static Map<SootMethod, Map<Local, Local>> methods2Baf2JLocals = new ConcurrentHashMap();
   public static Map<SootMethod, List<Local>> methods2JLocals = new ConcurrentHashMap();
   public static List<SootClass> IntermediateAppClasses = new CopyOnWriteArrayList();
   static List<Transformer> jbcotransforms = new CopyOnWriteArrayList();
   static String[][] optionStrings = new String[][]{{"Rename Classes", "Rename Methods", "Rename Fields", "Build API Buffer Methods", "Build Library Buffer Classes", "Goto Instruction Augmentation", "Add Dead Switche Statements", "Convert Arith. Expr. To Bitshifting Ops", "Convert Branches to JSR Instructions", "Disobey Constructor Conventions", "Reuse Duplicate Sequences", "Replace If(Non)Nulls with Try-Catch", "Indirect If Instructions", "Pack Locals into Bitfields", "Reorder Loads Above Ifs", "Combine Try and Catch Blocks", "Embed Constants in Fields", "Partially Trap Switches"}, {"wjtp.jbco_cr", "wjtp.jbco_mr", "wjtp.jbco_fr", "wjtp.jbco_blbc", "wjtp.jbco_bapibm", "jtp.jbco_gia", "jtp.jbco_adss", "jtp.jbco_cae2bo", "bb.jbco_cb2ji", "bb.jbco_dcc", "bb.jbco_rds", "bb.jbco_riitcb", "bb.jbco_iii", "bb.jbco_plvb", "bb.jbco_rlaii", "bb.jbco_ctbcb", "bb.jbco_ecvf", "bb.jbco_ptss"}};

   public static void main(String[] argv) {
      int rcount = 0;
      Vector<String> transformsToAdd = new Vector();
      boolean[] remove = new boolean[argv.length];

      int i;
      int tweight;
      String jl;
      int i;
      for(i = 0; i < argv.length; ++i) {
         String arg = argv[i];
         if (!arg.equals("-jbco:help")) {
            if (arg.equals("-jbco:metrics")) {
               metrics = true;
               remove[i] = true;
               ++rcount;
            } else if (arg.startsWith("-jbco:name:")) {
               remove[i] = true;
               ++rcount;
            } else if (arg.startsWith("-t:")) {
               arg = arg.substring(3);
               tweight = 9;
               char cweight = arg.charAt(0);
               if (cweight >= '0' && cweight <= '9') {
                  try {
                     tweight = Integer.parseInt(arg.substring(0, 1));
                  } catch (NumberFormatException var15) {
                     logger.debug("Improperly formated transformation weight: " + argv[i]);
                     System.exit(1);
                  }

                  arg = arg.substring(arg.indexOf(58) + 1);
               }

               transformsToAdd.add(arg);
               transformsToWeights.put(arg, new Integer(tweight));
               if (arg.equals("wjtp.jbco_fr")) {
                  FieldRenamer.rename_fields = true;
               }

               remove[i] = true;
               ++rcount;
            } else if (arg.equals("-jbco:verbose")) {
               jbcoVerbose = true;
               remove[i] = true;
               ++rcount;
            } else if (arg.equals("-jbco:silent")) {
               jbcoSummary = false;
               jbcoVerbose = false;
               remove[i] = true;
               ++rcount;
            } else if (arg.equals("-jbco:debug")) {
               jbcoDebug = true;
               remove[i] = true;
               ++rcount;
            } else if (arg.startsWith("-i") && arg.length() > 4 && arg.charAt(3) == ':' && arg.charAt(2) == 't') {
               Object o = null;
               arg = arg.substring(4);
               int tweight = 0;
               char cweight = arg.charAt(0);
               if (cweight >= '0' && cweight <= '9') {
                  try {
                     tweight = Integer.parseInt(arg.substring(0, 1));
                  } catch (NumberFormatException var14) {
                     logger.debug("Improperly formatted transformation weight: " + argv[i]);
                     System.exit(1);
                  }

                  if (arg.indexOf(58) < 0) {
                     logger.debug("Illegally Formatted Option: " + argv[i]);
                     System.exit(1);
                  }

                  arg = arg.substring(arg.indexOf(58) + 1);
               }

               int index = arg.indexOf(58);
               if (index < 0) {
                  logger.debug("Illegally Formatted Option: " + argv[i]);
                  System.exit(1);
               }

               String trans = arg.substring(0, index);
               arg = arg.substring(index + 1, arg.length());
               if (arg.startsWith("*")) {
                  arg = arg.substring(1);

                  try {
                     o = Pattern.compile(arg);
                  } catch (PatternSyntaxException var13) {
                     logger.debug("Illegal Regular Expression Pattern: " + arg);
                     System.exit(1);
                  }
               } else {
                  o = arg;
               }

               Map<Object, Integer> htmp = (Map)transformsToMethodsToWeights.get(trans);
               if (htmp == null) {
                  htmp = new HashMap();
                  transformsToMethodsToWeights.put(trans, htmp);
               }

               ((Map)htmp).put(o, new Integer(tweight));
               remove[i] = true;
               ++rcount;
            } else {
               remove[i] = false;
            }
         } else {
            System.out.println("The Java Bytecode Obfuscator (JBCO)\n\nGeneral Options:");
            System.out.println("\t-jbco:help     -  print this help message.");
            System.out.println("\t-jbco:verbose  -  print extra information during obfuscation.");
            System.out.println("\t-jbco:silent   -  turn off all output, including summary information.");
            System.out.println("\t-jbco:metrics  -  calculate total vertices and edges;\n\t                  calculate avg. and highest graph degrees.");
            System.out.println("\t-jbco:debug    -  turn on extra debugging like\n\t                  stack height and type verifier.\n\nTransformations ( -t:[W:]<name>[:pattern] )\n\tW              -  specify obfuscation weight (0-9)\n\t<name>         -  name of obfuscation (from list below)\n\tpattern        -  limit to method names matched by pattern\n\t                  prepend * to pattern if a regex\n");

            for(tweight = 0; tweight < optionStrings[0].length; ++tweight) {
               jl = "\t" + optionStrings[1][tweight];

               for(i = 20 - jl.length(); i-- > 0; jl = jl + " ") {
               }

               jl = jl + "-  " + optionStrings[0][tweight];
               System.out.println(jl);
            }

            System.exit(0);
         }
      }

      if (rcount > 0) {
         i = 0;
         String[] tmp = (String[])((String[])argv.clone());
         argv = new String[argv.length - rcount];

         for(tweight = 0; tweight < tmp.length; ++tweight) {
            if (!remove[tweight]) {
               argv[i++] = tmp[tweight];
            }
         }
      }

      transformsToAdd.remove("wjtp.jbco_cc");
      transformsToAdd.remove("jtp.jbco_jl");
      transformsToAdd.remove("bb.jbco_j2bl");
      transformsToAdd.remove("bb.jbco_ful");
      if (!metrics) {
         if (transformsToAdd.size() == 0) {
            logger.debug("No Jbco tasks to complete.  Shutting Down...");
            System.exit(0);
         }

         Pack wjtp = PackManager.v().getPack("wjtp");
         Pack jtp = PackManager.v().getPack("jtp");
         Pack bb = PackManager.v().getPack("bb");
         if (transformsToAdd.contains("jtp.jbco_adss")) {
            wjtp.add(new Transform("wjtp.jbco_fr", newTransform((Transformer)getTransform("wjtp.jbco_fr"))));
            if (transformsToAdd.remove("wjtp.jbco_fr")) {
               FieldRenamer.rename_fields = true;
            }
         }

         if (transformsToAdd.contains("bb.jbco_ecvf")) {
            wjtp.add(new Transform("wjtp.jbco_cc", newTransform((Transformer)getTransform("wjtp.jbco_cc"))));
         }

         jl = null;

         for(i = 0; i < transformsToAdd.size(); ++i) {
            if (((String)transformsToAdd.get(i)).startsWith("bb")) {
               jl = "jtp.jbco_jl";
               jtp.add(new Transform(jl, newTransform((Transformer)getTransform(jl))));
               bb.insertBefore(new Transform("bb.jbco_j2bl", newTransform((Transformer)getTransform("bb.jbco_j2bl"))), "bb.lso");
               bb.insertBefore(new Transform("bb.jbco_ful", newTransform((Transformer)getTransform("bb.jbco_ful"))), "bb.lso");
               bb.add(new Transform("bb.jbco_rrps", newTransform((Transformer)getTransform("bb.jbco_rrps"))));
               break;
            }
         }

         for(i = 0; i < transformsToAdd.size(); ++i) {
            String tname = (String)transformsToAdd.get(i);
            IJbcoTransform t = getTransform(tname);
            Pack p = tname.startsWith("wjtp") ? wjtp : (tname.startsWith("jtp") ? jtp : bb);
            String insertBefore = p == jtp ? jl : (p == bb ? "bb.jbco_ful" : null);
            if (insertBefore != null) {
               p.insertBefore(new Transform(tname, newTransform((Transformer)t)), insertBefore);
            } else {
               p.add(new Transform(tname, newTransform((Transformer)t)));
            }
         }

         Iterator phases = wjtp.iterator();

         while(phases.hasNext()) {
            if (((Transform)phases.next()).getPhaseName().indexOf("jbco") > 0) {
               argv = checkWhole(argv, true);
               break;
            }
         }

         if (jbcoSummary) {
            for(i = 0; i < 3; ++i) {
               Iterator<Transform> phases = i == 0 ? wjtp.iterator() : (i == 1 ? jtp.iterator() : bb.iterator());
               logger.debug(i == 0 ? "Whole Program Jimple Transformations:" : (i == 1 ? "Jimple Method Body Transformations:" : "Baf Method Body Transformations:"));

               while(phases.hasNext()) {
                  Transform o = (Transform)phases.next();
                  Transformer t = o.getTransformer();
                  if (t instanceof IJbcoTransform) {
                     logger.debug("\t" + ((IJbcoTransform)t).getName() + "  JBCO");
                  } else {
                     logger.debug("\t" + o.getPhaseName() + "  default");
                  }
               }
            }
         }

         bb.add(new Transform("bb.jbco_bln", new BafLineNumberer()));
         bb.add(new Transform("bb.jbco_lta", LineNumberTagAggregator.v()));
      } else {
         argv = checkWhole(argv, false);
      }

      soot.Main.main(argv);
      if (jbcoSummary) {
         logger.debug("\n***** JBCO SUMMARY *****\n");
         Iterator tit = jbcotransforms.iterator();

         while(tit.hasNext()) {
            Object o = tit.next();
            if (o instanceof IJbcoTransform) {
               ((IJbcoTransform)o).outputSummary();
            }
         }

         logger.debug("\n***** END SUMMARY *****\n");
      }

   }

   private static String[] checkWhole(String[] argv, boolean add) {
      for(int i = 0; i < argv.length; ++i) {
         if (argv[i].equals("-w")) {
            if (add) {
               return argv;
            }

            String[] tmp = new String[argv.length - 1];
            if (i == 0) {
               System.arraycopy(argv, 1, tmp, 0, tmp.length);
            } else {
               System.arraycopy(argv, 0, tmp, 0, i);
               if (i < argv.length - 1) {
                  System.arraycopy(argv, i + 1, tmp, i, tmp.length - i);
               }
            }

            return tmp;
         }
      }

      if (add) {
         String[] tmp = new String[argv.length + 1];
         tmp[0] = "-w";
         System.arraycopy(argv, 0, tmp, 1, argv.length);
         return tmp;
      } else {
         return argv;
      }
   }

   private static Transformer newTransform(Transformer t) {
      jbcotransforms.add(t);
      return t;
   }

   private static IJbcoTransform getTransform(String name) {
      if (name.startsWith("bb.jbco_rrps")) {
         return new RemoveRedundantPushStores();
      } else if (name.startsWith("bb.printout")) {
         return new BAFPrintout(name, true);
      } else if (name.equals("bb.jbco_j2bl")) {
         return new Jimple2BafLocalBuilder();
      } else if (name.equals("jtp.jbco_gia")) {
         return new GotoInstrumenter();
      } else if (name.equals("jtp.jbco_cae2bo")) {
         return new ArithmeticTransformer();
      } else if (name.equals("wjtp.jbco_cc")) {
         return new CollectConstants();
      } else if (name.equals("bb.jbco_ecvf")) {
         return new UpdateConstantsToFields();
      } else if (name.equals("bb.jbco_rds")) {
         return new FindDuplicateSequences();
      } else if (name.equals("bb.jbco_plvb")) {
         return new LocalsToBitField();
      } else if (name.equals("bb.jbco_rlaii")) {
         return new MoveLoadsAboveIfs();
      } else if (name.equals("bb.jbco_ptss")) {
         return new WrapSwitchesInTrys();
      } else if (name.equals("bb.jbco_iii")) {
         return new IndirectIfJumpsToCaughtGotos();
      } else if (name.equals("bb.jbco_ctbcb")) {
         return new TryCatchCombiner();
      } else if (name.equals("bb.jbco_cb2ji")) {
         return new AddJSRs();
      } else if (name.equals("bb.jbco_riitcb")) {
         return new IfNullToTryCatch();
      } else if (name.equals("wjtp.jbco_blbc")) {
         return new LibraryMethodWrappersBuilder();
      } else if (name.equals("wjtp.jbco_bapibm")) {
         return new BuildIntermediateAppClasses();
      } else if (name.equals("wjtp.jbco_cr")) {
         return ClassRenamer.v();
      } else if (name.equals("bb.jbco_ful")) {
         return new FixUndefinedLocals();
      } else if (name.equals("wjtp.jbco_fr")) {
         return new FieldRenamer();
      } else if (name.equals("wjtp.jbco_mr")) {
         return MethodRenamer.v();
      } else if (name.equals("jtp.jbco_adss")) {
         return new AddSwitches();
      } else if (name.equals("jtp.jbco_jl")) {
         return new CollectJimpleLocals();
      } else if (name.equals("bb.jbco_dcc")) {
         return new ConstructorConfuser();
      } else {
         return name.equals("bb.jbco_counter") ? new BAFCounter() : null;
      }
   }

   private static int getWeight(String phasename) {
      int weight = 9;
      Integer intg = (Integer)transformsToWeights.get(phasename);
      if (intg != null) {
         weight = intg;
      }

      return weight;
   }

   public static int getWeight(String phasename, String method) {
      Map<Object, Integer> htmp = (Map)transformsToMethodsToWeights.get(phasename);
      int result = 10;
      if (htmp != null) {
         Iterator keys = htmp.keySet().iterator();

         while(keys.hasNext()) {
            Integer intg = null;
            Object o = keys.next();
            if (o instanceof Pattern) {
               if (((Pattern)o).matcher(method).matches()) {
                  intg = (Integer)htmp.get(o);
               }
            } else if (o instanceof String && method.equals(o)) {
               intg = (Integer)htmp.get(o);
            }

            if (intg != null && intg < result) {
               result = intg;
            }
         }
      }

      if (result > 9 || result < 0) {
         result = getWeight(phasename);
      }

      if (jbcoVerbose) {
         logger.debug("[" + phasename + "] Processing " + method + " with weight: " + result);
      }

      return result;
   }
}
