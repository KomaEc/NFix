package soot.toolkits.astmetrics.DataHandlingApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import soot.CompilationDeathException;

public class ProcessData {
   private static final int CLASSNAMESIZE = 15;
   private static final int CLASS = 0;
   private static final int BENCHMARK = 1;
   private static String metricListFileName = null;
   private static final ArrayList<String> xmlFileList = new ArrayList();
   private static int aggregationMechanism = -1;
   private static OutputStream streamOut;
   private static PrintWriter bench;
   private static final boolean CSV = true;
   private static final boolean decompiler = false;

   public static void main(String[] args) {
      int argLength = args.length;
      if (argLength == 0) {
         printIntro();
         useHelp();
         System.exit(1);
      }

      if (args[0].equals("--help")) {
         printHelp();
         System.exit(1);
      } else if (args[0].equals("-metricList")) {
         metricListFileName(args);
         System.out.println("A list of metrics will be stored in: " + metricListFileName);
         readXMLFileNames(2, args);

         try {
            OutputStream streamOut = new FileOutputStream(metricListFileName);
            PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
            writeMetricLists(writerOut);
            writerOut.flush();
            streamOut.close();
         } catch (IOException var4) {
            throw new CompilationDeathException("Cannot output file " + metricListFileName);
         }
      } else if (args[0].equals("-tables")) {
         metricListFileName(args);
         System.out.println("Will read column table headings from: " + metricListFileName);
         aggregationOption(args);
         if (aggregationMechanism == 1) {
            System.out.println("Aggregating over benchmarks...each row is one of the xml files");
            System.out.println("Only one tex file with the name" + metricListFileName + ".tex will be created");
         } else if (aggregationMechanism == 0) {
            System.out.println("Aggregating over class...each row is one class...");
            System.out.println("Each benchmark (xml file) will have its own tex file");
         }

         readXMLFileNames(3, args);
         generateMetricsTables();
      } else {
         System.out.println("Incorrect argument number 1: expecting -metricList or -tables");
         System.exit(1);
      }

   }

   private static void aggregationOption(String[] args) {
      if (args.length < 3) {
         System.out.println("Expecting -class or -benchmark at argument number 3");
         System.exit(1);
      }

      if (args[2].equals("-class")) {
         aggregationMechanism = 0;
      } else if (args[2].equals("-benchmark")) {
         aggregationMechanism = 1;
      } else {
         System.out.println("Expecting -class or -benchmark at argument number 3");
         System.exit(1);
      }

   }

   private static void readXMLFileNames(int startIndex, String[] args) {
      if (args.length < startIndex + 1) {
         System.out.println("Expecting an xml file OR * symbol as argument number" + (startIndex + 1));
         System.exit(1);
      }

      if (args[startIndex].equals("*")) {
         System.out.println("Will read all xml files from directory");
         readStar();
      } else {
         for(int i = startIndex; i < args.length; ++i) {
            String temp = args[i];
            if (temp.endsWith(".xml")) {
               xmlFileList.add(temp);
            }
         }
      }

      Iterator it = xmlFileList.iterator();

      while(it.hasNext()) {
         System.out.println("Will be reading: " + (String)it.next());
      }

   }

   private static void metricListFileName(String[] args) {
      if (args.length < 2) {
         System.out.println("Expecting name of metricList as argumnet number 2");
         System.exit(1);
      }

      metricListFileName = args[1];
   }

   public static void printHelp() {
      printIntro();
      System.out.println("There are two main modes of execution");
      System.out.println("To execute the program the first argument should be one of these modes");
      System.out.println("-metricList and -tables");
      System.out.println("\n\n The -metricList mode");
      System.out.println("The argument at location 1 should be name of a file where the list of metrics will be stored");
      System.out.println("All arguments following argument 1 have to be xml files to be processed");
      System.out.println("If argument at location 2 is * then the current directory is searched and all xml files will be processed");
      System.out.println("\n\n The -tables mode");
      System.out.println("The argument at location 1 should be name of a file where the list of metrics are stored");
      System.out.println("These metrics will become the COLUMNS in the tables created");
      System.out.println("Argument at location 2 is the choice of aggregation");
      System.out.println("\t -class for class level metrics");
      System.out.println("\t -benchmark for benchmark level metrics");
      System.out.println("Each xml file is considered to be a benchmark with a bunch of classes in it");
      System.out.println("All arguments following argument 2 have to be xml files to be processed");
      System.out.println("If argument at location 3 is * then the current directory is searched and all xml files will be processed");
   }

