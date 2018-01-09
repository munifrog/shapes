package com.munifrog.simpleopengl.shapes;

/**
 * RegularPolygon - Equal length sides centered about the origin (0,0).
 **/
public abstract class Polygon {
    protected float [] mVertices2D = null;
    protected float [] mVertices3D = null;
    protected int mVertexCount = 0;

    public int getNumVertices(){ return mVertexCount; }
    void setNumVertices(int newVertexCount){ mVertexCount = newVertexCount; }

    public float [] getVertices2D(){ return mVertices2D; }
    void setVertices2D(float [] vertices2D) { mVertices2D = vertices2D; }

    public void setVertices3D(float [] vertices3D) { mVertices3D = vertices3D; }
    public float [] getVertices3D(){
        if(mVertices3D == null) {
            compute3DVertices();
        }
        return mVertices3D;
    }

    private void compute3DVertices() {
        mVertices3D = new float [3 * mVertexCount];
        int index2D, index3D;
        for(int i = 0; i < mVertexCount; i++) {
            index2D = 2 * i;
            index3D = 3 * i;
            mVertices3D[index3D]     = mVertices2D[index2D];
            mVertices3D[index3D + 1] = mVertices2D[index2D + 1];
            mVertices3D[index3D + 2] = 0.0f;
        }
    }

    public abstract int [] getDrawOrderWire();
    public abstract int [] getDrawOrderSolid();
    public abstract int [] getDrawOrderOutline();
}
