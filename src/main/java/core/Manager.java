package core;

import BossHDL.BossTG;
import ai.NhanBan;
import ev_he.*;
import event.Event_1;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import event_daily.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import client.Clan;
import gamble.VXKC2;
import gamble.VXMM2;
import io.Message;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import map.LeaveItemMap;
import map.Map;
import map.Mob_in_map;
import map.Vgo;
import org.json.simple.JSONObject;
import template.Clan_mems;
import template.Item3;
import template.Item47;
import template.ItemSell3;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Level;
import template.Medal_Material;
import template.Mob;
import template.Option;
import template.OptionItem;
import template.Part_fashion;
import template.Part_player;

public class Manager {

    public int size_mob;
    private static Manager instance;
    public final HashMap<String, Integer> ip_create_char = new HashMap<>();
    public final HashMap<String, Long> time_login_client = new HashMap<>();
    public final byte[] msg_25_new;
    public final byte[] msg_29_chienbinh;
    public final byte[] msg_29_satthu;
    public final byte[] msg_29_phapsu;
    public final byte[] msg_29_xathu;
    public final byte[][] data_part_char_x1;
    public final byte[][] data_part_char_x2;
    public final byte[][] data_part_char_x3;
    public final byte[][] data_part_char_x4;
    public final byte[] msg_1;
    public final byte[] msg_61;
    public final byte[] msg_26;
    public final byte[] msg_eff_70;
    public final byte[] msg_eff_71;
    public final byte[] msg_eff_109;
    public final byte[] msg_eff_105;
    public boolean debug;
    public String mysql_host;
    public String mysql_database;
    public String mysql_user;
    public String mysql_pass;
    public boolean isServerAdmin;
    public int event;
    public int server_port;
    public byte indexRes;
    public int indexCharPar;
    public int exp;
    public int lvmax;
    public int giakmngoc;
    public int giakmgold;
    public int vang;
    public int ngoc;
    public String thongbao;
    public int allow_ip_client;
    public int time_login;
    public List<ItemSell3[]> itemsellTB;
    public short[] itempoitionsell;
    public short[] item7sell;
    public short[] item7sell1;
    public VXMM2 vxmm;
    public VXKC2 vxkc;
    public int size_mob_now = -20;
    public ChiemMo chiem_mo;
     public ChienTruong chien_truong;
    public List<NhanBan> list_nhanban;
    public static boolean isLockVX = false;
    public static boolean isGiaoDich = false;
    public static boolean isqua = false;
    public static boolean isKmb = true;
    public static boolean isKTG = true;
    public static boolean isServerTest;
    public static boolean BuffAdmin = true;
    public static boolean BuffAdminMaterial = true;
    public static int timeRemoveClient = 1000 * 60;
    public static boolean logErrorLogin = false;
     public static String BXH_level = "";


    public static byte thue = 10;
    public static String nameClanThue;
    public static Clan ClanThue;
    public static final List<String> PlayersWinCThanh = new ArrayList<>();
    public static HashMap<Byte, List<Short>> item_sell;
    public BossTG bossTG;

    public static void setClanThue() {
        if (nameClanThue == null || nameClanThue.isEmpty()) {
            ResetCThanh();
            return;
        }
        for (Clan c : Clan.entrys) {
            if (c.name_clan.equals(nameClanThue)) {
                ClanThue = c;
                return;
            }
        }
        if (ClanThue == null) {
            ResetCThanh();
        }
    }

    public static void ResetCThanh() {
        PlayersWinCThanh.clear();
        nameClanThue = null;
        thue = 10;
        ClanThue = null;
    }

    public static final List<Integer> HouseWins = new ArrayList<>();
    public static int NgocCTWin;
    public static boolean VuaNhanQua;
    public static String VuaChienTruong = "";

    public static float ratio_hp = 1;

    private byte[] load_msg26() throws IOException {
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(Util.loadfile("data/msg/msg_26"));
        java.io.DataInputStream dis = new java.io.DataInputStream(bais);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        java.io.DataOutputStream ou = new DataOutputStream(os);
        short size = dis.readShort();
        ou.writeShort(size);
        for (int i = 0; i < size; i++) {
            short id = dis.readShort();
            String name = dis.readUTF();
            byte lv = dis.readByte();
            int maxhp = dis.readInt();
            byte b = dis.readByte();
            if (id == 151 || id == 152) {
                b = 17;
            }
            if (id == 129){
                name = "Ông già noel";
                b = 15;
            }
            ou.writeShort(id);
            ou.writeUTF(name);
            ou.writeByte(lv);
            ou.writeInt(maxhp);
            ou.writeByte(b);
        }
        ou.write(bais.readAllBytes());
        return os.toByteArray();
    }

