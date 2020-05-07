package ip;

public class GeoIpBean {

    private String country;
    private String province;
    private String postcode;//邮编
    private String longitude;//经度
    private String latitude;//纬度
    private String ipIsp;//运营商
    private String cityname;
    private String continentCnName;
    private String timezonecity;//城市
    private String timezone;
    private String countryCode;//国家代码
    private String provinceCode;//国家代码

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

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIpIsp() {
        return ipIsp;
    }

    public void setIpIsp(String ipIsp) {
        this.ipIsp = ipIsp;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getContinentCnName() {
        return continentCnName;
    }

    public void setContinentCnName(String continentCnName) {
        this.continentCnName = continentCnName;
    }

    public String getTimezonecity() {
        return timezonecity;
    }

    public void setTimezonecity(String timezonecity) {
        this.timezonecity = timezonecity;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "GeoIpBean{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", postcode='" + postcode + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", ipIsp='" + ipIsp + '\'' +
                ", cityname='" + cityname + '\'' +
                ", continentCnName='" + continentCnName + '\'' +
                ", timezonecity='" + timezonecity + '\'' +
                ", timezone='" + timezone + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
