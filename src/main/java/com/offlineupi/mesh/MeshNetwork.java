package com.offlineupi.mesh;

import java.util.ArrayList;
import java.util.List;

public class MeshNetwork {

    private final List<MeshNode> nodes = new ArrayList<>();

    public void addNode(MeshNode node) {
        nodes.add(node);
    }

    public List<MeshNode> getNodes() {
        return nodes;
    }
}