package org.apache.tools.ant.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;

public class FilterSet extends DataType implements Cloneable {
   public static final String DEFAULT_TOKEN_START = "@";
   public static final String DEFAULT_TOKEN_END = "@";
   private String startOfToken = "@";
   private String endOfToken = "@";
   private Vector passedTokens;
   private boolean duplicateToken = false;
   private boolean recurse = true;
   private Hashtable filterHash = null;
   private Vector filtersFiles = new Vector();
   private FilterSet.OnMissing onMissingFiltersFile;
   private boolean readingFiles;
   private int recurseDepth;
   private Vector filters;
   // $FF: synthetic field
   static Class class$org$apache$tools$ant$types$FilterSet;

   public FilterSet() {
      this.onMissingFiltersFile = FilterSet.OnMissing.FAIL;
      this.readingFiles = false;
      this.recurseDepth = 0;
      this.filters = new Vector();
   }

   protected FilterSet(FilterSet filterset) {
      this.onMissingFiltersFile = FilterSet.OnMissing.FAIL;
      this.readingFiles = false;
      this.recurseDepth = 0;
      this.filters = new Vector();
      this.filters = (Vector)filterset.getFilters().clone();
   }

   protected synchronized Vector getFilters() {
      if (this.isReference()) {
         return this.getRef().getFilters();
      } else {
         if (!this.readingFiles) {
            this.readingFiles = true;
            int i = 0;

            for(int sz = this.filtersFiles.size(); i < sz; ++i) {
               this.readFiltersFromFile((File)this.filtersFiles.get(i));
            }

            this.filtersFiles.clear();
            this.readingFiles = false;
         }

         return this.filters;
      }
   }

   protected FilterSet getRef() {
      return (FilterSet)this.getCheckedRef(class$org$apache$tools$ant$types$FilterSet == null ? (class$org$apache$tools$ant$types$FilterSet = class$("org.apache.tools.ant.types.FilterSet")) : class$org$apache$tools$ant$types$FilterSet, "filterset");
   }

   public synchronized Hashtable getFilterHash() {
      if (this.filterHash == null) {
         this.filterHash = new Hashtable(this.getFilters().size());
         Enumeration e = this.getFilters().elements();

         while(e.hasMoreElements()) {
            FilterSet.Filter filter = (FilterSet.Filter)e.nextElement();
            this.filterHash.put(filter.getToken(), filter.getValue());
         }
      }

      return this.filterHash;
   }

