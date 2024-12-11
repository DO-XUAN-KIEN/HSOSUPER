package ai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import client.Clan;
import client.Player;
import core.Manager;
import core.Service;
import io.Message;
import map.Map;
import map.MapService;
import org.json.simple.JSONArray;
import template.Item3;
import template.MainObject;
import template.Mob_MoTaiNguyen;
import template.Part_player;

public class NhanBan extends MainObject{

    public List<Part_player> part_p;//
    private byte head;//
    private byte eye;//
    private byte hair;//
    private int pointpk;//
    private short clan_icon = -1;
    private int clan_id = -1;
    private String clan_name_clan_shorted;
    private byte clan_mem_type;
    private short[] fashion;//
    private short mat_na;//
    private short phi_phong;//
	private short danh_hieu;
    private short weapon;//
    private short id_horse;//
    private short id_hair;//
    private short id_wing;//
    private short id_img_mob=-1;//
    private byte type_use_mount;//
    public long act_time;
    public boolean is_move;//
    public Player p_target;
    public int p_skill_id;//
    
    public long timeATK;
    public long time_hp_buff;
    private int pierce;
    private int crit;

    public NhanBan() {
    }

    public NhanBan(int mapid, int id, String name, int x, int y, int clzz, int head, int eye, int hair, int lv, int hpMax, int pk, short[] fashions, int mount, int matna,
            int phiphong, int weapon, int id_horse, int id_hair, int id_wing, int danhhieu, int dame, int def, int crit, List<Part_player> part_p, int id_img_mob) {
        this.map_id = map_id;
        this.index = id;
        this.name = name;
        this.x = x_old = (short) x;
        this.y = y_old = (short) y;
        this.clazz = (byte) clzz;
        this.head = (byte) head;
        this.eye = (byte) eye;
        this.hair = (byte) hair;
        this.level = (short) lv;
        this.hp = this.hp_max = hpMax;
        this.pointpk = (byte) pk;
        if(fashions == null)
            fashions = new short[]{-1,-1,-1,-1,-1,-1,-1};
        this.fashion = fashions;
        this.type_use_mount = (byte) mount;
        this.mat_na = (short) matna;
        this.phi_phong = (short) phiphong;
        this.weapon = (short) weapon;
        this.id_horse = (short) id_horse;
        this.id_hair = (short) id_hair;
        this.id_wing = (short) id_wing;
        this.danh_hieu = (short) danhhieu;
        is_move = true;
        this.pierce = 5000;
        this.dame = dame;
        this.def = def;
        this.crit = crit;
        clan_name_clan_shorted = "";
        this.part_p = part_p;
        this.id_img_mob = (short)id_img_mob;
        
    }

    public NhanBan(JSONArray jar) {
        map_id = ((Long) jar.get(0)).byteValue();
        index = ((Long) jar.get(1)).intValue();
        x = ((Long) jar.get(2)).shortValue();
        y = ((Long) jar.get(3)).shortValue();
        name = (String) jar.get(4);
        part_p = new ArrayList<>();
        JSONArray jar2 = (JSONArray) jar.get(5);
        if (jar2 != null && !jar2.isEmpty()) {

            for (int i = 0; i < jar2.size(); i++) {
                JSONArray jar3 = (JSONArray) jar2.get(i);
                if (jar3 == null || jar3.isEmpty()) {
                    continue;
                }
                Part_player pr = new Part_player();
                pr.type = ((Long) jar.get(0)).byteValue();
                pr.part = ((Long) jar.get(1)).byteValue();
                part_p.add(pr);
                jar3.clear();
            }
            jar2.clear();
        }

        clazz = ((Long) jar.get(6)).byteValue();
        head = ((Long) jar.get(7)).byteValue();
        eye = ((Long) jar.get(8)).byteValue();
        hair = ((Long) jar.get(9)).byteValue();
        level = ((Long) jar.get(10)).shortValue();
        hp = ((Long) jar.get(11)).intValue();
        hp_max = ((Long) jar.get(12)).intValue();
        pointpk = ((Long) jar.get(13)).shortValue();
        clan_icon = ((Long) jar.get(14)).shortValue();
        clan_id = ((Long) jar.get(15)).intValue();
        clan_name_clan_shorted = (String) jar.get(16);
        clan_mem_type = ((Long) jar.get(17)).byteValue();
        jar2 = (JSONArray) jar.get(18);
        if (jar2 != null) {
            fashion = new short[jar2.size()];
            for (int i = 0; i < jar2.size(); i++) {
                fashion[i] = ((Long) jar2.get(i)).byteValue();
            }
        }
        mat_na = ((Long) jar.get(19)).shortValue();
        phi_phong = ((Long) jar.get(20)).shortValue();
        weapon = ((Long) jar.get(21)).shortValue();
        id_horse = ((Long) jar.get(22)).shortValue();
        id_hair = ((Long) jar.get(23)).shortValue();
        id_wing = ((Long) jar.get(24)).shortValue();
        danh_hieu = ((Long) jar.get(20)).shortValue();
        type_use_mount = ((Long) jar.get(25)).byteValue();
        dame = ((Long) jar.get(26)).intValue();
        act_time = (Long) jar.get(27);
        is_move = (Boolean) jar.get(28);
        p_skill_id = ((Long) jar.get(29)).intValue();
        crit = ((Long) jar.get(30)).intValue();
        time_hp_buff = (Long) jar.get(31);
        def = ((Long) jar.get(32)).intValue();
        pierce = ((Long) jar.get(33)).intValue();
    }

