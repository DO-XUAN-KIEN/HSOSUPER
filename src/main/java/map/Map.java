package map;

import ai.Bot;
import ai.MobAi;
import ai.NhanBan;
import ai.Player_Nhan_Ban;

import java.io.IOException;
import java.util.*;

import client.Party;
import client.Pet;
import client.Player;
import client.Squire;
import core.Manager;
import core.Service;
import core.Util;
import ev_he.MobCay;
//import event_daily.LoiDai;
import ev_he.MobNoel;
import event_daily.KingCup;
import event_daily.UseItemArena;
import event_daily.ChienTruong;
import io.Message;
import io.Session;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import java.util.concurrent.CopyOnWriteArrayList;

import template.Item3;
import template.Item47;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.MainObject;
import template.Mob_MoTaiNguyen;
import template.NpcTemplate;
import template.Option_pet;
import template.StrucEff;

public class Map implements Runnable {

    public static final List<Map[]> entrys = new ArrayList<>();
    public final List<Player> players;
    public long time_use_item_arena;
    public final short map_id;
    public final byte zone_id;
    public final ItemMap[] item_map;
    private final Thread mapthread;
    public Mob_in_map[] mobs;
    public static short head;
    public static short eye;
    public static short hair;
    public static short weapon;
    public static short body;
    public static short leg;
    public static short hat;
    public static short wing;
    public static String name_mo = "";
    public final String[] npc_name_data;
    public final String name;
    public final List<Vgo> vgos;
    public final byte typemap;
    public final boolean ismaplang;
    public final boolean showhs;
    public final short maxplayer;
    public final byte maxzone;
    private final byte[] map_data;
    public int baseID = -1001;
    private boolean running;
    public Dungeon d;
    public short mapW;
    public short mapH;
    public long time_ct;
    public long time_chat;
    public CopyOnWriteArrayList<MobCay> mobEvens = new CopyOnWriteArrayList<>();
    public CopyOnWriteArrayList<MobNoel> mobnoel = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<Mob_in_map> Boss_entrys = new CopyOnWriteArrayList<>();
    public final CopyOnWriteArrayList<MobAi> Ai_entrys;
    public List<Bot> bots;
    public long time_add_bot;
    public UseItemArena Arena;
    public KingCup kingCupMap;
    public Leo_thap leot;
    public boolean mapsk;

