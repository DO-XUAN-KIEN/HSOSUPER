//package core;
//
//import client.Player;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONValue;
//import template.Item3;
//import template.Item47;
//import template.ItemTemplate3;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public class quanap {
//    public static void get_qua(Player p, byte index) throws IOException {
//        switch (index) {
//            case 0: { // 1l
//                if(p.check_nap()>= 100_000) {
//                    String text = "moc100";
//                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `quanap` WHERE `name` = '" + text + "';")) {
//                        byte empty_box = (byte) 0;
//                        if (!rs.next()) {
//                            Service.send_notice_box(p.conn, "Không Thấy Quà Hoặc chưa nạp đủ");
//                            return;
//                        }
//                        String mess = rs.getString("logger");
//                        empty_box = rs.getByte("empty_box");
//                        byte date = rs.getByte("date");
//                        if (p.item.get_bag_able() >= empty_box) {
//                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                if (jsar2 == null || jsar2.size() < 1) {
//                                    continue;
//                                }
//                                Item3 itbag = new Item3();
//                                short it = Short.parseShort(jsar2.get(0).toString());
//                                itbag.id = it;
//                                itbag.name = ItemTemplate3.item.get(it).getName();
//                                itbag.clazz = ItemTemplate3.item.get(it).getClazz();
//                                itbag.type = ItemTemplate3.item.get(it).getType();
//                                itbag.level = ItemTemplate3.item.get(it).getLevel();
//                                itbag.icon = ItemTemplate3.item.get(it).getIcon();
//                                itbag.op = new ArrayList<>();
//                                itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
//                                itbag.color = ItemTemplate3.item.get(it).getColor();
//                                itbag.part = ItemTemplate3.item.get(it).getPart();
//                                itbag.tier = 0;
//                                itbag.time_use = 0;
//                                itbag.islock = false;
//                                if (date > 0) {
//                                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
//                                }
//                                p.item.add_item_bag3(itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 4;
//                                p.item.add_item_bag47(4, itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 7;
//                                p.item.add_item_bag47(7, itbag);
//
//                            }
//                            jsar.clear();
//                            p.update_vang(rs.getLong("vang"));
//                            p.update_ngoc(rs.getLong("ngoc"));
//                            p.update_coin(rs.getInt("coin"));
//                            Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
//                            p.item.char_inventory(5);
//                            p.item.char_inventory(3);
//                            p.item.char_inventory(4);
//                            p.item.char_inventory(7);
//                            p.update_nap(-100_000);
//                            Service.send_notice_box(p.conn, mess);
//                        } else {
//                            Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Service.send_notice_box(p.conn,"Không đủ điểm để đổi");
//                }
//                break;
//            }
//            case 1: {// 2l
//                if(p.check_nap()>= 200_000) {
//                    String text = "moc200";
//                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `quanap` WHERE `name` = '" + text + "';")) {
//                        byte empty_box = (byte) 0;
//                        if (!rs.next()) {
//                            Service.send_notice_box(p.conn, "Không Thấy Quà Hoặc chưa nạp đủ");
//                            return;
//                        }
//                        String mess = rs.getString("logger");
//                        empty_box = rs.getByte("empty_box");
//                        byte date = rs.getByte("date");
//                        if (p.item.get_bag_able() >= empty_box) {
//                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                if (jsar2 == null || jsar2.size() < 1) {
//                                    continue;
//                                }
//                                Item3 itbag = new Item3();
//                                short it = Short.parseShort(jsar2.get(0).toString());
//                                itbag.id = it;
//                                itbag.name = ItemTemplate3.item.get(it).getName();
//                                itbag.clazz = ItemTemplate3.item.get(it).getClazz();
//                                itbag.type = ItemTemplate3.item.get(it).getType();
//                                itbag.level = ItemTemplate3.item.get(it).getLevel();
//                                itbag.icon = ItemTemplate3.item.get(it).getIcon();
//                                itbag.op = new ArrayList<>();
//                                itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
//                                itbag.color = ItemTemplate3.item.get(it).getColor();
//                                itbag.part = ItemTemplate3.item.get(it).getPart();
//                                itbag.tier = 0;
//                                itbag.time_use = 0;
//                                itbag.islock = true;
//                                if (date > 0) {
//                                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
//                                }
//                                p.item.add_item_bag3(itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 4;
//                                p.item.add_item_bag47(4, itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 7;
//                                p.item.add_item_bag47(7, itbag);
//
//                            }
//                            jsar.clear();
//                            p.update_vang(rs.getLong("vang"));
//                            p.update_ngoc(rs.getLong("ngoc"));
//                            p.update_coin(rs.getInt("coin"));
//                            Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
//                            p.item.char_inventory(5);
//                            p.item.char_inventory(3);
//                            p.item.char_inventory(4);
//                            p.item.char_inventory(7);
//                            p.update_nap(-200_000);
//                            Service.send_notice_box(p.conn, mess);
//                        } else {
//                            Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Service.send_notice_box(p.conn,"Không đủ điểm để đổi");
//                }
//                break;
//            }
//            case 2: { // mốc 3l
//                if(p.check_nap()>= 300_000) {
//                    String text = "moc300";
//                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `quanap` WHERE `name` = '" + text + "';")) {
//                        byte empty_box = (byte) 0;
//                        if (!rs.next()) {
//                            Service.send_notice_box(p.conn, "Không Thấy Quà Hoặc chưa nạp đủ");
//                            return;
//                        }
//                        String mess = rs.getString("logger");
//                        empty_box = rs.getByte("empty_box");
//                        byte date = rs.getByte("date");
//                        if (p.item.get_bag_able() >= empty_box) {
//                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                if (jsar2 == null || jsar2.size() < 1) {
//                                    continue;
//                                }
//                                Item3 itbag = new Item3();
//                                short it = Short.parseShort(jsar2.get(0).toString());
//                                itbag.id = it;
//                                itbag.name = ItemTemplate3.item.get(it).getName();
//                                itbag.clazz = ItemTemplate3.item.get(it).getClazz();
//                                itbag.type = ItemTemplate3.item.get(it).getType();
//                                itbag.level = ItemTemplate3.item.get(it).getLevel();
//                                itbag.icon = ItemTemplate3.item.get(it).getIcon();
//                                itbag.op = new ArrayList<>();
//                                itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
//                                itbag.color = ItemTemplate3.item.get(it).getColor();
//                                itbag.part = ItemTemplate3.item.get(it).getPart();
//                                itbag.tier = 0;
//                                itbag.time_use = 0;
//                                itbag.islock = true;
//                                if (date > 0) {
//                                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
//                                }
//                                p.item.add_item_bag3(itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 4;
//                                p.item.add_item_bag47(4, itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 7;
//                                p.item.add_item_bag47(7, itbag);
//
//                            }
//                            jsar.clear();
//                            p.update_vang(rs.getLong("vang"));
//                            p.update_ngoc(rs.getLong("ngoc"));
//                            p.update_coin(rs.getInt("coin"));
//                            Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
//                            p.item.char_inventory(5);
//                            p.item.char_inventory(3);
//                            p.item.char_inventory(4);
//                            p.item.char_inventory(7);
//                            p.update_nap(-300_000);
//                            Service.send_notice_box(p.conn, mess);
//                        } else {
//                            Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Service.send_notice_box(p.conn,"Không đủ điểm để đổi");
//                }
//                break;
//            }
//            case 3: { // mốc 4l
//                if(p.check_nap()>= 400_000) {
//                    String text = "moc400";
//                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `quanap` WHERE `name` = '" + text + "';")) {
//                        byte empty_box = (byte) 0;
//                        if (!rs.next()) {
//                            Service.send_notice_box(p.conn, "Không Thấy Quà Hoặc chưa nạp đủ");
//                            return;
//                        }
//                        String mess = rs.getString("logger");
//                        empty_box = rs.getByte("empty_box");
//                        byte date = rs.getByte("date");
//                        if (p.item.get_bag_able() >= empty_box) {
//                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                if (jsar2 == null || jsar2.size() < 1) {
//                                    continue;
//                                }
//                                Item3 itbag = new Item3();
//                                short it = Short.parseShort(jsar2.get(0).toString());
//                                itbag.id = it;
//                                itbag.name = ItemTemplate3.item.get(it).getName();
//                                itbag.clazz = ItemTemplate3.item.get(it).getClazz();
//                                itbag.type = ItemTemplate3.item.get(it).getType();
//                                itbag.level = ItemTemplate3.item.get(it).getLevel();
//                                itbag.icon = ItemTemplate3.item.get(it).getIcon();
//                                itbag.op = new ArrayList<>();
//                                itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
//                                itbag.color = ItemTemplate3.item.get(it).getColor();
//                                itbag.part = ItemTemplate3.item.get(it).getPart();
//                                itbag.tier = 0;
//                                itbag.time_use = 0;
//                                itbag.islock = false;
//                                if (date > 0) {
//                                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
//                                }
//                                p.item.add_item_bag3(itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 4;
//                                p.item.add_item_bag47(4, itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 7;
//                                p.item.add_item_bag47(7, itbag);
//
//                            }
//                            jsar.clear();
//                            p.update_vang(rs.getLong("vang"));
//                            p.update_ngoc(rs.getLong("ngoc"));
//                            p.update_coin(rs.getInt("coin"));
//                            Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
//                            p.item.char_inventory(5);
//                            p.item.char_inventory(3);
//                            p.item.char_inventory(4);
//                            p.item.char_inventory(7);
//                            p.update_nap(-400_000);
//                            Service.send_notice_box(p.conn, mess);
//                        } else {
//                            Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Service.send_notice_box(p.conn,"Không đủ điểm để đổi");
//                }
//                break;
//            }
//            case 4: { // mốc 5l
//                if(p.check_nap()>= 500_000) {
//                    String text = "moc500";
//                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `quanap` WHERE `name` = '" + text + "';")) {
//                        byte empty_box = (byte) 0;
//                        if (!rs.next()) {
//                            Service.send_notice_box(p.conn, "Không Thấy Quà Hoặc chưa nạp đủ");
//                            return;
//                        }
//                        String mess = rs.getString("logger");
//                        empty_box = rs.getByte("empty_box");
//                        byte date = rs.getByte("date");
//                        if (p.item.get_bag_able() >= empty_box) {
//                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                if (jsar2 == null || jsar2.size() < 1) {
//                                    continue;
//                                }
//                                Item3 itbag = new Item3();
//                                short it = Short.parseShort(jsar2.get(0).toString());
//                                itbag.id = it;
//                                itbag.name = ItemTemplate3.item.get(it).getName();
//                                itbag.clazz = ItemTemplate3.item.get(it).getClazz();
//                                itbag.type = ItemTemplate3.item.get(it).getType();
//                                itbag.level = ItemTemplate3.item.get(it).getLevel();
//                                itbag.icon = ItemTemplate3.item.get(it).getIcon();
//                                itbag.op = new ArrayList<>();
//                                itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
//                                itbag.color = ItemTemplate3.item.get(it).getColor();
//                                itbag.part = ItemTemplate3.item.get(it).getPart();
//                                itbag.tier = 0;
//                                itbag.time_use = 0;
//                                itbag.islock = false;
//                                if (date > 0) {
//                                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
//                                }
//                                p.item.add_item_bag3(itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 4;
//                                p.item.add_item_bag47(4, itbag);
//                            }
//                            jsar.clear();
//                            //
//                            jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
//                            for (int i = 0; i < jsar.size(); i++) {
//                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                                Item47 itbag = new Item47();
//                                itbag.id = Short.parseShort(jsar2.get(0).toString());
//                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
//                                itbag.category = 7;
//                                p.item.add_item_bag47(7, itbag);
//
//                            }
//                            jsar.clear();
//                            p.update_vang(rs.getLong("vang"));
//                            p.update_ngoc(rs.getLong("ngoc"));
//                            p.update_coin(rs.getInt("coin"));
//                            Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
//                            p.item.char_inventory(5);
//                            p.item.char_inventory(3);
//                            p.item.char_inventory(4);
//                            p.item.char_inventory(7);
//                            p.update_nap(-500_000);
//                            Service.send_notice_box(p.conn, mess);
//                        } else {
//                            Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Service.send_notice_box(p.conn,"Không đủ điểm để đổi");
//                }
//                break;
//            }
////            case 7: {
////                if (p.item.get_bag_able() < 20) {
////                    Service.send_notice_box(p.conn, "Yêu cầu hành trang phải chống hơn 20 ô!");
////                    return;
////                }
////                if(p.check_nap()>= 100_000) {
////                    String text = "moc100";
////                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `quanap` WHERE `name` = '" + text + "';")) {
////                        byte empty_box = (byte) 0;
////                        if (!rs.next()) {
////                            Service.send_notice_box(p.conn, "Không Thấy Quà Hoặc chưa nạp đủ");
////                            return;
////                        }
////                        String mess = rs.getString("logger");
////                        empty_box = rs.getByte("empty_box");
////                        byte date = rs.getByte("date");
////                        if (p.item.get_bag_able() >= empty_box) {
////                            JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
////                            for (int i = 0; i < jsar.size(); i++) {
////                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
////                                if (jsar2 == null || jsar2.size() < 1) {
////                                    continue;
////                                }
////                                Item3 itbag = new Item3();
////                                short it = Short.parseShort(jsar2.get(0).toString());
////                                itbag.id = it;
////                                itbag.name = ItemTemplate3.item.get(it).getName();
////                                itbag.clazz = ItemTemplate3.item.get(it).getClazz();
////                                itbag.type = ItemTemplate3.item.get(it).getType();
////                                itbag.level = ItemTemplate3.item.get(it).getLevel();
////                                itbag.icon = ItemTemplate3.item.get(it).getIcon();
////                                itbag.op = new ArrayList<>();
////                                itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
////                                itbag.color = ItemTemplate3.item.get(it).getColor();
////                                itbag.part = ItemTemplate3.item.get(it).getPart();
////                                itbag.tier = 0;
////                                itbag.time_use = 0;
////                                itbag.islock = false;
////                                if (date > 0) {
////                                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
////                                }
////                                p.item.add_item_bag3(itbag);
////                            }
////                            jsar.clear();
////                            //
////                            jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
////                            for (int i = 0; i < jsar.size(); i++) {
////                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
////                                Item47 itbag = new Item47();
////                                itbag.id = Short.parseShort(jsar2.get(0).toString());
////                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
////                                itbag.category = 4;
////                                p.item.add_item_bag47(4, itbag);
////                            }
////                            jsar.clear();
////                            //
////                            jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
////                            for (int i = 0; i < jsar.size(); i++) {
////                                JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
////                                Item47 itbag = new Item47();
////                                itbag.id = Short.parseShort(jsar2.get(0).toString());
////                                itbag.quantity = Short.parseShort(jsar2.get(1).toString());
////                                itbag.category = 7;
////                                p.item.add_item_bag47(7, itbag);
////
////                            }
////                            jsar.clear();
////                            p.update_vang(rs.getLong("vang"));
////                            p.update_ngoc(rs.getLong("ngoc"));
////                            Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
////                            p.item.char_inventory(5);
////                            p.item.char_inventory(3);
////                            p.item.char_inventory(4);
////                            p.item.char_inventory(7);
////
////                            Service.send_notice_box(p.conn, mess);
////                        } else {
////                            Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
////                        }
////                    } catch (SQLException e) {
////                        e.printStackTrace();
////                    }
////                }else {
////                    Service.send_notice_box(p.conn,"Không đủ điểm để đổi");
////                    break;
////                }
////                break;
////            }
//        }
//    }
//}
