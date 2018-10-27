package boat.golden.marketit.datatypes;

/**
 * Created by Vipin soni on 01-10-2018.
 */

public class ShopData {
    String shop_name,pic_add,shop_id,short_desc;
    float lat,lon;

    public ShopData(String shop_name, String pic_add, String shop_id, String shop_type, String tags, String short_desc, float lat, float lon, String type) {
        this.shop_name = shop_name;
        this.pic_add = pic_add;
        this.shop_id = shop_id;
        this.short_desc = short_desc;
        this.lat = lat;
        this.lon = lon;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getPic_add() {
        return pic_add;
    }

    public String getShop_id() {
        return shop_id;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }
}
