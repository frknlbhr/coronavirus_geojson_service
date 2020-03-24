package ilbahar.furkan.corona_geojson_service.util;

import org.locationtech.jts.geom.*;

/**
 * @author Furkan İlbahar
 * created at 24.03.2020
 */
public class GeometryUtils {

    public static final double ONE_DEGREE_LATITUDE_DISTANCE_KM = 111.32d;
    public static final double ONE_DEGREE_LATITUDE_DISTANCE_M = ONE_DEGREE_LATITUDE_DISTANCE_KM * 1000d;
    public static final double ONE_DEGREE_LONGITUDE_DISTANCE_AT_EQUATOR_KM = 111.321d;
    public static final double ONE_DEGREE_LONGITUDE_DISTANCE_AT_EQUATOR_M = ONE_DEGREE_LONGITUDE_DISTANCE_AT_EQUATOR_KM * 1000d;

    /**
     * Creates JTS GeometryFactory with configuration
     * @return GeometryFactory object
     */
    private static GeometryFactory geometryFactoryBuilder() {
        return new GeometryFactory(new PrecisionModel(), 4326);
    }

    /**
     * Creates Point from given coordinate
     * @param coordinate Coordinate
     * @return Point
     */
    public static Point createPoint(Coordinate coordinate) {
        GeometryFactory geometryFactory = geometryFactoryBuilder();
        return geometryFactory.createPoint(coordinate);
    }

    /**
     * Creates a buffer around the point with the given distance
     * @param point Point to be buffered
     * @param distance distance
     * @return Polygon geometry
     */
    public static Polygon bufferPoint(Point point, double distance) {

        return (Polygon) point.buffer(distance / GeometryUtils.ONE_DEGREE_LATITUDE_DISTANCE_M);
    }

}
