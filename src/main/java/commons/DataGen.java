package commons;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class DataGen {
	
	public static int GetEdgeCount(ArrayList<ArrayList<Integer>> graph)
	{
		int count = 0;
		for(int i = 0; i < graph.size(); i++)
		{
			count += graph.get(i).size();
		}
		return count;
	}
	
	public static int GetEdgeCountSet(ArrayList<TreeSet<Integer>> graph)
	{
		int count = 0;
		for(int i = 0; i < graph.size(); i++)
		{
			count += graph.get(i).size();
		}
		return count;	
	}
	
	public static int GetSpaEntityEdgeCount(ArrayList<ArrayList<Integer>> graph, ArrayList<Entity> entities)
	{
		int count = 0;
		for(int i = 0; i < graph.size(); i++)
		{
			for(int j = 0; j < graph.get(i).size(); j++)
			{
				int dest_id = graph.get(i).get(j);
				if(entities.get(dest_id).IsSpatial)
					count++;
			}
		}
		return count;
	}
	
	public static int GetSpatialEntityCount(ArrayList<Entity> entities)
	{
		int count = 0;
		for(Entity p_entity : entities)
			if(p_entity.IsSpatial)
				count++;
	
		return count;
	}
	
	
	public static ArrayList<Entity> ReadEntity(String entity_path)
	{
		ArrayList<Entity> entities = null;
		BufferedReader reader = null;
		String str = null;
		try {
			reader = new BufferedReader(new FileReader(new File(entity_path)));
			str = reader.readLine();
			int node_count = Integer.parseInt(str);
			entities = new ArrayList<Entity>(node_count);
			int id = 0;
			
			while((str = reader.readLine())!=null)
			{
				String[] str_l = str.split(",");
				int flag = Integer.parseInt(str_l[1]);
				if(flag == 0)
				{
					Entity entity = new Entity();
					entities.add(entity);
				}
				else
				{
					Entity entity = new Entity(Double.parseDouble(str_l[2]), Double.parseDouble(str_l[3]));
					entities.add(entity);
				}
				id++;
			}
			reader.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return entities;
	}
	
	public static ArrayList<ArrayList<Integer>> ReadGraph(String graph_path)
	{
		ArrayList<ArrayList<Integer>> graph = null;
		BufferedReader reader = null;
		String str = null;
		try {
			reader = new BufferedReader(new FileReader(new File(graph_path)));
			str = reader.readLine();
			int node_count = Integer.parseInt(str);
			graph = new ArrayList<ArrayList<Integer>>(node_count);
			while((str = reader.readLine())!=null)
			{
				String[] l_str = str.split(",");
				int id = Integer.parseInt(l_str[0]);
				int neighbor_count = Integer.parseInt(l_str[1]);
				
				ArrayList<Integer> line = new ArrayList<Integer>(neighbor_count);
				if(neighbor_count == 0)
					graph.add(line);
				else
				{
					for(int i = 2; i < l_str.length; i++)
						line.add(Integer.parseInt(l_str[i]));
					graph.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;
	}

	public static ArrayList<TreeSet<Integer>> ReadGraphSet(String graph_path)
	{
		ArrayList<TreeSet<Integer>> graph = null;
		BufferedReader reader = null;
		String str = null;
		try {
			reader = new BufferedReader(new FileReader(new File(graph_path)));
			str = reader.readLine();
			int node_count = Integer.parseInt(str);
			graph = new ArrayList<TreeSet<Integer>>(node_count);
			while((str = reader.readLine())!=null)
			{
				String[] l_str = str.split(",");
				int id = Integer.parseInt(l_str[0]);
				int neighbor_count = Integer.parseInt(l_str[1]);
				
				TreeSet<Integer> line = new TreeSet<Integer>();
				if(neighbor_count == 0)
					graph.add(line);
				else
				{
					for(int i = 2; i < l_str.length; i++)
						line.add(Integer.parseInt(l_str[i]));
					graph.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;
	}

	public static Map<Integer, Double[]> ReadDiameter(String diameter_path)
	{
		BufferedReader reader = null;
		String str = null;
		Map<Integer, Double[]> diameters = new HashMap<Integer, Double[]>();
		try {
			reader = new BufferedReader(new FileReader(new File(diameter_path)));
			while((str = reader.readLine())!=null)
			{
				String[] l_str = str.split("\t");
				int cluster_id = Integer.parseInt(l_str[0]);
				double x_avg = Double.parseDouble(l_str[1]);
				double y_avg = Double.parseDouble(l_str[2]);
				diameters.put(cluster_id, new Double[]{x_avg, y_avg});
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return diameters;
	}
	
	public static Map<Integer, Integer> ReadMap(String map_path)
	{
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		BufferedReader reader = null;
		String str = null;
		try {
			reader = new BufferedReader(new FileReader(new File(map_path)));
			while((str = reader.readLine())!=null)
			{
				String[] l_str = str.split("\t");
				map.put(Integer.parseInt(l_str[0]), Integer.parseInt(l_str[1]));
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
	
	public static void OutputGraph(ArrayList<ArrayList<Integer>> graph, String graph_path)
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(graph_path));
			fw.write(String.format("%d\n", graph.size()));
			for(int i = 0; i < graph.size(); i++)
			{
				ArrayList<Integer> line = graph.get(i);
				fw.write(String.format("%d,%d", i, line.size()));
				if(line.size()!=0)
				{
					for(int j = 0; j < line.size(); j++)
					{
						fw.write(String.format(",%d", line.get(j)));
					}
				}
				fw.write("\n");
			}
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//General function
	public static void OutputGraphSet(ArrayList<TreeSet<Integer>> graph, String graph_path)
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(graph_path));
			fw.write(String.format("%d\n", graph.size()));
			for(int i = 0; i < graph.size(); i++)
			{
				TreeSet<Integer> line = graph.get(i);
				fw.write(String.format("%d,%d", i, line.size()));
				if(line.size()!=0)
				{
					String str = line.toString();
					str = str.substring(1, str.length() - 1);
					str = str.replace(" ", "");
					str = "," + str + "\n";
					fw.write(str);
				}
				else {
					fw.write("\n");
				}
				
			}
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	//graph just stores nonspatial nodes edges without storing spatial nodes
	public static void OutputGraphSet(ArrayList<TreeSet<Integer>> graph, ArrayList<Entity> entities, String graph_path)
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(graph_path));
			fw.write(String.format("%d\n", entities.size()));
			for(int i = 0; i < graph.size(); i++)
			{
				TreeSet<Integer> line = graph.get(i);
				fw.write(String.format("%d,%d", i, line.size()));
				if(line.size()!=0)
				{
					String str = line.toString();
					str = str.substring(1, str.length() - 1);
					str = str.replace(" ", "");
					str = "," + str + "\n";
					fw.write(str);
				}
				else {
					fw.write("\n");
				}
			}
			
			for(int i = graph.size(); i < entities.size(); i++)
				fw.write(String.format("%d,0\n", i));
			
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void OutputEntity(ArrayList<Entity> entities, String entity_path)
	{
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(entity_path));
			fw.write(String.format("%d\n", entities.size()));
			for(int i = 0; i < entities.size(); i++)
			{
				Entity entity = entities.get(i);
				fw.write(String.format("%d,", i));
				if(entity.IsSpatial)
				{
					fw.write(String.format("1,%f,%f\n", entity.lon, entity.lat));
				}
				else {
					fw.write("0\n");
				}
			}
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void AddClusterData(ArrayList<TreeSet<Integer>> graph, ArrayList<Entity> entities, int add_node_count, int add_edge_count, Map<Integer, Double[]> diameters, Map<Integer, Integer> entity_cluster_map)
	{
		Random random = new Random();
		int ori_node_count = graph.size();
		
		int spa_start_index = 0;
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i).IsSpatial)
			{
				spa_start_index = i;
				break;
			}
		}
		
		for(int i = 0; i < add_node_count; i++)
		{
			int center_entity_id = (int) (random.nextDouble() * ori_node_count);
			Entity center_entity = entities.get(center_entity_id);
			if(center_entity.IsSpatial == false)
			{
				i--;
				continue;
			}
			else
			{
				int center_entity_clusterID = entity_cluster_map.get(center_entity_id);
				Double[] diameter = diameters.get(center_entity_clusterID);
				double off_x = random.nextGaussian() * diameter[0];
				double off_y = random.nextGaussian() * diameter[1];
				Entity new_entity = new Entity(center_entity.lon + off_x, center_entity.lat + off_y);
				entities.add(new_entity);
				
//				TreeSet<Integer> line = new TreeSet<Integer>();
//				graph.add(line);
			}
		}
		
		for(int i = 0; i < add_edge_count; i++)
		{
			int target_id = (int) (random.nextDouble() * add_node_count + ori_node_count);
			int source_id = (int) (random.nextDouble() * spa_start_index);
//			if(entities.get(source_id).IsSpatial == false)
//			{
//				i--;
//				continue;
//			}
//			else 
//			{
				if(graph.get(source_id).add(target_id))
					continue;
				else {
					i--;
					continue;
				}
//			}
		}
		
	}
	
	public static void AddClusterData()
	{
		String datasource = "Yelp";
		int ori_folder = 1;
		
		int trg_folder = 4;
		
		String diameter_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\diameter.txt", datasource, ori_folder);
		Map<Integer, Double[]> diameters = ReadDiameter(diameter_path);
		
		String graph_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\graph_entity_newformat.txt", datasource);
		ArrayList<TreeSet<Integer>> graph = ReadGraphSet(graph_path);
		
		String entity_cluster_map_filepath = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\entity_cluster_map.txt", datasource, ori_folder);
		Map<Integer, Integer> entity_cluster_map = ReadMap(entity_cluster_map_filepath);
		
		String entity_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\entity_newformat.txt", datasource, ori_folder);
		ArrayList<Entity> entities = ReadEntity(entity_path);
		
		int ori_node_count = graph.size();
		int ori_spa_node_count = 77445;
		int edge_count = 6033185;
		double avg_edge_count = (double)edge_count / ori_node_count;
		double ori_ratio = (double)ori_spa_node_count / ori_node_count;
		double target_ratio = Math.pow(2.0, trg_folder - 1.0) * ori_ratio;
//		target_ratio = 1.0 - target_ratio;
//		ori_ratio = 1.0 - ori_ratio;
		int add_node_count = (int)(((target_ratio - (double)ori_ratio)) / (1.0 - target_ratio)* ori_node_count);
		int target_node_count = ori_node_count + add_node_count;
		int add_edge_count = (int)(add_node_count * avg_edge_count);
		AddClusterData(graph, entities, add_node_count, add_edge_count, diameters, entity_cluster_map);
		
		String target_graph_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\graph_entity_newformat.txt", datasource, trg_folder);
		OutputGraphSet(graph, entities, target_graph_path);
		
		String target_entity_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\entity_newformat.txt", datasource, trg_folder);
		OutputEntity(entities, target_entity_path);
	}

	public static void tt()
	{
		ArrayList<Integer> x = new ArrayList<Integer>(){
			{
				add(0);
				add(1);
			}
		};
		OwnMethods.Print(x.toString());
		
//		String datasource = "Yelp";
//		String graph_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\graph_entity_newformat.txt", datasource);
//		ArrayList<TreeSet<Integer>> graph = ReadGraphSet(graph_path);
//		
//		String target_graph_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\graph_entity_newformat.txt", datasource, 2);
//		OutputGraphSet(graph, target_graph_path);
		
		String datasource = "Yelp";
//		String graph_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\graph_entity_newformat.txt", datasource);
//		ArrayList<ArrayList<Integer>> graph = ReadGraph(graph_path);
//		
//		String entity_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\entity_newformat.txt", datasource, 1);
//		ArrayList<Entity> entities = ReadEntity(entity_path);
		
//		OwnMethods.Print(GetSpaEntityEdgeCount(graph, entities));
		
		String graph_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\graph_entity_newformat.txt", datasource, 4);
//		String entity_path = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\entity_newformat.txt", datasource, 4);
//		OwnMethods.Print(OwnMethods.ReadFile(graph_path, 2).toString());
		ArrayList<ArrayList<Integer>> graph = ReadGraph(graph_path);
		FileWriter fWriter= null;
		try {
			fWriter = new FileWriter(graph_path);
			fWriter.write(34023304 + "\n");
			for(int i = 0; i < graph.size(); i++)
			{
				if(graph.get(i).size() == 0)
				{
					fWriter.write(String.format("%d,0\n", i));
					continue;
				}
				else {
					String line = graph.get(i).toString();
					line = line.substring(1, line.length() - 1);
					line = line.replaceAll(" ", "");
					fWriter.write(String.format("%d,%d,%s\n", i, graph.get(i).size(), line));
				}
			}
			for(int i = graph.size();i<34023304; i++)
			{
				fWriter.write(String.format("%d,0\n", i));
			}
			fWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
//		Random random = new Random();
//		for(int i = 0;i<100;i++)
//		{
//			OwnMethods.Print(random.nextGaussian());
//		}
//		double x = 1/(double)2;
//		OwnMethods.Print(x);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		tt();
		AddClusterData();
		
//		String filepath = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\new_graph.txt";
//		ArrayList<ArrayList<Integer>> graph = ReadGraph(filepath);
//		for(ArrayList<Integer> line : graph)
//		{
//			OwnMethods.Print(line.size());
//		}
//		String filepath = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\Random_spatial_distributed\\80\\new_entity.txt";
//		ArrayList<Entity> entities = ReadEntity(filepath);
//		for(Entity p_entity : entities)
//		{
//			OwnMethods.Print(p_entity);
//			break;
//		}
		
//		ArrayList<Integer> a = new ArrayList<Integer>();
//		OwnMethods.Print(a);
//		tt(a);
//		OwnMethods.Print(a);
	}

}
