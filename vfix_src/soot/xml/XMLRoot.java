package soot.xml;

public class XMLRoot {
   public String name = "";
   public String value = "";
   public String[] attributes = new String[]{""};
   public String[] values = new String[]{""};
   protected XMLNode child = null;

   XMLRoot() {
   }

   public String toString() {
      return "<?xml version=\"1.0\" ?>\n<!DOCTYPE jil SYSTEM \"http://www.sable.mcgill.ca/~flynn/jil/jil10.dtd\">\n" + this.child.toPostString();
   }

   public XMLNode addElement(String name) {
      return this.addElement(name, "", "", "");
   }

   public XMLNode addElement(String name, String value) {
      return this.addElement(name, value, "", "");
   }

   public XMLNode addElement(String name, String value, String[] attributes) {
      return this.addElement(name, value, (String[])attributes, (String[])null);
   }

   public XMLNode addElement(String name, String[] attributes, String[] values) {
      return this.addElement(name, "", attributes, values);
   }

   public XMLNode addElement(String name, String value, String attribute, String attributeValue) {
      return this.addElement(name, value, new String[]{attribute}, new String[]{attributeValue});
   }

   public XMLNode addElement(String name, String value, String[] attributes, String[] values) {
      XMLNode current = null;
      XMLNode newnode = new XMLNode(name, value, attributes, values);
      newnode.root = this;
      if (this.child == null) {
         this.child = newnode;
         newnode.parent = null;
      } else {
         for(current = this.child; current.next != null; current = current.next) {
         }

         current.next = newnode;
         newnode.prev = current;
      }

      return newnode;
   }
}
