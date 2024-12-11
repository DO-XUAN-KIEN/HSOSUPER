package core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import client.Clan;
import client.Player;
import core.BXH.Memin4;
import map.Map;
import template.Level;
import template.Part_player;
import event.Event_1;
import event_daily.ChiemThanhManager;
import event_daily.Wedding;

import java.sql.DriverManager;

import map.MapService;

public class SaveData {

    @SuppressWarnings("unchecked")
    public synchronized static void process() {
        if(Manager.isServerTest)
            return;
        long time_check = System.currentTimeMillis();
        try {
//            Connection conn = SQL.gI().getConnection();
            Connection conn = DriverManager.getConnection(SQL.gI().url, Manager.gI().mysql_user, Manager.gI().mysql_pass);
            Manager.gI().chiem_mo.SaveData(conn);
            // clan
            BXH.BXH_clan.clear();
            for (Clan clan : Clan.get_all_clan()) {
                BXH.BXH_clan.add(clan);
            }
//            Collections.sort(BXH.BXH_clan, new Comparator<Clan>() {
//                @Override
//                public int compare(Clan o1, Clan o2) {
//                    int com1 = (o1.level == o2.level) ? 0 : (o1.level > o2.level) ? -1 : 1;
//                    if (com1 != 0) {
//                        return com1;
//                    }
//                    return (o1.exp >= o2.exp) ? -1 : 1;
//                }
//            });
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE `clan` SET `level` = ?, `exp` = ?, `slogan` = ?, `rule` = ?, `mems` = ?, `item` = ?, `notice` = ?, `vang` = ?, `kimcuong` = ?, `icon` = ?, `max_mem` = ? WHERE `name` = ?;");
            // clan
            List<Clan> list_to_remove = new ArrayList<>();
            for (int i = 0; i < Clan.entrys.size(); i++) {
                Clan clan = Clan.entrys.get(i);
                if (clan.mems.size() < 1) {
                    list_to_remove.add(clan);
                    Clan.entrys.remove(clan);
                    i--;
                } else {
                    ps.clearParameters();
                    ps.setInt(1, clan.level);
                    ps.setLong(2, clan.exp);
                    ps.setNString(3, clan.slogan );
                    ps.setNString(4, clan.rule);
                    ps.setNString(5, Clan.flush_mem_json(clan.mems));
                    ps.setNString(6, Clan.flush_item_json(clan.item_clan));
                    ps.setNString(7, clan.notice);
                    ps.setLong(8, clan.get_vang());
                    ps.setInt(9, clan.get_ngoc());
                    ps.setInt(10, clan.icon);
                    ps.setInt(11, clan.max_mem);
                    ps.setNString(12, clan.name_clan);
                    ps.executeUpdate();
//                    ps.addBatch();
//                    if (i % 50 == 0) {
//                        ps.executeBatch();
//                    }
                }
            }
//            ps.executeBatch();
            //
            // save chiến trường
            ps.close();
            ps = conn.prepareStatement("UPDATE `config_server` SET `data1` = ?,`data2` = ? WHERE `name` = ?;");
            ChiemThanhManager.SaveData(ps);
            
            ps.close();
            ps = conn.prepareStatement("DELETE FROM `clan` WHERE `name` = ?;");
            for (int i = 0; i < list_to_remove.size(); i++) {
                Clan clan = list_to_remove.get(i);
                ps.clearParameters();
                ps.setNString(1, clan.name_clan);
                ps.executeUpdate();
//                ps.addBatch();
//                if (i % 50 == 0) {
//                    ps.executeBatch();
//                }
            }
            ps.close();
            ps = conn.prepareStatement("UPDATE `wedding` SET `item` = ? WHERE `name` = ?;");
            for (int i = 0; i < Wedding.list.size(); i++) {
                Wedding temp = Wedding.list.get(i);
                ps.clearParameters();
                JSONArray js2 = new JSONArray();
                js2.add(temp.exp);
                js2.add(temp.it.color);
                js2.add(temp.it.tier);
                JSONArray js22 = new JSONArray();
                for (int i2 = 0; i2 < temp.it.op.size(); i2++) {
                    JSONArray js23 = new JSONArray();
                    js23.add(temp.it.op.get(i2).id);
                    js23.add(temp.it.op.get(i2).getParam(0));
                    js22.add(js23);
                }
                js2.add(js22);
                ps.setNString(1, js2.toJSONString());
                JSONArray js = new JSONArray();
                js.add(temp.name_1);
                js.add(temp.name_2);
                ps.setNString(2, js.toJSONString());
                ps.execute();
            }
            ps.close();
            // flush player
            String query
                    = "UPDATE `player` SET `level` = ?, `exp` = ?, `site` = ?, `body` = ?, `eff` = ?, `friend` = ?, `skill` = ?, `item4` = ?, "
                    + "`item7` = ?, `item3` = ?, `itemwear` = ?, `giftcode` = ?, `enemies` = ?, `rms_save` = ?, `itembox4` = ?, "
                    + "`itembox7` = ?, `itembox3` = ?, `pet` = ?, `medal_create_material` = ?, `point_active` = ?, `vang` = ?, "
                    + "`kimcuong` = ?, `tiemnang` = ?, `kynang` = ?, `diemdanh` = ?, `chucphuc` = ?, `hieuchien` = ?, `typeexp` = ?, "
                    + "`date` = ?, `point1` = ?, `point2` = ?, `point3` = ?, `point4` = ?,`diemsukien` = ?,`dibuon` = ?  WHERE `id` = ?;";
            ps = conn.prepareStatement(query);
            for (Map[] map : Map.entrys) {
                if(map == null)continue;
                for (Map map0 : map) {
                    if(map0 == null || map0.players == null )continue;
                    for (int i1 = 0; i1 < map0.players.size(); i1++) {
                        // for (int i1 = 0; i1 < ServerManager.gI().t1.list_p.size(); i1++) {
                        try
                        {
                            ps.clearParameters();
                            Player p0 = map0.players.get(i1);
                            if(p0 == null || p0.conn == null || p0.conn.socket == null || p0.conn.socket.isClosed() || !p0.conn.connected)
                            {
                                MapService.leave(map0, p0);
                                continue;
                            }
                            p0.flush();
                        }catch(Exception ee){
                            Log.gI().add_Log_Server("save_data", ee.getMessage());
                            ee.printStackTrace();
                        }
                    }
                }
            }
            
            
//            try
//            {
//                SessionManager.checkBugAccount();
//            }catch(Exception eee){}
            // }
//            ps.executeBatch();
            ps.close();
            // event
            if (Manager.gI().event == 1) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, Event_1.SaveData().toJSONString());
                ps.setInt(2, 0);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 2) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_2.SaveData().toJSONString());
                ps.setInt(2, 1);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 3) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_3.SaveData().toJSONString());
                ps.setInt(2, 2);
                ps.executeUpdate();
                ps.close();
            }
             else if (Manager.gI().event == 4) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_4.SaveData().toJSONString());
                ps.setInt(2, 3);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 5) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_5.SaveData().toJSONString());
                ps.setInt(2, 4);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 6) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_6.SaveData().toJSONString());
                ps.setInt(2, 5);
                ps.executeUpdate();
                ps.close();
            }
            else if (Manager.gI().event == 8) {
                ps = conn.prepareStatement("UPDATE `event` SET `data` = ? WHERE `id` = ?;");
                ps.clearParameters();
                //
                ps.setNString(1, ev_he.Event_8.SaveData().toJSONString());
                ps.setInt(2, 6);
                ps.executeUpdate();
                ps.close();
            }
            // bxh
            BXH.BXH_level.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `exp`, `name`, `body`, `itemwear` FROM `player` WHERE `level` > 10 ORDER BY `level` DESC, exp DESC LIMIT 20;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                
                Memin4 temp = new Memin4();
                temp.level = rs.getShort("level");
                temp.exp = rs.getLong("exp");
                temp.name = rs.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rs.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percent
                        = String.format("%.1f", (((float) temp.exp * 1000) / Level.entrys.get(temp.level - 1).exp) / 10f);
                temp.info = "Level : " + (temp.level) + "\t-\t" + percent + "%";
                BXH.BXH_level.add(temp);
            }
            rs.close();
            // bxh chiến trường
            BXH.BXH_Ctr.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `point_arena` FROM `player` WHERE `point_arena` > 10 ORDER BY  point_arena DESC LIMIT 20;");
            ResultSet rp = ps.executeQuery();
            while (rp.next()) {
                Memin4 temp = new Memin4();
                temp.level = rp.getShort("level");
                temp.point_arena= rp.getInt("point_arena");
                temp.name = rp.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rp.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rp.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.point_arena)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_Ctr.add(temp);
            }
            rp.close();
            // bxh đổi quà ngọc rồng
            BXH.BXH_doiqua.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `doiqua` FROM `player` WHERE `doiqua` > 1 ORDER BY  doiqua DESC LIMIT 20;");
            ResultSet ri = ps.executeQuery();
            while (ri.next()) {
                Memin4 temp = new Memin4();
                temp.level = ri.getShort("level");
                temp.doiqua= ri.getInt("doiqua");
                temp.name = ri.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(ri.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(ri.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.doiqua)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_doiqua.add(temp);
            }
            ri.close();
            BXH.BXH_hongio.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `sk_hongio` FROM `player` WHERE `sk_hongio` > 1 ORDER BY  sk_hongio DESC LIMIT 20;");
            ri = ps.executeQuery();
            while (ri.next()) {
                Memin4 temp = new Memin4();
                temp.level = ri.getShort("level");
                temp.sk_hongio= ri.getInt("sk_hongio");
                temp.name = ri.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(ri.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(ri.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.sk_hongio)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_hongio.add(temp);
            }
            ri.close();
            // bxh danh vọng
            BXH.BXH_Danhvong.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `danhvong` FROM `player` WHERE `danhvong` > 1 ORDER BY  danhvong DESC LIMIT 20;");
            ResultSet rt= ps.executeQuery();
            while (rt.next()) {
                Memin4 temp = new Memin4();
                temp.level = rt.getShort("level");
                temp.danhvong= rt.getInt("danhvong");
                temp.name = rt.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rt.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rt.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.danhvong)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_Danhvong.add(temp);
            }
            rt.close();
            // bxh phó bản
            BXH.BXH_phoban.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `phoban` FROM `player` WHERE `phoban` > 1 ORDER BY  phoban DESC LIMIT 20;");
            ResultSet qw= ps.executeQuery();
            while (qw.next()) {
                Memin4 temp = new Memin4();
                temp.level = qw.getShort("level");
                temp.phoban= qw.getInt("phoban");
                temp.name = qw.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(qw.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(qw.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.phoban)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_phoban.add(temp);
            }
            qw.close();
            // bxh boss
            BXH.BXH_boss.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `boss` FROM `player` WHERE `boss` > 1 ORDER BY  boss DESC LIMIT 20;");
            ResultSet qt= ps.executeQuery();
            while (qt.next()) {
                Memin4 temp = new Memin4();
                temp.level = qt.getShort("level");
                temp.boss= qt.getInt("boss");
                temp.name = qt.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(qt.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(qt.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.boss)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_boss.add(temp);
            }
            qt.close();
            // bxh cướp
            BXH.BXH_Cuop.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `dicuop` FROM `player` WHERE `dicuop` > 1 ORDER BY  dicuop DESC LIMIT 20;");
            ResultSet rq = ps.executeQuery();
            while (rq.next()) {
                Memin4 temp = new Memin4();
                temp.level = rq.getShort("level");
                temp.dicuop= rq.getInt("dicuop");
                temp.name = rq.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rq.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rq.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.dicuop)));
                temp.info = "Level : " + (temp.level) + " - Điểm: " + percents +" ";
                BXH.BXH_Cuop.add(temp);
            }
            rq.close();
            // bxh buôn
            BXH.BXH_Buon.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `dibuon` FROM `player` WHERE `dibuon` > 10 ORDER BY  dibuon DESC LIMIT 20;");
            ResultSet ro = ps.executeQuery();
            while (ro.next()) {


                Memin4 temp = new Memin4();
                temp.level = ro.getShort("level");
                temp.dibuon= ro.getInt("dibuon");
                temp.name = ro.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(ro.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(ro.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.dibuon)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_Buon.add(temp);
            }
            ro.close();
//          // bxh đồ sát
            BXH.BXH_ds.clear();
            ps = conn.prepareStatement(
                    "SELECT`level`, `name`, `body`, `itemwear`, `hieuchien` FROM `player` WHERE `hieuchien` > 10 ORDER BY  hieuchien DESC LIMIT 20;");
            ResultSet rw = ps.executeQuery();
            while (rw.next()) {
                Memin4 temp = new Memin4();
                temp.level = rw.getShort("level");
                temp.hieuchien= rw.getInt("hieuchien");
                temp.name = rw.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rw.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rw.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.hieuchien)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_ds.add(temp);
            }
            // bxh lôi đài
            BXH.BXH_ld.clear();
            ps = conn.prepareStatement(
                    "SELECT `id`, `level`, `name`, `body`, `itemwear`, `point_king_cup` FROM `player` WHERE `point_king_cup` > 1 ORDER BY  point_king_cup DESC LIMIT 20;");
            ResultSet rc = ps.executeQuery();
            while (rc.next()) {
                Memin4 temp = new Memin4();
                temp.level = rc.getShort("level");
                temp.point_king_cup= rc.getInt("point_king_cup");
                temp.name = rc.getString("name");
                JSONArray jsar = (JSONArray) JSONValue.parse(rc.getString("body"));
                if (jsar == null) {
                    return;
                }
                temp.head = Byte.parseByte(jsar.get(0).toString());
                temp.hair = Byte.parseByte(jsar.get(2).toString());
                temp.eye = Byte.parseByte(jsar.get(1).toString());
                jsar.clear();
                jsar = (JSONArray) JSONValue.parse(rc.getString("itemwear"));
                if (jsar == null) {
                    return;
                }
                temp.itemwear = new ArrayList<>();
                for (int i3 = 0; i3 < jsar.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i3).toString());
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 6 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }
                    Part_player temp2 = new Part_player();
                    temp2.type = Byte.parseByte(jsar2.get(2).toString());
                    temp2.part = Byte.parseByte(jsar2.get(6).toString());
                    temp.itemwear.add(temp2);
                }
                temp.clan = Clan.get_clan_of_player(temp.name);
                String percents
                        = String.format("%.0f", (((float) temp.point_king_cup)));
                temp.info = "Level : " + (temp.level) + "\t-\t" + percents +" ";
                BXH.BXH_ld.add(temp);
            }
            rc.close();
            rw.close();
            //

            Map.head = -1;
            Map.eye = -1;
            Map.hair = -1;
            Map.weapon = -1;
            Map.body = -1;
            Map.leg = -1;
            Map.hat = -1;
            Map.wing = -1;
            Map.name_mo = "";
             rs = ps.executeQuery("SELECT * FROM `player` ORDER BY `level` DESC, `id` LIMIT 1");
