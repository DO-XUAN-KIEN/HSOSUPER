/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package template;

import client.Pet;
import client.Player;
import core.Log;
import core.Manager;
import core.Service;
import core.Util;
import event_daily.ChiemThanhManager;
import event_daily.ChienTruong;
import io.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import map.Eff_special_skill;
import map.Map;
import map.MapService;
import map.Mob_in_map;
import template.Horse;

/**
 * @author chien
 */
public class MainObject {

    public String name;
    public int hp, mp;
    public int hp_max;
    protected int mp_max;
    public boolean isDie, isATK = true;
    public int index;
    public short x, x_old, y, y_old;
    public short level;
    public short map_id;
    public byte zone_id;
    protected int dame, def;
    public boolean isExp = true;
    public byte color_name;
    public byte typepk;
    public Mob template;
    public long exp;
    public byte clazz;
    public Map map;
    public Kham_template kham;
    public int hieuchien;
    public long offlineTime = 0;
    public long fixedOfflineTime = 0;
    public int expGained = -1;

    public int Set_hpMax(int hp_max) {
        return this.hp_max = hp_max;
    }

    public int Set_Dame(int dame) {
        return this.dame = dame;
    }

    public List<EffTemplate> MainEff;
    protected List<EffTemplate> Eff_me_kham;
    protected List<EffTemplate> Eff_Tinh_Tu;
    protected List<EffTemplate> Eff_Off_line;
    public void update_timeoff(){
        if (Eff_Off_line != null) {
            synchronized (Eff_Off_line) {
                long elapsedTime = 0;
                System.out.println(offlineTime);
                // Tính thời gian người chơi đã offline
                if (isPlayer() && offlineTime > 0) {
                    elapsedTime = System.currentTimeMillis() - offlineTime;
                    offlineTime = 0; // Reset lại thời gian offline sau khi đã áp dụng
                }
                for (int i = Eff_Off_line.size() - 1; i >= 0; i--) {
                    EffTemplate temp = Eff_Off_line.get(i);

                    // Trừ thời gian offline vào thời gian còn lại của hiệu ứng
                    if (elapsedTime > 0) {
                        temp.time -= elapsedTime;
                    }

                    // Nếu là hiệu ứng nhận EXP offline (ID 101)
                    if (temp.id == 101 && isPlayer() && elapsedTime > 0) {
                        // Tính EXP nhận được
                        long timeOfflineMinutes = elapsedTime / 60000; // Chuyển ms -> phút
                        expGained = (int) (timeOfflineMinutes * temp.param); // param1 = tốc độ EXP/phút
                        // Thưởng EXP cho người chơi
                        ((Player) this).addExp(expGained);
                        System.out.println("Bạn nhận được " + expGained + " EXP sau khi offline!");
                        // Nếu hiệu ứng đã hết thời gian, loại bỏ nó
                        if (temp.time <= System.currentTimeMillis()) {
                            Eff_Off_line.remove(i);
                        }
                    } else if (temp.time <= System.currentTimeMillis()) {
                        // Loại bỏ các hiệu ứng khác nếu đã hết thời gian
                        Eff_Off_line.remove(i);
                    }
                }
            }
        }
    }

