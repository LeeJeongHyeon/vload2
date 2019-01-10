package util.category;

public class CategoryVO {

    private String imagePath;
    private String ca_name;
    private String ca_id;

    public String getCa_id() {
        return ca_id;
    }

    public CategoryVO(String imagePath, String ca_name, String ca_id) {
        this.imagePath = imagePath;
        this.ca_name = ca_name;
        this.ca_id = ca_id;
    }

    public CategoryVO(String imagePath, String ca_name) {
        this.imagePath = imagePath;
        this.ca_name = ca_name;
    }

    public String getImagePath() {
        return imagePath;
    }



    public String getCa_name() {
        return ca_name;
    }


}
