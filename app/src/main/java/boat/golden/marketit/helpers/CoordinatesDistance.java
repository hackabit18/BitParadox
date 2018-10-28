package boat.golden.marketit.helpers;

public class CoordinatesDistance {
    public double degreesToRadians(float degrees) {
        return degrees * Math.PI / 180;
    }

    public Boolean verify(float lat1,float lon1,float lat2,float lon2,float comparison) {
        float earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = (float) degreesToRadians(lat1);
        lat2 = (float) degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        if (earthRadiusKm*c<comparison)return true;
        return false;
    }

}