    public void updateEff() {
        try {
            if (MainEff != null) {
                synchronized (MainEff) {
                    for (int i = MainEff.size() - 1; i >= 0; i--) {
                        EffTemplate temp = MainEff.get(i);
                        if (temp.time <= System.currentTimeMillis()) {
                            MainEff.remove(i);
                            if (isPlayer()) {
                                if (temp.id == -125) {
                                    ((Player) this).set_x2_xp(0);
                                }
                                if (temp.id == 24 || temp.id == 23 || temp.id == 0 || temp.id == 2 || temp.id == 3
                                        || temp.id == 4 || temp.id == StrucEff.NOI_TAI_BANG) {
                                    if (temp.id == 2) {
                                        this.hp += ((Player) this).hp_restore;
                                    }
                                    Service.send_char_main_in4((Player) this);
                                    for (int j = 0; j < ((Player) this).map.players.size(); j++) {
                                        Player p2 = ((Player) this).map.players.get(j);
                                        if (p2 != null && p2.index != this.index) {
                                            MapService.send_in4_other_char(((Player) this).map, p2, (Player) this);
                                        }
                                    }
                                }
                            } else if (isMob()) {
                                if (temp.id == StrucEff.NOI_TAI_BANG) {
                                    for (int j = 0; j < ((Mob_in_map) this).map.players.size(); j++) {
                                        Player p2 = ((Mob_in_map) this).map.players.get(j);
                                        if (p2 != null && p2.index != this.index) {
                                            Service.mob_in4(p2, this.index);
                                        }
                                    }
                                }
                            }
                            continue;
                        }
                        //
                        if (temp.id == 1 && !this.isDie && isPlayer()) {
                            if (((Player) this).time_affect_special_sk < System.currentTimeMillis() && ((Player) this).dame_affect_special_sk > 0) {
                                ((Player) this).time_affect_special_sk = System.currentTimeMillis() + 1000L;
                                this.hp -= ((Player) this).dame_affect_special_sk;
                                Service.usepotion(((Player) this), 0, -((Player) this).dame_affect_special_sk);
                                if (this.hp < 0) {
                                    MapService.Player_Die(((Player) this).map, ((Player) this), ((Player) this), true);
                                }
                            }
                        }
                        // System.out.println(temp.id + " : " + (temp.time - System.currentTimeMillis()));
                    }
                }
            }
            if (Eff_me_kham != null) {
                synchronized (Eff_me_kham) {
                    for (int i = Eff_me_kham.size() - 1; i >= 0; i--) {
                        EffTemplate temp = Eff_me_kham.get(i);
                        if (temp.time <= System.currentTimeMillis()) {
                            Eff_me_kham.remove(i);
                        }
                    }
                }
            }
            if (Eff_Tinh_Tu != null) {
                synchronized (Eff_Tinh_Tu) {
                    for (int i = Eff_Tinh_Tu.size() - 1; i >= 0; i--) {
                        EffTemplate temp = Eff_Tinh_Tu.get(i);
                        if (temp.time <= System.currentTimeMillis()) {
                            Eff_Tinh_Tu.remove(i);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    public void setOfflineTime() {
        this.offlineTime = System.currentTimeMillis();      // Thời gian bắt đầu offline
        if (this.fixedOfflineTime == 0) {                  // Chỉ gắn giá trị khi nó chưa được gán
            this.fixedOfflineTime = this.offlineTime;
        }
    }
    public void resetOfflineTime() {
        this.offlineTime = 0; // Reset thời gian offline
    }
    public void add_EffOffline(int id, int param1, long time_end) {
        if (Eff_Off_line == null) {
            return;
        }
        synchronized (Eff_Off_line) {
            Eff_Off_line.add(new EffTemplate(id, param1, time_end));
        }
    }
    public void addOfflineExpEffect(long durationMillis, int expRatePerMinute) {
        long timeEnd = System.currentTimeMillis() + durationMillis;
        add_EffOffline(101, expRatePerMinute, timeEnd);
    }
    public void addExp(int exp) {
        this.exp += exp; // Cộng thêm EXP vào EXP hiện tại của người chơi
        System.out.println("EXP hiện tại: " + this.exp);
    }
    public void add_EffDefault(int id, int param1, long time_end) {
        if (MainEff == null) {
            return;
        }
        synchronized (MainEff) {
            if (param1 == 0) {
                return;
            }
            for (int i = MainEff.size() - 1; i >= 0; i--) {
                EffTemplate temp_test = MainEff.get(i);
                if (temp_test != null && temp_test.id == id) {
                    MainEff.remove(i);
                }
            }
            MainEff.add(new EffTemplate(id, param1, time_end));
        }
    }

    public void add_EffMe_Kham(int id, int param1, long time_end) {
        if (Eff_me_kham == null) {
            return;
        }
        synchronized (Eff_me_kham) {
            Eff_me_kham.add(new EffTemplate(id, param1, time_end));
        }
    }
    public EffTemplate get_EffDefault(int id) {
        if (MainEff == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        synchronized (MainEff) {
            for (EffTemplate e : MainEff) {
                if (e.id == id && e.time > time) {
                    return e;
                }
            }
        }
        return null;
    }
    public EffTemplate get_OfflineTime(int id) {
        if (Eff_Off_line == null) {
            return null;
        }

        long currentTime = System.currentTimeMillis(); // Lấy thời gian hiện tại
        long elapsedTime = 0;

        // Kiểm tra nếu người chơi đã offline
        if (offlineTime > 0) {
            elapsedTime = currentTime - offlineTime; // Tính thời gian đã offline
        }
        synchronized (Eff_Off_line) {
            for (EffTemplate e : Eff_Off_line) {
                // Điều chỉnh thời gian hiệu ứng theo thời gian offline
                long adjustedTime = e.time - elapsedTime;
                // Kiểm tra điều kiện ID và thời gian hiệu lực
                if (e.id == id && adjustedTime > currentTime) {
                    // Cập nhật thời gian còn lại (nếu cần)
                    e.time = adjustedTime;
                    return e;
                }
            }
        }

        return null;
    }


    public EffTemplate get_EffMe_Kham(int id) {
        if (Eff_me_kham == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        synchronized (Eff_me_kham) {
            for (EffTemplate e : Eff_me_kham) {
                if (e.id == id && e.time > time) {
                    return e;
                }
            }
        }
        return null;
    }

    public boolean isStunes(boolean isAtk) {
        if (MainEff == null) {
            return false;
        }
        long time = System.currentTimeMillis();
        if (get_EffDefault(145) != null){
            return false;
        }
        synchronized (MainEff) {
            for (EffTemplate e : MainEff) {
                if (e.id >= -124 && e.id <= -121 && (!isAtk || e.id != -123) && e.time > time) {
                    return true;
                }
            }
        }
        return false;
    }

    public int get_TypeObj() {
        return 1;
    }

    public int get_HpMax() {
        return hp_max;
    }

    public int get_MpMax() {
        return mp_max;
    }

    public int get_DameBase() {
        return dame;
    }

    public int get_DameProp(int type) {
        return 0;
    }

    public int get_PercentDameProp(int type) {
        return 0;
    }

    public int get_DefBase() {
        return def;
    }

    public int get_PercentDefBase() {
        return 0;
    }

    public int get_PercentDefProp(int type) {
        return 0;
    }

    public boolean isMob() {
        return false;
    }

    public boolean isMoTaiNguyen() {
        return false;
    }

    public boolean isMobDungeon() {
        return false;
    }

    public boolean isMobDiBuon() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isBot() {
        return false;
    }

    public boolean isNhanBan() {
        return false;
    }

    public boolean isMobCTruong() {
        return false;
    }

    public boolean isMobCTruongHouse() {
        return false;
    }

    public boolean isBoss() {
        return false;
    }

    public int get_Pierce() {//xuyên giáp
        return 0;
    }

    public int get_Crit() {
        return 0;
    }

    public int get_PhanDame() {
        return 0;
    }

    public int get_Miss(boolean giam_ne) {
        return 0;
    }

    public void Obj_Fire(Map map, MainObject objFocus, int skill, LvSkill temp) throws IOException {
        // không có thì đưa vào null;
    }

    public void SetDie(Map map, MainObject mainAtk) throws IOException {
        hp = 0;
        isDie = true;
    }

    public EffTemplate getEffTinhTu(int id) {
        long time = System.currentTimeMillis();
        synchronized (Eff_Tinh_Tu) {
            for (EffTemplate e : Eff_Tinh_Tu) {
                if (e.id == id && e.time > time) {
                    return e;
                }
            }
        }
        return null;
    }

    public void removeEffTinhTu(EffTemplate eff) {
        synchronized (Eff_Tinh_Tu) {
            Eff_Tinh_Tu.remove(eff);
        }
    }

    public void addEffTinhTu(int id, int param1, long time_end) {
        synchronized (Eff_Tinh_Tu) {
            Eff_Tinh_Tu.add(new EffTemplate(id, param1, System.currentTimeMillis() + time_end));
        }
    }

    public static void sendEffTinhTu(Player p, int id, int time) throws IOException {
        Message m = new Message(-49);
        m.writer().writeByte(2);
        m.writer().writeShort(0);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeByte(id);
        m.writer().writeShort(p.index);
        m.writer().writeByte(0);
        m.writer().writeByte(0);
        m.writer().writeInt(time);
        MapService.send_msg_player_inside(p.map, p, m, true);
        m.cleanup();
    }

    public static void MainAttack(Map map, MainObject ObjAtk, MainObject focus, int idxSkill, LvSkill temp, int type) {
        try {
            // pvp, pve, mob chiến trường, mob chiếm thành, nhân bản boss, (không đánh mob đi buôn)

            //<editor-fold defaultstate="collapsed" desc="... không thể tấn công    ...">
            if (ObjAtk == null || focus == null || ObjAtk.equals(focus) || ObjAtk.isDie || ObjAtk.isStunes(true)) {
                return;
            }
            if (focus.isMobDiBuon() && ObjAtk.isPlayer() && ((Player) ObjAtk).isTrader() && ((Pet_di_buon) focus).type == 131) {
                return;
            }
            if (focus.isMobDiBuon() && ObjAtk.isPlayer() && ((Player) ObjAtk).isRobber() && ((Pet_di_buon) focus).type == 132) {
                return;
            }
            if((ObjAtk.level - focus.level >= 10 || ObjAtk.level - focus.level <= -10) && map.zone_id == map.maxzone){
                return;
            }
            if ((focus.level - ObjAtk.level >=10 || focus.level - ObjAtk.level <= -10) && map.zone_id == map.maxzone){
                return;
            }
            if (ObjAtk.isPlayer() && focus.isPlayer() && map.zone_id == 1 && !Map.is_map_not_zone2(map.map_id) && ((Player) ObjAtk).conn.ac_admin < 66) {
                return;
            }
            if (ObjAtk.isPlayer() && focus.isPlayer() && !map.isMapChiemThanh() && (map.ismaplang || ObjAtk.level < 5 || focus.level < 5
                    || (ObjAtk.typepk != 0 && ObjAtk.typepk == focus.typepk) || ObjAtk.hieuchien > 320_000) && ((Player) ObjAtk).conn.ac_admin < 66) {
                return;
            }
            if (focus.isMob() && focus.template.mob_id == 152 && !ChiemThanhManager.isDameTruChinh(map)) {
                return;
            }
            if (Math.abs(ObjAtk.x - focus.x) > 300 || Math.abs(ObjAtk.y - focus.y) > 300) {
                return;
            }
            if (ObjAtk.isStunes(true)) {
                return;
            }
            if (focus.isPlayer() && focus.get_EffMe_Kham(StrucEff.TangHinh) != null) {
                return;
            }
            if (ObjAtk.isPlayer() && ObjAtk.get_EffMe_Kham(StrucEff.LuLan) != null) {
                return;
            }
            if (map.map_id == 102 && map.kingCupMap != null && map.kingCupMap.timeWait > System.currentTimeMillis()) {
                return;
            }
            if (ObjAtk.isPlayer() && (focus.isDie || focus.hp <= 0)) {
                if (focus.isPlayer()) {
                    MapService.Player_Die(map, (Player) focus, ObjAtk, false);
                } else {
                    MapService.MainObj_Die(map, ((Player) ObjAtk).conn, focus, false);
                }
                return;
            }
            if (ObjAtk.isPlayer() && focus.isPlayer() && focus.typepk == -1)// đồ sát
            {
                if (ObjAtk.hieuchien > 10000) {
                    Service.send_notice_box(((Player) ObjAtk).conn, "Không thể đồ sát quá nhiều, cần tẩy điểm trước.");
                    return;
                }
                if (focus.get_EffDefault(-126) != null) {
                    Service.send_notice_box(((Player) ObjAtk).conn, "Đối phương có hiệu ứng chống pk");
                    return;
                }
                if (map.zone_id == 1 && !Map.is_map_not_zone2(map.map_id) && ((Player) ObjAtk).conn.ac_admin < 66) {
                    return;
                }
                if (((Player) focus).pet_follow == 4708 && !map.isMapChiemThanh()){
                    Service.send_notice_box(((Player) ObjAtk).conn, "Đối phương đang được pet bảo vệ");
                    return;
                }
            }
            //</editor-fold>

            Player p = ObjAtk.isPlayer() ? (Player) ObjAtk : null;

            if (focus.isPlayer() && ObjAtk.isPlayer() && focus.getEffTinhTu(EffTemplate.BAT_TU) != null && p != null) {
                Service.send_notice_nobox_white(p.conn, "Đối phương đang trong trạng thái bất tử");
                return;
            }
            if (p != null && p.getEffTinhTu(EffTemplate.MU_MAT) != null) {
                Service.send_notice_nobox_white(p.conn, "Bạn đang bị mù mắt");
                MapService.Fire_Player(map, ((Player) ObjAtk).conn, idxSkill, focus.index, 0, focus.hp, new ArrayList<>(), (byte) 11, 0);
                return;
            }
            boolean giam_ne = p != null && p.isEffTinhTu(99);
            if (focus.get_Miss(giam_ne) > Util.random(10_000)) {
                if (ObjAtk.isPlayer()) {
                    MapService.Fire_Player(map, ((Player) ObjAtk).conn, idxSkill, focus.index, 0, focus.hp, new ArrayList<>(), (byte) 11, 0);
                }
                return;
            }
            if (p != null && ((idxSkill == 19 && p.skill_110[0] >= 1) || (idxSkill == 20 && p.skill_110[1] >= 1))) {
                send_eff_to_object(p, focus, p.get_id_eff_skill(idxSkill));
            }
//        if (p != null && (idxSkill == 3 || idxSkill == 4)) {
//            send_eff_to_object(p, focus, 56);
//        }
            EffTemplate ef;
            long dame = ObjAtk.get_DameBase();
            int hutHP = 0;
            float ptCrit = 0;
            float DamePlus = 0;
            float GiamDame = 0;
            float ptxuyengiap = 0;
            boolean xuyengiap = ObjAtk.get_Pierce() > Util.random(10_000);
            int noitai = -1;
            //<editor-fold defaultstate="collapsed" desc="Get Dame default...">
            byte type_dame = 2;
            if (type == 0) {
                int tempDameProp = ObjAtk.get_DameProp(0);
                int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(16));
                dame += dameProp < 0 ? 0 : dameProp;
                if (Util.random(10000) < 1000) {
                    noitai = 0;
                }
            } else if (type == 1) {
                switch (ObjAtk.clazz) {
                    case 0: {
                        type_dame = 0;
                        int tempDameProp = ObjAtk.get_DameProp(2);
                        int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(18));
                        dame += dameProp < 0 ? 0 : dameProp;
                        if (Util.random(10000) < 1000) {
                            noitai = 2;
                        }
                        break;
                    }
                    case 1: {
                        type_dame = 1;
                        int tempDameProp = ObjAtk.get_DameProp(4);
                        int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(20));
                        dame += dameProp < 0 ? 0 : dameProp;
                        if (Util.random(10000) < 1000) {
                            noitai = 4;
                        }
                        break;
                    }
                    case 2: {
                        type_dame = 3;
                        int tempDameProp = ObjAtk.get_DameProp(1);
                        int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(17));
                        dame += dameProp < 0 ? 0 : dameProp;
                        if (Util.random(10000) < 1000) {
                            noitai = 1;
                        }
                        break;
                    }
                    case 3: {
                        type_dame = 4;
                        int tempDameProp = ObjAtk.get_DameProp(3);
                        int dameProp = tempDameProp - (int) (xuyengiap ? 0 : tempDameProp * 0.0001 * focus.get_PercentDefProp(19));
                        dame += dameProp < 0 ? 0 : dameProp;
                        if (Util.random(10000) < 1000) {
                            noitai = 3;
                        }
                        break;
                    }
                }
            } else {
                dame += ObjAtk.get_DameProp(0);
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Skill...">
            if (ObjAtk.isPlayer()) {
                if (idxSkill == 19 && ObjAtk.clazz == 1) {
                    for (Option op : temp.minfo) {
                        if (op.id == 4) {
                            dame += op.getParam(0);
                        }
                        if (op.id == 11) {
                            dame += dame * (op.getParam(0) / 100) / 100;
                        }
                    }
                } else {
                    for (int i = temp.minfo.length - 1; i >= 0; i--) {
                        Option op = temp.minfo[i];
                        if (type == 0) {
                            if (op.id == 0) {
                                dame += op.getParam(0);
                            }
                            if (op.id == 7) {
                                dame += dame * (op.getParam(0) / 100) / 100;
                            }
                        } else {
                            if (op.id == 1 || op.id == 2 || op.id == 3 || op.id == 4) {
                                dame += op.getParam(0);
                            }
                            if (op.id == 9 || op.id == 10 || op.id == 11 || op.id == 8) {
                                dame += dame * (op.getParam(0) / 100) / 100;
                            }
                        }
                    }
                }
                if (p.get_EffDefault(StrucEff.NOI_TAI_DIEN) != null) {
                    dame -= dame / 5;
                }
                if (map.mapsk == true){
                    dame -= dame /2;
                }
                if (p.get_EffDefault(StrucEff.Quà_noel) != null && p.qua_noel == 3){
                    dame -= dame*1.15;
                }
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ngựa...">
            if (ObjAtk.isPlayer()) {
                if (p.get_EffDefault(EffTemplate.buffdame) != null || p.get_EffDefault(EffTemplate.bufftatca) != null){
                    DamePlus += 0.2;
                }
                if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
                    DamePlus += 0.3;
                }
                if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
                    DamePlus += 0.3;
                }
                if (p.type_use_mount == Horse.NGUA_XICH_THO) {
                    DamePlus += 0.2;
                } else if (p.type_use_mount == Horse.TUAN_LOC ) {
                    DamePlus += 0.4;
                } else if (p.type_use_mount == Horse.HEO_RUNG || p.type_use_mount == Horse.CON_LAN || p.type_use_mount == Horse.CA_CHEP) {
                    DamePlus += 0.1;
                } else if (p.type_use_mount == Horse.TRAU_RUNG || p.type_use_mount == Horse.MA_TOC_DO
                        || p.type_use_mount == Horse.PHUONG_HOANG_LUA) {
                    DamePlus += 0.15;
                } else if ((p.type_use_mount == Horse.HOA_KY_LAN)) {
                    DamePlus += 0.35;
                }
            }
            //</editor-fold>
            List<Float> giamdame = new ArrayList<>();
            ef = ObjAtk.get_EffDefault(3);
            if (ef != null) {
                giamdame.add((float) 0.2);
                GiamDame += 0.2;
            }
            if (ObjAtk.isPlayer() && p.getlevelpercent() < 0) {
                giamdame.add((float) 0.5);
                GiamDame += 0.5;
            }

            //<editor-fold defaultstate="collapsed" desc="Nộ cánh...">
            if (ObjAtk.isPlayer()) {
                EffTemplate temp2 = p.get_EffDefault(StrucEff.PowerWing);
                if (temp2 == null) {
                    Item3 it = p.item.wear[10];
                    if (it != null) {
                        int percent = 0;
                        int time = 0;
                        for (Option op : it.op) {
                            if (op.id == 41) {
                                percent = op.getParam(it.tier);
                            } else if (op.id == 42) {
                                time = op.getParam(it.tier);
                            }
                        }
                        if (percent > Util.random(10_000)) {
                            //
                            p.add_EffDefault(StrucEff.PowerWing, 1000, time);
                            //
                            Message mw = new Message(40);
                            mw.writer().writeByte(0);
                            mw.writer().writeByte(1);
                            mw.writer().writeShort(ObjAtk.index);
                            mw.writer().writeByte(21);
                            mw.writer().writeInt(time);
                            mw.writer().writeShort(ObjAtk.index);
                            mw.writer().writeByte(0);
                            mw.writer().writeByte(30);
                            byte[] id__ = new byte[]{7, 8, 9, 10, 11, 15, 0, 1, 2, 3, 4, 14};
                            int[] par__ = new int[]{3000, 3000, 3000, 3000, 3000, 3000,
                                3 * (ObjAtk.get_param_view_in4(0) / 10), 3 * (ObjAtk.get_param_view_in4(1) / 10),
                                3 * (ObjAtk.get_param_view_in4(2) / 10), 3 * (ObjAtk.get_param_view_in4(3) / 10),
                                3 * (ObjAtk.get_param_view_in4(4) / 10), 33 * (ObjAtk.get_param_view_in4(14) / 10)};
                            mw.writer().writeByte(id__.length);
                            //
                            for (int i = 0; i < id__.length; i++) {
                                mw.writer().writeByte(id__[i]);
                                mw.writer().writeInt(par__[i]);
                            }
                            //
                            MapService.send_msg_player_inside(p.map, p, mw, true);
                            mw.cleanup();
                        }
                    }
                }
            }

            //</editor-fold>
            ef = ObjAtk.get_EffDefault(53);
            int hpmax = ObjAtk.get_HpMax();
            int HoiHP = 0;
            if (ef != null && ObjAtk.hp < hpmax) {
                HoiHP += hpmax / 100;
            }

            //<editor-fold defaultstate="collapsed" desc="Tác dụng mề...">
            boolean isEffKhaiHoan = focus.isPlayer() && focus.get_EffMe_Kham(StrucEff.NgocKhaiHoan) != null;
            int prMeday = 0;
            if (focus.isPlayer()) {
                giamdame.add((float) (((Player) focus).total_item_param(80) * 0.0001));
            }
//        GiamDame += focus.isPlayer() ? (float) (((Player) focus).total_item_param(80) * 0.0001) : 0;//giáp hắc ám
            if (ObjAtk.isPlayer()) {
                if ((ef = ObjAtk.get_EffMe_Kham(StrucEff.TangHinh)) == null && ObjAtk.total_item_param(82) > Util.random(10_000)) {
                    ObjAtk.add_EffMe_Kham(StrucEff.TangHinh, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(81)));
                    Eff_special_skill.send_eff_TangHinh(p, 81, prMeday);
                } else if ((ef = ObjAtk.get_EffMe_Kham(StrucEff.KhienMaThuat)) == null && ObjAtk.total_item_param(85) > Util.random(10_000)) {
                    ObjAtk.add_EffMe_Kham(StrucEff.KhienMaThuat, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(86)));
                    Eff_special_skill.send_eff_Meday(p, 86, prMeday);
                }
            }
            if (focus.isPlayer() && !isEffKhaiHoan) {
                if (focus.get_EffMe_Kham(StrucEff.BongLua) == null && ObjAtk.total_item_param(76) > Util.random(10_000)) {
                    focus.add_EffMe_Kham(StrucEff.BongLua, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(77)));
                    Eff_special_skill.send_eff_Meday((Player) focus, 77, prMeday);
                } else if (focus.get_EffMe_Kham(StrucEff.BongLanh) == null && ObjAtk.total_item_param(78) > Util.random(10_000)) {
                    focus.add_EffMe_Kham(StrucEff.BongLanh, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(79)));
                    Eff_special_skill.send_eff_Meday((Player) focus, 79, prMeday);
                } else if (focus.get_EffMe_Kham(StrucEff.LuLan) == null && ObjAtk.total_item_param(87) > Util.random(10_000)) {
                    focus.add_EffMe_Kham(StrucEff.LuLan, 0, System.currentTimeMillis() + (prMeday = ObjAtk.total_item_param(88)));
                    Eff_special_skill.send_eff_Meday((Player) focus, 88, prMeday);
                }
                if (focus.get_EffMe_Kham(StrucEff.KhienMaThuat) != null) {
                    GiamDame += 0.5;
                    giamdame.add((float) 0.5);
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Tác dụng khảm...">
            int prKham = 0;
            if (focus.isPlayer() && (ObjAtk.isBoss() || ObjAtk.get_TypeObj() == 0)) {
                if (!isEffKhaiHoan && (prKham = focus.total_item_param(101)) > 0) {
                    if (focus.kham.idAtk_KH == ObjAtk.index) {
                        focus.kham.CountAtk_KH++;
                    } else {
                        focus.kham.idAtk_KH = ObjAtk.index;
                        focus.kham.CountAtk_KH = 1;
                    }

                    if (focus.kham.CountAtk_KH >= prKham) {
                        focus.kham.idAtk_KH = 0;
                        focus.kham.CountAtk_KH = 0;
                        focus.add_EffMe_Kham(StrucEff.NgocKhaiHoan, 0, System.currentTimeMillis() + 3000);
                        Eff_special_skill.send_eff_kham((Player) focus, StrucEff.NgocKhaiHoan, 3000);
                    }
                }

                if ((ef = focus.get_EffMe_Kham(StrucEff.NgocLucBao)) != null) {
                    hutHP += (int) (dame * 0.1);
                } else if ((prKham = focus.total_item_param(102)) > Util.random(10000)) {
                    focus.add_EffMe_Kham(StrucEff.NgocLucBao, prKham, System.currentTimeMillis() + 3000);
                    Eff_special_skill.send_eff_kham((Player) focus, StrucEff.NgocLucBao, 3000);
                }
            }

            if (ObjAtk.isPlayer()) {
                if ((focus.isBoss() || focus.get_TypeObj() == 0)) {
                    if (ObjAtk.get_EffMe_Kham(StrucEff.NgocHonNguyen) != null) {
                        DamePlus += 1;
                    }
                }
                double ptHP = (ObjAtk.hp / ObjAtk.get_HpMax()) * 100;
                if ((ef = ObjAtk.get_EffMe_Kham(StrucEff.NgocPhongMa)) != null) {
                    HoiHP += (int) (hpmax * ef.param * 0.0001);
                } else if (ptHP < ObjAtk.total_item_param(104) / 100 && (prKham = ObjAtk.total_item_param(103)) > Util.random(10_000)) {
                    ObjAtk.add_EffMe_Kham(StrucEff.NgocPhongMa, prKham, System.currentTimeMillis() + 5000);
                    Eff_special_skill.send_eff_kham(p, StrucEff.NgocPhongMa, 5000);
                }
                if (focus.isBoss() && (ef = ObjAtk.get_EffMe_Kham(StrucEff.NgocSinhMenh)) != null) {
                    DamePlus += 0.5;
//                dame += (long)(dame * 0.5); 
                } else if (focus.isBoss() && (prKham = ObjAtk.total_item_param(106)) > Util.random(10_000)) {
                    ObjAtk.add_EffMe_Kham(StrucEff.NgocSinhMenh, prKham, System.currentTimeMillis() + 3000);
                    Eff_special_skill.send_eff_kham(p, StrucEff.NgocSinhMenh, 3000);
                }
                if (p != null && focus.isPlayer() && (prKham = p.total_item_param(118)) > Util.random(10_000)){
                    int time = prKham / 1000;
                    //ObjAtk.add_EffMe_Kham(StrucEff.Ngoctraubo, prKham, System.currentTimeMillis() + 5000);
                    p.add_EffDefault(143, 1, time * 1000);
                    Eff_special_skill.send_eff_kham(p, StrucEff.Ngoctraubo, time * 1000);
                    //Service.send_time_box(p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Trâu bò"});
                }
                if (ObjAtk.get_EffDefault(StrucEff.Ngoccuongbao) != null &&
                        ObjAtk.total_item_param(119) > Util.random(10_000)){
                    p.cuong_bao += 1;
                }else if (p != null && focus.isPlayer() && (prKham = p.total_item_param(119)) > Util.random(10_000)){
                    p.cuong_bao = 1;
                    p.add_EffDefault(144,1,8* 1000);
                    Eff_special_skill.send_eff_kham(p,StrucEff.Ngoccuongbao,8 * 1000);
                    Service.send_notice_nobox_white(p.conn,"Cuồng bạo");
                }
                else if (ObjAtk.get_EffDefault(StrucEff.Ngoccuongbao) == null){
                    p.cuong_bao = 0;
                }
                ptCrit += ObjAtk.total_item_param(107) * 0.0001;
                ptxuyengiap += ObjAtk.total_item_param(117) * 0.0001;
            }
            //</editor-fold>

            dame += dame * DamePlus;

            int def = focus.get_DefBase();
//        def += def * focus.get_PercentDefBase() * 0.0001;
//        if (ObjAtk.isPlayer()) {
//            System.out.println("dame:   " + Util.number_format(dame)+"  def:   " +Util.number_format(def) + "   giam: "+GiamDame);
//        }
            if (dame > 2_000_000_000) {
                dame = 2_000_000_000;
            }
            dame -= dame * 0.35;
            dame -= (xuyengiap ? 0 : def);
            if (!giamdame.isEmpty()) {
                for (float f : giamdame) {
                    dame -= dame * f;
                }
            }

            if (ObjAtk.isPlayer() && focus.isMob()) {
                boolean check_mob_roi_ngoc_kham = focus.template.mob_id >= 167 && focus.template.mob_id <= 172;
                if (check_mob_roi_ngoc_kham) {
                    if (50 > Util.random(100)) {
                        dame = 0;
                    } else {
                        dame = 1;
                    }
                }
                boolean check = (dame < 0
                        || (focus.isBoss() && Math.abs(focus.level - ObjAtk.level) >= 5 && focus.level < 139 && focus.template.mob_id != 190
                        && focus.template.mob_id != 178 && focus.template.mob_id != 23 && focus.template.mob_id != 51 && focus.template.mob_id != 52
                        && focus.template.mob_id != 53 && focus.template.mob_id != 79 && !Map.is_map_cant_save_site(focus.map_id))
                        || (focus.isBoss() && focus.template.mob_id == 178 && map.zone_id == 0 && ObjAtk.level > 89)
                        || (focus.isBoss() && focus.template.mob_id == 178 && map.zone_id == 2 && !(ObjAtk.level >= 90 && ObjAtk.level < 110))
                        || (focus.isBoss() && focus.template.mob_id == 178 && map.zone_id == 3 && ObjAtk.level < 110)) && !(map.ismapkogioihan());
                if (check) {
                    dame = 0;
                }
            }
            if (focus.isMoTaiNguyen() && ObjAtk.isPlayer()) {
                Mob_MoTaiNguyen mo = (Mob_MoTaiNguyen) focus;
                if (!mo.is_atk) {
                    dame = 0;
                } else if (mo.nhanban != null && !mo.nhanban.isDie) {
                    mo.nhanban.p_target = (Player) ObjAtk;
                    mo.nhanban.is_move = false;
                }
            }
            if (Manager.gI().bossTG.p.equals(focus)) {
                Manager.gI().bossTG.update_dame(p.name, dame);
//                if (Manager.gI().bossTG.p.hp < Manager.gI().bossTG.p.get_HpMax() / 2) {
//                    Service.usepotion(Manager.gI().bossTG.p, 0, Manager.gI().bossTG.p.get_HpMax());
//                }
            }
            if (ObjAtk.isPlayer() && HoiHP > 0) {
                Service.usepotion(p, 0, HoiHP);
            }
            if (idxSkill == 17 && ObjAtk.isPlayer() && focus.isPlayer()) {
                MapService.add_eff_skill(map, p, (Player) focus, (byte) idxSkill);
            }

            //<editor-fold defaultstate="collapsed" desc="Hiệu ứng Crit vv       ...">
            List<Eff_TextFire> ListEf = new ArrayList<>();

            if (hutHP > 0) {
                ListEf.add(new Eff_TextFire(0, (int) dame));
                ListEf.add(new Eff_TextFire(2, hutHP));
                focus.hp += hutHP;
                if (focus.hp > focus.get_HpMax()) {
                    focus.hp = focus.get_HpMax();
                }
            }
            if (xuyengiap) {
                dame += dame * (ptxuyengiap + 0.5);
                if (dame > 2_000_000_000) {
                    dame = 2_000_000_000;
                }
                ListEf.add(new Eff_TextFire(1, (int) dame));
            } else if (ObjAtk.get_Crit() > Util.random(10_000)) {
                //       dame *= 2;
                dame += dame * (ptCrit + 1);
                if (dame > 2_000_000_000) {
                    dame = 2_000_000_000;
                }
                ListEf.add(new Eff_TextFire(4, (int) dame));
            }
            if(ObjAtk.isPlayer() && focus.isMob() && (focus.template.mob_id == 23 || (focus.template.mob_id >= 51 && focus.template.mob_id <= 53)  || focus.template.mob_id == 79) && dame > 222222){
                dame = 222222;
            }
            if (focus.get_EffDefault(StrucEff.GoiSet) != null){
                dame *= 0.7;
            }
            //<editor-fold defaultstate="collapsed" desc="Phản Dame       ...">
            if (ObjAtk.isPlayer() && focus.get_PhanDame() > Util.random(10_000)) {
                int DAMEpst = (int) (dame * 0.5);
                DAMEpst -= ObjAtk.get_DefBase();
                if (type == 1) {
                    if (ObjAtk.clazz == 0) {
                        DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(18);
                    } else if (ObjAtk.clazz == 1) {
                        DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(20);
                    } else if (ObjAtk.clazz == 2) {
                        DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(17);
                    } else if (ObjAtk.clazz == 3) {
                        DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(19);
                    }
                } else {
                    DAMEpst -= DAMEpst * 0.0001 * ObjAtk.get_PercentDefProp(16);
                }
                if (DAMEpst <= 0) {
                    DAMEpst = 1;
                }

                ListEf.add(new Eff_TextFire(5, DAMEpst));
                ObjAtk.hp -= DAMEpst;
                if (ObjAtk.hp <= 0) {
                    ObjAtk.hp = 5;
                }
            }
            //</editor-fold> Phản Dame

            //</editor-fold>    hiệu ứng crit vv
            //<editor-fold defaultstate="collapsed" desc="Set hp       ...">
            // xả item chiến trường
            long time = System.currentTimeMillis();
            if (ObjAtk.isMobCTruongHouse() && map.Arena != null && map.Arena.timeCnNha > time) {
                dame *= 2;
            } else if (!ObjAtk.isPlayer() && ObjAtk.get_TypeObj() == 0 && map.Arena != null && map.Arena.timeCnLinh > time) {
                dame *= 2;
            }
            if (dame > 2_000_000_000) {
                dame = 2_000_000_000;
            } else if (dame <= 0) {
                dame = 1;
            }
            float ptHP = (focus.hp / focus.get_HpMax()) * 100;

            //<editor-fold defaultstate="collapsed" desc="Tác dụng đồ tinh tú      ">
            if (focus.isPlayer() && ObjAtk.isPlayer()) {
                Player player = (Player) focus;
                // Miễn st
                if (player.mienST(type_dame)) {
                    dame = 0;
                }
                // Giáp bảo hộ
                EffTemplate eff_bao_ho = focus.getEffTinhTu(EffTemplate.GIAP_BAO_HO);
                if (eff_bao_ho == null && player.isEffTinhTu(-114)) {
                    focus.addEffTinhTu(EffTemplate.GIAP_BAO_HO, 3, 10000L);
                }
                if (eff_bao_ho != null) {
                    if (eff_bao_ho.param > 0) {
                        dame = dame - (4 - eff_bao_ho.param) * dame / 4;
                        eff_bao_ho.param--;
                    } else {
                        focus.removeEffTinhTu(eff_bao_ho);
                    }
                }
                // Bộc phá
                if (player.isEffTinhTu(-125)) {
                    for (int i = 0; i < map.players.size(); i++) {
                        Player p_ = map.players.get(i);
                        if (p_ != null && Math.abs(p.x - p_.x) < 150 && Math.abs(p.y - p_.y) < 150) {
                            long hp_ = (long) p_.hp / 100 * Util.random(10, 40);
                            Service.usepotion(map.players.get(i), 0, -hp_);
                        }
                    }
                    Service.send_notice_nobox_white(player.conn, "Bộc phá");
                }
                // Giáp hung tàn
                EffTemplate eff_hung_tan = p.getEffTinhTu(EffTemplate.HUNG_TAN);
                if (eff_hung_tan == null && p.isEffTinhTu(-117)) {
                    p.addEffTinhTu(EffTemplate.HUNG_TAN, Util.random(3, 6), 20000L);
                    sendEffTinhTu(p, EffTemplate.HUNG_TAN, 20000);
                }
                if (eff_hung_tan != null) {
                    if (eff_hung_tan.param > 0) {
                        if (eff_hung_tan.param > eff_hung_tan.param2) {
                            eff_hung_tan.param2++;
                        } else {
                            HoiHP = p.get_HpMax() * eff_hung_tan.param / 100;
                            Service.usepotion(p, 0, HoiHP);
                            eff_hung_tan.param = (short) Util.random(3, 6);
                        }
                    }
                }
                // Giáp bạch kim
                EffTemplate eff_giap_bach_kim = focus.getEffTinhTu(EffTemplate.GIAP_BACH_KIM);
                if (eff_giap_bach_kim == null && p.isEffTinhTu(-85)) {
                    focus.addEffTinhTu(EffTemplate.GIAP_BACH_KIM, 0, 3000L);
                }
                // Giáp thiên sứ
                EffTemplate eff_giap_thien_su = focus.getEffTinhTu(EffTemplate.GIAP_THIEN_SU);
                if (eff_giap_thien_su == null && p.isEffTinhTu(-83)) {
                    focus.addEffTinhTu(EffTemplate.GIAP_THIEN_SU, 0, 3000L);
                }
                // Giáp vệ binh
                EffTemplate eff_giap_ve_binh = focus.getEffTinhTu(EffTemplate.GIAP_VE_BINH);
                if (eff_giap_ve_binh == null && p.isEffTinhTu(-81)) {
                    focus.addEffTinhTu(EffTemplate.GIAP_VE_BINH, 0, 3000L);
                }
                // Ngu đần
                EffTemplate eff_giap_ngu_dan = focus.getEffTinhTu(EffTemplate.NGU_DAN);
                if (eff_giap_ngu_dan == null && p.isEffTinhTu(-90)) {
                    focus.addEffTinhTu(EffTemplate.NGU_DAN, 0, 30000L);
                }
                // Bất tử
                if (player.hp < (focus.get_HpMax() / 20) && player.total_item_param(-88) > 0
                        && player.cooldown_bat_tu < System.currentTimeMillis()) {
                    player.addEffTinhTu(EffTemplate.BAT_TU, 0, 5000L);
                    player.cooldown_bat_tu = System.currentTimeMillis() + 180_000L;
                }
                // Mù mắt
                EffTemplate eff_giap_mu_mat = focus.getEffTinhTu(EffTemplate.MU_MAT);
                if (eff_giap_mu_mat == null && p.isEffTinhTu(-116)) {
                    focus.addEffTinhTu(EffTemplate.MU_MAT, 0, 5000L);
                    sendEffTinhTu(player, EffTemplate.MU_MAT, 5000);
                }
                // Thiêu cháy
                if (player.hp < (player.get_HpMax() * 3 / 10) && player.total_item_param(-115) > 0 && player.cooldown_thieu_chay < System.currentTimeMillis()) {
                    for (int i = 0; i < map.players.size(); i++) {
                        Player p_ = map.players.get(i);
                        if (p_ != null && Math.abs(p.x - p_.x) < 250 && Math.abs(p.y - p_.y) < 250) {
                            p_.addEffTinhTu(EffTemplate.THIEU_CHAY, 1, 20000);
                            sendEffTinhTu(p_, EffTemplate.THIEU_CHAY, 20000);
                            Service.send_notice_nobox_white(p_.conn, "Bạn bị trúng hiệu ứng thiêu cháy");
                        }
                    }
                    player.cooldown_thieu_chay = System.currentTimeMillis() + 180_000L;
                }
                // Tàn phế
                EffTemplate eff_giap_tan_phe = focus.getEffTinhTu(EffTemplate.TAN_PHE);
                if (eff_giap_tan_phe == null && p.isEffTinhTu(-92)) {
                    long mp = focus.mp - focus.get_MpMax() / 50;
                    focus.addEffTinhTu(EffTemplate.TAN_PHE, 0, 5000L);
                    Service.usepotion(player, 1, -mp);
                    sendEffTinhTu(p, EffTemplate.TAN_PHE, 5000);
                }
                if (p.getEffTinhTu(EffTemplate.BAT_TU) != null) {
                    dame = 0;
                }
            }
            //</editor-fold> Tác dụng đồ tinh tú

            if (ObjAtk.isPlayer() && p != null && p.countTT() == 9 && p.count_special < 20) {
                p.count_special += 1;
                if (p.count_special >= 20) {
                    p.addEffTinhTu(EffTemplate.SPECIAL, 1, 5000);
                    sendEffTinhTu(p, EffTemplate.SPECIAL, 5000);
                    p.count_special = 0;
                    Message mw = new Message(40);
                    mw.writer().writeByte(0);
                    mw.writer().writeByte(1);
                    mw.writer().writeShort(ObjAtk.index);
                    mw.writer().writeByte(21);
                    mw.writer().writeInt(5000);
                    mw.writer().writeShort(ObjAtk.index);
                    mw.writer().writeByte(0);
                    mw.writer().writeByte(30);
                    byte[] id__ = new byte[]{7, 8, 9, 10, 11, 15, 0, 1, 2, 3, 4, 16, 17, 18, 19, 20};
                    int[] par__ = new int[]{4000, 4000, 4000, 4000, 4000, 4000,
                        4 * (ObjAtk.get_param_view_in4(0) / 10), 4 * (ObjAtk.get_param_view_in4(1) / 10),
                        4 * (ObjAtk.get_param_view_in4(2) / 10), 4 * (ObjAtk.get_param_view_in4(3) / 10),
                        4 * (ObjAtk.get_param_view_in4(4) / 10), 4 * (ObjAtk.get_param_view_in4(14) / 10),
                        4000, 4000, 4000, 4000, 4000};
                    mw.writer().writeByte(id__.length);
                    //
                    for (int i = 0; i < id__.length; i++) {
                        mw.writer().writeByte(id__[i]);
                        mw.writer().writeInt(par__[i]);
                    }
                    //
                    MapService.send_msg_player_inside(p.map, p, mw, true);
                    mw.cleanup();
                }
            }

            if (ObjAtk.isPlayer() && Util.random(100) < 10 && p != null && p.type_use_mount == 10) {
                List<MainObject> objects = new ArrayList<>();
                objects.add(p);
                Service.send_eff_auto(p.conn, objects, 80);
                focus.hp -= (int) (dame / 20);
            }

            Mob_in_map mob = focus.isMob() ? (Mob_in_map) focus : null;
            if (ObjAtk.isPlayer() && noitai != -1) {
                switch (noitai) {
                    case 0 -> {
                        if (focus.isPlayer()) {
                            ((Player) focus).add_EffDefault(StrucEff.NOI_TAI_VAT_LY, 10, 10000);
                        } else if (focus.isMob() && mob != null) {
                            mob.add_Eff(StrucEff.NOI_TAI_VAT_LY, 10, 10000);
                        }
                    }
                    case 2 -> {
                        if (focus.isPlayer()) {
                            ((Player) focus).add_EffDefault(StrucEff.NOI_TAI_LUA, 10, 10000);
                        } else if (focus.isMob() && mob != null) {
                            mob.add_Eff(StrucEff.NOI_TAI_LUA, 10, 10000);
                        }
                    }
                    case 4 -> {
                        if (focus.isPlayer()) {
                            ((Player) focus).add_EffDefault(StrucEff.NOI_TAI_DOC, (int) (dame / 25 * 4), 10000);
                        } else if (focus.isMob() && mob != null) {
                            mob.add_Eff(StrucEff.NOI_TAI_DOC, (int) (dame / 25 * 4), 10000);
                        }
                    }
                    case 1 -> {
                        if (focus.isPlayer()) {
                            ((Player) focus).add_EffDefault(StrucEff.NOI_TAI_BANG, 10, 10000);
                            MapService.send_in4_other_char(map, p, ((Player) focus));
                            Service.send_char_main_in4((Player) focus);
                        } else if (focus.isMob() && mob != null) {
                            mob.add_Eff(StrucEff.NOI_TAI_BANG, 10, 10000);
                            Service.mob_in4(p, focus.index);
                        }
                    }
                    default -> {
                        if (focus.isPlayer()) {
                            ((Player) focus).add_EffDefault(StrucEff.NOI_TAI_DIEN, 10, 10000);
                        } else if (focus.isMob() && mob != null) {
                            mob.add_Eff(StrucEff.NOI_TAI_DIEN, 10, 10000);
                        }
                    }
                }
                Service.send_eff_intrinsic(map, focus, noitai, 10);
            }
            if (ObjAtk.isBot()) {
                dame = Util.random(focus.hp / 10, focus.hp / 8);
            }
            if (focus.isMobDiBuon()) {
                dame = focus.get_HpMax() * 5L / 100;
            }

            byte type_spec = 11;
            int dame_spec = 0;
            if (ObjAtk.isPlayer() && p != null) {
                if (p.total_item_param(5) > 0) {
                    dame_spec = p.total_item_param(5);
                } else if (p.total_item_param(6) > 0) {
                    type_spec = 10;
                    dame_spec = p.total_item_param(6);
                }
            }

            Player p_focus = focus.isPlayer() ? (Player) focus : null;
            if (focus.isPlayer() && p != null && p.total_item_param((byte) 185) > Util.nextInt(10000)) {
                focus.hp -= p_focus.hp_max * (Util.nextInt(10, 15) / 100);
                Service.send_notice_nobox_white(p.conn, "Áp đảo");
            }
            if (p != null && focus.isPlayer() && p_focus.total_item_param((byte) 186) > Util.nextInt(10000)) {
                p.hp -= ((Player) p).hp_max * (Util.nextInt(3, 5) / 100);
                focus.hp += p_focus.hp_max * (Util.nextInt(20, 25) / 100);
                Service.send_notice_nobox_white(p_focus.conn, "Giáp cốt");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 67) > Util.nextInt(10000)) {
                p_focus.add_EffDefault(StrucEff.TE_CONG, 1, 5000);
                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị tê cóng");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 66) > Util.nextInt(10000)) {
                p_focus.add_EffDefault(StrucEff.DongBang, 1, 5000);
                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị đóng băng");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 180) > Util.nextInt(10000)) {
                p_focus.add_EffDefault(StrucEff.ChongMat, 1, 5000);
                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị chóng mặt");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 184) > Util.nextInt(10000)) {
                p_focus.add_EffDefault(StrucEff.GoiSet, 1, 5000);
                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị tê liệt");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 189) > Util.nextInt(10000)) {
                p_focus.add_EffDefault(StrucEff.AoGiapLua, 1, 10000);
                p.add_EffDefault(145,1,10000);
                Service.send_notice_nobox_white(p_focus.conn, "Áo giáp lửa");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 161) > Util.nextInt(10000)) {
                p.add_EffDefault(StrucEff.LuaHoaNguc, 1, 5000);
                p_focus.add_EffDefault(146,1,5000);
                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị Lửa hỏa ngục");
            }
            if (focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 160) > Util.nextInt(10000)) {
                p.add_EffDefault(StrucEff.LoiPhat, 1, 5000);
                p_focus.add_EffDefault(147,1,5000);
                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị Lôi phạt");
            }
            if(focus.isPlayer() && p != null &&
                    p.total_item_param((byte) 188) > Util.nextInt(10000)){
                int ran = Util.random(0,100);
                if (ran < 30){
                    p.qua_noel = 1;
                }else if (ran < 66){
                    p.qua_noel = 2;
                }else {
                    p.qua_noel = 3;
                }
                p.add_EffDefault(StrucEff.Quà_noel,1,5000);
                Service.send_notice_nobox_white(p.conn, "Bạn nhận được quà");
            }
            if (focus.isPlayer() && p != null && p.total_item_param((byte) 177) > Util.nextInt(10000)) {
                p.add_EffDefault(141,1,5*1000);
                p_focus.add_EffDefault(140,1,5*1000);
                //EffTemplate eff = p.get_EffDefault(141);
                //Service.send_time_box(p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Miễn nhiễm hiệu ứng"});
                Service.send_notice_nobox_white(p_focus.conn, "Vãn tiễn xuyên tâm");
            }
            if (p != null && focus.isPlayer() && p_focus.total_item_param((byte) 178) > Util.nextInt(10000)) {
                p_focus.add_EffDefault(142,1,10 * 1000);
                Service.send_notice_nobox_white(p_focus.conn, "Câm lặng");
            }
            focus.hp -= (dame + dame_spec);
            if (focus.isBoss() && mob != null && ObjAtk.isPlayer()) {
                if (!mob.top_dame.containsKey(p.name)) {
                    mob.top_dame.put(p.name, dame);
                } else {
                    long dame_boss = dame + mob.top_dame.get(p.name);
                    mob.top_dame.put(p.name, dame_boss);
                }
                focus.hp += (int) (dame / 3);
                upHP(map, focus, (int) (dame / 3));
            }

            if (focus.hp <= 0) {
                if (focus.isPlayer() && ptHP > 70) {
                    focus.hp = 5;
                } else {
                    if (map.isMapChiemThanh()) {
                        ChiemThanhManager.Obj_Die(map, ObjAtk, focus);
                    }
                    if (map.isMapChienTruong()) {
                        ChienTruong.Obj_Die(map, ObjAtk, focus);
                    }
                    focus.SetDie(map, ObjAtk);
                    if (!focus.isPlayer() && !focus.isBot() && !focus.isMobDiBuon() && focus.template != null 
                            && focus.template.mob_id >= 89 && focus.template.mob_id <= 92) { // house chien truong
                        p.update_point_arena(20);
                        Manager.gI().chatKTGprocess("@Server : " + p.name + " đã đánh sập " + focus.template.name);
                        ChienTruong.gI().update_house_die(focus.template.mob_id);
                    }
                    if (focus.isPlayer()) {
                        MapService.Player_Die(map, focus, ObjAtk, true);
                    } else {
                        MapService.MainObj_Die(map, null, focus, true);
                    }

                }

            }

            if (ObjAtk.isPlayer() && (focus.isPlayer() || focus.get_TypeObj() == 0)) {
                MapService.Fire_Player(map, p.conn, idxSkill, focus.index, (int) dame, focus.hp, ListEf, type_spec, dame_spec);
            } else if (ObjAtk.isPlayer() && focus.get_TypeObj() == 1) {
                if (map.isMapChienTruong()) {
                    switch (focus.template.mob_id) {
                        case 89: {
                            if (p.typepk == 4) {
                                return;
                            }
                            break;
                        }
                        case 90: {
                            if (p.typepk == 2) {
                                return;
                            }
                            break;
                        }
                        case 91: {
                            if (p.typepk == 5) {
                                return;
                            }
                            break;
                        }
                        case 92: {
                            if (p.typepk == 1) {
                                return;
                            }
                            break;
                        }

                    }
                    MapService.Fire_Mob(map, p.conn, idxSkill, focus.index, (int) dame, focus.hp, ListEf, focus.template.mob_id, type_spec, dame_spec);

                } else {
                    MapService.Fire_Mob(map, p.conn, idxSkill, focus.index, (int) dame, focus.hp, ListEf, 0, type_spec, dame_spec);

                }
            } else if (ObjAtk.get_TypeObj() == 1 && focus.isPlayer()) {
                MapService.mob_fire(map, (Mob_in_map) ObjAtk, (Player) focus, (int) dame);
            } else if (ObjAtk.get_TypeObj() == 0 && focus.isPlayer()) {
                MapService.MainObj_Fire_Player(map, (Player) focus, ObjAtk, idxSkill, (int) dame, ListEf);
            }

            //</editor-fold>    Set hp
            //<editor-fold defaultstate="collapsed" desc="Tính exp       ...">
            if (focus.isMobDungeon()
                    && ObjAtk.isPlayer()) {
                if (p.level >= 139){
                    return;
                }
                int expup = 0;
                expup = (int) dame; // tinh exp
                ef = p.get_EffDefault(-125);
                if (ef != null) {
                    expup += (expup * (ef.param / 100)) / 100;
                }
                expup = (int) (expup / 3);
                p.update_Exp(expup, true);
                // exp clan
                if (p.myclan != null) {
                    int exp_clan = ((int) dame) / 10_000;
                    if (exp_clan < 1 || exp_clan > 50) {
                        exp_clan = 1;
                    }
                    p.myclan.update_exp(exp_clan);
                }
            } else if (focus.isMob()
                    && focus.isExp && ObjAtk.isPlayer()) {
                int expup = 0;
                expup = (int) dame; // tinh exp
                if (p.level <= 10) {
                    expup = expup * 3;
                }
                if (Math.abs(focus.level - p.level) == 0) {
                    expup = expup;
                } else if (Math.abs(focus.level - p.level) > 1) {
                    expup = (expup * 11) / 10;
                } else if (Math.abs(focus.level - p.level) > 2) {
                    expup = (expup * 12) / 10;
                } else if (Math.abs(focus.level - p.level) > 3) {
                    expup = (expup * 13) / 10;
                } else if (Math.abs(focus.level - p.level) > 4) {
                    expup = (expup * 14) / 10;
                } else if (Math.abs(focus.level - p.level) > 5) {
                    expup = (expup * 15) / 10;
                } else if (Math.abs(p.level - focus.level) > 1) {
                    expup = (expup * 9) / 10;
                } else if (Math.abs(p.level - focus.level) > 2) {
                    expup = (expup * 8) / 10;
                } else if (Math.abs(p.level - focus.level) > 3) {
                    expup = (expup * 7) / 10;
                } else if (Math.abs(p.level - focus.level) > 4) {
                    expup = (expup * 6) / 10;
                } else if (Math.abs(p.level - focus.level) > 5) {
                    expup = (expup * 5) / 10;
                }
                if (p.hieuchien > 0) {
                    expup /= 2;
                }
                if (Math.abs(focus.level - p.level) <= 10 && expup > 0) {
                    if (p.party != null) {
                        for (int i = 0; i < p.party.get_mems().size(); i++) {
                            Player pm = p.party.get_mems().get(i);
                            if (pm.index != p.index && (Math.abs(pm.level - p.level) < 10) && pm.map.map_id == p.map.map_id
                                    && pm.map.zone_id == p.map.zone_id) {
                                pm.update_Exp((expup / 10), true);
                            }
                        }
                    }
                    ef = p.get_EffDefault(-125);
                    if (ef != null) {
                        expup += (expup * (ef.param / 100)) / 100;
                    }
                    p.update_Exp(expup, true);
                }else if (p.level >= 139 && map.map_id == 137){
                    p.update_Exp(1000000,false);
                } else if (expup > 0) {
                    p.update_Exp(2, false);
                }
                // exp clan
                if (p.myclan != null) {
                    int exp_clan = ((int) dame) / 10_000;
                    if (exp_clan < 1 || exp_clan > 50) {
                        exp_clan = 1;
                    }
                    p.myclan.update_exp(exp_clan);
                }

                if (p.it_wedding != null) {
                    if (p.party != null && p.party.get_mems() != null) {
                        for (int i = 0; i < p.party.get_mems().size(); i++) {
                            Player pm = p.party.get_mems().get(i);
                            if (p.it_wedding.equals(pm.it_wedding)) {
                                p.it_wedding.exp += dame / 1_000;
                                break;
                            }
                        }
                    }
                }
            }
            //</editor-fold>    Tính exp

            //<editor-fold defaultstate="collapsed" desc="Pet Attack       ...">
            if (ObjAtk.isPlayer()) {
                if (!focus.isDie && p.pet_follow != -1) {
                    Pet my_pet = null;
                    for (Pet pett : p.mypet) {
                        if (pett.is_follow) {
                            my_pet = pett;
                            break;
                        }
                    }
                    if (my_pet != null && my_pet.grown > 0 && p.getEffTinhTu(EffTemplate.NGU_DAN) == null) {
                        int a1 = 0;
                        int a2 = 1;
                        for (Option_pet temp1 : my_pet.op) {
                            if (temp1.maxdam > 0) {
                                a1 = temp1.param;
                                a2 = temp1.maxdam;
                                break;
                            }
                        }
                        int dame_pet = Util.random(a1, Math.max(a2, a1 + 1));
                        if (dame_pet <= 0) {
                            dame_pet = 1;
                        }
                        if (((focus.hp - dame_pet) > 0) && (p.pet_atk_speed < System.currentTimeMillis()) && (a2 > 1)) {
                            if (focus.isMob() && (my_pet.get_id() == 3269 || my_pet.name.equals("Đại Bàng"))) {
                                int vangjoin = Util.random(1666, 2292);
                                p.update_vang(vangjoin);
                                Log.gI().add_log(p.name, "Nhận " + vangjoin + " từ đại bàng");
                                Service.send_notice_nobox_white(p.conn, "+ " + vangjoin + " vàng");
                            }
                            if (focus.isPlayer() && my_pet.get_id() == 4614 && Util.nextInt(100) < 5) {
                                p_focus.add_EffDefault(StrucEff.VET_THUONG_SAU, 1, 5000);
                                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị vết thương sâu");
                            }
                            if (focus.isPlayer() && my_pet.get_id() == 4626 && Util.nextInt(100) < 5) {
                                p_focus.add_EffDefault(StrucEff.TE_CONG, 1, 5000);
                                Service.usepotion(p_focus, 0, (int) -(p_focus.hp * Util.random(5, 10) * 0.01));
                                Service.send_notice_nobox_white(p_focus.conn, "Bạn bị tê cóng");
                            }
                            if (focus.isPlayer() && my_pet.get_id() == 3616 && Util.nextInt(100) < 5){
                                int ran = Util.random(0,100);
                                if (ran < 30){
                                    p.qua_noel = 1;
                                }else if (ran < 66){
                                    p.qua_noel = 2;
                                }else {
                                    p.qua_noel = 3;
                                }
                                p.add_EffDefault(StrucEff.Quà_noel,1,5000);
                                Service.send_notice_nobox_white(p.conn, "Bạn nhận được quà");
                            }
                            p.pet_atk_speed = System.currentTimeMillis() + 5000L;
                            Message m = new Message(84);
                            m.writer().writeByte(2);
                            m.writer().writeShort(p.index);
                            m.writer().writeByte(focus.get_TypeObj());
                            m.writer().writeByte(1);
                            m.writer().writeShort(focus.index);
                            m.writer().writeInt(dame_pet);
                            focus.hp -= dame_pet;
                            m.writer().writeInt(focus.hp);
                            m.writer().writeInt(p.hp);
                            m.writer().writeInt(p.body.get_HpMax());
                            p.conn.addmsg(m);
                            m.cleanup();
                        }
                    }
                }
            }
            //</editor-fold>    Pet Attack

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void upHP(Map map, MainObject focus, int hp) throws IOException {
        Message m_hp = new Message(32);
        m_hp.writer().writeByte(1);
        m_hp.writer().writeShort(focus.index);
        m_hp.writer().writeShort(-1);
        m_hp.writer().writeByte(0);
        m_hp.writer().writeInt(focus.get_HpMax());
        m_hp.writer().writeInt(focus.hp);
        m_hp.writer().writeInt(hp);
        for (int i = 0; i < map.players.size(); i++) {
            map.players.get(i).conn.addmsg(m_hp);
        }
        m_hp.cleanup();
    }

    public int get_param_view_in4(int type) {
        switch (type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4: {
                return get_DameProp(type);
            }
            case 7:
            case 8:
            case 9:
            case 10:
            case 11: {
                return get_PercentDameProp(type);
            }
            case 14: {
                return get_DefBase();
            }
            case 15: {
                return get_PercentDefBase();
            }
            case 33: {
                return get_Crit();
            }
            case 34: {
                return get_Miss(false);
            }
            case 35: {
                return get_PhanDame();
            }
            case 36: {
                return get_Pierce();
            }
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 27:
            case 28:
            case 22: {
                return (total_item_param(type) + total_skill_param(type));
            }
            default: {
                return total_item_param(type);
            }
        }
    }

    public int total_skill_param(int id) {
        return 0;
    }

    public int total_item_param(int id) {
        return 0;
    }

    public void update(Map map) {

    }

    public static void send_eff_to_object(Player p, MainObject object, int id_eff) {
        try {
            byte[] data = Util.loadfile("data/part_char/imgver/x" + p.conn.zoomlv + "/Data/" + (112 + "_" + id_eff));
            Message m = new Message(-49);
            m.writer().writeByte(2);
            m.writer().writeShort(data.length);
            m.writer().write(data);
            m.writer().writeByte(0); // b3
            m.writer().writeByte(0); // b4
            m.writer().writeByte(id_eff); // num4
            m.writer().writeShort(object.index); // iD2
            m.writer().writeByte(object.get_TypeObj()); // tem2
            m.writer().writeByte(0); // b5
            m.writer().writeInt(1000); // num5

            MapService.send_msg_player_inside(p.map, p, m, true);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
