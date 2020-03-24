package ilbahar.furkan.corona_geojson_service.service;

import org.wololo.geojson.FeatureCollection;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Furkan İlbahar
 * created at 24.03.2020
 */

public interface Covid19DataCsvParser extends Serializable {

    FeatureCollection parseToGeojsonFeature();

}
