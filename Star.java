package com.munifrog.games.shapes.planar;

/**
 * Created by Karl on 4/21/2015.
 *
 * A star is a regular polygon with a large and small radius.
 * The polygons are offset so the point lies halfway between the arms (vertices)
 */
public class Star extends PlanarVertexShape {
    public Star(int numArms, double radiusA, double radiusB){
        computeCoordinates(numArms, radiusA, radiusB);
    }
    public Star(int numArms, double radiusA, int numArmsToSkip){
        double radiusB = radiusA * getSmallRadiusRatio(numArms, numArmsToSkip);
        computeCoordinates(numArms, radiusA, radiusB);
    }

    private double getSmallRadiusRatio(int numArms, int numArmsToSkip){
        int numSkipped = numArmsToSkip;
        int maxSkips = ( numArms % 2 == 0 ) ? numArms - 1 : numArms;
        maxSkips = (int)(Math.floor(maxSkips / 2.0) - 1);
        if( numSkipped > maxSkips)
            numSkipped = maxSkips;
        if( numSkipped < 1 )
            numSkipped = 1;

        double alpha = Math.PI / numArms;
        double gamma = (Math.PI - 2 * alpha * (numSkipped + 1)) / 2;
        return Math.sin(gamma) / Math.sin( Math.PI - alpha - gamma);
    }

    private void computeCoordinates(int numArms, double radiusA, double radiusB){
        m_numVertices = 2 * numArms;
        // Last index is for the center (0,0)
        m_vertices = new double[m_SHAPE_DIMENSION * (m_numVertices + 1)];
        // Generate coordinates in counter-clockwise order
        double angle = 0.0;
        double radius = radiusA;
        int index = 0;
        for(int i = 0; i < m_numVertices; i++){
            index = i * m_SHAPE_DIMENSION;
            angle = (i * Math.PI) / numArms;
            radius = ((i % 2) == 0) ? radiusA : radiusB;
            m_vertices[index] = Math.sin(angle) * radius;
            m_vertices[index + 1] = Math.cos(angle) * radius;
        }
        m_vertices[m_numVertices * m_SHAPE_DIMENSION] = 0.0;
        m_vertices[m_numVertices * m_SHAPE_DIMENSION + 1] = 0.0;
        m_numVertices++;
    }
}