    public Manager() throws IOException {
        this.msg_25_new = Util.loadfile("data/msg/msg_25_new");
        this.msg_29_chienbinh = Util.loadfile("data/msg/msg_29_chienbinh");
        this.msg_29_satthu = Util.loadfile("data/msg/msg_29_satthu");
        this.msg_29_phapsu = Util.loadfile("data/msg/msg_29_phapsu");
        this.msg_29_xathu = Util.loadfile("data/msg/msg_29_xathu");
        this.msg_1 = Util.loadfile("data/msg/msg_1");
        this.msg_61 = Util.loadfile("data/msg/msg_61");
//        this.msg_26 = Util.loadfile("data/msg/msg_26");
        this.msg_26 = load_msg26();
        this.msg_eff_70 = Util.loadfile("data/msg_eff/70");
        this.msg_eff_71 = Util.loadfile("data/msg_eff/71");
        this.msg_eff_109 = Util.loadfile("data/msg_eff/109");//109
        this.msg_eff_105 = Util.loadfile("data/msg_eff/105");
        //
        data_part_char_x1 = new byte[966][];
        data_part_char_x2 = new byte[966][];
        data_part_char_x3 = new byte[966][];
        data_part_char_x4 = new byte[966][];
        for (int i = 0; i < 966; i++) {
            data_part_char_x1[i] = Util.loadfile("data/part_char/x1/" + i + "_msg_-52");
            data_part_char_x2[i] = Util.loadfile("data/part_char/x2/" + i);
            data_part_char_x3[i] = Util.loadfile("data/part_char/x3/" + i);
            data_part_char_x4[i] = Util.loadfile("data/part_char/x4/" + i);
        }
    }

    public static Manager gI() {
        if (instance == null) {
            try {
                instance = new Manager();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("create cache fail!");
                System.exit(0);
            }
        }
        return instance;
    }

    public void init() {
        try {
            load_config();
            if (!load_database()) {
                System.err.println("load database err");
                System.exit(0);
                return;
            }
            list_nhanban = new ArrayList<>();
            chiem_mo = new ChiemMo();
            chiem_mo.init();
//            if (!chiem_mo.LoadData()) {
//                System.err.println("load database err");
//                System.exit(0);
//                return;
//            }
            System.out.println("cache loaded!");
            this.vxmm = new VXMM2();
            this.vxkc = new VXKC2();
            bossTG = new BossTG();
            bossTG.start_boss();
            Log.gI().start_log();
            for (Map[] temp : Map.entrys) {
                for (Map temp2 : temp) {
                    temp2.start_map();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("load database err");
            System.exit(0);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("load database err");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("load database err");
            System.exit(0);
        }
    }

    private boolean load_database() throws SQLException {
        // load item3
        Connection conn = SQL.gI().getConnection();
        Statement ps = conn.createStatement();
        ResultSet rs;
        String query = "SELECT * FROM `item3`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            ItemTemplate3 temp = new ItemTemplate3();
            temp.setId(rs.getShort("id"));
            temp.setName(rs.getString("name"));
            temp.setType(rs.getByte("type"));
            temp.setPart(rs.getByte("part"));
            temp.setClazz(rs.getByte("clazz"));
            temp.setIcon(rs.getShort("iconid"));
            temp.setLevel(rs.getShort("level"));
            temp.setColor(rs.getByte("color"));
            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("data"));
            if (jsar == null) {
                return false;
            }
            List<Option> buffer = new ArrayList<>();
            for (int i = 0; i < jsar.size(); i++) {
                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                if (jsar2 == null) {
                    return false;
                }
                buffer.add(new Option(Byte.parseByte(jsar2.get(0).toString()), Integer.parseInt(jsar2.get(1).toString()), (short) 0));
            }
            temp.setOp(buffer);
            // set type leave
            if (temp.getType() < 12 && temp.getType() != 7) {
                // load item leave
                switch (temp.getLevel()) {
                    case 1: {
                        LeaveItemMap.item0x.add(temp.getId());
                        break;
                    }
                    case 10: {
                        LeaveItemMap.item1x.add(temp.getId());
                        break;
                    }
                    case 20: {
                        LeaveItemMap.item2x.add(temp.getId());
                        break;
                    }
                    case 30: {
                        LeaveItemMap.item3x.add(temp.getId());
                        break;
                    }
                    case 40: {
                        LeaveItemMap.item4x.add(temp.getId());
                        break;
                    }
                    case 50: {
                        LeaveItemMap.item5x.add(temp.getId());
                        break;
                    }
                    case 60: {
                        LeaveItemMap.item6x.add(temp.getId());
                        break;
                    }
                    case 70: {
                        LeaveItemMap.item7x.add(temp.getId());
                        break;
                    }
                    case 80: {
                        LeaveItemMap.item8x.add(temp.getId());
                        break;
                    }
                    case 90: {
                        LeaveItemMap.item9x.add(temp.getId());
                        break;
                    }
                    case 100: {
                        LeaveItemMap.item10x.add(temp.getId());
                        break;
                    }
                    case 110: {
                        LeaveItemMap.item11x.add(temp.getId());
                        break;
                    }
                    case 120: {
                        LeaveItemMap.item12x.add(temp.getId());
                        break;
                    }
                    case 130: {
                        LeaveItemMap.item13x.add(temp.getId());
                        break;
                    }
                }
            }
            if (temp.getColor() == 2) {
                temp.setLevel((short) (temp.getLevel() + 2));
            } else if (temp.getColor() == 3) {
                temp.setLevel((short) (temp.getLevel() + 4));
            } else if (temp.getColor() == 4) {
                temp.setLevel((short) (temp.getLevel() + 5));
            }
            ItemTemplate3.item.add(temp);
        }
        rs.close();

        // load item4
        query = "SELECT * FROM `item4`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            ItemTemplate4 temp = new ItemTemplate4();
            temp.setId(rs.getShort("id"));
            temp.setIcon(rs.getShort("icon"));
            if (temp.getId() >= 113 && temp.getId() <= 116) {
                temp.setPrice(50);
            } else {
                temp.setPrice(rs.getLong("price"));
            }

            temp.setName(rs.getString("name"));
            temp.setContent(rs.getString("content"));
            temp.setType(rs.getByte("typepotion"));
            temp.setPricetype(rs.getByte("moneytype"));
            temp.setSell(rs.getByte("sell"));
            temp.setValue(rs.getShort("value"));
            temp.setTrade(rs.getByte("canTrade"));
            ItemTemplate4.item.add(temp);
        }
        rs.close();

