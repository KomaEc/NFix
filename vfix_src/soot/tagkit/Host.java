package soot.tagkit;

import java.util.List;

public interface Host {
   List<Tag> getTags();

   Tag getTag(String var1);

   void addTag(Tag var1);

   void removeTag(String var1);

   boolean hasTag(String var1);

   void removeAllTags();

   void addAllTagsOf(Host var1);

   int getJavaSourceStartLineNumber();

   int getJavaSourceStartColumnNumber();
}
