package ev_he;

import client.Player;
import core.Util;
import io.Message;
import io.Session;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import map.Map;

public class Mobngoc {
    private static final CopyOnWriteArrayList<Mob_Ngoc> mobTrees = new CopyOnWriteArrayList<>();
    public static long timeCreate;
    public static boolean runing = false;
    public static Mob_Ngoc getMob(int idx){
        for(Mob_Ngoc m : mobTrees){
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
                Map[] maps = Map.get_map_by_id(49);
                Map map = maps[0];
                short index = (short) (30000 + mobTrees.size());
                Mob_Ngoc plot = new Mob_Ngoc(map, index);
                plot.x = 504; // Khoảng cách giữa các ô
                plot.y = 352;
                mobTrees.add(plot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Class MobFarm
    public static class Mob_Ngoc {

        public long timeUpdate;
        public short index;
        public String name = "";
        public String nameOwner = "";
        public Player Owner;
        public Map map;
        public short x, y;
        public short currentEff = 109;

        public Mob_Ngoc(Map map, short idx) {
            timeUpdate = System.currentTimeMillis();
            this.map = map;
            this.index = idx;
            x= (short)(Util.random(map.mapW)*24);
            y= (short)(Util.random(map.mapH)*24);
            map.mobNgoc.add(this);
        }


        public void SendMob(Session conn) throws IOException {
            Message m = new Message(4);
            m.writer().writeByte(1);
            m.writer().writeShort(167);
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
            //updateMobInsiders();
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
            map.mobNgoc.remove(this);
        }
    }
    public static void ClearMob(){
        synchronized(mobTrees){
            for(Mob_Ngoc mob:mobTrees)
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
            if(time - timeCreate > 1000 * 60 * 2 && !mobTrees.isEmpty())
                ClearMob();
            if(time - timeCreate > 1000 * 60 * Util.random(3,6))
                taoMob();
        }catch(Exception e){}
    }
}