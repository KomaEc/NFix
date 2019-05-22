package soot.tagkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AbstractHost implements Host {
   protected int line;
   protected int col;
   protected List<Tag> mTagList = null;

   public List<Tag> getTags() {
      return this.mTagList == null ? Collections.emptyList() : this.mTagList;
   }

   public void removeTag(String aName) {
      int tagIndex;
      if ((tagIndex = this.searchForTag(aName)) != -1) {
         this.mTagList.remove(tagIndex);
      }

   }

   private int searchForTag(String aName) {
      if (this.mTagList == null) {
         return -1;
      } else {
         int i = 0;

         for(Iterator it = this.mTagList.iterator(); it.hasNext(); ++i) {
            Tag tag = (Tag)it.next();
            if (tag != null && tag.getName().equals(aName)) {
               return i;
            }
         }

         return -1;
      }
   }

   public Tag getTag(String aName) {
      int tagIndex;
      return (tagIndex = this.searchForTag(aName)) != -1 ? (Tag)this.mTagList.get(tagIndex) : null;
   }

   public boolean hasTag(String aName) {
      return this.searchForTag(aName) != -1;
   }

   public void addTag(Tag t) {
      if (this.mTagList == null) {
         this.mTagList = new ArrayList(1);
      }

      this.mTagList.add(t);
   }

   public void removeAllTags() {
      this.mTagList = null;
   }

   public void addAllTagsOf(Host h) {
      List<Tag> tags = h.getTags();
      if (!tags.isEmpty()) {
         if (this.mTagList == null) {
            this.mTagList = new ArrayList(tags.size());
         }

         this.mTagList.addAll(tags);
      }
   }

   public int getJavaSourceStartLineNumber() {
      if (this.line <= 0) {
         SourceLnPosTag tag = (SourceLnPosTag)this.getTag("SourceLnPosTag");
         if (tag != null) {
            this.line = tag.startLn();
         } else {
            LineNumberTag tag2 = (LineNumberTag)this.getTag("LineNumberTag");
            if (tag2 != null) {
               this.line = tag2.getLineNumber();
            } else {
               this.line = -1;
            }
         }
      }

      return this.line;
   }

   public int getJavaSourceStartColumnNumber() {
      if (this.col <= 0) {
         SourceLnPosTag tag = (SourceLnPosTag)this.getTag("SourceLnPosTag");
         if (tag != null) {
            this.col = tag.startPos();
         } else {
            this.col = -1;
         }
      }

      return this.col;
   }
}
