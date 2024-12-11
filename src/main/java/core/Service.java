package core;

import History.His_COIN;
import History.His_DelItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import client.Clan;
import client.Pet;
import client.Player;
import event_daily.st;
import io.Message;
import io.Session;
import map.Eff_player_in_map;
import map.Map;
import map.MapService;
import map.Mob_in_map;
import template.*;

public class Service {

    public static final Byte SHOP_POTION = 0;
    public static final Byte SHOP_ITEM = 1;
    public static final Byte SHOP_MATERIRAL = 4;

    public static void send_msg_data(Session conn, int cmd, String name) throws IOException {
        Message m = new Message(cmd);
        m.writer().write(Util.loadfile("data/msg/" + name));
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_msg_data(Session conn, int cmd, byte[] data) throws IOException {
        Message m = new Message(cmd);
        m.writer().write(data);
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_item_template(Session conn) throws IOException {
        Message m = new Message(25);
        m.writer().writeShort(ItemTemplate4.item.size());
        for (ItemTemplate4 temp : ItemTemplate4.item) {
            m.writer().writeShort(temp.getId());
            m.writer().writeShort(temp.getIcon());
            m.writer().writeLong(temp.getPrice());
            m.writer().writeUTF(temp.getName());
            m.writer().writeUTF(temp.getContent());
            m.writer().writeByte(temp.getType());
            m.writer().writeByte(temp.getPricetype());
            m.writer().writeByte(temp.getSell());
            m.writer().writeShort(temp.getValue());
            m.writer().writeBoolean(temp.getTrade() == 1);
        }
        //
        m.writer().writeByte(OptionItem.entrys.size());
        for (OptionItem temp : OptionItem.entrys) {
            m.writer().writeUTF(temp.getName());
            m.writer().writeByte(temp.getColor());
            m.writer().writeByte(temp.getIspercent());
        }
        //
        if (conn.zoomlv > 1) {
            m.writer().writeShort(ItemTemplate7.item.size());
            for (ItemTemplate7 temp : ItemTemplate7.item) {
                m.writer().writeShort(temp.getId());
                m.writer().writeShort(temp.getIcon());
                m.writer().writeLong(temp.getPrice());
                m.writer().writeUTF(temp.getName());
                m.writer().writeUTF(temp.getContent());
                m.writer().writeByte(temp.getType());
                m.writer().writeByte(temp.getPricetype());
                m.writer().writeByte(temp.getSell());
                m.writer().writeShort(temp.getValue());
                m.writer().writeByte(temp.getTrade());
                m.writer().writeByte(temp.getColor());
                //m.writer().writeByte(temp.getLamcoin());
            }
        } else {
            m.writer().writeShort(0);
        }
        m.writer().write(Manager.gI().msg_25_new);
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_notice_box(Session conn, String s) throws IOException {
        Message m2 = new Message(37);
        m2.writer().writeUTF(s);
        m2.writer().writeUTF("");
        m2.writer().writeByte(15);
        conn.addmsg(m2);
        m2.cleanup();
    }

    public static void send_quest(Session conn) throws IOException {
        Message m = new Message(52);
        m.writer().writeByte(10);
        m.writer().writeByte(10);
        m.writer().writeByte(10);
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_auto_atk(Session conn) throws IOException {
        Message m = new Message(-108);
        m.writer().writeByte(5);
        m.writer().writeByte(0);
        conn.addmsg(m);
        m.cleanup();
    }
    public static void eff_map(Map map, Player p, int idnpc, int id_eff, int x, int y, int b3, int b4, int b7) throws IOException {
        byte[] data= Util.loadfile("data/msg_eff/" + id_eff);
        Message m = new Message(-49);
        m.writer().writeByte(1);
        m.writer().writeShort(data.length);
        m.writer().write(data);
        m.writer().writeByte(b3);
        m.writer().writeByte(b4);
        m.writer().writeByte(id_eff);
        m.writer().writeShort(x);
        m.writer().writeShort(y);
        m.writer().writeByte(0);
        m.writer().writeByte(b7);
        m.writer().writeShort(idnpc);
        m.writer().writeShort(0);
        m.writer().writeByte(2);
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void send_char_main_in4(Player p) throws IOException {
        //try{
        int hpMax = p.body.get_HpMax();
        int mpMax = p.body.get_MpMax();
        if (p.hp > hpMax) {
            p.hp = hpMax;
        }
        if (p.mp > mpMax) {
            p.mp = mpMax;
        }
        Message m = new Message(3);
        m.writer().writeShort(p.index);
        if(Manager.gI().event == 12){
            m.writer().writeUTF(p.name+"-VIP: "+p.checkvip());
        }else {
            m.writer().writeUTF(p.name);
        }
        m.writer().writeInt(p.hp);
        m.writer().writeInt(hpMax);
        m.writer().writeInt(p.mp);
        m.writer().writeInt(mpMax);
        m.writer().writeByte(p.head);
        m.writer().writeByte(p.clazz);
        m.writer().writeByte(p.eye);
        m.writer().writeByte(p.hair);
        //
        byte[] i1 = new byte[]{0, 1, 2, 3, 4, 53, 54, 55, 7, 8, 9, 10, 11, 14, 15, 16, 17, 18, 19, 20, 27, 28, 33, 34,
                35, 36, 40, -79, -78, 117,118};
        m.writer().writeByte(i1.length);
        for (int i = 0; i < i1.length; i++) {
            m.writer().writeByte(i1[i]);
            m.writer().writeInt(p.body.get_param_view_in4(i1[i]));
        }
        ///
        m.writer().writeShort(p.level); // lv
        m.writer().writeShort(p.getlevelpercent()); // lv percent
        m.writer().writeShort(p.tiemnang); // tiem nang
        m.writer().writeShort(p.kynang); // ky nang
        ///
        m.writer().writeShort(p.point1); // tiem nang goc
        m.writer().writeShort(p.point2);
        m.writer().writeShort(p.point3);
        m.writer().writeShort(p.point4);
        ///
        m.writer().writeShort(p.body.get_plus_point(23)); // tiem nang them
        m.writer().writeShort(p.body.get_plus_point(24));
        m.writer().writeShort(p.body.get_plus_point(25));
        m.writer().writeShort(p.body.get_plus_point(26));
        ///// skill point
        for (int i = 0; i < 21; i++) {
            m.writer().writeByte(p.skill_point[i]);
        }
        // skill plus point
        for (int i = 0; i < 21; i++) {
            int pointP = p.body.get_skill_point_plus(i);
//            if(pointP>0){
//                LvSkill temp = p.skills[i].mLvSkill[p.skill_point[i] + (pointP -1)];
//                while(pointP > 0 && temp.LvRe > p.level){
//                    temp = p.skills[i].mLvSkill[p.skill_point[i] + (pointP -1)];
//                    pointP --;
//                }
//            }

            m.writer().writeByte(pointP);
        }
        m.writer().writeByte(p.typepk);
        m.writer().writeShort(p.pointpk);
        m.writer().writeByte(p.maxbag); // max bag
        if (p.myclan != null) {
            m.writer().writeShort(p.myclan.icon);
            m.writer().writeInt(Clan.get_id_clan(p.myclan));
            m.writer().writeUTF(p.myclan.name_clan_shorted);
            m.writer().writeByte(p.myclan.get_mem_type(p.name));
        } else {
            m.writer().writeShort(-1); // clan
        }
        m.writer().writeUTF("Vĩnh lỏh-Khu 2: ");
        if (p.get_EffDefault(-127) != null) {
            long time = p.get_EffDefault(-127).time;
            m.writer().writeLong(time);
        } else {
            m.writer().writeLong(0);
        }
        m.writer().writeByte(p.fashion.length);
        for (int i = 0; i < p.fashion.length; i++) {
            if (p.conn.version >= 280) {
                m.writer().writeShort(p.fashion[i]);
            } else {
                m.writer().writeByte(p.fashion[i]);
            }
        }
        m.writer().writeByte(3); // nap tien?
        m.writer().writeShort(get_id_mat_na(p)); // id mat na
        m.writer().writeByte(1); // paint mat na trc sau
        m.writer().writeShort(get_id_phiphong(p)); // phi phong
        m.writer().writeShort(get_id_weapon(p)); // id weapon
        m.writer().writeShort(p.id_horse);
        m.writer().writeShort(get_id_hair(p)); // idHair
        m.writer().writeShort(get_id_wing(p)); // idWing
        m.writer().writeShort(get_id_danhhieu(p)); // phi phong
        m.writer().writeShort(-1); // idName
        m.writer().writeShort(p.id_name); // idBody
        // m.writer().writeShort(-1); // idLeg
        // m.writer().writeShort(-1); // idBienhinh
        //
        p.conn.addmsg(m);
        m.cleanup();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    public static short get_id_hair(Player p) {
        short result = -1;
        if (p.item.wear[16] != null) {
            result = (short) (p.item.wear[16].part + 41);
        }
        return result;
    }

    public static short get_id_weapon(Player p) {
        short result = -1;
        if (p.item.wear[17] != null) {
            result = (short) (p.item.wear[17].part + 41);
        }
        return result;
    }

    public static short get_id_phiphong(Player p) {
        short result = -1;
        if (p.item.wear[15] != null) {
            result = (short) (p.item.wear[15].part + 41);
        }
        return result;
    }

    public static short get_id_danhhieu(Player p) {
        short result = -1;
        if (p.item.wear[19] != null) {
            result = (short) (p.item.wear[19].part + 41);
        }
        return result;
    }

    public static short get_id_wing(Player p) {
        short result = -1;
        if (p.item.wear[14] != null) {
            switch (p.item.wear[14].id) {
                case 4638:
                case 4639:
                case 4640:
                case 4641:
                case 4642:
                case 4643:
                case 4644:
                case 4645:
                case 4646:
                case 4647:
                case 4648: {
                    break;
                }
                case 4707: {
                    result = 75;
                    break;
                }
                case 4712: {
                    result = 82;
                    break;
                }
                case 4713: {
                    result = 83;
                    break;
                }
                case 4773: {
                    result = 128;
                    break;
                }
                case 4774: {
                    result = 129;
                    break;
                }
                case 4789: {
                    result = 130;
                    break;
                }
                case 4790: {
                    result = 131;
                    break;
                }

                case 4793: {
                    result = 84;
                    break;
                }


                case 4794: {
                    result = 85;
                    break;
                }


                case 4795: {
                    result = 86;
                    break;
                }


                case 4796: {
                    result = 87;
                    break;
                }


                case 4797: {
                    result = 88;
                    break;
                }


                // default: {
                // result = (short) (p.item.wear[14].part + 41);
                // break;
                // }
            }
        }
        return result;
    }

    public static short get_id_mat_na(Player p) {
        short result = -1;
        if (p.item.wear[13] != null) {
            result = (short) (p.item.wear[13].part + 41);
        }
        return result;
    }

    public static void send_skill(Player p) throws IOException {
        Message m = new Message(29);
        m.writer().writeByte(p.skills.length);
        for (int i = 0; i < p.skills.length; i++) {
            Skill skill = p.skills[i];
            m.writer().writeByte(skill.id);
            m.writer().writeByte(skill.iconid);
            m.writer().writeUTF(skill.name);
            m.writer().writeByte(skill.type);
            m.writer().writeShort(skill.range);
            m.writer().writeUTF(skill.detail);
            m.writer().writeByte(skill.typeBuff);
            m.writer().writeByte(skill.subEff);
            m.writer().writeByte(skill.mLvSkill.length);

            for (int j = 0; j < skill.mLvSkill.length; j++) {
                m.writer().writeShort(skill.mLvSkill[j].mpLost);
                m.writer().writeShort(skill.mLvSkill[j].LvRe);
                m.writer().writeInt(skill.mLvSkill[j].delay);
                m.writer().writeInt(skill.mLvSkill[j].timeBuff);
                m.writer().writeByte(skill.mLvSkill[j].per_Sub_Eff);
                m.writer().writeShort(skill.mLvSkill[j].time_Sub_Eff);
                m.writer().writeShort(skill.mLvSkill[j].plus_Hp);
                m.writer().writeShort(skill.mLvSkill[j].plus_Mp);
                m.writer().writeByte(skill.mLvSkill[j].minfo.length);

                for (int k = 0; k < skill.mLvSkill[j].minfo.length; k++) {
                    m.writer().writeByte(skill.mLvSkill[j].minfo[k].id);
                    m.writer().writeInt(skill.mLvSkill[j].minfo[k].param);
                }
                m.writer().writeByte(skill.mLvSkill[j].nTarget);
                m.writer().writeShort(skill.mLvSkill[j].range_lan);
            }
            m.writer().writeByte(skill.performDur);
            m.writer().writeShort(skill.typePaint);
        }
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void send_login_rms(Session conn) throws IOException {
        // id 1
        Message m = new Message(55);
        m.writer().writeByte(1);
        m.writer().writeShort(2);
        m.writer().writeByte(-1);
        m.writer().writeByte(0);
        conn.addmsg(m);
        m.cleanup();
        // id 2
        m = new Message(55);
        m.writer().writeByte(2);
        if (conn.p.map.map_id == 0 && conn.p.level < 2) { // is new begin
            m.writer().writeShort(0);
        } else {
            m.writer().writeShort(1);
            m.writer().writeByte(0);
        }
        conn.addmsg(m);
        m.cleanup();
        //
        if (conn.p.rms_save[0].length > 0) {
            m = new Message(55);
            m.writer().writeByte(0);
            m.writer().writeShort(conn.p.rms_save[0].length);
            m.writer().write(conn.p.rms_save[0]);
            conn.addmsg(m);
            m.cleanup();
        }
        if (conn.p.rms_save[1].length > 0) {
            m = new Message(55);
            m.writer().writeByte(3);
            m.writer().writeShort(conn.p.rms_save[1].length);
            m.writer().write(conn.p.rms_save[1]);
            conn.addmsg(m);
            m.cleanup();
        }
    }

    public static void send_notice_nobox_yellow(Session conn, String s) throws IOException {
        Message m = new Message(53);
        m.writer().writeUTF(s);
        m.writer().writeByte(1);
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_combo(Session conn) throws IOException {
        Message m = new Message(-108);
        m.writer().writeByte(3);
        m.writer().writeInt(0);
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_point_pk(Player p) throws IOException {
        Message m = new Message(59);
        m.writer().writeInt(p.suckhoe);
        m.writer().writeInt(p.pointarena);
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void send_health(Player p) throws IOException {
        Message m = new Message(59);
        m.writer().writeInt(p.suckhoe);
        m.writer().writeInt(p.pointarena);
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void send_wear(Player p) throws IOException {
        Message m = new Message(15);
        m.writer().writeShort(p.index);
        m.writer().writeByte(p.item.wear.length);
        for (int i = 0; i < p.item.wear.length; i++) {
            Item3 temp = p.item.wear[i];
            if (temp != null) {
                m.writer().writeByte(i);
                m.writer().writeUTF(temp.name);
                m.writer().writeByte(temp.clazz);
                m.writer().writeByte(temp.type);
                m.writer().writeShort(temp.icon);
                if (i == 10 && p.item.wear[14] != null && (p.item.wear[14].id >= 4638 && p.item.wear[14].id <= 4648)) {
                    m.writer().writeByte(p.item.wear[14].part);
                } else {
                    m.writer().writeByte(temp.part);
                }
                m.writer().writeByte(temp.tier); // plus item (tier)
                m.writer().writeShort(temp.level);
                m.writer().writeByte(temp.color);
                m.writer().writeByte(temp.op.size());
                for (int j = 0; j < temp.op.size(); j++) {
                    m.writer().writeByte(temp.op.get(j).id);
                    m.writer().writeInt(temp.op.get(j).getParam(temp.tier));
                }
                m.writer().writeByte(1); // islock
            } else {
                m.writer().writeByte(-1);
            }
        }
        if (p.pet_follow != -1) {
            for (Pet temp : p.mypet) {
                if (temp.is_follow) {
                    m.writer().writeByte(5);
                    m.writer().writeUTF(temp.name);
                    m.writer().writeByte(4);
                    m.writer().writeShort(temp.level);
                    m.writer().writeShort(temp.getlevelpercent());
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
                    for (int i12 = 0; i12 < temp.op.size(); i12++) {
                        m.writer().writeByte(temp.op.get(i12).id);
                        m.writer().writeInt(temp.op.get(i12).param);
                        m.writer().writeInt(temp.op.get(i12).maxdam);
                    }
                    if (temp.expiry_date <= 0)
                        m.writer().writeByte(0);
                    else { //hạn sử dụng
                        m.writer().writeByte(1);
                        m.writer().writeInt(43200);
                        m.writer().writeUTF("" + temp.expiry_date);
                    }
                    break;
                }
            }
        } else {
            m.writer().writeByte(-1); // pet
        }
        m.writer().writeByte(p.fashion.length);
        for (int i = 0; i < p.fashion.length; i++) {
            if (p.conn.version >= 280) {
                m.writer().writeShort(p.fashion[i]);
            } else {
                m.writer().writeByte(p.fashion[i]);
            }
        }
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void save_rms(Session conn, Message m) throws IOException {
        m.reader().readByte();
        byte id = m.reader().readByte();
        byte[] num = null;
        try {
            num = new byte[m.reader().readShort()];
            for (int i = 0; i < num.length; i++) {
                num[i] = m.reader().readByte();
            }
        } catch (IOException e) {
        }
        if (num != null && num.length > 0) {
            if (id == 0) {
                conn.p.rms_save[0] = new byte[num.length];
                conn.p.rms_save[0] = num;
            } else if (id == 3 && num.length == 11) {
                conn.p.rms_save[1] = new byte[num.length];
                conn.p.rms_save[1] = num;
                conn.p.load_in4_autoplayer(num);
            }
        }
    }

    public static void send_icon(Session conn, Message m) throws IOException {
        short id0 = 0;
        short id = m.reader().readShort();
        id0 = id;
        try {
            Message m2 = new Message(-51);
            m2.writer().writeShort(id0);
//            if(Manager.gI().event == 2 && id0==1150)
//                m2.writer().write(Util.loadfile("data/icon/x" + conn.zoomlv + "/" + 1151 + ".png"));
//            else 
            m2.writer().write(Util.loadfile("data/icon/x" + conn.zoomlv + "/" + id0 + ".png"));
            conn.addmsg(m2);
            m2.cleanup();
        } catch (IOException e) {
            if (id0 == 361) return;
            System.err.println("Icon " + id0 + " not found!");
        }
    }


    public static void SendEffMob(Session conn, Mob_in_map mob, int type) throws IOException {
//        System.out.println("core.Service.SendEffMob()"+Manager.gI().msg_eff_70.length);
        byte[] b = null;
        if (type == 70)
            b = Manager.gI().msg_eff_70;
        else if (type == 71)
            b = Manager.gI().msg_eff_71;
        else return;

        Message m = new Message(-49);
//        m.writer().writeByte(1);
//        
//        
//        m.writer().writeShort(b.length);
//        m.writer().write(b);
//        
//        m.writer().writeByte(50);
//        m.writer().writeByte(50);
//        m.writer().writeByte(type);
//        
//        m.writer().writeShort(mob.x);
//        m.writer().writeShort(mob.y);
//        m.writer().writeByte(3);
//        m.writer().writeByte(2);
//        m.writer().writeShort(mob.index);
//        m.writer().writeShort(8000);
//        m.writer().writeByte(1);

//        m.writer().writeByte(4);
//        
//        
//        m.writer().writeShort(b.length);
//        m.writer().write(b);
//        m.writer().writeShort(type);
//        m.writer().writeByte(1);
//        m.writer().writeShort(mob.index);
//        m.writer().writeByte(1);

//        m.writer().writeByte(0);
//        m.writer().writeShort(b.length);
//        m.writer().write(b);
//        m.writer().writeByte(0);
//        m.writer().writeByte(0);
//        m.writer().writeByte(type);
//        m.writer().writeShort(mob.index);
//        m.writer().writeByte(1);
//        m.writer().writeByte(0);
//        m.writer().writeShort(10000);
//        m.writer().writeByte(0);


        m.writer().writeByte(0);
        m.writer().writeShort(b.length);
        m.writer().write(b);

        m.writer().writeByte(0);
        m.writer().writeByte(1);
        m.writer().writeByte(type);

        m.writer().writeShort(mob.index);
        m.writer().writeByte(1);//tem mob
        m.writer().writeByte(0);
        m.writer().writeShort(8000);
        m.writer().writeByte(0);

        conn.addmsg(m);
        m.cleanup();
    }

    public static void mob_in4(Player p, int n) {
        try {
            Mob_in_map temp = MapService.get_mob_by_index(p.map, n);
            if (temp != null && !temp.isDie && temp.hp > 0) {
                Message m = new Message(7);
                m.writer().writeShort(n);
                m.writer().writeByte((byte) temp.level);
                m.writer().writeShort(temp.x);
                m.writer().writeShort(temp.y);
                m.writer().writeInt(temp.hp);
                m.writer().writeInt(temp.get_HpMax());
                //m.writer().writeByte(20); // id skill monster (Spec: 32, ...)
                if (temp.template.mob_id >= 89 && temp.template.mob_id <= 92)
                    m.writer().writeByte(temp.template.mob_id - 43); // 46 set
                else if (temp.template.mob_id == 151)
                    m.writer().writeByte(65);
                else if (temp.template.mob_id == 152)
                    m.writer().writeByte(66);
                else if (temp.template.mob_id == 154)
                    m.writer().writeByte(64);
                else
                    m.writer().writeByte(20);
//                m.writer().writeByte(idxDame);
                m.writer().writeInt(temp.time_refresh);
                m.writer().writeShort(-1); // clan monster
                m.writer().writeByte(0);
                m.writer().writeByte(2); // speed
                m.writer().writeByte(0);
                m.writer().writeUTF("");
                m.writer().writeLong(-11111);
                m.writer().writeByte(temp.color_name); // color name 1: blue, 2: yellow
                p.conn.addmsg(m);
                m.cleanup();
                if (temp.template.mob_id == 151 || temp.template.mob_id == 152)
                    SendEffMob(p.conn, temp, temp.template.mob_id - 81);
            } else if (p.map.zone_id == p.map.maxzone) {
                Pet_di_buon temp2 = Pet_di_buon_manager.check(n);
                if (temp2 != null) {
                    Message mm = new Message(7);
                    mm.writer().writeShort(n);
                    mm.writer().writeByte(temp2.level);
                    mm.writer().writeShort(temp2.x);
                    mm.writer().writeShort(temp2.y);
                    mm.writer().writeInt(temp2.hp);
                    mm.writer().writeInt(temp2.get_HpMax());
                    mm.writer().writeByte(0);
                    mm.writer().writeInt(-1);
                    mm.writer().writeShort(-1);
                    mm.writer().writeByte(1);
                    mm.writer().writeByte(temp2.speed);
                    mm.writer().writeByte(0);
                    mm.writer().writeUTF(temp2.name);
                    mm.writer().writeLong(-11111);
                    mm.writer().writeByte(4);
                    p.conn.addmsg(mm);
                    mm.cleanup();
                }
            } else if (Map.is_map_chiem_mo(p.map, true)) {
                Mob_MoTaiNguyen temp2 = Manager.gI().chiem_mo.get_mob_in_map(p.map);
                if (temp2 != null && temp2.index == n) {
                    Message mm = new Message(7);
                    mm.writer().writeShort(n);
                    mm.writer().writeByte((byte) temp2.level);
                    mm.writer().writeShort(temp2.x);
                    mm.writer().writeShort(temp2.y);
                    mm.writer().writeInt(temp2.hp);
                    mm.writer().writeInt(temp2.get_HpMax());
                    mm.writer().writeByte(0);
                    mm.writer().writeInt(4);
                    if (temp2.clan != null) {
                        mm.writer().writeShort(temp2.clan.icon);
                        mm.writer().writeInt(Clan.get_id_clan(temp2.clan));
                        mm.writer().writeUTF(temp2.clan.name_clan_shorted);
                        mm.writer().writeByte(122);
                    } else {
                        mm.writer().writeShort(-1);
                    }
                    mm.writer().writeUTF(temp2.name_monster);
                    mm.writer().writeByte(0);
                    mm.writer().writeByte(2);
                    mm.writer().writeByte(0);
                    mm.writer().writeUTF("");
                    mm.writer().writeLong(-11111);
                    mm.writer().writeByte(4);
                    p.conn.addmsg(mm);
                    mm.cleanup();
                    //
                    Eff_player_in_map.add(p, temp2.index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send_notice_nobox_white(Session conn, String s) throws IOException {
        Message m = new Message(53);
        m.writer().writeUTF(s);
        m.writer().writeByte(0);
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_box_input_yesno(Session conn, int type, String s) throws IOException {
        Message m2 = new Message(-32);
        m2.writer().writeShort(conn.p.index);
        m2.writer().writeByte(type);
        m2.writer().writeUTF(s);
        conn.addmsg(m2);
        m2.cleanup();
    }

    public static void usepotion(Player p, int type, long param) throws IOException {
        if (p.isDie) {
            return;
        }
        Message m = new Message(32);
        switch (type) {
            case 0: { // use hp potion

                long par_can_add = 2_000_000_000 - p.hp;
                if (param > par_can_add) {
                    param = par_can_add;
                }

                p.hp += param;
                int maxhp = p.body.get_HpMax();
                if (p.hp > maxhp) {
                    p.hp = maxhp;
                }
                m.writer().writeByte(0);
                m.writer().writeShort(p.index);
                m.writer().writeShort(-1); // id potion in bag
                m.writer().writeByte(type);
                m.writer().writeInt(maxhp); // max hp
                m.writer().writeInt(p.hp); // hp
                m.writer().writeInt((int) param); // param use
                break;
            }
            case 1: { // use mp potion
                long par_can_add = 2_000_000_000 - p.mp;
                if (param > par_can_add) {
                    param = par_can_add;
                }
                p.mp += param;
                int maxmp = p.body.get_MpMax();
                if (p.mp > maxmp) {
                    p.mp = maxmp;
                }
                m.writer().writeByte(0);
                m.writer().writeShort(p.index);
                m.writer().writeShort(-1); // id potion in bag
                m.writer().writeByte(type);
                m.writer().writeInt(maxmp); // max hp
                m.writer().writeInt(p.mp); // hp
                m.writer().writeInt((int) param); // param use
                break;
            }
        }
        MapService.send_msg_player_inside(p.map, p, m, true);
        m.cleanup();
    }

    public static void chat_KTG(Session conn, Message m2) throws IOException {
        if(!Manager.isKTG){
            Service.send_notice_nobox_white(conn,"Chức năng đang tạm khoá");
            return;
        }
        if (conn.p.get_vang() < 50_000) {
            send_notice_box(conn, "Không đủ ngọc để thực hiện");
            return;
        }
        if (conn.p.timeBlockCTG > Helps._Time.timeDay) {
            send_notice_box(conn, "Bạn đã bị khóa CTG");
            return;
        }
        // if (!conn.user.equals("ad1") && conn.p.time_chat_ktg > System.currentTimeMillis()) {
        // send_box_notice(conn, "Sau " + (conn.p.time_chat_ktg - System.currentTimeMillis()) / 1000
        // + "s nữa mới có thể tiếp tục chat KTG");
        // return;
        // }
        // conn.p.time_chat_ktg = System.currentTimeMillis() + 1000L * 60 * 5;
        conn.p.update_vang(-50_000);
        conn.p.item.char_inventory(5);
        String text = m2.reader().readUTF();

        if (text != null) {
            String text2 = text.toLowerCase();
            if (text2 != null && (text2.indexOf("reset") >= 0 || text2.indexOf("open") >= 0 || text2.indexOf("open") >= 0)) {
                send_notice_box(conn, "Thằng ranh con này bố lại khóa luôn mõm chó m lại bh.");
                return;
            } else {
                Manager.gI().chatKTGprocess("@" + conn.p.name + " : " + text);
            }
        }

    }

    public static void send_view_other_player_in4(Session conn, Message m) throws IOException {
        String name = m.reader().readUTF();
        byte type = m.reader().readByte();
        if (type == 0) { // xem thong tin other
            Player p0 = null;
            for (Map[] map : Map.entrys) {
                for (Map map0 : map) {
                    for (int i = 0; i < map0.players.size(); i++) {
                        Player p01 = map0.players.get(i);
                        if (p01.name.equals(name)) {
                            p0 = p01;
                            break;
                        }
                    }
                }
            }
            if (p0.isSquire) {
                return;
            }
            if (p0 != null) {
                send_notice_nobox_white(p0.conn, conn.p.name + " đang dòm ngó đồ đạc của bạn");
                Message m2 = new Message(49);
                m2.writer().writeShort(p0.index);
                if (Manager.gI().event == 12) {
                    m2.writer().writeUTF(name + "-VIP: " + p0.checkvip());
                }else {
                    m2.writer().writeUTF(name);
                }
                m2.writer().writeByte(p0.clazz);
                m2.writer().writeByte(p0.head);
                m2.writer().writeByte(p0.eye);
                m2.writer().writeByte(p0.hair);
                m2.writer().writeShort(p0.level);
                m2.writer().writeInt(p0.hp);
                m2.writer().writeInt(p0.body.get_HpMax());
                m2.writer().writeByte(p0.typepk);
                m2.writer().writeShort(p0.pointpk);
                m2.writer().writeByte(p0.item.wear.length);
                for (int i = 0; i < p0.item.wear.length; i++) {
                    Item3 temp = p0.item.wear[i];
                    if (temp != null) {
                        m2.writer().writeByte(i);
                        m2.writer().writeUTF(temp.name);
                        m2.writer().writeByte(temp.clazz);
                        m2.writer().writeByte(temp.type);
                        m2.writer().writeShort(temp.icon);
                        m2.writer().writeByte(temp.part); // show part char
                        m2.writer().writeByte(temp.tier); // plus item = tier
                        m2.writer().writeShort(temp.level);
                        m2.writer().writeByte(temp.color);
                        m2.writer().writeByte(temp.op.size());
                        for (int j = 0; j < temp.op.size(); j++) {
                            m2.writer().writeByte(temp.op.get(j).id);
                            m2.writer().writeInt(temp.op.get(j).getParam(temp.tier));
                        }
                        m2.writer().writeByte(0); // can sell
                    } else {
                        m2.writer().writeByte(-1);
                    }
                }
                if (p0.myclan != null) {
                    m2.writer().writeShort(p0.myclan.icon);
                    m2.writer().writeUTF(p0.myclan.name_clan_shorted);
                    m2.writer().writeByte(p0.myclan.get_mem_type(p0.name));
                    m2.writer().writeUTF(p0.myclan.name_clan);
                } else {
                    m2.writer().writeShort(-1); // clan
                }
                if (p0.pet_follow != -1) {
                    for (Pet temp : p0.mypet) {
                        if (temp.is_follow) {
                            m2.writer().writeByte(5);
                            m2.writer().writeUTF(temp.name);
                            m2.writer().writeByte(4);
                            m2.writer().writeShort(temp.level);
                            m2.writer().writeShort(temp.getlevelpercent());
                            m2.writer().writeByte(temp.type);
                            m2.writer().writeByte(temp.icon);
                            m2.writer().writeByte(temp.nframe);
                            m2.writer().writeByte(temp.color);
                            m2.writer().writeInt(temp.get_age());
                            m2.writer().writeShort(temp.grown);
                            m2.writer().writeShort(temp.maxgrown);
                            m2.writer().writeShort(temp.point1);
                            m2.writer().writeShort(temp.point2);
                            m2.writer().writeShort(temp.point3);
                            m2.writer().writeShort(temp.point4);
                            m2.writer().writeShort(temp.maxpoint);
                            m2.writer().writeByte(temp.op.size());
                            for (int i12 = 0; i12 < temp.op.size(); i12++) {
                                m2.writer().writeByte(temp.op.get(i12).id);
                                m2.writer().writeInt(temp.op.get(i12).param);
                                m2.writer().writeInt(temp.op.get(i12).maxdam);
                            }
                            // m.writer().writeByte(0);
                            break;
                        }
                    }
                } else {
                    m2.writer().writeByte(-1); // pet
                }
                m2.writer().writeByte(0);
                conn.addmsg(m2);
                m2.cleanup();
            } else {
                send_notice_nobox_white(conn, ("người chơi " + name + " đã offline"));
            }
        }
    }

    public static void send_param_item_wear(Session conn, Message m2) throws IOException {
        @SuppressWarnings("unused")
        byte invenid = m2.reader().readByte();
        byte id = m2.reader().readByte();
        if (id >= conn.p.item.bag3.length) {
            return;
        }
        Item3 temp = conn.p.item.bag3[id];
        if (temp != null) {
            Message m = new Message(21);
            m.writer().writeByte(temp.op.size());
            for (int i = 0; i < temp.op.size(); i++) {
                m.writer().writeByte(temp.op.get(i).id);
                m.writer().writeInt(temp.op.get(i).getParam(temp.tier));
            }
            conn.addmsg(m);
            m.cleanup();
        }
    }

    public static void send_box_UI(Session conn, int type) throws IOException {
        Message m = new Message(23);
        switch (type) {

            case 33: {
                if (conn.p.isCreateItemStar) {
                    m.writer().writeUTF("Nâng cấp đồ tinh tú");
                } else if (conn.p.isCreateArmor) {
                    m.writer().writeUTF("Code by Vĩnh Lỏh");
                } else if (conn.p.isdothan) {
                    m.writer().writeUTF("Tiến hóa đồ tinh tú[VIP PRO]");
                } else if (conn.p.ismdthan) {
                    m.writer().writeUTF("Tiến hóa mề đay");
                } else if (conn.p.istb2) {
                    m.writer().writeUTF("Cường hóa trang bị 2");
                } else {
                    m.writer().writeUTF("Nâng cấp mề đay");
                }
                m.writer().writeByte(20);
                m.writer().writeShort(0);
                break;
            }
            case 0: { // cua hang poition
                m.writer().writeUTF("Cửa hàng Poition");
                m.writer().writeByte(0);
                m.writer().writeShort(Manager.gI().itempoitionsell.length);
                for (int i = 0; i < Manager.gI().itempoitionsell.length; i++) {
                    m.writer().writeShort(Manager.gI().itempoitionsell[i]);
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
            case 16: {//  case 22:{
                m.writer().writeUTF("Cửa hàng trang bị");
                m.writer().writeByte(1);
                m.writer().writeShort(Manager.gI().itemsellTB.get(type - 1).length);
                for (int i = 0; i < Manager.gI().itemsellTB.get(type - 1).length; i++) {
                    ItemSell3 temp = Manager.gI().itemsellTB.get(type - 1)[i];
                    m.writer().writeShort(temp.id);
                    m.writer().writeUTF(ItemTemplate3.item.get(temp.id).getName());
                    m.writer().writeByte(temp.clazz);
                    m.writer().writeByte(temp.type);
                    m.writer().writeShort(ItemTemplate3.item.get(temp.id).getIcon());
                    m.writer().writeLong(temp.price);
                    m.writer().writeShort(temp.level);
                    m.writer().writeByte(temp.color);
                    m.writer().writeByte(temp.option.size());
                    for (int j = 0; j < temp.option.size(); j++) {
                        m.writer().writeByte(temp.option.get(j).id);
                        m.writer().writeInt(temp.option.get(j).getParam(0));
                    }
                    m.writer().writeByte(temp.pricetype);
                }
                break;
            }
            case 18: {
                m.writer().writeUTF("Cường hóa trang bị");
                m.writer().writeByte(5);
                m.writer().writeShort(0);
                break;
            }
            case 17: {
//                m.cleanup();
//                send_notice_box(conn, "thử nh"); return;
                m.writer().writeUTF("Đá nguyên liệu");
                m.writer().writeByte(4);
                m.writer().writeShort(Manager.gI().item7sell.length);
                for (int i = 0; i < Manager.gI().item7sell.length; i++) {
                    m.writer().writeShort(Manager.gI().item7sell[i]);
                }
                break;
            }
            case 19: {
                m.writer().writeUTF("Chuyển hóa trang bị");
                m.writer().writeByte(9);
                m.writer().writeShort(0);
                break;
            }
            case 20: {
                m.writer().writeUTF("Icon Clan");
                m.writer().writeByte(6);
                m.writer().writeShort(31); // 31 in team server
                for (int i = 0; i < 31; i++) {
                    m.writer().writeShort(i);
                }
                break;
            }
            case 21: {
                m.writer().writeUTF("Thú cưng");
                m.writer().writeByte(11);
                m.writer().writeShort(0);
                break;
            }
            case 22: {
                m.writer().writeUTF("Thức ăn thú cưng");
                m.writer().writeByte(0);
                m.writer().writeShort(4);
                for (int i = 48; i < 52; i++) {
                    m.writer().writeShort(i);
                }
                break;
            }
            case 23: {
                m.writer().writeUTF("Shop trứng");
                m.writer().writeByte(1);
                short[] id_egg = Manager.gI().event == 2 ? new short[]{2943, 2944} : new short[]{2943, 2944}; // thêm trứng  // , 4762, 4617, 4626, 4788
                long[] price_egg = Manager.gI().event == 2 ? new long[]{150, 150} : new long[]{150, 150}; // giá tiền
                m.writer().writeShort(id_egg.length);
                for (int i = 0; i < id_egg.length; i++) {
                    ItemTemplate3 temp = ItemTemplate3.item.get(id_egg[i]);
                    m.writer().writeShort(temp.getId());
                    m.writer().writeUTF(temp.getName());
                    m.writer().writeByte(temp.getClazz());
                    m.writer().writeByte(temp.getType());
                    m.writer().writeShort(temp.getIcon());
                    m.writer().writeLong(price_egg[i]); // 150 ngoc
                    m.writer().writeShort(temp.getLevel());
                    m.writer().writeByte(temp.getColor());
                    m.writer().writeByte(0); // op size
                    m.writer().writeByte(1); // pricetype
                }
                break;
            }
            case 24: {
                m.writer().writeUTF("Hợp nguyên liệu mề đay");
                m.writer().writeByte(18);
                m.writer().writeShort(0);
                break;
            }
            case 25:
            case 26:
            case 27:
            case 28: {
                m.writer().writeUTF("Tạo mề đay");
                m.writer().writeByte(19);
                m.writer().writeShort(0);
                m.writer().writeByte(5);
                //
                m.writer().writeShort(conn.p.medal_create_material[5 * (type - 25)]);
                if (conn.version >= 270) {
                    m.writer().writeShort(1);
                } else {
                    m.writer().writeByte(1);
                }
                m.writer().writeShort(conn.p.medal_create_material[1 + 5 * (type - 25)]);
                if (conn.version >= 270) {
                    m.writer().writeShort(1);
                } else {
                    m.writer().writeByte(1);
                }
                m.writer().writeShort(conn.p.medal_create_material[2 + 5 * (type - 25)]);
                if (conn.version >= 270) {
                    m.writer().writeShort(1);
                } else {
                    m.writer().writeByte(1);
                }
                m.writer().writeShort(conn.p.medal_create_material[3 + 5 * (type - 25)]);
                if (conn.version >= 270) {
                    m.writer().writeShort(1);
                } else {
                    m.writer().writeByte(1);
                }
                m.writer().writeShort(conn.p.medal_create_material[4 + 5 * (type - 25)]);
                if (conn.version >= 270) {
                    m.writer().writeShort(1);
                } else {
                    m.writer().writeByte(1);
                }
                break;
            }
            case 29: {
                m.writer().writeUTF("Icon Clan");
                m.writer().writeByte(6);
                m.writer().writeShort(358); // 31 in team server
                for (int i = 0; i < 31; i++) {
                    m.writer().writeShort(i);
                }
                for (int i = 500; i < 827; i++) {
                    m.writer().writeShort(i);
                }
                break;
            }
            case 30: { // cua hang shop bang
                m.writer().writeUTF("Shop Bang");
                m.writer().writeByte(8);
                m.writer().writeShort(Clan.item_shop.length);
                for (int i = 0; i < Clan.item_shop.length; i++) {
                    m.writer().writeShort(Clan.item_shop[i]);
                }
                break;
            }
            case 31: {
                m.writer().writeUTF("Cửa hàng đá quý");
                m.writer().writeByte(1);
                short[] id_case_31 = new short[]{3590, 3591, 3592};
                m.writer().writeShort(id_case_31.length);
                for (int i = 0; i < id_case_31.length; i++) {
                    ItemTemplate3 temp = ItemTemplate3.item.get(id_case_31[i]);
                    m.writer().writeShort(temp.getId());
                    m.writer().writeUTF(temp.getName());
                    m.writer().writeByte(temp.getClazz());
                    m.writer().writeByte(temp.getType());
                    m.writer().writeShort(temp.getIcon());
                    m.writer().writeLong(100_000 + i * 50_000); // price
                    m.writer().writeShort(10); // level
                    m.writer().writeByte(temp.getColor());
                    m.writer().writeByte(0); // option
                    m.writer().writeByte(0); // type money
                }
                break;
            }
            case 32: {
                m.writer().writeUTF("Pet thương nhân");
                m.writer().writeByte(0);
                m.writer().writeShort(1);
                m.writer().writeShort(84);
                break;
            }
            case 38: {
                m.writer().writeUTF("Pet cướp");
                m.writer().writeByte(0);
                m.writer().writeShort(1);
                m.writer().writeShort(86);
                break;
            }
            case 34: {
                m.writer().writeUTF("Hợp ngọc");
                m.writer().writeByte(15);
                m.writer().writeShort(0);
                break;
            }
            case 35: {
                m.writer().writeUTF("Khảm ngọc");
                m.writer().writeByte(14);
                m.writer().writeShort(0);
                break;
            }
            case 36: {
                m.writer().writeUTF("Đục lỗ");
                m.writer().writeByte(16);
                m.writer().writeShort(0);
                break;
            }

            case 37: {
                m.writer().writeUTF("Shop Coin");
                m.writer().writeByte(1);
                m.writer().writeShort(Itemsellcoin.entry.size());
                for (int i = 0; i < Itemsellcoin.entry.size(); i++) {
                    Itemsellcoin temp = Itemsellcoin.entry.get(i);
                    m.writer().writeShort(temp.id);
                    m.writer().writeUTF(ItemTemplate3.item.get(temp.id).getName());
                    m.writer().writeByte(ItemTemplate3.item.get(temp.id).getClazz());
                    m.writer().writeByte(ItemTemplate3.item.get(temp.id).getType());
                    m.writer().writeShort(ItemTemplate3.item.get(temp.id).getIcon());
                    m.writer().writeLong(temp.price);
                    m.writer().writeShort(10);
                    m.writer().writeByte(0);
                    m.writer().writeByte(temp.op.size());
                    for (int j = 0; j < temp.op.size(); j++) {
                        m.writer().writeByte(temp.op.get(j).id);
                        m.writer().writeInt(temp.op.get(j).getParam(0));
                    }
                    m.writer().writeByte(1);
                }
                break;
            }
            case 99: {
                m.writer().writeUTF("Code by Vĩnh Lỏh");
                m.writer().writeByte(4);
                m.writer().writeShort(Manager.gI().item7sell1.length);
                for (int i = 0; i < Manager.gI().item7sell1.length; i++) {
                    m.writer().writeShort(Manager.gI().item7sell1[i]);
                }
                break;
            }
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47: {
                m.writer().writeUTF("Tạo trang bị tinh tú");
                m.writer().writeByte(19);
                m.writer().writeShort(0);
                m.writer().writeByte(5);
                for (int i = conn.p.TypeItemStarCreate * 5; i < conn.p.TypeItemStarCreate * 5 + 5; i++) {
                    m.writer().writeShort(conn.p.MaterialItemStar[i]);
                    if (conn.version >= 270) {
                        m.writer().writeShort(1);
                    } else {
                        m.writer().writeByte(1);
                    }
                }
                break;
            }
            case 48: {
                m.writer().writeUTF("Shop Tinh Tú");
                m.writer().writeByte(1);
                m.writer().writeShort(Itemshoptt.entry.size());
                for (int i = 0; i < Itemshoptt.entry.size(); i++) {
                    Itemshoptt temp = Itemshoptt.entry.get(i);
                    m.writer().writeShort(temp.id);
                    m.writer().writeUTF(ItemTemplate3.item.get(temp.id).getName());
                    m.writer().writeByte(ItemTemplate3.item.get(temp.id).getClazz());
                    m.writer().writeByte(ItemTemplate3.item.get(temp.id).getType());
                    m.writer().writeShort(ItemTemplate3.item.get(temp.id).getIcon());
                    m.writer().writeLong(temp.price);
                    m.writer().writeShort(10);
                    m.writer().writeByte(0);
                    m.writer().writeByte(temp.op.size());
                    for (int j = 0; j < temp.op.size(); j++) {
                        m.writer().writeByte(temp.op.get(j).id);
                        m.writer().writeInt(temp.op.get(j).getParam(0));
                    }
                    m.writer().writeByte(1);
                }
                break;
            }
            case 49: {
                if (!conn.p.isCreateArmor) return;
                m.writer().writeUTF("Code by Vĩnh Lỏh");
                m.writer().writeByte(19);
                m.writer().writeShort(0);
                m.writer().writeByte(1);
                m.writer().writeShort(481 + conn.p.id_armor_create);
                if (conn.version >= 270) {
                    m.writer().writeShort(500);
                } else {
                    m.writer().writeByte(500);
                }
                break;
            }
            default: {
                send_notice_box(conn, "Lỗi, hãy thử lại sau");
                break;
            }
        }
        conn.addmsg(m);
        m.cleanup();
    }

    public static void revenge(Session conn, byte index) throws IOException {
        if (conn.p.get_ngoc() < 2) {
            send_notice_box(conn, "Không đủ ngọc");
            return;
        }
        String name = conn.p.list_enemies.get(conn.p.list_enemies.size() - index - 1);
        Player p0 = null;
        for (Map[] map : Map.entrys) {
            for (Map map0 : map) {
                for (int i = 0; i < map0.players.size(); i++) {
                    Player p2 = map0.players.get(i);
                    if (p2.name.equals(name)) {
                        p0 = p2;
                        break;
                    }
                }
                if (p0 != null) {
                    break;
                }
            }
            if (p0 != null) {
                break;
            }
        }
        if (p0 == null) {
        } else {
            EffTemplate ef = p0.get_EffDefault(-125);
            if (p0.map.zone_id == 1 && !Map.is_map_not_zone2(p0.map_id)) {
                send_notice_box(conn, "Kẻ thù đang trong khu vực không thể đến");
                return;
            }
            if (p0.map.isMapLangPhuSuong() || p0.map.isMapChienTruong() || p0.map.isMapChiemThanh() || p0.map.isMapLoiDai()) {
                send_notice_box(conn, "Kẻ thù đang trong khu vực không thể đến");
                return;
            }
            if (ef == null && p0.map.map_id != 0 && !p0.map.ismaplang) {
                conn.p.update_ngoc(-2);
                conn.p.is_changemap = false;
                Map mbuffer2 = p0.map;
                if (mbuffer2 != null) {
                    if (conn.p.isDie) {
                        return;
                    }
                    MapService.leave(conn.p.map, conn.p);
                    conn.p.map = mbuffer2;
                    conn.p.x = p0.x;
                    conn.p.y = p0.y;
                    MapService.enter(conn.p.map, conn.p);
                    Message m = new Message(4);
                    m.writer().writeByte(0);
                    m.writer().writeShort(0);
                    m.writer().writeShort(p0.index);
                    m.writer().writeShort(p0.x);
                    m.writer().writeShort(p0.y);
                    m.writer().writeByte(-1);
                    conn.addmsg(m);
                    m.cleanup();
                    //
                    m = new Message(4);
                    m.writer().writeByte(0);
                    m.writer().writeShort(0);
                    m.writer().writeShort(conn.p.index);
                    m.writer().writeShort(conn.p.x);
                    m.writer().writeShort(conn.p.y);
                    m.writer().writeByte(-1);
                    p0.conn.addmsg(m);
                    m.cleanup();
                } else {
                    send_notice_box(conn, "Có lỗi xảy ra khi chuyển map");
                }
            } else {
                send_notice_box(conn, "Kẻ thù đang trong khu vực không thể đến");
            }
        }
    }

    public static void send_box_input_text(Session conn, int type, String text, String[] in4) throws IOException {
        Message m = new Message(-31);
        m.writer().writeShort(type);
        m.writer().writeByte(0);
        m.writer().writeUTF(text);
        m.writer().writeByte(in4.length);
        for (int i = 0; i < in4.length; i++) {
            m.writer().writeUTF(in4[i]);
            m.writer().writeByte(0);
        }
        for (int i = 0; i < in4.length; i++) {
            m.writer().writeUTF("");
            m.writer().writeByte(0);
        }
        conn.addmsg(m);
        m.cleanup();
    }

    public static void send_in4_item(Session conn, Message m) throws IOException {
        short id = m.reader().readShort();
        // for (int i = 0; i < conn.p.item.bag3.length; i++) {
        // Item3 temp = conn.p.item.bag3[i];
        // if (temp != null && temp.id == 9) {
        Message m2 = new Message(28);
        m2.writer().writeShort(id); // index?
        m2.writer().writeUTF("Vật phẩm hiển thị lỗi, hãy thoát game vào lại để reset");
        m2.writer().writeByte(8); // type item
        m2.writer().writeByte(0); // id part
        m2.writer().writeByte(0); // class item
        m2.writer().writeShort(0); // icon id
        m2.writer().writeByte(1);// size
        for (int i2 = 0; i2 < 1; i2++) {
            m2.writer().writeByte(69);
            m2.writer().writeInt(99);
        }
        conn.addmsg(m2);
        m2.cleanup();
        // }
        // }
    }

    public static void send_item7_template(Player p, Message m2) throws IOException {
        short id = m2.reader().readShort();
        ItemTemplate7 it7 = ItemTemplate7.item.get(id);
        Message m = new Message(-106);
        m.writer().writeShort(it7.getId());
        m.writer().writeShort(it7.getIcon());
        m.writer().writeLong(it7.getPrice());
        m.writer().writeUTF(it7.getName());
        m.writer().writeUTF(it7.getContent());
        m.writer().writeByte(it7.getType());
        m.writer().writeByte(it7.getPricetype());
        m.writer().writeByte(it7.getSell());
        m.writer().writeShort(it7.getValue());
        m.writer().writeByte(it7.getTrade());
        m.writer().writeByte(it7.getColor());
        //m.writer().writeByte(it7.getLamcoin());
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void chat_clan(Clan clan, String text) throws IOException {
        Message m = new Message(34);
        m.writer().writeUTF("Bang Hội");
        m.writer().writeUTF("@Hệ thống : " + text);
        for (int i = 0; i < clan.mems.size(); i++) {
            String name2 = clan.mems.get(i).name;
            for (Map[] map : Map.entrys) {
                for (Map map2 : map) {
                    synchronized (map2) {
                        for (Player p0 : map2.players) {
                            if (p0.name.equals(name2)) {
                                p0.conn.addmsg(m);
                            }
                        }
                    }
                }
            }
        }
        m.cleanup();
    }

    public static void chat_tab(Session conn, Message m2) throws IOException {
        String name = m2.reader().readUTF();
        String chat = m2.reader().readUTF();
        if (name.equals("Đội nhóm") && conn.p.party != null && conn.p.party.get_mems().contains(conn.p)) {
            Message m = new Message(34);
            m.writer().writeUTF("Đội nhóm");
            m.writer().writeUTF("@" + conn.p.name + " : " + chat);
            for (int i = 0; i < conn.p.party.get_mems().size(); i++) {
                if (conn.p.party.get_mems().get(i).index != conn.p.index) {
                    conn.p.party.get_mems().get(i).conn.addmsg(m);
                }
            }
            m.cleanup();
        } else if (name.equals("Bang Hội") && conn.p.myclan != null) {
            Message m = new Message(34);
            m.writer().writeUTF("Bang Hội");
            m.writer().writeUTF("@" + conn.p.name + " : " + chat);
            for (int i = 0; i < conn.p.myclan.mems.size(); i++) {
                String name2 = conn.p.myclan.mems.get(i).name;
                if (!name2.equals(conn.p.name)) {
                    for (Map[] map : Map.entrys) {
                        for (Map map2 : map) {
                            synchronized (map2) {
                                for (Player p0 : map2.players) {
                                    if (p0.name.equals(name2)) {
                                        p0.conn.addmsg(m);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            m.cleanup();
        } else {
            Player p0 = null;
            for (Map[] map : Map.entrys) {
                for (Map map0 : map) {
                    for (int i = 0; i < map0.players.size(); i++) {
                        if (map0.players.get(i).name.equals(name)) {
                            p0 = map0.players.get(i);
                        }
                    }
                }
            }
            if (p0 == null) {
                send_notice_box(conn, "Người chơi đã offline!");
            } else {
                if (p0.name.equals(conn.p.name)) {
                    send_notice_box(conn, "bị khùng hay sao mà tự nói với chính mình vậy?????");
                    return;
                }
                Message m = new Message(34);
                m.writer().writeUTF(conn.p.name);
                m.writer().writeUTF(chat);
                p0.conn.addmsg(m);
                m.cleanup();
            }
        }
    }

    public static void pet_eat(Session conn, Message m2) throws IOException {
        short id_pet = m2.reader().readShort();
        short id_it = m2.reader().readShort();
        byte cat = m2.reader().readByte();
        byte type = m2.reader().readByte();
        // System.out.println(id_pet);
        // System.out.println(id_it);
        // System.out.println(cat);
        // System.out.println(type);
        if (cat != 3 && (cat == 4 && id_it != 48 && id_it != 49 && id_it != 50 && id_it != 51)) {
            send_notice_box(conn, "Không thể cho ăn vật phẩm này!");
            return;
        }
        int index_pet = id_pet;
        if (type == 1) {
            for (int i = 0; i < conn.p.mypet.size(); i++) {
                if (conn.p.mypet.get(i).is_follow) {
                    index_pet = i;
                    break;
                }
            }
        }
        Pet _ppp = conn.p.mypet.get(index_pet);
        if (_ppp.grown < _ppp.maxgrown)
            _ppp.grown += 5;
        if ((_ppp.level == 9 || _ppp.level == 19 || _ppp.level == 29) && Math.abs(Level.entrys.get(_ppp.level - 1).exp - _ppp.exp) < 10) {
            send_notice_box(conn, "Bạn cần sử dụng thuốc tăng trưởng");
            return;
        }
        if (cat == 4) {
            if (index_pet > -1) {
                if (id_it == 51 && conn.p.mypet.get(index_pet).point1 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point1 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm sức mạnh");
                } else if (id_it == 49 && conn.p.mypet.get(index_pet).point4 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point4 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm tinh thần");
                } else if (id_it == 50 && conn.p.mypet.get(index_pet).point3 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point3 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm thể lực");
                } else if (id_it == 48 && conn.p.mypet.get(index_pet).point2 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point2 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm khéo léo");
                } else {
                    send_notice_box(conn, "Không thể cho ăn");
                    return;
                }
                conn.p.mypet.get(index_pet).update_exp(3250);
                conn.p.item.remove(4, id_it, 1);
                conn.p.item.char_inventory(4);
            } else {
                send_notice_box(conn, "Có lỗi xảy ra!");
                return;
            }
        } else if (cat == 3) {
            if (conn.p.item.bag3[id_it] != null && index_pet > -1) {
                int type_ = conn.p.item.bag3[id_it].type;
                if ((type_ == 8 || type_ == 9) && _ppp.point1 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point1 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm sức mạnh");
                } else if ((type_ == 10 || type_ == 11) && _ppp.point4 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point4 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm tinh thần");
                } else if ((type_ == 0 || type_ == 1 || type_ == 2 || type_ == 3 || type_ == 6) && _ppp.point3 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point3 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm thể lực");
                } else if ((type_ == 4 || type_ == 5) && _ppp.point2 < _ppp.maxpoint) {
                    conn.p.mypet.get(index_pet).point2 += 10;
                    send_notice_box(conn, "+10 điểm vào nhóm khéo léo");
                } else {
                    send_notice_box(conn, "Không thể cho ăn");
                    return;
                }
//                His_DelItem hist = new His_DelItem(conn.p.name);
//                hist.Logger = "cho pet ăn";
//                hist.tem3 = conn.p.item.bag3[id_it];
//                hist.Flus();
                conn.p.mypet.get(index_pet).update_exp(3250);
                conn.p.item.bag3[id_it] = null;
                conn.p.item.char_inventory(3);
            } else {
                send_notice_box(conn, "Có lỗi xảy ra!");
                return;
            }
        }
        if (type == 1) {
            send_wear(conn.p);
            send_char_main_in4(conn.p);
        } else if (type == 0) {
            Message m = new Message(44);
            m = new Message(44);
            m.writer().writeByte(28);
            m.writer().writeByte(2);
            m.writer().writeByte(9);
            m.writer().writeByte(9);
            m.writer().writeShort(index_pet);
            conn.addmsg(m);
            m.cleanup();
            //
            m = new Message(44);
            m.writer().writeByte(28);
            m.writer().writeByte(1);
            m.writer().writeByte(9);
            m.writer().writeByte(9);
            m.writer().writeUTF(conn.p.mypet.get(index_pet).name);
            m.writer().writeByte(conn.p.mypet.get(index_pet).type);
            m.writer().writeShort(index_pet); // id
            m.writer().writeShort(conn.p.mypet.get(index_pet).level);
            m.writer().writeShort(conn.p.mypet.get(index_pet).getlevelpercent()); // exp
            m.writer().writeByte(conn.p.mypet.get(index_pet).type);
            m.writer().writeByte(conn.p.mypet.get(index_pet).icon);
            m.writer().writeByte(conn.p.mypet.get(index_pet).nframe);
            m.writer().writeByte(conn.p.mypet.get(index_pet).color);
            m.writer().writeInt(conn.p.mypet.get(index_pet).get_age());
            m.writer().writeShort(conn.p.mypet.get(index_pet).grown);
            m.writer().writeShort(conn.p.mypet.get(index_pet).maxgrown);
            m.writer().writeShort(conn.p.mypet.get(index_pet).point1);
            m.writer().writeShort(conn.p.mypet.get(index_pet).point2);
            m.writer().writeShort(conn.p.mypet.get(index_pet).point3);
            m.writer().writeShort(conn.p.mypet.get(index_pet).point4);
            m.writer().writeShort(conn.p.mypet.get(index_pet).maxpoint);
            m.writer().writeByte(conn.p.mypet.get(index_pet).op.size());
            for (int i2 = 0; i2 < conn.p.mypet.get(index_pet).op.size(); i2++) {
                Option_pet temp2 = conn.p.mypet.get(index_pet).op.get(i2);
                m.writer().writeByte(temp2.id);
                m.writer().writeInt(temp2.param);
                m.writer().writeInt(temp2.maxdam);
            }
            conn.addmsg(m);
            m.cleanup();
        }
    }

    public static void pet_process(Session conn, Message m2) throws IOException {
//        if (conn.p.level < 40) {
//            Service.send_notice_nobox_white(conn, "Yêu cầu trình độ cấp 40");
//            return;
//        }
        byte type = m2.reader().readByte();
        short id = m2.reader().readShort();
        // System.out.println(type);
        // System.out.println(id);
        if (type == 1) {
            boolean duplicated = false;
            for (Pet temp : conn.p.mypet) {
                if (temp.time_born > System.currentTimeMillis()) {
                    duplicated = true;
                    break;
                }
            }
            if (duplicated) {
                send_notice_box(conn, "trong chuồng đã có trứng đang ấp, hãy thử lại sau");
                return;
            }
            Item3 it = conn.p.item.bag3[id];
            if (it != null) {
                Pet temp = Pet.get_pet(it.id, it.expiry_date);
                if (temp == null) {
                    send_notice_box(conn, "có lỗi xảy ra, hãy thử lại sau");
                    return;
                }
                conn.p.mypet.add(temp);
                //
                Message m = new Message(44);
                m.writer().writeByte(28);
                m.writer().writeByte(1);
                m.writer().writeByte(3);
                m.writer().writeByte(3);
                m.writer().writeUTF(it.name);
                m.writer().writeByte(it.clazz);
                m.writer().writeShort(it.id);
                m.writer().writeByte(it.type);
                m.writer().writeShort(it.icon);
                m.writer().writeByte(it.tier);
                m.writer().writeShort(it.level);
                m.writer().writeByte(it.color);
                it.islock = false;
                m.writer().writeByte(it.islock ? 0 : 1);
                m.writer().writeByte(it.islock ? 0 : 1);
                m.writer().writeByte(0); // op size
                    m.writer().writeInt((int) ((temp.time_born - System.currentTimeMillis()) / 60000));
                m.writer().writeByte(it.islock ? 1 : 0);
                conn.addmsg(m);
                m.cleanup();
                conn.p.item.bag3[id] = null;
                conn.p.item.char_inventory(3);
            }
        } else if (type == 0) {
            Message m = null;
            if (conn.p.pet_follow != -1) {
                for (Pet temp : conn.p.mypet) {
                    if (temp.is_follow) {
                        temp.is_follow = false;
                        m = new Message(44);
                        m.writer().writeByte(28);
                        m.writer().writeByte(1);
                        m.writer().writeByte(9);
                        m.writer().writeByte(9);
                        m.writer().writeUTF(temp.name);
                        m.writer().writeByte(temp.type);
                        m.writer().writeShort(conn.p.mypet.indexOf(temp)); // id
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
                        conn.p.conn.addmsg(m);
                        m.cleanup();
                        break;
                    }
                }
            }
            conn.p.mypet.get(id).is_follow = true;
            conn.p.pet_follow = conn.p.mypet.get(id).get_id();
            m = new Message(44);
            m.writer().writeByte(28);
            m.writer().writeByte(2);
            m.writer().writeByte(9);
            m.writer().writeByte(9);
            m.writer().writeShort(id);
            conn.addmsg(m);
            m.cleanup();
            //
            Service.send_wear(conn.p);
            Service.send_char_main_in4(conn.p);
        }
    }

    public static void sell_item(Session conn, Message m2) throws IOException {
        if (conn.p.isDie) {
            return;
        }
        byte type = m2.reader().readByte();
        short id = m2.reader().readShort();
        byte typedel = m2.reader().readByte();
        switch (typedel) {
            case 0: { // drop 2 map
                conn.p.map.drop_item(conn.p, type, id);
                break;
            }
            case 1: { // sell in shop
                switch (type) {
                    case 3:
                    case 4:
                    case 7: {
                        int quant = conn.p.item.total_item_by_id(type, id);
                        conn.p.update_vang(0);
                        conn.p.item.remove(type, id, quant);
                        conn.p.item.char_inventory(type);
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa hỗ trợ bán vật phẩm này");
                        break;
                    }
                }
                break;
            }
        }
    }

    public static void buy_item(Player p, Message m) throws IOException {
        byte type = m.reader().readByte();
        short idbuy = m.reader().readShort();
        int quanity = Short.toUnsignedInt(m.reader().readShort());
        // System.out.println(type);
        // System.out.println(idbuy);
        // System.out.println(quanity);
        if ((type == 0 || type == 1 || type == 4) && !Manager.item_sell.get(type).contains(idbuy)) {
            send_notice_nobox_white(p.conn, "Không thể mua");
            return;
        }
        if (idbuy < 0 || quanity <= 0 || quanity > 32000) {
            return;
        }
        if (p.item.get_bag_able() < 1) {
            send_notice_nobox_white(p.conn, "Hành trang đầy!!");
            return;
        }
        switch (type) {
            case 0: { // cua hang potion
                if (idbuy > (ItemTemplate4.item.size() - 1)) {
                    return;
                }
                long price = ItemTemplate4.item.get(idbuy).getPrice() * quanity;
                if (ItemTemplate4.item.get(idbuy).getPricetype() == 0) {
                    if (p.get_vang() < price) {
                        send_notice_box(p.conn, "Không đủ " + price + " vàng");
                        return;
                    }
                    Log.gI().add_log(p.name, "Trừ " + price + " mua đồ lisa");
                    p.update_vang(-price);
                }else if (ItemTemplate4.item.get(idbuy).getPricetype() == 1 && (idbuy == 343 || idbuy == 350 || idbuy == 183)){
                    if (p.checkcoin() < price) {
                        send_notice_box(p.conn, "Không đủ " + price + " coin");
                        return;
                    }
                    p.update_coin((int) -price);
                    His_COIN hisc = new His_COIN(p.conn.user ,p.name);
                    hisc.coin_change = (int) price;
                    hisc.coin_last = p.checkcoin();
                    hisc.Logger = "(TRỪ COIN) từ mua do1";
                    hisc.Flus();
                    //Log.gI().add_log(p.name, "trừ "+price+" coin từ mua do1");
                } else {
                    if (p.get_ngoc() < price) {
                        send_notice_box(p.conn, "Không đủ " + price + " ngọc");
                        return;
                    }
                    p.update_ngoc(-price);
                }
                int quant_add_bag = quanity + p.item.total_item_by_id(4, idbuy);
                if (quant_add_bag > 32000) {
                    send_notice_box(p.conn, "không thể mua thêm");
                    return;
                }
                p.item.add_item_bag47(idbuy, (short) quanity, (byte) 4);
                p.item.char_inventory(4);
                p.item.char_inventory(7);
                p.item.char_inventory(3);
                break;
            }

            case 1: {

                if (idbuy > (ItemTemplate3.item.size() - 1)) {
                    return;
                }
                if (Helps.CheckItem.isBuyItemCoin(idbuy))//mua bằng coin
                {
                    for (Itemsellcoin itsell3 : Itemsellcoin.entry) {
                        if (itsell3.id == idbuy) {
                            if (!p.update_coin(-itsell3.price)) {
                                send_notice_box(p.conn, "Bạn không đủ coin để mua!");
                                return;
                            }
                            Item3 itbag = new Item3();
                            itbag.id = idbuy;
                            itbag.clazz = ItemTemplate3.item.get(idbuy).getClazz();
                            itbag.type = ItemTemplate3.item.get(idbuy).getType();
                            itbag.level = 10;
                            itbag.icon = ItemTemplate3.item.get(idbuy).getIcon();
                            itbag.color = itsell3.color;
                            itbag.part = ItemTemplate3.item.get(idbuy).getPart();
                            itbag.islock = true;
                            itbag.name = ItemTemplate3.item.get(idbuy).getName();
                            itbag.tier = 0;
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(itsell3.op);
                            itbag.time_use = 0;
                            p.item.add_item_bag3(itbag);
                            p.item.char_inventory(3);
                            send_notice_box(p.conn, "Mua thành công trang bị " + itbag.name);
                            return;
                        }
                    }
                    send_notice_box(p.conn, "Không tìm thấy vật phẩm!");
                    return;
                }
                if (idbuy >= 4831 && idbuy <= 4873)//mua bằng coin shop tt
                {
                    for (Itemshoptt itsell3 : Itemshoptt.entry) {
                        if (itsell3.id == idbuy) {
                            if (!p.update_coin(-itsell3.price)) {
                                send_notice_box(p.conn, "Bạn không đủ coin để mua!");
                                return;
                            }
                            Item3 itbag = new Item3();
                            itbag.id = idbuy;
                            itbag.clazz = ItemTemplate3.item.get(idbuy).getClazz();
                            itbag.type = ItemTemplate3.item.get(idbuy).getType();
                            itbag.level = 1;
                            itbag.icon = ItemTemplate3.item.get(idbuy).getIcon();
                            itbag.color = itsell3.color;
                            itbag.part = ItemTemplate3.item.get(idbuy).getPart();
                            itbag.islock = true;
                            itbag.name = ItemTemplate3.item.get(idbuy).getName();
                            itbag.tier = 15;
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(itsell3.op);
                            itbag.time_use = 0;
                            p.item.add_item_bag3(itbag);
                            p.item.char_inventory(3);
                            send_notice_box(p.conn, "Mua thành công trang bị " + itbag.name);
                            His_COIN hisc = new His_COIN(p.conn.user ,p.name);
                            hisc.coin_change = itsell3.price;
                            hisc.coin_last = p.checkcoin();
                            hisc.Logger = "(TRỪ COIN) từ mua do2";
                            hisc.Flus();
                            return;
                        }
                    }
                    send_notice_box(p.conn, "Không tìm thấy vật phẩm!");
                    return;
                }
                if (ev_he.Event_2.isBuyItemSK(p.conn, 3, idbuy, 1)) {
                    return;
                }
                if (idbuy == 2943 || idbuy == 2944 || (idbuy == 4762 && Manager.gI().event == 2)) {
                    if ((p.get_ngoc() < 500 && idbuy == 4762) || (p.get_ngoc() < 150 && idbuy != 4762)) {
                        send_notice_box(p.conn, "Không đủ ngọc");
                        return;
                    }
                    if (idbuy == 4762)
                        p.update_ngoc(-500);
                    else
                        p.update_ngoc(-150);
                    //
                    Item3 itbag = new Item3();
                    itbag.id = idbuy;
                    itbag.clazz = ItemTemplate3.item.get(idbuy).getClazz();
                    itbag.type = ItemTemplate3.item.get(idbuy).getType();
                    itbag.level = ItemTemplate3.item.get(idbuy).getLevel();
                    itbag.icon = ItemTemplate3.item.get(idbuy).getIcon();
                    itbag.color = ItemTemplate3.item.get(idbuy).getColor();
                    itbag.part = ItemTemplate3.item.get(idbuy).getPart();
                    itbag.islock = true;
                    itbag.name = ItemTemplate3.item.get(idbuy).getName();
                    itbag.tier = 0;
                    itbag.op = new ArrayList<>();
                    itbag.time_use = 0;
                    p.item.add_item_bag3(itbag);
                } else if (idbuy == 3590 || idbuy == 3591 || idbuy == 3592) {
                    if (p.map.map_id != 52) {
                        return;
                    }
                    int vang_quant = (idbuy - 3590) * 50_000 + 100_000;
                    if (p.get_vang() < vang_quant) {
                        send_notice_box(p.conn, "Không đủ " + vang_quant + " vàng");
                        return;
                    }
                    if (p.pet_di_buon.item.size() >= 12) {
                        send_notice_box(p.conn, "Nặng quá!!!");
                        return;
                    }
                    p.update_vang(-vang_quant);
                    Log.gI().add_log(p.name, "Trừ " + vang_quant + " mua kim cương đi buôn");
                    p.pet_di_buon.item.add(idbuy);

                } else {
                    long price = 0;
                    ItemSell3 buffer = null;
                    for (ItemSell3[] itsell_3 : Manager.gI().itemsellTB) {
                        for (ItemSell3 itsell3 : itsell_3) {
                            if (itsell3.id == idbuy) {
                                buffer = itsell3;
                                price = quanity * itsell3.price;
                                break;
                            }
                        }
                        if (price == 0) {
                            Itemsellcoin itemsellcoin = null;
                            for (int i = 0; i < Itemsellcoin.entry.size(); i++) {
                                if (Itemsellcoin.entry.get(i).id == idbuy) {
                                    itemsellcoin = Itemsellcoin.entry.get(i);
                                    break;
                                }
                            }
                            if (itemsellcoin != null) {
                                if (p.update_coin(-itemsellcoin.price)) {
                                    Item3 itbag = new Item3();
                                    itbag.id = idbuy;
                                    itbag.clazz = ItemTemplate3.item.get(idbuy).getClazz();
                                    itbag.type = ItemTemplate3.item.get(idbuy).getType();
                                    itbag.level = 10;
                                    itbag.icon = ItemTemplate3.item.get(idbuy).getIcon();
                                    itbag.color = itemsellcoin.color;
                                    itbag.part = ItemTemplate3.item.get(idbuy).getPart();
                                    itbag.islock = true;
                                    itbag.name = ItemTemplate3.item.get(idbuy).getName();
                                    itbag.tier = 0;
                                    itbag.op = new ArrayList<>();
                                    for (int i = 0; i < itemsellcoin.op.size(); i++) {
                                        itbag.op.add(new Option(itemsellcoin.op.get(i).id, itemsellcoin.op.get(i).getParam(0)));
                                    }
                                    itbag.time_use = 0;
                                    p.item.add_item_bag3(itbag);
                                    p.item.char_inventory(3);
                                    His_COIN hisc = new His_COIN(p.conn.user ,p.name);
                                    hisc.coin_change = itemsellcoin.price;
                                    hisc.coin_last = p.checkcoin();
                                    hisc.Logger = "(TRỪ COIN) từ mua do3";
                                    hisc.Flus();
                                    send_notice_box(p.conn, "Mua thành công " + ItemTemplate3.item.get(idbuy).getName());
                                } else {
                                    send_notice_box(p.conn, "Không đủ " + itemsellcoin.price + " coin");
                                }
                            }
                            return;
                        }
                        if (price == 0) {
                            Itemshoptt itemshoptt = null;
                            for (int i = 0; i < Itemshoptt.entry.size(); i++) {
                                if (Itemshoptt.entry.get(i).id == idbuy) {
                                    itemshoptt = Itemshoptt.entry.get(i);
                                    break;
                                }
                            }
                            if (itemshoptt != null) {
                                if (p.update_coin(-itemshoptt.price)) {
                                    Item3 itbag = new Item3();
                                    itbag.id = idbuy;
                                    itbag.clazz = ItemTemplate3.item.get(idbuy).getClazz();
                                    itbag.type = ItemTemplate3.item.get(idbuy).getType();
                                    itbag.level = 1;
                                    itbag.icon = ItemTemplate3.item.get(idbuy).getIcon();
                                    itbag.color = itemshoptt.color;
                                    itbag.part = ItemTemplate3.item.get(idbuy).getPart();
                                    itbag.islock = true;
                                    itbag.name = ItemTemplate3.item.get(idbuy).getName();
                                    itbag.tier = 15;
                                    itbag.op = new ArrayList<>();
                                    for (int i = 0; i < itemshoptt.op.size(); i++) {
                                        itbag.op.add(new Option(itemshoptt.op.get(i).id, itemshoptt.op.get(i).getParam(0)));
                                    }
                                    itbag.time_use = 0;
                                    p.item.add_item_bag3(itbag);
                                    p.item.char_inventory(3);
                                    His_COIN hisc = new His_COIN(p.conn.user ,p.name);
                                    hisc.coin_change = (int) itemshoptt.price;
                                    hisc.coin_last = p.checkcoin();
                                    hisc.Logger = "(TRỪ COIN) từ mua do4";
                                    hisc.Flus();
                                    send_notice_box(p.conn, "Mua thành công " + ItemTemplate3.item.get(idbuy).getName());
                                } else {
                                    send_notice_box(p.conn, "Không đủ " + itemshoptt.price + " coin");
                                }
                            }
                            return;
                        }
                    }
                    if (buffer.pricetype == 0) {
                        if (p.get_vang() < price) {
                            send_notice_box(p.conn, "Không đủ " + price + " vàng");
                            return;
                        }
                        p.update_vang(-price);
                        Log.gI().add_log(p.name, "Trừ " + price + " mua đồ shop trang bị");
                    } else {
                        if (p.get_ngoc() < price) {
                            send_notice_box(p.conn, "Không đủ " + price + " ngọc");
                            return;
                        }
                        p.update_ngoc(-price);
                    }
                    Item3 itbag = new Item3();
                    itbag.id = idbuy;
                    itbag.clazz = buffer.clazz;
                    itbag.type = buffer.type;
                    itbag.level = buffer.level;
                    itbag.icon = ItemTemplate3.item.get(idbuy).getIcon();
                    itbag.color = buffer.color;
                    itbag.part = ItemTemplate3.item.get(idbuy).getPart();
                    itbag.islock = false;
                    itbag.name = ItemTemplate3.item.get(idbuy).getName();
                    itbag.tier = 0;
                    List<Option> opnew = new ArrayList<>();
                    for (Option op_old : buffer.option) {
                        Option temp = new Option(1, 1, itbag.id);
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
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(opnew);
                    p.item.add_item_bag3(itbag);
                }
                p.item.char_inventory(4);
                p.item.char_inventory(7);
                p.item.char_inventory(3);
                break;
            }
            case 2: {
                p.hair = (byte) idbuy;
                for (int i = 0; i < p.map.players.size(); i++) {
                    Player p0 = p.map.players.get(i);
                    if (p0.index != p.index && Math.abs(p0.x - p.x) < 200 && Math.abs(p0.y - p.y) < 200) {
                        MapService.send_in4_other_char(p0.map, p0, p);
                    }
                }
                Service.send_char_main_in4(p);
                break;
            }
            case 4: {
                if (idbuy > (ItemTemplate7.item.size() - 1)) {
                    return;
                }
                long price = ItemTemplate7.item.get(idbuy).getPrice() * quanity;
                if (ItemTemplate7.item.get(idbuy).getPricetype() == 0) {
                    if (p.get_vang() < price) {
                        send_notice_box(p.conn, "Không đủ " + price + " vàng");
                        return;
                    }
                    p.update_vang(-price);
                    Log.gI().add_log(p.name, "mua " + quanity + " item " + ItemTemplate7.item.get(idbuy).getName() + " hết"
                            + Util.number_format(price) + " vàng");
                }else if(ItemTemplate7.item.get(idbuy).getPricetype() == 1 && ((idbuy >= 246 && idbuy <= 345) || (idbuy >= 384 && idbuy <= 409))){
                    if (p.checkcoin() < price){
                        send_notice_box(p.conn, "Không đủ " + price + " coin.");
                        return;
                    }
                    p.update_coin((int) -price);
                    His_COIN hisc = new His_COIN(p.conn.user ,p.name);
                    hisc.coin_change = (int) price;
                    hisc.coin_last = p.checkcoin();
                    hisc.Logger = "(TRỪ COIN) từ mua do5";
                    hisc.Flus();
                    //Log.gI().add_log(p.name, "trừ "+price+" coin từ mua do2");
                } else {
                    if (p.get_ngoc() < price) {
                        send_notice_box(p.conn, "Không đủ " + price + " ngọc");
                        return;
                    }
                    p.update_ngoc(-price);
                    Log.gI().add_log(p.name, "mua " + quanity + " item " + ItemTemplate7.item.get(idbuy).getName() + " hết"
                            + Util.number_format(price) + " ngọc");
                }
                int quant_add_bag = quanity + p.item.total_item_by_id(7, idbuy);
                if (quant_add_bag > 32000) {
                    send_notice_box(p.conn, "không thể mua thêm");
                    return;
                }
                Item47 itbag = new Item47();
                itbag.id = idbuy;
                itbag.quantity = (short) quanity;
                itbag.category = 7;
                p.item.add_item_bag47(7, itbag);
                p.item.char_inventory(4);
                p.item.char_inventory(7);
                p.item.char_inventory(3);
                break;
            }
            case 6: {
                int value = 200;
                if (idbuy >= 500 && idbuy <= 816) {
                    value = 500;
                } else if (idbuy >= 817 && idbuy <= 826) {
                    value = 100000;
                }
                if (p.myclan.icon == 0) {
                    if (value > 200 && p.get_ngoc() < (value - 200)) {
                        Service.send_notice_box(p.conn, "Không đủ " + (value - 200) + " ngọc!");
                        return;
                    }
                    p.update_ngoc(-(value - 200));
                    send_notice_box(p.conn,
                            "Chúc mừng bạn đã đăng ký thành công bang\nhội. Vui lòng vào Menu>Chức Năng>Bang hội\nđể xem thông tin bang");
                } else {
                    if (p.get_ngoc() < value) {
                        Service.send_notice_box(p.conn, "Không đủ " + value + " ngọc!");
                        return;
                    }
                    p.update_ngoc(-value);
                    send_notice_box(p.conn, "Thay đổi icon thành công");
                }
                p.item.char_inventory(5);
                p.myclan.icon = idbuy;
                MapService.update_in4_2_other_inside(p.map, p);
                Service.send_char_main_in4(p);
                for (Clan_mems mem : p.myclan.mems) {
                    Player p0 = Map.get_player_by_name(mem.name);
                    if (p0 != null) {
                        MapService.update_in4_2_other_inside(p0.map, p0);
                        Service.send_char_main_in4(p0);
                    }
                }
                break;
            }
            case 10: {
                Message m2 = new Message(77);
                m2.writer().writeByte(6);
                p.conn.addmsg(m2);
                m2.cleanup();
                //
                ItemTemplate3 it = ItemTemplate3.item.get(idbuy);
                m2 = new Message(77);
                m2.writer().writeByte(0);
                m2.writer().writeInt(it.getId());
                m2.writer().writeUTF("Chế tạo cánh");
                m2.writer().writeInt(200_000);
                m2.writer().writeShort(60);
                m2.writer().writeInt(0);
                m2.writer().writeByte(6);
                m2.writer().writeShort(8);
                m2.writer().writeShort(80);
                m2.writer().writeShort(9);
                m2.writer().writeShort(60);
                m2.writer().writeShort(10);
                m2.writer().writeShort(40);
                m2.writer().writeShort(11);
                m2.writer().writeShort(20);
                m2.writer().writeShort(0);
                m2.writer().writeShort(100);
                m2.writer().writeShort(3);
                m2.writer().writeShort(20);
                p.conn.addmsg(m2);
                m2.cleanup();
                //
                m2 = new Message(77);
                m2.writer().writeByte(1);
                m2.writer().writeUTF(it.getName());
                p.conn.addmsg(m2);
                m2.cleanup();
                p.is_create_wing = true;
                break;
            }
            case 8: {
                if (p.myclan != null && p.myclan.mems.get(0).name.equals(p.name)) {
                    long price = ItemTemplate4.item.get(idbuy).getPrice() * quanity;
                    if (ItemTemplate4.item.get(idbuy).getPricetype() == 0) {
                        if (p.myclan.get_vang() < price) {
                            send_notice_box(p.conn, "Không đủ " + price + " vàng");
                            return;
                        }
                        p.myclan.update_vang(-price);
                    } else {
                        if (p.myclan.get_ngoc() < price) {
                            send_notice_box(p.conn, "Không đủ " + price + " ngọc");
                            return;
                        }
                        p.myclan.update_ngoc(-((int) price));
                    }
                    Item47 itbag = new Item47();
                    itbag.id = idbuy;
                    itbag.quantity = (short) 1;
                    itbag.category = 4;
                    p.myclan.item_clan.add(itbag);
                }
                break;
            }
        }
        if (type == 6 || type == 10) {
        } else {
            send_notice_box(p.conn, "Mua Thành Công");
        }
    }

    public static void remove_time_use_item(Session conn, Message m2) throws IOException {
        byte type = m2.reader().readByte();
        byte cat = m2.reader().readByte();
        short iditem = m2.reader().readShort();
        // System.out.println(type);
        // System.out.println(cat);
        // System.out.println(iditem);
        if (type == 6) {
            switch (cat) {
                case 3: {
                    Item3 it = conn.p.item.bag3[iditem];
                    if (it != null && it.time_use > 0) {
                        int ngoc_ = conn.p.get_ngoc();
                        if (ngoc_ > 4) {
                            long price = it.time_use - System.currentTimeMillis();
                            price /= 3_600_000;
                            price = (price > 4) ? (price + 1) : 5;
                            if (ngoc_ >= price) {
                                send_box_input_yesno(conn, 115, "Đồng ý dùng " + price + " ngọc để mở khóa thời gian sử dụng?");
                            } else {
                                send_box_input_yesno(conn, 115, "Đồng ý dùng " + ngoc_ + " ngọc để mở khóa " + ngoc_ + "h");
                            }
                            conn.p.id_remove_time_use = iditem;
                        } else {
                            send_notice_box(conn, "Tối thiểu 5 ngọc!");
                        }
                    }
                    break;
                }
            }
        }
    }

    public static void open_box_notice_item(Player p, String notice, short[] id, short[] quant, short[] type)
            throws IOException {
        Message m = new Message(78);
        m.writer().writeUTF(notice);
        m.writer().writeByte(id.length);
        for (int i = 0; i < id.length; i++) {
            switch (type[i]) {
                case 3: {
                    m.writer().writeUTF(ItemTemplate3.item.get(id[i]).getName()); // name
                    m.writer().writeShort(ItemTemplate3.item.get(id[i]).getIcon()); // icon
                    m.writer().writeInt(1); // quantity
                    m.writer().writeByte(type[i]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(ItemTemplate3.item.get(id[i]).getColor()); // color
                    //
                    ItemTemplate3 item = ItemTemplate3.item.get(id[i]);
                    Item3 itbag = new Item3();
                    itbag.id = item.getId();
                    itbag.name = item.getName();
                    itbag.clazz = item.getClazz();
                    itbag.type = item.getType();
                    itbag.level = 10;
                    itbag.icon = item.getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(item.getOp());
                    itbag.color = item.getColor();
                    itbag.part = item.getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    p.item.add_item_bag3(itbag);
                    break;
                }
                case 4: {
                    if (id[i] == -1) {
                        m.writer().writeUTF("Vàng"); // name
                        m.writer().writeShort(0); // icon
                    } else {
                        m.writer().writeUTF(ItemTemplate4.item.get(id[i]).getName()); // name
                        m.writer().writeShort(ItemTemplate4.item.get(id[i]).getIcon()); // icon
                        //
                        Item47 it = new Item47();
                        it.id = (short) (id[i]);
                        it.quantity = (short) Util.random(1, 3);
                        it.category = 4;
                        p.item.add_item_bag47(4, it);
                    }
                    m.writer().writeInt(1); // quantity
                    m.writer().writeByte(type[i]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    break;
                }
                case 7: {
                    m.writer().writeUTF(ItemTemplate7.item.get(id[i]).getName()); // name
                    m.writer().writeShort(ItemTemplate7.item.get(id[i]).getIcon()); // icon
                    m.writer().writeInt(1); // quantity
                    m.writer().writeByte(type[i]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    //
                    Item47 it = new Item47();
                    it.id = (short) (id[i]);
                    it.quantity = (short) Util.random(1, 3);
                    it.category = 7;
                    p.item.add_item_bag47(7, it);
                    break;
                }
            }
        }
        m.writer().writeUTF("");
        m.writer().writeByte(1);
        m.writer().writeByte(1);
        p.conn.addmsg(m);
        m.cleanup();
        p.item.char_inventory(4);
        p.item.char_inventory(7);
        p.item.char_inventory(3);
    }

    public static void Show_open_box_notice_item(Player p, String notice, short[] id, int[] quant, short[] type)
            throws IOException {
        Message m = new Message(78);
        m.writer().writeUTF(notice);
        m.writer().writeByte(id.length);
        for (int i = 0; i < id.length; i++) {
            switch (type[i]) {
                case 3: {
                    m.writer().writeUTF(ItemTemplate3.item.get(id[i]).getName()); // name
                    m.writer().writeShort(ItemTemplate3.item.get(id[i]).getIcon()); // icon
                    m.writer().writeInt(1); // quantity
                    m.writer().writeByte(type[i]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(ItemTemplate3.item.get(id[i]).getColor()); // color
                    break;
                }
                case 4: {
                    if (id[i] == -1) {
                        m.writer().writeUTF("Vàng"); // name
                        m.writer().writeShort(0); // icon
                    } else if (id[i] == -2) {
                        m.writer().writeUTF("Kim cương"); // name
                        m.writer().writeShort(246); // icon
                    } else {
                        m.writer().writeUTF(ItemTemplate4.item.get(id[i]).getName()); // name
                        m.writer().writeShort(ItemTemplate4.item.get(id[i]).getIcon()); // icon
                    }
                    m.writer().writeInt(quant[i]); // quantity
                    m.writer().writeByte(type[i]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    break;
                }
                case 7: {
                    m.writer().writeUTF(ItemTemplate7.item.get(id[i]).getName()); // name
                    m.writer().writeShort(ItemTemplate7.item.get(id[i]).getIcon()); // icon
                    m.writer().writeInt(quant[i]); // quantity
                    m.writer().writeByte(type[i]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    break;
                }
            }
        }
        m.writer().writeUTF("");
        m.writer().writeByte(1);
        m.writer().writeByte(1);
        p.conn.addmsg(m);
        m.cleanup();
        p.item.char_inventory(4);
        p.item.char_inventory(7);
        p.item.char_inventory(3);
    }

    public static void Show_open_box_notice_item(Player p, String notice, List<box_item_template> items)
            throws IOException {
        Message m = new Message(78);
        m.writer().writeUTF(notice);
        m.writer().writeByte(items.size());
        for (box_item_template tem : items) {
            switch (tem.catagory) {
                case 3: {
                    m.writer().writeUTF(ItemTemplate3.item.get(tem.id).getName()); // name
                    m.writer().writeShort(ItemTemplate3.item.get(tem.id).getIcon()); // icon
                    m.writer().writeInt(1); // quantity
                    m.writer().writeByte(3); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(ItemTemplate3.item.get(tem.id).getColor()); // color
                    break;
                }
                case 4: {
                    if (tem.id == -1) {
                        m.writer().writeUTF("Vàng"); // name
                        m.writer().writeShort(0); // icon
                    } else if (tem.id == -2) {
                        m.writer().writeUTF("Kim cương"); // name
                        m.writer().writeShort(246); // icon
                    } else {
                        m.writer().writeUTF(ItemTemplate4.item.get(tem.id).getName()); // name
                        m.writer().writeShort(ItemTemplate4.item.get(tem.id).getIcon()); // icon
                    }
                    m.writer().writeInt(tem.quantity); // quantity
                    m.writer().writeByte(tem.catagory); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    break;
                }
                case 7: {
                    m.writer().writeUTF(ItemTemplate7.item.get(tem.id).getName()); // name
                    m.writer().writeShort(ItemTemplate7.item.get(tem.id).getIcon()); // icon
                    m.writer().writeInt(tem.quantity); // quantity
                    m.writer().writeByte(tem.catagory); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    break;
                }
            }
        }
        m.writer().writeUTF("");
        m.writer().writeByte(1);
        m.writer().writeByte(1);
        p.conn.addmsg(m);
        m.cleanup();
        p.item.char_inventory(3);
        p.item.char_inventory(4);
        p.item.char_inventory(7);

    }

    public static void send_time_box(Player p, final byte size, final short[] time, final String[] tile) throws IOException {
        Message m = new Message(-104);
        m.writer().writeByte(1);
        m.writer().writeByte(size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                m.writer().writeShort(time[i]);
                m.writer().writeUTF(tile[i]);
            }
        }
        p.conn.addmsg(m);
        m.cleanup();
    }

    public static void npcChat(Map map, int idNpc, String chat) throws IOException {
        Message m = new Message(23);
        m.writer().writeUTF(chat);
        m.writer().writeByte(idNpc);
        for (int i = 0; i < map.players.size(); i++) {
            Player p = map.players.get(i);
            if (p != null) {
                p.conn.addmsg(m);
            }
        }
        m.cleanup();
    }

    public static void send_eff_auto(Session conn, List<MainObject> objects, int id_eff) throws IOException {
        byte[] data = Util.loadfile("data/part_char/imgver/x" + conn.zoomlv + "/Data/" + (111 + "_" + id_eff));
        Message m = new Message(-49);
        m.writer().writeByte(4);
        m.writer().writeShort(data.length);
        m.writer().write(data);
        m.writer().writeShort(id_eff);
        m.writer().writeByte(objects.size());
        for (MainObject object : objects) {
            m.writer().writeShort(object.index);
            m.writer().writeByte(object.get_TypeObj());
        }
        for (int i = 0; i < conn.p.map.players.size(); i++) {
            Player p = conn.p.map.players.get(i);
            if (p != null) {
                p.conn.addmsg(m);
            }
        }
        m.cleanup();
    }

    public static void send_eff_intrinsic(Map map, MainObject object, int id, int time) throws IOException {
        // 0 vật lý, 1 băng, 2 lửa, 3 điện, 4 độc, 5 bóng tối, 6 ánh sáng
        Message msg = new Message(50);
        msg.writer().writeByte(object.get_TypeObj());
        msg.writer().writeShort(object.index);
        msg.writer().writeByte(id);
        msg.writer().writeByte(time);
        MapService.send_msg_player_inside(map, object, msg, true);
        msg.cleanup();
    }
}
