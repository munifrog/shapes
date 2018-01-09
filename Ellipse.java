package com.munifrog.games.shapes.planar;

/**
 * Created by Karl on 4/4/2015.
 *
 * Ellipse is just a circle with scaled coordinates
 */
public class Ellipse extends PlanarVertexShape{
    public Ellipse(){}
    public Ellipse(int numVertices, double horizontal, double vertical){
        int numVs = numVertices; // make a copy
        if( numVs < 3 )
            numVs = 3;
        RegularPolygon circle = new RegularPolygon(numVs);
        m_vertices = circle.getVertices().clone();
        m_numVertices = m_vertices.length / 2;
        scaleSides(horizontal, vertical);
    }

    protected void scaleSides(double horizontal, double vertical){
        // Allow any value for horizontal and vertical. If zero it becomes a line
        // And if negative it will just flip the coordinate.
        // Cannot assume unit anything for an ellipse, so do strict scaling
        int index = 0;
        int max = m_numVertices - 1; // Center (0,0) is not scaled, but it could be
        for(int i = 0; i < max; i++){
            index = i * m_SHAPE_DIMENSION;
            m_vertices[index] *= horizontal;
            m_vertices[index + 1] *= vertical;
        }
    }
}
