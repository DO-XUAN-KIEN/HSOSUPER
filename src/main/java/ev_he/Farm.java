//package ev_he;
//
//import client.Player;
//import core.Manager;
//import core.Util;
//import ev_he.Farm.MobFarm;
//import io.Message;
//import io.Session;
//import java.io.IOException;
//import java.util.concurrent.CopyOnWriteArrayList;
//import map.Map;
//
//public class Farm {
//
//    private static final CopyOnWriteArrayList<MobFarm> mobTrees = new CopyOnWriteArrayList<>();
//
//    public static MobFarm getMob(int idx){
//        for(MobFarm m : mobTrees){
//            if(m.index == idx )
//                return m;
//        }
//        return null;
//    }
//
//    public static void taoMob() {
//        try {
//            Map[] maps = Map.get_map_by_id(1);
//            if (maps == null || maps.length == 0) {
//                System.out.println("Map ID 1 không tồn tại.");
//                return;
//            }
//            Map map = maps[0];
//            for (int i = 0; i < 5; i++) { // Tạo 5 ô đất
//                short index = (short) (30000 + mobTrees.size());
//                MobFarm plot = new MobFarm(map, index);
//                plot.nameOwner = "Ô Đất Trống";
//                plot.x = (short) (198 + i * 50); // Khoảng cách giữa các ô
//                plot.y = 288;
//                mobTrees.add(plot);
//                for (Player player : map.players) {
//                    if (player != null && player.conn != null && player.conn.connected) {
//                        plot.SendMob(player.conn);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void clearMobTrees() {
//        try {
//            for (MobFarm mobTree : mobTrees) {
//                mobTree.MobLeave();
//            }
//            mobTrees.clear();
//            System.out.println("Tất cả mob cây đã được xóa.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    // Class MobFarm
//    public static class MobFarm {
//
//        public long timeUpdate;
//        public short index;
//        public String name = "";
//        public String nameOwner = "";
//        public Player Owner;
//        public boolean isRemove;
//        public Map map;
//        public short x, y;
//        public short currentEff = 109;
//
//        public MobFarm(Map map, short idx) {
//            this.timeUpdate = System.currentTimeMillis();
//            this.map = map;
//            this.index = idx;
//            this.x = (short) (Util.random(map.mapW) * 24);
//            this.y = (short) (Util.random(map.mapH) * 24);
//            this.nameOwner = "Ô Đất Trống";
//            this.name = "";
//            map.mobfarm.add(this);
//        }
//
//
//        public void SendMob(Session conn) throws IOException {
//            if (!conn.p.isShowMobEvents) return;
//            Message m = new Message(4);
//            m.writer().writeByte(1);
//            m.writer().writeShort(150);
//            m.writer().writeShort(index);
//            m.writer().writeShort(x);
//            m.writer().writeShort(y);
//            m.writer().writeByte(-1);
//            for (Player player : map.players) {
//                if (player != null && player.conn != null && player.conn.connected) {
//                    player.conn.addmsg(m);
//                }
//            }
//            m.cleanup();// Gửi thông tin bổ sung
//            m = new Message(7);
//            m.writer().writeShort(index);
//            m.writer().writeByte(10); // Lv
//            m.writer().writeShort(x);
//            m.writer().writeShort(y);
//            m.writer().writeInt(10); // hp
//            m.writer().writeInt(10); // hp max
//            m.writer().writeByte(0);
//            m.writer().writeInt(-2);
//            m.writer().writeShort(-1);
//
//            m.writer().writeByte(1);
//            m.writer().writeByte(1);
//            m.writer().writeByte(0);
//            m.writer().writeUTF(nameOwner);
//            m.writer().writeLong(-11111);
//            m.writer().writeByte(0);
//            for (Player player : map.players) {
//                if (player != null && player.conn != null && player.conn.connected) {
//                    player.conn.addmsg(m);
//                }
//            }
//            m.cleanup();
//
//            this.SendEffMob(conn, (byte) this.currentEff);
//        }
//
//
//        public void SendEffMob(Session conn, byte id_eff) throws IOException {
//            if (!conn.p.isShowMobEvents) {
//                return;
//            }
//
//            Message removeEff = new Message(-49);
//            removeEff.writer().writeByte(1);
//            removeEff.writer().writeShort(this.index);
//            removeEff.writer().writeByte(0);
//            for (Player player : this.map.players) {
//                if (player != null && player.conn != null && player.conn.connected) {
//                    player.conn.addmsg(removeEff);
//                }
//            }
//            removeEff.cleanup();
//
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            this.currentEff = id_eff;
//
//
//            Message m = new Message(-49);
//            byte[] data = Util.loadfile("data/part_char/imgver/x" + conn.zoomlv + "/Data/" + (111 + "_" + id_eff));
//            m.writer().writeByte(0);
//            m.writer().writeShort(data.length);
//            m.writer().write(data);
//            m.writer().writeByte(0);
//            m.writer().writeByte(1);
//            m.writer().writeByte(id_eff);
//            m.writer().writeShort(this.index);
//            m.writer().writeByte(1);
//            m.writer().writeByte(0);
//            m.writer().writeShort(1);
//            m.writer().writeByte(0);
//            for (Player player : this.map.players) {
//                if (player != null && player.conn != null && player.conn.connected) {
//                    player.conn.addmsg(m);
//                }
//            }
//            m.cleanup();
//        }
//
//        public void MobLeave() throws IOException {
//            Message m = new Message(17);
//            m.writer().writeShort(Owner == null ? -1 : Owner.index);
//            m.writer().writeShort(index);
//            for (Player player : map.players) {
//                if (player != null && player.conn != null && player.conn.connected) {
//                    player.conn.addmsg(m);
//                }
//            }
//            m.cleanup();
//            map.mobfarm.remove(this);
//        }
//    }
//}