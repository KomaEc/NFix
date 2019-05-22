package soot.tagkit;

public class LinkTag extends StringTag {
   Host link;
   String className;

   public LinkTag(String string, Host link, String className, String type) {
      super(string, type);
      this.link = link;
      this.className = className;
   }

   public LinkTag(String string, Host link, String className) {
      super(string);
      this.link = link;
      this.className = className;
   }

   public String toString() {
      return this.s;
   }

   public String getClassName() {
      return this.className;
   }

   public Host getLink() {
      return this.link;
   }

   public String getName() {
      return "StringTag";
   }

   public byte[] getValue() {
      throw new RuntimeException("StringTag has no value for bytecode");
   }
}
