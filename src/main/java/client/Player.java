package client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import core.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import event_daily.*;

import io.Message;
import io.Session;
import map.Map;
import map.MapService;
import map.Vgo;
import template.*;

public class Player extends Body2 {

    public List<EffTemplate> list_eff = new ArrayList<>();
    ;
    public boolean is_nhanban;
    public final Session conn;
    //    public final int index;
    public boolean already_setup;
    //    public String name;
    public Map map;
    public boolean is_changemap;
    public long timeCantChangeMap;
    public byte qua_noel;
    public byte cuong_bao;
    public byte head;
    public byte eye;
    public byte hair;
    public Date date;
    public byte diemdanh;
    public byte chucphuc;
    public byte quatanthu;
    public byte mm_tt;
    public byte mm_md;
    public byte dokho;
    public byte stt_level;
    public byte type_use_mount;
    // public int hieuchien;
    public int dibuon;
    public int dicuop;
    public int boss;
    public int phoban;
    public long danhvong;
    public int doiqua;
    public int sk_hongio;
    public byte type_exp;
    //    public byte clazz;
//    public short level;
//    public long exp;
    public long vang;
    public int kimcuong;
    //    public boolean isdie;
    public short tiemnang;
    public short kynang;
    public short point1;
    public short point2;
    public short point3;
    public short point4;
    public int suckhoe;
    public int pointarena;
    //    public byte typepk;
    public int pointpk;
    public byte[] skill_point;
    public long[] time_delay_skill;
    public Body2 body;
    public byte maxbag;
    public byte maxbox;
    public Item item;
    public List<String> giftcode;
    public byte[][] rms_save;
    public List<Pet> mypet;
    public short pet_follow = -1;
    public List<Friend> list_friend;
    public List<String> list_enemies;
    //    public int hp;
//    public int mp;
    public short[] fashion;
    public Skill[] skills;
    public byte item_color_can_pick;
    public byte hp_mp_can_pick;
    public HashMap<Integer, Boolean> other_player_inside;
    public HashMap<Integer, Boolean> bot_inside;
    public HashMap<Integer, Boolean> other_mob_inside;
    public HashMap<Integer, Boolean> other_mob_inside_update;
    public short id_item_rebuild;
    public boolean is_use_mayman;
    public short id_use_mayman;
    public short item_replace;
    public short item_replace2;
    public short id_buffer_126;
    public byte id_index_126;
    public Party party;
    public long time_use_poition_hp;
    public long time_use_poition_mp;
    public byte enough_time_disconnect;
    public int dame_affect_special_sk;
    public int hp_restore;
    public long time_buff_hp;
    public long time_buff_mp;
    public long time_affect_special_sk;
    public long time_speed_rebuild;
    public String name_trade;
    public short[] list_item_trade;
    public boolean lock_trade;
    public boolean accept_trade;
    public int money_trade;
    //    public Dungeon dungeon;
    public Clan myclan;
    public byte id_medal_is_created;
    public short[] medal_create_material;
    public short fusion_material_medal_id;
    public long pet_atk_speed;
    public long time_eff_medal;
    public long time_eff_wear;
    public long time_eff_21;
    public long time_eff_22;
    public int[] point_active;
    public short id_horse;
    public short id_ngua = -1;
    public boolean is_create_wing;
    public short id_remove_time_use;
    public byte id_wing_split;
    public byte[] in4_auto;
    public List<Player_store> my_store;
    public String my_store_name;
    public String Store_Sell_ToPL = "";
    public Pet_di_buon pet_di_buon;
    public short id_name;
    public String name_mem_clan_to_appoint = "";
    public byte id_select_mo_ly;
    public short id_hop_ngoc;
    public List<Item3> list_thao_kham_ngoc;
    public int time_atk_ngoc_hon_nguyen = 0;
    public short id_ngoc_tinh_luyen = -1;
    public long timeBlockCTG;
    public Wedding it_wedding;
    public String[] in4_wedding;
    public int[] quest_daily;
    public int[] st_ran;
    public int[] sc_ran;
    public int chuyencan;
    public int diemsukien;
    public byte khu2;
    public int jointx;
    public boolean tai;
    public boolean xiu;
    public Squire squire;
    public Player owner;
    public boolean isOwner = true;
    public boolean ngu = false;
    public boolean isSquire;
    public boolean isLiveSquire;
    public String taixiu = "Bạn chưa đặt cược.";
    public int id_index_temp = -1;
    public int[] skill_110;
    public long cooldown_bat_tu;
    public long cooldown_thieu_chay;
    public int group_king_cup;
    public byte type_reward_king_cup;
    public int point_king_cup;
    public int countWin;
    public long time_die;
    public byte type_armor_create = -1;
    public byte id_armor_create = -1;
    public List<NpcTemplate> npcs;

    public void datatx() {
        if (this.tai == true) {
            this.taixiu = "Tài";
        }
        if (this.xiu == true) {
            this.taixiu = "Xỉu";
        }
        if (!this.tai && !this.xiu) {
            this.taixiu = "Bạn chưa đặt cược.";
        }
    }

    // kĩ năng mề
    public boolean isTangHinh;
    public long time_move;

    public boolean isDropMaterialMedal = true;
    public boolean isShowMobEvents = true;
    public ArenaTemplate PointArena;

    //kĩ năng khảm
//    public Kham_template kham;
    //create item star
    public boolean isCreateItemStar = false;
    public boolean isCreateArmor = false;
    public boolean isdothan = false;
    public boolean ismdthan = false;
    public boolean istb2 = false;
    public byte ClazzItemStar = -1;
    public byte TypeItemStarCreate = -1;
    public short[] MaterialItemStar;
    public short[] NLdothan;
    public short[] NLtb2;
    public int id_Upgrade_Medal_Star = -1;

    //biến heo chiến trường
    public long timeBienHeo;
    public long time_use_item_arena;
    public int id_henshin;
    public int count_special;

    public void ResetCreateItemStar() {
        isCreateItemStar = false;
        isdothan = false;
        ismdthan = false;
        istb2 = false;
        isCreateArmor = false;
        ClazzItemStar = -1;
        TypeItemStarCreate = -1;
    }

    public EffTemplate get_eff(int id) {
        for (int i = 0; i < list_eff.size(); i++) {
            EffTemplate temp = list_eff.get(i);
            if (temp.id == id) {
                return temp;
            }
        }
        return null;
    }

    public void update_point_arena(int i) throws IOException {
        Member_ChienTruong temp = ChienTruong.gI().get_infor_register(this.name);
        if (temp != null) {
            temp.point += i;
            this.pointarena += i;
            Service.send_health(this);
            Message m = new Message(-95);
            m.writer().writeByte(0);
            m.writer().writeShort(this.index);
            m.writer().writeShort(this.pointarena);
            this.conn.addmsg(m);
            m.cleanup();
        }
    }