    public Map(int id, int zone, String[] npc_name, String name, byte typemap, boolean ismaplang, boolean showhs,
            int maxplayer, int maxzone, List<Vgo> vgo) throws IOException {
        this.map_id = (short) id;
        this.zone_id = (byte) zone;
        this.npc_name_data = npc_name;
        this.name = name;
        this.typemap = typemap;
        this.ismaplang = ismaplang;
        this.showhs = showhs;
        this.maxplayer = (short) maxplayer;
        this.maxzone = (byte) maxzone;
        this.item_map = new ItemMap[100];
        this.mapthread = new Thread(this);
        this.players = new ArrayList<>();
        this.vgos = vgo;
        this.running = false;
        this.map_data = Util.loadfile("data/mapnew/" + this.map_id);

        byte[] data = map_data; // Mảng byte chứa dữ liệu
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
        java.io.DataInputStream dis = new java.io.DataInputStream(bais);

        short number = dis.readShort();
        String str = dis.readUTF();
        short num = dis.readShort();
        this.mapW = dis.readByte();
        this.mapH = dis.readByte();
        Ai_entrys = new CopyOnWriteArrayList<>();
        if (map_id == 54 || map_id == 56 || map_id == 58 || map_id == 60 || map_id == 61) {
            Arena = new UseItemArena();
        }
        if (this.zone_id == maxzone) {
            bots = new ArrayList<>();
        }
    }
    @Override
    public void run() {
        this.running = true;
        long time1 = 0;
        long time2 = 0;
        long time3 = 0;
        while (this.running) {
            try {
                time1 = System.currentTimeMillis();
                update();
                if (this.zone_id == maxzone) {
                    Iterator<Bot> iterator = bots.iterator();
                    while (iterator.hasNext()) {
                        Bot bot = iterator.next();
                        if (bot != null) {
                            bot.update();
                            if (bot.isDie) {
                                iterator.remove();
                            }
                        }
                    }
                }
                if (this.time_chat < System.currentTimeMillis()) {
                    this.time_chat = System.currentTimeMillis() + 8000L;
                    auto_chat_npc();
                }
                if (Map.is_map_chiem_mo(this, true)) {
                    update_nhanban();
                }
                if (ChienTruong.gI().getStatus() == 2 && Map.is_map_chien_truong(this.map_id)) {
                    if (this.time_ct < System.currentTimeMillis()) {
                        this.time_ct = System.currentTimeMillis() + 5000L;
                        for (int i = 0; i < this.players.size(); i++) {
                            Player p0 = players.get(i);
                            ChienTruong.gI().send_info(p0);
                        }
                    }
                    Player_Nhan_Ban.update(this);
                }
                if (this.map_id == 48 && d != null) {
                    d.update();
                }
                if (this.map_id == 46 && leot != null){
                    leot.update();
                }
                if (map_id == 102 && kingCupMap != null) {
                    kingCupMap.update();
                    kingCupMap.finish();
                }
                time2 = System.currentTimeMillis();
                time3 = (1_000L - (time2 - time1));
                if (time3 > 0) {
                    if (time3 < 20) {
                        System.err.println("map_id " + this.map_id + " - zone " + (this.zone_id + 1) + " overload...");
                    }
                    Thread.sleep(time3);
                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void auto_chat_npc() {
        try {
            switch (this.map_id) {
                case 4: {
                    Npc.chat(this, Npc.CHAT_MR_BALLARD, -53);
                    break;
                }
                case 1: {
//                    Npc.chat(this, Npc.CHAT_TOP, -49);
//                    Npc.chat(this, Npc.CHAT_PHO_CHI_HUY, -37);
//                    Npc.chat(this, Npc.CHAT_PHAP_SU, -36);
//                    Npc.chat(this, Npc.CHAT_ZORO, -2);
//                    Npc.chat(this, Npc.CHAT_AMAN, -7);
//                    Npc.chat(this, Npc.CHAT_ODA, -81);
//                    Npc.chat(this, Npc.CHAT_LISA, -3);
//                    Npc.chat(this, Npc.CHAT_SOPHIA, -69);
//                    Npc.chat(this, Npc.CHAT_HAMMER, -5);
//                    Npc.chat(this, Npc.CHAT_ZULU, -8);
//                    Npc.chat(this, Npc.CHAT_DOUBA, -4);
//                    Npc.chat(this, Npc.CHAT_ANNA, -44);
                    Npc.chat(this, Npc.CHAT_BXH, -49);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isMapChiemThanh() {
        return map_id >= 83 && map_id <= 87;
    }
    public boolean ismapkogioihan(){
        return map_id == 55;
    }
    public boolean is_map_buon() {
        return map_id == 8 || map_id == 7 || (map_id >= 15 && map_id <= 18) || (map_id >= 20 && map_id <= 25) || map_id == 33
                || map_id == 34 || (map_id >= 37 && map_id <= 39) || (map_id >= 42 && map_id <= 45) || map_id == 52;
    }
    public static boolean is_map_di_buon(short id) {
        return id == 7 || id == 8 || id == 15 || id == 16 || id == 17 || id == 18 || id == 20 || id == 22 || id == 23 || id == 24
                || id == 25 || id == 33 || id == 34 || id == 42 || id == 44 || id == 45 || id == 52;
    }

    public boolean isMapLoiDai() {
        return map_id == 100 || map_id == 102;
    }

    public void BossDie(Mob_in_map mob) {
        mob.isDie = true;
        mob.time_back = System.currentTimeMillis() + mob.timeBossRecive;
        synchronized (Boss_entrys) {
            Boss_entrys.remove(mob);
        }
    }

    public void BossIn4(Session conn, int idx) throws IOException {
        for (Mob_in_map temp : Boss_entrys) {
            if (temp.index == idx) {
                Message m = new Message(7);
                m.writer().writeShort(idx);
                m.writer().writeByte((byte) temp.level);
                m.writer().writeShort(temp.x);
                m.writer().writeShort(temp.y);
                m.writer().writeInt(temp.hp);
                m.writer().writeInt(temp.get_HpMax());
                m.writer().writeByte(20); // id skill monster (Spec: 32, ...)
                m.writer().writeInt(temp.timeBossRecive / 1000);
                m.writer().writeShort(-1); // clan monster
                m.writer().writeByte(0);
                m.writer().writeByte(2); // speed
                m.writer().writeByte(0);
                m.writer().writeUTF("");
                m.writer().writeLong(-11111);
                m.writer().writeByte(temp.color_name); // color name 1: blue, 2: yellow
                conn.addmsg(m);
                m.cleanup();
                return;
            }
        }
        System.err.println("DIE BOSS" + idx);
        Message m2 = new Message(17);
        m2.writer().writeShort(-1);
        m2.writer().writeShort(idx);
        conn.addmsg(m2);
        m2.cleanup();
    }

    public Mob_in_map GetBoss(int index) {
        for (Mob_in_map mob : Boss_entrys) {
            if (mob.index == index) {
                return mob;
            }
        }
        return null;
    }

    private void update_AI() {

    }

    private synchronized void update_nhanban() throws IOException {
        // update mo tai nguyen
        Mob_MoTaiNguyen mobtainguyen = Manager.gI().chiem_mo.get_mob_in_map(this);
        if (mobtainguyen != null) {
            if (mobtainguyen.hp <= 0) {
                mobtainguyen.Set_hpMax((mobtainguyen.get_HpMax() / 10) * 12);
                if (mobtainguyen.get_HpMax() > 20_000_000) {
                    mobtainguyen.Set_hpMax(20_000_000);
                }
                mobtainguyen.hp = mobtainguyen.get_HpMax();
                mobtainguyen.isbuff_hp = false;
            }
            if (!mobtainguyen.isbuff_hp && mobtainguyen.hp < mobtainguyen.get_HpMax() / 2) {
                mobtainguyen.Set_hpMax((mobtainguyen.get_HpMax() / 10) * 12);
                if (mobtainguyen.get_HpMax() > 20_000_000) {
                    mobtainguyen.Set_hpMax(20_000_000);
                }
                mobtainguyen.isbuff_hp = true;
            }
            if (mobtainguyen.isbuff_hp && mobtainguyen.time_buff < System.currentTimeMillis()) {
                mobtainguyen.time_buff = System.currentTimeMillis() + 2500L;
                int par = mobtainguyen.get_HpMax() / 20;
                mobtainguyen.hp += par;
                if (mobtainguyen.hp > mobtainguyen.get_HpMax()) {
                    mobtainguyen.hp = mobtainguyen.get_HpMax();
                    mobtainguyen.isbuff_hp = false;
                }
                MainObject.upHP(mobtainguyen.map, mobtainguyen, par);
            }
        }
        //
        NhanBan temp = null;
        for (int i = 0; i < Manager.gI().list_nhanban.size(); i++) {
            if (Manager.gI().list_nhanban.get(i).map_id == this.map_id) {
                temp = Manager.gI().list_nhanban.get(i);
                break;
            }
        }
        if (temp != null) {
            if (temp.time_hp_buff < System.currentTimeMillis()) {
                temp.time_hp_buff = System.currentTimeMillis() + 2500L;
                if (temp.hp < temp.get_HpMax()) {
                    int par = temp.get_HpMax() / 20;
                    temp.hp += par;
                    if (temp.hp > temp.get_HpMax()) {
                        temp.hp = temp.get_HpMax();
                    }
                    Message m_hp = new Message(32);
                    m_hp.writer().writeByte(0);
                    m_hp.writer().writeShort(temp.index);
                    m_hp.writer().writeShort(-1); // id potion in bag
                    m_hp.writer().writeByte(0);
                    m_hp.writer().writeInt(temp.get_HpMax()); // max hp
                    m_hp.writer().writeInt(temp.hp); // hp
                    m_hp.writer().writeInt(par); // param use
                    for (int i = 0; i < this.players.size(); i++) {
                        this.players.get(i).conn.addmsg(m_hp);
                    }
                    m_hp.cleanup();
                }
            }
            if (temp.is_move && temp.act_time < System.currentTimeMillis()) {
                temp.act_time = System.currentTimeMillis() + 2000L;
                int[] x_ = new int[]{444, 1068, 228, 804, 516, 684, 540, 612, 1020, 444, 228, 612, 540, 492, 492, 756};
                int[] y_ = new int[]{156, 348, 516, 972, 372, 588, 588, 204, 204, 108, 372, 708, 396, 612, 420, 300};
                int[] map_ = new int[]{3, 5, 8, 9, 11, 12, 15, 16, 19, 21, 22, 24, 26, 27, 37, 42};
                for (int i = 0; i < map_.length; i++) {
                    if (map_[i] == temp.map_id) {
                        int x_old = temp.x;
                        int y_old = temp.y;
                        temp.x = (short) Util.random(x_[i] - 50, x_[i] + 50);
                        temp.y = (short) Util.random(y_[i] - 50, y_[i] + 50);
                        double a = Math.sqrt(Math.pow((x_old - temp.x), 2) + Math.pow((y_old - temp.y), 2));
                        if (a < 50) {
                            temp.x = (short) x_old;
                            temp.y = (short) y_old;
                        }
                        break;
                    }
                }
                Message m12 = new Message(4);
                m12.writer().writeByte(0);
                m12.writer().writeShort(0);
                m12.writer().writeShort(temp.index);
                m12.writer().writeShort(temp.x);
                m12.writer().writeShort(temp.y);
                m12.writer().writeByte(-1);
                for (int i = 0; i < this.players.size(); i++) {
                    Player p0 = this.players.get(i);
                    if (p0.map.map_id == this.map_id) {
                        p0.conn.addmsg(m12);
                    }
                }
                m12.cleanup();
            } else if (temp.p_target != null) {
                if (temp.p_target.conn.connected && temp.p_target.map.map_id == temp.map_id
                        && temp.p_target.map.zone_id == 4
                        && (Math.abs(temp.x - temp.p_target.x) < 200 && Math.abs(temp.y - temp.p_target.y) < 200)
                        && !temp.p_target.isDie) {
                    MainObject.MainAttack(this, temp, temp.p_target, Util.random(new int[]{0, 1, 2, 5, 6, 9, 10, 13, 14, 18}), null, 2);

                } else {
                    temp.p_target = null;
                    temp.is_move = true;
                }
            }
        }
    }

    private void update() {
        try {
            long _timec = System.currentTimeMillis();
            if (this.map_id >= 53 && this.map_id <= 60 && this.map_id % 2 == 1 && !vgos.isEmpty()) {
                Vgo v = vgos.get(0);
                for (int i1 = players.size() - 1; i1 >= 0 && v != null; i1--) {
                    Player p1 = players.get(i1);
                    if (p1 != null && _timec - p1.timeCantChangeMap > 15_000) {
                        p1.change_map(p1, v);
                    }
                }
            }
            //<editor-fold defaultstate="collapsed" desc="update Player       ...">  
            for (int i1 = players.size() - 1; i1 >= 0; i1--) {
                try {
                    Player p = players.get(i1);
                    Service.send_char_main_in4(p);
                    MapService.update_in4_2_other_inside(p.map, p);
                    if (p == null || p.conn == null || p.conn.socket == null || p.conn.socket.isClosed() || !p.conn.connected) {
                        players.remove(p);
                        if (p != null && p.conn != null) {
                            p.conn.close();
                        }
                        continue;
                    }
                    if (p != null && p.get_EffMe_Kham(StrucEff.TangHinh) != null) {
                        continue;
                    } else if (p != null && p.isTangHinh && p.get_EffMe_Kham(StrucEff.TangHinh) == null) {
                        p.isTangHinh = false;
                        //MapService.update_in4_2_other_inside(p.map, p);
                        Message m6 = new Message(4);
                        m6.writer().writeByte(0);
                        m6.writer().writeShort(0);
                        m6.writer().writeShort(p.index);
                        m6.writer().writeShort(p.x);
                        m6.writer().writeShort(p.y);
                        m6.writer().writeByte(-1);
                        MapService.send_msg_player_inside(p.map, p, m6, true);
                        m6.cleanup();
                    }
                    if (p.isDie && kingCupMap != null && p.time_die + 3000L > System.currentTimeMillis()) {
                        kingCupMap.refresh();
                    }
                    if (this.map_id == 136) {
                        if(p.get_EffDefault(-129) == null){
                            mapsk = true;
                            Service.usepotion(p, 0, (int) -(p.hp * Util.random(5, 10) * 0.01));
                        } else {
                            mapsk = false;
                        }
                    }else {
                        mapsk = false;
                    }
                    if (p.get_EffDefault(StrucEff.TE_CONG) != null || p.get_EffDefault(StrucEff.AoGiapLua) != null){
                        Service.usepotion(p, 0, (int) -(p.hp * Util.random(5, 10) * 0.01));
                    }
                    if (p.get_EffDefault(146) != null || p.get_EffDefault(147) != null) {
                        Service.usepotion(p, 0, (int) -(p.hp * 0.1)); // Giảm 10% HP
                    }
                    if (this.map_id == 50) { // pet_manager
                        long now_time = System.currentTimeMillis();
                        for (Pet temp : p.mypet) {
                            if (temp.expiry_date != 0 && temp.expiry_date < now_time) {
                                if (temp.is_follow) {
                                    p.pet_follow = -1;
                                }
                                p.mypet.remove(temp);
                                Service.send_wear(p);
                                Service.send_char_main_in4(p);
                                continue;
                            }
                            if (temp.is_hatch && temp.time_born < now_time) {
                                temp.is_hatch = false;
                                //
                                Message m = new Message(44);
                                //
                                m.writer().writeByte(28);
                                m.writer().writeByte(0);
                                m.writer().writeByte(3);
                                m.writer().writeByte(3);
                                int dem = 0;
                                for (Pet temp2 : p.mypet) {
                                    if (temp.is_hatch && temp2.time_born > now_time) {
                                        dem++;
                                    }
                                }
                                m.writer().writeByte(dem);
                                for (Pet temp2 : p.mypet) {
                                    if (temp.is_hatch && temp2.time_born > now_time) {
                                        int id_ = temp.get_id();
                                        m.writer().writeUTF(ItemTemplate3.item.get(id_).getName());
                                        m.writer().writeByte(4); // clazz
                                        m.writer().writeShort(id_);
                                        m.writer().writeByte(14); // type
                                        m.writer().writeShort(ItemTemplate3.item.get(id_).getIcon());
                                        m.writer().writeByte(0); // tier
                                        m.writer().writeShort(10); // level
                                        m.writer().writeByte(0); // color
                                        m.writer().writeByte(1);
                                        m.writer().writeByte(1);
                                        m.writer().writeByte(0); // op size
                                        long time2 = ((temp2.time_born - now_time) / 60000) + 1;
                                        m.writer().writeInt((int) time2);
                                        m.writer().writeByte(0);
                                    }
                                }
                                p.conn.addmsg(m);
                                m.cleanup();
                                //
                                m = new Message(44);
                                m.writer().writeByte(28);
                                m.writer().writeByte(1);
                                m.writer().writeByte(9);
                                m.writer().writeByte(9);
                                m.writer().writeUTF(temp.name);
                                m.writer().writeByte(temp.type);
                                m.writer().writeShort(p.mypet.indexOf(temp)); // id
                                m.writer().writeShort(temp.level);
                                m.writer().writeShort(temp.getlevelpercent()); // exp
                                m.writer().writeByte(temp.type);
                                m.writer().writeByte(temp.icon);
                                m.writer().writeByte(temp.nframe);
                                m.writer().writeByte(temp.color);
                                m.writer().writeInt(temp.get_age());
                                m.writer().writeShort(temp.grown);
                                m.writer().writeShort(temp.maxgrown);
                                m.writer().writeShort(temp.point1);
                                m.writer().writeShort(temp.point2);
                                m.writer().writeShort(temp.point3);
                                m.writer().writeShort(temp.point4);
                                m.writer().writeShort(temp.maxpoint);
                                m.writer().writeByte(temp.op.size());
                                for (int i2 = 0; i2 < temp.op.size(); i2++) {
                                    Option_pet temp2 = temp.op.get(i2);
                                    m.writer().writeByte(temp2.id);
                                    m.writer().writeInt(temp2.param);
                                    m.writer().writeInt(temp2.maxdam);
                                }
                                p.conn.addmsg(m);
                                m.cleanup();
                            }
                        }
                    }
                    if (p.squire != null && !p.isSquire) {
                        Squire.update(p);

                    }
                    p.update_wings_time();
                    for (Pet pet : p.mypet) {
                        if (pet.grown > 0 && pet.time_eat < System.currentTimeMillis()) {
                            pet.time_eat = System.currentTimeMillis() + 180_000L;
                            pet.grown -= 1;
                            if (pet.is_follow) {
                                //                            Service.send_wear(p);
                            }
                        }
                    }
                    p.updateEff();
                    if (!p.isDie) {
                        // auto +hp,mp
                        p.update(this);
                        if (p.squire != null && p.isLiveSquire) {
                            p.squire.update(this);
                        }
                        // auto trừ hp, mp khi dính bỏng lửa, bỏng lạnh
                        // eff medal
                        Item3 it = p.item.wear[12];
                        if (it != null && it.tier >= 3 && p.time_eff_medal < System.currentTimeMillis()) {
                            p.time_eff_medal = System.currentTimeMillis() + 5_000L;
                            Message m = new Message(-49);
                            m.writer().writeByte(2);
                            m.writer().writeShort(0);
                            m.writer().writeByte(0);
                            m.writer().writeByte(0);
                            switch (it.id) {
                                case 4588: {
                                    byte eff_ = 0;
                                    if(it.tier > 15) {
                                        eff_ = 65;
                                    }else if (it.tier == 15) {
                                        eff_ = 26;
                                    } else if (it.tier >= 12) {
                                        eff_ = 25;
                                    } else if (it.tier >= 9) {
                                        eff_ = 2;
                                    } else if (it.tier >= 6) {
                                        eff_ = 1;
                                    } else if (it.tier >= 3) {
                                        eff_ = 0;
                                    }
                                    m.writer().writeByte(eff_);
                                    break;
                                }
                                case 4589: {
                                    byte eff_ = 9;
                                    if(it.tier > 15) {
                                        eff_ = 64;
                                    }else if (it.tier == 15) {
                                        eff_ = 28;
                                    } else if (it.tier >= 12) {
                                        eff_ = 27;
                                    } else if (it.tier >= 9) {
                                        eff_ = 11;
                                    } else if (it.tier >= 6) {
                                        eff_ = 10;
                                    } else if (it.tier >= 3) {
                                        eff_ = 9;
                                    }
                                    m.writer().writeByte(eff_);
                                    break;
                                }
                                case 4590: {
                                    byte eff_ = 6;
                                    if(it.tier > 15) {
                                        eff_ = 60;
                                    }else if (it.tier == 15) {
                                        eff_ = 32;
                                    } else if (it.tier >= 12) {
                                        eff_ = 31;
                                    } else if (it.tier >= 9) {
                                        eff_ = 8;
                                    } else if (it.tier >= 6) {
                                        eff_ = 7;
                                    } else if (it.tier >= 3) {
                                        eff_ = 6;
                                    }
                                    m.writer().writeByte(eff_);
                                    break;
                                }
                                default: { // 4587
                                    byte eff_ = 3;
                                    if(it.tier > 15) {
                                        eff_ = 63;
                                    }else if (it.tier == 15) {
                                        eff_ = 78;
                                    } else if (it.tier >= 12) {
                                        eff_ = 30;
                                    } else if (it.tier >= 9) {
                                        eff_ = 5;
                                    } else if (it.tier >= 6) {
                                        eff_ = 4;
                                    } else if (it.tier >= 3) {
                                        eff_ = 3;
                                    }
                                    m.writer().writeByte(eff_);
                                    break;
                                }
                            }
                            m.writer().writeShort(p.index);
                            m.writer().writeByte(0);
                            m.writer().writeByte(0);
                            m.writer().writeInt(100000);
                            MapService.send_msg_player_inside(this, p, m, true);
                            m.cleanup();
                        }
//                        it = p.item.wear[20];
//                        if (it != null && p.time_eff_wear < System.currentTimeMillis()) {
//                            p.time_eff_wear = System.currentTimeMillis() + 5000L;
//                            Message m = new Message(-49);
//                            m.writer().writeByte(2);
//                            m.writer().writeShort(0);
//                            m.writer().writeByte(0);
//                            m.writer().writeByte(0);
//                            switch (it.id) {
//
//                                case 4784: {
//                                    byte eff_ = 62;
//                                    if (it.tier == 15) {
//                                        eff_ = 62;
//                                    }
//                                    m.writer().writeByte(eff_);
//                                    break;
//                                }
//                                case 4785: {
//                                    byte eff_ = 66;
//                                    if (it.tier == 15) {
//                                        eff_ = 66;
//                                    }
//                                    m.writer().writeByte(eff_);
//                                    break;
//                                }
//                                case 4786: {
//                                    byte eff_ = 65;
//                                    if (it.tier == 15) {
//                                        eff_ = 65;
//                                    }
//                                    m.writer().writeByte(eff_);
//                                    break;
//                                }
//                                case 4787: {
//                                    byte eff_ = 50;
//                                    if (it.tier == 15) {
//                                        eff_ = 50;
//                                    }
//                                    m.writer().writeByte(eff_);
//                                    break;
//                                }
//                                default: {
//                                    byte eff_ = 51;
//                                    if (it.tier == 15) {
//                                        eff_ = 51;
//                                    }
//                                    m.writer().writeByte(eff_);
//                                    break;
//                                }
//                            }
//                            m.writer().writeShort(p.index);
//                            m.writer().writeByte(0);
//                            m.writer().writeByte(0);
//                            m.writer().writeInt(5000);
//                            MapService.send_msg_player_inside(this, p, m, true);
//                            m.cleanup();
//                        }
                    }
                } catch (Exception eee) {
                }

            }
            //</editor-fold>   update player 

            // mob
            //<editor-fold defaultstate="collapsed" desc="update mob, boss      ...">  
            for (Mob_in_map mob : this.mobs) {
                mob.update(this);
            }
            for (Mob_in_map mob : this.Boss_entrys) {
                // mob fire
                mob.update(this);
            }
            //</editor-fold>    update mob, boss

        } catch (IOException e) {
            e.printStackTrace();
        }
        // update item map
        for (int i = 0; i < this.item_map.length; i++) {
            if (this.item_map[i] != null && this.item_map[i].idmaster != -1
                    && ((this.item_map[i].time_exist - System.currentTimeMillis()) < 15000L)) {
                this.item_map[i].idmaster = -1;
            }
            if (this.item_map[i] != null && this.item_map[i].time_exist < System.currentTimeMillis()) {
                this.item_map[i] = null;
            }
        }
    }

    public void start_map() {
        this.mapthread.start();
    }

    public void stop_map() {
        this.running = false;
        this.mapthread.interrupt();
    }

    public static Player get_player_by_name(String name) {
        for (Map[] maps : entrys) {
            for (Map map : maps) {
                for (Player p0 : map.players) {
                    if (p0.name.equals(name)) {
                        return p0;
                    }

                }
            }
        }
        return null;
    }

    public static Map[] get_map_by_id(int id) {
        for (Map[] temp : entrys) {
            if (temp[0].map_id == id) {
                return temp;
            }
        }
        return null;
    }

    public void send_map_data(Player p) throws IOException {
        if (p.x / 24 >= mapW || p.y / 24 >= mapH || p.x < 0 || p.y < 0) {
            Vgo vgo = new Vgo();
            vgo.id_map_go = 1;
            vgo.x_new = 432;
            vgo.y_new = 354;
            p.change_map(p, vgo);
            return;
        }
        p.npcs.clear();
        Message m = new Message(12);
        m.writer().writeShort(this.map_id);
        m.writer().writeShort((short) (p.x / 24));
        m.writer().writeShort((short) (p.y / 24));
        m.writer().write(this.map_data);
        m.writer().writeByte(this.zone_id); // zone
        m.writer().writeByte(this.typemap);
        if (p.conn.ac_admin < 1) {
            m.writer().writeBoolean(this.ismaplang);
        }
        m.writer().writeBoolean(this.showhs);
        p.conn.addmsg(m);
        m.cleanup();
        // send npc;
        String path = "data/npc/";
        if (Manager.gI().event == 1 && this.map_id == 1) {
            path = "data/npc/event" + Manager.gI().event + "/";
            Service.send_msg_data(p.conn, -49, "event1_1");
        }else if (Manager.gI().event == 8 && this.map_id == 1){
            path = "data/npc/event1/";
            Service.send_msg_data(p.conn, -49, "event1_1");
        } else if (Manager.gI().event == 3 && this.map_id == 1) {
            path = "data/npc/event4/";
            Service.eff_map(this, p, -65, 60, 648, 360, 4, 2, 95);
            Service.eff_map(this, p, -64, 59, 408, 360, 4, 2, 95);
            Service.eff_map(this, p, -62, 61, 528, 360, 3, 2, 75);
            Service.eff_map(this, p, -66, 64, 576, 216, 2, 2, 115);
            Service.eff_map(this, p, -89, 110, 288, 600, 4, 2, 115);
        }
        if (isMapLangPhuSuong() || map_id == 135) {
            path = "data/npc/langphusuong/";
        }
        for (String npc_name_data1 : this.npc_name_data) {
            byte[] data = Util.loadfile(path + npc_name_data1);
            m = new Message(-50);
            m.writer().write(data);
            p.conn.addmsg(m);
            m.cleanup();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bais);
            try {
                byte size = dis.readByte();
                for (int i = 0; i < size; i++) {
                    NpcTemplate npc = new NpcTemplate();
                    npc.name = dis.readUTF();
                    dis.readUTF();
                    npc.id = dis.readByte();
                    dis.readByte();
                    npc.x = dis.readShort();
                    npc.y = dis.readShort();
                    dis.readByte();
                    dis.readByte();
                    dis.readByte();
                    dis.readByte();
                    dis.readUTF();
                    dis.readByte();
                    dis.readByte();
                    p.npcs.add(npc);
                }
            } catch (IOException e) {
                System.err.println("ERROR " + this.map_id);
            }
        }
        // mob mo tai nguyen
        if (Map.is_map_chiem_mo(p.map, true)) {
            Mob_MoTaiNguyen mob_tainguyen = Manager.gI().chiem_mo.get_mob_in_map(p.map);
            m = new Message(4);
            m.writer().writeByte(1);
            m.writer().writeShort(64);
            m.writer().writeShort(mob_tainguyen.index);
            m.writer().writeShort(mob_tainguyen.x);
            m.writer().writeShort(mob_tainguyen.y);
            m.writer().writeByte(-1);
            if (mob_tainguyen.nhanban != null) {
                m.writer().writeByte(0);
                m.writer().writeShort(0);
                m.writer().writeShort(mob_tainguyen.nhanban.index);
                m.writer().writeShort(mob_tainguyen.nhanban.x);
                m.writer().writeShort(mob_tainguyen.nhanban.y);
                m.writer().writeByte(-1);
            }
            p.conn.addmsg(m);
            m.cleanup();

        }
        if (this.map_id == 52 && this.zone_id == this.maxzone) {
            m = new Message(-50);
            m.writer().writeByte(1);
            m.writer().writeUTF("Mr Dylan");
            m.writer().writeUTF("Mua bán");
            m.writer().writeByte(-57);
            m.writer().writeByte(34);
            m.writer().writeShort(384);
            m.writer().writeShort(432);
            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeByte(2);
            m.writer().writeByte(26);
            m.writer().writeUTF("Ta chuyên bán các loại hàng hóa phục vụ cho việc đi buôn. Hân hạnh phục vụ quý khách");
            m.writer().writeByte(1);
            m.writer().writeByte(0);
            p.conn.addmsg(m);
            m.cleanup();
            NpcTemplate npc = new NpcTemplate();
            npc.name = "Mr Dylan";
            npc.id = -57;
            npc.x = 384;
            npc.y = 432;
            p.npcs.add(npc);
        }
        if (this.map_id == 1 && p.conn.ac_admin > 120) {
            m = new Message(-50);
            m.writer().writeByte(1);
            m.writer().writeUTF("Ms AD");
            m.writer().writeUTF("Giao Tiếp");
            m.writer().writeByte(-1);// id npc
            m.writer().writeByte(22);   // icon
            m.writer().writeShort(358); // x
            m.writer().writeShort(302); // y
            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeByte(2);
            m.writer().writeByte(22); // icon 2
            m.writer().writeUTF("thích gì có đó");
            m.writer().writeByte(1);
            m.writer().writeByte(0);
            p.conn.addmsg(m);
            m.cleanup();
        }
        if (this.map_id == 1) {
            m = new Message(-50);
            m.writer().writeByte(1);
            m.writer().writeUTF("Ms mango");
            m.writer().writeUTF("Giao Tiếp");
            m.writer().writeByte(-99);// id npc
            m.writer().writeByte(56);   // icon
            if (Manager.gI().event == 8){
                m.writer().writeShort(598); // x
                m.writer().writeShort(246); // y
            }else {
                m.writer().writeShort(423); // x
                m.writer().writeShort(234); // y
            }
            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeByte(2);
            m.writer().writeByte(48); // icon 2
            m.writer().writeUTF("Có money có tất cả");
            m.writer().writeByte(1);
            m.writer().writeByte(0);
            p.conn.addmsg(m);
            m.cleanup();
        }
        // monument
        if (this.map_id == 1) {
            m = new Message(-96);
            m.writer().writeShort(288);
            m.writer().writeShort(312);
            m.writer().writeShort(264);
            m.writer().writeShort(288);
            m.writer().writeByte(3);
            m.writer().writeByte(1);
            m.writer().writeByte(-1);
            m.writer().writeByte(-25);
            m.writer().writeByte(1);
            m.writer().writeUTF("Đài vinh danh");
            m.writer().writeUTF(Map.name_mo);
            m.writer().writeByte(-49);
            m.writer().writeByte(15);
            //
            m.writer().writeShort(Map.weapon); // weapon
            m.writer().writeShort(Map.body); // body
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(3); // pet
            m.writer().writeShort(Map.hat); // hat
            m.writer().writeShort(Map.leg); // leg
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(Map.wing); // wing
            m.writer().writeShort(-1);
            m.writer().writeShort(Map.head); // head
            m.writer().writeShort(Map.eye); // eye
            m.writer().writeShort(Map.hair); // hair
            //
            m.writer().write(Util.loadfile("data/msg/msg_-96_x" + p.conn.zoomlv));
            p.conn.addmsg(m);
            m.cleanup();
        } else if (this.map_id == 50) { // map pet
            m = new Message(44);
            m.writer().writeByte(28);
            m.writer().writeByte(0);
            m.writer().writeByte(3);
            m.writer().writeByte(3);
            int dem = 0;
            long now_time = System.currentTimeMillis();
            for (Pet temp2 : p.mypet) {
                if (temp2.is_hatch && temp2.time_born > now_time) {
                    dem++;
                }
            }
            m.writer().writeByte(dem);
            for (Pet temp2 : p.mypet) {
                if (temp2.is_hatch && temp2.time_born > now_time) {
                    int id_ = temp2.get_id();
                    m.writer().writeUTF(ItemTemplate3.item.get(id_).getName());
                    m.writer().writeByte(4); // clazz
                    m.writer().writeShort(id_);
                    m.writer().writeByte(14); // type
                    m.writer().writeShort(ItemTemplate3.item.get(id_).getIcon());
                    m.writer().writeByte(0); // tier
                    m.writer().writeShort(10); // level
                    m.writer().writeByte(0); // color
                    m.writer().writeByte(1);
                    m.writer().writeByte(1);
                    m.writer().writeByte(0); // op size
                    long time2 = ((temp2.time_born - now_time) / 60000) + 1;
                    m.writer().writeInt((int) time2);
                    m.writer().writeByte(0);
                }
            }
            p.conn.addmsg(m);
            m.cleanup();
            //
            m = new Message(44);
            m.writer().writeByte(28);
            m.writer().writeByte(0);
            m.writer().writeByte(9);
            m.writer().writeByte(9);
            m.writer().writeByte(0);
            p.conn.addmsg(m);
            m.cleanup();
            //
            m = new Message(44);
            m.writer().writeByte(28);
            m.writer().writeByte(0);
            m.writer().writeByte(9);
            m.writer().writeByte(9);
            dem = 0;
            for (Pet temp : p.mypet) {
                if (!temp.is_follow && !temp.is_hatch) {
                    dem++;
                }
            }
            m.writer().writeByte(dem); // size pet
            //
            for (int i = 0; i < p.mypet.size(); i++) {
                if (!p.mypet.get(i).is_follow && !p.mypet.get(i).is_hatch) {
                    m.writer().writeUTF(p.mypet.get(i).name);
                    m.writer().writeByte(p.mypet.get(i).type);
                    m.writer().writeShort(i); // id
                    m.writer().writeShort(p.mypet.get(i).level);
                    m.writer().writeShort(p.mypet.get(i).getlevelpercent()); // exp
                    m.writer().writeByte(p.mypet.get(i).type);
                    m.writer().writeByte(p.mypet.get(i).icon);
                    m.writer().writeByte(p.mypet.get(i).nframe);
                    m.writer().writeByte(p.mypet.get(i).color);
                    m.writer().writeInt(p.mypet.get(i).get_age());
                    m.writer().writeShort(p.mypet.get(i).grown);
                    m.writer().writeShort(p.mypet.get(i).maxgrown);
                    m.writer().writeShort(p.mypet.get(i).point1);
                    m.writer().writeShort(p.mypet.get(i).point2);
                    m.writer().writeShort(p.mypet.get(i).point3);
                    m.writer().writeShort(p.mypet.get(i).point4);
                    m.writer().writeShort(p.mypet.get(i).maxpoint);
                    m.writer().writeByte(p.mypet.get(i).op.size());
                    for (int i2 = 0; i2 < p.mypet.get(i).op.size(); i2++) {
                        Option_pet temp = p.mypet.get(i).op.get(i2);
                        m.writer().writeByte(temp.id);
                        m.writer().writeInt(temp.param);
                        m.writer().writeInt(temp.maxdam);
                    }
                }
            }
            p.conn.addmsg(m);
            m.cleanup();
        }
    }

    public static Map get_map_dungeon(int id) {
        for (Map[] temp : entrys) {
            if (temp[0].map_id == id) {
                return temp[0];
            }
        }
        return null;
    }
    public static Map get_Leo_thap(int id) {
        for (Map[] temp : entrys) {
            if (temp[0].map_id == id) {
                return temp[0];
            }
        }
        return null;
    }
    public static Map get_id(int id) {
        for (Map[] temp : entrys) {
            if (temp[0].map_id == id) {
                return temp[0];
            }
        }
        return null;
    }

    public synchronized void drop_item(Player p, byte type, short id) throws IOException {
//        His_DelItem hist = new His_DelItem(p.name);
//        hist.Logger = "Vứt";
        switch (type) {
            case 3: {
                Item3 temp = p.item.bag3[id];
                if (temp != null) {
                    if (temp.islock) {
                        Service.send_notice_box(p.conn, "Vật phẩm đã khóa");
                        return;
                    }
//                    hist.tem3 = temp;
//                    hist.Flus();
                    p.item.remove(3, id, 1);
                }
                break;
            }
            case 4:
            case 7: {
//                hist.tem47 = new Item47();
//                hist.tem47.id = id;
//                hist.tem47.category = type;
//                hist.tem47.quantity = (short) p.item.total_item_by_id(type, id);
//                hist.Flus();
                p.item.remove(type, id, p.item.total_item_by_id(type, id));
                break;
            }
        }
    }

    public void send_mount(Player p) throws IOException {
        Message m = new Message(-97);
        m.writer().writeByte(0);
        m.writer().writeByte(p.type_use_mount);
        m.writer().writeShort(p.index);
        MapService.send_msg_player_inside(this, p, m, true);
        m.cleanup();
        Service.send_char_main_in4(p);
    }

    public synchronized void pick_item(Session conn, Message m2) throws IOException {
        short id = m2.reader().readShort();
        byte type = m2.reader().readByte();

        if (item_map[id] == null) {
            Message m = new Message(20);
            m.writer().writeByte(type);
            m.writer().writeShort(id);
            m.writer().writeShort(conn.p.index);
            MapService.send_msg_player_inside(this, conn.p, m, true);
            m.cleanup();
            item_map[id] = null;
            return;
        }
        if (type == 3 && item_map[id] != null
                && (item_map[id].id_item == 3590 || item_map[id].id_item == 3591 || item_map[id].id_item == 3592)) {
            if (item_map[id].idmaster != -1 && conn.p.index != item_map[id].idmaster) {
                Service.send_notice_nobox_white(conn, "Vật phẩm của người khác");
                return;
            }
            if (conn.p.pet_di_buon != null && conn.p.pet_di_buon.item.size() < 12) {
                conn.p.pet_di_buon.item.add(item_map[id].id_item);
                //
                Message m = new Message(20);
                m.writer().writeByte(type);
                m.writer().writeShort(id);
                m.writer().writeShort(conn.p.index);
                MapService.send_msg_player_inside(this, conn.p, m, true);
                m.cleanup();
                item_map[id] = null;
                //
            } else {
                Service.send_notice_nobox_white(conn, "Không thể nhặt!");
            }
            return;
        }
        type = item_map[id].category;
//        if (conn.p.isdie || conn.p.in4_auto[3] == 0
//                || (conn.p.in4_auto[4] == -1 && conn.p.in4_auto[5] == 1 && conn.p.in4_auto[6] == 3)) {
//            System.out.println("map.Map.pick_item()"+id+"   "+type);
//            return;
//        }
        if (conn.p.isDie) {
            return;
        }
        if (item_map[id].idmaster != -1 && conn.p.index != item_map[id].idmaster) {
            Service.send_notice_nobox_white(conn, "Vật phẩm của người khác");
            return;
        }
        if (item_map[id] != null && (item_map[id].idmaster == -1 || conn.p.index == item_map[id].idmaster)
                && (item_map[id].time_pick < System.currentTimeMillis())) {
            if (type == 4 && item_map[id].id_item == -1) { // vang
                if (conn.p.in4_auto[5] == 0) {
                    conn.p.update_vang(item_map[id].quantity);
                    conn.p.item.char_inventory(5);
                    Message m = new Message(20);
                    m.writer().writeByte(type);
                    m.writer().writeShort(id);
                    m.writer().writeShort(conn.p.index);
                    MapService.send_msg_player_inside(this, conn.p, m, true);
                    m.cleanup();
                    item_map[id] = null;
                }
            } else if (item_map[id].id_item != -1) {
                if (conn.p.item.get_bag_able() > 0
                        || ((type == 4 || type == 7) && (conn.p.item.total_item_by_id(type, item_map[id].id_item) > 0))) {
                    switch (type) {
                        case 3: {
                            if (item_map[id].id_item < ItemTemplate3.item.size()) {
                                Short idadd = item_map[id].id_item;
                                Item3 itbag = new Item3();
                                itbag.id = idadd;
                                itbag.name = ItemTemplate3.item.get(idadd).getName();
                                itbag.clazz = ItemTemplate3.item.get(idadd).getClazz();
                                itbag.type = ItemTemplate3.item.get(idadd).getType();
                                itbag.level = ItemTemplate3.item.get(idadd).getLevel();
                                itbag.icon = ItemTemplate3.item.get(idadd).getIcon();
                                itbag.op = new ArrayList<>();
                                itbag.op.addAll(item_map[id].op);
                                itbag.color = ItemTemplate3.item.get(idadd).getColor();
                                itbag.part = ItemTemplate3.item.get(idadd).getPart();
                                itbag.tier = 0;
                                itbag.islock = false;
                                itbag.time_use = 0;
                                if (conn.p.in4_auto[4] > itbag.color) {
                                    return;
                                }
                                conn.p.item.add_item_bag3(itbag);
                                conn.p.item.char_inventory(3);
                            }
                            break;
                        }
                        case 4: {
                            if (item_map[id].id_item < ItemTemplate4.item.size()) {
                                Short idadd = item_map[id].id_item;
                                if (ItemTemplate4.item.get(idadd).getType() == 1 && conn.p.in4_auto[6] == 1) {
                                    return;
                                } else if (ItemTemplate4.item.get(idadd).getType() == 0 && conn.p.in4_auto[6] == 2) {
                                    return;
                                }
                                Item47 itbag = new Item47();
                                itbag.id = idadd;
                                itbag.quantity = (short) item_map[id].quantity;
                                itbag.category = 4;
                                conn.p.item.add_item_bag47(4, itbag);
                                conn.p.item.char_inventory(4);
                            }
                            break;
                        }
                        case 7: {
                            if (item_map[id].id_item < ItemTemplate7.item.size()) {
                                Short idadd = item_map[id].id_item;
                                Item47 itbag = new Item47();
                                itbag.id = idadd;
                                itbag.quantity = (short) item_map[id].quantity;
                                itbag.category = 7;
                                conn.p.item.add_item_bag47(7, itbag);
                                conn.p.item.char_inventory(7);
                            }
                            break;
                        }
                    }
                    Message m = new Message(20);
                    m.writer().writeByte(type);
                    m.writer().writeShort(id);
                    m.writer().writeShort(conn.p.index);
                    MapService.send_msg_player_inside(this, conn.p, m, true);
                    m.cleanup();
                    item_map[id] = null;
                }
            }
        }
    }

    public int get_item_map_index_able() {
        for (int i = 0; i < item_map.length; i++) {
            if (item_map[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void create_party(Session conn, Message m2) throws IOException {
        byte type = m2.reader().readByte();
        String name = "";
        Player p0 = null;
        if (type != 0 && type != 5 && type != 4) {
            name = m2.reader().readUTF();
            p0 = Map.get_player_by_name(name);
        }
        switch (type) {
            case 1: { // request party other
                if (p0 == null) {
                    Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại");
                    return;
                }
                if (p0.party != null) {
                    if (conn.p.party != null && conn.p.party.get_mems().contains(p0)) {
                        Service.send_notice_box(conn, "Đối phương đã ở trong đội");
                    } else {
                        Service.send_notice_box(conn, "Đối phương đang trong đội khác");
                    }
                    return;
                }
                if (conn.p.party != null) {
                    if (conn.p.party.get_mems().get(0).index != conn.p.index) {
                        Service.send_notice_box(conn, "Bạn éo phải đội trưởng, đừng có ra dẻ!!!");
                        return;
                    }
                    if (conn.p.party.get_mems().size() > 4) {
                        Service.send_notice_box(conn, "không thể rủ rê thêm thành viên");
                        return;
                    }
                }
                if (conn.p.party == null) {
                    conn.p.party = new Party();
                    conn.p.party.add_mems(conn.p);
                    conn.p.party.sendin4();
                }
                //
                Message m = new Message(48);
                m.writer().writeByte(type);
                m.writer().writeUTF(conn.p.name);
                p0.conn.addmsg(m);
                m.cleanup();
                break;
            }
            case 2: { // accept
                if (conn.p.party != null) {
                    Service.send_notice_box(conn, "Bạn đã ở trong nhóm");
                    return;
                }
                if (p0 == null || (p0 != null && p0.party == null)) {
                    Service.send_notice_box(conn, "Nhóm không còn tồn tại");
                    return;
                }
                if (p0.party.get_mems().size() > 4) {
                    Service.send_notice_box(conn, "Nhóm đầy");
                    return;
                } else {
                    conn.p.party = p0.party;
                    p0.party.add_mems(conn.p);
                    p0.party.sendin4();
                    p0.party.send_txt_notice(conn.p.name + " vào nhóm");
                }
                break;
            }
            case 3: { // kick
                if (conn.p.party == null) {
                    Service.send_notice_box(conn, "Nhóm không tồn tại");
                    return;
                }
                Player p01 = null;
                for (int i = 0; i < conn.p.party.get_mems().size(); i++) {
                    if (conn.p.party.get_mems().get(i).name.equals(name)) {
                        p01 = conn.p.party.get_mems().get(i);
                        break;
                    }
                }
                if (p01 == null || name.equals("")) {
                    Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại");
                }
                p01.party.remove_mems(p01);
                p01.party.sendin4();
                p01.party = null;
                conn.p.party.send_txt_notice(p01.name + " đã bị đá khỏi đội");
                Service.send_notice_nobox_white(p01.conn, "Bạn đã bị đá khỏi đội ehehe");
                Message m22 = new Message(48);
                m22.writer().writeByte(5);
                p01.conn.addmsg(m22);
                m22.cleanup();
                break;
            }
            case 4: { // giai tan
                Message m = new Message(48);
                m.writer().writeByte(4);
                for (int i = 1; i < conn.p.party.get_mems().size(); i++) {
                    Player p02 = conn.p.party.get_mems().get(i);
                    p02.conn.addmsg(m);
                    p02.party = null;
                }
                conn.addmsg(m);
                conn.p.party.get_mems().clear();
                conn.p.party = null;
                m.cleanup();
                break;
            }
            case 5: { // leave
                if (conn.p.party.get_mems().get(0).index == conn.p.index) {
                    Service.send_notice_box(conn, "Là đội trưởng thì phải ra dáng, không đc bỏ nhóm!");
                    return;
                }
                conn.p.party.remove_mems(conn.p);
                conn.p.party.sendin4();
                conn.p.party.send_txt_notice(conn.p.name + " rời nhóm");
                conn.p.party = null;
                //
                Message m = new Message(48);
                m.writer().writeByte(5);
                conn.addmsg(m);
                m.cleanup();
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    public static Player get_player_by_id(int id_player_login) {
        for (Map[] maps : entrys) {
            for (Map map : maps) {
                for (Player p0 : map.players) {
                    if (p0.index == id_player_login) {
                        return p0;
                    }
                }

            }
        }
        return null;
    }

    public static boolean is_map_cant_save_site(short id) {
        return id == 48 || id == 88 || id == 89 || id == 90 || id == 91 || id == 82 || id == 102 || id == 100 || (id >= 83 && id <= 87) || (id >= 53 && id <= 61)
                || Map.is_map_chien_truong(id) || id == 46 || id == 125 || id == 127 || id == 129 || id == 132 || id == 135;
    }
    public static boolean is_map_not_zone2(short id) {
        return id == 48 || id == 88 || id == 89 || id == 90 || id == 91 || id == 82 || id == 102 || id == 100
                || (id >= 83 && id <= 87) || (id >= 53 && id <= 61) || Map.is_map_chien_truong(id) || id == 1 || id == 10
                || id == 14 || id == 18 || id == 28 || id == 32 || id == 33 || id == 34 || id == 35 || id == 36 || id == 67
                || id == 68 || id == 69 || id == 70 || id == 93;
    }

    public synchronized void add_item_map_leave(Map map, Player p_master, ItemMap temp, int mob_index)
            throws IOException {
        for (int i = 0; i < item_map.length; i++) {
            if (item_map[i] == null) {
                item_map[i] = temp;
                Message mi = new Message(19);
                mi.writer().writeByte(temp.category);
                mi.writer().writeShort(mob_index); // index mob die
                switch (temp.category) {
                    case 3: {
                        mi.writer().writeShort(ItemTemplate3.item.get(temp.id_item).getIcon());
                        mi.writer().writeShort(i); //
                        mi.writer().writeUTF(ItemTemplate3.item.get(temp.id_item).getName());
                        break;
                    }
                    case 4: {
                        mi.writer().writeShort(ItemTemplate4.item.get(temp.id_item).getIcon());
                        mi.writer().writeShort(i); //
                        mi.writer().writeUTF(ItemTemplate4.item.get(temp.id_item).getName());
                        break;
                    }
                    case 7: {
                        mi.writer().writeShort(ItemTemplate7.item.get(temp.id_item).getIcon());
                        mi.writer().writeShort(i); //
                        mi.writer().writeUTF(ItemTemplate7.item.get(temp.id_item).getName());
                        break;
                    }
                }
                mi.writer().writeByte(0); // color
                mi.writer().writeShort(-1); // id player
                MapService.send_msg_player_inside(map, p_master, mi, true);
                mi.cleanup();
                break;
            }
        }
    }

    public static boolean is_map_chiem_mo(Map map, boolean is_zone) {
        boolean is_map = false;
        int[] map_ = new int[]{3, 5, 8, 9, 11, 12, 15, 16, 19, 21, 22, 24, 26, 27, 37, 42};
        for (int i = 0; i < map_.length; i++) {
            if (map_[i] == map.map_id) {
                is_map = true;
                break;
            }
        }
        return (is_zone) ? (map.zone_id == 4 && is_map) : is_map;
    }
    public static boolean is_map__load_board_player(short id) {
        return id == 102;
    }

    public static boolean is_map_chien_truong(short id) {
        return id >= 53 && id <= 61;
    }

    public boolean isMapChienTruong() {
        return map_id >= 53 && map_id <= 61;
    }

    public boolean isMapLangPhuSuong() {
        return map_id == 125 || map_id == 127 || map_id == 129 || map_id == 132;
    }

}
