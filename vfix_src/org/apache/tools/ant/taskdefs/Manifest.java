package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class Manifest {
   public static final String ATTRIBUTE_MANIFEST_VERSION = "Manifest-Version";
   public static final String ATTRIBUTE_SIGNATURE_VERSION = "Signature-Version";
   public static final String ATTRIBUTE_NAME = "Name";
   public static final String ATTRIBUTE_FROM = "From";
   public static final String ATTRIBUTE_CLASSPATH = "Class-Path";
   public static final String DEFAULT_MANIFEST_VERSION = "1.0";
   public static final int MAX_LINE_LENGTH = 72;
   public static final int MAX_SECTION_LENGTH = 70;
   public static final String EOL = "\r\n";
   public static final String ERROR_FROM_FORBIDDEN = "Manifest attributes should not start with \"From\" in \"";
   public static final String JAR_ENCODING = "UTF-8";
   private String manifestVersion = "1.0";
   private Manifest.Section mainSection = new Manifest.Section();
   private Hashtable sections = new Hashtable();
   private Vector sectionIndex = new Vector();
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$taskdefs$Manifest;

   public static Manifest getDefaultManifest() throws BuildException {
      InputStream in = null;
      InputStreamReader insr = null;

      Manifest var4;
      try {
         String defManifest = "/org/apache/tools/ant/defaultManifest.mf";
         in = (class$org$apache$tools$ant$taskdefs$Manifest == null ? (class$org$apache$tools$ant$taskdefs$Manifest = class$("org.apache.tools.ant.taskdefs.Manifest")) : class$org$apache$tools$ant$taskdefs$Manifest).getResourceAsStream(defManifest);
         if (in == null) {
            throw new BuildException("Could not find default manifest: " + defManifest);
         }

         try {
            insr = new InputStreamReader(in, "UTF-8");
            Manifest defaultManifest = new Manifest(insr);
            Manifest.Attribute createdBy = new Manifest.Attribute("Created-By", System.getProperty("java.vm.version") + " (" + System.getProperty("java.vm.vendor") + ")");
            defaultManifest.getMainSection().storeAttribute(createdBy);
            Manifest var5 = defaultManifest;
            return var5;
         } catch (UnsupportedEncodingException var11) {
            insr = new InputStreamReader(in);
            var4 = new Manifest(insr);
         }
      } catch (ManifestException var12) {
         throw new BuildException("Default manifest is invalid !!", var12);
      } catch (IOException var13) {
         throw new BuildException("Unable to read default manifest", var13);
      } finally {
         FileUtils.close((Reader)insr);
         FileUtils.close(in);
      }

      return var4;
   }

   public Manifest() {
      this.manifestVersion = null;
   }

   public Manifest(Reader r) throws ManifestException, IOException {
      BufferedReader reader = new BufferedReader(r);
      String nextSectionName = this.mainSection.read(reader);
      String readManifestVersion = this.mainSection.getAttributeValue("Manifest-Version");
      if (readManifestVersion != null) {
         this.manifestVersion = readManifestVersion;
         this.mainSection.removeAttribute("Manifest-Version");
      }

      String line = null;

      while((line = reader.readLine()) != null) {
         if (line.length() != 0) {
            Manifest.Section section = new Manifest.Section();
            Manifest.Attribute sectionName;
            if (nextSectionName == null) {
               sectionName = new Manifest.Attribute(line);
               if (!sectionName.getName().equalsIgnoreCase("Name")) {
                  throw new ManifestException("Manifest sections should start with a \"Name\" attribute and not \"" + sectionName.getName() + "\"");
               }

               nextSectionName = sectionName.getValue();
            } else {
               sectionName = new Manifest.Attribute(line);
               section.addAttributeAndCheck(sectionName);
            }

            section.setName(nextSectionName);
            nextSectionName = section.read(reader);
            this.addConfiguredSection(section);
         }
      }

   }

   public void addConfiguredSection(Manifest.Section section) throws ManifestException {
      String sectionName = section.getName();
      if (sectionName == null) {
         throw new BuildException("Sections must have a name");
      } else {
         this.sections.put(sectionName, section);
         if (!this.sectionIndex.contains(sectionName)) {
            this.sectionIndex.addElement(sectionName);
         }

      }
   }

   public void addConfiguredAttribute(Manifest.Attribute attribute) throws ManifestException {
      if (attribute.getKey() != null && attribute.getValue() != null) {
         if (attribute.getKey().equalsIgnoreCase("Manifest-Version")) {
            this.manifestVersion = attribute.getValue();
         } else {
            this.mainSection.addConfiguredAttribute(attribute);
         }

      } else {
         throw new BuildException("Attributes must have name and value");
      }
   }

   public void merge(Manifest other) throws ManifestException {
      this.merge(other, false);
   }

   public void merge(Manifest other, boolean overwriteMain) throws ManifestException {
      if (other != null) {
         if (overwriteMain) {
            this.mainSection = (Manifest.Section)other.mainSection.clone();
         } else {
            this.mainSection.merge(other.mainSection);
         }

         if (other.manifestVersion != null) {
            this.manifestVersion = other.manifestVersion;
         }

         Enumeration e = other.getSectionNames();

         while(e.hasMoreElements()) {
            String sectionName = (String)e.nextElement();
            Manifest.Section ourSection = (Manifest.Section)this.sections.get(sectionName);
            Manifest.Section otherSection = (Manifest.Section)other.sections.get(sectionName);
            if (ourSection == null) {
               if (otherSection != null) {
                  this.addConfiguredSection((Manifest.Section)otherSection.clone());
               }
            } else {
               ourSection.merge(otherSection);
            }
         }
      }

   }

   public void write(PrintWriter writer) throws IOException {
      writer.print("Manifest-Version: " + this.manifestVersion + "\r\n");
      String signatureVersion = this.mainSection.getAttributeValue("Signature-Version");
      if (signatureVersion != null) {
         writer.print("Signature-Version: " + signatureVersion + "\r\n");
         this.mainSection.removeAttribute("Signature-Version");
      }

      this.mainSection.write(writer);
      if (signatureVersion != null) {
         try {
            Manifest.Attribute svAttr = new Manifest.Attribute("Signature-Version", signatureVersion);
            this.mainSection.addConfiguredAttribute(svAttr);
         } catch (ManifestException var6) {
         }
      }

      Enumeration e = this.sectionIndex.elements();

      while(e.hasMoreElements()) {
         String sectionName = (String)e.nextElement();
         Manifest.Section section = this.getSection(sectionName);
         section.write(writer);
      }

   }

   public String toString() {
      StringWriter sw = new StringWriter();

      try {
         this.write(new PrintWriter(sw));
      } catch (IOException var3) {
         return null;
      }

      return sw.toString();
   }

   public Enumeration getWarnings() {
      Vector warnings = new Vector();
      Enumeration warnEnum = this.mainSection.getWarnings();

      while(warnEnum.hasMoreElements()) {
         warnings.addElement(warnEnum.nextElement());
      }

      Enumeration e = this.sections.elements();

      while(e.hasMoreElements()) {
         Manifest.Section section = (Manifest.Section)e.nextElement();
         Enumeration e2 = section.getWarnings();

         while(e2.hasMoreElements()) {
            warnings.addElement(e2.nextElement());
         }
      }

      return warnings.elements();
   }

   public int hashCode() {
      int hashCode = 0;
      if (this.manifestVersion != null) {
         hashCode += this.manifestVersion.hashCode();
      }

      hashCode += this.mainSection.hashCode();
      hashCode += this.sections.hashCode();
      return hashCode;
   }

   public boolean equals(Object rhs) {
      if (rhs != null && rhs.getClass() == this.getClass()) {
         if (rhs == this) {
            return true;
         } else {
            Manifest rhsManifest = (Manifest)rhs;
            if (this.manifestVersion == null) {
               if (rhsManifest.manifestVersion != null) {
                  return false;
               }
            } else if (!this.manifestVersion.equals(rhsManifest.manifestVersion)) {
               return false;
            }

            return !this.mainSection.equals(rhsManifest.mainSection) ? false : this.sections.equals(rhsManifest.sections);
         }
      } else {
         return false;
      }
   }

   public String getManifestVersion() {
      return this.manifestVersion;
   }

   public Manifest.Section getMainSection() {
      return this.mainSection;
   }

   public Manifest.Section getSection(String name) {
      return (Manifest.Section)this.sections.get(name);
   }

   public Enumeration getSectionNames() {
      return this.sectionIndex.elements();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class Section {
      private Vector warnings = new Vector();
      private String name = null;
      private Hashtable attributes = new Hashtable();
      private Vector attributeIndex = new Vector();

      public void setName(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public String read(BufferedReader reader) throws ManifestException, IOException {
         Manifest.Attribute attribute = null;

         while(true) {
            String line = reader.readLine();
            if (line == null || line.length() == 0) {
               return null;
            }

            if (line.charAt(0) == ' ') {
               if (attribute == null) {
                  if (this.name == null) {
                     throw new ManifestException("Can't start an attribute with a continuation line " + line);
                  }

                  this.name = this.name + line.substring(1);
               } else {
                  attribute.addContinuation(line);
               }
            } else {
               attribute = new Manifest.Attribute(line);
               String nameReadAhead = this.addAttributeAndCheck(attribute);
               attribute = this.getAttribute(attribute.getKey());
               if (nameReadAhead != null) {
                  return nameReadAhead;
               }
            }
         }
      }

      public void merge(Manifest.Section section) throws ManifestException {
         if (this.name == null && section.getName() != null || this.name != null && !this.name.equalsIgnoreCase(section.getName())) {
            throw new ManifestException("Unable to merge sections with different names");
         } else {
            Enumeration e = section.getAttributeKeys();
            Manifest.Attribute classpathAttribute = null;

            while(true) {
               while(e.hasMoreElements()) {
                  String attributeName = (String)e.nextElement();
                  Manifest.Attribute attribute = section.getAttribute(attributeName);
                  if (attributeName.equalsIgnoreCase("Class-Path")) {
                     if (classpathAttribute == null) {
                        classpathAttribute = new Manifest.Attribute();
                        classpathAttribute.setName("Class-Path");
                     }

                     Enumeration cpe = attribute.getValues();

                     while(cpe.hasMoreElements()) {
                        String value = (String)cpe.nextElement();
                        classpathAttribute.addValue(value);
                     }
                  } else {
                     this.storeAttribute(attribute);
                  }
               }

               if (classpathAttribute != null) {
                  this.storeAttribute(classpathAttribute);
               }

               Enumeration warnEnum = section.warnings.elements();

               while(warnEnum.hasMoreElements()) {
                  this.warnings.addElement(warnEnum.nextElement());
               }

               return;
            }
         }
      }

      public void write(PrintWriter writer) throws IOException {
         if (this.name != null) {
            Manifest.Attribute nameAttr = new Manifest.Attribute("Name", this.name);
            nameAttr.write(writer);
         }

         Enumeration e = this.getAttributeKeys();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            Manifest.Attribute attribute = this.getAttribute(key);
            attribute.write(writer);
         }

         writer.print("\r\n");
      }

      public Manifest.Attribute getAttribute(String attributeName) {
         return (Manifest.Attribute)this.attributes.get(attributeName.toLowerCase());
      }

      public Enumeration getAttributeKeys() {
         return this.attributeIndex.elements();
      }

      public String getAttributeValue(String attributeName) {
         Manifest.Attribute attribute = this.getAttribute(attributeName.toLowerCase());
         return attribute == null ? null : attribute.getValue();
      }

      public void removeAttribute(String attributeName) {
         String key = attributeName.toLowerCase();
         this.attributes.remove(key);
         this.attributeIndex.removeElement(key);
      }

      public void addConfiguredAttribute(Manifest.Attribute attribute) throws ManifestException {
         String check = this.addAttributeAndCheck(attribute);
         if (check != null) {
            throw new BuildException("Specify the section name using the \"name\" attribute of the <section> element rather than using a \"Name\" manifest attribute");
         }
      }

      public String addAttributeAndCheck(Manifest.Attribute attribute) throws ManifestException {
         if (attribute.getName() != null && attribute.getValue() != null) {
            if (attribute.getKey().equalsIgnoreCase("Name")) {
               this.warnings.addElement("\"Name\" attributes should not occur in the main section and must be the first element in all other sections: \"" + attribute.getName() + ": " + attribute.getValue() + "\"");
               return attribute.getValue();
            } else {
               if (attribute.getKey().startsWith("From".toLowerCase())) {
                  this.warnings.addElement("Manifest attributes should not start with \"From\" in \"" + attribute.getName() + ": " + attribute.getValue() + "\"");
               } else {
                  String attributeKey = attribute.getKey();
                  if (attributeKey.equalsIgnoreCase("Class-Path")) {
                     Manifest.Attribute classpathAttribute = (Manifest.Attribute)this.attributes.get(attributeKey);
                     if (classpathAttribute == null) {
                        this.storeAttribute(attribute);
                     } else {
                        this.warnings.addElement("Multiple Class-Path attributes are supported but violate the Jar specification and may not be correctly processed in all environments");
                        Enumeration e = attribute.getValues();

                        while(e.hasMoreElements()) {
                           String value = (String)e.nextElement();
                           classpathAttribute.addValue(value);
                        }
                     }
                  } else {
                     if (this.attributes.containsKey(attributeKey)) {
                        throw new ManifestException("The attribute \"" + attribute.getName() + "\" may not occur more " + "than once in the same section");
                     }

                     this.storeAttribute(attribute);
                  }
               }

               return null;
            }
         } else {
            throw new BuildException("Attributes must have name and value");
         }
      }

      public Object clone() {
         Manifest.Section cloned = new Manifest.Section();
         cloned.setName(this.name);
         Enumeration e = this.getAttributeKeys();

         while(e.hasMoreElements()) {
            String key = (String)e.nextElement();
            Manifest.Attribute attribute = this.getAttribute(key);
            cloned.storeAttribute(new Manifest.Attribute(attribute.getName(), attribute.getValue()));
         }

         return cloned;
      }

      private void storeAttribute(Manifest.Attribute attribute) {
         if (attribute != null) {
            String attributeKey = attribute.getKey();
            this.attributes.put(attributeKey, attribute);
            if (!this.attributeIndex.contains(attributeKey)) {
               this.attributeIndex.addElement(attributeKey);
            }

         }
      }

      public Enumeration getWarnings() {
         return this.warnings.elements();
      }

      public int hashCode() {
         return this.attributes.hashCode();
      }

      public boolean equals(Object rhs) {
         if (rhs != null && rhs.getClass() == this.getClass()) {
            if (rhs == this) {
               return true;
            } else {
               Manifest.Section rhsSection = (Manifest.Section)rhs;
               return this.attributes.equals(rhsSection.attributes);
            }
         } else {
            return false;
         }
      }
   }

   public static class Attribute {
      private static final int MAX_NAME_VALUE_LENGTH = 68;
      private static final int MAX_NAME_LENGTH = 70;
      private String name = null;
      private Vector values = new Vector();
      private int currentIndex = 0;

      public Attribute() {
      }

      public Attribute(String line) throws ManifestException {
         this.parse(line);
      }

      public Attribute(String name, String value) {
         this.name = name;
         this.setValue(value);
      }

      public int hashCode() {
         int hashCode = 0;
         if (this.name != null) {
            hashCode += this.getKey().hashCode();
         }

         hashCode += this.values.hashCode();
         return hashCode;
      }

      public boolean equals(Object rhs) {
         if (rhs != null && rhs.getClass() == this.getClass()) {
            if (rhs == this) {
               return true;
            } else {
               Manifest.Attribute rhsAttribute = (Manifest.Attribute)rhs;
               String lhsKey = this.getKey();
               String rhsKey = rhsAttribute.getKey();
               return (lhsKey != null || rhsKey == null) && (lhsKey == null || rhsKey != null) && lhsKey.equals(rhsKey) ? this.values.equals(rhsAttribute.values) : false;
            }
         } else {
            return false;
         }
      }

      public void parse(String line) throws ManifestException {
         int index = line.indexOf(": ");
         if (index == -1) {
            throw new ManifestException("Manifest line \"" + line + "\" is not valid as it does not " + "contain a name and a value separated by ': ' ");
         } else {
            this.name = line.substring(0, index);
            this.setValue(line.substring(index + 2));
         }
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getName() {
         return this.name;
      }

      public String getKey() {
         return this.name == null ? null : this.name.toLowerCase();
      }

      public void setValue(String value) {
         if (this.currentIndex >= this.values.size()) {
            this.values.addElement(value);
            this.currentIndex = this.values.size() - 1;
         } else {
            this.values.setElementAt(value, this.currentIndex);
         }

      }

      public String getValue() {
         if (this.values.size() == 0) {
            return null;
         } else {
            String fullValue = "";

            String value;
            for(Enumeration e = this.getValues(); e.hasMoreElements(); fullValue = fullValue + value + " ") {
               value = (String)e.nextElement();
            }

            return fullValue.trim();
         }
      }

      public void addValue(String value) {
         ++this.currentIndex;
         this.setValue(value);
      }

      public Enumeration getValues() {
         return this.values.elements();
      }

      public void addContinuation(String line) {
         String currentValue = (String)this.values.elementAt(this.currentIndex);
         this.setValue(currentValue + line.substring(1));
      }

      public void write(PrintWriter writer) throws IOException {
         Enumeration e = this.getValues();

         while(e.hasMoreElements()) {
            this.writeValue(writer, (String)e.nextElement());
         }

      }

      private void writeValue(PrintWriter writer, String value) throws IOException {
         String line = null;
         int nameLength = this.name.getBytes("UTF-8").length;
         if (nameLength > 68) {
            if (nameLength > 70) {
               throw new IOException("Unable to write manifest line " + this.name + ": " + value);
            }

            writer.print(this.name + ": " + "\r\n");
            line = " " + value;
         } else {
            line = this.name + ": " + value;
         }

         while(line.getBytes("UTF-8").length > 70) {
            int breakIndex = 70;
            if (breakIndex >= line.length()) {
               breakIndex = line.length() - 1;
            }

            String section;
            for(section = line.substring(0, breakIndex); section.getBytes("UTF-8").length > 70 && breakIndex > 0; section = line.substring(0, breakIndex)) {
               --breakIndex;
            }

            if (breakIndex == 0) {
               throw new IOException("Unable to write manifest line " + this.name + ": " + value);
            }

            writer.print(section + "\r\n");
            line = " " + line.substring(breakIndex);
         }

         writer.print(line + "\r\n");
      }
   }
}
