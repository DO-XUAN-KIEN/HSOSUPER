package BossHDL;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import client.Clan;
import client.Pet;
import client.Player;
import core.Manager;
import core.Service;
import core.Util;
import io.Message;
import io.Session;
import map.Map;
import map.MapService;
import map.Mob_in_map;
import map.Vgo;
import template.*;

public class BossTG implements Runnable {
    public static String[] auto_chat = new String[] {"Chào em, đi đâu đây", "giao lưu cái nhẹ nào", "Bố đấm chết cđmm"};
    public static byte[] skill = new byte[] {0, 4, 6, 8, 19, 20};
    public List<Top_Dame> top_dame;
    public HashMap<String, Long> time_use_skill;
    public HashMap<String, Long> time_enter_map;
    public Player p;
    public short x;
    public short y;
    public int id;
    public boolean running;
    private Thread myth;
    public long time_atk;
    public long time_move;
    public Map map;
    public List<Player> list_atk;
    private long time_chat;
    private Player p_target;

    public BossTG() throws IOException {
        p = new Player(new Session(new Socket()), 2);
        p.setup();
        p.set_in4();
        p.x = 558;
        p.y = 708;
        p.typepk = 0;
        this.id = 2;
        //
        Map temp = Map.get_map_by_id(7)[4];
        this.map = new Map(7, 98, temp.npc_name_data, temp.name, temp.typemap, temp.ismaplang, temp.showhs,
                temp.maxplayer, temp.maxzone, new ArrayList<Vgo>());
        this.map.mobs = new Mob_in_map[0];
        this.map.start_map();
        //
        this.p.map = this.map;
        this.p.isDie = true;
        this.p.hp = 0;
        this.list_atk = new ArrayList<>();
        this.time_use_skill = new HashMap<>();
        time_enter_map = new HashMap<>();
        top_dame = new ArrayList<>();
        for (int i = 0; i < BossTG.skill.length; i++) {
            this.time_use_skill.put(("" + BossTG.skill[i]), 0L);
        }
        this.running = false;
        this.myth = new Thread(this);
    }

    public void send_in4(Player p) throws IOException {
        int dem = 0;
        for (int i = 0; i < 11; i++) {
            if (i != 0 && i != 1 && i != 6 && i != 7 && i != 10) {
                continue;
            }
            if (this.p.item.wear[i] != null) {
                dem++;
            }
        }
        Message m = new Message(5);
        m.writer().writeShort(this.id);
        m.writer().writeUTF(this.p.name);
        m.writer().writeShort(this.p.x);
        m.writer().writeShort(this.p.y);
        m.writer().writeByte(this.p.clazz);
        m.writer().writeByte(-1);
        m.writer().writeByte(this.p.head);
        m.writer().writeByte(this.p.eye);
        m.writer().writeByte(this.p.hair);
        m.writer().writeShort(this.p.level);
        m.writer().writeInt((int) Math.min(this.p.hp, Integer.MAX_VALUE));
        m.writer().writeInt((int) Math.min(this.p.get_HpMax(), Integer.MAX_VALUE));
        m.writer().writeByte(this.p.typepk);
        m.writer().writeShort(this.p.pointpk);
        m.writer().writeByte(dem);
        //
        for (int i = 0; i < this.p.item.wear.length; i++) {
            if (i != 0 && i != 1 && i != 6 && i != 7 && i != 10) {
                continue;
            }
            Item3 temp = this.p.item.wear[i];
            if (temp != null) {
                m.writer().writeByte(temp.type);
                if (i == 10 && this.p.item.wear[14] != null
                        && (this.p.item.wear[14].id >= 4638 && this.p.item.wear[14].id <= 4648)) {
                    m.writer().writeByte(this.p.item.wear[14].part);
                } else {
                    m.writer().writeByte(temp.part);
                }
                m.writer().writeByte(3);
                m.writer().writeShort(-1);
                m.writer().writeShort(-1);
                m.writer().writeShort(-1);
                m.writer().writeShort(-1); // eff
            }
        }
        //
        if (this.p.myclan != null) {
            m.writer().writeShort(this.p.myclan.icon);
            m.writer().writeInt(Clan.get_id_clan(this.p.myclan));
            m.writer().writeUTF(this.p.myclan.name_clan_shorted);
            m.writer().writeByte(this.p.myclan.get_mem_type(this.p.name));
        } else {
            m.writer().writeShort(-1); // clan
        }
        if (this.p.pet_follow != -1) {
            for (Pet temp : this.p.mypet) {
                if (temp.is_follow) {
                    m.writer().writeByte(temp.type); // type
                    m.writer().writeByte(temp.icon); // icon
                    m.writer().writeByte(temp.nframe); // nframe
                    break;
                }
            }
        } else {
            m.writer().writeByte(-1); // pet
        }
        m.writer().writeByte(this.p.fashion.length);
        for (int i = 0; i < this.p.fashion.length; i++) {
            m.writer().writeByte(this.p.fashion[i]);
        }
        //
        m.writer().writeShort(-1);
        m.writer().writeByte(Horse.SOI_BAO_LUA);
        m.writer().writeBoolean(false);
        m.writer().writeByte(1);
        m.writer().writeByte(0);
        m.writer().writeShort(Service.get_id_mat_na(this.p)); // mat na
        m.writer().writeByte(1); // paint mat na trc sau
        m.writer().writeShort(Service.get_id_phiphong(this.p)); // phi phong
        m.writer().writeShort(Service.get_id_weapon(this.p)); // weapon
        m.writer().writeShort(this.p.id_horse);
        m.writer().writeShort(Service.get_id_hair(this.p)); // hair
        m.writer().writeShort(Service.get_id_wing(this.p)); // wing
        m.writer().writeShort(-1); // body
        m.writer().writeShort(-1); // leg
        m.writer().writeShort(-1); // bienhinh
        p.conn.addmsg(m);
        m.cleanup();
    }

