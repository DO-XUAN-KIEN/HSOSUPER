package client;

import core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ev_he.Event_Ruong;
import event.Event_1;
import io.Message;
import io.Session;
import map.LeaveItemMap;
import map.MapService;
import map.Vgo;
import map.Map;
import template.EffTemplate;
import template.Item3;
import template.Item47;
import template.ItemTemplate3;
import template.ItemTemplate4;
import template.ItemTemplate7;
import template.Level;
import template.Option;
import template.Pet_di_buon;
import template.Pet_di_buon_manager;
import template.StrucEff;
import template.Horse;
import template.box_item_template;

public class UseItem {

    public static void ProcessItem4(Session conn, Message m2) throws IOException {
        short id = m2.reader().readShort();
        if (conn.p.item.total_item_by_id(4, id) > 0) {
            switch (ItemTemplate4.item.get(id).getType()) {
                case 44: {
                    if (conn.p.id_horse != -1) {
                        conn.p.id_horse = -1;
                        MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                        Service.send_char_main_in4(conn.p);
                    }
                    use_item_mount(conn, id);
                    break;
                }
                default: {
                    use_item4_default(conn, id);
                    break;
                }
            }
            conn.p.item.char_inventory(4);
            conn.p.item.char_inventory(7);
            conn.p.item.char_inventory(3);
        } else if (conn.p.myclan != null && conn.p.myclan.check_id(id)) {
            if (ItemTemplate4.item.get(id).getType() == 44) {
                if (conn.p.id_horse != -1) {
                    conn.p.id_horse = -1;
                    MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                    Service.send_char_main_in4(conn.p);
                }
                use_item_mount(conn, id);
            } else {
                use_item4_default(conn, id);
                conn.p.item.char_inventory(4);
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(3);
            }
        }

    }
    private static void use_item4_default(Session conn, short id_potion) throws IOException {
        switch (id_potion) {
            case 57:
            case 58:
            case 59:
            case 60: {
                if (Map.is_map_chien_truong(conn.p.map.map_id)) {
                    MapService.use_item_arena(conn.p.map, conn.p, id_potion);
                }
                break;
            }
            case 328: {
                if (conn.p.get_EffDefault(EffTemplate.buffdame) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.buffdame,1,30 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.buffdame);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff dame"});
                break;
            }
            case 332:{
                if (conn.p.get_EffDefault(EffTemplate.buffhp) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.buffhp,1,30 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.buffhp);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff hp"});
                break;
            }
            case 331:{
                if (conn.p.get_EffDefault(EffTemplate.buffne) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.buffne,1,30 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.buffne);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff né"});
                break;
            }
            case 330:{
                if (conn.p.get_EffDefault(EffTemplate.buffpst) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.buffpst,1,30 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.buffpst);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff pst"});
                break;
            }
            case 329:{
                if (conn.p.get_EffDefault(EffTemplate.buffpt) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.buffpt,1,30 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.buffpt);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff pt"});
                break;
            }
            case 333:{
                if (conn.p.get_EffDefault(EffTemplate.buffhoihp) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.buffhoihp,1,30 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.buffhoihp);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff hồi hp"});
                break;
            }
            case 334:{
                if (conn.p.get_EffDefault(EffTemplate.bufftatca) != null){
                    Service.send_notice_box(conn,"Bạn đang sử dụng buff rồi");
                    return;
                }
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(EffTemplate.bufftatca,1,60 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(EffTemplate.bufftatca);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Buff a đến z"});
                break;
            }
            case 339: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.quask_hon_gio(conn, (byte) 0);
                break;
            }
            case 341: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.quask_hon_gio(conn, (byte) 1);
                break;
            }
            case 340: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.quask_hon_gio(conn, (byte) 2);
                break;
            }
            case 343: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.add_EffDefault(-129, 1, 2 * 60 * 60 * 1000);
                EffTemplate eff = conn.p.get_EffDefault(-129);
                Service.send_time_box(conn.p, (byte) 1, new short[]{(short) ((eff.time - System.currentTimeMillis()) / 1000)}, new String[]{"Miễn nhiễm hiệu ứng"});
                break;
            }
            case 346: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.qua_sk_moi(conn, (byte) 0);
                break;
            }
            case 347: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.qua_sk_moi(conn, (byte) 1);
                break;
            }
            case 349: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.qua_sk_hallweeen(conn, (byte) 0);
                break;
            }
            case 350: {
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.qua_sk_hallweeen(conn, (byte) 1);
                break;
            }
            case 353: {// rương đồng money
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.Menu_noel(conn, (byte) 0);
                break;
            }
            case 354: {// rương ngọc khảm
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.Menu_noel(conn, (byte) 1);
                break;
            }
            case 355: {// rương thú cưỡi
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                if (Util.random(100)< 5){
                    Quask.Menu_noel(conn,(byte) 4);
                }else {
                    Quask.Menu_noel(conn,(byte) 3);
                }
                break;
            }
            case 356: {// rương hồ quang
                conn.p.item.remove(4,id_potion,1);
                conn.p.item.char_inventory(4);
                Quask.Menu_noel(conn, (byte) 2);
                break;
            }
            case 84: {
                if (conn.p.map.zone_id != conn.p.map.maxzone) {
                    Service.send_notice_box(conn, "Chỉ dùng được trong khu đi buôn");
                    return;
                }
                if (!conn.p.isTrader()) {
                    Service.send_notice_box(conn, "Chỉ dùng được khi là buôn ");
                    return;
                }
                if (conn.p.pet_di_buon == null) {
                    conn.p.pet_di_buon = new Pet_di_buon(131, Manager.gI().get_index_mob_new(), conn.p.x, conn.p.y,
                            conn.p.map.map_id, conn.p.name, conn.p);
                    Pet_di_buon_manager.add(conn.p.name, conn.p.pet_di_buon);
                    //
                    Message m22 = new Message(4);
                    m22.writer().writeByte(1);
                    m22.writer().writeShort(131);
                    m22.writer().writeShort(conn.p.pet_di_buon.index);
                    m22.writer().writeShort(conn.p.pet_di_buon.x);
                    m22.writer().writeShort(conn.p.pet_di_buon.y);
                    m22.writer().writeByte(-1);
                    conn.addmsg(m22);
                    m22.cleanup();
                    //
                    conn.p.item.remove(4, id_potion, 1);
                } else {
                    Service.send_notice_box(conn,
                            "Vật đi buôn của bạn đang ở\nVị trí:\n" + Map.get_map_by_id(conn.p.pet_di_buon.id_map)[0].name + "\n"
                                    + conn.p.pet_di_buon.x + " " + conn.p.pet_di_buon.y);
                }
            break;
        }
        case 86: {
            if (conn.p.map.zone_id != conn.p.map.maxzone) {
                Service.send_notice_box(conn, "Chỉ dùng được trong khu đi buôn");
                return;
            }
            if (!conn.p.isRobber()) {
                Service.send_notice_box(conn, "Chỉ dùng được khi là cướp ");
                return;
            }
            if (conn.p.pet_di_buon == null) {
                conn.p.pet_di_buon = new Pet_di_buon(132, Manager.gI().get_index_mob_new(), conn.p.x, conn.p.y,
                        conn.p.map.map_id, conn.p.name, conn.p);
                Pet_di_buon_manager.add(conn.p.name, conn.p.pet_di_buon);
                //
                Message m22 = new Message(4);
                m22.writer().writeByte(1);
                m22.writer().writeShort(132);
                m22.writer().writeShort(conn.p.pet_di_buon.index);
                m22.writer().writeShort(conn.p.pet_di_buon.x);
                m22.writer().writeShort(conn.p.pet_di_buon.y);
                m22.writer().writeByte(-1);
                conn.addmsg(m22);
                m22.cleanup();
                //
                conn.p.item.remove(4, id_potion, 1);
            } else {
                Service.send_notice_box(conn,
                        "Vật đi buôn của bạn đang ở\nVị trí:\n" + Map.get_map_by_id(conn.p.pet_di_buon.id_map)[0].name + "\n"
                                + conn.p.pet_di_buon.x + " " + conn.p.pet_di_buon.y);
            }
            break;
        }
            case 0:
            case 1:
            case 25:
            case 2: {
                EffTemplate vet_thuong_sau = conn.p.get_EffDefault(StrucEff.VET_THUONG_SAU);
                EffTemplate te_cong = conn.p.get_EffDefault(StrucEff.TE_CONG);
                if (conn.p.time_use_poition_hp < System.currentTimeMillis() && te_cong == null && vet_thuong_sau == null) {
                    conn.p.time_use_poition_hp = System.currentTimeMillis() + 2000L;
                    conn.p.item.remove(4, id_potion, 1);
                    int param = ItemTemplate4.item.get(id_potion).getValue();
                    param += ((param * 5 * conn.p.body.get_skill_point(9)) / 100);
                    Service.usepotion(conn.p, 0, param);
                }
                break;
            }
            case 3:
            case 4:
            case 5: {
                if (conn.p.time_use_poition_mp < System.currentTimeMillis()) {
                    conn.p.time_use_poition_mp = System.currentTimeMillis() + 2000L;
                    conn.p.item.remove(4, id_potion, 1);
                    int param = ItemTemplate4.item.get(id_potion).getValue();
                    param += ((param * 5 * conn.p.body.get_skill_point(10)) / 100);
                    Service.usepotion(conn.p, 1, param);
                }
                break;
            }
            case 6: {
                Service.send_box_input_yesno(conn, 125, "Hãy xác nhận bạn muốn tẩy tiềm năng");
                break;
            }
            case 7: {
                Service.send_box_input_yesno(conn, 124, "Hãy xác nhận bạn muốn tẩy kỹ năng");
                break;
            }
            case 10: {
                conn.p.item.remove(4, id_potion, 1);
                EffTemplate ef = conn.p.get_EffDefault(-125);
                if (ef != null) {
                    long time_extra = (ef.time - System.currentTimeMillis()) + (1000 * 60 * 120 - 1);
                    if (time_extra > (1000 * 60 * 60 * 24 * 3 - 1)) {
                        time_extra = 1000 * 60 * 60 * 24 * 3 - 1;
                    }
                    conn.p.add_EffDefault(-125, 5000, (int) time_extra);
                } else {
                    conn.p.add_EffDefault(-125, 5000, (1000 * 60 * 120 - 1));
                }
                conn.p.set_x2_xp(1);
                break;
            }
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16: {
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                    return;
                }
                Item47 it = new Item47();
                it.id = (short) (id_potion - 11);
                it.quantity = ItemTemplate4.item.get(id_potion).getValue();
                conn.p.item.add_item_bag47(4, it);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
             case 19: {
                conn.p.item.remove(4, id_potion, 1);
                EffTemplate ef = conn.p.get_EffDefault(-125);
                if (ef != null) {
                    long time_extra = (ef.time - System.currentTimeMillis()) + (1000 * 60 * 30 - 1);
                    if (time_extra > (1000 * 60 * 60 * 24 * 3 - 1)) {
                        time_extra = 1000 * 60 * 60 * 24 * 3 - 1;
                    }
                    conn.p.add_EffDefault(-125, 5000, (int) time_extra);
                } else {
                    conn.p.add_EffDefault(-125, 5000, (1000 * 60 * 30 - 1));
                }
                conn.p.set_x2_xp(1);
                break;
            }
            case 24: {
//                if (conn.p.hieuchien == 0) {
//                    Service.send_notice_box(conn, "Tính tình con còn tốt chán, chưa cần rửa tội đâu!");
//                    return;
//                }
//                conn.p.item.remove(4, id_potion, 1);
//                conn.p.hieuchien = 0;
                break;
            }
            case 26: {
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                    return;
                }
                Item47 it = new Item47();
                it.id = (short) (id_potion - 1);
                it.quantity = ItemTemplate4.item.get(id_potion).getValue();
                conn.p.item.add_item_bag47(4, it);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 69: {
//                if (conn.p.level < 40) {
//                    Service.send_notice_nobox_white(conn, "Yêu cầu trình độ cấp 40");
//                    return;
//                }
                if (conn.p.pet_follow == -1) {
                    Service.send_notice_nobox_white(conn, "Chưa mang theo pet!");
                    return;
                }
                Pet pet = null;
                for (Pet temp : conn.p.mypet) {
                    if (temp.is_follow) {
                        pet = temp;
                    }
                }
                if (pet != null) {
                    if (pet.can_revolution()) {
                        pet.UpgradeLevel();
                        pet.exp = 0;
                        pet.icon++;
                        Service.send_wear(conn.p);
                        conn.p.item.remove(4, id_potion, 1);
                    } else if (pet.level >= 29) {
                        Service.send_notice_box(conn, "Pet đã đạt cấp tối đa!");
                    } else {
                        Service.send_notice_nobox_white(conn, "Pet còn non lắm!");
                    }
                }
                break;
            }
             case 142: {
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 1;
                vgo.x_new = 492;
                vgo.y_new = 366;
                conn.p.change_map(conn.p, vgo);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
              case 241: {
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 103;
                vgo.x_new = 282;
                vgo.y_new = 186;
                conn.p.change_map(conn.p, vgo);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 164: {
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 88;
                vgo.x_new = 456;
                vgo.y_new = 360;
                conn.p.change_map(conn.p, vgo);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 166: {
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 89;
                vgo.x_new = 456;
                vgo.y_new = 360;
                conn.p.change_map(conn.p, vgo);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 168: {
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 90;
                vgo.x_new = 456;
                vgo.y_new = 360;
                conn.p.change_map(conn.p, vgo);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 170: {
                Vgo vgo = null;
                vgo = new Vgo();
                vgo.id_map_go = 91;
                vgo.x_new = 456;
                vgo.y_new = 360;
                conn.p.change_map(conn.p, vgo);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }

            case 162: {
                int exp_add = Util.random(conn.p.level * 100);
                conn.p.update_Exp(exp_add, false);
                int quant_item = Util.random(2, 5);
                short[] item_add
                        = new short[]{13, 5, 25, 8, 9, 10, 11, 18, 131, 132, 133, 33, 44, 48, 49, 50, 51, 205, 206, 207, 142};
                byte[] item_type = new byte[]{7, 4, 4, 7, 7, 7, 7, 4, 4, 4, 4, 7, 7, 4, 4, 4, 4, 4, 4, 4, 4};
                //
                Message m = new Message(78);
                m.writer().writeUTF("Bạn nhận được: " + exp_add + " exp");
                m.writer().writeByte(quant_item); // size
                for (int i = 0; i < quant_item; i++) {
                    int index = Util.random(item_add.length);
                    // if (25 > Util.random(110)) {
                    // index = Medal_Material.m_yellow[Util.random(Medal_Material.m_yellow.length)];
                    // }
                    m.writer().writeUTF(""); // name
                    m.writer().writeShort((item_type[index] == 4) ? ItemTemplate4.item.get(item_add[index]).getIcon()
                            : ItemTemplate7.item.get(item_add[index]).getIcon()); // icon
                    int quant_ = Util.random(1, 3);
                    m.writer().writeInt(quant_); // quantity
                    m.writer().writeByte(item_type[index]); // type in bag
                    m.writer().writeByte(0); // tier
                    m.writer().writeByte(0); // color
                    //
                    // m.writer().writeUTF(""); // name
                    // m.writer().writeShort(item2.getIcon()); // icon
                    // m.writer().writeInt(quant2_); // quantity
                    // m.writer().writeByte(7); // type in bag
                    // m.writer().writeByte(0); // tier
                    // m.writer().writeByte(0); // color
                    //
                    // m.writer().writeUTF(""); // name
                    // m.writer().writeShort(0); // icon
                    // m.writer().writeInt(quant3_); // quantity
                    // m.writer().writeByte(4); // type in bag
                    // m.writer().writeByte(0); // tier
                    // m.writer().writeByte(0); // color
                    Item47 itbag = new Item47();
                    itbag.id = item_add[index];
                    itbag.quantity = (short) quant_;
                    itbag.category = item_type[index];
                    conn.p.item.add_item_bag47(item_type[index], itbag);
                }
                m.writer().writeUTF("");
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                conn.addmsg(m);
                m.cleanup();
                //
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
//            case 328:{ // rương Vàng 777
//                
//                
//                break;
//            }
//           
//            case 329:{//Rương Kim Cương
//                break;
//            }
//            case 330:{ // Rương Báu Vật
//                break;
//            }
            
            
            case 318 :{ // rương Black FriDay
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                    return;
                }
                ItemTemplate7 item1 = ItemTemplate7.item.get(Util.random(316, 345));
                ItemTemplate4 item2 = ItemTemplate4.item.get(246);
                int quant1_ = Util.random(1,4);
                int quant2_ = Util.random(0,10);
                //
                Message m = new Message(78);
                m.writer().writeUTF("");
                m.writer().writeByte(3); // size
//                for (int i = 0; i < 3; i++) {
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item1.getIcon()); // icon
                m.writer().writeInt(quant1_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item2.getIcon()); // icon
                m.writer().writeInt(quant2_); // quantity
                m.writer().writeByte(4); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF("");
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                conn.addmsg(m);
                m.cleanup();
                //
                Item47 itbag = new Item47();
                itbag.id = item1.getId();
                itbag.quantity = (short) quant1_;
                itbag.category = 7;
                conn.p.item.add_item_bag47(7, itbag);
                //
                Item47 itbag2 = new Item47();
                itbag2.id = item2.getId();
                itbag2.quantity = (short) quant2_;
                itbag2.category = 4;
                conn.p.item.add_item_bag47(4, itbag2);
                //
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(4);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
//            case 327 :{ // rương vip
//                if (conn.p.item.get_bag_able() < 1) {
//                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
//                    return;
//                }
//                 ItemTemplate7 item1 = ItemTemplate7.item.get(Util.random(319, 323));
//                // ItemTemplate7 item2 = ItemTemplate7.item.get(Util.random(319, 323));
////                 ItemTemplate7 item3 = ItemTemplate7.item.get(Util.random(246, 345));
//                int quant1_ = Util.random(1, 2);
//              //  int quant2_ = Util.random(1, 2);
////                int quant3 = Util.random(1, 2);
//                //
//                Message m = new Message(78);
//                m.writer().writeUTF("Rương V.I.P");
//                m.writer().writeByte(3); // size
////                for (int i = 0; i < 3; i++) {
//                m.writer().writeUTF(""); // name
//                m.writer().writeShort(item1.getIcon()); // icon
//                m.writer().writeInt(quant1_); // quantity
//                m.writer().writeByte(4); // type in bag
//                m.writer().writeByte(0); // tier
//                m.writer().writeByte(0); // color
//                //
////                m.writer().writeUTF(""); // name
////                m.writer().writeShort(item2.getIcon()); // icon
////                m.writer().writeInt(quant2_); // quantity
////                m.writer().writeByte(7); // type in bag
////                m.writer().writeByte(0); // tier
////                m.writer().writeByte(0); // color
//                //
//                m.writer().writeUTF(""); // name
//                m.writer().writeShort(0); // icon
//
//                m.writer().writeByte(4); // type in bag
//                m.writer().writeByte(0); // tier
//                m.writer().writeByte(0); // color
////                }
//                m.writer().writeUTF("");
//                m.writer().writeByte(1);
//                m.writer().writeByte(0);
//                conn.addmsg(m);
//                m.cleanup();
//                //
//                Item47 itbag = new Item47();
//                itbag.id = item1.getId();
//                itbag.quantity = (short) quant1_;
//                itbag.category = 4;
//                conn.p.item.add_item_bag47(4, itbag);
//                //
////                Item47 itbag2 = new Item47();
////                itbag2.id = item2.getId();
////                itbag2.quantity = (short) quant2_;
////                itbag2.category = 4;
////                conn.p.item.add_item_bag47(4, itbag2);
//                //
//
//                //
//                conn.p.item.char_inventory(7);
//                conn.p.item.char_inventory(4);
//                conn.p.item.remove(4, id_potion, 1); // remove item
//                break;
//            }
            case 207: // ruong tim
            case 205: { // ruong do
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                    return;
                }
                List<Short> it_ = new ArrayList<>();
                if (conn.p.level < 30) {
                    it_.addAll(LeaveItemMap.item2x);
                } else if (conn.p.level < 40) {
                    it_.addAll(LeaveItemMap.item3x);
                } else if (conn.p.level < 50) {
                    it_.addAll(LeaveItemMap.item4x);
                } else if (conn.p.level < 60) {
                    it_.addAll(LeaveItemMap.item5x);
                } else if (conn.p.level < 70) {
                    it_.addAll(LeaveItemMap.item6x);
                } else if (conn.p.level < 80) {
                    it_.addAll(LeaveItemMap.item7x);
                } else if (conn.p.level < 90) {
                    it_.addAll(LeaveItemMap.item8x);
                } else if (conn.p.level < 100) {
                    it_.addAll(LeaveItemMap.item9x);
                } else if (conn.p.level < 110) {
                    it_.addAll(LeaveItemMap.item10x);
                } else if (conn.p.level < 120) {
                    it_.addAll(LeaveItemMap.item11x);
                } else if (conn.p.level < 130) {
                    it_.addAll(LeaveItemMap.item12x);
                } else if (conn.p.level < 140) {
                    it_.addAll(LeaveItemMap.item13x);
                }
                if (it_.size() < 1) {
                    Service.send_notice_box(conn, "rương rỗng!");
                    conn.p.item.remove(4, id_potion, 1);
                    return;
                }
                int id_item_can_drop = it_.get(Util.random(it_.size()));
                int dem = 0;
                if (id_potion == 207) {
                    while (dem < 50 && it_.size() > 2 && conn.p.level > 0
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 3)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 4)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 5)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 6)) {
                        id_item_can_drop = it_.get(Util.random(it_.size()));
                        dem++;
                    }
                } else {
                    while (dem < 50 && it_.size() > 2 && conn.p.level > 0
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 2
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 0
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 1
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 8
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 9
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 10
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)
                            && !(ItemTemplate3.item.get(id_item_can_drop).getType() == 11
                            && ItemTemplate3.item.get(id_item_can_drop).getClazz() == conn.p.clazz)) {
                        id_item_can_drop = it_.get(Util.random(it_.size()));
                        dem++;
                    }
                }
                if (dem >= 50) {
                    Service.send_notice_box(conn, "rương rỗng kkk");
                    conn.p.item.remove(4, id_potion, 1);
                    return;
                }
                byte color_ = (byte) Util.random(5);
                byte tier_ = (byte) 0;
                //
                short index_real = 0;
                String name = ItemTemplate3.item.get(id_item_can_drop).getName();
                for (int i = id_item_can_drop - 5; i < id_item_can_drop + 5; i++) {
                    if (ItemTemplate3.item.get(i).getName().equals(name) && ItemTemplate3.item.get(i).getColor() == color_) {
                        index_real = (short) i;
                        break;
                    }
                }
                ItemTemplate3 item = ItemTemplate3.item.get(index_real);
                //
                Message m = new Message(78);
                if (id_potion == 207) {
                    m.writer().writeUTF("Rương tím");
                } else if (id_potion == 205) {
                    m.writer().writeUTF("Rương đỏ");
                }
                m.writer().writeByte(1); // size
                for (int i = 0; i < 1; i++) {
                    m.writer().writeUTF(item.getName()); // name
                    m.writer().writeShort(item.getIcon()); // icon
                    m.writer().writeInt(1); // quantity
                    m.writer().writeByte(3); // type in bag
                    m.writer().writeByte(tier_); // tier
                    m.writer().writeByte(color_); // color
                }
                m.writer().writeUTF("");
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                conn.addmsg(m);
                m.cleanup();
                //
                Item3 itbag = new Item3();
                itbag.id = item.getId();
                itbag.name = item.getName();
                itbag.clazz = item.getClazz();
                itbag.type = item.getType();
                itbag.level = item.getLevel();
                itbag.icon = item.getIcon();
                itbag.op = new ArrayList<>();
                itbag.op.addAll(item.getOp());
                itbag.color = item.getColor();
                itbag.part = item.getPart();
                itbag.tier = tier_;
                itbag.islock = false;
                itbag.time_use = 0;
                conn.p.item.add_item_bag3(itbag);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 206: {
                ItemTemplate7 item1 = ItemTemplate7.item.get(Util.random(8, 12));
                ItemTemplate7 item2 = ItemTemplate7.item.get((50 > Util.random(0, 100)) ? 0 : 3);
                int quant1_ = Util.random(1, 6);
                int quant2_ = Util.random(1, 6);
                int quant3_ = Util.random(300, 500);
                //
                Message m = new Message(78);
                m.writer().writeUTF("Rương vàng");
                m.writer().writeByte(3); // size
//                for (int i = 0; i < 3; i++) {
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item1.getIcon()); // icon
                m.writer().writeInt(quant1_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item2.getIcon()); // icon
                m.writer().writeInt(quant2_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(0); // icon
                m.writer().writeInt(quant3_); // quantity
                m.writer().writeByte(4); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
//                }
                m.writer().writeUTF("");
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                conn.addmsg(m);
                m.cleanup();
                //
                Item47 itbag = new Item47();
                itbag.id = item1.getId();
                itbag.quantity = (short) quant1_;
                itbag.category = 7;
                conn.p.item.add_item_bag47(7, itbag);
                //
                Item47 itbag2 = new Item47();
                itbag2.id = item2.getId();
                itbag2.quantity = (short) quant2_;
                itbag2.category = 7;
                conn.p.item.add_item_bag47(7, itbag2);
                //
                conn.p.update_vang(quant3_);
                Log.gI().add_log(conn.p.name, "Nhận " + quant3_ + " từ rương vàng");
                //
                conn.p.item.char_inventory(7);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
             case 273: {
                ItemTemplate7 item1 = ItemTemplate7.item.get(Util.random(246, 345));
                ItemTemplate7 item2 = ItemTemplate7.item.get((50 > Util.random(0, 100)) ? 0 : 3);
                int quant1_ = Util.random(1, 2);
                int quant2_ = Util.random(1, 6);
                int quant3_ = Util.random(30000, 50000);
                //
                Message m = new Message(78);
                m.writer().writeUTF("Rương boss phe");
                m.writer().writeByte(3); // size
//                for (int i = 0; i < 3; i++) {
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item1.getIcon()); // icon
                m.writer().writeInt(quant1_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item2.getIcon()); // icon
                m.writer().writeInt(quant2_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(0); // icon
                m.writer().writeInt(quant3_); // quantity
                m.writer().writeByte(4); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
//                }
                m.writer().writeUTF("");
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                conn.addmsg(m);
                m.cleanup();
                //
                Item47 itbag = new Item47();
                itbag.id = item1.getId();
                itbag.quantity = (short) quant1_;
                itbag.category = 7;
                conn.p.item.add_item_bag47(7, itbag);
                //
                Item47 itbag2 = new Item47();
                itbag2.id = item2.getId();
                itbag2.quantity = (short) quant2_;
                itbag2.category = 7;
                conn.p.item.add_item_bag47(7, itbag2);
                //
                conn.p.update_vang(quant3_);
                Log.gI().add_log(conn.p.name, "Nhận " + quant3_ + " từ rương boss phe");
                //
                conn.p.item.char_inventory(7);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
              case 274: {
                ItemTemplate7 item1 = ItemTemplate7.item.get(Util.random(417, 456));
                 ItemTemplate7 item2 = ItemTemplate7.item.get(Util.random(457, 463));
                 ItemTemplate7 item3 = ItemTemplate7.item.get(Util.random(246, 345));
                int quant1_ = Util.random(10, 11);
                int quant2_ = Util.random(2, 3);
                int quant3 = Util.random(1, 2);
                //
                Message m = new Message(78);
                m.writer().writeUTF("Rương chiến công");
                m.writer().writeByte(3); // size
//                for (int i = 0; i < 3; i++) {
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item1.getIcon()); // icon
                m.writer().writeInt(quant1_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(item2.getIcon()); // icon
                m.writer().writeInt(quant2_); // quantity
                m.writer().writeByte(7); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
                //
                m.writer().writeUTF(""); // name
                m.writer().writeShort(0); // icon
               
                m.writer().writeByte(4); // type in bag
                m.writer().writeByte(0); // tier
                m.writer().writeByte(0); // color
//                }
                m.writer().writeUTF("");
                m.writer().writeByte(1);
                m.writer().writeByte(0);
                conn.addmsg(m);
                m.cleanup();
                //
                Item47 itbag = new Item47();
                itbag.id = item1.getId();
                itbag.quantity = (short) quant1_;
                itbag.category = 7;
                conn.p.item.add_item_bag47(7, itbag);
                //
                Item47 itbag2 = new Item47();
                itbag2.id = item2.getId();
                itbag2.quantity = (short) quant2_;
                itbag2.category = 7;
                conn.p.item.add_item_bag47(7, itbag2);
                //
               
                //
                conn.p.item.char_inventory(7);
                conn.p.item.char_inventory(4);
                conn.p.item.remove(4, id_potion, 1);
                break;
            }
            case 261: {
                if(!conn.p.isOwner){
                    return;
                }
                if (conn.p.level < 10 || conn.p.level == 20 || conn.p.level == 30 || conn.p.level == 40) {
                    Service.send_notice_nobox_white(conn, "level không phù hợp");
                    return;
                }
                conn.p.item.remove(4, id_potion, 1);
                if (conn.p.getlevelpercent() >= 500) {
                    conn.p.update_Exp(-(Level.entrys.get(conn.p.level - 1).exp / 2), false);
                } else {
                    int levelchange = conn.p.level - 1;
                    long exp_add = (Level.entrys.get(levelchange - 1).exp * (500 + conn.p.getlevelpercent())) / 1000;
                    conn.p.level = (short) (levelchange - 1);
                    conn.p.exp = Level.entrys.get(levelchange - 2).exp - 1;
                    conn.p.tiemnang = (short) (1 + Level.get_tiemnang_by_level(conn.p.level - 1));
                    conn.p.kynang = (short) (1 + Level.get_kynang_by_level(conn.p.level - 1));
                    conn.p.point1 = (short) (4 + conn.p.level);
                    conn.p.point2 = (short) (4 + conn.p.level);
                    conn.p.point3 = (short) (4 + conn.p.level);
                    conn.p.point4 = (short) (4 + conn.p.level);
                    conn.p.skill_point = new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    conn.p.update_Exp(1 + exp_add, false);
                    //
                    Service.send_char_main_in4(conn.p);
                    for (int i = 0; i < conn.p.map.players.size(); i++) {
                        Player p0 = conn.p.map.players.get(i);
                        if (p0.index != conn.p.index && (Math.abs(p0.x - conn.p.x) < 200) && (Math.abs(p0.y - conn.p.y) < 200)) {
                            MapService.send_in4_other_char(p0.map, p0, conn.p);
                        }
                    }
                }
                break;
            }
            case 228:
            case 229:
            case 230:
            case 231:
            case 232:
            case 233:
            case 234: {
                short id_wing_recei = (short) (id_potion + 4414);
                boolean check = true;
                for (int i = 0; i < conn.p.item.bag3.length; i++) {
                    if (conn.p.item.bag3[i] != null && conn.p.item.bag3[i].id == id_wing_recei) {
                        check = false;
                        break;
                    }
                }
                if (conn.p.item.wear[14] != null && conn.p.item.wear[14].id == id_wing_recei) {
                    check = false;
                }
                if (check) {
                    Item3 itbag = new Item3();
                    itbag.id = id_wing_recei;
                    itbag.name = ItemTemplate3.item.get(id_wing_recei).getName();
                    itbag.clazz = ItemTemplate3.item.get(id_wing_recei).getClazz();
                    itbag.type = ItemTemplate3.item.get(id_wing_recei).getType();
                    itbag.level = 60;
                    itbag.icon = ItemTemplate3.item.get(id_wing_recei).getIcon();
                    itbag.op = new ArrayList<>();
                    //
                    itbag.op.add(new Option(7, Util.random(100, 500), itbag.id));
                    itbag.op.add(new Option(8, Util.random(100, 500), itbag.id));
                    itbag.op.add(new Option(9, Util.random(100, 500), itbag.id));
                    itbag.op.add(new Option(10, Util.random(100, 500), itbag.id));
                    itbag.op.add(new Option(11, Util.random(100, 500), itbag.id));
                    //
                    itbag.color = ItemTemplate3.item.get(id_wing_recei).getColor();
                    itbag.part = ItemTemplate3.item.get(id_wing_recei).getPart();
                    itbag.tier = 0;
                    itbag.islock = true;
                    itbag.time_use = 0;
                    conn.p.item.add_item_bag3(itbag);
                } else {
                    Service.send_notice_nobox_white(conn, "Đã có trong hành trang!");
                }
                break;
            }
            case 245 : {
                            Message m = new Message(23);
				m.writer().writeUTF("Túi Hành Trang");
				m.writer().writeByte(3);
				m.writer().writeShort(0);
				conn.addmsg(m);
				m.cleanup();
				break;
                        }
			

            case 253: {
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
                    conn.p.item.remove(4, id_potion, 1);
                    conn.p.item.add_item_bag47((short) 252, (short) 10, (byte) 4);
                    conn.p.item.char_inventory(4);
                }
                break;
            }
            case 195: { // bánh dày
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
//                    try{
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
//                    }catch(Exception e){e.printStackTrace();}
                }
                break;
            }
            case 31: { // bánh trưng
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
//                    try{
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
//                    }catch(Exception e){e.printStackTrace();}
                }
                break;
            }
            case 90:{ // bánh dẻo
                 conn.p.update_Exp(10000L, false);
                 break;
            }
            case 91:{ // bánh nướng
                 conn.p.update_Exp(50000L, false);
                 break;
            }
            case 101: { // rương huyền bí
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 126:{// pháo hoa
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            
            }
            case 193: { // hộp quà lộc
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            }
            case 32: { // bánh trung vip
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                conn.p.update_Exp(5400L, false);
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
//                    try{
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18,28,29,89,5,4));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147,299,230));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 2) { //sách
                            short idsach = (short) Util.random(4577, 4585);
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
                break;
            } 
            case 303: { // đèn hoa đăng
                Service.send_box_input_text(conn, 28, "Thả đèn", new String[]{"Tên bạn thả cùng"});
                break;
            }
            case 305: { // bó sen hồng
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
//                    try{
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(12, 13, 11));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(14, 471, 346, 33));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(294, 275, 52, 18));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(206, 147));
                    for (int i = 0; i < Util.random(1, 4); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 2) { //sách
                            short idsach = (short) Util.random(4577, 4585);
                            ids.add(new box_item_template(idsach, (short) 1, (byte) 3));
                            conn.p.item.add_item_bag3_default(idsach, 0, false);
                        } else if (ran < 5) {//nlmd vang tim
                            short id = (short) Util.random(126, 146);
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 15) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 28) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
//                    }catch(Exception e){e.printStackTrace();}
                }
                break;
            }
            case 307: { // bó sen trắng
//                try{
                if (conn.p.item.get_bag_able() < 3) {
                    Service.send_notice_box(conn, "Cần 3 ô trống trong hành trang!");
                    return;
                }
                if (conn.p.item.total_item_by_id(4, id_potion) > 0) {
                    conn.p.item.remove(4, id_potion, 1);
                    List<box_item_template> ids = new ArrayList<>();

                    List<Integer> it7 = new ArrayList<>(java.util.Arrays.asList(1, 2, 3));
                    List<Integer> it7_vip = new ArrayList<>(java.util.Arrays.asList(12, 8, 9, 10));
                    List<Integer> it4 = new ArrayList<>(java.util.Arrays.asList(48, 49, 50, 51, 18, 10));
                    List<Integer> it4_vip = new ArrayList<>(java.util.Arrays.asList(205, 207, 24, 52, 275, 84));
                    for (int i = 0; i < Util.random(1, 3); i++) {
                        int ran = Util.random(100);
                        if (ran < 0) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(2, 5);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 6) { // nltt
                            short id = (short) Util.random(417, 464);
                            short quant = (short) Util.random(2);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 16) {
                            short id = Util.random(it4_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 30) {
                            short id = Util.random(it7_vip, new ArrayList<>()).shortValue();
                            short quant = (short) 1;
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else if (ran < 45) {
                            short id = Util.random(it4, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        } else if (ran < 70) {
                            short id = Util.random(it7, new ArrayList<>()).shortValue();
                            short quant = (short) Util.random(1, 3);
                            ids.add(new box_item_template(id, quant, (byte) 7));
                            conn.p.item.add_item_bag47(id, quant, (byte) 7);
                        } else {
                            short id = (short) Util.random(new int[]{2, 5});
                            short quant = (short) Util.random(100, 300);
                            ids.add(new box_item_template(id, quant, (byte) 4));
                            conn.p.item.add_item_bag47(id, quant, (byte) 4);
                        }
                    }
//                    conn.p.item.char_inventory(3);
//                    conn.p.item.char_inventory(4);
//                    
//                    conn.p.item.char_inventory(7);
                    Service.Show_open_box_notice_item(conn.p, "Bạn nhận được", ids);
                }
//                }catch(Exception e){e.printStackTrace();}
                break;
            }
            case 219: {
                if (conn.p.squire != null) {
                    if (conn.p.isLiveSquire) {
                        return;
                    }
                    conn.p.item.remove(4, 219, 1);
                    Squire.callSquire(conn);
                } else {
                    Service.send_notice_box(conn, "Chưa co de tu");
                }
                break;
            }
            default: {
                Service.send_notice_nobox_white(conn, "4Chưa có chức năng này");
                break;
            }
        }
    }

    private static void use_item_mount(Session conn, short id) throws IOException {
        conn.p.id_ngua = -1;
        switch (id) {
            case 62:
            case 63:
            case 64:
            case 65:
            case 66: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = (byte) (id - 62);
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 70:
            case 71:
            case 72:
            case 73:
            case 74: {
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                    return;
                }
                conn.p.item.remove(4, id, 1);
                Item47 itbag = new Item47();
                itbag.id = (short) (id - 8);
                itbag.quantity = 99;
                conn.p.item.add_item_bag47(4, itbag);
                break;
            }
            case 124: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.TUAN_LOC;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 125, 223, 247, 272, 276, 295, 282, 280, 300, 324, 270, 302: {
                if (conn.p.item.get_bag_able() < 1) {
                    Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                    return;
                }
                conn.p.item.remove(4, id, 1);
                Item47 itbag = new Item47();
                itbag.id = (short) (id - 1);
                itbag.quantity = 99;
                conn.p.item.add_item_bag47(4, itbag);
                break;
            }
            case 146: {
                conn.p.type_use_mount = Horse.SOI_XAM;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 159: {
                conn.p.type_use_mount = Horse.SOI_GIO_TUYET;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 160: {
                conn.p.type_use_mount = Horse.SOI_BAO_LUA;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 161: {
                conn.p.type_use_mount = Horse.SOI_BONG_MA;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 163: {
                conn.p.type_use_mount = Horse.SU_TU;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 222: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.HEO_RUNG;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 246: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.CON_LAN;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 248: {
                conn.p.type_use_mount = Horse.CON_LAN;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 250: {
                conn.p.type_use_mount = Horse.SKELETON;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 251: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.CHUOT_TUYET;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 69;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 268: {
                conn.p.type_use_mount = Horse.CHUOT_TUYET;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 69;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 271: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.TRAU_RUNG;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 107;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }

            case 275: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.CAN_DAU_VAN;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 111;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 294: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.HOA_KY_LAN;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 116;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 281: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.XE_TRUOT_TUYET;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 115;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 279: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.MA_TOC_DO;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 114;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 315: {
                conn.p.type_use_mount = Horse.HOA_KY_LAN;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 116;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 317: {
                conn.p.type_use_mount = Horse.XE_TRUOT_TUYET;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 115;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 299: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.PHUONG_HOANG_LUA;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 117;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 269: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.VOI_MA_MUT;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 106;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 296: {
                conn.p.type_use_mount = Horse.VOI_MA_MUT;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 106;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 301: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.RONG_BANG;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 121;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 313: {
                conn.p.type_use_mount = Horse.PHUONG_HOANG_LUA;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 117;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 314: {
                conn.p.type_use_mount = Horse.RONG_BANG;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 121;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 316: {
                conn.p.type_use_mount = Horse.MA_TOC_DO;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 114;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 323: {
                conn.p.item.remove(4, id, 1);
                conn.p.type_use_mount = Horse.CA_CHEP;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 145;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 325: {
                conn.p.type_use_mount = Horse.CA_CHEP;
                conn.p.map.send_mount(conn.p);
                conn.p.id_horse = 145;
                MapService.update_in4_2_other_inside(conn.p.map, conn.p);
                Service.send_char_main_in4(conn.p);
                break;
            }
            case 351: {
                conn.p.type_use_mount = Horse.SKELETON;
                conn.p.id_ngua = 200;
                conn.p.map.send_mount(conn.p);
                break;
            }
            case 352: {
                conn.p.type_use_mount = Horse.TUAN_LOC;
                conn.p.id_ngua = 100;
                conn.p.map.send_mount(conn.p);
                break;
            }
        }
    }

    public static void ProcessItem3(Session conn, Message m2) throws IOException {
        byte id = m2.reader().readByte();
        byte index = m2.reader().readByte();
        if (id < 0 || id > (conn.p.maxbag - 1)) {
            return;
        }
        //
        Item3 temp3 = conn.p.item.bag3[id];
        if (temp3 != null) {
            if (temp3.clazz != 4 && temp3.clazz != conn.p.clazz) {
                Service.send_notice_nobox_white(conn, "Class không hợp lệ");
                return;
            }
            if (temp3.level > conn.p.level) {
                Service.send_notice_nobox_white(conn, "Level quá thấp");
                return;
            }
            if (temp3.type == 14) {
                Service.send_notice_nobox_white(conn, "đem đi ấp ở vườn thú đi chứ dùng thế đéo nào đc hả?");
                return;
            }
            if (temp3.time_use > 0) {
                long time_ = temp3.time_use - System.currentTimeMillis();
                time_ /= 60_000;
                Service.send_notice_nobox_white(conn, "Sử dụng sau " + ((time_ > 0) ? time_ : 1) + "p nữa");
                return;
            }
            if (temp3.islock) {
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
                    case 27:
                    case 11:
                    case 28: { // weapon
                        conn.p.player_wear(conn, temp3, id, index);
                        break;
                    }
                    case 55: { // weapon
                        conn.p.player_wear(conn, temp3, id, index);
                        break;
                    }
                    case 102: { // weapon
                        conn.p.player_wear(conn, temp3, id, index);
                        break;
                    }
                    default: {
                        Service.send_notice_nobox_white(conn, "Ấn 2 lần mới có thể sử dụng");
                        break;
                    }
                }
            } else {
                conn.p.id_buffer_126 = id;
                conn.p.id_index_126 = index;
                Service.send_box_input_yesno(conn, 126, "Sử dụng vật phẩm này sẽ khóa, hãy xác nhận!");
            }
        }
    }

    public static void ProcessItem7(Session conn, Message m2) throws IOException {
        short id = m2.reader().readShort();
        //
        if (conn.p.item.total_item_by_id(7, id) > 0) {
            if (id >= 352 && id <= 381) {
                conn.p.id_ngoc_tinh_luyen = id;
                Service.send_box_input_text(conn, 16, "Nhập số lượng", new String[]{"Nhập số lượng"});

            } else if (id >= 498 && id <= 502){
                conn.p.id_ngoc_tinh_luyen = id;
                Service.send_box_input_text(conn,117,"Nhập số lượng", new String[]{"Nhập số lượng"});

            }else {
                switch (id) {
                    case 5:
                    case 6:
                    case 7: {
                        if (conn.p.item.get_bag_able() < 1) {
                            Service.send_notice_nobox_white(conn, "Hành trang đầy!");
                            return;
                        }
                        Item47 it = new Item47();
                        it.id = (short) (id - 4);
                        it.quantity = ItemTemplate7.item.get(id).getValue();
                        conn.p.item.add_item_bag47(7, it);
                        conn.p.item.remove(7, id, 1);
                        break;
                    }
                    default: {
                        Service.send_notice_nobox_white(conn, "7Chưa có chức năng này");
                        break;
                    }
                }
            }
            // conn.p.item.char_inventory(7);
            conn.p.item.char_inventory(4);
            conn.p.item.char_inventory(7);
            conn.p.item.char_inventory(3);
        }
    }
}