    public void SetMaterialItemStar() {
        MaterialItemStar = new short[]{
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),
                (short) Util.random(417, 437), (short) Util.random(437, 457), (short) Util.random(326, 336), (short) Util.random(336, 346), (short) Util.random(457, 464),};
    }
    public void setnldothan() {
        short[] baseValues = {
                (short) Util.random(246, 268),
                (short) Util.random(268, 316),
                (short) Util.random(316, 336),
                (short) Util.random(326, 336),
                (short) Util.random(336, 346)
        };
        NLdothan = new short[baseValues.length * 8];
        for (int i = 0; i < NLdothan.length; i++) {
            NLdothan[i] = baseValues[i % baseValues.length];
        }
    }
    public void setnltb2() {
        short[] baseValues = {493};
        NLtb2 = new short[baseValues.length * 8];
        for (int i = 0; i < NLtb2.length; i++) {
            NLtb2[i] = baseValues[i % baseValues.length];
        }
    }

    public void ChangeMaterialItemStar(byte type) {
        if (type >= 8) {
            return;
        }
        MaterialItemStar[type * 5] = (short) Util.random(417, 437);
        MaterialItemStar[type * 5 + 1] = (short) Util.random(437, 457);

        MaterialItemStar[type * 5 + 2] = (short) Util.random(326, 336);
        MaterialItemStar[type * 5 + 3] = (short) Util.random(336, 346);

        MaterialItemStar[type * 5 + 4] = (short) Util.random(457, 464);
    }

    public void ChangeNL_dothan(byte type) {
//        if (type >= 8) {
//            return;
//        }
        NLdothan[type * 5] = (short) Util.random(246, 286);
        NLdothan[type * 5 + 1] = (short) Util.random(286, 316);
        NLdothan[type * 5 + 2] = (short) Util.random(316, 326);
        NLdothan[type * 5 + 3] = (short) Util.random(326, 336);
        NLdothan[type * 5 + 4] = (short) Util.random(336, 346);
    }

    public Player(Session conn, int id) {
        this.conn = conn;
        this.index = id;
        body = this;
        SetPlayer(this);
    }

    public void CheckSkillPoint() {
        for (int i = 0; i < skill_point.length; i++) {
            if (skill_point[i] <= 0) {
                continue;
            }
            LvSkill temp = skills[i].mLvSkill[skill_point[i] - 1];
            while (skill_point[i] > 0 && temp.LvRe > level) {
                temp = skills[i].mLvSkill[(--skill_point[i]) - 1];
                kynang++;
            }
        }
    }

    public boolean setup() {
        try {
            long _time = System.currentTimeMillis();
            String query = "SELECT * FROM `player` WHERE `id` = '" + this.index + "' LIMIT 1;";
            try (Connection connection = SQL.gI().getConnection(); Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery(query)) {
                if (!rs.next()) {
                    return false;
                }
                //
                this.kham = new Kham_template();
                this.name = rs.getString("name");
                this.timeBlockCTG = rs.getLong("time_block_ctg");
                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("body"));
                if (jsar == null) {
                    return false;
                }
                head = Byte.parseByte(jsar.get(0).toString());
                eye = Byte.parseByte(jsar.get(1).toString());
                hair = Byte.parseByte(jsar.get(2).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("site"));
                if (jsar == null) {
                    return false;
                }
                Map[] map_enter = Map.get_map_by_id(Short.parseShort(jsar.get(0).toString()));
                if (map_enter != null) {
                    x = Short.parseShort(jsar.get(1).toString());
                    y = Short.parseShort(jsar.get(2).toString());
                } else {
                    map_enter = Map.entrys.get(1);
                    x = 432;
                    y = 354;
                }
                map = map_enter[0];
                other_player_inside = new HashMap<>();
                other_mob_inside = new HashMap<>();
                other_mob_inside_update = new HashMap<>();
                bot_inside = new HashMap<>();
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("eff"));
                if (jsar == null) {
                    return false;
                }
//            list_eff = new ArrayList<>();
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    if (jsar2 == null) {
                        return false;
                    }
                    this.body.add_EffDefault(Integer.parseInt(jsar2.get(0).toString()), Integer.parseInt(jsar2.get(1).toString()),
                            (System.currentTimeMillis() + Long.parseLong(jsar2.get(2).toString())));
//                list_eff.add(
//                        new EffTemplate(Integer.parseInt(jsar2.get(0).toString()), Integer.parseInt(jsar2.get(1).toString()),
//                                (System.currentTimeMillis() + Long.parseLong(jsar2.get(2).toString()))));
                }
                jsar.clear();
                date = Util.getDate(rs.getString("date"));
                diemdanh = rs.getByte("diemdanh");
                chucphuc = rs.getByte("chucphuc");
                quatanthu = rs.getByte("quatanthu");
                mm_tt = rs.getByte("mm_tt");
                mm_md = rs.getByte("mm_md");
                dokho = rs.getByte("dokho");
                stt_level = rs.getByte("stt_level");
                khu2 = rs.getByte("khu2");
                hieuchien = rs.getInt("hieuchien");
                dibuon = rs.getInt("dibuon");
                dicuop = rs.getInt("dicuop");
                boss = rs.getInt("boss");
                phoban = rs.getInt("phoban");
                danhvong = rs.getLong("danhvong");
                doiqua = rs.getInt("doiqua");
                sk_hongio = rs.getInt("sk_hongio");
                chuyencan = rs.getInt("chuyencan");
                diemsukien = rs.getInt("diemsukien");
                type_exp = rs.getByte("typeexp");
                clazz = rs.getByte("clazz");
                level = rs.getShort("level");
                exp = rs.getLong("exp");
                type_use_mount = rs.getByte("type_use_mount");
                //
                if (level > Manager.gI().lvmax) {
                    level = (short) Manager.gI().lvmax;
                    if (exp >= Level.entrys.get(level - 1).exp) {
                        exp = Level.entrys.get(level - 1).exp - 1;
                    }
                }
                //
                vang = rs.getLong("vang");
                kimcuong = rs.getInt("kimcuong");
                isDie = false;
                tiemnang = rs.getShort("tiemnang");
                kynang = rs.getShort("kynang");
                point1 = rs.getShort("point1");
                point2 = rs.getShort("point2");
                point3 = rs.getShort("point3");
                point4 = rs.getShort("point4");
                pointarena = rs.getInt("point_arena");
                group_king_cup = rs.getByte("group_king_cup");
                point_king_cup = rs.getShort("point_king_cup");
                type_reward_king_cup = rs.getByte("type_reward_king_cup");
                short it_name_ = rs.getShort("id_name");
                if (it_name_ != -1) {
                    id_name = (short) (ItemTemplate3.item.get(it_name_).getPart() + 41);
                    id_name
                            = (short) (((it_name_ >= 4720 && it_name_ <= 4727) || (it_name_ >= 4765 && it_name_ <= 4767)) ? id_name
                            : 78);
                } else {
                    id_name = -1;
                }
                skill_point = new byte[21];
                time_delay_skill = new long[21];
                jsar = (JSONArray) JSONValue.parse(rs.getString("skill"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < 21; i++) {
                    skill_point[i] = Byte.parseByte(jsar.get(i).toString());
                    time_delay_skill[i] = 0;
                }
                jsar.clear();
                // load item

                maxbag = rs.getByte("maxbag");
                maxbox = 126;
                item = new Item(this);
                item.bag3 = new Item3[maxbag];
                item.box3 = new Item3[maxbag];
                item.wear = new Item3[24];
                skill_110 = new int[2];
                item.bag47 = new ArrayList<>();
                item.box47 = new ArrayList<>();
                for (int i = 0; i < 24; i++) {
                    item.wear[i] = null;
                }
                for (int i = 0; i < maxbag; i++) {
                    item.bag3[i] = null;
                    item.box3[i] = null;
                }
                jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Item47 temp = new Item47();
                    temp.id = Short.parseShort(jsar2.get(0).toString());
                    if (temp.id > ItemTemplate4.item.size()) {
                        continue;
                    }
                    temp.quantity = Short.parseShort(jsar2.get(1).toString());
                    temp.category = 4;
                    if (temp.quantity > 0) {
                        item.bag47.add(temp);
                    }
                    jsar2.clear();
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Item47 temp = new Item47();
                    temp.id = Short.parseShort(jsar2.get(0).toString());
                    temp.quantity = Short.parseShort(jsar2.get(1).toString());
                    temp.category = 7;
                    if (temp.quantity > 0) {
                        item.bag47.add(temp);
                    }
                    jsar2.clear();
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {

                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Item3 temp = new Item3();
                    temp.id = Short.parseShort(jsar2.get(0).toString());
                    temp.clazz = Byte.parseByte(jsar2.get(1).toString());
                    temp.type = Byte.parseByte(jsar2.get(2).toString());
                    temp.level = Short.parseShort(jsar2.get(3).toString());
                    temp.icon = Short.parseShort(jsar2.get(4).toString());
                    temp.color = Byte.parseByte(jsar2.get(5).toString());
                    temp.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.islock = Byte.parseByte(jsar2.get(7).toString()) == 1;
                    temp.name = ItemTemplate3.item.get(temp.id).getName();
                    if (temp.islock) {
                        temp.name += " [Khóa]";
                    }
                    if (!jsar2.get(8).toString().matches("\\d+")) {
                        jsar2.add(8, "0");
                    }
                    temp.tier = Byte.parseByte(jsar2.get(8).toString());

                    JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(9).toString());
                    temp.op = new ArrayList<>();
                    for (int j = 0; j < jsar3.size(); j++) {
                        JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                        temp.op.add(
                                new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()), temp.id));
                    }
                    temp.time_use = 0;
                    if (jsar2.size() >= 11) {
                        temp.tierStar = Byte.parseByte(jsar2.get(10).toString());
                    }
                    if (jsar2.size() >= 12) {
                        temp.time_use = Long.parseLong(jsar2.get(11).toString());
                    }
                    if (jsar2.size() >= 13) {
                        temp.expiry_date = Long.parseLong(jsar2.get(12).toString());
                    }
                    temp.UpdateName();
                    if (temp.expiry_date == 0 || temp.expiry_date > _time) {
                        item.bag3[i] = temp;
                    }
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("itemwear"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    try {
                        JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                        if (jsar2 == null) {
                            return false;
                        }
                        Item3 temp = new Item3();
                        temp.id = Short.parseShort(jsar2.get(0).toString());
                        temp.name = ItemTemplate3.item.get(temp.id).getName() + " [Khóa]";
                        temp.clazz = Byte.parseByte(jsar2.get(1).toString());
                        temp.type = Byte.parseByte(jsar2.get(2).toString());
                        temp.level = Short.parseShort(jsar2.get(3).toString());
                        temp.icon = Short.parseShort(jsar2.get(4).toString());
                        temp.color = Byte.parseByte(jsar2.get(5).toString());
                        temp.part = Byte.parseByte(jsar2.get(6).toString());
                        temp.tier = Byte.parseByte(jsar2.get(7).toString());
                        // if (temp.type == 15) {
                        // temp.tier = 0;
                        // }
                        temp.islock = true;
                        JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(8).toString());
                        temp.op = new ArrayList<>();
                        for (int j = 0; j < jsar3.size(); j++) {
                            JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                            if (jsar4 == null) {
                                return false;
                            }
                            temp.op.add(
                                    new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()), temp.id));
                        }
                        Byte idx = Byte.parseByte(jsar2.get(9).toString());
                        if (jsar2.size() >= 11) {
                            temp.tierStar = Byte.parseByte(jsar2.get(10).toString());
                        }
                        if (jsar2.size() >= 12) {
                            temp.expiry_date = Long.parseLong(jsar2.get(11).toString());
                        }
                        temp.time_use = 0;
                        temp.UpdateName();
                        if (temp.expiry_date == 0 || temp.expiry_date > _time) {
                            item.wear[idx] = temp;
                        }
                    } catch (Exception eee) {
                        eee.printStackTrace();
                    }
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("giftcode"));
                if (jsar == null) {
                    return false;
                }
                giftcode = new ArrayList<>();
                for (int i = 0; i < jsar.size(); i++) {
                    giftcode.add(jsar.get(i).toString());
                }
                jsar.clear();

                jsar = (JSONArray) JSONValue.parse(rs.getString("skill_110"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < 2; i++) {
                    skill_110[i] = Byte.parseByte(jsar.get(i).toString());
                }
                jsar.clear();

                // box
                jsar = (JSONArray) JSONValue.parse(rs.getString("itembox4"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    if (jsar2 == null) {
                        return false;
                    }
                    Item47 temp = new Item47();
                    temp.id = Short.parseShort(jsar2.get(0).toString());
                    temp.quantity = Short.parseShort(jsar2.get(1).toString());
                    temp.category = 4;
                    if (temp.quantity > 0) {
                        item.box47.add(temp);
                    }
                    jsar2.clear();
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("itembox7"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    if (jsar2 == null) {
                        return false;
                    }
                    Item47 temp = new Item47();
                    temp.id = Short.parseShort(jsar2.get(0).toString());
                    temp.quantity = Short.parseShort(jsar2.get(1).toString());
                    temp.category = 7;
                    if (temp.quantity > 0) {
                        item.box47.add(temp);
                    }
                    jsar2.clear();
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("itembox3"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {

                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Item3 temp = new Item3();
                    temp.id = Short.parseShort(jsar2.get(0).toString());
                    temp.clazz = Byte.parseByte(jsar2.get(1).toString());
                    temp.type = Byte.parseByte(jsar2.get(2).toString());
                    temp.level = Short.parseShort(jsar2.get(3).toString());
                    temp.icon = Short.parseShort(jsar2.get(4).toString());
                    temp.color = Byte.parseByte(jsar2.get(5).toString());
                    temp.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.islock = Byte.parseByte(jsar2.get(7).toString()) == 1;
                    temp.name = ItemTemplate3.item.get(temp.id).getName();
                    if (temp.islock) {
                        temp.name += " [Khóa]";
                    }
                    if (jsar2.get(8).toString().length() >= 3) {
                        jsar2.add(8, "0");
                    }
                    temp.tier = Byte.parseByte(jsar2.get(8).toString());

                    JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(9).toString());
                    temp.op = new ArrayList<>();
                    for (int j = 0; j < jsar3.size(); j++) {
                        JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                        temp.op.add(
                                new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()), temp.id));
                    }
                    temp.time_use = 0;
                    if (jsar2.size() >= 11) {
                        temp.tierStar = Byte.parseByte(jsar2.get(10).toString());
                    }
                    if (jsar2.size() >= 12) {
                        temp.time_use = Long.parseLong(jsar2.get(11).toString());
                    }
                    if (jsar2.size() >= 13) {
                        temp.expiry_date = Long.parseLong(jsar2.get(12).toString());
                    }
                    temp.UpdateName();
                    if (temp.expiry_date == 0 || temp.expiry_date > _time) {
                        item.box3[i] = temp;
                    }
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("rms_save"));
                if (jsar == null) {
                    return false;
                }
                rms_save = new byte[jsar.size()][];
                for (int i = 0; i < rms_save.length; i++) {
                    JSONArray js = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    rms_save[i] = new byte[js.size()];
                    for (int j = 0; j < rms_save[i].length; j++) {
                        rms_save[i][j] = Byte.parseByte(js.get(j).toString());
                    }
                }
                jsar.clear();
                //
                mypet = new ArrayList<>();
                pet_follow = -1;
                jsar = (JSONArray) JSONValue.parse(rs.getString("pet"));
                long t_off = 0;
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray js = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Pet temp = new Pet();
                    temp.setup(js);
                    temp.update_grown(t_off);
                    if (temp.is_follow) {
                        pet_follow = temp.get_id();
                    }
                    if (temp.expiry_date == 0 || _time < temp.expiry_date) {
                        mypet.add(temp);
                    }
                }
                jsar.clear();
                list_friend = new ArrayList<>();
                jsar = (JSONArray) JSONValue.parse(rs.getString("friend"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray js12 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Friend temp = new Friend();
                    temp.name = js12.get(0).toString();
                    temp.level = Short.parseShort(js12.get(1).toString());
                    temp.head = Byte.parseByte(js12.get(2).toString());
                    temp.hair = Byte.parseByte(js12.get(3).toString());
                    temp.eye = Byte.parseByte(js12.get(4).toString());
                    temp.itemwear = new ArrayList<>();
                    JSONArray js2 = (JSONArray) JSONValue.parse(js12.get(5).toString());
                    for (int j = 0; j < js2.size(); j++) {
                        JSONArray js3 = (JSONArray) JSONValue.parse(js2.get(j).toString());
                        Part_player part = new Part_player();
                        part.type = Byte.parseByte(js3.get(0).toString());
                        part.part = Byte.parseByte(js3.get(1).toString());
                        temp.itemwear.add(part);
                    }
                    list_friend.add(temp);
                }
                jsar.clear();
                list_enemies = new ArrayList<>();
                jsar = (JSONArray) JSONValue.parse(rs.getString("enemies"));
                if (jsar == null) {
                    return false;
                }
                for (int i = 0; i < jsar.size(); i++) {
                    String n = jsar.get(i).toString();
                    if (!list_enemies.contains(n)) {
                        list_enemies.add(n);
                    }
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("medal_create_material"));
                if (jsar == null) {
                    return false;
                }
                medal_create_material = new short[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    medal_create_material[i] = Short.parseShort(jsar.get(i).toString());
                }
                jsar.clear();

                jsar = (JSONArray) JSONValue.parse(rs.getString("item_star_material"));
                if (jsar == null) {
                    return false;
                }
                MaterialItemStar = new short[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    MaterialItemStar[i] = Short.parseShort(jsar.get(i).toString());
                }
                if (MaterialItemStar == null || MaterialItemStar.length < 40) {
                    SetMaterialItemStar();
                }
                jsar.clear();

                jsar = (JSONArray) JSONValue.parse(rs.getString("NL_tb2"));
                if (jsar == null) {
                    return false;
                }
                NLtb2 = new short[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    NLtb2[i] = Short.parseShort(jsar.get(i).toString());
                }
                if (NLtb2 == null || NLtb2.length < 40) {
                    setnltb2();
                }
                jsar.clear();

                jsar = (JSONArray) JSONValue.parse(rs.getString("NL_do_than"));
                if (jsar == null) {
                    return false;
                }
                NLdothan = new short[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    NLdothan[i] = Short.parseShort(jsar.get(i).toString());
                }
                if (NLdothan == null || NLdothan.length < 40) {
                    setnldothan();
                }
                jsar.clear();

                jsar = (JSONArray) JSONValue.parse(rs.getString("quest_daily"));
                if (jsar == null) {
                    return false;
                }
                quest_daily = new int[]{-1, -1, 0, 0, 20};
                for (int i = 0; i < 5; i++) {
                    if (jsar.size() < 5) continue;
                    quest_daily[i] = Integer.parseInt(jsar.get(i).toString());
                }
                jsar.clear();
                // đồ thần random
                jsar = (JSONArray) JSONValue.parse(rs.getString("st_ran"));
                if (jsar == null) {
                    return false;
                }
                st_ran = new  int[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    st_ran[i] = Integer.parseInt(jsar.get(i).toString());
                }
                //
                jsar = (JSONArray) JSONValue.parse(rs.getString("sc_ran"));
                if (jsar == null) {
                    return false;
                }
                sc_ran = new  int[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    sc_ran[i] = Integer.parseInt(jsar.get(i).toString());
                }
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("point_active"));
                if (jsar == null) {
                    return false;
                }
                point_active = new int[jsar.size()];
                for (int i = 0; i < jsar.size(); i++) {
                    point_active[i] = Integer.parseInt(jsar.get(i).toString());
                }
                jsar.clear();
                myclan = Clan.get_clan_of_player(this.name);
                //
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            //
            already_setup = true;
            return true;
        } catch (Exception eee) {
            eee.printStackTrace();
        }
        return false;
    }

    public void load_skill() throws IOException {
        ByteArrayInputStream bais = null;
        DataInputStream dis = null;
        try {
            switch (clazz) {
                case 0: { // chien binh
                    bais = new ByteArrayInputStream(Manager.gI().msg_29_chienbinh);
                    break;
                }
                case 1: { // sat thu
                    bais = new ByteArrayInputStream(Manager.gI().msg_29_satthu);
                    break;
                }
                case 2: { // phap su
                    bais = new ByteArrayInputStream(Manager.gI().msg_29_phapsu);
                    break;
                }
                case 3: { // xa thu
                    bais = new ByteArrayInputStream(Manager.gI().msg_29_xathu);
                    break;
                }
            }
            dis = new DataInputStream(bais);
            int size = dis.readByte();
            skills = new Skill[size];
            for (int i = 0; i < size; i++) {
                Skill skill = new Skill();
                skill.id = dis.readByte();
                skill.iconid = dis.readByte();
                skill.name = dis.readUTF();
                if (skill.id == 19 && skill_110[0] >= 1) {
                    skill.name += " [" + skill_110[0] + "]";
                } else if (skill.id == 20 && skill_110[1] >= 1) {
                    skill.name += " [" + skill_110[1] + "]";
                }
                skill.type = dis.readByte();
                skill.range = dis.readShort();
                skill.detail = dis.readUTF();
                skill.typeBuff = dis.readByte();
                skill.subEff = dis.readByte();
                byte b2 = dis.readByte();
                skill.mLvSkill = new LvSkill[(int) b2];
                for (int j = 0; j < (int) b2; j++) {
                    skill.mLvSkill[j] = new LvSkill();
                    skill.mLvSkill[j].mpLost = dis.readShort();
                    skill.mLvSkill[j].LvRe = dis.readShort();
                    skill.mLvSkill[j].delay = dis.readInt();
                    skill.mLvSkill[j].timeBuff = dis.readInt();
                    skill.mLvSkill[j].per_Sub_Eff = dis.readByte();
                    skill.mLvSkill[j].time_Sub_Eff = dis.readShort();
                    skill.mLvSkill[j].plus_Hp = dis.readShort();
                    skill.mLvSkill[j].plus_Mp = dis.readShort();
                    byte b3 = dis.readByte();
                    skill.mLvSkill[j].minfo = new Option[(int) b3];
                    for (int k = 0; k < (int) b3; k++) {
                        skill.mLvSkill[j].minfo[k] = new Option(dis.readUnsignedByte(), dis.readInt(), (short) 0);
                        if (skill.id == 19 && skill_110[0] > 1 && skill.mLvSkill[j].minfo[k].id >= 7 && skill.mLvSkill[j].minfo[k].id <= 11) {
                            skill.mLvSkill[j].minfo[k].param += 800 + skill_110[0] * 600;
                        } else if (skill.id == 20 && skill_110[1] > 1 && skill.mLvSkill[j].minfo[k].id >= 7 && skill.mLvSkill[j].minfo[k].id <= 11) {
                            skill.mLvSkill[j].minfo[k].param += 800 + skill_110[1] * 600;
                        }
                    }
                    skill.mLvSkill[j].nTarget = dis.readByte();
                    skill.mLvSkill[j].range_lan = dis.readShort();
                }
                skill.performDur = dis.readShort();
                skill.typePaint = dis.readByte();
                skills[skill.id] = skill;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (dis != null) {
                dis.close();
            }
            if (bais != null) {
                bais.close();
            }
        }
    }

    //    public EffTemplate get_EffDefault(int id) {
//        for (int i = 0; i < list_eff.size(); i++) {
//            EffTemplate temp = list_eff.get(i);
//            if (temp.id == id) {
//                return temp;
//            }
//        }
//        return null;
//    }
    public synchronized long get_vang() {
        return this.vang;
    }

    public synchronized int get_ngoc() {
        return this.kimcuong;
    }

    public synchronized void update_vang(long i) {
        if ((i + vang) > 2__000_000_000_000_000L) {
            vang = 2__000_000_000_000_000L;
        } else {
            vang += i;
        }
        try {
            Message m = new Message(16);
            m.writer().writeByte(0);
            m.writer().writeByte(5);
            m.writer().writeLong(this.get_vang());
            m.writer().writeInt(this.get_ngoc());
            m.writer().writeByte(5);
            m.writer().writeByte(0); // size item quest
            conn.addmsg(m);
            m.cleanup();
        } catch (Exception e) {
        }
    }

    public synchronized void update_ngoc(long i) {
        if ((i + kimcuong) > 2_000_000_000L) {
            kimcuong = 2_000_000_000;
        } else {
            kimcuong += i;
        }
        try {
            Message m = new Message(16);
            m.writer().writeByte(0);
            m.writer().writeByte(5);
            m.writer().writeLong(this.get_vang());
            m.writer().writeInt(this.get_ngoc());
            m.writer().writeByte(5);
            m.writer().writeByte(0); // size item quest
            conn.addmsg(m);
            m.cleanup();
        } catch (Exception e) {
        }

    }

    @SuppressWarnings("unchecked")
    public void flush() {
        if (!already_setup) {
            return;
        }
        if (isSquire) {
            return;
        }
        try (Connection connection = SQL.gI().getConnection(); Statement ps = connection.createStatement()) {
            if (isOwner) {
                String a = "`level` = " + level;
                a += ",`exp` = " + exp;
                JSONArray jsar = new JSONArray();
                if (isDie || Map.is_map_cant_save_site(map.map_id)) {
                    jsar.add(1);
                    jsar.add(432);
                    jsar.add(354);
                } else {
                    jsar.add(map.map_id);
                    jsar.add(x);
                    jsar.add(y);
                }
                a += ",`site` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                jsar.add(head);
                jsar.add(eye);
                jsar.add(hair);
                a += ",`body` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                for (int i = 0; i < MainEff.size(); i++) {
                    EffTemplate temp = MainEff.get(i);
                    if (temp.id != -126 && temp.id != -125 && temp.id != -127 && temp.id != -128 && temp.id != -129) {
                        continue;
                    }
                    JSONArray jsar21 = new JSONArray();
                    jsar21.add(temp.id);
                    jsar21.add(temp.param);
                    long time = temp.time - System.currentTimeMillis();
                    jsar21.add(time);
                    jsar.add(jsar21);
                }
                a += ",`eff` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                for (int i = 0; i < list_friend.size(); i++) {
                    JSONArray js12 = new JSONArray();
                    Friend temp = list_friend.get(i);
                    js12.add(temp.name);
                    js12.add(temp.level);
                    js12.add(temp.head);
                    js12.add(temp.hair);
                    js12.add(temp.eye);
                    JSONArray js = new JSONArray();
                    for (Part_player part : temp.itemwear) {
                        JSONArray js2 = new JSONArray();
                        js2.add(part.type);
                        js2.add(part.part);
                        js.add(js2);
                    }
                    js12.add(js);
                    jsar.add(js12);
                }
                a += ",`friend` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                for (int j : quest_daily) {
                    jsar.add(j);
                }
                a += ",`quest_daily` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                // random đồ thần
                for (int i = 0; i < st_ran.length; i++) {
                    jsar.add(st_ran[i]);
                }
                a += ",`st_ran` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                // random đồ thần
                for (int i = 0; i < sc_ran.length; i++) {
                    jsar.add(sc_ran[i]);
                }
                a += ",`sc_ran` = '" + jsar.toJSONString() + "'";
                jsar.clear();

                for (int i = 0; i < 21; i++) {
                    jsar.add(skill_point[i]);
                }
                a += ",`skill` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                for (int i = 0; i < 2; i++) {
                    jsar.add(skill_110[i]);
                }
                a += ",`skill_110` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (Item47 it : item.bag47) {
                    if (it.category == 4) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(it.id);
                        jsar2.add(it.quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`item4` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (Item47 it : item.bag47) {
                    if (it.category == 7) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(it.id);
                        jsar2.add(it.quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`item7` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                long _time = System.currentTimeMillis();
                for (int i = 0; i < item.bag3.length; i++) {
                    Item3 temp = item.bag3[i];
                    if (temp != null) {
                        if (temp.expiry_date != 0 && _time > temp.expiry_date) {
                            item.bag3[i] = null;
                            try {
                                conn.p.item.char_inventory(3);
                            } catch (IOException eee) {
                            }
                            continue;
                        }
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(temp.id);
                        jsar2.add(temp.clazz);
                        jsar2.add(temp.type);
                        jsar2.add(temp.level);
                        jsar2.add(temp.icon);
                        jsar2.add(temp.color);
                        jsar2.add(temp.part);
                        jsar2.add(temp.islock ? 1 : 0);
                        jsar2.add(temp.tier);
                        JSONArray jsar3 = new JSONArray();
                        for (int j = 0; j < temp.op.size(); j++) {
                            JSONArray jsar4 = new JSONArray();
                            jsar4.add(temp.op.get(j).id);
                            jsar4.add(temp.op.get(j).getParam(0));
                            jsar3.add(jsar4);
                        }
                        jsar2.add(jsar3);
                        jsar2.add(temp.tierStar);
                        jsar2.add(temp.time_use);
                        jsar2.add(temp.expiry_date);
                        jsar.add(jsar2);
                    }
                }
                a += ",`item3` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.wear.length; i++) {
                    Item3 temp = item.wear[i];
                    if (temp != null) {
                        if (temp.expiry_date != 0 && _time > temp.expiry_date) {
                            item.wear[i] = null;
                            try {
                                item.char_inventory(3);
                                fashion = Part_fashion.get_part(this);
                                Service.send_wear(this);
                                Service.send_char_main_in4(this);
                                MapService.update_in4_2_other_inside(this.map, this);
                            } catch (IOException eee) {
                            }
                            continue;
                        }
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(temp.id);
                        jsar2.add(temp.clazz);
                        jsar2.add(temp.type);
                        jsar2.add(temp.level);
                        jsar2.add(temp.icon);
                        jsar2.add(temp.color);
                        jsar2.add(temp.part);
                        jsar2.add(temp.tier);
                        JSONArray jsar3 = new JSONArray();
                        for (int j = 0; j < temp.op.size(); j++) {
                            JSONArray jsar4 = new JSONArray();
                            jsar4.add(temp.op.get(j).id);
                            jsar4.add(temp.op.get(j).getParam(0));
                            jsar3.add(jsar4);
                        }
                        jsar2.add(jsar3);
                        jsar2.add(i);
                        jsar2.add(temp.tierStar);
                        jsar2.add(temp.expiry_date);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itemwear` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < giftcode.size(); i++) {
                    jsar.add(giftcode.get(i));
                }
                a += ",`giftcode` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                for (int i = 0; i < list_enemies.size(); i++) {
                    jsar.add(list_enemies.get(i));
                }
                a += ",`enemies` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                for (int i = 0; i < rms_save.length; i++) {
                    JSONArray js = new JSONArray();
                    for (int i1 = 0; i1 < rms_save[i].length; i1++) {
                        js.add(rms_save[i][i1]);
                    }
                    jsar.add(js);
                }
                a += ",`rms_save` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.box47.size(); i++) {
                    if (item.box47.get(i).category == 4) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(item.box47.get(i).id);
                        jsar2.add(item.box47.get(i).quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itembox4` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.box47.size(); i++) {
                    if (item.box47.get(i).category == 7) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(item.box47.get(i).id);
                        jsar2.add(item.box47.get(i).quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itembox7` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.box3.length; i++) {
                    Item3 temp = item.box3[i];
                    if (temp != null) {
                        if (temp.expiry_date != 0 && _time > temp.expiry_date) {
                            item.box3[i] = null;
                            try {
                                conn.p.item.char_chest(3);
                            } catch (IOException eee) {
                            }
                            continue;
                        }
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(temp.id);
                        jsar2.add(temp.clazz);
                        jsar2.add(temp.type);
                        jsar2.add(temp.level);
                        jsar2.add(temp.icon);
                        jsar2.add(temp.color);
                        jsar2.add(temp.part);
                        jsar2.add(temp.islock ? 1 : 0);
                        jsar2.add(temp.tier);
                        JSONArray jsar3 = new JSONArray();
                        for (int j = 0; j < temp.op.size(); j++) {
                            JSONArray jsar4 = new JSONArray();
                            jsar4.add(temp.op.get(j).id);
                            jsar4.add(temp.op.get(j).getParam(0));
                            jsar3.add(jsar4);
                        }
                        jsar2.add(jsar3);
                        jsar2.add(temp.tierStar);
                        jsar2.add(temp.time_use);
                        jsar2.add(temp.expiry_date);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itembox3` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < mypet.size(); i++) {
                    JSONArray js1 = new JSONArray();
                    js1.add(mypet.get(i).level);
                    js1.add(mypet.get(i).type);
                    js1.add(mypet.get(i).icon);
                    js1.add(mypet.get(i).nframe);
                    js1.add(mypet.get(i).color);
                    js1.add(mypet.get(i).grown);
                    js1.add(mypet.get(i).maxgrown);
                    js1.add(mypet.get(i).point1);
                    js1.add(mypet.get(i).point2);
                    js1.add(mypet.get(i).point3);
                    js1.add(mypet.get(i).point4);
                    js1.add(mypet.get(i).maxpoint);
                    js1.add(mypet.get(i).exp);
                    js1.add(mypet.get(i).is_follow ? 1 : 0);
                    js1.add(mypet.get(i).is_hatch ? 1 : 0);
                    js1.add(mypet.get(i).time_born);
                    JSONArray js2 = new JSONArray();
                    for (int i2 = 0; i2 < mypet.get(i).op.size(); i2++) {
                        JSONArray js3 = new JSONArray();
                        js3.add(mypet.get(i).op.get(i2).id);
                        js3.add(mypet.get(i).op.get(i2).param);
                        js3.add(mypet.get(i).op.get(i2).maxdam);
                        js2.add(js3);
                    }
                    js1.add(js2);
                    js1.add(mypet.get(i).expiry_date);
                    jsar.add(js1);
                }
                a += ",`pet` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < medal_create_material.length; i++) {
                    jsar.add(medal_create_material[i]);
                }
                a += ",`medal_create_material` = '" + jsar.toJSONString() + "'";
                jsar.clear();

                //
                for (int i = 0; i < MaterialItemStar.length; i++) {
                    jsar.add(MaterialItemStar[i]);
                }
                a += ",`item_star_material` = '" + jsar.toJSONString() + "'";
                jsar.clear();

                //nldothan
                for (int i = 0; i < NLdothan.length; i++) {
                    jsar.add(NLdothan[i]);
                }
                a += ",`NL_do_than` = '" + jsar.toJSONString() + "'";
                jsar.clear();

                //nltb2
                for (int i = 0; i < NLtb2.length; i++) {
                    jsar.add(NLtb2[i]);
                }
                a += ",`NL_tb2` = '" + jsar.toJSONString() + "'";
                jsar.clear();

                for (int i = 0; i < point_active.length; i++) {
                    jsar.add(point_active[i]);
                }
                a += ",`point_active` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                a += ",`vang` = " + vang;
                a += ",`kimcuong` = " + kimcuong;
                a += ",`tiemnang` = " + tiemnang;
                a += ",`kynang` = " + kynang;
                a += ",`diemdanh` = " + diemdanh;
                a += ",`chucphuc` = " + chucphuc;
                a += ",`quatanthu` = " + quatanthu;
                a += ",`mm_tt` = " + mm_tt;
                a += ",`mm_md` = " + mm_md;
                a += ",`dokho` = " + dokho;
                a += ",`stt_level` = " + stt_level;
                a += ",`khu2` = " + khu2;
                a += ",`hieuchien` = " + hieuchien;
                a += ",`dibuon` = " + dibuon;
                a += ",`dicuop` = " + dicuop;
                a += ",`boss` = " + boss;
                a += ",`phoban` = " + phoban;
                a += ",`danhvong` = " + danhvong;
                a += ",`doiqua` = " + doiqua;
                a += ",`sk_hongio` = " + sk_hongio;
                a += ",`chuyencan` = " + chuyencan;
                a += ",`diemsukien` = " + diemsukien;
                a += ",`typeexp` = " + type_exp;
                a += ",`date` = '" + date.toString() + "'";
                a += ",`point1` = " + point1;
                a += ",`point2` = " + point2;
                a += ",`point3` = " + point3;
                a += ",`point4` = " + point4;
                a += ",`point_arena` = " + pointarena;
                a += ",`type_reward_king_cup` = " + type_reward_king_cup;
                a += ",`point_king_cup` = " + point_king_cup;
                a += ",`group_king_cup` = " + group_king_cup;
                int id_hore = type_use_mount;
                if (!Horse.isHorseClan(id_hore)) {
                    id_hore = -1;
                }
                a += ",`type_use_mount` = " + id_hore;

                if (ps.executeUpdate("UPDATE `player` SET " + a + " WHERE `id` = " + this.index + ";") > 0) {
                    connection.commit();
                }
                if (connection != null) {
                    ps.close();
                    connection.close();
                }
            } else {
                String a = "";
                JSONArray jsar = new JSONArray();
                for (Item47 it : item.bag47) {
                    if (it.category == 4) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(it.id);
                        jsar2.add(it.quantity);
                        jsar.add(jsar2);
                    }
                }
                a += "`item4` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (Item47 it : item.bag47) {
                    if (it.category == 7) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(it.id);
                        jsar2.add(it.quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`item7` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                long _time = System.currentTimeMillis();
                for (int i = 0; i < item.bag3.length; i++) {
                    Item3 temp = item.bag3[i];
                    if (temp != null) {
                        if (temp.expiry_date != 0 && _time > temp.expiry_date) {
                            item.bag3[i] = null;
                            try {
                                conn.p.item.char_inventory(3);
                            } catch (IOException eee) {
                            }
                            continue;
                        }
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(temp.id);
                        jsar2.add(temp.clazz);
                        jsar2.add(temp.type);
                        jsar2.add(temp.level);
                        jsar2.add(temp.icon);
                        jsar2.add(temp.color);
                        jsar2.add(temp.part);
                        jsar2.add(temp.islock ? 1 : 0);
                        jsar2.add(temp.tier);
                        JSONArray jsar3 = new JSONArray();
                        for (int j = 0; j < temp.op.size(); j++) {
                            JSONArray jsar4 = new JSONArray();
                            jsar4.add(temp.op.get(j).id);
                            jsar4.add(temp.op.get(j).getParam(0));
                            jsar3.add(jsar4);
                        }
                        jsar2.add(jsar3);
                        jsar2.add(temp.tierStar);
                        jsar2.add(temp.time_use);
                        jsar2.add(temp.expiry_date);
                        jsar.add(jsar2);
                    }
                }
                a += ",`item3` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.box47.size(); i++) {
                    if (item.box47.get(i).category == 4) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(item.box47.get(i).id);
                        jsar2.add(item.box47.get(i).quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itembox4` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.box47.size(); i++) {
                    if (item.box47.get(i).category == 7) {
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(item.box47.get(i).id);
                        jsar2.add(item.box47.get(i).quantity);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itembox7` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                //
                for (int i = 0; i < item.box3.length; i++) {
                    Item3 temp = item.box3[i];
                    if (temp != null) {
                        if (temp.expiry_date != 0 && _time > temp.expiry_date) {
                            item.box3[i] = null;
                            try {
                                conn.p.item.char_chest(3);
                            } catch (IOException eee) {
                            }
                            continue;
                        }
                        JSONArray jsar2 = new JSONArray();
                        jsar2.add(temp.id);
                        jsar2.add(temp.clazz);
                        jsar2.add(temp.type);
                        jsar2.add(temp.level);
                        jsar2.add(temp.icon);
                        jsar2.add(temp.color);
                        jsar2.add(temp.part);
                        jsar2.add(temp.islock ? 1 : 0);
                        jsar2.add(temp.tier);
                        JSONArray jsar3 = new JSONArray();
                        for (int j = 0; j < temp.op.size(); j++) {
                            JSONArray jsar4 = new JSONArray();
                            jsar4.add(temp.op.get(j).id);
                            jsar4.add(temp.op.get(j).getParam(0));
                            jsar3.add(jsar4);
                        }
                        jsar2.add(jsar3);
                        jsar2.add(temp.tierStar);
                        jsar2.add(temp.time_use);
                        jsar2.add(temp.expiry_date);
                        jsar.add(jsar2);
                    }
                }
                a += ",`itembox3` = '" + jsar.toJSONString() + "'";
                jsar.clear();
                a += ",`vang` = " + vang;
                a += ",`kimcuong` = " + kimcuong;

                if (ps.executeUpdate("UPDATE `player` SET " + a + " WHERE `id` = " + this.owner.index + ";") > 0) {
                    connection.commit();
                }
                if (connection != null) {
                    ps.close();
                    connection.close();
                }
                squire.flushSquire();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println("flush " + this.conn.user);
    }


    public void change_new_date() {
        if (!Util.is_same_day(Date.from(Instant.now()), date) && isOwner) {
            // diem danh
            diemdanh = 1;
            chucphuc = 1;
            if(quatanthu > 1) {
                quatanthu = 1;
            }
            if (Manager.gI().event == 8){
                diemsukien = 0;
            }
            khu2 = 2;
            point_active[0] = 2;
            point_active[1] = 0;
            quest_daily = new int[]{-1, -1, 0, 0, 10};
            date = Date.from(Instant.now());
        }
    }
    public void set_x2_xp(int type) throws IOException {
        switch (type) {
            case 0: {
                Message m = new Message(62);
                m.writer().writeByte(0);
                m.writer().writeShort(0);
                conn.addmsg(m);
                m.cleanup();
                break;
            }
            case 1: {
                EffTemplate tempp = conn.p.get_EffDefault(-125);
                if (tempp != null) {
                    long time_eff = tempp.time - System.currentTimeMillis();
                    Message m = new Message(62);
                    m.writer().writeByte(1);
                    m.writer().writeShort((short) (time_eff / 60000L));
                    conn.addmsg(m);
                    m.cleanup();
//                    add_EffDefault(-125, 5000, (int) time_eff);
                }
                break;
            }
        }
    }

    public void add_EffDefault(int id, int param, int time) {
        this.body.add_EffDefault(id, param, System.currentTimeMillis() + time);
    }

    public int getlevelpercent() {
        return (int) ((exp * 1000) / Level.entrys.get(level - 1).exp);
    }

    public void load_in4_autoplayer(byte[] num) {
        this.in4_auto = num;
        // System.out.println(hp_mp_can_pick);
        // num[0]; on off auto use poition (0 = off)
        // num[1]; %hp use poition
        // num[2]; %mp use poition
        // num[3]; on off pick item (0 = off)
        // num[4];(0 = all, 1 ->)
        // num[5]; (0 = all, 1 = non)
        // num[6]; (0 = all, 1 = hp, 2 = mp, 3 = non)
    }

    public void change_map(Player p, Vgo vgo) throws IOException {
//        if ((vgo.id_map_go == 36 || vgo.id_map_go == 50) && this.level < 40) {
//            Service.send_notice_nobox_white(conn, "Yêu cầu trình độ cấp 40");
//            return;
//        }
        if (p.pet_di_buon != null && !Map.is_map_di_buon(p.map.map_id)) {
            p.pet_di_buon = null;
            Service.send_notice_box(p.conn,"mày đã đi quá xa bò, bò của mày về với tổ tiên r mua bò mới đê");
            return;
        }
        if (map.map_id == 0) {
            Message m = new Message(55);
            m.writer().writeByte(1);
            m.writer().writeShort(2);
            m.writer().writeByte(-1);
            m.writer().writeByte(0);
            conn.addmsg(m);
            m.cleanup();
        }
        p.is_changemap = false;
        p.x_old = vgo.x_old;
        p.y_old = vgo.y_old;
        Map[] mbuffer = Map.get_map_by_id(vgo.id_map_go);
        if (mbuffer != null) {
            Map mbuffer2 = null;
            if (party != null) {
                for (int i = 0; i < party.get_mems().size(); i++) {
                    Player p0 = party.get_mems().get(i);
                    if (p0.map.map_id == mbuffer[0].map_id) {
                        mbuffer2 = p0.map;
                    }
                }
            }
            if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                    || conn.p.item.wear[11].id == 3596)) {
                mbuffer2 = mbuffer[mbuffer[0].maxzone];
            } else {
                if (mbuffer2 == null) {
                    for (Map mapp : mbuffer) {
                        if (mapp.players.size() < mapp.maxplayer) {
                            mbuffer2 = mapp;
                            break;
                        }
                    }
                }
            }
            if (mbuffer2 == null) {
                Service.send_notice_box(p.conn, "có lỗi xảy ra khi chuyển map");
                return;
            }
            // di buon
            boolean tele = true;
            for (Vgo item : p.map.vgos) {
                if (item.id_map_go == mbuffer2.map_id) {
                    tele = false;
                    break;
                }
            }
            if (p.pet_di_buon != null && !tele
                    && (Math.abs(p.pet_di_buon.x - p.x) < 125 && Math.abs(p.pet_di_buon.y - p.y) < 125)) {
                Message mout = new Message(8);
                mout.writer().writeShort(p.pet_di_buon.index);
                for (int i = 0; i < map.players.size(); i++) {
                    Player p0 = map.players.get(i);
                    if (p0 != null) {
                        p0.conn.addmsg(mout);
                    }
                }
                mout.cleanup();
                p.pet_di_buon.x = vgo.x_new;
                p.pet_di_buon.y = vgo.y_new;
                p.pet_di_buon.id_map = mbuffer2.map_id;
                Message m22 = new Message(4);
                m22.writer().writeByte(1);
                m22.writer().writeShort(131);
                m22.writer().writeShort(conn.p.pet_di_buon.index);
                m22.writer().writeShort(conn.p.pet_di_buon.x);
                m22.writer().writeShort(conn.p.pet_di_buon.y);
                m22.writer().writeByte(-1);
                conn.addmsg(m22);
                m22.cleanup();
            }
            //
            MapService.leave(p.map, p);
            p.map = mbuffer2;
            p.x = vgo.x_new;
            p.y = vgo.y_new;
            p.x_old = p.x;
            p.y_old = p.y;
            MapService.enter(p.map, p);
        } else {
            Service.send_notice_box(p.conn, "Có lỗi xảy ra khi chuyển map");
        }
    }
    public void update_Exp(long expup, boolean expmulti) throws IOException {
        long dame_exp = expup;
        if ((conn.p.level >= 140 && conn.p.stt_level < 1) || (conn.p.level >= 141 && conn.p.stt_level < 2) ||
                (conn.p.level >= 142 && conn.p.stt_level < 3) || (conn.p.level >= 143 && conn.p.stt_level < 4) ||
                (conn.p.level >= 144 && conn.p.stt_level < 5) || (conn.p.level >= 145 && conn.p.stt_level < 6) ||
                (conn.p.level >= 146 && conn.p.stt_level < 7) || (conn.p.level >= 147 && conn.p.stt_level < 8) ||
                (conn.p.level >= 148 && conn.p.stt_level < 9) || (conn.p.level >= 149 && conn.p.stt_level < 10) ||
                (conn.p.level >= 150 && conn.p.stt_level < 11) && !(conn.p.map_id == 136)){
            return;
        }
        if (expmulti && this.getlevelpercent() >= 0) {
            dame_exp *= Manager.gI().exp;
        }
        if (conn.p.isTrader() == true || conn.p.isRobber() == true || conn.p.isKnight() == true){
            dame_exp = 0;
        }
        if (type_use_mount == Horse.NGUA_DEN) {
            dame_exp += ((dame_exp * 5) / 100);
        }
        if ((type_exp == 0 && this.typepk != 0) || this.getlevelpercent() < (-500)) {
            return;
        }
        if (!isOwner && owner.level <= level) {
            level = owner.level;
            return;
        }
        if (this.map.zone_id == 1 && !Map.is_map_not_zone2(this.map.map_id)) { // Khu 2
            dame_exp += ((dame_exp * 5) / 100);
        }
        if (level >= Manager.gI().lvmax || type_exp == 0) {
            return;
        }
        Message m;
        if (this.getlevelpercent() < 0) {
            if (dame_exp > 0) {
                dame_exp /= 5;
            } else {
                dame_exp *= 2;
            }
        }
        exp += dame_exp;
        if (this.getlevelpercent() < (-500)) {
            exp = -(Level.entrys.get(level - 1).exp * 15) / 10;
        }
        int exp_as_int = 0;
        if (dame_exp > 2_000_000_000L) {
            exp_as_int = 2_000_000_000;
        } else {
            exp_as_int = (int) dame_exp;
        }
        if (exp >= Level.entrys.get(level - 1).exp) {
            while (exp >= Level.entrys.get(level - 1).exp && level < Manager.gI().lvmax) {
                exp -= Level.entrys.get(level - 1).exp;
                level++;
                if ((tiemnang + point1 + point2 + point3 + point4) < 32000) {
                    point1++;
                    point2++;
                    point3++;
                    point4++;
                    if (kynang < 10000) {
                        kynang += Level.entrys.get(level - 1).kynang;
                    }
                    tiemnang += Level.entrys.get(level - 1).tiemnang;
                }
            }
            if (level == Manager.gI().lvmax && exp >= Level.entrys.get(level - 1).exp) {
                exp = Level.entrys.get(level - 1).exp - 1;
            }
            hp = body.get_HpMax();
            mp = body.get_MpMax();
            m = new Message(33);
            m.writer().writeShort(index);
            m.writer().writeByte(level);
            MapService.send_msg_player_inside(map, this, m, true);
            m.cleanup();
            Service.send_char_main_in4(this);
            MapService.update_in4_2_other_inside(map, this);
            if (party != null) {
                party.sendin4();
            }
        }
        m = new Message(30);
        m.writer().writeShort(index);
        m.writer().writeShort(getlevelpercent());
        m.writer().writeInt(exp_as_int);
        conn.addmsg(m);
        m.cleanup();
    }

    public void change_zone(Session conn2, Message m2) throws IOException {

        if (this.map.map_id == 0) {
            Message m = new Message(55);
            m.writer().writeByte(1);
            m.writer().writeShort(2);
            m.writer().writeByte(-1);
            m.writer().writeByte(0);
            conn.addmsg(m);
            m.cleanup();
        }
        byte zone = m2.reader().readByte();
        Map map_change = Map.get_map_by_id(this.map.map_id)[zone];
        if (zone == map.maxzone && !conn.p.isKnight() && !conn.p.isRobber() && !conn.p.isTrader() && map_change.is_map_buon()) {
            return;
        }
        if (zone < this.map.maxzone || (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599
                || conn.p.item.wear[11].id == 3593 || conn.p.item.wear[11].id == 3596))) {
            if (zone != this.map.zone_id) {
                Map map = Map.get_map_by_id(this.map.map_id)[zone];
                if (map.players.size() >= map.maxplayer) {
                    Service.send_notice_box(conn, "Có lỗi xảy ra khi chuyển map hoặc đã đầy, hãy thử lại sau");
                    return;
                }
                if (zone == 1 && !Map.is_map_not_zone2(map_change.map_id)) {
                    EffTemplate ff = conn.p.get_EffDefault(-127);
                    if (ff == null) {
                        if (conn.p.khu2 > 0) {
                            conn.p.add_EffDefault(-127, 1, 2 * 60 * 60 * 1000);
                            MapService.leave(conn.p.map, conn.p);
                            conn.p.map = map_change;
                            MapService.enter(conn.p.map, conn.p);
                            khu2--;
//                        } else if (conn.p.item.total_item_by_id(4, (short) 54) >= 1) {
//                            MenuController.send_menu_select(conn, -43, new String[]{"Đồng bạc Tyche", "Dùng ngọc"}, (byte) 1);
                        } else {
                            Service.send_box_input_yesno(conn, -112, "Bạn có muốn vào khu 2 với 5k coin cho 2 giờ?");
                        }
                        return;
                    }
                }
                MapService.leave(this.map, this);
                this.map = map_change;
                MapService.enter(this.map, this);
            } else {
                Service.send_notice_box(conn, "Bạn đang ở khu vực này!");
            }
        }
    }

    public synchronized boolean update_coin(int coin_exchange) throws IOException {
        String query = "SELECT `coin` FROM `account` WHERE `user` = '" + conn.user + "' LIMIT 1;";
        int coin_old = 0;
        try (Connection connection = SQL.gI().getConnection(); Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery(query)) {
            rs.next();
            coin_old = rs.getInt("coin");
            if (coin_old + coin_exchange < 0) {
                Service.send_notice_box(conn, "Không đủ coin");
                return false;
            }
            coin_old += coin_exchange;
            if (ps.executeUpdate(
                    "UPDATE `account` SET `coin` = " + coin_old + " WHERE `user` = '" + conn.user + "'") == 1) {
                connection.commit();
            }
        } catch (SQLException e) {
            Service.send_notice_box(conn, "Đã xảy ra lỗi-p");
        }
        return true;
    }
    public synchronized boolean update_nap(int nap_exchange) throws IOException {
        String query = "SELECT `tichdiem` FROM `account` WHERE `user` = '" + conn.user + "' LIMIT 1;";
        int nap_old = 0;
        try (Connection connection = SQL.gI().getConnection(); Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery(query)) {
            rs.next();
            nap_old = rs.getInt("tichdiem");
            nap_old += nap_exchange;
            if (ps.executeUpdate(
                    "UPDATE `account` SET `tichdiem` = " + nap_old + " WHERE `user` = '" + conn.user + "'") == 1) {
                connection.commit();
            }
        } catch (SQLException e) {
            Service.send_notice_box(conn, "Đã xảy ra lỗi-p1");
        }
        return true;
    }
    public synchronized int checkcoin() {
        int result = 0;
        String query = "SELECT `coin` FROM `account` WHERE `user` = '" + conn.user + "' LIMIT 1;";
        try ( Connection connection = SQL.gI().getConnection();  Statement ps = connection.createStatement();  ResultSet rs = ps.executeQuery(query)) {
            rs.next();
            result = rs.getInt("coin");
        } catch (SQLException e) {
            result = 0;
        }
        return result;
    }
    public synchronized int check_nap() {
        int result = 0;
        String query = "SELECT `tichdiem` FROM `account` WHERE `user` = '" + conn.user + "' LIMIT 1;";
        try ( Connection connection = SQL.gI().getConnection();  Statement ps = connection.createStatement();  ResultSet rs = ps.executeQuery(query)) {
            rs.next();
            result = rs.getInt("tichdiem");
        } catch (SQLException e) {
            result = 0;
        }
        return result;
    }
    public synchronized int checkvip() {
        int result = 0;
        String query = "SELECT `Vip` FROM `account` WHERE `user` = '" + conn.user + "' LIMIT 1;";
        try ( Connection connection = SQL.gI().getConnection();  Statement ps = connection.createStatement();  ResultSet rs = ps.executeQuery(query)) {
            rs.next();
            result = rs.getByte("Vip");
        } catch (SQLException e) {
            result = 0;
        }
        return result;
    }
    public synchronized boolean history_coin(int coin_exchange, String log) throws IOException {
        String query
                = "INSERT INTO `history_coin` (`user_id`, `user_name`, `name_player` , `coin_change`, `logger`) VALUES ('"
                + this.conn.id + "', '" + this.conn.user + "', '" + this.name + "', '" + coin_exchange + "', '" + log + "')";
        try (Connection connection = SQL.gI().getConnection(); Statement statement = connection.createStatement();) {
            if (statement.executeUpdate(query) > 0) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void down_mount(Message m2) throws IOException {
        byte type = m2.reader().readByte();
        if (type == -1) {
            Message m = new Message(-97);
            m.writer().writeByte(0);
            m.writer().writeByte(-1);
            m.writer().writeShort(this.index);
            MapService.send_msg_player_inside(this.map, this, m, true);
            m.cleanup();
            this.type_use_mount = -1;
            this.id_horse = -1;
            MapService.update_in4_2_other_inside(this.map, this);
            Service.send_char_main_in4(this);
        }
    }

    public void rest_skill_point() throws IOException {
//        for (int i = 0; i < skill_point.length - 2; i++) {
//            if (skill_point[i] > 0) {
//                kynang += skill_point[i];
//            }
//        }
        short sk_110 = 0;
        for (int i = 0; i < skill_point.length; i++) {
            if (skill_point.length - i <= 2) {
                sk_110 += skill_point[i];
            } else if (skill_point[i] > 0) {
                skill_point[i] = 0;
            }
        }
        skill_point[0] = 1;
//        kynang -= 1;
        kynang = (short) (1 + Level.get_kynang_by_level(level - 1));
        kynang -= sk_110;
        hp = body.get_HpMax();
        mp = body.get_MpMax();
        Service.send_char_main_in4(this);
        for (int i = 0; i < map.players.size(); i++) {
            Player p0 = map.players.get(i);
            if (p0.index != this.index && ((Math.abs(p0.x - this.x) < 200 && Math.abs(p0.y - this.y) < 200)
                    || Map.is_map__load_board_player(map.map_id))) {
                MapService.send_in4_other_char(p0.map, p0, this);
            }
        }
    }

    public void rest_potential_point() throws IOException {
        tiemnang += (short) (point1 + point2 + point3 + point4);
        point1 = (short) (4 + level);
        point2 = (short) (4 + level);
        point3 = (short) (4 + level);
        point4 = (short) (4 + level);
        tiemnang -= (point1 + point2 + point3 + point4);
        hp = body.get_HpMax();
        mp = body.get_MpMax();
        Service.send_char_main_in4(this);
        for (int i = 0; i < map.players.size(); i++) {
            Player p0 = map.players.get(i);
            if (p0.index != this.index && ((Math.abs(p0.x - this.x) < 200 && Math.abs(p0.y - this.y) < 200)
                    || Map.is_map__load_board_player(map.map_id))) {
                MapService.send_in4_other_char(p0.map, p0, this);
            }
        }
    }

    public void checkFullSetTT() {
        if (item.wear[1] != null && item.wear[7] != null && item.wear[6] != null) {
            if (item.wear[1].isTT() && item.wear[7].isTT() && item.wear[6].isTT()) {
                item.wear[1].UpdateOption();
                item.wear[7].UpdateOption();
                item.wear[6].UpdateOption();
            }

        }
        if (item.wear[0] != null && item.wear[3] != null && item.wear[9] != null) {
            if (item.wear[0].isTT() && item.wear[3].isTT() && item.wear[9].isTT()) {
                item.wear[0].UpdateOption();
                item.wear[3].UpdateOption();
                item.wear[9].UpdateOption();
            }

        }
        if (item.wear[8] != null && item.wear[4] != null && item.wear[2] != null && item.wear[8].isTT() && item.wear[4].isTT() && item.wear[2].isTT()) {
            if (item.wear[8].isTT() && item.wear[4].isTT() && item.wear[2].isTT()) {
                item.wear[8].UpdateOption();
                item.wear[4].UpdateOption();
                item.wear[2].UpdateOption();
            }
        }
        if (item.wear[1] != null && item.wear[7] != null && item.wear[6] != null) {
            if (!item.wear[1].isTT() || !item.wear[7].isTT() || !item.wear[6].isTT()) {
                item.wear[1].ReUpdateOption();
                item.wear[7].ReUpdateOption();
                item.wear[6].ReUpdateOption();
            }
        }
        if (item.wear[0] != null && item.wear[3] != null && item.wear[9] != null) {
            if (!item.wear[0].isTT() || !item.wear[3].isTT() || !item.wear[9].isTT()) {
                item.wear[0].ReUpdateOption();
                item.wear[3].ReUpdateOption();
                item.wear[9].ReUpdateOption();
            }
        }
        if (item.wear[8] != null && item.wear[4] != null && item.wear[2] != null) {
            if (!item.wear[8].isTT() || !item.wear[4].isTT() || !item.wear[2].isTT()) {
                item.wear[8].ReUpdateOption();
                item.wear[4].ReUpdateOption();
                item.wear[2].ReUpdateOption();
            }
        }
    }

    public int countTT() {
        int count = 0;
        for (int i = 0; i < 10; i++) {
            if (i == 5) continue;
            if (item.wear[i] != null && item.wear[i].isTT() && item.wear[i].color == 5) {
                count++;
            }
        }
        return count;
    }

    public void player_wear(Session conn2, Item3 temp3, int index_bag, byte index_wear) throws IOException {
        byte b = -1;
        switch (temp3.type) {
            case 0: {// coat
                b = 1;
                break;
            }
            case 1: {// pant
                b = 7;
                break;
            }
            case 2: {// crown
                b = 6;
                break;
            }
            case 3: {// grove
                b = 2;
                break;
            }
            case 4: {// ring
                if (index_wear == 3 || index_wear == 9) {
                    b = index_wear;
                } else {
                    b = 3;
                }
                break;
            }
            case 5: {// chain
                b = 4;
                break;
            }
            case 6: {// shoes
                b = 8;
                break;
            }
            case 7: {// wing
                b = 10;
                break;
            }
            case 8:
            case 9:
            case 10:
            case 11: { // weapon
                b = 0;
                break;
            }
            case 15: {
                b = 11;
                break;
            }
            case 16: {
                b = 12;
                break;
            }
            case 21: {
                b = 13;
                break;
            }
            case 22: {
                b = 14;
                break;
            }
            case 23: {
                b = 15;
                break;
            }
            case 24: {
                b = 17;
                break;
            }
            case 25: {
                b = 16;
                break;
            }
            case 26: {
                b = 18;
                break;
            }
            case 27: {
                b = 19;
                break;
            }
            case 28: {
                b = 20;
                break;
            }
            case 55: {
                b = 21;
                break;
            }
            case 102: {
                b = 22;
                break;
            }
        }
        if (b == -1) {
            Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại");
            return;
        }
        if (item.wear[b] == null) {
            temp3.name = ItemTemplate3.item.get(temp3.id).getName() + " [Khóa]";
            temp3.UpdateName();
            item.wear[b] = temp3;
            checkFullSetTT();
            item.remove(3, index_bag, 1);
        } else {
            Item3 buffer = item.wear[b];
            temp3.name = ItemTemplate3.item.get(temp3.id).getName() + " [Khóa]";
            temp3.UpdateName();
            item.wear[b] = temp3;
            checkFullSetTT();
            item.remove(3, index_bag, 1);
            if (buffer.id != 3593 && buffer.id != 3599 && buffer.id != 3596) {
                buffer.ReUpdateOption();
                item.add_item_bag3(buffer);
            }
        }
        if (b == 11) {
            fashion = Part_fashion.get_part(this);
        }
        item.char_inventory(4);
        item.char_inventory(7);
        item.char_inventory(3);
        Service.send_wear(this);
        Service.send_char_main_in4(conn.p);
        MapService.update_in4_2_other_inside(this.map, this);
    }

    public void plus_point(Message m) throws IOException {
        byte type = m.reader().readByte();
        byte index = m.reader().readByte();
        short value = 1;
        try {
            value = m.reader().readShort();
        } catch (IOException e) {
        }
        if (isDie || value <= 0) {
            return;
        }
        if (type == 1) {
            if (kynang >= value) {
//                if (skill_point[index] == 0 && skills[index].mLvSkill[0].LvRe > this.level) {
//                    Service.send_notice_box(conn, "Level quá thấp!");
//                    return;
//                }
                if (skill_point[index] >= 10) {
                    Service.send_notice_box(conn, "Skill đã đạt cấp tối đa");
                    return;
                }
                if (skills[index].mLvSkill[skill_point[index]].LvRe > this.level) {
                    Service.send_notice_box(conn, "Cần tối thiểu level " + skills[index].mLvSkill[skill_point[index]].LvRe + " để có thể nâng cấp");
                    return;
                }
                if (skill_point[index] == 0 && (index == 19 || index == 20)) {
                    boolean dont_have_book_skill_110 = true;
                    switch (clazz) {
                        case 0: {
                            if (item.total_item_book_skill(4577) > 0 && index == 19) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4577) {
                                        item.bag3[i] = null;
                                    }
                                }
                            } else if (item.total_item_book_skill(4578) > 0 && index == 20) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4578) {
                                        item.bag3[i] = null;
                                    }
                                }
                            }
                            break;
                        }
                        case 1: {
                            if (item.total_item_book_skill(4579) > 0 && index == 19) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4579) {
                                        item.bag3[i] = null;
                                    }
                                }
                            } else if (item.total_item_book_skill(4580) > 0 && index == 20) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4580) {
                                        item.bag3[i] = null;
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            if (item.total_item_book_skill(4581) > 0 && index == 19) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4581) {
                                        item.bag3[i] = null;
                                    }
                                }
                            } else if (item.total_item_book_skill(4582) > 0 && index == 20) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4582) {
                                        item.bag3[i] = null;
                                    }
                                }
                            }
                            break;
                        }
                        case 3: {
                            if (item.total_item_book_skill(4583) > 0 && index == 19) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4583) {
                                        item.bag3[i] = null;
                                    }
                                }
                            } else if (item.total_item_book_skill(4584) > 0 && index == 20) {
                                dont_have_book_skill_110 = false;
                                for (int i = 0; i < item.bag3.length; i++) {
                                    if (item.bag3[i] != null && item.bag3[i].id == 4584) {
                                        item.bag3[i] = null;
                                    }
                                }
                            }
                            break;
                        }
                    }
                    if (dont_have_book_skill_110 && conn.ac_admin < 4) {
                        Service.send_notice_box(conn, "Chưa có sách kỹ năng để học!");
                        return;
                    }
                    if(conn.p.level < 111){
                        Service.send_notice_box(conn,"level 111 mới học được kĩ năng này");
                        return;
                    }
                    item.char_inventory(3);
                }
                if (skill_point[index] + value > skills[index].mLvSkill.length - 5) {
                    value = (short) (skills[index].mLvSkill.length - 5 - skill_point[index]);
                    kynang -= value;
                    skill_point[index] = (byte) (skills[index].mLvSkill.length - 5);
                } else {
                    kynang -= value;
                    skill_point[index] += value;
                }
                while (skill_point[index] > 0 && this.level < (skills[index].mLvSkill[skill_point[index]].LvRe)) {
                    kynang += 1;
                    skill_point[index] -= 1;
                }
                CheckSkillPoint();
                MapService.update_in4_2_other_inside(this.map, this);
                Service.send_char_main_in4(this);
            }
        } else if (type == 0) {
            if (tiemnang >= value) {
                switch (index) {
                    case 0: {
                        if ((point1 + value) <= 32000) {
                            point1 += value;
                            tiemnang -= value;
                        }
                        break;
                    }
                    case 1: {
                        if ((point2 + value) <= 32000) {
                            point2 += value;
                            tiemnang -= value;
                        }
                        break;
                    }
                    case 2: {
                        if ((point3 + value) <= 32000) {
                            point3 += value;
                            tiemnang -= value;
                        }
                        break;
                    }
                    case 3: {
                        if ((point4 + value) <= 32000) {
                            point4 += value;
                            tiemnang -= value;
                        }
                        break;
                    }
                }
                MapService.update_in4_2_other_inside(this.map, this);
                Service.send_char_main_in4(this);
            }
        } else if (type == 2) {
            if (index == 19 || index == 20) {
                if (conn.p.skill_point[index] != 10) {
                    conn.p.id_index_temp = -1;
                    Service.send_notice_box(conn, "Kỹ năng chưa được cộng tối đa");
                    return;
                }
                if (conn.p.skill_110[index - 19] >= 10) {
                    conn.p.id_index_temp = -1;
                    Service.send_notice_box(conn, "Kỹ năng được nâng cấp tối đa");
                    return;
                }
                this.id_index_temp = (byte) (index - 19);
                MenuController.send_menu_select(conn, 999, new String[]{"Nâng cấp bằng sách", "Nâng cấp bằng sách ghép"});
            }
        }
    }

    public void friend_process(Message m2) throws IOException {
        byte type = m2.reader().readByte();
        String name = m2.reader().readUTF();
        switch (type) {
            case 0: { // request friend
                for (Friend name0 : list_friend) {
                    if (name0.name.equals(name)) {
                        Service.send_notice_box(conn, (name + " đã có trong danh sách bạn bè!"));
                        return;
                    }
                }
                Player p0 = Map.get_player_by_name(name);
                if (p0 == null) {
                    Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại!");
                } else {
                    if (!p0.isOwner) {
                        return;
                    }
                    Message m = new Message(35);
                    m.writer().writeByte(0);
                    m.writer().writeUTF(this.name);
                    p0.conn.addmsg(m);
                    m.cleanup();
                }
                break;
            }
            case 1: { // accept
                Player p0 = Map.get_player_by_name(name);
                if (p0 == null) {
                    Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại!");
                } else {
                    boolean is_fr = false;
                    for (int i = 0; i < list_friend.size(); i++) {
                        if (list_friend.get(i).name.equals(name)) {
                            is_fr = true;
                            break;
                        }
                    }
                    if (!is_fr) {
                        Friend temp = new Friend();
                        temp.name = p0.name;
                        temp.level = p0.level;
                        temp.head = p0.head;
                        temp.hair = p0.hair;
                        temp.eye = p0.eye;
                        temp.itemwear = new ArrayList<>();
                        for (int i = 0; i < p0.item.wear.length; i++) {
                            Item3 it = p0.item.wear[i];
                            if (it != null && (i == 0 || i == 1 || i == 6 || i == 7 || i == 10)) {
                                Part_player part = new Part_player();
                                part.type = it.type;
                                part.part = it.part;
                                temp.itemwear.add(part);
                            }
                        }
                        list_friend.add(temp);
                        //
                        Message m = new Message(35);
                        m.writer().writeByte(1);
                        m.writer().writeUTF(temp.name);
                        m.writer().writeByte(temp.head);
                        m.writer().writeByte(temp.eye);
                        m.writer().writeByte(temp.hair);
                        m.writer().writeShort(temp.level);
                        m.writer().writeByte(temp.itemwear.size()); // part
                        for (Part_player part : temp.itemwear) {
                            m.writer().writeByte(part.part);
                            m.writer().writeByte(part.type);
                        }
                        m.writer().writeByte(1); // type onl
                        if (p0.myclan != null) {
                            m.writer().writeShort(p0.myclan.icon);
                            m.writer().writeUTF(p0.myclan.name_clan_shorted);
                            m.writer().writeByte(p0.myclan.get_mem_type(p0.name));
                        } else {
                            m.writer().writeShort(-1); // clan
                        }
                        conn.addmsg(m);
                        m.cleanup();
                        // //
                        temp = new Friend();
                        temp.name = this.name;
                        temp.level = level;
                        temp.head = head;
                        temp.hair = hair;
                        temp.eye = eye;
                        temp.itemwear = new ArrayList<>();
                        for (int i = 0; i < item.wear.length; i++) {
                            Item3 it = item.wear[i];
                            if (it != null && (i == 0 || i == 1 || i == 6 || i == 7 || i == 10)) {
                                Part_player part = new Part_player();
                                part.type = it.type;
                                part.part = it.part;
                                temp.itemwear.add(part);
                            }
                        }
                        //
                        p0.list_friend.add(temp);
                        //
                        m = new Message(35);
                        m.writer().writeByte(1);
                        m.writer().writeUTF(temp.name);
                        m.writer().writeByte(temp.head);
                        m.writer().writeByte(temp.eye);
                        m.writer().writeByte(temp.hair);
                        m.writer().writeShort(temp.level);
                        m.writer().writeByte(temp.itemwear.size()); // part
                        for (Part_player part : temp.itemwear) {
                            m.writer().writeByte(part.part);
                            m.writer().writeByte(part.type);
                        }
                        m.writer().writeByte(1); // type onl
                        if (this.myclan != null) {
                            m.writer().writeShort(this.myclan.icon);
                            m.writer().writeUTF(this.myclan.name_clan_shorted);
                            m.writer().writeByte(this.myclan.get_mem_type(this.name));
                        } else {
                            m.writer().writeShort(-1); // clan
                        }
                        p0.conn.addmsg(m);
                        m.cleanup();
                    } else {
                        Service.send_notice_box(conn, name + " đã là bạn");
                    }
                }
                break;
            }
            case 2: {
                Player p0 = Map.get_player_by_name(name);
                if (p0 == null) {
                    Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại!");
                } else {
                    Service.send_notice_box(p0.conn, (conn.p.name + " cự tuyệt lời mời kết bạn của bạn kkk!"));
                }
                break;
            }
            case 3: { // remove friend
                for (int i = 0; i < list_friend.size(); i++) {
                    Friend temp = list_friend.get(i);
                    if (temp.name.equals(name)) {
                        list_friend.remove(temp);
                        break;
                    }
                }
                break;
            }
            case 4: {
                Friend.send_list_friend(this);
                break;
            }
        }
    }

    public int get_pramskill_byid(byte index_skill, byte id_param) {
        int param = 0;
        for (Option temp : skills[index_skill].mLvSkill[body.get_skill_point(index_skill) - 1].minfo) {
            if (temp.id == id_param) {
                param += temp.getParam(0);
            }
        }
        return param;
    }

    public void set_in4() throws IOException {
        id_henshin = -1;
        this.already_setup = true;
        time_use_item_arena = System.currentTimeMillis() + 250_000L;
        load_skill();
//        try{
//            CheckSkillPoint();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        CheckSkillPoint();
        suckhoe = 30000;
        if (this.isTrader()) {
            typepk = 13;
        } else if (isRobber()) {
            typepk = 12;
        } else {
            typepk = -1;
        }
        pointpk = 0;
        hp = body.get_HpMax();
        mp = body.get_MpMax();
        fashion = Part_fashion.get_part(this);
        id_item_rebuild = -1;
        is_use_mayman = false;
        id_use_mayman = -1;
        item_replace = -1;
        item_replace2 = -1;
        id_buffer_126 = -1;
        id_index_126 = -1;
        id_medal_is_created = -1;
        fusion_material_medal_id = -1;
        id_remove_time_use = -1;
        id_horse = -1;
        is_create_wing = false;
        id_wing_split = -1;
        in4_auto = new byte[]{0, 50, 50, 0, 0, 0, 0, 0, 0, 0, 0};
        my_store = new ArrayList<>();
        my_store_name = "";
        id_select_mo_ly = -1;
        id_hop_ngoc = -1;
        list_thao_kham_ngoc = new ArrayList<>();
        this.it_wedding = Wedding.get_obj(this.name);
        if (this.it_wedding != null) {
            this.item.wear[23] = this.it_wedding.it;
        }

        //
        Map[] map_enter = Map.get_map_by_id(map.map_id);
        int d = 0;
        while ((d < (map_enter[d].maxzone - 1)) && map_enter[d].players.size() >= map_enter[d].maxplayer) {
            d++;
        }
        map = map_enter[d];
        //
        this.is_changemap = false;
        this.x_old = this.x;
        this.y_old = this.y;
        //
        HashMap<Short, Integer> hm = new HashMap<>();
        for (Item47 it : item.bag47) {
            if (it.category == 7) {
                if (!hm.containsKey(it.id)) {
                    hm.put(it.id, (int) it.quantity);
                } else {
                    int quant = hm.get(it.id);
                    hm.replace(it.id, quant, quant + it.quantity);
                }
            }
        }
        HashMap<Short, Integer> hm2 = new HashMap<>();
        for (Item47 it : item.bag47) {
            if (it.category == 4) {
                if (!hm2.containsKey(it.id)) {
                    hm2.put(it.id, (int) it.quantity);
                } else {
                    int quant = hm2.get(it.id);
                    hm2.replace(it.id, quant, quant + it.quantity);
                }
            }
        }
        item.bag47.clear();
        for (Entry<Short, Integer> entry : hm.entrySet()) {
            Item47 temp = new Item47();
            temp.category = 7;
            temp.id = entry.getKey();
            int quant_ = entry.getValue();
            temp.quantity = (short) quant_;
            item.bag47.add(temp);
        }
        for (Entry<Short, Integer> entry : hm2.entrySet()) {
            Item47 temp = new Item47();
            temp.category = 4;
            temp.id = entry.getKey();
            int quant_ = entry.getValue();
            temp.quantity = (short) quant_;
            item.bag47.add(temp);
        }
        //
        item.char_inventory(4);
        item.char_chest(4);
        item.char_inventory(7);
        item.char_chest(7);
        item.char_inventory(3);
        item.char_chest(3);

        npcs = new ArrayList<>();
        isOwner = true;
        owner = this;
        squire = new Squire(this.conn, this.index);
        squire = squire.load();
        Log.gI().add_log(this.name,
                "Login : [Vàng] : " + Util.number_format(this.vang) + " : [Ngọc] : " + Util.number_format(this.kimcuong));
    }

    public void update_wings_time() throws IOException {
        boolean check = false;
        for (int i = 0; i < item.bag3.length; i++) {
            Item3 it = item.bag3[i];
            if (it != null && it.type == 7 && it.time_use != 0) {
                if ((it.time_use - System.currentTimeMillis()) <= 0) {
                    it.time_use = 0;
                    check = true;
                }
            }
        }
        if (check) {
            item.char_inventory(4);
            item.char_inventory(7);
            item.char_inventory(3);
        }
    }

    public void change_map_di_buon(Player p) throws IOException {
        p.is_changemap = false;
        Map[] mbuffer = Map.get_map_by_id(p.map.map_id);
        if (mbuffer != null) {
            MapService.leave(p.map, p);
            p.map = mbuffer[p.map.maxzone];
            MapService.enter(p.map, p);
            if (p.pet_di_buon != null) {
                Message mout = new Message(8);
                mout.writer().writeShort(p.pet_di_buon.index);
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player p0 = p.map.players.get(i);
                    if (p0 != null) {
                        p0.conn.addmsg(mout);
                    }
                }
                mout.cleanup();
                //
                Pet_di_buon_manager.remove(p.pet_di_buon.name);
                p.pet_di_buon = null;
            }
        } else {
            Service.send_notice_box(p.conn, "Có lỗi xảy ra khi chuyển map");
        }
    }

    public void show_eff_p(int id_eff, int time) throws IOException {
        Message m = new Message(-49);
        m.writer().writeByte(2);
        m.writer().writeShort(0);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeByte(id_eff);
        m.writer().writeShort(this.index);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeInt(time);
        MapService.send_msg_player_inside(this.map, this, m, true);
        m.cleanup();
    }

    public int get_id_eff_skill(int type) {
        int id = 0;
        switch (this.clazz) {
            case 0:
                if (type == 19) {
                    id = 56;
                } else if (type == 20) {
                    id = 53;
                }
                break;
            case 1:
                if (type == 19) {
                    id = 59; // 59
                } else if (type == 20) {
                    id = 76;
                }
                break;
            case 2:
                if (type == 19) {
                    id = 49;
                } else if (type == 20) {
                    id = 56;
                }
                break;
            case 3:
                if (type == 19) {
                    id = 49;
                } else if (type == 20) {
                    id = 77;
                }
                break;
        }
        return id;
    }

    public void veLang() throws IOException {
        Vgo vgo = new Vgo();
        vgo.id_map_go = 1;
        vgo.x_new = 432;
        vgo.y_new = 354;
        change_map(conn.p, vgo);
    }

    public void goMapTapKet() throws IOException {
        Vgo vgo = new Vgo();
        vgo.id_map_go = 100;
        vgo.x_new = 312;
        vgo.y_new = 216;
        change_map(conn.p, vgo);
    }

    public boolean isKnight() {
        return item.wear[11] != null && item.wear[11].id >= 3596 && item.wear[11].id <= 3598;
    }

    public boolean isRobber() {
        return item.wear[11] != null && item.wear[11].id >= 3593 && item.wear[11].id <= 3595;
    }

    public boolean isTrader() {
        return item.wear[11] != null && item.wear[11].id >= 3599 && item.wear[11].id <= 3601;
    }

    public boolean isSonTinh() {
        return item.wear[11] != null && item.wear[11].id == 4585;
    }

    public boolean isThuyTinh() {
        return item.wear[11] != null && item.wear[11].id == 4586;
    }

    public void langPhuSuong() throws IOException {
        Vgo vgo = new Vgo();
        vgo.id_map_go = 135;
        vgo.x_new = 642;
        vgo.y_new = 318;
        conn.p.change_map(conn.p, vgo);
    }
    
//    public NpcTemplate findNPC(byte id) {
//        for (NpcTemplate npc : npcs) {
//            if (npc.id == id && Math.abs(npc.x - this.x) < 150 && Math.abs(npc.y - this.y) < 150) {
//                return npc;
//            }
//        }
//        return null;
//    }
}