    @Override
    public void run() {
        while (this.running) {
            if (!this.p.isDie) {
                try {
                    for (int i = 0; i < this.map.players.size(); i++) {
                        Player p0 = this.map.players.get(i);
                        if (!p0.map.equals(this.map) && this.list_atk.contains(p0)) {
                            this.list_atk.remove(p0);
                        } else if (!this.list_atk.contains(p0) && p0.level > 10) {
                            this.list_atk.add(p0);
                        }
                    }
                    for (int i = 0; i < this.list_atk.size(); i++) {
                        Player p0 = this.list_atk.get(i);
                        if (!p0.map.equals(this.map) && this.list_atk.contains(p0) || p0.isDie) {
                            this.list_atk.remove(p0);
                            if (this.p_target != null && this.p_target.equals(p0)) {
                                this.p_target = null;
                            }
                        }
                    }
                    if (this.list_atk.size() > 0) {
                        if (this.time_atk < System.currentTimeMillis()) {
                            this.time_atk = System.currentTimeMillis() + 1000L;
                            if (this.p_target == null) {
                                this.p_target = this.list_atk.get(Util.random(this.list_atk.size()));
                            }
                            int x_go = Math.abs(this.p_target.x + this.p.x) / 2;
                            int y_go = Math.abs(this.p_target.y + this.p.y) / 2;
                            double distance =
                                    Math.sqrt(Math.pow(Math.abs(x_go - this.p.x), 2) + Math.pow(Math.abs(y_go - this.p.y), 2));
                            while (distance > 150) {
                                x_go = Math.abs(x_go + this.p.x) / 2;
                                y_go = Math.abs(y_go + this.p.y) / 2;
                                distance = Math
                                        .sqrt(Math.pow(Math.abs(x_go - this.p.x), 2) + Math.pow(Math.abs(y_go - this.p.y), 2));
                            }
                            this.p.x = (short) x_go;
                            this.p.y = (short) y_go;
                            Message m3 = new Message(4);
                            m3.writer().writeByte(0);
                            m3.writer().writeShort(0);
                            m3.writer().writeShort(this.id);
                            m3.writer().writeShort(this.p.x);
                            m3.writer().writeShort(this.p.y);
                            m3.writer().writeByte(-1);
                            for (int i = 0; i < this.map.players.size(); i++) {
                                Player p0 = this.map.players.get(i);
                                if (p0.map.equals(this.map)) {
                                    p0.conn.addmsg(m3);
                                }
                            }
                            m3.cleanup();
                            this.time_move = System.currentTimeMillis() + 4000L;
                            if (Math.abs(this.p_target.x - this.p.x) < 50 && Math.abs(this.p_target.y - this.p.y) < 50) {
                                atk_target();
                            }
                        }
                    } else {
                        if (this.time_move < System.currentTimeMillis()) {
                            this.time_move = System.currentTimeMillis() + 4000L;
                            //
                            int x_go = Math.abs(558 + this.p.x) / 2;
                            int y_go = Math.abs(708 + this.p.y) / 2;
                            double distance =
                                    Math.sqrt(Math.pow(Math.abs(x_go - this.p.x), 2) + Math.pow(Math.abs(y_go - this.p.y), 2));
                            while (distance > 150) {
                                x_go = Math.abs(x_go + this.p.x) / 2;
                                y_go = Math.abs(y_go + this.p.y) / 2;
                                distance = Math
                                        .sqrt(Math.pow(Math.abs(x_go - this.p.x), 2) + Math.pow(Math.abs(y_go - this.p.y), 2));
                            }
                            if (distance > 10) {
                                this.p.x = (short) x_go;
                                this.p.y = (short) y_go;
                                //
                                Message m3 = new Message(4);
                                m3.writer().writeByte(0);
                                m3.writer().writeShort(0);
                                m3.writer().writeShort(this.id);
                                m3.writer().writeShort(this.p.x);
                                m3.writer().writeShort(this.p.y);
                                m3.writer().writeByte(-1);
                                for (int i = 0; i < this.map.players.size(); i++) {
                                    Player p0 = this.map.players.get(i);
                                    if (p0.map.equals(this.map)) {
                                        p0.conn.addmsg(m3);
                                    }
                                }
                                m3.cleanup();
                            }
                        }
                        //
                        if (this.time_chat < System.currentTimeMillis()) {
                            this.time_chat = System.currentTimeMillis() + 2000L;
                            chat(BossTG.auto_chat[Util.random(BossTG.auto_chat.length)]);
                        }
                    }
                } catch (IOException e) {
                } catch (Exception e2) {
                }
            } else {
                if (this.time_chat < System.currentTimeMillis()) {
                    this.time_chat = System.currentTimeMillis() + 2000L;
                    try {
                        chat("Xin lỗi e chừa rồi...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(250L);
            } catch (InterruptedException e) {
//				e.printStackTrace();
            }
        }
    }

    private void atk_target() throws IOException {
        byte index_skill = 0;
        for (int i = BossTG.skill.length - 1; i >= 1; i--) {
            long t = this.time_use_skill.get("" + BossTG.skill[i]);
            if (t < System.currentTimeMillis()) {
                index_skill = BossTG.skill[i];
                this.time_use_skill.replace(("" + BossTG.skill[i]), t,
                        (System.currentTimeMillis() + this.p.skills[index_skill].mLvSkill[14].delay));
                break;
            }
        }
        LvSkill _skill = this.p.skills[index_skill].mLvSkill[14];
        int range = this.p.skills[index_skill].mLvSkill[14].range_lan;
        List<Player> list_p_to_atk = new ArrayList<>();
        list_p_to_atk.add(p_target);
        for (int i = 0; i < this.map.players.size(); i++) {
            Player p0 = this.map.players.get(i);
            if (p0.map.equals(this.map) && p0.index != this.p_target.index && Math.abs(p0.x - this.p_target.x) < range
                    && Math.abs(p0.y - this.p_target.y) < range) {
                list_p_to_atk.add(p0);
            }
        }
        for (int i = 0; i < list_p_to_atk.size(); i++) {
            MainObject.MainAttack(this.map, this.p, list_p_to_atk.get(i), Util.random(new int[]{0, 1, 2, 5, 6, 9, 10, 13, 14, 18}), _skill, 1);
        }
        //MainObject.MainAttack(this.map, p_target, this.p, Util.random(new int[]{0, 1, 2, 5, 6, 9, 10, 13, 14, 18}), _skill, 1);
        if (25 > Util.random(100)) {
            chat("thích va chạm với t à?");
        }
    }

    private void chat(String string) throws IOException {
        Message m = new Message(27);
        m.writer().writeShort(this.id);
        m.writer().writeByte(0);
        m.writer().writeUTF(string);
        for (int i = 0; i < this.map.players.size(); i++) {
            Player p0 = this.map.players.get(i);
            if (p0.map.equals(this.map)) {
                p0.conn.addmsg(m);
            }
        }
        m.cleanup();
    }

    public void refresh() throws IOException {
        this.top_dame.clear();
        this.p.isDie = false;
        this.p.typepk =0;
        this.p.hp = this.p.get_HpMax();
        this.p.mp = this.p.get_MpMax();
        // chest in4
        Service.usepotion(this.p, 0, this.p.hp);
        Service.usepotion(this.p, 1, this.p.mp);
        //
        Manager.gI().chatKTGprocess("@Server : Boss Tg xuất hiện.");
    }

    public void finish() throws IOException {
        p.dame_affect_special_sk = 0;
        p.hp = 0;
        p.isDie = true;
        MapService.change_flag(map, p, -1);
        Message m = new Message(41);
        m.writer().writeShort(p.index);
        m.writer().writeShort(-1); // point pk
        m.writer().writeByte(1); // type main object
        MapService.send_msg_player_inside(map, p, m, true);
        m.cleanup();
        if (!Horse.isHorseClan(p.type_use_mount)) {
            p.type_use_mount = -1;
        }
    }
    public void start_boss(){
        this.running = true;
        this.myth.start();
    }
    public void stop_boss(){
        this.running = false;
        this.myth.interrupt();
        this.map.stop_map();
    }

    public void update_dame(String name, long dame) {
        Top_Dame temp = null;
        for (int i = 0; i < this.top_dame.size(); i++) {
            if (this.top_dame.get(i).name.equals(name)) {
                temp = this.top_dame.get(i);
                break;
            }
        }
        if (temp != null) {
            temp.dame += dame;
        } else {
            temp = new Top_Dame();
            temp.name = name;
            temp.dame = 0;
            temp.receiv = false;
            this.top_dame.add(temp);
        }
    }
}
