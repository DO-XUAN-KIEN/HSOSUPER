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
import template.ItemTemplate3;
import template.MainObject;
import template.Horse;
import template.Member_ChienTruong;
import BossHDL.*;

public class ChienTruong {
    private static ChienTruong instance;
    private HashMap<String, Member_ChienTruong> list;
    private List<Member_ChienTruong> BXH;
    private int status; // 0 sleep, 1 : register, 2 : start
    private int time;
    public int[] info_house;
    public List<Player_Nhan_Ban> list_ai;
    public List<Mob_in_map> boss;
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

    public static ChienTruong gI() {
        if (instance == null) {
            instance = new ChienTruong();
            instance.init();
        }
        return instance;
    }

    private void init() {
        this.list = new HashMap<>();
        this.BXH = new ArrayList<>();
        this.status = 0;
        this.time = 0;
        this.vang = 0;
        this.ngoc = 0;
        list_ai = Player_Nhan_Ban.init();
        this.boss = new ArrayList<>();
        //
    }

    public synchronized void update() {
        try {
            if (this.status == 1) {
                this.time--;
                if (this.time <= 0) {
                    this.start();
                }
            } else if (this.status == 2) {
                this.time--;
                if(this.time == 60* 55){
                    BossManager.callBossToMap(61, 101, 487,510 , 250_000_000, 55);
                    Manager.gI().chatKTGprocess("Xà nữ đã xuất hiện tại chiến trường");
                }else if(this.time == 60 * 50){
                    BossManager.callBossToMap(61, 101, 487,510, 250_000_000, 55);
                    Manager.gI().chatKTGprocess("Xà nữ đã xuất hiện tại chiến trường");
                }
                //
                if (this.time <= 0) {
                    this.finish();
                }
            }
        } catch (IOException e) {
        }
    }
    private void create_boss(int i) {
//		if (i == 20) {
//			Mob_in_map m = null;
//			for (int j = 0; j < boss.size(); j++) {
//				if (boss.get(j).is_boss_active) {
//					m = boss.get(j);
//					break;
//				}
//			}
//			if (m == null) {
//				int index = Util.random(boss.size());
//				if (!boss.get(index).is_boss_active && boss.get(index).level == 10) {
//					boss.get(index).level = 100;
//					boss.get(index).is_boss_active = true;
//				}
//				try {
//					Manager.gI().chatKTGprocess(" Xà nữ xuất hiện tại chiến trường.");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			} else {
//				for (int j = 0; j < boss.size(); j++) {
//					if (!boss.get(j).equals(m) && boss.get(j).zone_id == m.zone_id) {
//						boss.get(j).level = 100;
//						boss.get(j).is_boss_active = true;
//						break;
//					}
//				}
//				try {
//					Manager.gI().chatKTGprocess(" Xà nữ xuất hiện tại chiến trường.");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		} else {
//			int index = Util.random(boss.size());
//			if (!boss.get(index).is_boss_active && boss.get(index).level == 10) {
//				boss.get(index).level = 50;
//				boss.get(index).is_boss_active = true;
//			}
//			try {
//				Manager.gI().chatKTGprocess(" Xà nữ xuất hiện tại chiến trường.");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
    }

