package com.offlineupi.mesh;

public class MeshNode {

    private String nodeId;
    private String nodeName;

    public MeshNode(String nodeId, String nodeName) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }
}