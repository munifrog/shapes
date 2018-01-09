package com.munifrog.games.shapes.planar;

/**
 * Created by Karl on 4/21/2015.
 *
 * Uses Polar Coordinates to define a heart outline.
 * Shift all coordinates up (in Cartesian) so the origin is about the center of the shape
 */
public class Heart extends PlanarVertexShape {
    //final double m_ANGLE_END = 1.35769497492; // See note to radiusFormula function
    final double m_ANGLE_END = 2.92849130172; // See note to radiusFormula function
    // These are intended to be modified only once, with the known scale factor
    double m_formula_0 =  1.0;
    double m_formula_1 =  1.3;
    double m_formula_2 = -1.5;

    public Heart(int halfNumSegments, double scaleFactor){
        m_formula_0 *= scaleFactor;
        m_formula_1 *= scaleFactor;
        m_formula_2 *= scaleFactor;
        computeVertices(halfNumSegments);
    }

    private void computeVertices(int halfNumSegments){
        m_numVertices = 2 * halfNumSegments; // Heart point, peek, and center will be common to both halves
        m_vertices = new double [m_SHAPE_DIMENSION * (m_numVertices + 1)]; // Center is always last, but point would be duplicate

        //double angleStart = 0.0;
        double angleStart = Math.PI / 2.0;
        double angleEnd = m_ANGLE_END;
        double angle;
        double radius;
        double verticalShift = -m_formula_0 / 2.0;
        int index;
        int indexMirror;

        for(int i = 1; i < halfNumSegments; i++){
            index = i * m_SHAPE_DIMENSION;
            indexMirror = m_numVertices * 2 - index;

            angle = angleStart + i * (angleEnd - angleStart) / halfNumSegments;
            radius = radiusFormula(angle);

            m_vertices[index] = radius * Math.cos(angle);
            m_vertices[index + 1] = radius * Math.sin(angle) + verticalShift;

            m_vertices[indexMirror] = -1 * m_vertices[index];
            m_vertices[indexMirror + 1] = m_vertices[index + 1];
        }

        // The first position is actually known
        m_vertices[0] = 0.0;
        m_vertices[1] = m_formula_0 + verticalShift;
        // The heart point is also known
        m_vertices[halfNumSegments * m_SHAPE_DIMENSION] = 0.0;
        m_vertices[halfNumSegments * m_SHAPE_DIMENSION + 1] = verticalShift;
        // Last point should be the center (0,0), even if it was filled earlier
        m_vertices[m_numVertices * m_SHAPE_DIMENSION] = 0.0;
        m_vertices[m_numVertices * m_SHAPE_DIMENSION + 1] = 0.0;
        m_numVertices++;
    }

    // Through trial and error I found the following polar coordinate formula to work well:
    //   radius(theta) = 2 + 2.6*(theta - pi/2) - 3.0*(theta - pi/2)^2
    // Factoring the formula determines that the function passes through zero at 2.92849130172
    private double radiusFormula(double theta){
        //double angle = theta;
        double angle = (theta - Math.PI / 2.0);
        return m_formula_0 + m_formula_1 * angle + m_formula_2 * angle * angle;
    }
}
