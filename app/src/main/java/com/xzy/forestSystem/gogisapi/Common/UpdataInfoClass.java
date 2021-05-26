package  com.xzy.forestSystem.gogisapi.Common;

public class UpdataInfoClass {
    private String description;
    private String url;
    private int verionNumber = 0;
    private String version;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version2) {
        this.version = version2;
        try {
            this.verionNumber = Integer.parseInt(version2.replaceAll("\\.", "").replaceAll("V", ""));
        } catch (Exception e) {
        }
    }

    public int getVerionNumber() {
        return this.verionNumber;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }
}
