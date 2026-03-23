/*
    Creme de la creme of system design, apparantly. 
    Where to use? simple, where there is object initiation just replace it with factory call. 
    But the question is, would you create a Factory to create a Factory? Its a joke, dont take it literally, unless.
    Use it when you call fresh uncontaminated object on multiple spaces so wrapping it would save you future refactoring
 */

import java.util.ArrayList;
import java.util.List;

public class Factory {
    public static void main(String[] args) {
        Mesh m1 = MeshFactory.createMesh(MeshType.CUBE);
        m1.printVolume();
        Mesh m2 = MeshFactory.createMesh(MeshType.PYRAMID);
        m2.printVolume();
        Mesh m3 = MeshFactory.createMesh(MeshType.POINT);
        m3.printVolume();
        Mesh m4 = MeshFactory.createMesh(null);
        m4.printVolume();
    }
}

class MeshFactory {
    public static Mesh createMesh(MeshType type) {

        if (type == null) {
            return new Mesh(); 
        }

        switch (type) {
            case CUBE:
                return new Cube();
            case PYRAMID:
                return new Pyramid();
            case POINT:
                return new Point();
            default:
                return new Mesh();
        }
    }
}

class Mesh {
    public List<Vertex> vertices;
    public List<Edge> edges;
    public List<Face> faces;

    protected Mesh() {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.faces = new ArrayList<>();
    }

    public final void addVertex(double x, double y, double z) {
        Vertex v = new Vertex(x, y, z);
        this.vertices.add(v);
    }

    public final void addEdge(Integer vi1, Integer vi2) {
        if (vi1 < this.vertices.size() && vi2 < this.vertices.size()) {
            Vertex v1 = this.vertices.get(vi1);
            Vertex v2 = this.vertices.get(vi2);
            Edge e = new Edge(v1, v2);
            this.edges.add(e);
        }
    }

    public final void addFace(Integer... vertexIndices) {
        List<Vertex> faceVerts = new ArrayList<>();
        for (Integer index: vertexIndices) {
            if (index < this.vertices.size()) {
                faceVerts.add(this.vertices.get(index));
            } else {
                return;
            }
        }
        Face f = new Face(faceVerts);
        this.faces.add(f);
    }

    public void printVolume() {
        System.out.println("Volume of custom mesh is ∞");
    }
}

class Vertex {
    public double x, y, z;

    public Vertex(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

class Edge {
    public Vertex v1, v2;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
}

class Face {
    public List<Vertex> faceVertices;

    public Face(List<Vertex> faceVertices) {
        this.faceVertices = faceVertices;
    }
}

enum MeshType {
    CUBE,
    PYRAMID,
    POINT
}

class Cube extends Mesh {
    protected Cube() {
        addVertex(0,0,0); 
        addVertex(1,0,0); 
        addVertex(1,1,0); 
        addVertex(0,1,0);
        addVertex(0,0,1); 
        addVertex(1,0,1); 
        addVertex(1,1,1); 
        addVertex(0,1,1);
        addEdge(0, 1); 
        addEdge(1, 2); 
        addEdge(2, 3); 
        addEdge(3, 0);
        addEdge(4, 5); 
        addEdge(5, 6); 
        addEdge(6, 7); 
        addEdge(7, 4);
        addEdge(0, 4); 
        addEdge(1, 5); 
        addEdge(2, 6); 
        addEdge(3, 7);
        addFace(0, 1, 2, 3);
        addFace(4, 5, 6, 7);
        addFace(0, 1, 5, 4);
        addFace(1, 2, 6, 5);
        addFace(2, 3, 7, 6);
        addFace(3, 0, 4, 7);
    }

    @Override
    public void printVolume() {
        System.out.println("Volume of cube is 1");
    }
}

class Pyramid extends Mesh {
    protected Pyramid() {
        addVertex(0, 0, 0);
        addVertex(1, 0, 0);
        addVertex(1, 1, 0);
        addVertex(0, 1, 0);
        addVertex(0.5, 0.5, 1);
        addEdge(0, 1); 
        addEdge(1, 2); 
        addEdge(2, 3); 
        addEdge(3, 0);
        addEdge(0, 4); 
        addEdge(1, 4); 
        addEdge(2, 4); 
        addEdge(3, 4);
        addFace(0, 1, 2, 3); 
        addFace(0, 1, 4); 
        addFace(1, 2, 4);
        addFace(2, 3, 4);
        addFace(3, 0, 4);
    }

    @Override
    public void printVolume() {
        System.out.println("Volume of pyramid is 0.33");
    }
}

class Point extends Mesh {
    protected Point() {
        addVertex(0, 0, 0);
    }

    @Override
    public void printVolume() {
        System.out.println("Volume of point is 0");
    }
}