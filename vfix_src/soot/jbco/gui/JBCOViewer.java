package soot.jbco.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JBCOViewer extends JFrame {
   private static final Logger logger = LoggerFactory.getLogger(JBCOViewer.class);
   private JMenuItem speedMenuItem;
   private JMenuItem sizeMenuItem;
   private JMenuItem protMenuItem;
   private JLabel LabelMainClass;
   private JRadioButton RadioSummary;
   private JRadioButton RadioVerbose;
   private JPanel PanelExecute;
   private JPanel PanelTransforms;
   private JPanel PanelBasicOptions;
   private JTabbedPane TabbedPane;
   private JMenuItem exitMenuItem;
   private JTextField TextFieldConstraint;
   private JTextPane PaneExplain;
   private JList AvoidList;
   private JPanel jPanel1;
   private JTextField ClasspathTextField;
   private JLabel LabelClassPath;
   private JTextField TextFieldMain;
   public JMenuItem openFileMenuItem;
   public JMenuItem newFileMenuItem;
   private JMenu jMenu3;
   private JMenuBar jMenuBar1;
   private JTextPane jTextPane1;
   private JRadioButton DebugRadio;
   private JTextField WorkingDirTextField;
   private JLabel LabelWorkingDir;
   private JTextPane DefaultClassPathPane;
   public JTextArea TextAreaOutput;
   public JScrollPane jScrollPane1;
   private JPanel jPanel2;
   private JTextField TextFieldMinMem;
   private JButton ButtonAddItem;
   private JComboBox ComboBoxDefWeight;
   private JLabel LabelDefWeight;
   private JLabel LabelTransformHeading;
   private JList ListTransforms;
   private JComboBox ComboWeight;
   private JLabel LabelOutputDir;
   private JTextField TextField;
   private JTextField TextFieldJVMArgs;
   private JLabel LabelJVM;
   private JTextField TextFieldMaxMem;
   private JLabel LabelMaxMem;
   private JLabel LabelMinMem;
   private JTextField TextFieldOutputFolder;
   private JButton ButtonSaveOutput;
   private JButton ButtonRemove;
   private JFrame thisRef;
   private RunnerThread runner;
   static int previousSelected = -1;
   static ListModel[] models = new ListModel[20];
   static String[][] optionStrings = new String[][]{{"Rename Classes", "Rename Methods", "Rename Fields", "Build API Buffer Methods", "Build Library Buffer Classes", "Goto Instruction Augmentation", "Add Dead Switch Statements", "Convert Arith. Expr. To Bit Ops", "Convert Branches to JSR Instructions", "Disobey Constructor Conventions", "Reuse Duplicate Sequences", "Replace If(Non)Nulls with Try-Catch", "Indirect If Instructions", "Pack Locals into Bitfields", "Reorder Loads Above Ifs", "Combine Try and Catch Blocks", "Embed Constants in Fields", "Partially Trap Switches"}, {"wjtp.jbco_cr", "wjtp.jbco_mr", "wjtp.jbco_fr", "wjtp.jbco_bapibm", "wjtp.jbco_blbc", "jtp.jbco_gia", "jtp.jbco_adss", "jtp.jbco_cae2bo", "bb.jbco_cb2ji", "bb.jbco_dcc", "bb.jbco_rds", "bb.jbco_riitcb", "bb.jbco_iii", "bb.jbco_plvb", "bb.jbco_rlaii", "bb.jbco_ctbcb", "bb.jbco_ecvf", "bb.jbco_ptss"}};
   static int[][] defaultWeights = new int[][]{{9, 9, 9, 9, 9, 9, 6, 9, 0, 0, 3, 9, 6, 3, 9, 9, 0, 0}, {0, 0, 0, 0, 9, 6, 0, 9, 9, 9, 0, 9, 0, 0, 9, 9, 0, 9}, {5, 5, 5, 6, 9, 9, 5, 9, 9, 5, 7, 9, 9, 2, 9, 9, 0, 9}};
   static String[] arguments = null;

   public static void main(String[] args) {
      arguments = args;
      JBCOViewer inst = new JBCOViewer();
      inst.setVisible(true);
   }

   public JBCOViewer() {
      this.initGUI();
   }

   private void initGUI() {
      this.thisRef = this;

      try {
         this.setDefaultCloseOperation(3);
         this.setIconImage((new ImageIcon(this.getClass().getClassLoader().getResource("soot/jbco/gui/jbco.jpg"))).getImage());
         this.setTitle("Java Bytecode Obfuscator");
         this.TabbedPane = new JTabbedPane();
         this.getContentPane().add(this.TabbedPane, "Center");
         this.PanelBasicOptions = new JPanel();
         this.TabbedPane.addTab("Basic Options", (Icon)null, this.PanelBasicOptions, (String)null);
         this.PanelBasicOptions.setLayout((LayoutManager)null);
         this.PanelBasicOptions.setPreferredSize(new Dimension(623, 413));
         this.RadioVerbose = new JRadioButton();
         this.PanelBasicOptions.add(this.RadioVerbose);
         this.RadioVerbose.setText("Verbose Output");
         this.RadioVerbose.setBounds(7, 9, 130, 26);
         this.RadioVerbose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               if (JBCOViewer.this.RadioVerbose.isSelected()) {
                  JBCOViewer.this.RadioSummary.setSelected(false);
               }

            }
         });
         this.RadioSummary = new JRadioButton();
         this.PanelBasicOptions.add(this.RadioSummary);
         this.RadioSummary.setText("Silent Output");
         this.RadioSummary.setBounds(147, 7, 140, 28);
         this.RadioSummary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               if (JBCOViewer.this.RadioSummary.isSelected()) {
                  JBCOViewer.this.RadioVerbose.setSelected(false);
               }

            }
         });
         this.LabelMainClass = new JLabel();
         this.PanelBasicOptions.add(this.LabelMainClass);
         this.LabelMainClass.setText("Main Class");
         this.LabelMainClass.setHorizontalTextPosition(0);
         this.LabelMainClass.setBounds(14, 98, 77, 28);
         this.TextFieldMain = new JTextField();
         this.PanelBasicOptions.add(this.TextFieldMain);
         this.TextFieldMain.setBounds(98, 98, 245, 28);
         this.ClasspathTextField = new JTextField();
         this.PanelBasicOptions.add(this.ClasspathTextField);
         this.ClasspathTextField.setBounds(98, 203, 511, 28);
         this.LabelClassPath = new JLabel();
         this.PanelBasicOptions.add(this.LabelClassPath);
         this.LabelClassPath.setText("Classpath");
         this.LabelClassPath.setBounds(14, 203, 77, 28);
         this.LabelMinMem = new JLabel();
         this.PanelBasicOptions.add(this.LabelMinMem);
         this.LabelMinMem.setText("Minimum Memory (MB)");
         this.LabelMinMem.setBounds(378, 7, 161, 28);
         this.LabelMaxMem = new JLabel();
         this.PanelBasicOptions.add(this.LabelMaxMem);
         this.LabelMaxMem.setText("Maximum Memory (MB)");
         this.LabelMaxMem.setBounds(378, 42, 161, 28);
         this.TextFieldMinMem = new JTextField();
         this.PanelBasicOptions.add(this.TextFieldMinMem);
         this.TextFieldMinMem.setBounds(546, 7, 63, 28);
         this.TextFieldMinMem.setText("256");
         this.TextFieldMaxMem = new JTextField();
         this.PanelBasicOptions.add(this.TextFieldMaxMem);
         this.TextFieldMaxMem.setText("1024");
         this.TextFieldMaxMem.setBounds(546, 42, 63, 28);
         this.LabelJVM = new JLabel();
         this.PanelBasicOptions.add(this.LabelJVM);
         this.LabelJVM.setText("JVM Args");
         this.LabelJVM.setBounds(14, 42, 77, 28);
         this.TextFieldJVMArgs = new JTextField();
         this.PanelBasicOptions.add(this.TextFieldJVMArgs);
         this.TextFieldJVMArgs.setBounds(98, 42, 245, 28);
         this.TextField = new JTextField();
         this.PanelBasicOptions.add(this.TextField);
         this.TextField.setBounds(98, 133, 511, 28);
         this.LabelOutputDir = new JLabel();
         this.PanelBasicOptions.add(this.LabelOutputDir);
         this.LabelOutputDir.setText("Output Dir");
         this.LabelOutputDir.setBounds(14, 133, 77, 28);
         this.jPanel2 = new JPanel();
         this.PanelBasicOptions.add(this.jPanel2);
         this.jPanel2.setBounds(14, 84, 595, 7);
         this.jPanel2.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));
         this.jPanel2.setPreferredSize(new Dimension(2, 2));
         this.jPanel2.setSize(595, 2);
         this.DefaultClassPathPane = new JTextPane();
         this.DefaultClassPathPane.setText("./:/usr/lib/jvm/java-1.5.0-sun-1.5.0.06/jre/lib/charsets.jar\n:/usr/lib/jvm/java-1.5.0-sun-1.5.0.06/jre/lib/jce.jar\n:/usr/lib/jvm/java-1.5.0-sun-1.5.0.06/jre/lib/jsse.jar\n:/usr/lib/jvm/java-1.5.0-sun-1.5.0.06/jre/lib/rt.jar");
         if (arguments != null) {
            for(int i = 0; i < arguments.length; ++i) {
               if (arguments[i].equals("-cp") || arguments[i].equals("-classpath") && arguments.length > i + 1) {
                  StringTokenizer cptokenizer = new StringTokenizer(arguments[i + 1], ":");

                  for(String cp = cptokenizer.nextToken(); cptokenizer.hasMoreTokens(); cp = cp + "\n:" + cptokenizer.nextToken()) {
                  }

                  this.DefaultClassPathPane.setText(arguments[i + 1]);
               }
            }
         }

         this.PanelBasicOptions.add(this.DefaultClassPathPane);
         this.DefaultClassPathPane.setBounds(98, 238, 518, 133);
         this.LabelWorkingDir = new JLabel();
         this.PanelBasicOptions.add(this.LabelWorkingDir);
         this.LabelWorkingDir.setText("Working Dir");
         this.LabelWorkingDir.setBounds(14, 168, 84, 28);
         this.WorkingDirTextField = new JTextField();
         this.WorkingDirTextField.setText(System.getProperty("user.dir"));
         this.PanelBasicOptions.add(this.WorkingDirTextField);
         this.WorkingDirTextField.setBounds(98, 168, 511, 28);
         this.DebugRadio = new JRadioButton();
         this.PanelBasicOptions.add(this.DebugRadio);
         this.DebugRadio.setText("Debug");
         this.DebugRadio.setBounds(280, 7, 84, 28);
         this.jTextPane1 = new JTextPane();
         this.PanelBasicOptions.add(this.jTextPane1);
         this.jTextPane1.setText("Default Classpath");
         this.jTextPane1.setBounds(14, 238, 84, 35);
         this.PanelTransforms = new JPanel();
         this.TabbedPane.addTab("Transforms", (Icon)null, this.PanelTransforms, (String)null);
         this.PanelTransforms.setLayout((LayoutManager)null);
         this.PanelTransforms.setPreferredSize(new Dimension(630, 385));
         this.jPanel1 = new JPanel();
         this.PanelTransforms.add(this.jPanel1);
         this.jPanel1.setBounds(245, 49, 378, 329);
         this.jPanel1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
         this.jPanel1.setLayout((LayoutManager)null);
         ListModel ListTransformsModel = new DefaultComboBoxModel(new String[0]);
         this.AvoidList = new JList();
         this.jPanel1.add(this.AvoidList);
         this.AvoidList.setModel(ListTransformsModel);
         this.AvoidList.setBounds(7, 112, 364, 210);
         this.AvoidList.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(0, 0, 0)));
         this.AvoidList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
               int length = JBCOViewer.this.AvoidList.getSelectedIndices().length;
               if (length < 1) {
                  JBCOViewer.this.ButtonRemove.setEnabled(false);
               } else {
                  JBCOViewer.this.ButtonRemove.setEnabled(true);
               }

            }
         });
         this.TextFieldConstraint = new JTextField();
         this.jPanel1.add(this.TextFieldConstraint);
         this.TextFieldConstraint.setBounds(7, 42, 294, 28);
         this.ButtonRemove = new JButton();
         this.jPanel1.add(this.ButtonRemove);
         this.ButtonRemove.setText("Remove Item");
         this.ButtonRemove.setBounds(231, 77, 133, 28);
         this.ButtonRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               int[] index = JBCOViewer.this.AvoidList.getSelectedIndices();
               if (index.length < 1) {
                  Toolkit.getDefaultToolkit().beep();
               } else {
                  Object[] o = new Object[index.length];
                  DefaultComboBoxModel lm = (DefaultComboBoxModel)JBCOViewer.this.AvoidList.getModel();

                  int i;
                  for(i = 0; i < index.length; ++i) {
                     o[i] = lm.getElementAt(index[i]);
                  }

                  for(i = 0; i < index.length; ++i) {
                     lm.removeElement(o[i]);
                  }

                  JBCOViewer.models[JBCOViewer.previousSelected] = lm;
               }
            }
         });
         ListTransformsModel = new DefaultComboBoxModel(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
         this.ComboWeight = new JComboBox();
         this.jPanel1.add(this.ComboWeight);
         this.ComboWeight.setModel(ListTransformsModel);
         this.ComboWeight.setBounds(308, 42, 56, 28);
         this.LabelTransformHeading = new JLabel();
         this.jPanel1.add(this.LabelTransformHeading);
         this.LabelTransformHeading.setText("Rename Classes");
         this.LabelTransformHeading.setBounds(7, 7, 182, 28);
         this.LabelDefWeight = new JLabel();
         this.jPanel1.add(this.LabelDefWeight);
         this.LabelDefWeight.setText("Default Weight");
         this.LabelDefWeight.setBounds(203, 7, 98, 28);
         ListTransformsModel = new DefaultComboBoxModel(new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"});
         this.ComboBoxDefWeight = new JComboBox();
         this.jPanel1.add(this.ComboBoxDefWeight);
         this.ComboBoxDefWeight.setModel(ListTransformsModel);
         this.ComboBoxDefWeight.setBounds(308, 7, 56, 28);
         this.ComboBoxDefWeight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               int index = JBCOViewer.this.ListTransforms.getSelectedIndex();
               if (index >= 0) {
                  DefaultComboBoxModel lm = (DefaultComboBoxModel)JBCOViewer.this.ListTransforms.getModel();
                  lm.removeElementAt(index);
                  lm.insertElementAt(JBCOViewer.optionStrings[0][index] + " - " + JBCOViewer.this.ComboBoxDefWeight.getSelectedItem(), index);
               }
            }
         });
         this.ButtonAddItem = new JButton();
         this.jPanel1.add(this.ButtonAddItem);
         this.ButtonAddItem.setText("Add Item");
         this.ButtonAddItem.setBounds(91, 77, 133, 28);
         this.ButtonAddItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               String text = JBCOViewer.this.TextFieldConstraint.getText();
               if (text != null && text.trim().length() != 0) {
                  boolean regex = text.startsWith("*");
                  if (regex) {
                     try {
                        Pattern.compile(text.substring(1));
                     } catch (PatternSyntaxException var8) {
                        Toolkit.getDefaultToolkit().beep();
                        return;
                     }
                  }

                  DefaultComboBoxModel lm = (DefaultComboBoxModel)JBCOViewer.this.AvoidList.getModel();
                  int size = lm.getSize();

                  for(int i = 0; i < size; ++i) {
                     String item = (String)lm.getElementAt(i);
                     if (item != null && item.equals(text)) {
                        JBCOViewer.this.TextFieldConstraint.setText("");
                        return;
                     }
                  }

                  lm.addElement(text + " - " + JBCOViewer.this.ComboWeight.getSelectedItem());
                  JBCOViewer.models[JBCOViewer.previousSelected] = lm;
                  JBCOViewer.this.TextFieldConstraint.setText("");
                  JBCOViewer.this.ComboWeight.setSelectedIndex(0);
               } else {
                  Toolkit.getDefaultToolkit().beep();
               }
            }
         });
         this.PaneExplain = new JTextPane();
         this.PanelTransforms.add(this.PaneExplain);
         this.PaneExplain.setText("Adjust transform weights and add restrictions for specific Classes, Methods, and Fields.");
         this.PaneExplain.setBounds(7, 7, 616, 35);
         this.PaneExplain.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
         this.PaneExplain.setEditable(false);
         ListTransformsModel = new DefaultComboBoxModel();

         for(int i = 0; i < optionStrings[0].length; ++i) {
            ListTransformsModel.addElement(optionStrings[0][i] + " - 9");
         }

         this.ListTransforms = new JList();
         this.PanelTransforms.add(this.ListTransforms);
         this.ListTransforms.setModel(ListTransformsModel);
         this.ListTransforms.setBounds(7, 49, 238, 329);
         this.ListTransforms.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
               int[] selected = JBCOViewer.this.ListTransforms.getSelectedIndices();
               if (selected.length > 1) {
                  JBCOViewer.this.ListTransforms.setSelectedIndices(new int[0]);
               } else if (selected.length == 0) {
                  return;
               }

               String val = (String)JBCOViewer.this.ListTransforms.getSelectedValue();
               if (JBCOViewer.this.ListTransforms.getSelectedIndex() != JBCOViewer.previousSelected) {
                  JBCOViewer.previousSelected = JBCOViewer.this.ListTransforms.getSelectedIndex();
                  if (val.indexOf("-") > 0) {
                     String weight = val.substring(val.indexOf("-") + 1, val.length()).trim();
                     val = val.substring(0, val.indexOf("-"));

                     try {
                        int w = Integer.parseInt(weight);
                        if (w < 0 || w > 10) {
                           weight = "0";
                        }
                     } catch (NumberFormatException var6) {
                        weight = "0";
                     }

                     JBCOViewer.this.ComboBoxDefWeight.setSelectedItem(weight);
                  }

                  JBCOViewer.this.LabelTransformHeading.setText(val);
                  DefaultComboBoxModel lm = (DefaultComboBoxModel)JBCOViewer.models[JBCOViewer.previousSelected];
                  if (lm == null) {
                     lm = new DefaultComboBoxModel(new String[0]);
                  }

                  JBCOViewer.this.AvoidList.setModel(lm);
               }
            }
         });
         this.PanelExecute = new JPanel();
         this.TabbedPane.addTab("Output", (Icon)null, this.PanelExecute, (String)null);
         this.PanelExecute.setLayout((LayoutManager)null);
         this.PanelExecute.setPreferredSize(new Dimension(623, 427));
         this.ButtonSaveOutput = new JButton();
         this.PanelExecute.add(this.ButtonSaveOutput);
         this.ButtonSaveOutput.setText("Save Output To File:");
         this.ButtonSaveOutput.setBounds(7, 382, 182, 28);
         this.ButtonSaveOutput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               String file = JBCOViewer.this.TextFieldOutputFolder.getText();
               if (file.startsWith("~")) {
                  file = System.getProperty("user.home") + file.substring(1);
               }

               try {
                  File f = new File(file);
                  if (!f.getParentFile().exists() || !f.getParentFile().isDirectory()) {
                     throw new Exception("Directory does not appear to exist");
                  }

                  if (f.exists() && f.isDirectory()) {
                     throw new Exception("File points to a directory");
                  }

                  if (f.exists()) {
                     f.delete();
                  }

                  f.createNewFile();
                  RandomAccessFile rf = new RandomAccessFile(f, "rw");

                  try {
                     rf.write(JBCOViewer.this.TextAreaOutput.getText().getBytes());
                  } catch (Exception var10) {
                     throw var10;
                  } finally {
                     rf.close();
                  }
               } catch (Exception var12) {
                  new PopupDialog(JBCOViewer.this.thisRef, true, "Exception: " + var12.toString());
               }

            }
         });
         this.TextFieldOutputFolder = new JTextField();
         this.PanelExecute.add(this.TextFieldOutputFolder);
         this.TextFieldOutputFolder.setBounds(196, 382, 427, 28);
         this.TextAreaOutput = new JTextArea();
         this.TextAreaOutput.setFont(new Font("Courier 10 Pitch", 0, 10));
         this.jScrollPane1 = new JScrollPane(this.TextAreaOutput);
         this.PanelExecute.add(this.jScrollPane1);
         this.jScrollPane1.setBounds(7, 0, 616, 378);
         this.jScrollPane1.setAutoscrolls(true);
         this.setSize(640, 504);
         this.jMenuBar1 = new JMenuBar();
         this.setJMenuBar(this.jMenuBar1);
         this.jMenu3 = new JMenu();
         this.jMenuBar1.add(this.jMenu3);
         this.jMenu3.setText("File");
         this.speedMenuItem = new JMenuItem();
         this.jMenu3.add(this.speedMenuItem);
         this.speedMenuItem.setText("Use Speed-Tuned Combo");
         this.speedMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               DefaultComboBoxModel ListTransformsModel = new DefaultComboBoxModel();

               for(int i = 0; i < JBCOViewer.optionStrings[0].length; ++i) {
                  ListTransformsModel.addElement(JBCOViewer.optionStrings[0][i] + " - " + JBCOViewer.defaultWeights[0][i]);
               }

               JBCOViewer.this.ListTransforms.setModel(ListTransformsModel);
            }
         });
         this.sizeMenuItem = new JMenuItem();
         this.jMenu3.add(this.sizeMenuItem);
         this.sizeMenuItem.setText("Use Size-Tuned Combo");
         this.sizeMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               DefaultComboBoxModel ListTransformsModel = new DefaultComboBoxModel();

               for(int i = 0; i < JBCOViewer.optionStrings[0].length; ++i) {
                  ListTransformsModel.addElement(JBCOViewer.optionStrings[0][i] + " - " + JBCOViewer.defaultWeights[1][i]);
               }

               JBCOViewer.this.ListTransforms.setModel(ListTransformsModel);
            }
         });
         this.protMenuItem = new JMenuItem();
         this.jMenu3.add(this.protMenuItem);
         this.protMenuItem.setText("Use Protection-Tuned Combo");
         this.protMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               DefaultComboBoxModel ListTransformsModel = new DefaultComboBoxModel();

               for(int i = 0; i < JBCOViewer.optionStrings[0].length; ++i) {
                  ListTransformsModel.addElement(JBCOViewer.optionStrings[0][i] + " - " + JBCOViewer.defaultWeights[2][i]);
               }

               JBCOViewer.this.ListTransforms.setModel(ListTransformsModel);
            }
         });
         this.newFileMenuItem = new JMenuItem();
         this.jMenu3.add(this.newFileMenuItem);
         this.newFileMenuItem.setText("Execute");
         this.newFileMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               String main = JBCOViewer.this.TextFieldMain.getText().trim();
               if (main.length() == 0) {
                  new PopupDialog(JBCOViewer.this.thisRef, true, "No Main Class Specified");
               } else {
                  String cp = JBCOViewer.this.ClasspathTextField.getText().trim();
                  StringTokenizer cptokenizer;
                  if (cp.length() == 0) {
                     cptokenizer = new StringTokenizer(JBCOViewer.this.DefaultClassPathPane.getText());

                     for(cp = cptokenizer.nextToken(); cptokenizer.hasMoreTokens(); cp = cp + cptokenizer.nextToken()) {
                     }
                  }

                  cptokenizer = null;
                  Integer max = null;

                  Integer min;
                  try {
                     min = new Integer(JBCOViewer.this.TextFieldMinMem.getText());
                  } catch (NumberFormatException var27) {
                     min = null;
                  }

                  try {
                     max = new Integer(JBCOViewer.this.TextFieldMaxMem.getText());
                  } catch (NumberFormatException var26) {
                     max = null;
                  }

                  Vector<String> tmp = new Vector();
                  String args = JBCOViewer.this.TextFieldJVMArgs.getText();
                  StringTokenizer st = new StringTokenizer(args, ",");

                  while(st.hasMoreTokens()) {
                     tmp.add(st.nextToken());
                  }

                  boolean customclasspath = false;
                  String[] vmargs = new String[tmp.size() + (min == null ? 0 : 1) + (max == null ? 0 : 1)];

                  for(int i = 0; i < tmp.size(); ++i) {
                     vmargs[i] = (String)tmp.get(i);
                     if (vmargs[i].startsWith("-cp") || vmargs[i].startsWith("-classpath")) {
                        customclasspath = true;
                     }
                  }

                  if (min != null) {
                     vmargs[tmp.size()] = "-Xms" + min + "m";
                     if (max != null) {
                        vmargs[tmp.size() + 1] = "-Xmx" + max + "m";
                     }
                  } else if (max != null) {
                     vmargs[tmp.size()] = "-Xmx" + max + "m";
                  }

                  Vector trans = new Vector();
                  ListModel lmy = JBCOViewer.this.ListTransforms.getModel();

                  label164:
                  for(int ix = 0; ix < lmy.getSize(); ++ix) {
                     String text = (String)lmy.getElementAt(ix);

                     for(int j = 0; j < JBCOViewer.optionStrings[0].length; ++j) {
                        if (text.startsWith(JBCOViewer.optionStrings[0][j])) {
                           String weight = "9";
                           if (text.lastIndexOf("-") > 0) {
                              weight = text.substring(text.lastIndexOf("-") + 1).trim();

                              try {
                                 Integer.parseInt(weight);
                              } catch (Exception var25) {
                                 weight = "9";
                              }
                           }

                           trans.add("-t:" + weight + ":" + JBCOViewer.optionStrings[1][j]);
                           ListModel lmx = JBCOViewer.models[j];
                           if (lmx == null) {
                              break;
                           }

                           int k = 0;

                           while(true) {
                              if (k >= lmx.getSize()) {
                                 continue label164;
                              }

                              String val = (String)lmx.getElementAt(k);
                              weight = val.substring(val.lastIndexOf("-") + 1).trim();
                              val = val.substring(0, val.lastIndexOf("-") - 1);
                              trans.add("-it:" + weight + ":" + JBCOViewer.optionStrings[1][j] + ":\"" + val + "\"");
                              ++k;
                           }
                        }
                     }
                  }

                  String[] transforms = new String[trans.size()];
                  trans.copyInto(transforms);
                  trans = null;
                  int index = 0;
                  String outdir = JBCOViewer.this.TextField.getText();
                  String[] cmdarray = new String[6 + (customclasspath ? 0 : 2) + vmargs.length + transforms.length + (JBCOViewer.this.RadioSummary.isSelected() ? 1 : 0) + (JBCOViewer.this.RadioVerbose.isSelected() ? 1 : 0) + (JBCOViewer.this.DebugRadio.isSelected() ? 1 : 0) + (outdir.length() > 0 ? 2 : 0)];
                  int indexx = index + 1;
                  cmdarray[index] = "java";
                  if (!customclasspath) {
                     cmdarray[indexx++] = "-cp";
                     cmdarray[indexx++] = System.getProperty("java.class.path");
                  }

                  System.arraycopy(vmargs, 0, cmdarray, indexx, vmargs.length);
                  cmdarray[vmargs.length + indexx++] = "soot.jbco.Main";
                  cmdarray[vmargs.length + indexx++] = "-cp";
                  cmdarray[vmargs.length + indexx++] = cp;
                  if (outdir.length() > 0) {
                     cmdarray[vmargs.length + indexx++] = "-d";
                     cmdarray[vmargs.length + indexx++] = outdir;
                  }

                  cmdarray[vmargs.length + indexx++] = "-app";
                  cmdarray[vmargs.length + indexx++] = main;
                  if (JBCOViewer.this.RadioSummary.isSelected()) {
                     cmdarray[vmargs.length + indexx++] = "-jbco:silent";
                  }

                  if (JBCOViewer.this.RadioVerbose.isSelected()) {
                     cmdarray[vmargs.length + indexx++] = "-jbco:verbose";
                  }

                  if (JBCOViewer.this.DebugRadio.isSelected()) {
                     cmdarray[vmargs.length + indexx++] = "-jbco:debug";
                  }

                  System.arraycopy(transforms, 0, cmdarray, vmargs.length + indexx, transforms.length);
                  String output = "";
                  String[] var37 = cmdarray;
                  int var39 = cmdarray.length;

                  for(int var20 = 0; var20 < var39; ++var20) {
                     String element = var37[var20];
                     output = output + element + " ";
                  }

                  output = output + "\n";
                  JBCOViewer.this.TextAreaOutput.setText(output);
                  JBCOViewer.this.TabbedPane.setSelectedComponent(JBCOViewer.this.PanelExecute);

                  try {
                     JBCOViewer.this.runner = new RunnerThread(cmdarray, (JBCOViewer)JBCOViewer.this.thisRef, JBCOViewer.this.WorkingDirTextField.getText());
                     Thread t = new Thread(JBCOViewer.this.runner);
                     t.start();
                  } catch (Exception var24) {
                     JBCOViewer.this.TextAreaOutput.append("\n\n" + var24.toString());
                     synchronized(JBCOViewer.this.runner) {
                        JBCOViewer.this.runner.stopRun = true;
                     }

                     JBCOViewer.this.runner = null;
                  }

               }
            }
         });
         this.openFileMenuItem = new JMenuItem();
         this.jMenu3.add(this.openFileMenuItem);
         this.openFileMenuItem.setEnabled(false);
         this.openFileMenuItem.setText("Stop");
         this.openFileMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               if (JBCOViewer.this.runner != null) {
                  synchronized(JBCOViewer.this.runner) {
                     JBCOViewer.this.runner.stopRun = true;
                  }

                  JBCOViewer.this.runner = null;
               }

               JBCOViewer.this.openFileMenuItem.setEnabled(false);
            }
         });
         this.exitMenuItem = new JMenuItem();
         this.jMenu3.add(this.exitMenuItem);
         this.exitMenuItem.setText("Exit");
         this.exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
               System.exit(0);
            }
         });
      } catch (Exception var4) {
         logger.error((String)var4.getMessage(), (Throwable)var4);
      }

   }
}
