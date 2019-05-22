package org.apache.maven.surefire.shade.org.apache.maven.shared.utils.xml;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

public class Xpp3Dom implements Iterable<Xpp3Dom> {
   private static final long serialVersionUID = 2567894443061173996L;
   private String name;
   private String value;
   private Map<String, String> attributes;
   private final List<Xpp3Dom> childList;
   private final Map<String, Xpp3Dom> childMap;
   private Xpp3Dom parent;
   public static final String CHILDREN_COMBINATION_MODE_ATTRIBUTE = "combine.children";
   private static final String CHILDREN_COMBINATION_MERGE = "merge";
   public static final String CHILDREN_COMBINATION_APPEND = "append";
   private static final String DEFAULT_CHILDREN_COMBINATION_MODE = "merge";
   public static final String SELF_COMBINATION_MODE_ATTRIBUTE = "combine.self";
   public static final String SELF_COMBINATION_OVERRIDE = "override";
   public static final String SELF_COMBINATION_MERGE = "merge";
   private static final String DEFAULT_SELF_COMBINATION_MODE = "merge";
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final Xpp3Dom[] EMPTY_DOM_ARRAY = new Xpp3Dom[0];

   public Xpp3Dom(String name) {
      this.name = name;
      this.childList = new ArrayList();
      this.childMap = new HashMap();
   }

   public Xpp3Dom(Xpp3Dom source) {
      this(source, source.getName());
   }

