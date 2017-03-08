package gowalla_preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import commons.*;


public class data_convert {
	
	public static int person_count = 196591;
	public static int target_folder = 2;
	public static String directory = "D:\\Ubuntu_shared\\Real_Data\\Gowalla\\Random_spatial_distributed\\"+target_folder+"\\";
	public static String social_path = "D:\\Ubuntu_shared\\Real_Data\\Gowalla\\Gowalla_edges.txt";
	public static String checkin_path = "D:\\Ubuntu_shared\\Real_Data\\Gowalla\\Gowalla_totalCheckins.txt";
	
	public static void Proprocess()
	{
		BufferedReader reader = null;
		String str = null;
		FileWriter fWriter = null;
		try {
			reader = new BufferedReader(new FileReader(new File(checkin_path)));
			fWriter = new FileWriter(new File("D:\\Ubuntu_shared\\Real_Data\\Gowalla\\Gowalla_totalCheckins_new.txt"));
			while((str = reader.readLine())!=null)
			{
				String [] l = str.split("\t");
				double lat = Double.parseDouble(l[2]);
				double lon = Double.parseDouble(l[3]);
				
				if(lat<=-90.0 || lat >= 90.0 || lon <= -180.0 || lon >= 180.0)
				{
					OwnMethods.Print(str);
					continue;
				}
				else {
					fWriter.write(str + "\n");
				}
			}
			fWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void GetPersonID()
	{
		{
			BufferedReader reader = null;
			String str = null;
			Set<Integer> ids = new TreeSet<Integer>();
			
			FileWriter fwWriter = null;
			try {
				reader = new BufferedReader(new FileReader(new File(social_path)));
				while((str = reader.readLine())!= null)
				{
					String[] l = str.split("\t");
					int person_start = Integer.parseInt(l[0]);
					int person_end = Integer.parseInt(l[1]);
					ids.add(person_start);
					ids.add(person_end);
				}
				reader.close();
				
				fwWriter = new FileWriter(directory + "Person_id.txt");
				for(int id : ids)
				{
					fwWriter.write(String.format("%d\n", id));
				}
				fwWriter.close();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public static void GetLocationID()
	{
		BufferedReader reader = null;
		String str = null;
		Set<Integer> ids = new TreeSet<Integer>();
		
		FileWriter fwWriter = null;
		try {
			reader = new BufferedReader(new FileReader(new File(checkin_path)));
			while((str = reader.readLine())!= null)
			{
				int loc_id = Integer.parseInt(str.split("\t")[4]);
				ids.add(loc_id);
			}
			reader.close();
			
			fwWriter = new FileWriter(directory + "Loc_id.txt");
			for(int id : ids)
			{
				fwWriter.write(String.format("%d\n", id));
			}
			fwWriter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static ArrayList<Integer> ReadPersonID(String filepath)
	{
		BufferedReader reader = null;
		String str = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		try {
			reader = new BufferedReader(new FileReader(new File(filepath)));
			while((str = reader.readLine())!=null)
			{
				int id = Integer.parseInt(str);
				ids.add(id);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ids;
	}
	
	public static ArrayList<Integer> ReadLocID(String filepath)
	{
		BufferedReader reader = null;
		String str = null;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		try {
			reader = new BufferedReader(new FileReader(new File(filepath)));
			while((str = reader.readLine())!=null)
			{
				int id = Integer.parseInt(str);
				ids.add(id);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ids;
	}
	
	public static void MapLocIDToOriGraphID()
	{
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(new File(directory + "locid_origraph_map.txt"));
			String loc_ids_path = directory + "Loc_id.txt";
			ArrayList<Integer> loc_ids = ReadLocID(loc_ids_path);
			for(int i = 0; i < loc_ids.size(); i++)
			{
				fWriter.write(String.format("%d\t%d\n", loc_ids.get(i), i + person_count));
			}
			fWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void GenerateEntity()
	{
		FileWriter fWriter = null;
		BufferedReader reader = null;
		String str = null;
		try {
			ArrayList<Integer> person_ids = ReadPersonID(directory + "Person_id.txt");
			ArrayList<Integer> loc_ids = ReadLocID(directory + "Loc_id.txt");
			fWriter = new FileWriter(new File(directory + "entity_newformat.txt"));
			fWriter.write(String.format("%d\n", person_ids.size() + loc_ids.size()));
			for(int id : person_ids)
			{
				fWriter.write(String.format("%d,0\n", id));
			}
			fWriter.close();
			
			Map<Integer, Integer> locid_origraph_map = DataGen.ReadMap(directory + "locid_origraph_map.txt");
			
			reader = new BufferedReader(new FileReader(new File(checkin_path)));
			Map<Integer, Entity> loc_id_location_map = new HashMap<Integer, Entity>();
			int location_count = 0;
			while((str = reader.readLine()) != null)
			{
				String [] l = str.split("\t");
				double lat = Double.parseDouble(l[2]);
				double lon = Double.parseDouble(l[3]);
				int id = Integer.parseInt(l[4]);
				
				if(loc_id_location_map.containsKey(id))
					continue;
				else {
					Entity p_entity = new Entity(lon, lat);
					loc_id_location_map.put(id, p_entity);
					location_count ++;
					if(location_count == locid_origraph_map.size())
					{
						OwnMethods.Print("here");
						break;
					}
				}
			}
			reader.close();
			
			fWriter = new FileWriter(new File(directory + "entity_newformat.txt"), true);
			OwnMethods.Print(loc_ids.size());
			OwnMethods.Print(loc_id_location_map.size());
			for(int id : loc_ids)
			{
				if(locid_origraph_map.containsKey(id))
				{
					int origraph_id = locid_origraph_map.get(id);
					Entity p_entity = loc_id_location_map.get(id);
					String line = String.format("%d,1,%f,%f\n", origraph_id, p_entity.lon, p_entity.lat);
					fWriter.write(line);
				}
				else {
					OwnMethods.Print("Map not contain" + id);
					break;
				}
			}
			fWriter.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void GenerateOriGraph()
	{
		BufferedReader reader = null;
		String str = null;
		ArrayList<TreeSet<Integer>> graph = new ArrayList<TreeSet<Integer>>(person_count);
		for(int i = 0; i < person_count; i++)
			graph.add(new TreeSet<Integer>());
		try {
			//person-person edge
			reader = new BufferedReader(new FileReader(new File(social_path)));
			while((str = reader.readLine())!= null)
			{
				String [] l = str.split("\t");
				int s_id = Integer.parseInt(l[0]);
				int t_id = Integer.parseInt(l[1]);
				graph.get(s_id).add(t_id);
			}
			reader.close();
			
			//person-location edge
			Map<Integer, Integer> locid_origraph_map = DataGen.ReadMap(directory + "locid_origraph_map.txt");
			reader = new BufferedReader(new FileReader(new File(checkin_path)));
			while((str = reader.readLine())!=null)
			{
//				OwnMethods.Print(str);
				String [] l = str.split("\t");
				
				int start_id = Integer.parseInt(l[0]);
//				OwnMethods.Print(start_id);
				int end_id = Integer.parseInt(l[4]);
//				OwnMethods.Print(end_id);
				int ori_graph_id = locid_origraph_map.get(end_id);
//				OwnMethods.Print(ori_graph_id);
				graph.get(start_id).add(ori_graph_id);
				
//				OwnMethods.Print(start_id);
//				OwnMethods.Print(ori_graph_id);

						
			}
			reader.close();
			
			ArrayList<Entity> entities = DataGen.ReadEntity(directory + "entity_newformat.txt");
			
			DataGen.OutputGraphSet(graph, entities, directory + "graph_entity_newformat.txt");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static void main(String[] args) {
		
		try {
			// TODO Auto-generated method stub
//			Proprocess();
			
//			GetLocationID();
//			GetPersonID();
//			MapLocIDToOriGraphID();
//			GenerateEntity();
			GenerateOriGraph();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
	}

}
