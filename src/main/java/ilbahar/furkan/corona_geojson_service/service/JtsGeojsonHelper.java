package ilbahar.furkan.corona_geojson_service.service;

import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.util.List;

/**
 * @author Furkan İlbahar
 * created at 24.03.2020
 */
public class JtsGeojsonHelper {

    public static org.wololo.geojson.Geometry convertJtsGeometryToGeoJson(Geometry geometry) {
        return new GeoJSONWriter().write(geometry);
    }

    public static Geometry convertGeoJsonToJtsGeometry(org.wololo.geojson.Geometry geoJson) {
        return new GeoJSONReader().read(geoJson);
    }

    public static FeatureCollection convertFeatureListToFeatureCollection(List<Feature> featureList) {
        return new FeatureCollection(featureList.toArray(new Feature[0]));
    }

}
