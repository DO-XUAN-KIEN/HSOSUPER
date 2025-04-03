/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package event;

import core.*;
import io.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import template.ItemTemplate4;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class EventManager {
	public static final short[][] item_leave = new short[][] {new short[] {118, 119, 120, 121, 122, 153, 154, 155, 156}, // event
			// 1
			new short[] {}, // event 2
			new short[] {}, // event 3
			new short[] {29, 30, 89, 28} // event 4
	};
	public String name;
	public static ArrayList<PlayerRegister> registerList = new ArrayList<>();
	public static int time = 0;
	public static EventManager eventManager;
	public static final short[][] item_drop = new short[][]{
			new short[]{29, 30, 89, 28}, // Tết
			new short[]{118, 119, 120, 121, 122, 153, 154, 155, 156}, // noel
			new short[]{},
			new short[]{},
			new short[]{29, 30, 89, 28},// thông tin ăn vào đây
	};

	public synchronized void start() {
		try {
			try {
				LunarNewYear.list_nhan_banh.clear();
				LunarNewYear.runing = true;
				Manager.gI().chatKTGprocess("Bắt đầu nấu bánh, các bạn có thể tăng tốc nấu");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			time = 120;
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public synchronized static void update(int t) throws IOException {
		if (time != 0) {
			time -= t;
			System.out.println("Thời gian nấu : " + time);
			if (time <= 0) {
				time = 0;
				finish();
			}
		}
	}

	public synchronized static void finish() throws IOException {
		for (PlayerRegister playerRegister : registerList) {
			playerRegister.setTotal();
		}
		registerList.sort(Comparator.comparingInt(playerRegister -> -playerRegister.total));
		Manager.gI().chatKTGprocess("Thời gian nấu bánh đã xong");
		LunarNewYear.finish();
	}

	public static void send_info(Session conn) throws IOException {
		try {
			EventManager.PlayerRegister player = EventManager.getPlayer(EventManager.registerList, conn.p.name);
			if (player != null) {
				String txt = "";
				for (int i = 0; i < item_drop[Manager.gI().event].length; i++) {
					ItemTemplate4 it = ItemTemplate4.item.get(item_drop[Manager.gI().event][i]);
					txt += it.getName() + " " + player.material[i] + "\n";
				}
				Service.send_notice_box(conn, txt);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void top_event(Session conn) throws IOException {
		try (Connection connection = SQL.gI().getConnection();
			 PreparedStatement psSelect = connection.prepareStatement("SELECT `name`,`doiqua` FROM `player` WHERE `doiqua` > 100 ORDER BY `doiqua` DESC LIMIT 10;")) {
			try (ResultSet rs = psSelect.executeQuery()) {
				List<String> txt = new ArrayList<>();
				boolean inTop = false;
				int index = 0;
				while (rs.next()) {
					String top = "Hạng " + ++index + ": ";
					String name = rs.getString("name");
					if (name.equals(conn.p.name) || conn.ac_admin > 3) {
						name += " " + rs.getString("doiqua");
						inTop = true;
					}
					txt.add(top + name);
				}
				if (!inTop) {
					txt.add("..." + conn.p.name + " " + conn.p.doiqua);
				}
				if (!txt.isEmpty()) {
					String[] myArray = txt.toArray(new String[txt.size()]);
					MenuController.send_menu_select(conn, -66, myArray);
				} else {
					Service.send_notice_box(conn, "Chưa có ai trong top");
				}
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void top_event1(Session conn) throws IOException {
		try (Connection connection = SQL.gI().getConnection();
			 PreparedStatement psSelect = connection.prepareStatement("SELECT `name`,`sk_hongio` FROM `player` WHERE `sk_hongio` > 10 ORDER BY `sk_hongio` DESC LIMIT 10;")) {
			try (ResultSet rs = psSelect.executeQuery()) {
				List<String> txt = new ArrayList<>();
				boolean inTop = false;
				int index = 0;
				while (rs.next()) {
					String top = "Hạng " + ++index + ": ";
					String name = rs.getString("name");
					if (name.equals(conn.p.name) || conn.ac_admin > 3) {
						name += " " + rs.getString("sk_hongio");
						inTop = true;
					}
					txt.add(top + name);
				}
				if (!inTop) {
					txt.add("..." + conn.p.name + " " + conn.p.sk_hongio);
				}
				if (!txt.isEmpty()) {
					String[] myArray = txt.toArray(new String[txt.size()]);
					MenuController.send_menu_select(conn, -66, myArray);
				} else {
					Service.send_notice_box(conn, "Chưa có ai trong top");
				}
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public synchronized static boolean check(ArrayList<PlayerRegister> list, String name) {
		for (PlayerRegister p : list) {
			if (p.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static PlayerRegister getPlayer(ArrayList<PlayerRegister> list, String name) {
		for (PlayerRegister p : list) {
			if (p.name.equals(name)) {
				return p;
			}
		}
		return null;
	}

	public static JSONArray save(ArrayList<PlayerRegister> list) {
		synchronized (list) {
			JSONArray jsar = new JSONArray();
			for (PlayerRegister playerRegister : list) {
				JSONArray jsar_1 = new JSONArray();
				jsar_1.add(playerRegister.name);
				for (short quant : playerRegister.material) {
					jsar_1.add(quant);
				}
				jsar_1.add(playerRegister.total);
				jsar_1.add(playerRegister.rank);
				jsar.add(jsar_1);
			}
			return jsar;
		}
	}

	public synchronized static void loadDatabase(JSONObject jsob) {
		registerList = new ArrayList<>();
		JSONArray jsar = (JSONArray) JSONValue.parse(jsob.get("register").toString());
		for (Object o : jsar) {
			JSONArray jsar_1 = (JSONArray) o;

			String name = (String) jsar_1.get(0);

			short[] material = new short[4];
			for (int i = 0; i < 4; i++) {
				material[i] = ((Long) jsar_1.get(i + 1)).shortValue();
			}

			short total = ((Long) jsar_1.get(5)).shortValue();
			int rank = ((Long) jsar_1.get(6)).intValue();

			PlayerRegister playerRegister = new PlayerRegister(name);
			playerRegister.material = material;
			playerRegister.total = total;
			playerRegister.rank = rank;
			registerList.add(playerRegister);
		}
		jsar.clear();

		LunarNewYear.list_nhan_banh = new ArrayList<>();
		jsar = (JSONArray) JSONValue.parse(jsob.get("receive").toString());
		for (Object o : jsar) {
			JSONArray jsar_1 = (JSONArray) o;

			String name = (String) jsar_1.get(0);

			short[] material = new short[4];
			for (int i = 0; i < 4; i++) {
				material[i] = ((Long) jsar_1.get(i + 1)).shortValue();
			}

			short total = ((Long) jsar_1.get(5)).shortValue();
			int rank = ((Long) jsar_1.get(6)).intValue();

			PlayerRegister playerRegister = new PlayerRegister(name);
			playerRegister.material = material;
			playerRegister.total = total;
			playerRegister.rank = rank;
			LunarNewYear.list_nhan_banh.add(playerRegister);
		}
		jsar.clear();
	}

	public static class PlayerRegister {
		public String name;
		public short[] material;
		public short total;
		public int rank;

		public PlayerRegister(String name) {
			this.name = name;
			this.material = new short[4];
			this.total = 0;
			this.rank = -1;
		}

		public void setTotal() {
			int minValue = material[0];
			for (int i = 1; i < material.length; i++) {
				if (material[i] < minValue) {
					minValue = material[i];
				}
			}
			this.total = (short) (minValue);
		}
	}
}
