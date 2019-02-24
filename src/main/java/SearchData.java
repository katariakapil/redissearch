public class SearchData {


    String id;

    public String getId() {
        return id;
    }

    public SearchData() {
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String keyword;
    String category;

    public SearchData(String id, String keyword, String category) {
        this.id = id;
        this.keyword = keyword;
        this.category = category;
    }

}
