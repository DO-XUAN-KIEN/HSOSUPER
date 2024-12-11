/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ev_he;

import core.Manager;
import core.Util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import map.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author chien
 */
public class Event_8 {
    private static String name_event = "sự kiện Noel";
    public static final CopyOnWriteArrayList<MobNoel> entrys = new CopyOnWriteArrayList<>();
    public static long timeCreate;
    public static final List<BXH_DoiQua> list_Moqua = new ArrayList<>();

    public static MobNoel getMob(int idx){
        for(MobNoel m : entrys){
            if(m.index == idx )
                return m;
        }
        return null;
    }

    public static void addmob(MobNoel mob){
        synchronized(entrys){
            if(!entrys.contains(mob)){
                entrys.add(mob);
            }
        }
    }
    public static void removemob(MobNoel mob){
        try{
            synchronized(entrys){
                mob.MobLeave();
                entrys.remove(mob);
            }
        }
        catch(Exception e){}

    }
    public static void ClearMob(){
        long time = System.currentTimeMillis();
        synchronized(entrys){
            for(MobNoel mob:entrys)
            {
                try{
                    mob.MobLeave();
                }
                catch(Exception e){}
            }
            entrys.clear();
        }
        if(timeCreate > 0 && (time - timeCreate) / 1000 / 60 > 44){

        }
    }

    public static void ResetMob() throws IOException {
        long time = System.currentTimeMillis();
        timeCreate = time;
        short idx = 30000;

        synchronized (entrys) {
            // Tạo danh sách ID map hợp lệ từ 1-98
            List<Integer> validMapIds = new ArrayList<>();
            for (int i = 1; i <= 98; i++) {
                Map m = Map.get_id(i); // Giả sử Map.getById() lấy map dựa trên ID
                if (m != null && !m.ismaplang && !m.showhs && m.typemap == 0 && !Map.is_map_cant_save_site(m.map_id)) {
                    validMapIds.add(i);
                }
            }

            // Random số lượng map để spawn Mob (ví dụ: từ 5 đến 15 map)
            int numberOfMaps = Util.random(5, 15);
            Set<Integer> selectedMapIds = new HashSet<>();
            while (selectedMapIds.size() < numberOfMaps && !validMapIds.isEmpty()) {
                int randomIndex = Util.random(0, validMapIds.size() - 1);
                selectedMapIds.add(validMapIds.get(randomIndex));
            }

            // Duyệt qua danh sách các map đã chọn và spawn Mob
            for (int mapId : selectedMapIds) {
                Map m = Map.get_id(mapId);
                if (m == null) continue;
                // Số lượng Mob ngẫu nhiên trên mỗi map (1-3 Mob)
                int mobCount = Util.random(1, 3);
                for (int j = 0; j < mobCount; j++) {
                    MobNoel mob = new MobNoel(m, idx);
                    addmob(mob);
                    idx++;
                    System.out.println("Spawn Mob tại map: " + m.name);
                }
            }

            // Thông báo
            //System.out.println("Ông già Noel đã xuất hiện, hãy nhanh chân lên nào!");
            Manager.gI().chatKTGprocess("Ông già Noel đã xuất hiện, hãy nhanh chân lên nào!");
        }
    }
    public static void Update(){
        try{
            long time = System.currentTimeMillis();
            if(time - timeCreate > 1000 * 60 * 28 && !entrys.isEmpty())
                ClearMob();
            if(time - timeCreate > 1000 * 60 * 30)
                ResetMob();
            for(MobNoel mob: entrys){
                mob.update();
            }

        }catch(Exception e){}
    }

    public static class BXH_DoiQua {

        public String name;
        public int quant;
        public long time;

        public BXH_DoiQua(String name2, int integer, long t) {
            name = name2;
            quant = integer;
            time = t;
        }
    }
    public static void add_doiqua(String name, int quant) {
        synchronized(list_Moqua){
            for (int i = 0; i < list_Moqua.size(); i++) {
                if (list_Moqua.get(i).name.equals(name)) {
                    list_Moqua.get(i).quant += quant;
                    list_Moqua.get(i).time = System.currentTimeMillis();
                    return;
                }
            }
            list_Moqua.add(new BXH_DoiQua(name, quant, System.currentTimeMillis()));
        }
    }
    public static void LoadDB(JSONObject jsob){
        synchronized(list_Moqua){
            list_Moqua.clear();
            long t_ = System.currentTimeMillis();
            JSONArray jsar_1 = (JSONArray) JSONValue.parse(jsob.get("list_Moqua").toString());
            for (int i = 0; i < jsar_1.size(); i++) {
                JSONArray jsar_2 = (JSONArray) JSONValue.parse(jsar_1.get(i).toString());
                list_Moqua.add(new BXH_DoiQua(jsar_2.get(0).toString(), Integer.parseInt(jsar_2.get(1).toString()), t_));
            }
            jsar_1.clear();
        }
    }
    public static JSONObject SaveData(){
        synchronized(list_Moqua){
            JSONArray jsar_1 = new JSONArray();
            for (int i = 0; i < list_Moqua.size(); i++) {
                JSONArray jsar_2 = new JSONArray();
                jsar_2.add(list_Moqua.get(i).name);
                jsar_2.add(list_Moqua.get(i).quant);
                jsar_1.add(jsar_2);
            }
            //
            JSONObject jsob = new JSONObject();
            jsob.put("list_Moqua", jsar_1);
            return jsob;
        }
    }
    public static void sort_bxh() {
        synchronized(list_Moqua){
            Collections.sort(list_Moqua, new Comparator<BXH_DoiQua>() {
                @Override
                public int compare(BXH_DoiQua o1, BXH_DoiQua o2) {
                    int compare = (o1.quant == o2.quant) ? 0 : ((o1.quant > o2.quant) ? -1 : 1);
                    if (compare != 0) {
                        return compare;
                    }
                    return (o1.time > o2.time) ? 1 : -1;
                }
            });
        }
    }
    public static String[] get_top() {
        synchronized(list_Moqua){
            if (list_Moqua.isEmpty()) {
                return new String[]{"Chưa có thông tin"};
            }
            String[] top;
            if (list_Moqua.size() < 10) {
                top = new String[list_Moqua.size()];
            } else {
                top = new String[10];
            }
            for (int i = 0; i < top.length; i++) {
                top[i] = "Top " + (i + 1) + " : " + list_Moqua.get(i).name + " : " + list_Moqua.get(i).quant + " lần";
            }
            return top;
        }
    }
}
