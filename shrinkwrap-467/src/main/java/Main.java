import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.util.HashMap;

import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.nio.file.ShrinkWrapFileSystems;
import org.jboss.shrinkwrap.api.spec.JavaArchive;


public class Main {
    public void noArchiveInEnvShouldResultInIAE() throws Exception {
        FileSystems.newFileSystem(
            ShrinkWrapFileSystems.getRootUri(
                    ShrinkWrap.create(JavaArchive.class)), new HashMap());
    }
    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.noArchiveInEnvShouldResultInIAE();
    }
}