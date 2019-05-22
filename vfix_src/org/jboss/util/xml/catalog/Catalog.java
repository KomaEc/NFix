package org.jboss.util.xml.catalog;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.SAXParserFactory;
import org.jboss.util.xml.catalog.helpers.PublicId;
import org.jboss.util.xml.catalog.readers.CatalogReader;
import org.jboss.util.xml.catalog.readers.SAXCatalogReader;
import org.jboss.util.xml.catalog.readers.TR9401CatalogReader;

public class Catalog {
   public static final int BASE = CatalogEntry.addEntryType("BASE", 1);
   public static final int CATALOG = CatalogEntry.addEntryType("CATALOG", 1);
   public static final int DOCUMENT = CatalogEntry.addEntryType("DOCUMENT", 1);
   public static final int OVERRIDE = CatalogEntry.addEntryType("OVERRIDE", 1);
   public static final int SGMLDECL = CatalogEntry.addEntryType("SGMLDECL", 1);
   public static final int DELEGATE_PUBLIC = CatalogEntry.addEntryType("DELEGATE_PUBLIC", 2);
   public static final int DELEGATE_SYSTEM = CatalogEntry.addEntryType("DELEGATE_SYSTEM", 2);
   public static final int DELEGATE_URI = CatalogEntry.addEntryType("DELEGATE_URI", 2);
   public static final int DOCTYPE = CatalogEntry.addEntryType("DOCTYPE", 2);
   public static final int DTDDECL = CatalogEntry.addEntryType("DTDDECL", 2);
   public static final int ENTITY = CatalogEntry.addEntryType("ENTITY", 2);
   public static final int LINKTYPE = CatalogEntry.addEntryType("LINKTYPE", 2);
   public static final int NOTATION = CatalogEntry.addEntryType("NOTATION", 2);
   public static final int PUBLIC = CatalogEntry.addEntryType("PUBLIC", 2);
   public static final int SYSTEM = CatalogEntry.addEntryType("SYSTEM", 2);
   public static final int URI = CatalogEntry.addEntryType("URI", 2);
   public static final int REWRITE_SYSTEM = CatalogEntry.addEntryType("REWRITE_SYSTEM", 2);
   public static final int REWRITE_URI = CatalogEntry.addEntryType("REWRITE_URI", 2);
   protected URL base;
   protected URL catalogCwd;
   protected Vector catalogEntries = new Vector();
   protected boolean default_override = true;
   protected CatalogManager catalogManager = CatalogManager.getStaticManager();
   protected Vector catalogFiles = new Vector();
   protected Vector localCatalogFiles = new Vector();
   protected Vector catalogs = new Vector();
   protected Vector localDelegate = new Vector();
   protected Hashtable readerMap = new Hashtable();
   protected Vector readerArr = new Vector();

   public Catalog() {
   }

   public Catalog(CatalogManager manager) {
      this.catalogManager = manager;
   }

   public CatalogManager getCatalogManager() {
      return this.catalogManager;
   }

   public void setCatalogManager(CatalogManager manager) {
      this.catalogManager = manager;
   }

