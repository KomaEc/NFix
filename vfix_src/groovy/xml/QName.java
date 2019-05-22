package groovy.xml;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class QName implements Serializable {
   private static final String EMPTY_STRING = "".intern();
   private String namespaceURI;
   private String localPart;
   private String prefix;

   public QName(String localPart) {
      this(EMPTY_STRING, localPart, EMPTY_STRING);
   }

   public QName(String namespaceURI, String localPart) {
      this(namespaceURI, localPart, EMPTY_STRING);
   }

   public QName(String namespaceURI, String localPart, String prefix) {
      this.namespaceURI = namespaceURI == null ? EMPTY_STRING : namespaceURI.trim().intern();
      if (localPart == null) {
         throw new IllegalArgumentException("invalid QName local part");
      } else {
         this.localPart = localPart.trim().intern();
         if (prefix == null) {
            throw new IllegalArgumentException("invalid QName prefix");
         } else {
            this.prefix = prefix.trim().intern();
         }
      }
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public String getLocalPart() {
      return this.localPart;
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getQualifiedName() {
      return this.prefix.equals(EMPTY_STRING) ? this.localPart : this.prefix + ':' + this.localPart;
   }

   public String toString() {
      return this.namespaceURI.equals(EMPTY_STRING) ? this.localPart : '{' + this.namespaceURI + '}' + this.localPart;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o == null) {
         return false;
      } else if (o instanceof QName) {
         QName qName = (QName)o;
         return !this.namespaceURI.equals(qName.namespaceURI) ? false : this.localPart.equals(qName.localPart);
      } else if (o instanceof String) {
         String string = (String)o;
         if (string.length() == 0) {
            return false;
         } else {
            int lastColonIndex = string.lastIndexOf(":");
            if (lastColonIndex >= 0 && lastColonIndex != string.length() - 1) {
               String stringPrefix = string.substring(0, lastColonIndex);
               String stringLocalPart = string.substring(lastColonIndex + 1);
               return !stringPrefix.equals(this.prefix) && !stringPrefix.equals(this.namespaceURI) ? false : this.localPart.equals(stringLocalPart);
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public boolean matches(Object o) {
      if (this == o) {
         return true;
      } else if (o == null) {
         return false;
      } else if (o instanceof QName) {
         QName qName = (QName)o;
         if (!this.namespaceURI.equals(qName.namespaceURI) && !this.namespaceURI.equals("*") && !qName.namespaceURI.equals("*")) {
            return false;
         } else {
            return this.localPart.equals(qName.localPart) || this.localPart.equals("*") || qName.localPart.equals("*");
         }
      } else if (o instanceof String) {
         String string = (String)o;
         if (string.length() == 0) {
            return false;
         } else {
            int lastColonIndex = string.lastIndexOf(":");
            if (lastColonIndex < 0 && this.prefix.length() == 0) {
               return string.equals(this.localPart);
            } else if (lastColonIndex >= 0 && lastColonIndex != string.length() - 1) {
               String stringPrefix = string.substring(0, lastColonIndex);
               String stringLocalPart = string.substring(lastColonIndex + 1);
               if (!stringPrefix.equals(this.prefix) && !stringPrefix.equals(this.namespaceURI) && !stringPrefix.equals("*")) {
                  return false;
               } else {
                  return this.localPart.equals(stringLocalPart) || stringLocalPart.equals("*");
               }
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public static QName valueOf(String s) {
      if (s != null && !s.equals("")) {
         if (s.charAt(0) == '{') {
            int i = s.indexOf(125);
            if (i == -1) {
               throw new IllegalArgumentException("invalid QName literal");
            } else if (i == s.length() - 1) {
               throw new IllegalArgumentException("invalid QName literal");
            } else {
               return new QName(s.substring(1, i), s.substring(i + 1));
            }
         } else {
            return new QName(s);
         }
      } else {
         throw new IllegalArgumentException("invalid QName literal");
      }
   }

   public int hashCode() {
      int result = this.namespaceURI.hashCode();
      result = 29 * result + this.localPart.hashCode();
      return result;
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.namespaceURI = this.namespaceURI.intern();
      this.localPart = this.localPart.intern();
      this.prefix = this.prefix.intern();
   }
}
