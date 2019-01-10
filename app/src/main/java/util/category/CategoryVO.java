package util.category;

public class CategoryVO {

    private String imagePath;
    private String ca_name;

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
