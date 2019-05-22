package groovy.inspect.swingui;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.lang.ref.SoftReference;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class TreeNodeWithProperties extends DefaultMutableTreeNode implements GroovyObject {
   private List<List<String>> properties;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204111L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204111 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$javax$swing$tree$DefaultMutableTreeNode;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$inspect$swingui$TreeNodeWithProperties;

   public TreeNodeWithProperties(Object userObject, List<List<String>> properties) {
      CallSite[] var3 = $getCallSiteArray();
      Object[] var10000 = new Object[]{userObject};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 3, $get$$class$javax$swing$tree$DefaultMutableTreeNode());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super();
         break;
      case 1:
         super(var10001[0]);
         break;
      case 2:
         super(var10001[0], DefaultTypeTransformation.booleanUnbox(var10001[1]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.properties = (List)ScriptBytecodeAdapter.castToType(properties, $get$$class$java$util$List());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$inspect$swingui$TreeNodeWithProperties()) {
         return ScriptBytecodeAdapter.initMetaClass(this);
      } else {
         ClassInfo var1 = $staticClassInfo;
         if (var1 == null) {
            $staticClassInfo = var1 = ClassInfo.getClassInfo(this.getClass());
         }

         return var1.getMetaClass();
      }
   }

   // $FF: synthetic method
   public Object this$dist$invoke$3(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$inspect$swingui$TreeNodeWithProperties();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$inspect$swingui$TreeNodeWithProperties(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$inspect$swingui$TreeNodeWithProperties(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public MetaClass getMetaClass() {
      MetaClass var10000 = this.metaClass;
      if (var10000 != null) {
         return var10000;
      } else {
         this.metaClass = this.$getStaticMetaClass();
         return this.metaClass;
      }
   }

   // $FF: synthetic method
   public void setMetaClass(MetaClass var1) {
      this.metaClass = var1;
   }

   // $FF: synthetic method
   public Object invokeMethod(String var1, Object var2) {
      return this.getMetaClass().invokeMethod(this, var1, var2);
   }

   // $FF: synthetic method
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public List<List<String>> getProperties() {
      return this.properties;
   }

   public void setProperties(List<List<String>> var1) {
      this.properties = var1;
   }

   // $FF: synthetic method
   public Enumeration super$2$pathFromAncestorEnumeration(TreeNode var1) {
      return super.pathFromAncestorEnumeration(var1);
   }

   // $FF: synthetic method
   public String super$2$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public Enumeration super$2$children() {
      return super.children();
   }

   // $FF: synthetic method
   public TreeNode super$2$getFirstChild() {
      return super.getFirstChild();
   }

   // $FF: synthetic method
   public boolean super$2$isNodeSibling(TreeNode var1) {
      return super.isNodeSibling(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isLeaf() {
      return super.isLeaf();
   }

   // $FF: synthetic method
   public void super$2$setParent(MutableTreeNode var1) {
      super.setParent(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isRoot() {
      return super.isRoot();
   }

   // $FF: synthetic method
   public TreeNode super$2$getChildBefore(TreeNode var1) {
      return super.getChildBefore(var1);
   }

   // $FF: synthetic method
   public Enumeration super$2$postorderEnumeration() {
      return super.postorderEnumeration();
   }

   // $FF: synthetic method
   public Enumeration super$2$preorderEnumeration() {
      return super.preorderEnumeration();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getLastLeaf() {
      return super.getLastLeaf();
   }

   // $FF: synthetic method
   public void super$2$insert(MutableTreeNode var1, int var2) {
      super.insert(var1, var2);
   }

   // $FF: synthetic method
   public int super$2$getLevel() {
      return super.getLevel();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public TreeNode super$2$getRoot() {
      return super.getRoot();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getNextLeaf() {
      return super.getNextLeaf();
   }

   // $FF: synthetic method
   public TreeNode super$2$getChildAfter(TreeNode var1) {
      return super.getChildAfter(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Object[] super$2$getUserObjectPath() {
      return super.getUserObjectPath();
   }

   // $FF: synthetic method
   public int super$2$getDepth() {
      return super.getDepth();
   }

   // $FF: synthetic method
   public boolean super$2$isNodeAncestor(TreeNode var1) {
      return super.isNodeAncestor(var1);
   }

   // $FF: synthetic method
   public int super$2$getChildCount() {
      return super.getChildCount();
   }

   // $FF: synthetic method
   public boolean super$2$getAllowsChildren() {
      return super.getAllowsChildren();
   }

   // $FF: synthetic method
   public boolean super$2$isNodeDescendant(DefaultMutableTreeNode var1) {
      return super.isNodeDescendant(var1);
   }

   // $FF: synthetic method
   public boolean super$2$isNodeRelated(DefaultMutableTreeNode var1) {
      return super.isNodeRelated(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public int super$2$getSiblingCount() {
      return super.getSiblingCount();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getNextSibling() {
      return super.getNextSibling();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getPreviousSibling() {
      return super.getPreviousSibling();
   }

   // $FF: synthetic method
   public int super$2$getIndex(TreeNode var1) {
      return super.getIndex(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$2$remove(MutableTreeNode var1) {
      super.remove(var1);
   }

   // $FF: synthetic method
   public int super$2$getLeafCount() {
      return super.getLeafCount();
   }

   // $FF: synthetic method
   public void super$2$add(MutableTreeNode var1) {
      super.add(var1);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public boolean super$2$isNodeChild(TreeNode var1) {
      return super.isNodeChild(var1);
   }

   // $FF: synthetic method
   public TreeNode super$2$getChildAt(int var1) {
      return super.getChildAt(var1);
   }

   // $FF: synthetic method
   public void super$2$removeFromParent() {
      super.removeFromParent();
   }

   // $FF: synthetic method
   public void super$2$removeAllChildren() {
      super.removeAllChildren();
   }

   // $FF: synthetic method
   public TreeNode[] super$2$getPath() {
      return super.getPath();
   }

   // $FF: synthetic method
   public Object super$2$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getNextNode() {
      return super.getNextNode();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getPreviousNode() {
      return super.getPreviousNode();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getFirstLeaf() {
      return super.getFirstLeaf();
   }

   // $FF: synthetic method
   public void super$2$setAllowsChildren(boolean var1) {
      super.setAllowsChildren(var1);
   }

   // $FF: synthetic method
   public TreeNode[] super$2$getPathToRoot(TreeNode var1, int var2) {
      return super.getPathToRoot(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public TreeNode super$2$getParent() {
      return super.getParent();
   }

   // $FF: synthetic method
   public DefaultMutableTreeNode super$2$getPreviousLeaf() {
      return super.getPreviousLeaf();
   }

   // $FF: synthetic method
   public TreeNode super$2$getLastChild() {
      return super.getLastChild();
   }

   // $FF: synthetic method
   public Enumeration super$2$depthFirstEnumeration() {
      return super.depthFirstEnumeration();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public Enumeration super$2$breadthFirstEnumeration() {
      return super.breadthFirstEnumeration();
   }

   // $FF: synthetic method
   public TreeNode super$2$getSharedAncestor(DefaultMutableTreeNode var1) {
      return super.getSharedAncestor(var1);
   }

   // $FF: synthetic method
   public void super$2$remove(int var1) {
      super.remove(var1);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public Object super$2$getUserObject() {
      return super.getUserObject();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public void super$2$setUserObject(Object var1) {
      super.setUserObject(var1);
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[0];
      return new CallSiteArray($get$$class$groovy$inspect$swingui$TreeNodeWithProperties(), var0);
   }

   // $FF: synthetic method
   private static CallSite[] $getCallSiteArray() {
      CallSiteArray var0;
      if ($callSiteArray == null || (var0 = (CallSiteArray)$callSiteArray.get()) == null) {
         var0 = $createCallSiteArray();
         $callSiteArray = new SoftReference(var0);
      }

      return var0.array;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$MetaClass() {
      Class var10000 = $class$groovy$lang$MetaClass;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MetaClass = class$("groovy.lang.MetaClass");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$List() {
      Class var10000 = $class$java$util$List;
      if (var10000 == null) {
         var10000 = $class$java$util$List = class$("java.util.List");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$tree$DefaultMutableTreeNode() {
      Class var10000 = $class$javax$swing$tree$DefaultMutableTreeNode;
      if (var10000 == null) {
         var10000 = $class$javax$swing$tree$DefaultMutableTreeNode = class$("javax.swing.tree.DefaultMutableTreeNode");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$inspect$swingui$TreeNodeWithProperties() {
      Class var10000 = $class$groovy$inspect$swingui$TreeNodeWithProperties;
      if (var10000 == null) {
         var10000 = $class$groovy$inspect$swingui$TreeNodeWithProperties = class$("groovy.inspect.swingui.TreeNodeWithProperties");
      }

      return var10000;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
