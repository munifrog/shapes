package com.munifrog.simpleopengl.shapes;

/**
 * RegularPolygon - Equal length sides centered about the origin (0,0).
 **/
public class RegularPolygon extends Polygon {
    private float mRadius;
    private int [] mDrawOrderSolid = null;
    private int [] mDrawOrderPerimeter = null;

    public RegularPolygon(int numVertices){ this(numVertices, 1.0f); }
	public RegularPolygon(int numVertices, float radius) {
        // Store the number of vertices and centerToVertex
        mRadius = radius;
        mVertexCount = numVertices;

        // Lines (2) and points (1) are not polygons
        if (mVertexCount < 3)
            mVertexCount = 3;

        // Figure out the coordinates upon generation
        computeCoordinates();
    }

    private void computeCoordinates(){
        // Generate coordinates in counter-clockwise order
        float [] newVertices = new float[2 * (mVertexCount + 1)];
        // Create a unit circle
        double angle;
        int index;
        for(int i = 0; i < mVertexCount; i++){
            index = i * 2;
            // Take care of two indices at a time
            // Calculate each vertex from zero rather than incrementally
            angle = (i * 2.0 * Math.PI) / mVertexCount;
            // x
            newVertices[index] = (float)(mRadius * Math.sin(angle));
            // y
            newVertices[index + 1] = (float)(mRadius * Math.cos(angle));
        }
        // The last index holds the center coordinate
        newVertices[mVertexCount * 2] = 0.0f;
        newVertices[mVertexCount * 2 + 1] = 0.0f;
        mVertexCount++;

        setVertices2D(newVertices);
    }

    @Override
    public int [] getDrawOrderWire() { return getDrawOrderSolid(); }
    @Override
    public int [] getDrawOrderSolid() {
        if(mDrawOrderSolid == null) {
            mDrawOrderSolid = computeDrawOrderSolid();
        }
        return mDrawOrderSolid.clone();
    }
    @Override
    public int [] getDrawOrderOutline() {
        if(mDrawOrderPerimeter == null) {
            mDrawOrderPerimeter = computeDrawOrderPerimeter();
        }
        return mDrawOrderPerimeter.clone();
    }

    private int [] computeDrawOrderSolid() {
        // Assumes the last vertex is the center, and to be left out
        int numSides = mVertexCount - 1;
        int[] drawOrder = new int[numSides * 3];
        int index = 0;
        for (int i = 0; i < numSides; i++) {
            index = i * 3;
            drawOrder[index] = i;
            drawOrder[index + 1] = i + 1;
            drawOrder[index + 2] = numSides; // Third vertex always center
        }
        // Fix the 2nd to last coordinate; needs to use first, not last
        drawOrder[(numSides * 3) - 2] = 0;
        return drawOrder;
    }

    private int [] computeDrawOrderPerimeter() {
        // Assumes the last vertex is the center, and to be left out
        int [] drawOrder = new int[mVertexCount];
        for( int i = 0; i < mVertexCount - 1; i++)
            drawOrder[i] = i;
        drawOrder[mVertexCount - 1] = 0;
        return drawOrder;
    }

}
