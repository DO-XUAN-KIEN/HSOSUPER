/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import ai.Bot;
import core.Manager;
import core.Service;
import core.Util;
import event_daily.ChiemThanhManager;

import java.io.IOException;
import java.util.ArrayList;

import map.Map;
import map.MapService;
import template.EffTemplate;
import template.Item3;
import template.Kham_template;
import template.MainObject;
import template.Option;
import template.Option_pet;
import template.StrucEff;
import template.Horse;

/**
 * @author chien
 */
public class Body2 extends MainObject {

    private Player p;

    protected void SetPlayer(Player p) {
        if (this.p != null) {
            return;
        }
        this.p = p;
        kham = new Kham_template();
        MainEff = new ArrayList<>();
        Eff_me_kham = new ArrayList<>();
        Eff_Tinh_Tu = new ArrayList<>();
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    private int get_point(int i) {
        int point = 0;
        switch (i) {
            case 1: {
                point += p.point1 + get_plus_point(23);
                break;
            }
            case 2: {
                point += p.point2 + get_plus_point(24);
                break;
            }
            case 3: {
                point += p.point3 + get_plus_point(25);
                break;
            }
            case 4: {
                point += p.point4 + get_plus_point(26);
                break;
            }
        }
        return point;
    }

    public int get_plus_point(int i) {
        int param = 0;
        switch (i) {
            case 23: {
                param += total_item_param(i);
                EffTemplate ef = p.get_EffDefault(23);
                if (ef != null) {
                    param += (p.point1 * (ef.param / 100)) / 100;
                }
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 23) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 24: {
                param += total_item_param(i);
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 24) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 25: {
                param += total_item_param(i);
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 25) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case 26: {
                param += total_item_param(i);
                for (Pet temp : p.mypet) {
                    if (temp.is_follow) {
                        for (Option_pet op : temp.op) {
                            if (op.id == 26) {
                                param += op.param;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return param;
    }

    @Override
    public int total_item_param(int id) {
        int param = 0;
        for (int i = 0; i < p.item.wear.length; i++) {
            Item3 temp = p.item.wear[i];
            if (temp != null) {
                if (p.level < temp.level) {
                    continue;
                }
                for (Option op : temp.op) {
                    if (op.id == id) {
                        param += op.getParam(temp.tier);
                    }
                }
            }
        }
        return param;
    }

    @Override
    public int get_HpMax() {
        long hpm = (int) (2500 * Manager.ratio_hp);
        switch (p.clazz) {
            case 0: {
                hpm += (550 + get_point(3) * 320);
                break;
            }
            case 1: {
                hpm += (get_point(3) * 300);
                break;
            }
            case 2: {
                hpm += (50 + get_point(3) * 310);
                break;
            }
            case 3: {
                hpm += (120 + get_point(3) * 300);
                break;
            }
        }
        int percent = total_item_param(27);
        if (p.skill_point[9] > 0) {
            for (Option op : p.skills[9].mLvSkill[p.skill_point[9] - 1].minfo) {
                if (op.id == 27) {
                    percent += op.getParam(0);
                    break;
                }
            }
        }
        if(p.get_EffDefault(EffTemplate.buffhp) != null || p.get_EffDefault(EffTemplate.bufftatca) != null){
            percent += 2000;
        }
        if (p.get_EffDefault(StrucEff.Quà_noel) != null && p.qua_noel == 1){
            hpm *= 1.5;
            hp *= 1.5;
        }
        if(p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            percent += 3000;
        }
        if(p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            percent += 3000;
        }
        if (p.type_use_mount == Horse.HEO_RUNG || p.type_use_mount == Horse.CON_LAN || p.type_use_mount == Horse.SKELETON
                || p.type_use_mount == Horse.CHUOT_TUYET || p.type_use_mount == Horse.VOI_MA_MUT
                || p.type_use_mount == Horse.MA_TOC_DO || p.type_use_mount == Horse.RONG_BANG) {
            percent += 1000;
        }
        hpm += ((hpm * (percent / 100)) / 100);
        if (p.get_EffDefault(2) != null) {
            hpm = (hpm * 8) / 10;
        }
        if (hpm > 2_000_000_000) {
            hpm = 2_000_000_000;
        }
        if (get_EffDefault(StrucEff.NOI_TAI_BANG) != null) {
            hpm = hpm / 5 * 4;
            if (hp > hpm) {
                hp = (int) hpm;
            }
        }
        if (p.map != null && p.map.mapsk == true) {
            hpm /= 2;
        }
        if (p.get_EffDefault(143) != null){
            hpm *= 0.05;
            hp *= 0.05;
        }
        if (p.get_EffDefault(StrucEff.GoiSet) != null){
            hpm *= 0.9;
            hp *= 0.9;
        }
        return (int) (hpm * Manager.ratio_hp);
    }

    @Override
    public int get_MpMax() {
        long mpm = 250;
        switch (p.clazz) {
            case 0:
            case 1: {
                mpm += get_point(4) * 10;
                break;
            }
            case 2: {
                mpm += 10 + get_point(3) + get_point(4) * 11;
                break;
            }
            case 3: {
                mpm += 5 + get_point(3) + get_point(4) * 11;
                break;
            }
        }
        int percent = total_item_param(28);
        if (p.skill_point[10] > 0) {
            for (Option op : p.skills[10].mLvSkill[p.skill_point[10] - 1].minfo) {
                if (op.id == 28) {
                    percent += op.getParam(0);
                    break;
                }
            }
        }
        mpm += ((mpm * (percent / 100)) / 100);
        EffTemplate ef = p.get_EffDefault(2);
        if (ef != null) {
            mpm = (mpm * 8) / 10;
        }
        if (mpm > 2_000_000_000) {
            mpm = 2_000_000_000;
        }
        if (p.map != null && p.map.mapsk == true) {
            mpm /= 2;
        }
        return (int) mpm;
    }

    @Override
    public int total_skill_param(int id) {
        int param = 0;
        for (int i = 0; i < p.skill_point.length; i++) {
            if (p.skill_point[i] > 0) {
                Option[] temp = p.skills[i].mLvSkill[get_skill_point(i) - 1].minfo;
                for (Option op : temp) {
                    if (op.id == id) {
                        param += op.getParam(0);
                    }
                }
            }
        }
        return param;
    }

    @Override
    public int get_Pierce() {
        int pie = total_item_param(36) + total_skill_param(36);
        pie += get_point(4) * 2;
        EffTemplate ef = get_EffDefault(36);
        if (ef != null) {
            pie += ef.param;
        }
        EffTemplate eff_ve_binh = getEffTinhTu(EffTemplate.GIAP_VE_BINH);
        if (eff_ve_binh != null) {
            pie = pie / 10;
        }
        if (p.map.mapsk == true){
            pie /= 2;
        }
        if (p.get_EffDefault(143) != null){
            pie *= 0.05;
        }
        if (p.get_EffDefault(142) != null) {
            pie *= 0;
        }
        if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            pie += 2000;
        }
        if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            pie += 2000;
        }
        return (int) (pie / 2.1);
    }
    @Override
    public int get_PhanDame() {
        int param = 2 * get_point(3);
        param += total_item_param(35);
        if (p.get_EffDefault(35) != null) {
            param += p.get_EffDefault(35).param;
        }
        if(p.get_EffDefault(EffTemplate.buffpst) != null || get_EffDefault(EffTemplate.bufftatca) != null){
            param += 2000;
        }
        EffTemplate eff_thien_su = getEffTinhTu(EffTemplate.GIAP_THIEN_SU);
        if (eff_thien_su != null) {
            param = param / 10;
        }
        if (p.get_EffDefault(143) != null){
            param *= 0.05;
        }
        if (p.get_EffDefault(142) != null) {
            param *= 0;
        }
        if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            param += 2000;
        }
        if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            param += 2000;
        }
        return (int) (param / 2.1);
    }
    @Override
    public int get_Miss(boolean giam_ne) {
        int param = 2 * get_point(2);
        param += total_item_param(34);
        if (get_EffDefault(34) != null) {
            param += get_EffDefault(34).param;
        }
        if (get_EffDefault(EffTemplate.buffne) != null || get_EffDefault(EffTemplate.bufftatca) != null){
            param += 2000;
        }
        if (giam_ne) {
            param = param / 10 * 9;
        }
        if (p.get_EffDefault(143) != null){
            param *= 0.05;
        }
        if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            param += 2000;
        }
        if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            param += 2000;
        }
        if (p.get_EffDefault(142) != null) {
            param *= 0;
        }
        return (int) (param / 1.8);
    }
    @Override
    public int get_Crit() {
        int crit = total_item_param(33) + total_skill_param(33);
        crit += get_point(1) * 2;
        EffTemplate ef = get_EffDefault(33);
        if (ef != null) {
            crit += ef.param;
        }
        EffTemplate eff_bach_kim = getEffTinhTu(EffTemplate.GIAP_BACH_KIM);
        if (eff_bach_kim != null) {
            crit = crit / 10;
        }
        if (p.map.mapsk == true){
            crit /= 2;
        }
        if (p.get_EffDefault(141) != null) {
            crit *= 10;
        }
        if (p.get_EffDefault(143) != null){
            crit *= 0.05;
        }
        if (p.get_EffDefault(142) != null) {
            crit *= 0;
        }
        if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            crit += 2000;
        }
        if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            crit += 2000;
        }
        return (int) (crit / 2.1);
    }

    public int get_skill_point(int i) {
        if (p.skill_point[i] > 0) {
            int par_ = p.skill_point[i] + get_skill_point_plus(i);
            return (par_ > 15 ? 15 : par_);
        }
        return 0;
    }

    @Override
    public int get_PercentDefBase() {
        int def = total_item_param(15);
        def += get_point(2) * 10;
        if (get_skill_point(15) > 0) {
            for (Option op : p.skills[15].mLvSkill[get_skill_point(15) - 1].minfo) {
                if (op.id == 15) {
                    def += op.getParam(0);
                    break;
                }
            }
        }
        if (p.get_EffDefault(24) != null) {
            def += p.get_EffDefault(24).param;
        }
        if (p.get_EffDefault(EffTemplate.buffpt) != null || get_EffDefault(EffTemplate.bufftatca) != null){
            def += 2000;
        }
        if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            def += 2000;
        }
        if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            def += 2000;
        }
        if (p.type_use_mount == Horse.NGUA_CHIEN_GIAP || p.type_use_mount == Horse.VOI_MA_MUT) {
            def += 2000;
        } else if (p.type_use_mount == Horse.NGUA_XICH_THO || p.type_use_mount == Horse.TUAN_LOC
                || p.type_use_mount == Horse.CAN_DAU_VAN || p.type_use_mount == Horse.XE_TRUOT_TUYET
                || p.type_use_mount == Horse.CA_CHEP) {
            def += 1000;
        } else if (p.type_use_mount == Horse.MA_TOC_DO || p.type_use_mount == Horse.HOA_KY_LAN
                || p.type_use_mount == Horse.PHUONG_HOANG_LUA) {
            def += 1500;
        } else if (p.type_use_mount == Horse.TRAU_RUNG) {
            def += 3000;
        }
        EffTemplate temp2 = p.get_EffDefault(StrucEff.PowerWing);
        if (temp2 != null) {
            def += 3000;
        }
        if(p.get_EffDefault(EffTemplate.SPECIAL) != null){
            def *= 1.5;
        }
        if (p.get_EffDefault(StrucEff.NOI_TAI_LUA) != null) {
            def -= 2000;
        }
        if (p.get_EffDefault(143) != null){
            def *= 0.05;
        }
        if (p.get_EffDefault(140) != null) {
            def *= 0;
        }
        if (def < 0) {
            def = 0;
        }
        return def;
    }

    @Override
    public int get_DefBase() {
        long def = total_item_param(14);
        switch (p.clazz) {
            case 0, 2: {
                def += get_point(2) * 20;
                break;
            }
            case 1, 3: {
                def += get_point(2) * 22;
                break;
            }
        }
        def += ((def * (get_PercentDefBase() / 100)) / 100);
        if (def >= 2_000_000_000L){
            def = 2_000_000_000L;
        }
        EffTemplate ef = p.get_EffDefault(0);
        if (ef != null) {
            def = (def * 8) / 10;
        }
        ef = p.get_EffDefault(15);
        if (ef != null) {
            def += (def * (ef.param / 100)) / 100;
        }
        if(p.get_EffDefault(StrucEff.Quà_noel) != null && p.qua_noel == 2){
            def *= 0.15;
        }
        if (p.get_EffDefault(143) != null){
            def *= 0.05;
        }
        if (p.get_EffDefault(140) != null) {
            def *= 0;
        }
        return (int) (def * 0.8);
    }

    @Override
    public int get_PercentDameProp(int type) {
        if (type == 0) {
            int percent = total_item_param(7);
            switch (p.clazz) {
                case 0:
                case 1: {
                    percent += get_point(1) * 20;
                    break;
                }
                case 2:
                case 3: {
                    percent += get_point(1) * 20 + get_point(4) * 18;
                    break;
                }
            }
            if (get_skill_point(11) > 0) {
                for (Option op : p.skills[11].mLvSkill[get_skill_point(11) - 1].minfo) {
                    if (op.id == 7) {
                        percent += op.getParam(0);
                        break;
                    }
                }
            }
            EffTemplate eff = get_EffDefault(StrucEff.BuffSTVL);
            if (eff != null) {
                percent += eff.param;
            }
            if (p.getEffTinhTu(EffTemplate.SPECIAL) != null) {
                percent *= 1.5;
            }
            EffTemplate temp2 = p.get_EffDefault(StrucEff.PowerWing);
            if (temp2 != null) {
                percent += 3000;
            }
            if (p.get_EffDefault(144) != null){
                percent *= 0.1 * p.cuong_bao;
            }
            //<editor-fold defaultstate="collapsed" desc="ngựa...">
            if (p.type_use_mount == Horse.NGUA_XICH_THO) {
                percent += 2000;
            } else if (p.type_use_mount == Horse.TUAN_LOC) {
                percent += 4000;
            } else if (p.type_use_mount == Horse.HEO_RUNG || p.type_use_mount == Horse.CON_LAN || p.type_use_mount == Horse.CA_CHEP) {
                percent += 1000;
            } else if (p.type_use_mount == Horse.TRAU_RUNG || p.type_use_mount == Horse.MA_TOC_DO
                    || p.type_use_mount == Horse.PHUONG_HOANG_LUA) {
                percent += 1500;
            } else if (p.type_use_mount == Horse.HOA_KY_LAN) {
                percent += 3500;
            }
            //</editor-fold>
            return percent;
        }
        int perct = 0;
        switch (p.clazz) {
            case 0: {
                if (type == 9 || type == 2) {
                    perct += get_point(1) * 20;
                    perct += total_item_param(9);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 9) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTLua);
                if (eff != null) {
                    perct += eff.param;
                }
                break;
            }
            case 1: {
                if (type == 11 || type == 4) {
                    perct += get_point(1) * 20;
                    perct += total_item_param(11);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 11) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTDoc);
                if (eff != null) {
                    perct += eff.param;
                }
                break;
            }
            case 2: {
                if (type == 8 || type == 1) {
                    perct += get_point(1) * 20 + get_point(4) * 18;
                    perct += total_item_param(8);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 8) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTBang);
                if (eff != null) {
                    perct += eff.param;
                }
                break;
            }
            case 3: {
                if (type == 10 || type == 3) {
                    perct += get_point(1) * 20 + get_point(4) * 18;
                    perct += total_item_param(10);
                    if (get_skill_point(12) > 0) {
                        for (Option op : p.skills[12].mLvSkill[get_skill_point(12) - 1].minfo) {
                            if (op.id == 10) {
                                perct += op.getParam(0);
                                break;
                            }
                        }
                    }
                }
                EffTemplate eff = get_EffDefault(StrucEff.BuffSTDien);
                if (eff != null) {
                    perct += eff.param;
                }
                break;
            }
        }
        if (p.getEffTinhTu(EffTemplate.SPECIAL) != null) {
            perct *= 1.5;
        }
        EffTemplate temp2 = p.get_EffDefault(StrucEff.PowerWing);
        if (temp2 != null) {
            perct += 3000;
        }
        //<editor-fold defaultstate="collapsed" desc="ngựa...">
        if (p.type_use_mount == Horse.SKELETON && p.id_ngua == 200){
            perct += 3500;
        }
        if (p.type_use_mount == Horse.TUAN_LOC && p.id_ngua == 100){
            perct += 3500;
        }
        if (p.type_use_mount == Horse.NGUA_XICH_THO) {
            perct += 2000;
        } else if (p.type_use_mount == Horse.TUAN_LOC) {
            perct += 4000;
        } else if (p.type_use_mount == Horse.HEO_RUNG || p.type_use_mount == Horse.CON_LAN || p.type_use_mount == Horse.CA_CHEP) {
            perct += 1000;
        } else if (p.type_use_mount == Horse.TRAU_RUNG || p.type_use_mount == Horse.MA_TOC_DO
                || p.type_use_mount == Horse.PHUONG_HOANG_LUA) {
            perct += 1500;
        } else if (p.type_use_mount == Horse.HOA_KY_LAN) {
            perct += 3500;
        }
        //</editor-fold>
        return perct;
    }

    @Override
    public int get_DameBase() {
        return get_param_view_in4(40);
    }

    @Override
    public int get_DameProp(int type) {
        if (type == 0) {
            long dame = total_item_param(0);
            switch (p.clazz) {
                case 0, 1: {
                    dame += get_point(1) * 4;
                    break;
                }
                case 2, 3: {
                    dame += get_point(4) * 4;
                    break;
                }
            }
            if (p.get_EffDefault(StrucEff.LoiPhat) != null){
                dame *= 1.3;
            }
            dame += ((dame * (get_PercentDameProp(0) / 100)) / 100);
            if (dame > 2_000_000_000) {
                dame = 2_000_000_000;
            }
            return (int) dame;
        }
        long dprop = 0;
        switch (p.clazz) {
            case 0: {
                if (type == 2) {
                    dprop += get_point(1) * 4L;
                    dprop += total_item_param(2);
                }
                break;
            }
            case 1: {
                if (type == 4) {
                    dprop += get_point(1) * 4L;
                    dprop += total_item_param(4);
                }
                break;
            }
            case 2: {
                if (type == 1) {
                    dprop += get_point(4) * 4L;
                    dprop += total_item_param(1);
                }
                break;
            }
            case 3: {
                if (type == 3) {
                    dprop += get_point(4) * 4L;
                    dprop += total_item_param(3);
                }
                break;
            }
        }
        if (p.get_EffDefault(StrucEff.LuaHoaNguc) != null){
            dprop *= 1.15;
        }
        dprop += ((dprop * (get_PercentDameProp(type) / 100)) / 100);
        if (dprop > 2_000_000_000) {
            dprop = 2_000_000_000;
        }
        return (int) dprop;
    }

    public int get_skill_point_plus(int i) {
        int par = 0;
        if (i >= 1 && i <= 8 || i == 19 || i == 20 || i == 17) {
            par = total_item_param(37);
        }
        if ((i >= 9 && i <= 16) || i == 18) {
            par = total_item_param(38);
        }
        return (par > 5) ? 5 : par;
    }

    @Override
    public int get_PercentDefProp(int type) {
        int param = total_item_param(type) + total_skill_param(type);
        EffTemplate ef = p.get_EffDefault(4);
        if (ef != null) {
            param -= 1000;
        }
        if (param < 0) {
            param = 0;
        }
        if (p.getEffTinhTu(EffTemplate.SPECIAL) != null) {
            param += 4000;
        }
        if (p.get_EffDefault(StrucEff.NOI_TAI_VAT_LY) != null) {
            param -= 1000;
        }
        if (param < 0) {
            param = 0;
        }
        return param;
    }

    @Override
    public void SetDie(Map map, MainObject mainAtk) throws IOException {
        if (map.map_id == 87) {
            ChiemThanhManager.PlayerDie(p);
        }
        if (map.map_id == 102 && map.kingCupMap != null && map.kingCupMap.timeWait < System.currentTimeMillis()) {
            Player p0 = (Player) mainAtk;
            map.kingCupMap.end_round();
            p0.countWin++;
            map.kingCupMap.send_notify(String.format("%s đã chiến thắng %s trong hiệp thi đấu thứ %s", mainAtk.name, p.name, map.kingCupMap.countMatch));
        }
        p.time_die = System.currentTimeMillis();
        p.dame_affect_special_sk = 0;
        p.hp = 0;
        p.isDie = true;
        if (!Horse.isHorseClan(p.type_use_mount)) {
            p.type_use_mount = -1;
            map.send_mount(p);
        }
        Player pATK = mainAtk.isPlayer() ? (Player) mainAtk : null;
        if (p.isLiveSquire) {
            Squire.squireLeaveMap(p);
            p.isLiveSquire = false;
        }
        if (map.isMapLangPhuSuong()) {
            if (pATK!= null && pATK.isPlayer()) {
                pATK.langPhuSuong();
            }
            p.langPhuSuong();
        }
        if (pATK != null) {
            if (pATK.list_enemies.contains(this.name)) {
                pATK.list_enemies.remove(this.name);
                MapService.SendChat(map, pATK, "Này Thì Cắn :v", true);
            } else if (p.typepk == -1) {
                if (!p.list_enemies.contains(pATK.name)) {
                    p.list_enemies.add(pATK.name);
                    if (p.list_enemies.size() > 20) {
                        p.list_enemies.remove(0);
                    }
                }
                MapService.SendChat(map, p, "Thằng ranh con này tí bố online bố sút cho không trượt phát nào ><", true);
            }
            if (pATK.total_item_param(116) > Util.nextInt(10000)) {
                p.veLang();
                Service.send_notice_nobox_white(pATK.conn, "Bạn đã đánh đối thủ đăng xuất");
            }
        }
    }

    public boolean mienST(byte type_dame) {
        return this.total_item_param(-123 + type_dame) > Util.random(10000);
    }

    public boolean isEffTinhTu(int id) {
        return this.total_item_param(id) > Util.random(10000);
    }

    @Override
    public int get_TypeObj() {
        return 0;
    }

    @Override
    public void update(Map map) {
        try {
            if (isDie) {
                return;
            }
            //<editor-fold defaultstate="collapsed" desc="auto +hp,mp       ...">
            EffTemplate vet_thuong_sau = p.get_EffDefault(StrucEff.VET_THUONG_SAU);
            EffTemplate te_cong = p.get_EffDefault(StrucEff.TE_CONG);
            int percent = p.body.total_skill_param(29) + p.body.total_item_param(29);
            if (p.get_EffDefault(EffTemplate.buffhoihp) != null || get_EffDefault(EffTemplate.bufftatca) != null){
                percent += 2000;
            }
            if (p.time_buff_hp < System.currentTimeMillis() && vet_thuong_sau == null && te_cong == null) {
                p.time_buff_hp = System.currentTimeMillis() + 5000L;
                if (percent > 0 && p.hp < p.body.get_HpMax()) {
                    long param = (((long) p.body.get_HpMax()) * (percent / 100)) / 100;
                    Service.usepotion(p, 0, param);
                }
            }
            EffTemplate eff_tan_phe = p.getEffTinhTu(EffTemplate.TAN_PHE);
            percent = p.body.total_skill_param(30) + p.body.total_item_param(30);
            if (p.time_buff_mp < System.currentTimeMillis() && eff_tan_phe == null) {
                p.time_buff_mp = System.currentTimeMillis() + 5000L;
                if (percent > 0 && p.mp < p.body.get_MpMax()) {
                    long param = (((long) p.body.get_MpMax()) * (percent / 100)) / 100;
                    Service.usepotion(p, 1, param);
                }
            }
            //</editor-fold>    auto +hp,mp

            //<editor-fold defaultstate="collapsed" desc="eff Player       ...">  
            long _time = System.currentTimeMillis();
            if (get_EffMe_Kham(StrucEff.BongLua) != null && !p.isDie) {
                Service.usepotion(p, 0, (int) -(p.hp * Util.random(5, 10) * 0.01));
//                p.hp -= (int) (p.hp * Util.random(5, 10) * 0.01);
                if (p.hp <= 0) {
                    p.hp = 1;
                }
//                Service.send_char_main_in4(p);
            }
            if (get_EffMe_Kham(StrucEff.BongLanh) != null) {
                Service.usepotion(p, 1, (int) -(p.mp * Util.random(5, 10) * 0.01));
//                p.mp -= (int) (p.mp * Util.random(5, 10) * 0.01);
//                Service.send_char_main_in4(p);
            }
            if (get_EffDefault(StrucEff.VET_THUONG_SAU) != null){
                Service.usepotion(p, 0, (int) -(p.hp * 3 * 0.01));
            }
            if (getEffTinhTu(EffTemplate.THIEU_CHAY) != null) {
                Service.usepotion(p, 0, -1500);
                if (p.hp <= 0) {
                    p.hp = 1;
                }
            }
            EffTemplate eff = get_EffDefault(StrucEff.NOI_TAI_DOC);
            if (eff != null) {
                Service.usepotion(p, 0, -eff.param);
            }
            if (p.hp <= 0 && !p.isDie) {
                p.hp = 0;
                p.isDie = true;
            }
            //</editor-fold>    eff Player
            if (map.zone_id == 1 && !Map.is_map_not_zone2(map.map_id) && !p.isSquire) {
                EffTemplate ff = get_EffDefault(-127);
                if (ff == null) {
                    Map m = Map.get_map_by_id(map.map_id)[0];
                    MapService.leave(map, p);
                    p.map = m;
                    MapService.enter(p.map, p);
                }
            }
            if (p.pet_di_buon != null && p.pet_di_buon.id_map == p.map.map_id && p.map.zone_id == p.map.maxzone
                    && !p.pet_di_buon.item.isEmpty() && map.time_add_bot < System.currentTimeMillis()) {
                Bot bot = new Bot(map.baseID--, p);
                bot.target = p;
                for (int i = 0; i < map.players.size(); i++) {
                    if (map.players.get(i) != null) {
                        bot.send_info(map.players.get(i));
                    }
                }
                map.time_add_bot = System.currentTimeMillis() + 15000L;
                map.bots.add(bot);
            }
            if (map.isMapLangPhuSuong() && !p.isSquire) {
                EffTemplate ef = get_EffDefault(-128);
                if (ef == null) {
                    p.langPhuSuong();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
