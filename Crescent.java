package com.munifrog.games.shapes.planar;

/**
 * Created by Karl on 4/27/2015.
 */
public class Crescent extends PlanarVertexShape{
    public Crescent(int halfNumVertices, double vertical, double horizontalLeft, double horizontalRight){
        int numVs = (halfNumVertices * 2); // make a copy
        if( numVs < 2 )
            numVs = 2;
        RegularPolygon circle = new RegularPolygon(numVs);
        m_vertices = circle.getVertices().clone();
        m_numVertices = m_vertices.length / 2;
        scaleSides(vertical, horizontalLeft, horizontalRight);
    }

    private void scaleSides(double vertical, double horizontalLeft, double horizontalRight){
        // Allow any value for horizontal and vertical. If zero it becomes a line
        // And if negative it will just flip the coordinate.
        // Cannot assume unit anything for an crescent, so do strict scaling
        int index = 0;
        int max = (m_numVertices - 1) / 2; // Center (0,0) is not scaled, but it could be
        for(int i = 0; i < max; i++){
            index = i * m_SHAPE_DIMENSION;
            m_vertices[index] *= horizontalRight;
            m_vertices[index + 1] *= vertical;
            index = (max * m_SHAPE_DIMENSION) + index;
            m_vertices[index] *= horizontalLeft;
            m_vertices[index + 1] *= vertical;
        }
    }

    @Override
    protected void computeDrawOrder(eDrawStyle style) {
        switch (style) {
            default:
            case eSolid:
                m_drawOrderSolid = computeDrawOrderSolid();
                break;
            case eWire: // drawn same as solid
                m_drawOrderWire = computeDrawOrderSolid();
                break;
            case eOutline: // Perimeter
                m_drawOrderPerimeter = computeDrawOrderPerimeter();
                break;
        }
    }

    private int [] computeDrawOrderSolid() {
        // Assumes the last vertex is the center, and to be left out
        int numSides = m_numVertices - 1;
        int[] drawOrder = new int[(numSides - 2) * 3];

        int index = 0;
        int halfIndex = numSides / 2;

        // Top - single triangle
        drawOrder[0] = 0;
        drawOrder[1] = 1;
        drawOrder[2] = numSides - 1;

        // Quadrilateral section - left side
        for (int i = 1; i < halfIndex - 1; i++) {
            index = i * (m_SHAPE_DIMENSION + 1);
            drawOrder[index] = i;
            drawOrder[index + 1] = i + 1;
            drawOrder[index + 2] = numSides - i;
        }

        // Bottom - single triangle
        index = (m_SHAPE_DIMENSION + 1) * (halfIndex - 1);
        drawOrder[index] = halfIndex - 1;
        drawOrder[index + 1] = halfIndex;
        drawOrder[index + 2] = halfIndex + 1;

        // Quadrilateral section - right side
        for (int i = halfIndex; i < numSides - 2; i++) {
            index = i * (m_SHAPE_DIMENSION + 1);
            drawOrder[index] = i + 1;
            drawOrder[index + 1] = i + 2;
            drawOrder[index + 2] = numSides - 1 - i;
        }

        return drawOrder;
    }

    private int [] computeDrawOrderPerimeter() {
        // Assumes the last vertex is the center, and to be left out
        int [] drawOrder = new int[m_numVertices];
        for( int i = 0; i < m_numVertices - 1; i++)
            drawOrder[i] = i;
        drawOrder[m_numVertices - 1] = 0;
        return drawOrder;
    }
}

