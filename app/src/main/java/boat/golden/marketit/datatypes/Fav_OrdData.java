package boat.golden.marketit.datatypes;

public class Fav_OrdData {
   ProductData productData;
   String favUid;

    public Fav_OrdData() {
    }


    public Fav_OrdData(ProductData productData, String favUid) {
        this.productData = productData;
        this.favUid = favUid;
    }

    public ProductData getProductData() {
        return productData;
    }

    public String getFavUid() {
        return favUid;
    }
}
