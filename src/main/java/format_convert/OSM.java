package format_convert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import commons.Entity;
import commons.OwnMethods;

public class OSM {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dataset = "foursquare";
		String entity_path = "D:\\Ubuntu_shared\\GeoMinHop\\data\\"+dataset+"\\entity.txt";
//		String csv_path = "D:\\Ubuntu_shared\\GeoMinHop\\data\\"+dataset+"\\entity_csv.txt";
		String osm_path = "D:\\Ubuntu_shared\\GeoMinHop\\data\\"+dataset+"\\entity.osm";
//		EntityToCSV(entity_path, csv_path);
		EntityToOSM(entity_path, osm_path);
		
//		OwnMethods.Print(OwnMethods.ReadFile(osm_path, 5));
		
//		long time = 1280698563000L;
//		OwnMethods.Print(new Date(time));
	}
	
	public static void EntityToOSM(String entity_path, String osm_path)
	{
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(osm_path);
			String line = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n "
					+ "<osm version=\"0.6\" generator=\"CGImap 0.0.2\">\n";
			writer.write(line);
			ArrayList<Entity> entities = OwnMethods.ReadEntity(entity_path);
			int node_count = entities.size();
			for (int i = 0; i < entities.size(); i++)
			{
				Entity entity = entities.get(i);
				if(entity.IsSpatial)
				{
					line = String.format("<node id=\"%d\" lat=\"%s\" lon=\"%s\" "
							+ "user=\"Yuhan Sun\" visible=\"true\" version=\"1\""
							+ " uid=\"%d\" changeset=\"%d\" timestamp=\"2010-08-12T16:15:03Z\">\n"
							+ "  <tag k=\"amenity\" v=\"school\"/>\n  <tag k=\"name\" v=\"Dammfriskolan\"/>"
							+ " ", i, String.valueOf(entity.lat), String.valueOf(entity.lon),
							node_count, node_count + 1);
					line += "</node>\n";
					writer.write(line);
				}
			}
			line = "</osm>\n";
			writer.write(line);
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void EntityToCSV(String entity_path, String csv_path)
	{
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(csv_path);
			String line = "id,lon,lat,changeset\n";
			writer.write(line);
			ArrayList<Entity> entities = OwnMethods.ReadEntity(entity_path);
			int node_count = entities.size();
			for (int i = 0; i < entities.size(); i++)
			{
				Entity entity = entities.get(i);
				if(entity.IsSpatial)
					writer.write(String.format("%d,%s,%s,%d\n", i, String.valueOf(entity.lon), String.valueOf(entity.lat), node_count));
			}
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
