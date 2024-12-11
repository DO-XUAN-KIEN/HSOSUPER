package event_daily;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import History.His_COIN;
import client.Player;
import core.Log;
import core.Manager;
import core.Service;
import core.Util;
import map.Map;

import static javax.swing.UIManager.get;

import map.Mob_in_map;
import template.Item47;
import template.Mob;

public class DailyQuest {
    public static void get_quest(Player p, byte select) throws IOException {
        List<Integer> list_mob = new ArrayList<>();
        for (int i = 0; i < Mob.entrys.size(); i++) {
            if (Math.abs(Mob.entrys.get(i).level - p.level) <= 10 && !Mob.entrys.get(i).is_boss && Mob.entrys.get(i).mob_id != 167
                    && Mob.entrys.get(i).mob_id != 168 && Mob.entrys.get(i).mob_id != 169 && Mob.entrys.get(i).mob_id != 170
                    && Mob.entrys.get(i).mob_id != 171 && Mob.entrys.get(i).mob_id != 172 && Mob.entrys.get(i).mob_id != 5
                    && Mob.entrys.get(i).mob_id != 11 && Mob.entrys.get(i).mob_id != 23 && Mob.entrys.get(i).mob_id != 91
                    && Mob.entrys.get(i).mob_id != 51 && Mob.entrys.get(i).mob_id != 24 && Mob.entrys.get(i).mob_id != 17
                    && Mob.entrys.get(i).mob_id != 52 && Mob.entrys.get(i).mob_id != 53 && Mob.entrys.get(i).mob_id != 89
                    && Mob.entrys.get(i).mob_id != 92 && Mob.entrys.get(i).mob_id != 155 && Mob.entrys.get(i).mob_id != 106
                    && Mob.entrys.get(i).mob_id != 79 && Mob.entrys.get(i).mob_id != 149 && Mob.entrys.get(i).mob_id != 174
                    && Mob.entrys.get(i).mob_id != 83 && Mob.entrys.get(i).mob_id != 105 && Mob.entrys.get(i).mob_id != 84
                    && Mob.entrys.get(i).mob_id != 101 && Mob.entrys.get(i).mob_id != 104 && Mob.entrys.get(i).mob_id != 103
                    && Mob.entrys.get(i).mob_id != 198 && Mob.entrys.get(i).mob_id != 199 && Mob.entrys.get(i).mob_id != 200
                    && Mob.entrys.get(i).mob_id != 201 && Mob.entrys.get(i).mob_id != 202 && Mob.entrys.get(i).mob_id != 203
                    && Mob.entrys.get(i).mob_id != 204 && Mob.entrys.get(i).mob_id != 205 && Mob.entrys.get(i).mob_id != 206
                    && Mob.entrys.get(i).mob_id != 207 && Mob.entrys.get(i).mob_id != 208 && Mob.entrys.get(i).mob_id != 209
                    && Mob.entrys.get(i).mob_id != 210 && Mob.entrys.get(i).mob_id != 211 && Mob.entrys.get(i).mob_id != 212
                    && Mob.entrys.get(i).mob_id != 213 && Mob.entrys.get(i).mob_id != 214 && Mob.entrys.get(i).mob_id != 215
                    && Mob.entrys.get(i).mob_id != 216 && Mob.entrys.get(i).mob_id != 217 && Mob.entrys.get(i).mob_id != 218) {
                if (checkmob(i)) {
                    list_mob.add(i);
                }
            }
        }

        if (list_mob.isEmpty()) {
            Mob_in_map mob_add = null;
            for (Entry<Integer, Mob_in_map> en : Mob_in_map.ENTRYS.entrySet()) {
                if (mob_add == null) {
                    mob_add = en.getValue();
                } else if (mob_add.level < en.getValue().level) {
                    mob_add = en.getValue();
                }
            }
            list_mob.add(mob_add.index);
        }
        int index = Util.random(list_mob.size());
        p.quest_daily[0] = list_mob.get(index);
        p.quest_daily[1] = select;
        p.quest_daily[2] = 0;
        p.quest_daily[4] -= 1;
        switch (select) {
            case 0: {
                p.quest_daily[3] = Util.random(25, 50);
                break;
            }
            case 1: {
                p.quest_daily[3] = Util.random(100, 150);
                break;
            }
            case 2: {
                p.quest_daily[3] = Util.random(250, 600);
                break;
            }
            case 3: {
                p.quest_daily[3] = Util.random(800, 1500);
                break;
            }
        }
        Mob mob_info = Mob.entrys.get(p.quest_daily[0]);
        Service.send_notice_box(p.conn, String.format("Nhiệm vụ hiện tại:\nTiêu diệt %s.\nMap : %s\nHôm nay còn %s lượt.",
                (p.quest_daily[2] + " / " + p.quest_daily[3] + " " + mob_info.name), mob_info.map.name, p.quest_daily[4]));
    }

