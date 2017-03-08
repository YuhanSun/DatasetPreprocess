package gowalla_preprocess;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

import javax.sound.sampled.Line;

import commons.DataGen;
import commons.Entity;
import commons.OwnMethods;

public class AddData {
	
	public static String datasource = "Gowalla";
	public static int ori_folder = 2;
	public static String directory = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\", datasource, ori_folder);
	
	public static void AddData(ArrayList<TreeSet<Integer>> graph, ArrayList<Entity> entities, int ori_spa_count, int add_node_count, int add_edge_count)
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
			int center_entity_id = (int) (random.nextDouble() * ori_spa_count) + (ori_node_count - ori_spa_count);
			Entity center_entity = entities.get(center_entity_id);
			if(center_entity.IsSpatial == false)
			{
				OwnMethods.Print(String.format("%d", center_entity_id));
				return;
			}
			else
			{
				
				double off_x = random.nextGaussian() * 0.05;
				double off_y = random.nextGaussian() * 0.05;
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
			
			if(entities.get(source_id).IsSpatial == true)
			{
				OwnMethods.Print(String.format("Source is spatial: %d", source_id));
				break;
			}
			if(entities.get(target_id).IsSpatial == false)
			{
				OwnMethods.Print(String.format("Target is not spatial: %d", target_id));
				break;
			}
			
			if(graph.get(source_id).add(target_id))
				continue;
			else {
				i--;
				continue;
			}
		}
		
	}
	
	public static void add()
	{
		int target_folder = 4;
		String target_directory = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\", datasource, target_folder);
		
		String graph_path = directory + "graph_entity_newformat.txt";
		ArrayList<TreeSet<Integer>> graph = DataGen.ReadGraphSet(graph_path);
		
		String entity_path = directory + "entity_newformat.txt";
		ArrayList<Entity> entities = DataGen.ReadEntity(entity_path);
		
		int total_count = DataGen.GetEdgeCountSet(graph);
		double avg_edge_count = (double)total_count / graph.size();

		int ori_spa_count = DataGen.GetSpatialEntityCount(entities);		
		int target_spa_count = (int) (ori_spa_count * Math.pow(2.0, target_folder - ori_folder));
		int add_node_count = target_spa_count - ori_spa_count;
		int add_edge_count = (int) (add_node_count * avg_edge_count);
		
		OwnMethods.Print(String.format("%d\t%d", add_node_count, add_edge_count));
		

		AddData(graph, entities, ori_spa_count, add_node_count, add_edge_count);
		
		String target_graph_path = target_directory + "graph_entity_newformat.txt";
		DataGen.OutputGraphSet(graph, entities, target_graph_path);
		
		String target_entity_path = target_directory + "entity_newformat.txt";
		DataGen.OutputEntity(entities, target_entity_path);
	}
	
	public static void DeleteData(ArrayList<TreeSet<Integer>> graph, ArrayList<Entity> entities, int ori_spa_count, int delete_node_count)
	{
		TreeSet<Integer> deleted_ids = new TreeSet<Integer>();
		
		ArrayList<TreeSet<Integer>> in_edge_graph = OwnMethods.GetInedgeGraph(graph);
		
		Random random = new Random();
		for(int i = 0; i<delete_node_count; i++)
		{
			int delete_id = (int) (random.nextDouble() * ori_spa_count + (entities.size() - ori_spa_count));
			if(deleted_ids.add(delete_id))
			{
//				OwnMethods.Print(String.format("%d: %d", deleted_ids.size(), delete_id));
				for(int source_id : in_edge_graph.get(delete_id))
				{
					graph.get(source_id).remove(delete_id);
				}
			}
			else {
				i--;
				continue;
			}
		}
		
		ArrayList<Integer> reverse_deleted_ids = new ArrayList<Integer>(deleted_ids.size());
		Iterator<Integer> iter = deleted_ids.iterator();
		
		ArrayList<TreeSet<Integer>> new_graph = new ArrayList<TreeSet<Integer>>();
		for(int i = 0; i < graph.size(); i++)
		{
			if(deleted_ids.contains(i))
			{
				entities.set(i, null);
				continue;
			}
			else
			{
				TreeSet<Integer> line = graph.get(i);
				new_graph.add(line);
			}
		}
		graph.clear();
		graph.addAll(new_graph);
	}
	
	public static ArrayList<Integer> GenerateMap(ArrayList<Entity> entities)
	{
		ArrayList<Integer> map = new ArrayList<Integer>();
		int id = 0;
		for(int i = 0; i < entities.size(); i++)
		{
			Entity p_entity = entities.get(i);
			if(p_entity != null)
			{
				map.add(id);
				id++;
			}
			else {
				map.add(-1);
			}
		}
		return map;
	}
	
	public static void delete()
	{
		int target_folder = 1;
		String target_directory = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\", datasource, target_folder);
		
		String graph_path = directory + "graph_entity_newformat.txt";
		ArrayList<TreeSet<Integer>> graph = DataGen.ReadGraphSet(graph_path);
		
		String entity_path = directory + "entity_newformat.txt";
		ArrayList<Entity> entities = DataGen.ReadEntity(entity_path);
		
		int ori_spa_count = DataGen.GetSpatialEntityCount(entities);		
		int target_spa_count = (int) (ori_spa_count * Math.pow(2.0, target_folder - ori_folder));
		int delete_node_count = -(target_spa_count - ori_spa_count);
		
		OwnMethods.Print(String.format("%d\t", delete_node_count));
		
		DeleteData(graph, entities, ori_spa_count, delete_node_count);
		
		ArrayList<Integer> entity_id_map = GenerateMap(entities);
		
		for(TreeSet<Integer> line : graph)
		{
			TreeSet<Integer> new_line = new TreeSet<Integer>();
			for(int out_id : line)
			{
				int new_id = entity_id_map.get(out_id);
				if(new_id != out_id)
				{
					new_line.add(new_id);
				}
			}
			line.clear();
			line.addAll(new_line);
		}
		
		ArrayList<Entity> new_entities = new ArrayList<Entity>();
		for(Entity p_entity : entities)
		{
			if(p_entity != null)
			{
				if(p_entity.IsSpatial)
				{
					Entity entity = new Entity(p_entity.lon, p_entity.lat);
					new_entities.add(entity);
				}
				else {
					new_entities.add(new Entity());
				}
				
			}
		}
		entities.clear();
		entities.addAll(new_entities);
		
		String target_graph_path = target_directory + "graph_entity_newformat.txt";
		DataGen.OutputGraphSet(graph, target_graph_path);
		
		String target_entity_path = target_directory + "entity_newformat.txt";
		DataGen.OutputEntity(entities, target_entity_path);
	}
	
	public static void test(ArrayList<TreeSet<Integer>> line)
	{
		line.clear();
	}
	
	public static void app()
	{
		int target_folder = 1;
		String target_directory = String.format("D:\\Ubuntu_shared\\Real_Data\\%s\\Random_spatial_distributed\\%d\\", datasource, target_folder);
		String graph_path = target_directory + "graph_entity_newformat.txt";
		ArrayList<TreeSet<Integer>> graph = DataGen.ReadGraphSet(graph_path);
		
		int edge_count = DataGen.GetEdgeCountSet(graph);
		OwnMethods.Print(String.format("%f", edge_count / (double) graph.size()));
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		add();
		delete();
		
//		app();
	}

}
