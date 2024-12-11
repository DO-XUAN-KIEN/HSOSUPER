package core;

import io.Session;
import template.Item3;
import template.ItemTemplate3;
import template.box_item_template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Quask {
    //<editor-fold defaultstate="collapsed" desc="sk hồn gió...">
    public static void quask_hon_gio(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.item.get_bag_able() <= 1) {
                    Service.send_notice_box(conn, "Hành trang đầy");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                List<Integer> duclo = new ArrayList<>(java.util.Arrays.asList(33, 44, 45));
                List<Integer> danangcap = new ArrayList<>(java.util.Arrays.asList(471, 349));
                List<Integer> thucuoi = new ArrayList<>(java.util.Arrays.asList(281, 251, 301, 269, 275, 299, 279, 271, 246, 323));
                for (int i = 0; i < 3; i++) {
                    int ran = Util.random(101);
                    if (ran > 100) { // đá nâng cấp
                        short id = Util.random(danangcap, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 90 && ran <= 93) { // nlmd
                        short id = (short) Util.random(46, 346);
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 85 && ran < 90) { // đục
                        short id = Util.random(duclo, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 60 && ran < 80) { // vé mở ly
                        short id = 52;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 53 && ran < 55) { // thú cưỡi
                        short id = Util.random(thucuoi, new ArrayList<>()).shortValue();
                        ;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 20 && ran < 35) { // sách nâng skill
                        short id = (short) Util.random(472, 480);
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 10 && ran < 20) { // hồ quang
                        short id = (short) Util.random(472, 480);
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else {
                        Service.send_notice_nobox_white(conn, "Đen vl. Bạn dell đc gì");
                    }
                }
                conn.p.sk_hongio += 1;
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.item.get_bag_able() <= 1) {
                    Service.send_notice_box(conn, "Hành trang đầy");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                List<Integer> duclo = new ArrayList<>(java.util.Arrays.asList(33, 44, 45));
                List<Integer> thucuoi = new ArrayList<>(java.util.Arrays.asList(281, 251, 301, 269, 275, 299, 279, 271, 246, 323));
                List<Integer> ngockham = new ArrayList<>(java.util.Arrays.asList(354, 359, 364, 369, 374, 379));
                List<Integer> danangcap = new ArrayList<>(java.util.Arrays.asList(471, 350));
                List<Integer> nlmdtim = new ArrayList<>(java.util.Arrays.asList(
                        136, 137, 138, 139, 140, 141, 142, 143, 144, 145,
                        236, 237, 238, 239, 240, 241, 242, 243, 244, 245,
                        336, 337, 338, 339, 340, 341, 342, 343, 344, 345));
                for (int i = 0; i < 3; i++) {
                    int ran = Util.random(101);
                    if (ran > 101) { // sách 110
                        short iditem = (short) Util.random(4577, 4585);
                        Item3 itbag = new Item3();
                        itbag.id = iditem;
                        itbag.name = ItemTemplate3.item.get(iditem).getName();
                        itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                        itbag.type = ItemTemplate3.item.get(iditem).getType();
                        itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                        itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                        itbag.color = ItemTemplate3.item.get(iditem).getColor();
                        itbag.part = ItemTemplate3.item.get(iditem).getPart();
                        itbag.tier = 0;
                        itbag.islock = false;
                        itbag.time_use = 0;
                        conn.p.item.add_item_bag3(itbag);
                        conn.p.item.char_inventory(5);
                        ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    } else if (ran > 95 && ran < 97) { // rương boss phe
                        short id = 273;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 93 && ran < 95) { // nlmd
                        short id = Util.random(nlmdtim, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 85 && ran < 88) {// ngọc khảm
                        short id = Util.random(ngockham, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 80 && ran < 84) { // đá nâng cấp
                        short id = Util.random(danangcap, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 70 && ran < 75) { // đục
                        short id = Util.random(duclo, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 60 && ran < 65) { // rương chiến công
                        short id = 274;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 50 && ran < 55) { // vé mở ly
                        short id = 52;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 40 && ran < 45) { // thú cưỡi
                        short id = Util.random(thucuoi, new ArrayList<>()).shortValue();
                        ;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 20 && ran < 35) { // sách nâng skill
                        short id = (short) Util.random(472, 480);
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 10 && ran < 20) { // hồ quang
                        short id = (short) Util.random(472, 480);
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else {
                        Service.send_notice_nobox_white(conn, "Đen vl bạn dell đc gì");
                    }
                }
                conn.p.sk_hongio += 3;
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 2: {
                if (conn.p.item.get_bag_able() <= 1) {
                    Service.send_notice_box(conn, "Hành trang đầy");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                List<Integer> duclo = new ArrayList<>(java.util.Arrays.asList(33, 44, 45));
                List<Integer> item3 = new ArrayList<>(java.util.Arrays.asList(4732, 4733, 4710, 4730, 4731));
                List<Integer> thucuoi = new ArrayList<>(java.util.Arrays.asList(281, 251, 301, 269, 275, 299, 279, 271, 246, 323));
                List<Integer> ngockham = new ArrayList<>(java.util.Arrays.asList(354, 355, 356, 359, 360, 361, 364, 365, 366, 369, 370, 371, 374, 375, 376, 379, 380, 381));
                List<Integer> danangcap = new ArrayList<>(java.util.Arrays.asList(471, 351));
                List<Integer> nlmdtim = new ArrayList<>(java.util.Arrays.asList(
                        136, 137, 138, 139, 140, 141, 142, 143, 144, 145,
                        236, 237, 238, 239, 240, 241, 242, 243, 244, 245,
                        336, 337, 338, 339, 340, 341, 342, 343, 344, 345));

                for (int i = 0; i < 3; i++) {
                    int ran = Util.random(101);
                    if (ran >= 100.9999) {// trang bị
                        short iditem = Util.random(item3, new ArrayList<>()).shortValue();
                        ;
                        Item3 itbag = new Item3();
                        itbag.id = iditem;
                        itbag.name = ItemTemplate3.item.get(iditem).getName();
                        itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                        itbag.type = ItemTemplate3.item.get(iditem).getType();
                        itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                        itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                        itbag.color = ItemTemplate3.item.get(iditem).getColor();
                        itbag.part = ItemTemplate3.item.get(iditem).getPart();
                        itbag.tier = 0;
                        itbag.islock = false;
                        itbag.time_use = 0;
                        conn.p.item.add_item_bag3(itbag);
                        conn.p.item.char_inventory(5);
                        ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    } else if (ran >= 99 && ran < 100) { // sách 110
                        short iditem = (short) Util.random(4577, 4585);
                        Item3 itbag = new Item3();
                        itbag.id = iditem;
                        itbag.name = ItemTemplate3.item.get(iditem).getName();
                        itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                        itbag.type = ItemTemplate3.item.get(iditem).getType();
                        itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                        itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                        itbag.color = ItemTemplate3.item.get(iditem).getColor();
                        itbag.part = ItemTemplate3.item.get(iditem).getPart();
                        itbag.tier = 0;
                        itbag.islock = false;
                        itbag.time_use = 0;
                        conn.p.item.add_item_bag3(itbag);
                        conn.p.item.char_inventory(5);
                        ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    } else if (ran > 90 && ran < 94) { // rương boss phe
                        short id = 273;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 85 && ran < 88) { // nlmd
                        short id = Util.random(nlmdtim, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                    } else if (ran > 80 && ran < 82) { // vp buff
                        short id = (short) Util.random(328, 334);
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 77 && ran < 80) { // đồng money
                        short id = 494;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 70 && ran < 75) {// ngọc khảm
                        short id = Util.random(ngockham, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 64 && ran < 68) { // đá nâng cấp
                        short id = Util.random(danangcap, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 52 && ran < 64) { // đục
                        short id = Util.random(duclo, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 40 && ran < 50) { // rương chiến công
                        short id = 274;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 30 && ran < 40) { // vé mở ly
                        short id = 52;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 20 && ran < 30) { // thú cưỡi
                        short id = Util.random(thucuoi, new ArrayList<>()).shortValue();
                        ;
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 10 && ran < 20) { // sách nâng skill
                        short id = (short) Util.random(472, 480);
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran > 1 && ran < 10) { // hồ quang
                        short id = (short) Util.random(472, 480);
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else {
                        Service.send_notice_nobox_white(conn, "Đen vl bạn dell đc gì");
                    }
                }
                conn.p.sk_hongio += 5;
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="sk mới...">
    public static void qua_sk_hallweeen(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.item.get_bag_able() <= 5) {
                    Service.send_notice_box(conn, "Hành trang đầy");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    int ran = Util.random(101);
                    if(ran > 60) {// nltt
                        short id = (short) Util.random(417, 464);
                        short quant = 2;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran > 30 && ran <= 45) { // nlmd
                        short id = (short) Util.random(246, 346);
                        short quant = 2;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran > 10 && ran <= 20) { // vàng
                        int vag = Util.random(100_000, 1_000_000);
                        conn.p.update_vang(vag);
                        Service.send_notice_nobox_white(conn,"Bận nhận được "+vag+" vàng.");
                    }else if (ran > 1 && ran <= 10) { // coin
                        int coin = Util.random(100, 1_000);
                        conn.p.update_coin(coin);
                        Service.send_notice_nobox_white(conn,"Bận nhận được "+coin+" coin.");
                    } else {
                        Service.send_notice_nobox_white(conn, "Đen vl. Bạn dell đc gì");
                    }
                }
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
            case 1: {
                if (conn.p.item.get_bag_able() <= 5) {
                    Service.send_notice_box(conn, "Hành trang đầy");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                List<Integer> item3 = new ArrayList<>(java.util.Arrays.asList(4577, 4580, 4582, 4584));
                List<Integer> item3no1 = new ArrayList<>(java.util.Arrays.asList(4578, 4579, 4581, 4584));
                for (int i = 0; i < 2; i++) {
                    int ran = Util.random(101);
                    if(ran > 99) {// coin
                        int coin = Util.random(5000, 10000);
                        conn.p.update_coin(coin);
                        Service.send_notice_nobox_white(conn, "Bận nhận được " + coin + " coin.");
                    }else if (ran == 95) {// đồng monney
                        short id = 494;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran > 92 && ran < 95) { // sách 110
                            short idsach = Util.random(item3, new ArrayList<>()).shortValue();
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                    }else if (ran == 90) { // sách 110
                        short idsach = Util.random(item3no1, new ArrayList<>()).shortValue();
                        ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                        conn.p.item.add_item_bag3_default(idsach, 0, false);
                    }else if (ran < 90 && ran >= 85){ // Đá krypton cấp 1
                        short id = 349;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran < 85 && ran >= 80){ // Đá krypton cấp 2
                        short id = 350;
                        short quant = (short) Util.random(1, 5);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran < 80 && ran >= 74){ // Đá krypton cấp 3
                        short id = 351;
                        short quant = (short) Util.random(5, 10);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran < 72 && ran >= 65){ // hộ vệ buff
                        short id = (short) Util.random(328, 334);
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    }else if (ran < 63 && ran >= 55){// hồ quang
                        short id = (short) Util.random(481, 493);
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran < 53 && ran >= 45){// Ngọc nâng tb2
                        short id = 493;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    }else if (ran < 44 && ran >= 30){ // vé khu boss cá nhân
                        short id = 342;
                        short quant = (short) Util.random(1, 10);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    }else if (ran < 30 && ran >= 15){ // rương chiến công
                        short id = 274;
                        short quant = (short) Util.random(1, 100);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    }else if (ran < 15 && ran > 10){ // rương boss phe
                        short id = 273;
                        short quant = (short) Util.random(1, 100);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    }else if (ran < 10 && ran >= 1) { // vàng
                        int vag = Util.random(1_000_000, 50_000_000);
                        conn.p.update_vang(vag);
                        Service.send_notice_nobox_white(conn,"Bận nhận được "+vag+" vàng.");
                    } else {
                        Service.send_notice_nobox_white(conn, "Đen vl. Bạn dell đc gì");
                    }
                }
                conn.p.doiqua += 1;
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            } default: {
                Service.send_notice_box(conn,"Có cái nịt");
                break;
            }
        }
    }
    public static void Menu_noel(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {// Đồng money
                short id = 494;
                short quant = (short) Util.random(1, 10);
                conn.p.item.add_item_bag47(id, quant, (byte) 7);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{7});
                break;
            }
            case 1: {// ngọc khảm
                short id = (short) Util.random(352, 382);
                short quant = (short) Util.random(1, 10);
                conn.p.item.add_item_bag47(id, quant, (byte) 7);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{7});
                break;
            }
            case 2: {// hồ quang
                short id = (short) Util.random(481, 493);
                short quant = (short) Util.random(1, 50);
                conn.p.item.add_item_bag47(id, quant, (byte) 7);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{7});
                break;
            }
            case 3: {// thú cưỡi thường
                List<Integer> thucuoithuong = new ArrayList<>(java.util.Arrays.asList(Util.random(62, 67), 124, 222, 246, 251, 269, 271, 275, 279, 281, 294, 299, 301, 323));
                short id = Util.random(thucuoithuong, new ArrayList<>()).shortValue();
                short quant = (short) Util.random(1, 10);
                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                break;
            }
            case 4: {// thú cưỡi vv
                List<Integer> thucuoivv = new ArrayList<>(java.util.Arrays.asList(248, 250, 268, 296, 313, 314, 315, 316, 317));
                short id = Util.random(thucuoivv, new ArrayList<>()).shortValue();
                short quant = 1;
                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                break;
            }
            case 5: { // tuần lộc vip
                short id = 352;
                short quant = 1;
                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                Service.Show_open_box_notice_item(conn.p,"Bạn nhận được", new short[]{id},new int[]{quant}, new short[]{4});
                break;
            }
            case 6: { // dây chuyền vip
                short id = 4880;
                conn.p.item.add_item_bag3_default(id, 0, true);
                Service.Show_open_box_notice_item(conn.p,"Bạn nhận được", new short[]{id},new int[]{1}, new short[]{3});
                break;
            }
            case 7: { // Găng vip
                short id = 4881;
                conn.p.item.add_item_bag3_default(id, 0, true);
                Service.Show_open_box_notice_item(conn.p,"Bạn nhận được", new short[]{id},new int[]{1}, new short[]{3});
                break;
            }
            case 8: { // Giày vip
                short id = 4882;
                conn.p.item.add_item_bag3_default(id, 0, true);
                Service.Show_open_box_notice_item(conn.p,"Bạn nhận được", new short[]{id},new int[]{1}, new short[]{3});
                break;
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="sk mới...">
    public static void qua_sk_moi(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                try {
                    if (conn.p.item.get_bag_able() <= 5) {
                        Service.send_notice_box(conn, "Hành trang đầy");
                        return;
                    }
                    List<box_item_template> ids = new ArrayList<>();
                    List<Integer> item3 = new ArrayList<>(java.util.Arrays.asList(4718, 4719));
                    List<Integer> duclo = new ArrayList<>(java.util.Arrays.asList(33, 44, 45));
                    List<Integer> ngockham = new ArrayList<>(java.util.Arrays.asList(354, 355, 356, 359, 360, 361, 364, 365, 366, 369, 370, 371, 374, 375, 376, 379, 380, 381));
                    for (int i = 0; i < 3; i++) {
                        int ran = Util.random(101);
                        if (ran > 100.999999) { // sách 110
                            short idsach = Util.random(item3, new ArrayList<>()).shortValue();
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran > 100) { // sách 110
                            short idsach = (short) Util.random(4577, 4585);
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran > 95 && ran <= 99) { // nlmd
                            short id = (short) Util.random(46, 346);
                            short quant = 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran > 90 && ran <= 95) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran > 85 && ran < 88) { // đục
                            short id = Util.random(duclo, new ArrayList<>()).shortValue();
                            short quant = 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran > 70 && ran < 80) { // vé đi buôn
                            short id = 147;
                            short quant = (short) Util.random(0, 5);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran > 60 && ran < 70) { // vé mở ly
                            short id = 52;
                            short quant = (short) Util.random(1, 10);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran > 40 && ran < 55) { // ngọc khảm
                            short id = Util.random(ngockham, new ArrayList<>()).shortValue();
                            short quant = 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran > 20 && ran < 35) { // sách nâng skill
                            short id = (short) Util.random(472, 480);
                            short quant = (short) Util.random(1, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran > 10 && ran < 20) { // hồ quang
                            short id = (short) Util.random(472, 480);
                            short quant = (short) Util.random(1, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else {
                            Service.send_notice_nobox_white(conn, "Đen vl. Bạn dell đc gì");
                        }
                    }
                    conn.p.sk_hongio += 1;
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 1: {
                if (conn.p.item.get_bag_able() <= 5) {
                    Service.send_notice_box(conn, "Hành trang đầy");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                List<Integer> trung_xin = new ArrayList<>(java.util.Arrays.asList(3269, 4617, 4626, 4699, 4708));
                List<Integer> trung_vua = new ArrayList<>(java.util.Arrays.asList(3616, 4622, 4631, 4788));
                List<Integer> trung_phe = new ArrayList<>(java.util.Arrays.asList(2939, 2943, 2944, 4761, 4762, 4768));
                List<Integer> thucuoi = new ArrayList<>(java.util.Arrays.asList(317, 316, 314, 313, 296, 268));
                for (int i = 0; i < 2; i++) {
                    int ran = Util.random(101);
                    if (ran > 100.9999) { // trứng xịn
                        short id = Util.random(trung_xin, new ArrayList<>()).shortValue();
                        ids.add(new box_item_template(id, (short) 1, (byte) 3));
                        conn.p.item.add_item_bag3_default(id, 0, false);
                    } else if (ran > 94 && ran < 100) { // trứng vừa
                        short id = Util.random(trung_vua, new ArrayList<>()).shortValue();
                        ;
                        ids.add(new box_item_template(id, (short) 1, (byte) 3));
                        conn.p.item.add_item_bag3_default(id, 0, false);
                    } else if (ran > 60 && ran < 90) { // trứng phế
                        short id = Util.random(trung_phe, new ArrayList<>()).shortValue();
                        ;
                        ids.add(new box_item_template(id, (short) 1, (byte) 3));
                        conn.p.item.add_item_bag3_default(id, 0, false);
                    } else if (ran > 30 && ran < 60) { // thú cưỡi vv
                        short id = Util.random(thucuoi, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 2 && ran < 30) { // vé đi buôn
                        short id = 147;
                        short quant = (short) Util.random(10, 20);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else {
                        Service.send_notice_nobox_white(conn, "Đen vl. Bạn dell đc gì");
                    }
                }
                conn.p.sk_hongio += 5;
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
        }
    }
    //</editor-fold>
}