   public static void printIntro() {
      System.out.println("Welcome to the processData application");
      System.out.println("The application is an xml document parser.");
      System.out.println("Its primary aim is to create pretty tex tables");
   }

   public static void useHelp() {
      System.out.println("Use the --help flag for more details");
   }

   private static void readStar() {
      String curDir = System.getProperty("user.dir");
      System.out.println("Current system directory is" + curDir);
      File dir = new File(curDir);
      String[] children = dir.list();
      if (children != null) {
         FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
               return name.endsWith(".xml");
            }
         };
         children = dir.list(filter);
         if (children != null) {
            String[] var4 = children;
            int var5 = children.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String element = var4[var6];
               xmlFileList.add(element);
            }
         }
      }

   }

   private static void writeMetricLists(PrintWriter out) {
      ArrayList<String> metricList = new ArrayList();
      Iterator it = xmlFileList.iterator();

      while(it.hasNext()) {
         String fileName = (String)it.next();

         try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(fileName));
            System.out.println("Retrieving Metric List from xml file: " + fileName);
            doc.getDocumentElement().normalize();
            NodeList metrics = doc.getElementsByTagName("Metric");

            for(int s = 0; s < metrics.getLength(); ++s) {
               Node metricNode = metrics.item(s);
               if (metricNode.getNodeType() == 1) {
                  Element metricElement = (Element)metricNode;
                  NodeList metricName = metricElement.getElementsByTagName("MetricName");
                  Element name = (Element)metricName.item(0);
                  NodeList textFNList = name.getChildNodes();
                  if (!metricList.contains(textFNList.item(0).getNodeValue().trim())) {
                     metricList.add(textFNList.item(0).getNodeValue().trim());
                  }
               }
            }
         } catch (SAXParseException var14) {
            System.out.println("** Parsing error, line " + var14.getLineNumber() + ", uri " + var14.getSystemId());
            System.out.println(" " + var14.getMessage());
         } catch (SAXException var15) {
            Exception x = var15.getException();
            ((Exception)(x == null ? var15 : x)).printStackTrace();
         } catch (Throwable var16) {
            var16.printStackTrace();
         }
      }

      it = metricList.iterator();

      while(it.hasNext()) {
         out.println((String)it.next());
      }

      System.out.println(metricListFileName + " created.");
   }

   private static void generateMetricsTables() {
      Vector columns = new Vector();

      try {
         FileReader file = new FileReader(metricListFileName);
         BufferedReader fileInput = new BufferedReader(file);

         String text;
         while((text = fileInput.readLine()) != null) {
            columns.add(text);
         }

         fileInput.close();
      } catch (Exception var42) {
         System.out.println("Exception while reading from metricList" + metricListFileName);
         System.exit(1);
      }

      Vector allMetrics = new Vector();

      String fileName;
      try {
         FileReader file = new FileReader("myList");
         BufferedReader fileInput = new BufferedReader(file);

         while((fileName = fileInput.readLine()) != null) {
            allMetrics.add(fileName);
         }

         fileInput.close();
      } catch (Exception var41) {
         System.out.println("Exception while reading from metricList" + metricListFileName);
         System.exit(1);
      }

      String newClassName = "";
      if (aggregationMechanism != 1) {
         Iterator it = xmlFileList.iterator();

         while(it.hasNext()) {
            fileName = (String)it.next();

            try {
               DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
               Document doc = docBuilder.parse(new File(fileName));
               System.out.println("Gethering metric info from from xml file: " + fileName);
               doc.getDocumentElement().normalize();
               if (aggregationMechanism == 0) {
                  getClassMetrics(fileName, doc, columns);
               } else {
                  System.out.println("Unknown aggregation Mechanism");
                  System.exit(1);
               }
            } catch (SAXParseException var35) {
               System.out.println("** Parsing error, line " + var35.getLineNumber() + ", uri " + var35.getSystemId());
               System.out.println(" " + var35.getMessage());
            } catch (SAXException var36) {
               Exception x = var36.getException();
               ((Exception)(x == null ? var36 : x)).printStackTrace();
            } catch (Throwable var37) {
               var37.printStackTrace();
            }
         }

      } else {
         newClassName = metricListFileName;
         newClassName = newClassName + ".csv";
         System.out.println("Creating csv file" + newClassName + " from metrics info");
         bench = openWriteFile(newClassName);
         Map<String, List<String>> benchMarkToFiles = new HashMap();
         Iterator it = xmlFileList.iterator();

         while(true) {
            String benchmark;
            List tempValue;
            ArrayList files;
            if (it.hasNext()) {
               String fileName = (String)it.next();
               if (fileName.indexOf(45) < 0) {
                  System.out.println("XML files should have following syntax:\n <BENCHMARKNAME>-<PROPERTY>.xml\n PROPERTY should be enabled disabled etc");
                  return;
               }

               benchmark = fileName.substring(0, fileName.indexOf(45));
               tempValue = (List)benchMarkToFiles.get(benchmark);
               files = null;
               Object tempList;
               if (tempValue == null) {
                  tempList = new ArrayList();
               } else {
                  tempList = tempValue;
               }

               ((List)tempList).add(fileName);
               benchMarkToFiles.put(benchmark, tempList);
               if (fileName.indexOf(45) >= 0 && fileName.lastIndexOf(".xml") >= 0) {
                  String xmlfileColumnType = fileName.substring(fileName.lastIndexOf(45) + 1, fileName.lastIndexOf(".xml"));
                  System.out.println("XML FILE COLUMN TYPE" + xmlfileColumnType);
                  if (!xmlfileColumnType.equals("Jad") && !xmlfileColumnType.equals("original") && !xmlfileColumnType.equals("SourceAgain") && !xmlfileColumnType.equals("disabled") && !xmlfileColumnType.equals("enabled")) {
                     throw new RuntimeException("XML FILE <property> not recognized");
                  }
                  continue;
               }

               System.out.println("XML files should have following syntax:\n <BENCHMARKNAME>-<PROPERTY>.xml\n PROPERTY should be enabled disabled etc");
               return;
            }

            printCSVHeader(bench);
            Iterator keys = benchMarkToFiles.keySet().iterator();

            while(true) {
               do {
                  if (!keys.hasNext()) {
                     closeWriteFile(bench, newClassName);
                     return;
                  }

                  benchmark = (String)keys.next();
                  tempValue = (List)benchMarkToFiles.get(benchmark);
               } while(tempValue == null);

               if (tempValue.size() != 5) {
                  throw new RuntimeException("not all xml files available for this benchmark!!");
               }

               System.out.println("old order" + tempValue.toString());
               String[] newFileOrder = new String[tempValue.size()];
               Iterator tempIt = tempValue.iterator();

               String fileName;
               while(tempIt.hasNext()) {
                  fileName = (String)tempIt.next();
                  if (fileName.indexOf("original") > -1) {
                     newFileOrder[0] = fileName;
                  } else if (fileName.indexOf("jbco-enabled") > -1) {
                     newFileOrder[1] = fileName;
                  } else if (fileName.indexOf("jbco-disabled") > -1) {
                     newFileOrder[2] = fileName;
                  } else if (fileName.indexOf("klassmaster-enabled") > -1) {
                     newFileOrder[3] = fileName;
                  } else {
                     if (fileName.indexOf("klassmaster-disabled") <= -1) {
                        throw new RuntimeException("property xml not correct");
                     }

                     newFileOrder[4] = fileName;
                  }
               }

               files = new ArrayList();
               files.add(newFileOrder[0]);
               files.add(newFileOrder[1]);
               files.add(newFileOrder[2]);
               files.add(newFileOrder[3]);
               files.add(newFileOrder[4]);
               System.out.println("new order" + files.toString());
               Iterator<String> fileIt = files.iterator();
               int count = -1;

               while(fileIt.hasNext()) {
                  fileName = (String)fileIt.next();
                  ++count;

                  try {
                     DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                     DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                     Document doc = docBuilder.parse(new File(fileName));
                     System.out.println("Gethering metric info from from xml file: " + fileName);
                     HashMap<String, Number> aggregatedValues = new HashMap();
                     Iterator tempIt = allMetrics.iterator();

                     while(tempIt.hasNext()) {
                        aggregatedValues.put(tempIt.next(), new Integer(0));
                     }

                     aggregateXMLFileMetrics(doc, aggregatedValues);
                     Object myTemp = aggregatedValues.get("Total-Conditionals");
                     if (myTemp == null) {
                        System.out.println("Total-Conditionals not found in aggregatedValues");
                        System.exit(1);
                     }

                     double total_if_ifelse = ((Integer)myTemp).doubleValue();
                     myTemp = aggregatedValues.get("Total Loops");
                     if (myTemp == null) {
                        System.out.println("Total Loops not found in aggregatedValues");
                        System.exit(1);
                     }

                     double totalLoops = ((Integer)myTemp).doubleValue();
                     double totalConditional = total_if_ifelse + totalLoops;
                     myTemp = aggregatedValues.get("AST-Node-Count");
                     if (myTemp == null) {
                        System.out.println("AST-Node-Count not found in aggregatedValues");
                        System.exit(1);
                     }

                     double astCount = ((Integer)myTemp).doubleValue();
                     myTemp = aggregatedValues.get("NameCount");
                     if (myTemp == null) {
                        System.out.println("NameCount not found in aggregatedValues");
                        System.exit(1);
                     }

                     double nameCount = (Double)myTemp;
                     myTemp = aggregatedValues.get("Expr-Count");
                     if (myTemp == null) {
                        System.out.println("ExprCount not found in aggregatedValues");
                        System.exit(1);
                     }

                     double exprCount = (Double)myTemp;
                     tempIt = columns.iterator();

                     while(tempIt.hasNext()) {
                        String nexttempit = (String)tempIt.next();
                        Object temp = aggregatedValues.get(nexttempit);
                        if (temp instanceof Integer) {
                           int val = (Integer)temp;
                           switch(count) {
                           case 0:
                              bench.print(fileName.substring(0, fileName.indexOf(45)));
                           case 1:
                           case 2:
                           case 3:
                           case 4:
                              if (nexttempit.equals("Total-Abrupt")) {
                                 bench.print("," + val);
                              } else if (nexttempit.equals("Total-Cond-Complexity")) {
                                 if (totalConditional != 0.0D) {
                                    System.out.println("conditional complexit is" + val);
                                    System.out.println("totalConditionals are" + totalConditional);
                                    bench.print("," + (double)val / totalConditional);
                                 } else if (val == 0) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but totalConditionals are zero!!!");
                                    System.exit(1);
                                 }
                              } else if (nexttempit.equals("D-W-Complexity")) {
                                 if (astCount != 0.0D) {
                                    bench.print("," + (double)val / astCount);
                                 } else if (val == 0) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but astcount is zero!!!");
                                    System.exit(1);
                                 }
                              } else if (nexttempit.equals("Expr-Complexity")) {
                                 if (exprCount != 0.0D) {
                                    bench.print("," + (double)val / exprCount);
                                 } else if (val == 0) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but exprcount is zero!!!");
                                    System.exit(1);
                                 }
                              } else if (nexttempit.equals("Name-Complexity")) {
                                 if (nameCount != 0.0D) {
                                    bench.print("," + (double)val / nameCount);
                                 } else if (val == 0) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but name-count is zero!!!");
                                    System.exit(1);
                                 }
                              } else {
                                 bench.print("," + val);
                              }
                              break;
                           default:
                              System.out.println("unhandled count value");
                              System.exit(1);
                           }
                        } else {
                           if (!(temp instanceof Double)) {
                              throw new RuntimeException("Unknown type of object stored!!!");
                           }

                           double val = (Double)temp;
                           switch(count) {
                           case 0:
                              bench.print(fileName.substring(0, fileName.indexOf(45)));
                           case 1:
                           case 2:
                           case 3:
                           case 4:
                              if (nexttempit.equals("Total-Abrupt")) {
                                 bench.print("," + val);
                              } else if (nexttempit.equals("Total-Cond-Complexity")) {
                                 if (totalConditional != 0.0D) {
                                    System.out.println("conditional complexit is" + val);
                                    System.out.println("totalConditionals are" + totalConditional);
                                    bench.print("," + val / totalConditional);
                                 } else if (val == 0.0D) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but totalConditionals are zero!!!");
                                    System.exit(1);
                                 }
                              } else if (nexttempit.equals("D-W-Complexity")) {
                                 if (astCount != 0.0D) {
                                    bench.print("," + val / astCount);
                                 } else if (val == 0.0D) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but astcount is zero!!!");
                                    System.exit(1);
                                 }
                              } else if (nexttempit.equals("Expr-Complexity")) {
                                 if (exprCount != 0.0D) {
                                    bench.print("," + val / exprCount);
                                 } else if (val == 0.0D) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but exprcount is zero!!!");
                                    System.exit(1);
                                 }
                              } else if (nexttempit.equals("Name-Complexity")) {
                                 if (nameCount != 0.0D) {
                                    bench.print("," + val / nameCount);
                                 } else if (val == 0.0D) {
                                    bench.print("," + val);
                                 } else {
                                    System.out.println("Val not 0 but name-count is zero!!!");
                                    System.exit(1);
                                 }
                              } else {
                                 bench.print("," + val);
                              }
                              break;
                           default:
                              System.out.println("unhandled count value");
                              System.exit(1);
                           }
                        }

                        if (tempIt.hasNext()) {
                           System.out.println("Only allowed one metric for CSV");
                           System.exit(1);
                        }
                     }
                  } catch (SAXParseException var38) {
                     System.out.println("** Parsing error, line " + var38.getLineNumber() + ", uri " + var38.getSystemId());
                     System.out.println(" " + var38.getMessage());
                  } catch (SAXException var39) {
                     Exception x = var39.getException();
                     ((Exception)(x == null ? var39 : x)).printStackTrace();
                  } catch (Throwable var40) {
                     var40.printStackTrace();
                  }
               }

               bench.println("");
            }
         }
      }
   }

   private static PrintWriter openWriteFile(String fileName) {
      try {
         streamOut = new FileOutputStream(fileName);
         PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
         return writerOut;
      } catch (IOException var3) {
         throw new CompilationDeathException("Cannot output file " + fileName);
      }
   }

   private static void closeWriteFile(PrintWriter writerOut, String fileName) {
      try {
         writerOut.flush();
         streamOut.close();
      } catch (IOException var3) {
         throw new CompilationDeathException("Cannot output file " + fileName);
      }
   }

   private static int aggregateXMLFileMetrics(Document doc, HashMap<String, Number> aggregated) {
      NodeList classes = doc.getElementsByTagName("Class");
      int numClasses = classes.getLength();
      System.out.println("NumClasses for this document are" + numClasses);
      NodeList metrics = doc.getElementsByTagName("Metric");

      for(int s = 0; s < metrics.getLength(); ++s) {
         Node metricNode = metrics.item(s);
         if (metricNode.getNodeType() == 1) {
            Element metricElement = (Element)metricNode;
            NodeList metricName = metricElement.getElementsByTagName("MetricName");
            Element name = (Element)metricName.item(0);
            NodeList textFNList = name.getChildNodes();
            String tempName = textFNList.item(0).getNodeValue().trim();
            Object tempObj = aggregated.get(tempName);
            if (tempObj != null) {
               NodeList value = metricElement.getElementsByTagName("Value");
               Element name1 = (Element)value.item(0);
               NodeList textFNList1 = name1.getChildNodes();
               String valToPrint = textFNList1.item(0).getNodeValue().trim();
               boolean notInt = false;

               try {
                  int temp = Integer.parseInt(valToPrint);
                  if (tempObj instanceof Integer) {
                     Integer valSoFar = (Integer)tempObj;
                     aggregated.put(tempName, new Integer(valSoFar + temp));
                  } else {
                     if (!(tempObj instanceof Double)) {
                        throw new RuntimeException("\n\nobject type not found");
                     }

                     Double valSoFar = (Double)tempObj;
                     aggregated.put(tempName, new Double(valSoFar + (double)temp));
                  }
               } catch (Exception var22) {
                  notInt = true;
               }

               if (notInt) {
                  try {
                     double temp = Double.parseDouble(valToPrint);
                     if (tempObj instanceof Integer) {
                        Integer valSoFar = (Integer)tempObj;
                        aggregated.put(tempName, new Double((double)valSoFar + temp));
                     } else {
                        if (!(tempObj instanceof Double)) {
                           throw new RuntimeException("\n\nobject type not found");
                        }

                        Double valSoFar = (Double)tempObj;
                        aggregated.put(tempName, new Double(valSoFar + temp));
                     }
                  } catch (Exception var21) {
                     throw new RuntimeException("\n\n not an integer not a double unhandled!!!!");
                  }
               }
            }
         }
      }

      return numClasses;
   }

   private static void getClassMetrics(String fileName, Document doc, Vector<String> columns) {
      String newClassName = fileName;
      if (fileName.endsWith(".xml")) {
         newClassName = fileName.substring(0, fileName.length() - 4);
      }

      newClassName = newClassName + ".tex";
      System.out.println("Creating tex file" + newClassName + " from metrics info in file" + fileName);
      PrintWriter writerOut = openWriteFile(newClassName);
      printTexTableHeader(writerOut, "Classes", columns);
      ArrayList<String> classNames = new ArrayList();
      HashMap<String, String> classData = new HashMap();
      NodeList classes = doc.getElementsByTagName("Class");

      for(int cl = 0; cl < classes.getLength(); ++cl) {
         Node classNode = classes.item(cl);
         if (classNode.getNodeType() == 1) {
            Element classElement = (Element)classNode;
            NodeList classNameNodeList = classElement.getElementsByTagName("ClassName");
            Element classNameElement = (Element)classNameNodeList.item(0);
            NodeList classNameTextFNList = classNameElement.getChildNodes();
            String className = classNameTextFNList.item(0).getNodeValue().trim();
            className = className.replace('_', '-');
            if (className.length() > 15) {
               className = className.substring(0, 15);
               classNames.add(className);
            } else {
               classNames.add(className);
            }

            System.out.print("\nclassName " + className);
            String data = "   ";
            NodeList metrics = classElement.getElementsByTagName("Metric");
            int columnIndex = 0;

            for(int s = 0; s < metrics.getLength() && columnIndex < columns.size(); ++s) {
               Node metricNode = metrics.item(s);
               if (metricNode.getNodeType() == 1) {
                  Element metricElement = (Element)metricNode;
                  NodeList metricName = metricElement.getElementsByTagName("MetricName");
                  Element name = (Element)metricName.item(0);
                  NodeList textFNList = name.getChildNodes();
                  String tempName = textFNList.item(0).getNodeValue().trim();
                  if (tempName.equals(columns.elementAt(columnIndex))) {
                     NodeList value = metricElement.getElementsByTagName("Value");
                     Element name1 = (Element)value.item(0);
                     NodeList textFNList1 = name1.getChildNodes();
                     String valToPrint = textFNList1.item(0).getNodeValue().trim();
                     System.out.print(" " + valToPrint);
                     data = data + "&" + valToPrint;
                     ++columnIndex;
                     if (columns.size() > columnIndex) {
                        data = data + "   ";
                     } else {
                        data = data + "\\\\";
                     }
                  }
               }
            }

            classData.put(className, data);
         }
      }

      Collections.sort(classNames);
      Iterator tempIt = classNames.iterator();

      while(tempIt.hasNext()) {
         String className = (String)tempIt.next();
         String data = (String)classData.get(className);
         writerOut.print(className);
         writerOut.println(data);
      }

      printTexTableFooter(writerOut, fileName);
      closeWriteFile(writerOut, metricListFileName);
   }

   private static void printTexTableFooter(PrintWriter out, String tableCaption) {
      out.println("");
      out.println("\\hline");
      out.println("\\end{tabular}");
      out.println("\\caption{ ..." + tableCaption + "..... }");
      out.println("\\end{table}");
   }

   private static void printCSVHeader(PrintWriter out) {
      out.println(",Original,JBCO-enabled,JBCO-disabled,klassmaster-enabled,klassmaster-disabled");
   }

   private static void printTexTableHeader(PrintWriter out, String rowHeading, Vector<String> columns) {
      out.println("\\begin{table}[hbtp]");
      out.print("\\begin{tabular}{");

      for(int i = 0; i <= columns.size(); ++i) {
         out.print("|l");
      }

      out.println("|}");
      out.println("\\hline");
      out.print(rowHeading + "   ");
      Iterator it = columns.iterator();

      while(it.hasNext()) {
         out.print("&" + (String)it.next());
         if (it.hasNext()) {
            out.print("   ");
         }
      }

      out.println("\\\\");
      out.println("\\hline");
   }
}