   public void setupReaders() {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      spf.setValidating(false);
      SAXCatalogReader saxReader = new SAXCatalogReader(spf);
      saxReader.setCatalogParser((String)null, "XMLCatalog", "org.apache.xml.resolver.readers.XCatalogReader");
      saxReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "org.apache.xml.resolver.readers.OASISXMLCatalogReader");
      this.addReader("application/xml", saxReader);
      TR9401CatalogReader textReader = new TR9401CatalogReader();
      this.addReader("text/plain", textReader);
   }

   public void addReader(String mimeType, CatalogReader reader) {
      Integer pos;
      if (this.readerMap.containsKey(mimeType)) {
         pos = (Integer)this.readerMap.get(mimeType);
         this.readerArr.set(pos, reader);
      } else {
         this.readerArr.add(reader);
         pos = new Integer(this.readerArr.size() - 1);
         this.readerMap.put(mimeType, pos);
      }

   }

   protected void copyReaders(Catalog newCatalog) {
      Vector mapArr = new Vector(this.readerMap.size());

      for(int count = 0; count < this.readerMap.size(); ++count) {
         mapArr.add((Object)null);
      }

      Enumeration enumt = this.readerMap.keys();

      while(enumt.hasMoreElements()) {
         String mimeType = (String)enumt.nextElement();
         Integer pos = (Integer)this.readerMap.get(mimeType);
         mapArr.set(pos, mimeType);
      }

      for(int count = 0; count < mapArr.size(); ++count) {
         String mimeType = (String)mapArr.get(count);
         Integer pos = (Integer)this.readerMap.get(mimeType);
         newCatalog.addReader(mimeType, (CatalogReader)this.readerArr.get(pos));
      }

   }

   protected Catalog newCatalog() {
      String catalogClass = this.getClass().getName();

      Catalog c;
      try {
         c = (Catalog)((Catalog)Class.forName(catalogClass).newInstance());
         c.setCatalogManager(this.catalogManager);
         this.copyReaders(c);
         return c;
      } catch (ClassNotFoundException var3) {
         this.catalogManager.debug.message(1, "Class Not Found Exception: " + catalogClass);
      } catch (IllegalAccessException var4) {
         this.catalogManager.debug.message(1, "Illegal Access Exception: " + catalogClass);
      } catch (InstantiationException var5) {
         this.catalogManager.debug.message(1, "Instantiation Exception: " + catalogClass);
      } catch (ClassCastException var6) {
         this.catalogManager.debug.message(1, "Class Cast Exception: " + catalogClass);
      } catch (Exception var7) {
         this.catalogManager.debug.message(1, "Other Exception: " + catalogClass);
      }

      c = new Catalog();
      c.setCatalogManager(this.catalogManager);
      this.copyReaders(c);
      return c;
   }

   public String getCurrentBase() {
      return this.base.toString();
   }

   public String getDefaultOverride() {
      return this.default_override ? "yes" : "no";
   }

   public void loadSystemCatalogs() throws MalformedURLException, IOException {
      Vector catalogs = this.catalogManager.getCatalogFiles();
      if (catalogs != null) {
         for(int count = 0; count < catalogs.size(); ++count) {
            this.catalogFiles.addElement(catalogs.elementAt(count));
         }
      }

      if (this.catalogFiles.size() > 0) {
         String catfile = (String)this.catalogFiles.lastElement();
         this.catalogFiles.removeElement(catfile);
         this.parseCatalog(catfile);
      }

   }

   public synchronized void parseCatalog(String fileName) throws MalformedURLException, IOException {
      this.default_override = this.catalogManager.getPreferPublic();
      this.catalogManager.debug.message(4, "Parse catalog: " + fileName);
      this.catalogFiles.addElement(fileName);
      this.parsePendingCatalogs();
   }

   public synchronized void parseCatalog(String mimeType, InputStream is) throws IOException, CatalogException {
      this.default_override = this.catalogManager.getPreferPublic();
      this.catalogManager.debug.message(4, "Parse " + mimeType + " catalog on input stream");
      CatalogReader reader = null;
      if (this.readerMap.containsKey(mimeType)) {
         int arrayPos = (Integer)this.readerMap.get(mimeType);
         reader = (CatalogReader)this.readerArr.get(arrayPos);
      }

      if (reader == null) {
         String msg = "No CatalogReader for MIME type: " + mimeType;
         this.catalogManager.debug.message(2, msg);
         throw new CatalogException(6, msg);
      } else {
         reader.readCatalog(this, is);
         this.parsePendingCatalogs();
      }
   }

   public synchronized void parseCatalog(URL aUrl) throws IOException {
      this.catalogCwd = aUrl;
      this.base = aUrl;
      this.default_override = this.catalogManager.getPreferPublic();
      this.catalogManager.debug.message(4, "Parse catalog: " + aUrl.toString());
      DataInputStream inStream = null;
      boolean parsed = false;

      for(int count = 0; !parsed && count < this.readerArr.size(); ++count) {
         CatalogReader reader = (CatalogReader)this.readerArr.get(count);

         try {
            inStream = new DataInputStream(aUrl.openStream());
         } catch (FileNotFoundException var9) {
            break;
         }

         try {
            reader.readCatalog(this, (InputStream)inStream);
            parsed = true;
         } catch (CatalogException var8) {
            if (var8.getExceptionType() == 7) {
               break;
            }
         }

         try {
            inStream.close();
         } catch (IOException var7) {
         }
      }

      if (parsed) {
         this.parsePendingCatalogs();
      }

   }

   protected synchronized void parsePendingCatalogs() throws MalformedURLException, IOException {
      Enumeration e;
      if (!this.localCatalogFiles.isEmpty()) {
         Vector newQueue = new Vector();
         e = this.localCatalogFiles.elements();

         while(e.hasMoreElements()) {
            newQueue.addElement(e.nextElement());
         }

         for(int curCat = 0; curCat < this.catalogFiles.size(); ++curCat) {
            String catfile = (String)this.catalogFiles.elementAt(curCat);
            newQueue.addElement(catfile);
         }

         this.catalogFiles = newQueue;
         this.localCatalogFiles.clear();
      }

      if (this.catalogFiles.isEmpty() && !this.localDelegate.isEmpty()) {
         Enumeration e = this.localDelegate.elements();

         while(e.hasMoreElements()) {
            this.catalogEntries.addElement(e.nextElement());
         }

         this.localDelegate.clear();
      }

      while(!this.catalogFiles.isEmpty()) {
         String catfile = (String)this.catalogFiles.elementAt(0);

         try {
            this.catalogFiles.remove(0);
         } catch (ArrayIndexOutOfBoundsException var6) {
         }

         if (this.catalogEntries.size() == 0 && this.catalogs.size() == 0) {
            try {
               this.parseCatalogFile(catfile);
            } catch (CatalogException var5) {
               System.out.println("FIXME: " + var5.toString());
            }
         } else {
            this.catalogs.addElement(catfile);
         }

         if (!this.localCatalogFiles.isEmpty()) {
            Vector newQueue = new Vector();
            Enumeration q = this.localCatalogFiles.elements();

            while(q.hasMoreElements()) {
               newQueue.addElement(q.nextElement());
            }

            for(int curCat = 0; curCat < this.catalogFiles.size(); ++curCat) {
               catfile = (String)this.catalogFiles.elementAt(curCat);
               newQueue.addElement(catfile);
            }

            this.catalogFiles = newQueue;
            this.localCatalogFiles.clear();
         }

         if (!this.localDelegate.isEmpty()) {
            e = this.localDelegate.elements();

            while(e.hasMoreElements()) {
               this.catalogEntries.addElement(e.nextElement());
            }

            this.localDelegate.clear();
         }
      }

      this.catalogFiles.clear();
   }

   protected synchronized void parseCatalogFile(String fileName) throws MalformedURLException, IOException, CatalogException {
      String inStream;
      try {
         inStream = this.fixSlashes(System.getProperty("user.dir"));
         this.catalogCwd = new URL("file:" + inStream + "/basename");
      } catch (MalformedURLException var11) {
         String userdir = this.fixSlashes(System.getProperty("user.dir"));
         this.catalogManager.debug.message(1, "Malformed URL on cwd", userdir);
         this.catalogCwd = null;
      }

      try {
         this.base = new URL(this.catalogCwd, this.fixSlashes(fileName));
      } catch (MalformedURLException var10) {
         try {
            this.base = new URL("file:" + this.fixSlashes(fileName));
         } catch (MalformedURLException var9) {
            this.catalogManager.debug.message(1, "Malformed URL on catalog filename", this.fixSlashes(fileName));
            this.base = null;
         }
      }

      this.catalogManager.debug.message(2, "Loading catalog", fileName);
      this.catalogManager.debug.message(4, "Default BASE", this.base.toString());
      fileName = this.base.toString();
      inStream = null;
      boolean parsed = false;
      boolean notFound = false;

      for(int count = 0; !parsed && count < this.readerArr.size(); ++count) {
         CatalogReader reader = (CatalogReader)this.readerArr.get(count);

         DataInputStream inStream;
         try {
            notFound = false;
            inStream = new DataInputStream(this.base.openStream());
         } catch (FileNotFoundException var13) {
            notFound = true;
            break;
         }

         try {
            reader.readCatalog(this, (InputStream)inStream);
            parsed = true;
         } catch (CatalogException var12) {
            if (var12.getExceptionType() == 7) {
               break;
            }
         }

         try {
            inStream.close();
         } catch (IOException var8) {
         }
      }

      if (!parsed) {
         if (notFound) {
            this.catalogManager.debug.message(3, "Catalog does not exist", fileName);
         } else {
            this.catalogManager.debug.message(1, "Failed to parse catalog", fileName);
         }
      }

   }

   public void addEntry(CatalogEntry entry) {
      int type = entry.getEntryType();
      String fsi;
      if (type == BASE) {
         fsi = entry.getEntryArg(0);
         URL newbase = null;
         this.catalogManager.debug.message(5, "BASE CUR", this.base.toString());
         this.catalogManager.debug.message(4, "BASE STR", fsi);

         try {
            fsi = this.fixSlashes(fsi);
            newbase = new URL(this.base, fsi);
         } catch (MalformedURLException var8) {
            try {
               newbase = new URL("file:" + fsi);
            } catch (MalformedURLException var7) {
               this.catalogManager.debug.message(1, "Malformed URL on base", fsi);
               newbase = null;
            }
         }

         if (newbase != null) {
            this.base = newbase;
         }

         this.catalogManager.debug.message(5, "BASE NEW", this.base.toString());
      } else if (type == CATALOG) {
         fsi = this.makeAbsolute(entry.getEntryArg(0));
         this.catalogManager.debug.message(4, "CATALOG", fsi);
         this.localCatalogFiles.addElement(fsi);
      } else {
         String fsi;
         if (type == PUBLIC) {
            fsi = PublicId.normalize(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, fsi);
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "PUBLIC", fsi, fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == SYSTEM) {
            fsi = this.normalizeURI(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "SYSTEM", fsi, fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == URI) {
            fsi = this.normalizeURI(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "URI", fsi, fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == DOCUMENT) {
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(0)));
            entry.setEntryArg(0, fsi);
            this.catalogManager.debug.message(4, "DOCUMENT", fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == OVERRIDE) {
            this.catalogManager.debug.message(4, "OVERRIDE", entry.getEntryArg(0));
            this.catalogEntries.addElement(entry);
         } else if (type == SGMLDECL) {
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(0)));
            entry.setEntryArg(0, fsi);
            this.catalogManager.debug.message(4, "SGMLDECL", fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == DELEGATE_PUBLIC) {
            fsi = PublicId.normalize(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, fsi);
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "DELEGATE_PUBLIC", fsi, fsi);
            this.addDelegate(entry);
         } else if (type == DELEGATE_SYSTEM) {
            fsi = this.normalizeURI(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, fsi);
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "DELEGATE_SYSTEM", fsi, fsi);
            this.addDelegate(entry);
         } else if (type == DELEGATE_URI) {
            fsi = this.normalizeURI(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, fsi);
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "DELEGATE_URI", fsi, fsi);
            this.addDelegate(entry);
         } else if (type == REWRITE_SYSTEM) {
            fsi = this.normalizeURI(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, fsi);
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "REWRITE_SYSTEM", fsi, fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == REWRITE_URI) {
            fsi = this.normalizeURI(entry.getEntryArg(0));
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(0, fsi);
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "REWRITE_URI", fsi, fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == DOCTYPE) {
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "DOCTYPE", entry.getEntryArg(0), fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == DTDDECL) {
            fsi = PublicId.normalize(entry.getEntryArg(0));
            entry.setEntryArg(0, fsi);
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "DTDDECL", fsi, fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == ENTITY) {
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "ENTITY", entry.getEntryArg(0), fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == LINKTYPE) {
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "LINKTYPE", entry.getEntryArg(0), fsi);
            this.catalogEntries.addElement(entry);
         } else if (type == NOTATION) {
            fsi = this.makeAbsolute(this.normalizeURI(entry.getEntryArg(1)));
            entry.setEntryArg(1, fsi);
            this.catalogManager.debug.message(4, "NOTATION", entry.getEntryArg(0), fsi);
            this.catalogEntries.addElement(entry);
         } else {
            this.catalogEntries.addElement(entry);
         }
      }

   }

   public void unknownEntry(Vector strings) {
      if (strings != null && strings.size() > 0) {
         String keyword = (String)strings.elementAt(0);
         this.catalogManager.debug.message(2, "Unrecognized token parsing catalog", keyword);
      }

   }

   public void parseAllCatalogs() throws MalformedURLException, IOException {
      for(int catPos = 0; catPos < this.catalogs.size(); ++catPos) {
         Catalog c = null;

         try {
            c = (Catalog)this.catalogs.elementAt(catPos);
         } catch (ClassCastException var5) {
            String catfile = (String)this.catalogs.elementAt(catPos);
            c = this.newCatalog();
            c.parseCatalog(catfile);
            this.catalogs.setElementAt(c, catPos);
            c.parseAllCatalogs();
         }
      }

      Enumeration enumt = this.catalogEntries.elements();

      while(true) {
         CatalogEntry e;
         do {
            if (!enumt.hasMoreElements()) {
               return;
            }

            e = (CatalogEntry)enumt.nextElement();
         } while(e.getEntryType() != DELEGATE_PUBLIC && e.getEntryType() != DELEGATE_SYSTEM && e.getEntryType() != DELEGATE_URI);

         Catalog dcat = this.newCatalog();
         dcat.parseCatalog(e.getEntryArg(1));
      }
   }

   public String resolveDoctype(String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
      String resolved = null;
      this.catalogManager.debug.message(3, "resolveDoctype(" + entityName + "," + publicId + "," + systemId + ")");
      systemId = this.normalizeURI(systemId);
      if (publicId != null && publicId.startsWith("urn:publicid:")) {
         publicId = PublicId.decodeURN(publicId);
      }

      if (systemId != null && systemId.startsWith("urn:publicid:")) {
         systemId = PublicId.decodeURN(systemId);
         if (publicId != null && !publicId.equals(systemId)) {
            this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            systemId = null;
         } else {
            publicId = systemId;
            systemId = null;
         }
      }

      if (systemId != null) {
         resolved = this.resolveLocalSystem(systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      if (publicId != null) {
         resolved = this.resolveLocalPublic(DOCTYPE, entityName, publicId, systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      boolean over = this.default_override;
      Enumeration enumt = this.catalogEntries.elements();

      CatalogEntry e;
      do {
         do {
            label47:
            do {
               while(enumt.hasMoreElements()) {
                  e = (CatalogEntry)enumt.nextElement();
                  if (e.getEntryType() != OVERRIDE) {
                     continue label47;
                  }

                  over = e.getEntryArg(0).equalsIgnoreCase("YES");
               }

               return this.resolveSubordinateCatalogs(DOCTYPE, entityName, publicId, systemId);
            } while(e.getEntryType() != DOCTYPE);
         } while(!e.getEntryArg(0).equals(entityName));
      } while(!over && systemId != null);

      return e.getEntryArg(1);
   }

   public String resolveDocument() throws MalformedURLException, IOException {
      this.catalogManager.debug.message(3, "resolveDocument");
      Enumeration enumt = this.catalogEntries.elements();

      CatalogEntry e;
      do {
         if (!enumt.hasMoreElements()) {
            return this.resolveSubordinateCatalogs(DOCUMENT, (String)null, (String)null, (String)null);
         }

         e = (CatalogEntry)enumt.nextElement();
      } while(e.getEntryType() != DOCUMENT);

      return e.getEntryArg(1);
   }

   public String resolveEntity(String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
      String resolved = null;
      this.catalogManager.debug.message(3, "resolveEntity(" + entityName + "," + publicId + "," + systemId + ")");
      systemId = this.normalizeURI(systemId);
      if (publicId != null && publicId.startsWith("urn:publicid:")) {
         publicId = PublicId.decodeURN(publicId);
      }

      if (systemId != null && systemId.startsWith("urn:publicid:")) {
         systemId = PublicId.decodeURN(systemId);
         if (publicId != null && !publicId.equals(systemId)) {
            this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            systemId = null;
         } else {
            publicId = systemId;
            systemId = null;
         }
      }

      if (systemId != null) {
         resolved = this.resolveLocalSystem(systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      if (publicId != null) {
         resolved = this.resolveLocalPublic(ENTITY, entityName, publicId, systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      boolean over = this.default_override;
      Enumeration enumt = this.catalogEntries.elements();

      CatalogEntry e;
      do {
         do {
            label47:
            do {
               while(enumt.hasMoreElements()) {
                  e = (CatalogEntry)enumt.nextElement();
                  if (e.getEntryType() != OVERRIDE) {
                     continue label47;
                  }

                  over = e.getEntryArg(0).equalsIgnoreCase("YES");
               }

               return this.resolveSubordinateCatalogs(ENTITY, entityName, publicId, systemId);
            } while(e.getEntryType() != ENTITY);
         } while(!e.getEntryArg(0).equals(entityName));
      } while(!over && systemId != null);

      return e.getEntryArg(1);
   }

   public String resolveNotation(String notationName, String publicId, String systemId) throws MalformedURLException, IOException {
      String resolved = null;
      this.catalogManager.debug.message(3, "resolveNotation(" + notationName + "," + publicId + "," + systemId + ")");
      systemId = this.normalizeURI(systemId);
      if (publicId != null && publicId.startsWith("urn:publicid:")) {
         publicId = PublicId.decodeURN(publicId);
      }

      if (systemId != null && systemId.startsWith("urn:publicid:")) {
         systemId = PublicId.decodeURN(systemId);
         if (publicId != null && !publicId.equals(systemId)) {
            this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            systemId = null;
         } else {
            publicId = systemId;
            systemId = null;
         }
      }

      if (systemId != null) {
         resolved = this.resolveLocalSystem(systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      if (publicId != null) {
         resolved = this.resolveLocalPublic(NOTATION, notationName, publicId, systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      boolean over = this.default_override;
      Enumeration enumt = this.catalogEntries.elements();

      CatalogEntry e;
      do {
         do {
            label47:
            do {
               while(enumt.hasMoreElements()) {
                  e = (CatalogEntry)enumt.nextElement();
                  if (e.getEntryType() != OVERRIDE) {
                     continue label47;
                  }

                  over = e.getEntryArg(0).equalsIgnoreCase("YES");
               }

               return this.resolveSubordinateCatalogs(NOTATION, notationName, publicId, systemId);
            } while(e.getEntryType() != NOTATION);
         } while(!e.getEntryArg(0).equals(notationName));
      } while(!over && systemId != null);

      return e.getEntryArg(1);
   }

   public String resolvePublic(String publicId, String systemId) throws MalformedURLException, IOException {
      this.catalogManager.debug.message(3, "resolvePublic(" + publicId + "," + systemId + ")");
      systemId = this.normalizeURI(systemId);
      if (publicId != null && publicId.startsWith("urn:publicid:")) {
         publicId = PublicId.decodeURN(publicId);
      }

      if (systemId != null && systemId.startsWith("urn:publicid:")) {
         systemId = PublicId.decodeURN(systemId);
         if (publicId != null && !publicId.equals(systemId)) {
            this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
            systemId = null;
         } else {
            publicId = systemId;
            systemId = null;
         }
      }

      String resolved;
      if (systemId != null) {
         resolved = this.resolveLocalSystem(systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      resolved = this.resolveLocalPublic(PUBLIC, (String)null, publicId, systemId);
      return resolved != null ? resolved : this.resolveSubordinateCatalogs(PUBLIC, (String)null, publicId, systemId);
   }

   protected synchronized String resolveLocalPublic(int entityType, String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
      publicId = PublicId.normalize(publicId);
      if (systemId != null) {
         String resolved = this.resolveLocalSystem(systemId);
         if (resolved != null) {
            return resolved;
         }
      }

      boolean over = this.default_override;
      Enumeration enumt = this.catalogEntries.elements();

      CatalogEntry e;
      do {
         do {
            label81:
            do {
               while(enumt.hasMoreElements()) {
                  e = (CatalogEntry)enumt.nextElement();
                  if (e.getEntryType() != OVERRIDE) {
                     continue label81;
                  }

                  over = e.getEntryArg(0).equalsIgnoreCase("YES");
               }

               over = this.default_override;
               enumt = this.catalogEntries.elements();
               Vector delCats = new Vector();

               while(true) {
                  String delegatedCatalog;
                  while(enumt.hasMoreElements()) {
                     CatalogEntry e = (CatalogEntry)enumt.nextElement();
                     if (e.getEntryType() == OVERRIDE) {
                        over = e.getEntryArg(0).equalsIgnoreCase("YES");
                     } else if (e.getEntryType() == DELEGATE_PUBLIC && (over || systemId == null)) {
                        delegatedCatalog = e.getEntryArg(0);
                        if (delegatedCatalog.length() <= publicId.length() && delegatedCatalog.equals(publicId.substring(0, delegatedCatalog.length()))) {
                           delCats.addElement(e.getEntryArg(1));
                        }
                     }
                  }

                  if (delCats.size() <= 0) {
                     return null;
                  }

                  Enumeration enumCats = delCats.elements();
                  if (this.catalogManager.debug.getDebug() > 1) {
                     this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");

                     while(enumCats.hasMoreElements()) {
                        delegatedCatalog = (String)enumCats.nextElement();
                        this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
                     }
                  }

                  Catalog dcat = this.newCatalog();
                  enumCats = delCats.elements();

                  while(enumCats.hasMoreElements()) {
                     String delegatedCatalog = (String)enumCats.nextElement();
                     dcat.parseCatalog(delegatedCatalog);
                  }

                  return dcat.resolvePublic(publicId, (String)null);
               }
            } while(e.getEntryType() != PUBLIC);
         } while(!e.getEntryArg(0).equals(publicId));
      } while(!over && systemId != null);

      return e.getEntryArg(1);
   }

   public String resolveSystem(String systemId) throws MalformedURLException, IOException {
      this.catalogManager.debug.message(3, "resolveSystem(" + systemId + ")");
      systemId = this.normalizeURI(systemId);
      if (systemId != null && systemId.startsWith("urn:publicid:")) {
         systemId = PublicId.decodeURN(systemId);
         return this.resolvePublic(systemId, (String)null);
      } else {
         if (systemId != null) {
            String resolved = this.resolveLocalSystem(systemId);
            if (resolved != null) {
               return resolved;
            }
         }

         return this.resolveSubordinateCatalogs(SYSTEM, (String)null, (String)null, systemId);
      }
   }

   protected String resolveLocalSystem(String systemId) throws MalformedURLException, IOException {
      String osname = System.getProperty("os.name");
      boolean windows = osname.indexOf("Windows") >= 0;
      Enumeration enumt = this.catalogEntries.elements();

      CatalogEntry e;
      do {
         do {
            if (!enumt.hasMoreElements()) {
               enumt = this.catalogEntries.elements();
               String startString = null;
               String prefix = null;

               do {
                  if (!enumt.hasMoreElements()) {
                     enumt = this.catalogEntries.elements();
                     Vector delCats = new Vector();

                     String delegatedCatalog;
                     while(enumt.hasMoreElements()) {
                        CatalogEntry e = (CatalogEntry)enumt.nextElement();
                        if (e.getEntryType() == DELEGATE_SYSTEM) {
                           delegatedCatalog = e.getEntryArg(0);
                           if (delegatedCatalog.length() <= systemId.length() && delegatedCatalog.equals(systemId.substring(0, delegatedCatalog.length()))) {
                              delCats.addElement(e.getEntryArg(1));
                           }
                        }
                     }

                     if (delCats.size() <= 0) {
                        return null;
                     }

                     Enumeration enumCats = delCats.elements();
                     if (this.catalogManager.debug.getDebug() > 1) {
                        this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");

                        while(enumCats.hasMoreElements()) {
                           delegatedCatalog = (String)enumCats.nextElement();
                           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
                        }
                     }

                     Catalog dcat = this.newCatalog();
                     enumCats = delCats.elements();

                     while(enumCats.hasMoreElements()) {
                        String delegatedCatalog = (String)enumCats.nextElement();
                        dcat.parseCatalog(delegatedCatalog);
                     }

                     return dcat.resolveSystem(systemId);
                  }

                  CatalogEntry e = (CatalogEntry)enumt.nextElement();
                  if (e.getEntryType() == REWRITE_SYSTEM) {
                     String p = e.getEntryArg(0);
                     if (p.length() <= systemId.length() && p.equals(systemId.substring(0, p.length())) && (startString == null || p.length() > startString.length())) {
                        startString = p;
                        prefix = e.getEntryArg(1);
                     }
                  }
               } while(prefix == null);

               return prefix + systemId.substring(startString.length());
            }

            e = (CatalogEntry)enumt.nextElement();
         } while(e.getEntryType() != SYSTEM);
      } while(!e.getEntryArg(0).equals(systemId) && (!windows || !e.getEntryArg(0).equalsIgnoreCase(systemId)));

      return e.getEntryArg(1);
   }

   public String resolveURI(String uri) throws MalformedURLException, IOException {
      this.catalogManager.debug.message(3, "resolveURI(" + uri + ")");
      uri = this.normalizeURI(uri);
      if (uri != null && uri.startsWith("urn:publicid:")) {
         uri = PublicId.decodeURN(uri);
         return this.resolvePublic(uri, (String)null);
      } else {
         if (uri != null) {
            String resolved = this.resolveLocalURI(uri);
            if (resolved != null) {
               return resolved;
            }
         }

         return this.resolveSubordinateCatalogs(URI, (String)null, (String)null, uri);
      }
   }

   protected String resolveLocalURI(String uri) throws MalformedURLException, IOException {
      Enumeration enumt = this.catalogEntries.elements();

      while(enumt.hasMoreElements()) {
         CatalogEntry e = (CatalogEntry)enumt.nextElement();
         if (e.getEntryType() == URI && e.getEntryArg(0).equals(uri)) {
            return e.getEntryArg(1);
         }
      }

      enumt = this.catalogEntries.elements();
      String startString = null;
      String prefix = null;

      do {
         if (!enumt.hasMoreElements()) {
            enumt = this.catalogEntries.elements();
            Vector delCats = new Vector();

            String delegatedCatalog;
            while(enumt.hasMoreElements()) {
               CatalogEntry e = (CatalogEntry)enumt.nextElement();
               if (e.getEntryType() == DELEGATE_URI) {
                  delegatedCatalog = e.getEntryArg(0);
                  if (delegatedCatalog.length() <= uri.length() && delegatedCatalog.equals(uri.substring(0, delegatedCatalog.length()))) {
                     delCats.addElement(e.getEntryArg(1));
                  }
               }
            }

            if (delCats.size() <= 0) {
               return null;
            }

            Enumeration enumCats = delCats.elements();
            if (this.catalogManager.debug.getDebug() > 1) {
               this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");

               while(enumCats.hasMoreElements()) {
                  delegatedCatalog = (String)enumCats.nextElement();
                  this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
               }
            }

            Catalog dcat = this.newCatalog();
            enumCats = delCats.elements();

            while(enumCats.hasMoreElements()) {
               String delegatedCatalog = (String)enumCats.nextElement();
               dcat.parseCatalog(delegatedCatalog);
            }

            return dcat.resolveURI(uri);
         }

         CatalogEntry e = (CatalogEntry)enumt.nextElement();
         if (e.getEntryType() == REWRITE_URI) {
            String p = e.getEntryArg(0);
            if (p.length() <= uri.length() && p.equals(uri.substring(0, p.length())) && (startString == null || p.length() > startString.length())) {
               startString = p;
               prefix = e.getEntryArg(1);
            }
         }
      } while(prefix == null);

      return prefix + uri.substring(startString.length());
   }

   protected synchronized String resolveSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId) throws MalformedURLException, IOException {
      for(int catPos = 0; catPos < this.catalogs.size(); ++catPos) {
         Catalog c = null;

         try {
            c = (Catalog)this.catalogs.elementAt(catPos);
         } catch (ClassCastException var13) {
            String catfile = (String)this.catalogs.elementAt(catPos);
            c = this.newCatalog();

            try {
               c.parseCatalog(catfile);
            } catch (MalformedURLException var10) {
               this.catalogManager.debug.message(1, "Malformed Catalog URL", catfile);
            } catch (FileNotFoundException var11) {
               this.catalogManager.debug.message(1, "Failed to load catalog, file not found", catfile);
            } catch (IOException var12) {
               this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", catfile);
            }

            this.catalogs.setElementAt(c, catPos);
         }

         String resolved = null;
         if (entityType == DOCTYPE) {
            resolved = c.resolveDoctype(entityName, publicId, systemId);
         } else if (entityType == DOCUMENT) {
            resolved = c.resolveDocument();
         } else if (entityType == ENTITY) {
            resolved = c.resolveEntity(entityName, publicId, systemId);
         } else if (entityType == NOTATION) {
            resolved = c.resolveNotation(entityName, publicId, systemId);
         } else if (entityType == PUBLIC) {
            resolved = c.resolvePublic(publicId, systemId);
         } else if (entityType == SYSTEM) {
            resolved = c.resolveSystem(systemId);
         } else if (entityType == URI) {
            resolved = c.resolveURI(systemId);
         }

         if (resolved != null) {
            return resolved;
         }
      }

      return null;
   }

   protected String fixSlashes(String sysid) {
      return sysid.replace('\\', '/');
   }

   protected String makeAbsolute(String sysid) {
      URL local = null;
      sysid = this.fixSlashes(sysid);

      try {
         local = new URL(this.base, sysid);
      } catch (MalformedURLException var4) {
         this.catalogManager.debug.message(1, "Malformed URL on system identifier", sysid);
      }

      return local != null ? local.toString() : sysid;
   }

   protected String normalizeURI(String uriref) {
      String newRef = "";
      if (uriref == null) {
         return null;
      } else {
         byte[] bytes;
         try {
            bytes = uriref.getBytes("UTF-8");
         } catch (UnsupportedEncodingException var6) {
            this.catalogManager.debug.message(1, "UTF-8 is an unsupported encoding!?");
            return uriref;
         }

         for(int count = 0; count < bytes.length; ++count) {
            int ch = bytes[count] & 255;
            if (ch > 32 && ch <= 127 && ch != 34 && ch != 60 && ch != 62 && ch != 92 && ch != 94 && ch != 96 && ch != 123 && ch != 124 && ch != 125 && ch != 127) {
               newRef = newRef + (char)bytes[count];
            } else {
               newRef = newRef + this.encodedByte(ch);
            }
         }

         return newRef;
      }
   }

   protected String encodedByte(int b) {
      String hex = Integer.toHexString(b).toUpperCase();
      return hex.length() < 2 ? "%0" + hex : "%" + hex;
   }

   protected void addDelegate(CatalogEntry entry) {
      int pos = 0;
      String partial = entry.getEntryArg(0);
      Enumeration local = this.localDelegate.elements();

      while(true) {
         if (local.hasMoreElements()) {
            CatalogEntry dpe = (CatalogEntry)local.nextElement();
            String dp = dpe.getEntryArg(0);
            if (dp.equals(partial)) {
               return;
            }

            if (dp.length() > partial.length()) {
               ++pos;
            }

            if (dp.length() >= partial.length()) {
               continue;
            }
         }

         if (this.localDelegate.size() == 0) {
            this.localDelegate.addElement(entry);
         } else {
            this.localDelegate.insertElementAt(entry, pos);
         }

         return;
      }
   }
}
