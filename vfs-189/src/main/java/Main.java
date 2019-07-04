import java.io.File;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.VFS;

public class Main {

    public void testResolveFileNameNull() throws FileSystemException
    {
        VFS.getManager().resolveName((FileName) null, "../");
    }

    public static void main(String... args) throws Exception {
        Main run = new Main();
        run.testResolveFileNameNull();
    }
}