package com.munifrog.games.shapes.planar;

/**
 * Created by Karl on 5/5/2015.
 */
public class Spiral extends PlanarVertexShape {
    private int m_numLineSegments;
    private int m_numCapSegments;

    public Spiral(int numLineSegments, double thickness, double spacing, double angleMultiplier, double angleStart, double angleEnd, eFlipDirection howToFlip) {
        double [] polarMultipliers = new double[2];
        polarMultipliers[0] = spacing;
        polarMultipliers[1] = angleMultiplier;
        double angleOffset = 0;
        int numEndSegments = 1;
        computeVertices(numLineSegments, numEndSegments, thickness, polarMultipliers, angleStart, angleEnd, angleOffset, howToFlip);
    }
    public Spiral(int numLineSegments, int numEndSegments, double thickness, double spacing, double angleMultiplier, double angleStart, double angleEnd, eFlipDirection howToFlip) {
        double [] polarMultipliers = new double[2];
        polarMultipliers[0] = spacing;
        polarMultipliers[1] = angleMultiplier;
        double angleOffset = 0;
        computeVertices(numLineSegments, numEndSegments, thickness, polarMultipliers, angleStart, angleEnd, angleOffset, howToFlip);
    }
    public Spiral(int numLineSegments, int numEndSegments, double thickness, double[] polarMultipliers, double angleStart, double angleEnd, double angleOffset, eFlipDirection howToFlip) {
        computeVertices(numLineSegments, numEndSegments, thickness, polarMultipliers, angleStart, angleEnd, angleOffset, howToFlip);
    }

    private void computeVertices(
            int numLineSegments,
            int numEndSegments,
            double thickness,
            double[] polarMultipliers,
            double angleStart,
            double angleEnd,
            double angleOffset,
            eFlipDirection howToFlip)
    {
        double halfThickness = thickness / 2;
        if(numEndSegments < 1)
            numEndSegments = 1;

        double arcLength = angleEnd - angleStart;
        boolean bNegAngle = arcLength < 0;
        double aStart = bNegAngle ? angleEnd : angleStart;
        double aEnd = bNegAngle ? angleStart : angleEnd;
        if(bNegAngle)
            arcLength *= -1;

        m_numLineSegments = Math.abs(numLineSegments);
//        m_numCapSegments = numEndSegments; // allow any value in case we want to allow halfThickness changes
        m_numCapSegments = (halfThickness == 0) ? 1 : numEndSegments;

        m_numVertices = 3 * m_numLineSegments + 2 * m_numCapSegments + 1;
        m_vertices = new double[m_SHAPE_DIMENSION * m_numVertices];

        double vertiSignum = (howToFlip == eFlipDirection.eHorizontal) || (howToFlip == eFlipDirection.eOrigin) ? -1 : 1;
        double horizSignum = (howToFlip == eFlipDirection.eVertical) || (howToFlip == eFlipDirection.eOrigin) ? -1 : 1;

        double [] multipliers = polarMultipliers;
        if (multipliers == null) {
            multipliers = new double[2];
            // Circle
            multipliers[0] = 1.0;
            multipliers[1] = 1.0;
        }

        double angle;
        double radius;
        double radius2;
        double cosAngle;
        double sinAngle;
        int index;
        for (int i = 0; i <= m_numLineSegments; i++) {
            angle = aStart + i * arcLength / m_numLineSegments;
            cosAngle = Math.cos(angle);
            sinAngle = Math.sin(angle);
            radius = computeRadius(multipliers, (angle + angleOffset));

            // 1st N coordinates along central spine
            index = i * m_SHAPE_DIMENSION;
            m_vertices[index] = horizSignum * radius * cosAngle;
            m_vertices[index + 1] = vertiSignum * radius * sinAngle;

            // 2nd N coordinates along outside
            index = (m_numLineSegments + 1 + i) * m_SHAPE_DIMENSION;
            radius2 = radius + halfThickness;
            m_vertices[index] = horizSignum * radius2 * cosAngle;
            m_vertices[index + 1] = vertiSignum * radius2 * sinAngle;

            // 3rd N coordinates along inside
            index = (3 * m_numLineSegments + 1 + m_numCapSegments - i) * m_SHAPE_DIMENSION;
            radius2 = radius - halfThickness;
            m_vertices[index] = horizSignum * radius2 * cosAngle;
            m_vertices[index + 1] = vertiSignum * radius2 * sinAngle;
        }

        double deltaAngle;
        double endAngleZero = aEnd;
        double startAngleZero = aStart + Math.PI;
        int endIndex = m_numLineSegments * m_SHAPE_DIMENSION;
        for (int i = 1; i < m_numCapSegments; i++) {
            index = (2 * m_numLineSegments + 1 + i) * m_SHAPE_DIMENSION;
            deltaAngle = endAngleZero + i * Math.PI / m_numCapSegments;
            m_vertices[index] = m_vertices[endIndex] + horizSignum * halfThickness * Math.cos(deltaAngle);
            m_vertices[index + 1] = m_vertices[endIndex + 1] + vertiSignum * halfThickness * Math.sin(deltaAngle);

            index = (3 * m_numLineSegments + 1 + m_numCapSegments + i) * m_SHAPE_DIMENSION;
            deltaAngle = startAngleZero + i * Math.PI / m_numCapSegments;
            m_vertices[index] = m_vertices[0] + horizSignum * halfThickness * Math.cos(deltaAngle);
            m_vertices[index + 1] = m_vertices[1] + vertiSignum * halfThickness * Math.sin(deltaAngle);
        }
    }

