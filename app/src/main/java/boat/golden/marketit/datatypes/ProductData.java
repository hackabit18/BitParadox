package boat.golden.marketit.datatypes;


public class ProductData
{
   public String product_name,product_desc,product_type,pic_add;
    public String price;

    public ProductData()
    {

    }
    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public String getProduct_type() {
        return product_type;
    }

    public String getPic_add() {
        return pic_add;
    }

    public String getPrice() {
        return price;
    }

    public ProductData(String product_name, String product_desc, String product_type, String pic_add, String price) {
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_type = product_type;
        this.pic_add = pic_add;
        this.price = price;
    }
}
