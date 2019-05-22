package soot.jimple.spark.ondemand.pautil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.CompilationDeathException;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.ondemand.genericutil.ArraySet;
import soot.jimple.spark.ondemand.genericutil.ArraySetMultiMap;
import soot.jimple.spark.ondemand.genericutil.ImmutableStack;
import soot.jimple.spark.ondemand.genericutil.Predicate;
import soot.jimple.spark.ondemand.genericutil.Util;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.FieldRefNode;
import soot.jimple.spark.pag.GlobalVarNode;
import soot.jimple.spark.pag.LocalVarNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.PAG;
import soot.jimple.spark.pag.Parm;
import soot.jimple.spark.pag.SparkField;
import soot.jimple.spark.pag.StringConstantNode;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.spark.sets.DoublePointsToSet;
import soot.jimple.spark.sets.HybridPointsToSet;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.options.Options;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;
import soot.util.queue.ChunkedQueue;

public class SootUtil {
   private static final Logger logger = LoggerFactory.getLogger(SootUtil.class);
   private static final String[] lib13Packages = new String[]{"java.applet", "java.awt", "java.awt.color", "java.awt.datatransfer", "java.awt.dnd", "java.awt.dnd.peer", "java.awt.event", "java.awt.font", "java.awt.geom", "java.awt.im", "java.awt.im.spi", "java.awt.image", "java.awt.image.renderable", "java.awt.peer", "java.awt.print", "java.beans", "java.beans.beancontext", "java.io", "java.lang", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.rmi", "java.rmi.activation", "java.rmi.dgc", "java.rmi.registry", "java.rmi.server", "java.security", "java.security.acl", "java.security.cert", "java.security.interfaces", "java.security.spec", "java.sql", "java.text", "java.text.resources", "java.util", "java.util.jar", "java.util.zip", "javax.accessibility", "javax.naming", "javax.naming.directory", "javax.naming.event", "javax.naming.ldap", "javax.naming.spi", "javax.rmi", "javax.rmi.CORBA", "javax.sound.midi", "javax.sound.midi.spi", "javax.sound.sampled", "javax.sound.sampled.spi", "javax.swing", "javax.swing.border", "javax.swing.colorchooser", "javax.swing.event", "javax.swing.filechooser", "javax.swing.plaf", "javax.swing.plaf.basic", "javax.swing.plaf.metal", "javax.swing.plaf.multi", "javax.swing.table", "javax.swing.text", "javax.swing.text.html", "javax.swing.text.html.parser", "javax.swing.text.rtf", "javax.swing.tree", "javax.swing.undo", "javax.transaction", "org.omg.CORBA", "org.omg.CORBA.DynAnyPackage", "org.omg.CORBA.ORBPackage", "org.omg.CORBA.TypeCodePackage", "org.omg.CORBA.portable", "org.omg.CORBA_2_3", "org.omg.CORBA_2_3.portable", "org.omg.CosNaming", "org.omg.CosNaming.NamingContextPackage", "org.omg.SendingContext", "org.omg.stub.java.rmi", "sun.applet", "sun.applet.resources", "sun.audio", "sun.awt", "sun.awt.color", "sun.awt.dnd", "sun.awt.font", "sun.awt.geom", "sun.awt.im", "sun.awt.image", "sun.awt.image.codec", "sun.awt.motif", "sun.awt.print", "sun.beans.editors", "sun.beans.infos", "sun.dc.path", "sun.dc.pr", "sun.io", "sun.java2d", "sun.java2d.loops", "sun.java2d.pipe", "sun.jdbc.odbc", "sun.misc", "sun.net", "sun.net.ftp", "sun.net.nntp", "sun.net.smtp", "sun.net.www", "sun.net.www.content.audio", "sun.net.www.content.image", "sun.net.www.content.text", "sun.net.www.http", "sun.net.www.protocol.doc", "sun.net.www.protocol.file", "sun.net.www.protocol.ftp", "sun.net.www.protocol.gopher", "sun.net.www.protocol.http", "sun.net.www.protocol.jar", "sun.net.www.protocol.mailto", "sun.net.www.protocol.netdoc", "sun.net.www.protocol.systemresource", "sun.net.www.protocol.verbatim", "sun.rmi.log", "sun.rmi.registry", "sun.rmi.server", "sun.rmi.transport", "sun.rmi.transport.proxy", "sun.rmi.transport.tcp", "sun.security.acl", "sun.security.action", "sun.security.pkcs", "sun.security.provider", "sun.security.tools", "sun.security.util", "sun.security.x509", "sun.tools.jar", "sun.tools.util", "sunw.io", "sunw.util", "com.sun.corba.se.internal.CosNaming", "com.sun.corba.se.internal.corba", "com.sun.corba.se.internal.core", "com.sun.corba.se.internal.iiop", "com.sun.corba.se.internal.io", "com.sun.corba.se.internal.io.lang", "com.sun.corba.se.internal.io.util", "com.sun.corba.se.internal.javax.rmi", "com.sun.corba.se.internal.javax.rmi.CORBA", "com.sun.corba.se.internal.orbutil", "com.sun.corba.se.internal.util", "com.sun.image.codec.jpeg", "com.sun.java.swing.plaf.motif", "com.sun.java.swing.plaf.windows", "com.sun.jndi.cosnaming", "com.sun.jndi.ldap", "com.sun.jndi.rmi.registry", "com.sun.jndi.toolkit.corba", "com.sun.jndi.toolkit.ctx", "com.sun.jndi.toolkit.dir", "com.sun.jndi.toolkit.url", "com.sun.jndi.url.iiop", "com.sun.jndi.url.iiopname", "com.sun.jndi.url.ldap", "com.sun.jndi.url.rmi", "com.sun.media.sound", "com.sun.naming.internal", "com.sun.org.omg.CORBA", "com.sun.org.omg.CORBA.ValueDefPackage", "com.sun.org.omg.CORBA.portable", "com.sun.org.omg.SendingContext", "com.sun.org.omg.SendingContext.CodeBasePackage", "com.sun.rmi.rmid", "com.sun.rsajca", "com.sun.rsasign"};

   public static SootUtil.FieldAccessMap buildStoreMap(PAG pag) {
      SootUtil.FieldAccessMap ret = new SootUtil.FieldAccessMap();
      Iterator frNodeIter = pag.storeInvSourcesIterator();

      while(frNodeIter.hasNext()) {
         FieldRefNode frNode = (FieldRefNode)frNodeIter.next();
         SparkField field = frNode.getField();
         Node[] targets = pag.storeInvLookup(frNode);

         for(int i = 0; i < targets.length; ++i) {
            VarNode target = (VarNode)targets[i];
            if (!(target instanceof GlobalVarNode)) {
               ret.put(field, new Pair(frNode, (LocalVarNode)target));
            }
         }
      }

      return ret;
   }

   public static boolean isRetNode(VarNode node) {
      if (node.getVariable() instanceof Parm) {
         Parm parm = (Parm)node.getVariable();
         return parm.getIndex() == -2;
      } else {
         return false;
      }
   }

   public static boolean isParamNode(VarNode node) {
      if (!(node.getVariable() instanceof Pair)) {
         return false;
      } else {
         Pair pair = (Pair)node.getVariable();
         return pair.getO1() instanceof SootMethod && (pair.getO2() instanceof Integer || pair.getO2() == "THIS_NODE");
      }
   }

   public static boolean isThisNode(VarNode node) {
      if (!(node.getVariable() instanceof Pair)) {
         return false;
      } else {
         Pair pair = (Pair)node.getVariable();
         return pair.getO1() instanceof SootMethod && pair.getO2() == "THIS_NODE";
      }
   }

   public static boolean inLibrary(String className) {
      for(int i = 0; i < lib13Packages.length; ++i) {
         String libPackage = lib13Packages[i];
         if (className.startsWith(libPackage)) {
            return true;
         }
      }

      return false;
   }

   public static boolean inLibrary(RefType type) {
      return inLibrary(type.getClassName());
   }

   public static boolean isStringNode(VarNode node) {
      if (node instanceof GlobalVarNode) {
         GlobalVarNode global = (GlobalVarNode)node;
         if (global.getVariable() instanceof AllocNode) {
            AllocNode alloc = (AllocNode)global.getVariable();
            return alloc.getNewExpr() == "STRING_NODE" || alloc instanceof StringConstantNode;
         }
      }

      return false;
   }

   public static boolean isExceptionNode(VarNode node) {
      if (node instanceof GlobalVarNode) {
         GlobalVarNode global = (GlobalVarNode)node;
         return global.getVariable() == "EXCEPTION_NODE";
      } else {
         return false;
      }
   }

   public static SootUtil.FieldToEdgesMap storesOnField(PAG pag) {
      SootUtil.FieldToEdgesMap storesOnField = new SootUtil.FieldToEdgesMap();
      Iterator frNodeIter = pag.storeInvSourcesIterator();

      while(frNodeIter.hasNext()) {
         FieldRefNode frNode = (FieldRefNode)frNodeIter.next();
         VarNode source = frNode.getBase();
         SparkField field = frNode.getField();
         Node[] targets = pag.storeInvLookup(frNode);

         for(int i = 0; i < targets.length; ++i) {
            VarNode target = (VarNode)targets[i];
            storesOnField.put(field, new Pair(target, source));
         }
      }

      return storesOnField;
   }

   public static SootUtil.FieldToEdgesMap loadsOnField(PAG pag) {
      SootUtil.FieldToEdgesMap loadsOnField = new SootUtil.FieldToEdgesMap();
      Iterator frNodeIter = pag.loadSourcesIterator();

      while(frNodeIter.hasNext()) {
         FieldRefNode frNode = (FieldRefNode)frNodeIter.next();
         VarNode source = frNode.getBase();
         SparkField field = frNode.getField();
         Node[] targets = pag.loadLookup(frNode);

         for(int i = 0; i < targets.length; ++i) {
            VarNode target = (VarNode)targets[i];
            loadsOnField.put(field, new Pair(target, source));
         }
      }

      return loadsOnField;
   }

   public static PointsToSetInternal constructIntersection(PointsToSetInternal set1, PointsToSetInternal set2, PAG pag) {
      HybridPointsToSet hybridSet1 = null;
      HybridPointsToSet hybridSet2 = null;
      hybridSet1 = convertToHybrid(set1);
      hybridSet2 = convertToHybrid(set2);
      HybridPointsToSet intersection = HybridPointsToSet.intersection(hybridSet1, hybridSet2, pag);
      return intersection;
   }

   private static void checkSetsEqual(final HybridPointsToSet intersection, final PointsToSetInternal set1, final PointsToSetInternal set2, PAG pag) {
      final PointsToSetInternal ret = new HybridPointsToSet(Scene.v().getObjectType(), pag);
      set1.forall(new P2SetVisitor() {
         public void visit(Node n) {
            if (set2.contains(n)) {
               ret.add(n);
            }

         }
      });
      ret.forall(new P2SetVisitor() {
         public void visit(Node n) {
            if (!intersection.contains(n)) {
               SootUtil.logger.debug("" + n + " missing from intersection");
               SootUtil.logger.debug("" + set1);
               SootUtil.logger.debug("" + set2);
               SootUtil.logger.debug("" + intersection);
               SootUtil.logger.debug("" + ret);
               throw new RuntimeException("intersection too small");
            }
         }
      });
      intersection.forall(new P2SetVisitor() {
         public void visit(Node n) {
            if (!ret.contains(n)) {
               SootUtil.logger.debug("" + n + " missing from ret");
               SootUtil.logger.debug("" + set1);
               SootUtil.logger.debug("" + set2);
               SootUtil.logger.debug("" + intersection);
               SootUtil.logger.debug("" + ret);
               throw new RuntimeException("old way too small???");
            }
         }
      });
   }

   private static HybridPointsToSet convertToHybrid(PointsToSetInternal set) {
      HybridPointsToSet ret = null;
      if (set instanceof HybridPointsToSet) {
         ret = (HybridPointsToSet)set;
      } else if (set instanceof DoublePointsToSet) {
         assert ((DoublePointsToSet)set).getNewSet().isEmpty();

         ret = (HybridPointsToSet)((DoublePointsToSet)set).getOldSet();
      }

      return ret;
   }

   public static boolean isThreadGlobal(VarNode node) {
      if (node instanceof GlobalVarNode) {
         GlobalVarNode global = (GlobalVarNode)node;
         return global.getVariable() == "MAIN_THREAD_GROUP_NODE_LOCAL";
      } else {
         return false;
      }
   }

   public static boolean isThreadStartMethod(SootMethod method) {
      return method.toString().equals("<java.lang.Thread: void start()>");
   }

   public static boolean hasRecursiveField(SootClass sootClass) {
      Chain fields = sootClass.getFields();
      Iterator iter = fields.iterator();

      while(iter.hasNext()) {
         SootField sootField = (SootField)iter.next();
         Type type = sootField.getType();
         if (type instanceof RefType) {
            RefType refType = (RefType)type;
            SootClass sootClass2 = refType.getSootClass();
            if (sootClass == sootClass2) {
               return true;
            }
         }
      }

      return false;
   }

   public static void dumpVarNodeInfo(PAG pag) {
      PrintWriter varNodeWriter = null;

      try {
         varNodeWriter = new PrintWriter(new BufferedWriter(new FileWriter("varNodeInfo")));
      } catch (IOException var4) {
         logger.error((String)var4.getMessage(), (Throwable)var4);
      }

      Iterator iter = pag.getVarNodeNumberer().iterator();

      while(iter.hasNext()) {
         VarNode varNode = (VarNode)iter.next();
         varNodeWriter.println(varNode.getNumber() + " " + varNode);
      }

      varNodeWriter.flush();
      varNodeWriter.close();
   }

   public static boolean noRefTypeParameters(SootMethod method) {
      if (!method.isStatic()) {
         return false;
      } else {
         Predicate<Type> notRefTypePred = new Predicate<Type>() {
            public boolean test(Type obj_) {
               return !(obj_ instanceof RefType) && !(obj_ instanceof ArrayType);
            }
         };
         return notRefTypePred.test(method.getReturnType()) && Util.forAll(method.getParameterTypes(), notRefTypePred);
      }
   }

   public static SootMethod getMainMethod() {
      return Scene.v().getMainClass().getMethod(Scene.v().getSubSigNumberer().findOrAdd("void main(java.lang.String[])"));
   }

   public static boolean isResolvableCall(SootMethod invokedMethod) {
      if (invokedMethod.isStatic()) {
         return true;
      } else {
         return isConstructor(invokedMethod);
      }
   }

   public static Collection<? extends SootMethod> getCallTargets(Type type, SootMethod invokedMethod) {
      if (isConstructor(invokedMethod)) {
         return Collections.singleton(invokedMethod);
      } else {
         Type receiverType = invokedMethod.getDeclaringClass().getType();
         ChunkedQueue chunkedQueue = new ChunkedQueue();
         Iterator iter = chunkedQueue.reader();
         VirtualCalls.v().resolve(type, receiverType, invokedMethod.getNumberedSubSignature(), (SootMethod)null, chunkedQueue);
         ArraySet ret = new ArraySet();

         while(iter.hasNext()) {
            SootMethod target = (SootMethod)iter.next();
            ret.add(target);
         }

         return ret;
      }
   }

   private static boolean isConstructor(SootMethod invokedMethod) {
      return invokedMethod.getName().equals("<init>");
   }

   public static String createDirIfNotExist(String dirName) {
      File dir = new File(dirName);
      if (!dir.exists()) {
         try {
            if (!Options.v().output_jar()) {
               dir.mkdirs();
            }
         } catch (SecurityException var3) {
            logger.debug("Unable to create " + dirName);
            throw new CompilationDeathException(0);
         }
      }

      return dirName;
   }

   public static long getFreeLiveMemory() {
      Runtime r = Runtime.getRuntime();
      r.gc();
      return r.freeMemory();
   }

   public static void printNodeNumberMapping(String fileName, PAG pag) {
      try {
         PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
         Iterator iter = pag.getVarNodeNumberer().iterator();

         while(iter.hasNext()) {
            VarNode vn = (VarNode)iter.next();
            pw.println(vn.getNumber() + "\t" + vn);
         }

         pw.close();
      } catch (FileNotFoundException var5) {
         logger.error((String)var5.getMessage(), (Throwable)var5);
      }

   }

   public static SootMethod getAmbiguousMethodByName(String methodName) {
      SootClass sc = Scene.v().tryLoadClass(getClassName(methodName), 2);
      SootMethod sm = sc.getMethodByName(getMethodName(methodName));
      return sm;
   }

   public static String fakeSignature(String qualifiedName) {
      String cname = qualifiedName.substring(0, qualifiedName.lastIndexOf(46));
      String mname = qualifiedName.substring(qualifiedName.lastIndexOf(46) + 1, qualifiedName.length());
      return "<" + cname + ": " + mname + ">";
   }

   public static String getClassName(String qualifiedName) {
      return qualifiedName.substring(0, qualifiedName.lastIndexOf(46));
   }

   public static String getMethodName(String qualifiedName) {
      return qualifiedName.substring(qualifiedName.lastIndexOf(46) + 1, qualifiedName.length());
   }

   public static boolean isNewInstanceMethod(SootMethod method) {
      return method.toString().equals("<java.lang.Class: java.lang.Object newInstance()>");
   }

   public static final class FieldToEdgesMap extends ArraySetMultiMap<SparkField, Pair<VarNode, VarNode>> {
   }

   public static final class FieldAccessMap extends ArraySetMultiMap<SparkField, Pair<FieldRefNode, LocalVarNode>> {
   }

   public static final class CallSiteAndContext extends Pair<Integer, ImmutableStack<Integer>> {
      public CallSiteAndContext(Integer callSite, ImmutableStack<Integer> callingContext) {
         super(callSite, callingContext);
      }
   }
}
