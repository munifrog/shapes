# shapes
# These shapes are intended for being drawn using OpenGL.
# Each shape is composed of vertices and their coordinates.
# When the shapes is to be drawn, the shape must also specify the draw order.
#
# For example, a regular polygon has vertices equally spread out on a unit circle.
# To be drawn using triangles, there must also be a vertex at the center of the polygon.
# When drawing as triangles, each set of three coordinates will include this center point.
# When drawing just the outline of the polygon, the center is omitted and the outer vertices
# drawn in order.
#
# Look to Polygon and RegularPolygon for contributing.
# The other shapes are accurate at locating coordinates and their draw order but need trimming.
#

