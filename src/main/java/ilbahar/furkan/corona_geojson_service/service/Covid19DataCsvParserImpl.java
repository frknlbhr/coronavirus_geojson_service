package ilbahar.furkan.corona_geojson_service.service;

import ilbahar.furkan.corona_geojson_service.util.CSVFileConstants;
import ilbahar.furkan.corona_geojson_service.util.GeometryUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Furkan İlbahar
 * created at 24.03.2020
 */

@Component
public class Covid19DataCsvParserImpl implements Covid19DataCsvParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Covid19DataCsvParserImpl.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Autowired
    private RestTemplate restTemplate;

    public FeatureCollection parseToGeojsonFeature() {

        LocalDate todayDate = LocalDate.now().minusDays(1);
        String today = formatter.format(todayDate);
        String CSV_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + today + ".csv";

        String caseCsvDatas = restTemplate.getForObject(CSV_URL, String.class);
        if (StringUtils.isEmpty(caseCsvDatas)) {
            LOGGER.error("Covid19DataCsvParserImpl : parse(),  No data received from daily report csv!");
            throw new RuntimeException("Covid19DataCsvParserImpl : parse(),  No data received from daily report csv!");
        }

        try {
            CSVParser parsedCaseCsvDatas = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(caseCsvDatas));
            List<Feature> featureList = new ArrayList<>();

            for (CSVRecord caseRecord : parsedCaseCsvDatas) {
                double longitude = Double.parseDouble(caseRecord.get(CSVFileConstants.LONGITUDE_COLUMN_HEADER));
                double latitude = Double.parseDouble(caseRecord.get(CSVFileConstants.LATITUDE_COLUMN_HEADER));
                Point point = GeometryUtils.createPoint(new Coordinate(longitude, latitude));

                org.wololo.geojson.Geometry geometry;
                if (!"0".equals(caseRecord.get(CSVFileConstants.CONFIRMED_COLUMN_HEADER))) {
                    Polygon bufferedPoint = GeometryUtils.bufferPoint(point, 5 * Double.parseDouble(caseRecord.get(CSVFileConstants.CONFIRMED_COLUMN_HEADER)));
                    geometry = JtsGeojsonHelper.convertJtsGeometryToGeoJson(bufferedPoint);
                } else {
                    geometry = JtsGeojsonHelper.convertJtsGeometryToGeoJson(point);
                }
                featureList.add(new Feature(geometry, preparePropertiesForFeature(caseRecord)));
            }
            return JtsGeojsonHelper.convertFeatureListToFeatureCollection(featureList);

        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
            throw new RuntimeException("Error during parsing CSV StringReader");
        }
    }


    private Map<String, Object> preparePropertiesForFeature(CSVRecord caseRecord) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("Country", caseRecord.get(CSVFileConstants.COUNTRY_COLUMN_HEADER));
        if (!StringUtils.isEmpty(caseRecord.get(CSVFileConstants.PROVINCE_COLUMN_HEADER))) {
            properties.put("Province/State", caseRecord.get(CSVFileConstants.PROVINCE_COLUMN_HEADER));
        }
        properties.put("Confirmed", Integer.parseInt(caseRecord.get(CSVFileConstants.CONFIRMED_COLUMN_HEADER)));
        properties.put("Deaths", Integer.parseInt(caseRecord.get(CSVFileConstants.DEATHS_COLUMN_HEADER)));
        properties.put("Recovered", Integer.parseInt(caseRecord.get(CSVFileConstants.RECOVERED_COLUMN_HEADER)));
        properties.put("Active", Integer.parseInt(caseRecord.get(CSVFileConstants.ACTIVE_COLUMN_HEADER)));
        return properties;
    }

}
