package client;

import ai.Bot;
import ai.MobAi;
import ai.NhanBan;
import ai.Player_Nhan_Ban;

import java.io.IOException;

import core.*;

import static core.Service.send_notice_nobox_white;

import ev_he.*;
import event_daily.MoLy;
import event_daily.ChienTruong;
import io.Message;
import io.Session;
import map.*;
import template.EffTemplate;
import template.Horse;

public class MessageHandler {

    private final Session conn;
    private  Map map;

    public MessageHandler(Session conn) {
        this.conn = conn;
    }

    public void process_msg(Message m) throws IOException {
        switch (m.cmd) {
            case -100: {
                GameSrc.Hop_Ngoc_Kham(conn.p, m);
                break;
            }
            case -102: {
                GameSrc.player_store(conn, m);
                break;
            }
            case -91: {
                if (m.reader().available() == 4) {
                    Service.remove_time_use_item(conn, m);
                } else if (m.reader().available() == 2) {
                    MoLy.Lottery_process(conn.p, m);
                } else {
                    String[] menu = new String[]{"Mở ly", "Vòng xoay", "Điểm pk", "Thoát kẹt", "Rơi nguyên liệu mề đay", "Xem lôi đài",
                        (conn.p.isShowMobEvents ? "Tắt " : "Bật ") + "hiển thị sự kiện"};
                    MenuController.send_menu_select(conn, -91, menu);
                }
                break;
            }
            case 77: {
                GameSrc.Wings_Process(conn, m);
                break;
            }
            case -105: {
                if (conn.p.isCreateItemStar) {
                    GameSrc.ActionsItemStar(conn, m);
                } else if (conn.p.isCreateArmor) {
                    GameSrc.ActionsItemArmor(conn, m);
                } else if (conn.p.isdothan) {
                    DoSieucap.nangdothan(conn,m);
                } else if (conn.p.ismdthan) {
                    DoSieucap.nangmdthan(conn,m);
                } else if (conn.p.istb2) {
                    DoSieucap.nangtb2(conn,m);
                } else if (conn.p.istb1) {
                    DoSieucap.nangtb1(conn,m);
                } else if (conn.p.id_medal_is_created != -1){
                    GameSrc.Create_Medal(conn, m);
                }
                break;
            }
            case 69: {
                byte type = m.reader().readByte();
                if (type == 11) {
                    Player p0 = Map.get_player_by_name(m.reader().readUTF());
                    if (p0 != null && p0.myclan != null) {
                        p0.myclan.accept_mem(conn, p0);
                    }
                } else if (conn.p.myclan != null) {
                    conn.p.myclan.clan_process(conn, m, type);
                }
                break;
            }
            case 73: {
                GameSrc.replace_item_process(conn.p, m);
                break;
            }
            case 36: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt, hãy kích hoạt");
                    return;
                }
                GameSrc.trade_process(conn, m);
                break;
            }
            case 48: {
                conn.p.map.create_party(conn, m);
                break;
            }
            case 67: {
                GameSrc.rebuild_item(conn, m);
                break;
            }
            case 9: {
                // if (conn.p.map.map_id == 48) {
                // conn.p.dungeon.use_skill(conn, m);
                // } else {
                // conn.p.map.use_skill(;
                // }
                MapService.use_skill(conn.p.map, conn, m, 0);
                break;
            }
            case 6: {
                // if (conn.p.map.map_id == 48) {
                // } else {
                // conn.p.map.use_skill(conn, m, 1);
                // }
                MapService.use_skill(conn.p.map, conn, m, 1);
                break;
            }
            case 40: {
                // if (conn.p.map.map_id == 48) {
                // } else {
                // conn.p.map.buff_skill(conn, m);
                // }
                MapService.buff_skill(conn.p.map, conn, m);
                break;
            }
            case 20: {
                conn.p.map.pick_item(conn, m);
//                if (conn.p.map.map_id == 48) {
//                } else {
//                    conn.p.map.pick_item(conn, m);
//                }
                break;
            }
            case 11: {
                if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                    if (++conn.p.enough_time_disconnect > 2) {
                        conn.close();
                    }
                    return;
                }
                conn.p.time_speed_rebuild = System.currentTimeMillis() + 500L;
                conn.p.enough_time_disconnect = 0;
                UseItem.ProcessItem3(conn, m);
                break;
            }
            case -107: {
                if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                    if (++conn.p.enough_time_disconnect > 2) {
                        conn.close();
                    }
                    return;
                }
                conn.p.time_speed_rebuild = System.currentTimeMillis() + 500L;
                conn.p.enough_time_disconnect = 0;
                UseItem.ProcessItem7(conn, m);
                break;
            }
            case 32: {
                if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                    if (++conn.p.enough_time_disconnect > 2) {
                        conn.close();
                    }
                    return;
                }
                conn.p.time_speed_rebuild = System.currentTimeMillis() + 500L;
                conn.p.enough_time_disconnect = 0;
                UseItem.ProcessItem4(conn, m);
                break;
            }
            case 24: {
                Service.buy_item(conn.p, m);
                break;
            }
            case 18: {
                Service.sell_item(conn, m);
                break;
            }
            case 37: {
                // arena
                break;
            }
            case 65: {
                conn.p.item.box_process(m);
                break;
            }
            case 44: {
                Service.pet_process(conn, m);
                break;
            }
            case 45: {
                Service.pet_eat(conn, m);
                break;
            }
            case 35: {
                conn.p.friend_process(m);
                break;
            }
            case 34: {
                Service.chat_tab(conn, m);
                break;
            }
            case 22: {
                conn.p.plus_point(m);
                break;
            }
            case -32: {
                Process_Yes_no_box.process(conn, m);
                break;
            }
            case -106: {
                Service.send_item7_template(conn.p, m);
                break;
            }
            case -97: {
                conn.p.down_mount(m);
                break;
            }
            case 28: {
                Service.send_in4_item(conn, m);
                break;
            }
            case 31: {
                // if (conn.p.map.map_id == 48) {
                // conn.p.dungeon.request_livefromdie(conn, m);
                // } else {
                // conn.p.map.request_livefromdie(conn, m);
                // }
                MapService.request_livefromdie(conn.p.map, conn, m);
                break;
            }
            case -31: {
                TextFromClient.process(conn, m);
                break;
            }
            case -53: {
                TextFromClient_2.process(conn, m);
                break;
            }
            case 21: {
                Service.send_param_item_wear(conn, m);
                break;
            }
            case 51: {
                conn.p.change_zone(conn, m);
                break;
            }
            case 42: {
                MapService.change_flag(conn.p.map, conn.p, m.reader().readByte());
                break;
            }
            case 49: {
                Service.send_view_other_player_in4(conn, m);
                break;
            }
            case 71: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt, hãy kích hoạt");
                    return;
                }
                Service.chat_KTG(conn, m);
                break;
            }
            case -30: {
                MenuController.processmenu(conn, m);
                break;
            }
            case 23: {
                MenuController.request_menu(conn, m);
                break;
            }
            case 27: {
                MapService.send_chat(conn.p.map, conn, m);
                break;
            }
            case 12: {
                conn.p.is_changemap = false;
                if (Map.is_map_chien_truong(conn.p.map.map_id)) {
                    ChienTruong.gI().send_info(conn.p);
                    //
                    Message m22 = new Message(4);
                    for (int i = 0; i < ChienTruong.gI().list_ai.size(); i++) {
                        Player_Nhan_Ban p0 = ChienTruong.gI().list_ai.get(i);
                        if (!p0.isdie && p0.map.equals(conn.p.map)) {
                            m22.writer().writeByte(0);
                            m22.writer().writeShort(0);
                            m22.writer().writeShort(p0.id);
                            m22.writer().writeShort(p0.x);
                            m22.writer().writeShort(p0.y);
                            m22.writer().writeByte(-1);
                        }
                    }
                    if (m22.writer().size() > 0) {
                        for (int i = 0; i < conn.p.map.players.size(); i++) {
                            Player p0 = conn.p.map.players.get(i);
                            p0.conn.addmsg(m22);
                        }
                    }
                    m22.cleanup();
                }
                if (conn.p.map.map_id == 48) {
                    // weather map dungeon
                    Message mw = new Message(76);
                    mw.writer().writeByte(4);
                    mw.writer().writeShort(-1);
                    mw.writer().writeShort(-1);
                    conn.addmsg(mw);
                    mw.cleanup();
                }else {
                    Message mw = new Message(76);
                    mw.writer().writeByte(2);
                    mw.writer().writeShort(-1);
                    mw.writer().writeShort(-1);
                    conn.addmsg(mw);
                    mw.cleanup();
                }
                break;
            }
            case -44: {
                Dungeon d = DungeonManager.get_list(conn.p.name);
                if (d != null) {
                    d.send_in4_npc(conn, m);
                }
                break;
            }
            case 5: {
                //int id = Short.toUnsignedInt(m.reader().readShort());
                int id = m.reader().readShort();
                //Leo_thap d = Leo_thapManager.get_list(conn.p.name);
                if (id >= -1000 && id < 0) {
                    for (MobAi temp : conn.p.map.Ai_entrys) {
                        if (temp != null && temp.index == id && !temp.isDie) {
                            temp.send_in4(conn.p);
                            return;
                        }
                    }
                    id = Short.toUnsignedInt((short) id);
                }
                if (conn.p.map.zone_id == conn.p.map.maxzone) {
                    for (Bot temp : conn.p.map.bots) {
                        if (temp != null && temp.index == id && !temp.isDie) {
                            temp.send_info(conn.p);
                            return;
                        }
                    }
                }

                Player p0 = null;
                for (int i = 0; i < conn.p.map.players.size(); i++) {
                    Player p01 = conn.p.map.players.get(i);
                    if (p01.index == id) {
                        p0 = p01;
                        break;
                    }
                }
                if (p0 != null) {
                    MapService.send_in4_other_char(conn.p.map, conn.p, p0);
                } else if (Map.is_map_chiem_mo(conn.p.map, true)) {
                    NhanBan temp = null;
                    for (int i = 0; i < Manager.gI().list_nhanban.size(); i++) {
                        NhanBan temp2 = Manager.gI().list_nhanban.get(i);
                        if (temp2.index == id) {
                            temp = temp2;
                            break;
                        }
                    }
                    if (temp != null) {
                        try {
                            temp.send_in4(conn.p);
                        } catch (Exception e) {
                        }
                    }
                } else if (Map.is_map_chien_truong(conn.p.map.map_id)) {
                    ChienTruong.gI().get_ai(conn.p, id);
                } else {
                    Message m3 = new Message(8);
                    m3.writer().writeShort(id);
                    conn.addmsg(m3);
                    m3.cleanup();
                }
                break;
            }
            case 7: {
                int n = Short.toUnsignedInt(m.reader().readShort());
                if (n >= 30_000 && n < 31_000)//mob event
                {
                    return;
                }
                if (n > 10_000 && n < 11_000) {//mob boss
                    conn.p.map.BossIn4(conn, n);
                    return;
                }
                Dungeon d = DungeonManager.get_list(conn.p.name);
                if (d != null) {
                    d.send_mob_in4(conn, n);
                } else {
                    Service.mob_in4(conn.p, n);
                }
                Leo_thap leoThap = Leo_thapManager.get_list(conn.p.name);
                if (leoThap != null) {
                    leoThap.send_mob_in4(conn, n);
                } else {
                    Service.mob_in4(conn.p, n);
                }
                break;
            }
            case 4: {
                // if (conn.p.map.map_id == 48) {
                // conn.p.dungeon.send_move(conn.p, m);
                // } else {
                // conn.p.map.send_move(conn.p, m);
                // }
                MapService.send_move(conn.p.map, conn.p, m);
                break;
            }
            case -51: {
                Service.send_icon(conn, m);
                break;
            }
            case -52: {
                try {
                    byte type = m.reader().readByte();
                    short id = m.reader().readShort();
                    Message m2 = new Message(-52);
                    m2.writer().writeByte(type);
                    m2.writer().writeShort(id);
                    byte[] arrData = Util.loadfile("data/part_char/imgver/x" + conn.zoomlv + "/Data/" + (type + "_" + id));
                    byte[] arrImg = Util.loadfile("data/part_char/imgver/x" + conn.zoomlv + "/Img/" + (type + "_" + id) + ".png");
                    m2.writer().writeInt(arrImg.length);
                    m2.writer().write(arrImg);
                    m2.writer().write(arrData);
                    conn.addmsg(m2);
                    m2.cleanup();
                } catch (IOException e) {
                }
                break;
            }
            case 55: {
                Service.save_rms(conn, m);
                break;
            }
            case 59: {
                Service.send_health(conn.p);
                break;
            }
            case 13: {
                try {
                    login(m);
                } catch (Exception e) {
                    if (Manager.logErrorLogin) {
                        e.printStackTrace();
                    }
                    conn.close();
                }
                break;
            }
            case 14: {
                conn.char_create(m);
                break;
            }
            case 1: {
                if (!conn.get_in4) {
                    conn.getclientin4(m);
                }
                break;
            }
            case 61: {
                Service.send_msg_data(conn, 61, Manager.gI().msg_61);
                Service.send_item_template(conn);
                Service.send_msg_data(conn, 26, Manager.gI().msg_26);
                break;
            }
            case -103: {//click mob minuong
                byte b = m.reader().readByte();
                if (b != 0) {
                    break;
                }
                short id = (short) (m.reader().readShort() - 1000);
                Mobngoc.Mob_Ngoc mobngoc = Mobngoc.getMob(id);
                Mobtb2.Mob_tb2 mobTb2 = Mobtb2.getMob(id);
                Mobkhu2.Mob_khu2 mobKhu2 = Mobkhu2.getMob(id);
                try {
                    if (mobngoc != null) {
                        MenuController.send_menu_select(conn, id, new String[]{"Nhận ngọc"}, (byte) 113);
                    } else if (mobTb2 != null) {
                        MenuController.send_menu_select(conn, id, new String[]{"Ngươi chọn ta ư"}, (byte) 112);
                    } else if (mobKhu2 != null) {
                        MenuController.send_menu_select(conn, id, new String[]{"Ăn xin"}, (byte) 111);
                    } else if (Manager.gI().event == 8) {
                        MenuController.send_menu_select(conn, id, new String[]{"Nhận quà"}, (byte) Manager.gI().event);
                    } else {
                        MenuController.send_menu_select(conn, id, new String[]{"Hái quả"}, (byte) Manager.gI().event);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
            default: {
                System.out.println("default onRecieveMsg : " + m.cmd);
                break;
            }
        }
    }

    private void login(Message m) throws IOException {
        if (conn.p == null) {
            m.reader().readByte(); // type login
            int id_player_login = m.reader().readInt();
            Player p0 = new Player(conn, id_player_login);

            if (p0 != null && p0.setup()) {
//                synchronized (Session.client_entrys) {
//                    
//                }
                for (int i = Session.client_entrys.size() - 1; i >= 0; i--) {
                    Session s = Session.client_entrys.get(i);
                    if (s == null || s.equals(conn) || s.user == null) {
                        continue;
                    }
                    if (s.get_in4 && s.id == conn.id && s.connected) {
                        try {
                            if (conn.socket.isConnected() && s.socket.isConnected()) {
                                System.out.println("-----errorLogin ----conn: " + conn.socket.getInetAddress() + "-----lastConnect: " + s.socket.getInetAddress());
                            } else {
                                System.out.println("+---- errorLogin ----+");
                            }
                        } catch (Exception e) {
                        }
                        conn.close();
                        s.close();
//                        synchronized (Session.client_entrys) {
//                            Session.client_entrys.remove(conn);
//                            if(Session.client_entrys.get(i).id == conn.id)
//                                Session.client_entrys.remove(i);
//                        }
                        return;
                    }
                }
                conn.p = p0;
                conn.p.set_in4();
                conn.SaveIP();
                MessageHandler.dataloginmap(conn);
            }
        }
    }

    public static void dataloginmap(Session conn) throws IOException {
        Service.send_quest(conn);
        Service.send_auto_atk(conn);
        Service.send_char_main_in4(conn.p);
        Service.send_msg_data(conn, 1, Manager.gI().msg_1);
        Service.send_skill(conn.p);
        Service.send_login_rms(conn);
        Service.send_notice_nobox_yellow(conn, ("Chào Mừng Bạn Đến Với HSO SUPER !! ")); //Số người online : " + (Session.client_entrys.size() + 30)));
//        send_notice_nobox_white(conn, ("Đổi Coin Sang Vàng Ngọc Tại Npc Zuru - Nạp Coin Tại hsomeobeo.pro  "));
//        send_notice_nobox_white(conn, ("Số người online : " + (Session.client_entrys.size())));
        send_notice_nobox_white(conn, ("Bang " +  Manager.nameClanThue  + " Đang Sở Hữu  Quyền Thu Thuế Trên Toàn Sever " + " Thuế " + Manager.thue + " % "));
        Service.send_notice_nobox_yellow(conn, ("Bang " + Manager.nameClanThue + " - Đang Là Bang Hùng Mạnh Nhất Thế Giới Hiệp Sĩ"));
        // add x2 xp
        if (conn.p.get_EffDefault(-125) != null) {
            conn.p.set_x2_xp(1);
        }else if (conn.p.get_EffDefault(-222) != null) {
            conn.p.set_x2_xp(2);
        }else if (conn.p.get_EffDefault(-223) != null) {
            conn.p.set_x2_xp(3);
        }else if (conn.p.get_EffDefault(-224) != null) {
            conn.p.set_x2_xp(4);
        }else if (conn.p.get_EffDefault(-225) != null) {
            conn.p.set_x2_xp(5);
        }
        conn.p.dokho = 0;
        if(conn.p.get_EffDefault(-129) != null) {
            EffTemplate eff = conn.p.get_EffDefault(-129);
            Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Miễn nhiễm hiệu ứng"});
        }
        if (conn.p.myclan == null || !Horse.isHorseClan(conn.p.type_use_mount)) {
            conn.p.type_use_mount = -1;
        }
        MapService.enter(conn.p.map, conn.p);
    }

}
