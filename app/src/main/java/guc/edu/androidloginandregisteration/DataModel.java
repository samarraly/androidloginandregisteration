package guc.edu.androidloginandregisteration;

public class DataModel {
    private String carname, fuellevel, production_year, imgURL,latitude,longitude;
    private String id;
    private float distance;


    public String getImgURL(){
        return imgURL;
    }

    public void setId(String id) {
        this.id = id;
    }

//
   public String getId() {
        return id;
    }


    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }

    public String getName()
    {
        return carname;
    }

    public void setName(String carname)
    {
        this.carname = carname;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public String getFuellevel()
    {
        return fuellevel;
    }

    public void setFuellevel(String fuellevel)
    {
        this.fuellevel = fuellevel;
    }

    public String getProduction_year()
    {
        return production_year;
    }

    public void setProduction_year(String production_year)
    {
        this.production_year = production_year;
    }

    public float getdistance ()
    {
        return distance;

    }

    public void setdistance(float distance)
    {
        this.distance=distance;
    }
}
