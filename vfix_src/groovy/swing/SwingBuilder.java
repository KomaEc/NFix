package groovy.swing;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.GroovyClassLoader;
import groovy.lang.MetaClass;
import groovy.lang.Reference;
import groovy.lang.Script;
import groovy.swing.factory.BindFactory;
import groovy.swing.factory.RendererFactory;
import groovy.util.Factory;
import groovy.util.FactoryBuilderSupport;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.GStringImpl;
import org.codehaus.groovy.runtime.GeneratedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.callsite.CallSiteArray;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class SwingBuilder extends FactoryBuilderSupport {
   private static final Logger LOG;
   private static boolean headless;
   public static final String DELEGATE_PROPERTY_OBJECT_ID = (String)"_delegateProperty:id";
   public static final String DEFAULT_DELEGATE_PROPERTY_OBJECT_ID = (String)"id";
   private static final Random random;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo;
   // $FF: synthetic field
   private static final Integer $const$0 = (Integer)1;
   // $FF: synthetic field
   private static final Integer $const$1 = (Integer)0;
   // $FF: synthetic field
   private static ClassInfo $staticClassInfo$;
   // $FF: synthetic field
   public static Long __timeStamp = (Long)1292524205610L;
   // $FF: synthetic field
   public static Long __timeStamp__239_neverHappen1292524205610 = (Long)0L;
   // $FF: synthetic field
   private static SoftReference $callSiteArray;
   // $FF: synthetic field
   private static Class $class$javax$swing$SpinnerListModel;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TextArgWidgetFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TRFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTree;
   // $FF: synthetic field
   private static Class $class$javax$swing$JPopupMenu;
   // $FF: synthetic field
   private static Class $class$java$lang$Math;
   // $FF: synthetic field
   private static Class $class$javax$swing$JLayeredPane;
   // $FF: synthetic field
   private static Class $class$groovy$util$FactoryBuilderSupport;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$FormattedTextFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ImageIconFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TableFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ActionFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$InternalFrameFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$HGlueFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JToolBar;
   // $FF: synthetic field
   private static Class $class$javax$swing$JLabel;
   // $FF: synthetic field
   private static Class $class$javax$swing$JPasswordField;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$VGlueFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JApplet;
   // $FF: synthetic field
   private static Class $class$javax$swing$JComponent;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$FrameFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JRadioButton;
   // $FF: synthetic field
   private static Class $class$javax$swing$SwingUtilities;
   // $FF: synthetic field
   private static Class $class$javax$swing$JPanel;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BoxLayoutFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JProgressBar;
   // $FF: synthetic field
   private static Class $class$java$util$LinkedList;
   // $FF: synthetic field
   private static Class $class$javax$swing$SpringLayout;
   // $FF: synthetic field
   private static Class $class$java$awt$FlowLayout;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ListFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$SpinnerNumberModel;
   // $FF: synthetic field
   private static Class $class$javax$swing$JScrollBar;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$PropertyColumnFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JMenuBar;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$VStrutFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JSpinner;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$RigidAreaFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TableLayoutFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$RendererUpdateFactory;
   // $FF: synthetic field
   private static Class $class$java$util$Random;
   // $FF: synthetic field
   private static Class $class$java$awt$GridLayout;
   // $FF: synthetic field
   private static Class $class$javax$swing$JRadioButtonMenuItem;
   // $FF: synthetic field
   private static Class $class$java$awt$GraphicsEnvironment;
   // $FF: synthetic field
   private static Class $class$javax$swing$JWindow;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ColumnModelFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JDialog;
   // $FF: synthetic field
   private static Class $class$javax$swing$JMenuItem;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$LayoutFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$DialogFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ColumnFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$VBoxFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BindGroupFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$RendererFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$LineBorderFactory;
   // $FF: synthetic field
   private static Class $class$java$util$logging$Logger;
   // $FF: synthetic field
   private static Class $class$javax$swing$JEditorPane;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$CompoundBorderFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TDFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$LookAndFeel;
   // $FF: synthetic field
   private static Class $class$javax$swing$OverlayLayout;
   // $FF: synthetic field
   private static Class $class$java$lang$Object;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$EmptyBorderFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JOptionPane;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTable;
   // $FF: synthetic field
   private static Class $class$groovy$lang$GroovyRuntimeException;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$MatteBorderFactory;
   // $FF: synthetic field
   private static Class $class$java$awt$CardLayout;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTabbedPane;
   // $FF: synthetic field
   private static Class $class$java$lang$RuntimeException;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ComponentFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$String;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$CellEditorFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$KeyStroke;
   // $FF: synthetic field
   private static Class $class$javax$swing$table$TableColumn;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ScrollPaneFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JScrollPane;
   // $FF: synthetic field
   private static Class $class$javax$swing$JSlider;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$HBoxFactory;
   // $FF: synthetic field
   private static Class $class$java$lang$Thread;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTextField;
   // $FF: synthetic field
   private static Class $class$groovy$lang$MissingMethodException;
   // $FF: synthetic field
   private static Class $class$javax$swing$JViewport;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BoxFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BevelBorderFactory;
   // $FF: synthetic field
   private static Class $class$java$awt$Toolkit;
   // $FF: synthetic field
   private static Class $class$groovy$lang$Closure;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$CellEditorGetValueFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$GlueFactory;
   // $FF: synthetic field
   private static Class $class$java$awt$Component;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TableModelFactory;
   // $FF: synthetic field
   private static Class $class$java$awt$GridBagConstraints;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$SeparatorFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TitledBorderFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTextArea;
   // $FF: synthetic field
   private static Class $class$javax$swing$JDesktopPane;
   // $FF: synthetic field
   private static Class $class$javax$swing$JButton;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ClosureColumnFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ComboBoxFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$MapFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$border$BevelBorder;
   // $FF: synthetic field
   private static Class $class$javax$swing$JFrame;
   // $FF: synthetic field
   private static Class $class$javax$swing$SpinnerDateModel;
   // $FF: synthetic field
   private static Class $class$java$util$Map;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$EtchedBorderFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$border$EtchedBorder;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$WidgetFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$GridBagFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JMenu;
   // $FF: synthetic field
   private static Class $class$javax$swing$JToggleButton;
   // $FF: synthetic field
   private static Class $class$javax$swing$JCheckBox;
   // $FF: synthetic field
   private static Class $class$javax$swing$JFileChooser;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BindProxyFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$BindFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$HStrutFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$TabbedPaneFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$WindowFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JTextPane;
   // $FF: synthetic field
   private static Class $class$javax$swing$DefaultBoundedRangeModel;
   // $FF: synthetic field
   private static Class $class$javax$swing$JColorChooser;
   // $FF: synthetic field
   private static Class $class$java$awt$BorderLayout;
   // $FF: synthetic field
   private static Class $class$groovy$swing$LookAndFeelHelper;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$CollectionFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$RichActionWidgetFactory;
   // $FF: synthetic field
   private static Class $class$javax$swing$JCheckBoxMenuItem;
   // $FF: synthetic field
   private static Class $class$groovy$swing$SwingBuilder;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$SplitPaneFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$CellEditorPrepareFactory;
   // $FF: synthetic field
   private static Class $class$groovy$swing$factory$ButtonGroupFactory;
   // $FF: synthetic field
   private static Class $class$java$awt$LayoutManager;

   public SwingBuilder(boolean init) {
      CallSite[] var2 = $getCallSiteArray();
      Object[] var10000 = new Object[]{DefaultTypeTransformation.box(init)};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 3, $get$$class$groovy$util$FactoryBuilderSupport());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         super();
         break;
      case 1:
         super((Closure)var10001[0]);
         break;
      case 2:
         super(DefaultTypeTransformation.booleanUnbox(var10001[0]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

      headless = DefaultTypeTransformation.booleanUnbox(var2[0].call($get$$class$java$awt$GraphicsEnvironment()));
      ScriptBytecodeAdapter.setGroovyObjectProperty(var2[1].callConstructor($get$$class$java$util$LinkedList()), $get$$class$groovy$swing$SwingBuilder(), this, "containingWindows");
      CallSite var4 = var2[2];
      String var3 = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID;
      var4.call(this, DELEGATE_PROPERTY_OBJECT_ID, var3);
   }

   public SwingBuilder() {
      CallSite[] var1 = $getCallSiteArray();
      Object[] var10000 = new Object[]{ScriptBytecodeAdapter.createPojoWrapper(Boolean.TRUE, Boolean.TYPE)};
      int var10002 = ScriptBytecodeAdapter.selectConstructorAndTransformArguments(var10000, 2, $get$$class$groovy$swing$SwingBuilder());
      Object[] var10001 = var10000;
      if ((var10002 & 1) != 0) {
         var10001 = (Object[])var10000[0];
      }

      switch(var10002 >> 8) {
      case 0:
         this();
         break;
      case 1:
         this(DefaultTypeTransformation.booleanUnbox(var10001[0]));
         break;
      default:
         throw new IllegalArgumentException("illegal constructor number");
      }

   }

   public Object registerSupportNodes() {
      CallSite[] var1 = $getCallSiteArray();
      var1[3].callCurrent(this, "action", var1[4].callConstructor($get$$class$groovy$swing$factory$ActionFactory()));
      var1[5].callCurrent(this, "actions", var1[6].callConstructor($get$$class$groovy$swing$factory$CollectionFactory()));
      var1[7].callCurrent(this, "map", var1[8].callConstructor($get$$class$groovy$swing$factory$MapFactory()));
      var1[9].callCurrent(this, "imageIcon", var1[10].callConstructor($get$$class$groovy$swing$factory$ImageIconFactory()));
      var1[11].callCurrent(this, "buttonGroup", var1[12].callConstructor($get$$class$groovy$swing$factory$ButtonGroupFactory()));
      var1[13].callCurrent(this, (Object)ScriptBytecodeAdapter.getMethodPointer($get$$class$groovy$swing$factory$ButtonGroupFactory(), "buttonGroupAttributeDelegate"));
      var1[14].callCurrent(this, (Object)ScriptBytecodeAdapter.getMethodPointer($get$$class$groovy$swing$SwingBuilder(), "objectIDAttributeDelegate"));
      var1[15].callCurrent(this, (Object)ScriptBytecodeAdapter.getMethodPointer($get$$class$groovy$swing$SwingBuilder(), "clientPropertyAttributeDelegate"));
      var1[16].callCurrent(this, "noparent", var1[17].callConstructor($get$$class$groovy$swing$factory$CollectionFactory()));
      var1[18].callCurrent(this, "keyStrokeAction", ScriptBytecodeAdapter.getMethodPointer(this, "createKeyStrokeAction"));
      return var1[19].callCurrent(this, "shortcut", ScriptBytecodeAdapter.getMethodPointer(this, "shortcut"));
   }

   public Object registerBinding() {
      CallSite[] var1 = $getCallSiteArray();
      BindFactory bindFactory = var1[20].callConstructor($get$$class$groovy$swing$factory$BindFactory());
      var1[21].callCurrent(this, "bind", bindFactory);
      var1[22].callCurrent(this, (Object)ScriptBytecodeAdapter.getMethodPointer(bindFactory, "bindingAttributeDelegate"));
      var1[23].callCurrent(this, "bindProxy", var1[24].callConstructor($get$$class$groovy$swing$factory$BindProxyFactory()));
      return var1[25].callCurrent(this, "bindGroup", var1[26].callConstructor($get$$class$groovy$swing$factory$BindGroupFactory()));
   }

   public Object registerPassThruNodes() {
      CallSite[] var1 = $getCallSiteArray();
      var1[27].callCurrent(this, "widget", var1[28].callConstructor($get$$class$groovy$swing$factory$WidgetFactory(), $get$$class$java$awt$Component(), Boolean.TRUE));
      var1[29].callCurrent(this, "container", var1[30].callConstructor($get$$class$groovy$swing$factory$WidgetFactory(), $get$$class$java$awt$Component(), Boolean.FALSE));
      return var1[31].callCurrent(this, "bean", var1[32].callConstructor($get$$class$groovy$swing$factory$WidgetFactory(), $get$$class$java$lang$Object(), Boolean.TRUE));
   }

   public Object registerWindows() {
      CallSite[] var1 = $getCallSiteArray();
      var1[33].callCurrent(this, "dialog", var1[34].callConstructor($get$$class$groovy$swing$factory$DialogFactory()));
      var1[35].callCurrent(this, "fileChooser", $get$$class$javax$swing$JFileChooser());
      var1[36].callCurrent(this, "frame", var1[37].callConstructor($get$$class$groovy$swing$factory$FrameFactory()));
      var1[38].callCurrent(this, "optionPane", $get$$class$javax$swing$JOptionPane());
      return var1[39].callCurrent(this, "window", var1[40].callConstructor($get$$class$groovy$swing$factory$WindowFactory()));
   }

   public Object registerActionButtonWidgets() {
      CallSite[] var1 = $getCallSiteArray();
      var1[41].callCurrent(this, "button", var1[42].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JButton()));
      var1[43].callCurrent(this, "checkBox", var1[44].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JCheckBox()));
      var1[45].callCurrent(this, "checkBoxMenuItem", var1[46].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JCheckBoxMenuItem()));
      var1[47].callCurrent(this, "menuItem", var1[48].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JMenuItem()));
      var1[49].callCurrent(this, "radioButton", var1[50].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JRadioButton()));
      var1[51].callCurrent(this, "radioButtonMenuItem", var1[52].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JRadioButtonMenuItem()));
      return var1[53].callCurrent(this, "toggleButton", var1[54].callConstructor($get$$class$groovy$swing$factory$RichActionWidgetFactory(), (Object)$get$$class$javax$swing$JToggleButton()));
   }

   public Object registerTextWidgets() {
      CallSite[] var1 = $getCallSiteArray();
      var1[55].callCurrent(this, "editorPane", var1[56].callConstructor($get$$class$groovy$swing$factory$TextArgWidgetFactory(), (Object)$get$$class$javax$swing$JEditorPane()));
      var1[57].callCurrent(this, "label", var1[58].callConstructor($get$$class$groovy$swing$factory$TextArgWidgetFactory(), (Object)$get$$class$javax$swing$JLabel()));
      var1[59].callCurrent(this, "passwordField", var1[60].callConstructor($get$$class$groovy$swing$factory$TextArgWidgetFactory(), (Object)$get$$class$javax$swing$JPasswordField()));
      var1[61].callCurrent(this, "textArea", var1[62].callConstructor($get$$class$groovy$swing$factory$TextArgWidgetFactory(), (Object)$get$$class$javax$swing$JTextArea()));
      var1[63].callCurrent(this, "textField", var1[64].callConstructor($get$$class$groovy$swing$factory$TextArgWidgetFactory(), (Object)$get$$class$javax$swing$JTextField()));
      var1[65].callCurrent(this, "formattedTextField", var1[66].callConstructor($get$$class$groovy$swing$factory$FormattedTextFactory()));
      return var1[67].callCurrent(this, "textPane", var1[68].callConstructor($get$$class$groovy$swing$factory$TextArgWidgetFactory(), (Object)$get$$class$javax$swing$JTextPane()));
   }

   public Object registerMDIWidgets() {
      CallSite[] var1 = $getCallSiteArray();
      var1[69].callCurrent(this, "desktopPane", $get$$class$javax$swing$JDesktopPane());
      return var1[70].callCurrent(this, "internalFrame", var1[71].callConstructor($get$$class$groovy$swing$factory$InternalFrameFactory()));
   }

   public Object registerBasicWidgets() {
      CallSite[] var1 = $getCallSiteArray();
      var1[72].callCurrent(this, "colorChooser", $get$$class$javax$swing$JColorChooser());
      var1[73].callCurrent(this, "comboBox", var1[74].callConstructor($get$$class$groovy$swing$factory$ComboBoxFactory()));
      var1[75].callCurrent(this, "list", var1[76].callConstructor($get$$class$groovy$swing$factory$ListFactory()));
      var1[77].callCurrent(this, "progressBar", $get$$class$javax$swing$JProgressBar());
      var1[78].callCurrent(this, "separator", var1[79].callConstructor($get$$class$groovy$swing$factory$SeparatorFactory()));
      var1[80].callCurrent(this, "scrollBar", $get$$class$javax$swing$JScrollBar());
      var1[81].callCurrent(this, "slider", $get$$class$javax$swing$JSlider());
      var1[82].callCurrent(this, "spinner", $get$$class$javax$swing$JSpinner());
      return var1[83].callCurrent(this, "tree", $get$$class$javax$swing$JTree());
   }

   public Object registerMenuWidgets() {
      CallSite[] var1 = $getCallSiteArray();
      var1[84].callCurrent(this, "menu", $get$$class$javax$swing$JMenu());
      var1[85].callCurrent(this, "menuBar", $get$$class$javax$swing$JMenuBar());
      return var1[86].callCurrent(this, "popupMenu", $get$$class$javax$swing$JPopupMenu());
   }

   public Object registerContainers() {
      CallSite[] var1 = $getCallSiteArray();
      var1[87].callCurrent(this, "panel", $get$$class$javax$swing$JPanel());
      var1[88].callCurrent(this, "scrollPane", var1[89].callConstructor($get$$class$groovy$swing$factory$ScrollPaneFactory()));
      var1[90].callCurrent(this, "splitPane", var1[91].callConstructor($get$$class$groovy$swing$factory$SplitPaneFactory()));
      var1[92].callCurrent(this, "tabbedPane", var1[93].callConstructor($get$$class$groovy$swing$factory$TabbedPaneFactory(), (Object)$get$$class$javax$swing$JTabbedPane()));
      var1[94].callCurrent(this, "toolBar", $get$$class$javax$swing$JToolBar());
      var1[95].callCurrent(this, "viewport", $get$$class$javax$swing$JViewport());
      return var1[96].callCurrent(this, "layeredPane", $get$$class$javax$swing$JLayeredPane());
   }

   public Object registerDataModels() {
      CallSite[] var1 = $getCallSiteArray();
      var1[97].callCurrent(this, "boundedRangeModel", $get$$class$javax$swing$DefaultBoundedRangeModel());
      var1[98].callCurrent(this, "spinnerDateModel", $get$$class$javax$swing$SpinnerDateModel());
      var1[99].callCurrent(this, "spinnerListModel", $get$$class$javax$swing$SpinnerListModel());
      return var1[100].callCurrent(this, "spinnerNumberModel", $get$$class$javax$swing$SpinnerNumberModel());
   }

   public Object registerTableComponents() {
      CallSite[] var1 = $getCallSiteArray();
      var1[101].callCurrent(this, "table", var1[102].callConstructor($get$$class$groovy$swing$factory$TableFactory()));
      var1[103].callCurrent(this, "tableColumn", $get$$class$javax$swing$table$TableColumn());
      var1[104].callCurrent(this, "tableModel", var1[105].callConstructor($get$$class$groovy$swing$factory$TableModelFactory()));
      var1[106].callCurrent(this, "propertyColumn", var1[107].callConstructor($get$$class$groovy$swing$factory$PropertyColumnFactory()));
      var1[108].callCurrent(this, "closureColumn", var1[109].callConstructor($get$$class$groovy$swing$factory$ClosureColumnFactory()));
      var1[110].callCurrent(this, "columnModel", var1[111].callConstructor($get$$class$groovy$swing$factory$ColumnModelFactory()));
      return var1[112].callCurrent(this, "column", var1[113].callConstructor($get$$class$groovy$swing$factory$ColumnFactory()));
   }

   public Object registerBasicLayouts() {
      CallSite[] var1 = $getCallSiteArray();
      var1[114].callCurrent(this, "borderLayout", var1[115].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)$get$$class$java$awt$BorderLayout()));
      var1[116].callCurrent(this, "cardLayout", var1[117].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)$get$$class$java$awt$CardLayout()));
      var1[118].callCurrent(this, "flowLayout", var1[119].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)$get$$class$java$awt$FlowLayout()));
      var1[120].callCurrent(this, "gridLayout", var1[121].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)$get$$class$java$awt$GridLayout()));
      var1[122].callCurrent(this, "overlayLayout", var1[123].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)$get$$class$javax$swing$OverlayLayout()));
      var1[124].callCurrent(this, "springLayout", var1[125].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)$get$$class$javax$swing$SpringLayout()));
      var1[126].callCurrent(this, "gridBagLayout", var1[127].callConstructor($get$$class$groovy$swing$factory$GridBagFactory()));
      var1[128].callCurrent(this, "gridBagConstraints", $get$$class$java$awt$GridBagConstraints());
      var1[129].callCurrent(this, "gbc", $get$$class$java$awt$GridBagConstraints());
      var1[130].callCurrent(this, (Object)ScriptBytecodeAdapter.getMethodPointer($get$$class$groovy$swing$factory$GridBagFactory(), "processGridBagConstraintsAttributes"));
      return var1[131].callCurrent(this, (Object)ScriptBytecodeAdapter.getMethodPointer($get$$class$groovy$swing$factory$LayoutFactory(), "constraintsAttributeDelegate"));
   }

   public Object registerBoxLayout() {
      CallSite[] var1 = $getCallSiteArray();
      var1[132].callCurrent(this, "boxLayout", var1[133].callConstructor($get$$class$groovy$swing$factory$BoxLayoutFactory()));
      var1[134].callCurrent(this, "box", var1[135].callConstructor($get$$class$groovy$swing$factory$BoxFactory()));
      var1[136].callCurrent(this, "hbox", var1[137].callConstructor($get$$class$groovy$swing$factory$HBoxFactory()));
      var1[138].callCurrent(this, "hglue", var1[139].callConstructor($get$$class$groovy$swing$factory$HGlueFactory()));
      var1[140].callCurrent(this, "hstrut", var1[141].callConstructor($get$$class$groovy$swing$factory$HStrutFactory()));
      var1[142].callCurrent(this, "vbox", var1[143].callConstructor($get$$class$groovy$swing$factory$VBoxFactory()));
      var1[144].callCurrent(this, "vglue", var1[145].callConstructor($get$$class$groovy$swing$factory$VGlueFactory()));
      var1[146].callCurrent(this, "vstrut", var1[147].callConstructor($get$$class$groovy$swing$factory$VStrutFactory()));
      var1[148].callCurrent(this, "glue", var1[149].callConstructor($get$$class$groovy$swing$factory$GlueFactory()));
      return var1[150].callCurrent(this, "rigidArea", var1[151].callConstructor($get$$class$groovy$swing$factory$RigidAreaFactory()));
   }

   public Object registerTableLayout() {
      CallSite[] var1 = $getCallSiteArray();
      var1[152].callCurrent(this, "tableLayout", var1[153].callConstructor($get$$class$groovy$swing$factory$TableLayoutFactory()));
      var1[154].callCurrent(this, "tr", var1[155].callConstructor($get$$class$groovy$swing$factory$TRFactory()));
      return var1[156].callCurrent(this, "td", var1[157].callConstructor($get$$class$groovy$swing$factory$TDFactory()));
   }

   public Object registerBorders() {
      CallSite[] var1 = $getCallSiteArray();
      var1[158].callCurrent(this, "lineBorder", var1[159].callConstructor($get$$class$groovy$swing$factory$LineBorderFactory()));
      var1[160].callCurrent(this, "loweredBevelBorder", var1[161].callConstructor($get$$class$groovy$swing$factory$BevelBorderFactory(), (Object)var1[162].callGetProperty($get$$class$javax$swing$border$BevelBorder())));
      var1[163].callCurrent(this, "raisedBevelBorder", var1[164].callConstructor($get$$class$groovy$swing$factory$BevelBorderFactory(), (Object)var1[165].callGetProperty($get$$class$javax$swing$border$BevelBorder())));
      var1[166].callCurrent(this, "etchedBorder", var1[167].callConstructor($get$$class$groovy$swing$factory$EtchedBorderFactory(), (Object)var1[168].callGetProperty($get$$class$javax$swing$border$EtchedBorder())));
      var1[169].callCurrent(this, "loweredEtchedBorder", var1[170].callConstructor($get$$class$groovy$swing$factory$EtchedBorderFactory(), (Object)var1[171].callGetProperty($get$$class$javax$swing$border$EtchedBorder())));
      var1[172].callCurrent(this, "raisedEtchedBorder", var1[173].callConstructor($get$$class$groovy$swing$factory$EtchedBorderFactory(), (Object)var1[174].callGetProperty($get$$class$javax$swing$border$EtchedBorder())));
      var1[175].callCurrent(this, "titledBorder", var1[176].callConstructor($get$$class$groovy$swing$factory$TitledBorderFactory()));
      var1[177].callCurrent(this, "emptyBorder", var1[178].callConstructor($get$$class$groovy$swing$factory$EmptyBorderFactory()));
      var1[179].callCurrent(this, "compoundBorder", var1[180].callConstructor($get$$class$groovy$swing$factory$CompoundBorderFactory()));
      return var1[181].callCurrent(this, "matteBorder", var1[182].callConstructor($get$$class$groovy$swing$factory$MatteBorderFactory()));
   }

   public Object registerRenderers() {
      CallSite[] var1 = $getCallSiteArray();
      RendererFactory renderFactory = var1[183].callConstructor($get$$class$groovy$swing$factory$RendererFactory());
      var1[184].callCurrent(this, "tableCellRenderer", renderFactory);
      var1[185].callCurrent(this, "listCellRenderer", renderFactory);
      var1[186].callCurrent(this, "onRender", var1[187].callConstructor($get$$class$groovy$swing$factory$RendererUpdateFactory()));
      var1[188].callCurrent(this, "cellRenderer", renderFactory);
      return var1[189].callCurrent(this, "headerRenderer", renderFactory);
   }

   public Object registerEditors() {
      CallSite[] var1 = $getCallSiteArray();
      var1[190].callCurrent(this, "cellEditor", var1[191].callConstructor($get$$class$groovy$swing$factory$CellEditorFactory()));
      var1[192].callCurrent(this, "editorValue", var1[193].callConstructor($get$$class$groovy$swing$factory$CellEditorGetValueFactory()));
      return var1[194].callCurrent(this, "prepareEditor", var1[195].callConstructor($get$$class$groovy$swing$factory$CellEditorPrepareFactory()));
   }

   public Object registerThreading() {
      CallSite[] var1 = $getCallSiteArray();
      var1[196].callCurrent(this, "edt", ScriptBytecodeAdapter.getMethodPointer(this, "edt"));
      var1[197].callCurrent(this, "doOutside", ScriptBytecodeAdapter.getMethodPointer(this, "doOutside"));
      return var1[198].callCurrent(this, "doLater", ScriptBytecodeAdapter.getMethodPointer(this, "doLater"));
   }

   public void registerBeanFactory(String nodeName, String groupName, Class klass) {
      CallSite[] var4 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(var4[199].call($get$$class$java$awt$LayoutManager(), (Object)klass))) {
         var4[200].callCurrent(this, nodeName, groupName, var4[201].callConstructor($get$$class$groovy$swing$factory$LayoutFactory(), (Object)klass));
      } else if (DefaultTypeTransformation.booleanUnbox(var4[202].call($get$$class$javax$swing$JScrollPane(), (Object)klass))) {
         var4[203].callCurrent(this, nodeName, groupName, var4[204].callConstructor($get$$class$groovy$swing$factory$ScrollPaneFactory(), (Object)klass));
      } else if (DefaultTypeTransformation.booleanUnbox(var4[205].call($get$$class$javax$swing$JTable(), (Object)klass))) {
         var4[206].callCurrent(this, nodeName, groupName, var4[207].callConstructor($get$$class$groovy$swing$factory$TableFactory(), (Object)klass));
      } else if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(var4[208].call($get$$class$javax$swing$JComponent(), (Object)klass)) && !DefaultTypeTransformation.booleanUnbox(var4[209].call($get$$class$javax$swing$JApplet(), (Object)klass)) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(var4[210].call($get$$class$javax$swing$JDialog(), (Object)klass)) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(var4[211].call($get$$class$javax$swing$JFrame(), (Object)klass)) ? Boolean.FALSE : Boolean.TRUE) && !DefaultTypeTransformation.booleanUnbox(var4[212].call($get$$class$javax$swing$JWindow(), (Object)klass)) ? Boolean.FALSE : Boolean.TRUE)) {
         var4[213].callCurrent(this, nodeName, groupName, var4[214].callConstructor($get$$class$groovy$swing$factory$ComponentFactory(), (Object)klass));
      } else {
         ScriptBytecodeAdapter.invokeMethodOnSuperN($get$$class$groovy$util$FactoryBuilderSupport(), this, "registerBeanFactory", new Object[]{nodeName, groupName, klass});
      }

   }

   public SwingBuilder edt(Closure c) {
      Closure c = new Reference(c);
      CallSite[] var3 = $getCallSiteArray();
      var3[215].call(c.get(), (Object)this);
      if (DefaultTypeTransformation.booleanUnbox(!DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(headless)) && !DefaultTypeTransformation.booleanUnbox(var3[216].call($get$$class$javax$swing$SwingUtilities())) ? Boolean.FALSE : Boolean.TRUE)) {
         var3[217].call(c.get(), (Object)this);
      } else {
         Reference continuationData = new Reference((Map)ScriptBytecodeAdapter.castToType(var3[218].callCurrent(this), $get$$class$java$util$Map()));
         boolean var9 = false;

         try {
            var9 = true;
            if (!(c.get() instanceof MethodClosure)) {
               c.set((Closure)ScriptBytecodeAdapter.castToType(var3[219].call(c.get(), (Object)ScriptBytecodeAdapter.createList(new Object[]{this})), $get$$class$groovy$lang$Closure()));
            }

            var3[220].call($get$$class$javax$swing$SwingUtilities(), (Object)(new GeneratedClosure(this, this, c, continuationData) {
               private Reference<T> c;
               private Reference<T> continuationData;
               // $FF: synthetic field
               private static ClassInfo $staticClassInfo;
               // $FF: synthetic field
               private static SoftReference $callSiteArray;
               // $FF: synthetic field
               private static Class $class$groovy$swing$SwingBuilder$_edt_closure1;
               // $FF: synthetic field
               private static Class $class$java$lang$Object;
               // $FF: synthetic field
               private static Class $class$groovy$lang$Closure;
               // $FF: synthetic field
               private static Class $class$java$util$Map;

               public {
                  CallSite[] var5 = $getCallSiteArray();
                  this.c = (Reference)c;
                  this.continuationData = (Reference)continuationData;
               }

               public Object doCall(Object it) {
                  CallSite[] var2 = $getCallSiteArray();
                  var2[0].callCurrent(this, (Object)this.continuationData.get());
                  var2[1].call(this.c.get());
                  Map var10000 = (Map)ScriptBytecodeAdapter.castToType(var2[2].callCurrent(this), $get$$class$java$util$Map());
                  this.continuationData.set(var10000);
                  return var10000;
               }

               public Closure getC() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Closure)ScriptBytecodeAdapter.castToType(this.c.get(), $get$$class$groovy$lang$Closure());
               }

               public Map<String, Object> getContinuationData() {
                  CallSite[] var1 = $getCallSiteArray();
                  return (Map)ScriptBytecodeAdapter.castToType(this.continuationData.get(), $get$$class$java$util$Map());
               }

               public Object doCall() {
                  CallSite[] var1 = $getCallSiteArray();
                  return var1[3].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
               }

               // $FF: synthetic method
               protected MetaClass $getStaticMetaClass() {
                  if (this.getClass() == $get$$class$groovy$swing$SwingBuilder$_edt_closure1()) {
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
               private static void $createCallSiteArray_1(String[] var0) {
                  var0[0] = "restoreFromContinuationData";
                  var0[1] = "call";
                  var0[2] = "getContinuationData";
                  var0[3] = "doCall";
               }

               // $FF: synthetic method
               private static CallSiteArray $createCallSiteArray() {
                  String[] var0 = new String[4];
                  $createCallSiteArray_1(var0);
                  return new CallSiteArray($get$$class$groovy$swing$SwingBuilder$_edt_closure1(), var0);
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
               private static Class $get$$class$groovy$swing$SwingBuilder$_edt_closure1() {
                  Class var10000 = $class$groovy$swing$SwingBuilder$_edt_closure1;
                  if (var10000 == null) {
                     var10000 = $class$groovy$swing$SwingBuilder$_edt_closure1 = class$("groovy.swing.SwingBuilder$_edt_closure1");
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
               private static Class $get$$class$groovy$lang$Closure() {
                  Class var10000 = $class$groovy$lang$Closure;
                  if (var10000 == null) {
                     var10000 = $class$groovy$lang$Closure = class$("groovy.lang.Closure");
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
               static Class class$(String var0) {
                  try {
                     return Class.forName(var0);
                  } catch (ClassNotFoundException var2) {
                     throw new NoClassDefFoundError(var2.getMessage());
                  }
               }
            }));
            var9 = false;
         } catch (InterruptedException var10) {
            throw (Throwable)var3[221].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), "interrupted swing interaction", var10);
         } catch (InvocationTargetException var11) {
            throw (Throwable)var3[222].callConstructor($get$$class$groovy$lang$GroovyRuntimeException(), "exception in event dispatch thread", var3[223].call(var11));
         } finally {
            if (var9) {
               var3[225].callCurrent(this, (Object)continuationData.get());
            }
         }

         var3[224].callCurrent(this, (Object)continuationData.get());
      }

      return (SwingBuilder)ScriptBytecodeAdapter.castToType(this, $get$$class$groovy$swing$SwingBuilder());
   }

   public SwingBuilder doLater(Closure c) {
      CallSite[] var2 = $getCallSiteArray();
      var2[226].call(c, (Object)this);
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.box(headless))) {
         var2[227].call(c);
      } else {
         if (!(c instanceof MethodClosure)) {
            c = (Closure)ScriptBytecodeAdapter.castToType(var2[228].call(c, (Object)ScriptBytecodeAdapter.createList(new Object[]{this})), $get$$class$groovy$lang$Closure());
         }

         var2[229].call($get$$class$javax$swing$SwingUtilities(), (Object)c);
      }

      return (SwingBuilder)ScriptBytecodeAdapter.castToType(this, $get$$class$groovy$swing$SwingBuilder());
   }

   public SwingBuilder doOutside(Closure c) {
      CallSite[] var2 = $getCallSiteArray();
      var2[230].call(c, (Object)this);
      if (!(c instanceof MethodClosure)) {
         c = (Closure)ScriptBytecodeAdapter.castToType(var2[231].call(c, (Object)ScriptBytecodeAdapter.createList(new Object[]{this})), $get$$class$groovy$lang$Closure());
      }

      if (DefaultTypeTransformation.booleanUnbox(var2[232].call($get$$class$javax$swing$SwingUtilities()))) {
         var2[233].call($get$$class$java$lang$Thread(), (Object)c);
      } else {
         var2[234].call(c);
      }

      return (SwingBuilder)ScriptBytecodeAdapter.castToType(this, $get$$class$groovy$swing$SwingBuilder());
   }

   public static SwingBuilder edtBuilder(Closure c) {
      CallSite[] var1 = $getCallSiteArray();
      SwingBuilder builder = var1[235].callConstructor($get$$class$groovy$swing$SwingBuilder());
      return (SwingBuilder)ScriptBytecodeAdapter.castToType(var1[236].call(builder, (Object)c), $get$$class$groovy$swing$SwingBuilder());
   }

   /** @deprecated */
   @Deprecated
   public static SwingBuilder $static_methodMissing(String method, Object args) {
      CallSite[] var2 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(ScriptBytecodeAdapter.compareEqual(method, "build") && ScriptBytecodeAdapter.compareEqual(var2[237].callGetProperty(args), $const$0) ? Boolean.TRUE : Boolean.FALSE) && var2[238].call(args, (Object)$const$1) instanceof Closure ? Boolean.TRUE : Boolean.FALSE)) {
         return (SwingBuilder)ScriptBytecodeAdapter.castToType(var2[239].callStatic($get$$class$groovy$swing$SwingBuilder(), var2[240].call(args, (Object)$const$1)), $get$$class$groovy$swing$SwingBuilder());
      } else {
         throw (Throwable)var2[241].callConstructor($get$$class$groovy$lang$MissingMethodException(), method, $get$$class$groovy$swing$SwingBuilder(), args, Boolean.TRUE);
      }
   }

   public Object build(Closure c) {
      CallSite[] var2 = $getCallSiteArray();
      var2[242].call(c, (Object)this);
      return (Object)ScriptBytecodeAdapter.castToType(var2[243].call(c), $get$$class$java$lang$Object());
   }

   public KeyStroke shortcut(Object key, Object modifier) {
      CallSite[] var3 = $getCallSiteArray();
      return (KeyStroke)ScriptBytecodeAdapter.castToType(var3[244].call($get$$class$javax$swing$KeyStroke(), key, var3[245].call(var3[246].call(var3[247].call($get$$class$java$awt$Toolkit())), modifier)), $get$$class$javax$swing$KeyStroke());
   }

   public KeyStroke shortcut(String key, Object modifier) {
      CallSite[] var3 = $getCallSiteArray();
      KeyStroke ks = (KeyStroke)ScriptBytecodeAdapter.castToType(var3[248].call($get$$class$javax$swing$KeyStroke(), (Object)key), $get$$class$javax$swing$KeyStroke());
      return ScriptBytecodeAdapter.compareEqual(ks, (Object)null) ? (KeyStroke)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$KeyStroke()) : (KeyStroke)ScriptBytecodeAdapter.castToType(var3[249].call($get$$class$javax$swing$KeyStroke(), var3[250].call(ks), var3[251].call(var3[252].call(var3[253].call(ks), modifier), var3[254].call(var3[255].call($get$$class$java$awt$Toolkit())))), $get$$class$javax$swing$KeyStroke());
   }

   public static LookAndFeel lookAndFeel(Object laf, Closure initCode) {
      CallSite[] var2 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var2[256].callStatic($get$$class$groovy$swing$SwingBuilder(), ScriptBytecodeAdapter.createMap(new Object[0]), laf, initCode), $get$$class$javax$swing$LookAndFeel());
   }

   public static LookAndFeel lookAndFeel(Map attributes, Object laf, Closure initCode) {
      CallSite[] var3 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var3[257].call(var3[258].callGetProperty($get$$class$groovy$swing$LookAndFeelHelper()), laf, attributes, initCode), $get$$class$javax$swing$LookAndFeel());
   }

   public static LookAndFeel lookAndFeel(Object... param0) {
      // $FF: Couldn't be decompiled
   }

   private static LookAndFeel _laf(List s) {
      CallSite[] var1 = $getCallSiteArray();
      CallSite var10000 = var1[267];
      Class var10001 = $get$$class$groovy$swing$SwingBuilder();
      Object[] var10002 = new Object[0];
      Object[] var10003 = new Object[]{s};
      int[] var2 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var10000.callStatic(var10001, ScriptBytecodeAdapter.despreadList(var10002, var10003, var2)), $get$$class$javax$swing$LookAndFeel());
   }

   private static LookAndFeel _laf(String s, Map m) {
      CallSite[] var2 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var2[268].callStatic($get$$class$groovy$swing$SwingBuilder(), m, s, ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.asType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   private static LookAndFeel _laf(LookAndFeel laf, Map m) {
      CallSite[] var2 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var2[269].callStatic($get$$class$groovy$swing$SwingBuilder(), m, laf, ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.asType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   private static LookAndFeel _laf(String s) {
      CallSite[] var1 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var1[270].callStatic($get$$class$groovy$swing$SwingBuilder(), ScriptBytecodeAdapter.createMap(new Object[0]), s, ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.asType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   private static LookAndFeel _laf(LookAndFeel laf) {
      CallSite[] var1 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var1[271].callStatic($get$$class$groovy$swing$SwingBuilder(), ScriptBytecodeAdapter.createMap(new Object[0]), laf, ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.asType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   public static Object objectIDAttributeDelegate(Object builder, Object node, Object attributes) {
      CallSite[] var3 = $getCallSiteArray();
      Object var10000 = var3[272].call(builder, (Object)DELEGATE_PROPERTY_OBJECT_ID);
      if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
         var10000 = DEFAULT_DELEGATE_PROPERTY_OBJECT_ID;
      }

      Object idAttr = var10000;
      Object theID = var3[273].call(attributes, idAttr);
      return DefaultTypeTransformation.booleanUnbox(theID) ? var3[274].call(builder, theID, node) : null;
   }

   public static Object clientPropertyAttributeDelegate(Object builder, Object node, Object attributes) {
      Object node = new Reference(node);
      Object attributes = new Reference(attributes);
      CallSite[] var5 = $getCallSiteArray();
      Object clientPropertyMap = var5[275].call(attributes.get(), (Object)"clientProperties");
      var5[276].call(clientPropertyMap, (Object)(new GeneratedClosure($get$$class$groovy$swing$SwingBuilder(), $get$$class$groovy$swing$SwingBuilder(), node) {
         private Reference<T> node;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure2;

         public {
            CallSite[] var4 = $getCallSiteArray();
            this.node = (Reference)node;
         }

         public Object doCall(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[0].call(this.node.get(), keyx.get(), valuex.get());
         }

         public Object call(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[1].callCurrent(this, keyx.get(), valuex.get());
         }

         public Object getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return this.node.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure2()) {
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
         private static void $createCallSiteArray_1(String[] var0) {
            var0[0] = "putClientProperty";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure2(), var0);
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
         private static Class $get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure2() {
            Class var10000 = $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure2;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure2 = class$("groovy.swing.SwingBuilder$_clientPropertyAttributeDelegate_closure2");
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
      }));
      return var5[277].call(var5[278].call(attributes.get(), (Object)(new GeneratedClosure($get$$class$groovy$swing$SwingBuilder(), $get$$class$groovy$swing$SwingBuilder()) {
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$java$lang$Object;
         // $FF: synthetic field
         private static Class $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure3;

         public {
            CallSite[] var3 = $getCallSiteArray();
         }

         public Object doCall(Object it) {
            Object itx = new Reference(it);
            CallSite[] var3 = $getCallSiteArray();
            return ScriptBytecodeAdapter.findRegex(var3[0].callGetProperty(itx.get()), "clientProperty(\\w)");
         }

         public Object doCall() {
            CallSite[] var1 = $getCallSiteArray();
            return var1[1].callCurrent(this, (Object)ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()));
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure3()) {
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
         private static void $createCallSiteArray_1(String[] var0) {
            var0[0] = "key";
            var0[1] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[2];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure3(), var0);
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
         private static Class $get$$class$java$lang$Object() {
            Class var10000 = $class$java$lang$Object;
            if (var10000 == null) {
               var10000 = $class$java$lang$Object = class$("java.lang.Object");
            }

            return var10000;
         }

         // $FF: synthetic method
         private static Class $get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure3() {
            Class var10000 = $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure3;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure3 = class$("groovy.swing.SwingBuilder$_clientPropertyAttributeDelegate_closure3");
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
      })), (Object)(new GeneratedClosure($get$$class$groovy$swing$SwingBuilder(), $get$$class$groovy$swing$SwingBuilder(), node, attributes) {
         private Reference<T> node;
         private Reference<T> attributes;
         // $FF: synthetic field
         private static ClassInfo $staticClassInfo;
         // $FF: synthetic field
         private static SoftReference $callSiteArray;
         // $FF: synthetic field
         private static Class $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure4;

         public {
            CallSite[] var5 = $getCallSiteArray();
            this.node = (Reference)node;
            this.attributes = (Reference)attributes;
         }

         public Object doCall(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            var5[0].call(this.attributes.get(), keyx.get());
            return var5[1].call(this.node.get(), var5[2].call(keyx.get(), (Object)"clientProperty"), valuex.get());
         }

         public Object call(Object key, Object value) {
            Object keyx = new Reference(key);
            Object valuex = new Reference(value);
            CallSite[] var5 = $getCallSiteArray();
            return var5[3].callCurrent(this, keyx.get(), valuex.get());
         }

         public Object getNode() {
            CallSite[] var1 = $getCallSiteArray();
            return this.node.get();
         }

         public Object getAttributes() {
            CallSite[] var1 = $getCallSiteArray();
            return this.attributes.get();
         }

         // $FF: synthetic method
         protected MetaClass $getStaticMetaClass() {
            if (this.getClass() == $get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure4()) {
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
         private static void $createCallSiteArray_1(String[] var0) {
            var0[0] = "remove";
            var0[1] = "putClientProperty";
            var0[2] = "minus";
            var0[3] = "doCall";
         }

         // $FF: synthetic method
         private static CallSiteArray $createCallSiteArray() {
            String[] var0 = new String[4];
            $createCallSiteArray_1(var0);
            return new CallSiteArray($get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure4(), var0);
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
         private static Class $get$$class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure4() {
            Class var10000 = $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure4;
            if (var10000 == null) {
               var10000 = $class$groovy$swing$SwingBuilder$_clientPropertyAttributeDelegate_closure4 = class$("groovy.swing.SwingBuilder$_clientPropertyAttributeDelegate_closure4");
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
      }));
   }

   public void createKeyStrokeAction(Map attributes, JComponent component) {
      JComponent component = new Reference(component);
      CallSite[] var4 = $getCallSiteArray();
      component.set((JComponent)ScriptBytecodeAdapter.castToType(var4[279].callCurrent(this, attributes, component.get()), $get$$class$javax$swing$JComponent()));
      if (!DefaultTypeTransformation.booleanUnbox(var4[280].call(attributes, (Object)"keyStroke"))) {
         throw (Throwable)var4[281].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"You must define a value for keyStroke:");
      } else if (!DefaultTypeTransformation.booleanUnbox(var4[282].call(attributes, (Object)"action"))) {
         throw (Throwable)var4[283].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"You must define a value for action:");
      } else {
         Object var10000 = var4[284].call(attributes, (Object)"condition");
         if (!DefaultTypeTransformation.booleanUnbox(var10000)) {
            var10000 = var4[285].callGetProperty($get$$class$javax$swing$JComponent());
         }

         Object condition = new Reference(var10000);
         if (condition.get() instanceof GString) {
            condition.set((String)ScriptBytecodeAdapter.asType(condition.get(), $get$$class$java$lang$String()));
         }

         if (condition.get() instanceof String) {
            condition.set(var4[286].call(var4[287].call(condition.get()), " ", "_"));
            if (!DefaultTypeTransformation.booleanUnbox(var4[288].call(condition.get(), (Object)"WHEN_"))) {
               condition.set(var4[289].call("WHEN_", (Object)condition.get()));
            }
         }

         Object var6 = condition.get();
         if (!ScriptBytecodeAdapter.isCase(var6, var4[290].callGetProperty($get$$class$javax$swing$JComponent())) && !ScriptBytecodeAdapter.isCase(var6, var4[291].callGetProperty($get$$class$javax$swing$JComponent())) && !ScriptBytecodeAdapter.isCase(var6, var4[292].callGetProperty($get$$class$javax$swing$JComponent()))) {
            if (ScriptBytecodeAdapter.isCase(var6, "WHEN_FOCUSED")) {
               condition.set(var4[293].callGetProperty($get$$class$javax$swing$JComponent()));
            } else if (ScriptBytecodeAdapter.isCase(var6, "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT")) {
               condition.set(var4[294].callGetProperty($get$$class$javax$swing$JComponent()));
            } else if (ScriptBytecodeAdapter.isCase(var6, "WHEN_IN_FOCUSED_WINDOW")) {
               condition.set(var4[295].callGetProperty($get$$class$javax$swing$JComponent()));
            } else {
               condition.set(var4[296].callGetProperty($get$$class$javax$swing$JComponent()));
            }
         }

         Object actionKey = new Reference(var4[297].call(attributes, (Object)"actionKey"));
         if (!DefaultTypeTransformation.booleanUnbox(actionKey.get())) {
            actionKey.set(var4[298].call("Action", (Object)var4[299].call($get$$class$java$lang$Math(), (Object)var4[300].call(random))));
         }

         Object keyStroke = var4[301].call(attributes, (Object)"keyStroke");
         Object action = var4[302].call(attributes, (Object)"action");
         if (keyStroke instanceof GString) {
            keyStroke = (String)ScriptBytecodeAdapter.asType(keyStroke, $get$$class$java$lang$String());
         }

         if (DefaultTypeTransformation.booleanUnbox(!(keyStroke instanceof String) && !(keyStroke instanceof Number) ? Boolean.FALSE : Boolean.TRUE)) {
            keyStroke = ScriptBytecodeAdapter.createList(new Object[]{keyStroke});
         }

         var4[303].call(keyStroke, (Object)(new GeneratedClosure(this, this, condition, component, actionKey) {
            private Reference<T> condition;
            private Reference<T> component;
            private Reference<T> actionKey;
            // $FF: synthetic field
            private static ClassInfo $staticClassInfo;
            // $FF: synthetic field
            private static SoftReference $callSiteArray;
            // $FF: synthetic field
            private static Class $class$java$lang$Number;
            // $FF: synthetic field
            private static Class $class$javax$swing$KeyStroke;
            // $FF: synthetic field
            private static Class $class$javax$swing$JComponent;
            // $FF: synthetic field
            private static Class $class$java$lang$RuntimeException;
            // $FF: synthetic field
            private static Class $class$groovy$swing$SwingBuilder$_createKeyStrokeAction_closure5;
            // $FF: synthetic field
            private static Class $class$java$lang$String;

            public {
               CallSite[] var6 = $getCallSiteArray();
               this.condition = (Reference)condition;
               this.component = (Reference)component;
               this.actionKey = (Reference)actionKey;
            }

            public Object doCall(Object ks) {
               Object ksx = new Reference(ks);
               CallSite[] var3 = $getCallSiteArray();
               Object var4 = ksx.get();
               if (ScriptBytecodeAdapter.isCase(var4, $get$$class$javax$swing$KeyStroke())) {
                  return var3[0].call(var3[1].call(this.component.get(), this.condition.get()), ksx.get(), this.actionKey.get());
               } else if (ScriptBytecodeAdapter.isCase(var4, $get$$class$java$lang$String())) {
                  return var3[2].call(var3[3].call(this.component.get(), this.condition.get()), var3[4].call($get$$class$javax$swing$KeyStroke(), (Object)ksx.get()), this.actionKey.get());
               } else if (ScriptBytecodeAdapter.isCase(var4, $get$$class$java$lang$Number())) {
                  return var3[5].call(var3[6].call(this.component.get(), this.condition.get()), var3[7].call($get$$class$javax$swing$KeyStroke(), (Object)var3[8].call(ksx.get())), this.actionKey.get());
               } else {
                  throw (Throwable)var3[9].callConstructor($get$$class$java$lang$RuntimeException(), (Object)(new GStringImpl(new Object[]{ksx.get()}, new String[]{"Cannot apply ", " as a KeyStroke value."})));
               }
            }

            public Object getCondition() {
               CallSite[] var1 = $getCallSiteArray();
               return this.condition.get();
            }

            public JComponent getComponent() {
               CallSite[] var1 = $getCallSiteArray();
               return (JComponent)ScriptBytecodeAdapter.castToType(this.component.get(), $get$$class$javax$swing$JComponent());
            }

            public Object getActionKey() {
               CallSite[] var1 = $getCallSiteArray();
               return this.actionKey.get();
            }

            // $FF: synthetic method
            protected MetaClass $getStaticMetaClass() {
               if (this.getClass() == $get$$class$groovy$swing$SwingBuilder$_createKeyStrokeAction_closure5()) {
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
            private static void $createCallSiteArray_1(String[] var0) {
               var0[0] = "put";
               var0[1] = "getInputMap";
               var0[2] = "put";
               var0[3] = "getInputMap";
               var0[4] = "getKeyStroke";
               var0[5] = "put";
               var0[6] = "getInputMap";
               var0[7] = "getKeyStroke";
               var0[8] = "intValue";
               var0[9] = "<$constructor$>";
            }

            // $FF: synthetic method
            private static CallSiteArray $createCallSiteArray() {
               String[] var0 = new String[10];
               $createCallSiteArray_1(var0);
               return new CallSiteArray($get$$class$groovy$swing$SwingBuilder$_createKeyStrokeAction_closure5(), var0);
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
            private static Class $get$$class$java$lang$Number() {
               Class var10000 = $class$java$lang$Number;
               if (var10000 == null) {
                  var10000 = $class$java$lang$Number = class$("java.lang.Number");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$swing$KeyStroke() {
               Class var10000 = $class$javax$swing$KeyStroke;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$KeyStroke = class$("javax.swing.KeyStroke");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$javax$swing$JComponent() {
               Class var10000 = $class$javax$swing$JComponent;
               if (var10000 == null) {
                  var10000 = $class$javax$swing$JComponent = class$("javax.swing.JComponent");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$java$lang$RuntimeException() {
               Class var10000 = $class$java$lang$RuntimeException;
               if (var10000 == null) {
                  var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
               }

               return var10000;
            }

            // $FF: synthetic method
            private static Class $get$$class$groovy$swing$SwingBuilder$_createKeyStrokeAction_closure5() {
               Class var10000 = $class$groovy$swing$SwingBuilder$_createKeyStrokeAction_closure5;
               if (var10000 == null) {
                  var10000 = $class$groovy$swing$SwingBuilder$_createKeyStrokeAction_closure5 = class$("groovy.swing.SwingBuilder$_createKeyStrokeAction_closure5");
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
            static Class class$(String var0) {
               try {
                  return Class.forName(var0);
               } catch (ClassNotFoundException var2) {
                  throw new NoClassDefFoundError(var2.getMessage());
               }
            }
         }));
         var4[304].call(var4[305].callGetProperty(component.get()), actionKey.get(), action);
      }
   }

   private Object findTargetComponent(Map attributes, JComponent component) {
      CallSite[] var3 = $getCallSiteArray();
      if (DefaultTypeTransformation.booleanUnbox(component)) {
         return component;
      } else {
         Object c;
         if (DefaultTypeTransformation.booleanUnbox(var3[306].call(attributes, (Object)"component"))) {
            c = var3[307].call(attributes, (Object)"component");
            if (!(c instanceof JComponent)) {
               throw (Throwable)var3[308].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"The property component: is not of type JComponent.");
            } else {
               return c;
            }
         } else {
            c = var3[309].callCurrent(this);
            if (c instanceof JComponent) {
               return c;
            } else {
               throw (Throwable)var3[310].callConstructor($get$$class$java$lang$RuntimeException(), (Object)"You must define one of the following: a value of type JComponent, a component: attribute or nest this node inside another one that produces a JComponent.");
            }
         }
      }
   }

   // $FF: synthetic method
   protected MetaClass $getStaticMetaClass() {
      if (this.getClass() == $get$$class$groovy$swing$SwingBuilder()) {
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
   public Object this$dist$invoke$5(String name, Object args) {
      CallSite[] var3 = $getCallSiteArray();
      Class var10000 = $get$$class$groovy$swing$SwingBuilder();
      String var10002 = (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String());
      Object[] var10003 = new Object[0];
      Object[] var10004 = new Object[]{args};
      int[] var4 = new int[]{DefaultTypeTransformation.intUnbox(ScriptBytecodeAdapter.castToType(0, Integer.TYPE))};
      return ScriptBytecodeAdapter.invokeMethodOnCurrentN(var10000, this, var10002, ScriptBytecodeAdapter.despreadList(var10003, var10004, var4));
   }

   // $FF: synthetic method
   public void this$dist$set$5(String name, Object value) {
      CallSite[] var3 = $getCallSiteArray();
      ScriptBytecodeAdapter.setGroovyObjectField(value, $get$$class$groovy$swing$SwingBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   // $FF: synthetic method
   public Object this$dist$get$5(String name) {
      CallSite[] var2 = $getCallSiteArray();
      return ScriptBytecodeAdapter.getGroovyObjectField($get$$class$groovy$swing$SwingBuilder(), this, (String)ScriptBytecodeAdapter.castToType(new GStringImpl(new Object[]{name}, new String[]{"", ""}), $get$$class$java$lang$String()));
   }

   public KeyStroke shortcut(Object key) {
      CallSite[] var2 = $getCallSiteArray();
      return (KeyStroke)ScriptBytecodeAdapter.castToType(var2[311].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(key, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createPojoWrapper($const$1, $get$$class$java$lang$Object())), $get$$class$javax$swing$KeyStroke());
   }

   public KeyStroke shortcut(String key) {
      CallSite[] var2 = $getCallSiteArray();
      return (KeyStroke)ScriptBytecodeAdapter.castToType(var2[312].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(key, $get$$class$java$lang$String()), ScriptBytecodeAdapter.createPojoWrapper($const$1, $get$$class$java$lang$Object())), $get$$class$javax$swing$KeyStroke());
   }

   public static LookAndFeel lookAndFeel(Map attributes, Object laf) {
      CallSite[] var2 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var2[313].callStatic($get$$class$groovy$swing$SwingBuilder(), ScriptBytecodeAdapter.createPojoWrapper(attributes, $get$$class$java$util$Map()), ScriptBytecodeAdapter.createPojoWrapper(laf, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   public static LookAndFeel lookAndFeel(Map attributes) {
      CallSite[] var1 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var1[314].callStatic($get$$class$groovy$swing$SwingBuilder(), ScriptBytecodeAdapter.createPojoWrapper(attributes, $get$$class$java$util$Map()), ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   public static LookAndFeel lookAndFeel() {
      CallSite[] var0 = $getCallSiteArray();
      return (LookAndFeel)ScriptBytecodeAdapter.castToType(var0[315].callStatic($get$$class$groovy$swing$SwingBuilder(), ScriptBytecodeAdapter.createPojoWrapper(ScriptBytecodeAdapter.createMap(new Object[0]), $get$$class$java$util$Map()), ScriptBytecodeAdapter.createPojoWrapper((Object)null, $get$$class$java$lang$Object()), ScriptBytecodeAdapter.createGroovyObjectWrapper((Closure)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$groovy$lang$Closure()), $get$$class$groovy$lang$Closure())), $get$$class$javax$swing$LookAndFeel());
   }

   public void createKeyStrokeAction(Map attributes) {
      CallSite[] var2 = $getCallSiteArray();
      var2[316].callCurrent(this, ScriptBytecodeAdapter.createPojoWrapper(attributes, $get$$class$java$util$Map()), ScriptBytecodeAdapter.createPojoWrapper((JComponent)ScriptBytecodeAdapter.castToType((Object)null, $get$$class$javax$swing$JComponent()), $get$$class$javax$swing$JComponent()));
   }

   static {
      headless = DefaultTypeTransformation.booleanUnbox(Boolean.FALSE);
      LOG = (Logger)((Logger)ScriptBytecodeAdapter.castToType($getCallSiteArray()[317].call($get$$class$java$util$logging$Logger(), (Object)$getCallSiteArray()[318].callGetProperty($get$$class$groovy$swing$SwingBuilder())), $get$$class$java$util$logging$Logger()));
      random = (Random)$getCallSiteArray()[319].callConstructor($get$$class$java$util$Random());
   }

   // $FF: synthetic method
   public Object this$5$findTargetComponent(Map var1, JComponent var2) {
      return this.findTargetComponent(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$registerExplicitMethod(String var1, Closure var2) {
      super.registerExplicitMethod(var1, var2);
   }

   // $FF: synthetic method
   public Map super$4$popContext() {
      return super.popContext();
   }

   // $FF: synthetic method
   public Object super$4$getCurrent() {
      return super.getCurrent();
   }

   // $FF: synthetic method
   public Object super$4$getName(String var1) {
      return super.getName(var1);
   }

   // $FF: synthetic method
   public Object super$4$dispathNodeCall(Object var1, Object var2) {
      return super.dispathNodeCall(var1, var2);
   }

   // $FF: synthetic method
   public String super$4$getParentName() {
      return super.getParentName();
   }

   // $FF: synthetic method
   public Map super$4$getExplicitMethods() {
      return super.getExplicitMethods();
   }

   // $FF: synthetic method
   public Closure super$4$addPostInstantiateDelegate(Closure var1) {
      return super.addPostInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public void super$4$preInstantiate(Object var1, Map var2, Object var3) {
      super.preInstantiate(var1, var2, var3);
   }

   // $FF: synthetic method
   public Map super$4$getContext() {
      return super.getContext();
   }

   // $FF: synthetic method
   public Factory super$4$getParentFactory() {
      return super.getParentFactory();
   }

   // $FF: synthetic method
   public Object super$4$withBuilder(FactoryBuilderSupport var1, Closure var2) {
      return super.withBuilder(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$getParentNode() {
      return super.getParentNode();
   }

   // $FF: synthetic method
   public void super$4$autoRegisterNodes() {
      super.autoRegisterNodes();
   }

   // $FF: synthetic method
   public Object super$4$build(Script var1) {
      return super.build(var1);
   }

   // $FF: synthetic method
   public void super$4$postInstantiate(Object var1, Map var2, Object var3) {
      super.postInstantiate(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$2$setMetaClass(MetaClass var1) {
      super.setMetaClass(var1);
   }

   // $FF: synthetic method
   public Closure[] super$4$resolveExplicitProperty(String var1) {
      return super.resolveExplicitProperty(var1);
   }

   // $FF: synthetic method
   public void super$4$removePostInstantiateDelegate(Closure var1) {
      super.removePostInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public Set super$4$getRegistrationGroups() {
      return super.getRegistrationGroups();
   }

   // $FF: synthetic method
   public Class super$1$getClass() {
      return super.getClass();
   }

   // $FF: synthetic method
   public Map super$4$getFactories() {
      return super.getFactories();
   }

   // $FF: synthetic method
   public List super$4$getPreInstantiateDelegates() {
      return super.getPreInstantiateDelegates();
   }

   // $FF: synthetic method
   public Closure super$4$getNameMappingClosure() {
      return super.getNameMappingClosure();
   }

   // $FF: synthetic method
   public Object super$4$build(String var1, GroovyClassLoader var2) {
      return super.build(var1, var2);
   }

   // $FF: synthetic method
   public Closure super$4$addAttributeDelegate(Closure var1) {
      return super.addAttributeDelegate(var1);
   }

   // $FF: synthetic method
   public int super$1$hashCode() {
      return super.hashCode();
   }

   // $FF: synthetic method
   public void super$4$setNameMappingClosure(Closure var1) {
      super.setNameMappingClosure(var1);
   }

   // $FF: synthetic method
   public Map super$4$getLocalExplicitMethods() {
      return super.getLocalExplicitMethods();
   }

   // $FF: synthetic method
   public Object super$4$invokeMethod(String var1) {
      return super.invokeMethod(var1);
   }

   // $FF: synthetic method
   public Map super$4$getLocalExplicitProperties() {
      return super.getLocalExplicitProperties();
   }

   // $FF: synthetic method
   public void super$4$removeAttributeDelegate(Closure var1) {
      super.removeAttributeDelegate(var1);
   }

   // $FF: synthetic method
   public Object super$4$postNodeCompletion(Object var1, Object var2) {
      return super.postNodeCompletion(var1, var2);
   }

   // $FF: synthetic method
   public Map super$4$getExplicitProperties() {
      return super.getExplicitProperties();
   }

   // $FF: synthetic method
   public FactoryBuilderSupport super$4$getCurrentBuilder() {
      return super.getCurrentBuilder();
   }

   // $FF: synthetic method
   public Set super$4$getRegistrationGroupItems(String var1) {
      return super.getRegistrationGroupItems(var1);
   }

   // $FF: synthetic method
   public void super$4$registerExplicitProperty(String var1, String var2, Closure var3, Closure var4) {
      super.registerExplicitProperty(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public List super$4$getPostInstantiateDelegates() {
      return super.getPostInstantiateDelegates();
   }

   // $FF: synthetic method
   public LinkedList super$4$getContexts() {
      return super.getContexts();
   }

   // $FF: synthetic method
   public void super$4$addDisposalClosure(Closure var1) {
      super.addDisposalClosure(var1);
   }

   // $FF: synthetic method
   public void super$4$handleNodeAttributes(Object var1, Map var2) {
      super.handleNodeAttributes(var1, var2);
   }

   // $FF: synthetic method
   public String super$4$getCurrentName() {
      return super.getCurrentName();
   }

   // $FF: synthetic method
   public void super$4$setParent(Object var1, Object var2) {
      super.setParent(var1, var2);
   }

   // $FF: synthetic method
   public FactoryBuilderSupport super$4$getChildBuilder() {
      return super.getChildBuilder();
   }

   // $FF: synthetic method
   public String super$1$toString() {
      return super.toString();
   }

   // $FF: synthetic method
   public Map super$4$getParentContext() {
      return super.getParentContext();
   }

   // $FF: synthetic method
   public void super$4$setProperty(String var1, Object var2) {
      super.setProperty(var1, var2);
   }

   // $FF: synthetic method
   public List super$4$getAttributeDelegates() {
      return super.getAttributeDelegates();
   }

   // $FF: synthetic method
   public Map super$4$getContinuationData() {
      return super.getContinuationData();
   }

   // $FF: synthetic method
   public Closure super$4$addPostNodeCompletionDelegate(Closure var1) {
      return super.addPostNodeCompletionDelegate(var1);
   }

   // $FF: synthetic method
   public Object super$4$build(Class var1) {
      return super.build(var1);
   }

   // $FF: synthetic method
   public void super$4$registerFactory(String var1, String var2, Factory var3) {
      super.registerFactory(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$1$wait() {
      super.wait();
   }

   // $FF: synthetic method
   public Closure super$4$addPreInstantiateDelegate(Closure var1) {
      return super.addPreInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public MetaClass super$2$getMetaClass() {
      return super.getMetaClass();
   }

   // $FF: synthetic method
   public void super$4$setClosureDelegate(Closure var1, Object var2) {
      super.setClosureDelegate(var1, var2);
   }

   // $FF: synthetic method
   public void super$4$setVariable(String var1, Object var2) {
      super.setVariable(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$getContextAttribute(String var1) {
      return super.getContextAttribute(var1);
   }

   // $FF: synthetic method
   public Map super$4$getVariables() {
      return super.getVariables();
   }

   // $FF: synthetic method
   public void super$4$restoreFromContinuationData(Map var1) {
      super.restoreFromContinuationData(var1);
   }

   // $FF: synthetic method
   public Object super$4$createNode(Object var1, Map var2, Object var3) {
      return super.createNode(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$newContext() {
      super.newContext();
   }

   // $FF: synthetic method
   public void super$4$setNodeAttributes(Object var1, Map var2) {
      super.setNodeAttributes(var1, var2);
   }

   // $FF: synthetic method
   public FactoryBuilderSupport super$4$getProxyBuilder() {
      return super.getProxyBuilder();
   }

   // $FF: synthetic method
   public Object super$4$invokeMethod(String var1, Object var2) {
      return super.invokeMethod(var1, var2);
   }

   // $FF: synthetic method
   public Object super$4$withBuilder(FactoryBuilderSupport var1, String var2, Closure var3) {
      return super.withBuilder(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$4$getProperty(String var1) {
      return super.getProperty(var1);
   }

   // $FF: synthetic method
   public void super$1$notify() {
      super.notify();
   }

   // $FF: synthetic method
   public void super$4$nodeCompleted(Object var1, Object var2) {
      super.nodeCompleted(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$notifyAll() {
      super.notifyAll();
   }

   // $FF: synthetic method
   public Object super$4$withBuilder(Map var1, FactoryBuilderSupport var2, String var3, Closure var4) {
      return super.withBuilder(var1, var2, var3, var4);
   }

   // $FF: synthetic method
   public void super$4$registerBeanFactory(String var1, Class var2) {
      super.registerBeanFactory(var1, var2);
   }

   // $FF: synthetic method
   public List super$4$getPostNodeCompletionDelegates() {
      return super.getPostNodeCompletionDelegates();
   }

   // $FF: synthetic method
   public void super$4$registerExplicitProperty(String var1, Closure var2, Closure var3) {
      super.registerExplicitProperty(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$registerFactory(String var1, Factory var2) {
      super.registerFactory(var1, var2);
   }

   // $FF: synthetic method
   public boolean super$4$checkExplicitMethod(String var1, Object var2, Reference var3) {
      return super.checkExplicitMethod(var1, var2, var3);
   }

   // $FF: synthetic method
   public Object super$1$clone() {
      return super.clone();
   }

   // $FF: synthetic method
   public void super$1$wait(long var1, int var3) {
      super.wait(var1, var3);
   }

   // $FF: synthetic method
   public Object super$4$getVariable(String var1) {
      return super.getVariable(var1);
   }

   // $FF: synthetic method
   public void super$4$dispose() {
      super.dispose();
   }

   // $FF: synthetic method
   public void super$4$removePreInstantiateDelegate(Closure var1) {
      super.removePreInstantiateDelegate(var1);
   }

   // $FF: synthetic method
   public void super$1$finalize() {
      super.finalize();
   }

   // $FF: synthetic method
   public Factory super$4$resolveFactory(Object var1, Map var2, Object var3) {
      return super.resolveFactory(var1, var2, var3);
   }

   // $FF: synthetic method
   public Factory super$4$getCurrentFactory() {
      return super.getCurrentFactory();
   }

   // $FF: synthetic method
   public Map super$4$getLocalFactories() {
      return super.getLocalFactories();
   }

   // $FF: synthetic method
   public void super$4$removePostNodeCompletionDelegate(Closure var1) {
      super.removePostNodeCompletionDelegate(var1);
   }

   // $FF: synthetic method
   public Closure super$4$resolveExplicitMethod(String var1, Object var2) {
      return super.resolveExplicitMethod(var1, var2);
   }

   // $FF: synthetic method
   public void super$1$wait(long var1) {
      super.wait(var1);
   }

   // $FF: synthetic method
   public void super$4$registerBeanFactory(String var1, String var2, Class var3) {
      super.registerBeanFactory(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$setProxyBuilder(FactoryBuilderSupport var1) {
      super.setProxyBuilder(var1);
   }

   // $FF: synthetic method
   public void super$4$registerExplicitMethod(String var1, String var2, Closure var3) {
      super.registerExplicitMethod(var1, var2, var3);
   }

   // $FF: synthetic method
   public void super$4$reset() {
      super.reset();
   }

   // $FF: synthetic method
   public boolean super$1$equals(Object var1) {
      return super.equals(var1);
   }

   // $FF: synthetic method
   private static void $createCallSiteArray_1(String[] var0) {
      var0[0] = "isHeadless";
      var0[1] = "<$constructor$>";
      var0[2] = "putAt";
      var0[3] = "registerFactory";
      var0[4] = "<$constructor$>";
      var0[5] = "registerFactory";
      var0[6] = "<$constructor$>";
      var0[7] = "registerFactory";
      var0[8] = "<$constructor$>";
      var0[9] = "registerFactory";
      var0[10] = "<$constructor$>";
      var0[11] = "registerFactory";
      var0[12] = "<$constructor$>";
      var0[13] = "addAttributeDelegate";
      var0[14] = "addAttributeDelegate";
      var0[15] = "addAttributeDelegate";
      var0[16] = "registerFactory";
      var0[17] = "<$constructor$>";
      var0[18] = "registerExplicitMethod";
      var0[19] = "registerExplicitMethod";
      var0[20] = "<$constructor$>";
      var0[21] = "registerFactory";
      var0[22] = "addAttributeDelegate";
      var0[23] = "registerFactory";
      var0[24] = "<$constructor$>";
      var0[25] = "registerFactory";
      var0[26] = "<$constructor$>";
      var0[27] = "registerFactory";
      var0[28] = "<$constructor$>";
      var0[29] = "registerFactory";
      var0[30] = "<$constructor$>";
      var0[31] = "registerFactory";
      var0[32] = "<$constructor$>";
      var0[33] = "registerFactory";
      var0[34] = "<$constructor$>";
      var0[35] = "registerBeanFactory";
      var0[36] = "registerFactory";
      var0[37] = "<$constructor$>";
      var0[38] = "registerBeanFactory";
      var0[39] = "registerFactory";
      var0[40] = "<$constructor$>";
      var0[41] = "registerFactory";
      var0[42] = "<$constructor$>";
      var0[43] = "registerFactory";
      var0[44] = "<$constructor$>";
      var0[45] = "registerFactory";
      var0[46] = "<$constructor$>";
      var0[47] = "registerFactory";
      var0[48] = "<$constructor$>";
      var0[49] = "registerFactory";
      var0[50] = "<$constructor$>";
      var0[51] = "registerFactory";
      var0[52] = "<$constructor$>";
      var0[53] = "registerFactory";
      var0[54] = "<$constructor$>";
      var0[55] = "registerFactory";
      var0[56] = "<$constructor$>";
      var0[57] = "registerFactory";
      var0[58] = "<$constructor$>";
      var0[59] = "registerFactory";
      var0[60] = "<$constructor$>";
      var0[61] = "registerFactory";
      var0[62] = "<$constructor$>";
      var0[63] = "registerFactory";
      var0[64] = "<$constructor$>";
      var0[65] = "registerFactory";
      var0[66] = "<$constructor$>";
      var0[67] = "registerFactory";
      var0[68] = "<$constructor$>";
      var0[69] = "registerBeanFactory";
      var0[70] = "registerFactory";
      var0[71] = "<$constructor$>";
      var0[72] = "registerBeanFactory";
      var0[73] = "registerFactory";
      var0[74] = "<$constructor$>";
      var0[75] = "registerFactory";
      var0[76] = "<$constructor$>";
      var0[77] = "registerBeanFactory";
      var0[78] = "registerFactory";
      var0[79] = "<$constructor$>";
      var0[80] = "registerBeanFactory";
      var0[81] = "registerBeanFactory";
      var0[82] = "registerBeanFactory";
      var0[83] = "registerBeanFactory";
      var0[84] = "registerBeanFactory";
      var0[85] = "registerBeanFactory";
      var0[86] = "registerBeanFactory";
      var0[87] = "registerBeanFactory";
      var0[88] = "registerFactory";
      var0[89] = "<$constructor$>";
      var0[90] = "registerFactory";
      var0[91] = "<$constructor$>";
      var0[92] = "registerFactory";
      var0[93] = "<$constructor$>";
      var0[94] = "registerBeanFactory";
      var0[95] = "registerBeanFactory";
      var0[96] = "registerBeanFactory";
      var0[97] = "registerBeanFactory";
      var0[98] = "registerBeanFactory";
      var0[99] = "registerBeanFactory";
      var0[100] = "registerBeanFactory";
      var0[101] = "registerFactory";
      var0[102] = "<$constructor$>";
      var0[103] = "registerBeanFactory";
      var0[104] = "registerFactory";
      var0[105] = "<$constructor$>";
      var0[106] = "registerFactory";
      var0[107] = "<$constructor$>";
      var0[108] = "registerFactory";
      var0[109] = "<$constructor$>";
      var0[110] = "registerFactory";
      var0[111] = "<$constructor$>";
      var0[112] = "registerFactory";
      var0[113] = "<$constructor$>";
      var0[114] = "registerFactory";
      var0[115] = "<$constructor$>";
      var0[116] = "registerFactory";
      var0[117] = "<$constructor$>";
      var0[118] = "registerFactory";
      var0[119] = "<$constructor$>";
      var0[120] = "registerFactory";
      var0[121] = "<$constructor$>";
      var0[122] = "registerFactory";
      var0[123] = "<$constructor$>";
      var0[124] = "registerFactory";
      var0[125] = "<$constructor$>";
      var0[126] = "registerFactory";
      var0[127] = "<$constructor$>";
      var0[128] = "registerBeanFactory";
      var0[129] = "registerBeanFactory";
      var0[130] = "addAttributeDelegate";
      var0[131] = "addAttributeDelegate";
      var0[132] = "registerFactory";
      var0[133] = "<$constructor$>";
      var0[134] = "registerFactory";
      var0[135] = "<$constructor$>";
      var0[136] = "registerFactory";
      var0[137] = "<$constructor$>";
      var0[138] = "registerFactory";
      var0[139] = "<$constructor$>";
      var0[140] = "registerFactory";
      var0[141] = "<$constructor$>";
      var0[142] = "registerFactory";
      var0[143] = "<$constructor$>";
      var0[144] = "registerFactory";
      var0[145] = "<$constructor$>";
      var0[146] = "registerFactory";
      var0[147] = "<$constructor$>";
      var0[148] = "registerFactory";
      var0[149] = "<$constructor$>";
      var0[150] = "registerFactory";
      var0[151] = "<$constructor$>";
      var0[152] = "registerFactory";
      var0[153] = "<$constructor$>";
      var0[154] = "registerFactory";
      var0[155] = "<$constructor$>";
      var0[156] = "registerFactory";
      var0[157] = "<$constructor$>";
      var0[158] = "registerFactory";
      var0[159] = "<$constructor$>";
      var0[160] = "registerFactory";
      var0[161] = "<$constructor$>";
      var0[162] = "LOWERED";
      var0[163] = "registerFactory";
      var0[164] = "<$constructor$>";
      var0[165] = "RAISED";
      var0[166] = "registerFactory";
      var0[167] = "<$constructor$>";
      var0[168] = "LOWERED";
      var0[169] = "registerFactory";
      var0[170] = "<$constructor$>";
      var0[171] = "LOWERED";
      var0[172] = "registerFactory";
      var0[173] = "<$constructor$>";
      var0[174] = "RAISED";
      var0[175] = "registerFactory";
      var0[176] = "<$constructor$>";
      var0[177] = "registerFactory";
      var0[178] = "<$constructor$>";
      var0[179] = "registerFactory";
      var0[180] = "<$constructor$>";
      var0[181] = "registerFactory";
      var0[182] = "<$constructor$>";
      var0[183] = "<$constructor$>";
      var0[184] = "registerFactory";
      var0[185] = "registerFactory";
      var0[186] = "registerFactory";
      var0[187] = "<$constructor$>";
      var0[188] = "registerFactory";
      var0[189] = "registerFactory";
      var0[190] = "registerFactory";
      var0[191] = "<$constructor$>";
      var0[192] = "registerFactory";
      var0[193] = "<$constructor$>";
      var0[194] = "registerFactory";
      var0[195] = "<$constructor$>";
      var0[196] = "registerExplicitMethod";
      var0[197] = "registerExplicitMethod";
      var0[198] = "registerExplicitMethod";
      var0[199] = "isAssignableFrom";
      var0[200] = "registerFactory";
      var0[201] = "<$constructor$>";
      var0[202] = "isAssignableFrom";
      var0[203] = "registerFactory";
      var0[204] = "<$constructor$>";
      var0[205] = "isAssignableFrom";
      var0[206] = "registerFactory";
      var0[207] = "<$constructor$>";
      var0[208] = "isAssignableFrom";
      var0[209] = "isAssignableFrom";
      var0[210] = "isAssignableFrom";
      var0[211] = "isAssignableFrom";
      var0[212] = "isAssignableFrom";
      var0[213] = "registerFactory";
      var0[214] = "<$constructor$>";
      var0[215] = "setDelegate";
      var0[216] = "isEventDispatchThread";
      var0[217] = "call";
      var0[218] = "getContinuationData";
      var0[219] = "curry";
      var0[220] = "invokeAndWait";
      var0[221] = "<$constructor$>";
      var0[222] = "<$constructor$>";
      var0[223] = "getTargetException";
      var0[224] = "restoreFromContinuationData";
      var0[225] = "restoreFromContinuationData";
      var0[226] = "setDelegate";
      var0[227] = "call";
      var0[228] = "curry";
      var0[229] = "invokeLater";
      var0[230] = "setDelegate";
      var0[231] = "curry";
      var0[232] = "isEventDispatchThread";
      var0[233] = "start";
      var0[234] = "call";
      var0[235] = "<$constructor$>";
      var0[236] = "edt";
      var0[237] = "length";
      var0[238] = "getAt";
      var0[239] = "edtBuilder";
      var0[240] = "getAt";
      var0[241] = "<$constructor$>";
      var0[242] = "setDelegate";
      var0[243] = "call";
      var0[244] = "getKeyStroke";
      var0[245] = "or";
      var0[246] = "getMenuShortcutKeyMask";
      var0[247] = "getDefaultToolkit";
      var0[248] = "getKeyStroke";
      var0[249] = "getKeyStroke";
      var0[250] = "getKeyCode";
      var0[251] = "or";
      var0[252] = "or";
      var0[253] = "getModifiers";
      var0[254] = "getMenuShortcutKeyMask";
      var0[255] = "getDefaultToolkit";
      var0[256] = "lookAndFeel";
      var0[257] = "lookAndFeel";
      var0[258] = "instance";
      var0[259] = "length";
      var0[260] = "lookAndFeel";
      var0[261] = "getAt";
      var0[262] = "iterator";
      var0[263] = "_laf";
      var0[264] = "_laf";
      var0[265] = "fine";
      var0[266] = "warning";
      var0[267] = "_laf";
      var0[268] = "lookAndFeel";
      var0[269] = "lookAndFeel";
      var0[270] = "lookAndFeel";
      var0[271] = "lookAndFeel";
      var0[272] = "getAt";
      var0[273] = "remove";
      var0[274] = "setVariable";
      var0[275] = "remove";
      var0[276] = "each";
      var0[277] = "each";
      var0[278] = "findAll";
      var0[279] = "findTargetComponent";
      var0[280] = "containsKey";
      var0[281] = "<$constructor$>";
      var0[282] = "containsKey";
      var0[283] = "<$constructor$>";
      var0[284] = "remove";
      var0[285] = "WHEN_FOCUSED";
      var0[286] = "replaceAll";
      var0[287] = "toUpperCase";
      var0[288] = "startsWith";
      var0[289] = "plus";
      var0[290] = "WHEN_FOCUSED";
      var0[291] = "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT";
      var0[292] = "WHEN_IN_FOCUSED_WINDOW";
      var0[293] = "WHEN_FOCUSED";
      var0[294] = "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT";
      var0[295] = "WHEN_IN_FOCUSED_WINDOW";
      var0[296] = "WHEN_FOCUSED";
      var0[297] = "remove";
      var0[298] = "plus";
      var0[299] = "abs";
      var0[300] = "nextLong";
      var0[301] = "remove";
      var0[302] = "remove";
      var0[303] = "each";
      var0[304] = "put";
      var0[305] = "actionMap";
      var0[306] = "containsKey";
      var0[307] = "remove";
      var0[308] = "<$constructor$>";
      var0[309] = "getCurrent";
      var0[310] = "<$constructor$>";
      var0[311] = "shortcut";
      var0[312] = "shortcut";
      var0[313] = "lookAndFeel";
      var0[314] = "lookAndFeel";
      var0[315] = "lookAndFeel";
      var0[316] = "createKeyStrokeAction";
      var0[317] = "getLogger";
      var0[318] = "name";
      var0[319] = "<$constructor$>";
   }

   // $FF: synthetic method
   private static CallSiteArray $createCallSiteArray() {
      String[] var0 = new String[320];
      $createCallSiteArray_1(var0);
      return new CallSiteArray($get$$class$groovy$swing$SwingBuilder(), var0);
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
   private static Class $get$$class$javax$swing$SpinnerListModel() {
      Class var10000 = $class$javax$swing$SpinnerListModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$SpinnerListModel = class$("javax.swing.SpinnerListModel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TextArgWidgetFactory() {
      Class var10000 = $class$groovy$swing$factory$TextArgWidgetFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TextArgWidgetFactory = class$("groovy.swing.factory.TextArgWidgetFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TRFactory() {
      Class var10000 = $class$groovy$swing$factory$TRFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TRFactory = class$("groovy.swing.factory.TRFactory");
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
   private static Class $get$$class$javax$swing$JPopupMenu() {
      Class var10000 = $class$javax$swing$JPopupMenu;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JPopupMenu = class$("javax.swing.JPopupMenu");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Math() {
      Class var10000 = $class$java$lang$Math;
      if (var10000 == null) {
         var10000 = $class$java$lang$Math = class$("java.lang.Math");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JLayeredPane() {
      Class var10000 = $class$javax$swing$JLayeredPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JLayeredPane = class$("javax.swing.JLayeredPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$util$FactoryBuilderSupport() {
      Class var10000 = $class$groovy$util$FactoryBuilderSupport;
      if (var10000 == null) {
         var10000 = $class$groovy$util$FactoryBuilderSupport = class$("groovy.util.FactoryBuilderSupport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$FormattedTextFactory() {
      Class var10000 = $class$groovy$swing$factory$FormattedTextFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$FormattedTextFactory = class$("groovy.swing.factory.FormattedTextFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ImageIconFactory() {
      Class var10000 = $class$groovy$swing$factory$ImageIconFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ImageIconFactory = class$("groovy.swing.factory.ImageIconFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TableFactory() {
      Class var10000 = $class$groovy$swing$factory$TableFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TableFactory = class$("groovy.swing.factory.TableFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ActionFactory() {
      Class var10000 = $class$groovy$swing$factory$ActionFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ActionFactory = class$("groovy.swing.factory.ActionFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$InternalFrameFactory() {
      Class var10000 = $class$groovy$swing$factory$InternalFrameFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$InternalFrameFactory = class$("groovy.swing.factory.InternalFrameFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$HGlueFactory() {
      Class var10000 = $class$groovy$swing$factory$HGlueFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$HGlueFactory = class$("groovy.swing.factory.HGlueFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JToolBar() {
      Class var10000 = $class$javax$swing$JToolBar;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JToolBar = class$("javax.swing.JToolBar");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JLabel() {
      Class var10000 = $class$javax$swing$JLabel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JLabel = class$("javax.swing.JLabel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JPasswordField() {
      Class var10000 = $class$javax$swing$JPasswordField;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JPasswordField = class$("javax.swing.JPasswordField");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$VGlueFactory() {
      Class var10000 = $class$groovy$swing$factory$VGlueFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$VGlueFactory = class$("groovy.swing.factory.VGlueFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JApplet() {
      Class var10000 = $class$javax$swing$JApplet;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JApplet = class$("javax.swing.JApplet");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JComponent() {
      Class var10000 = $class$javax$swing$JComponent;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JComponent = class$("javax.swing.JComponent");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$FrameFactory() {
      Class var10000 = $class$groovy$swing$factory$FrameFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$FrameFactory = class$("groovy.swing.factory.FrameFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JRadioButton() {
      Class var10000 = $class$javax$swing$JRadioButton;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JRadioButton = class$("javax.swing.JRadioButton");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$SwingUtilities() {
      Class var10000 = $class$javax$swing$SwingUtilities;
      if (var10000 == null) {
         var10000 = $class$javax$swing$SwingUtilities = class$("javax.swing.SwingUtilities");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JPanel() {
      Class var10000 = $class$javax$swing$JPanel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JPanel = class$("javax.swing.JPanel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BoxLayoutFactory() {
      Class var10000 = $class$groovy$swing$factory$BoxLayoutFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BoxLayoutFactory = class$("groovy.swing.factory.BoxLayoutFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JProgressBar() {
      Class var10000 = $class$javax$swing$JProgressBar;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JProgressBar = class$("javax.swing.JProgressBar");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$LinkedList() {
      Class var10000 = $class$java$util$LinkedList;
      if (var10000 == null) {
         var10000 = $class$java$util$LinkedList = class$("java.util.LinkedList");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$SpringLayout() {
      Class var10000 = $class$javax$swing$SpringLayout;
      if (var10000 == null) {
         var10000 = $class$javax$swing$SpringLayout = class$("javax.swing.SpringLayout");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$FlowLayout() {
      Class var10000 = $class$java$awt$FlowLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$FlowLayout = class$("java.awt.FlowLayout");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ListFactory() {
      Class var10000 = $class$groovy$swing$factory$ListFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ListFactory = class$("groovy.swing.factory.ListFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$SpinnerNumberModel() {
      Class var10000 = $class$javax$swing$SpinnerNumberModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$SpinnerNumberModel = class$("javax.swing.SpinnerNumberModel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JScrollBar() {
      Class var10000 = $class$javax$swing$JScrollBar;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JScrollBar = class$("javax.swing.JScrollBar");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$PropertyColumnFactory() {
      Class var10000 = $class$groovy$swing$factory$PropertyColumnFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$PropertyColumnFactory = class$("groovy.swing.factory.PropertyColumnFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JMenuBar() {
      Class var10000 = $class$javax$swing$JMenuBar;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JMenuBar = class$("javax.swing.JMenuBar");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$VStrutFactory() {
      Class var10000 = $class$groovy$swing$factory$VStrutFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$VStrutFactory = class$("groovy.swing.factory.VStrutFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JSpinner() {
      Class var10000 = $class$javax$swing$JSpinner;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JSpinner = class$("javax.swing.JSpinner");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$RigidAreaFactory() {
      Class var10000 = $class$groovy$swing$factory$RigidAreaFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$RigidAreaFactory = class$("groovy.swing.factory.RigidAreaFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TableLayoutFactory() {
      Class var10000 = $class$groovy$swing$factory$TableLayoutFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TableLayoutFactory = class$("groovy.swing.factory.TableLayoutFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$RendererUpdateFactory() {
      Class var10000 = $class$groovy$swing$factory$RendererUpdateFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$RendererUpdateFactory = class$("groovy.swing.factory.RendererUpdateFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$Random() {
      Class var10000 = $class$java$util$Random;
      if (var10000 == null) {
         var10000 = $class$java$util$Random = class$("java.util.Random");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$GridLayout() {
      Class var10000 = $class$java$awt$GridLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$GridLayout = class$("java.awt.GridLayout");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JRadioButtonMenuItem() {
      Class var10000 = $class$javax$swing$JRadioButtonMenuItem;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JRadioButtonMenuItem = class$("javax.swing.JRadioButtonMenuItem");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$GraphicsEnvironment() {
      Class var10000 = $class$java$awt$GraphicsEnvironment;
      if (var10000 == null) {
         var10000 = $class$java$awt$GraphicsEnvironment = class$("java.awt.GraphicsEnvironment");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JWindow() {
      Class var10000 = $class$javax$swing$JWindow;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JWindow = class$("javax.swing.JWindow");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ColumnModelFactory() {
      Class var10000 = $class$groovy$swing$factory$ColumnModelFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ColumnModelFactory = class$("groovy.swing.factory.ColumnModelFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JDialog() {
      Class var10000 = $class$javax$swing$JDialog;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JDialog = class$("javax.swing.JDialog");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JMenuItem() {
      Class var10000 = $class$javax$swing$JMenuItem;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JMenuItem = class$("javax.swing.JMenuItem");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$LayoutFactory() {
      Class var10000 = $class$groovy$swing$factory$LayoutFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$LayoutFactory = class$("groovy.swing.factory.LayoutFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$DialogFactory() {
      Class var10000 = $class$groovy$swing$factory$DialogFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$DialogFactory = class$("groovy.swing.factory.DialogFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ColumnFactory() {
      Class var10000 = $class$groovy$swing$factory$ColumnFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ColumnFactory = class$("groovy.swing.factory.ColumnFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$VBoxFactory() {
      Class var10000 = $class$groovy$swing$factory$VBoxFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$VBoxFactory = class$("groovy.swing.factory.VBoxFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BindGroupFactory() {
      Class var10000 = $class$groovy$swing$factory$BindGroupFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BindGroupFactory = class$("groovy.swing.factory.BindGroupFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$RendererFactory() {
      Class var10000 = $class$groovy$swing$factory$RendererFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$RendererFactory = class$("groovy.swing.factory.RendererFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$LineBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$LineBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$LineBorderFactory = class$("groovy.swing.factory.LineBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$util$logging$Logger() {
      Class var10000 = $class$java$util$logging$Logger;
      if (var10000 == null) {
         var10000 = $class$java$util$logging$Logger = class$("java.util.logging.Logger");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JEditorPane() {
      Class var10000 = $class$javax$swing$JEditorPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JEditorPane = class$("javax.swing.JEditorPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$CompoundBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$CompoundBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$CompoundBorderFactory = class$("groovy.swing.factory.CompoundBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TDFactory() {
      Class var10000 = $class$groovy$swing$factory$TDFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TDFactory = class$("groovy.swing.factory.TDFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$LookAndFeel() {
      Class var10000 = $class$javax$swing$LookAndFeel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$LookAndFeel = class$("javax.swing.LookAndFeel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$OverlayLayout() {
      Class var10000 = $class$javax$swing$OverlayLayout;
      if (var10000 == null) {
         var10000 = $class$javax$swing$OverlayLayout = class$("javax.swing.OverlayLayout");
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
   private static Class $get$$class$groovy$swing$factory$EmptyBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$EmptyBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$EmptyBorderFactory = class$("groovy.swing.factory.EmptyBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JOptionPane() {
      Class var10000 = $class$javax$swing$JOptionPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JOptionPane = class$("javax.swing.JOptionPane");
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
   private static Class $get$$class$groovy$lang$GroovyRuntimeException() {
      Class var10000 = $class$groovy$lang$GroovyRuntimeException;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$GroovyRuntimeException = class$("groovy.lang.GroovyRuntimeException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$MatteBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$MatteBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$MatteBorderFactory = class$("groovy.swing.factory.MatteBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$CardLayout() {
      Class var10000 = $class$java$awt$CardLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$CardLayout = class$("java.awt.CardLayout");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JTabbedPane() {
      Class var10000 = $class$javax$swing$JTabbedPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JTabbedPane = class$("javax.swing.JTabbedPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$RuntimeException() {
      Class var10000 = $class$java$lang$RuntimeException;
      if (var10000 == null) {
         var10000 = $class$java$lang$RuntimeException = class$("java.lang.RuntimeException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ComponentFactory() {
      Class var10000 = $class$groovy$swing$factory$ComponentFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ComponentFactory = class$("groovy.swing.factory.ComponentFactory");
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
   private static Class $get$$class$groovy$swing$factory$CellEditorFactory() {
      Class var10000 = $class$groovy$swing$factory$CellEditorFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$CellEditorFactory = class$("groovy.swing.factory.CellEditorFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$KeyStroke() {
      Class var10000 = $class$javax$swing$KeyStroke;
      if (var10000 == null) {
         var10000 = $class$javax$swing$KeyStroke = class$("javax.swing.KeyStroke");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$table$TableColumn() {
      Class var10000 = $class$javax$swing$table$TableColumn;
      if (var10000 == null) {
         var10000 = $class$javax$swing$table$TableColumn = class$("javax.swing.table.TableColumn");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ScrollPaneFactory() {
      Class var10000 = $class$groovy$swing$factory$ScrollPaneFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ScrollPaneFactory = class$("groovy.swing.factory.ScrollPaneFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JScrollPane() {
      Class var10000 = $class$javax$swing$JScrollPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JScrollPane = class$("javax.swing.JScrollPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JSlider() {
      Class var10000 = $class$javax$swing$JSlider;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JSlider = class$("javax.swing.JSlider");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$HBoxFactory() {
      Class var10000 = $class$groovy$swing$factory$HBoxFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$HBoxFactory = class$("groovy.swing.factory.HBoxFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$lang$Thread() {
      Class var10000 = $class$java$lang$Thread;
      if (var10000 == null) {
         var10000 = $class$java$lang$Thread = class$("java.lang.Thread");
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
   private static Class $get$$class$groovy$lang$MissingMethodException() {
      Class var10000 = $class$groovy$lang$MissingMethodException;
      if (var10000 == null) {
         var10000 = $class$groovy$lang$MissingMethodException = class$("groovy.lang.MissingMethodException");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JViewport() {
      Class var10000 = $class$javax$swing$JViewport;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JViewport = class$("javax.swing.JViewport");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BoxFactory() {
      Class var10000 = $class$groovy$swing$factory$BoxFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BoxFactory = class$("groovy.swing.factory.BoxFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BevelBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$BevelBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BevelBorderFactory = class$("groovy.swing.factory.BevelBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$Toolkit() {
      Class var10000 = $class$java$awt$Toolkit;
      if (var10000 == null) {
         var10000 = $class$java$awt$Toolkit = class$("java.awt.Toolkit");
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
   private static Class $get$$class$groovy$swing$factory$CellEditorGetValueFactory() {
      Class var10000 = $class$groovy$swing$factory$CellEditorGetValueFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$CellEditorGetValueFactory = class$("groovy.swing.factory.CellEditorGetValueFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$GlueFactory() {
      Class var10000 = $class$groovy$swing$factory$GlueFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$GlueFactory = class$("groovy.swing.factory.GlueFactory");
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
   private static Class $get$$class$groovy$swing$factory$TableModelFactory() {
      Class var10000 = $class$groovy$swing$factory$TableModelFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TableModelFactory = class$("groovy.swing.factory.TableModelFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$GridBagConstraints() {
      Class var10000 = $class$java$awt$GridBagConstraints;
      if (var10000 == null) {
         var10000 = $class$java$awt$GridBagConstraints = class$("java.awt.GridBagConstraints");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$SeparatorFactory() {
      Class var10000 = $class$groovy$swing$factory$SeparatorFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$SeparatorFactory = class$("groovy.swing.factory.SeparatorFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TitledBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$TitledBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TitledBorderFactory = class$("groovy.swing.factory.TitledBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JTextArea() {
      Class var10000 = $class$javax$swing$JTextArea;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JTextArea = class$("javax.swing.JTextArea");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JDesktopPane() {
      Class var10000 = $class$javax$swing$JDesktopPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JDesktopPane = class$("javax.swing.JDesktopPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JButton() {
      Class var10000 = $class$javax$swing$JButton;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JButton = class$("javax.swing.JButton");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ClosureColumnFactory() {
      Class var10000 = $class$groovy$swing$factory$ClosureColumnFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ClosureColumnFactory = class$("groovy.swing.factory.ClosureColumnFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ComboBoxFactory() {
      Class var10000 = $class$groovy$swing$factory$ComboBoxFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ComboBoxFactory = class$("groovy.swing.factory.ComboBoxFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$MapFactory() {
      Class var10000 = $class$groovy$swing$factory$MapFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$MapFactory = class$("groovy.swing.factory.MapFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$border$BevelBorder() {
      Class var10000 = $class$javax$swing$border$BevelBorder;
      if (var10000 == null) {
         var10000 = $class$javax$swing$border$BevelBorder = class$("javax.swing.border.BevelBorder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JFrame() {
      Class var10000 = $class$javax$swing$JFrame;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JFrame = class$("javax.swing.JFrame");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$SpinnerDateModel() {
      Class var10000 = $class$javax$swing$SpinnerDateModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$SpinnerDateModel = class$("javax.swing.SpinnerDateModel");
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
   private static Class $get$$class$groovy$swing$factory$EtchedBorderFactory() {
      Class var10000 = $class$groovy$swing$factory$EtchedBorderFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$EtchedBorderFactory = class$("groovy.swing.factory.EtchedBorderFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$border$EtchedBorder() {
      Class var10000 = $class$javax$swing$border$EtchedBorder;
      if (var10000 == null) {
         var10000 = $class$javax$swing$border$EtchedBorder = class$("javax.swing.border.EtchedBorder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$WidgetFactory() {
      Class var10000 = $class$groovy$swing$factory$WidgetFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$WidgetFactory = class$("groovy.swing.factory.WidgetFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$GridBagFactory() {
      Class var10000 = $class$groovy$swing$factory$GridBagFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$GridBagFactory = class$("groovy.swing.factory.GridBagFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JMenu() {
      Class var10000 = $class$javax$swing$JMenu;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JMenu = class$("javax.swing.JMenu");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JToggleButton() {
      Class var10000 = $class$javax$swing$JToggleButton;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JToggleButton = class$("javax.swing.JToggleButton");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JCheckBox() {
      Class var10000 = $class$javax$swing$JCheckBox;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JCheckBox = class$("javax.swing.JCheckBox");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JFileChooser() {
      Class var10000 = $class$javax$swing$JFileChooser;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JFileChooser = class$("javax.swing.JFileChooser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BindProxyFactory() {
      Class var10000 = $class$groovy$swing$factory$BindProxyFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BindProxyFactory = class$("groovy.swing.factory.BindProxyFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$BindFactory() {
      Class var10000 = $class$groovy$swing$factory$BindFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$BindFactory = class$("groovy.swing.factory.BindFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$HStrutFactory() {
      Class var10000 = $class$groovy$swing$factory$HStrutFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$HStrutFactory = class$("groovy.swing.factory.HStrutFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$TabbedPaneFactory() {
      Class var10000 = $class$groovy$swing$factory$TabbedPaneFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$TabbedPaneFactory = class$("groovy.swing.factory.TabbedPaneFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$WindowFactory() {
      Class var10000 = $class$groovy$swing$factory$WindowFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$WindowFactory = class$("groovy.swing.factory.WindowFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JTextPane() {
      Class var10000 = $class$javax$swing$JTextPane;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JTextPane = class$("javax.swing.JTextPane");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$DefaultBoundedRangeModel() {
      Class var10000 = $class$javax$swing$DefaultBoundedRangeModel;
      if (var10000 == null) {
         var10000 = $class$javax$swing$DefaultBoundedRangeModel = class$("javax.swing.DefaultBoundedRangeModel");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JColorChooser() {
      Class var10000 = $class$javax$swing$JColorChooser;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JColorChooser = class$("javax.swing.JColorChooser");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$BorderLayout() {
      Class var10000 = $class$java$awt$BorderLayout;
      if (var10000 == null) {
         var10000 = $class$java$awt$BorderLayout = class$("java.awt.BorderLayout");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$LookAndFeelHelper() {
      Class var10000 = $class$groovy$swing$LookAndFeelHelper;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$LookAndFeelHelper = class$("groovy.swing.LookAndFeelHelper");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$CollectionFactory() {
      Class var10000 = $class$groovy$swing$factory$CollectionFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$CollectionFactory = class$("groovy.swing.factory.CollectionFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$RichActionWidgetFactory() {
      Class var10000 = $class$groovy$swing$factory$RichActionWidgetFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$RichActionWidgetFactory = class$("groovy.swing.factory.RichActionWidgetFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$javax$swing$JCheckBoxMenuItem() {
      Class var10000 = $class$javax$swing$JCheckBoxMenuItem;
      if (var10000 == null) {
         var10000 = $class$javax$swing$JCheckBoxMenuItem = class$("javax.swing.JCheckBoxMenuItem");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$SwingBuilder() {
      Class var10000 = $class$groovy$swing$SwingBuilder;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$SwingBuilder = class$("groovy.swing.SwingBuilder");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$SplitPaneFactory() {
      Class var10000 = $class$groovy$swing$factory$SplitPaneFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$SplitPaneFactory = class$("groovy.swing.factory.SplitPaneFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$CellEditorPrepareFactory() {
      Class var10000 = $class$groovy$swing$factory$CellEditorPrepareFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$CellEditorPrepareFactory = class$("groovy.swing.factory.CellEditorPrepareFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$groovy$swing$factory$ButtonGroupFactory() {
      Class var10000 = $class$groovy$swing$factory$ButtonGroupFactory;
      if (var10000 == null) {
         var10000 = $class$groovy$swing$factory$ButtonGroupFactory = class$("groovy.swing.factory.ButtonGroupFactory");
      }

      return var10000;
   }

   // $FF: synthetic method
   private static Class $get$$class$java$awt$LayoutManager() {
      Class var10000 = $class$java$awt$LayoutManager;
      if (var10000 == null) {
         var10000 = $class$java$awt$LayoutManager = class$("java.awt.LayoutManager");
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
