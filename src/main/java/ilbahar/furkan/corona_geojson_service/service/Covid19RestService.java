package ilbahar.furkan.corona_geojson_service.service;

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
@RequestMapping("/Covid19Service")
public class Covid19RestService {

    @Autowired
    private Covid19DataCsvParser covid19DataCsvParser;

    @RequestMapping(value = "/allAsGeojson", method = RequestMethod.GET)
    public ResponseEntity<FeatureCollection> getAllDataAsGeojson() {

        FeatureCollection result = covid19DataCsvParser.parseToGeojsonFeature();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
