package client;

import History.His_COIN;
import core.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import event_daily.ChiemThanhManager;
import event_daily.Wedding;
import event_daily.*;
import event_daily.st.*;

import io.Message;
import io.Session;
import map.*;
import template.*;

public class Process_Yes_no_box {

    public static void process(Session conn, Message m) throws IOException {
        short id = m.reader().readShort(); // id
        if (id != conn.p.index) {
            return;
        
        }
        byte type = m.reader().readByte(); // type
        byte value = m.reader().readByte(); // value
        if (value != 1) {
            switch (type) {
               case 110: {
					Player p0 = Map.get_player_by_name(conn.p.in4_wedding[1]);
					Service.send_notice_box(p0.conn, "rất tiếc " + conn.p.name + " đã từ chối lời cầu hôn của bạn");
					conn.p.in4_wedding = null;
					break;
				}
                
                case 115: {
                    conn.p.id_remove_time_use = -1;
                    break;
                }
                case 70: {
                    Service.send_notice_box(conn, "Cần 20k ngọc!");
                    break;
                }
                case 126: {
                    conn.p.id_buffer_126 = -1;
                    conn.p.id_index_126 = -1;
                    break;
                }
                case 114: {
                    conn.p.id_wing_split = -1;
                    break;
                }
                case 113: {
                    conn.p.name_mem_clan_to_appoint = "";
                    break;
                }
            }
        } else {
            switch (type) {
                case -1: {
                    Dungeon d = DungeonManager.get_list(conn.p.name);
                    if(d == null){
                        Service.send_notice_box(conn, "Phải vào phó bản trước rồi mới dùng được chức năng này");
                        return;
                    }
                    if(conn.p.point_active[0] <1){
                        Service.send_notice_box(conn,"Bạn không còn lượt nào để trừ");
                        return;
                    }
                    conn.p.point_active[0] -= 1;
                    d.finish_dungeon();
                    conn.p.item.char_inventory(3);
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn,"Bạn đã sử dụng quyền trợ giúp hoàn thành phó bản");
                    break;
                }
                case -127: {
                    if (!conn.p.isOwner) {
                        return;
                    }
                    if (conn.p.checkcoin() < 100000) {
                        Service.send_notice_box(conn, "Không đủ coin");
                        return;
                    }
                    if (conn.p.level < 10) {
                        Service.send_notice_box(conn, "Yêu cầu level 10");
                        return;
                    }
                    if (conn.p.squire == null) {
                        Squire.create(conn.p);
                        conn.p.squire = new Squire(conn, conn.p.index);
                        conn.p.squire.load();
                        conn.p.update_coin(-100_000);
                        His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                        hisc.coin_change = 100000;
                        hisc.coin_last = conn.p.checkcoin();
                        hisc.Logger = "(TRỪ COIN) từ đệ tử";
                        hisc.Flus();
                        //Log.gI().add_log(conn.p.name, "trừ 100k coin từ đệ tử");
                        Service.send_notice_box(conn, "Nhận thành công đệ tử");
                        Squire.callSquire(conn);
                    }
                    break;
                }
                case -124: {
                    if (conn.p.squire != null && conn.p.isOwner) {
                        try (Connection connnect = SQL.gI().getConnection(); PreparedStatement ps = connnect.prepareStatement("DELETE FROM `squire` WHERE `id` = ?;")) {
                            ps.setInt(1, conn.p.squire.index);
                            ps.executeUpdate();
                            connnect.commit();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (conn.p.isLiveSquire) {
                            Squire.squireLeaveMap(conn.p);
                        }
                        conn.p.isLiveSquire = false;
                        conn.p.squire = null;
                        Service.send_notice_box(conn, "Đã huỷ đệ tử");
                    } else {
                        Service.send_notice_box(conn, "Chưa có đệ tử");
                    }
                }
                case -112: {
                    if (conn.p.checkcoin() < 5_000) {
                        Service.send_notice_box(conn, "Không đủ coin");
                        return;
                    }
                    Map map = Map.get_map_by_id(conn.p.map.map_id)[1];
                    if (map != null && map.players.size() >= map.maxplayer) {
                        Service.send_notice_box(conn, "Khu vực đầy!!!");
                        return;
                    }
                    conn.p.update_coin(-5_000);
                    His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                    hisc.coin_change = 5000;
                    hisc.coin_last = conn.p.checkcoin();
                    hisc.Logger = "(TRỪ COIN) từ làng phủ sương";
                    hisc.Flus();
                    //Log.gI().add_log(conn.p.name, "trừ 5k coin từ làng phủ sương");
                    conn.p.add_EffDefault(-127, 1, 2 * 60 * 60 * 1000);
                    MapService.leave(conn.p.map, conn.p);
                    conn.p.map = map;
                    MapService.enter(conn.p.map, conn.p);
                    break;
                }
                case -122: {
                    // Đệ tử
                    if (!conn.p.isOwner) {
                        return;
                    }
                    if (conn.p.get_ngoc() > 5) {
                        if ((conn.p.item.total_item_by_id(7, 472) > 50 && conn.p.item.total_item_by_id(7, 473) > 50
                                && conn.p.item.total_item_by_id(7, 474) > 50 && conn.p.item.total_item_by_id(7, 475) > 50) || conn.ac_admin > 2) {
                            short[] id_ma_phap = new short[]{4578, 4579, 4581, 4584};
                            ItemTemplate3 temp3 = ItemTemplate3.item.get(id_ma_phap[Util.random(id_ma_phap.length)]);
                            Item3 it = new Item3();
                            it.id = temp3.getId();
                            it.name = temp3.getName();
                            it.clazz = temp3.getClazz();
                            it.type = temp3.getType();
                            it.level = temp3.getLevel();
                            it.icon = temp3.getIcon();
                            it.op = temp3.getOp();
                            it.op.add(new Option((byte) 61, 0));
                            it.color = 3;
                            it.islock = true;
                            it.part = temp3.getPart();
                            conn.p.item.add_item_bag3(it);
                            it.op.clear();
                            conn.p.item.remove(7, 472, 50);
                            conn.p.item.remove(7, 473, 50);
                            conn.p.item.remove(7, 474, 50);
                            conn.p.item.remove(7, 475, 50);
                            conn.p.item.char_inventory(3);
                            conn.p.item.char_inventory(4);
                            conn.p.item.char_inventory(7);
                            conn.p.update_ngoc(-5);

                            Service.send_notice_box(conn, "Ghép thành công");
                        } else {
                            Service.send_notice_box(conn, "Bạn cần có 50 mảnh (Mảnh sách đỏ 1, Mảnh sách đỏ 2," +
                                    " Mảnh sách đỏ 3, Mảnh sách đỏ 4)");
                        }
                    } else {
                        Service.send_notice_box(conn, "Không đủ ngọc");
                    }
                    break;
                }
                case -123: {
                    // Đệ tử
                    if (!conn.p.isOwner) {
                        return;
                    }
                    if (conn.p.get_ngoc() >= 10) {
                        if ((conn.p.item.total_item_by_id(7, 476) > 50 && conn.p.item.total_item_by_id(7, 477) > 50
                                && conn.p.item.total_item_by_id(7, 478) > 50 && conn.p.item.total_item_by_id(7, 479) > 50) || conn.ac_admin > 2) {
                            short[] id_vat_ly = new short[]{4577, 4580, 4582, 4583};
                            ItemTemplate3 temp3 = ItemTemplate3.item.get(id_vat_ly[Util.random(id_vat_ly.length)]);
                            Item3 it = new Item3();
                            it.id = temp3.getId();
                            it.name = temp3.getName();
                            it.clazz = temp3.getClazz();
                            it.type = temp3.getType();
                            it.level = temp3.getLevel();
                            it.icon = temp3.getIcon();
                            it.op = temp3.getOp();
                            it.op.add(new Option((byte) 61, 0));
                            it.color = 0;
                            it.islock = true;
                            it.part = temp3.getPart();
                            conn.p.item.add_item_bag3(it);
                            it.op.clear();
                            conn.p.item.remove(7, 476, 50);
                            conn.p.item.remove(7, 477, 50);
                            conn.p.item.remove(7, 478, 50);
                            conn.p.item.remove(7, 479, 50);
                            conn.p.item.char_inventory(3);
                            conn.p.item.char_inventory(4);
                            conn.p.item.char_inventory(7);
                            conn.p.update_ngoc(-10);

                            Service.send_notice_box(conn, "Ghép thành công");
                        } else {
                            Service.send_notice_box(conn, "Bạn cần có 50 mảnh (Mảnh sách xanh 1, Mảnh sách xanh 2," +
                                    " Mảnh sách xanh 3, Mảnh sách xanh 4)");
                        }
                    } else {
                        Service.send_notice_box(conn, "Không đủ ngọc");
                    }
                    break;
                }
                case -121: {
                    // Đệ tử
                    if (!conn.p.isOwner) {
                        return;
                    }
                    int level = conn.p.skill_110[conn.p.id_index_temp];
                    if (conn.p.skill_110[conn.p.id_index_temp] >= 10) {
                        conn.p.id_index_temp = -1;
                        Service.send_notice_box(conn, "Kỹ năng được nâng cấp tối đa");
                        return;
                    }
                    int id_book = -1;
                    if (conn.p.id_index_temp == 1) {
                        id_book = switch (conn.p.clazz) {
                            case 0 -> 4578;
                            case 1 -> 4580;
                            case 2 -> 4582;
                            case 3 -> 4584;
                            default -> id_book;
                        };
                    } else if (conn.p.id_index_temp == 0) {
                        id_book = switch (conn.p.clazz) {
                            case 0 -> 4577;
                            case 1 -> 4579;
                            case 2 -> 4581;
                            case 3 -> 4583;
                            default -> id_book;
                        };
                    }
                    if (conn.p.get_ngoc() < level * 5 + 10) {
                        conn.p.id_index_temp = -1;
                        Service.send_notice_box(conn, "Không đủ ngọc");
                        return;
                    }
                    if (conn.p.item.total_item_book_skill(id_book) >= (level + 1)) {
                        if (Util.nextInt(100) < 20 - level || level == 0 || conn.ac_admin > 111) {
                            conn.p.skill_110[conn.p.id_index_temp] += 1;
                            Service.send_notice_box(conn, "Nâng cấp thành công");
                            conn.p.item.remove_item_book_skill(id_book, (level + 1));
                            conn.p.load_skill();
                            Service.send_skill(conn.p);
                        } else {
                            Service.send_notice_box(conn, "Thất bại rồi");
                            conn.p.item.remove_item_book_skill(id_book, 1);
                        }
                        //conn.p.item.remove_item_book_skill(id_book, (level + 1));
                        conn.p.item.char_inventory(3);
                        conn.p.id_index_temp = -1;
                        conn.p.update_ngoc(-(level * 5 + 10));
                    } else {
                        Service.send_notice_box(conn, "Không đủ sách");
                    }
                    break;
                }
                case -120: {
                    // Đệ tử
                    if (!conn.p.isOwner) {
                        return;
                    }
                    int level = conn.p.skill_110[conn.p.id_index_temp];
                    if (conn.p.skill_110[conn.p.id_index_temp] >= 10) {
                        conn.p.id_index_temp = -1;
                        Service.send_notice_box(conn, "Kỹ năng được nâng cấp tối đa");
                        return;
                    }
                    int id_book = -1;
                    int type_book = -1;
                    if (conn.p.id_index_temp == 1) {
                        id_book = switch (conn.p.clazz) {
                            case 0 ->
                                    4578;
                            case 1 ->
                                    4580;
                            case 2 ->
                                    4582;
                            case 3 ->
                                    4584;
                            default ->
                                    id_book;
                        };
                    } else if (conn.p.id_index_temp == 0) {
                        id_book = switch (conn.p.clazz) {
                            case 0 ->
                                    4577;
                            case 1 ->
                                    4579;
                            case 2 ->
                                    4581;
                            case 3 ->
                                    4583;
                            default ->
                                    id_book;
                        };
                    }
                    if (conn.p.get_ngoc() < level * 5 + 10) {
                        conn.p.id_index_temp = -1;
                        Service.send_notice_box(conn, "Không đủ ngọc");
                        return;
                    }
                    if (conn.p.item.total_item_book(type_book, id_book) >= (level + 1)) {
                        if (Util.nextInt(100) < 20 - level || level == 0) {
                            conn.p.skill_110[conn.p.id_index_temp] += 1;
                            Service.send_notice_box(conn, "Nâng cấp thành công");
                            conn.p.load_skill();
                            conn.p.item.remove_item_book_skill(id_book, (level + 1));
                            Service.send_skill(conn.p);
                        } else {
                            Service.send_notice_box(conn, "Thất bại rồi");
                            conn.p.item.remove_item_book_skill(id_book, 1);
                        }
                        conn.p.id_index_temp = -1;
                        //conn.p.item.remove_item_book_skill(id_book, (level + 1));
                        conn.p.item.char_inventory(3);
                        conn.p.update_ngoc(-(level * 5 + 10));
                    } else {
                        Service.send_notice_box(conn, "Không đủ sách");
                    }
                    break;
                }
                case 112: {
                    Wedding temp = Wedding.get_obj(conn.p.name);
                    if (temp.exp < Level.entrys.get(temp.it.tier).exp) {
                        Service.send_notice_box(conn, "chưa đủ 100% exp!");
                        return;
                    }
                    if(temp.it.tier >= 25){
                        Service.send_notice_box(conn, "Nhẫn đã nâng cấp tối đa");
                        return;
                    }
                    long vang_req = (3 * (temp.it.tier + 1)) * 5_000_000L;
                    int ngoc_req = (3 * (temp.it.tier + 1)) * 10_000;
                    if (conn.p.get_vang() < vang_req) {
                        Service.send_notice_box(conn, "chưa đủ " + vang_req + " vàng!");
                        return;
                    }
                    if (conn.p.get_ngoc() < ngoc_req) {
                        Service.send_notice_box(conn, "chưa đủ " + ngoc_req + " ngọc!");
                        return;
                    }
                    conn.p.update_vang(-vang_req);
                    Log.gI().add_log(conn.p.name, "Trừ " + vang_req + " nâng nhẫn cưới");
                    conn.p.update_ngoc(-ngoc_req);
                    conn.p.item.char_inventory(5);
                    int suc = Util.random(100);
                    if ((suc < 80 && temp.it.tier <= 5) ||
                        (suc < 60 && (temp.it.tier >= 5 && temp.it.tier < 10)) ||
                        (suc < 40 && (temp.it.tier >= 10 && temp.it.tier < 15)) ||
                        (suc < 10 && (temp.it.tier >= 15 && temp.it.tier < 20)) ||
                        (suc < 2 && (temp.it.tier >= 20 && temp.it.tier < 25)) ||
                        (suc < 1 && (temp.it.tier >= 25 && temp.it.tier < 33)) || conn.ac_admin > 66) {
                        temp.exp -= Level.entrys.get(temp.it.tier).exp;
                        temp.it.tier++;
                        Service.send_notice_box(conn, "nâng cấp thành công lên +" + temp.it.tier);
                        conn.p.item.wear[23] = temp.it;
                        Service.send_wear(conn.p);
                        Service.send_char_main_in4(conn.p);
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        //
                        Player p0 = Map.get_player_by_name(temp.name_1.equals(conn.p.name) ? temp.name_2 : temp.name_1);
                        if (p0 != null) {
                            p0.item.wear[23] = temp.it;
                            Service.send_wear(p0);
                            Service.send_char_main_in4(p0);
                            MapService.update_in4_2_other_inside(p0.map, p0);
                        }
                    } else {
                        if(temp.it.tier >= 15){
                            temp.exp -=(Level.entrys.get(temp.it.tier).exp)/2;
                        }
                        Service.send_notice_box(conn, "nâng cấp thất bại!");
                    }
                    break;
                }
                case 111: {
                    Wedding temp = Wedding.get_obj(conn.p.name);
                    conn.p.item.wear[23] = null;
                    Service.send_wear(conn.p);
                    Service.send_char_main_in4(conn.p);
                    MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                    Service.send_notice_box(conn, "Ly hôn thành công");
                    conn.p.it_wedding = null;
                    Player p0 = Map.get_player_by_name(temp.name_1.equals(conn.p.name) ? temp.name_2 : temp.name_1);
                    if (p0 != null) {
                        p0.item.wear[23] = null;
                        Service.send_wear(p0);
                        Service.send_char_main_in4(p0);
                        MapService.update_in4_2_other_inside(p0.map, p0);
                        Service.send_notice_box(p0.conn, conn.p.name + " đã rời xa bạn");
                        p0.it_wedding = null;
                    }
                    Wedding.remove_wed(temp);
                    break;
                }
                case 110: {
                    Player p0 = Map.get_player_by_name(conn.p.in4_wedding[1]);
                    Wedding.add_new(Integer.parseInt(conn.p.in4_wedding[0]), p0, conn.p);
                    conn.p.in4_wedding = null;
                    Service.send_notice_box(p0.conn, "chúc mừng " + conn.p.name + " trở thành bạn đời của bạn");
                    Service.send_notice_box(conn, "chúc mừng " + p0.name + " trở thành bạn đời của bạn");
                    break;
                }
                 
                case 97:{
                    conn.p.Store_Sell_ToPL="no name";
                    Service.send_box_input_text(conn, 20, "Bán riêng cho nhân vật", new String[]{"Tên nhân vật"});
                    break;
                }
                case 113: {
                    if (conn.p.name_mem_clan_to_appoint.isEmpty()) {
                        return;
                    }
                    if (conn.p.myclan != null && conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        boolean suc = false;
                        for (int i = 1; i < conn.p.myclan.mems.size(); i++) {
                            if (conn.p.myclan.mems.get(i).name.equals(conn.p.name_mem_clan_to_appoint)) {
                                Clan_mems temp = conn.p.myclan.mems.get(0);
                                //
                                conn.p.myclan.mems.get(i).mem_type = 127;
                                conn.p.myclan.mems.get(0).mem_type = 122;
                                //
                                conn.p.myclan.mems.set(0, conn.p.myclan.mems.get(i));
                                conn.p.myclan.mems.set(i, temp);

                                //
                                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                                MapService.send_in4_other_char(conn.p.map, conn.p, conn.p);
                                Service.send_char_main_in4(conn.p);

                                Player p0 = Map.get_player_by_name(conn.p.myclan.mems.get(0).name);
                                if (p0 != null) {
                                    MapService.update_in4_2_other_inside(p0.map, p0);
                                    MapService.send_in4_other_char(p0.map, p0, p0);
                                    Service.send_char_main_in4(p0);
                                }
                                //
                                suc = true;
                                break;
                            }
                        }
                        if (suc) {
                            Service.send_notice_box(conn, "Thành công!");
                        } else {
                            Service.send_notice_box(conn, "Tên không tồn tại");
                        }
                    } else {
                        Service.send_notice_box(conn, "Đã xảy ra lỗi-ys");
                    }
                    break;
                }
                case 114: {
                    Item3 item = null;
                    int count = 0;
                    for (int i = 0; i < conn.p.item.bag3.length; i++) {
                        Item3 it = conn.p.item.bag3[i];
                        if (it != null && it.type == 7 && it.tier > 0) {
                            if (count == conn.p.id_wing_split) {
                                item = it;
                                break;
                            }
                            count++;
                        }
                    }
                    if (item != null) {

                        int quant1 = 40;
                        int quant2 = 10;
                        int quant3 = 50;
                        for (int i = 0; i < item.tier; i++) {
                            quant1 += GameSrc.wing_upgrade_material_long_khuc_xuong[i];
                            quant2 += GameSrc.wing_upgrade_material_kim_loai[i];
                            quant3 += GameSrc.wing_upgrade_material_da_cuong_hoa[i];
                            if ((i + 1) == 10 || (i + 1) == 20 || (i + 1) == 30) {
                                item.part--;
                            }
                        }
                        if (item.tier > 15) {
                            quant1 /= 2;
                            quant2 /= 2;
                            quant3 /= 2;
                        } else {
                            quant1 /= 3;
                            quant2 /= 3;
                            quant3 /= 3;
                        }
                        short[] id_ = new short[]{8, 9, 10, 11, 3, 0};
                        int[] quant_ = new int[]{quant1, quant1, quant1, quant1, quant2, quant3};
                        for (int i = 0; i < id_.length; i++) {
                            Item47 it = new Item47();
                            it.category = 7;
                            it.id = id_[i];
                            it.quantity = (short) quant_[i];
                            conn.p.item.add_item_bag47(7, it);
                        }
                        //
//                                                item = null;
                        count = 0;
                        for (int i = 0; i < conn.p.item.bag3.length; i++) {

                            Item3 it = conn.p.item.bag3[i];
                            if (it != null && it.type == 7 && it.tier > 0) {
                                if (count == conn.p.id_wing_split) {
                                    conn.p.item.bag3[i] = null;
                                    break;
                                }
                                count++;
                            }
                        }
                        conn.p.id_wing_split = -1;
//                                                for (int i = 0; i < item.op.size(); i++) {
//                                                if ((item.op.get(i).id > 26 || item.op.get(i).id < 23)
//							      && (item.op.get(i).id != 41 && item.op.get(i).id != 42)) {
//                                                    item.op.remove(i--);
//                                                }
//                                            }
                        //
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);
                        conn.p.item.char_inventory(3);
                        Service.send_notice_box(conn, "Thành công");
                    } else {
                        Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại!");
                        conn.p.id_wing_split = -1;
                    }
                    break;
                }
                case 9: {
                    if(conn.p.map.isMapChienTruong()){
                        if (conn.p.get_vang() >= 5_000) {
                            conn.p.isDie = false;
                            conn.p.hp = conn.p.body.get_HpMax();
                            conn.p.mp = conn.p.body.get_MpMax();
                            conn.p.update_vang(-5_000);
                            conn.p.item.char_inventory(5);
                            Service.send_char_main_in4(conn.p);
                            // chest in4
                            Service.send_combo(conn);
                            Service.usepotion(conn.p, 0, conn.p.body.get_HpMax());
                            Service.usepotion(conn.p, 1, conn.p.body.get_MpMax());
                        } else {
                            Service.send_notice_box(conn, "Không đủ vàng để thực hiện");
                        }
                    }
                    else if(conn.p.map.isMapChiemThanh()){
                        ChiemThanhManager.ActionHoiSinh(conn.p.map, conn.p);
                    }
                    else {
                        if (conn.p.get_vang() >= 10_000) {
                            conn.p.isDie = false;
                            conn.p.hp = conn.p.body.get_HpMax();
                            conn.p.mp = conn.p.body.get_MpMax();
                            conn.p.update_vang(-10_000);
                            conn.p.item.char_inventory(5);
                            Service.send_char_main_in4(conn.p);
                            // chest in4
                            Service.send_combo(conn);
                            Service.usepotion(conn.p, 0, conn.p.body.get_HpMax());
                            Service.usepotion(conn.p, 1, conn.p.body.get_MpMax());
                        } else {
                            Service.send_notice_box(conn, "Không đủ vàng để thực hiện");
                        }
                    }
                    break;
                }
                case 86: {
                    Manager.gI().vxmm.send_in4(conn.p);
                    break;
                }
                case 87: {
                    Manager.gI().vxkc.send_in4(conn.p);
                    break;
                }
                case 94: {
                    GameSrc.ChangeCS_Medal(conn, 94);
                    break;
                }
                case 98: {
                    GameSrc.ChangeCS_Medal(conn, 98);
                    break;
                }

                case 115: {
                    if (conn.p.id_remove_time_use != -1) {
                        Item3 it = conn.p.item.bag3[conn.p.id_remove_time_use];
                        if (it != null && it.time_use > 0) {
                            int ngoc_ = conn.p.get_ngoc();
                            if (ngoc_ > 4) {
                                long price = it.time_use - System.currentTimeMillis();
                                price /= 30_600_000;
                                price = (price > 4) ? (price + 1) : 5;
                                boolean ch = false;
                                if (ngoc_ >= price) {
                                    ch = true;
                                } else {
                                    price = ngoc_;
                                }
                                it.time_use -= (price * 30_600_000);
                                conn.p.update_ngoc(-price);
                                conn.p.item.char_inventory(4);
                                conn.p.item.char_inventory(7);
                                conn.p.item.char_inventory(3);
                                conn.p.id_remove_time_use = -1;
                                if (ch) {
                                    Service.send_notice_box(conn, "Nhận được " + it.name + " +" + it.tier + "!");
                                }
                            } else {
                                Service.send_notice_box(conn, "Tối thiểu 5 ngọc!");
                            }
                        }
                    }
                    break;
                }
                case 116: {
                    if (conn.p.myclan != null && conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        conn.p.myclan.remove_all_mem();
                        conn.p.myclan.remove_mem(conn.p.name);
                        conn.p.myclan = null;
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        MapService.send_in4_other_char(conn.p.map, conn.p, conn.p);
                        Service.send_char_main_in4(conn.p);
                        Service.send_notice_box(conn, "Hủy bang thành công");
                    }
                    break;
                }
                case 117: {
                    if (conn.p.myclan != null) {
                        conn.p.myclan.remove_mem(conn.p.name);
                        conn.p.myclan = null;
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        MapService.send_in4_other_char(conn.p.map, conn.p, conn.p);
                        Service.send_char_main_in4(conn.p);
                        Service.send_notice_box(conn, "Rời bang thành công");
                    }
                    break;
                }
                case 118: {
                    if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        if ((Clan.vang_upgrade[1] * conn.p.myclan.level) > conn.p.myclan.get_vang()) {
                            Service.send_notice_box(conn, "Không đủ vàng để thực hiện");
                            return;
                        }
                        if ((Clan.ngoc_upgrade[1] * conn.p.myclan.level) > conn.p.myclan.get_ngoc()) {
                            Service.send_notice_box(conn, "Không đủ ngọc để thực hiện");
                            return;
                        }
                        conn.p.myclan.update_vang(-Clan.vang_upgrade[1] * conn.p.myclan.level);
                        conn.p.myclan.update_ngoc(-Clan.ngoc_upgrade[1] * conn.p.myclan.level);
                        conn.p.myclan.level++;
                        conn.p.myclan.exp = 0;
//                        if (conn.p.myclan.max_mem < 45 && conn.p.myclan.level % 5 == 0) {
//                            conn.p.myclan.max_mem += 5;
//                        }
                        Service.send_notice_box(conn, "Nâng bang lên cấp " + conn.p.myclan.level + " thành công");
                    }
                    break;
                }
                case -118: {
                    try {
                        Leo_thap d = Leo_thapManager.get_list(conn.p.name);
                        if (d == null) {
                            try {
                                d = new Leo_thap();
                                conn.p.item.remove(4,342,1);
                                conn.p.item.char_inventory(4);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (d != null) {
                                d.name_party = conn.p.name;
                                if(conn.p.dokho == 1){
                                    d.setMode(0);
                                    d.wave = 1;
                                }else if(conn.p.dokho == 2){
                                    d.setMode(1);
                                    d.wave = 3;
                                }else if(conn.p.dokho == 3){
                                    d.setMode(2);
                                    d.state = 4;
                                }else if(conn.p.dokho == 4){
                                    d.setMode(3);
                                    d.wave = 5;
                                }else if(conn.p.dokho == 5){
                                    d.setMode(4);
                                    d.wave = 6;
                                }
                                //
                                MapService.leave(conn.p.map, conn.p);
                                conn.p.map = d.template;
                                conn.p.x = 550;
                                conn.p.y = 400;
                                MapService.enter(conn.p.map, conn.p);
                                //
                                Leo_thapManager.add_list(d);
                                Service.send_char_main_in4(conn.p);
                            } else {
                                Service.send_notice_box(conn, "Lỗi, hãy thử lại sau!");
                            }
                        } else {
                            MapService.leave(conn.p.map, conn.p);
                            conn.p.map = d.template;
                            MapService.enter(conn.p.map, conn.p);
                            d.send_mob_move_when_exit(conn.p);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case -117: {
                    String notice = "Danh sách người chơi vào phó bản :\n1) " + conn.p.name + " : level " + conn.p.level;
                    if (conn.p.party != null) {
                        Service.send_notice_box(conn, "Phó bản hiện tại chỉ hỗ trợ chế độ solo");
                        return;
                    }
                    notice += "\nĐộ khó: ??? \nHãy xác nhận." + (conn.p.point_active[0] != 10 ? " phí vào là 5000 coin." : "");
//                conn.p.dungeon = null;
                    conn.p.update_coin(-5000);
                    conn.p.point_active[0] += 1;
                    Service.send_box_input_yesno(conn, 119, notice);
                    break;
                }
                case 119: {
                    if(conn.p.point_active[0] !=10)
                    {
                        if(conn.p.get_ngoc() < 30)
                        {
                            Service.send_notice_box(conn, "Bạn không đủ ngọc để tham gia!");
                            return;
                        }
                        conn.p.update_ngoc(-30);
                        conn.p.item.char_chest(5);
                    }
                    Dungeon d = DungeonManager.get_list(conn.p.name);
                    if (d == null) {
                        try {
                            d = new Dungeon();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(conn.p.point_active[0] <= 0)
                            Service.send_notice_box(conn, "Hãy quay lại vào ngày hôm sau!");
                        else if (d != null) {
                            conn.p.point_active[0]--;
                            //
                            d.name_party = conn.p.name;
                            d.setMode(0);
                            //
                            MapService.leave(conn.p.map, conn.p);
                            conn.p.map = d.template;
                            conn.p.x = 584;
                            conn.p.y = 672;
                            MapService.enter(conn.p.map, conn.p);
                            d.send_map_data(conn.p);
                            //
                            DungeonManager.add_list(d);
                        } else {
                            Service.send_notice_box(conn, "Lỗi, hãy thử lại sau!");
                        }
                    } else {
                        MapService.leave(conn.p.map, conn.p);
                        conn.p.map = d.template;
                        MapService.enter(conn.p.map, conn.p);
                        d.send_map_data(conn.p);
                        d.send_mob_move_when_exit(conn.p);
                    }
                    break;
                }
                case 70: {
                    if (conn.p.get_ngoc() < 20000) {
                        Service.send_notice_box(conn, "20k ngọc còn không có thì không xứng đáng làm anh hùng!");
                        return;
                    }
                    Service.send_box_input_text(conn, 23, "Bang hội", new String[]{"Tên (4-20 ký tự) :", "Tên viết tắt (3 ký tự(VIẾT IN HOA)) :"});
//                    Message m12 = new Message(-53);
//                    m12.writer().writeShort(0);
//                    String[] txt = new String[]{"Tên (4-20 ký tự) :", "Tên viết tắt (3 ký tự) :"};
//                    m12.writer().writeByte(txt.length);
//                    for (String string : txt) {
//                        m12.writer().writeUTF(string);
//                    }
//                    m12.writer().writeUTF("Đăng ký bang");
//                    m12.writer().writeUTF("Bang hội");
//                    conn.addmsg(m12);
//                    m12.cleanup();
                    break;
                }
//                case 121: {
//                    Service.send_notice_box(conn, "Chức năng không còn tồn tại.");
//                    if (conn.p.get_ngoc() < 1000) {
//                        Service.send_notice_box(conn, "Không đủ 1000 ngọc!");
//                        return;
//                    }
//                    conn.p.update_ngoc(-1000);
//                    conn.p.item.char_inventory(5);
//                    EffTemplate ef = conn.p.get_eff(-126);
//                    if (ef != null) {
//                        long time_extra = (ef.time - System.currentTimeMillis()) + (1000 * 60 * 60 * 2);
//                        if (time_extra > (1000 * 60 * 60 * 24 * 3)) {
//                            time_extra = 1000 * 60 * 60 * 24 * 3;
//                        }
//                        conn.p.add_eff(-126, 99, (int) time_extra);
//                    } else {
//                        conn.p.add_eff(-126, 99, (1000 * 60 * 60 * 2));
//                    }
//                    Service.send_notice_box(conn, "Đăng ký thành công");
//                    break;
//                }
                case 122: { // chuyển hoá
//                    if((conn.p.item.bag3[conn.p.item_replace].id >= 4587 && conn.p.item.bag3[conn.p.item_replace].id<= 4590) || 
//                            (conn.p.item.bag3[conn.p.item_replace2].id >= 4587 && conn.p.item.bag3[conn.p.item_replace2].id<= 4590))
//                    {
//                        Service.send_notice_box(conn, "Trang bị không phù hợp!");
//                        return;a
//                    }
                    int fee = 50 * conn.p.item.bag3[conn.p.item_replace].tier;
                    if (conn.p.get_ngoc() < fee) {
                        Service.send_notice_box(conn, "Không đủ " + fee + " ngọc!");
                        return;
                    }
                    conn.p.item.bag3[conn.p.item_replace2].tier = conn.p.item.bag3[conn.p.item_replace].tier;
                    conn.p.item.bag3[conn.p.item_replace].tier = 0;
//                    if (conn.p.item.bag3[conn.p.item_replace2].type == 5
//                            && conn.p.item.bag3[conn.p.item_replace2].tier >= 9) {
//                        for (Option op_ : conn.p.item.bag3[conn.p.item_replace2].op) {
//                            if (op_.id == 37 && op_.getParam(conn.p.item.bag3[conn.p.item_replace2].tier) > 1) {
//                                op_.setParam(op_.getParam(conn.p.item.bag3[conn.p.item_replace2].tier));
//                            }
//                        }
//                    }
                    conn.p.update_ngoc(-fee);
                    conn.p.item.char_inventory(3);
                    Service.send_notice_box(conn, "Chuyển hóa thành công!");
                    //
                    Message m3 = new Message(73);
                    m3.writer().writeByte(0);
                    m3.writer().writeShort(conn.p.item_replace2);
                    m3.writer().writeByte(0);
                    conn.addmsg(m3);
                    m3.cleanup();
                    //
                    m3 = new Message(73);
                    m3.writer().writeByte(0);
                    m3.writer().writeShort(conn.p.item_replace);
                    m3.writer().writeByte(1);
                    conn.addmsg(m3);
                    m3.cleanup();
                    //
                    break;
                }
                case 123: {
                    break;
                }
                case 124: {
                    conn.p.rest_skill_point();
                    conn.p.item.remove(4, 7, 1);
                    conn.p.item.char_inventory(4);
                    Service.send_notice_box(conn, "Tẩy điểm kỹ năng thành công");
                    break;
                }
                case 125: {
                    conn.p.rest_potential_point();
                    conn.p.item.remove(4, 6, 1);
                    conn.p.item.char_inventory(4);
                    Service.send_notice_box(conn, "Tẩy điểm tiềm năng thành công");
                    break;
                }
                case 126: {
                    if (conn.p.id_buffer_126 != -1) {
                        Item3 temp3 = conn.p.item.bag3[conn.p.id_buffer_126];
                        temp3.islock = true;
                        switch (temp3.type) {
                            case 0: // coat
                            case 1: // pant
                            case 2: // crown
                            case 3: // grove
                            case 4: // ring
                            case 5: // chain
                            case 6: // shoes
                            case 7: // wing
                            case 15:
                            case 8:
                            case 9:
                            case 10:
                            case 16:
                            case 21:
                            case 22:
                            case 23:
                            case 24:
                            case 25:
                            case 26:
                            case 11: { // weapon
                                conn.p.player_wear(conn, temp3, conn.p.id_buffer_126, conn.p.id_index_126);
                                break;
                            }
                            default: {
                                Service.send_notice_nobox_white(conn, "Ấn 2 lần mới có thể sử dụng");
                                break;
                            }
                        }
                    }
                    conn.p.id_buffer_126 = -1;
                    conn.p.id_index_126 = -1;
                    break;
                }
                case 88:{
                    if (conn.ac_admin < 10) {
                        Service.send_notice_box(conn, "Bạn chưa đủ quyền để thực hiện!");
                        return;
                    }
                    ServerManager.gI().close();
                    System.out.println("Close server is processing....");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SaveData.process();
                            for (int k = Session.client_entrys.size() - 1; k >= 0; k--) {

                                try {
                                    Session.client_entrys.get(k).p = null;
                                    Session.client_entrys.get(k).close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Manager.gI().close();
                        }
                    }).start();
                    break;
                }
            }
        }
    }
}
