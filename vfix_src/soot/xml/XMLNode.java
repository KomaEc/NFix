package soot.xml;

public class XMLNode extends XMLRoot {
   public static final int TAG_STRING_BUFFER = 4096;
   public XMLNode next = null;
   public XMLNode prev = null;
   public XMLNode parent = null;
   public XMLNode child = null;
   public XMLRoot root = null;

   public XMLNode(String in_name, String in_value, String[] in_attributes, String[] in_values) {
      this.name = in_name;
      this.value = in_value;
      this.attributes = in_attributes;
      this.values = in_values;
   }

   public XMLNode(XMLNode node) {
      if (node != null) {
         this.name = node.name;
         this.value = node.value;
         this.attributes = node.attributes;
         this.values = node.values;
         if (node.child != null) {
            this.child = (XMLNode)node.child.clone();
         }

         if (node.next != null) {
            this.next = (XMLNode)node.next.clone();
         }
      }

   }

   public Object clone() {
      return new XMLNode(this);
   }

   public String toPostString() {
      return this.toPostString("");
   }

   public String toPostString(String indent) {
      return this.next != null ? this.toString(indent) + this.next.toPostString(indent) : this.toString(indent);
   }

   public int getNumberOfChildren() {
      int count = 0;
      if (this.child != null) {
         for(XMLNode current = this.child; current != null; ++count) {
            current = current.next;
         }
      }

      return count;
   }

   public XMLNode addAttribute(String attribute, String value) {
      String[] tempAttributes = this.attributes;
      String[] tempValues = this.values;
      this.attributes = new String[tempAttributes.length + 1];
      this.values = new String[tempValues.length + 1];

      for(int i = 0; i < tempAttributes.length; ++i) {
         this.attributes[i] = tempAttributes[i];
         if (tempValues.length > i) {
            this.values[i] = tempValues[i];
         }
      }

      this.attributes[tempAttributes.length] = attribute.trim();
      this.values[tempValues.length] = value.trim();
      return this;
   }

   public String toString() {
      return this.toString("");
   }

   public String toString(String indent) {
      StringBuffer beginTag = new StringBuffer(4096);
      StringBuffer endTag = new StringBuffer(4096);
      String xmlName = this.eliminateSpaces(this.name);
      beginTag.append("<" + xmlName);
      if (this.attributes != null) {
         for(int i = 0; i < this.attributes.length; ++i) {
            if (this.attributes[i].length() > 0) {
               String attributeName = this.eliminateSpaces(this.attributes[i].toString().trim());
               beginTag.append(" " + attributeName + "=\"");
               if (this.values != null) {
                  if (i < this.values.length) {
                     beginTag.append(this.values[i].toString().trim() + "\"");
                  } else {
                     beginTag.append(attributeName.trim() + "\"");
                  }
               }
            }
         }
      }

      if (this.value.length() < 1 && this.child == null) {
         beginTag.append(" />\n");
         endTag.setLength(0);
      } else {
         beginTag.append(">");
         endTag.append("</" + xmlName + ">\n");
      }

      String returnStr = indent + beginTag.toString();
      if (this.value.length() > 0) {
         returnStr = returnStr + this.value.toString();
      }

      if (this.child != null) {
         returnStr = returnStr + "\n" + this.child.toPostString(indent + "  ");
      }

      if (this.child != null) {
         returnStr = returnStr + indent;
      }

      if (endTag.length() > 0) {
         returnStr = returnStr + endTag.toString();
      }

      return returnStr;
   }

   public XMLNode insertElement(String name) {
      return this.insertElement(name, "", "", "");
   }

   public XMLNode insertElement(String name, String value) {
      return this.insertElement(name, value, "", "");
   }

   public XMLNode insertElement(String name, String value, String[] attributes) {
      return this.insertElement(name, value, (String[])attributes, (String[])null);
   }

   public XMLNode insertElement(String name, String[] attributes, String[] values) {
      return this.insertElement(name, "", attributes, values);
   }

   public XMLNode insertElement(String name, String value, String attribute, String attributeValue) {
      return this.insertElement(name, value, new String[]{attribute}, new String[]{attributeValue});
   }

   public XMLNode insertElement(String name, String value, String[] attributes, String[] values) {
      XMLNode newnode = new XMLNode(name, value, attributes, values);
      if (this.parent != null) {
         if (this.parent.child.equals(this)) {
            this.parent.child = newnode;
         }
      } else if (this.prev == null) {
         this.root.child = newnode;
      }

      newnode.child = null;
      newnode.parent = this.parent;
      newnode.prev = this.prev;
      if (newnode.prev != null) {
         newnode.prev.next = newnode;
      }

      this.prev = newnode;
      newnode.next = this;
      return newnode;
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
      XMLNode newnode = new XMLNode(name, value, attributes, values);
      return this.addElement(newnode);
   }

   public XMLNode addElement(XMLNode node) {
      XMLNode current;
      for(current = this; current.next != null; current = current.next) {
      }

      current.next = node;
      node.prev = current;
      return node;
   }

   public XMLNode addChildren(XMLNode children) {
      XMLNode current;
      for(current = children; current != null; current = current.next) {
         current.parent = this;
      }

      if (this.child == null) {
         this.child = children;
      } else {
         for(current = this.child; current.next != null; current = current.next) {
         }

         current.next = children;
      }

      return this;
   }

   public XMLNode addChild(String name) {
      return this.addChild(name, "", "", "");
   }

   public XMLNode addChild(String name, String value) {
      return this.addChild(name, value, "", "");
   }

   public XMLNode addChild(String name, String value, String[] attributes) {
      return this.addChild(name, value, (String[])attributes, (String[])null);
   }

   public XMLNode addChild(String name, String[] attributes, String[] values) {
      return this.addChild(name, "", attributes, values);
   }

   public XMLNode addChild(String name, String value, String attribute, String attributeValue) {
      return this.addChild(name, value, new String[]{attribute}, new String[]{attributeValue});
   }

   public XMLNode addChild(String name, String value, String[] attributes, String[] values) {
      XMLNode newnode = new XMLNode(name, value, attributes, values);
      return this.addChild(newnode);
   }

   public XMLNode addChild(XMLNode node) {
      if (this.child == null) {
         this.child = node;
         node.parent = this;
      } else {
         XMLNode current;
         for(current = this.child; current.next != null; current = current.next) {
         }

         current.next = node;
         node.prev = current;
         node.parent = this;
      }

      return node;
   }

   private String eliminateSpaces(String str) {
      return str.trim().replace(' ', '_');
   }
}
