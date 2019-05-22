package soot.util.dot;

import java.io.IOException;
import java.io.OutputStream;

public class DotGraphCommand implements Renderable {
   String command;

   public DotGraphCommand(String cmd) {
      this.command = cmd;
   }

   public void render(OutputStream out, int indent) throws IOException {
      DotGraphUtility.renderLine(out, this.command, indent);
   }
}
