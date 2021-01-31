package ilbahar.furkan.corona_geojson_service.web;

import ilbahar.furkan.corona_geojson_service.service.Covid19DataCsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wololo.geojson.FeatureCollection;

/**
 * @author Furkan İlbahar
 * created at 24.03.2020
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/covid19")
public class Covid19RestService {

    private final Covid19DataCsvParser covid19DataCsvParser;

    @Autowired
    public Covid19RestService(Covid19DataCsvParser covid19DataCsvParser) {
        this.covid19DataCsvParser = covid19DataCsvParser;
    }


    @RequestMapping(value = "/asGeojson", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getAllDataAsGeojson() {

        FeatureCollection result = covid19DataCsvParser.parseToGeojsonFeature();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
