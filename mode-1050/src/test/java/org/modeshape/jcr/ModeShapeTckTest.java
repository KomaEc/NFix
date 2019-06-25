package org.modeshape.jcr;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collections;
import javax.jcr.AccessDeniedException;
import javax.jcr.Binary;
import javax.jcr.Credentials;
import javax.jcr.ImportUUIDBehavior;
import javax.jcr.InvalidItemStateException;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.ValueFactory;
import javax.jcr.lock.LockException;
import javax.jcr.lock.LockManager;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeDefinitionTemplate;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.nodetype.NodeTypeTemplate;
import javax.jcr.nodetype.PropertyDefinitionTemplate;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import javax.jcr.version.VersionManager;
import junit.framework.TestSuite;
import org.apache.jackrabbit.test.AbstractJCRTest;
import org.modeshape.common.FixFor;
import org.junit.Test;
/**
 * Additional ModeShape tests that check for JCR compliance.
 */
public class ModeShapeTckTest extends AbstractJCRTest {

    Session session;

    public ModeShapeTckTest( String testName ) {
        super();

        this.setName(testName);
        this.isReadOnly = true;
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            superuser.getRootNode().getNode(this.nodeName1).remove();
            superuser.save();
        } catch (PathNotFoundException ignore) {
        }

        if (session != null) {
            session.logout();
            session = null;
        }
        super.tearDown();
    }


    boolean isVersionable( VersionManager vm,
                           Node node ) throws RepositoryException {
        try {
            vm.getVersionHistory(node.getPath());
            return true;
        } catch (UnsupportedRepositoryOperationException e) {
            return false;
        }
    }

    @Test
    @SuppressWarnings( "unchecked" )
    @FixFor( "MODE-1050" )
    public void testShouldSuccessfullyRegisterChildAndParentSameTypes() throws RepositoryException {
        Session session = getHelper().getSuperuserSession();

        session.getWorkspace().getNamespaceRegistry().registerNamespace("mx", "urn:test");
        final NodeTypeManager nodeTypeMgr = session.getWorkspace().getNodeTypeManager();

        final NodeTypeTemplate nodeType = nodeTypeMgr.createNodeTypeTemplate();
        nodeType.setName("mx:type");
        nodeType.setDeclaredSuperTypeNames(new String[] {"nt:folder"});

        final NodeDefinitionTemplate subTypes = nodeTypeMgr.createNodeDefinitionTemplate();
        subTypes.setRequiredPrimaryTypeNames(new String[] {"mx:type"});
        nodeType.getNodeDefinitionTemplates().add(subTypes);

        nodeTypeMgr.registerNodeType(nodeType, false);
    }
}