//            rs = ps.executeQuery("SELECT * FROM `player` ORDER BY `hieuchien` DESC, `id` LIMIT 1");
  //          rs = ps.executeQuery("SELECT * FROM `player` WHERE `name` = '"+Manager.VuaChienTruong+"' LIMIT 1");
            if (rs.next()) {
                Map.name_mo = rs.getString("name");
                JSONArray js = (JSONArray) JSONValue.parse(rs.getString("body"));
                Map.head = Short.parseShort(js.get(0).toString());
                Map.eye = Short.parseShort(js.get(1).toString());
                Map.hair = Short.parseShort(js.get(2).toString());
                js.clear();
                js = (JSONArray) JSONValue.parse(rs.getString("itemwear"));
                for (int i3 = 0; i3 < js.size(); i3++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(js.get(i3).toString());
                    if (jsar2 == null) {
                        return;
                    }
                    byte index_wear = Byte.parseByte(jsar2.get(9).toString());
                    if (index_wear != 0 && index_wear != 1 && index_wear != 2 && index_wear != 7 && index_wear != 10) {
                        continue;
                    }

                    Part_player temp = new Part_player();
                    temp.type = Byte.parseByte(jsar2.get(2).toString());
                    temp.part = Byte.parseByte(jsar2.get(6).toString());
                    if (temp.type == 2) {
                        Map.hat = temp.part;
                    }
                    if (temp.type == 0) {
                        Map.body = temp.part;
                    }
                    if (temp.type == 1) {
                        Map.leg = temp.part;
                    }
                    if (temp.type == 7) {
                        Map.wing = temp.part;
                    }
                    if (temp.type == 10) {
                        Map.weapon = temp.part;
                    }
                }

            }
            
            //
//            conn.commit();
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[" + Util.get_now_by_time() + "] Save__Data__Fail!");
            return;
        }
        System.out.println("[" + Util.get_now_by_time() + "] Save__Data__Ok ___-->" + (System.currentTimeMillis() - time_check));
        ServerManager.gI().time_l = System.currentTimeMillis()+60_000L;
    }
}
