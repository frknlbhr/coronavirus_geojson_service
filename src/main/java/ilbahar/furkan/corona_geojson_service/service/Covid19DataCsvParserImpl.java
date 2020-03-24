package ilbahar.furkan.corona_geojson_service.service;

import ilbahar.furkan.corona_geojson_service.util.CSVFileConstants;
import ilbahar.furkan.corona_geojson_service.util.GeometryUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
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

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    private RestTemplate restTemplate;

    public FeatureCollection parseToGeojsonFeature() {

        LocalDate todayDate = LocalDate.now();
        String today = formatter.format(todayDate);
        String CSV_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + today + ".csv";

        String caseCsvDatas = restTemplate.getForObject(CSV_URL, String.class);
        if (StringUtils.isEmpty(caseCsvDatas)) throw new RuntimeException("Covid19DataCsvParserImpl : parse(),  No data received from daily report csv!");

        try {
            CSVParser parsedCaseCsvDatas = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(caseCsvDatas));
            List<Feature> featureList = new ArrayList<>();

            for (CSVRecord caseRecord : parsedCaseCsvDatas) {
                double longitude = Double.parseDouble(caseRecord.get(CSVFileConstants.LONGITUDE_COLUMN_HEADER));
                double latitude = Double.parseDouble(caseRecord.get(CSVFileConstants.LATITUDE_COLUMN_HEADER));
                Point point = GeometryUtils.createPoint(new Coordinate(longitude, latitude));
                Polygon bufferedPoint = GeometryUtils.bufferPoint(point, Double.parseDouble(caseRecord.get(today)));

                org.wololo.geojson.Geometry geometry = JtsGeojsonHelper.convertJtsGeometryToGeoJson(bufferedPoint);
                featureList.add(new Feature(geometry, preparePropertiesForFeature(caseRecord)));
            }
            return JtsGeojsonHelper.convertFeatureListToFeatureCollection(featureList);

        } catch (IOException e) {
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
