package core;

import BossHDL.BossManager;
import Helps.CheckItem;
import Helps.Save_Log;
import History.His_COIN;
import ev_he.*;
import event.Event_1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import client.Clan;
import client.Pet;
import client.Player;
import event_daily.*;
import event_daily.st.*;
import event_daily.sc.*;

//import event_daily.LoiDai;
import io.Message;
import io.Session;

import java.sql.ResultSet;
import java.util.Map.Entry;

import map.*;
import map.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import template.*;

public class MenuController {

    public static void request_menu(Session conn, Message m) throws IOException {
        byte idnpc = m.reader().readByte();
//        if (idnpc != -49 && conn.p.findNPC(idnpc) == null
//                && !conn.p.name.equals("banvang") && !conn.p.name.equals("darklord")) {
//            Service.send_notice_box(conn, "Khoảng cách quá xa");
//            return;
//        }
//        System.out.println("core.MenuController.request_menu()" + idnpc);
        if (idnpc == -43 || idnpc == -45 || idnpc == -48 || idnpc == -46 || idnpc == -47) {
            Menu_ChangeZone(conn);
            return;
        }
        if(!conn.p.my_store_name.isEmpty()){
            Service.send_notice_box(conn,"Bạn đang bán hàng hãy hủy bán hàng trước khi thực hiện hành động này");
            return;
        }
        // create menu per id npc
        String[] menu;
        switch (idnpc) {
            case -81: {
                menu = new String[]{"Đăng ký lôi đài", "Vào lôi đài", "Xem lôi đài", "Xem điểm lôi đài", "Nhận quà lôi đài", "Hướng dẫn", "Tháo ngọc khảm", "Luyện đệ tử", "Hủy đệ tử"};
                break;
            }
            case -104:
                Npc.chat(conn.p.map, "bấm vào làm gì", -104);
                if (conn.p.map.map_id == 135) {
                    menu = new String[]{"Về Làng sói trắng", "Vượt Làng phủ sương"};
                } else {
                    menu = new String[]{"Về Làng sói trắng"};
                }
                break;
            case -3: { // Lisa
                menu = new String[]{"Mua bán", "Mở ly", "Thuế", "Nhận quà chiến trường",
                    "Đóng"};
                break;
            }
            case -1: { // admin
                menu = new String[]{"Bảo trì", "Cộng vàng x1.000.000.000",
                        "Cộng ngọc x1.000.000", "Update data", "Lấy item", "Up level", "Set Xp", "Khóa mõm", "Gỡ khóa mõm", "Khóa vòng quay", "Khóa GD", "Khóa KTG", "Khóa KMB", "Ấp trứng nhanh",
                        "Buff Admin", "Buff Nguyên liệu", "Mở chiếm mỏ", "Đóng chiếm mỏ", " đăng kí Lôi Đài", "Reset mob events",
                        (ChiemThanhManager.isRegister ? "Đóng" : "Mở") + " đăng kí chiếm thành", "Mở đăng kí chiến trường", "Dịch map", "loadconfig",
                        (Manager.logErrorLogin ? "tắt" : "bật") + " log bug", "disconnect client", "check bug", "fix bug","Mở quà 20-10"};
                break;
            }
            case -99: { // shop_coin
                menu = new String[]{"Shop coin", "Shop đồ tinh tú","Shop nlmd = coin","Tiến hoá đồ tt[VIP PRO]","Tiến hóa mề đay","Cường hóa trang bị 2","Nhận quà tích lũy","Đổi đồng money","Hợp thể buff","Săn boss cá nhân","Nâng level 140++","VVafomap test"};
                break;
            }
//              case -20: { // Lisa
//                menu = new String[]{"Mua bán", "Mở ly", "Thuế", "Nhận quà chiến trường",
//                    "Đóng"};
//                break;
//            }

            case -5: { // Hammer
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ", "Chế tạo trang bị tinh tú", "Nâng cấp trang bị tinh tú", "Tháo Giáp Siêu Nhân ", "Tháo danh hiệu", "Chế Tạo Giáp Siêu Nhân", "Nâng cấp Giáp siên nhân", "Ghép sách"};
                break;
            }
            case -77: // Alisama
            case -22: { // Hammer
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ"};
                break;
            }
            case -4: {// Doubar
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ", "Thông Tin Boss"};
                break;
            }
            case -33: { // da dich chuyen
                menu = new String[]{"Thành Phố Cảng", "Thành Phố Kho Báu", "Khu Mua Bán", "Sa Mạc", "Vực Lún",
                    "Nghĩa Địa Cát", "Suối Ma", "Hầm Mộ Tầng 1", "Hầm Mộ Tầng 3", "Rừng Cao Nguyên", "Vách Đá Cheo Leo",
                    "Lối Lên Thượng Giới", "Đường Xuống Lòng Đất", "Cổng Vào Hạ Giới", "Khu Vườn"};
                break;
            }
            case -55: { // da dich chuyen
                menu = new String[]{"Thành Phố Cảng", "Khu Mua Bán", "Mê Cung", "Mê Cung Tầng 3", "Thị Trấn Mùa Đông",
                    "Thung lũng băng giá", "Chân núi tuyết", "Đèo băng giá", "Vực thẳm sương mù", "Trạm núi tuyết",
                    "Thành Phố Kho Báu"};
                break;
            }
            case -10: { // da dich chuyen
                menu = new String[]{"Làng Sói Trắng", "Thành Phố Kho Báu", "Khu Mua Bán", "Hang Lửa", "Rừng Ảo Giác",
                    "Thung Lũng Kỳ Bí", "Hồ Kí Ức", "Bờ Biển", "Vực Đá", "Rặng Đá Ngầm", "Đầm Lầy", "Đền Cổ", "Hang Dơi"};
                break;
            }
            case -8: {
                menu = new String[]{"Cửa Hàng Tóc", "Điểm danh hằng ngày", "Nhiệm vụ hàng ngày", "Đổi quà đặc biệt", "Đổi coin sang vàng", (conn.p.type_exp == 0 ? "Bật" : "Tắt") + " nhận exp", "Đổi Mật Khẩu"};
//                if (conn.p.type_exp == 1) {
//                    menu = new String[]{"Cửa Hàng Tóc", "Điểm danh hằng ngày", "Đổi coin sang ngọc", "Đổi coin sang vàng",
//                        "Đăng ký treo chống pk", "Tgian còn lại", "Tắt nhận exp"};
//                } else {
//                    menu = new String[]{"Cửa Hàng Tóc", "Điểm danh hằng ngày", "Đổi coin sang ngọc", "Đổi coin sang vàng",
//                        "Đăng ký treo chống pk", "Tgian còn lại", "Bật nhận exp"};
//                }
                break;
            }
            case -36: {
                menu = new String[]{"Cường Hóa Trang Bị", "Shop Nguyên Liệu", "Chuyển hóa", "Hợp nguyên liệu mề đay",
                    "Mề đay chiến binh", "Mề đay pháp sư", "Mề đay sát thủ", "Mề đay xạ thủ", "Nâng cấp mề đay",
                    "Đổi dòng sát thương", "Đổi dòng % sát thương", "Hợp ngọc", "Khảm ngọc", "Đục lỗ"};
                break;
            }
            case -44: {
                menu = new String[]{"Nhận GiftCode", "Tháo cánh", "Tháo cải trang", "Tháo mề đay", "Tháo mặt nạ",
                    "Tháo cánh thời trang", "Tháo áo choàng", "Tháo tóc thời trang", "Tháo vũ khí thời trang",
                    "Tháo tai nghe thời trang", "Nhận đồ đã mua", "Nhận quà top"};
//                menu = new String[]{"Nhận GiftCode", "Tháo cánh", "Tháo cải trang", "Tháo mề đay", "Tháo mặt nạ",
//                    "Tháo cánh thời trang", "Tháo áo choàng", "Tháo tóc thời trang", "Tháo vũ khí thời trang",
//                    "Tháo tai nghe thời trang"};             
                break;
            }
            case -32: {
                menu = new String[]{"Xem BXH Danh vọng", "Xem BXH Cao Thủ", "Xem BXH Đi Buôn", "Xem BXH Đi Cướp", "Xem BXH Chiến trường",
                    "Xem BXH Đổi quà", "Xem BXH Bang", "Xem BXH Hiếu chiến", "Xem BXH Phó Bản"};
                break;
            }
            case -21: { // blackeye
                menu = new String[]{"Chiến Binh", "Sát Thủ", "Pháp Sư", "Xạ Thủ"};
                break;
            }
            case -90: { // keva
                menu = new String[]{"Map Boss", "Về Làng "};
                //menu = new String[]{"Shop", "Khu Phủ Sương Up", "Map Boss", "Quay Về Làng Phủ Sương", "Về Làng "};
                break;
            }
            case -7: {
                if (conn.user.contains("knightauto_hsr_")) {
                    menu = new String[]{"Rương giữ đồ", "Mở thêm 126 hành trang (1K ngọc)", "Đăng ký tài khoản"};
                } else {
                    menu = new String[]{"Rương giữ đồ", "Mở thêm 126 hành trang(1K ngọc)", "Mật khẩu rương"};
                }
                break;
            }
            case -34: { // cuop bien
                menu = new String[]{"Vòng xoay Vàng", "Vòng xoay ngọc", "Lịch sử"};
                break;
            }
            case -2: { // zoro
                if (conn.p.myclan != null) {
                    if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        menu = new String[]{"Quản lý bang", "Shop Icon", "Shop Bang"};
                    } else {
                        menu = new String[]{"Kho Bang", "Góp Vàng", "Góp Ngọc", "Rời bang"};
                    }
                } else {
                    menu = new String[]{"Đăng ký bang", "Thông tin"};
                }
                break;
            }
            case -19: { //
                if (conn.p.myclan != null) {
                    if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                        menu = new String[]{"Quản lý bang", "Shop Icon", "Shop Bang"};
                    } else {
                        menu = new String[]{"Kho Bang", "Góp Vàng", "Góp Ngọc", "Rời bang"};
                    }
                } else {
                    menu = new String[]{"Đăng ký bang", "Thông tin"};
                }
                break;
            }
            case -85: { // mr edgar
                menu = new String[]{"Báo Thù", "Thông tin"};
                break;
            }
            case -42: { // pet
                menu = new String[]{"Chuồng thú", "Shop thức ăn", "Shop trứng", "Tháo pet"};
                break;
            }
            case -37: {
                menu = new String[]{"Vào Ngã Tư Tử Thần", "Giới thiệu", "BXH phó bản", "Đăng ký chiếm thành", "Vào chiếm thành",
                    "Xem Điểm Hiện Tại", "Nhận phần thưởng", "Trở thành hiệp sĩ","Hoàn thành phó bản khẩn cấp"};
                break;
            }
            case -38:
            case -40: {
//                if (conn.p.dungeon != null && conn.p.dungeon.getWave() == 20) {
//                    menu = new String[]{"Tiếp tục chinh phục", "Bỏ cuộc", "Hướng dẫn"};
//                } else {
                Service.send_notice_box(conn, "Chưa có chức năng :(.");
                return;

            }
            case -41: {
                menu = new String[]{"Tạo cánh", "Nâng cấp cánh", "Kích hoạt cánh", "Tách cánh"};
                break;
            }
            case -49: { //menu_top
                // menu = new String[]{"Yêu thích", "Shop Dola","Kết Hôn", "Thông Tin Cá Nhân " , "Nhận quà hiếu chiến","BXH Top Rương", "Khu Boss"};
                menu = new String[]{"Like","Kết Hôn", "Thông Tin Cá Nhân", "Khu boss","Nhận quà tân thủ"};
                break;
            }
            case -82: {
                menu = new String[]{"Rời khỏi đây", "Xem BXH Lôi đài"};
                break;
            }
            case -69: {
                if (Manager.gI().event == 1) { // sự kiện noel
                    menu = new String[]{"Đổi hộp đồ chơi", "Hướng dẫn", "Đăng ký nấu kẹo", "Bỏ nguyên liệu vào nồi kẹo",
                        "Lấy kẹo đã nấu", "Đổi túi kẹo", "Đổi trứng phượng hoàng băng", "Đổi trứng yêu tinh",
                        "Đổi giày băng giá", "Đổi mặt nạ băng giá", "Đổi kẹo gậy", "Đổi gậy tuyết", "Đổi xe trượt tuyết",
                        "Đổi trứng khỉ nâu"};

                } else if (Manager.gI().event == 2) { // sự kiện hè
                    menu = new String[]{"Mâm trái cây", "Top sự kiện", "Đổi quà may mắn"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                } else if (Manager.gI().event == 3) { // sự kiện vu lan
                    menu = new String[]{"Đổi bó sen trắng", "Đổi hoa sen hồng", "Đổi bó sen hồng", "Xem top", "Đổi con lân", "Đổi trứng khỉ nâu", "Đổi trứng tiểu yêu", "Đổi cánh thời trang"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};

                } else if (Manager.gI().event == 4) { // sự kiện tết
                    menu = new String[]{"Làm bánh trưng", "Làm bánh trưng đặc biệt", "Làm bánh dày", "Ghép Chữ HAPPY NEW YEAR", "Làm Pháo Hoa", "Xem Top"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                } else if (Manager.gI().event == 5) { // sự kiện trung thu 
                    menu = new String[]{"Làm lồng đèn", "Làm bánh nướng", "Làm bánh dẻo", "Xem Top"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                } else if (Manager.gI().event == 6) { // sự kiện Hallowen
                    menu = new String[]{"Rương huyền bí", "Xem Top"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                } else if (Manager.gI().event == 7) { // sự kiện xoài
                    menu = new String[]{"Vào map sự kiện","Ghép Rương sự kiện Xoài thường","Ghép Rương sự kiện Xoài VIP", "Xem Top", "Xem BXH Săn Boss"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                } else if (Manager.gI().event == 8) { // sự kiện xoài
                    menu = new String[]{"Đổi 10 bánh quy","Đổi hoa tuyết","BXH tặng bánh","Vào map boss thế giới", "Xem BXH BossTG", "Nhận quà top BossTG"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                } else if (Manager.gI().event == 11) { // sự kiện hồn gió
                    menu = new String[]{"Ghép linh hồn 4 mùa thường","Ghép Linh Hồn 4 Mùa Trung Cấp","Ghép Linh Hồn 4 Mùa Cao Cấp", "Xem Top"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                } else if (Manager.gI().event == 12) { // sự kiện hallveen
                    menu = new String[]{"Ghép rương thường","Đổi quà rương đặc biệt","Ghép rương đặc biệt", "Xem Top"};
                    send_menu_select(conn, -69, menu, (byte) Manager.gI().event);
                    return;
                } else {
                    Service.send_notice_box(conn, "Chưa có chức năng :(.");
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                }
                break;
            }
            case -62: {
                if (Manager.gI().event == 1) {
                    menu = new String[]{"Tăng tốc nấu", "Hướng dẫn", "Thông tin", "Top Nguyên Liệu"};
                } else {
                    Service.send_notice_box(conn, "Chưa có chức năng :(.");
                    return;
                    //menu = new String[]{"Coming soon", infoServer.Website};
                }
                break;
            }
            case -66: {
                if (Manager.gI().event == 8){
                    return;
                }
                if (Manager.gI().event == 1) {
                    menu = new String[]{"Hoa tuyết", "Ngôi sao", "Quả châu", "Thiệp", "Top trang trí cây thông"};
                }
                menu = new String[]{"Hoa tuyết", "Ngôi sao", "Quả châu", "Thiệp", "Top trang trí cây thông"};
                //  menu = new String[]{"Coming soon", infoServer.Website};
                break;
            }
            case -57: {
                menu = new String[]{"Mua bán"};
                break;
            }
            case -54: {
                menu = new String[]{"Đến Thành Phó Kho Báu"};
                break;
            }
            case -58: {
                menu = new String[]{"Mua lạc đà", "Bán đá quý", "Đồ thương nhân"};
                break;
            }
            case -59: {
                menu = new String[]{"Mua lạc đà", "Bán đá quý", "Đồ cướp"};
                break;
            }
            case -53: {
                menu = new String[]{" Đăng Ký Chiến trường", "Hướng dẫn", "Đổi Quà", "Vào Chiến Trường"};
                break;
            }
            default: {
                //System.out.println("core.MenuController.request_menu()"+idnpc);
                Service.send_notice_box(conn, "Chưa có chức năng :(.");
                return;
                //menu = new String[]{"Coming soon", infoServer.Website};
                //break;
            }
        }
        //
        send_menu_select(conn, idnpc, menu);
    }

    public static void processmenu(Session conn, Message m) throws IOException {
        short idnpc = m.reader().readShort();
        @SuppressWarnings("unused")
        byte idmenu = m.reader().readByte();
        byte index = m.reader().readByte();
        if (index < 0) {
            return;
        }
        if (idnpc == -56) {
            send_menu_select(conn, 119, new String[]{"Thông tin", "Bảo hộ", "Hồi máu", "Tăng tốc"});
            return;
        }
        if (idnpc == -53) {
            Menu_Mr_Ballard(conn, idnpc, idmenu, index);
            return;
        }
        if (idnpc >= 30000 && idmenu == Manager.gI().event) {
            if (Manager.gI().event == 8) {
                Menu_Mobnoel(conn, idnpc, idmenu, index);
            } else {
                Menu_MobEvent(conn, idnpc, idmenu, index);
            }
            return;
        }
        switch (idnpc) {
            case -43: {
                if (idmenu == 1) {
                    switch (index) {
                        case 0:
                            if (conn.p.item.total_item_by_id(4, 54) >= 1) {
                                Map map = Map.get_map_by_id(conn.p.map.map_id)[1];
                                if (map != null && map.players.size() >= map.maxplayer) {
                                    Service.send_notice_box(conn, "Khu vực đã đầy!!!");
                                    return;
                                }
                                conn.p.item.remove(4, 54, 1);
                                conn.p.add_EffDefault(-127, 1, 2 * 60 * 60 * 1000);
                                MapService.leave(conn.p.map, conn.p);
                                conn.p.map = map;
                                MapService.enter(conn.p.map, conn.p);
                            } else {
                                Service.send_notice_box(conn, "Không đủ Đồng bạc Tyche");
                            }
                            break;
                        case 1:
                            Service.send_box_input_yesno(conn, -112, "Bạn có muốn vào khu 2 với 50 ngọc cho 2 giờ?");
                            break;
                    }
                }
                break;
            }
            case 2: {
                Menu_diemct(conn, index);
                break;
            }
            case -122: {
                Menu_doiruong(conn, index);
                break;
            }
            case 4: {
                Menu_DoiDongMeDaySTG(conn, index);
                break;
            }
            case 5: {
                Menu_DoiDongMeDaySTPT(conn, index);
                break;
            }
            case 130: {
                Menu_sieuthan(conn,index);
//                Player p = conn.p;
//                conn.p.id_than = index;
//                Item3 it_change = conn.p.item.bag3[index];
//                int[] values = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
//                int tierStar = it_change.tierStar >= 0 && it_change.tierStar < values.length ? values[it_change.tierStar] : it_change.tierStar;
//                Service.send_box_input_yesno(conn, -12, "Nâng cấp " + it_change.name + "\n"
//                        + (tierStar) + "/" + conn.p.item.total_item_by_id(7, st.id[p.st_ran[0]]) + " " + ItemTemplate7.item.get(st.id[p.st_ran[0]]).getName() + "\n"
//                        + (tierStar) + "/" + conn.p.item.total_item_by_id(7, st1.id[p.st_ran[1]]) + " " + ItemTemplate7.item.get(st1.id[p.st_ran[1]]).getName() + "\n"
//                        + (tierStar) + "/" + conn.p.item.total_item_by_id(7, st2.id[p.st_ran[2]]) + " " + ItemTemplate7.item.get(st2.id[p.st_ran[2]]).getName() + "\n"
//                        + (tierStar) + "/" + conn.p.item.total_item_by_id(7, st3.id[p.st_ran[3]]) + " " + ItemTemplate7.item.get(st3.id[p.st_ran[3]]).getName() + "\n"
//                        + (tierStar) + "/" + conn.p.item.total_item_by_id(7, st4.id[p.st_ran[4]]) + " " + ItemTemplate7.item.get(st4.id[p.st_ran[4]]).getName());
                break;
            }
            case 114: {
                Menu_Wedding(conn, index);
                break;
            }
            case 115: {
                Menu_Thongtincanhan(conn, index);
                break;
            }
            case 999: {
                Menu_Nang_Skill(conn, index);
                break;
            }
            case 117: {
                Menu_ThaoKhamNgoc(conn, index);
                break;
            }
            case -54: {
                Menu_Mr_Haku(conn, index);
                break;
            }
            case -81: {
                Menu_Mrs_Oda(conn, index, idmenu);
                break;
            }
            case -1: {
                Menu_Admin(conn, index);
                break;
            }
            case -99: { // shopcoin
                Menu_shopcoin(conn, index);
                break;
            }
            case -98: { // quanap
                Menu_quanap(conn,index);
                //quanap.get_qua(conn.p, index);
                break;
            }
            case -97: { // dokho
                Menu_dokho(conn, index);
                break;
            }
            case -96: { // tiến hóa tt
                Menu_tienhoatt(conn, index);
                break;
            }
            case -95: { // tiến hóa md
                Menu_tienhoamd(conn, index);
                break;
            }
            case -94: { // cường hóa tb2
                Menu_tb2(conn, index);
                break;
            }
            case -93: {
                Menu_quavip(conn, index);
                break;
            }
            case -92: {
                Menu_nanglevel(conn, index);
                break;
            }
            case -82: {
                if (conn.p.map.kingCupMap != null) {
                    Menu_Mrs_Oda_trong_LoiDai(conn, index);
                } else {
                    Menu_MissAnwen(conn, index);
                }
                break;
            }
            case 210: {
                Menu_ThayDongCanh_percent(conn, index);
            }
            case 119: {
                Menu_Pet_di_buon(conn, index);
                break;
            }
            case -57: {
                Menu_Mr_Dylan(conn, index);
                break;
            }
            case -58: {
                Menu_Graham(conn, index);
                break;
            }
            case -59: {
                Menu_Mr_Frank(conn, index);
                break;
            }
            case 121: {
                Menu_TachCanh(conn, index);
                break;
            }
            case -3: { // Lisa
                Menu_Lisa(conn, index);
                break;
            }
            case -20: {
                Menu_Emma(conn, index);
            }
            case -90: { // keva
                Menu_keva(conn, index, idmenu);
                break;
            }
            case -4: {
                Menu_Doubar(conn, index, idmenu);
                break;
            }

            case -5: {
                Menu_Hammer(conn, index, idmenu);
                break;
            }
            case -22: {
                Menu_Alisama(conn, index, idmenu);
                break;
            }
            case -33: {
                Menu_DaDichChuyen33(conn, index);
                break;
            }
            case -55: {
                Menu_DaDichChuyen55(conn, index);
                break;
            }
            case -10: {
                Menu_DaDichChuyen10(conn, index);
                break;
            }

            case -77: {
                Menu_Alisama(conn, index);
                break;
            }
            case -8: {
                Menu_Zulu(conn, index);
                break;
            }
            case 126: {
                Menu_Admin(conn, index);
                break;
            }
            case -36: {
                Menu_PhapSu(conn, index);
                break;
            }
            case -44: {
                Menu_Miss_Anna(conn, index);
                break;
            }
            case -32: {
                Menu_BXH(conn, index);
                break;
            }
            case -21: {
                Menu_Black_Eye(conn, index);
                break;
            }

            case -7: {
                Menu_Aman(conn, index);
                break;
            }
            case -34: {
                Menu_CuopBien(conn, index);
                break;
            }
            case 125: { // vxmm
                Menu_VXMM(conn, index);
                break;
            }
            case 132: { // vxmm
                Menu_VXKC(conn, index);
                break;
            }
            case -104: {
                Menu_Serena(conn, index);
                break;
            }
            case -2: { // vxmm
                Menu_Zoro(conn, index);
                break;
            }
            case -19: { // vxmm
                Menu_Benjamin(conn, index);
                break;
            }
            case -85: { //
                Menu_Mr_Edgar(conn, index);
                break;
            }
            case 124: {
                Service.revenge(conn, index);
                break;
            }
            case 123: {
                Menu_Dungeon_Mode_Selection(conn, index);
                break;
            }
            case 122: {
                Menu_Clan_Manager(conn, index);
                break;
            }
            case -42: {
                Menu_Pet_Manager(conn, index);
                break;
            }
            case -37: {
                Menu_PhoChiHuy(conn, index);
                break;
            }
            case -38:
            case -40: {
                Menu_LinhCanh(conn, index);
                break;
            }
            case -41: {
                Menu_TienCanh(conn, index);
                break;
            }
            case -49: {
                Menu_top(conn, index, idmenu);
                break;
            }
//            case -81: {
//                Menu_diempk(conn, index);
//                break;
//            }
            case -69: { // chỉnh nhận event
                if (Manager.gI().event == 1) { // noel
                    Menu_Event(conn, index);
                }
                if (Manager.gI().event == 2) { // hè
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 3) { // vu lan
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 4) { // tết
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 5) { // trung thu
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 6) { // hallowin
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 7) {
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 8) { // hồn gió
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 11) { // hồn gió
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                if (Manager.gI().event == 12) { // hallween
                    Menu_MissSophia(conn, idnpc, idmenu, index);
                }
                break;
            }
            case -62: {
                if (Manager.gI().event == 1) {
                    Menu_NauKeo(conn, index);
                }
                break;
            }
            case -66: {
                if (Manager.gI().event == 1) {
                    Menu_CayThong(conn, index);
                }
                if (Manager.gI().event == 8){
                    return;
                }
                break;
            }
            case 120: {
                break;
            }
            case -91: {
                Menu_Khac(conn, idmenu, index);
                break;
            }
            case 998: {
                Menu_Krypton(conn, idmenu, index);
                break;
            }
            case -103: {
                break;
            }
            default: {
                //Service.send_notice_box(conn, "Đã xảy ra lỗi-menucontroller");
                break;
            }
        }
    }

    private static void Menu_Nang_Skill(Session conn, byte index) throws IOException {
        // Đệ tử
        if (!conn.p.isOwner) {
            return;
        }
        if (conn.p.skill_110[conn.p.id_index_temp] >= 10) {
            conn.p.id_index_temp = -1;
            Service.send_notice_box(conn, "Kỹ năng được nâng cấp tối đa");
            return;
        }
        int level = conn.p.skill_110[conn.p.id_index_temp];
        String name_book = "";
        if (conn.p.id_index_temp == 1) {
            name_book = switch (conn.p.clazz) {
                case 0 ->
                        "sách học bão lửa";
                case 1 ->
                        "sách học thần tốc";
                case 2 ->
                        "sách học cơn phẫn nộ";
                case 3 ->
                        "sách học súng điện từ";
                default ->
                        name_book;
            };
        } else if (conn.p.id_index_temp == 0) {
            name_book = switch (conn.p.clazz) {
                case 0 ->
                        "sách học khiên địa chấn";
                case 1 ->
                        "sách học bão độc";
                case 2 ->
                        "sách học băng trận";
                case 3 ->
                        "sách học súng thần công";
                default ->
                        name_book;
            };
        }
        String format = String.format("Để nâng từ cấp %s lên cấp %s bạn cần %s sách %s và %s ngọc."
                + " Bạn có muốn thực hiện", level, level + 1, level + 1, name_book, level * 5 + 10);
        if (index == 0) {
            Service.send_box_input_yesno(conn, -121, format);
        } else if (index == 1) {
            Service.send_box_input_yesno(conn, -120, format);
        }
    }
    private static void Menu_sieuthan(Session conn, byte index) throws IOException {
        try {
            if (conn.p.get_ngoc() < 10_000) {
                Service.send_notice_box(conn, "Không đủ 10k ngọc");
                return;
            }
            Item3 it_pr = null;
            for (int i = 0; i < conn.p.item.bag3.length; i++) {
                Item3 it = conn.p.item.bag3[i];
                if (it != null && it.id >= 4831 && it.id <= 4873 && it.color == 5 && it.tierStar <= 15) {
                    if (index == 0) {
                        it_pr = it;
                        break;
                    }
                    index--;
                }

            }
            if (it_pr != null) {
                Player p = conn.p;
                    Service.send_box_input_yesno(conn, -12, "Nâng cấp " + it_pr.name + "\n"
                            + (st.sl[p.st_ran[0]] + 2) + "/" + conn.p.item.total_item_by_id(7, st.id[p.st_ran[0]]) + " " + ItemTemplate7.item.get(st.id[p.st_ran[0]]).getName() + "\n"
                            + (st1.sl[p.st_ran[1]] + it_pr.tierStar) + "/" + conn.p.item.total_item_by_id(7, st1.id[p.st_ran[1]]) + " " + ItemTemplate7.item.get(st1.id[p.st_ran[1]]).getName() + "\n"
                            + (st2.sl[p.st_ran[2]] + it_pr.tierStar) + "/" + conn.p.item.total_item_by_id(7, st2.id[p.st_ran[2]]) + " " + ItemTemplate7.item.get(st2.id[p.st_ran[2]]).getName() + "\n"
                            + (st3.sl[p.st_ran[3]] + it_pr.tierStar) + "/" + conn.p.item.total_item_by_id(7, st3.id[p.st_ran[3]]) + " " + ItemTemplate7.item.get(st3.id[p.st_ran[3]]).getName() + "\n"
                            + (st4.sl[p.st_ran[4]] + it_pr.tierStar) + "/" + conn.p.item.total_item_by_id(7, st4.id[p.st_ran[4]]) + " " + ItemTemplate7.item.get(st4.id[p.st_ran[4]]).getName());
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void Menu_mdsieuthan(Session conn, byte index) throws IOException {
        try {
            if (conn.p.checkcoin() < 10_000) {
                Service.send_notice_box(conn, "Không đủ 10k coin");
                return;
            }
            Item3 it_pr = null;
            for (int i = 0; i < conn.p.item.bag3.length; i++) {
                Item3 it = conn.p.item.bag3[i];
                if (it != null && it.id >= 4587 && it.id <= 4590 && it.color == 5 && it.tierStar <= 15) {
                    if (index == 0) {
                        it_pr = it;
                        break;
                    }
                    index--;
                }
            }
            if (it_pr != null) {
                Player p = conn.p;
                Service.send_box_input_yesno(conn, -13, "Nâng cấp " + it_pr.name + "\n"
                        + sc.sl[p.st_ran[0]] * it_pr.tierStar / 9 + "/" + conn.p.item.total_item_by_id(7, sc.id[p.st_ran[0]]) + " " + ItemTemplate7.item.get(sc.id[p.st_ran[0]]).getName() + "\n"
                        + sc1.sl[p.st_ran[1]] * it_pr.tierStar / 9 + "/" + conn.p.item.total_item_by_id(7, sc1.id[p.st_ran[1]]) + " " + ItemTemplate7.item.get(sc1.id[p.st_ran[1]]).getName() + "\n"
                        + sc2.sl[p.st_ran[2]] * it_pr.tierStar / 9 + "/" + conn.p.item.total_item_by_id(7, sc2.id[p.st_ran[2]]) + " " + ItemTemplate7.item.get(sc2.id[p.st_ran[2]]).getName() + "\n"
                        + sc3.sl[p.st_ran[3]] * it_pr.tierStar / 9 + "/" + conn.p.item.total_item_by_id(7, sc3.id[p.st_ran[3]]) + " " + ItemTemplate7.item.get(sc3.id[p.st_ran[3]]).getName() + "\n"
                        + sc4.sl[p.st_ran[4]] * it_pr.tierStar / 9 + "/" + conn.p.item.total_item_by_id(7, sc4.id[p.st_ran[4]]) + " " + ItemTemplate7.item.get(sc4.id[p.st_ran[4]]).getName());
            }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void Menu_diemct(Session conn, byte index) throws IOException {
        if(!conn.p.isOwner){
            return;
        }
        switch (index){
            case 0:{//nlmd
                if (conn.p.pointarena < 1000) {
                    Service.send_notice_box(conn, "Phải cần tối thiểu 1000 điểm tích lũy chiến trường để có thể đổi trứng đại bàng.");
                } else if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Cần tối thiểu 1 ô trống để có thể đổi.");
                } else {
                    conn.p.pointarena -= 1000;
                    short id = (short) Util.random(146,346);
                    short quant = 5;
                    conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, quant, (byte) 7));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 1:{// hồ quang
                if (conn.p.pointarena < 1000) {
                    Service.send_notice_box(conn, "Phải cần tối thiểu 1000 điểm tích lũy chiến trường để có thể đổi trứng đại bàng.");
                } else if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Cần tối thiểu 1 ô trống để có thể đổi.");
                } else {
                    conn.p.pointarena -= 1000;
                    short id = (short) Util.random(481,493);
                    short quant = 10;
                    conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, quant, (byte) 7));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 2:{// ngọc rồng
                if (conn.p.pointarena < 1000) {
                    Service.send_notice_box(conn, "Phải cần tối thiểu 1000 điểm tích lũy chiến trường để có thể đổi trứng đại bàng.");
                } else if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Cần tối thiểu 1 ô trống để có thể đổi.");
                } else {
                    conn.p.pointarena -= 1000;
                    short id = (short) Util.random(465,472);
                    short quant = 3;
                    conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, quant, (byte) 7));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 3: {// tb2
                if (conn.p.pointarena < 2000) {
                    Service.send_notice_box(conn, "Phải cần tối thiểu 2000 điểm tích lũy chiến trường để có thể đổi trứng đại bàng.");
                } else if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Cần tối thiểu 1 ô trống để có thể đổi.");
                } else {
                    conn.p.pointarena -= 2000;
                    List<Integer> item3 = new ArrayList<>(Arrays.asList(4700, 4704, 4763, 4764, 4791, 4792));
                    short iditem = Util.random(item3, new ArrayList<>()).shortValue();
                    ;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 15;
                    itbag.islock = true;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 8;
                    itbag.time_use = 0;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 4:{// trứng đại bàng
                if (conn.p.pointarena < 50000) {
                    Service.send_notice_box(conn, "Phải cần tối thiểu 50000 điểm tích lũy chiến trường để có thể đổi trứng đại bàng.");
                } else if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_box(conn, "Cần tối thiểu 1 ô trống để có thể đổi.");
                } else {
                    try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `history_doi_dai_bang` WHERE `user` = '" + conn.user + "' AND `time` >= DATE_SUB(NOW(), INTERVAL 1 WEEK);")) {
                        if (rs.next()) {
                            Service.send_notice_box(conn, "Trong vòng 1 tuần 1 tài khoản chỉ có thể đổi 1 lần.");
                            return;
                        } else {
                            int last_point = conn.p.pointarena;
                            short iditem = 3269;
                            Item3 itbag = new Item3();
                            itbag.id = iditem;
                            itbag.name = ItemTemplate3.item.get(iditem).getName();
                            itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                            itbag.type = ItemTemplate3.item.get(iditem).getType();
                            itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                            itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                            itbag.color = ItemTemplate3.item.get(iditem).getColor();
                            itbag.part = ItemTemplate3.item.get(iditem).getPart();
                            itbag.tier = 0;
                            itbag.islock = false;
                            itbag.time_use = 0;
                            conn.p.item.add_item_bag3(itbag);
                            conn.p.pointarena -= 50000;
                            conn.p.item.char_inventory(3);
                            String query
                                    = "INSERT INTO `history_doi_dai_bang` (`user`, `name_player`, `last_point` , `point_arena`) VALUES ('"
                                    + conn.user + "', '" + conn.p.name + "', '" + last_point + "', '" + conn.p.pointarena + "')";
                            if (st.executeUpdate(query) > 0) {
                                connection.commit();
                            }
                            List<box_item_template> ids = new ArrayList<>();
                            ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                            Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
    private static void Menu_Mr_Ballard(Session conn, int idNPC, byte idmenu, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        switch (idmenu) {
            case 0: {
                switch (index) {
                    case 0: { // dang ky
                        if (ChienTruong.gI().getStatus() == 1) {
                            ChienTruong.gI().register(conn.p);
                        } else {
                            Service.send_notice_box(conn, "Không trong thời gian diễn ra");
                        }
                        break;
                    }
                    case 1: {
                        String s = "Đăng ký tại NPC Mr. Ballard ở map Hang Lửa trước thời gian bắt đầu 45 phút";
                        s += "Phí tham gia 2 ngọc hoặc 500.000 vàng";
                        s += "Người chơi phải từ level 40 trở lên";
                        s += "Cách chơi :";
                        s += " Người chơi được ngẫu nhiên chia đều làm 4 phe, khi gần đến giờ người chơi sẽ được chuyển đến map tập trung .";
                        s += "Đến đúng giờ diễn ra sự kiện , map tập trung sẽ mở cửa để đi sang các map khác.";

                        s += "Mỗi phe sẽ có một map tập trung của phe mình và ít nhất 1 map nhà chính , điều này tùy thuộc vào số người đăng ký . Tối đa 25 map";

                        s += "Ví dụ : tổng số người đăng ký là 40 người - mỗi phe 10 người - thì mỗi phe sẽ sở hữu 1 nhà chính . Nếu tổng số người đăng ký là 80 người - mỗi phe 20 người - thì mỗi phe sẽ sở hữu 2 nhà chính.";

                        s += "\nTại mỗi map nhà chính chỉ có thể cho phép 40 người vào cùng 1 lượt và chia đều 10 người mỗi phe. Nghĩa là tại mỗi map nhà chính bất kỳ chỉ có thể vào được 10 người trong cùng 1 phe.";
                        s += "\nTại mỗi map nhà chính sẽ có 10 lính canh , 10 lính canh này sẽ đánh tất cả những người nào khác phe của nó";
                        s += "\nNhững map nhà chính sẽ được nối với 1 map chung , tại map chung phút thứ 5 và phút thứ 10 sẽ xuất hiện Boss Xà Nữ, nếu Boss thứ nhất ở phút thứ 5 đầu tiên đến phút thứ 10 vẫn chưa bị chết thì vẫn xuất hiện Boss thứ 2. Đánh chết boss Xà Nữ sẽ được điểm chiến trường";

                        s += "\n4. Điều kiện chiến thắng :";
                        s += "Nếu chưa hết 60 phút mà đã có 3 phe sập hết nhà chính, chỉ còn lại 1 phe còn nhà chính thì phe đó là phe thắng cuộc, sự kiện sẽ kết thúc. Phần thưởng sẽ là tổng số ngọc đăng ký của tất cả các phe chia đều cho những người còn sống trong phe thắng cuộc.";
                        s += " Khi hết 60 phút mà chưa có phe nào chiến thắng thì phần thưởng sẽ là ngọc đăng ký chia đều cho tất cả những người còn sống trong đấu trường.";
                        s += "Người chơi nhớ vào NPC Lisa hoặc Emma để nhận quà";

                        s += "Điểm chiến trường";
                        s += " Cách kiếm điểm chiến trường gồm";
                        s += "Tiêu diệt một người chơi khác phe : 1 điểm";
                        s += "Giết quái hoặc một lính canh khác phe : 2 điểm";
                        s += "Tiêu diệt một nhà khác phe bất kì : 20 điểm";
                        s += "Sử dụng item chiến trường : 10 điểm";
                        s += "Đánh chết Boss Xà Nữ : 30 điểm";
                        Service.send_notice_box(conn, s);
                        break;
                    }
                    case 3: {
                        if (ChienTruong.gI().getStatus() == 2) {
//                            if (conn.p.time_use_item_arena > System.currentTimeMillis()) {
//                                Service.send_notice_box(conn,
//                                        "Chờ sau " + (conn.p.time_use_item_arena - System.currentTimeMillis()) / 1000 + " s");
//                                return;
//                            }
                            Member_ChienTruong info = ChienTruong.gI().get_infor_register(conn.p.name);
                            if (info != null) {
                                conn.p.time_use_item_arena = System.currentTimeMillis() + 250_000;
                                Vgo vgo = new Vgo();
                                switch (info.village) {
                                    case 2: { // lang gio
                                        vgo.id_map_go = 55;
                                        vgo.x_new = 224;
                                        vgo.y_new = 256;
                                        MapService.change_flag(conn.p.map, conn.p, 2);
                                        break;
                                    }
                                    case 3: { // lang lua
                                        vgo.id_map_go = 59;
                                        vgo.x_new = 240;
                                        vgo.y_new = 224;
                                        MapService.change_flag(conn.p.map, conn.p, 1);
                                        break;
                                    }
                                    case 4: { // lang set
                                        vgo.id_map_go = 57;
                                        vgo.x_new = 264;
                                        vgo.y_new = 272;
                                        MapService.change_flag(conn.p.map, conn.p, 4);
                                        break;
                                    }
                                    default: { // 5 lang anh sang
                                        vgo.id_map_go = 53;
                                        vgo.x_new = 276;
                                        vgo.y_new = 246;
                                        MapService.change_flag(conn.p.map, conn.p, 5);
                                        break;
                                    }
                                }
                                conn.p.change_map(conn.p, vgo);
                            } else {
                                Service.send_notice_box(conn, "Chưa đăng ký");
                            }
                            // Vgo vgo = new Vgo();
                            // vgo.id_map_go = 61;
                            // vgo.x_new = 432;
                            // vgo.y_new = 354;
                            // conn.p.change_map(conn.p, vgo);
                        } else {
                            Service.send_notice_box(conn, "Không trong thời gian diễn ra");
                        }
                        break;
                    }
                    case 2: {
                        send_menu_select(conn,2,new String[]{"Đổi ngẫu nhiên nlmd cấp 2-3","Đổi ngẫu nhiên hồ quang cấp 1-2-3", "Đổi ngẫu nhiên ngọc rồng 0*-7*", "Đổi ngẫu nhiên Trang bị 2(Hạn 8 giờ)", "Đổi trứng đại bàng"});
                        break;
                    }
                    default:
                        Service.send_notice_box(conn, "Chức năng đang được hoàn thiện.");
                        break;
                }
                break;
            }
            case 1: {
                Service.send_notice_box(conn, "Chức năng đang được hoàn thiện.");
                break;
            }
            //  }
        }

    }
    private static void Menu_doiruong(Session conn, byte index) throws IOException {
        if(!conn.p.isOwner){
            return;
        }
        switch (index){
            case 0: {
                short sl = 50;
                int coin = 50000;
                if (conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                } else if (conn.p.checkcoin() < coin) {
                    Service.send_notice_box(conn, "Không đủ coin");
                    return;
                } else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    List<box_item_template> ids = new ArrayList<>();
                    List<Integer> item3 = new ArrayList<>(Arrays.asList(4691, 4692, 4693, 4694));
                    short id = Util.random(item3, new ArrayList<>()).shortValue();
                    ids.add(new box_item_template(id, (short) 1, (byte) 3));
                    conn.p.item.add_item_bag3_default(id, 0, true);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 1: {
                short sl = 50;
                int coin = 50000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 496;
                    short quant = 10;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, quant, (byte) 7));
                    conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 2: {
                short sl = 50;
                int coin = 50000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 497;
                    short quant = 10;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, quant, (byte) 7));
                    conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 3: {
                short sl = 50;
                int coin = 50000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 498;
                    short quant = 10;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, quant, (byte) 7));
                    conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 4: {
                short sl = 100;
                int coin = 200000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 4796;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, (short) 1, (byte) 3));
                    conn.p.item.add_item_bag3_default(id, 0, true);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 5: {
                short sl = 150;
                int coin = 200000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short iditem = 4748;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    conn.p.item.add_item_bag3_default(iditem,0, true);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 6: {
                short sl = 200;
                int coin = 200000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 4617;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, (short) 1, (byte) 3));
                    conn.p.item.add_item_bag3_default(id, 0, true);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 7: {
                short sl = 250;
                int coin = 200000;
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 4626;
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(id, (short) 1, (byte) 3));
                    conn.p.item.add_item_bag3_default(id, 0, true);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 8: {
                short sl = 300;
                int coin = 500000;
                List<box_item_template> ids = new ArrayList<>();
                if(conn.p.item.total_item_by_id(4, 350) < sl) {
                    Service.send_notice_box(conn, "Không đủ rương đặc biệt");
                    return;
                }else if (conn.p.checkcoin() < coin){
                    Service.send_notice_box(conn,"Không đủ coin");
                    return;
                }else {
                    conn.p.update_coin(-coin);
                    conn.p.item.remove(4, 350, sl);
                    short id = 4708;
                    ids.add(new box_item_template(id, (short) 1, (byte) 3));
                    conn.p.item.add_item_bag3_default(id, 0, true);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
        }
    }
    private static void Menu_qua20_10(Player p) throws IOException {
        String text = "qua20_10";
        try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `giftcode2` WHERE `name_player` = '" + text + "';")) {
            byte empty_box = (byte) 0;
            if (!rs.next()) {
                Service.send_notice_box(p.conn, "Không Thấy Quà");
                return;
            }
            String mess = rs.getString("logger");
            empty_box = rs.getByte("empty_box");
            byte date = rs.getByte("date");
            if (p.item.get_bag_able() >= empty_box) {
                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    if (jsar2 == null || jsar2.size() < 1) {
                        continue;
                    }
                    Item3 itbag = new Item3();
                    short it = Short.parseShort(jsar2.get(0).toString());
                    itbag.id = it;
                    itbag.name = ItemTemplate3.item.get(it).getName();
                    itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                    itbag.type = ItemTemplate3.item.get(it).getType();
                    itbag.level = ItemTemplate3.item.get(it).getLevel();
                    itbag.icon = ItemTemplate3.item.get(it).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                    itbag.color = ItemTemplate3.item.get(it).getColor();
                    itbag.part = ItemTemplate3.item.get(it).getPart();
                    itbag.tier = 0;
                    itbag.time_use = 0;
                    itbag.islock = false;
                    if (date > 0) {
                        itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                    }
                    p.item.add_item_bag3(itbag);
                }
                jsar.clear();
                //
                jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Item47 itbag = new Item47();
                    itbag.id = Short.parseShort(jsar2.get(0).toString());
                    itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                    itbag.category = 4;
                    p.item.add_item_bag47(4, itbag);
                }
                jsar.clear();
                //
                jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                for (int i = 0; i < jsar.size(); i++) {
                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                    Item47 itbag = new Item47();
                    itbag.id = Short.parseShort(jsar2.get(0).toString());
                    itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                    itbag.category = 7;
                    p.item.add_item_bag47(7, itbag);
                }
                jsar.clear();
                p.update_vang(rs.getLong("vang"));
                p.update_ngoc(rs.getLong("ngoc"));
                p.update_coin(rs.getInt("coin"));
                Log.gI().add_log(p.name, "Get order :" + rs.getInt("id"));
                p.item.char_inventory(5);
                p.item.char_inventory(3);
                p.item.char_inventory(4);
                p.item.char_inventory(7);
                Service.send_notice_box(p.conn, mess);
            } else {
                Service.send_notice_box(p.conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Leo_thap d;
    private static void Menu_shopcoin(Session conn, byte index) throws IOException{
        if (!conn.p.isOwner){
            return;
        }
        conn.p.ResetCreateItemStar();
        switch (index){
            case 0: {
                Service.send_box_UI(conn, 37);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 48);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 99);
                break;
            }
            case 3: {
                send_menu_select(conn, -96,new String[]{"Hướng dẫn","Tiến hóa đồ tinh tú[VIP PRO]"});
                break;
            }
            case 4: {
                send_menu_select(conn, -95,new String[]{"Hướng dẫn","Tiến hóa mề đay"});
                break;
            }
            case 5: {
                send_menu_select(conn, -94,new String[]{"Hướng dẫn","Cường hóa trang bị 2"});
                break;
            }
            case 6: {
                send_menu_select(conn, -98,new String[]{"Mốc 100 điểm(cánh V1)","Mốc 200 điểm(Cánh V2)","Mốc 300 điểm(Cánh V3)", "Mốc 300 điểm(Áo choàng đại gia)", "Mốc 400 điểm(Cánh V4)", "Mốc 400 điểm(Áo choàng Triệu phú)", "Mốc 500 điểm(Cánh V5)", "Mốc 500 điểm(Áo choàng tỷ phú)"});
                break;
            }
            case 7: {
                //Service.send_notice_box(conn,"Chưa ra mắt");
                Service.send_box_input_text(conn,49,"Đổi đồng money",new String[]{"Nhập số lượng"});
                break;
            }
            case 8: {
                //Service.send_notice_box(conn,"Chưa ra mắt");
                Service.send_box_input_text(conn,50,"Hợp thể buff",new String[]{"Nhập số lượng"});
                break;
            }
            case 9: {
                if(d == null) {
                    if (conn.p.item.total_item_by_id(4, 342) <= 0) {
                        Service.send_notice_box(conn, "Không có vé săn boss");
                        return;
                    }
                    send_menu_select(conn,-97,new String[]{"Mức độ dễ","Mức độ trung bình-dễ","Mức độ trung bình","Mức độ khó","Mức độ khó[VIP PRO]"});
                }
                send_menu_select(conn,-97,new String[]{"Mức độ dễ","Mức độ trung bình-dễ","Mức độ trung bình","Mức độ khó","Mức độ khó[VIP PRO]"});
                break;
            }
            case 10: {
                send_menu_select(conn, -92, new String[]{"Nâng level bằng coin","Nâng level bằng vàng"});
                break;
            }
            case 11: {
                if(conn.p.level < 150 && conn.ac_admin < 111){
                    Service.send_notice_box(conn,"Chưa mở");
                    return;
                }
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 137;
                vgo.x_new = 150;
                vgo.y_new = 54;
                conn.p.change_map(conn.p, vgo);
                break;
            }
//            case 10: {
//                if (!Manager.isqua) {
//                    Service.send_notice_box(conn, "Đã hết TIME nhận quà");
//                    return;
//                }
//                if (conn.p.clazz != 2 && conn.p.clazz != 3){
//                    Service.send_notice_box(conn,"Bạn ko phải phái nữ");
//                    return;
//                }
//                if (conn.p.level < 100){
//                    Service.send_notice_box(conn,"Đừng tạo clone nữa. Muốn nhận quà hãy up lên level 100");
//                    return;
//                }
//                if(conn.p.chuyencan > 0){
//                    Service.send_notice_box(conn,"Bạn đã nhận quà rồi");
//                    return;
//                }
//                Menu_qua20_10(conn.p);
//                conn.p.chuyencan += 1;
//                break;
//            }
            default:{
                Service.send_notice_box(conn,"Chưa có chức năng!!!");
                break;
            }
        }
    }
    private static void Menu_nanglevel(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {
                if(conn.p.checkcoin() < 2_000_000_000 && conn.ac_admin < 111){
                    Service.send_notice_nobox_white(conn,"Chưa mở");
                    return;
                }
                if (conn.p.stt_level >= 0 && conn.p.stt_level < 1 && conn.p.level == 140 ||
                        conn.p.stt_level >= 1 && conn.p.stt_level < 2 && conn.p.level == 141 ||
                        conn.p.stt_level >= 2 && conn.p.stt_level < 3 && conn.p.level == 142 ||
                        conn.p.stt_level >= 3 && conn.p.stt_level < 4 && conn.p.level == 143 ||
                        conn.p.stt_level >= 4 && conn.p.stt_level < 5 && conn.p.level == 144 ||
                        conn.p.stt_level >= 5 && conn.p.stt_level < 6 && conn.p.level == 145 ||
                        conn.p.stt_level >= 6 && conn.p.stt_level < 7 && conn.p.level == 146 ||
                        conn.p.stt_level >= 7 && conn.p.stt_level < 8 && conn.p.level == 147 ||
                        conn.p.stt_level >= 8 && conn.p.stt_level < 9 && conn.p.level == 148 ||
                        conn.p.stt_level >= 9 && conn.p.stt_level < 10 && conn.p.level == 149 ||
                        conn.p.stt_level >= 10 && conn.p.stt_level < 11 && conn.p.level == 150){
                    if (Util.random(100) < 10){
                        conn.p.tiemnang += 50;
                        conn.p.stt_level++;
                        Service.send_notice_box(conn ,"Xin chúc mừng!!!");
                    }else {
                        Service.send_notice_box(conn ,"Thất bại rồi");
                    }
                } else {
                    Service.send_notice_box(conn,"Chưa đạt yêu cầu level để nâng cấp");
                }
                break;
            }
            case 1: {
                if(conn.p.checkcoin() < 2_000_000_000){
                    Service.send_notice_nobox_white(conn,"Chưa mở");
                    return;
                }
                if (conn.p.stt_level >= 0 && conn.p.stt_level < 1 && conn.p.level == 140 ||
                        conn.p.stt_level >= 1 && conn.p.stt_level < 2 && conn.p.level == 141 ||
                        conn.p.stt_level >= 2 && conn.p.stt_level < 3 && conn.p.level == 142 ||
                        conn.p.stt_level >= 3 && conn.p.stt_level < 4 && conn.p.level == 143 ||
                        conn.p.stt_level >= 4 && conn.p.stt_level < 5 && conn.p.level == 144 ||
                        conn.p.stt_level >= 5 && conn.p.stt_level < 6 && conn.p.level == 145 ||
                        conn.p.stt_level >= 6 && conn.p.stt_level < 7 && conn.p.level == 146 ||
                        conn.p.stt_level >= 7 && conn.p.stt_level < 8 && conn.p.level == 147 ||
                        conn.p.stt_level >= 8 && conn.p.stt_level < 9 && conn.p.level == 148 ||
                        conn.p.stt_level >= 9 && conn.p.stt_level < 10 && conn.p.level == 149 ||
                        conn.p.stt_level >= 10 && conn.p.stt_level < 11 && conn.p.level == 150){
                    if (Util.random(100) < 2){
                        conn.p.tiemnang += 30;
                        conn.p.stt_level++;
                        Service.send_notice_box(conn ,"Xin chúc mừng!!!");
                    }else {
                        Service.send_notice_box(conn ,"Thất bại rồi");
                    }
                } else {
                    Service.send_notice_box(conn,"Chưa đạt yêu cầu level để nâng cấp");
                }
                break;
            }
        }
    }
    private static void Menu_quavip(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {
                if (conn.p.item.total_item_by_id(4,113)< 100){
                    Service.send_notice_box(conn,"Không đủ hoa tuyết");
                    return;
                }
                conn.p.item.remove(4, 113, 100);
                Quask.Menu_noel(conn,(byte) 5);
                break;
            }
            case 1: {
                if (conn.p.item.total_item_by_id(4,113)< 100){
                    Service.send_notice_box(conn,"Không đủ hoa tuyết");
                    return;
                }
                conn.p.item.remove(4, 113, 100);
                Quask.Menu_noel(conn,(byte) 6);
                break;
            }
            case 2: {
                if (conn.p.item.total_item_by_id(4,113)< 100){
                    Service.send_notice_box(conn,"Không đủ hoa tuyết");
                    return;
                }
                conn.p.item.remove(4, 113, 100);
                Quask.Menu_noel(conn,(byte) 7);
                break;
            }
            case 3: {
                if (conn.p.item.total_item_by_id(4,113)< 100){
                    Service.send_notice_box(conn,"Không đủ hoa tuyết");
                    return;
                }
                conn.p.item.remove(4, 113, 100);
                Quask.Menu_noel(conn,(byte) 8);
                break;
            }
            default: {
                Service.send_notice_box(conn,"Chưa có chức năng...vip");
                break;
            }
        }
    }
    private static void Menu_tb2(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {
                Service.send_notice_box(conn,"Cường hóa đồ sẽ được tăng rất nhiều dame!\n"
                        +"Để cường hóa trang bị 2 cần ngọc nâng cấp, vàng và coin!\n"
                        +"Một lần cường hóa trang bị 2 cần 100 ngọc nâng cấp, 5.000.000 vàng và 50.000 coin!\n"
                        +"Cường hóa trang bị 2 thành công thì ngọc nâng giữ 100 cái, vàng và coin nhân 2 so với lần trước!\n"
                        +"Ngọc nâng cấp kiếm được từ đánh boss cá nhân\n"
                        +"Cường hóa trang bị 2 cao nhất là 100 lần.");
                break;
            }
            case 1: {
                conn.p.istb2 = true;
                Service.send_box_UI(conn, 33);
                break;
            }
            default: {
                Service.send_notice_box(conn,"Chưa có chức năng...");
                break;
            }
        }
    }
    private static void Menu_tienhoatt(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {
                Service.send_notice_box(conn,"Tiến hóa đồ sẽ được tăng rất nhiều dame!\n"
                        +"Để Để tiến hóa đồ tinh tú[VIP PRO] cần nlmd cấp 3 ngẫu nhiên, vàng và coin!\n"
                        +"Một lần tiến hóa lần đầu cần 10 cái của 5 loại nguyên liệu mề đay, 10.000.000 vàng và 50.000 coin!\n"
                        +"Tiến hóa thành công thì nguyên liệu mề đay sẽ tăng lên 2 cái, vàng và coin nhân 2 so với lần trước!\n"
                        +"Tiến hóa cao nhất là 15 lần.");
                break;
            }
            case 1: {
                conn.p.isdothan = true;
                Service.send_box_UI(conn, 33);
                break;
            }
            default: {
                Service.send_notice_box(conn,"Chưa có chức năng...");
                break;
            }
        }
    }
    private static void Menu_tienhoamd(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {
                Service.send_notice_box(conn,"Tiến hóa đồ sẽ được tăng rất nhiều dame!\n"
                        +"Để Để tiến hóa Mề đay cần nlmd cấp 3 ngẫu nhiên, vàng và coin!\n"
                        +"Một lần tiến hóa lần đầu cần 10 cái của 5 loại nguyên liệu mề đay, 10.000.000 vàng và 50.000 coin!\n"
                        +"Tiến hóa thành công thì nguyên liệu mề đay sẽ tăng lên 2 cái, vàng và coin nhân 2 so với lần trước!\n"
                        +"Tiến hóa cao nhất là 15 lần.");
                break;
            }
            case 1: {
                conn.p.ismdthan = true;
                Service.send_box_UI(conn, 33);
                break;
            }
            default: {
                Service.send_notice_box(conn,"Chưa có chức năng...");
                break;
            }
        }
    }
    private static void Menu_dokho(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {
                conn.p.dokho = 1;
                Service.send_box_input_yesno(conn, -118, "Bạn muốn vào đánh boss với mức độ dễ");
                break;
            }
            case 1: {
                conn.p.dokho = 2;
                Service.send_box_input_yesno(conn, -118, "Bạn muốn vào đánh boss với mức độ trung bình-dễ");
                break;
            }
            case 2: {
                conn.p.dokho = 3;
                Service.send_box_input_yesno(conn, -118, "Bạn muốn vào đánh boss với mức độ trung bình");
                break;
            }
            case 3: {
                conn.p.dokho = 4;
                Service.send_box_input_yesno(conn, -118, "Bạn muốn vào đánh boss với mức độ khó");
                break;
            }
            case 4: {
                conn.p.dokho = 5;
                Service.send_box_input_yesno(conn, -118, "Bạn muốn vào đánh boss với mức độ khó[VIP PRO]");
                break;
            }
        }
    }
    private static void Menu_quanap(Session conn, byte index) throws IOException{
        switch (index){
            case 0: {//100
                try {
                    if (conn.p.check_nap() >= 100) {
                        if (conn.p.item.get_bag_able() < 5) {
                            Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                            return;
                        }
                        short iditem = 4793;
                        Item3 itbag = new Item3();
                        itbag.id = iditem;
                        itbag.name = ItemTemplate3.item.get(iditem).getName();
                        itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                          itbag.type = ItemTemplate3.item.get(iditem).getType();
                        itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                        itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                        itbag.color = ItemTemplate3.item.get(iditem).getColor();
                        itbag.part = ItemTemplate3.item.get(iditem).getPart();
                        itbag.op = ItemTemplate3.item.get(iditem).getOp();
                        itbag.tier = 0;
                        itbag.islock = true;
                        itbag.time_use = 0;
                        itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                        conn.p.item.add_item_bag3(itbag);
                        conn.p.update_nap(-100);
                        conn.p.item.char_inventory(5);
                        conn.p.item.remove(4, 326, 1);
                        List<box_item_template> ids = new ArrayList<>();
                        ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                        Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    }else {
                        Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có "+conn.p.check_nap()+" điểm tích lũy.");
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                break;
            }
            case 1: {//200
                if (conn.p.check_nap() >= 200) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4794;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-200);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có "+conn.p.check_nap()+" điểm tích lũy.");
                }
                break;
            }
            case 2: {//300
                if (conn.p.check_nap() >= 300) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4795;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-300);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                } else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có " + conn.p.check_nap() + " điểm tích lũy.");
                }
                break;
            }
            case 3: {//300
                if (conn.p.check_nap() >= 300) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4748;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-300);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                } else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có " + conn.p.check_nap() + " điểm tích lũy.");
                }
                break;
            }
            case 4: {//400
                if (conn.p.check_nap() >= 400) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4796;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-400);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                } else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có " + conn.p.check_nap() + " điểm tích lũy.");
                }
                break;
            }
            case 5: {//400
                if (conn.p.check_nap() >= 400) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4747;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-400);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                } else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có " + conn.p.check_nap() + " điểm tích lũy.");
                }
                break;
            }
            case 6: {//500
                if (conn.p.check_nap() >= 500) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4797;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-500);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                } else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có " + conn.p.check_nap() + " điểm tích lũy.");
                }
                break;
            }
            case 7: {//500
                if (conn.p.check_nap() >= 500) {
                    if (conn.p.item.get_bag_able() < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 ô trống");
                        return;
                    }
                    short iditem = 4746;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.op = ItemTemplate3.item.get(iditem).getOp();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.update_nap(-500);
                    conn.p.item.char_inventory(5);
                    conn.p.item.remove(4, 326, 1);
                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                } else {
                    Service.send_notice_box(conn, "Chưa đủ điểm để đổi.\n Bạn đang có " + conn.p.check_nap() + " điểm tích lũy.");
                }
                break;
            }
            default:{
                Service.send_notice_box(conn,"Chưa thêm mốc điểm");
                break;
            }
        }
    }
    private static void Menu_MissSophia(Session conn, int idNPC, byte idmenu, byte index) throws IOException {
//        System.out.println("core.MenuController.Menu_MissSophia() id: "+idmenu);
//        System.out.println("core.MenuController.Menu_MissSophia() idx: "+index);
//        System.out.println("core.MenuController.Menu_MissSophia() ev: "+Manager.gI().event);
        if (idmenu == 2 && Manager.gI().event == 2) {
            try{
            switch (index) {
                case 0: {
                    if (conn.p.level < 40) {
                        Service.send_notice_box(conn, "Level quá thấp.");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 4) {
                        Service.send_notice_box(conn, "Hành trang đầy");
                        return;
                    }
                    if (conn.p.item.total_item_by_id(4, 141) < 1 && (!Manager.BuffAdminMaterial || conn.ac_admin < 4)) {
                        Service.send_notice_box(conn, "Thiếu " + ItemTemplate4.item.get(141).getName());
                        return;
                    }
                    for (int i = 254; i <= 258; i++) {
                        if (conn.p.item.total_item_by_id(4, i) < 1 && (!Manager.BuffAdminMaterial || conn.ac_admin < 4)) {
                            Service.send_notice_box(conn, "Thiếu " + ItemTemplate4.item.get(i).getName());
                            return;
                        }
                    }

                    conn.p.item.remove(4, 141, 1);
                    for (int i = 254; i <= 258; i++) {
                        conn.p.item.remove(4, i, 1);
                    }
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(Arrays.asList(0, 1, 4, 8, 9, 10, 11, 12, 13, 14));
                    List<Integer> it7_vip = new ArrayList<>(Arrays.asList(33, 346, 347, 349));
                    List<Integer> it4 = new ArrayList<>(Arrays.asList(2, 5, 61, 67, 269));
                    List<Integer> it4_vip = new ArrayList<>(Arrays.asList(131, 123, 132, 133, 52, 235, 147));
                    for (int i = 0; i < Util.random(1, 5); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 2) {// sách
                            short idsach = (short) Util.random(4577, 4585);
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran < 6) {
                            short idsach = (short) 4762;
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, Util.random(10, 20), true);
                        } else if (ran < 14) {
                            short id = (short) Util.random(46, 246);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 24) {
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 41) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 57) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 2);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 77) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
                    Event_2.add_caythong(conn.p.name, 1);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                case 1: {
                    send_menu_select(conn, 120, Event_2.get_top());
                    break;
                }
                case 2: {
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Hành trang đầy");
                        return;
                    }
                    if (conn.p.item.total_item_by_id(4, 123) < 5) {
                        Service.send_notice_box(conn, "Cần tối thiểu 5 chuông vàng");
                        return;
                    }
                    List<box_item_template> ids = new ArrayList<>();
                    conn.p.item.remove(4, 123, 5);
                    List<Integer> it = new ArrayList<>(Arrays.asList(4612, 4632, 4633, 4634, 4635));
                    List<Integer> it4 = new ArrayList<>(Arrays.asList(299, 205, 207));
                    if (Util.random(100) < 60) {
                        short id = Util.random(it4, new ArrayList<>()).shortValue();
                        short quant = (short) Util.random(1, 3);
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_box47(id, quant, (byte) 4);
                    } else {
                        short id = Util.random(it, new ArrayList<>()).shortValue();
                        ids.add(new box_item_template(id, (short) 1, (byte) 3));
                        conn.p.item.add_item_bag3_default(id, Util.random(5, 7), true);
                    }

                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev2!");
                    break;
            }
            }catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (idmenu == 3 && Manager.gI().event == 3) {
            try{
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 25, "Đổi bó sen trắng", new String[]{"30 sen trắng + 100k vàng"});
                    break;
                }
                case 1: {
                    Service.send_box_input_text(conn, 26, "Đổi hoa sen hồng", new String[]{"10 sen trắng + 25k vàng"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 27, "Đổi bó sen hồng", new String[]{"5 sen hồng + 100 ngọc"});
                    break;
                }
                case 3: {
                    send_menu_select(conn, 120, Event_3.get_top());
                    break;
                }
                case 4: {
                    if (conn.p.get_ngoc() < 100 || conn.p.item.total_item_by_id(4, 304) < 10) {
                        Service.send_notice_box(conn, "Cần tối thiểu 100 ngọc và 10 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-100);
                    conn.p.item.remove(4, 304, 10);
                    Item47 itbag = new Item47();
                    itbag.id = 246;
                    itbag.quantity = (short) 100;
                    itbag.category = 4;
                    conn.p.item.add_item_bag47(4, itbag);
                    conn.p.item.char_inventory(5);

                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{246}, new int[]{100}, new short[]{4});
                    break;
                }
                case 5: {
                    if (conn.p.get_ngoc() < 200 || conn.p.item.total_item_by_id(4, 304) < 50) {
                        Service.send_notice_box(conn, "Cần tối thiểu 200 ngọc và 50 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-200);
                    conn.p.item.remove(4, 304, 50);
                    short iditem = 3616;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);

                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                case 6: {
                    if (conn.p.get_ngoc() < 200 || conn.p.item.total_item_by_id(4, 304) < 50) {
                        Service.send_notice_box(conn, "Cần tối thiểu 200 ngọc và 50 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-200);
                    conn.p.item.remove(4, 304, 50);
                    short iditem = 4761;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);

                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                case 7: {
                    if (conn.p.get_ngoc() < 500 || conn.p.item.total_item_by_id(4, 304) < 50) {
                        Service.send_notice_box(conn, "Cần tối thiểu 500 ngọc và 50 bông sen hồng để đổi!");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Không đủ ô trống!");
                        return;
                    }
                    conn.p.update_ngoc(-500);
                    conn.p.item.remove(4, 304, 50);
                    short iditem = 4642;
                    Item3 itbag = new Item3();
                    itbag.id = iditem;
                    itbag.name = ItemTemplate3.item.get(iditem).getName();
                    itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                    itbag.type = ItemTemplate3.item.get(iditem).getType();
                    itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                    itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                    itbag.op = new ArrayList<>();
                    itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                    itbag.color = ItemTemplate3.item.get(iditem).getColor();
                    itbag.part = ItemTemplate3.item.get(iditem).getPart();
                    itbag.tier = 0;
                    itbag.islock = false;
                    itbag.time_use = 0;
                    itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30;
                    conn.p.item.add_item_bag3(itbag);
                    conn.p.item.char_inventory(5);

                    List<box_item_template> ids = new ArrayList<>();
                    ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev3!");
                    break;
            }
            }catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (idmenu == 4 && Manager.gI().event == 4) { // sự kiện tết
            try{
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 40, "Làm bánh Trưng", new String[]{"20 gạo nếp + 20 đậu xanh + 20 lá dong + 100k vàng"});
                    break;
                }
                case 1: {
                    Service.send_box_input_text(conn, 41, "Làm bánh Trưng V.I.P", new String[]{"20 gạo nếp + 20 đậu xanh + 20 lá dong + 50 ngọc"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 42, "Làm bánh Dày", new String[]{"20 gạo nếp + 20 đậu xanh + 20 lá dong + 100k vàng"});
                    break;
                }
                case 3: {
                    Service.send_box_input_text(conn, 43, " Ghép Chữ HAPPY NEW YEAR", new String[]{"Bộ chữ HAPPY NEW YEAR + 100.000 vàng "});
                    break;
                }
                case 4: {
                    Service.send_box_input_text(conn, 44, "Làm Pháo Hoa", new String[]{"5 giấy + 5 thuốc nổ + 30 ngọc "});
                    break;
                }
                case 5: {
                    send_menu_select(conn, 120, Event_4.get_top());
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev4!");
                    break;
            }
            }catch (Exception e) {
                System.out.println("Lỗi: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (idmenu == 5 && Manager.gI().event == 5) { // sự kiện trung thu
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 45, "Làm lồng đèn", new String[]{"10 Giấy + 10 Tre + 30 ngọc"});
                    break;
                }
                case 1: {
                    Service.send_box_input_text(conn, 46, "Làm bánh nướng", new String[]{"50 Bột + 50 Trứng + 50 Đậu xanh + 50 Thịt + 100k vàng"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 47, "Làm bánh Dẻo", new String[]{"100 Bột + 100 Trứng + 100 Đậu xanh + 100k vàng"});
                    break;
                }
                case 3: {
                    send_menu_select(conn, 120, Event_5.get_top());
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev5!");
                    break;
            }
        } else if (idmenu == 6 && Manager.gI().event == 6) { // sự kiện halloween
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 48, "Rương huyền bí", new String[]{"Bộ chữ H A L O W E N + 100.000 vàng"});
                    break;
                }
                case 1: {
                    send_menu_select(conn, 120, Event_6.get_top());
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev6!");
                    break;
            }
        } else if (idmenu == 7 && Manager.gI().event == 7) { // sự kiện xoài
            switch (index) {
                case 0: {
                    if(conn.p.level < 44){
                        Service.send_notice_box(conn,"Bạn chưa đủ level");
                        return;
                    }
                    Vgo vgo = null;
                    vgo = new Vgo();
                    vgo.id_map_go = 136;
                    vgo.x_new = 150;
                    vgo.y_new = 54;
                    conn.p.change_map(conn.p, vgo);
                    break;
                }
                case 1: {
                    Service.send_box_input_text(conn, 54, "Rương sự kiện Xoài thường", new String[]{"Nguyên liệu số 1 + 500.000 vàng + 30.000 coin"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 55, "Rương sự kiện Xoài VIP", new String[]{"(Nguyên liệu số 1 + 2) + 2.000.000 vàng + 120.000 coin"});
                    break;
                }
                case 3: {
                    BXH.send1(conn,2);
                    break;
                }
                case 4: {
                    BXH.send3(conn, 2);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev11!");
                    break;
            }
        } else if (idmenu == 8 && Manager.gI().event == 8) { // sự kiện halloween
            switch (index) {
                case 0: {
                    if(conn.p.get_vang() < 20_000_000){
                        Service.send_notice_box(conn,"Không đủ 20.000.000 vàng");
                        return;
                    }
                    if (conn.p.item.get_bag_able() < 1){
                        Service.send_notice_box(conn,"Hành trang đầy");
                        return;
                    }
                    if (conn.p.item.total_item_by_id(4,30) < 200){
                        Service.send_notice_box(conn,"Không đủ 200 thịt heo");
                        return;
                    }
                    if (conn.p.diemsukien > 0){
                        Service.send_notice_box(conn,"Mỗi ngày chỉ đổi được 10 bánh quy");
                        return;
                    }
                    conn.p.diemsukien += 11;
                    conn.p.update_vang(-20_000_000);
                    short id = 183;
                    short quant = 10;
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    Service.Show_open_box_notice_item(conn.p,"Bạn nhận được", new short[]{id},new int[]{quant}, new short[]{4});
                    break;
                }
                case 1: {
                    send_menu_select(conn,-93,new String[]{"Đổi Tuần Lộc PROVIP","Đổi Dây Chuyền PROVIP","Đổi Găng PROVIP","Đổi Giày PROVIP"});
                    break;
                }
                case 2: {
                    send_menu_select(conn, 120, Event_8.get_top());
                    break;
                }
                case 3: {
                    if (Manager.gI().bossTG.p.isDie) {
                        Service.send_notice_box(conn, "Chưa thể vào map thời gian này!");
                        return;
                    }
                    boolean enter = true;
                    long t = 0;
                    if (Manager.gI().bossTG.time_enter_map.containsKey(conn.p.name)) {
                        t = Manager.gI().bossTG.time_enter_map.get(conn.p.name);
                        if (t > System.currentTimeMillis()) {
                            enter = false;
                        }
                    } else {
                        Manager.gI().bossTG.time_enter_map.put(conn.p.name, 0L);
                    }
                    if (enter) {
                        MapService.leave(conn.p.map, conn.p);
                        conn.p.map = Manager.gI().bossTG.map;
                        conn.p.x = 504;
                        conn.p.y = 288;
                        MapService.enter(conn.p.map, conn.p);
                    } else {
                        Service.send_notice_box(conn, "Hãy chờ " + ((t - System.currentTimeMillis()) / 1000) + " s để vào lại");
                    }
                    break;
                }
                case 4: {
                    if (Manager.gI().bossTG.top_dame.size() > 1) {
                        Collections.sort(Manager.gI().bossTG.top_dame, new Comparator<Top_Dame>() {
                            @Override
                            public int compare(Top_Dame o1, Top_Dame o2) {
                                return (o1.dame > o2.dame) ? -1 : 1;
                            }
                        });
                    }
                    String[] list = new String[]{"Trống"};
                    if (Manager.gI().bossTG.top_dame.size() > 0) {
                        int size = Manager.gI().bossTG.top_dame.size();
                        list = new String[(size > 10) ? 10 : size];
                        for (int i = 0; i < list.length; i++) {
                            list[i] = "Top " + (i + 1) + " : " + Manager.gI().bossTG.top_dame.get(i).name + " "
                                    + String.format("%.02f", ((float) Manager.gI().bossTG.top_dame.get(i).dame / 1_000_000f)) + "m";
                        }
                    }
                    send_menu_select(conn, 120, list);
                    break;
                }
                case 5: {
                    if (!Manager.gI().bossTG.p.isDie) {
                        Service.send_notice_box(conn, "Chưa thể nhận vào thời gian này!");
                        return;
                    }
                    Top_Dame td = null;
                    for (int i = 0; i < Manager.gI().bossTG.top_dame.size(); i++) {
                        if (i >= 10) {
                            break;
                        }
                        Top_Dame td2 = Manager.gI().bossTG.top_dame.get(i);
                        if (td2.name.equals(conn.p.name)) {
                            if (!td2.receiv) {
                                td = td2;
                                break;
                            } else {
                                Service.send_notice_box(conn, "Đã nhận rồi, khôn vừa thôi!");
                                return;
                            }
                        }
                    }
                    if (td != null) {
                        if (conn.p.item.get_bag_able() < 3) {
                            Service.send_notice_box(conn, "Hành trang không đủ chỗ!");
                            return;
                        }
                        switch (Manager.gI().bossTG.top_dame.indexOf(td)) {
                            case 0: {
                                List<Integer> item = new ArrayList<>(java.util.Arrays.asList(353, 354));
                                if (Util.random(0,100) < 5){
                                    short id = 274;
                                    short quant = 1;
                                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                                }
                                conn.p.update_vang(30_000_000);
                                conn.p.update_coin(100_000);
                                short id = Util.random(item, new ArrayList<>()).shortValue();
                                short quant = 5;
                                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                                break;
                            }
                            case 1: {
                                conn.p.update_vang(20_000_000);
                                List<Integer> item = new ArrayList<>(java.util.Arrays.asList(356, 355));
                                short id = Util.random(item, new ArrayList<>()).shortValue();
                                short quant = 5;
                                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                                break;
                            }
                            case 2: {
                                conn.p.update_vang(10_000_000);
                                List<Integer> item = new ArrayList<>(java.util.Arrays.asList(356, 355));
                                short id = Util.random(item, new ArrayList<>()).shortValue();
                                short quant = 2;
                                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                                break;
                            }
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                            case 9: {
//                                List<Integer> item = new ArrayList<>(java.util.Arrays.asList(356));
                                conn.p.update_vang(1_000_000);
                                short id = 356;
                                short quant = 2;
                                conn.p.item.add_item_bag47(id, quant, (byte) 4);
                                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{quant}, new short[]{4});
                                break;
                            }
                        }
                        td.receiv = true;
                    } else {
                        Service.send_notice_box(conn, "Không có trong top mà đòi nhận thưởng cái éo gì?");
                    }
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev8!");
                    break;
            }
        } else if (idmenu == 11 && Manager.gI().event == 11) { // sự kiện halloween
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 51, "Linh hồn 4 mùa thường", new String[]{"4 loại hồn gió + 500.000 vàng"});
                    break;
                }
                case 1: {
                    Service.send_box_input_text(conn, 52, "Linh Hồn 4 Mùa Trung Cấp", new String[]{"4 loại hồn gió + 10.000 coin"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 53, "Linh Hồn 4 Mùa Cao Cấp", new String[]{"4 loại hồn gió + 2.000.000 vàng + 20.000 coin"});
                    break;
                }
                case 3: {
                    BXH.send1(conn,2);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev11!");
                    break;
            }
        } else if (idmenu == 12 && Manager.gI().event == 12) { // sự kiện halloween
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 60, "Rương thường", new String[]{"5 Nguyên liệu"});
                    break;
                }
                case 1: {
                    send_menu_select(conn,-122, new String[]{"Đổi vũ khí trang bị 2","Đổi ngọc xuyên tim","Đổi ngọc châu bò","Đổi ngọc cuồng bạo"
                            ,"Đổi cánh v4 vĩnh viễn","Đổi áo choàng đại gia","Đổi pet phượng rồng lửa","Đổi pet phượng hoàng băng","Đổi pet dê con"});
                    break;
                }
                case 2: {
                    Service.send_box_input_text(conn, 61, "Rương đặc biệt", new String[]{"10 rương thường"});
                    break;
                }
                case 3: {
                    BXH.send1(conn,1);
                    break;  
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng ev12!");
                    break;
            }
        } else {
            Service.send_notice_box(conn, "menu: " + idmenu + "  ev: " + Manager.gI().event);
        }

    }

    private static void Menu_MobEvent(Session conn, int idmob, byte idmenu, byte index) throws IOException {
        try{
        if (idmenu == 2) {
            if (index != 0) {
                return;
            }
            if (conn.p.level < 40) {
                Service.send_notice_box(conn, "Cần lên level 40 để có thể chơi sự kiện.");
                return;
            }
            MobCay mob = Event_2.getMob(idmob);
            if (mob == null || !mob.map.equals(conn.p.map)) {
                Message m2 = new Message(17);
                m2.writer().writeShort(-1);
                m2.writer().writeShort(idmob);
                conn.addmsg(m2);
                m2.cleanup();
                Service.send_notice_box(conn, "Không tìm thấy");
                return;
            }
            if (!(mob.map.equals(conn.p.map) && Math.abs(mob.x - conn.p.x) < 150 && Math.abs(mob.y - conn.p.y) < 150)) {
                Service.send_notice_box(conn, "Khoảng cách quá xa.\nNếu thực sự ở gần hãy thử load lại map.");
                return;
            }
            if (mob.Owner != null) {
                Service.send_notice_box(conn, "Đã có người khác hái quả.");
                return;
            }
            if (conn.p.item.get_bag_able() < 1) {
                Service.send_notice_nobox_white(conn, "Hành trang đầy.");
                return;
            }
            if (conn.p.item.total_item_by_id(4, 252) < 1) {
                Service.send_notice_nobox_white(conn, "Hãy mua giỏ hái quả để chứa.");
                return;
            }
            conn.p.item.remove(4, 252, 1);
            mob.setOwner(conn.p);
            short id = (short) Util.random(254, 259);
            conn.p.item.add_item_bag47(id, (short) 1, (byte) 4);
            conn.p.item.char_inventory(4);
            Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", new short[]{id}, new int[]{1}, new short[]{4});
            //Service.send_notice_box(conn, "Nhận quả: "+mob.nameOwner);
        }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void Menu_Mobnoel(Session conn, int idmob, byte idmenu, byte index) throws IOException {
        try{
            if (idmenu == 8) {
                if (index != 0) {
                    return;
                }
                if (conn.p.level < 40) {
                    Service.send_notice_box(conn, "Cần lên level 40 để có thể chơi sự kiện.");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, 183) <= 0){
                    Service.send_notice_box(conn,"Bạn không có bánh quy trong hành trang(mua tại lisa hoặc đổi ở npc Ông già noel)");
                    return;
                }
                MobNoel mob = Event_8.getMob(idmob);
                if (mob == null || !mob.map.equals(conn.p.map)) {
                    Message m2 = new Message(17);
                    m2.writer().writeShort(-1);
                    m2.writer().writeShort(idmob);
                    conn.addmsg(m2);
                    m2.cleanup();
                    Service.send_notice_box(conn, "Không tìm thấy");
                    return;
                }
                if (!(mob.map.equals(conn.p.map) && Math.abs(mob.x - conn.p.x) < 150 && Math.abs(mob.y - conn.p.y) < 150)) {
                    Service.send_notice_box(conn, "Khoảng cách quá xa.\nNếu thực sự ở gần hãy thử load lại map.");
                    return;
                }
                if (mob.Owner != null) {
                    Service.send_notice_box(conn, "Đã có người khác nhận quà.");
                    return;
                }
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy.");
                    return;
                }
                mob.setOwner(conn.p);
                int ran = Util.random(0,100);
                List<box_item_template> ids = new ArrayList<>();
                if (ran > 90){
                    short id = 273;
                    short quant = (short) Util.random(1,3);
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else if (ran > 80 && ran <= 90){
                    short id = 274;
                    short quant = (short) Util.random(1,3);
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else if (ran == 77){
                    short id = 353;
                    short quant = (short) Util.random(1,3);
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else if (ran > 40 && ran <= 60){
                    short id = 354;
                    short quant = (short) Util.random(1,3);
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else if (ran > 20 && ran <= 30){
                    short id = 355;
                    short quant = (short) Util.random(1,3);
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else if (ran > 5 && ran <= 20){
                    short id = 356;
                    short quant = (short) Util.random(1,3);
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else if (ran == 2){
                    short id = 113;
                    short quant = 1;
                    ids.add(new box_item_template(id, quant, (byte) 4));
                    conn.p.item.add_item_bag47(id, quant, (byte) 4);
                }else {
                    Service.send_notice_box(conn,"Bạn đen vl kiếm ô già noel khác đi");
                }
                conn.p.item.remove(4, 183, 1);
                Event_8.add_doiqua(conn.p.name, 1);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
            }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void Menu_Krypton(Session conn, byte idmenu, byte index) throws IOException {
        if (idmenu == 0)//nâng mề dùng đá Krypton
        {
            GameSrc.UpgradeMedal(conn, index);
        } else if (idmenu == 1) {
            GameSrc.UpgradeItemStar(conn, index);
        }
        conn.p.id_Upgrade_Medal_Star = -1;
    }

    private static void Menu_Khac(Session conn, byte idmenu, byte index) throws IOException {
        try{
        if (idmenu == 0) {
            switch (index) {
                case 0: {
                    if (conn.p.item.total_item_by_id(4, 52) > 0) {
                        MoLy.show_table_to_choose_item(conn.p);
                    } else {
                        Service.send_notice_box(conn, "Không đủ vé mở trong hành trang");
                    }
                    break;
                }
                case 1: {
                    String[] menu = new String[]{"Vòng xoay Vàng", "Vòng xoay ngọc", "Lịch sử"};
                    MenuController.send_menu_select(conn, -34, menu);
                    break;
                }
                case 2: {
                    Service.send_notice_box(conn, "Bạn đang có " + conn.p.hieuchien + " Điểm Pk.");
                    break;
                }
                case 3: {
                    if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593 || conn.p.item.wear[11].id == 3596) && conn.ac_admin < 66) {
                        Service.send_notice_box(conn, "Bạn không thể về làng nhanh khi đang mặc loại giáp liên quan đến chức năng buôn");
                        return;
                    }
                    Vgo vgo = new Vgo();
                    vgo.id_map_go = 1;
                    vgo.x_new = 432;
                    vgo.y_new = 354;
                    conn.p.change_map(conn.p, vgo);
                    break;
                }
                case 4: {
                    conn.p.isDropMaterialMedal = !conn.p.isDropMaterialMedal;
                    Service.send_notice_box(conn, "Rơi nguyên liệu mề đay đã " + (conn.p.isDropMaterialMedal ? "bật" : "tắt"));
                    break;
                }
                case 5: {
                    break;
                }
                case 6: {
                    conn.p.isShowMobEvents = !conn.p.isShowMobEvents;
                    Service.send_notice_box(conn, "Đã " + (conn.p.isShowMobEvents ? "bật" : "tắt") + " hiển thị cây sự kiện");
                    break;

                }
            }
        }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void Menu_View_LoiDai(Session conn, byte index) throws IOException {
        Service.send_notice_box(conn, "Chức năng chưa hoàn thiện");
        //LoiDaiManager.gI().JoinMap(conn.p, index);
    }

    private static void Menu_Mrs_Oda_trong_LoiDai(Session conn, byte index) throws IOException {
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                BXH.send3(conn, 1);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Mrs_Oda(Session conn, byte index, byte idMenu) throws IOException {
        try{
        if (!conn.p.isOwner) {
            return;
        }
        if (idMenu == 0) {
            if (conn.p.point_active.length < 3) {
                int a0 = conn.p.point_active[0];
                int a1 = conn.p.point_active[1];
                conn.p.point_active = new int[]{a0, a1, 0};
            }
            switch (index) {
                case 0: {
                    if (conn.p.type_reward_king_cup != 0) {
                        Service.send_notice_box(conn, "Phải nhận quà lôi đài trước");
                        return;
                    }
                    if (KingCupManager.list_name.contains(conn.p.name)) {
                        Service.send_notice_box(conn, "Bạn đã đăng ký rồi.");
                    } else {
                        KingCupManager.register(conn.p);
                    }
                    break;
                }
                case 1: {
                    if (!KingCupManager.list_name.contains(conn.p.name)) {
                        Service.send_notice_box(conn, "Bạn không thể vào khi chưa đăng ký tham gia lôi đài");
                        return;
                    }
                    conn.p.goMapTapKet();
                    break;
                }
                case 2: {
                    if (KingCup.kingCup != null && KingCup.kingCups != null) {
                        String[] arrKingCup = new String[KingCup.kingCups.size()];
                        for (int i = 0; i < KingCup.kingCups.size(); i++) {
                            KingCup ld = KingCup.kingCups.get(i);
                            arrKingCup[i] = ld.name1 + "(" + ld.players_attack.get(0).level + ") vs " + ld.name2 + "(" + ld.players_attack.get(1).level + ")";
                        }
                        send_menu_select(conn, -81, arrKingCup, (byte) 1);
                        break;
                    } else {
                        Service.send_notice_box(conn, "Chưa tới giờ thi đấu lôi đài");
                    }
                    break;
                }
                case 3: {
                    if (!KingCupManager.list_name.contains(conn.p.name)) {
                        Service.send_notice_box(conn, "Bạn chưa đăng ký tham gia lôi đài");
                        return;
                    }
                    Service.send_notice_box(conn, "Điểm lôi đài : " + conn.p.point_king_cup);
                    break;
                }
                case 4:
                    KingCupManager.rewardKingCup(conn.p);
                    break;
                case 5: {
                    break;
                }
                case 6: {
                    conn.p.list_thao_kham_ngoc.clear();
                    for (int i = 0; i < conn.p.item.wear.length; i++) {
                        Item3 it = conn.p.item.wear[i];
                        if (it != null) {
                            short[] b = conn.p.item.check_kham_ngoc(it);
                            boolean check = false;
                            if ((b[0] != -2 && b[0] != -1) || (b[1] != -2 && b[1] != -1) || (b[2] != -2 && b[2] != -1)) {
                                check = true;
                            }
                            if (check) {
                                conn.p.list_thao_kham_ngoc.add(it);
                            }
                        }
                    }
                    String[] list_show = new String[]{"Trống"};
                    if (conn.p.list_thao_kham_ngoc.size() > 0) {
                        list_show = new String[conn.p.list_thao_kham_ngoc.size()];
                        for (int i = 0; i < list_show.length; i++) {
                            list_show[i] = conn.p.list_thao_kham_ngoc.get(i).name;
                        }
                    }
                    MenuController.send_menu_select(conn, 117, list_show);
                    break;
                }
                case 7: {
                    if (conn.p.squire != null) {
                        conn.p.squire.switchToSquire(conn.p);
                    } else {
                        Service.send_box_input_yesno(conn, -127, "Bạn muốn nhận đệ tử với giá 100_000 coin?");
                    }
                    break;
                }
                case 8: {
                    if (conn.p.squire != null) {
                        Service.send_box_input_yesno(conn, -124, "Hủy đệ tử sẽ mất hết trang bị đang mặc. Bạn muốn hủy?");
                    } else {
                        Service.send_notice_box(conn, "Chưa có đệ tử");
                    }
                    break;
                }
//                case 9: {
//                    if (conn.p.level < 100) {
//                        Service.send_notice_box(conn, "Bạn phải đạt từ cấp độ 100 mới có thể thực hiện chức năng này");
//                        return;
//                    }
//                    conn.p.langPhuSuong();
//                    break;
//                }

                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        } else if (idMenu == 1) {
            viewKingCup(conn, index);
        }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewKingCup(Session conn, byte index) throws IOException {
        Vgo vgo = new Vgo();
        vgo.id_map_go = 102;
        vgo.x_new = 365;
        vgo.y_new = 395;
        KingCup.goToLD(conn.p, vgo, index);
    }

    private static void Menu_Pet_di_buon(Session conn, byte index) throws IOException {
        try{
        if (!conn.p.isOwner) {
            return;
        }
        switch (index) {
            case 0: {
                String notice = null;
                if (conn.p.pet_di_buon != null && conn.p.pet_di_buon.item.size() > 0) {
                    notice = "%s " + ItemTemplate3.item.get(3590).getName() + "\n";
                    notice += "%s " + ItemTemplate3.item.get(3591).getName() + "\n";
                    notice += "%s " + ItemTemplate3.item.get(3592).getName() + "\n";
                    int n1 = 0, n2 = 0, n3 = 0;
                    for (int i = 0; i < conn.p.pet_di_buon.item.size(); i++) {
                        if (conn.p.pet_di_buon.item.get(i) == 3590) {
                            n1++;
                        } else if (conn.p.pet_di_buon.item.get(i) == 3591) {
                            n2++;
                        } else {
                            n3++;
                        }
                    }
                    notice = String.format(notice, n1, n2, n3);
                } else {
                    notice = "Trống";
                }
                Service.send_notice_box(conn, notice);
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                if (conn.p.get_ngoc() > 5) {
                    conn.p.pet_di_buon.update_hp(conn.p, 100);
                } else {
                    Service.send_notice_box(conn, "Không đủ 5 ngọc");
                }
                break;
            }
            case 3: {
                if (conn.p.get_ngoc() > 5) {
                    conn.p.pet_di_buon.update_speed(conn.p);
                } else {
                    Service.send_notice_box(conn, "Không đủ 5 ngọc");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void Menu_Mr_Frank(Session conn, byte index) throws IOException {
        try{
        if (!conn.p.isOwner) {
            return;
        }
        if (conn.p.map.map_id != 17) {
            Service.send_notice_box(conn, "Nhầm rồi e ơi!");
            return;
        }
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 38);
                break;
            }
            case 1: {
                if (conn.p.pet_di_buon != null && Math.abs(conn.p.pet_di_buon.x - conn.p.x) < 75
                        && Math.abs(conn.p.pet_di_buon.y - conn.p.y) < 75 && conn.p.item.wear[11] != null
                        && conn.p.item.wear[11].id == 3593) {
                    //
                    int vang_recei = 0;
                    for (int i = 0; i < conn.p.pet_di_buon.item.size(); i++) {
                        if (conn.p.item.total_item_by_id(4, 147) >0) {
                            if (null != conn.p.pet_di_buon.item.get(i)) switch (conn.p.pet_di_buon.item.get(i)) {
                                case 3590 -> vang_recei += 400_000;
                                case 3591 -> vang_recei += 800_000;
                                case 3592 -> vang_recei += 1_600_000;
                                default -> {
                                }
                            }
                        }else {
                            if (null != conn.p.pet_di_buon.item.get(i)) switch (conn.p.pet_di_buon.item.get(i)) {
                                case 3590 -> vang_recei += 200_000;
                                case 3591 -> vang_recei += 400_000;
                                case 3592 -> vang_recei += 800_000;
                                default -> {
                                }
                            }
                        }
                    }
                    if (vang_recei > 0) {
                        conn.p.update_vang(vang_recei);
                        Log.gI().add_log(conn.p.name, "Nhận " + vang_recei + " đi cướp");
                        conn.p.item.char_inventory(5);
                        conn.p.dicuop += (vang_recei/ 1000);
                        //
                        Message mout = new Message(8);
                        mout.writer().writeShort(conn.p.pet_di_buon.index);
                        for (int i = 0; i < conn.p.map.players.size(); i++) {
                            Player p0 = conn.p.map.players.get(i);
                            if (p0 != null) {
                                p0.conn.addmsg(mout);
                            }
                        }
                        mout.cleanup();
                        //
                        Pet_di_buon_manager.remove(conn.p.pet_di_buon.name);
                        conn.p.pet_di_buon = null;
                        Service.send_notice_box(conn, "Nhận được " + vang_recei + " vàng!");
                    } else {
                        Service.send_notice_box(conn, "Chưa cướp được gì cả, thật kém cỏi!");
                    }
                } else {
                    Service.send_notice_box(conn, "Ta không thấy con vật đi buôn của ngươi");
                }
                break;
            }
            case 2: {
                Item3 itbag = new Item3();
                itbag.id = 3593;
                itbag.clazz = ItemTemplate3.item.get(3593).getClazz();
                itbag.type = ItemTemplate3.item.get(3593).getType();
                itbag.level = ItemTemplate3.item.get(3593).getLevel();
                itbag.icon = ItemTemplate3.item.get(3593).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(3593).getOp());
                itbag.color = 5;
                itbag.part = ItemTemplate3.item.get(3593).getPart();
                itbag.tier = 0;
                itbag.islock = true;
                itbag.time_use = 0;
                // thao do
                if (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3593 && conn.p.item.wear[11].id != 3599
                        && conn.p.item.wear[11].id != 3596) {
                    Item3 buffer = conn.p.item.wear[11];
                    conn.p.item.wear[11] = null;
                    conn.p.item.add_item_bag3(buffer);
                }
                itbag.name = ItemTemplate3.item.get(3593).getName() + " [Khóa]";
                itbag.UpdateName();
                conn.p.item.wear[11] = itbag;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.fashion = Part_fashion.get_part(conn.p);
                conn.p.change_map_di_buon(conn.p);
                conn.p.typepk = 12;
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
        }catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void Menu_Graham(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        if (conn.p.map.map_id != 8) {
            return;
        }
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 32);
                break;
            }
            case 1: {
                if (conn.p.isTrader()) {
                    if (conn.p.pet_di_buon != null && Math.abs(conn.p.pet_di_buon.x - conn.p.x) < 75
                            && Math.abs(conn.p.pet_di_buon.y - conn.p.y) < 75 && conn.p.item.wear[11] != null
                            && conn.p.item.wear[11].id == 3599) {
                        //
                        int vang_recei = 0;
                        for (int i = 0; i < conn.p.pet_di_buon.item.size(); i++) {
                            if (conn.p.item.total_item_by_id(4, 147) >0) {
                                if (null != conn.p.pet_di_buon.item.get(i)) switch (conn.p.pet_di_buon.item.get(i)) {
                                    case 3590:
                                        vang_recei += 400_000;
                                        break;
                                    case 3591:
                                        vang_recei += 800_000;
                                        break;
                                    case 3592:
                                        vang_recei += 1_600_000;
                                        break;
                                    default:
                                        break;
                                }
                            }else {
                                if (null != conn.p.pet_di_buon.item.get(i)) switch (conn.p.pet_di_buon.item.get(i)) {
                                    case 3590:
                                        vang_recei += 200_000;
                                        break;
                                    case 3591:
                                        vang_recei += 400_000;
                                        break;
                                    case 3592:
                                        vang_recei += 800_000;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        if (vang_recei > 0) {
                            conn.p.update_vang(vang_recei);
                            Log.gI().add_log(conn.p.name, "Nhận " + vang_recei + " đi buôn");
                            conn.p.item.char_inventory(5);
                            //
                            Message mout = new Message(8);
                            mout.writer().writeShort(conn.p.pet_di_buon.index);
                            for (int i = 0; i < conn.p.map.players.size(); i++) {
                                Player p0 = conn.p.map.players.get(i);
                                if (p0 != null) {
                                    p0.conn.addmsg(mout);
                                }
                            }
                            mout.cleanup();
                            //
                            Pet_di_buon_manager.remove(conn.p.pet_di_buon.name);
                            conn.p.pet_di_buon = null;
                            conn.p.dibuon += (vang_recei / 1000);
                            Service.send_notice_box(conn, "Nhận được " + vang_recei + " vàng!");
                        } else {
                            Service.send_notice_box(conn, "Ngươi chưa có gì mà hay bị cướp mất hết hàng rồi!");
                        }
                    } else {
                        Service.send_notice_box(conn, "Ta không thấy con vật đi buôn của ngươi");
                    }
                } else {
                    Service.send_notice_box(conn, "Ta chỉ tiếp các thương nhân");
                }
                break;
            }
            case 2: {
                Item3 itbag = new Item3();
                itbag.id = 3599;
                itbag.clazz = ItemTemplate3.item.get(3599).getClazz();
                itbag.type = ItemTemplate3.item.get(3599).getType();
                itbag.level = ItemTemplate3.item.get(3599).getLevel();
                itbag.icon = ItemTemplate3.item.get(3599).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(3599).getOp());
                itbag.color = 5;
                itbag.part = ItemTemplate3.item.get(3599).getPart();
                itbag.tier = 0;
                itbag.islock = true;
                itbag.time_use = 0;
                // thao do
                if (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3593 && conn.p.item.wear[11].id != 3599
                        && conn.p.item.wear[11].id != 3596) {
                    Item3 buffer = conn.p.item.wear[11];
                    conn.p.item.wear[11] = null;
                    conn.p.item.add_item_bag3(buffer);
                }
                itbag.name = ItemTemplate3.item.get(3599).getName() + " [Khóa]";
                itbag.UpdateName();
                conn.p.item.wear[11] = itbag;
                conn.p.typepk = 13;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.fashion = Part_fashion.get_part(conn.p);
                conn.p.change_map_di_buon(conn.p);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Mr_Dylan(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        if (conn.p.map.map_id != 52) {
            return;
        }
        if (!conn.p.isTrader()) {
            Service.send_notice_box(conn, "Không phải là thương nhân đừng nói chuyện với ta.");
            return;
        }
        if (conn.p.pet_di_buon != null && Math.abs(conn.p.pet_di_buon.x - conn.p.x) < 75
                && Math.abs(conn.p.pet_di_buon.y - conn.p.y) < 75) {
            switch (index) {
                case 0: {
                    Service.send_box_UI(conn, 31);
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        } else {
            Service.send_notice_box(conn, "Ta không thấy con vật đi buôn của ngươi");
        }
    }

    private static void Menu_NauKeo(Session conn, byte index) throws IOException {
        if (Manager.gI().event == 1) {
            switch (index) {
                case 0: {
                    // Service.send_box_input_text(conn, 11, "Nhập số lượng", new String[] {"Số lượng :"});
                    if (conn.p.get_ngoc() < 10) {
                        Service.send_notice_box(conn, "Không đủ 10 ngọc");
                        return;
                    }
                    if (Event_1.naukeo.time <= 30) {
                        Service.send_notice_box(conn, "Không thể tăng tốc");
                        return;
                    }
                    conn.p.update_ngoc(-10);
                    conn.p.item.char_inventory(5);
                    Event_1.naukeo.update(1);
                    Service.send_notice_box(conn, "Tăng tốc thành công");
                    break;
                }
                case 1: {
                    Service.send_notice_box(conn, "Nguyên liệu cần để nấu kẹo như sau: Đường, Sữa, Bơ, Vani\r\n"
                            + "- Mỗi ngày server cho nấu kẹo 1 lần vào lúc 17h , thời gian nấu là 2 tiếng.\r\n"
                            + "- Thời gian đăng ký là từ 19h ngày hôm trước đến 16h30 ngày hôm sau. Phí đăng ký là 5 ngọc\r\n"
                            + "- Một lần tăng tốc mất 10 ngọc và sẽ giảm được 2 phút nấu\r\n"
                            + "- Số kẹo tối đa nhận được là 20 kẹo.Tuy nhiên nếu các hiệp sĩ góp càng nhiều thì càng có lợi vì 10 người chơi góp nhiều nguyên liệu nhất sẽ được cộng thêm 20 cái\r\n"
                            + "+ Số kẹo nhận được sẽ tính theo công thức 1 Kẹo = 1 Đường + 1 Sữa + 1 Bơ+ 1 Vani");
                    break;
                }
                case 2: {
                    Service.send_notice_box(conn,
                            "Thông tin:\nĐã góp : " + Event_1.get_keo_now(conn.p.name) + "\nThời gian nấu còn lại : "
                            + ((Event_1.naukeo.time == 0) ? "Không trong thời gian nấu"
                                    : ("Còn lại " + Event_1.naukeo.time + "p")));
                    break;
                }
                case 3: {
                    send_menu_select(conn, 120, Event_1.get_top_naukeo());
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_Event(Session conn, byte index) throws IOException {
        if (Manager.gI().event == 1) {
            switch (index) {
                case 0: {
                    Service.send_box_input_text(conn, 10, "Nhập số lượng", new String[]{"Số lượng :"});
                    break;
                }
                case 1: {
                    Service.send_notice_box(conn,
                            "Để đổi thành Hộp đồ chơi hoàn chỉnh theo công thức: 20.000 vàng + 50 Bức tượng rồng + 50 Kiếm đồ chơi + 50 Đôi giày nhỏ xíu + 50 Trang phục tí hon + 50 Mũ lính chì."
                            + "\nĐể đổi thành Túi kẹo hoàn chỉnh theo công thức: 50.000 vàng + 5 Kẹo.");
                    break;
                }
                case 2: {
                    if (!Event_1.check_time_can_register()) {
                        Service.send_notice_box(conn, "Không trong thời gian đăng ký!");
                        return;
                    }
                    if (conn.p.get_ngoc() < 5) {
                        Service.send_notice_box(conn, "Không đủ 5 ngọc");
                        return;
                    }
                    if (Event_1.check(conn.p.name)) {
                        Service.send_notice_box(conn, "Đã đăng ký rồi, quên à!");
                        return;
                    }
                    conn.p.update_ngoc(-5);
                    conn.p.item.char_inventory(5);
                    Event_1.add_material(conn.p.name, 0);
                    Service.send_notice_box(conn, "Đăng ký thành công, có thể góp nguyên liệu rồi");
                    break;
                }
                case 3: {
                    if (!Event_1.check_time_can_register()) {
                        Service.send_notice_box(conn, "Không trong thời gian đăng ký!");
                        return;
                    }
                    if (Event_1.check(conn.p.name)) {
                        Service.send_box_input_text(conn, 11, "Nhập số lượng", new String[]{"Số lượng :"});
                    } else {
                        Service.send_notice_box(conn, "Chưa đăng ký nấu kẹo, hãy đăng ký!");
                    }
                    break;
                }
                case 4: {
                    int quant = Event_1.get_keo(conn.p.name);
                    if (quant > 0) {
                        quant = (quant > 20) ? 20 : quant;
                        if (Event_1.list_bxh_naukeo_name.contains(conn.p.name)) {
                            quant += 20;
                        }
                        quant *= 3;
                        Item47 it = new Item47();
                        it.category = 4;
                        it.id = 162;
                        it.quantity = (short) quant;
                        conn.p.item.add_item_bag47(4, it);
                        conn.p.item.char_inventory(4);
                        Service.send_notice_box(conn, "Nhận được " + quant + " kẹo");
                    } else {
                        Service.send_notice_box(conn, "Đã nhận rồi hoặc chưa tham gia!");
                    }
                    break;
                }
                case 5: {
                    Service.send_box_input_text(conn, 12, "Nhập số lượng", new String[]{"Số lượng :"});
                    break;
                }
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13: {
                    if (conn.p.item.get_bag_able() < 1) {
                        Service.send_notice_box(conn, "Hành trang không đủ chỗ trống!");
                        return;
                    }
                    short[] id_receiv = new short[]{4626, 4761, 3610, 4636, 4709, 4710, 281, 3616};
                    short[] tuikeo_required = new short[]{120, 120, 60, 60, 30, 30, 15, 60};
                    short[] hopdochoi_required = new short[]{120, 120, 60, 60, 30, 30, 15, 60};
                    int[] ngoc_required = new int[]{360, 330, 60, 60, 60, 60, 15, 300};
                    if (tuikeo_required[index - 6] > conn.p.item.total_item_by_id(4, 157)) {
                        Service.send_notice_box(conn, "Không đủ " + tuikeo_required[index - 6] + " túi kẹo!");
                        return;
                    }
                    if (hopdochoi_required[index - 6] > conn.p.item.total_item_by_id(4, 158)) {
                        Service.send_notice_box(conn, "Không đủ " + hopdochoi_required[index - 6] + " hộp đồ chơi!");
                        return;
                    }
                    if (ngoc_required[index - 6] > conn.p.get_ngoc()) {
                        Service.send_notice_box(conn, "Không đủ " + ngoc_required[index - 6] + " ngọc!");
                        return;
                    }
                    if (index != 12) {
                        Item3 itbag = new Item3();
                        ItemTemplate3 it_temp = ItemTemplate3.item.get(id_receiv[index - 6]);
                        itbag.id = it_temp.getId();
                        itbag.name = it_temp.getName();
                        itbag.clazz = it_temp.getClazz();
                        itbag.type = it_temp.getType();
                        itbag.level = 10;
                        itbag.icon = it_temp.getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(it_temp.getOp());
                        itbag.color = it_temp.getColor();
                        itbag.part = it_temp.getPart();
                        itbag.tier = 0;
                        itbag.islock = false;
                        itbag.time_use = 0;
                        conn.p.item.add_item_bag3(itbag);
                        Service.send_notice_box(conn, "Nhận được " + itbag.name + ".");
                    } else {
                        Item47 itbag = new Item47();
                        itbag.id = id_receiv[index - 6];
                        itbag.quantity = (short) 20;
                        itbag.category = 4;
                        conn.p.item.add_item_bag47(4, itbag);
                        Service.send_notice_box(conn, "Nhận được 20 xe trượt tuyết.");
                    }
                    conn.p.item.remove(4, 157, tuikeo_required[index - 6]);
                    conn.p.item.remove(4, 158, hopdochoi_required[index - 6]);
                    conn.p.update_ngoc(-ngoc_required[index - 6]);
                    conn.p.item.char_inventory(4);
                    conn.p.item.char_inventory(3);
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Đang được chuẩn bị");
                    break;
                }
            }
        }
    }

    //    private static void Menu_diempk(Session conn, byte index) throws IOException {
//        switch (index) {
//            case 0: {
//                Service.send_notice_box(conn, "Bạn đang có " + conn.p.hieuchien + " Điểm Pk.");
//                break;
//            }
//            default: {
//                Service.send_notice_box(conn, "Chưa có chức năng");
//                break;
//            }
//        }
//    }
    private static void Menu_MissAnwen(Session conn, byte index) throws IOException {
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                BXH.send3(conn, 1);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Thongtincanhan(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_notice_box(conn, "Thông tin:\n Hiệp Sĩ Online \n Địa Chỉ Ip:" + conn.ip + "\nTài Khoản:" + conn.user + "\nMật Khẩu Là:" + conn.pass + "\n Số Coin Còn Lại \n:" + conn.p.checkcoin() + "\n(Ghi Chú 0 = Đã Kích hoạt, 1 = Chưa Kích hoạt) \n :" + conn.status);
                break;

            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_top(Session conn, byte index, byte idmenu) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        if (idmenu == 0) {
            switch (index) {
            case 0: {
                if (conn.p.chucphuc == 1) {
                    conn.p.chucphuc = 0;
                    int vang_ = Util.random(1_000, 20_000);
                    int coin_ = Util.random(1000, 10000);
                    conn.p.update_vang(vang_);
                    conn.p.update_coin(coin_);
                    His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                    hisc.coin_change = coin_;
                    hisc.coin_last = conn.p.checkcoin();
                    hisc.Logger = "(NHẬN) từ Like";
                    hisc.Flus();
                    //Log.gI().add_log(conn.p.name, "nhận "+coin_+" coin từ điểm danh");
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn,
                            "Cảm ơn bạn đã yêu thích cho tôi, để tỏ lòng biết ơn tôi tặng bạn: " + coin_ + " coin, " + vang_ + " Vàng.");
                } else {
                    Service.send_notice_box(conn, "Hôm nay bạn đã yêu thích tôi  rồi, tôi không có nhiều tiền để phát quà vậy đâu, bạn đi nạp tiền đi!");
                }
                break;
            }
//                case 0: {
////                Service.send_notice_box(conn, "Giftcode : \n"+
////                        "- tanthu \n"+
////                        "- code400tv \n"+
////                        "--- Nếu kẹt map thì hãy chát lệnh -- velang -- xin cảm ơn !");
//                    Service.send_box_UI(conn, 37); // shop coin
//                    break;
//                }

                case 1: {
                    if (conn.p.level < 50) {
                        Service.send_notice_box(conn, "Yêu cầu level trên 50");
                        return;
                    }
                    send_menu_select(conn, 114, new String[]{"Cầu hôn", "Ly hôn","Nâng cấp nhẫn", "Hướng dẫn"});
                    break;
                }
                case 2: {
                    send_menu_select(conn, 115, new String[]{"Chest Thông Tin Tài Khoản", "Thông Tin Bản Thân"});
                    break;
                }
//            case 4: {
//                if (conn.p.hieuchien < 1000) {
//                    Service.send_notice_box(conn, "Chưa đủ 1000 điểm pk");
//                    return;
//                }
//                conn.p.hieuchien -= 1000;
//                int random = Util.random(100);
//                if (random < 10) {
//                    short id_ = 4718;
//                    Item3 itbag = new Item3();
//                    itbag.id = id_;
//                    itbag.name = ItemTemplate3.item.get(id_).getName();
//                    itbag.clazz = ItemTemplate3.item.get(id_).getClazz();
//                    itbag.type = ItemTemplate3.item.get(id_).getType();
//                    itbag.level = ItemTemplate3.item.get(id_).getLevel();
//                    itbag.icon = ItemTemplate3.item.get(id_).getIcon();
//                    itbag.op = ItemTemplate3.item.get(id_).getOp();
//                    itbag.color = ItemTemplate3.item.get(id_).getColor();
//                    itbag.part = ItemTemplate3.item.get(id_).getPart();
//                    itbag.tier = 0;
//                    itbag.islock = false;
//                    itbag.time_use = 0;
//                    conn.p.item.add_item_bag3(itbag);
//                    conn.p.item.char_inventory(3);
//                    Service.send_notice_box(conn, "Nhận được " + itbag.name);
//                    return;
//                } else if (random > 10 && random < 20) {
//                    short id_ = 4719;
//                    Item3 itbag = new Item3();
//                    itbag.id = id_;
//                    itbag.name = ItemTemplate3.item.get(id_).getName();
//                    itbag.clazz = ItemTemplate3.item.get(id_).getClazz();
//                    itbag.type = ItemTemplate3.item.get(id_).getType();
//                    itbag.level = ItemTemplate3.item.get(id_).getLevel();
//                    itbag.icon = ItemTemplate3.item.get(id_).getIcon();
//                    itbag.op = ItemTemplate3.item.get(id_).getOp();
//                    itbag.color = ItemTemplate3.item.get(id_).getColor();
//                    itbag.part = ItemTemplate3.item.get(id_).getPart();
//                    itbag.tier = 0;
//                    itbag.islock = false;
//                    itbag.time_use = 0;
//                    conn.p.item.add_item_bag3(itbag);
//                    conn.p.item.char_inventory(3);
//                    Service.send_notice_box(conn, "Nhận được " + itbag.name);
//                    return;
//                } else if (random > 20 && random < 30) {
//                    short id_ = 4709;
//                    Item3 itbag = new Item3();
//                    itbag.id = id_;
//                    itbag.name = ItemTemplate3.item.get(id_).getName();
//                    itbag.clazz = ItemTemplate3.item.get(id_).getClazz();
//                    itbag.type = ItemTemplate3.item.get(id_).getType();
//                    itbag.level = ItemTemplate3.item.get(id_).getLevel();
//                    itbag.icon = ItemTemplate3.item.get(id_).getIcon();
//                    itbag.op = ItemTemplate3.item.get(id_).getOp();
//                    itbag.color = ItemTemplate3.item.get(id_).getColor();
//                    itbag.part = ItemTemplate3.item.get(id_).getPart();
//                    itbag.tier = 0;
//                    itbag.islock = false;
//                    itbag.time_use = 0;
//                    conn.p.item.add_item_bag3(itbag);
//                    conn.p.item.char_inventory(3);
//                    Service.send_notice_box(conn, "Nhận được " + itbag.name);
//                    return;
//                } else if (random > 30) {
//                    int vang = Util.random(5000, 100000);
//                    int ngoc = Util.random(200, 400);
//                    conn.p.update_vang(vang);
//                    conn.p.update_ngoc(ngoc);
//                    conn.p.item.char_inventory(5);
//                    Service.send_notice_box(conn, "Nhận được " + vang + "Vàng. " + ngoc + "Ngọc");
//                }
//                break;
//            }
//            case 2: {
//                send_menu_select(conn, 120, ev_he.Event_Ruong.get_top());
////                Service.send_notice_box(conn, "Chức năng này đang fix lỗi");
////                  if (conn.status != 0) {
////                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
////                    return;
////                }
////                   if (conn.p.get_ngoc() < 1000) {
////                                Service.send_notice_box(conn, "Không đủ 1000 ngọc");
////                                return;
////                    }
////                    conn.p.update_ngoc(-100);
////
////                Vgo vgo = null;
////                vgo = new Vgo();
////                vgo.id_map_go = 103;
////                vgo.x_new = 282;
////                vgo.y_new = 186;
////                conn.p.change_map(conn.p, vgo);
//
//                break;
//
                case 3: {
                    if (conn.status != 0) {
                        Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                        return;
                    }
                    if (conn.p.get_ngoc() < 100) {
                        Service.send_notice_box(conn, "Không đủ 100 ngọc");
                        return;
                    }
                    conn.p.update_ngoc(-100);

                    send_menu_select(conn, -49, new String[]{"Khu Boss Even 0x", "Khu Boss Even 1x", "Khu Boss Even 2x", "Khu Boss Even 7x", "Khu Boss Even 8x", "Khu Boss Even 11x", "Khu Boss Even 13x"}, (byte) 3);
                    break;
                }
                case 4: {
                    if (conn.p.quatanthu > 1 && conn.ac_admin < 111){
                        Service.send_notice_box(conn,"Bạn đã nhận quà tân thủ rồi");
                        return;
                    }
                    if (conn.p.quatanthu == 0) {
                        String text = "quatanthu";
                        try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `giftcode2` WHERE `name_player` = '" + text + "';")) {
                            byte empty_box = (byte) 0;
                            if (!rs.next()) {
                                Service.send_notice_box(conn, "Không Thấy Quà");
                                return;
                            }
                            String mess = rs.getString("logger");
                            empty_box = rs.getByte("empty_box");
                            byte date = rs.getByte("date");
                            if (conn.p.item.get_bag_able() >= empty_box) {
                                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                                for (int i = 0; i < jsar.size(); i++) {
                                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                    if (jsar2 == null || jsar2.size() < 1) {
                                        continue;
                                    }
                                    Item3 itbag = new Item3();
                                    short it = Short.parseShort(jsar2.get(0).toString());
                                    itbag.id = it;
                                    itbag.name = ItemTemplate3.item.get(it).getName();
                                    itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                                    itbag.type = ItemTemplate3.item.get(it).getType();
                                    itbag.level = ItemTemplate3.item.get(it).getLevel();
                                    itbag.icon = ItemTemplate3.item.get(it).getIcon();
                                    itbag.op = new ArrayList<>();
                                    itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                                    itbag.color = ItemTemplate3.item.get(it).getColor();
                                    itbag.part = ItemTemplate3.item.get(it).getPart();
                                    itbag.tier = 0;
                                    itbag.time_use = 0;
                                    itbag.islock = false;
                                    if (date > 0) {
                                        itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                                    }
                                    conn.p.item.add_item_bag3(itbag);
                                }
                                jsar.clear();
                                //
                                jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                                for (int i = 0; i < jsar.size(); i++) {
                                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                    Item47 itbag = new Item47();
                                    itbag.id = Short.parseShort(jsar2.get(0).toString());
                                    itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                                    itbag.category = 4;
                                    conn.p.item.add_item_bag47(4, itbag);
                                }
                                jsar.clear();
                                //
                                jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                                for (int i = 0; i < jsar.size(); i++) {
                                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                                    Item47 itbag = new Item47();
                                    itbag.id = Short.parseShort(jsar2.get(0).toString());
                                    itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                                    itbag.category = 7;
                                    conn.p.item.add_item_bag47(7, itbag);
                                }
                                jsar.clear();
                                conn.p.update_vang(rs.getLong("vang"));
                                conn.p.update_ngoc(rs.getLong("ngoc"));
                                conn.p.update_coin(rs.getInt("coin"));
                                conn.p.quatanthu += 2;
                                LeaveItemMap.randomMD(conn);
                                conn.p.item.char_inventory(5);
                                conn.p.item.char_inventory(3);
                                conn.p.item.char_inventory(4);
                                conn.p.item.char_inventory(7);
                                Service.send_notice_box(conn, mess);
                            } else {
                                Service.send_notice_box(conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (conn.p.quatanthu == 1 || conn.ac_admin > 111){
                        LeaveItemMap.randomMD(conn);
                        conn.p.quatanthu += 2;
                    }
                    break;
                }
//                case 5: {
//                    if (conn.p.settimeoff != 1) {
//                        conn.p.settimeoff = 1;
//                        Service.send_notice_box(conn, "ĐK thành công");
//                    }else {
//                        Service.send_notice_box(conn,"Bạn đã đk rồi");
//                    }
//                    break;
//                }
//                case 6: {
//                    conn.p.addExp(conn.p.expGained);
//                    Service.send_notice_box(conn,"Bạn nhận được " + conn.p.expGained + " EXP sau khi offline!");
//                    break;
//                }
                case 5: {
                    send_menu_select(conn, -49,
                            new String[]{"Hướng dẫn", "Nhận nhiệm vụ", "Hủy nhiệm vụ", "Trả nhiệm vụ", "Kiểm tra"}, (byte) 1);
                    break;
                }
//            case 7:{
//                Service.send_notice_box(conn, "Bạn phải là V.I.P_1,");
//                break;
//            }
//            case 8:{
//                 Service.send_notice_box(conn, "Bạn phải là V.I.P_2");
//                break;
//            }
//            case 9:{
//                 Service.send_notice_box(conn, "Bạn phải là V.I.P_3");
//                break;
//            }
//             case 7:{
//                send_menu_select(conn, 1001,
//                        new String[]{"Hướng dẫn", "Nhận nhiệm vụ", "Hủy nhiệm vụ", "Trả nhiệm vụ", "Kiểm tra"});
//                break;
//             }
//            case 7:{
//                Service.send_box_UI(conn, 48); // shop Tinh tú
//                break;
//            }
            }
        } else if (idmenu == 1) {
            switch (index) {
                case 0: {
                    String notice
                            = "Nhiệm vụ Ngày: đánh quái ngẫu nhiên theo level, tối đa ngày nhận 20 nhiệm vụ, mỗi nhiệm vụ sẽ nhận được phần thưởng kinh nghiệm, ngọc và có cơ hội nhận nguyên liệu mề đay."
                            + "\n Dễ : Vàng Ngọc + Exp" + "\n Bình Thường : Vàng Ngọc, Exp + NL mề Xanh"
                            + "\n Khó :Vàng Ngọc, Exp + NL mề Vàng" + "\n Siêu Khó : Vàng Ngọc, Exp + NL mề Tím";
                    Service.send_notice_box(conn, notice);
                    break;
                }
                case 1: {
                    if (conn.p.quest_daily[0] != -1) {
                        Service.send_notice_box(conn, "Đã nhận nhiệm vụ rồi!");
                    } else {
                        if (conn.p.quest_daily[4] > 0) {
                            send_menu_select(conn, -49, new String[]{"Cực Dễ", "Bình thường", "Khó", "Siêu Khó"}, (byte) 2);
                        } else {
                            Service.send_notice_box(conn, "Hôm nay đã hết lượt, quay lại vào ngày mai");
                        }
                    }
                    break;
                }
                case 2: {
                    DailyQuest.remove_quest(conn.p);
                    break;
                }
                case 3: {
                    DailyQuest.finish_quest(conn.p);
                    break;
                }
                case 4: {
                    Service.send_notice_box(conn, DailyQuest.info_quest(conn.p));
                    break;
                }
            }
        } else if (idmenu == 2) {
            DailyQuest.get_quest(conn.p, index);
        } else if (idmenu == 3) {
            Menu_Langphusuongboss(conn, index);
        } 
    }

    private static void Menu_TachCanh(Session conn, byte index) throws IOException {
        Item3 item = null;
        int count = 0;
        for (int i = 0; i < conn.p.item.bag3.length; i++) {
            Item3 it = conn.p.item.bag3[i];
            if (it != null && it.type == 7 && it.tier > 0) {
                if (count == index) {
                    item = it;
                    break;
                }
                count++;
            }
        }
        if (item != null) {
            conn.p.id_wing_split = index;
            int quant1 = 40;
            int quant2 = 10;
            int quant3 = 50;
            for (int i = 0; i < item.tier; i++) {
                quant1 += GameSrc.wing_upgrade_material_long_khuc_xuong[i];
                quant2 += GameSrc.wing_upgrade_material_kim_loai[i];
                quant3 += GameSrc.wing_upgrade_material_da_cuong_hoa[i];
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
            Service.send_box_input_yesno(conn, 114, "Bạn có muốn tách cánh này và nhận được: " + quant1
                    + " lông và khúc xương, " + quant2 + " kim loại, " + quant3 + " đá cường hóa?");
        }
    }

    private static void Menu_TienCanh(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                // Service.send_box_UI(conn, index);
                Service.send_msg_data(conn, 23, "create_wings");
                break;
            }
            case 1: {
                Message m2 = new Message(77);
                m2.writer().writeByte(6);
                conn.addmsg(m2);
                m2.cleanup();
                //
                m2 = new Message(77);
                m2.writer().writeByte(1);
                m2.writer().writeUTF("Nâng cấp cánh");
                conn.addmsg(m2);
                m2.cleanup();
                conn.p.is_create_wing = false;
                break;
            }
//             case 2: {
//                List<String> list = new ArrayList<>();
//                for (int i = 0; i < conn.p.item.bag3.length; i++) {
//                    Item3 it = conn.p.item.bag3[i];
//                    if (it != null && it.type == 7) {
//                        list.add(it.name + " +" + it.tier);
//                    }
//                }
//
//                String[] list_2 = new String[]{"Trống"};
//                if (list.size() > 0) {
//                    list_2 = new String[list.size()];
//                    for (int i = 0; i < list_2.length; i++) {
//                        list_2[i] = list.get(i);
//                    }
//                }
//                MenuController.send_menu_select(conn, 210, list_2);
//
//                break;
//            }
            case 2: {
                conn.p.id_wing_split = -1;
                List<String> list = new ArrayList<>();
                for (int i = 0; i < conn.p.item.bag3.length; i++) {
                    Item3 it = conn.p.item.bag3[i];
                    if (it != null && it.type == 7 && it.tier > 0) {
                        list.add((it.name + " +" + it.tier));
                    }
                }
                if (list.size() > 0) {
                    String[] list_2 = new String[list.size()];
                    for (int i = 0; i < list_2.length; i++) {
                        list_2[i] = list.get(i);
                    }
                    send_menu_select(conn, 121, list_2);
                } else {
                    Service.send_notice_box(conn, "Làm gì có cánh mà đòi tách?");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_ThayDongCanh_percent(Session conn, byte index) throws IOException {
        if (conn.p.get_ngoc() < 300) {
            Service.send_notice_box(conn, "Không đủ 300 ngọc");
            return;
        }
        conn.p.update_ngoc(-300);
        Log.gI().add_log(conn.p.name, "hết 300 ngọc");
        Item3 it_process = null;
        for (int i = 0; i < conn.p.item.bag3.length; i++) {
            Item3 it = conn.p.item.bag3[i];
            if (it != null && it.type == 7) {
                if (index == 0) {
                    it_process = it;
                    break;
                }
                index--;
            }
        }
        if (it_process != null) {
            Option[] process = new Option[2];
            for (int i = 0; i < it_process.op.size(); i++) {
                if (it_process.op.get(i).id >= 7 && it_process.op.get(i).id <= 11) {
                    if (process[0] == null) {
                        process[0] = it_process.op.get(i);
                    } else if (process[1] == null) {
                        process[1] = it_process.op.get(i);
                    } else {
                        break;
                    }
                }
            }
            if (process[0] != null) {
                process[0].id = (byte) Util.random(7, 12);
                process[0].setParam(Util.random(1500, 2500));
                // process[0].setParam(process[0].getParam(0) + Util.random(50, 100));
            }
            if (process[1] != null) {
                process[1].id = (byte) Util.random(7, 12);
                process[1].setParam(Util.random(1500, 2500));
                //  process[1].setParam(process[1].getParam(0) + Util.random(50, 100));
            }
            Service.send_notice_box(conn, "Thành công");
            conn.p.item.char_inventory(3);
        }
    }

    private static void Menu_Clan_Manager(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
            switch (index) {
                case 0: {
                    conn.p.myclan.open_box_clan(conn);
                    break;
                }
                case 1: {
                    if (conn.p.myclan.get_percent_level() >= 100) {
                        Service.send_box_input_yesno(conn, 118,
                                "Bạn có muốn nâng cấp bang lên level " + (conn.p.myclan.level + 1) + " với "
                                + (Clan.vang_upgrade[1] * conn.p.myclan.level) + " vàng và " + (conn.p.myclan.level + 1)
                                + " với " + (Clan.ngoc_upgrade[1] * conn.p.myclan.level) + " ngọc không?");
                    } else {
                        Service.send_notice_box(conn, "Chưa đủ exp để nâng cấp!");
                    }
                    break;
                }
                case 2: {
                    Service.send_box_input_yesno(conn, 116,
                            "Hãy xác nhận việc hủy bang, đừng hối hận khóc lóc xin admin khôi phục lại nhá!");
                    break;
                }
                case 3: {
                    Service.send_box_input_text(conn, 13, "Nhập tên :", new String[]{"Nhập tên :"});
                    break;
                }
                default: {

                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_Dungeon_Mode_Selection(Session conn, byte index) throws IOException {
//        if (conn.p.dungeon != null && conn.p.dungeon.getWave() == 20) {
//            if (index != 2 && conn.p.party != null && conn.p.party.get_mems().get(0).id != conn.p.id) {
//                Service.send_notice_box(conn, "Chỉ có đội trưởng mới có quyền quyết định!");
//                return;
//            }
//            conn.p.dungeon.setMode(index);
//            if (conn.p.dungeon != null) {
//                conn.p.dungeon.setWave(21);
//                conn.p.dungeon.state = 1;
//            } else {
//                Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử chọn lại!");
//            }
//        }
    }

    private static void Menu_LinhCanh(Session conn, byte index) throws IOException {
//        if (conn.p.dungeon != null && conn.p.dungeon.getWave() == 20) {
//            if (index != 2 && conn.p.party != null && conn.p.party.get_mems().get(0).id != conn.p.id) {
//                Service.send_notice_box(conn, "Chỉ có đội trưởng mới có quyền quyết định!");
//                return;
//            }
//            switch (index) {
//                case 0: {
//                    send_menu_select(conn, 123, new String[]{"Easy", "Normal", "Hard", "Nightmare", "Hell"});
//                    break;
//                }
//                case 1: {
//                    if (conn.p.dungeon != null) {
//                        conn.p.dungeon.state = 6;
//                    } else {
//                        Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử chọn lại!");
//                    }
//                    Service.send_notice_box(conn, "gà thật, éo dám đi tiếp à");
//                    break;
//                }
//                case 2: {
//                    Service.send_notice_box(conn,
//                            "Đến được đây quả là có cố gắng, hãy nói chuyện với phó chỉ huy để nhận thưởng hoàn thành phó bản, hoặc chọn tiếp tục chinh phục để có thể nhận được nhiều phần thưởng hơn");
//                    break;
//                }
//                default: {
//                    Service.send_notice_box(conn, "Chưa có chức năng");
//                    break;
//                }
//            }
//        }
    }

    private static void Menu_PhoChiHuy(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.level < 30) {
                    Service.send_notice_box(conn, "Đạt level 30 mới có thể vào phó bản");
                    return;
                }
                if (conn.p.point_active[0] < 1) {
                    if (conn.p.checkcoin() < 5000){
                        Service.send_notice_box(conn,"Không đủ coin");
                        return;
                    }
                    Service.send_box_input_yesno(conn, -117, "Bạn có chắc chắn muốn vào phó bản với giá 5000 coin");
                    //Service.send_notice_box(conn, "Đã hết lượt đi, hãy quay lại vào ngày mai");
                    return;
                }
                String notice = "Danh sách người chơi vào phó bản :\n1) " + conn.p.name + " : level " + conn.p.level;
                if (conn.p.party != null) {
                    Service.send_notice_box(conn, "Phó bản hiện tại chỉ hỗ trợ chế độ solo");
                    return;
                }
                notice += "\nĐộ khó: ??? \nHãy xác nhận." + (conn.p.point_active[0] != 10 ? " phí vào là 30 ngọc." : "");
//                conn.p.dungeon = null;
                Service.send_box_input_yesno(conn, 119, notice);
                break;
            }
            case 1: {
                Service.send_notice_box(conn,
                        "Ngã tư tử thần nâng cấp:\nSau khi vượt qua 20 ải đầu sẽ nhận phần thưởng hoàn thành phó bản, sau đó hãy nói chuyện với npc trong phó bản để quyết định độ khó, cuối cùng là tiếp tục chinh phục phó bản.\n Càng tích nhiều điểm càng được nhiều phần thưởng nhé, điểm sẽ được tính dựa trên số ải vượt qua và lượng dame gây ra.\nLưu ý mỗi ngày đi tối đa 10 lần.");
                break;
            }
            case 2: {
                synchronized (Dungeon.bxh_time_complete) {
                    String notice;
                    if (Dungeon.bxh_time_complete.size() > 0) {
                        notice = "BXH thời gian hoàn thành:\n";
                        int dem = 1;
                        for (Dungeon.BXH_Dungeon_Finished set : Dungeon.bxh_time_complete) {
                            notice += (dem++) + ". " + set.name + " : " + set.time + "s\n";
                        }
                    } else {
                        notice = "Chưa có thông tin";
                    }
                    Service.send_notice_box(conn, notice);
                }
                break;
            }
            case 3: {
                ChiemThanhManager.ClanRegister(conn.p);
                break;
            }
            case 4: {
                if (!conn.p.isOwner) {
                    return;
                }
                if (ChiemThanhManager.timeAttack < System.currentTimeMillis()) {
                    Service.send_notice_box(conn, "Chiếm thành đã kết thúc.");
                } else if (!ChiemThanhManager.joinMap(conn.p)) {
                    Service.send_notice_box(conn, "Bạn không đủ điều kiện tham gia chiếm thành.");
                }
                break;
            }
            case 5: {
                Service.send_notice_box(conn, "Thông tin:\nSố lần còn lại " + conn.p.point_active[0]
                        + ": lần\nTổng điểm hôm nay : " + conn.p.point_active[1]);
                break;
            }
            case 6: {
                if (conn.p.point_active[1] < 1) {
                    Service.send_notice_box(conn, "Hôm nay chưa làm gì cả, không làm mà đòi có ăn à con??");
                    return;
                }
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Hành trang không đủ chỗ!");
                    return;
                }
                while (conn.p.point_active[1] > 0) {
                    conn.p.point_active[1]--;
                    short id_ = Medal_Material.m_blue[Util.random(0, 10)];
                    if (conn.p.item.get_bag_able() <= 0) {
                        Service.send_notice_box(conn, "Hành trang không đủ chỗ!");
                        conn.p.item.char_inventory(7);
                        conn.p.update_vang(Util.random(10, 50));
                        return;
                    }
                    if (25 > Util.random(0, 100) && ((conn.p.item.get_bag_able() > 0))) {
                        Item47 itbag = new Item47();
                        itbag.id = id_;
                        itbag.quantity = (short) Util.random(0, 2);
                        itbag.category = 7;
                        conn.p.item.add_item_bag47(7, itbag);
                    }
                    //
                    conn.p.update_vang(Util.random(10, 50));
                }
                conn.p.item.char_inventory(7);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            case 7: {
                Item3 itbag = new Item3();
                itbag.id = 3596;
                itbag.clazz = ItemTemplate3.item.get(3596).getClazz();
                itbag.type = ItemTemplate3.item.get(3596).getType();
                itbag.level = ItemTemplate3.item.get(3596).getLevel();
                itbag.icon = ItemTemplate3.item.get(3596).getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(ItemTemplate3.item.get(3596).getOp());
                itbag.color = 5;
                itbag.part = ItemTemplate3.item.get(3596).getPart();
                itbag.tier = 0;
                itbag.islock = true;
                itbag.time_use = 0;
                // thao do
                if (conn.p.item.wear[11] != null && conn.p.item.wear[11].id != 3593 && conn.p.item.wear[11].id != 3599
                        && conn.p.item.wear[11].id != 3596) {
                    Item3 buffer = conn.p.item.wear[11];
                    conn.p.item.wear[11] = null;
                    conn.p.item.add_item_bag3(buffer);
                }
                itbag.name = ItemTemplate3.item.get(3596).getName() + " [Khóa]";
                itbag.UpdateName();
                conn.p.item.wear[11] = itbag;
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                conn.p.fashion = Part_fashion.get_part(conn.p);
                conn.p.change_map_di_buon(conn.p);
                Service.send_notice_box(conn, "Nhận thành công");
                break;
            }
            case 8: {
                Service.send_box_input_yesno(conn, -1, "Bạn có chắc chắn muốn dùng hoàn thành phó bản khẩn cấp");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Pet_Manager(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 21);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 22);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 23);
                break;
            }
            case 3: {
                if (conn.p.pet_follow != -1) {
                    for (Pet temp : conn.p.mypet) {
                        if (temp.is_follow) {
                            temp.is_follow = false;
                            Message m = new Message(44);
                            m.writer().writeByte(28);
                            m.writer().writeByte(1);
                            m.writer().writeByte(9);
                            m.writer().writeByte(9);
                            m.writer().writeUTF(temp.name);
                            m.writer().writeByte(temp.type);
                            m.writer().writeShort(conn.p.mypet.indexOf(temp)); // id
                            m.writer().writeShort(temp.level);
                            m.writer().writeShort(temp.getlevelpercent()); // exp
                            m.writer().writeByte(temp.type);
                            m.writer().writeByte(temp.icon);
                            m.writer().writeByte(temp.nframe);
                            m.writer().writeByte(temp.color);
                            m.writer().writeInt(temp.get_age());
                            m.writer().writeShort(temp.grown);
                            m.writer().writeShort(temp.maxgrown);
                            m.writer().writeShort(temp.point1);
                            m.writer().writeShort(temp.point2);
                            m.writer().writeShort(temp.point3);
                            m.writer().writeShort(temp.point4);
                            m.writer().writeShort(temp.maxpoint);
                            m.writer().writeByte(temp.op.size());
                            for (int i2 = 0; i2 < temp.op.size(); i2++) {
                                Option_pet temp2 = temp.op.get(i2);
                                m.writer().writeByte(temp2.id);
                                m.writer().writeInt(temp2.param);
                                m.writer().writeInt(temp2.maxdam);
                            }
                            conn.p.conn.addmsg(m);
                            m.cleanup();
                            break;
                        }
                    }
                    conn.p.pet_follow = -1;
                    Service.send_wear(conn.p);
                    Service.send_char_main_in4(conn.p);
                } else {
                    Service.send_notice_box(conn, "Đã đeo pet đâu mà đòi tháo??");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Mr_Edgar(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.list_enemies.size() > 0) {
                    String[] name = new String[conn.p.list_enemies.size()];
                    for (int i = 0; i < name.length; i++) {
                        name[i] = conn.p.list_enemies.get(name.length - i - 1);
                    }
                    send_menu_select(conn, 124, name);
                } else {
                    Service.send_notice_box(conn, "Danh sách chưa có ai, hãy đi cà khịa để tạo thêm!");
                }
                break;
            }
            case 1: {
                Service.send_notice_box(conn,
                        "Bị người chơi khác pk thì sẽ được lưu vào danh sách, "
                        + "mỗi lần trả thù sẽ được đưa tới nơi kẻ thù đang đứng với chi phí chỉ vỏn vẹn 2 ngọc.\n"
                        + "Sau khi được đưa tới nơi, tên kẻ thù sẽ được loại ra khỏi danh sách");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Zoro(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        if (conn.p.myclan != null) {
            if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                switch (index) {
                    case 0: {
                        send_menu_select(conn, 122,
                                new String[]{"Kho bang", "Nâng cấp bang", "Hủy bang hội", "Chuyển thủ lĩnh"});
                        break;
                    }
                    case 1: { //
                        Service.send_box_UI(conn, 29);
                        break;
                    }
                    case 2: {
                        Service.send_box_UI(conn, 30);
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            } else {
                switch (index) {
                    case 0: {
                        conn.p.myclan.open_box_clan(conn);
                        break;
                    }
                    case 1: {
                        if(conn.status != 10){
                            Service.send_notice_box(conn,"Không thể góp vàng vào bang");
                            return;
                        }
                        Service.send_box_input_text(conn, 8, "Góp vàng", new String[]{"Số lượng :"});
                        break;
                    }
                    case 2: {
                        if(conn.status != 10){
                            Service.send_notice_box(conn,"Không thể góp ngọc vào bang");
                            return;
                        }
                        Service.send_box_input_text(conn, 9, "Góp Ngọc", new String[]{"Số lượng :"});
                        break;
                    }
                    case 3: {
                        Service.send_box_input_yesno(conn, 117,
                                "Hãy xác nhận việc rời bang, có khi đi rồi quay lại éo đc đâu nhá");
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            }
        } else {
            switch (index) {
                case 0: {
                    Service.send_box_input_yesno(conn, 70, "Bạn có muốn đăng ký tạo bang với phí là 20.000 ngọc");
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_Benjamin(Session conn, byte index) throws IOException {
        if (conn.p.myclan != null) {
            if (conn.p.myclan.mems.get(0).name.equals(conn.p.name)) {
                switch (index) {
                    case 0: {
                        send_menu_select(conn, 122,
                                new String[]{"Kho bang", "Nâng cấp bang", "Hủy bang hội", "Chuyển thủ lĩnh"});
                        break;
                    }
                    case 1: { //
                        Service.send_box_UI(conn, 29);
                        break;
                    }
                    case 2: {
                        Service.send_box_UI(conn, 30);
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            } else {
                switch (index) {
                    case 0: {
                        conn.p.myclan.open_box_clan(conn);
                        break;
                    }
                    case 1: {
                        if(conn.status != 10){
                            Service.send_notice_box(conn,"Không thể góp vàng vào bang");
                            return;
                        }
                        Service.send_box_input_text(conn, 8, "Góp vàng", new String[]{"Số lượng :"});
                        break;
                    }
                    case 2: {
                        if(conn.status != 10){
                            Service.send_notice_box(conn,"Không thể góp vàng vào bang");
                            return;
                        }
                        Service.send_box_input_text(conn, 9, "Góp Ngọc", new String[]{"Số lượng :"});
                        break;
                    }
                    case 3: {
                        Service.send_box_input_yesno(conn, 117,
                                "Hãy xác nhận việc rời bang, có khi đi rồi quay lại éo đc đâu nhá");
                        break;
                    }
                    default: {
                        Service.send_notice_box(conn, "Chưa có chức năng");
                        break;
                    }
                }
            }
        } else {
            switch (index) {
                case 0: {
                    Service.send_box_input_yesno(conn, 70, "Bạn có muốn đăng ký tạo bang với phí là 20.000 ngọc");
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        }
    }

    private static void Menu_VXMM(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        switch (index) {
            case 0: {
                Manager.gI().vxmm.send_in4(conn.p);
                break;
            }
            case 1: {
                Service.send_box_input_text(conn, 3, "Vòng xoay vàng", new String[]{"Tham gia (tối thiểu 1m) :"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_taixiu(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                send_menu_select(conn, 131, new String[]{"Tài", "Xỉu", "Soi Cầu"});
                break;
            }

        }
    }

    private static void Menu_VXKC(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Manager.gI().vxkc.send_in4(conn.p);
                break;
            }
            case 1: {
                Service.send_box_input_text(conn, 17, "Vòng xoay ngọc", new String[]{"Tham gia (tối thiểu 500) :"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_CuopBien(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner) {
            return;
        }
        switch (index) {
            case 0: {
                send_menu_select(conn, 125, new String[]{"Xem thông tin", "Tham gia"});
                break;
            }
            case 1: {
                send_menu_select(conn, 131, new String[]{"Xem thông tin", "Tham gia"});
                //Service.send_notice_box(conn, "Sắp ra mắt");
                break;
            }
            case 2: {
                Service.send_notice_box(conn, "Sắp ra mắt");
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    public static void send_menu_select(Session conn, int idnpc, String[] menu) throws IOException {
        if (!conn.p.isDie) {
            if (menu != null && menu.length > 0) {
                Message m2 = new Message(-30);
                m2.writer().writeShort(idnpc);
                m2.writer().writeByte(0);
                m2.writer().writeByte(menu.length);
                for (int i = 0; i < menu.length; i++) {
                    m2.writer().writeUTF(menu[i]);
                }
                if (conn.ac_admin > 10) {
                    m2.writer().writeUTF("MENU : " + idnpc);
                } else {
                    m2.writer().writeUTF("MENU");
                }
                conn.addmsg(m2);
                m2.cleanup();
            }
        }
    }

    public static void send_menu_select(Session conn, int idnpc, String[] menu, byte idmenu) throws IOException {
        if (!conn.p.isDie) {
            if (menu != null && menu.length > 0) {
                Message m2 = new Message(-30);
                m2.writer().writeShort(idnpc);
                m2.writer().writeByte(idmenu);
                m2.writer().writeByte(menu.length);
                for (int i = 0; i < menu.length; i++) {
                    m2.writer().writeUTF(menu[i]);
                }
                if (conn.ac_admin > 10) {
                    m2.writer().writeUTF("MENU : " + idnpc);
                } else {
                    m2.writer().writeUTF("MENU");
                }
                conn.addmsg(m2);
                m2.cleanup();
            }
        }
    }

    private static void Menu_Aman(Session conn, byte index) throws IOException {
        if (!conn.p.isOwner){
            return;
        }
        switch (index) {
            case 0: {
                Message m = new Message(23);
                m.writer().writeUTF("Rương đồ");
                m.writer().writeByte(3);
                m.writer().writeShort(0);
                conn.addmsg(m);
                m.cleanup();
                break;
            }
            case 1: {
                if (conn.p.maxbag >= 126) {
                    Service.send_notice_box(conn, "Đã mở rương rồi!");
                    return;
                }
                try ( Connection connection = SQL.gI().getConnection();  Statement statement = connection.createStatement()) {
                    if (statement.executeUpdate("UPDATE `player` SET `maxbag` = 126 WHERE `id` = " + conn.p.index + ";") > 0) {
                        connection.commit();
                    }
                    Service.send_notice_box(conn, "Mở thành công 126 ô, hãy thoát game vào lại để cập nhật!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    //     Service.send_notice_box(conn, "Có lỗi xảy ra, hãy thử lại!");
                }
                break;
            }
            case 2: {
                // if (conn.p.update_coin(-25_000)) {
                // if (true) {
                // String js =
                // "[1,%s,%s,%s,%s,10,100,300,1,1,1,1,100,[[0,%s,%s],[23,%s,0],[24,%s,0],[25,%s,0],[26,%s,0]]]";
                // byte[] type = new byte[] {8, 13, 10, 11, 11, 11, 12, 12, 12, 9, 7, 7, 7, 6, 5, 5, 5, 4, 4, 4, 3,
                // 3, 3, 2,
                // 2, 2, 1, 1, 1, 0, 0, 0};
                // byte[] icon = new byte[] {26, 41, 32, 33, 34, 35, 36, 37, 38, 29, 21, 22, 23, 20, 15, 16, 17, 12,
                // 13, 14,
                // 9, 10, 11, 6, 7, 8, 0, 1, 2, 3, 4, 5};
                // int rd = Util.random(0, 32);
                // js = String.format(js, type[rd], icon[rd], 3, 0, Util.random(0, 99), Util.random(99, 999),
                // Util.random(1, 50), Util.random(1, 50), Util.random(1, 50), Util.random(1, 50));
                // if (conn.p.mypet == null) {
                // conn.p.mypet = new Pet(conn.p);
                // }
                // conn.p.mypet.setup((JSONArray) JSONValue.parse(js));
                // Service.send_wear(conn.p);
                // Service.send_char_main_in4(conn.p);
                // conn.p.map.update_in4_inside(conn.p);
                // Service.send_box_notice(conn, "Nhận thành công!");
                // // } else {
                // // Service.send_box_notice(conn, "Éo đủ coin, đi nạp đi!");
                // }
                if (conn.user.contains("knightauto_hsr_")) {
                    if (conn.p.level < 10) {
                        Service.send_notice_box(conn, "Đạt level 10 mới có thể đăng ký tài khoản");
                        return;
                    }
                    Service.send_box_input_text(conn, 6, "Nhập thông tin",
                            new String[]{"Tên đăng nhập mới", "Mật khẩu mới"});
                } else {
                    Service.send_notice_box(conn, "Chức năng đang phát triển...");
                }
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Black_Eye(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 13);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 14);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 15);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 16);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_BXH(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                BXH.send(conn, 0);
                break;
            }
            case 1: {
                BXH.send(conn, 1);
                break;
            }
            case 2: {
                BXH.send2(conn, 0);
                break;
            }
            case 3: {
                BXH.send2(conn, 1);
                break;
            }
            case 4: {
                BXH.send1(conn, 0);
                break;
            }
            case 5: {
                BXH.send1(conn, 1);
                break;
            }
            case 6: {
                String[] list = new String[Math.min(20, BXH.BXH_clan.size())];
                for (int i = 0; i < list.length; i++) {
                    list[i] = ("Top " + (i + 1) + " : " + BXH.BXH_clan.get(i).name_clan + "(" + BXH.BXH_clan.get(i).name_clan_shorted + ")");
                }
                if (list.length > 0) {
                    send_menu_select(conn, 120, list);
                } else {
                    Service.send_notice_box(conn, "Chưa có thông tin");
                }

                break;
            }
            case 7: {
                BXH.send3(conn, 0);
                break;
            }
            case 8: {
                BXH.send3(conn, 3);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có thông tin!!!");
                break;
            }
        }
    }

    private static void Menu_Miss_Anna(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_input_text(conn, 0, "Nhập mã code", new String[]{"Code"});
                break;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {
                if (conn.p.item.wear[index + 9] == null) {
                    Service.send_notice_box(conn, "Mặc đâu mà tháo");
                } else if (conn.p.item.get_bag_able() > 0) {
                    Item3 buffer = conn.p.item.wear[index + 9];
                    conn.p.item.wear[index + 9] = null;
                    if (buffer.id != 3599 && buffer.id != 3593 && buffer.id != 3596) {
                        conn.p.item.add_item_bag3(buffer);
                    }
                    conn.p.item.char_inventory(3);
                    conn.p.fashion = Part_fashion.get_part(conn.p);
                    Service.send_wear(conn.p);
                    Service.send_char_main_in4(conn.p);
                    MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                    Service.send_notice_box(conn, "Tháo thành công");
                    //
                    if (index == 2 && conn.p.pet_di_buon != null) {
                        Message mout = new Message(8);
                        mout.writer().writeShort(conn.p.pet_di_buon.index);
                        for (int i = 0; i < conn.p.map.players.size(); i++) {
                            Player p0 = conn.p.map.players.get(i);
                            if (p0 != null) {
                                p0.conn.addmsg(mout);
                            }
                        }
                        mout.cleanup();
                        //
                        Pet_di_buon_manager.remove(conn.p.pet_di_buon.name);
                        conn.p.pet_di_buon = null;
                    }
                } else {
                    Service.send_notice_box(conn, "Hành trang đầy!");
                }
                break;
            }
            case 10: {
                // nhận quà top lv
                if (conn.p.item.get_bag_able() < 20) {
                    Service.send_notice_box(conn, "Yêu cầu hành trang phải chống hơn 20 ô!");
                    return;
                }
                try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `giftcode2` WHERE `name_player` = '" + conn.p.name + "' AND `type_gift` = '2' AND `status` = '0';")) {
                    byte empty_box = (byte) 0;
                    if (!rs.next()) {
                        Service.send_notice_box(conn, "Không tìm thấy đơn hàng, hoặc bạn đã nhận trước đó!");
                        return;
                    }

                    String mess = rs.getString("logger");
                    empty_box = rs.getByte("empty_box");
                    byte date = rs.getByte("date");
                    if (conn.p.item.get_bag_able() >= empty_box) {
                        if (ps.executeUpdate("UPDATE `giftcode2` SET `status` = '1' WHERE `id` = '" + rs.getInt("id") + "';") > 0) {
                            connection.commit();
                        }
                        JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3_defauft"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            if (jsar2 == null || jsar2.size() < 1) {
                                continue;
                            }
                            Item3 itbag = new Item3();
                            short it = Short.parseShort(jsar2.get(0).toString());
                            itbag.id = it;
                            itbag.name = ItemTemplate3.item.get(it).getName();
                            itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                            itbag.type = ItemTemplate3.item.get(it).getType();
                            itbag.level = ItemTemplate3.item.get(it).getLevel();
                            itbag.icon = ItemTemplate3.item.get(it).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                            itbag.color = ItemTemplate3.item.get(it).getColor();
                            itbag.part = ItemTemplate3.item.get(it).getPart();
                            itbag.tier = 0;
                            itbag.time_use = 0;
                            itbag.islock = false;
                            if (date > 0) {
                                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                            }
                            conn.p.item.add_item_bag3(itbag);
                        }
                        jsar.clear();
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            if (jsar2 == null || jsar2.size() < 1) {
                                continue;
                            }
                            Item3 itbag = new Item3();
                            short it = Short.parseShort(jsar2.get(0).toString());
                            itbag.id = it;
                            itbag.name = ItemTemplate3.item.get(it).getName();
                            itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                            itbag.type = ItemTemplate3.item.get(it).getType();
                            itbag.level = ItemTemplate3.item.get(it).getLevel();
                            itbag.icon = ItemTemplate3.item.get(it).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                            itbag.color = ItemTemplate3.item.get(it).getColor();
                            itbag.part = ItemTemplate3.item.get(it).getPart();
                            itbag.tier = 0;
                            itbag.time_use = 0;
                            itbag.islock = true;
                            if (date > 0) {
                                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                            }
                            conn.p.item.add_item_bag3(itbag);
                        }
                        jsar.clear();

                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 4;
                            conn.p.item.add_item_bag47(4, itbag);
                        }
                        jsar.clear();
                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 7;
                            conn.p.item.add_item_bag47(7, itbag);

                        }
                        jsar.clear();
                        conn.p.update_coin(rs.getInt("coin"));
                        conn.p.update_vang(rs.getLong("vang"));
                        conn.p.update_ngoc(rs.getLong("ngoc"));
                        Log.gI().add_log(conn.p.name, "Get order :" + rs.getInt("id"));
                        conn.p.item.char_inventory(5);
                        conn.p.item.char_inventory(3);
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);

                        Service.send_notice_box(conn, mess);
                    } else {
                        Service.send_notice_box(conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }
            case 11: {
                // nhận quà top lv
                try (Connection connection = SQL.gI().getConnection(); Statement st = connection.createStatement(); Statement ps = connection.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM `giftcode2` WHERE `name_player` = '" + conn.p.name + "' AND `type_gift` = '1' AND `status` = '0';")) {
                    byte empty_box = (byte) 0;
                    if (!rs.next()) {
                        Service.send_notice_box(conn, "Không tìm thấy phần quà, hoặc bạn đã nhận trước đó!");
                        return;
                    }
                    String mess = rs.getString("logger");
                    empty_box = rs.getByte("empty_box");
                    byte date = rs.getByte("date");
                    if (conn.p.item.get_bag_able() >= empty_box) {
                        if (ps.executeUpdate("UPDATE `giftcode2` SET `status` = '1' WHERE `id` = '" + rs.getInt("id") + "';") > 0) {
                            connection.commit();
                        }
                        JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3_defauft"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            if (jsar2 == null || jsar2.size() < 1) {
                                continue;
                            }
                            Item3 itbag = new Item3();
                            short it = Short.parseShort(jsar2.get(0).toString());
                            itbag.id = it;
                            itbag.name = ItemTemplate3.item.get(it).getName();
                            itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                            itbag.type = ItemTemplate3.item.get(it).getType();
                            itbag.level = ItemTemplate3.item.get(it).getLevel();
                            itbag.icon = ItemTemplate3.item.get(it).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                            itbag.color = ItemTemplate3.item.get(it).getColor();
                            itbag.part = ItemTemplate3.item.get(it).getPart();
                            itbag.tier = 0;
                            itbag.time_use = 0;
                            itbag.islock = false;
                            if (date > 0) {
                                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                            }
                            conn.p.item.add_item_bag3(itbag);
                        }
                        jsar.clear();
                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            if (jsar2 == null || jsar2.size() < 1) {
                                continue;
                            }
                            Item3 itbag = new Item3();
                            short it = Short.parseShort(jsar2.get(0).toString());
                            itbag.id = it;
                            itbag.name = ItemTemplate3.item.get(it).getName();
                            itbag.clazz = ItemTemplate3.item.get(it).getClazz();
                            itbag.type = ItemTemplate3.item.get(it).getType();
                            itbag.level = ItemTemplate3.item.get(it).getLevel();
                            itbag.icon = ItemTemplate3.item.get(it).getIcon();
                            itbag.op = new ArrayList<>();
                            itbag.op.addAll(ItemTemplate3.item.get(it).getOp());
                            itbag.color = ItemTemplate3.item.get(it).getColor();
                            itbag.part = ItemTemplate3.item.get(it).getPart();
                            itbag.tier = 0;
                            itbag.time_use = 0;
                            itbag.islock = true;
                            if (date > 0) {
                                itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * date;
                            }
                            conn.p.item.add_item_bag3(itbag);
                        }
                        jsar.clear();
                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item4"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 4;
                            conn.p.item.add_item_bag47(4, itbag);
                        }
                        jsar.clear();
                        //
                        jsar = (JSONArray) JSONValue.parse(rs.getString("item7"));
                        for (int i = 0; i < jsar.size(); i++) {
                            JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
                            Item47 itbag = new Item47();
                            itbag.id = Short.parseShort(jsar2.get(0).toString());
                            itbag.quantity = Short.parseShort(jsar2.get(1).toString());
                            itbag.category = 7;
                            conn.p.item.add_item_bag47(7, itbag);

                        }
                        jsar.clear();
                        conn.p.update_coin(rs.getInt("coin"));
                        conn.p.update_vang(rs.getLong("vang"));
                        conn.p.update_ngoc(rs.getLong("ngoc"));
                        Log.gI().add_log(conn.p.name, "Get order :" + rs.getInt("id"));
                        conn.p.item.char_inventory(5);
                        conn.p.item.char_inventory(3);
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);

                        Service.send_notice_box(conn, mess);
                    } else {
                        Service.send_notice_box(conn, "Hành trang phải trống " + empty_box + " ô trở lên!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_PhapSu(Session conn, byte index) throws IOException {
        conn.p.ResetCreateItemStar();
        if (!conn.p.isOwner){
            return;
        }
        switch (index) {
            case 0: {
                conn.p.id_item_rebuild = -1;
                conn.p.is_use_mayman = false;
                conn.p.id_use_mayman = -1;
                Service.send_box_UI(conn, 18);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 17);
                break;
            }
            case 2: {
                conn.p.item_replace = -1;
                conn.p.item_replace2 = -1;
                Service.send_box_UI(conn, 19);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 24);
                break;
            }
            case 4: {
                Service.send_box_UI(conn, 25);
                conn.p.id_medal_is_created = 0;
                break;
            }
            case 5: {
                Service.send_box_UI(conn, 26);
                conn.p.id_medal_is_created = 1;
                break;
            }
            case 6: {
                Service.send_box_UI(conn, 27);
                conn.p.id_medal_is_created = 2;
                break;
            }
            case 7: {
                Service.send_box_UI(conn, 28);
                conn.p.id_medal_is_created = 3;
                break;
            }
            case 8: {
                Service.send_box_UI(conn, 33);
                break;
            }
            case 9:
            case 10: {
                ArrayList<String> myList = new ArrayList<String>();
                //Item3[] item3 = conn.p.item.bag3;
                Item3[] itemw = conn.p.item.wear;

                if (itemw == null || itemw.length < 13) {
                    Service.send_notice_box(conn, "Lỗi hành trang hoặc trang bị!");
                    return;
                }
                if (itemw.length > 12 && itemw[12] != null && CheckItem.isMeDay(itemw[12].id)) {
                    myList.add(itemw[12].name + " (1000 ngọc)");
                }
                if (myList == null || myList.size() <= 0) {
                    Service.send_notice_box(conn, "Không có vật phẩm phù hợp!");
                    return;
                }

                send_menu_select(conn, index == 9 ? 4 : 5, myList.toArray(new String[0]));

                //Service.send_notice_box(conn, "Chưa có chức năng"); doi dong st
                break;
            }
//            case 10: {
//                Service.send_notice_box(conn, "Chưa có chức năng"); doi pt st
//                break;
//            }
            case 11: {
                Service.send_box_UI(conn, 34);
                break;
            }
            case 12: {
                Service.send_box_UI(conn, 35);
                break;
            }
            case 13: {
                Service.send_box_UI(conn, 36);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Admin(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn chưa đủ quyền để thực hiện!");
                    return;
                }
                Service.send_box_input_yesno(conn, 88, "Bạn có chắc chắn muốn bảo trì server?");
                break;
            }
            case 1: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                conn.p.update_vang(1_000_000_000);
                Log.gI().add_log(conn.p.name, "Nhận " + 1_000_000_000 + " quyền admin");
                //conn.p.item.char_inventory(5);
                Service.send_notice_nobox_white(conn, "+ 1.000.000.000 vàng");
                break;
            }
            case 2: {
                if (conn.ac_admin <= 3) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                conn.p.update_ngoc(1_000_000);
                //conn.p.item.char_inventory(5);
                Service.send_notice_nobox_white(conn, "+ 1.000.000 ngọc");
                break;
            }
            case 3: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                SaveData.process();
                CheckDDOS.ips.clear();
                Service.send_notice_nobox_white(conn, "data đã đc cập nhật");
                break;
            }
            case 4: {
                Service.send_box_input_text(conn, 1, "Get Item",
                        new String[]{"Nhập loại (3,4,7) vật phẩm :", "Nhập id vật phẩm", "Nhập số lượng"});
                break;
            }
            case 5: {
                Service.send_box_input_text(conn, 2, "Plus Level", new String[]{"Nhập level :"});
                break;
            }
            case 6: {
                Service.send_box_input_text(conn, 4, "Set Xp", new String[]{"Nhập mức x :"});
                break;
            }
            case 7: {
                Service.send_box_input_text(conn, 18, "Tên nhân vật", new String[]{"Nhập Tên nhân vật :"});
                break;
            }
            case 8: {
                Service.send_box_input_text(conn, 19, "Tên nhân vật", new String[]{"Nhập Tên nhân vật :"});
                break;
            }
            case 9: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isLockVX = !Manager.isLockVX;
                Service.send_notice_box(conn, "Vòng xoay vàng ngọc đã " + (Manager.isLockVX ? "khóa" : "mở"));
                //Service.send_box_input_text(conn, 19, "Tên nhân vật", new String[]{"Nhập Tên nhân vật :"});
                break;
            }
            case 10: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isGiaoDich = !Manager.isGiaoDich;
                Service.send_notice_box(conn, "Giao dịch đã " + (Manager.isGiaoDich ? "mở" : "khóa"));
                break;
            }
            case 11: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isKTG = !Manager.isKTG;
                Service.send_notice_box(conn, "Chat KTG đã " + (Manager.isKTG ? "mở" : "khóa"));
                break;
            }
            case 12: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isKmb = !Manager.isKmb;
                Service.send_notice_box(conn, "KMB đã " + (Manager.isKmb ? "mở" : "khóa"));
                break;
            }
            case 13: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                for (Pet pet : conn.p.mypet) {
                    if (pet.time_born > 0) {
                        pet.time_born = 3;
                    }
                }
                Service.send_notice_box(conn, "Đã xong");
                break;
            }
            case 14: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.BuffAdmin = !Manager.BuffAdmin;
                Service.send_notice_box(conn, "Buff Admin đã: " + (Manager.BuffAdmin ? "Bật" : "Tắt"));
                break;
            }
            case 15: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.BuffAdminMaterial = !Manager.BuffAdminMaterial;
                Service.send_notice_box(conn, "Buff nguyên liệu cho Admin Đã: " + (Manager.BuffAdminMaterial ? "Bật" : "Tắt"));
                break;
            }
            case 16: {
                if (conn.ac_admin < 5) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.gI().chiem_mo.mo_open_atk();
                Manager.gI().chatKTGprocess(" Thời gian chiếm mỏ đã đến!");
                break;
            }
            case 17: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.gI().chiem_mo.mo_close_atk();
                Manager.gI().chatKTGprocess(" Thời gian chiếm mỏ đã đóng!");
                break;
            }
            case 18: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                break;
            }
            case 19: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                if (Manager.gI().event == 2) {
                    Event_2.ClearMob();
                    Event_2.ResetMob();
                    Service.send_notice_box(conn, "Đã thực hiện reset mob events");
                }
                break;
            }
            case 20: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                if (ChiemThanhManager.isRegister) {
                    ChiemThanhManager.EndRegister();
                } else {
                    ChiemThanhManager.StartRegister();
                }
                Service.send_notice_box(conn, "Đã thực hiện " + (ChiemThanhManager.isRegister ? "mở" : "đóng") + " đăng kí chiếm thành");
                break;
            }
            case 21: {
                ChienTruong.gI().open_register();
                Manager.gI().chatKTGprocess("Chiến Trường Đã Bắt Đầu Nhanh Tay Lẹ Chân Lên");
                break;
            }
            case 22: {
                if (conn.ac_admin < 4) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Service.send_box_input_text(conn, 21, "Dịch chuyển map",
                        new String[]{"Nhập idMap", "Nhập tọa độ x", "Nhập tọa độ y"});
                break;
            }
            case 23: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.gI().load_config();
                break;
            }
            case 24: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.logErrorLogin = !Manager.logErrorLogin;
                Service.send_notice_box(conn, "Bạn đã " + (Manager.logErrorLogin ? "Bật" : "Tắt") + " log error");
                break;
            }
            case 25: {
                Service.send_box_input_text(conn, 24, "Disconnect", new String[]{"Nhập loại :", "Nhập Tên :"});
                break;
            }
            case 26: {
                String ssss = "Start Check \n-----------------------------\n";
                try {
                    Message m = new Message(53);
                    m.writer().writeUTF("check log");
                    m.writer().writeByte(1);
                    int mapnulls = 0;
                    int mapnull = 0;
                    int pnull = 0;
                    ssss += "\nvo ne";
                    for (Map[] map : Map.entrys) {
                        if (map == null) {
                            mapnulls++;
                            continue;
                        }
                        for (Map map0 : map) {
                            if (map0 == null) {
                                mapnull++;
                                continue;
                            }
                            for (int i = 0; i < map0.players.size(); i++) {
                                if (map0.players.get(i) == null || map0.players.get(i).conn == null) {
                                    pnull++;
                                    continue;
                                }
                                map0.players.get(i).conn.addmsg(m);
                            }
                        }
                    }
                    ssss += "\n" + mapnulls + " Map[]Null";
                    ssss += "\n" + mapnull + " MapNull";
                    ssss += "\n" + pnull + " PlayerNull";
                    m.cleanup();
                } catch (Exception ex) {
                    Service.send_notice_box(conn, "Lỗi: " + ex.getMessage());
                    ex.printStackTrace();
                    StackTraceElement[] stackTrace = ex.getStackTrace(); // Lấy thông tin ngăn xếp gọi hàm

                    for (StackTraceElement element : stackTrace) {
                        ssss += ("Class: " + element.getClassName());
                        ssss += ("\nMethod: " + element.getMethodName());
                        ssss += ("\nFile: " + element.getFileName());
                        ssss += ("\nLine: " + element.getLineNumber());
                        ssss += ("------------------------\n");
                    }

                }
                Save_Log.process("checkbug.txt", ssss);
                break;
            }
            case 27: {
                String ssss = "Start Fix \n-----------------------------\n";
                try {
                    Message m = new Message(53);
                    m.writer().writeUTF("check log");
                    m.writer().writeByte(1);
                    int mapnulls = 0;
                    int mapnull = 0;
                    int pnull = 0;
                    ssss += "\nvo ne";
                    for (Map[] map : Map.entrys) {
                        if (map == null) {
                            mapnulls++;
                            continue;
                        }
                        for (Map map0 : map) {
                            if (map0 == null) {
                                mapnull++;
                                continue;
                            }
                            for (int i = map0.players.size() - 1; i >= 0; i--) {
                                if (map0.players.get(i) == null || map0.players.get(i).conn == null) {
                                    map0.players.remove(i);
                                }
                            }
                        }
                    }
                    ssss += "\n" + mapnulls + " Map[]Null";
                    ssss += "\n" + mapnull + " MapNull";
                    ssss += "\n" + pnull + " PlayerNull";
                    m.cleanup();
                } catch (Exception ex) {
                    Service.send_notice_box(conn, "Lỗi: " + ex.getMessage());
                    ex.printStackTrace();
                    StackTraceElement[] stackTrace = ex.getStackTrace(); // Lấy thông tin ngăn xếp gọi hàm

                    for (StackTraceElement element : stackTrace) {
                        ssss += ("Class: " + element.getClassName());
                        ssss += ("\nMethod: " + element.getMethodName());
                        ssss += ("\nFile: " + element.getFileName());
                        ssss += ("\nLine: " + element.getLineNumber());
                        ssss += ("------------------------\n");
                    }

                }
                Service.send_notice_box(conn, "xong");
                Save_Log.process("checkbug.txt", ssss);
                break;
            }
            case 28: {
                if (conn.ac_admin < 10) {
                    Service.send_notice_box(conn, "Bạn không đủ quyền!");
                    return;
                }
                Manager.isqua = !Manager.isqua;
                Service.send_notice_box(conn, "Giao dịch đã " + (Manager.isqua ? "mở" : "khóa"));
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Zulu(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (!conn.p.isOwner){
                    return;
                }
                switch (conn.p.clazz) {
                    case 0: {
                        Service.send_msg_data(conn, 23, "tocchienbinh");
                        break;
                    }
                    case 1: {
                        Service.send_msg_data(conn, 23, "tocsatthu");
                        break;
                    }
                    case 2:
                    case 3: {
                        Service.send_msg_data(conn, 23, "tocphapsu");
                        break;
                    }
                }
                break;
            }
            case 1: {
                if (!conn.p.isOwner){
                    return;
                }
                if (conn.p.diemdanh == 1) {
                    conn.p.diemdanh = 0;
                    if (conn.p.item.get_bag_able()< 1){
                        Service.send_notice_box(conn,"Cần để trống 1 ô");
                        return;
                    }
                    int vang_ = Util.random(1_000, 20_000);
                    int coin_ = Util.random(1_000,5_000);
                    conn.p.update_vang(vang_);
                    conn.p.update_coin(coin_);
                    Item47 itbag = new Item47();
                    itbag.id = 342;
                    itbag.quantity = 5;
                    itbag.category = 4;
                    conn.p.item.add_item_bag47(4, itbag);
                    if(Manager.gI().event == 12){
                        if (conn.p.checkvip() == 0){
                            itbag.id = 350;
                            itbag.quantity = 1;
                        }else if (conn.p.checkvip() == 1){
                            itbag.id = 350;
                            itbag.quantity = 2;
                        }else if (conn.p.checkvip() == 2){
                            itbag.id = 350;
                            itbag.quantity = 4;
                        }else if (conn.p.checkvip() == 3){
                            itbag.id = 350;
                            itbag.quantity = 6;
                        }else if (conn.p.checkvip() == 4){
                            itbag.id = 350;
                            itbag.quantity = 8;
                        }else if (conn.p.checkvip() == 5){
                            itbag.id = 350;
                            itbag.quantity = 10;
                        }
                        itbag.category = 4;
                        conn.p.item.add_item_bag47(4, itbag);
                    }
                    His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                    hisc.coin_change = coin_;
                    hisc.coin_last = conn.p.checkcoin();
                    hisc.Logger = "(NHẬN) từ điểm danh";
                    hisc.Flus();
                    Log.gI().add_log(conn.p.name, "Điểm danh ngày được " + Util.number_format(vang_) + " vàng và " + coin_+" coin.");
                    conn.p.item.char_inventory(4);
                    conn.p.item.char_inventory(5);
                    Service.send_notice_box(conn, "Bạn đã điểm danh thành công, được " + vang_ + " vàng và " + coin_+ " coin.");
                } else {
                    Service.send_notice_box(conn, "Bạn đã điểm danh hôm nay rồi");
                }
                break;
            }
            case 2: {
                if (!conn.p.isOwner){
                    return;
                }
                send_menu_select(conn, -49,
                        new String[]{"Hướng dẫn", "Nhận nhiệm vụ", "Hủy nhiệm vụ", "Trả nhiệm vụ", "Kiểm tra"}, (byte) 1);
                break;
            }
            case 3: {
                if (!conn.p.isOwner){
                    return;
                }
                short id_3, id_0, id_4, id_e, id_n, id_d, id_1, id_5;
                id_3 = 464;
                id_0 = 465;
                id_4 = 466;
                id_e = 467;
                id_n = 468;
                id_1 = 469;
                id_5 = 470;
                if (conn.p.item.total_item_by_id(7, id_3) <= 0
                        || conn.p.item.total_item_by_id(7, id_0) <= 0
                        || conn.p.item.total_item_by_id(7, id_4) <= 0
                        || conn.p.item.total_item_by_id(7, id_e) <= 0
                        || conn.p.item.total_item_by_id(7, id_n) <= 0
                        || conn.p.item.total_item_by_id(7, id_1) <= 0
                        || conn.p.item.total_item_by_id(7, id_5) <= 0) {
                    Service.send_notice_box(conn, "Đi tập hợp đủ 7 viên ngọc rồng đi");
                    return;
                }
                if (conn.p.get_vang() < 10_000_000) {
                    Service.send_notice_box(conn, "Không đủ 10m vàng");
                    return;
                }
                List<box_item_template> ids = new ArrayList<>();
                List<Integer> it7 = new ArrayList<>(Arrays.asList(356, 361, 366, 371, 376, 381));
                List<Integer> it7_vip = new ArrayList<>(Arrays.asList(471, 45, 351));
                conn.p.update_vang(-10_000_000);
                conn.p.item.remove(7, id_3, 1);
                conn.p.item.remove(7, id_0, 1);
                conn.p.item.remove(7, id_4, 1);
                conn.p.item.remove(7, id_e, 1);
                conn.p.item.remove(7, id_n, 1);
                conn.p.item.remove(7, id_1, 1);
                conn.p.item.remove(7, id_5, 1);
                for (int i = 0; i < 2; i++) {
                    int ran = Util.random(1, 100);
                    if (ran < 10) {
                        short id = Util.random(it7, new ArrayList<>()).shortValue();
                        short quant = 1;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran < 30) {
                        short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                        short quant = 5;
                        ids.add(new box_item_template(id, quant, (byte) 7));
                        conn.p.item.add_item_bag47(id, quant, (byte) 7);
                    } else if (ran < 50) {
                        short id = 318;
                        short quant = 5;
                        ids.add(new box_item_template(id, quant, (byte) 4));
                        conn.p.item.add_item_bag47(id, quant, (byte) 4);
                    } else if (ran > 99.9) {
                        short[] allowedIds = {4761};
                        short iditem = allowedIds[Util.random(0, allowedIds.length - 1)];
                        Item3 itbag = new Item3();
                        itbag.id = iditem;
                        itbag.name = ItemTemplate3.item.get(iditem).getName();
                        itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                        itbag.type = ItemTemplate3.item.get(iditem).getType();
                        itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                        itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                        itbag.color = ItemTemplate3.item.get(iditem).getColor();
                        itbag.part = ItemTemplate3.item.get(iditem).getPart();
                        itbag.tier = 0;
                        itbag.islock = false;
                        itbag.time_use = 0;
                        itbag.expiry_date = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 15;
                        conn.p.item.add_item_bag3(itbag);
                        conn.p.item.char_inventory(5);
                        ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    } else if (ran > 99.999) {
                        short[] allowedIds = {4587, 4588, 4589, 4590};
                        short iditem = allowedIds[Util.random(0, allowedIds.length - 1)];
                        Item3 itbag = new Item3();
                        itbag.id = iditem;
                        itbag.name = ItemTemplate3.item.get(iditem).getName();
                        itbag.clazz = ItemTemplate3.item.get(iditem).getClazz();
                        itbag.type = ItemTemplate3.item.get(iditem).getType();
                        itbag.level = ItemTemplate3.item.get(iditem).getLevel();
                        itbag.icon = ItemTemplate3.item.get(iditem).getIcon();
                        itbag.op = new ArrayList<>();
                        itbag.op.addAll(ItemTemplate3.item.get(iditem).getOp());
                        itbag.color = ItemTemplate3.item.get(iditem).getColor();
                        itbag.part = ItemTemplate3.item.get(iditem).getPart();
                        itbag.op = ItemTemplate3.item.get(iditem).getOp();
                        itbag.tier = 0;
                        itbag.islock = true;
                        itbag.time_use = 0;
                        conn.p.item.add_item_bag3(itbag);
                        conn.p.item.char_inventory(5);
                        ids.add(new box_item_template(iditem, (short) 1, (byte) 3));
                    }
                }
                //conn.p.doiqua++;
                conn.p.item.char_inventory(3);
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(5);
                conn.p.item.char_inventory(7);
                Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                break;
            }
//            case 3: {
//                Service.send_box_input_text(conn, 5, "Đổi coin sang ngọc \n" + "Khuyến Mại X" + Manager.gI().giakmngoc, new String[]{"Tỷ lệ 1000 coin = " + 500 * Manager.gI().giakmngoc + " Ngọc"});
//                break;
//            }
            case 4: {
                if (!conn.p.isOwner){
                    return;
                }
                //Service.send_notice_box(conn,"Chức năng đang tạm khóa" );
               Service.send_box_input_text(conn, 14, "Đổi coin sang vàng \n" + "Khuyến Mại X" + Manager.gI().giakmgold, new String[]{"Tỷ lệ 1000 coin = " + 1000000 * Manager.gI().giakmgold + " Vàng"});
                break;
            }

//            case 4: {
//                Service.send_notice_box(conn, "Chức năng không còn tồn tại.");
//                //Service.send_box_input_yesno(conn, 121, "1000 ngọc cho 2h, hãy xác nhận");
//                break;
//            }
//            case 5: {
//                EffTemplate ef = conn.p.get_eff(-126);
//                if (ef != null && ef.time > System.currentTimeMillis()) {
//                    Service.send_notice_box(conn,
//                            "Thời gian còn lại : " + Util.getTime((int) (ef.time - System.currentTimeMillis()) / 1000));
//                } else {
//                    Service.send_notice_box(conn, "Chức năng không còn tồn tại.");
//                    //Service.send_notice_box(conn, "Chưa đăng ký kiểm tra cái gì?");
//                }
//                break;
//            }
            case 5: {
                if (conn.p.type_exp == 0) {
                    conn.p.type_exp = 1;
                    Service.send_notice_box(conn, "Đã bật nhận exp");
                } else {
                    conn.p.type_exp = 0;
                    Service.send_notice_box(conn, "Đã tắt nhận exp");
                }
                break;
            }
            case 6: {
                if (!conn.p.isOwner){
                    return;
                }
                Service.send_box_input_text(conn, 30, "Đổi mật khẩu", new String[]{"nhập mật khẩu cũ",
                    "nhập mật khẩu mới", "nhập lại mật khẩu mới"});
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_ChangeZone(Session conn) throws IOException {
        Map[] map = Map.get_map_by_id(conn.p.map.map_id);
        if (map != null) {
            Message m = new Message(54);
            if (Map.is_map_cant_save_site(conn.p.map.map_id)) {
                m.writer().writeByte(conn.p.map.maxzone);
            } else {
                m.writer().writeByte(conn.p.map.maxzone + 1);
            }
            //
            for (int i = 0; i < conn.p.map.maxzone; i++) {
                if (map[i].players.size() > (map[i].maxplayer - 2)) {
                    m.writer().writeByte(2); // redzone
                } else if (map[i].players.size() >= (map[i].maxplayer / 2)) {
                    m.writer().writeByte(1); // yellow zone
                } else {
                    m.writer().writeByte(0); // green zone
                }
                if (i == 4 && Map.is_map_chiem_mo(conn.p.map, false)) {
                    m.writer().writeByte(4);
                } else if (i == 1 && !Map.is_map_not_zone2(conn.p.map.map_id)) {
                    m.writer().writeByte(3);
                } else {
                    m.writer().writeByte(0);
                }
            }
            if (!Map.is_map_cant_save_site(conn.p.map.map_id)) {
                m.writer().writeByte(1);
                m.writer().writeByte(5);
            }
            for (int i = 0; i < conn.p.map.maxzone; i++) {
                m.writer().writeUTF(
                        "Khu " + (map[i].zone_id + 1) + " (" + map[i].players.size() + "/" + map[i].maxplayer + ")");
            }
            if (!Map.is_map_cant_save_site(conn.p.map.map_id)) {
                m.writer().writeUTF("Khu đi buôn");
            }
            //
            conn.addmsg(m);
            m.cleanup();
        }
    }

    private static void Menu_Alisama(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 9);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 10);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 11);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 12);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_DaDichChuyen10(Session conn, byte index) throws IOException {
        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                || conn.p.item.wear[11].id == 3596)) {
            return;
        }
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                vgo = new Vgo();
                vgo.id_map_go = 33;
                vgo.x_new = 432;
                vgo.y_new = 480;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt, hãy kích hoạt");
                    return;
                }
                if (conn.p.get_ngoc() < 5_000) {
                    Service.send_notice_box(conn, "Không đủ 5k ngọc");
                }
                if (!conn.p.isOwner){
                    Service.send_notice_box(conn,"Đệ tử không vào được kmb");
                    return;
                }
                conn.p.update_ngoc(-5_000);
                vgo = new Vgo();
                vgo.id_map_go = 82;
                vgo.x_new = 432;
                vgo.y_new = 354;
                conn.p.item.char_inventory(5);
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 3: {
                vgo = new Vgo();
                vgo.id_map_go = 4;
                vgo.x_new = 888;
                vgo.y_new = 672;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 4: {
                vgo = new Vgo();
                vgo.id_map_go = 5;
                vgo.x_new = 1056;
                vgo.y_new = 864;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 5: {
                vgo = new Vgo();
                vgo.id_map_go = 8;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 6: {
                vgo = new Vgo();
                vgo.id_map_go = 9;
                vgo.x_new = 1243;
                vgo.y_new = 876;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 7: {
                vgo = new Vgo();
                vgo.id_map_go = 11;
                vgo.x_new = 286;
                vgo.y_new = 708;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 8: {
                vgo = new Vgo();
                vgo.id_map_go = 12;
                vgo.x_new = 240;
                vgo.y_new = 732;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 9: {
                vgo = new Vgo();
                vgo.id_map_go = 13;
                vgo.x_new = 150;
                vgo.y_new = 979;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 10: {
                vgo = new Vgo();
                vgo.id_map_go = 15;
                vgo.x_new = 469;
                vgo.y_new = 1099;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 11: {
                vgo = new Vgo();
                vgo.id_map_go = 16;
                vgo.x_new = 673;
                vgo.y_new = 1093;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 12: {
                vgo = new Vgo();
                vgo.id_map_go = 17;
                vgo.x_new = 660;
                vgo.y_new = 612;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_DaDichChuyen33(Session conn, byte index) throws IOException {
        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                || conn.p.item.wear[11].id == 3596)) {
            return;
        }
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 67;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                vgo = new Vgo();
                vgo.id_map_go = 33;
                vgo.x_new = 432;
                vgo.y_new = 480;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 2: {
                // vgo = new Vgo();
                // vgo.idmapgo = 82;
                // vgo.xnew = 432;
                // vgo.ynew = 354;
                // conn.p.changemap(conn.p, vgo);
                Service.send_notice_box(conn, "Đang bảo trì khu vực này");
                break;
            }
            case 3: {
                vgo = new Vgo();
                vgo.id_map_go = 20;
                vgo.x_new = 787;
                vgo.y_new = 966;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 4: {
                vgo = new Vgo();
                vgo.id_map_go = 22;
                vgo.x_new = 120;
                vgo.y_new = 678;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 5: {
                vgo = new Vgo();
                vgo.id_map_go = 24;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 6: {
                vgo = new Vgo();
                vgo.id_map_go = 26;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 7: {
                vgo = new Vgo();
                vgo.id_map_go = 29;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 8: {
                vgo = new Vgo();
                vgo.id_map_go = 31;
                vgo.x_new = 360;
                vgo.y_new = 624;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 9: {
                vgo = new Vgo();
                vgo.id_map_go = 37;
                vgo.x_new = 150;
                vgo.y_new = 674;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 10: {
                vgo = new Vgo();
                vgo.id_map_go = 39;
                vgo.x_new = 199;
                vgo.y_new = 882;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 11: {
                vgo = new Vgo();
                vgo.id_map_go = 41;
                vgo.x_new = 187;
                vgo.y_new = 462;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 12: {
                vgo = new Vgo();
                vgo.id_map_go = 43;
                vgo.x_new = 228;
                vgo.y_new = 43;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 13: {
                vgo = new Vgo();
                vgo.id_map_go = 45;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 14: {
                vgo = new Vgo();
                vgo.id_map_go = 50;
                vgo.x_new = 300;
                vgo.y_new = 300;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_DaDichChuyen55(Session conn, byte index) throws IOException {
        if (conn.p.item.wear[11] != null && (conn.p.item.wear[11].id == 3599 || conn.p.item.wear[11].id == 3593
                || conn.p.item.wear[11].id == 3596)) {
            return;
        }
        Vgo vgo = null;
        switch (index) {
            case 0: {
                vgo = new Vgo();
                vgo.id_map_go = 67;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 1: {
                // vgo = new Vgo();
                // vgo.idmapgo = 82;
                // vgo.xnew = 432;
                // vgo.ynew = 354;
                // conn.p.changemap(conn.p, vgo);
                Service.send_notice_box(conn, "Đang bảo trì khu vực này");
                break;
            }
            case 2: {
                vgo = new Vgo();
                vgo.id_map_go = 74;
                vgo.x_new = 258;
                vgo.y_new = 354;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 3: {
                vgo = new Vgo();
                vgo.id_map_go = 77;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 4: {
                vgo = new Vgo();
                vgo.id_map_go = 93;
                vgo.x_new = 462;
                vgo.y_new = 342;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 5: {
                vgo = new Vgo();
                vgo.id_map_go = 94;
                vgo.x_new = 306;
                vgo.y_new = 240;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 6: {
                vgo = new Vgo();
                vgo.id_map_go = 95;
                vgo.x_new = 390;
                vgo.y_new = 162;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 7: {
                vgo = new Vgo();
                vgo.id_map_go = 96;
                vgo.x_new = 198;
                vgo.y_new = 666;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 8: {
                vgo = new Vgo();
                vgo.id_map_go = 97;
                vgo.x_new = 432;
                vgo.y_new = 168;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 9: {
                vgo = new Vgo();
                vgo.id_map_go = 98;
                vgo.x_new = 270;
                vgo.y_new = 132;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            case 10: {
                vgo = new Vgo();
                vgo.id_map_go = 33;
                vgo.x_new = 432;
                vgo.y_new = 480;
                conn.p.change_map(conn.p, vgo);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Hammer(Session conn, byte index, byte idmenu) throws IOException {
        conn.p.ResetCreateItemStar();
        if (idmenu == 0) {
            switch (index) {
                case 0: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    Service.send_box_UI(conn, 5);
                    break;
                }
                case 1: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    Service.send_box_UI(conn, 6);
                    break;
                }
                case 2: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    Service.send_box_UI(conn, 7);
                    break;
                }
                case 3: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    Service.send_box_UI(conn, 8);
                    break;
                }
                case 4: // chế tạo tinh tú
                {
                    if (!conn.p.isOwner){
                        return;
                    }
                    send_menu_select(conn, -5, new String[]{"Chiến binh", "Sát thủ", "Pháp sư", "Xạ thủ"}, (byte) 1);
                    break;
                }
                case 5:// nâng cấp tinh tú
                {
                    if (!conn.p.isOwner){
                        return;
                    }
                    conn.p.isCreateItemStar = true;
                    Service.send_box_UI(conn, 33);
                    break;
                }
                case 6: { // giap sieu nhan
                    if (conn.p.item.wear[20] == null) {
                        Service.send_notice_box(conn, "Mặc đéo đâu mà tháo hả thằng lol?");
                    } else {
                        Item3 buffer = conn.p.item.wear[20];
                        conn.p.item.wear[20] = null;
                        conn.p.item.add_item_bag3(buffer);
                        conn.p.item.char_inventory(3);
                        conn.p.fashion = Part_fashion.get_part(conn.p);
                        Service.send_wear(conn.p);
                        Service.send_char_main_in4(conn.p);
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        Service.send_notice_box(conn, "Tháo thành công");
                    }
                    break;
                }
                case 7: { // thao danh hiẹu
                    if (conn.p.item.wear[19] == null) {
                        Service.send_notice_box(conn, "Mặc đéo đâu mà tháo hả thằng lol?");
                    } else {
                        Item3 buffer = conn.p.item.wear[19];
                        conn.p.item.wear[19] = null;
                        conn.p.item.add_item_bag3(buffer);
                        conn.p.item.char_inventory(3);
                        conn.p.fashion = Part_fashion.get_part(conn.p);
                        Service.send_wear(conn.p);
                        Service.send_char_main_in4(conn.p);
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        Service.send_notice_box(conn, "Tháo thành công");
                    }
                    break;
                }
                case 8: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    String[] nemu = new String[]{"Kháng băng", "Kháng lửa", "Kháng điện", "Kháng độc"};
                    send_menu_select(conn, -5, nemu, (byte) 15);
                    break;
                }
                case 9: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    conn.p.isCreateArmor = true;
                    Service.send_box_UI(conn, 33);
                    break;
                }
                case 10: {
                    if (!conn.p.isOwner){
                        return;
                    }
                    String[] nemu = new String[]{"Sách vật lý", "Sách ma pháp"};
                    send_menu_select(conn, -5, nemu, (byte) 14);
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        } else if (idmenu == 1) {
            String[] nemu = new String[]{"Nón", "Áo", "Quần", "Giày", "Găng tay", "Nhẫn", "Vũ khí", "Dây chuyền"};
            send_menu_select(conn, -5, nemu, (byte) (10 + index));
        } else if (idmenu >= 10 && idmenu <= 13) {
            conn.p.isCreateItemStar = true;
            conn.p.ClazzItemStar = (byte) (idmenu - 10);
            conn.p.TypeItemStarCreate = index;
            Service.send_box_UI(conn, 40 + index);
        } else if (idmenu == 14) {
            Service.send_box_input_yesno(conn, -123 + index, "Giá ghép sách là 10 ngọc, bạn có muốn tiếp tục không?");
        } else if (idmenu == 15) {
            conn.p.type_armor_create = index;
            String[] nemu = new String[]{"Giáp siêu nhân bạc", "Giáp siêu nhân tím", "Giáp siêu nhân xanh", "Giáp siêu nhân vàng"};
            send_menu_select(conn, -5, nemu, (byte) 16);
        } else if (idmenu == 16) {
            conn.p.id_armor_create = index;
            conn.p.isCreateArmor = true;
            Service.send_box_UI(conn, 49);
        }
    }

    private static void Menu_Alisama(Session conn, byte index, byte idmenu) throws IOException {
        if (idmenu == 0) {
            switch (index) {
                case 0: {
                    Service.send_box_UI(conn, 9);
                    break;
                }
                case 1: {
                    Service.send_box_UI(conn, 10);
                    break;
                }
                case 2: {
                    Service.send_box_UI(conn, 11);
                    break;
                }
                case 3: {
                    Service.send_box_UI(conn, 12);
                    break;
                }
                default:
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
            }
        }
    }

    private static void Menu_Doubar(Session conn, byte index, byte idmenu) throws IOException {
        if (idmenu == 1) {
            String s = "Có lỗi xảy ra";
            if (index == 0) {
                s = BossManager.GetInfoBoss(83);
            } else if (index == 1) {
                s = BossManager.GetInfoBoss(84);
            } else if (index == 2) {
                s = BossManager.GetInfoBoss(101);
            } else if (index == 3) {
                s = BossManager.GetInfoBoss(103);
            } else if (index == 4) {
                s = BossManager.GetInfoBoss(104);
            } else if (index == 5) {
                s = BossManager.GetInfoBoss(105);
            } else if (index == 6) {
                s = BossManager.GetInfoBoss(106);
            } else if (index == 7) {
                s = BossManager.GetInfoBoss(149);
            } else if (index == 8) {
                s = BossManager.GetInfoBoss(155);
            } else if (index == 9) {
                s = BossManager.GetInfoBoss(197);
            } else if (index == 10) {
                s = BossManager.GetInfoBoss(196);
            } else if (index == 11) {
                s = BossManager.GetInfoBoss(186);
            } else if (index == 12) {
                s = BossManager.GetInfoBoss(187);
            } else if (index == 13) {
                s = BossManager.GetInfoBoss(188);
            } else if (index == 14) {
                s = BossManager.GetInfoBoss(195);
            } else if (index == 15) {
                s = BossManager.GetInfoBoss(173);
            } else if (index == 16) {
                s = BossManager.GetInfoBoss(190);
            }
            Service.send_notice_box(conn, s);
            return;

        }
        // cua hang item wear
        switch (index) {
            case 0: {
                Service.send_box_UI(conn, 1);
                break;
            }
            case 1: {
                Service.send_box_UI(conn, 2);
                break;
            }
            case 2: {
                Service.send_box_UI(conn, 3);
                break;
            }
            case 3: {
                Service.send_box_UI(conn, 4);
                break;
            }
            case 4: {
                send_menu_select(conn, -4, new String[]{
                    "Dê bạc",
                    "Dê vàng",
                    "Xà nữ",
                    "Bọ cạp chúa",
                    "Quỷ một mắt",
                    "Quỷ đầu bò",
                    "Kỵ sĩ địa ngục",
                    "Nhện chúa",
                    "Giant Skeleton",
                    "Bos Even 11x",
                    "Bos Even 13x",
                    "Bos Even 0x",
                    "Bos Even 1x",
                    "Bos Even 2x",
                    "Bos Even 7x",
                    "Bos Even 8x",
                    "Boss sự kiện"

                }, (byte) 1);
                break;
            }
            case 5: {
                Service.send_box_UI(conn, 37);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_keva(Session conn, byte index, byte idmenu) throws IOException {
        if (idmenu == 0) {
            switch (index) {
//                case 0: { // cua hang potion
//                    Service.send_box_UI(conn, 0);
//                    break;
//                }
//                case 0: {
//                    if (conn.status != 0) {
//                        Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
//                        return;
//                    }
//                    if (conn.p.get_ngoc() < 100) {
//                        Service.send_notice_box(conn, "Không đủ 100 ngọc");
//                        return;
//                    }
//                    conn.p.update_ngoc(-100);
//
//                    send_menu_select(conn, -90, new String[]{"Khu Làng 10x", "Khu Lảng 11x", "Khu Lảng 12x", "Khu Lảng 13x", "Khu Lảng 14x"}, (byte) 1);
//                    break;
//                }
                case 0: {
                    if (conn.status != 0) {
                        Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                        return;
                    }
                    if (conn.p.get_ngoc() < 100) {
                        Service.send_notice_box(conn, "Không đủ 100 ngọc");
                        return;
                    }
                    conn.p.update_ngoc(-100);

                    send_menu_select(conn, -90, new String[]{"Khu Boss Even 0x", "Khu Boss Even 1x", "Khu Boss Even 2x", "Khu Boss Even 7x", "Khu Boss Even 8x", "Khu Boss Even 11x", "Khu Boss Even 13x"}, (byte) 2);
                    break;
                }
//                case 3: {
//                    if (conn.status != 0) {
//                        Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
//                        return;
//                    }
//                    if (conn.p.get_ngoc() < 100) {
//                        Service.send_notice_box(conn, "Không đủ 100 ngọc");
//                        return;
//                    }
//                    conn.p.update_ngoc(-100);
//
//                    Vgo vgo = null;
//                    vgo = new Vgo();
//                    vgo.id_map_go = 103;
//                    vgo.x_new = 282;
//                    vgo.y_new = 186;
//                    conn.p.change_map(conn.p, vgo);
//
//                    break;
//                }
                case 1: {
                    if (conn.status != 0) {
                        Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                        return;
                    }
                    if (conn.p.get_ngoc() < 100) {
                        Service.send_notice_box(conn, "Không đủ 100 ngọc");
                        return;
                    }
                    conn.p.update_ngoc(-100);

                    Vgo vgo = null;
                    vgo = new Vgo();
                    vgo.id_map_go = 1;
                    vgo.x_new = 282;
                    vgo.y_new = 186;
                    conn.p.change_map(conn.p, vgo);

                    break;
                }

                default: {
                    Service.send_notice_box(conn, "Chưa có chức năng");
                    break;
                }
            }
        } else if (idmenu == 1) {
            Menu_Langphusuongup(conn, index);
        } else if (idmenu == 2) {
            Menu_Langphusuongboss(conn, index);
        } 
    }

    private static void Menu_Mr_Haku(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_vang() < 500) {
                    Service.send_notice_box(conn, "Không đủ 500 vàng");
                    return;
                }
                conn.p.update_vang(-500);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 67;
                vgo.x_new = 576;
                vgo.y_new = 222;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default:
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
        }
    }

    private static void Menu_Langphusuongup(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 104;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 1: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 105;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 106;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 3: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 107;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 4: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 108;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Langphusuongboss(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 109;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 1: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 110;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 2: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 111;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 3: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 112;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 4: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 113;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 5: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 114;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }
            case 6: {
                if (conn.status != 0) {
                    Service.send_notice_box(conn, "Tài khoản chưa được kích hoạt,");
                    return;
                }
                if (conn.p.get_ngoc() < 100) {
                    Service.send_notice_box(conn, "Không đủ 100 ngọc");
                    return;
                }
                conn.p.update_ngoc(-100);

                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 115;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);

                break;
            }

            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }
    private static void Menu_Lisa(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: { // cua hang potion
                Service.send_box_UI(conn, 0);
                break;
            }
            case 1: {
                if (conn.p.item.total_item_by_id(4, 52) > 0) {
                    MoLy.show_table_to_choose_item(conn.p);
                } else {
                    Service.send_notice_box(conn, "Không đủ vé mở trong hành trang");
                }
                break;
            }
            case 2: { // cua hang potion
                Service.send_notice_box(conn,"Không sửa đc thuế");
                //Service.send_box_input_text(conn, 22, "% thuế", new String[]{"Nhập % thuế 5 - 20%"});
                break;
            }
            case 3: {
                Member_ChienTruong temp = ChienTruong.gI().get_bxh(conn.p.name);
                if (temp != null) {
                    switch (ChienTruong.gI().get_index_bxh(temp)) {
                        case 0: {
                            short[] id_ = new short[]{3, 2, 53, 54, 18};
                            short[] id2_ = new short[]{5, 5, 1, 1, 10};
                            short[] id3_ = new short[]{7, 7, 4, 4, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                        case 1:
                        case 2: {
                            short[] id_ = new short[]{3, 2, 18};
                            short[] id2_ = new short[]{5, 5, 10};
                            short[] id3_ = new short[]{7, 7, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9: {
                            short[] id_ = new short[]{3, 18};
                            short[] id2_ = new short[]{5, 10};
                            short[] id3_ = new short[]{7, 4};
                            for (int i = 0; i < id_.length; i++) {
                                Item47 it = new Item47();
                                it.id = id_[i];
                                it.quantity = id2_[i];
                                conn.p.item.add_item_bag47(id3_[i], it);
                            }
                            break;
                        }
                    }
                } else {
                    Service.send_notice_box(conn, "Không có tên trong danh sách");
                }
                break;
            }
            case 5: { // cua hang potion
                ChiemThanhManager.NhanQua(conn.p);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Emma(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: { // cua hang potion
                Service.send_box_UI(conn, 0);
                break;
            }
            case 1: {
                if (conn.p.item.total_item_by_id(4, 52) > 0) {
                    MoLy.show_table_to_choose_item(conn.p);
                } else {
                    Service.send_notice_box(conn, "Không đủ vé mở trong hành trang");
                }
                break;
            }
            case 2: { // cua hang potion
                Service.send_notice_box(conn, "Không sửa được thuế");
                //Service.send_box_input_text(conn, 22, "% thuế", new String[]{"Nhập % thuế 5 - 20%"});
                break;
            }
            case 3: {
                Service.send_notice_box(conn, "Nhóttttttttttttttttttt");

//                Member_ChienTruong temp = ChienTruong.gI().get_bxh(conn.p.name);
//                if (temp != null) {
//                    switch (ChienTruong.gI().get_index_bxh(temp)) {
//                        case 0: {
//                            short[] id_ = new short[]{3, 2, 53, 54, 18};
//                            short[] id2_ = new short[]{5, 5, 1, 1, 10};
//                            short[] id3_ = new short[]{7, 7, 4, 4, 4};
//                            for (int i = 0; i < id_.length; i++) {
//                                Item47 it = new Item47();
//                                it.id = id_[i];
//                                it.quantity = id2_[i];
//                                conn.p.item.add_item_bag47(id3_[i], it);
//                            }
//                            break;
//                        }
//                        case 1:
//                        case 2: {
//                            short[] id_ = new short[]{3, 2, 18};
//                            short[] id2_ = new short[]{5, 5, 10};
//                            short[] id3_ = new short[]{7, 7, 4};
//                            for (int i = 0; i < id_.length; i++) {
//                                Item47 it = new Item47();
//                                it.id = id_[i];
//                                it.quantity = id2_[i];
//                                conn.p.item.add_item_bag47(id3_[i], it);
//                            }
//                            break;
//                        }
//                        case 3:
//                        case 4:
//                        case 5:
//                        case 6:
//                        case 7:
//                        case 8:
//                        case 9: {
//                            short[] id_ = new short[]{3, 18};
//                            short[] id2_ = new short[]{5, 10};
//                            short[] id3_ = new short[]{7, 4};
//                            for (int i = 0; i < id_.length; i++) {
//                                Item47 it = new Item47();
//                                it.id = id_[i];
//                                it.quantity = id2_[i];
//                                conn.p.item.add_item_bag47(id3_[i], it);
//                            }
//                            break;
//                        }
//                    }
//                } else {
//                    Service.send_notice_box(conn, "Không có tên trong danh sách");
//                }
                break;
            }
            case 4: { // cua hang potion
                ChiemThanhManager.NhanQua(conn.p);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_CayThong(Session conn, byte index) throws IOException {
        if (Manager.gI().event == 1) {
            switch (index) {
                case 0:
                case 1:
                case 2:
                case 3: {
                    int quant = conn.p.item.total_item_by_id(4, (113 + index));
                    if (quant > 0) {
                        //
                        short[] id_4 = new short[]{2, 5, 52, 142, 225, 271};
                        short[] id_7 = new short[]{0, 4, 23, 34, 39, 352, 357, 362, 367, 372, 377, 382, 387, 392, 397, 402,
                            407, 412,};
                        HashMap<Short, Short> list_4 = new HashMap<>();
                        HashMap<Short, Short> list_7 = new HashMap<>();
                        for (int i = 0; i < quant; i++) {
                            if (conn.p.item.get_bag_able() > 1) {
                                if (80 > Util.random(100)) {
                                    Item47 it = new Item47();
                                    it.category = 4;
                                    it.id = id_4[Util.random(id_4.length)];
                                    it.quantity = (short) Util.random(1, 3);
                                    if (!list_4.containsKey(it.id)) {
                                        list_4.put(it.id, it.quantity);
                                    } else {
                                        short quant_ = it.quantity;
                                        list_4.put(it.id, (short) (list_4.get(it.id) + quant_));
                                    }
                                    conn.p.item.add_item_bag47(4, it);
                                } else {
                                    Item47 it = new Item47();
                                    it.category = 7;
                                    it.id = id_7[Util.random(id_7.length)];
                                    it.quantity = (short) Util.random(1, 2);
                                    if (!list_7.containsKey(it.id)) {
                                        list_7.put(it.id, it.quantity);
                                    } else {
                                        short quant_ = it.quantity;
                                        list_7.put(it.id, (short) (list_7.get(it.id) + quant_));
                                    }
                                    conn.p.item.add_item_bag47(7, it);
                                }
                            }
                        }
                        //
                        Event_1.add_caythong(conn.p.name, quant);
                        conn.p.item.remove(4, (113 + index), quant);
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);
                        String item_receiv = "\n";
                        for (Entry<Short, Short> en : list_4.entrySet()) {
                            item_receiv += ItemTemplate4.item.get(en.getKey()).getName() + " " + en.getValue() + "\n";
                        }
                        for (Entry<Short, Short> en : list_7.entrySet()) {
                            item_receiv += ItemTemplate7.item.get(en.getKey()).getName() + " " + en.getValue() + "\n";
                        }
                        Service.send_notice_box(conn, "Trang trí thành công " + quant + " lần và nhận được:" + item_receiv);
                    } else {
                        Service.send_notice_box(conn, "Không đủ trong hành trang!");
                    }
                    break;
                }
                case 4: {
                    send_menu_select(conn, 120, Event_1.get_top_caythong());
                    break;
                }
                default: {
                    Service.send_notice_box(conn, "Đang bảo trì");
                    break;
                }
            }
        }
    }

    private static void Menu_ThaoKhamNgoc(Session conn, byte index) throws IOException {
        if (conn.p.list_thao_kham_ngoc.size() > 0) {
            if (conn.p.item.get_bag_able() < 3) {
                Service.send_notice_box(conn, "Hành trang không đủ chỗ");
                return;
            }
            Item3 it = conn.p.list_thao_kham_ngoc.get(index);
            if (it != null) {
                for (int i = it.op.size() - 1; i >= 0; i--) {
                    byte id = it.op.get(i).id;
                    if (id == 58 || id == 59 || id == 60) {
                        if (it.op.get(i).getParam(0) != -1) {
                            Item47 it_add = new Item47();
                            it_add.id = (short) (it.op.get(i).getParam(0));
                            it_add.quantity = 1;
                            it_add.category = 7;
                            conn.p.item.add_item_bag47(7, it_add);
                        }
                        it.op.get(i).setParam(-1);
                    } else if (id >= 100 && id <= 107 || id == 5 || id == 6  || (id >= 116 && id <= 120)) {
                        it.op.remove(i);
                    }
                }
//                for (int i = 0; i < it.op.size(); i++) {
//                    if (it.op.get(i).id == 58) {
//                        if (it.op.get(i).getParam(0) != -1) {
//                            Item47 it_add = new Item47();
//                            it_add.id = (short) (it.op.get(i).getParam(0));
//                            it_add.quantity = 1;
//                            it_add.category = 7;
//                            conn.p.item.add_item_bag47(7, it_add);
//                        }
//                        it.op.get(i).setParam(-1);
//                    }
//                    if (it.op.get(i).id == 59) {
//                        if (it.op.get(i).getParam(0) != -1) {
//                            Item47 it_add = new Item47();
//                            it_add.id = (short) (it.op.get(i).getParam(0));
//                            it_add.quantity = 1;
//                            it_add.category = 7;
//                            conn.p.item.add_item_bag47(7, it_add);
//                        }
//                        it.op.get(i).setParam(-1);
//                    }
//                    if (it.op.get(i).id == 60) {
//                        if (it.op.get(i).getParam(0) != -1) {
//                            Item47 it_add = new Item47();
//                            it_add.id = (short) (it.op.get(i).getParam(0));
//                            it_add.quantity = 1;
//                            it_add.category = 7;
//                            conn.p.item.add_item_bag47(7, it_add);
//                        }
//                        it.op.get(i).setParam(-1);
//                    }
//                }
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
                Service.send_wear(conn.p);
                Service.send_notice_box(conn, "Tháo thành công");
            }
        }
    }

    private static void Menu_DoiDongMeDaySTG(Session conn, byte index) throws IOException {
        if (conn.p.item.wear != null && conn.p.item.wear.length > 12 && CheckItem.isMeDay(conn.p.item.wear[12].id)) {
            Service.send_box_input_yesno(conn, 94, "Thực hiện này sẽ tiêu tốn 1000 ngọc, bạn có chắc chắn?");
        } else {
            Service.send_notice_box(conn, "Không có vật phẩm phù hợp!");
        }
    }

    private static void Menu_DoiDongMeDaySTPT(Session conn, byte index) throws IOException {
        if (conn.p.item.wear != null && conn.p.item.wear.length > 12 && CheckItem.isMeDay(conn.p.item.wear[12].id)) {
            Service.send_box_input_yesno(conn, 98, "Thực hiện này sẽ tiêu tốn 1000 ngọc, bạn có chắc chắn?");
        } else {
            Service.send_notice_box(conn, "Không có vật phẩm phù hợp!");
        }
    }

    private static void Menu_Wedding(Session conn, byte index) throws IOException {
        switch (index) {
            case 0: {
                if (conn.p.item.wear[23] == null) {
                    Service.send_box_input_text(conn, 66, "Nhập thông tin",
                            new String[]{"Nhập số 4 : ", "Tên đối phương : "});
                } else {
                    Service.send_notice_box(conn, "Nhẫn cưới thì đeo đấy mà đòi cưới thêm ai??");
                }
                break;
            }
            case 1: {
                if (conn.p.item.wear[23] != null) {
                    Service.send_notice_box(conn, "Hãy trân trọng đi. Ngoài kia bao nhiêu người dell có ny kia kìa");
                    return;
//                    Wedding temp = Wedding.get_obj(conn.p.name);
//                    if (temp != null) {
//                        String name_target = "";
//                        if (temp.name_1.equals(conn.p.name)) {
//                            name_target = temp.name_2;
//                        } else {
//                            name_target = temp.name_1;
//                        }
//                        Service.send_box_input_yesno(conn, 111, "Xác định hủy hôn ước với " + name_target);
//                    }
                } else {
                    Service.send_notice_box(conn, "Đã cưới ai đâu, ảo tưởng à??");
                }
                break;
            }
            case 2: {
                Item3 it = conn.p.item.wear[23];
                if (it != null) {
                    float perc = (((float) Wedding.get_obj(conn.p.name).exp) / Level.entrys.get(it.tier).exp) * 100f;
                    String notice = "Exp hiện tại : %s, nâng cấp cần %str vàng và %sk ngọc";
                    String a = String.format("%.2f", perc) + "%";
                    Service.send_box_input_yesno(conn, 112,
                            String.format(notice, a, (3 * (it.tier + 1)) * 5_000_000L, (3 * (it.tier + 1)) * 10_000));
                } else {
                    Service.send_notice_box(conn, "Đã cưới ai éo đâu, ảo tưởng à??");
                }
                break;
            }
            case 3: {
                String notice = "- 100k coin \" nhẫn cưới 4\"\r\n" + "Nâng cấp nhẫn:\r\n"
                        + "Khi đã kết hôn vợ và chồng cùng chung 1 nhóm đi up quái hoặc giết boss thì nhẫn cưới sẽ đc + exp\r\n"
                        + "Đến npc Anna để tiến hành nâng cấp nhẫn, mỗi lần nâng cấp tốn 3k ngọc 3tr vàng.\r\n"
                        + "Cấp tối đa của nhẫn là 30. \r\n" + "Lưu ý: Mỗi lần nâng cấp số vàng và ngọc sẽ nhân lên \r\n"
                        + "Ví dụ: Lever 1-2 phí 3k và 3tr lên level 2-3 phí sẽ nhân lên 6k và 6tr vàng";
                Service.send_notice_box(conn, notice);
                break;
            }
            default: {
                Service.send_notice_box(conn, "Chưa có chức năng");
                break;
            }
        }
    }

    private static void Menu_Serena(Session conn, byte index) throws IOException {
        if (conn.p.map.map_id == 135) {
            if (index == 0) {
                conn.p.veLang();
            } else if (index == 1) {
                if (conn.p.get_EffDefault(-128) == null) {
                    if (conn.p.item.total_item_by_id(4, 327) < 1) {
                        Service.send_notice_box(conn, "Không có Vé vào làng phủ sương trong hành trang");
                        return;
                    } else {
                        conn.p.add_EffDefault(-128, 1, 4 * 60 * 60 * 1000);
                        conn.p.item.remove(4, 327, 1);
                    }
                }
                EffTemplate eff = conn.p.get_EffDefault(-128);
                if (eff != null) {
                    Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Làng phủ sương"});
                    if (100 <= conn.p.level && conn.p.level < 110) {
                        Vgo vgo = new Vgo();
                        vgo.id_map_go = 125;
                        vgo.x_new = 100;
                        vgo.y_new = 100;
                        conn.p.change_map(conn.p, vgo);
                    } else if (110 <= conn.p.level && conn.p.level < 120) {
                        Vgo vgo = new Vgo();
                        vgo.id_map_go = 127;
                        vgo.x_new = 100;
                        vgo.y_new = 100;
                        conn.p.change_map(conn.p, vgo);
                    } else if (120 <= conn.p.level && conn.p.level < 130) {
                        Vgo vgo = new Vgo();
                        vgo.id_map_go = 129;
                        vgo.x_new = 200;
                        vgo.y_new = 200;
                        conn.p.change_map(conn.p, vgo);
                    } else if (130 <= conn.p.level) {
                        Vgo vgo = new Vgo();
                        vgo.id_map_go = 132;
                        vgo.x_new = 100;
                        vgo.y_new = 100;
                        conn.p.change_map(conn.p, vgo);
                    }
                }
            }
        } else {
            //conn.p.veLang();
        }
    }
}
