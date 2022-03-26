// 
// Decompiled by Procyon v0.5.36
// 

package dataStructure.pqTree;

import java.awt.*;
import java.util.Vector;

public class PQNode
{
    public static final boolean SHOW_DEBUG_OUTPUT = false;
    public static final int DRAW_SIZE = 12;
    public static final int DRAW_BOUNDARY_SIZE = 12;
    public static final int DRAW_CONNECTOR_SIZE = 24;
    public static final Color DRAW_NORMAL_COLOR;
    public static final Color DRAW_PARTIAL_COLOR;
    public static final Color DRAW_NO_PARENT_COLOR;
    public static final Color DRAW_PSEUDO_PARENT_COLOR;
    public static final Color DRAW_DEBUG_COLOR;
    private static final int LABEL_EMPTY = 0;
    private static final int LABEL_PARTIAL = 1;
    private static final int LABEL_FULL = 2;
    private static final int TYPE_PNODE = 0;
    private static final int TYPE_QNODE = 1;
    protected static final int TYPE_DNODE = 3;
    private int label;
    protected int type;
    private boolean blocked;
    private boolean queued;
    private int pertinentChildCount;
    private int pertinentLeafCount;
    private Object data;
    private boolean deleted;
    private boolean pseudoNode;
    private PQNode parent;
    protected PQNodePair siblings;
    private PQNode left;
    private PQNode right;
    private PQNode fullLeft;
    private PQNode fullRight;
    private PQNode partialLeft;
    private PQNode partialRight;
    private int childCount;
    private int fullChildCount;
    private int partialChildCount;
    private PQNodePair endMostChildren;
    private PQNode childAccessNode;
    private PQNode fullChildAccessNode;
    private PQNode partialChildAccessNode;
    private int subLeafCount;
    private int depth;
    private int childBounds;
    private int leftBound;
    
    static {
        DRAW_NORMAL_COLOR = Color.black;
        DRAW_PARTIAL_COLOR = Color.gray;
        DRAW_NO_PARENT_COLOR = Color.red;
        DRAW_PSEUDO_PARENT_COLOR = Color.green;
        DRAW_DEBUG_COLOR = Color.blue;
    }
    
    public PQNode() {
        this.data = null;
        this.parent = null;
        this.siblings = null;
        this.left = null;
        this.right = null;
        this.fullLeft = null;
        this.fullRight = null;
        this.partialLeft = null;
        this.partialRight = null;
        this.endMostChildren = null;
        this.childAccessNode = null;
        this.fullChildAccessNode = null;
        this.partialChildAccessNode = null;
        this.init(true);
    }
    
    public PQNode(final Object data) {
        this.data = null;
        this.parent = null;
        this.siblings = null;
        this.left = null;
        this.right = null;
        this.fullLeft = null;
        this.fullRight = null;
        this.partialLeft = null;
        this.partialRight = null;
        this.endMostChildren = null;
        this.childAccessNode = null;
        this.fullChildAccessNode = null;
        this.partialChildAccessNode = null;
        this.init(true);
        this.data = data;
    }
    
    public void init(final boolean pNode) {
        this.childAccessNode = null;
        this.fullChildAccessNode = null;
        this.partialChildAccessNode = null;
        this.endMostChildren = null;
        this.siblings = null;
        this.childCount = 0;
        this.fullChildCount = 0;
        this.partialChildCount = 0;
        this.label = 0;
        this.pertinentChildCount = 0;
        this.pertinentLeafCount = 0;
        this.queued = false;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.fullLeft = null;
        this.fullRight = null;
        this.partialLeft = null;
        this.partialRight = null;
        if (pNode) {
            this.type = 0;
        }
        else {
            this.type = 1;
        }
        this.data = null;
        this.deleted = false;
        this.pseudoNode = false;
    }
    
    public void delete() {
        if (this.pseudoNode) {
            this.childAccessNode = null;
            this.partialChildAccessNode = null;
            this.siblings = null;
            this.childCount = 0;
            this.partialChildCount = 0;
            this.pertinentChildCount = 0;
            this.pertinentLeafCount = 0;
            this.queued = false;
            this.parent = null;
            this.left = null;
            this.right = null;
            this.fullLeft = null;
            this.fullRight = null;
            this.partialLeft = null;
            this.partialRight = null;
        }
        else {
            this.init(false);
        }
        this.deleted = true;
    }
    
    public int getPertinentChildCount() {
        return this.pertinentChildCount;
    }
    
    public void setPertinentChildCount(final int value) {
        this.pertinentChildCount = value;
    }
    
    public int getPertinentLeafCount() {
        return this.pertinentLeafCount;
    }
    
    public void setPertinentLeafCount(final int value) {
        this.pertinentLeafCount = value;
    }
    
    public boolean isPseudoNode() {
        return this.pseudoNode;
    }
    
    public void pseudoNode() {
        this.pseudoNode = true;
        this.childBounds = 0;
        this.leftBound = Integer.MAX_VALUE;
        this.subLeafCount = this.pertinentChildCount;
    }
    
    public boolean isDeleted() {
        return this.deleted;
    }
    
    public PQNode getParent() {
        return this.parent;
    }
    
    public void setParent(final PQNode theParent) {
        this.parent = theParent;
    }
    
    public PQNodePair getSiblings() {
        return this.siblings;
    }
    
    public int getNumFullChildren() {
        return this.fullChildCount;
    }
    
    public int getNumPartialChildren() {
        return this.partialChildCount;
    }
    
    public boolean isQNode() {
        return this.type == 1;
    }
    
    public boolean isPNode() {
        return this.type == 0;
    }
    
    public boolean isDNode() {
        return this.type == 3;
    }
    
    public boolean isFull() {
        return this.label == 2;
    }
    
    public boolean isFullOrDirectedFull(final PQNode aNode) throws Exception {
        return (!this.isDNode() && this.isFull()) || (this.isDNode() && this.siblings.otherPQNode(aNode) != null && this.siblings.otherPQNode(aNode).isFullOrDirectedFull(this));
    }
    
    public boolean isPartial() {
        return this.label == 1;
    }
    
    public boolean isPartialOrDirectedPartial(final PQNode aNode) throws Exception {
        return (!this.isDNode() && this.isPartial()) || (this.isDNode() && this.siblings.otherPQNode(aNode) != null && this.siblings.otherPQNode(aNode).isPartialOrDirectedPartial(this));
    }
    
    public boolean isEmpty() {
        return this.label == 0;
    }
    
    public boolean isEmptyOrDirectedEmpty(final PQNode aNode) throws Exception {
        return (!this.isDNode() && this.isEmpty()) || (this.isDNode() && this.siblings.otherPQNode(aNode) != null && this.siblings.otherPQNode(aNode).isEmptyOrDirectedEmpty(this));
    }
    
    public boolean isBlocked() {
        return this.blocked;
    }
    
    public void setBlocked() {
        this.blocked = true;
    }
    
    public void setUnblocked() {
        this.blocked = false;
    }
    
    public void setQueued() {
        this.queued = true;
    }
    
    public void setUnqueued() {
        this.queued = false;
    }
    
    public boolean isQueued() {
        return this.queued;
    }
    
    public void setData(final Object someData) {
        this.data = someData;
    }
    
    public Object getData() {
        return this.data;
    }
    
    public int getLabel() {
        return this.label;
    }
    
    public PQNodePair getEndMostChildren() {
        return this.endMostChildren;
    }
    
