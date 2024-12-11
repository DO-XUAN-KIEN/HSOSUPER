package map;

import BossHDL.BossManager;

import java.util.ArrayList;
import java.util.List;

import client.Player;
import core.Manager;
import core.Service;
import core.Util;
import event_daily.ChiemThanhManager;
import io.Message;

import java.io.IOException;
import java.util.HashMap;

import template.EffTemplate;
import template.MainObject;
import template.StrucEff;

public class Mob_in_map extends MainObject {
    public final static HashMap<Integer, Mob_in_map> ENTRYS = new HashMap<>();
    public int time_refresh = 3;
    private boolean is_boss;
    public long time_back;
    public final List<Player> list_fight = new ArrayList<>();
    public long time_fight;
    public boolean is_boss_active;
    public int timeBossRecive = 1000 * 60 * 60 * 8;
    public final HashMap<String, Long> top_dame = new HashMap<>();
    public Map map;
    public boolean ishs = true;

    public void Set_isBoss(boolean isBoss) {
        is_boss = isBoss;
    }

    @Override
    public boolean isBoss() {
        return is_boss;
    }

    @Override
    public boolean isMobCTruongHouse() {
        return template.mob_id >= 89 && template.mob_id <= 92;
    }

    @Override
    public boolean isMob() {
        return true;
    }

    @Override
    public int get_DameBase() {
        if (dame <= 0) {
            dame = level * 75;
        }

        int dmob = Util.random((int) (this.dame * 0.95), (int) (this.dame * 1.05));
//            System.out.println("map.MapService.mob_fire()"+dmob);
        if (this.level > 30 && this.level <= 50) {
            dmob = (dmob * 13) / 10;
        } else if (this.level > 50 && this.level <= 70) {
            dmob = (dmob * 16) / 10;
        } else if (this.level > 70 && this.level <= 100) {
            dmob = (dmob * 19) / 10;
        } else if (this.level > 100 && this.level <= 600) {
            dmob = (dmob * 21) / 10;
        }
        if (this.map_id == 136){
            dmob = (dmob * 60) / 10;
        }
        if (this.is_boss) {
            dmob = (int) (dmob * this.level * 0.03);
        }
        if (this.color_name != 0 && (this.template.mob_id < 89 || this.template.mob_id > 92)) {
            dmob *= 2;
        }
        return dmob;
    }

    @Override
    public int get_HpMax() {
        int hpm = hp_max;
        if (get_EffDefault(StrucEff.NOI_TAI_BANG) != null) {
            hpm = hpm / 5 * 4;
            if (hp > hpm) {
                hp = hpm;
            }
        }
        return hpm;
    }

    @Override
    public int get_Miss(boolean giam_ne) {
        return 800;
    }

