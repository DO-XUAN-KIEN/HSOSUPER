///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package core;
//
//import client.Item;
//import client.Player;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONValue;
//import template.Item3;
//import template.Item47;
//import template.ItemTemplate3;
//import template.Option;
//
///**
// *
// * @author chien
// */
//public class tools {
//    public static void loadacc()
//    {
//////        String query = "SELECT * FROM `player` WHERE `name` = 'AD Support'";
////        String query = "SELECT * FROM `player`";
////        try ( Connection connection = SQL.gI().getConnection();  Statement ps = connection.createStatement();  ResultSet rs = ps.executeQuery(query))
////        {
////            while (rs.next()) {
////                boolean bug =false;
////                String a = "`exp` = " + 0;
////                int id = rs.getInt("id");
////                short tiemnang = rs.getShort("tiemnang");
////                short kynang = rs.getShort("kynang");
////                short point1 = rs.getShort("point1");
////                short point2 = rs.getShort("point2");
////                short point3 = rs.getShort("point3");
////                short point4 = rs.getShort("point4");
////                int lv =0;
////                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
////                JSONArray jsare = new JSONArray();
////                //Item item = new Item(new Player(conn, 0));
////                if(jsar!=null)
////                {
////                    lv = rs.getShort("level");
////                    for (int i = 0; i < jsar.size(); i++) {
////                        JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
////                        Item3 temp = new Item3();
////                        temp.id = Short.parseShort(jsar2.get(0).toString());
////                        temp.clazz = Byte.parseByte(jsar2.get(1).toString());
////                        temp.type = Byte.parseByte(jsar2.get(2).toString());
////                        temp.level = Short.parseShort(jsar2.get(3).toString());
////                        temp.icon = Short.parseShort(jsar2.get(4).toString());
////                        temp.color = Byte.parseByte(jsar2.get(5).toString());
////                        temp.part = Byte.parseByte(jsar2.get(6).toString());
////                        temp.islock = Byte.parseByte(jsar2.get(7).toString()) == 1;
////                        temp.name = ItemTemplate3.item.get(temp.id).getName();
////                        if (temp.islock) {
////                            temp.name += " [Khóa]";
////                        }
////                        temp.tier = Byte.parseByte(jsar2.get(8).toString());
////                        JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(9).toString());
////                        temp.op = new ArrayList<>();
////                        for (int j = 0; j < jsar3.size(); j++) {
////                            JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
////                            temp.op.add(
////                                    new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()),temp.id));
////                        }
////                        temp.time_use = 0;
////                        if (jsar2.size() == 11) {
////                            temp.time_use = Long.parseLong(jsar2.get(10).toString());
////                        }
////                        JSONArray re111 = getitem3(temp);
////                        if(temp.id >= 4587 && temp.id <=4590 )
////                        {
//////                            if(temp.op!=null)
//////                            {
//////                                for(int l=0; l< temp.op.size();l++)
//////                                {
//////                                    Option op = temp.op.get(l);
//////                                    if(op!=null && op.id >=0&& op.id <=6 && op.getParam(0)>10000)
//////                                        bug=true;
//////                                    op.setParam(0);
//////                                }
//////                            }
////
////                        }
////                        else if(re111!=null )
////                        {
////                            jsare.add(re111);
////                        }
////                    }
////
////                    jsar.clear();
////                }
////                a += ",`item3` = '" + jsare.toJSONString() + "'";
////                jsare.clear();
////
////                jsar = (JSONArray) JSONValue.parse(rs.getString("itembox3"));
////                //Item item = new Item(new Player(conn, 0));
////                if(jsar!=null)
////                {
////                    lv = rs.getShort("level");
////                    for (int i = 0; i < jsar.size(); i++) {
////                        JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
////                        Item3 temp = new Item3();
////                        temp.id = Short.parseShort(jsar2.get(0).toString());
////                        temp.clazz = Byte.parseByte(jsar2.get(1).toString());
////                        temp.type = Byte.parseByte(jsar2.get(2).toString());
////                        temp.level = Short.parseShort(jsar2.get(3).toString());
////                        temp.icon = Short.parseShort(jsar2.get(4).toString());
////                        temp.color = Byte.parseByte(jsar2.get(5).toString());
////                        temp.part = Byte.parseByte(jsar2.get(6).toString());
////                        temp.islock = Byte.parseByte(jsar2.get(7).toString()) == 1;
////                        temp.name = ItemTemplate3.item.get(temp.id).getName();
////                        if (temp.islock) {
////                            temp.name += " [Khóa]";
////                        }
////                        temp.tier = Byte.parseByte(jsar2.get(8).toString());
////                        JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(9).toString());
////                        temp.op = new ArrayList<>();
////                        for (int j = 0; j < jsar3.size(); j++) {
////                            JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
////                            temp.op.add(
////                                    new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()),temp.id));
////                        }
////                        temp.time_use = 0;
////                        if (jsar2.size() == 11) {
////                            temp.time_use = Long.parseLong(jsar2.get(10).toString());
////                        }
////                        JSONArray re111 = getitem3(temp);
////                        if(temp.id >= 4587 && temp.id <=4590 )
////                        {
//////                            if(temp.op!=null)
//////                            {
//////                                for(int l=0; l< temp.op.size();l++)
//////                                {
//////                                    Option op = temp.op.get(l);
//////                                    if(op!=null && op.id >=0&& op.id <=6 && op.getParam(0)>10000)
//////                                        bug=true;
//////                                    op.setParam(0);
//////                                }
//////                            }
////
////                        }
////                        else if(re111!=null )
////                        {
////                            jsare.add(re111);
////                        }
////                    }
////
////                    jsar.clear();
////                }
////                a += ",`itembox3` = '" + jsare.toJSONString() + "'";
////                jsare.clear();
////
////                jsar = (JSONArray) JSONValue.parse(rs.getString("itemwear"));
////                if(jsar!=null)
////                {
////                    Item3[] wwww = itemW(jsar);
////                    if(wwww!=null)
////                    {
////                        for (int i = 0; i < wwww.length; i++) {
////                            Item3 temp = wwww[i];
////                            if (temp != null) {
////                                JSONArray jsar2 = new JSONArray();
////                                jsar2.add(temp.id);
////                                jsar2.add(temp.clazz);
////                                jsar2.add(temp.type);
////                                jsar2.add(temp.level);
////                                jsar2.add(temp.icon);
////                                jsar2.add(temp.color);
////                                jsar2.add(temp.part);
////                                jsar2.add(temp.tier);
////                                JSONArray jsar3 = new JSONArray();
////                                for (int j = 0; j < temp.op.size(); j++) {
////                                    JSONArray jsar4 = new JSONArray();
////                                    jsar4.add(temp.op.get(j).id);
////                                    jsar4.add(temp.op.get(j).getParam(0));
////                                    jsar3.add(jsar4);
////                                }
////                                if(temp.id >= 4587 && temp.id <=4590 )
////                                {
//////                                    if(temp.op!=null)
//////                                    {
//////                                        for(int l=0; l< temp.op.size();l++)
//////                                        {
//////                                            Option op = temp.op.get(l);
//////                                            if(op!=null && op.id >=0&& op.id <=6 && op.getParam(0)>10000)
//////                                                bug=true;
//////                                            op.setParam(0);
//////                                        }
//////                                    }
////
////                                }
////                                else
////                                {
////                                    jsar2.add(jsar3);
////                                    jsar2.add(i);
////                                    jsare.add(jsar2);
////                                }
////
////                            }
////                        }
////                        //jsare = getitemW(wwww);
////                    }
////
////                    jsar.clear();
////                }
////                a += ",`itemwear` = '" + jsare.toJSONString() + "'";
////                if(bug)
////                {
////                    lv = lv>50? lv-50:lv;
////                    tiemnang =(short)(lv*5);
////                    kynang =(short)lv;
////                    point1 =0;
////                    point2 =0;
////                    point3 =0;
////                    point4 =0;
////
////                }
////                a += ",`level` = " + lv;
////                a += ",`tiemnang` = " + tiemnang;
////                a += ",`kynang` = " + kynang;
////                a += ",`point1` = " + point1;
////                a += ",`point2` = " + point2;
////                a += ",`point3` = " + point3;
////                a += ",`point4` = " + point4;
////                Updatesql(a,id);
////            }
////            System.out.println("xong");
////        }catch (SQLException e) {
////            e.printStackTrace();
////        }
//    }
//    public static void Updatesql(String a, int id)
//    {
//        try ( Connection connection = SQL.gI().getConnection();  Statement ps = connection.createStatement())
//        {
//            if (ps.executeUpdate("UPDATE `player` SET " + a + " WHERE `id` = " + id + ";") > 0) {
//                connection.commit();
//            }
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//            //return false;
//        }
//    }
//    public static Item3[] itemW(JSONArray jsar)
//    {
//        if (jsar == null) {
//            return null;
//        }
//        Item3[] wear = new Item3[24];
//        for (int i = 0; i < 24; i++) {
//            wear[i] = null;
//        }
//        for (int i = 0; i < jsar.size(); i++) {
//            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//            if (jsar2 == null) {
//                return null;
//            }
//            Item3 temp = new Item3();
//            temp.id = Short.parseShort(jsar2.get(0).toString());
//            temp.name = ItemTemplate3.item.get(temp.id).getName() + " [Khóa]";
//            temp.clazz = Byte.parseByte(jsar2.get(1).toString());
//            temp.type = Byte.parseByte(jsar2.get(2).toString());
//            temp.level = Short.parseShort(jsar2.get(3).toString());
//            temp.icon = Short.parseShort(jsar2.get(4).toString());
//            temp.color = Byte.parseByte(jsar2.get(5).toString());
//            temp.part = Byte.parseByte(jsar2.get(6).toString());
//            temp.tier = Byte.parseByte(jsar2.get(7).toString());
//            // if (temp.type == 15) {
//            // temp.tier = 0;
//            // }
//            temp.islock = true;
//            JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(8).toString());
//            temp.op = new ArrayList<>();
//            for (int j = 0; j < jsar3.size(); j++) {
//                JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
//                if (jsar4 == null) {
//                    return null;
//                }
//                temp.op.add(
//                        new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()),temp.id));
//            }
//            temp.time_use = 0;
//            wear[Byte.parseByte(jsar2.get(9).toString())] = temp;
//        }
//        return wear;
//    }
////    private static JSONArray getitemW(Item3[] ltem)
////    {
////        JSONArray jsare = new JSONArray();
////        for (int i = 0; i < ltem.length; i++) {
////            Item3 temp = ltem[i];
////            if (temp != null) {
////                JSONArray jsar2 = new JSONArray();
////                jsar2.add(temp.id);
////                jsar2.add(temp.clazz);
////                jsar2.add(temp.type);
////                jsar2.add(temp.level);
////                jsar2.add(temp.icon);
////                jsar2.add(temp.color);
////                jsar2.add(temp.part);
////                jsar2.add(temp.tier);
////                JSONArray jsar3 = new JSONArray();
////                for (int j = 0; j < temp.op.size(); j++) {
////                    JSONArray jsar4 = new JSONArray();
////                    jsar4.add(temp.op.get(j).id);
////                    jsar4.add(temp.op.get(j).getParam(0));
////                    jsar3.add(jsar4);
////                }
////                if(temp.id >= 4587 && temp.id <=4590 )
////                {
////
////                }
////                jsar2.add(jsar3);
////                jsar2.add(i);
////                jsare.add(jsar2);
////            }
////        }
////        return jsar;
////    }
//    private static JSONArray getitem3(Item3 temp)
//    {
//        if (temp != null)
//        {
//            JSONArray jsar12 = new JSONArray();
//            jsar12.add(temp.id);
//            jsar12.add(temp.clazz);
//            jsar12.add(temp.type);
//            jsar12.add(temp.level);
//            jsar12.add(temp.icon);
//            jsar12.add(temp.color);
//            jsar12.add(temp.part);
//            jsar12.add(temp.islock ? 1 : 0);
//            jsar12.add(temp.tier);
//            JSONArray jsar13 = new JSONArray();
//            for (int j = 0; j < temp.op.size(); j++) {
//                JSONArray jsar14 = new JSONArray();
//                jsar14.add(temp.op.get(j).id);
//                jsar14.add(temp.op.get(j).getParam(0));
//                jsar13.add(jsar14);
//            }
//            jsar12.add(jsar13);
//            jsar12.add(temp.time_use);
//            return jsar12;
//        }
//        return  null;
//    }
//}
////for (int i = 0; i < jsar.size(); i++) {
////                        JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
////                        Item3 temp = new Item3();
////                        if (jsar2 != null) {
////                            temp.id = Short.parseShort(jsar2.get(0).toString());
////                            temp.name = ItemTemplate3.item.get(temp.id).getName() + " [Khóa]";
////                            temp.clazz = Byte.parseByte(jsar2.get(1).toString());
////                            temp.type = Byte.parseByte(jsar2.get(2).toString());
////                            temp.level = Short.parseShort(jsar2.get(3).toString());
////                            temp.icon = Short.parseShort(jsar2.get(4).toString());
////                            temp.color = Byte.parseByte(jsar2.get(5).toString());
////                            temp.part = Byte.parseByte(jsar2.get(6).toString());
////                            temp.tier = Byte.parseByte(jsar2.get(7).toString());
////                            temp.islock = true;
////                        }
////
////                        JSONArray jsar3 = (JSONArray) JSONValue.parse(jsar2.get(8).toString());
////                        temp.op = new ArrayList<>();
////                        for (int j = 0; j < jsar3.size(); j++) {
////                            JSONArray jsar4 = (JSONArray) JSONValue.parse(jsar3.get(j).toString());
////                            if (jsar4 != null) {
////                                temp.op.add(new Option(Byte.parseByte(jsar4.get(0).toString()), Integer.parseInt(jsar4.get(1).toString()),temp.id));
////                            }
////                        }
////                        temp.time_use = 0;
////                        JSONArray re111 = getitem3(temp);
////                        if(temp.id >= 4587 && temp.id <=4590 )
////                        {
////                            if(temp.op!=null)
////                            {
////                                for(int l=0; l< temp.op.size();l++)
////                                {
////                                    Option op = temp.op.get(l);
////                                    if(op!=null && op.id >=0&& op.id <=6 && op.getParam(0)>10000)
////                                        bug=true;
////                                    op.setParam(0);
////                                }
////                            }
////
////                        }
////                        if(re111!=null)
////                        {
////                            jsare.add(re111);
////                        }
////                        //item.wear[Byte.parseByte(jsar2.get(9).toString())] = temp;
////                    }