    public JSONArray GetData() {
        JSONArray jar = new JSONArray();
        try {
            JSONArray jar2 = new JSONArray();
            jar.add(map_id);
            jar.add(index);
            jar.add(x);
            jar.add(y);
            jar.add(name);
            if (part_p != null && !part_p.isEmpty()) {
                JSONArray jar3 = new JSONArray();
                for (Part_player pr : part_p) {
                    jar3.add(pr.type);
                    jar3.add(pr.part);
                    jar2.add(jar3);
                }

                //jar3.clear();
            }
            jar.add(jar2);
            //jar2.clear();
            jar.add(clazz);
            jar.add(head);
            jar.add(eye);
            jar.add(hair);
            jar.add(level);
            jar.add(hp);
            jar.add(hp_max);
            jar.add(pointpk);
            jar.add(clan_icon);
            jar.add(clan_id);
            jar.add(clan_name_clan_shorted);
            jar.add(clan_mem_type);
            JSONArray jar4 = new JSONArray();
            if (fashion != null) {
                for (short b : fashion) {
                    jar4.add(b);
                }
            }
            jar.add(jar4);
            //jar2.clear();
            jar.add(mat_na);
            jar.add(phi_phong);
            jar.add(weapon);
            jar.add(id_horse);
            jar.add(id_hair);
            jar.add(id_wing);
            jar.add(danh_hieu);
            jar.add(type_use_mount);
            jar.add(dame);
            jar.add(act_time);
            jar.add(is_move);

            jar.add(p_skill_id);
            jar.add(crit);
            jar.add(time_hp_buff);
            jar.add(def);
            jar.add(pierce);
        } catch (Exception e) {
            jar.clear();
            e.printStackTrace();
            core.Log.gI().add_Log_Server("ChiemMo", "Save NhanBan: " + e.getMessage());
        }

        return jar;
        //
    }

