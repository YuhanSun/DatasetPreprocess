package commons;

public class Entity {
	
	public boolean IsSpatial;
	public double lon, lat;
	
	public Entity()
	{
		IsSpatial = false;
		lon = 0;
		lat = 0;
	}
	
	public Entity(double lon, double lat)
	{
		IsSpatial = true;
		this.lon = lon;
		this.lat = lat;
	}

}
