package event_daily;

import ai.NhanBan;
import client.Clan;
import core.Log;
import core.Manager;
import core.SQL;
import core.Service;
import core.Util;
import io.Message;
import io.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import map.Eff_player_in_map;
import map.Map;
import map.MapService;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import template.*;

public class ChiemMo {
    
    private boolean running;
    private List<Mob_MoTaiNguyen> list_mo_tai_nguyen;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public List<Mob_MoTaiNguyen> getList_mo_tai_nguyen() {
        return list_mo_tai_nguyen;
    }

    public void setList_mo_tai_nguyen(List<Mob_MoTaiNguyen> list_mo_tai_nguyen) {
        this.list_mo_tai_nguyen = list_mo_tai_nguyen;
    }

    public void init() {
        this.running = false;
        this.list_mo_tai_nguyen = new ArrayList<>();
        int[] x_ = new int[]{444, 1068, 228, 804, 516, 684, 540, 612, 1020, 444, 228, 612, 540, 492, 492, 756};
        int[] y_ = new int[]{156, 348, 516, 972, 372, 588, 588, 204, 204, 108, 372, 708, 396, 612, 420, 300};
        int[] map_ = new int[]{3, 5, 8, 9, 11, 12, 15, 16, 19, 21, 22, 24, 26, 27, 37, 42};
        String[] name_ = new String[]{"Mỏ Vàng", "Mỏ Tri Thức", "Mỏ Ngọc", "Mỏ Tri Thức", "Mỏ Vàng", "Mỏ Vàng",
            "Mỏ Tri Thức", "Mỏ Vàng", "Mỏ Vàng", "Mỏ Ngọc", "Mỏ Tri Thức", "Mỏ Vàng", "Mỏ Tri Thức", "Mỏ Ngọc",
            "Mỏ Vàng", "Mỏ Ngọc"};
        for (int i = 0; i < x_.length; i++) {
            this.list_mo_tai_nguyen.add(new Mob_MoTaiNguyen((i - 19), x_[i], y_[i], 4_000_000, 4_000_000, 120,
                    Map.get_map_by_id(map_[i])[4], name_[i]));
        }
        Manager.gI().list_nhanban.clear();
    }

    public Mob_MoTaiNguyen get_mob_in_map(Map map) {
        for (Mob_MoTaiNguyen mob_MoTaiNguyen : list_mo_tai_nguyen) {
            if (mob_MoTaiNguyen.map.equals(map)) {
                return mob_MoTaiNguyen;
            }
        }
        return null;
    }

    public void mo_open_atk() {
        ResetChiemMo();
        setRunning(true);
        for (Mob_MoTaiNguyen mob_MoTaiNguyen : list_mo_tai_nguyen) {
            mob_MoTaiNguyen.is_atk = true;
        }
    }

    public void mo_close_atk() throws IOException {
        setRunning(false);
        for (Mob_MoTaiNguyen mob_MoTaiNguyen : list_mo_tai_nguyen) {
            mob_MoTaiNguyen.is_atk = false;
            mob_MoTaiNguyen.Set_hpMax(10_000_000);
            mob_MoTaiNguyen.hp = mob_MoTaiNguyen.get_HpMax();
            //
            MainObject.upHP(mob_MoTaiNguyen.map, mob_MoTaiNguyen, 0);
        }
    }
    

    public void harvest_all() {
        for (Mob_MoTaiNguyen mob_MoTaiNguyen : list_mo_tai_nguyen) {
            if (mob_MoTaiNguyen.clan != null) {
                switch (mob_MoTaiNguyen.name_monster) {
                    case "Mỏ Vàng": {
                        mob_MoTaiNguyen.clan.vang += 25_000;
                        break;
                    }
                    case "Mỏ Ngọc": {
                        mob_MoTaiNguyen.clan.kimcuong += 50;
                        break;
                    }
                    case "Mỏ Tri Thức": {
                        mob_MoTaiNguyen.clan.exp += 50_000;
                        break;
                    }
                }
            }
        }
    }
    
    public void ResetChiemMo(){
        try{
            synchronized(list_mo_tai_nguyen){
                if(list_mo_tai_nguyen!=null)
                    list_mo_tai_nguyen.clear();
                init();
            }
            Clan.ResetMoTaiNguyen();
        }catch(Exception e){
            core.Log.gI().add_Log_Server("ChiemMo", "Reset: "+e.getMessage());
        }
    }
    
    public boolean LoadData(){
        try (Connection connection = SQL.gI().getConnection(); 
            Statement st = connection.createStatement(); 
            ) {
            for(Mob_MoTaiNguyen m:list_mo_tai_nguyen){
                ResultSet rs = st.executeQuery("SELECT * FROM `chiem_mo` WHERE `idx` = '" + m.index + "' ;");
                if(rs.next())
                {
                    m.isbuff_hp = rs.getBoolean("isbuff_hp");
                    String nameClan = rs.getString("name_clan");
                    String nb = rs.getString("nhanban");
                    String nbs = rs.getString("nhanban_save");
                    if(nameClan == null)continue;
                    
                    JSONArray jar = null;
                    if(nb!=null){
                        jar = (JSONArray) JSONValue.parse(nb);
                        if(jar != null && !jar.isEmpty())
                        {
                            m.nhanban = new NhanBan(jar);
                            Manager.gI().add_list_nhanbban(m.nhanban);
                        }
                    }
                    if(jar!=null)
                        jar.clear();
                    if(nbs!=null){
                        jar = (JSONArray) JSONValue.parse(nbs);
                        if(jar != null && !jar.isEmpty())
                            m.nhanban_save = new NhanBan(jar);
                    }
                    if(jar!=null)
                        jar.clear();
                    
                    for(Clan c: Clan.entrys){
                        if(c.name_clan.equals(nameClan)){
                            c.add_mo_tai_nguyen(m);
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public void SaveData(Connection connection){
        try (
                //Connection connection = SQL.gI().getConnection(); 
            Statement st = connection.createStatement(); 
            //Statement ps = connection.createStatement(); 
            PreparedStatement ps = connection.prepareStatement("UPDATE `chiem_mo` SET `isbuff_hp` = ?, `name_clan` = ?, `nhanban` = ?, `nhanban_save` = ?  WHERE `idx` = ?;");
            ) {
            synchronized (list_mo_tai_nguyen) {
                for(Mob_MoTaiNguyen m:list_mo_tai_nguyen){
                    ResultSet rs = st.executeQuery("SELECT `idx` FROM `chiem_mo` WHERE `idx` = '" + m.index + "' ;");
                    if(!rs.next())
                    {//chưa có
                        String query
                        = "INSERT INTO `chiem_mo` (`idx`) VALUES ('"+ m.index +"')";
                        if (st.executeUpdate(query) > 0) {
                            connection.commit();
                        }
                    }else{
                        ps.setBoolean(1, m.isbuff_hp );
                        if(m.clan!=null)
                            ps.setString(2, m.clan.name_clan);
                        else
                            ps.setString(2, null);
                        if(m.nhanban != null)
                            ps.setString(3, m.nhanban.GetData().toJSONString());
                        else
                            ps.setString(3, null);
                        if(m.nhanban_save != null)
                            ps.setString(4, m.nhanban_save.GetData().toJSONString());
                        else
                            ps.setString(4, null);
                        ps.setInt(5, m.index);
                        ps.executeUpdate();
                        ps.clearParameters();
                    }
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
