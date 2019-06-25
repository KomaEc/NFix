/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * ModeShape is free software. Unless otherwise indicated, all code in ModeShape
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * ModeShape is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.modeshape.jcr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.jcr.AccessDeniedException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.version.Version;
import javax.jcr.version.VersionException;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionIterator;
import org.modeshape.graph.Graph;
import org.modeshape.graph.connector.RepositorySourceException;
import org.modeshape.graph.property.Name;
import org.modeshape.graph.property.Path;
import org.modeshape.graph.property.Path.Segment;
import org.modeshape.graph.property.Reference;

/**
 * Convenience wrapper around a version history {@link JcrNode node}.
 */
class JcrVersionHistoryNode extends JcrNode implements VersionHistory {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public JcrVersionHistoryNode( AbstractJcrNode node ) {
        super(node.cache, node.nodeId, node.location);

        assert !node.isRoot() : "Version histories should always be located in the /jcr:system/jcr:versionStorage subgraph";
    }

    /**
     * @return a reference to the {@code jcr:versionLabels} child node of this history node.
     * @throws RepositoryException if an error occurs accessing this node
     */
    private AbstractJcrNode versionLabels() throws RepositoryException {
        Segment segment = segmentFrom(JcrLexicon.VERSION_LABELS);
        return nodeInfo().getChild(segment).getPayload().getJcrNode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getAllVersions()
     */
    public VersionIterator getAllVersions() throws RepositoryException {
        return new JcrVersionIterator(getNodes());
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getRootVersion()
     */
    public JcrVersionNode getRootVersion() throws RepositoryException {
        // Copied from AbstractJcrNode.getNode(String) to avoid double conversion. Needs to be refactored.
        Segment segment = context().getValueFactories().getPathFactory().createSegment(JcrLexicon.ROOT_VERSION);
        try {
            return (JcrVersionNode)nodeInfo().getChild(segment).getPayload().getJcrNode();
        } catch (org.modeshape.graph.property.PathNotFoundException e) {
            String msg = JcrI18n.childNotFoundUnderNode.text(segment, getPath(), cache.workspaceName());
            throw new PathNotFoundException(msg);
        } catch (RepositorySourceException e) {
            throw new RepositoryException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getVersion(java.lang.String)
     */
    public JcrVersionNode getVersion( String versionName ) throws VersionException, RepositoryException {
        try {
            AbstractJcrNode version = getNode(versionName);
            return (JcrVersionNode)version;
        } catch (PathNotFoundException pnfe) {
            throw new VersionException(JcrI18n.invalidVersionName.text(versionName, getPath()));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getVersionByLabel(java.lang.String)
     */
    public JcrVersionNode getVersionByLabel( String label ) throws VersionException, RepositoryException {
        Property prop = versionLabels().getProperty(label);
        if (prop == null) throw new VersionException(JcrI18n.invalidVersionLabel.text(label, getPath()));

        AbstractJcrNode version = session().getNodeByUUID(prop.getString());

        assert version != null;

        return (JcrVersionNode)version;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getVersionLabels()
     */
    public String[] getVersionLabels() throws RepositoryException {
        PropertyIterator iter = versionLabels().getProperties();

        String[] labels = new String[(int)iter.getSize()];
        for (int i = 0; iter.hasNext(); i++) {
            labels[i] = iter.nextProperty().getName();
        }

        return labels;
    }

    /**
     * Returns the version labels that point to the given version
     * 
     * @param version the version for which the labels should be retrieved
     * @return the version labels for that version
     * @throws RepositoryException if an error occurs accessing the repository
     */
    @SuppressWarnings( "deprecation" )
    private Collection<String> versionLabelsFor( Version version ) throws RepositoryException {
        if (!version.getParent().equals(this)) {
            throw new VersionException(JcrI18n.invalidVersion.text(version.getPath(), getPath()));
        }

        String versionUuid = version.getUUID();

        PropertyIterator iter = versionLabels().getProperties();

        List<String> labels = new LinkedList<String>();
        for (int i = 0; iter.hasNext(); i++) {
            Property prop = iter.nextProperty();

            if (versionUuid.equals(prop.getString())) {
                labels.add(prop.getName());
            }
        }

        return labels;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getVersionLabels(javax.jcr.version.Version)
     */
    public String[] getVersionLabels( Version version ) throws RepositoryException {
        return versionLabelsFor(version).toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#getVersionableUUID()
     */
    public String getVersionableUUID() throws RepositoryException {
        return getProperty(JcrLexicon.VERSIONABLE_UUID).getString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#hasVersionLabel(java.lang.String)
     */
    public boolean hasVersionLabel( String label ) throws RepositoryException {
        return versionLabels().hasProperty(label);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#hasVersionLabel(javax.jcr.version.Version, java.lang.String)
     */
    public boolean hasVersionLabel( Version version,
                                    String label ) throws RepositoryException {
        Collection<String> labels = versionLabelsFor(version);

        return labels.contains(label);
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#removeVersion(java.lang.String)
     */
    @SuppressWarnings( "deprecation" )
    public void removeVersion( String versionName )
        throws ReferentialIntegrityException, AccessDeniedException, UnsupportedRepositoryOperationException, VersionException,
        RepositoryException {

        JcrVersionNode version = getVersion(versionName);

        /*
         * Verify that the only references to this version are from its predecessors and successors in the version history.s
         */
        Path versionHistoryPath = version.path().getParent();
        for (PropertyIterator iter = version.getReferences(); iter.hasNext();) {
            AbstractJcrProperty prop = (AbstractJcrProperty)iter.next();

            Path nodePath = prop.path().getParent();

            // If the property's parent is the root node, fail.
            if (nodePath.isRoot()) {
                throw new ReferentialIntegrityException(JcrI18n.cannotRemoveVersion.text(prop.getPath()));
            }

            if (!versionHistoryPath.equals(nodePath.getParent())) {
                throw new ReferentialIntegrityException(JcrI18n.cannotRemoveVersion.text(prop.getPath()));
            }

        }

        String versionUuid = version.getUUID();

        // Get the predecessors and successors for the version being removed ...
        Property predecessors = version.getProperty(JcrLexicon.PREDECESSORS);
        Property successors = version.getProperty(JcrLexicon.SUCCESSORS);

        // Remove the reference to the dead version from the successors property of all the predecessors
        Set<Value> addedValues = new HashSet<Value>();
        for (Value predecessorUuid : predecessors.getValues()) {
            addedValues.clear();
            List<Value> newNodeSuccessors = new ArrayList<Value>();

            // Add each of the successors from the version's predecessor ...
            AbstractJcrNode predecessor = session().getNodeByUUID(predecessorUuid.getString());
            Value[] nodeSuccessors = predecessor.getProperty(JcrLexicon.SUCCESSORS).getValues();
            addValuesNotInSet(nodeSuccessors, newNodeSuccessors, versionUuid, addedValues);

            // Add each of the successors from the version being removed ...
            addValuesNotInSet(successors.getValues(), newNodeSuccessors, versionUuid, addedValues);

            // Set the property ...
            Value[] newSuccessors = newNodeSuccessors.toArray(new Value[newNodeSuccessors.size()]);
            predecessor.editor().setProperty(JcrLexicon.SUCCESSORS, newSuccessors, PropertyType.REFERENCE, false);
            addedValues.clear();
        }

        // Remove the reference to the dead version from the predecessors property of all the successors
        for (Value successorUuid : successors.getValues()) {
            addedValues.clear();
            List<Value> newNodePredecessors = new ArrayList<Value>();

            // Add each of the predecessors from the version's successor ...
            AbstractJcrNode successor = session().getNodeByUUID(successorUuid.getString());
            Value[] nodePredecessors = successor.getProperty(JcrLexicon.PREDECESSORS).getValues();
            addValuesNotInSet(nodePredecessors, newNodePredecessors, versionUuid, addedValues);

            // Add each of the predecessors from the version being removed ...
            addValuesNotInSet(predecessors.getValues(), newNodePredecessors, versionUuid, addedValues);

            // Set the property ...
            Value[] newPredecessors = newNodePredecessors.toArray(new Value[newNodePredecessors.size()]);
            successor.editor().setProperty(JcrLexicon.PREDECESSORS, newPredecessors, PropertyType.REFERENCE, false);
        }

        session().recordRemoval(version.location); // do this first before we destroy the node!
        version.editor().destroy();
    }

    private void addValuesNotInSet( Value[] values,
                                    List<Value> newValues,
                                    String versionUuid,
                                    Set<Value> exceptIn ) throws RepositoryException {
        for (Value value : values) {
            if (!versionUuid.equals(value.getString()) && !exceptIn.contains(value)) {
                exceptIn.add(value);
                newValues.add(value);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#addVersionLabel(java.lang.String, java.lang.String, boolean)
     */
    @SuppressWarnings( "deprecation" )
    public void addVersionLabel( String versionName,
                                 String label,
                                 boolean moveLabel ) throws VersionException, RepositoryException {
        AbstractJcrNode versionLabels = versionLabels();
        Version version = getVersion(versionName);

        try {
            // This throws a PNFE if the named property doesn't already exist
            versionLabels.getProperty(label);
            if (!moveLabel) throw new VersionException(JcrI18n.versionLabelAlreadyExists.text(label));

        } catch (PathNotFoundException pnfe) {
            // This gets thrown if the label doesn't already exist
        }

        Graph graph = cache.session().repository().createSystemGraph(context());
        Reference ref = context().getValueFactories().getReferenceFactory().create(version.getUUID());
        graph.set(label).on(versionLabels.location).to(ref);

        versionLabels.refresh(false);

    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.jcr.version.VersionHistory#removeVersionLabel(java.lang.String)
     */
    public void removeVersionLabel( String label ) throws VersionException, RepositoryException {
        AbstractJcrNode versionLabels = versionLabels();

        try {
            // This throws a PNFE if the named property doesn't already exist
            versionLabels.getProperty(label);
        } catch (PathNotFoundException pnfe) {
            // This gets thrown if the label doesn't already exist
            throw new VersionException(JcrI18n.invalidVersionLabel.text(label, getPath()));
        }

        Graph graph = cache.session().repository().createSystemGraph(context());
        graph.remove(label).on(versionLabels.location).and();

        versionLabels.refresh(false);
    }

    public NodeIterator getAllFrozenNodes() throws RepositoryException {
        return new FrozenNodeIterator(getAllVersions());
    }

    public NodeIterator getAllLinearFrozenNodes() throws RepositoryException {
        return new FrozenNodeIterator(getAllLinearVersions());
    }

    public VersionIterator getAllLinearVersions() throws RepositoryException {
        AbstractJcrNode existingNode = session().getNodeByIdentifier(getVersionableIdentifier());
        if (existingNode == null) return getAllVersions();

        assert existingNode.isNodeType(JcrMixLexicon.VERSIONABLE);

        LinkedList<JcrVersionNode> versions = new LinkedList<JcrVersionNode>();
        JcrVersionNode baseVersion = existingNode.getBaseVersion();

        while (baseVersion != null) {
            versions.addFirst(baseVersion);
            baseVersion = baseVersion.getLinearPredecessor();
        }

        return new LinearVersionIterator(versions, versions.size());
    }

    public String getVersionableIdentifier() throws RepositoryException {
        // ModeShape uses a node's UUID as it's identifier
        return getVersionableUUID();
    }

    /**
     * Iterator over the versions within a version history. This class wraps the {@link JcrChildNodeIterator node iterator} for
     * all nodes of the {@link JcrVersionHistoryNode version history}, silently ignoring the {@code jcr:rootVersion} and
     * {@code jcr:versionLabels} children.
     */
    class JcrVersionIterator implements VersionIterator {

        private final NodeIterator nodeIterator;
        private Version next;
        private int position = 0;

        public JcrVersionIterator( NodeIterator nodeIterator ) {
            super();
            this.nodeIterator = nodeIterator;
        }

        /**
         * {@inheritDoc}
         * 
         * @see javax.jcr.version.VersionIterator#nextVersion()
         */
        public Version nextVersion() {
            Version next = this.next;

            if (next != null) {
                this.next = null;
                return next;
            }

            next = nextVersionIfPossible();

            if (next == null) {
                throw new NoSuchElementException();
            }

            position++;
            return next;
        }

        private JcrVersionNode nextVersionIfPossible() {
            while (nodeIterator.hasNext()) {
                AbstractJcrNode node = (AbstractJcrNode)nodeIterator.nextNode();

                Name nodeName;
                try {
                    nodeName = node.segment().getName();
                } catch (RepositoryException re) {
                    throw new IllegalStateException(re);
                }

                if (!JcrLexicon.VERSION_LABELS.equals(nodeName)) {
                    return (JcrVersionNode)node;
                }
            }

            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see javax.jcr.RangeIterator#getPosition()
         */
        public long getPosition() {
            return position;
        }

        /**
         * {@inheritDoc}
         * 
         * @see javax.jcr.RangeIterator#getSize()
         */
        public long getSize() {
            // The number of version nodes is the number of child nodes of the version history - 1
            // (the jcr:versionLabels node)
            return nodeIterator.getSize() - 1;
        }

        /**
         * {@inheritDoc}
         * 
         * @see javax.jcr.RangeIterator#skip(long)
         */
        public void skip( long count ) {
            // Walk through the list to make sure that we don't accidentally count jcr:rootVersion or jcr:versionLabels as a
            // skipped node
            while (count-- > 0) {
                nextVersion();
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            if (this.next != null) return true;

            this.next = nextVersionIfPossible();

            return this.next != null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Iterator#next()
         */
        public Object next() {
            return nextVersion();
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * An implementation of {@link VersionIterator} that iterates over a given set of versions. This differs from
     * {@link JcrVersionIterator} in that it expects an exact list of versions to iterate over whereas {@code JcrVersionIterator}
     * expects list of children for a {@code nt:versionHistory} node and filters out the label child.
     */
    class LinearVersionIterator implements VersionIterator {

        private final Iterator<? extends Version> versions;
        private final int size;
        private int pos;

        protected LinearVersionIterator( Iterable<? extends Version> versions,
                                         int size ) {
            this.versions = versions.iterator();
            this.size = size;
            this.pos = 0;
        }

        public long getPosition() {
            return pos;
        }

        public long getSize() {
            return this.size;
        }

        public void skip( long skipNum ) {
            while (skipNum-- > 0 && versions.hasNext()) {
                versions.next();
                pos++;
            }

        }

        public Version nextVersion() {
            return versions.next();
        }

        public boolean hasNext() {
            return versions.hasNext();
        }

        public Object next() {
            return nextVersion();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    class FrozenNodeIterator implements NodeIterator {
        private final VersionIterator versions;

        FrozenNodeIterator( VersionIterator versionIter ) {
            this.versions = versionIter;
        }

        public boolean hasNext() {
            return versions.hasNext();
        }

        public Object next() {
            return nextNode();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Node nextNode() {
            try {
                return versions.nextVersion().getFrozenNode();
            } catch (RepositoryException re) {
                // ModeShape doesn't throw a RepositoryException on getFrozenNode() from a valid version node
                throw new IllegalStateException(re);
            }
        }

        public long getPosition() {
            return versions.getPosition();
        }

        public long getSize() {
            return versions.getSize();
        }

        public void skip( long skipNum ) {
            versions.skip(skipNum);
        }
    }
}
