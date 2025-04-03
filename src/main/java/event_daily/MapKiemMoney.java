package event_daily;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import ai.Player_Nhan_Ban;
import client.Player;
import core.Manager;
import core.Service;
import core.Util;
import io.Message;
import map.LeaveItemMap;
import static map.LeaveItemMap.leave_vang;
import map.Map;
import map.MapService;
import map.Mob_in_map;
import map.Vgo;
import template.*;
import BossHDL.*;

public class MapKiemMoney {
    private static MapKiemMoney instance;
    private HashMap<String, Player_Money> list;
    private int status; // 0 sleep, 1 : register, 2 : start
    private int time;
    public long vang;
    public long ngoc;


    public int getStatus() {
        return status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int i) {
        this.time = i;
    }

    public static MapKiemMoney gI() {
        if (instance == null) {
            instance = new MapKiemMoney();
            instance.init();
        }
        return instance;
    }

    private void init() {
        this.list = new HashMap<>();
        this.status = 0;
        this.time = 0;
        this.vang = 0;
        this.ngoc = 0;
        //
    }

    public synchronized void update() {
        try {
            if (this.status == 2) {
                this.time--;
                //
                if (this.time <= 0) {
                    this.finish();
                }
            }
        } catch (IOException e) {
        }
    }
    private void finish() throws IOException {
        if (this.status == 2) {
            Manager.gI().chatKTGprocess("Chiến trường kết thúc, hẹn gặp lại vào ngày mai");
            //
            for (Entry<String, Player_Money> en : this.list.entrySet()) {
                System.out.println(en.getKey() + " " + en.getValue().village);
                Player p0 = Map.get_player_by_name(en.getKey());
                if (p0 != null) {
                    Vgo vgo = new Vgo();
                    vgo.id_map_go = 1;
                    vgo.x_new = 432;
                    vgo.y_new = 354;
                    p0.change_map(p0, vgo);
                    MapService.change_flag(p0.map, p0, -1);
                }
            }
            //
            this.list.clear();
            this.status = 0;
            this.time = 0;
        }
    }

    public synchronized void register(Player p) throws IOException {
        if (this.getStatus() != 2){
            Service.send_notice_box(p.conn,"Chưa đến giờ tham gia");
            return;
        }
        Vgo vgo = null;
        if (this.list.containsKey(p.name)) {
            vgo = new Vgo();
            vgo.id_map_go = 90;
            vgo.x_new = 456;
            vgo.y_new = 360;
            p.change_map(p, vgo);
        } else {
            //
            Player_Money temp = new Player_Money();
            temp.name = p.name;
            temp.point = 0;
            temp.village = 0;
            temp.received = false;
            this.list.put(p.name, temp);
            Service.send_notice_nobox_white(p.conn, "Đăng ký thành công");
            //
            vgo = new Vgo();
            vgo.id_map_go = 90;
            vgo.x_new = 456;
            vgo.y_new = 360;
            p.change_map(p, vgo);
        }
    }

    public synchronized void open_register() throws IOException {
        if (this.status == 0) {
            Manager.gI().chatKTGprocess("Map money đã mở, mau mau đến tham gia");
            this.status = 2;
            this.time = 60*120;
        }
    }
    public void update_time(Player p) throws IOException {
        Message m = new Message(-94);
        m.writer().writeByte(-1); //
        m.writer().writeByte(0);
        m.writer().writeShort(0);
        m.writer().writeByte(0);
        m.writer().writeLong(System.currentTimeMillis() - (60 * 60 - this.time) * 1000L);
        p.conn.addmsg(m);
        m.cleanup();
    }
    public static void Obj_Die(Map map, MainObject mainAtk, MainObject focus)throws IOException{
        if(!mainAtk.isPlayer() || !focus.isMob())return;
        Player p = (Player)mainAtk;
        Mob_in_map mob = (Mob_in_map)focus;
        if (mob != null) {
            // roi do boss co dinh
            short[] id_item_leave3 = new short[]{};
            short[] id_item_leave4 = new short[]{};
            short[] id_item_leave7 = new short[]{};
            //short id_medal_material = -1;
            short sizeRandomMedal = 0;
            switch (mob.template.mob_id) {
                case 133: {
                    long vang = 10000;
                    int ngoc = 100;
                    int coin = 100;
                    p.update_vang(vang);
                    p.update_ngoc(ngoc);
                    p.update_coin(coin);
                    Service.send_notice_nobox_white(p.conn,"Bạn nhận được: "+vang+" vàng, "+ngoc+" ngọc và "+coin+" coin.");
                }
            }
            for (short id : id_item_leave3) {
                ItemTemplate3 temp = ItemTemplate3.item.get(id);
                LeaveItemMap.leave_item_by_type3(map, id, temp.getColor(), p, temp.getName(), mob.index);
            }
            for (int i = 0; i < 3; i++) {
                for (short id : id_item_leave4) {
                    if (id == -1) {
                        leave_vang(map, mob, p);
                    } else {
                        LeaveItemMap.leave_item_by_type4(map, id, p, mob.index,p.index);
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                for (short id : id_item_leave7) {
                    LeaveItemMap.leave_item_by_type7(map, id, p, mob.index, p.index);
                }
            }
            for (int l = 0; l < sizeRandomMedal; l++) {
                LeaveItemMap.leave_item_by_type7(map, (short) Util.random(136, 146), p, mob.index,p.index);
            }
        }
    }
}
