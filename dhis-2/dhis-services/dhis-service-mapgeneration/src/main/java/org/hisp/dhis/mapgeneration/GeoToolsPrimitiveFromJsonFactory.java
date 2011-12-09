package org.hisp.dhis.mapgeneration;

import net.sf.json.JSONException;

import org.codehaus.jackson.JsonNode;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Factory for producing GeoTools geometric primitives from coordinates in json.
 * 
 * @author Olai Solheim <olais@ifi.uio.no>
 */
public class GeoToolsPrimitiveFromJsonFactory
{
    // Factory creating GeoTools geometric primitives
    private static final GeometryFactory factory = JTSFactoryFinder.getGeometryFactory( null );

    /**
     * Create a GeoTools geometric point primitive from coordinates in json.
     * 
     * @param json the json array of components
     * @return the point
     */
    public static Point createPointFromJson( JsonNode json )
    {
        return factory.createPoint( createCoordinateFromJson( json ) );
    }

    /**
     * Create a GeoTools geometric coordinate primitive from coordinates in
     * json.
     * 
     * @param json the json array of components
     * @return the coordinate
     */
    public static Coordinate createCoordinateFromJson( JsonNode json )
    {

        // Parse the double values from the json and create the coordinate
        return new Coordinate( json.get( 0 ).getValueAsDouble(), json.get( 1 ).getValueAsDouble() );
    }

    /**
     * Create a GeoTools geometric multi-polygon primitive from coordinates in
     * json.
     * 
     * @param json the json array of polygons
     * @return the multi-polygon
     * @throws JSONException
     */
    public static MultiPolygon createMultiPolygonFromJson( JsonNode json )
    {
        // Native array of polygons to pass to GeoFactory
        Polygon[] polygons = new Polygon[json.size()];

        // Read all the polygons from the json array
        for ( int i = 0; i < json.size(); i++ )
        {
            polygons[i] = createPolygonFromJson( json.get( i ) );
        }

        // Create the multi-polygon from factory
        return factory.createMultiPolygon( polygons );
    }

    /**
     * Create a GeoTools geometric polygon primitive from coordinates in json.
     * 
     * @param json the json array of linear ring
     * @return the polygon
     */
    public static Polygon createPolygonFromJson( JsonNode json )
    {

        // Get the json array of coordinates representing the shell and make a
        // linear-ring out of them
        JsonNode shell = json.get( 0 );
        LinearRing sh = createLinearRingFromJson( shell );

        // Native array of linear-ring holes to pass to GeoFactory
        LinearRing[] holes = null;

        // Get the linear-ring holes if the polygon has any holes
        if ( json.size() > 1 )
        {
            // Allocate memory for the holes, i.e. minus the shell
            holes = new LinearRing[shell.size() - 1];

            // Read the json array of linear-ring into holes
            for ( int i = 1; i < shell.size(); i++ )
            {
                JsonNode hole = json.get( i );
                holes[i] = createLinearRingFromJson( hole );
            }
        }

        // Create the polygon from factory
        return factory.createPolygon( sh, holes );
    }

    /**
     * Create a GeoTools geometric linear-ring from coordinates in json.
     * 
     * @param json the json array of coordinates
     * @return the linear-ring
     */
    public static LinearRing createLinearRingFromJson( JsonNode json )
    {
        // Native array of coordinates to pass to GeoFactory
        Coordinate[] coords = new Coordinate[json.size()];

        // Read the json array of coordinates
        for ( int i = 0; i < json.size(); i++ )
        {
            coords[i] = createCoordinateFromJson( json.get( i ) );
        }

        // Create the linear-ring from factory
        return factory.createLinearRing( coords );
    }
}
