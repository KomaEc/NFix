package bsh.util;

import bsh.BshClassManager;
import bsh.ClassPathException;
import bsh.Interpreter;
import bsh.StringUtil;
import bsh.classpath.BshClassPath;
import bsh.classpath.ClassManagerImpl;
import bsh.classpath.ClassPathListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ClassBrowser extends JSplitPane implements ListSelectionListener, ClassPathListener {
   BshClassPath classPath;
   BshClassManager classManager;
   JFrame frame;
   JInternalFrame iframe;
   JList classlist;
   JList conslist;
   JList mlist;
   JList fieldlist;
   ClassBrowser.PackageTree ptree;
   JTextArea methodLine;
   JTree tree;
   String[] packagesList;
   String[] classesList;
   Constructor[] consList;
   Method[] methodList;
   Field[] fieldList;
   String selectedPackage;
   Class selectedClass;
   private static final Color LIGHT_BLUE = new Color(245, 245, 255);

   public ClassBrowser() {
      this(BshClassManager.createClassManager((Interpreter)null));
   }

   public ClassBrowser(BshClassManager var1) {
      super(0, true);
      this.classManager = var1;
      this.setBorder((Border)null);
      SplitPaneUI var2 = this.getUI();
      if (var2 instanceof BasicSplitPaneUI) {
         ((BasicSplitPaneUI)var2).getDivider().setBorder((Border)null);
      }

   }

   String[] toSortedStrings(Collection var1) {
      ArrayList var2 = new ArrayList(var1);
      String[] var3 = (String[])var2.toArray(new String[0]);
      return StringUtil.bubbleSort(var3);
   }

   void setClist(String var1) {
      this.selectedPackage = var1;
      Object var2 = this.classPath.getClassesForPackage(var1);
      if (var2 == null) {
         var2 = new HashSet();
      }

      ArrayList var3 = new ArrayList();
      Iterator var4 = ((Set)var2).iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (var5.indexOf("$") == -1) {
            var3.add(BshClassPath.splitClassname(var5)[1]);
         }
      }

      this.classesList = this.toSortedStrings(var3);
      this.classlist.setListData(this.classesList);
   }

   String[] parseConstructors(Constructor[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Constructor var4 = var1[var3];
         var2[var3] = StringUtil.methodString(var4.getName(), var4.getParameterTypes());
      }

      return var2;
   }

   String[] parseMethods(Method[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = StringUtil.methodString(var1[var3].getName(), var1[var3].getParameterTypes());
      }

      return var2;
   }

   String[] parseFields(Field[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Field var4 = var1[var3];
         var2[var3] = var4.getName();
      }

      return var2;
   }

   Constructor[] getPublicConstructors(Constructor[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (Modifier.isPublic(var1[var3].getModifiers())) {
            var2.addElement(var1[var3]);
         }
      }

      Constructor[] var4 = new Constructor[var2.size()];
      var2.copyInto(var4);
      return var4;
   }

   Method[] getPublicMethods(Method[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (Modifier.isPublic(var1[var3].getModifiers())) {
            var2.addElement(var1[var3]);
         }
      }

      Method[] var4 = new Method[var2.size()];
      var2.copyInto(var4);
      return var4;
   }

   Field[] getPublicFields(Field[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (Modifier.isPublic(var1[var3].getModifiers())) {
            var2.addElement(var1[var3]);
         }
      }

      Field[] var4 = new Field[var2.size()];
      var2.copyInto(var4);
      return var4;
   }

   void setConslist(Class var1) {
      if (var1 == null) {
         this.conslist.setListData(new Object[0]);
      } else {
         this.consList = this.getPublicConstructors(var1.getDeclaredConstructors());
         this.conslist.setListData(this.parseConstructors(this.consList));
      }
   }

   void setMlist(String var1) {
      if (var1 == null) {
         this.mlist.setListData(new Object[0]);
         this.setConslist((Class)null);
         this.setClassTree((Class)null);
      } else {
         try {
            if (this.selectedPackage.equals("<unpackaged>")) {
               this.selectedClass = this.classManager.classForName(var1);
            } else {
               this.selectedClass = this.classManager.classForName(this.selectedPackage + "." + var1);
            }
         } catch (Exception var3) {
            System.err.println(var3);
            return;
         }

         if (this.selectedClass == null) {
            System.err.println("class not found: " + var1);
         } else {
            this.methodList = this.getPublicMethods(this.selectedClass.getDeclaredMethods());
            this.mlist.setListData(this.parseMethods(this.methodList));
            this.setClassTree(this.selectedClass);
            this.setConslist(this.selectedClass);
            this.setFieldList(this.selectedClass);
         }
      }
   }

   void setFieldList(Class var1) {
      if (var1 == null) {
         this.fieldlist.setListData(new Object[0]);
      } else {
         this.fieldList = this.getPublicFields(var1.getDeclaredFields());
         this.fieldlist.setListData(this.parseFields(this.fieldList));
      }
   }

   void setMethodLine(Object var1) {
      this.methodLine.setText(var1 == null ? "" : var1.toString());
   }

   void setClassTree(Class var1) {
      if (var1 == null) {
         this.tree.setModel((TreeModel)null);
      } else {
         DefaultMutableTreeNode var2 = null;
         DefaultMutableTreeNode var3 = null;

         DefaultMutableTreeNode var4;
         do {
            var4 = new DefaultMutableTreeNode(var1.toString());
            if (var3 != null) {
               var4.add(var3);
            } else {
               var2 = var4;
            }

            var3 = var4;
         } while((var1 = var1.getSuperclass()) != null);

         this.tree.setModel(new DefaultTreeModel(var4));
         TreeNode var5 = var2.getParent();
         if (var5 != null) {
            TreePath var6 = new TreePath(((DefaultTreeModel)this.tree.getModel()).getPathToRoot(var5));
            this.tree.expandPath(var6);
         }

      }
   }

   JPanel labeledPane(JComponent var1, String var2) {
      JPanel var3 = new JPanel(new BorderLayout());
      var3.add("Center", var1);
      var3.add("North", new JLabel(var2, 0));
      return var3;
   }

   public void init() throws ClassPathException {
      this.classPath = ((ClassManagerImpl)this.classManager).getClassPath();
      this.classPath.addListener(this);
      Set var1 = this.classPath.getPackagesSet();
      this.ptree = new ClassBrowser.PackageTree(var1);
      this.ptree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent var1) {
            TreePath var2 = var1.getPath();
            Object[] var3 = var2.getPath();
            StringBuffer var4 = new StringBuffer();

            for(int var5 = 1; var5 < var3.length; ++var5) {
               var4.append(var3[var5].toString());
               if (var5 + 1 < var3.length) {
                  var4.append(".");
               }
            }

            ClassBrowser.this.setClist(var4.toString());
         }
      });
      this.classlist = new JList();
      this.classlist.setBackground(LIGHT_BLUE);
      this.classlist.addListSelectionListener(this);
      this.conslist = new JList();
      this.conslist.addListSelectionListener(this);
      this.mlist = new JList();
      this.mlist.setBackground(LIGHT_BLUE);
      this.mlist.addListSelectionListener(this);
      this.fieldlist = new JList();
      this.fieldlist.addListSelectionListener(this);
      JSplitPane var2 = this.splitPane(0, true, this.labeledPane(new JScrollPane(this.conslist), "Constructors"), this.labeledPane(new JScrollPane(this.mlist), "Methods"));
      JSplitPane var3 = this.splitPane(0, true, var2, this.labeledPane(new JScrollPane(this.fieldlist), "Fields"));
      JSplitPane var4 = this.splitPane(1, true, this.labeledPane(new JScrollPane(this.classlist), "Classes"), var3);
      var4 = this.splitPane(1, true, this.labeledPane(new JScrollPane(this.ptree), "Packages"), var4);
      JPanel var5 = new JPanel(new BorderLayout());
      this.methodLine = new JTextArea(1, 60);
      this.methodLine.setBackground(LIGHT_BLUE);
      this.methodLine.setEditable(false);
      this.methodLine.setLineWrap(true);
      this.methodLine.setWrapStyleWord(true);
      this.methodLine.setFont(new Font("Monospaced", 1, 14));
      this.methodLine.setMargin(new Insets(5, 5, 5, 5));
      this.methodLine.setBorder(new MatteBorder(1, 0, 1, 0, LIGHT_BLUE.darker().darker()));
      var5.add("North", this.methodLine);
      JPanel var6 = new JPanel(new BorderLayout());
      this.tree = new JTree();
      this.tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent var1) {
            ClassBrowser.this.driveToClass(var1.getPath().getLastPathComponent().toString());
         }
      });
      this.tree.setBorder(BorderFactory.createRaisedBevelBorder());
      this.setClassTree((Class)null);
      var6.add("Center", this.tree);
      var5.add("Center", var6);
      var5.setPreferredSize(new Dimension(150, 150));
      this.setTopComponent(var4);
      this.setBottomComponent(var5);
   }

   private JSplitPane splitPane(int var1, boolean var2, JComponent var3, JComponent var4) {
      JSplitPane var5 = new JSplitPane(var1, var2, var3, var4);
      var5.setBorder((Border)null);
      SplitPaneUI var6 = var5.getUI();
      if (var6 instanceof BasicSplitPaneUI) {
         ((BasicSplitPaneUI)var6).getDivider().setBorder((Border)null);
      }

      return var5;
   }

   public static void main(String[] var0) throws Exception {
      ClassBrowser var1 = new ClassBrowser();
      var1.init();
      JFrame var2 = new JFrame("BeanShell Class Browser v1.0");
      var2.getContentPane().add("Center", var1);
      var1.setFrame(var2);
      var2.pack();
      var2.setVisible(true);
   }

   public void setFrame(JFrame var1) {
      this.frame = var1;
   }

   public void setFrame(JInternalFrame var1) {
      this.iframe = var1;
   }

   public void valueChanged(ListSelectionEvent var1) {
      if (var1.getSource() == this.classlist) {
         String var2 = (String)this.classlist.getSelectedValue();
         this.setMlist(var2);
         String var3;
         if (var2 == null) {
            var3 = "Package: " + this.selectedPackage;
         } else {
            String var4 = this.selectedPackage.equals("<unpackaged>") ? var2 : this.selectedPackage + "." + var2;
            var3 = var4 + " (from " + this.classPath.getClassSource(var4) + ")";
         }

         this.setMethodLine(var3);
      } else {
         int var5;
         if (var1.getSource() == this.mlist) {
            var5 = this.mlist.getSelectedIndex();
            if (var5 == -1) {
               this.setMethodLine((Object)null);
            } else {
               this.setMethodLine(this.methodList[var5]);
            }
         } else if (var1.getSource() == this.conslist) {
            var5 = this.conslist.getSelectedIndex();
            if (var5 == -1) {
               this.setMethodLine((Object)null);
            } else {
               this.setMethodLine(this.consList[var5]);
            }
         } else if (var1.getSource() == this.fieldlist) {
            var5 = this.fieldlist.getSelectedIndex();
            if (var5 == -1) {
               this.setMethodLine((Object)null);
            } else {
               this.setMethodLine(this.fieldList[var5]);
            }
         }
      }

   }

   public void driveToClass(String var1) {
      String[] var2 = BshClassPath.splitClassname(var1);
      String var3 = var2[0];
      String var4 = var2[1];
      if (this.classPath.getClassesForPackage(var3).size() != 0) {
         this.ptree.setSelectedPackage(var3);

         for(int var5 = 0; var5 < this.classesList.length; ++var5) {
            if (this.classesList[var5].equals(var4)) {
               this.classlist.setSelectedIndex(var5);
               this.classlist.ensureIndexIsVisible(var5);
               break;
            }
         }

      }
   }

   public void toFront() {
      if (this.frame != null) {
         this.frame.toFront();
      } else if (this.iframe != null) {
         this.iframe.toFront();
      }

   }

   public void classPathChanged() {
      Set var1 = this.classPath.getPackagesSet();
      this.ptree.setPackages(var1);
      this.setClist((String)null);
   }

   class PackageTree extends JTree {
      TreeNode root;
      DefaultTreeModel treeModel;
      Map nodeForPackage = new HashMap();

      PackageTree(Collection var2) {
         this.setPackages(var2);
         this.setRootVisible(false);
         this.setShowsRootHandles(true);
         this.setExpandsSelectedPaths(true);
      }

      public void setPackages(Collection var1) {
         this.treeModel = this.makeTreeModel(var1);
         this.setModel(this.treeModel);
      }

      DefaultTreeModel makeTreeModel(Collection var1) {
         HashMap var2 = new HashMap();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            String[] var5 = StringUtil.split(var4, ".");
            Object var6 = var2;

            for(int var7 = 0; var7 < var5.length; ++var7) {
               String var8 = var5[var7];
               Object var9 = (Map)((Map)var6).get(var8);
               if (var9 == null) {
                  var9 = new HashMap();
                  ((Map)var6).put(var8, var9);
               }

               var6 = var9;
            }
         }

         this.root = this.makeNode(var2, "root");
         this.mapNodes(this.root);
         return new DefaultTreeModel(this.root);
      }

      MutableTreeNode makeNode(Map var1, String var2) {
         DefaultMutableTreeNode var3 = new DefaultMutableTreeNode(var2);
         Iterator var4 = var1.keySet().iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            Map var6 = (Map)var1.get(var5);
            if (var6.size() == 0) {
               DefaultMutableTreeNode var7 = new DefaultMutableTreeNode(var5);
               var3.add(var7);
            } else {
               MutableTreeNode var8 = this.makeNode(var6, var5);
               var3.add(var8);
            }
         }

         return var3;
      }

      void mapNodes(TreeNode var1) {
         this.addNodeMap(var1);
         Enumeration var2 = var1.children();

         while(var2.hasMoreElements()) {
            TreeNode var3 = (TreeNode)var2.nextElement();
            this.mapNodes(var3);
         }

      }

      void addNodeMap(TreeNode var1) {
         StringBuffer var2 = new StringBuffer();

         for(TreeNode var3 = var1; var3 != this.root; var3 = var3.getParent()) {
            var2.insert(0, var3.toString());
            if (var3.getParent() != this.root) {
               var2.insert(0, ".");
            }
         }

         String var4 = var2.toString();
         this.nodeForPackage.put(var4, var1);
      }

      void setSelectedPackage(String var1) {
         DefaultMutableTreeNode var2 = (DefaultMutableTreeNode)this.nodeForPackage.get(var1);
         if (var2 != null) {
            TreePath var3 = new TreePath(this.treeModel.getPathToRoot(var2));
            this.setSelectionPath(var3);
            ClassBrowser.this.setClist(var1);
            this.scrollPathToVisible(var3);
         }
      }
   }
}
