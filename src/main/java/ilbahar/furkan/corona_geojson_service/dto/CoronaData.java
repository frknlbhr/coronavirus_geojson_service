package ilbahar.furkan.corona_geojson_service.dto;

import org.locationtech.jts.geom.Geometry;
import org.wololo.geojson.Feature;

import java.io.Serializable;

/**
 * @author Furkan İlbahar
 * created at 24.03.2020
 */
public class CoronaData implements Serializable {

    private static final long serialVersionUID = 4L;

    private String country;
    private String province;
    private int caseCount;
    private int deathCount;
    private Geometry bufferedGeomOfCountry;

    public CoronaData() {
    }

    public CoronaData(String country, String province, int caseCount, int deathCount, Geometry bufferedGeomOfCountry) {
        this.country = country;
        this.province = province;
        this.caseCount = caseCount;
        this.deathCount = deathCount;
        this.bufferedGeomOfCountry = bufferedGeomOfCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public Geometry getBufferedGeomOfCountry() {
        return bufferedGeomOfCountry;
    }

    public void setBufferedGeomOfCountry(Geometry bufferedGeomOfCountry) {
        this.bufferedGeomOfCountry = bufferedGeomOfCountry;
    }
}