        query = "SELECT * FROM `mconfig`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            if (rs.getString("name").equals("name_server")) {
                core.infoServer.NameServer = rs.getString("data");
            }
            if (rs.getString("name").equals("site_server")) {
                core.infoServer.Website = rs.getString("data");
            }
        }
        rs.close();
        // load item7
        query = "SELECT * FROM `item7`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            ItemTemplate7 temp = new ItemTemplate7();
            temp.setId(rs.getShort("id"));
            temp.setIcon(rs.getShort("imgid"));
            temp.setPrice(rs.getLong("price"));
            temp.setName(rs.getString("name"));
            temp.setContent(rs.getString("content"));
            temp.setType(rs.getByte("type"));
            temp.setPricetype(rs.getByte("pricetype"));
            temp.setSell(rs.getByte("sell"));
            temp.setValue(rs.getShort("value"));
            temp.setTrade(rs.getByte("trade"));
            temp.setColor(rs.getByte("setcolorname"));
            //temp.setLamcoin(rs.getByte("lamcoin"));
            ItemTemplate7.item.add(temp);
        }
        // load item medal
        for (int i = 0; i < 10; i++) {
            Medal_Material.m_blue[i] = (short) (i + 116);
            Medal_Material.m_yellow[i] = (short) (i + 126);
            Medal_Material.m_violet[i] = (short) (i + 136);
        }
        Medal_Material.m_white = new short[7][];
        int dem = 46;
        for (int i = 0; i < 7; i++) {
            Medal_Material.m_white[i] = new short[10];
            for (int j = 0; j < 10; j++) {
                Medal_Material.m_white[i][j] = (short) (dem++);
            }
        }
        //
        rs.close();
        // load item option
        query = "SELECT * FROM `itemoption`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            OptionItem temp = new OptionItem();
            temp.setName(rs.getString("name"));
            temp.setColor(rs.getByte("colorInfoItem"));
            temp.setIspercent(rs.getByte("isPercentInfoItem"));
            OptionItem.entrys.add(temp);
        }
        rs.close();
        // load item sell
        query = "SELECT * FROM `itemsell`;";
        rs = ps.executeQuery(query);
        itemsellTB = new ArrayList<>();

        item_sell = new HashMap<>();
        item_sell.put(Service.SHOP_POTION, new ArrayList<>());
        item_sell.put(Service.SHOP_ITEM, new ArrayList<>());
        item_sell.put(Service.SHOP_MATERIRAL, new ArrayList<>());
        while (rs.next()) {
            byte type = rs.getByte("id");
            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("data"));
            if (jsar == null) {
                return false;
            }
            switch (type) {
                case 0: {
                    switch (this.event) {
                        case 1: {
                            itempoitionsell = new short[jsar.size() + 4];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 4] = 113;
                            itempoitionsell[itempoitionsell.length - 3] = 114;
                            itempoitionsell[itempoitionsell.length - 2] = 115;
                            itempoitionsell[itempoitionsell.length - 1] = 116;
                            item_sell.get(Service.SHOP_POTION).add((short) 113);
                            item_sell.get(Service.SHOP_POTION).add((short) 114);
                            item_sell.get(Service.SHOP_POTION).add((short) 115);
                            item_sell.get(Service.SHOP_POTION).add((short) 116);
                            break;
                        }
                        case 2: {
                            itempoitionsell = new short[jsar.size() + 3];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 3] = 253;
                            itempoitionsell[itempoitionsell.length - 2] = 252;
                            itempoitionsell[itempoitionsell.length - 1] = 141;
                            item_sell.get(Service.SHOP_POTION).add((short) 253);
                            item_sell.get(Service.SHOP_POTION).add((short) 252);
                            item_sell.get(Service.SHOP_POTION).add((short) 141);
                            break;
                        }
                        case 3: {
                            itempoitionsell = new short[jsar.size() + 1];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 1] = 303;
                            item_sell.get(Service.SHOP_POTION).add((short) 303);
                            break;
                        }
                        case 4: {
                            itempoitionsell = new short[jsar.size() + 1];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 1] = 30;
                            item_sell.get(Service.SHOP_POTION).add((short) 30);
                            break;
                        }
                        case 7: {
                            itempoitionsell = new short[jsar.size() + 4];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 4] = 343;
                            itempoitionsell[itempoitionsell.length - 3] = 344;
                            itempoitionsell[itempoitionsell.length - 2] = 345;
                            item_sell.get(Service.SHOP_POTION).add((short) 343);
                            item_sell.get(Service.SHOP_POTION).add((short) 344);
                            item_sell.get(Service.SHOP_POTION).add((short) 345);
                            break;
                        }
                        case 8: {
                            itempoitionsell = new short[jsar.size() + 1];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 1] = 183;
                            item_sell.get(Service.SHOP_POTION).add((short) 183);
                            break;
                        }
                        case 11: {
                            itempoitionsell = new short[jsar.size() + 4];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 4] = 335;
                            itempoitionsell[itempoitionsell.length - 3] = 336;
                            itempoitionsell[itempoitionsell.length - 2] = 337;
                            itempoitionsell[itempoitionsell.length - 1] = 338;
                            item_sell.get(Service.SHOP_POTION).add((short) 335);
                            item_sell.get(Service.SHOP_POTION).add((short) 336);
                            item_sell.get(Service.SHOP_POTION).add((short) 337);
                            item_sell.get(Service.SHOP_POTION).add((short) 338);
                            break;
                        }
                        case 12: {
                            itempoitionsell = new short[jsar.size() + 1];
                            for (int i = 0; i < jsar.size(); i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            itempoitionsell[itempoitionsell.length - 1] = 350;
                            item_sell.get(Service.SHOP_POTION).add((short) 350);
                            break;
                        }
                        default: {
                            itempoitionsell = new short[jsar.size()];
                            for (int i = 0; i < itempoitionsell.length; i++) {
                                itempoitionsell[i] = Short.parseShort(jsar.get(i).toString());
                                item_sell.get(Service.SHOP_POTION).add(itempoitionsell[i]);
                            }
                            break;
                        }
                    }

                    break;
                }
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16: {
                    int size = jsar.size();
                    if (Manager.gI().event == 2 && type >= 1 && type <= 4) {
                        size += 6;
                    } else if (Manager.gI().event == 2 && type >= 5 && type <= 8) {
                        size += 4;
                    }
                    ItemSell3[] itemsell3 = new ItemSell3[size];
                    for (int i = 0; i < jsar.size(); i++) {
                        itemsell3[i] = new ItemSell3();
                        JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                        itemsell3[i].id = Short.parseShort(jsar2.get(0).toString());
                        item_sell.get(Service.SHOP_ITEM).add(itemsell3[i].id);
                        itemsell3[i].clazz = Byte.parseByte(jsar2.get(1).toString());
                        itemsell3[i].type = Byte.parseByte(jsar2.get(2).toString());
                        itemsell3[i].price = Long.parseLong(jsar2.get(3).toString());
                        itemsell3[i].level = Short.parseShort(jsar2.get(4).toString());
                        itemsell3[i].color = Byte.parseByte(jsar2.get(5).toString());
                        itemsell3[i].option = new ArrayList<>();
                        JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(6).toString());
                        for (int j = 0; j < jsar3.size(); j++) {
                            JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                            itemsell3[i].option.add(new Option(Byte.parseByte(jsar4.get(0).toString()),
                                    Integer.parseInt(jsar4.get(1).toString()), itemsell3[i].id));
                        }
                        itemsell3[i].pricetype = Byte.parseByte(jsar2.get(7).toString());
                    }
                    if (Manager.gI().event == 2 && type >= 1 && type <= 4) {
                        short[] id_ = new short[]{4714, 4715, 4769, 4770, 4771, 4772};
                        for (int i = 0; i < id_.length; i++) {
                            ItemTemplate3 temp = ItemTemplate3.item.get(id_[i]);
                            itemsell3[i + jsar.size()] = new ItemSell3();
                            itemsell3[i + jsar.size()].id = id_[i];
                            item_sell.get(Service.SHOP_ITEM).add(itemsell3[i + jsar.size()].id);
                            itemsell3[i + jsar.size()].clazz = temp.getClazz();
                            itemsell3[i + jsar.size()].type = temp.getType();
                            itemsell3[i + jsar.size()].price = 50;
                            itemsell3[i + jsar.size()].level = temp.getLevel();
                            itemsell3[i + jsar.size()].color = temp.getColor();
                            itemsell3[i + jsar.size()].option = new ArrayList<>();
                            itemsell3[i + jsar.size()].option.addAll(temp.getOp());
                            itemsell3[i + jsar.size()].pricetype = 1;
                        }
                    } else if (Manager.gI().event == 2 && type >= 5 && type <= 8) {
                        short[] id_ = new short[]{4716, 4717, 4718, 4719};
                        for (int i = 0; i < id_.length; i++) {
                            ItemTemplate3 temp = ItemTemplate3.item.get(id_[i]);
                            itemsell3[i + jsar.size()] = new ItemSell3();
                            itemsell3[i + jsar.size()].id = id_[i];
                            item_sell.get(Service.SHOP_ITEM).add(itemsell3[i + jsar.size()].id);
                            itemsell3[i + jsar.size()].clazz = temp.getClazz();
                            itemsell3[i + jsar.size()].type = temp.getType();
                            itemsell3[i + jsar.size()].price = 50;
                            itemsell3[i + jsar.size()].level = temp.getLevel();
                            itemsell3[i + jsar.size()].color = temp.getColor();
                            itemsell3[i + jsar.size()].option = new ArrayList<>();
                            itemsell3[i + jsar.size()].option.addAll(temp.getOp());
                            itemsell3[i + jsar.size()].pricetype = 1;
                        }
                    }

                    itemsellTB.add(itemsell3);
                    break;
                }
                case 17: {
                    item7sell = new short[jsar.size()];
                    for (int i = 0; i < item7sell.length; i++) {
                        item7sell[i] = Short.parseShort(jsar.get(i).toString());
                        item_sell.get(Service.SHOP_MATERIRAL).add(item7sell[i]);
                    }
                    break;
                }
                case 99: {
                    item7sell1 = new short[jsar.size()];
                    for (int i = 0; i < item7sell1.length; i++) {
                        item7sell1[i] = Short.parseShort(jsar.get(i).toString());
                        item_sell.get(Service.SHOP_MATERIRAL).add(item7sell1[i]);
                    }
                    break;
                }
            }
        }
        rs.close();
        //
        System.out.println("item loaded!");
        // load mob temp
        query = "SELECT * FROM `mobs`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            Mob temp = new Mob();
            temp.mob_id = Short.parseShort(rs.getString("id"));
            temp.name = rs.getString("name");
            temp.level = Short.parseShort(rs.getString("level"));
            temp.hpmax = Integer.parseInt(rs.getString("hp"));
            temp.typemove = Byte.parseByte(rs.getString("typemove"));
            Mob.entrys.add(temp);
        }
        rs.close();
        // load map
        query = "SELECT * FROM `maps`;";
        rs = ps.executeQuery(query);
        int index_mob = 0;
        while (rs.next()) {
            byte maxzone = rs.getByte("maxzone");
            Map[] temp_all_zone = new Map[maxzone + 1];
            short map_id = rs.getShort("id");
            String name = rs.getString("name");
            //
            List<Vgo> vgo_temp = new ArrayList<>();
            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("vgos"));
            if (jsar == null) {
                return false;
            }
            for (int i = 0; i < jsar.size(); i++) {
                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                Vgo vgo = new Vgo();
                vgo.id_map_go = Short.parseShort(jsar2.get(0).toString());
                vgo.x_old = Short.parseShort(jsar2.get(1).toString());
                vgo.y_old = Short.parseShort(jsar2.get(2).toString());
                vgo.name_map_go = jsar2.get(3).toString();
                vgo.x_new = Short.parseShort(jsar2.get(4).toString());
                vgo.y_new = Short.parseShort(jsar2.get(5).toString());
                vgo_temp.add(vgo);
            }
            jsar.clear();
            //
            jsar = (JSONArray) JSONValue.parse(rs.getString("npcs"));
            if (jsar == null) {
                return false;
            }
            String[] name_npc = new String[jsar.size()];
            for (int i = 0; i < jsar.size(); i++) {
                name_npc[i] = jsar.get(i).toString();
            }
            jsar.clear();
            //
            jsar = (JSONArray) JSONValue.parse(rs.getString("mobs"));
            if (jsar == null) {
                return false;
            }
            List<Mob_in_map> mob_in_map = new ArrayList<>();
            for (int i = 0; i < jsar.size(); i++) {
                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                Mob_in_map mob = new Mob_in_map();
                short id = Short.parseShort(jsar2.get(0).toString());
                if (id == 101 || id == 84 || id == 83 || id == 103 || id == 104 || id == 105 || id == 106 || id == 149 || id == 155 || id == 173 || id == 195 || id == 194 || id == 196 || id == 197
                        || id == 186 || id == 187 || id == 188 || id == 189 || id == 190 || id == 191 || id == 192 || id == 193 || id == 178) {
                    continue;
                }
                mob.template = Mob.entrys.get(id);
                mob.x = Short.parseShort(jsar2.get(1).toString());
                mob.y = Short.parseShort(jsar2.get(2).toString());
                mob.level = mob.template.level;
                mob.map_id = map_id;
                mob.isDie = false;
                mob.ishs = true;
                mob.time_back = System.currentTimeMillis() + 3_000L;
                mob.color_name = 0;
                mob.MainEff = new ArrayList<>();
                mob.Set_isBoss(false);
                mob.Set_hpMax(mob.template.hpmax);
                mob.hp = mob.get_HpMax();
//                mob.dame = mob.level * 75;
                mob_in_map.add(mob);
            }
            jsar.clear();
            //
            byte typemap = rs.getByte("type");
            byte maxplayer = rs.getByte("maxplayer");
            boolean ismaplang = rs.getByte("ismaplang") == 1;
            boolean showhs = rs.getByte("showhs") == 1;
            for (int i = 0; i < maxzone + 1; i++) {
                Map m = null;
                try {
                    m = new Map(map_id, i, name_npc, name, typemap, ismaplang, showhs, maxplayer, maxzone, vgo_temp);
                } catch (IOException e) {
                    System.err.println("load data map err " + map_id);
                    System.exit(0);
                }
                //
                if (i < maxzone) {
                    m.mobs = new Mob_in_map[mob_in_map.size()];
                    int idxmob = 1;
                    for (int i1 = 0; i1 < mob_in_map.size(); i1++) {
                        Mob_in_map mob = new Mob_in_map();
                        mob.index = idxmob++;
                        index_mob++;
                        mob.template = mob_in_map.get(i1).template;
                        mob.x = mob_in_map.get(i1).x;
                        mob.y = mob_in_map.get(i1).y;
                        mob.Set_hpMax(mob_in_map.get(i1).hp);
                        mob.hp = mob.get_HpMax();
                        mob.level = mob_in_map.get(i1).level;
                        mob.map_id = map_id;
                        mob.zone_id = (byte) i;
                        mob.isDie = mob_in_map.get(i1).isDie;
                        mob.color_name = mob_in_map.get(i1).color_name;
                        mob.Set_isBoss(mob_in_map.get(i1).isBoss());
                        mob.time_back = mob_in_map.get(i1).time_back;
                        mob.is_boss_active = false;
                        mob.map = m;
                        m.mobs[i1] = mob;
                        if (mob.isBoss()) {
                            if (Map.is_map_chien_truong(m.map_id)) {
                                ChienTruong.gI().boss.add(mob);
                                mob.level = 5;
                            }
                        }
                    }
                } else {
                    m.mobs = new Mob_in_map[0];
                }
                //
                temp_all_zone[i] = m;
            }
            Map.entrys.add(temp_all_zone);
        }
        rs.close();
        this.size_mob = index_mob;
        //
        System.out.println("map loaded, mob size " + (this.size_mob));
        // load level
        query = "SELECT * FROM `level`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            Level temp2 = new Level();
            temp2.level = rs.getShort("level");
            temp2.exp = rs.getLong("exp");
            temp2.tiemnang = rs.getShort("tiemnang");
            temp2.kynang = rs.getShort("kynang");
            Level.entrys.add(temp2);
        }
        if (lvmax > Level.entrys.size()) {
            for (int i = Level.entrys.size() + 1; i < lvmax + 2; i++) {
                Level temp2 = new Level();
                temp2.level = (short) i;
                temp2.exp = Level.entrys.get(i - 2).exp;
                temp2.tiemnang = 5;
                temp2.kynang = 1;
                Level.entrys.add(temp2);
            }
        }
        rs.close();
        // load part fashion temp
        query = "SELECT * FROM `fashiontemplate`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            Part_fashion temp = new Part_fashion();
            temp.id = (short) rs.getInt("id");
            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("part"));
            if (jsar == null) {
                return false;
            }
            temp.part = new short[jsar.size()];
            for (int i = 0; i < temp.part.length; i++) {
                temp.part[i] = Byte.parseByte(jsar.get(i).toString());
            }
            Part_fashion.fashions.add(temp.id);
            Part_fashion.entrys.add(temp);
        }
        System.out.println("part_fashion loaded!");
        rs.close();
        // load clan
        query = "SELECT * FROM `clan`;";
        rs = ps.executeQuery(query);
        List<Clan> clan_list = new ArrayList<>();
        while (rs.next()) {
            Clan temp = new Clan();
            temp.name_clan = rs.getString("name");
            temp.name_clan_shorted = rs.getString("name_short");
            temp.icon = rs.getShort("icon");
            temp.level = rs.getShort("level");
            temp.exp = rs.getLong("exp");
            temp.slogan = rs.getString("slogan");
            temp.rule = rs.getString("rule");
            temp.notice = rs.getString("notice");
            temp.setVang(rs.getLong("vang"));
            temp.setKimcuong(rs.getInt("kimcuong"));
            temp.max_mem = rs.getShort("max_mem");
            //temp.max_mem = Clan.get_mem_by_level(temp.level);
            //
            temp.item_clan = new ArrayList<>();
            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item"));
            if (jsar == null) {
                return false;
            }
            for (int i = 0; i < jsar.size(); i++) {
                JSONArray js2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                Item47 item = new Item47();
                item.id = Short.parseShort(js2.get(0).toString());
                item.quantity = Short.parseShort(js2.get(1).toString());;
                temp.item_clan.add(item);
            }
            //
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("mems"));
            if (jsar == null) {
                return false;
            }
            temp.mems = new ArrayList<>();
            for (int i = 0; i < jsar.size(); i++) {
                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                Clan_mems mem = new Clan_mems();
                mem.name = jsar2.get(0).toString();
                mem.mem_type = Byte.parseByte(jsar2.get(1).toString());
                mem.kimcuong = Integer.parseInt(jsar2.get(2).toString());
                mem.vang = Long.parseLong(jsar2.get(3).toString());
                mem.head = Byte.parseByte(jsar2.get(4).toString());
                mem.eye = Byte.parseByte(jsar2.get(5).toString());
                mem.hair = Byte.parseByte(jsar2.get(6).toString());
                mem.level = Short.parseShort(jsar2.get(7).toString());
                mem.itemwear = new ArrayList<>();
                JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(8).toString());
                for (int j = 0; j < jsar3.size(); j++) {
                    JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
                    Part_player part = new Part_player();
                    part.part = Byte.parseByte(jsar4.get(0).toString());
                    part.type = Byte.parseByte(jsar4.get(1).toString());
                    mem.itemwear.add(part);
                }
                temp.mems.add(mem);
            }
            temp.mo_tai_nguyen = new ArrayList<>();
            clan_list.add(temp);
        }
        Clan.set_clan(clan_list);
        System.out.println("clan loaded!");
        rs.close();
        // load event
        if (this.event == 1) {
            query = "SELECT * FROM `event` WHERE `id` = 0;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_1.LoadDB(jsob);
            }
        } else if (this.event == 2) {
            query = "SELECT * FROM `event` WHERE `id` = 1;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_2.LoadDB(jsob);
            }
        } else if (this.event == 3) {
            query = "SELECT * FROM `event` WHERE `id` = 2;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_3.LoadDB(jsob);
            }
        }else if (this.event == 4) {
            query = "SELECT * FROM `event` WHERE `id` = 3;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_4.LoadDB(jsob);
            }
        }else if (this.event == 5) {
            query = "SELECT * FROM `event` WHERE `id` = 4;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_5.LoadDB(jsob);
            }
        }
        else if (this.event == 6) {
            query = "SELECT * FROM `event` WHERE `id` = 5;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_6.LoadDB(jsob);
            }
        }
        else if (this.event == 8) {
            query = "SELECT * FROM `event` WHERE `id` = 6;";
            rs = ps.executeQuery(query);
            long t_ = System.currentTimeMillis();
            while (rs.next()) {
                JSONObject jsob = (JSONObject) JSONValue.parse(rs.getString("data"));
                Event_8.LoadDB(jsob);
            }
        }
        query = "SELECT * FROM `config_server`;";
        rs = ps.executeQuery(query);
        ChiemThanhManager.LoadData(rs);
        rs.close();
        System.out.println("event loaded!");
        query = "SELECT * FROM `wedding`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            Wedding temp_wed = new Wedding();
            JSONArray js_w = (JSONArray) JSONValue.parse(rs.getString("name"));
            temp_wed.name_1 = js_w.get(0).toString();
            temp_wed.name_2 = js_w.get(1).toString();
            js_w.clear();
            js_w = (JSONArray) JSONValue.parse(rs.getString("item"));
            temp_wed.exp = Long.parseLong(js_w.get(0).toString());
            temp_wed.it = new Item3();
            temp_wed.it.id = 0;
            temp_wed.it.name = "Cặp đôi yêu nhau " + temp_wed.name_1 + " " + temp_wed.name_2;
            temp_wed.it.clazz = 4;
            temp_wed.it.type = 103;
            temp_wed.it.level = 60;
            temp_wed.it.icon = 13165;
            temp_wed.it.color = Byte.parseByte(js_w.get(1).toString());
            temp_wed.it.part = 0;
            temp_wed.it.tier = Byte.parseByte(js_w.get(2).toString());
            temp_wed.it.islock = true;
            temp_wed.it.op = new ArrayList<>();
            JSONArray js_op = (JSONArray) JSONValue.parse(js_w.get(3).toString());
            for (int i = 0; i < js_op.size(); i++) {
                JSONArray js_op_2 = (JSONArray) JSONValue.parse(js_op.get(i).toString());
                temp_wed.it.op.add(
                        new Option(Byte.parseByte(js_op_2.get(0).toString()), Integer.parseInt(js_op_2.get(1).toString())));
            }
            temp_wed.it.time_use = 0;
            Wedding.list.add(temp_wed);
        }
        rs.close();
        // load loi dai data
        query = "SELECT * FROM `king_cup`;";
        rs = ps.executeQuery(query);
        while (rs.next()) {
            KingCupManager.TURN_KING_CUP = rs.getInt("turn_king_cup");
            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("group_65_74"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_65_74.add(jsar.get(i).toString());
            }
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("group_75_84"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_75_84.add(jsar.get(i).toString());
            }
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("group_85_94"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_85_94.add(jsar.get(i).toString());
            }
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("group_95_104"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_95_104.add(jsar.get(i).toString());
            }
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("group_105_114"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_105_114.add(jsar.get(i).toString());
            }
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("group_115_124"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_115_124.add(jsar.get(i).toString());
            }
            jsar.clear();
            jsar = (JSONArray) JSONValue.parse(rs.getString("group_125_139"));
            for (int i = 0; i < jsar.size(); i++) {
                KingCupManager.group_125_139.add(jsar.get(i).toString());
            }
            jsar.clear();
            KingCupManager.list_name.addAll(KingCupManager.group_65_74);
            KingCupManager.list_name.addAll(KingCupManager.group_75_84);
            KingCupManager.list_name.addAll(KingCupManager.group_85_94);
            KingCupManager.list_name.addAll(KingCupManager.group_95_104);
            KingCupManager.list_name.addAll(KingCupManager.group_105_114);
            KingCupManager.list_name.addAll(KingCupManager.group_115_124);
            KingCupManager.list_name.addAll(KingCupManager.group_125_139);
        }
        // close all
        rs.close();
        ps.close();
        conn.close();
        add_item_sell_other();
        return true;
    }

    public void load_config() throws IOException {
        final byte[] ab = Util.loadfile("hso.conf");
        if (ab == null) {
            System.out.println("Config file not found!");
            System.exit(0);
        }
        final String data = new String(ab);
        final HashMap<String, String> configMap = new HashMap<String, String>();
        final StringBuilder sbd = new StringBuilder();
        boolean bo = false;
        for (int i = 0; i <= data.length(); ++i) {
            final char es;
            if (i == data.length() || (es = data.charAt(i)) == '\n') {
                bo = false;
                final String sbf = sbd.toString().trim();
                if (sbf != null && !sbf.equals("") && sbf.charAt(0) != '#') {
                    final int j = sbf.indexOf(58);
                    if (j > 0) {
                        final String key = sbf.substring(0, j).trim();
                        final String value = sbf.substring(j + 1).trim();
                        configMap.put(key, value);
                        System.out.println("config: " + key + ": " + value);
                    }
                }
                sbd.setLength(0);
            } else {
                if (es == '#') {
                    bo = true;
                }
                if (!bo) {
                    sbd.append(es);
                }
            }
        }
        if (configMap.containsKey("timeRemoveClient")) {
            timeRemoveClient = Integer.parseInt(configMap.get("timeRemoveClient"));
        }
        if (configMap.containsKey("port")) {
            this.server_port = Integer.parseInt(configMap.get("port"));
        } else {
            this.server_port = 19129;
        }
        if (configMap.containsKey("server_admin")) {
            this.isServerAdmin = Boolean.parseBoolean(configMap.get("server_admin"));
        } else {
            this.isServerAdmin = false;
        }
        if (configMap.containsKey("debug")) {
            this.debug = Boolean.parseBoolean(configMap.get("debug"));
        } else {
            this.debug = false;
        }
        if (configMap.containsKey("mysql-host")) {
            this.mysql_host = configMap.get("mysql-host");
        } else {
            this.mysql_host = "127.0.0.1";
        }
        if (configMap.containsKey("mysql-user")) {
            this.mysql_user = configMap.get("mysql-user");
        } else {
            this.mysql_user = "root";
        }
        if (configMap.containsKey("mysql-password")) {
            this.mysql_pass = configMap.get("mysql-password");
        } else {
            this.mysql_pass = "12345678";
        }
        if (configMap.containsKey("mysql-database")) {
            this.mysql_database = configMap.get("mysql-database");
        } else {
            this.mysql_database = "hso";
        }
        if (configMap.containsKey("indexRes")) {
            this.indexRes = Byte.parseByte(configMap.get("indexRes"));
        } else {
            this.indexRes = 60;
        }
        if (configMap.containsKey("indexCharPar")) {
            this.indexCharPar = Short.parseShort(configMap.get("indexCharPar"));
        } else {
            this.indexCharPar = -22031;
        }
        if (configMap.containsKey("exp")) {
            this.exp = Integer.parseInt(configMap.get("exp"));
        } else {
            this.exp = 1;
        }
        if (configMap.containsKey("lvmax")) {
            this.lvmax = Short.parseShort(configMap.get("lvmax"));
        } else {
            this.lvmax = 150;
        }
        if (configMap.containsKey("giakmngoc")) {
            this.giakmngoc = Integer.parseInt(configMap.get("giakmngoc"));
        } else {
            this.giakmngoc = 1;
        }
        if (configMap.containsKey("giakmgold")) {
            this.giakmgold = Integer.parseInt(configMap.get("giakmgold"));
        } else {
            this.giakmgold = 1;
        }
        if (configMap.containsKey("vang")) {
            this.vang = Integer.parseInt(configMap.get("vang"));
        } else {
            this.vang = 0;
        }
        if (configMap.containsKey("kimcuong")) {
            this.ngoc = Integer.parseInt(configMap.get("kimcuong"));
        } else {
            this.ngoc = 0;
        }
        if (configMap.containsKey("thongbao")) {
            this.thongbao = configMap.get("thongbao");
        } else {
            this.thongbao = "";
        }
        if (configMap.containsKey("allow_ip_client")) {
            this.allow_ip_client = Integer.parseInt(configMap.get("allow_ip_client"));
        } else {
            this.allow_ip_client = 3;
        }
        if (configMap.containsKey("time_login")) {
            this.time_login = Integer.parseInt(configMap.get("time_login"));
        } else {
            this.time_login = 3000;
        }
        if (configMap.containsKey("event")) {
            this.event = Integer.parseInt(configMap.get("event"));
        } else {
            this.event = 0;
        }
        if (configMap.containsKey("server_test")) {
            isServerTest = Boolean.parseBoolean(configMap.get("server_test"));
        }
        if (configMap.containsKey("ratio_hp")) {
            ratio_hp = Float.parseFloat(configMap.get("ratio_hp"));
        }
    }

    public void chatKTGprocess(String s) throws IOException {
        Message m = new Message(53);
        m.writer().writeUTF(s);
        m.writer().writeByte(1);
        for (Map[] map : Map.entrys) {
            for (Map map0 : map) {
                for (int i = 0; i < map0.players.size(); i++) {
                    map0.players.get(i).conn.addmsg(m);
                }
            }
        }
        m.cleanup();
    }

    public void close() {
        vxmm.close();
        bossTG.stop_boss();
        Log.gI().close_log();
        //
        for (int i = 0; i < Map.entrys.size(); i++) {
            for (int j = 0; j < Map.entrys.get(i).length; j++) {
                Map.entrys.get(i)[j].stop_map();
            }
        }
//		instance = null;
    }

    public synchronized int get_index_mob_new() {
        if (this.size_mob_now < -5000) {
            this.size_mob_now = -20;
        }
        return (--this.size_mob_now);
    }

    public synchronized void add_list_nhanbban(NhanBan nhanban) {
        this.list_nhanban.add(nhanban);
    }

    public synchronized void remove_list_nhanbban(NhanBan nhanban) {
        this.list_nhanban.remove(nhanban);
    }

    public static void add_item_sell_other() {
        item_sell.get(Service.SHOP_POTION).add((short) 48);
        item_sell.get(Service.SHOP_POTION).add((short) 49);
        item_sell.get(Service.SHOP_POTION).add((short) 50);
        item_sell.get(Service.SHOP_POTION).add((short) 51);
        item_sell.get(Service.SHOP_POTION).add((short) 84);
        item_sell.get(Service.SHOP_POTION).add((short) 86);
        item_sell.get(Service.SHOP_POTION).add((short) 205);
        item_sell.get(Service.SHOP_POTION).add((short) 206);
        item_sell.get(Service.SHOP_POTION).add((short) 207);
        item_sell.get(Service.SHOP_POTION).add((short) 244);
        item_sell.get(Service.SHOP_POTION).add((short) 245);

        item_sell.get(Service.SHOP_ITEM).add((short) 2943);
        item_sell.get(Service.SHOP_ITEM).add((short) 2944);
        item_sell.get(Service.SHOP_ITEM).add((short) 3590);
        item_sell.get(Service.SHOP_ITEM).add((short) 3591);
        item_sell.get(Service.SHOP_ITEM).add((short) 3592);
    }
}
