// 
// Decompiled by Procyon v0.5.36
// 

package operation.extenders;

import graphStructure.NodeExtender;

public class CanNodeEx extends NodeExtender
{
    protected int canonicalNumber;
    protected int outerFaceEdgeCount;
    protected boolean isOnOuterFace;
    protected CanNodeEx candidateLeft;
    protected CanNodeEx candidateRight;
    
    public CanNodeEx() {
        this.canonicalNumber = -1;
    }
    
    public void setCanonicalNumber(final int canNum) {
        this.canonicalNumber = canNum;
    }
    
    public int getCanonicalNumber() {
        return this.canonicalNumber;
    }
    
    public void setOuterFaceEdgeCount(final int count) {
        this.outerFaceEdgeCount = count;
    }
    
    public int getOuterFaceEdgeCount() {
        return this.outerFaceEdgeCount;
    }
    
    public void incrementOuterFaceEdgeCount() {
        ++this.outerFaceEdgeCount;
    }
    
    public void decrementOuterFaceEdgeCount() {
        --this.outerFaceEdgeCount;
    }
    
    public void setIsOnOuterFace(final boolean isOn) {
        this.isOnOuterFace = isOn;
    }
    
    public boolean isOnOuterFace() {
        return this.isOnOuterFace;
    }
    
    public CanNodeEx getCandidateLeft() {
        return this.candidateLeft;
    }
    
    public void setCandidateLeft(final CanNodeEx cand) {
        this.candidateLeft = cand;
    }
    
    public CanNodeEx getCandidateRight() {
        return this.candidateRight;
    }
    
    public void setCandidateRight(final CanNodeEx cand) {
        this.candidateRight = cand;
    }
}
