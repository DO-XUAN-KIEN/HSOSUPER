package ev_he;

import client.Player;
import core.SQL;
import core.Service;
import core.Util;
import io.Message;
import io.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CopyOnWriteArrayList;
import map.Map;

public class Mobtb2 {
    private static final CopyOnWriteArrayList<Mob_tb2> mobTrees = new CopyOnWriteArrayList<>();
    public static long timeCreate;
    public static boolean runing = false;
    public static boolean check = false;
    public static Mob_tb2 getMob(int idx){
        for(Mob_tb2 m : mobTrees){
            if(m.index == idx )
                return m;
        }
        return null;
    }

    public static void taoMob() {
        if (runing == true) {
            long time = System.currentTimeMillis();
            timeCreate = time;
            try {
                Map[] maps = Map.get_map_by_id(81);
                if (maps == null || maps.length == 0) {
                    System.out.println("Map ID 1 không tồn tại.");
                    return;
                }
                Map map = maps[0];
                for (int i = 0; i <= 20; i++) {// tạo 20 con mob random trên map
                    short index = (short) (30000 + mobTrees.size());
                    //System.out.println(index);
                    Mob_tb2 plot = new Mob_tb2(map, index);
                    plot.nameOwner = "Hãy chọn ta";
                    plot.x = (short) (Util.random(map.mapW) * 24);
                    plot.y = (short) (Util.random(map.mapH) * 24);
                    mobTrees.add(plot);
                    for (Player player : map.players) {
                        if (player != null && player.conn != null && player.conn.connected) {
                            plot.SendMob(player.conn);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Class MobFarm
    public static class Mob_tb2 {

        public long timeUpdate;
        public short index;
        public String name = "";
        public String nameOwner = "";
        public Player Owner;
        public Map map;
        public short x, y;

        public Mob_tb2(Map map, short idx) {
            timeUpdate = System.currentTimeMillis();
            this.map = map;
            this.index = idx;
            x= (short)(Util.random(map.mapW)*24);
            y= (short)(Util.random(map.mapH)*24);
            map.mobTb2.add(this);
        }


        public void SendMob(Session conn) throws IOException {
            Message m = new Message(4);
            m.writer().writeByte(1);
            m.writer().writeShort(169);
            m.writer().writeShort(index);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeByte(-1);
            conn.addmsg(m);
            for (Player player : map.players) {
                if (player != null && player.conn != null && player.conn.connected) {
                    player.conn.addmsg(m);
                }
            }
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
            for (Player player : map.players) {
                if (player != null && player.conn != null && player.conn.connected) {
                    player.conn.addmsg(m);
                }
            }
        }
        public void setOwner(Player p)throws IOException{
            if(p==null)return;
            nameOwner = p.name;
            Owner = p;
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
            map.mobTb2.remove(this);
        }
    }
    public static void ClearMob(){
        synchronized(mobTrees){
            for(Mob_tb2 mob:mobTrees)
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
            if(time - timeCreate > 1000 * 60 * 4 && !mobTrees.isEmpty()) {
                ClearMob();
                check = true;
                //System.out.println("Đã clear hết");
            }
            if(time - timeCreate > 1000 * 60 * 5) {
                try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement()) {
                    st.execute("UPDATE `account` SET `tichdiem` = '" + 0 + "' ;");
                    connection.commit();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    return;
                }
                check = false;
                taoMob();
            }
        }catch(Exception e){}
    }
}