    private double computeRadius(double[] polarMultipliers, double angle) {
        // Archimedes' Spiral: radius(theta) = a + b * theta
        double radius = 0;
        double angleMultiplier = 1;
        for (int i = 0; i < polarMultipliers.length; i++) {
            radius += polarMultipliers[i] * angleMultiplier;
            angleMultiplier *= angle;
        }
        return radius;
    }

    @Override
    protected void computeDrawOrder(eDrawStyle style) {
        int vertexIndex;
        int orderIndex;
        switch( style ) {
            default:
            case eSolid:
            case eWire:
                m_drawOrderSolid = new int[3 * (4 * m_numLineSegments + 2 * m_numCapSegments)];

                for (int i = 0; i < m_numLineSegments; i++) {
                    // Outer-Spine
                    vertexIndex = i;
                    orderIndex = vertexIndex * 3;
                    m_drawOrderSolid[orderIndex] = vertexIndex;
                    m_drawOrderSolid[orderIndex + 1] = m_numLineSegments + 1 + i;
                    m_drawOrderSolid[orderIndex + 2] = vertexIndex + 1;

                    // Inner-Spine
                    vertexIndex = m_numLineSegments - i;
                    orderIndex = (m_numLineSegments + i) * 3;
                    m_drawOrderSolid[orderIndex] = vertexIndex;
                    m_drawOrderSolid[orderIndex + 1] = 2 * m_numLineSegments + 1 + m_numCapSegments + i;
                    m_drawOrderSolid[orderIndex + 2] = vertexIndex - 1;

                    // Outer-Perimeter
                    vertexIndex = m_numLineSegments + 1 + i;
                    orderIndex = (2 * m_numLineSegments + i) * 3;
                    m_drawOrderSolid[orderIndex] = vertexIndex;
                    m_drawOrderSolid[orderIndex + 1] = vertexIndex + 1;
                    m_drawOrderSolid[orderIndex + 2] = 1 + i;

                    // Inner-Perimeter
                    vertexIndex = (2 * m_numLineSegments + m_numCapSegments + 1 + i);
                    orderIndex = (3 * m_numLineSegments + i) * 3;
                    m_drawOrderSolid[orderIndex] = vertexIndex;
                    m_drawOrderSolid[orderIndex + 1] = vertexIndex + 1;
                    m_drawOrderSolid[orderIndex + 2] = m_numLineSegments - 1 - i;
                }

                for (int i = 0; i < m_numCapSegments; i++) {
                    // Start cap
                    vertexIndex = 3 * m_numLineSegments + m_numCapSegments + 1 + i;
                    orderIndex = (4 * m_numLineSegments + i) * 3;
                    m_drawOrderSolid[orderIndex] = vertexIndex;
                    m_drawOrderSolid[orderIndex + 1] = vertexIndex + 1;
                    m_drawOrderSolid[orderIndex + 2] = 0;

                    // End cap
                    vertexIndex = (2 * m_numLineSegments + 1 + i);
                    orderIndex = (4 * m_numLineSegments + m_numCapSegments + i) * 3;
                    m_drawOrderSolid[orderIndex] = vertexIndex;
                    m_drawOrderSolid[orderIndex + 1] = vertexIndex + 1;
                    m_drawOrderSolid[orderIndex + 2] = m_numLineSegments;
                }
                orderIndex = (4 * m_numLineSegments + m_numCapSegments) * 3 - 2;
                m_drawOrderSolid[orderIndex] = m_numLineSegments + 1;

                m_drawOrderWire = m_drawOrderSolid;
                break;
            case eOutline:
                m_drawOrderPerimeter = new int[2 * (m_numLineSegments + m_numCapSegments) + 1];
                int firstIndex = m_numLineSegments + 1;
                int lastIndex = 3 * m_numLineSegments + 2 * m_numCapSegments + 1;
                vertexIndex = 0;
                for (int i = firstIndex; i < lastIndex; i++, vertexIndex++)
                    m_drawOrderPerimeter[vertexIndex] = i;
                m_drawOrderPerimeter[lastIndex - firstIndex] = firstIndex;
                break;
        }
    }
}