    private static boolean checkmob(int i) {
        for (Map[] map : Map.entrys) {
            for (Mob_in_map m_temp : map[0].mobs) {
                if (m_temp == null) {
                    continue;
                }
                if (m_temp.template.mob_id == i) {
                    Mob.entrys.get(i).map = map[0];
                    return true;
                }
            }
        }
        return false;
    }

    public static void remove_quest(Player p) throws IOException {
        if (p.quest_daily[0] == -1) {
            Service.send_notice_box(p.conn, "Hiện tại không nhận nhiệm vụ nào!");
        } else {
            p.quest_daily[0] = -1;
            p.quest_daily[1] = -1;
            p.quest_daily[2] = 0;
            p.quest_daily[3] = 0;
            Service.send_notice_box(p.conn, "Hủy nhiệm vụ thành công, nào rảnh quay lại nhận tiếp nhá!");
        }
    }

    public static String info_quest(Player p) {
        if (p.quest_daily[0] == -1) {
            return String.format("Bạn chưa nhận nhiệm vụ.\nHôm nay còn %s lượt.", p.quest_daily[4]);
        } else {
            checkmob(p.quest_daily[0]);
            Mob mob_info = Mob.entrys.get(p.quest_daily[0]);
            return String.format("Nhiệm vụ hiện tại:\nTiêu diệt %s.\nMap : %s\nHôm nay còn %s lượt.",
                    (p.quest_daily[2] + " / " + p.quest_daily[3] + " " + mob_info.name), mob_info.map.name, p.quest_daily[4]);
        }
    }

    public static void finish_quest(Player p) throws IOException {
        if (p.quest_daily[0] == -1) {
            Service.send_notice_box(p.conn, "Hiện tại không nhận nhiệm vụ nào!");
        } else if (p.quest_daily[2] == p.quest_daily[3]) {
            short id_blue = (short) Util.random(216, 226);
            short id_yellow = (short) Util.random(226, 236);
            short id_violet = (short) Util.random(236, 246);
            short id_ngocrong = (short) Util.random(464,470);
            int vang = Util.random(100_000, 500_000);
            int ngoc = p.quest_daily[1] == 3 ? Util.random(100, 1000)
                    : (p.quest_daily[1] == 2 ? Util.random(400, 500)
                    : (p.quest_daily[1] == 1 ? Util.random(10, 200) : Util.random(10, 20)));
            int exp = Util.random(1000, 10000) * (p.quest_daily[1] + 1) * p.quest_daily[2];
            p.update_vang(vang);
            Log.gI().add_log(p.name, "Nhận " + vang + " nhiệm vụ hàng ngày");
            p.update_coin(ngoc);
            His_COIN hisc = new His_COIN(p.conn.user ,p.name);
            hisc.coin_change = ngoc;
            hisc.coin_last = p.checkcoin();
            hisc.Logger = "(NHẬN) từ nhiệm vụ hàng ngày";
            hisc.Flus();
            //Log.gI().add_log(p.name, "Nhận " + ngoc + " từ điểm danh hàng ngày");
            p.update_Exp(exp, false);
            if (p.quest_daily[1] == 1) {
                if (((p.item.get_bag_able() > 0) || (p.item.total_item_by_id(7, id_blue) > 0))) {
                    Item47 itbag = new Item47();
                    itbag.id = id_blue;
                    itbag.quantity = (short) Util.random(0, 5);
                    itbag.category = 7;
                    p.item.add_item_bag47(7, itbag);
                }
            } else if (p.quest_daily[1] == 2) {
                if (((p.item.get_bag_able() > 0) || (p.item.total_item_by_id(7, id_yellow) > 0))) {
                    Item47 itbag = new Item47();
                    itbag.id = id_yellow;
                    itbag.quantity = (short) Util.random(0, 5);
                    itbag.category = 7;
                    p.item.add_item_bag47(7, itbag);
                }
            } else if (p.quest_daily[1] == 3) {
                if (((p.item.get_bag_able() > 5))) {
                    Item47 itbag = new Item47();
                    itbag.id = id_ngocrong;
                    itbag.quantity = 1;
                    itbag.category = 7;
                    p.item.add_item_bag47(7, itbag);
                }
                if (Manager.gI().event == 11){
                    Item47 itbag = new Item47();
                    itbag.id = 338;
                    itbag.quantity = 2;
                    itbag.category = 4;
                    p.item.add_item_bag47(4, itbag);
                }
            }
            p.item.char_inventory(3);
            p.item.char_inventory(4);
            p.item.char_inventory(7);
            Service.send_notice_box(p.conn,
                    "Trả thành công, nhận được " + vang + " vàng, " + ngoc + " coin và " + exp + " kinh nghiệm!");
            p.quest_daily[0] = -1;
            p.quest_daily[1] = -1;
            p.quest_daily[2] = 0;
            p.quest_daily[3] = 0;
        } else {
            Service.send_notice_box(p.conn, "Chưa hoàn thành được nhiệm vụ!");
        }
    }
}

