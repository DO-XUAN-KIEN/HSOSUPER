package map;

import Helps.ItemStar;
import core.Service;
import event.EventManager;
import event.Event_1;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import client.Player;
import core.Manager;
import core.Util;
import io.Message;
import io.Session;
import template.*;

public class LeaveItemMap {

    public static List<Short> item0x = new ArrayList<>();
    public static List<Short> item1x = new ArrayList<>();
    public static List<Short> item2x = new ArrayList<>();
    public static List<Short> item3x = new ArrayList<>();
    public static List<Short> item4x = new ArrayList<>();
    public static List<Short> item5x = new ArrayList<>();
    public static List<Short> item6x = new ArrayList<>();
    public static List<Short> item7x = new ArrayList<>();
    public static List<Short> item8x = new ArrayList<>();
    public static List<Short> item9x = new ArrayList<>();
    public static List<Short> item10x = new ArrayList<>();
    public static List<Short> item11x = new ArrayList<>();
    public static List<Short> item12x = new ArrayList<>();
    public static List<Short> item13x = new ArrayList<>();
    public static List<Short> item14x = new ArrayList<>();
    public static List<Short> item15x = new ArrayList<>();

    public static void leave_vang(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null) {
            int index_item_map = map.get_item_map_index_able();
            if (index_item_map > -1) {
                //
                map.item_map[index_item_map] = new ItemMap();
                map.item_map[index_item_map].id_item = -1;
                map.item_map[index_item_map].color = 0;
                int vang_drop = Util.random(mob.level * 25, mob.level * 60);
                EffTemplate ef = p.get_EffDefault(52);
                if (ef != null) {
                    vang_drop += (vang_drop * (ef.param / 100)) / 100;
                }
                map.item_map[index_item_map].quantity = vang_drop;
                map.item_map[index_item_map].category = 4;
                map.item_map[index_item_map].idmaster = (short) p.index;
                map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
                map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
                String name = "vàng *" + map.item_map[index_item_map].quantity;
                // add in4 game scr
                Message mi = new Message(19);
                mi.writer().writeByte(4);
                mi.writer().writeShort(mob.index); // index mob die
                mi.writer().writeShort(0); // id icon (0 : vang)
                mi.writer().writeShort(index_item_map); //
                mi.writer().writeUTF(name);
                mi.writer().writeByte(0); // color
                mi.writer().writeShort(p.index); // id player
                MapService.send_msg_player_inside(map, p, mi, true);
                mi.cleanup();
            }
        }
    }

    public static void leave_vang(Map map, MainObject mob, Player p, int vang_drop) throws IOException {
        if (mob != null) {
            int index_item_map = map.get_item_map_index_able();
            if (index_item_map > -1) {
                //
                map.item_map[index_item_map] = new ItemMap();
                map.item_map[index_item_map].id_item = -1;
                map.item_map[index_item_map].color = 0;
                EffTemplate ef = p.get_EffDefault(52);
                if (ef != null) {
                    vang_drop += (vang_drop * (ef.param / 100)) / 100;
                }
                map.item_map[index_item_map].quantity = vang_drop;
                map.item_map[index_item_map].category = 4;
                map.item_map[index_item_map].idmaster = (short) p.index;
                map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
                map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
                String name = "vàng *" + map.item_map[index_item_map].quantity;
                // add in4 game scr
                Message mi = new Message(19);
                mi.writer().writeByte(4);
                mi.writer().writeShort(mob.index); // index mob die
                mi.writer().writeShort(0); // id icon (0 : vang)
                mi.writer().writeShort(index_item_map); //
                mi.writer().writeUTF(name);
                mi.writer().writeByte(0); // color
                mi.writer().writeShort(p.index); // id player
                MapService.send_msg_player_inside(map, p, mi, true);
                mi.cleanup();
            }
        }
    }

    public static void leave_item_3(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null) {
            short id_item_can_drop = 0;
            byte color_ = 0;
            if (60 > Util.random(0, 200)) {
                color_ = 1;
            } else if (25 > Util.random(0, 275)) {
                color_ = 2;
            } else if (15 > Util.random(0, 350)) {
                color_ = 3;
            } else if (10 > Util.random(0, 500)) {
                color_ = 4;
            }
            if (mob.color_name != 0) {
                color_ = 3;
            }
            if (mob.level >= 1 && mob.level < 10 && item0x.size() > 0) {
                id_item_can_drop = item0x.get(Util.random(0, item0x.size()));
            } else if (mob.level >= 10 && mob.level < 20 && item1x.size() > 0) {
                id_item_can_drop = item1x.get(Util.random(0, item1x.size()));
            } else if (mob.level >= 20 && mob.level < 30 && item2x.size() > 0) {
                id_item_can_drop = item2x.get(Util.random(0, item2x.size()));
            } else if (mob.level >= 30 && mob.level < 40 && item3x.size() > 0) {
                id_item_can_drop = item3x.get(Util.random(0, item3x.size()));
            } else if (mob.level >= 40 && mob.level < 50 && item4x.size() > 0) {
                id_item_can_drop = item4x.get(Util.random(0, item4x.size()));
            } else if (mob.level >= 50 && mob.level < 60 && item5x.size() > 0) {
                id_item_can_drop = item5x.get(Util.random(0, item5x.size()));
            } else if (mob.level >= 60 && mob.level < 70 && item6x.size() > 0) {
                id_item_can_drop = item6x.get(Util.random(0, item6x.size()));
            } else if (mob.level >= 70 && mob.level < 80 && item7x.size() > 0) {
                id_item_can_drop = item7x.get(Util.random(0, item7x.size()));
            } else if (mob.level >= 80 && mob.level < 90 && item8x.size() > 0) {
                id_item_can_drop = item8x.get(Util.random(0, item8x.size()));
            } else if (mob.level >= 90 && mob.level < 100 && item9x.size() > 0) {
                id_item_can_drop = item9x.get(Util.random(0, item9x.size()));
            } else if (mob.level >= 100 && mob.level < 110 && item10x.size() > 0) {
                id_item_can_drop = item10x.get(Util.random(0, item10x.size()));
            } else if (mob.level >= 110 && mob.level < 120 && item11x.size() > 0) {
                id_item_can_drop = item11x.get(Util.random(0, item11x.size()));
            } else if (mob.level >= 120 && mob.level < 130 && item12x.size() > 0) {
                id_item_can_drop = item12x.get(Util.random(0, item12x.size()));
            } else if (mob.level >= 130 && item13x.size() > 0) {
                id_item_can_drop = item13x.get(Util.random(0, item13x.size()));
            }
            String name = ItemTemplate3.item.get(id_item_can_drop).getName();
            short index_real = 0;
            if (id_item_can_drop < 20) {
                for (int i = 0; i < 20; i++) {
                    if (ItemTemplate3.item.get(i).getName().equals(name) && ItemTemplate3.item.get(i).getColor() == color_) {
                        index_real = (short) i;
                        break;
                    }
                }
            } else {
                for (int i = id_item_can_drop - 5; i < id_item_can_drop + 5; i++) {
                    if (ItemTemplate3.item.get(i).getName().equals(name) && ItemTemplate3.item.get(i).getColor() == color_) {
                        index_real = (short) i;
                        break;
                    }
                }
            }
            //
            leave_item_by_type3(map, index_real, color_, p, name, mob.index);
        }
    }

    public static void leave_item_by_type3(Map map, int index_real, int color_, Player p_master, String name, int index)
            throws IOException {
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = (short) index_real;
            map.item_map[index_item_map].color = (byte) color_;
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 3;
            map.item_map[index_item_map].idmaster = (short) p_master.index;
            List<Option> opnew = new ArrayList<Option>();
            for (Option op_old : ItemTemplate3.item.get(index_real).getOp()) {
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

    public static void leave_item_4(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null) {
            short index_real = (short) Util.random(0, 6);
            //
            leave_item_by_type4(map, index_real, p, mob.index);
            if(Manager.gI().event == 11) {
                if (Util.random(100) < 2) {
                    leave_item_by_type4(map, (short) 335, p, mob.index);
                }
            }
            if(Manager.gI().event == 8) {
                if (Util.random(100) < 6) {
                    leave_item_by_type4(map, (short) 30, p, mob.index);
                }
            }
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

    public static void leave_item_7(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null) {
            short index_real = (short) Util.random(0, 4);
            //
            leave_item_by_type7(map, index_real, p, mob.index);
        }
    }

    public static void leave_item_by_type7(Map map, short id_it, Player p_master, int indexmob) throws IOException {
        if (p_master != null && !p_master.isDropMaterialMedal && id_it >= 46 && id_it <= 345) {
            return;
        }
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = id_it;
            if (ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getColor() == 21) {
                map.item_map[index_item_map].color = 1;
            } else {
                map.item_map[index_item_map].color = 0;
            }
            map.item_map[index_item_map].quantity = 3;
            map.item_map[index_item_map].category = 7;
            map.item_map[index_item_map].idmaster = (short) p_master.index;
            map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 10000L;
            map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1500L;
            // add in4 game scr
            Message mi = new Message(19);
            mi.writer().writeByte(7);
            mi.writer().writeShort(indexmob); // id mob die
            mi.writer().writeShort(ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getIcon());
            mi.writer().writeShort(index_item_map); //
            mi.writer().writeUTF(ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getName());
            mi.writer().writeByte(map.item_map[index_item_map].color); // color
            mi.writer().writeShort(-1); // id player
            MapService.send_msg_player_inside(map, p_master, mi, true);
            mi.cleanup();
        }
    }

    public static void leave_item_boss(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null && mob.isBoss()) {
            // roi do boss co dinh
            short[] id_item_leave3 = new short[]{};
            short[] id_item_leave4 = new short[]{};
            short[] id_item_leave7 = new short[]{};
            short[] id_item_hongio = new short[]{};
            short[] id_item_leave = new short[]{};
            short[] id_sk4 = new short[]{};
            short[] id_sk4_1 = new short[]{};
            short[] id_sk7 = new short[]{};
            short[] id_item_leave_boss = new short[]{};
            short[] id_medal_material = new short[]{};
          //  short id_medal_material = -19;

            short sizeRandomMedal = 0;
            switch (mob.template.mob_id) {
                case 101: { // xa nu
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{138,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (30);
                    break;
                }
                case 84: { // de vang
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 27, 27};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{139,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (35);

                    break;
                }
                case 83: { // de bac
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{141,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (45);
                    break;
                }
                case 103: { // bo cap chua
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 48, 50, 18, 9, 48, 50, 18, 9, 2, 5, 2, 5, 2, 5, 2, 5};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{136,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (20);
                    break;
                }
                case 104: { // quy 1 mat
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 10, 10};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{137,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (25);
                    break;
                }
                case 105: { // quy dau bo
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 49, 49};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{140,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (40);
                    break;
                }
                case 106: { // ky sy dia nguc
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 18};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{142,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (50);
                    break;
                }
                case 149: { // nhen chua
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 18};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{136,138,140,142,143,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (55);
                    break;
                }
                case 155: { // giant skeleton
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 18};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave_boss = new short[]{144,145,137,139,141,(short) Util.random(472,480)};
                    sizeRandomMedal = (short) (60);
                    break;
                }
                case 173, 195, 196, 197, 186, 187, 188: { //boss khu event
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 273, 274, 251};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 11){
                        id_item_hongio = new short[]{336};
                    }
                    id_item_leave = new short[]{(short) Util.random(481, 493)};
                    id_item_leave7 = new short[]{14};
                    // sizeRandomMedal = (short) (60);
                    break;
                }
                case 178: { //boss sk
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 273, 274, 251};
                    id_medal_material =new short[]{236,237,238,239,240,241,242,243,244,245};
                    id_item_leave = new  short[]{471,349};
                    id_sk7 = new short[]{470};
                    // sizeRandomMedal = (short) (60);
                    break;
                }
                case 190: {
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 273, 274, 251};
                    if(Util.random(100)<10){
                        id_item_leave7 = new short[]{(short) Util.random(481,493)};
                    }
                    if(Manager.gI().event == 7){
                        id_item_hongio = new short[]{345};
                    }
                    id_item_leave7 = new short[]{14};
                    p.boss += 1;
                    break;
                }
                case 23, 51, 52, 53, 79: {
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 273, 274, 251};
                    id_sk4 = new short[]{348};
                    id_item_leave7 = new short[]{(short) Util.random(146, 246),(short) Util.random(481, 485)};
                    break;
                }
                case 193: {
                    id_item_leave4 = new short[]{-1, -1, -1, -1, -1, -1, 273, 274, 251};
                    if (Util.random(11111) < 5){
                        id_item_hongio = new short[]{351};
                    }
                    if (Util.random(5555)< 5){
                        id_item_leave3 = new short[]{4878};
                    }
                    if (Util.random(100) < 25) {
                        id_sk4_1 = new short[]{350};
                    }
                    id_sk4 = new short[]{348};
                    id_item_leave7 = new short[]{(short) Util.random(246, 346),(short) Util.random(481, 493)};
                    break;
                }
            }
            for (short id : id_item_leave3) {
                ItemTemplate3 temp = ItemTemplate3.item.get(id);
                leave_item_by_type3(map, id, temp.getColor(), p, temp.getName(), mob.index);
            }
            for (int i = 0; i < 2; i++) {
                for (short id : id_item_leave4) {
                    if (id == -1) {
                        leave_vang(map, mob, p);
                    } else {
                        leave_item_by_type4(map, id, p, mob.index, p.index);
                    }
                }
            }
            for (int i = 0; i < Util.random(0,10); i++){
                for (short id : id_sk4) {
                    leave_item_by_type4(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 1; i++){
                for (short id : id_sk4_1) {
                    leave_item_by_type4(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 1; i++) {
                for (short id : id_item_hongio) {
                    leave_item_by_type4(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 5; i++) {
                for (short id : id_item_leave7) {
                    leave_item_by_type7(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 5; i++) {
                for (short id : id_item_leave_boss) {
                    leave_item_by_type7(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 5; i++) {
                for (short id : id_item_leave) {
                    leave_item_by_type7(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 4; i++) {
                for (short id : id_medal_material) {
                    leave_item_by_type7(map, id, p, mob.index, p.index);
                }
            }
            for (int i = 0; i < 2; i++) {
                for (short id : id_sk7) {
                    leave_item_by_type7(map, id, p, mob.index, p.index);
                }
            }
            for (int l = 0; l < sizeRandomMedal; l++) {
                leave_item_by_type7(map, (short) Util.random(136, 146), p, mob.index, p.index);
            }
        }
    }

    public static void leave_material(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null) {
            short index_real = -1;
            switch (mob.level) {
                case 1:
                case 2:
                case 3:
                case 4: {
                    index_real = Medal_Material.m_white[0][Util.random(0, 3)];
                    break;
                }
                case 5: {
                    index_real = Medal_Material.m_white[0][Util.random(3, 6)];
                    break;
                }
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17: {
                    index_real = Medal_Material.m_white[0][Util.random(6, 10)];
                    break;
                }
                case 19: {
                    index_real = Medal_Material.m_white[1][Util.random(0, 3)];
                    break;
                }
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26: {
                    index_real = Medal_Material.m_white[1][Util.random(3, 6)];
                    break;
                }
                case 28:
                case 29: {
                    index_real = Medal_Material.m_white[1][Util.random(6, 9)];
                    break;
                }
                case 31:
                case 32:
                case 33:
                case 34:
                case 35: {
                    index_real = Medal_Material.m_white[1][9];
                    break;
                }
                case 37: {
                    index_real = Medal_Material.m_white[2][Util.random(0, 3)];
                    break;
                }
                case 39:
                case 40:
                case 41:
                case 42:
                case 43: {
                    index_real = Medal_Material.m_white[2][Util.random(3, 6)];
                    break;
                }
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54: {
                    index_real = Medal_Material.m_white[2][Util.random(6, 10)];
                    break;
                }
                case 55: {
                    index_real = Medal_Material.m_white[3][Util.random(0, 3)];
                    break;
                }
                case 57:
                case 58:
                case 59: {
                    index_real = Medal_Material.m_white[3][Util.random(3, 6)];
                    break;
                }
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69: {
                    index_real = Medal_Material.m_white[3][Util.random(6, 10)];
                    break;
                }
                case 71:
                case 72:
                case 73:
                case 74: {
                    index_real = Medal_Material.m_white[4][Util.random(0, 3)];
                    break;
                }
                case 76:
                case 77: {
                    index_real = Medal_Material.m_white[4][Util.random(3, 6)];
                    break;
                }
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89: {
                    index_real = Medal_Material.m_white[4][Util.random(6, 10)];
                    break;
                }
                case 90:
                case 91:
                case 92: {
                    index_real = Medal_Material.m_white[5][Util.random(0, 3)];
                    break;
                }
                case 93:
                case 94:
                case 95: {
                    index_real = Medal_Material.m_white[5][Util.random(3, 6)];
                    break;
                }
                case 96:
                case 97:
                case 98:
                case 99:
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                case 106:
                case 107: {
                    index_real = Medal_Material.m_white[5][Util.random(6, 10)];
                    break;
                }
                case 109:
                case 110: {
                    index_real = Medal_Material.m_white[6][Util.random(0, 3)];
                    break;
                }
                case 112:
                case 113: {
                    index_real = Medal_Material.m_white[6][Util.random(3, 6)];
                    break;
                }
                case 114:
                case 115:
                case 116:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123: {
                    index_real = Medal_Material.m_white[6][Util.random(6, 10)];
                    break;
                }
                case 125:
                case 126:
                case 127: {
                    index_real = Medal_Material.m_white[0][Util.random(6, 10)];
                    break;
                }
            }
            //
            if (index_real > -1) {
                //
                leave_item_by_type7(map, index_real, p, mob.index);
            }
//            if (25 > Util.random(0, 100)) {
//                index_real = Medal_Material.m_yellow[Util.random(0, 10)];
//                //
//                leave_item_by_type7(map, index_real, p, mob.index);
//            }
            if (25 > Util.random(0, 100)) {
                index_real = (short) ((15 > Util.random(0, 120)) ? 11
                        : ((35 > Util.random(0, 120)) ? 10 : ((50 > Util.random(0, 120)) ? 9 : 8)));
                //
                leave_item_by_type7(map, index_real, p, mob.index);
            }
        }
    }

    public static void leave_item_event(Map map, Mob_in_map mob, Player p) throws IOException {
        if (Manager.gI().event != 1) {
            return;
        }
        if (mob != null) {
            short index_real = EventManager.item_leave[Manager.gI().event - 1][Util
                    .random(EventManager.item_leave[Manager.gI().event - 1].length)];
            if (!Event_1.check(p.name) && index_real >= 153 && index_real <= 156) {
                return;
            }
            //
            leave_item_by_type4(map, index_real, p, mob.index);
        }
    }

    public static void leave_material_ngockham(Map map, Mob_in_map mob, Player p) throws IOException {
        if (mob != null) {
            short index_real = -1;
            //
            if (25 > Util.random(120)) {
                switch (mob.template.mob_id) {
                    case 167: {
                        index_real = 362;
                        break;
                    }
                    case 168: {
                        index_real = 372;
                        break;
                    }
                    case 169: {
                        index_real = 367;
                        break;
                    }
                    case 170: {
                        index_real = 357;
                        break;
                    }
                    case 171: {
                        index_real = 377;
                        break;
                    }
                    case 172: {
                        index_real = 352;
                        break;
                    }
                }
            }
            if (index_real != -1) {
                leave_item_by_type7(map, index_real, p, mob.index);
            }
        }
    }
     

    public static void leave_vang(Map map, Mob_in_map mob, int id_mater) throws IOException {
        if (mob != null) {
            int index_item_map = map.get_item_map_index_able();
            if (index_item_map > -1) {
                //
                map.item_map[index_item_map] = new ItemMap();
                map.item_map[index_item_map].id_item = -1;
                map.item_map[index_item_map].color = 0;
                int vang_drop = Util.random(mob.level * 25, mob.level * 60);
                map.item_map[index_item_map].quantity = vang_drop;
                map.item_map[index_item_map].category = 4;
                map.item_map[index_item_map].idmaster = (short) id_mater;
                map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
                map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
                String name = "vàng *" + map.item_map[index_item_map].quantity;
                // add in4 game scr
                Message mi = new Message(19);
                mi.writer().writeByte(4);
                mi.writer().writeShort(mob.index); // index mob die
                mi.writer().writeShort(0); // id icon (0 : vang)
                mi.writer().writeShort(index_item_map); //
                mi.writer().writeUTF(name);
                mi.writer().writeByte(0); // color
                mi.writer().writeShort(-1); // id player
                MapService.send_msg_player_inside(map, mob, mi, true);
                mi.cleanup();
            }
        }
    }

    public static void leave_item_by_type3(Map map, int index_real, int color_, Player p_master, String name, int index, int idP)
            throws IOException {
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = (short) index_real;
            map.item_map[index_item_map].color = (byte) color_;
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 3;
            map.item_map[index_item_map].idmaster = (short) idP;
            List<Option> opnew = new ArrayList<>();
            for (Option op_old : ItemTemplate3.item.get(index_real).getOp()) {
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

    public static void leave_item_by_type4(Map map, short index_real, Player p_master, int index, int idp) throws IOException {
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = index_real;
            map.item_map[index_item_map].color = 0;
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 4;
            map.item_map[index_item_map].idmaster = (short) idp;
            map.item_map[index_item_map].time_exist = System.currentTimeMillis() + 60_000L;
            map.item_map[index_item_map].time_pick = System.currentTimeMillis() + 1_500L;
            // add in4 game scr
            Message mi = new Message(19);
            mi.writer().writeByte(4);
            mi.writer().writeShort(index); // id mob die
            mi.writer().writeShort(ItemTemplate4.item.get(map.item_map[index_item_map].id_item).getIcon());
            mi.writer().writeShort(index_item_map); //
            mi.writer().writeUTF(ItemTemplate4.item.get(map.item_map[index_item_map].id_item).getName());
            mi.writer().writeByte(0); // color
            mi.writer().writeShort(-1); // id player
            MapService.send_msg_player_inside(map, p_master, mi, true);
            mi.cleanup();
        }
    }

    public static void leave_item_by_type7(Map map, short id_it, Player p_master, int index, int idp) throws IOException {
        if (p_master != null && !p_master.isDropMaterialMedal && id_it >= 46 && id_it <= 345) {
            return;
        }
        int index_item_map = map.get_item_map_index_able();
        if (index_item_map > -1) {
            //
            map.item_map[index_item_map] = new ItemMap();
            map.item_map[index_item_map].id_item = id_it;
            if (ItemTemplate7.item.get(map.item_map[index_item_map].id_item).getColor() == 21) {
                map.item_map[index_item_map].color = 1;
            } else {
                map.item_map[index_item_map].color = 0;
            }
            map.item_map[index_item_map].quantity = 1;
            map.item_map[index_item_map].category = 7;
            map.item_map[index_item_map].idmaster = (short) idp;
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
            mi.writer().writeShort(p_master.index); // id player
            MapService.send_msg_player_inside(map, p_master, mi, true);
            mi.cleanup();
        }
    }
    public static void randomMD(Session conn) throws IOException {
        try {
            List<box_item_template> ids = new ArrayList<>();
            byte tier = (byte) Util.random(0, 8);
            short id_item = (short) Util.random(4587, 4591);
            Item3 itbag = new Item3();
            itbag.id = id_item;
            itbag.name = ItemTemplate3.item.get(id_item).getName();
            itbag.clazz = ItemTemplate3.item.get(id_item).getClazz();
            itbag.type = ItemTemplate3.item.get(id_item).getType();
            itbag.part = ItemTemplate3.item.get(id_item).getPart();
            itbag.icon = ItemTemplate3.item.get(id_item).getIcon();
            itbag.level = 1;
            itbag.op = new ArrayList<>();
            int[] st = {0,1, 2, 3, 4};
            int[] stthem = {7, 8, 9, 10, 11};
            int[] Khang = {16, 17, 18, 19, 20, 21, 22};
            int[] point = {23, 24, 25, 26};
            int[] donghe = {33, 34, 35, 36};
            int[] hp_mp = {27, 28, 29, 30, 31, 32};
            int[] phongthu = {14, 15};
            int[] kntc_pt = {37, 38};
            int[] dongan= {96};
            int opidan = dongan[Util.nextInt(dongan.length)];
            int opid0 = st[Util.nextInt(st.length)];
            int opid1 = stthem[Util.nextInt(stthem.length)];
            int opid2 = Khang[Util.nextInt(Khang.length)];
            int opid3 = Khang[Util.nextInt(Khang.length)];
            int opid4 = Khang[Util.nextInt(Khang.length)];
            int opid5 = Khang[Util.nextInt(Khang.length)];
            int opid6 = Khang[Util.nextInt(Khang.length)];
            int opid7 = Khang[Util.nextInt(Khang.length)];
            int opid8 = Khang[Util.nextInt(Khang.length)];
            int opid9 = point[Util.nextInt(point.length)];
            int opid10 = point[Util.nextInt(point.length)];
            int opid11 = point[Util.nextInt(point.length)];
            int opid12 = point[Util.nextInt(point.length)];
            int opid13 = point[Util.nextInt(point.length)];
            int opid14 = donghe[Util.nextInt(donghe.length)];
            int opid15 = donghe[Util.nextInt(donghe.length)];
            int opid16 = donghe[Util.nextInt(donghe.length)];
            int opid17 = hp_mp[Util.nextInt(hp_mp.length)];
            int opid18 = hp_mp[Util.nextInt(hp_mp.length)];
            int opid19 = hp_mp[Util.nextInt(hp_mp.length)];
            int opid20 = hp_mp[Util.nextInt(hp_mp.length)];
            int opid21 = hp_mp[Util.nextInt(hp_mp.length)];
            int opid22 = phongthu[Util.nextInt(phongthu.length)];
            int opid23 = kntc_pt[Util.nextInt(kntc_pt.length)];
            List<Integer> opidList = new ArrayList<>();
            Collections.addAll(opidList, opid1, opid2, opid3, opid4, opid5, opid6, opid7, opid8,
                    opid9, opid10, opid11, opid12, opid13, opid14, opid15, opid16, opid17, opid18, opid19,
                    opid20, opid21, opid22, opid23);
            Collections.shuffle(opidList);
            if (tier == 0) {
                itbag.op.add(Option.randomdongmd(opid0, itbag.id));
                itbag.op.add(Option.randomdongmd(opidan, itbag.id));
            } else if (tier >= 1) {
                itbag.op.add(Option.randomdongmd(opid0, itbag.id));
                itbag.op.add(Option.randomdongmd(opidan, itbag.id));
                for (int i = 0; i < tier && i < opidList.size(); i++) {
                    itbag.op.add(Option.randomdongmd(opidList.get(i), itbag.id));
                }
            }
            itbag.color = 4;
            itbag.tier = tier;
            itbag.time_use = 0;
            itbag.islock = true;
            conn.p.item.add_item_bag3(itbag);
            conn.p.item.char_inventory(3);
            ids.add(new box_item_template(id_item, (short) 1, (byte) 3));
            Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