    public void becomeRoot() {
        this.parent = null;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public int getNumChildren() throws Exception {
        if (this.isQNode()) {
            throw new Exception("*** Warning, Qnodes do not store num children");
        }
        return this.childCount;
    }
    
    public int getNumEmptyChildren() throws Exception {
        if (this.isQNode()) {
            throw new Exception("*** Warning, Qnodes do not store num (empty) children");
        }
        return this.childCount - this.fullChildCount - this.partialChildCount;
    }
    
    public void convertToQNode() throws Exception {
        this.type = 1;
        this.endMostChildren = new PQNodePair();
        if (this.childCount > 0) {
            throw new Exception("*** ERROR cannot convert to qnode unless no children present!");
        }
    }
    
    public void convertToPNode() throws Exception {
        if (this.isQNode()) {
            final PQNode aNode = this.endMostChildren.PQNodeAt(0);
            final PQNode bNode = this.endMostChildren.PQNodeAt(1);
            if (!this.hasOnlyTwoChildren()) {
                throw new Exception("*** ERROR convert to pnode was only designed for cases when a qnode has 2 children!");
            }
            this.type = 0;
            this.childAccessNode = aNode;
            this.childCount = 2;
            aNode.siblings = null;
            aNode.left = bNode;
            aNode.right = bNode;
            bNode.siblings = null;
            bNode.left = aNode;
            bNode.right = aNode;
            this.endMostChildren = null;
        }
    }
    
    public void convertToDNode() throws Exception {
        if (!this.isPNode()) {
            throw new Exception("*** ERROR convert to dnode is only allowed for pnodes!");
        }
        if (this.childCount == 0) {
            this.type = 3;
            return;
        }
        throw new Exception("*** ERROR convert to dnode is only allowed for child-less pnodes!");
    }
    
    public void labelAsFull() throws Exception {
        if (!this.isFull()) {
            if (this.parent != null) {
                this.parent.removeChild(this, false);
            }
            this.label = 2;
            if (this.parent != null) {
                this.parent.addChild(this, false);
            }
        }
    }
    
    public void labelAsPartial() throws Exception {
        if (!this.isPartial()) {
            if (this.parent != null) {
                this.parent.removeChild(this, false);
            }
            this.label = 1;
            if (this.parent != null) {
                this.parent.addChild(this, false);
            }
        }
    }
    
    public void labelAsEmpty() throws Exception {
        if (!this.isEmpty()) {
            if (this.parent != null) {
                this.parent.removeChild(this, false);
            }
            this.label = 0;
            if (this.parent != null) {
                this.parent.addChild(this, false);
            }
        }
    }
    
    public boolean hasChildren() {
        if (this.isPNode()) {
            return this.childCount > 0;
        }
        return this.isQNode() && this.endMostChildren.size() > 0;
    }
    
    private Vector getAllChildren() throws Exception {
        final Vector allVector = new Vector();
        if (this.isPNode()) {
            if (this.hasChildren()) {
                PQNode currentNode = this.childAccessNode;
                do {
                    allVector.addElement(currentNode);
                    currentNode = currentNode.right;
                } while (currentNode != this.childAccessNode);
            }
        }
        else if (this.isQNode() && this.hasChildren()) {
            PQNode previousNode = null;
            PQNode currentNode2 = this.endMostChildren.PQNodeAt(0);
            PQNode lastNode = null;
            if (this.isPseudoNode()) {
                if (currentNode2.siblings.PQNodeAt(0) != null && currentNode2.siblings.PQNodeAt(0).parent != this) {
                    previousNode = currentNode2.siblings.PQNodeAt(0);
                }
                else if (currentNode2.siblings.PQNodeAt(1) != null && currentNode2.siblings.PQNodeAt(1).parent != this) {
                    previousNode = currentNode2.siblings.PQNodeAt(1);
                }
                if (this.endMostChildren.size() > 1) {
                    final PQNode tempNode = this.endMostChildren.PQNodeAt(1);
                    if (tempNode.siblings.PQNodeAt(0) != null && tempNode.siblings.PQNodeAt(0).parent != this) {
                        lastNode = tempNode.siblings.PQNodeAt(0);
                    }
                    else if (tempNode.siblings.PQNodeAt(1) != null && tempNode.siblings.PQNodeAt(1).parent != this) {
                        lastNode = tempNode.siblings.PQNodeAt(1);
                    }
                }
            }
            do {
                allVector.addElement(currentNode2);
                final PQNode nextNode = currentNode2.siblings.otherPQNode(previousNode);
                previousNode = currentNode2;
                currentNode2 = nextNode;
            } while (currentNode2 != lastNode);
        }
        return allVector;
    }
    
    public boolean isSiblingOf(final PQNode aNode) throws Exception {
        if (this.siblings != null && aNode.siblings != null) {
            return (this.siblings.contains(aNode) && aNode.siblings.contains(this)) || (this.siblings.PQNodeAt(0) != null && this.siblings.PQNodeAt(0).isDNode() && this.siblings.PQNodeAt(0).siblings.otherPQNode(this) == aNode) || (this.siblings.PQNodeAt(1) != null && this.siblings.PQNodeAt(1).isDNode() && this.siblings.PQNodeAt(1).siblings.otherPQNode(this) == aNode);
        }
        throw new Exception("*** ERROR isSiblingOf was used on non Q-Node children!");
    }
    
    public PQNode getFullLeavesFrom() throws Exception {
        if (this.isQNode()) {
            PQNode aFullChild = this.fullChildAccessNode;
            PQNode prevFullChild = null;
            PQNode firstFullChild = null;
            while (true) {
                while (aFullChild.siblings.PQNodeAt(0) != null && (aFullChild.siblings.PQNodeAt(0) == null || aFullChild.siblings.PQNodeAt(0).isFullOrDirectedFull(aFullChild))) {
                    if (aFullChild.siblings.PQNodeAt(1) == null || (aFullChild.siblings.PQNodeAt(1) != null && !aFullChild.siblings.PQNodeAt(1).isFullOrDirectedFull(aFullChild))) {
                        prevFullChild = aFullChild.siblings.PQNodeAt(1);
                        firstFullChild = aFullChild;
                    }
                    else {
                        aFullChild = aFullChild.fullRight;
                        if (aFullChild != this.fullChildAccessNode) {
                            continue;
                        }
                    }
                    if (prevFullChild != null && prevFullChild.isDNode()) {
                        PQNode nextNode = prevFullChild;
                        PQNode tempNode;
                        for (PQNode prevNode = firstFullChild; nextNode != null && nextNode.isDNode(); nextNode = nextNode.siblings.otherPQNode(prevNode), prevNode = tempNode) {
                            tempNode = nextNode;
                        }
                        prevFullChild = nextNode;
                    }
                    return prevFullChild;
                }
                prevFullChild = aFullChild.siblings.PQNodeAt(0);
                firstFullChild = aFullChild;
                continue;
            }
        }
        throw new Exception("*** ERROR getFullLeavesFrom() is only meant for Q-Nodes!");
    }
    
    public PQNode getFullLeavesTo() throws Exception {
        PQNode prevFullChild = this.getFullLeavesFrom();
        PQNode nextFullChild = null;
        if (prevFullChild == null) {
            PQNode aFullChild = this.fullChildAccessNode;
            while (true) {
                while (aFullChild.siblings.PQNodeAt(0) == null || aFullChild.siblings.PQNodeAt(0).isFullOrDirectedFull(aFullChild)) {
                    if (aFullChild.siblings.PQNodeAt(1) != null && !aFullChild.siblings.PQNodeAt(1).isFullOrDirectedFull(aFullChild)) {
                        nextFullChild = aFullChild.siblings.PQNodeAt(1);
                        prevFullChild = aFullChild;
                    }
                    else {
                        aFullChild = aFullChild.fullRight;
                        if (aFullChild != this.fullChildAccessNode) {
                            continue;
                        }
                    }
                    if (nextFullChild.isDNode()) {
                        do {
                            final PQNode tempFullChild = nextFullChild;
                            nextFullChild = nextFullChild.siblings.otherPQNode(prevFullChild);
                            prevFullChild = tempFullChild;
                        } while (nextFullChild != null && nextFullChild.isDNode());
                        return nextFullChild;
                    }
                    return nextFullChild;
                }
                nextFullChild = aFullChild.siblings.PQNodeAt(0);
                prevFullChild = aFullChild;
                continue;
            }
        }
        if (prevFullChild.siblings.PQNodeAt(0) != null && prevFullChild.siblings.PQNodeAt(0).isFullOrDirectedFull(prevFullChild)) {
            final PQNode aFullChild = prevFullChild.siblings.PQNodeAt(0);
        }
        else {
            if (prevFullChild.siblings.PQNodeAt(1) == null || !prevFullChild.siblings.PQNodeAt(1).isFullOrDirectedFull(prevFullChild)) {
                throw new Exception("*** ERROR getFullLeavesTo() failed to get a valid fullLeaveFrom!");
            }
            final PQNode aFullChild = prevFullChild.siblings.PQNodeAt(1);
        }
        final PQNode pqNode;
        nextFullChild = pqNode;
        PQNode aPrevFullChild = prevFullChild;
        do {
            final PQNode tempFullChild = nextFullChild;
            nextFullChild = nextFullChild.siblings.otherPQNode(aPrevFullChild);
            aPrevFullChild = tempFullChild;
            if (nextFullChild != null) {
                continue;
            }
            break;
        } while (nextFullChild.isFullOrDirectedFull(aPrevFullChild) || nextFullChild.isDNode());
        return nextFullChild;
    }
    
    public Vector getFullLeaves() throws Exception {
        final Vector fullLeafVector = new Vector();
        if (this.isQNode()) {
            if (this.hasChildren()) {
                PQNode firstFullChild = null;
                PQNode aFullChild = this.fullChildAccessNode;
                PQNode prevFullChild = null;
                while (true) {
                    while (aFullChild.siblings.PQNodeAt(0) != null && (aFullChild.siblings.PQNodeAt(0) == null || aFullChild.siblings.PQNodeAt(0).isFullOrDirectedFull(aFullChild))) {
                        if (aFullChild.siblings.PQNodeAt(1) == null || (aFullChild.siblings.PQNodeAt(1) != null && !aFullChild.siblings.PQNodeAt(1).isFullOrDirectedFull(aFullChild))) {
                            prevFullChild = aFullChild.siblings.PQNodeAt(1);
                            firstFullChild = aFullChild;
                        }
                        else {
                            aFullChild = aFullChild.fullRight;
                            if (aFullChild != this.fullChildAccessNode) {
                                continue;
                            }
                        }
                        if (prevFullChild != null && prevFullChild.isDNode()) {
                            final Vector directedNodes = new Vector();
                            PQNode tempNode;
                            for (PQNode nextNode = prevFullChild, prevNode = firstFullChild; nextNode != null && nextNode.isDNode(); nextNode = nextNode.siblings.otherPQNode(prevNode), prevNode = tempNode) {
                                if (prevNode == ((PQDNode)nextNode).getDirection()) {
                                    ((PQDNode)nextNode).setReadInReverseDirection(false);
                                }
                                else {
                                    if (nextNode.siblings.otherPQNode(prevNode) != ((PQDNode)nextNode).getDirection()) {
                                        throw new Exception("*** ERROR: Could not verify DNode read direction!");
                                    }
                                    ((PQDNode)nextNode).setReadInReverseDirection(true);
                                }
                                directedNodes.addElement(nextNode);
                                tempNode = nextNode;
                            }
                            for (int i = directedNodes.size(); i > 0; --i) {
                                fullLeafVector.addElement(directedNodes.elementAt(i - 1));
                            }
                        }
                        aFullChild = firstFullChild;
                        do {
                            if (aFullChild.isDNode()) {
                                if (prevFullChild == ((PQDNode)aFullChild).getDirection()) {
                                    ((PQDNode)aFullChild).setReadInReverseDirection(true);
                                }
                                else {
                                    if (aFullChild.siblings.otherPQNode(prevFullChild) != ((PQDNode)aFullChild).getDirection()) {
                                        throw new Exception("Could not verify DNode read direction!");
                                    }
                                    ((PQDNode)aFullChild).setReadInReverseDirection(false);
                                }
                            }
                            final Vector subLeafVector = aFullChild.getFullLeaves();
                            fullLeafVector.addAll(subLeafVector);
                            final PQNode tempFullChild = aFullChild;
                            aFullChild = aFullChild.siblings.otherPQNode(prevFullChild);
                            prevFullChild = tempFullChild;
                            if (aFullChild != null) {
                                continue;
                            }
                            break;
                        } while (aFullChild.isFullOrDirectedFull(prevFullChild) || aFullChild.isDNode());
                        return fullLeafVector;
                    }
                    prevFullChild = aFullChild.siblings.PQNodeAt(0);
                    firstFullChild = aFullChild;
                    continue;
                }
            }
            throw new Exception("*** ERROR: QNode with no full children to get!");
        }
        else if (this.hasChildren()) {
            PQNode aFullChild2 = this.fullChildAccessNode;
            do {
                final Vector subLeafVector = aFullChild2.getFullLeaves();
                fullLeafVector.addAll(subLeafVector);
                aFullChild2 = aFullChild2.fullRight;
            } while (aFullChild2 != this.fullChildAccessNode);
        }
        else {
            fullLeafVector.addElement(this);
        }
        return fullLeafVector;
    }
    
    public void moveFullChildrenTo(final PQNode newNode) throws Exception {
        if (this.isPNode()) {
            if (this.fullChildCount > 0) {
                PQNode currentNode = this.fullChildAccessNode;
                do {
                    final PQNode nextNode = currentNode.fullRight;
                    this.removeChild(currentNode);
                    newNode.addChild(currentNode);
                    currentNode = nextNode;
                } while (this.fullChildAccessNode != null);
            }
            return;
        }
        throw new Exception("*** ERROR move full children method not meant for children of q nodes!");
    }
    
    public PQNode getPartialChild(final int index) throws Exception {
        if (index + 1 > this.partialChildCount) {
            throw new Exception("*** ERROR tried to get a partial child that does not exist! [" + index + "]");
        }
        if (index == 0) {
            return this.partialChildAccessNode;
        }
        if (index == 1) {
            return this.partialChildAccessNode.partialRight;
        }
        throw new Exception("*** ERROR tried to get a partial child that does not exist! [" + index + "]");
    }
    
    public PQNode removeOnlyFullChild() throws Exception {
        if (!this.isPNode()) {
            throw new Exception("*** ERROR remove only full child is only meant for p nodes!");
        }
        if (this.fullChildCount != 1) {
            throw new Exception("*** ERROR not exactly one full child to remove! " + this.fullChildCount);
        }
        final PQNode returnNode = this.fullChildAccessNode;
        this.removeChild(returnNode);
        return returnNode;
    }
    
    public PQNode getOnlyFullChild() throws Exception {
        if (!this.isPNode()) {
            throw new Exception("*** ERROR retrieve only full child is only meant for p nodes!");
        }
        if (this.fullChildCount != 1) {
            throw new Exception("*** ERROR not exactly one full child to retrieve! " + this.fullChildCount);
        }
        return this.fullChildAccessNode;
    }
    
    public PQNode removeOnlyEmptyChild() throws Exception {
        if (!this.isPNode()) {
            throw new Exception("*** ERROR remove only empty child is only meant for p nodes!");
        }
        if (this.getNumEmptyChildren() != 1) {
            throw new Exception("*** ERROR not exactly one empty child to remove! " + this.getNumEmptyChildren());
        }
        PQNode returnNode = this.childAccessNode;
        while (true) {
            while (!returnNode.isEmpty()) {
                returnNode = returnNode.right;
                if (returnNode == this.childAccessNode) {
                    this.removeChild(returnNode);
                    return returnNode;
                }
            }
            continue;
        }
    }
    
    public void addChild(final PQNode pq) throws Exception {
        this.addChild(pq, true);
    }
    
    public void addChild(final PQNode pq, final boolean modify) throws Exception {
        if (pq.isFull()) {
            ++this.fullChildCount;
            pq.fullLeft = null;
            pq.fullRight = null;
            if (this.fullChildAccessNode == null) {
                this.fullChildAccessNode = pq;
                this.fullChildAccessNode.fullLeft = this.fullChildAccessNode;
                this.fullChildAccessNode.fullRight = this.fullChildAccessNode;
            }
            else {
                pq.fullLeft = this.fullChildAccessNode.fullLeft;
                pq.fullLeft.fullRight = pq;
                this.fullChildAccessNode.fullLeft = pq;
                pq.fullRight = this.fullChildAccessNode;
                this.fullChildAccessNode = pq;
            }
        }
        else if (pq.isPartial()) {
            ++this.partialChildCount;
            pq.partialLeft = null;
            pq.partialRight = null;
            if (this.partialChildAccessNode == null) {
                this.partialChildAccessNode = pq;
                this.partialChildAccessNode.partialLeft = this.partialChildAccessNode;
                this.partialChildAccessNode.partialRight = this.partialChildAccessNode;
            }
            else {
                pq.partialLeft = this.partialChildAccessNode.partialLeft;
                pq.partialLeft.partialRight = pq;
                this.partialChildAccessNode.partialLeft = pq;
                pq.partialRight = this.partialChildAccessNode;
                this.partialChildAccessNode = pq;
            }
        }
        if (this.isPNode() && modify) {
            pq.parent = this;
            ++this.childCount;
            pq.left = null;
            pq.right = null;
            pq.siblings = null;
            if (this.childAccessNode == null) {
                this.childAccessNode = pq;
                this.childAccessNode.left = this.childAccessNode;
                this.childAccessNode.right = this.childAccessNode;
            }
            else {
                pq.left = this.childAccessNode.left;
                pq.left.right = pq;
                this.childAccessNode.left = pq;
                pq.right = this.childAccessNode;
                this.childAccessNode = pq;
            }
        }
        else if (this.isQNode() && !this.isPseudoNode() && modify) {
            pq.parent = this;
            PQNode sibling = null;
            if (pq.siblings != null) {
                if (pq.siblings.PQNodeAt(0) != null && this.endMostChildren.contains(pq.siblings.PQNodeAt(0))) {
                    sibling = pq.siblings.PQNodeAt(0);
                }
                else if (pq.siblings.PQNodeAt(1) != null && this.endMostChildren.contains(pq.siblings.PQNodeAt(1))) {
                    sibling = pq.siblings.PQNodeAt(1);
                }
            }
            else {
                pq.siblings = new PQNodePair();
            }
            if (sibling == null) {
                for (int i = 0; i < this.endMostChildren.size(); ++i) {
                    if (this.endMostChildren.PQNodeAt(i).label == pq.label) {
                        sibling = this.endMostChildren.PQNodeAt(i);
                        break;
                    }
                    if (this.endMostChildren.PQNodeAt(i).isFull() && pq.isPartial()) {
                        sibling = this.endMostChildren.PQNodeAt(i);
                        break;
                    }
                    if (this.endMostChildren.PQNodeAt(i).isPartial() && pq.isFull()) {
                        sibling = this.endMostChildren.PQNodeAt(i);
                        break;
                    }
                }
            }
            if (sibling == null && this.endMostChildren.size() > 0) {
                sibling = this.endMostChildren.PQNodeAt(0);
            }
            if (sibling != null) {
                if (this.endMostChildren.size() > 1) {
                    this.endMostChildren.removePQNode(sibling);
                }
                this.endMostChildren.addPQNode(pq);
                sibling.siblings.addPQNode(pq);
                pq.siblings.addPQNode(sibling);
            }
            else {
                this.endMostChildren.addPQNode(pq);
            }
        }
        else if (this.isQNode() && this.isPseudoNode() && modify) {
            pq.parent = this;
            if (this.childAccessNode == null) {
                this.childAccessNode = pq;
            }
            if (pq.siblings == null || pq.siblings.size() != 2) {
                throw new Exception("*** ERROR invalid child being added to pseudonode!");
            }
            if (pq.siblings.PQNodeAt(0).parent != this || pq.siblings.PQNodeAt(1).parent != this) {
                this.endMostChildren.addPQNode(pq);
            }
        }
    }
    
    public void absorbPartialChild(final PQNode partialChild) throws Exception {
        if (this.isQNode() && partialChild.isQNode() && partialChild.isPartial()) {
            PQNode fullConnectChild = partialChild.siblings.PQNodeAt(0);
            if (!fullConnectChild.isFullOrDirectedFull(partialChild) && !fullConnectChild.isPartialOrDirectedPartial(partialChild)) {
                fullConnectChild = partialChild.siblings.PQNodeAt(1);
                if (fullConnectChild != null && !fullConnectChild.isFullOrDirectedFull(partialChild) && !fullConnectChild.isPartialOrDirectedPartial(partialChild)) {
                    fullConnectChild = null;
                }
            }
            PQNode emptyConnectChild = partialChild.siblings.PQNodeAt(0);
            if (!emptyConnectChild.isEmptyOrDirectedEmpty(partialChild)) {
                emptyConnectChild = partialChild.siblings.PQNodeAt(1);
                if (emptyConnectChild != null && !emptyConnectChild.isEmptyOrDirectedEmpty(partialChild)) {
                    emptyConnectChild = null;
                }
            }
            PQNode fullJoinChild = partialChild.endMostChildren.PQNodeAt(0);
            if (!fullJoinChild.isFullOrDirectedFull(null)) {
                fullJoinChild = null;
                if (partialChild.endMostChildren.size() > 1) {
                    fullJoinChild = partialChild.endMostChildren.PQNodeAt(1);
                    if (!fullJoinChild.isFullOrDirectedFull(null)) {
                        fullJoinChild = null;
                    }
                }
            }
            PQNode emptyJoinChild = partialChild.endMostChildren.PQNodeAt(0);
            if (!emptyJoinChild.isEmptyOrDirectedEmpty(null)) {
                emptyJoinChild = null;
                if (partialChild.endMostChildren.size() > 1) {
                    emptyJoinChild = partialChild.endMostChildren.PQNodeAt(1);
                    if (!emptyJoinChild.isEmptyOrDirectedEmpty(null)) {
                        emptyJoinChild = null;
                    }
                }
            }
            if (fullJoinChild == null || emptyJoinChild == null) {
                throw new Exception("*** ERROR invalid partial child in absorb partial child!");
            }
            if (fullConnectChild != null) {
                fullJoinChild.siblings.addPQNode(fullConnectChild);
                fullConnectChild.siblings.replacePQNode(partialChild, fullJoinChild);
            }
            else {
                if (!this.endMostChildren.removePQNode(partialChild)) {
                    throw new Exception("*** ERROR could not absorb partial child!");
                }
                fullJoinChild.parent = this;
                this.endMostChildren.addPQNode(fullJoinChild);
            }
            if (emptyConnectChild != null) {
                emptyJoinChild.siblings.addPQNode(emptyConnectChild);
                emptyConnectChild.siblings.replacePQNode(partialChild, emptyJoinChild);
            }
            else {
                if (!this.endMostChildren.removePQNode(partialChild)) {
                    throw new Exception("*** ERROR could not absorb partial child!");
                }
                emptyJoinChild.parent = this;
                this.endMostChildren.addPQNode(emptyJoinChild);
            }
            if (partialChild.fullChildCount > 0) {
                PQNode currentNode = partialChild.fullChildAccessNode;
                do {
                    final PQNode nextNode = currentNode.fullRight;
                    partialChild.removeChild(currentNode, false);
                    this.addChild(currentNode, false);
                    currentNode.parent = this;
                    if (nextNode == currentNode) {
                        break;
                    }
                    currentNode = nextNode;
                } while (partialChild.fullChildAccessNode != null);
            }
            this.removeChild(partialChild, false);
            partialChild.delete();
        }
    }
    
    public void removeChild(final PQNode pq) {
        this.removeChild(pq, true);
    }
    
    public void removeChild(final PQNode pq, final boolean modify) {
        if (pq.isFull()) {
            --this.fullChildCount;
            if (pq.fullRight == pq) {
                pq.fullRight = null;
                pq.fullLeft = null;
                this.fullChildAccessNode = null;
            }
            else {
                if (pq == this.fullChildAccessNode) {
                    this.fullChildAccessNode = this.fullChildAccessNode.fullRight;
                }
                pq.fullRight.fullLeft = pq.fullLeft;
                pq.fullLeft.fullRight = pq.fullRight;
                pq.fullLeft = null;
                pq.fullRight = null;
            }
        }
        else if (pq.isPartial()) {
            --this.partialChildCount;
            if (pq.partialRight == pq) {
                pq.partialRight = null;
                pq.partialLeft = null;
                this.partialChildAccessNode = null;
            }
            else {
                if (pq == this.partialChildAccessNode) {
                    this.partialChildAccessNode = this.partialChildAccessNode.partialRight;
                }
                pq.partialRight.partialLeft = pq.partialLeft;
                pq.partialLeft.partialRight = pq.partialRight;
                pq.partialLeft = null;
                pq.partialRight = null;
            }
        }
        if (this.isPNode() && modify) {
            pq.parent = null;
            --this.childCount;
            if (pq == this.childAccessNode) {
                if (pq.right == pq) {
                    pq.right = null;
                    pq.left = null;
                    this.childAccessNode = null;
                }
                else {
                    this.childAccessNode = this.childAccessNode.right;
                    pq.right.left = pq.left;
                    pq.left.right = pq.right;
                    pq.left = null;
                    pq.right = null;
                }
            }
            else {
                pq.right.left = pq.left;
                pq.left.right = pq.right;
                pq.left = null;
                pq.right = null;
            }
        }
        else if (this.isQNode() && modify) {
            pq.parent = null;
            if (pq.siblings.PQNodeAt(1) == null) {
                this.endMostChildren.removePQNode(pq);
                if (pq.siblings.PQNodeAt(0) != null) {
                    if (!this.endMostChildren.contains(pq.siblings.PQNodeAt(0))) {
                        try {
                            this.endMostChildren.addPQNode(pq.siblings.PQNodeAt(0));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    pq.siblings.PQNodeAt(0).siblings.removePQNode(pq);
                    pq.siblings = null;
                }
            }
            else {
                pq.siblings.PQNodeAt(0).siblings.removePQNode(pq);
                pq.siblings.PQNodeAt(1).siblings.removePQNode(pq);
                pq.siblings = null;
            }
        }
    }
    
    public boolean replaceChild(final PQNode oldPQNode, final PQNode newPQNode) throws Exception {
        return this.replaceChild(oldPQNode, newPQNode, true);
    }
    
    public boolean replaceChild(final PQNode oldPQNode, final PQNode newPQNode, final boolean modify) throws Exception {
        if (this.isQNode()) {
            newPQNode.siblings = oldPQNode.siblings;
            if (oldPQNode.siblings.PQNodeAt(0) != null) {
                oldPQNode.siblings.PQNodeAt(0).siblings.replacePQNode(oldPQNode, newPQNode);
            }
            if (oldPQNode.siblings.PQNodeAt(1) != null) {
                oldPQNode.siblings.PQNodeAt(1).siblings.replacePQNode(oldPQNode, newPQNode);
            }
            if (this.endMostChildren.removePQNode(oldPQNode)) {
                this.endMostChildren.addPQNode(newPQNode);
            }
        }
        else {
            newPQNode.left = oldPQNode.left;
            if (oldPQNode.left != null) {
                oldPQNode.left.right = newPQNode;
            }
            newPQNode.right = oldPQNode.right;
            if (oldPQNode.right != null) {
                oldPQNode.right.left = newPQNode;
            }
        }
        (newPQNode.parent = this).removeChild(oldPQNode, false);
        this.addChild(newPQNode, false);
        if (this.childAccessNode == oldPQNode) {
            this.childAccessNode = newPQNode;
        }
        if (modify) {
            oldPQNode.fullLeft = null;
            oldPQNode.fullRight = null;
            oldPQNode.partialLeft = null;
            oldPQNode.partialRight = null;
            oldPQNode.parent = null;
            if (this.isQNode()) {
                oldPQNode.siblings = null;
            }
            else {
                oldPQNode.left = null;
                oldPQNode.right = null;
            }
        }
        return true;
    }
    
    public void replaceFullChildrenWith(final PQNode pq) throws Exception {
        if (this.isQNode()) {
            if (this.fullChildCount <= 0) {
                throw new Exception("*** ERROR Could not replace full children since none existed!");
            }
            final Vector newSiblings = new Vector();
            PQNode tempNode = this.getFullLeavesFrom();
            if (tempNode != null) {
                newSiblings.addElement(tempNode);
            }
            tempNode = this.getFullLeavesTo();
            if (tempNode != null) {
                newSiblings.addElement(tempNode);
            }
            tempNode = null;
            if (newSiblings.size() == 0) {
                (this.endMostChildren = new PQNodePair()).addPQNode(pq);
            }
            else if (newSiblings.size() == 1) {
                PQNode endNode = null;
                for (int i = 0; i < this.endMostChildren.size(); ++i) {
                    endNode = null;
                    if (this.isPseudoNode()) {
                        endNode = this.endMostChildren.PQNodeAt(i);
                        if (endNode.siblings.PQNodeAt(0) != null && endNode.siblings.PQNodeAt(0).parent != this) {
                            endNode = endNode.siblings.PQNodeAt(0);
                        }
                        else {
                            if (endNode.siblings.PQNodeAt(1) == null || endNode.siblings.PQNodeAt(1).parent == this) {
                                throw new Exception("*** ERROR could not find ends of pseudonode for checkPartialAreAtEnds!");
                            }
                            endNode = endNode.siblings.PQNodeAt(1);
                        }
                    }
                    if (this.endMostChildren.PQNodeAt(i).isFullOrDirectedFull(endNode)) {
                        this.endMostChildren.removePQNodeAt(i);
                        break;
                    }
                }
                this.endMostChildren.addPQNode(pq);
            }
            pq.siblings = new PQNodePair();
            for (int j = 0; j < newSiblings.size(); ++j) {
                tempNode = newSiblings.elementAt(j);
                if (tempNode.siblings.PQNodeAt(0) != null && tempNode.siblings.PQNodeAt(0).isFullOrDirectedFull(tempNode)) {
                    tempNode.siblings.replacePQNode(tempNode.siblings.PQNodeAt(0), pq);
                    pq.siblings.addPQNode(tempNode);
                }
                else if (tempNode.siblings.PQNodeAt(1) != null && tempNode.siblings.PQNodeAt(1).isFullOrDirectedFull(tempNode)) {
                    tempNode.siblings.replacePQNode(tempNode.siblings.PQNodeAt(1), pq);
                    pq.siblings.addPQNode(tempNode);
                }
            }
            pq.setParent(this);
            while (this.fullChildAccessNode != null) {
                tempNode = this.fullChildAccessNode;
                this.removeChild(tempNode, false);
                tempNode.label = 0;
            }
        }
    }
    
    public void becomeChild(final PQNode theChild) throws Exception {
        this.childAccessNode = theChild.childAccessNode;
        this.fullChildAccessNode = theChild.fullChildAccessNode;
        this.partialChildAccessNode = theChild.partialChildAccessNode;
        this.endMostChildren = theChild.endMostChildren;
        this.childCount = theChild.childCount;
        this.fullChildCount = theChild.fullChildCount;
        this.partialChildCount = theChild.partialChildCount;
        this.pertinentChildCount = theChild.pertinentChildCount;
        this.pertinentLeafCount = theChild.pertinentLeafCount;
        this.type = theChild.type;
        this.data = theChild.data;
        this.deleted = theChild.deleted;
        this.pseudoNode = theChild.pseudoNode;
        if (this.isPNode()) {
            throw new Exception("*** ERROR Nodes are only meant to assume the identity of one of their Q-Node children!");
        }
        if (this.isQNode()) {
            for (int i = 0; i < this.endMostChildren.size(); ++i) {
                this.endMostChildren.PQNodeAt(i).parent = this;
            }
            if (this.fullChildCount > 0) {
                PQNode currentNode = this.fullChildAccessNode;
                do {
                    final PQNode nextNode = currentNode.fullRight;
                    currentNode.parent = this;
                    if (nextNode == currentNode) {
                        break;
                    }
                    currentNode = nextNode;
                } while (currentNode != this.fullChildAccessNode);
            }
        }
        theChild.delete();
    }
    
    public void mergePartialChildren(final PQNode partialChild1, final PQNode partialChild2) throws Exception {
        if (!partialChild1.isPartial() || !partialChild2.isPartial()) {
            throw new Exception("*** ERROR merge only meant for partial children!");
        }
        final PQNodePair endMostChildren1 = partialChild1.getEndMostChildren();
        final PQNodePair endMostChildren2 = partialChild2.getEndMostChildren();
        if (endMostChildren1.size() != 2 || endMostChildren2.size() != 2) {
            throw new Exception("*** ERROR merge children were not partial!");
        }
        PQNode fullEndMostNode1 = null;
        PQNode emptyEndMostNode1 = null;
        for (int i = 0; i < endMostChildren1.size(); ++i) {
            final PQNode tempNode = endMostChildren1.PQNodeAt(i);
            if (tempNode.isFullOrDirectedFull(null)) {
                fullEndMostNode1 = tempNode;
            }
            else if (tempNode.isEmptyOrDirectedEmpty(null)) {
                emptyEndMostNode1 = tempNode;
            }
        }
        PQNode fullEndMostNode2 = null;
        PQNode emptyEndMostNode2 = null;
        for (int j = 0; j < endMostChildren2.size(); ++j) {
            final PQNode tempNode = endMostChildren2.PQNodeAt(j);
            if (tempNode.isFullOrDirectedFull(null)) {
                fullEndMostNode2 = tempNode;
            }
            else if (tempNode.isEmptyOrDirectedEmpty(null)) {
                emptyEndMostNode2 = tempNode;
            }
        }
        if (fullEndMostNode1 != null && emptyEndMostNode1 != null && fullEndMostNode2 != null && emptyEndMostNode2 != null) {
            fullEndMostNode1.parent = partialChild2;
            emptyEndMostNode1.parent = partialChild2;
            fullEndMostNode1.siblings.addPQNode(fullEndMostNode2);
            fullEndMostNode2.siblings.addPQNode(fullEndMostNode1);
            endMostChildren2.removePQNode(fullEndMostNode2);
            endMostChildren2.addPQNode(emptyEndMostNode1);
            endMostChildren1.removePQNode(fullEndMostNode1);
            endMostChildren1.removePQNode(emptyEndMostNode1);
            if (partialChild1.fullChildCount > 0) {
                PQNode currentNode = partialChild1.fullChildAccessNode;
                do {
                    final PQNode nextNode = currentNode.fullRight;
                    partialChild1.removeChild(currentNode, false);
                    partialChild2.addChild(currentNode, false);
                    currentNode.parent = partialChild2;
                    if (nextNode == currentNode) {
                        break;
                    }
                    currentNode = nextNode;
                } while (partialChild1.fullChildAccessNode != null);
            }
            this.removeChild(partialChild1);
            return;
        }
        throw new Exception("*** ERROR merge children were not partial (null)!");
    }
    
    public Vector getMaximalConsecutiveBlockedSiblings() throws Exception {
        final Vector aVector = new Vector();
        PQNode previousNode = null;
        PQNode currentNode = null;
        PQNode nextNode = null;
        for (int i = 0; i < this.siblings.size(); ++i) {
            previousNode = this;
            for (currentNode = this.siblings.PQNodeAt(i); currentNode != null && (currentNode.isBlocked() || currentNode.isDNode()); currentNode = nextNode) {
                if (!currentNode.isDNode()) {
                    aVector.addElement(currentNode);
                }
                nextNode = currentNode.siblings.otherPQNode(previousNode);
                previousNode = currentNode;
            }
        }
        return aVector;
    }
    
    public Vector getFullEndMostChildren() {
        final Vector aVector = new Vector();
        for (int i = 0; i < this.endMostChildren.size(); ++i) {
            final PQNode aNode = this.endMostChildren.PQNodeAt(i);
            if (aNode.isFull()) {
                aVector.addElement(aNode);
            }
        }
        return aVector;
    }
    
    public Vector getEmptyEndMostChildren() {
        final Vector aVector = new Vector();
        for (int i = 0; i < this.endMostChildren.size(); ++i) {
            final PQNode aNode = this.endMostChildren.PQNodeAt(i);
            if (aNode.isEmpty()) {
                aVector.addElement(aNode);
            }
        }
        return aVector;
    }
    
    public Vector getBlockedSiblings() throws Exception {
        final Vector aVector = new Vector();
        if (this.siblings != null) {
            PQNode checkNode = this.getNonDirectedSibling(this.siblings.PQNodeAt(1));
            if (checkNode != null && checkNode.isBlocked()) {
                aVector.addElement(checkNode);
            }
            checkNode = this.getNonDirectedSibling(this.siblings.PQNodeAt(0));
            if (checkNode != null && checkNode.isBlocked()) {
                aVector.addElement(checkNode);
            }
        }
        return aVector;
    }
    
    public Vector getUnblockedSiblings() throws Exception {
        final Vector aVector = new Vector();
        if (this.siblings != null) {
            PQNode checkNode = this.getNonDirectedSibling(this.siblings.PQNodeAt(1));
            if (checkNode == null) {
                checkNode = this.getEndMostDirectedSibling(this.siblings.PQNodeAt(1));
                if (checkNode != null) {
                    aVector.addElement(checkNode);
                }
            }
            else if (checkNode != null && !checkNode.isBlocked() && checkNode.parent != null && !checkNode.parent.isDeleted()) {
                aVector.addElement(checkNode);
            }
            checkNode = this.getNonDirectedSibling(this.siblings.PQNodeAt(0));
            if (checkNode == null) {
                checkNode = this.getEndMostDirectedSibling(this.siblings.PQNodeAt(0));
                if (checkNode != null) {
                    aVector.addElement(checkNode);
                }
            }
            else if (checkNode != null && !checkNode.isBlocked() && checkNode.parent != null && !checkNode.parent.isDeleted()) {
                aVector.addElement(checkNode);
            }
        }
        return aVector;
    }
    
    public PQNode getEndMostDirectedSibling(final PQNode otherSide) throws Exception {
        PQNode returnSibling = null;
        if (this.siblings.otherPQNode(otherSide) != null) {
            returnSibling = this.siblings.otherPQNode(otherSide).getEndMostDirectedSibling(this);
        }
        else if (this.isDNode()) {
            returnSibling = this;
        }
        return returnSibling;
    }
    
    public PQNode getNonDirectedSibling(final PQNode otherSide) throws Exception {
        PQNode returnSibling = null;
        if (this.siblings.otherPQNode(otherSide) != null) {
            if (!this.siblings.otherPQNode(otherSide).isDNode()) {
                returnSibling = this.siblings.otherPQNode(otherSide);
            }
            else {
                returnSibling = this.siblings.otherPQNode(otherSide).getNonDirectedSibling(this);
            }
        }
        return returnSibling;
    }
    
    public boolean checkFullAreAdjacent() throws Exception {
        if (this.fullChildCount == 0) {
            return true;
        }
        PQNode previousChild = this.fullChildAccessNode;
        int fullCount = 1;
        for (int i = 0; i < this.fullChildAccessNode.siblings.size(); ++i) {
            PQNode currentChild = this.fullChildAccessNode.siblings.PQNodeAt(i);
            previousChild = this.fullChildAccessNode;
            while (fullCount < this.fullChildCount && currentChild != null && (currentChild.isFull() || currentChild.isDNode())) {
                if (!currentChild.isDNode()) {
                    ++fullCount;
                }
                final PQNode nextChild = currentChild.siblings.otherPQNode(previousChild);
                previousChild = currentChild;
                currentChild = nextChild;
            }
        }
        return fullCount == this.fullChildCount;
    }
    
    public boolean checkFullAreAdjacentTo(final PQNode aNode) throws Exception {
        return this.fullChildCount == 0 || (this.checkFullAreAdjacent() && ((aNode.siblings.PQNodeAt(0) != null && aNode.siblings.PQNodeAt(0).isFullOrDirectedFull(aNode)) || (aNode.siblings.PQNodeAt(1) != null && aNode.siblings.PQNodeAt(1).isFullOrDirectedFull(aNode))));
    }
    
    public boolean checkFullAreEndMost() throws Exception {
        if (this.fullChildCount == 0) {
            return true;
        }
        if (!this.checkFullAreAdjacent()) {
            return false;
        }
        for (int i = 0; i < this.endMostChildren.size(); ++i) {
            if (this.isPseudoNode()) {
                PQNode endNode = this.endMostChildren.PQNodeAt(i);
                if (endNode.siblings.PQNodeAt(0) != null && endNode.siblings.PQNodeAt(0).parent != this) {
                    endNode = endNode.siblings.PQNodeAt(0);
                }
                else {
                    if (endNode.siblings.PQNodeAt(1) == null || endNode.siblings.PQNodeAt(1).parent == this) {
                        throw new Exception("*** ERROR could not find ends of pseudonode for checkPartialAreAtEnds!");
                    }
                    endNode = endNode.siblings.PQNodeAt(1);
                }
                if (this.endMostChildren.PQNodeAt(i).isFullOrDirectedFull(endNode)) {
                    return true;
                }
            }
            else if (this.endMostChildren.PQNodeAt(i).isFullOrDirectedFull(null)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkPartialAreAtEnds() throws Exception {
        if (this.partialChildCount == 0) {
            return true;
        }
        if (this.partialChildCount == 1) {
            for (int i = 0; i < this.endMostChildren.size(); ++i) {
                if (this.isPseudoNode()) {
                    PQNode endNode = this.endMostChildren.PQNodeAt(i);
                    if (endNode.siblings.PQNodeAt(0) != null && endNode.siblings.PQNodeAt(0).parent != this) {
                        endNode = endNode.siblings.PQNodeAt(0);
                    }
                    else {
                        if (endNode.siblings.PQNodeAt(1) == null || endNode.siblings.PQNodeAt(1).parent == this) {
                            throw new Exception("*** ERROR could not find ends of pseudonode for checkPartialAreAtEnds!");
                        }
                        endNode = endNode.siblings.PQNodeAt(1);
                    }
                    if (this.endMostChildren.PQNodeAt(i).isPartialOrDirectedPartial(endNode)) {
                        return true;
                    }
                }
                else if (this.endMostChildren.PQNodeAt(i).isPartialOrDirectedPartial(null)) {
                    return true;
                }
            }
            return false;
        }
        for (int i = 0; i < this.endMostChildren.size(); ++i) {
            if (this.isPseudoNode()) {
                PQNode endNode = this.endMostChildren.PQNodeAt(i);
                if (endNode.siblings.PQNodeAt(0) != null && endNode.siblings.PQNodeAt(0).parent != this) {
                    endNode = endNode.siblings.PQNodeAt(0);
                }
                else {
                    if (endNode.siblings.PQNodeAt(1) == null || endNode.siblings.PQNodeAt(1).parent == this) {
                        throw new Exception("*** ERROR could not find ends of pseudonode for checkPartialAreAtEnds!");
                    }
                    endNode = endNode.siblings.PQNodeAt(1);
                }
                if (!this.endMostChildren.PQNodeAt(i).isPartialOrDirectedPartial(endNode)) {
                    return false;
                }
            }
            else if (!this.endMostChildren.PQNodeAt(i).isPartialOrDirectedPartial(null)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkEndMostAreEmptyOrPartial() throws Exception {
        for (int i = 0; i < this.endMostChildren.size(); ++i) {
            if (this.endMostChildren.PQNodeAt(i).isFullOrDirectedFull(null)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean childrenAreFull() throws Exception {
        if (this.isQNode()) {
            int countFullEndMost = 0;
            for (int i = 0; i < this.endMostChildren.size(); ++i) {
                if (this.endMostChildren.PQNodeAt(i).isFullOrDirectedFull(null)) {
                    ++countFullEndMost;
                }
            }
            return countFullEndMost == this.endMostChildren.size() && this.checkFullAreAdjacent();
        }
        return this.fullChildCount == this.childCount;
    }
    
    public boolean hasOnlyOneChild() {
        if (this.isQNode()) {
            return this.endMostChildren.size() == 1;
        }
        return this.childCount == 1;
    }
    
    public boolean hasOnlyTwoChildren() {
        if (this.isQNode()) {
            return this.endMostChildren.size() == 2 && this.endMostChildren.PQNodeAt(0).siblings.PQNodeAt(0) == this.endMostChildren.PQNodeAt(1);
        }
        return this.childCount == 2;
    }
    
    public void clear() throws Exception {
        this.clear(true);
    }
    
    public void clear(final boolean recurse) throws Exception {
        this.labelAsEmpty();
        this.queued = false;
        this.blocked = false;
        this.pertinentChildCount = 0;
        this.pertinentLeafCount = 0;
        if (recurse && this.parent != null && (this.parent.label != 0 || this.parent.queued || this.parent.blocked || this.parent.pertinentChildCount != 0 || this.parent.pertinentLeafCount != 0 || this.parent.fullChildCount != 0 || this.parent.partialChildCount != 0)) {
            this.parent.clear();
        }
    }
    
    public String infoString() {
        String outString = new String("[");
        if (this.isQNode()) {
            outString = String.valueOf(outString) + "Q";
            if (this.isPseudoNode()) {
                outString = String.valueOf(outString) + "P";
            }
        }
        else if (this.isPNode()) {
            outString = String.valueOf(outString) + "P";
        }
        else if (this.isDNode()) {
            outString = String.valueOf(outString) + "D";
        }
        if (this.parent != null) {
            outString = String.valueOf(outString) + " p: " + this.parent.hashCode();
        }
        else {
            outString = String.valueOf(outString) + " p: null";
        }
        if (this.isDeleted()) {
            outString = String.valueOf(outString) + " DELETED]";
            return outString;
        }
        if (this.data != null) {
            outString = String.valueOf(outString) + " " + this.hashCode() + " " + this.data.toString();
        }
        else {
            outString = String.valueOf(outString) + " " + this.hashCode() + " null ";
        }
        if (this.isFull()) {
            outString = String.valueOf(outString) + " f ";
        }
        else if (this.isPartial()) {
            outString = String.valueOf(outString) + " p ";
        }
        else if (this.isEmpty()) {
            outString = String.valueOf(outString) + " e ";
        }
        outString = String.valueOf(outString) + " fc: " + this.fullChildCount;
        outString = String.valueOf(outString) + " pc: " + this.partialChildCount;
        if (this.parent != null) {
            outString = String.valueOf(outString) + " p: " + this.parent.hashCode();
            if (this.parent.isPNode()) {
                if (this.left != null) {
                    outString = String.valueOf(outString) + " l: " + this.left.hashCode();
                }
                else {
                    outString = String.valueOf(outString) + " l: " + "null";
                }
                if (this.right != null) {
                    outString = String.valueOf(outString) + " r: " + this.right.hashCode();
                }
                else {
                    outString = String.valueOf(outString) + " r: " + "null";
                }
            }
            else if (this.parent.isQNode()) {
                if (this.siblings == null) {
                    outString = String.valueOf(outString) + " siblings are null!";
                }
                else {
                    if (this.siblings.PQNodeAt(0) != null) {
                        outString = String.valueOf(outString) + " s1: " + this.siblings.PQNodeAt(0).hashCode();
                    }
                    else {
                        outString = String.valueOf(outString) + " s1: " + "null";
                    }
                    if (this.siblings.PQNodeAt(1) != null) {
                        outString = String.valueOf(outString) + " s2: " + this.siblings.PQNodeAt(1).hashCode();
                    }
                    else {
                        outString = String.valueOf(outString) + " s2: " + "null";
                    }
                }
            }
            if (this.fullLeft != null) {
                outString = String.valueOf(outString) + " fl: " + this.fullLeft.hashCode();
            }
            else {
                outString = String.valueOf(outString) + " fl: " + "null";
            }
            if (this.fullRight != null) {
                outString = String.valueOf(outString) + " fr: " + this.fullRight.hashCode();
            }
            else {
                outString = String.valueOf(outString) + " fr: " + "null";
            }
            if (this.partialLeft != null) {
                outString = String.valueOf(outString) + " pl: " + this.partialLeft.hashCode();
            }
            else {
                outString = String.valueOf(outString) + " pl: " + "null";
            }
            if (this.partialRight != null) {
                outString = String.valueOf(outString) + " pr: " + this.partialRight.hashCode();
            }
            else {
                outString = String.valueOf(outString) + " pr: " + "null";
            }
        }
        if (this.isQNode()) {
            outString = String.valueOf(outString) + " e:";
            for (int i = 0; i < this.endMostChildren.size(); ++i) {
                outString = String.valueOf(outString) + " " + this.endMostChildren.PQNodeAt(i).infoString();
            }
        }
        outString = String.valueOf(outString) + " perl: " + this.pertinentLeafCount;
        outString = String.valueOf(outString) + " perc: " + this.pertinentChildCount;
        if (this.fullChildAccessNode == null) {
            outString = String.valueOf(outString) + " fcan: null";
        }
        else {
            outString = String.valueOf(outString) + " fcan: " + this.fullChildAccessNode;
        }
        outString = String.valueOf(outString) + " " + this.queued + " " + this.blocked;
        outString = String.valueOf(outString) + "]";
        return outString;
    }
    
    public String toString() {
        String returnString = new String();
        if (this.isQNode()) {
            returnString = String.valueOf(returnString) + "Q";
        }
        else if (this.isPNode()) {
            returnString = String.valueOf(returnString) + "P";
        }
        if (this.data != null) {
            return String.valueOf(returnString) + this.data.toString();
        }
        return String.valueOf(returnString) + "Interior Node";
    }
    
    public void printStructure() throws Exception {
        System.out.print(this.infoString());
        if (this.isDeleted()) {
            System.out.println(" DELETED");
        }
        else {
            System.out.println();
            final Vector children = this.getAllChildren();
            for (int i = 0; i < children.size(); ++i) {
                children.elementAt(i).printStructure();
            }
        }
    }
    
    public int countSubLeaves(final int parent_depth) throws Exception {
        this.subLeafCount = 0;
        this.depth = parent_depth + 1;
        final int tempDepth = this.depth;
        if (this.hasChildren()) {
            final Vector children = this.getAllChildren();
            for (int i = 0; i < children.size(); ++i) {
                final PQNode childNode = children.elementAt(i);
                this.subLeafCount += childNode.countSubLeaves(tempDepth);
                if (childNode.depth > this.depth) {
                    this.depth = childNode.depth;
                }
            }
            return this.subLeafCount;
        }
        return 1;
    }
    
    public int countSubDeletedNodes() throws Exception {
        return this.countSubDeletedNodes(new Vector());
    }
    
    public int countSubDeletedNodes(final Vector deletedNodes) throws Exception {
        int subDeletedNodeCount = 0;
        if (this.hasChildren()) {
            final Vector children = this.getAllChildren();
            for (int i = 0; i < children.size(); ++i) {
                final PQNode childNode = children.elementAt(i);
                if (childNode.parent.isDeleted() && !deletedNodes.contains(childNode.parent)) {
                    deletedNodes.addElement(childNode.parent);
                    ++subDeletedNodeCount;
                }
                subDeletedNodeCount += childNode.countSubDeletedNodes(deletedNodes);
            }
        }
        return subDeletedNodeCount;
    }
    
    public int countSubNodes() throws Exception {
        int subNodeCount = 1;
        if (this.hasChildren()) {
            final Vector children = this.getAllChildren();
            for (int i = 0; i < children.size(); ++i) {
                subNodeCount += children.elementAt(i).countSubNodes();
            }
        }
        return subNodeCount;
    }
}