    @Override
    public void SetDie(Map map, MainObject mainAtk) {
        try {
            hp = 0;
            isDie = true;
            if (MainEff != null) {
                MainEff.clear();
            }
            Mob_in_map mob = (Mob_in_map) this;
            if (mainAtk.isPlayer()) {
                if (((Player) mainAtk).hieuchien > 0 && Math.abs(mainAtk.level - mob.level) <= 5) {
                    ((Player) mainAtk).hieuchien--;
                }
                if (mob.template.mob_id == 152) {
                    ChiemThanhManager.SetOwner((Player) mainAtk);
                }
            }
            Player p = (Player) mainAtk;
            if (mainAtk.isPlayer() && p.quest_daily != null && p.quest_daily[0] != -1 && p.quest_daily[2] < p.quest_daily[3] && p.quest_daily[0] == this.template.mob_id) {
                p.quest_daily[2] += 1;
                Service.send_notice_nobox_white(p.conn, "Nhiệm vụ hàng ngày " + p.quest_daily[2] + "/" + p.quest_daily[3]);
            }
            boolean check_mob_roi_ngoc_kham = mob.template.mob_id >= 167 && mob.template.mob_id <= 172;
            if (mob.isBoss()) {
                map.BossDie(mob);
                String p_name = "";
                long top_dame = 0;
                for (java.util.Map.Entry<String, Long> en : mob.top_dame.entrySet()) {
                    if (en.getValue() > top_dame) {
                        top_dame = en.getValue();
                        p_name = en.getKey();
                    }
                }
                mob.is_boss_active = false;
                if (!Map.is_map_cant_save_site(mob.map_id)) {
                    Manager.gI().chatKTGprocess("" + mainAtk.name + " Đã Tiêu Diệt " + mob.template.name);
                    Manager.gI().chatKTGprocess("" + top_dame + " Đã Nhận Quà 1 Top Sát Thương Đánh " + mob.template.name + "");
                    Manager.gI().chatKTGprocess("" + mainAtk.name + " Chỵ Xin Boss Nhé Mấy Cưng !!!");
                }
                if (mainAtk.isPlayer()) {
                    if (mob.template.mob_id == 174) {
                        BossManager.DropItemBossEvent(map, mob, (Player) mainAtk);
                    } else {
                        LeaveItemMap.leave_item_boss(map, mob, (Player) mainAtk);
                    }
                }
            } else {
                mob.time_back = System.currentTimeMillis() + mob.time_refresh * 1000L - 1000L;
                p.danhvong += 1;
                p.item.char_inventory(5);
                if (mainAtk.isPlayer()) {
                    if ((Math.abs(mob.level - mainAtk.level) <= 10 || map.map_id == 136 || map.map_id == 137) && !check_mob_roi_ngoc_kham) {
                        if (map.isMapLangPhuSuong()) {
                            int percent = 20;
                            if (percent > Util.random(0, 300)) {
                                LeaveItemMap.leave_vang(map, mob, (Player) mainAtk);
                            } else if (percent > Util.random(0, 300)) {
                                LeaveItemMap.leave_item_by_type7(map, (short) Util.random(481, 485), p, mob.index);
                            } else if (percent > Util.random(0, 500)) {
                                LeaveItemMap.leave_item_by_type7(map, (short) Util.random(485, 489), p, mob.index);
                            } else if (percent > Util.random(0, 300)) {
                                LeaveItemMap.leave_item_by_type7(map, (short) Util.random(471, 480), p, mob.index);
                            } else if (percent > Util.random(0, 100)) {
                                LeaveItemMap.leave_item_by_type7(map, (short) Util.random(0, 2), p, mob.index);
                            }
                            if (Manager.gI().event != -1 && 30 > Util.random(0, 100) && Math.abs(mob.level - mainAtk.level) <= 5) {
                                LeaveItemMap.leave_item_event(map, mob, (Player) mainAtk);
                            }
                        } else {
                            int percent = 20;
                            if (zone_id == 1 && !Map.is_map_not_zone2(map_id)
                                    && p.get_EffDefault(-127) != null) {
                                percent = 40;
                            }
                            if (Math.abs(mob.level - mainAtk.level) <= 5 && Manager.gI().event == 3 && Util.random_ratio(10)) {
                                ev_he.Event_3.LeaveItemMap(map, this, mainAtk);
//                            } else if (mob.level >= 133) {
//                                ev_he.Event_3.LeaveItemMap(map, this, mainAtk);
                            }
                            if (percent > Util.random(0, 300) || mob.color_name != 0) {
                                LeaveItemMap.leave_item_3(map, mob, (Player) mainAtk);
                            }
                            if (map_id == 136){
                                if (Util.random(0,10000) < 10) {
                                    LeaveItemMap.leave_item_by_type4(map, (short) 344, p, mob.index);
                                }
                            }
                            if (percent > Util.random(0, 500) && zone_id == 1 && !Map.is_map_not_zone2(map_id)
                                    && p.get_EffDefault(-127) != null) {
                                if(Util.random(20000) < 4) {
                                    LeaveItemMap.leave_item_by_type7(map, (short) 494, p, mob.index);
                                }
                                if (Util.random(0, 10) < 2) {
                                    LeaveItemMap.leave_item_by_type7(map, (short) Util.random(116, 126), p, mob.index);
                                } else {
                                    LeaveItemMap.leave_item_by_type7(map, (short) 13, p, mob.index);
                                }
                                if (Manager.gI().event == 11){
                                    if (Util.random(300) < 4){
                                        LeaveItemMap.leave_item_by_type4(map,(short) Util.random(335, 339),p,mob.index);
                                    }
                                }
                            }
                            if (percent > Util.random(0, 300)) {
                                LeaveItemMap.leave_item_4(map, mob, (Player) mainAtk);
                            }
                            if (percent > Util.random(0, 300)) {
                                LeaveItemMap.leave_item_7(map, mob, (Player) mainAtk);
                            }
                            if (percent + 10 > Util.random(0, 300)) {
                                LeaveItemMap.leave_vang(map, mob, (Player) mainAtk);
                            }
                            if (percent + 10 > Util.random(0, 300)) {
                                LeaveItemMap.leave_material(map, mob, (Player) mainAtk);
                            }
                            if (Manager.gI().event != 0 && 30 > Util.random(0, 100) && Math.abs(mob.level - mainAtk.level) <= 5) {
                                LeaveItemMap.leave_item_event(map, mob, (Player) mainAtk);
                            }
                        }
                    }
                    if (check_mob_roi_ngoc_kham) {
                        LeaveItemMap.leave_material_ngockham(map, mob, (Player) mainAtk);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Map map) {
        try {
            this.updateEff();
            EffTemplate eff = get_EffDefault(StrucEff.NOI_TAI_DOC);
            if (!isDie && eff != null) {
                this.hp -= eff.param;
                upHP(map, this, -eff.param);
            }
            if (this.isDie && this.ishs && this.time_back < System.currentTimeMillis()) {
                this.isDie = false;
                this.hp = get_HpMax();
                synchronized (list_fight) {
                    list_fight.clear();
                }
                synchronized (top_dame) {
                    top_dame.clear();
                }
                if (this.isBoss()) {
                    this.color_name = 3;
                } else if (5 > Util.random(200) && this.level > 50) {
                    this.color_name = (new byte[]{1, 2, 4, 5})[Util.random(4)];
                } else {
                    this.color_name = 0;
                }
                for (int j = 0; j < map.players.size(); j++) {
                    Player pp = map.players.get(j);
                    if (pp != null && !pp.other_mob_inside.containsKey(this.index)) {
                        pp.other_mob_inside.put(this.index, true);
                    }
                    if (pp != null && pp.other_mob_inside.get(this.index)) {
                        Message mm = new Message(4);
                        mm.writer().writeByte(1);
                        mm.writer().writeShort(this.template.mob_id);
                        mm.writer().writeShort(this.index);
                        mm.writer().writeShort(this.x);
                        mm.writer().writeShort(this.y);
                        mm.writer().writeByte(-1);
                        pp.conn.addmsg(mm);
                        mm.cleanup();
                        pp.other_mob_inside.replace(this.index, true, false);
                    } else {
                        Service.mob_in4(pp, this.index);
                    }
                }
            }
            if (!this.isDie && this.isATK && this.time_fight < System.currentTimeMillis()) {
                if ((this.template.mob_id == 151 || this.template.mob_id == 152 || this.template.mob_id == 154)) {
                    for (Player p0 : this.list_fight) {
                        if (p0 != null && !p0.isDie && p0.map.map_id == this.map_id && p0.map.zone_id == this.zone_id
                                && Math.abs(this.x - p0.x) < 200 && Math.abs(this.y - p0.y) < 200) {
                            MainObject.MainAttack(map, this, p0, 0, null, 2);
                        }
                    }
                    this.time_fight = System.currentTimeMillis() + 3500L;
                } else if (!this.list_fight.isEmpty()) {
                    Player p0 = this.list_fight.get(Util.random(this.list_fight.size()));
                    if (p0 != null && !p0.isDie && p0.map.map_id == this.map_id && p0.map.zone_id == this.zone_id) {
                        if (Math.abs(this.x - p0.x) < 200 && Math.abs(this.y - p0.y) < 200) {
                            if (this.time_fight < System.currentTimeMillis()) {
                                this.time_fight = System.currentTimeMillis() + 3500L;
                                MainObject.MainAttack(map, this, p0, 0, null, 2);
//                                    MapService.mob_fire(this, mob, p0);
                            }
                        } else {
                            this.list_fight.remove(p0);
                            //
                            Message m = new Message(10);
                            m.writer().writeByte(0);
                            m.writer().writeShort(this.index);
                            MapService.send_msg_player_inside(map, p0, m, true);
                            m.cleanup();
                        }
                    }
                    if (p0 != null && p0.isDie) {
                        this.list_fight.remove(p0);
                        //
                        Message m = new Message(10);
                        m.writer().writeByte(0);
                        m.writer().writeShort(this.index);
                        MapService.send_msg_player_inside(map, p0, m, true);
                        m.cleanup();
                    }
                    if (this.list_fight.contains(p0) && !(p0 != null && p0.map.map_id == this.map_id && p0.map.zone_id == this.zone_id)) {
                        this.list_fight.remove(p0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_Eff(int id, int param, int time) {
        this.add_EffDefault(id, param, System.currentTimeMillis() + time);
    }
}
