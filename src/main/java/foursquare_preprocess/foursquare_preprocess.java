package foursquare_preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import commons.*;

public class foursquare_preprocess {
	
	public static String datasource = "foursquare";
	public static int ori_folder = 2;
	public static String directory = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\", datasource);
	public static String users_filepath = directory + "users.dat";
	public static String venues_filepath = directory + "venues.dat";
	public static String checkin_path = directory + "checkins.dat";
	public static String socialgraph_path = directory + "socialgraph.dat";

	public static String user_id_filepath = directory + "User_id.txt";
	
	public static String user_map_filepath = directory + "User_id_map.txt";
	public static String venue_id_map_filepath = directory + "Venue_id_map.txt";

	public static void App()
	{
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
//			GetUserID();
//			GetVenueID();
			
//			GetUserIDMap();
//			GetVenueIDMap();
//			
//			GenerateOriGraph();
			GenerateEntity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void GetUserID()
		{
			BufferedReader reader = null;
			String str = null;
			Set<Integer> ids = new TreeSet<Integer>();
			
			FileWriter fwWriter = null;
			try {
				reader = new BufferedReader(new FileReader(new File(users_filepath)));
				reader.readLine();
				reader.readLine();
				while((str = reader.readLine())!= null)
				{
					if(str.indexOf('|') == -1)
						continue;
					String[] l = str.split("\\|");
					
					int user_id = Integer.parseInt(l[0].trim());
					
	//				String lat_str = l[1].trim();
	//				String lon_str = l[2].trim();
	//				
	//				if(!lat_str.equals("") && !lon_str.equals(""))
	//				{
	//					double lat = Double.parseDouble(lat_str);
	//					double lon = Double.parseDouble(lon_str);
	//					if(lon > 180 || lon < -180 || lat > 90 || lat < -90)
	//					{
	//						OwnMethods.Print(str);
	//						continue;
	//					}
	//				}
					
					ids.add(user_id);
				}
				reader.close();
				
				fwWriter = new FileWriter(directory + "User_id.txt");
				for(int id : ids)
				{
					fwWriter.write(String.format("%d\n", id));
				}
				fwWriter.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				OwnMethods.Print(str);
				e.printStackTrace();
				
			}
		}

	public static void GetVenueID()
	{
		BufferedReader reader = null;
		String str = null;
		Set<Integer> ids = new TreeSet<Integer>();
		int remove_count = 0;
		
		FileWriter fwWriter = null;
		try {
			reader = new BufferedReader(new FileReader(new File(venues_filepath)));
			reader.readLine();
			reader.readLine();
			while((str = reader.readLine())!= null)
			{
				if(str.indexOf('|') == -1)
					continue;
				String[] l = str.split("\\|");
				
				int loc_id = Integer.parseInt(l[0].trim());
				
				String lat_str = l[1].trim();
				String lon_str = l[2].trim();
				
				if(!lat_str.equals("") && !lon_str.equals(""))
				{
					double lat = Double.parseDouble(lat_str);
					double lon = Double.parseDouble(lon_str);
					if(lon > 180 || lon < -180 || lat > 90 || lat < -90)
					{
						OwnMethods.Print(str);
						continue;
					}
				}
				else {
					remove_count++;
				}
				
				ids.add(loc_id);
			}
			reader.close();
			OwnMethods.Print(String.format("Remove count:%d\n", remove_count));
			fwWriter = new FileWriter(directory + "Loc_id.txt");
			for(int id : ids)
			{
				fwWriter.write(String.format("%d\n", id));
			}
			fwWriter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			OwnMethods.Print(str);
			e.printStackTrace();
			
		}
	}
	
	public static void GetUserIDMap()
	{
		BufferedReader reader = null;
		FileWriter fWriter = null;
		String str = null;
		try {
			reader = new BufferedReader(new FileReader(new File(user_id_filepath)));
			fWriter = new FileWriter(new File(user_map_filepath));
			int new_id = 0;
			while((str = reader.readLine())!=null)
			{
				int ori_id = Integer.parseInt(str);
				fWriter.write(String.format("%d\t%d\n", ori_id, new_id));
				new_id++;
			}
			reader.close();
			fWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void GetVenueIDMap()
	{
		Map<Integer, Integer> user_map = OwnMethods.ReadMap(user_map_filepath);
		
		BufferedReader reader = null;
		FileWriter fWriter = null;
		String str = null;
		
		String venue_id_filepath = directory + "Loc_id.txt";
		try {
			reader = new BufferedReader(new FileReader(new File(venue_id_filepath)));
			fWriter = new FileWriter(new File(venue_id_map_filepath));
			int new_id = 0;
			while((str = reader.readLine())!=null)
			{
				int ori_id = Integer.parseInt(str);
				fWriter.write(String.format("%d\t%d\n", ori_id, new_id + user_map.size()));
				new_id++;
			}
			reader.close();
			fWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void GenerateOriGraph()
	{
		Map<Integer, Integer> user_id_map = OwnMethods.ReadMap(user_map_filepath);
		Map<Integer, Integer> venue_id_map = OwnMethods.ReadMap(venue_id_map_filepath);
		int social_not_exist_count = 0, check_not_exist_count = 0;
		BufferedReader reader = null;
		String str = null;
		ArrayList<TreeSet<Integer>> graph = new ArrayList<TreeSet<Integer>>(user_id_map.size() + venue_id_map.size());
		for(int i = 0; i < user_id_map.size() + venue_id_map.size(); i++)
			graph.add(new TreeSet<Integer>());
		try {
			reader = new BufferedReader(new FileReader(new File(socialgraph_path)));
			reader.readLine();
			reader.readLine();
			while((str = reader.readLine())!=null)
			{
				if(str.indexOf('|') == -1)
					continue;
				String[] l = str.split("\\|");
				int start_id = Integer.parseInt(l[0].trim());
				int end_id = Integer.parseInt(l[1].trim());
				
				if(user_id_map.containsKey(start_id) && user_id_map.containsKey(end_id))
				{
					int start_map_id = user_id_map.get(start_id);
					int end_map_id = user_id_map.get(end_id);
					graph.get(start_map_id).add(end_map_id);
				}
				else {
//					OwnMethods.Print(String.format("Map from %d and %d does not exist", start_id, end_id));
					social_not_exist_count ++ ;
					continue;
				}
			}
			reader.close();
			
			reader = new BufferedReader(new FileReader(new File(checkin_path)));
			reader.readLine();
			reader.readLine();
			while((str = reader.readLine())!=null)
			{
				if(str.indexOf('|') == -1)
					continue;
				String[] l = str.split("\\|");
				int start_id = Integer.parseInt(l[1].trim());
				int end_id = Integer.parseInt(l[2].trim());
				
				if(user_id_map.containsKey(start_id) && venue_id_map.containsKey(end_id))
				{
					int start_map_id = user_id_map.get(start_id);
					int end_map_id = venue_id_map.get(end_id);
					graph.get(start_map_id).add(end_map_id);
				}
				else {
//					OwnMethods.Print(String.format("Map from %d and %d does not exist", start_id, end_id));
					check_not_exist_count++;
					continue;
				}
			}
			reader.close();
			
			OwnMethods.Print(String.format("Social not exist count: %d\nCheckin not exist count:%d\n", social_not_exist_count, check_not_exist_count));
			
			String graph_path = directory + "graph_entity_newformat.txt";
			OwnMethods.OutputGraphSet(graph, graph_path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void GenerateEntity()
	{
		Map<Integer, Integer> user_id_map = OwnMethods.ReadMap(user_map_filepath);
		Map<Integer, Integer> venue_id_map = OwnMethods.ReadMap(venue_id_map_filepath);
		
		ArrayList<Entity> entities = new ArrayList<Entity>(user_id_map.size() + venue_id_map.size());
		for(int i = 0; i < user_id_map.size() + venue_id_map.size(); i++)
			entities.add(new Entity());
			
		BufferedReader reader = null;
		String str = null;
		try {
			//user entity
			reader = new BufferedReader(new FileReader(new File(users_filepath)));
			reader.readLine();
			reader.readLine();
			while((str = reader.readLine())!=null)
			{
				if(str.indexOf('|') == -1)
					continue;
				String[] l = str.split("\\|");
				int id = Integer.parseInt(l[0].trim());
				if(user_id_map.containsKey(id))
				{
					int id_map = user_id_map.get(id);

					String lat_str = l[1].trim();
					String lon_str = l[2].trim();
					
					if(lat_str.equals("") || lon_str.equals(""))
						continue;
					else
					{
						double lat = Double.parseDouble(lat_str);
						double lon = Double.parseDouble(lon_str);
						Entity p_entit = new Entity(lon, lat);
						entities.set(id_map, p_entit);
					}
					
				}
				else {
					OwnMethods.Print(String.format("The user does not exist in graph\n%s", str));
					continue;
				}
			}
			reader.close();
//			
			//venue entity
			reader = new BufferedReader(new FileReader(new File(venues_filepath)));
			reader.readLine();
			reader.readLine();
			while((str = reader.readLine())!=null)
			{
				if(str.indexOf('|') == -1)
					continue;
				String[] l = str.split("\\|");
				int id = Integer.parseInt(l[0].trim());
				if(venue_id_map.containsKey(id))
				{
					int id_map = venue_id_map.get(id);
					
					String lat_str = l[1].trim();
					String lon_str = l[2].trim();
					
					if(lat_str.equals("") || lon_str.equals(""))
						continue;
					else
					{
						double lat = Double.parseDouble(lat_str);
						double lon = Double.parseDouble(lon_str);
						Entity p_entit = new Entity(lon, lat);
						entities.set(id_map, p_entit);
					}
				}
				else {
					OwnMethods.Print(String.format("The venue does not exist in graph\n%s", str));
					continue;
				}
			}
			reader.close();
			
			if(entities.size() != user_id_map.size() + venue_id_map.size())
				OwnMethods.Print("Size not equal");
			
			OwnMethods.OutputEntity(entities, directory + "entity_newformat_Minhop.txt");
		} catch (Exception e) {
			e.printStackTrace();
			OwnMethods.Print(str);
		}
	}
}
