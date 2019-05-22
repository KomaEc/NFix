package org.testng;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.testng.collections.Sets;
import org.testng.internal.Yaml;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.xml.sax.SAXException;

public class Converter {
   @Parameter(
      description = "file1 [file2 file3...]",
      required = true
   )
   private List<String> m_files;
   @Parameter(
      names = {"-d"},
      description = "The directory where the file(s) will be created"
   )
   private String m_outputDirectory = ".";

   public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
      Converter c = new Converter();
      c.run(args);
   }

   private void findAllSuites(Collection<XmlSuite> suites, Set<XmlSuite> result) {
      Iterator i$ = suites.iterator();

      while(i$.hasNext()) {
         XmlSuite s = (XmlSuite)i$.next();
         result.add(s);
         Iterator i$ = s.getChildSuites().iterator();

         while(i$.hasNext()) {
            XmlSuite xs = (XmlSuite)i$.next();
            this.findAllSuites(Arrays.asList(xs), result);
         }
      }

   }

   private void run(String[] args) throws ParserConfigurationException, SAXException, IOException {
      JCommander jc = new JCommander(this);

      try {
         jc.parse(args);
         File f = new File(this.m_outputDirectory);
         if (!f.exists()) {
            f.mkdir();
         }

         Iterator i$ = this.m_files.iterator();

         while(i$.hasNext()) {
            String file = (String)i$.next();
            Set<XmlSuite> allSuites = Sets.newHashSet();
            Parser parser = new Parser(file);
            parser.setLoadClasses(false);
            this.findAllSuites(parser.parse(), allSuites);
            Iterator i$ = allSuites.iterator();

            while(i$.hasNext()) {
               XmlSuite suite = (XmlSuite)i$.next();
               String fileName = suite.getFileName();
               int ind = fileName.lastIndexOf(".");
               String bn = fileName.substring(0, ind);
               int ind2 = bn.lastIndexOf(File.separatorChar);
               String baseName = bn.substring(ind2 + 1);
               File newFile;
               if (file.endsWith(".xml")) {
                  newFile = new File(this.m_outputDirectory, baseName + ".yaml");
                  this.writeFile(newFile, Yaml.toYaml(suite).toString());
               } else {
                  if (!file.endsWith(".yaml")) {
                     throw new TestNGException("Unknown file type:" + file);
                  }

                  newFile = new File(this.m_outputDirectory, baseName + ".xml");
                  this.writeFile(newFile, suite.toXml());
               }
            }
         }
      } catch (ParameterException var16) {
         System.out.println("Error: " + var16.getMessage());
         jc.usage();
      }

   }

   private void writeFile(File newFile, String content) throws IOException {
      FileWriter bw = new FileWriter(newFile);
      bw.write(content);
      bw.close();
      System.out.println("Wrote " + newFile);
   }
}
