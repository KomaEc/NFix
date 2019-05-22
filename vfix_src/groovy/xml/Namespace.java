package groovy.xml;

public class Namespace {
   private String uri;
   private String prefix;

   public Namespace() {
   }

   public Namespace(String uri) {
      this.uri = uri.trim();
   }

   public Namespace(String uri, String prefix) {
      this.uri = uri.trim();
      this.prefix = prefix.trim();
   }

   public QName get(String localName) {
      if (this.uri != null && this.uri.length() > 0) {
         return this.prefix != null ? new QName(this.uri, localName, this.prefix) : new QName(this.uri, localName);
      } else {
         return new QName(localName);
      }
   }

   public String getPrefix() {
      return this.prefix;
   }

   public String getUri() {
      return this.uri;
   }
}
