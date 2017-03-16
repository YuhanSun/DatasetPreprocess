package commons;

import java.awt.Image;
import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.*;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;

public class OwnMethods {
	
	 public static ArrayList<Entity> ReadEntity(String entity_path) {
	        ArrayList<Entity> entities = null;
	        BufferedReader reader = null;
	        String str = null;
	        try {
	            reader = new BufferedReader(new FileReader(new File(entity_path)));
	            str = reader.readLine();
	            int node_count = Integer.parseInt(str);
	            entities = new ArrayList<Entity>(node_count);
	            int id = 0;
	            while ((str = reader.readLine()) != null) {
	                Entity entity;
	                String[] str_l = str.split(",");
	                int flag = Integer.parseInt(str_l[1]);
	                if (flag == 0) {
	                    entity = new Entity();
	                    entities.add(entity);
	                } else {
	                    entity = new Entity(Double.parseDouble(str_l[2]), Double.parseDouble(str_l[3]));
	                    entities.add(entity);
	                }
	                ++id;
	            }
	            reader.close();
	        }
	        catch (Exception node_count) {
	            // empty catch block
	        }
	        return entities;
	    }
	
	public static ArrayList<ArrayList<MyRectangle>> ReadHmbr(String hmbr_path)
	{
		ArrayList<ArrayList<MyRectangle>> hmbr = null;
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new FileReader(new File(hmbr_path)));
			line = reader.readLine();
			String [] list_hmbr = line.split(",");
			int node_count = Integer.parseInt(list_hmbr[0]);
			int hop_num = Integer.parseInt(list_hmbr[1]);
			hmbr = new ArrayList<ArrayList<MyRectangle>>(node_count);
			for ( int i = 0; i < node_count; i++)
				hmbr.add(new ArrayList<MyRectangle>(hop_num));
			
			for ( int i = 0; i < node_count; i++)
			{
				line = reader.readLine();
				list_hmbr = line.split(";");
				ArrayList<MyRectangle> hmbr_line = hmbr.get(i);
				for ( int j = 0; j < hop_num; j++)
				{
					String start = list_hmbr[j].substring(0, 1);
					if(start.equals("1") == true)
					{
						String rect = list_hmbr[j].substring(3, list_hmbr[j].length()-1);
						String[] liString = rect.split(",");
						double minx = Double.parseDouble(liString[0]);
						double miny = Double.parseDouble(liString[1]);
						double maxx = Double.parseDouble(liString[2]);
						double maxy = Double.parseDouble(liString[3]);
						hmbr_line.add(new MyRectangle(minx, miny, maxx, maxy));
					}
					else
					{
						hmbr_line.add(null);
					}
				}
			}
			reader.close();
			return hmbr;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		OwnMethods.Print("ReadHmbr return null!");
		return hmbr;
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
	
	public static ArrayList<TreeSet<Integer>> GetInedgeGraph(ArrayList<TreeSet<Integer>> graph)
	{
		ArrayList<TreeSet<Integer>> in_edge_graph = new ArrayList<TreeSet<Integer>>(graph.size());
		for(int i = 0; i < graph.size(); i++)
			in_edge_graph.add(new TreeSet<Integer>());
		for(int i = 0; i < graph.size(); i++)
		{
			TreeSet<Integer> out_list = graph.get(i);
			for(int out_id : out_list)
			{
				in_edge_graph.get(out_id).add(i);
			}
		}
		return in_edge_graph;
	}
	
	public static long GeoReachIndexSize(String GeoReach_filepath)
	{
		BufferedReader reader_GeoReach = null;
		File file_GeoReach = null;
		long bits = 0;
		try
		{
			file_GeoReach = new File(GeoReach_filepath);
			reader_GeoReach = new BufferedReader(new FileReader(file_GeoReach));
			String tempString_GeoReach = null;

			while((tempString_GeoReach = reader_GeoReach.readLine())!= null)
			{
				String[] l_GeoReach = tempString_GeoReach.split(",");
				
				int type = Integer.parseInt(l_GeoReach[1]);
				switch (type)
				{
				case 0:
					RoaringBitmap r = new RoaringBitmap();
					for(int i = 2;i<l_GeoReach.length;i++)
					{
						int out_neighbor = Integer.parseInt(l_GeoReach[i]);
						r.add(out_neighbor);
					}
					String bitmap_ser = OwnMethods.Serialize_RoarBitmap_ToString(r);
					bits += bitmap_ser.getBytes().length * 8;
					break;
				case 1:
					bits += 32 * 4;
					break;
				case 2:
					bits += 1;
					break;
				}
			}
			reader_GeoReach.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader_GeoReach!=null)
			{
				try
				{
					reader_GeoReach.close();
				}
				catch(IOException e)
				{	
					e.printStackTrace();
				}
			}
		}
		return bits / 8;
	}
	
	//Generate Random node_count vertices in the range(0, graph_size) which is attribute id
	public static HashSet<String> GenerateRandomInteger(long graph_size, int node_count)
	{
		HashSet<String> ids = new HashSet<String>();
		
		Random random = new Random();
		while(ids.size()<node_count)
		{
			Integer id = (int) (random.nextDouble()*graph_size);
			ids.add(id.toString());
		}
		
		return ids;
	}
	
