package ev_he;

import client.Player;
import core.Manager;
import core.Util;
import io.Message;
import io.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import map.Map;

public class Mobkhu2 {
    private static final CopyOnWriteArrayList<Mob_khu2> mobTrees = new CopyOnWriteArrayList<>();
    public static long timeCreate;
    public static Mob_khu2 getMob(int idx){
        for(Mob_khu2 m : mobTrees){
            if(m.index == idx )
                return m;
        }
        return null;
    }
    public static void taoMob() {
        long time = System.currentTimeMillis();
        timeCreate = time;
        try {
            // Tạo danh sách ID map hợp lệ từ 1-98
            List<Integer> validMapIds = new ArrayList<>();
            for (int i = 1; i <= 98; i++) {
                Map m = Map.get_id(i); // Giả sử Map.getById() lấy map dựa trên ID
                if (m != null && !m.ismaplang && !m.showhs && m.typemap == 0 && !Map.is_map_cant_save_site(m.map_id) && !(m.map_id == 49 || m.map_id == 81)) {
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
                Map[] maps = Map.get_map_by_id(mapId);
                Map m = maps[1];
                if (m == null) continue;
                // Số lượng Mob ngẫu nhiên trên mỗi map (1-3 Mob)
                int mobCount = Util.random(1, 3);
                for (int j = 0; j < mobCount; j++) {
                    short index = (short) (30000 + mobTrees.size());
                    Mob_khu2 plot = new Mob_khu2(m, index);
                    mobTrees.add(plot);
                    System.out.println("Spawn Mob tại map111: " + m.name);
                }
            }
            Manager.gI().chatKTGprocess("Ông già Noel đã xuất hiện, hãy nhanh chân lên nào!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class MobFarm
    public static class Mob_khu2 {

        public long timeUpdate;
        public short index;
        public String name = "";
        public String nameOwner = "";
        public Player Owner;
        public Map map;
        public short x, y;

        public Mob_khu2(Map map, short idx) {
            timeUpdate = System.currentTimeMillis();
            this.map = map;
            this.index = idx;
            x= (short)(Util.random(map.mapW)*24);
            y= (short)(Util.random(map.mapH)*24);
            map.mobkhu2.add(this);
        }


        public void SendMob(Session conn) throws IOException {
            Message m = new Message(4);
            m.writer().writeByte(1);
            m.writer().writeShort(168);
            m.writer().writeShort(index);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeByte(-1);
            conn.addmsg(m);
            m.cleanup();
            m = new Message(7);
            m.writer().writeShort(index);
            m.writer().writeByte(40);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeInt(1);
            m.writer().writeInt(1);
            m.writer().writeByte(0);
            m.writer().writeInt(-2);
            m.writer().writeShort(-1);

            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeByte(0);
            m.writer().writeUTF(nameOwner);
            m.writer().writeLong(-11111);
            m.writer().writeByte(0);
            conn.addmsg(m);
            m.cleanup();
        }
        public void setOwner(Player p)throws IOException{
            if(p==null)return;
            nameOwner = p.name;
            Owner = p;
            MobLeave();
        }
        public void MobLeave() throws IOException {
            Message m = new Message(17);
            m.writer().writeShort(Owner == null ? -1 : Owner.index);
            m.writer().writeShort(index);
            for (Player player : map.players) {
                if (player != null && player.conn != null && player.conn.connected) {
                    player.conn.addmsg(m);
                }
            }
            m.cleanup();
            map.mobkhu2.remove(this);
        }
    }
    public static void ClearMob(){
        synchronized(mobTrees){
            for(Mob_khu2 mob:mobTrees)
            {
                try{
                    mob.MobLeave();
                }
                catch(Exception e){}
            }
            mobTrees.clear();
        }
    }
    public static void Update(){
        try{
            long time = System.currentTimeMillis();
            if(time - timeCreate > 1000 * 60 * 28 && !mobTrees.isEmpty())
                ClearMob();
            if(time - timeCreate > 1000 * 60 * Util.random(30,40))
                taoMob();
        }catch(Exception e){}
    }
}