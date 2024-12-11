package template;

import ai.NhanBan;
import client.Clan;
import client.Player;
import core.Manager;
import io.Message;
import java.io.IOException;
import java.util.List;

import map.Eff_player_in_map;
import map.LeaveItemMap;
import map.Map;
import map.MapService;

public class Mob_MoTaiNguyen extends MainObject{
    public Map map;
    public String name_monster;
    public List<NhanBan> nhanBans;
    public NhanBan nhanban;
    public NhanBan nhanban_save;
    public Clan clan;
    public boolean is_atk;
    public boolean isbuff_hp;
    public long time_buff;
    

    public Mob_MoTaiNguyen(int index, int x, int y, int hp, int hp_max, int level, Map map, String name_monster) {
        this.index = Short.toUnsignedInt((short) index);
        this.x = (short) x;
        this.y = (short) y;
        this.hp = hp;
        this.hp_max = hp_max;
        this.level = (short)level;
        this.map = map;
        this.name_monster = name_monster;
        this.is_atk = false;
        this.isbuff_hp = false;
    }
    @Override
    public boolean isMoTaiNguyen() {
        return true;
    }
    
    @Override
    public int get_DefBase(){
        return nhanban!= null && !nhanban.isDie ? nhanban.get_DefBase():0;
    }
    
    @Override
    public void SetDie(Map map, MainObject mainAtk){
        if(hp >0 || !mainAtk.isPlayer())return;
        try{
            this.hp = 0;
            Manager.gI()
                    .chatKTGprocess("@Server : @" + mainAtk.name + " thuộc bang "
                            + ((Player)mainAtk).myclan.name_clan_shorted.toUpperCase() + " chiếm được "
                            + this.name_monster + " tại " + map.name);
            ((Player)mainAtk).myclan.add_mo_tai_nguyen(this);
            if (this.clan != null) {
                this.clan.remove_mo_tai_nguyen(this);
            }
            this.clan = ((Player)mainAtk).myclan;
            if (this.nhanban != null) {
                Message m13 = new Message(8);
                m13.writer().writeShort(this.nhanban.index);
                for (int j = 0; j < map.players.size(); j++) {
                    map.players.get(j).conn.addmsg(m13);
                }
                m13.cleanup();
                Manager.gI().remove_list_nhanbban(this.nhanban);
            }
            this.nhanban = new NhanBan();
            this.nhanban_save = this.nhanban;
            this.nhanban.setup((Player)mainAtk);
            this.nhanban.p_skill_id = 1;
            Manager.gI().add_list_nhanbban(this.nhanban);
            Message m12 = new Message(4);
            m12.writer().writeByte(0);
            m12.writer().writeShort(0);
            m12.writer().writeShort(this.nhanban.index);
            m12.writer().writeShort(this.nhanban.x);
            m12.writer().writeShort(this.nhanban.y);
            m12.writer().writeByte(-1);
            MapService.send_msg_player_inside(map, this, m12, true);
            m12.cleanup();
            
            this.hp = this.hp_max = 10_000_000;
            //
            Message mm = new Message(7);
            mm.writer().writeShort(this.index);
            mm.writer().writeByte((byte) this.level);
            mm.writer().writeShort(this.x);
            mm.writer().writeShort(this.y);
            mm.writer().writeInt(this.hp);
            mm.writer().writeInt(this.hp_max);
            mm.writer().writeByte(0);
            mm.writer().writeInt(4);
            if (this.clan != null) {
                mm.writer().writeShort(this.clan.icon);
                mm.writer().writeInt(Clan.get_id_clan(this.clan));
                mm.writer().writeUTF(this.clan.name_clan_shorted);
                mm.writer().writeByte(122);
            } else {
                mm.writer().writeShort(-1);
            }
            mm.writer().writeUTF(this.name_monster);
            mm.writer().writeByte(0);
            mm.writer().writeByte(2);
            mm.writer().writeByte(0);
            mm.writer().writeUTF("");
            mm.writer().writeLong(-11111);
            mm.writer().writeByte(4);
            final int a = this.index;
            new Thread(() -> {
                try {
                    Thread.sleep(5500L);
                    MapService.send_msg_player_inside(map, this, mm, true);
                    mm.cleanup();
                    if(mainAtk.isPlayer())
                        Eff_player_in_map.add((Player)mainAtk, a);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }catch(Exception e){}
    }
}
