package event;

import core.*;
import io.Session;
import template.Item47;
import template.box_item_template;

import java.io.IOException;
import java.util.*;

public class LunarNewYear {
    public static final String name_event = "Tết nguyên đán";
    public static boolean runing = false;
    public static String[] menu = new String[]{"Góp gạo", "Góp thịt", "Góp đậu xanh", "Góp lá", "Nhận bánh",
            "Đổi bánh dày", "Ghép chữ Happy New Year", "Dâng bánh"};
    public static ArrayList<EventManager.PlayerRegister> list_nhan_banh = new ArrayList<>();

    public static void Menu(Session conn, byte index) throws IOException {
        switch (index) {
            case 0:
            case 1:
            case 2:
            case 3: {
                if (EventManager.check(EventManager.registerList, conn.p.name)) {
                    Service.send_box_input_text(conn, 29 + index, menu[index], new String[]{"Số lượng"});
                } else {
                    if (runing == true) {
                        Service.send_notice_box(conn, "Không trong thời gian đăng ký");
                        return;
                    }
                    Service.send_box_input_yesno(conn, -111, "Bạn có muốn đăng ký nấu bánh với giá 5 ngọc?");
                }
                break;
            }
            case 4: {
                EventManager.PlayerRegister playerRegister = EventManager.getPlayer(list_nhan_banh, conn.p.name);
                if (playerRegister != null) {
                    Item47 it = new Item47();
                    it.category = 4;
                    it.id = 31;
                    short quantity = (short) ((0 <= playerRegister.rank && playerRegister.rank < 5) ? Math.min(playerRegister.total * 2, 80) : Math.min(playerRegister.total, 40));
                    it.quantity = quantity;
                    conn.p.item.add_item_bag47(4, it);
                    conn.p.item.char_inventory(4);
                    Service.send_notice_box(conn, "Nhận được " + quantity + " bánh chưng");
                    list_nhan_banh.remove(playerRegister);
                } else {
                    Service.send_notice_box(conn, "Bạn không có tên trong danh sách hoặc đã nhận rồi.");
                }
                break;
            }
            case 5: {
                Service.send_box_input_text(conn, 33, menu[index], new String[]{"Số lượng"});
                break;
            }
            case 6: {
                Service.send_box_input_text(conn, 34, menu[index], new String[]{"Số lượng"});
                break;
            }
            case 7: {
                if (conn.p.item.total_item_by_id(4, 31) > 0 && conn.p.item.total_item_by_id(4, 195) > 0) {
                    if (conn.p.item.get_bag_able() > 4) {
                        Quask.qua_sk_tet(conn,(byte) 0);
                    } else {
                        Service.send_notice_box(conn, "Hành trang đầy");
                    }
                } else {
                    Service.send_notice_box(conn, "Dâng bánh cần 1 bánh chưng và 1 bánh dày");
                }
                break;
            }
        }
    }

    public static void ban_phao(Session conn) throws IOException {
        if (conn.p.item.total_item_by_id(4, 259) >= 1 && conn.p.item.total_item_by_id(4, 129) >= 1 && conn.p.checkcoin() > 20000) {
            if (conn.p.item.get_bag_able() < 4) {
                Service.send_notice_box(conn, "Hành trang đầy");
                return;
            }
            Quask.qua_sk_tet(conn,(byte) 1);
            conn.p.item.char_inventory(4);
            //Service.eff_map(conn.p.map, conn.p,-66, 62, conn.p.x, conn.p.y, 0, 0, 0);
            conn.p.doiqua++;
        } else {
            Service.send_notice_box(conn, "Cần 1 thuốc nổ, 1 giấy đỏ và 20k coin");
        }
    }

    public synchronized static void add_material(String name, int type, int index, int quant) {
        if (type == 0) {
            EventManager.PlayerRegister playerRegister = new EventManager.PlayerRegister(name);
            EventManager.registerList.add(playerRegister);
        } else {
            EventManager.PlayerRegister playerRegister = EventManager.getPlayer(EventManager.registerList, name);
            if (playerRegister != null) {
                playerRegister.material[index] += (short) quant;
            }
        }
    }

    public static void setList_nhan_banh() {
        int counter = 0;
        for (EventManager.PlayerRegister playerRegister : EventManager.registerList) {
            if (playerRegister.total > 0) {
                playerRegister.rank = counter;
                list_nhan_banh.add(playerRegister);
                counter++;
            }
        }
    }

    public static void finish() {
        runing = false;
        list_nhan_banh.clear();
        setList_nhan_banh();
        EventManager.registerList.clear();
    }
}
