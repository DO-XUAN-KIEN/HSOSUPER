package map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import client.Pet;
import client.Player;
import core.Service;
import core.Util;
import io.Message;
import io.Session;
import template.*;

public class Leo_thap {
    public Map template;
    private Leo_thap leoThap;
    public String name_party;
    public long time_live;
    public int state;
    private int mob_speed;
    public int wave;
    public int num_mob;
    private int num_mob_max;
    private int dame_buff;
    private int id_mob_in_wave;
    public int index_mob;
    private int hp;
    private byte mode_now;
    private List<Mob_Leothap> mobs;

    private long TimeStar = 5 * 60 * 1000;
    private long isTime_mob;
    long lastBuffTime ;

    public Leo_thap() throws IOException {
        init();
    }

    private void init() {
        try {
            Map temp = Map.get_Leo_thap(46);
            template = new Map(46, 0, temp.npc_name_data, temp.name, temp.typemap, temp.ismaplang, temp.showhs,
                    temp.maxplayer, temp.maxzone, temp.vgos);
            template.mobs = new Mob_in_map[0];
            template.start_map();
            template.leot = this;
            index_mob = 1;
            state = 1;
            isTime_mob = System.currentTimeMillis();
            mobs = new ArrayList<>();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
//    public void finish_leothap() {
//        Vgo vgo = null;
//        vgo = new Vgo();
//        vgo.id_map_go = 1;
//        vgo.x_new = 432;
//        vgo.y_new = 354;
//        Leo_thapManager.remove_list(this);
//    }

    public void update() {
        if (this.index_mob > 65_000) {
            this.state = 7;
        }
        long currentTime = System.currentTimeMillis();
        int time_now = getTime_live();
        if (lastBuffTime == 0) {
            lastBuffTime = currentTime;
        }
        try {
            if (this.state == 0) { // wait to begin
                this.state = 1;
            } else if (this.state == 1) { // wait at wave 20
                if (wave != 21) {
                    int dem = 0;
                    if (mobs.size() > 0) {
                        for (Mob_Leothap mob_temp : mobs) {
                            for (Player p0 : template.players) {
                                Message m2 = new Message(17);
                                m2.writer().writeShort(p0.index);
                                m2.writer().writeShort(mob_temp.index);
                                p0.conn.addmsg(m2);
                                m2.cleanup();
                                dem++;
                                if (dem > 10_000) {
                                    break;
                                }
                            }
                            if (dem > 10_000) {
                                break;
                            }
                        }
                    }
                    this.state = 2;
                    notice_all_player_in_leothap("Boss đã xuất hiện, hãy cẩn thận!!!");
                    this.num_mob = num_mob_max;
                    mobs.clear();
                    int[] id_mob_values = {23, 133, 135, 136, 156}; // id mob
                    if (wave == 1) {
                        id_mob_in_wave = id_mob_values[0]; // wave 1 => id_mob_values = 1
                    } else if (wave == 3) {
                        id_mob_in_wave = id_mob_values[1]; // wave 3 => id_mob_values = 2
                    } else if (wave == 4) {
                        id_mob_in_wave = id_mob_values[2]; // wave 4 => id_mob_values = 3
                    } else if (wave == 5) {
                        id_mob_in_wave = id_mob_values[3]; // wave 5 => id_mob_values = 4
                    } else if (wave == 6) {
                        id_mob_in_wave = id_mob_values[4]; // wave 6 => id_mob_values = 5
                    }
                    isTime_mob = System.currentTimeMillis();
                } else {
                    //leave_item_by_type3(template,)
                    notice_all_player_in_leothap("Diệt boss thành công, xin chúc mừng các đại hiệp, sẽ tự động thoát sau 30s");
                    this.num_mob = num_mob_max;
                    mobs.clear();
                    this.state = 8;
                    // cal prime
                }
            } else if (this.state == 2) { // fight
                if (currentTime - isTime_mob >= TimeStar) {
                    notice_all_player_in_leothap("Thời gian đã hết, tất cả người chơi sẽ rời bản đồ.");
                    this.state = 7;
                } else {
                    long LoadTime = TimeStar - (currentTime - isTime_mob);
                    for (Player p : template.players) {
                        Message m = new Message(-104);
                        m.writer().writeByte(1);
                        m.writer().writeByte(2);
                        m.writer().writeShort((int) (LoadTime / 1000));
                        m.writer().writeUTF("Leo tháp");
                        m.writer().writeShort(-1);
                        m.writer().writeUTF("");
                        p.conn.addmsg(m);
                        m.cleanup();
                    }

                    if (currentTime - lastBuffTime >= 5_000) {
                        for (Mob_Leothap mob : mobs) {
                            mob.hp += 10000;
                            if (mob.hp > mob.get_HpMax()) {
                                mob.hp = mob.get_HpMax();
                            }
                        }
                        lastBuffTime = currentTime;
                    }
                }
                if (mobs.size() < num_mob_max) {
                    Mob_Leothap mob = new Mob_Leothap(this, this.index_mob++, Mob.entrys.get(id_mob_in_wave));
                    mob.x = (short) Util.random(400, 600);
                    mob.y = (short) Util.random(200, 400);
                    if (this.wave >= 21) {
                        mob.Set_hpMax(500_000 * (this.wave / 10));
                    } else if(wave == 1){
                        mob.Set_hpMax(10_000_000);
                    }else if (wave == 3){
                        mob.Set_hpMax(50_000_000);
                    }else if (wave == 4){
                        mob.Set_hpMax(80_000_000);
                    }else if (wave == 5){
                        mob.Set_hpMax(100_000_000);
                    }else if(wave == 6){
                        mob.Set_hpMax(150_000_000);
                    }else {
                        mob.Set_hpMax(mob.hp_max);
                    }
                    mob.hp = mob.get_HpMax();
                    mobs.add(mob);
                    Message m = new Message(4);
                    m.writer().writeByte(1);
                    m.writer().writeShort(mob.template.mob_id);
                    m.writer().writeShort(mob.index);
                    m.writer().writeShort(mob.x);
                    m.writer().writeShort(mob.y);
                    m.writer().writeByte(-1);
                    for (int i = 0; i < template.players.size(); i++) {
                        Player p0 = template.players.get(i);
                        p0.conn.addmsg(m);
                    }
                    m.cleanup();
                }
                update_mob();
            } else if (this.state == 4) {
                // this.state = 6;
            } else if (this.state == 5) {
                for (int i = 0; i < mobs.size(); i++) {
                    Mob_Leothap mob = mobs.get(i);
                    Message m2 = new Message(17);
                    m2.writer().writeShort(-1);
                    m2.writer().writeShort(mob.index);
                    for (int i2 = 0; i2 < template.players.size(); i2++) {
                        Player p0 = template.players.get(i2);
                        p0.conn.addmsg(m2);
                    }
                    m2.cleanup();
                }
                mobs.clear();
                for (int i2 = 0; i2 < template.players.size(); i2++) {
                    Player p0 = template.players.get(i2);
//                    Service.send_notice_box(p0.conn,
//                            "Thất bại rồi hãy tu luyện thêm sức mạnh đi s\nTự thoát sau 5s");
                }
                notice_all_player_in_leothap("Thất bại rồi hãy tu luyện thêm sức mạnh đi!!!\nTự thoát sau 5s");
                this.state = 6;
            } else if (this.state == 6) {
                this.state = 7;
                //
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (this.state == 7) {
                for (int i = 0; i < template.players.size(); i++) {
                    Player p0 = template.players.get(i);
                    p0.dokho = 0;
                    p0.x = 432;
                    p0.y = 354;
                    Map[] map_enter = Map.get_map_by_id(1);
                    int d = 0;
                    while ((d < (map_enter[d].maxzone - 1)) && map_enter[d].players.size() >= map_enter[d].maxplayer) {
                        d++;
                    }
                    p0.map = map_enter[d];
                    //
                    p0.is_changemap = false;
                    p0.x_old = p0.x;
                    p0.y_old = p0.y;
                    MapService.enter(p0.map, p0);
                    //
                }
                this.template.stop_map();
                this.template.d = null;
                Leo_thapManager.remove_list(this);
            } else if (this.state >= 8 && this.state <= 13) {
                if (this.state == 13) {
                    this.state = -2;
                } else {
                    this.state++;
                }
            } else if (this.state == -2) {
                this.state = 7;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notice_all_player_in_leothap(String s) throws IOException {
        Message m = new Message(53);
        m.writer().writeUTF(s);
        m.writer().writeByte(0);
        for (int i = 0; i < template.players.size(); i++) {
            Player p0 = template.players.get(i);
            p0.conn.addmsg(m);
        }
        m.cleanup();
    }

    private void update_mob() throws IOException {
        for (int i = 0; i < mobs.size(); i++) {
            Mob_Leothap mob = mobs.get(i);
            if (!mob.isDie) {
                mob_act(mob);
            }
        }
    }

    private void mob_act(Mob_Leothap mob) throws IOException {
        boolean is_atk = true;
        if (is_atk) {
            int dame = 20000;
            dame = Util.random((dame * 9) / 10, (dame + 1));
            dame += this.wave * 10;
            dame *= dame_buff;
            if (!template.players.isEmpty()) {
                Player targetPlayer = template.players.get(0); // Simplified target selection, can be more complex

                // Inflict damage on the player
                targetPlayer.hp -= dame;
                if (targetPlayer.hp < 0) {
                    targetPlayer.hp = 0;
                }
                if (targetPlayer.hp ==0) {
                    this.state = 5;
                }

                // Notify the player about the attack
                Message m = new Message(10);
                m.writer().writeByte(1);
                m.writer().writeShort(mob.index);
                m.writer().writeInt(mob.hp);
                m.writer().writeByte(2);
                m.writer().writeByte(1);
                m.writer().writeShort(targetPlayer.index); // index of the attack skill
                m.writer().writeInt(dame); // damage inflicted
                m.writer().writeInt(targetPlayer.hp); // player's remaining HP
                m.writer().writeByte(6); // skill
                m.writer().writeByte(0);
                targetPlayer.conn.addmsg(m);
                m.cleanup();
            }
        }else {
            Message m = new Message(4);
            m.writer().writeByte(1);
            m.writer().writeShort(mob.template.mob_id);
            m.writer().writeShort(mob.index); // index
            m.writer().writeShort(mob.x);
            m.writer().writeShort(mob.y);
            m.writer().writeByte(-1);
            for (int i = 0; i < template.players.size(); i++) {
                Player p0 = template.players.get(i);
                p0.conn.addmsg(m);
            }
            m.cleanup();
        }
    }

    public int getTime_live() {
        return (int) ((System.currentTimeMillis() - time_live + 2) / 1000);
    }

    public void send_mob_in4(Session conn, int index) throws IOException {
        int index_ = -1;
        for (int i = 0; i < mobs.size(); i++) {
            Mob_Leothap mob = mobs.get(i);
            if (mob.index == index) {
                index_ = i;
                break;
            }
        }
        if (index_ > -1) {
            Mob_Leothap temp = mobs.get(index_);
            Message m = new Message(7);
            m.writer().writeShort(index);
            m.writer().writeByte(temp.template.level);
            m.writer().writeShort(temp.x);
            m.writer().writeShort(temp.y);
            m.writer().writeInt(temp.hp);
            m.writer().writeInt(temp.get_HpMax());
            int[] skills = new int[]{0, 2, 4, 6, 8};
            m.writer().writeByte(skills[Util.random(skills.length)]); // id skill monster (Spec: 32, ...)
            m.writer().writeInt(-4);
            m.writer().writeShort(temp.x);
            m.writer().writeShort(temp.y);
            m.writer().writeShort(-1); // clan monster
            m.writer().writeByte(1);
            m.writer().writeByte(this.mob_speed / 25); // speed
            m.writer().writeByte(0);
            m.writer().writeUTF("");
            m.writer().writeLong(-11111);
            m.writer().writeByte(0); // color name 1: blue, 2: yellow
            conn.addmsg(m);
            m.cleanup();
        }
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int i) {
        wave = i;
    }

    public void setMode(int mode) throws IOException {
        String name = "";
        this.mode_now = (byte) mode;
        switch (mode) {
            case 0: {
                mob_speed = 50;
                num_mob_max = 1;
                num_mob = num_mob_max;
                dame_buff = 1;
                name = "Easy";
                break;
            }
            case 1: {
                hp = 50_000;
                mob_speed = 75;
                num_mob_max = 3;
                num_mob = num_mob_max;
                dame_buff = 3;
                name = "Normal";
                break;
            }
            case 2: {
                hp = 50_000;
                mob_speed = 100;
                num_mob_max = 5;
                num_mob = num_mob_max;
                dame_buff = 5;
                name = "Hard";
                break;
            }
            case 3: {
                hp = 50_000;
                mob_speed = 150;
                num_mob_max = 8;
                num_mob = num_mob_max;
                dame_buff = 8;
                name = "Nightmare";
                break;
            }
            case 4: {
                hp = 50_000;
                mob_speed = 200;
                num_mob_max = 10;
                num_mob = num_mob_max;
                dame_buff = 10;
                name = "Hell";
                break;
            }
        }
        if (this.wave == 20) {
            notice_all_player_in_leothap("Đội trưởng đã chọn độ khó " + name);
        }
    }

    public Mob_Leothap get_mob(int id) {
        for (int i = 0; i < this.mobs.size(); i++) {
            if (this.mobs.get(i).index == id) {
                return this.mobs.get(i);
            }
        }
        return null;
    }

    public synchronized void fire_mob(Map map, Mob_Leothap mob, Player p, byte index_skill, long dame_atk)
            throws IOException {
        if (mob.isDie) {
            Message m2 = new Message(17);
            m2.writer().writeShort(p.index);
            m2.writer().writeShort(mob.index);
            p.conn.addmsg(m2);
            m2.cleanup();
            return;
        }
        //
        int clazz = switch (p.clazz) {
            case 0 -> 2;
            case 1 -> 4;
            case 2 -> 1;
            case 3 -> 3;
            default -> p.get_DameBase();
        };
        dame_atk = p.get_DameProp(clazz)* 2 ;
        long dame = dame_atk;
        //
        EffTemplate ef = null;
        int cr = p.body.get_Crit();
        ef = p.get_EffDefault(33);
        if (ef != null) {
            cr += ef.param;
        }
        boolean crit = cr > Util.random(0, 10000);
        boolean pierce = false;
        if (crit) {
            dame *= 2;
        }
        if (!crit) {
            int pier = p.body.get_Pierce();
            ef = p.get_EffDefault(36);
            if (ef != null) {
                pier += ef.param;
            }
            if (pier > Util.random(0, 10000)) {
                pierce = true;
            }
        }
        if (!pierce) {
            long dameresist = (this.wave * this.wave * this.wave / 2);
            // if (mob.is_boss) {
            // dameresist *= 2;
            // }
            dame -= dameresist;
            if (dame <= 0) {
                dame = 1;
            }
        }
        if (mob.color_name != 0) {
            dame = (dame * 8) / 10;
            switch (mob.color_name) {
                case 1: {
                    if (p.clazz == 2) {
                        dame /= 2;
                    }
                    break;
                }
                case 2: {
                    if (p.clazz == 3) {
                        dame /= 2;
                    }
                    break;
                }
                case 4: {
                    if (p.clazz == 0) {
                        dame /= 2;
                    }
                    break;
                }
                case 5: {
                    if (p.clazz == 1) {
                        dame /= 2;
                    }
                    break;
                }
            }
        }
        if (mob.template.mob_id == 167 || mob.template.mob_id == 168 || mob.template.mob_id == 169
                || mob.template.mob_id == 170 || mob.template.mob_id == 171 || mob.template.mob_id == 172) {
            dame = 1;
        }
        if (15 > Util.random(0, 100)) { // mob get miss
            dame = 0;
        }
        if (dame < 0) {
            dame = 0;
        }
        if (dame > 2_000_000_000) {
            dame = 2_000_000_000;
        }
        mob.hp -= dame;
        // if mob die
        if (mob.hp <= 0) {
            mob.hp = 0;
            // mob die
            if (!mob.isDie) {
                mob.isDie = true;
                // send p outside
//                if(Util.random_ratio(17))
//                    ev_he.Event_3.LeaveItemMap(map, mob, p);

                Message m2 = new Message(17);
                m2.writer().writeShort(p.index);
                m2.writer().writeShort(mob.index);
                for (int i = 0; i < map.players.size(); i++) {
                    Player p2 = map.players.get(i);
                    if (!((Math.abs(p2.x - p.x) < 200) && (Math.abs(p2.y - p.y) < 200))) {
                        p2.conn.addmsg(m2);
                    }
                }
                m2.cleanup();
            }
            num_mob--;
            if (num_mob == 0) {
                state = 1;
                wave = 21;
            }
        }
        // attached in4
        Message m = new Message(9);
        m.writer().writeShort(p.index);
        m.writer().writeByte(index_skill);
        m.writer().writeByte(1);
        m.writer().writeShort(mob.index);
        m.writer().writeInt((int)dame); // dame
        m.writer().writeInt(mob.hp); // hp mob after
        if (dame > 0 && crit) {
            m.writer().writeByte(1); // size color show
            //
            m.writer().writeByte(4); // 1: xuyen giap, 2:hut hp, 3: hut mp, 4: chi mang, 5: phan don
            m.writer().writeInt((int)dame); // par
            //
        } else if (dame > 0 && pierce) {
            m.writer().writeByte(1); // size color show
            //
            m.writer().writeByte(1); // 1: xuyen giap, 2:hut hp, 3: hut mp, 4: chi mang, 5: phan don
            m.writer().writeInt((int)dame); // par
            //
        } else {
            m.writer().writeByte(0);
        }
        m.writer().writeInt(p.hp);
        m.writer().writeInt(p.mp);
        m.writer().writeByte(11); // 1: green, 5: small white 9: big white, 10: st dien, 11: st bang
        m.writer().writeInt(0); // dame plus
        MapService.send_msg_player_inside(map, p, m, true);
        m.cleanup();
        // exp
        int expup = 0;
        expup = (int) dame; // tinh exp
        ef = p.get_EffDefault(-125);
        if (ef != null) {
            expup += (expup * (ef.param / 100)) / 100;
        }
        expup=(int)(expup/3);
        p.update_Exp(expup, true);
        // exp clan
        if (p.myclan != null) {
            int exp_clan = ((int) dame) / 10_000;
            if (exp_clan < 1 || exp_clan > 50) {
                exp_clan = 1;
            }
            p.myclan.update_exp(exp_clan);
        }
        // pet attack
        if (!mob.isDie && p.pet_follow != -1) {
            Pet my_pet = null;
            for (Pet pett : p.mypet) {
                if (pett.is_follow) {
                    my_pet = pett;
                    break;
                }
            }
            if (my_pet != null && my_pet.grown > 0) {
                int a1 = 0;
                int a2 = 1;
                for (Option_pet temp : my_pet.op) {
                    if (temp.maxdam > 0) {
                        a1 = temp.param;
                        a2 = temp.maxdam;
                        break;
                    }
                }
                int dame_pet = Util.random(a1, Math.max(a2, a1+1)) - (this.wave * this.wave * 5);
                if (dame_pet <= 0) {
                    dame_pet = 1;
                }
                if (((mob.hp - dame_pet) > 0) && (p.pet_atk_speed < System.currentTimeMillis()) && (a2 > 1)) {
                    p.pet_atk_speed = System.currentTimeMillis() + 1500L;
                    m = new Message(84);
                    m.writer().writeByte(2);
                    m.writer().writeShort(p.index);
                    m.writer().writeByte(1);
                    m.writer().writeByte(1);
                    m.writer().writeShort(mob.index);
                    m.writer().writeInt(dame_pet);
                    mob.hp -= dame_pet;
                    m.writer().writeInt(mob.hp);
                    m.writer().writeInt(p.hp);
                    m.writer().writeInt(p.body.get_HpMax());
                    p.conn.addmsg(m);
                    m.cleanup();
                }
            }
        }
    }

    private static void leave_item_by_type3(Map map, int idItem, int color_, Player p_master, String name, int index)
            throws IOException {
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = (short) idItem;
            map.item_map[index_item_map].color = (byte) color_;
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 3;
            map.item_map[index_item_map].idmaster = (short) p_master.index;
            List<Option> opnew = new ArrayList<Option>();
            for (Option op_old : ItemTemplate3.item.get(idItem).getOp()) {
                Option temp = new Option(1, 1, (short) 0);
                temp.id = op_old.id;
                if (temp.id != 37 && temp.id != 38) {
                    if (op_old.getParam(0) < 10) {
                        temp.setParam(Util.random(0, 10));
                    } else {
                        temp.setParam(Util.random((9 * op_old.getParam(0)) / 10, op_old.getParam(0)));
                    }
                } else {
                    temp.setParam(1);
                }
                opnew.add(temp);
            }
            map.item_map[index_item_map].op = new ArrayList<>();
            map.item_map[index_item_map].op.addAll(opnew);
            map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
            map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
            // add in4 game scr
            Message mi = new Message(19);
            mi.writer().writeByte(3);
            mi.writer().writeShort(index); // index mob die
            mi.writer().writeShort(ItemTemplate3.item.get(map.item_map[index_item_map].id_item).getIcon());
            mi.writer().writeShort(index_item_map); //
            mi.writer().writeUTF(name);
            mi.writer().writeByte(color_); // color
            mi.writer().writeShort(-1); // id player
            MapService.send_msg_player_inside(map, p_master, mi, true);
            mi.cleanup();
        }
    }
    public static void leave_item_by_type4(Map map, short id_item, Player p_master, int index_mob) throws IOException {
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = id_item;
            map.item_map[index_item_map].color = 0;
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 4;
            map.item_map[index_item_map].idmaster = (short) p_master.index;
            map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
            map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
            // add in4 game scr
            Message mi = new Message(19);
            mi.writer().writeByte(4);
            mi.writer().writeShort(index_mob); // id mob die
            mi.writer().writeShort(ItemTemplate4.item.get(map.item_map[index_item_map].id_item).getIcon());
            mi.writer().writeShort(index_item_map); //
            mi.writer().writeUTF(ItemTemplate4.item.get(map.item_map[index_item_map].id_item).getName());
            mi.writer().writeByte(0); // color
            mi.writer().writeShort(-1); // id player
            MapService.send_msg_player_inside(map, p_master, mi, true);
            mi.cleanup();
        }
    }

    public static void leave_item_by_type7(Map map, short idItem, Player p_master, int index) throws IOException {
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = idItem;
            if (ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getColor() == 21) {
                map.item_map[index_item_map].color = 1;
            } else {
                map.item_map[index_item_map].color = 0;
            }
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 7;
            map.item_map[index_item_map].idmaster = (short) p_master.index;
            map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
            map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
            // add in4 game scr
            Message mi = new Message(19);
            mi.writer().writeByte(7);
            mi.writer().writeShort(index); // id mob die
            mi.writer().writeShort(ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getIcon());
            mi.writer().writeShort(index_item_map); //
            mi.writer().writeUTF(ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getName());
            mi.writer().writeByte(map.item_map[index_item_map].color); // color
            mi.writer().writeShort(-1); // id player
            MapService.send_msg_player_inside(map, p_master, mi, true);
            mi.cleanup();
        }
    }


    public void send_mob_move_when_exit(Player p) throws IOException {
        Message m = new Message(4);
        for (int i = 0; i < this.mobs.size(); i++) {
            Mob_Leothap mob = this.mobs.get(i);
            m.writer().writeByte(1);
            m.writer().writeShort(mob.template.mob_id);
            m.writer().writeShort(mob.index); // index
            m.writer().writeShort(mob.x);
            m.writer().writeShort(mob.y);
            m.writer().writeByte(-1);
        }
        for (int i = 0; i < template.players.size(); i++) {
            Player p0 = template.players.get(i);
            p0.conn.addmsg(m);
        }
        m.cleanup();
    }
}
