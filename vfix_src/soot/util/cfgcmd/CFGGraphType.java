package soot.util.cfgcmd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.toolkits.graph.ArrayRefBlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.ClassicCompleteBlockGraph;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.CompleteBlockGraph;
import soot.toolkits.graph.CompleteUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalBlockGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.TrapUnitGraph;
import soot.toolkits.graph.ZonedBlockGraph;
import soot.util.dot.DotGraph;

public abstract class CFGGraphType extends CFGOptionMatcher.CFGOption {
   private static final Logger logger = LoggerFactory.getLogger(CFGGraphType.class);
   private static final boolean DEBUG = true;
   public static final CFGGraphType BRIEF_UNIT_GRAPH = new CFGGraphType("BriefUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new BriefUnitGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType EXCEPTIONAL_UNIT_GRAPH = new CFGGraphType("ExceptionalUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new ExceptionalUnitGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG((ExceptionalUnitGraph)g);
      }
   };
   public static final CFGGraphType COMPLETE_UNIT_GRAPH = new CFGGraphType("CompleteUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new CompleteUnitGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG((CompleteUnitGraph)g);
      }
   };
   public static final CFGGraphType TRAP_UNIT_GRAPH = new CFGGraphType("TrapUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new TrapUnitGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType CLASSIC_COMPLETE_UNIT_GRAPH = new CFGGraphType("ClassicCompleteUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new ClassicCompleteUnitGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType BRIEF_BLOCK_GRAPH = new CFGGraphType("BriefBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new BriefBlockGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType EXCEPTIONAL_BLOCK_GRAPH = new CFGGraphType("ExceptionalBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new ExceptionalBlockGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG((ExceptionalBlockGraph)g);
      }
   };
   public static final CFGGraphType COMPLETE_BLOCK_GRAPH = new CFGGraphType("CompleteBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new CompleteBlockGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType CLASSIC_COMPLETE_BLOCK_GRAPH = new CFGGraphType("ClassicCompleteBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new ClassicCompleteBlockGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ARRAY_REF_BLOCK_GRAPH = new CFGGraphType("ArrayRefBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new ArrayRefBlockGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ZONED_BLOCK_GRAPH = new CFGGraphType("ZonedBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return new ZonedBlockGraph(b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_BRIEF_UNIT_GRAPH = new CFGGraphType("AltBriefUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.BriefUnitGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_COMPLETE_UNIT_GRAPH = new CFGGraphType("AltCompleteUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.CompleteUnitGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_TRAP_UNIT_GRAPH = new CFGGraphType("AltTrapUnitGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.TrapUnitGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_ARRAY_REF_BLOCK_GRAPH = new CFGGraphType("AltArrayRefBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.ArrayRefBlockGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_BRIEF_BLOCK_GRAPH = new CFGGraphType("AltBriefBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.BriefBlockGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_COMPLETE_BLOCK_GRAPH = new CFGGraphType("AltCompleteBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.CompleteBlockGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   public static final CFGGraphType ALT_ZONED_BLOCK_GRAPH = new CFGGraphType("AltZonedBlockGraph") {
      public DirectedGraph buildGraph(Body b) {
         return CFGGraphType.loadAltGraph("soot.toolkits.graph.ZonedBlockGraph", b);
      }

      public DotGraph drawGraph(CFGToDotGraph drawer, DirectedGraph g, Body b) {
         return drawer.drawCFG(g, b);
      }
   };
   private static final CFGOptionMatcher graphTypeOptions;

   public abstract DirectedGraph buildGraph(Body var1);

   public abstract DotGraph drawGraph(CFGToDotGraph var1, DirectedGraph var2, Body var3);

   private CFGGraphType(String name) {
      super(name);
   }

   public static CFGGraphType getGraphType(String option) {
      return (CFGGraphType)graphTypeOptions.match(option);
   }

   public static String help(int initialIndent, int rightMargin, int hangingIndent) {
      return graphTypeOptions.help(initialIndent, rightMargin, hangingIndent);
   }

   private static DirectedGraph loadAltGraph(String className, Body b) {
      try {
         Class<?> graphClass = AltClassLoader.v().loadClass(className);
         Class<?>[] paramTypes = new Class[]{Body.class};
         Constructor constructor = graphClass.getConstructor(paramTypes);
         DirectedGraph result = (DirectedGraph)constructor.newInstance(b);
         return result;
      } catch (ClassNotFoundException var6) {
         logger.error((String)var6.getMessage(), (Throwable)var6);
         throw new IllegalArgumentException("Unable to find " + className + " in alternate classpath: " + var6.getMessage());
      } catch (NoSuchMethodException var7) {
         logger.error((String)var7.getMessage(), (Throwable)var7);
         throw new IllegalArgumentException("There is no " + className + "(Body) constructor: " + var7.getMessage());
      } catch (InstantiationException var8) {
         logger.error((String)var8.getMessage(), (Throwable)var8);
         throw new IllegalArgumentException("Unable to instantiate " + className + " in alternate classpath: " + var8.getMessage());
      } catch (IllegalAccessException var9) {
         logger.error((String)var9.getMessage(), (Throwable)var9);
         throw new IllegalArgumentException("Unable to access " + className + "(Body) in alternate classpath: " + var9.getMessage());
      } catch (InvocationTargetException var10) {
         logger.error((String)var10.getMessage(), (Throwable)var10);
         throw new IllegalArgumentException("Unable to invoke " + className + "(Body) in alternate classpath: " + var10.getMessage());
      }
   }

   // $FF: synthetic method
   CFGGraphType(String x0, Object x1) {
      this(x0);
   }

   static {
      graphTypeOptions = new CFGOptionMatcher(new CFGGraphType[]{BRIEF_UNIT_GRAPH, EXCEPTIONAL_UNIT_GRAPH, COMPLETE_UNIT_GRAPH, TRAP_UNIT_GRAPH, CLASSIC_COMPLETE_UNIT_GRAPH, BRIEF_BLOCK_GRAPH, EXCEPTIONAL_BLOCK_GRAPH, COMPLETE_BLOCK_GRAPH, CLASSIC_COMPLETE_BLOCK_GRAPH, ARRAY_REF_BLOCK_GRAPH, ZONED_BLOCK_GRAPH, ALT_ARRAY_REF_BLOCK_GRAPH, ALT_BRIEF_UNIT_GRAPH, ALT_COMPLETE_UNIT_GRAPH, ALT_TRAP_UNIT_GRAPH, ALT_BRIEF_BLOCK_GRAPH, ALT_COMPLETE_BLOCK_GRAPH, ALT_ZONED_BLOCK_GRAPH});
   }
}
