package org.apache.maven.doxia.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.codehaus.plexus.util.StringUtils;

public class IndexEntry {
   private IndexEntry parent;
   private String id;
   private String title;
   private List childEntries = new ArrayList();
   private static final String EOL = System.getProperty("line.separator");

   public IndexEntry(String newId) {
      this.id = newId;
   }

   public IndexEntry(IndexEntry newParent, String newId) {
      if (newParent == null) {
         throw new NullPointerException("parent cannot be null.");
      } else if (newId == null) {
         throw new NullPointerException("id cannot be null.");
      } else {
         this.parent = newParent;
         this.id = newId;
         this.parent.childEntries.add(this);
      }
   }

   public IndexEntry getParent() {
      return this.parent;
   }

   public String getId() {
      return this.id;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String newTitle) {
      this.title = newTitle;
   }

   public List getChildEntries() {
      return Collections.unmodifiableList(this.childEntries);
   }

   public void setChildEntries(List entries) {
      if (entries == null) {
         this.childEntries = new ArrayList();
      }

      this.childEntries = entries;
   }

   public IndexEntry getNextEntry() {
      if (this.parent == null) {
         return null;
      } else {
         List entries = this.parent.getChildEntries();
         int index = entries.indexOf(this);
         return index + 1 >= entries.size() ? null : (IndexEntry)entries.get(index + 1);
      }
   }

   public IndexEntry getPrevEntry() {
      if (this.parent == null) {
         return null;
      } else {
         List entries = this.parent.getChildEntries();
         int index = entries.indexOf(this);
         return index == 0 ? null : (IndexEntry)entries.get(index - 1);
      }
   }

   public IndexEntry getFirstEntry() {
      List entries = this.getChildEntries();
      return entries.size() == 0 ? null : (IndexEntry)entries.get(0);
   }

   public IndexEntry getLastEntry() {
      List entries = this.getChildEntries();
      return entries.size() == 0 ? null : (IndexEntry)entries.get(entries.size() - 1);
   }

   public IndexEntry getRootEntry() {
      List entries = this.getChildEntries();
      if (entries.size() == 0) {
         return null;
      } else if (entries.size() > 1) {
         throw new RuntimeException("This index has more than one root entry");
      } else {
         return (IndexEntry)entries.get(0);
      }
   }

   public String toString() {
      return this.toString(0);
   }

   public String toString(int depth) {
      StringBuffer message = new StringBuffer();
      message.append("Id: ").append(this.id);
      if (StringUtils.isNotEmpty(this.title)) {
         message.append(", title: ").append(this.title);
      }

      message.append(EOL);
      String indent = "";

      for(int i = 0; i < depth; ++i) {
         indent = indent + " ";
      }

      Iterator it = this.getChildEntries().iterator();

      while(it.hasNext()) {
         IndexEntry entry = (IndexEntry)it.next();
         message.append(indent).append(entry.toString(depth + 1));
      }

      return message.toString();
   }
}
