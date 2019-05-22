package org.apache.tools.ant.input;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.tools.ant.BuildException;

public class PropertyFileInputHandler implements InputHandler {
   private Properties props = null;
   public static final String FILE_NAME_KEY = "ant.input.properties";

   public void handleInput(InputRequest request) throws BuildException {
      this.readProps();
      Object o = this.props.get(request.getPrompt());
      if (o == null) {
         throw new BuildException("Unable to find input for '" + request.getPrompt() + "'");
      } else {
         request.setInput(o.toString());
         if (!request.isInputValid()) {
            throw new BuildException("Found invalid input " + o + " for '" + request.getPrompt() + "'");
         }
      }
   }

   private synchronized void readProps() throws BuildException {
      if (this.props == null) {
         String propsFile = System.getProperty("ant.input.properties");
         if (propsFile == null) {
            throw new BuildException("System property ant.input.properties for PropertyFileInputHandler not set");
         }

         this.props = new Properties();

         try {
            this.props.load(new FileInputStream(propsFile));
         } catch (IOException var3) {
            throw new BuildException("Couldn't load " + propsFile, var3);
         }
      }

   }
}
