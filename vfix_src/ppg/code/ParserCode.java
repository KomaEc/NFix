package ppg.code;

public class ParserCode extends Code {
   String classname;
   String extendsimpls;

   public ParserCode(String classname, String extendsimpls, String parserCode) {
      this.classname = classname;
      this.extendsimpls = extendsimpls;
      this.value = parserCode;
   }

   public Object clone() {
      return new ParserCode(this.classname, this.extendsimpls, this.value);
   }

   public String toString() {
      if (this.classname == null) {
         this.classname = "code";
      }

      return "parser " + this.classname + this.extendsimpls + " {:\n" + this.value + "\n:}\n";
   }
}