//	//Generate absolute id in database depends on attribute_id and node label
//	public static ArrayList<String> GenerateStartNode(WebResource resource, HashSet<String> attribute_ids, String label)
//	{
//		String query = "match (a:" + label + ") where a.id in " + attribute_ids.toString() + " return id(a)";
//		String result = Neo4j_Graph_Store.Execute(resource, query);
//		ArrayList<String> graph_ids = Neo4j_Graph_Store.GetExecuteResultData(result);
//		return graph_ids;
//	}
//	
//	//Generate absolute id in database depends on attribute_id and node label
//	public static ArrayList<String> GenerateStartNode(HashSet<String> attribute_ids, String label)
//	{
//		Neo4j_Graph_Store p_neo4j_graph_store = new Neo4j_Graph_Store();
//		String query = "match (a:" + label + ") where a.id in " + attribute_ids.toString() + " return id(a)";
//		String result = p_neo4j_graph_store.Execute(query);
//		ArrayList<String> graph_ids = Neo4j_Graph_Store.GetExecuteResultData(result);
//		return graph_ids;
//	}
	
	public static ArrayList<String> ReadFile(String filename, int limit)
	{
		ArrayList<String> lines = new ArrayList<String>();
		
		File file = new File(filename);
		BufferedReader reader = null;
		
		try
		{
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			if(limit == -1)
				while((tempString = reader.readLine())!=null)
				{
					lines.add(tempString);
				}
			else {
				for (int i = 0; i < limit; i++)
				{
					tempString = reader.readLine();
					lines.add(tempString);
				}
			}
			
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(reader!=null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e)
				{					
				}
			}
		}
		return lines;
	}
	
	public static void WriteFile(String filename, boolean app, ArrayList<String> lines)
	{
		try 
		{
			FileWriter fw = new FileWriter(filename,app);
			for(int i = 0;i<lines.size();i++)
			{
				fw.write(lines.get(i)+"\n");
			}
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void WriteFile(String filename, boolean app, String str)
	{
		try 
		{
			FileWriter fw = new FileWriter(filename,app);
			fw.write(str);
			fw.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public static long getDirSize(File file) {     
        if (file.exists()) {     
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                long size = 0;     
                for (File f : children)     
                    size += getDirSize(f);     
                return size;     
            } else {
            	long size = file.length(); 
                return size;     
            }     
        } else {     
            System.out.println("File not exists!");     
            return 0;     
        }     
    }
	
	public static int GetNodeCount(String datasource)
	{
		int node_count = 0;
		File file = null;
		BufferedReader reader = null;
		try
		{
			file = new File("/home/yuhansun/Documents/Real_data/"+datasource+"/graph.txt");
			reader = new BufferedReader(new FileReader(file));
			String str = reader.readLine();
			String[] l = str.split(" ");
			node_count = Integer.parseInt(l[0]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return node_count;		
	}
	
	public static String ClearCache()
	{
		//String[] command = {"/bin/bash","-c","echo data| sudo -S ls"};
		String []cmd = {"/bin/bash","-c","echo data | sudo -S sh -c \"sync; echo 3 > /proc/sys/vm/drop_caches\""};
		String result = null;
		try 
		{
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) 
	        {  
	            sb.append(line).append("\n");  
	        }  
	        result = sb.toString();
	        result+="\n";
	        
        }   
		catch (Exception e) 
		{  
			e.printStackTrace();
        }
		return result;
	}
	
//	public static String RestartNeo4jClearCache(String datasource)
//	{
//		String result = "";
//		result += Neo4j_Graph_Store.StopMyServer(datasource);
//		result += ClearCache();
//		result += Neo4j_Graph_Store.StartMyServer(datasource);
//		return result;
//	}
	
	public static String Serialize_RoarBitmap_ToString(RoaringBitmap r)
	{
		r.runOptimize();
				
		ByteBuffer outbb = ByteBuffer.allocate(r.serializedSizeInBytes());
        // If there were runs of consecutive values, you could
        // call mrb.runOptimize(); to improve compression 
        try {
			r.serialize(new DataOutputStream(new OutputStream(){
			    ByteBuffer mBB;
			    OutputStream init(ByteBuffer mbb) {mBB=mbb; return this;}
			    public void close() {}
			    public void flush() {}
			    public void write(int b) {
			        mBB.put((byte) b);}
			    public void write(byte[] b) {mBB.put(b);}            
			    public void write(byte[] b, int off, int l) {mBB.put(b,off,l);}
			}.init(outbb)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //
        outbb.flip();
        String serializedstring = Base64.getEncoder().encodeToString(outbb.array());
        return serializedstring;
	}
	
	public static ImmutableRoaringBitmap Deserialize_String_ToRoarBitmap(String serializedstring)
	{
		ByteBuffer newbb = ByteBuffer.wrap(Base64.getDecoder().decode(serializedstring));
	    ImmutableRoaringBitmap ir = new ImmutableRoaringBitmap(newbb);
	    return ir;
	}

	public static void Print(Object o)
	{
		System.out.println(o);
	}

}
