package polyglot.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;

public class TypeEncoder {
   protected TypeSystem ts;
   protected final boolean zip = false;
   protected final boolean base64 = true;
   protected final boolean test = false;

   public TypeEncoder(TypeSystem ts) {
      this.ts = ts;
   }

   public String encode(Type t) throws IOException {
      if (Report.should_report((String)"serialize", 1)) {
         Report.report(1, "Encoding type " + t);
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new TypeOutputStream(baos, this.ts, t);
      oos.writeObject(t);
      oos.flush();
      oos.close();
      byte[] b = baos.toByteArray();
      if (Report.should_report((String)"serialize", 2)) {
         Report.report(2, "Size of serialization (without zipping) is " + b.length + " bytes");
      }

      String s = new String(Base64.encode(b));
      if (Report.should_report((String)"serialize", 2)) {
         Report.report(2, "Size of serialization after conversion to string is " + s.length() + " characters");
      }

      return s;
   }

   public Type decode(String s) throws InvalidClassException {
      byte[] b = Base64.decode(s.toCharArray());

      try {
         ObjectInputStream ois = new TypeInputStream(new ByteArrayInputStream(b), this.ts);
         return (Type)ois.readObject();
      } catch (InvalidClassException var5) {
         throw var5;
      } catch (IOException var6) {
         throw new InternalCompilerError("IOException thrown while decoding serialized type info: " + var6.getMessage(), var6);
      } catch (ClassNotFoundException var7) {
         throw new InternalCompilerError("Unable to find one of the classes for the serialized type info: " + var7.getMessage(), var7);
      }
   }
}