    private void start() throws IOException {
        if (this.status == 1) {
            Manager.gI().chatKTGprocess("Chiến trường bắt đầu , cố gắng lấy nhiều nguyên liệu, điểm nhất nào");
            this.BXH.clear();
            this.status = 2;
            this.time = 60 * 60;
            int init_house = this.list.size() / 40;
            init_house = init_house > 0 ? init_house : 10;
            this.info_house = new int[] {init_house, init_house, init_house, init_house
                    // Map.get_map_by_id(56)[0].maxzone, Map.get_map_by_id(60)[0].maxzone,
                    // Map.get_map_by_id(58)[0].maxzone, Map.get_map_by_id(54)[0].maxzone
            };
            //
            List<Member_ChienTruong> list = new ArrayList<>();
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
                list.add(en.getValue());
            }
            Collections.shuffle(list);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).village = ((i % 4) + 2);
            }
            if (list.size() == 1) {
                list.get(0).village = Util.random(4) + 2;
            }
            if (list.size() < 4) {
                for (int i = 0; i < list.size(); i++) {
                    this.info_house[list.get(i).village - 2]++;
                }
                for (int i = 0; i < this.info_house.length; i++) {
                    this.info_house[i]--;
                }
            }
            // for (int mem : this.info_p) {
            // System.out.print(mem + " ");
            // }
            // System.out.println();
            list.clear();
            //
            byte[] id_map = new byte[] {54, 56, 58, 60};
            for (int i = 0; i < id_map.length; i++) {
                Map[] mapp = Map.get_map_by_id(id_map[i]);
                for (Map map : mapp) {
                    for (Mob_in_map mobb : map.mobs) {
                        mobb.isDie = false;
                    }
                }
            }
            for (Player_Nhan_Ban temp : this.list_ai) {
                temp.isdie = false;
                temp.hp = temp.hp_max;
            }
            //
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
                // System.out.println(en.getKey() + " " + en.getValue().village);
                Player p0 = Map.get_player_by_name(en.getKey());
                if (p0 != null) {
                    Vgo vgo = new Vgo();
                    switch (en.getValue().village) {
                        case 2: { // lang gio
                            vgo.id_map_go = 55;
                            vgo.x_new = 224;
                            vgo.y_new = 256;
                            MapService.change_flag(p0.map, p0, 2);
                            break;
                        }
                        case 3: { // lang lua
                            vgo.id_map_go = 59;
                            vgo.x_new = 240;
                            vgo.y_new = 224;
                            MapService.change_flag(p0.map, p0, 1);
                            break;
                        }
                        case 4: { // lang set
                            vgo.id_map_go = 57;
                            vgo.x_new = 264;
                            vgo.y_new = 272;
                            MapService.change_flag(p0.map, p0, 4);
                            break;
                        }
                        default: { // 5 lang anh sang
                            vgo.id_map_go = 53;
                            vgo.x_new = 276;
                            vgo.y_new = 246;
                            MapService.change_flag(p0.map, p0, 5);
                            break;
                        }
                    }
                    p0.change_map(p0, vgo);
                }
            }
        }
    }

    public synchronized void update_house_die(short id) throws IOException {
        switch (id) {
            case 89: { // nha set type 4
                if (this.info_house[2] > 0) {
                    this.info_house[2]--;
                }
                break;
            }
            case 90: { // nha gio type 2
                if (this.info_house[0] > 0) {
                    this.info_house[0]--;
                }
                break;
            }
            case 91: { // nha anh sang type 5
                if (this.info_house[3] > 0) {
                    this.info_house[3]--;
                }
                break;
            }
            case 92: { // nha lua type 3
                if (this.info_house[1] > 0) {
                    this.info_house[1]--;
                }
                break;
            }
        }
        for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
            Player p0 = Map.get_player_by_name(en.getKey());
            if (p0 != null) {
                ChienTruong.gI().send_info(p0);
            }
        }
        int dem = 4;
        for (int i = 0; i < info_house.length; i++) {
            if (info_house[i] <= 0) {
                finish_house(i + 2);
                dem--;
            }
        }
        if (dem <= 1) {
            this.time = 5;
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
                Player p0 = Map.get_player_by_name(en.getKey());
                if (p0 != null && Map.is_map_chien_truong(p0.map.map_id)) {
                    p0.update_point_arena(1);
                    ChienTruong.gI().send_info(p0);
                    this.update_time(p0);
                }
            }
            // try {
            // this.finish();
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
        }
    }

    private void finish_house(int i) {
        try {
            List<String> list_remove = new ArrayList<>();
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
                if (en.getValue().village == i) {
                    list_remove.add(en.getKey());
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
            }
            list_remove.forEach(l -> {
                this.list.remove(l);
            });
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
                Player p0 = Map.get_player_by_name(en.getKey());
                if (p0 != null && Map.is_map_chien_truong(p0.map.map_id)) {
                    ChienTruong.gI().send_info(p0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finish() throws IOException {
        if (this.status == 2) {
            Manager.gI().chatKTGprocess("Chiến trường kết thúc, hẹn gặp lại vào ngày mai");
            //
//			List<Player> list_receiv = new ArrayList<>();
//			for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
//				Player p0 = Map.get_player_by_name(en.getKey());
//				if (p0 != null && Map.is_map_chien_truong(p0.map.map_id)) {
//					list_receiv.add(p0);
//				}
//			}
//			long vang_re = this.vang / (list_receiv.size() < 0 ? 1 : list_receiv.size());
//			long ngoc_re = this.ngoc / (list_receiv.size() < 0 ? 1 : list_receiv.size());
//			list_receiv.forEach(l -> {
//				if (l.conn != null) {
//					l.update_vang(vang_re);
//					l.update_ngoc(ngoc_re);
//					try {
//						l.item.char_inventory(5);
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//			this.vang = 0;
//			this.ngoc = 0;
//			list_receiv.clear();
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
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
//			for (int i = 0; i < boss.size(); i++) {
//				boss.get(i).level = 10;
//				boss.get(i).hp = 0;
//				boss.get(i).is_boss_active = false;
//				boss.get(i).isdie = true;
//			}
            for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
                this.BXH.add(en.getValue());
            }
            this.BXH.sort(new Comparator<Member_ChienTruong>() {
                @Override
                public int compare(Member_ChienTruong o1, Member_ChienTruong o2) {
                    return o1.point > o2.point ? -1 : 1;
                }
            });
            //
            this.list.clear();
            this.status = 0;
            this.time = 0;
            this.info_house = null;
        }
    }

    public synchronized void register(Player p) throws IOException {

        if (this.list.containsKey(p.name)) {
            Service.send_notice_box(p.conn, "Đã đăng ký rồi");
        } else {
            Member_ChienTruong temp = new Member_ChienTruong();
            temp.name = p.name;
            temp.point = 0;
            temp.village = 0;
            temp.received = false;
            this.list.put(p.name, temp);
            Service.send_notice_box(p.conn, "Đăng ký thành công");
        }
    }

    public synchronized void open_register() throws IOException {
        if (this.status == 0) {
            Manager.gI().chatKTGprocess("Chiến trường mở đăng ký, mau mau đến ");
            this.status = 1;
            this.time = 60*44;
        }
    }

    public void send_info(Player p) throws IOException {
        this.update_time(p);
        for (int i = 2; i < 6; i++) {
            Message m = new Message(-94);
            m.writer().writeByte(i); //
            m.writer().writeByte(this.info_house[i - 2]); // total house
            m.writer().writeShort(total_p_of_house(i)); // total p
            m.writer().writeByte(1);
            p.conn.addmsg(m);
            m.cleanup();
        }
    }
    private int total_p_of_house(int i) {
        int result = 0;
        for (Entry<String, Member_ChienTruong> en : this.list.entrySet()) {
            if (en.getValue().village == i) {
                Player p0 = Map.get_player_by_name(en.getKey());
                if (p0 != null && Map.is_map_chien_truong(p0.map.map_id)) {
                    result++;
                }
            }
        }
        return result;
    }

    private void update_time(Player p) throws IOException {
        Message m = new Message(-94);
        m.writer().writeByte(-1); //
        m.writer().writeByte(0);
        m.writer().writeShort(0);
        m.writer().writeByte(0);
        m.writer().writeLong(System.currentTimeMillis() - (60 * 60 - this.time) * 1000L);
        p.conn.addmsg(m);
        m.cleanup();
    }

    public void get_ai(Player p, int id) throws IOException {
        for (int i = 0; i < this.list_ai.size(); i++) {
            Player_Nhan_Ban temp = this.list_ai.get(i);
            if (temp.id == ((short) id)) {
                Message m = new Message(5);
                m.writer().writeShort(temp.id);
                m.writer().writeUTF("Lính canh");
                m.writer().writeShort(temp.x);
                m.writer().writeShort(temp.y);
                m.writer().writeByte(Util.random(0, 4)); // clazz
                m.writer().writeByte(126);
                m.writer().writeByte(1); // head
                m.writer().writeByte(1); // eye
                m.writer().writeByte(Util.random(32, 34)); // hair
                m.writer().writeShort(40); // level
                m.writer().writeInt(temp.hp); // hp
                m.writer().writeInt(temp.hp_max); // hp max
                m.writer().writeByte(0); // type
                m.writer().writeShort(0); // point pk
                m.writer().writeByte(3); // size part
                //
                byte[] part_ = new byte[] {8, 0, 1};
                for (int j = 0; j < 3; j++) {
                    m.writer().writeByte(part_[j]);
                    m.writer().writeByte(0);
                    m.writer().writeByte(3);
                    m.writer().writeShort(-1);
                    m.writer().writeShort(-1);
                    m.writer().writeShort(-1);
                    m.writer().writeShort(-1); // eff
                }
                //
                m.writer().writeShort(-1); // clan
                m.writer().writeByte(-1); // pet
                m.writer().writeByte(7);
                for (int i1 = 0; i1 < 7; i1++) {
                    if (p.conn.version >= 280) {
                        m.writer().writeShort(-1);
                    } else {
                        m.writer().writeByte(-1);
                    }
                }
                //
                m.writer().writeShort(-1);
                m.writer().writeByte(Horse.SU_TU); // type use mount
                m.writer().writeBoolean(false);
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                m.writer().writeShort(-1); // mat na
                m.writer().writeByte(1); // paint mat na trc sau
                m.writer().writeShort(-1); // phi phong
                m.writer().writeShort(-1); // weapon
                m.writer().writeShort(-1);
                m.writer().writeShort(-1); // hair
                m.writer().writeShort(-1); // wing
                m.writer().writeShort(-1); // body
                m.writer().writeShort(-1); // leg
                m.writer().writeShort(-1); // bienhinh
                p.conn.addmsg(m);
                m.cleanup();
                break;
            }
        }
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
                case 93, 94, 95, 96, 97, 98, 99, 100: {
                    if(Util.random(100)>90){
                        id_item_leave7 =new short[]{(short) Util.random(126, 136)};
                    }
                    if(Util.random(10000) < 1 ){
                        id_item_leave3 = new short[]{(short) Util.random(4577, 4585)};
                    }
                    if (Manager.gI().event == 11){
                        if (Util.random(100) < 5){
                            id_item_leave4 = new short[]{337};
                        }
                    }
                    p.update_point_arena(1);
                    p.item.char_inventory(5);
                    break;
                }
                case 89,90,91,92: {
                    id_item_leave7 = new short[]{(short) Util.random(126, 146)};
                    if(Util.random(5000) < 1){
                        id_item_leave3 = new short[]{(short) Util.random(4577, 4585)};
                    }
                    if (Manager.gI().event == 11){
                        id_item_leave4 = new short[]{337};
                    }
                    p.update_point_arena(5);
                    p.item.char_inventory(5);
                    break;
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
    public Member_ChienTruong get_infor_register(String name) {
        return this.list.get(name);
    }

    public Member_ChienTruong get_bxh(String name) {
        for (int i = 0; i < this.BXH.size(); i++) {
            if (this.BXH.get(i).name.equals(name)) {
                if (i < 10) {
                    return this.BXH.get(i);
                }
            }
        }
        return null;
    }

    public int get_index_bxh(Member_ChienTruong temp) {
        return this.BXH.indexOf(temp);
    }
}