   public void setFiltersfile(File filtersFile) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         this.filtersFiles.add(filtersFile);
      }
   }

   public void setBeginToken(String startOfToken) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else if (startOfToken != null && !"".equals(startOfToken)) {
         this.startOfToken = startOfToken;
      } else {
         throw new BuildException("beginToken must not be empty");
      }
   }

   public String getBeginToken() {
      return this.isReference() ? this.getRef().getBeginToken() : this.startOfToken;
   }

   public void setEndToken(String endOfToken) {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else if (endOfToken != null && !"".equals(endOfToken)) {
         this.endOfToken = endOfToken;
      } else {
         throw new BuildException("endToken must not be empty");
      }
   }

   public String getEndToken() {
      return this.isReference() ? this.getRef().getEndToken() : this.endOfToken;
   }

   public void setRecurse(boolean recurse) {
      this.recurse = recurse;
   }

   public boolean isRecurse() {
      return this.recurse;
   }

   public synchronized void readFiltersFromFile(File filtersFile) throws BuildException {
      if (this.isReference()) {
         throw this.tooManyAttributes();
      } else {
         if (!filtersFile.exists()) {
            this.handleMissingFile("Could not read filters from file " + filtersFile + " as it doesn't exist.");
         }

         if (filtersFile.isFile()) {
            this.log("Reading filters from " + filtersFile, 3);
            FileInputStream in = null;

            try {
               Properties props = new Properties();
               in = new FileInputStream(filtersFile);
               props.load(in);
               Enumeration e = props.propertyNames();
               Vector filts = this.getFilters();

               while(e.hasMoreElements()) {
                  String strPropName = (String)e.nextElement();
                  String strValue = props.getProperty(strPropName);
                  filts.addElement(new FilterSet.Filter(strPropName, strValue));
               }
            } catch (Exception var11) {
               throw new BuildException("Could not read filters from file: " + filtersFile);
            } finally {
               FileUtils.close((InputStream)in);
            }
         } else {
            this.handleMissingFile("Must specify a file rather than a directory in the filtersfile attribute:" + filtersFile);
         }

         this.filterHash = null;
      }
   }

   public synchronized String replaceTokens(String line) {
      return this.iReplaceTokens(line);
   }

   public synchronized void addFilter(FilterSet.Filter filter) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.filters.addElement(filter);
         this.filterHash = null;
      }
   }

   public FilterSet.FiltersFile createFiltersfile() {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         return new FilterSet.FiltersFile();
      }
   }

   public synchronized void addFilter(String token, String value) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         this.addFilter(new FilterSet.Filter(token, value));
      }
   }

   public synchronized void addConfiguredFilterSet(FilterSet filterSet) {
      if (this.isReference()) {
         throw this.noChildrenAllowed();
      } else {
         Enumeration e = filterSet.getFilters().elements();

         while(e.hasMoreElements()) {
            this.addFilter((FilterSet.Filter)e.nextElement());
         }

      }
   }

   public synchronized boolean hasFilters() {
      return this.getFilters().size() > 0;
   }

   public synchronized Object clone() throws BuildException {
      if (this.isReference()) {
         return this.getRef().clone();
      } else {
         try {
            FilterSet fs = (FilterSet)super.clone();
            fs.filters = (Vector)this.getFilters().clone();
            fs.setProject(this.getProject());
            return fs;
         } catch (CloneNotSupportedException var2) {
            throw new BuildException(var2);
         }
      }
   }

   public void setOnMissingFiltersFile(FilterSet.OnMissing onMissingFiltersFile) {
      this.onMissingFiltersFile = onMissingFiltersFile;
   }

   public FilterSet.OnMissing getOnMissingFiltersFile() {
      return this.onMissingFiltersFile;
   }

   private synchronized String iReplaceTokens(String line) {
      String beginToken = this.getBeginToken();
      String endToken = this.getEndToken();
      int index = line.indexOf(beginToken);
      if (index > -1) {
         Hashtable tokens = this.getFilterHash();

         try {
            StringBuffer b = new StringBuffer();
            int i = 0;
            String token = null;

            for(String value = null; index > -1; index = line.indexOf(beginToken, i)) {
               int endIndex = line.indexOf(endToken, index + beginToken.length() + 1);
               if (endIndex == -1) {
                  break;
               }

               token = line.substring(index + beginToken.length(), endIndex);
               b.append(line.substring(i, index));
               if (tokens.containsKey(token)) {
                  value = (String)tokens.get(token);
                  if (this.recurse && !value.equals(token)) {
                     value = this.replaceTokens(value, token);
                  }

                  this.log("Replacing: " + beginToken + token + endToken + " -> " + value, 3);
                  b.append(value);
                  i = index + beginToken.length() + token.length() + endToken.length();
               } else {
                  b.append(beginToken);
                  i = index + beginToken.length();
               }
            }

            b.append(line.substring(i));
            return b.toString();
         } catch (StringIndexOutOfBoundsException var11) {
            return line;
         }
      } else {
         return line;
      }
   }

   private synchronized String replaceTokens(String line, String parent) throws BuildException {
      String beginToken = this.getBeginToken();
      String endToken = this.getEndToken();
      if (this.recurseDepth == 0) {
         this.passedTokens = new Vector();
      }

      ++this.recurseDepth;
      if (this.passedTokens.contains(parent) && !this.duplicateToken) {
         this.duplicateToken = true;
         System.out.println("Infinite loop in tokens. Currently known tokens : " + this.passedTokens.toString() + "\nProblem token : " + beginToken + parent + endToken + " called from " + beginToken + this.passedTokens.lastElement().toString() + endToken);
         --this.recurseDepth;
         return parent;
      } else {
         this.passedTokens.addElement(parent);
         String value = this.iReplaceTokens(line);
         if (value.indexOf(beginToken) == -1 && !this.duplicateToken && this.recurseDepth == 1) {
            this.passedTokens = null;
         } else if (this.duplicateToken && this.passedTokens.size() > 0) {
            value = (String)this.passedTokens.remove(this.passedTokens.size() - 1);
            if (this.passedTokens.size() == 0) {
               value = beginToken + value + endToken;
               this.duplicateToken = false;
            }
         }

         --this.recurseDepth;
         return value;
      }
   }

   private void handleMissingFile(String message) {
      switch(this.onMissingFiltersFile.getIndex()) {
      case 0:
         throw new BuildException(message);
      case 1:
         this.log(message, 1);
         return;
      case 2:
         return;
      default:
         throw new BuildException("Invalid value for onMissingFiltersFile");
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public static class OnMissing extends EnumeratedAttribute {
      private static final String[] VALUES = new String[]{"fail", "warn", "ignore"};
      public static final FilterSet.OnMissing FAIL = new FilterSet.OnMissing("fail");
      public static final FilterSet.OnMissing WARN = new FilterSet.OnMissing("warn");
      public static final FilterSet.OnMissing IGNORE = new FilterSet.OnMissing("ignore");
      private static final int FAIL_INDEX = 0;
      private static final int WARN_INDEX = 1;
      private static final int IGNORE_INDEX = 2;

      public OnMissing() {
      }

      public OnMissing(String value) {
         this.setValue(value);
      }

      public String[] getValues() {
         return VALUES;
      }
   }

   public class FiltersFile {
      public void setFile(File file) {
         FilterSet.this.filtersFiles.add(file);
      }
   }

   public static class Filter {
      String token;
      String value;

      public Filter(String token, String value) {
         this.setToken(token);
         this.setValue(value);
      }

      public Filter() {
      }

      public void setToken(String token) {
         this.token = token;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public String getToken() {
         return this.token;
      }

      public String getValue() {
         return this.value;
      }
   }
}
