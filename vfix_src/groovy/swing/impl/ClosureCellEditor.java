package groovy.swing.impl;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import java.awt.Component;
import java.lang.ref.SoftReference;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.ArrayUtil;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ClosureCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor, GroovyObject {
   private Map<String, Closure> callbacks;
   private Closure prepareEditor;
   private Closure editorValue;
   private List children;
   private boolean defaultEditor;
   private JTable table;
   private JTree tree;
   private Object value;
   private boolean selected;
   private boolean expanded;
   private boolean leaf;
   private int row;
   private int column;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private transient MetaClass metaClass;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)-1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524204572L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524204572 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$DefaultCellEditor;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTextField;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTree;
   // $FF: synthetic field
   private static Class $class$java$util$List;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$java$awt$Component;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$TableCellEditor;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MetaClass;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$impl$ClosureCellEditor;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTable;

   public ClosureCellEditor(Closure c, Map<String, Closure> callbacks) {
      CallSite[] var3 = $getCallSiteArray();
      this.callbacks = (Map)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$Map());
      this.children = (List)ScriptBytecodeAdapter.castToType(ScriptBytecodeAdapter.createList(new Object[0]), $get$$class$java$util$List());
      this.metaClass = (MetaClass)ScriptBytecodeAdapter.castToType(this.$getStaticMetaClass(), $get$$class$groovy$lang$MetaClass());
      this.editorValue = (Closure)ScriptBytecodeAdapter.castToType(c, $get$$class$groovy$lang$Closure());
      var3[0].call(this.callbacks, (Object)callbacks);
   }

   public ClosureCellEditor(Closure c) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createGroovyObjectWrapper(c, $get$$class$groovy$lang$Closure()), ScriptBytecodeAdapter.createPojoWrapper(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$Map())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 3, $get$$class$groovy$swing$impl$ClosureCellEditor());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Closure)var10001[0]);
         break;
      case 2:
         this((Closure)var10001[0], (Map)var10001[1]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public ClosureCellEditor() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure()), ScriptBytecodeAdapter.createPojoWrapper(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$Map())};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 3, $get$$class$groovy$swing$impl$ClosureCellEditor());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this((Closure)var10001[0]);
         break;
      case 2:
         this((Closure)var10001[0], (Map)var10001[1]);
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      CallSite[] var6 = $getCallSiteArray();
      this.table = (JTable)ScriptBytecodeAdapter.castToType(table, $get$$class$javax$swing$JTable());
      this.tree = (JTree)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$JTree());
      this.value = (Object)ScriptBytecodeAdapter.castToType(value, $get$$class$java$lang$Object());
      this.selected = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(isSelected));
      this.expanded = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.leaf = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      this.row = DefaultTypeTransformation.intUnbox(DefaultTypeTransformation.box(row));
      this.column = DefaultTypeTransformation.intUnbox(DefaultTypeTransformation.box(column));
      return (Component)ScriptBytecodeAdapter.castToType(var6[1].callCurrent(this), $get$$class$java$awt$Component());
   }

   public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
      CallSite[] var7 = $getCallSiteArray();
      this.table = (JTable)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$JTable());
      this.tree = (JTree)ScriptBytecodeAdapter.castToType(tree, $get$$class$javax$swing$JTree());
      this.value = (Object)ScriptBytecodeAdapter.castToType(value, $get$$class$java$lang$Object());
      this.selected = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(isSelected));
      this.expanded = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(expanded));
      this.leaf = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(leaf));
      this.row = DefaultTypeTransformation.intUnbox(DefaultTypeTransformation.box(row));
      this.column = DefaultTypeTransformation.intUnbox($const$0);
      return (Component)ScriptBytecodeAdapter.castToType(var7[2].callCurrent(this), $get$$class$java$awt$Component());
   }

   private Component prepare() {
      CallSite[] var1 = $getCallSiteArray();
      Object tce;
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var1[3].call(this.children)) && !DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(this.defaultEditor)) ? Boolean.FALSE : Boolean.TRUE)) {
         this.defaultEditor = DefaultTypeTransformation.booleanUnbox(Boolean.TRUE);
         var1[4].call(this.children);
         if (DefaultTypeTransformation.booleanUnbox(this.table)) {
            TableCellEditor tce = (TableCellEditor)ScriptBytecodeAdapter.castToType(var1[5].call(this.table, (Object)var1[6].call(this.table, (Object)DefaultTypeTransformation.box(this.column))), $get$$class$javax$swing$table$TableCellEditor());
            var1[7].call(this.children, (Object)var1[8].call(tce, (Object[])ArrayUtil.createArray(this.table, this.value, DefaultTypeTransformation.box(this.selected), DefaultTypeTransformation.box(this.row), DefaultTypeTransformation.box(this.column))));
         } else if (DefaultTypeTransformation.booleanUnbox(this.tree)) {
            tce = var1[9].callConstructor($get$$class$javax$swing$DefaultCellEditor(), (Object)var1[10].callConstructor($get$$class$javax$swing$JTextField()));
            var1[11].call(this.children, (Object)var1[12].call(tce, ArrayUtil.createArray(this.tree, this.value, DefaultTypeTransformation.box(this.selected), DefaultTypeTransformation.box(this.expanded), DefaultTypeTransformation.box(this.leaf), DefaultTypeTransformation.box(this.row))));
         }
      }

      tce = var1[13].call(this.prepareEditor);
      return tce instanceof Component ? (Component)ScriptBytecodeAdapter.castToType((Component)ScriptBytecodeAdapter.castToType(tce, $get$$class$java$awt$Component()), $get$$class$java$awt$Component()) : (Component)ScriptBytecodeAdapter.castToType((Component)ScriptBytecodeAdapter.castToType(var1[14].call(this.children, (Object)$const$1), $get$$class$java$awt$Component()), $get$$class$java$awt$Component());
   }

   public Object getCellEditorValue() {
      CallSite[] var1 = $getCallSiteArray();
      return (Object)ScriptBytecodeAdapter.castToType(var1[15].call(this.editorValue), $get$$class$java$lang$Object());
   }

   public void setEditorValue(Closure editorValue) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareNotEqual(editorValue, (Object)null)) {
         ScriptBytecodeAdapter.setGroovyObjectProperty(this, $get$$class$groovy$swing$impl$ClosureCellEditor(), editorValue, "delegate");
         ScriptBytecodeAdapter.setGroovyObjectProperty(var2[16].callGetProperty($get$$class$groovy$lang$Closure()), $get$$class$groovy$swing$impl$ClosureCellEditor(), editorValue, "resolveStrategy");
      }

      this.editorValue = (Closure)ScriptBytecodeAdapter.castToType(editorValue, $get$$class$groovy$lang$Closure());
   }

   public void setPrepareEditor(Closure prepareEditor) {
      CallSite[] var2 = $getCallSiteArray();
      if (ScriptBytecodeAdapter.compareNotEqual(prepareEditor, (Object)null)) {
         ScriptBytecodeAdapter.setGroovyObjectProperty(this, $get$$class$groovy$swing$impl$ClosureCellEditor(), prepareEditor, "delegate");
         ScriptBytecodeAdapter.setGroovyObjectProperty(var2[17].callGetProperty($get$$class$groovy$lang$Closure()), $get$$class$groovy$swing$impl$ClosureCellEditor(), prepareEditor, "resolveStrategy");
      }

      this.prepareEditor = (Closure)ScriptBytecodeAdapter.castToType(prepareEditor, $get$$class$groovy$lang$Closure());
   }

   public Object invokeMethod(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Object calledMethod = var3[18].call(var3[19].callGetProperty($get$$class$groovy$swing$impl$ClosureCellEditor()), name, args);
      return DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.getProperty($get$$class$groovy$swing$impl$ClosureCellEditor(), this.callbacks, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()))) && ScriptBytecodeAdapter.getProperty($get$$class$groovy$swing$impl$ClosureCellEditor(), this.callbacks, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String())) instanceof Closure ? Boolean.TRUE : Boolean.FALSE) ? (Object)ScriptBytecodeAdapter.castToType(var3[20].call(ScriptBytecodeAdapter.getProperty($get$$class$groovy$swing$impl$ClosureCellEditor(), this.callbacks, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String())), calledMethod, this, args), $get$$class$java$lang$Object()) : (Object)ScriptBytecodeAdapter.castToType(var3[21].callSafe(calledMethod, this, args), $get$$class$java$lang$Object());
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$impl$ClosureCellEditor()) {
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
      Class var10000 = $get$$class$groovy$swing$impl$ClosureCellEditor();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$3(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$impl$ClosureCellEditor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$3(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$impl$ClosureCellEditor(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
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
   public Object getProperty(String var1) {
      return this.getMetaClass().getProperty(this, var1);
   }

   // $FF: synthetic method
   public void setProperty(String var1, Object var2) {
      this.getMetaClass().setProperty(this, var1, var2);
   }

   public Map<String, Closure> getCallbacks() {
      return this.callbacks;
   }

   public void setCallbacks(Map<String, Closure> var1) {
      this.callbacks = var1;
   }

   public Closure getPrepareEditor() {
      return this.prepareEditor;
   }

   public Closure getEditorValue() {
      return this.editorValue;
   }

   public List getChildren() {
      return this.children;
   }

   public void setChildren(List var1) {
      this.children = var1;
   }

   public boolean getDefaultEditor() {
      return this.defaultEditor;
   }

   public boolean isDefaultEditor() {
      return this.defaultEditor;
   }

   public void setDefaultEditor(boolean var1) {
      this.defaultEditor = var1;
   }

   public JTable getTable() {
      return this.table;
   }

   public void setTable(JTable var1) {
      this.table = var1;
   }

   public JTree getTree() {
      return this.tree;
   }

   public void setTree(JTree var1) {
      this.tree = var1;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object var1) {
      this.value = var1;
   }

   public boolean getSelected() {
      return this.selected;
   }

   public boolean isSelected() {
      return this.selected;
   }

   public void setSelected(boolean var1) {
      this.selected = var1;
   }

   public boolean getExpanded() {
      return this.expanded;
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void setExpanded(boolean var1) {
      this.expanded = var1;
   }

   public boolean getLeaf() {
      return this.leaf;
   }

   public boolean isLeaf() {
      return this.leaf;
   }

   public void setLeaf(boolean var1) {
      this.leaf = var1;
   }

   public int getRow() {
      return this.row;
   }

   public void setRow(int var1) {
      this.row = var1;
   }

   public int getColumn() {
      return this.column;
   }

   public void setColumn(int var1) {
      this.column = var1;
   }

   // $FF: synthetic method
   public Component this$3$prepare() {
      return this.prepare();
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public void super$2$fireEditingStopped() {
      super.fireEditingStopped();
   }

   // $FF: synthetic method
   public CellEditorListener[] super$2$getCellEditorListeners() {
      return super.getCellEditorListeners();
   }

   // $FF: synthetic method
   public void super$2$addCellEditorListener(CellEditorListener var1) {
      super.addCellEditorListener(var1);
   }

   // $FF: synthetic method
   public void super$2$removeCellEditorListener(CellEditorListener var1) {
      super.removeCellEditorListener(var1);
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public void super$2$cancelCellEditing() {
      super.cancelCellEditing();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public boolean super$2$isCellEditable(EventObject var1) {
      return super.isCellEditable(var1);
   }

   // $FF: synthetic method
   public boolean super$2$shouldSelectCell(EventObject var1) {
      return super.shouldSelectCell(var1);
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public boolean super$2$stopCellEditing() {
      return super.stopCellEditing();
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$2$fireEditingCanceled() {
      super.fireEditingCanceled();
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "putAll";
      var0[1] = "prepare";
      var0[2] = "prepare";
      var0[3] = "isEmpty";
      var0[4] = "clear";
      var0[5] = "getDefaultEditor";
      var0[6] = "getColumnClass";
      var0[7] = "add";
      var0[8] = "getTableCellEditorComponent";
      var0[9] = "<$constructor$>";
      var0[10] = "<$constructor$>";
      var0[11] = "add";
      var0[12] = "getTreeCellEditorComponent";
      var0[13] = "call";
      var0[14] = "getAt";
      var0[15] = "call";
      var0[16] = "DELEGATE_FIRST";
      var0[17] = "DELEGATE_FIRST";
      var0[18] = "getMetaMethod";
      var0[19] = "metaClass";
      var0[20] = "call";
      var0[21] = "invoke";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[22];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$impl$ClosureCellEditor(), var0);
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
   private static Class $get$$class$javax$swing$DefaultCellEditor() {
      Class var10000 = $class$javax$swing$DefaultCellEditor;
      if (var10000 == null) {
         var10000 = $class$javax$swing$DefaultCellEditor = class$("javax.swing.DefaultCellEditor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JTextField() {
      Class var10000 = $class$javax$swing$JTextField;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JTextField = class$("javax.swing.JTextField");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JTree() {
      Class var10000 = $class$javax$swing$JTree;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JTree = class$("javax.swing.JTree");
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
   private static Class $get$$class$java$lang$String() {
      Class var10000 = $class$java$lang$String;
      if (var10000 == null) {
         var10000 = $class$java$lang$String = class$("java.lang.String");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Map() {
      Class var10000 = $class$java$util$Map;
      if (var10000 == null) {
         var10000 = $class$java$util$Map = class$("java.util.Map");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$lang$Closure() {
      Class var10000 = $class$groovy$lang$Closure;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Component() {
      Class var10000 = $class$java$awt$Component;
      if (var10000 == null) {
         var10000 = $class$java$awt$Component = class$("java.awt.Component");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$table$TableCellEditor() {
      Class var10000 = $class$javax$swing$table$TableCellEditor;
      if (var10000 == null) {
         var10000 = $class$javax$swing$table$TableCellEditor = class$("javax.swing.table.TableCellEditor");
      }

      return var10000;
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
   private static Class $get$$class$java$lang$Object() {
      Class var10000 = $class$java$lang$Object;
      if (var10000 == null) {
         var10000 = $class$java$lang$Object = class$("java.lang.Object");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$impl$ClosureCellEditor() {
      Class var10000 = $class$groovy$swing$impl$ClosureCellEditor;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$impl$ClosureCellEditor = class$("groovy.swing.impl.ClosureCellEditor");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JTable() {
      Class var10000 = $class$javax$swing$JTable;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JTable = class$("javax.swing.JTable");
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