   public Xpp3Dom(@Nonnull Xpp3Dom src, String name) {
      this.name = name;
      int size = src.getChildCount();
      this.childList = new ArrayList(size);
      this.childMap = new HashMap();
      this.setValue(src.getValue());
      String[] arr$ = src.getAttributeNames();
      int len$ = arr$.length;

      int i$;
      for(i$ = 0; i$ < len$; ++i$) {
         String attributeName = arr$[i$];
         this.setAttribute(attributeName, src.getAttribute(attributeName));
      }

      Xpp3Dom[] arr$ = src.getChildren();
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         Xpp3Dom xpp3Dom = arr$[i$];
         this.addChild(new Xpp3Dom(xpp3Dom));
      }

   }

   public String getName() {
      return this.name;
   }

   @Nonnull
   public String getValue() {
      return this.value;
   }

   public void setValue(@Nonnull String value) {
      this.value = value;
   }

   public String[] getAttributeNames() {
      boolean isNothing = this.attributes == null || this.attributes.isEmpty();
      return isNothing ? EMPTY_STRING_ARRAY : (String[])this.attributes.keySet().toArray(new String[this.attributes.size()]);
   }

   public String getAttribute(String name) {
      return this.attributes != null ? (String)this.attributes.get(name) : null;
   }

   public void setAttribute(@Nonnull String name, @Nonnull String value) {
      if (value == null) {
         throw new NullPointerException("value can not be null");
      } else if (name == null) {
         throw new NullPointerException("name can not be null");
      } else {
         if (this.attributes == null) {
            this.attributes = new HashMap();
         }

         this.attributes.put(name, value);
      }
   }

   public Xpp3Dom getChild(int i) {
      return (Xpp3Dom)this.childList.get(i);
   }

   public Xpp3Dom getChild(String name) {
      return (Xpp3Dom)this.childMap.get(name);
   }

   public void addChild(Xpp3Dom child) {
      child.setParent(this);
      this.childList.add(child);
      this.childMap.put(child.getName(), child);
   }

   public Xpp3Dom[] getChildren() {
      boolean isNothing = this.childList == null || this.childList.isEmpty();
      return isNothing ? EMPTY_DOM_ARRAY : (Xpp3Dom[])this.childList.toArray(new Xpp3Dom[this.childList.size()]);
   }

   private List<Xpp3Dom> getChildrenList() {
      boolean isNothing = this.childList == null || this.childList.isEmpty();
      return isNothing ? Collections.emptyList() : this.childList;
   }

   public Xpp3Dom[] getChildren(String name) {
      List<Xpp3Dom> children = this.getChildrenList(name);
      return (Xpp3Dom[])children.toArray(new Xpp3Dom[children.size()]);
   }

   private List<Xpp3Dom> getChildrenList(String name) {
      if (this.childList == null) {
         return Collections.emptyList();
      } else {
         ArrayList<Xpp3Dom> children = new ArrayList();
         Iterator i$ = this.childList.iterator();

         while(i$.hasNext()) {
            Xpp3Dom aChildList = (Xpp3Dom)i$.next();
            if (name.equals(aChildList.getName())) {
               children.add(aChildList);
            }
         }

         return children;
      }
   }

   public int getChildCount() {
      return this.childList == null ? 0 : this.childList.size();
   }

   public void removeChild(int i) {
      Xpp3Dom child = (Xpp3Dom)this.childList.remove(i);
      this.childMap.values().remove(child);
      child.setParent((Xpp3Dom)null);
   }

   public Xpp3Dom getParent() {
      return this.parent;
   }

   public void setParent(Xpp3Dom parent) {
      this.parent = parent;
   }

   private static Xpp3Dom merge(Xpp3Dom dominant, Xpp3Dom recessive, Boolean childMergeOverride) {
      if (recessive != null && !isCombineSelfOverride(dominant)) {
         if (isEmpty(dominant.getValue())) {
            dominant.setValue(recessive.getValue());
         }

         String[] arr$ = recessive.getAttributeNames();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String attr = arr$[i$];
            if (isEmpty(dominant.getAttribute(attr))) {
               dominant.setAttribute(attr, recessive.getAttribute(attr));
            }
         }

         if (recessive.getChildCount() > 0) {
            boolean mergeChildren = isMergeChildren(dominant, childMergeOverride);
            Xpp3Dom dominantChild;
            Iterator i$;
            Xpp3Dom recessiveChild;
            if (mergeChildren) {
               Map<String, Iterator<Xpp3Dom>> commonChildren = getCommonChildren(dominant, recessive);
               i$ = recessive.iterator();

               while(i$.hasNext()) {
                  recessiveChild = (Xpp3Dom)i$.next();
                  Iterator<Xpp3Dom> it = (Iterator)commonChildren.get(recessiveChild.getName());
                  if (it == null) {
                     dominant.addChild(new Xpp3Dom(recessiveChild));
                  } else if (it.hasNext()) {
                     dominantChild = (Xpp3Dom)it.next();
                     merge(dominantChild, recessiveChild, childMergeOverride);
                  }
               }
            } else {
               Xpp3Dom[] dominantChildren = dominant.getChildren();
               dominant.childList.clear();
               i$ = recessive.iterator();

               while(i$.hasNext()) {
                  recessiveChild = (Xpp3Dom)i$.next();
                  dominant.addChild(new Xpp3Dom(recessiveChild));
               }

               Xpp3Dom[] arr$ = dominantChildren;
               int len$ = dominantChildren.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  dominantChild = arr$[i$];
                  dominant.addChild(dominantChild);
               }
            }
         }

         return dominant;
      } else {
         return dominant;
      }
   }

   private static Map<String, Iterator<Xpp3Dom>> getCommonChildren(Xpp3Dom dominant, Xpp3Dom recessive) {
      Map<String, Iterator<Xpp3Dom>> commonChildren = new HashMap();
      Iterator i$ = recessive.childMap.keySet().iterator();

      while(i$.hasNext()) {
         String childName = (String)i$.next();
         List<Xpp3Dom> dominantChildren = dominant.getChildrenList(childName);
         if (dominantChildren.size() > 0) {
            commonChildren.put(childName, dominantChildren.iterator());
         }
      }

      return commonChildren;
   }

   private static boolean isMergeChildren(Xpp3Dom dominant, Boolean override) {
      return override != null ? override : !isMergeChildren(dominant);
   }

   private static boolean isMergeChildren(Xpp3Dom dominant) {
      return "append".equals(dominant.getAttribute("combine.children"));
   }

   private static boolean isCombineSelfOverride(Xpp3Dom xpp3Dom) {
      String selfMergeMode = xpp3Dom.getAttribute("combine.self");
      return "override".equals(selfMergeMode);
   }

   public static Xpp3Dom mergeXpp3Dom(Xpp3Dom dominant, Xpp3Dom recessive, Boolean childMergeOverride) {
      return dominant != null ? merge(dominant, recessive, childMergeOverride) : recessive;
   }

   public static Xpp3Dom mergeXpp3Dom(Xpp3Dom dominant, Xpp3Dom recessive) {
      return dominant != null ? merge(dominant, recessive, (Boolean)null) : recessive;
   }

   public boolean equals(Object obj) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Xpp3Dom)) {
         return false;
      } else {
         boolean var10000;
         label60: {
            label53: {
               Xpp3Dom dom = (Xpp3Dom)obj;
               if (this.name == null) {
                  if (dom.name != null) {
                     break label53;
                  }
               } else if (!this.name.equals(dom.name)) {
                  break label53;
               }

               if (this.value == null) {
                  if (dom.value != null) {
                     break label53;
                  }
               } else if (!this.value.equals(dom.value)) {
                  break label53;
               }

               if (this.attributes == null) {
                  if (dom.attributes != null) {
                     break label53;
                  }
               } else if (!this.attributes.equals(dom.attributes)) {
                  break label53;
               }

               if (this.childList == null) {
                  if (dom.childList == null) {
                     break label60;
                  }
               } else if (this.childList.equals(dom.childList)) {
                  break label60;
               }
            }

            var10000 = false;
            return var10000;
         }

         var10000 = true;
         return var10000;
      }
   }

   public int hashCode() {
      int result = 17;
      int result = 37 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 37 * result + (this.value != null ? this.value.hashCode() : 0);
      result = 37 * result + (this.attributes != null ? this.attributes.hashCode() : 0);
      result = 37 * result + (this.childList != null ? this.childList.hashCode() : 0);
      return result;
   }

   public String toString() {
      StringWriter writer = new StringWriter();
      Xpp3DomWriter.write((XMLWriter)this.getPrettyPrintXMLWriter(writer), this);
      return writer.toString();
   }

   public String toUnescapedString() {
      StringWriter writer = new StringWriter();
      Xpp3DomWriter.write(this.getPrettyPrintXMLWriter(writer), this, false);
      return writer.toString();
   }

   private PrettyPrintXMLWriter getPrettyPrintXMLWriter(StringWriter writer) {
      return new PrettyPrintXMLWriter(writer, "UTF-8", (String)null);
   }

   public static boolean isNotEmpty(String str) {
      return str != null && str.length() > 0;
   }

   public static boolean isEmpty(String str) {
      return str == null || str.trim().length() == 0;
   }

   public Iterator<Xpp3Dom> iterator() {
      return this.getChildrenList().iterator();
   }
}