    public void setup(Player p0) {
        this.index = Short.toUnsignedInt((short) Manager.gI().get_index_mob_new());
        this.x = p0.x;
        this.y = p0.y;
        this.part_p = new ArrayList<>();
        for (int i = 0; i < p0.item.wear.length; i++) {
            Part_player temp_add = new Part_player();
            if (i != 0 && i != 1 && i != 6 && i != 7 && i != 10) {
                continue;
            }
            Item3 temp = p0.item.wear[i];
            if (temp != null) {
                temp_add.type = temp.type;
                if (i == 10 && p0.item.wear[14] != null && (p0.item.wear[14].id >= 4638 && p0.item.wear[14].id <= 4648)) {
                    temp_add.part = p0.item.wear[14].part;
                } else {
                    temp_add.part = temp.part;
                }
            }
            this.part_p.add(temp_add);
        }
        this.name = "Nhân bản - " + p0.name;
        this.clazz = p0.clazz;
        this.head = p0.head;
        this.eye = p0.eye;
        this.hair = p0.hair;
        this.level = p0.level;
        this.hp = p0.hp;
        this.hp_max = p0.body.get_HpMax();
        this.pointpk = p0.pointpk;
        this.clan_icon = p0.myclan.icon;
        this.clan_id = Clan.get_id_clan(p0.myclan);
        this.clan_name_clan_shorted = p0.myclan.name_clan_shorted;
        this.clan_mem_type = p0.myclan.get_mem_type(p0.name);
        this.fashion = p0.fashion;
        this.mat_na = Service.get_id_mat_na(p0);
        this.phi_phong = Service.get_id_phiphong(p0);
        this.weapon = Service.get_id_weapon(p0);
        this.id_horse = p0.id_horse;
        this.id_hair = Service.get_id_hair(p0);
        this.id_wing = Service.get_id_wing(p0);
        this.danh_hieu = Service.get_id_danhhieu(p0);
        this.type_use_mount = p0.type_use_mount;
        this.dame = (p0.body.get_DameProp(0) + p0.body.get_DameProp(1) + p0.body.get_DameProp(2)
                + p0.body.get_DameProp(3) + p0.body.get_DameProp(4)) / 2;
        this.map_id = p0.map.map_id;
        this.crit = p0.body.get_Crit();
        this.def = p0.body.get_DefBase();
        this.pierce = p0.body.get_Pierce();
        if (this.pierce > 5000) {
            this.pierce = 5000;
        }
        this.is_move = true;
    }

    public void send_in4(Player p) throws IOException {
        Message m = new Message(5);
        m.writer().writeShort(this.index);
        m.writer().writeUTF(this.name);
        m.writer().writeShort(this.x);
        m.writer().writeShort(this.y);
        m.writer().writeByte(this.clazz);
        m.writer().writeByte(-1);
        m.writer().writeByte(this.head);
        m.writer().writeByte(this.eye);
        m.writer().writeByte(this.hair);
        m.writer().writeShort(this.level);
        m.writer().writeInt(this.hp);
        m.writer().writeInt(this.hp_max);
        m.writer().writeByte(0); // type pk
        m.writer().writeShort(this.pointpk);
        m.writer().writeByte(this.part_p.size());
        //
        for (int i = 0; i < this.part_p.size(); i++) {
            m.writer().writeByte(this.part_p.get(i).type);
            m.writer().writeByte(this.part_p.get(i).part);
            m.writer().writeByte(3);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1);
            m.writer().writeShort(-1); // eff
        }
        //
        m.writer().writeShort(this.clan_icon);
        if(clan_icon>-1)
        {
            m.writer().writeInt(this.clan_id);
            m.writer().writeUTF(this.clan_name_clan_shorted);
            m.writer().writeByte(this.clan_mem_type);
        }
        m.writer().writeByte(-1); // pet
        m.writer().writeByte(this.fashion.length);
        for (int i = 0; i < this.fashion.length; i++) {
            if (p.conn.version >= 280) {
                m.writer().writeShort(this.fashion[i]);
            } else {
                m.writer().writeByte(this.fashion[i]);
            }
        }
        //
        m.writer().writeShort(id_img_mob);//id_img_mob
        m.writer().writeByte(this.type_use_mount);
        m.writer().writeBoolean(false);
        m.writer().writeByte(1);
        m.writer().writeByte(0);
        m.writer().writeShort(this.mat_na); // mat na
        m.writer().writeByte(1); // paint mat na trc sau
        m.writer().writeShort(this.phi_phong); // phi phong
        m.writer().writeShort(this.weapon); // weapon
        m.writer().writeShort(this.id_horse);
        m.writer().writeShort(this.id_hair); // hair
        m.writer().writeShort(this.id_wing); // wing
        m.writer().writeShort(-1); // body
        m.writer().writeShort(-1); // leg
        m.writer().writeShort(-1); // bienhinh
        p.conn.addmsg(m);
        m.cleanup();
    }
    
    @Override
    public int get_TypeObj(){
        return 0;
    }
    
    @Override
    public int get_DefBase(){
        return def;
    }
    
    @Override
    public void SetDie(Map map, MainObject mainAtk){
        try{
            if (this.hp <= 0) {
                Manager.gI().remove_list_nhanbban(this);
                this.hp = 0;
                Mob_MoTaiNguyen temp_mob = Manager.gI().chiem_mo.get_mob_in_map(map);
                if(temp_mob!=null)
                    temp_mob.nhanban = null;
            }
        }catch(Exception e){}
    }
}
