package yelp;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import commons.OwnMethods;

/**
 * Hello world!
 *
 */
public class App {
	private static BufferedReader reader;
	private static String temp;

	public static void GetUserID() {
		String filepath = "D:\\yelp_dataset_challenge_academic_dataset";
		String result_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\user_id.txt";
		BufferedReader reader = null;
		FileWriter fw = null;
		String temp = null;

		try {
			int current_id = 0;
			reader = new BufferedReader(new FileReader(new File(filepath)));
			fw = new FileWriter(result_path);
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_user.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			String id = jsonObject.get("user_id").getAsString();
			fw.write(String.format("%d\t%s\n", current_id, id));
			current_id++;

			while (true) {
				temp = reader.readLine();
				if (temp.contains("average_stars")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());
					jsonObject = (JsonObject) jsonParser.parse(temp);
					id = jsonObject.get("user_id").getAsString();
					fw.write(String.format("%d\t%s\n", current_id, id));
					current_id++;
				} else {
					break;
				}

			}
			reader.close();
			fw.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void GetBusinessID() {
		String yelp_dataset_path = "D:\\yelp_dataset_challenge_academic_dataset";
		String result_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\business_id.txt";
		BufferedReader reader = null;
		FileWriter fw = null;
		String temp = null;

		try {
			int current_id = 0;
			reader = new BufferedReader(new FileReader(new File(
					yelp_dataset_path)));
			fw = new FileWriter(result_path);
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_business.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			String id = jsonObject.get("business_id").getAsString();
			fw.write(String.format("%d\t%s\n", current_id + 552339, id));
			current_id++;

			while (true) {
				temp = reader.readLine();
				if (temp.contains("full_address")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());
					jsonObject = (JsonObject) jsonParser.parse(temp);
					id = jsonObject.get("business_id").getAsString();
					fw.write(String.format("%d\t%s\n", current_id + 552339, id));
					current_id++;
				} else {
					break;
				}

			}
			reader.close();
			fw.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void BuildFriend() {
		String yelp_dataset_path = "D:\\yelp_dataset_challenge_academic_dataset";
		String users_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\user_id.txt";
		String friend_graph_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\friend_graph.txt";
		HashMap<String, Integer> user_map = ReadUserMap(users_path);
		BufferedReader reader = null;
		FileWriter fw = null;
		String temp = null;

		try {
			reader = new BufferedReader(new FileReader(new File(
					yelp_dataset_path)));
			fw = new FileWriter(friend_graph_path);
			fw.write(user_map.size() + "\n");
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_user.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			String id = jsonObject.get("user_id").getAsString();
			int current_id = user_map.get(id);

			JsonArray jsonArray = jsonObject.get("friends").getAsJsonArray();
			fw.write(String.format("%d %d", current_id, jsonArray.size()));
			for (JsonElement jsonElement : jsonArray) {
				String friend_id = jsonElement.getAsString();
				int friend_index = user_map.get(friend_id);
				fw.write(" " + friend_index);
			}
			fw.write("\n");

			while (true) {
				temp = reader.readLine();
				if (temp.contains("average_stars")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());
					jsonParser = new JsonParser();
					jsonObject = (JsonObject) jsonParser.parse(temp);
					id = jsonObject.get("user_id").getAsString();
					current_id = user_map.get(id);

					jsonArray = jsonObject.get("friends").getAsJsonArray();
					fw.write(String.format("%d %d", current_id,
							jsonArray.size()));
					for (JsonElement jsonElement : jsonArray) {
						String friend_id = jsonElement.getAsString();
						int friend_index = user_map.get(friend_id);
						fw.write(" " + friend_index);
					}
					fw.write("\n");
				} else
					break;

			}
			reader.close();
			fw.close();

		} catch (Exception e) {
			// 
			OwnMethods.Print(temp);
			OwnMethods.Print(user_map.size());
			e.printStackTrace();
		}
	}

	public static void GetGraph() {
		String yelp_dataset_path = "D:\\yelp_dataset_challenge_academic_dataset";
		String users_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\user_id.txt";
		String business_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\business_id.txt";
		String graph_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\graph_entity.txt";
		HashMap<String, Integer> user_map = ReadUserMap(users_path);
		HashMap<String, Integer> business_map = ReadBusinessMap(business_path);

		ArrayList<HashSet<Integer>> graph = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < (user_map.size() + business_map.size()); i++) {
			HashSet<Integer> hs = new HashSet<Integer>();
			graph.add(hs);
		}

		FileWriter fw = null;
		String temp = null;

		// friend edge
		try {
			reader = new BufferedReader(new FileReader(new File(
					yelp_dataset_path)));
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_user.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			String id = jsonObject.get("user_id").getAsString();
			int current_id = user_map.get(id);

			JsonArray jsonArray = jsonObject.get("friends").getAsJsonArray();

			for (JsonElement jsonElement : jsonArray) {
				String friend_id = jsonElement.getAsString();
				int friend_index = user_map.get(friend_id);
				graph.get(current_id).add(friend_index);
			}

			while (true) {
				temp = reader.readLine();
				if (temp.contains("average_stars")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());
					jsonParser = new JsonParser();
					jsonObject = (JsonObject) jsonParser.parse(temp);
					id = jsonObject.get("user_id").getAsString();
					current_id = user_map.get(id);

					jsonArray = jsonObject.get("friends").getAsJsonArray();
					for (JsonElement jsonElement : jsonArray) {
						String friend_id = jsonElement.getAsString();
						int friend_index = user_map.get(friend_id);
						graph.get(current_id).add(friend_index);
					}
				} else
					break;

			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// review edge
		try {
			reader = new BufferedReader(new FileReader(new File(
					yelp_dataset_path)));
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_review.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			String user_id = jsonObject.get("user_id").getAsString();
			int user_index = user_map.get(user_id);

			String business_id = jsonObject.get("business_id").getAsString();
			int business_index = business_map.get(business_id);

			graph.get(user_index).add(business_index);

			while (true) {
				temp = reader.readLine();
				if (!temp.contains("yelp_academic_dataset")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());

					jsonParser = new JsonParser();
					jsonObject = (JsonObject) jsonParser.parse(temp);
					user_id = jsonObject.get("user_id").getAsString();
					user_index = user_map.get(user_id);

					business_id = jsonObject.get("business_id").getAsString();
					business_index = business_map.get(business_id);

					graph.get(user_index).add(business_index);
				} else
					break;

			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		// tip edge
		try {
			reader = new BufferedReader(new FileReader(new File(
					yelp_dataset_path)));
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_tip.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());

			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			String user_id = jsonObject.get("user_id").getAsString();
			int user_index = user_map.get(user_id);

			String business_id = jsonObject.get("business_id").getAsString();
			int business_index = business_map.get(business_id);

			graph.get(user_index).add(business_index);

			while (true) {
				temp = reader.readLine();
				if (!temp.contains("yelp_academic_dataset")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());

					jsonParser = new JsonParser();
					jsonObject = (JsonObject) jsonParser.parse(temp);
					user_id = jsonObject.get("user_id").getAsString();
					user_index = user_map.get(user_id);

					business_id = jsonObject.get("business_id").getAsString();
					business_index = business_map.get(business_id);

					graph.get(user_index).add(business_index);
				} else
					break;

			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//write graph
		try {
			fw = new FileWriter(graph_path);
			fw.write(graph.size()+"\n");
			int i = 0;
			for(HashSet<Integer> hashSet : graph)
			{
				fw.write(String.format("%d %d", i, hashSet.size()));
				for(Integer out_id : hashSet)
				{
					fw.write(String.format(" %d", out_id));
				}
				fw.write("\n");
				i++;
			}
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void GetEntity()
	{
		String yelp_dataset_path = "D:\\yelp_dataset_challenge_academic_dataset";
		String users_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\user_id.txt";
		String business_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\business_id.txt";
		HashMap<String, Integer> userHashMap = ReadUserMap(users_path);
		HashMap<String, Integer> businessHashMap = ReadBusinessMap(business_path);
		FileWriter fw = null;
		
		String entity_path = "D:\\Ubuntu_shared\\Real_Data\\Yelp\\new_entity.txt";
		try {
			
			fw = new FileWriter(entity_path);
			fw.write(String.format("%d\n", userHashMap.size() + businessHashMap.size()));
			for(int i = 0;i<userHashMap.size();i++)
			{
				fw.write(String.format("%d,0\n", i));
			}
			
			reader = new BufferedReader(new FileReader(new File(yelp_dataset_path)));
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_business.json"))
					break;
			}

			int pos_start = temp.indexOf("{");
			temp = temp.substring(pos_start, temp.length());
			
			JsonParser jsonParser = new JsonParser();
			JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			
			String business_id = jsonObject.get("business_id").getAsString();
			int business_index = businessHashMap.get(business_id);
			String longitude = jsonObject.get("longitude").getAsString();
			String latitude = jsonObject.get("latitude").getAsString();
			
			fw.write(String.format("%d,1,%s,%s\n", business_index, longitude, latitude));
			
			while (true) {
				temp = reader.readLine();
				if (!temp.contains("yelp_academic_dataset")) {
					pos_start = temp.indexOf("{");
					temp = temp.substring(pos_start, temp.length());
					
					jsonParser = new JsonParser();
					jsonObject = (JsonObject) jsonParser.parse(temp);
					
					business_id = jsonObject.get("business_id").getAsString();
					business_index = businessHashMap.get(business_id);
					longitude = jsonObject.get("longitude").getAsString();
					latitude = jsonObject.get("latitude").getAsString();
					
					fw.write(String.format("%d,1,%s,%s\n", business_index, longitude, latitude));
				} else {
					break;
				}
			}
			reader.close();
			fw.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	/**
	 * @param users_path
	 * @return
	 */
	public static HashMap<String, Integer> ReadUserMap(String users_path) {
		HashMap<String, Integer> usersmap = new HashMap<String, Integer>();
		BufferedReader reader = null;
		temp = null;
		try {
			reader = new BufferedReader(new FileReader(new File(users_path)));
			while((temp = reader.readLine())!=null)
			{
				String[] l = temp.split("\t");
				int id = Integer.parseInt(l[0]);
				String str_id = l[1];
				usersmap.put(str_id, id);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return usersmap;
	}

	public static HashMap<String, Integer> ReadBusinessMap(
			String business_pathString) {
		HashMap<String, Integer> businessMap = new HashMap<String, Integer>();
		reader = null;
		String temp = null;
		try {
			reader = new BufferedReader(new FileReader(new File(
					business_pathString)));
			while ((temp = reader.readLine()) != null) {
				String[] l = temp.split("\t");
				int id = Integer.parseInt(l[0]);
				String str_id = l[1];
				businessMap.put(str_id, id);
			}
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return businessMap;
	}

	public static void app() {
		// String x =
		ArrayList<HashSet<Integer>> test = new ArrayList<HashSet<Integer>>();
		HashSet<Integer> hs = new HashSet<Integer>();
		hs.add(0);
		hs.add(10);
		test.add(hs);
		test.get(0).add(100);
		for (Integer iter : test.get(0)) {
			OwnMethods.Print(iter);
		}
	}

	public static int GetReviewCount() {
		String filepath = "D:\\yelp_dataset_challenge_academic_dataset";
		BufferedReader reader = null;
		String temp = null;

		try {
			int count = 0;
			reader = new BufferedReader(new FileReader(new File(filepath)));
			while (true) {
				temp = reader.readLine();
				if (temp.contains("yelp_academic_dataset_review.json")) {
					count++;
					break;
				}
			}

			while (true) {
				temp = reader.readLine();
				if (!temp.contains("yelp_academic_dataset")) {
					count++;
				} else {
					break;
				}
			}
			OwnMethods.Print(count);
			// int pos_start = temp.indexOf("{");
			// temp = temp.substring(pos_start, temp.length());
			// JsonParser jsonParser = new JsonParser();
			// JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
			// String id = jsonObject.get("user_id").getAsString();
			// fw.write(String.format("%d\t%s\n", current_id, id));
			// current_id ++ ;
			//
			// while(true)
			// {
			// temp = reader.readLine();
			// if(temp.contains("average_stars"))
			// {
			// pos_start = temp.indexOf("{");
			// temp = temp.substring(pos_start, temp.length());
			// jsonObject = (JsonObject) jsonParser.parse(temp);
			// id = jsonObject.get("user_id").getAsString();
			// fw.write(String.format("%d\t%s\n", current_id, id));
			// current_id ++ ;
			// }
			// else {
			// break;
			// }
			//
			// }
			reader.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args) {
		
		GetEntity();
		GetGraph();
		
		// app();
//		 BuildFriend();
		// GetUserID();
		// GetBusinessID();
		// String filepath = "D:\\yelp_dataset_challenge_academic_dataset";
		// BufferedReader reader = null;
		// String temp = null;
		//
		// try {
		// int count = 0;
		// reader = new BufferedReader(new FileReader( new File(filepath)));
		// // for (int i = 0;i<10000;i++)
		// while(true)
		// {
		// temp = reader.readLine();
		// // OwnMethods.Print(temp);
		// // if(temp.contains("business_id"))
		// if(temp.contains("average_stars"))
		// {
		// int pos_start = temp.indexOf("{");
		// temp = temp.substring(pos_start, temp.length());
		// // String[] l = temp.split(":");
		// // for(int j = 0; j<l.length;j++)
		// // {
		// // OwnMethods.Print(temp);
		// // OwnMethods.Print(String.format("%d\t%s", j, l[j]));
		// // }
		// JsonParser jsonParser = new JsonParser();
		// JsonObject jsonObject = (JsonObject) jsonParser.parse(temp);
		// OwnMethods.Print(jsonObject.toString());
		// String id = jsonObject.get("user_id").getAsString();
		// OwnMethods.Print(id);
		// count++;
		// if(count == 2)
		// break;
		// }
		//
		// }
		// reader.close();
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
	}
}
