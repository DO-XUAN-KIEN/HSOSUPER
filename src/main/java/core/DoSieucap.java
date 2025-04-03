package core;

import Helps.ItemStar;
import Helps.medal;
import History.His_COIN;
import io.Message;
import io.Session;
import template.*;

import java.io.IOException;
import java.util.List;

public class DoSieucap {

    public static void nangdothan(Session conn, Message m) {
        try {
            if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                Service.send_notice_box(conn, "Chậm thôi!");
                return;
            }
            conn.p.time_speed_rebuild = System.currentTimeMillis() + 2000;
            byte type = m.reader().readByte();
            short id = -1;
            byte tem = -1;
            try {
                id = m.reader().readShort();
                tem = m.reader().readByte();
            } catch (IOException e) {
            }
            switch (type) {
                case 0: {
                    try {
                        if (tem != 3) {
                            Service.send_notice_box(conn, "Trang bị không phù hợp!");
                            return;
                        }
                        if (id >= conn.p.item.bag3.length) {
                            return;
                        }
                        Item3 temp = conn.p.item.bag3[id];
                        if (!((temp.id >= 4831 && temp.id <= 4873) || temp.id == 4878 || (temp.id >= 4880 && temp.id <= 4886))) {
                            Service.send_notice_box(conn, "Trang bị không phù hợp!");
                            return;
                        }
                        if (temp.tierStar >= 15){
                            Service.send_notice_box(conn, "Trang bị đã đạt cấp tối đa!");
                            return;
                        }
                        if (temp.tierStar < 15) {
                            conn.p.TypeItemStarCreate = ItemStar.ConvertType(temp.type);
                            Message m_send = new Message(-105);
                            m_send.writer().writeByte(4);
                            m_send.writer().writeByte(5);
                            m_send.writer().writeShort(0);
                            conn.addmsg(m_send);
                            m_send.cleanup();
                        } else {
                            Service.send_notice_box(conn, "Lỗi hãy thử lại!");
                        }
                        conn.p.time_speed_rebuild = 0;
                    } catch (Exception e) {
                        System.out.println("Lỗi: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                }
                case 4: {//nâng cấp tbtt
                    if (id >= conn.p.item.bag3.length || id < 0) {
                        return;
                    }
                    Item3 temp = conn.p.item.bag3[id];
                    if (temp == null && !((temp.id >= 4831 && temp.id <= 4873) || temp.id == 4878 || (temp.id >= 4880 && temp.id <= 4886)) && temp.color != 5 && temp.tierStar >= 15) {
                        Service.send_notice_box(conn, "Trang bị không phù hợp!");
                        return;
                    }
                    if (temp != null && ((temp.id >= 4831 && temp.id <= 4873) || temp.id == 4878 || (temp.id >= 4880 && temp.id <= 4886)) && temp.color == 5 && temp.tierStar <= 15) {
                        conn.p.id_Upgrade_Medal_Star = id;
                        Upgrade_dothan(conn, tem);
                    } else {
                        Service.send_notice_box(conn, "Trang bị không phù hợp hoặc đã đạt cấp tối đa!");
                        return;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void Upgrade_dothan(Session conn, byte index) throws IOException { // nâng cấp tt
        try {
            int id = conn.p.id_Upgrade_Medal_Star;
            if (id >= conn.p.item.bag3.length || id < 0) {
                return;
            }
            Item3 temp = conn.p.item.bag3[id];
            if (temp == null && !((temp.id >= 4831 && temp.id <= 4873) || temp.id == 4878 || (temp.id >= 4880 && temp.id <= 4886)) && temp.color != 5 && temp.tierStar >= 15) {
                Service.send_notice_box(conn, "Trang bị không phù hợp!");
                return;
            }
            if (temp.tierStar >= 15){
                Service.send_notice_box(conn, "Trang bị đã đạt cấp tối đa!");
                return;
            }
            if (conn.p.get_vang() < 10_000_000 || conn.p.get_ngoc() < 10_000 || conn.p.checkcoin() < 100_000){
                Service.send_notice_box(conn,"Không đủ 10.000.000 vàng, 10.000 ngọc và 100.000 coin");
                return;
            } else {
                conn.p.update_vang(-10_000_000);
                conn.p.update_ngoc(-10_000);
                conn.p.update_coin(-100_000);
            }
            conn.p.update_vang(-10_000_000);
            conn.p.update_ngoc(-10_000);
            conn.p.update_coin(-100_000);
            His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
            hisc.coin_change = 100_000;
            hisc.coin_last = conn.p.checkcoin();
            hisc.Logger = "(TRỪ COIN) từ nâng do1";
            hisc.Flus();
            List<Option> ops = ItemStar.GetOpsItemStarUpgrade(temp.clazz, temp.type, temp.id, temp.tierStar + 1, temp.op);
            if ((ops == null || ops.isEmpty()) && (temp.id >= 4831 && temp.id <= 4873)) {
                Service.send_notice_box(conn, "Lỗi không tìm thấy chỉ số, hãy chụp lại chỉ số và báo ngay cho ad \"Nhắn riêng\"");
                return;
            }
            temp.tierStar++;
            temp.UpdateName();
            conn.p.setnldothan();
            for (int i = 0; i < temp.op.size(); i++) {
                Option op = temp.op.get(i);
                if (op.id >= 0 && op.id <= 99) {
                    if (op.id >= 0 && op.id <= 7) {
                        op.setParam(op.getParam(0)+ 50);
                    } else if (op.id == 14 || op.id == 15){
                        op.setParam(op.getParam(0)+1);
                    } else if (op.id == 37 || op.id == 38) {
                        op.setParam(3);
                    } else {
                        op.setParam(op.getParam(1));
                    }
                }
                if (op != null && op.id >= -128 && op.id <= -80 || (op.id == 99)) {
                    op.setParam(op.getParam(0) + 100);
                }
            }
            conn.p.item.char_inventory(4);
            conn.p.item.char_inventory(7);
            conn.p.item.char_inventory(3);
            Message m = new Message(-105);
            m.writer().writeByte(3);
            m.writer().writeByte(3);
            m.writer().writeUTF("Thành công!");
            m.writer().writeByte(3);
            m.writer().writeUTF(temp.name);
            m.writer().writeByte(temp.clazz);
            m.writer().writeShort(temp.id);
            m.writer().writeByte(temp.type);
            m.writer().writeShort(temp.icon);
            m.writer().writeByte(temp.tier); // tier
            m.writer().writeShort(1); // level required
            m.writer().writeByte(temp.color); // color
            m.writer().writeByte(0); // can sell
            m.writer().writeByte(0); // can trade
            m.writer().writeByte(temp.op.size());
            for (int i = 0; i < temp.op.size(); i++) {
                m.writer().writeByte(temp.op.get(i).id);
                m.writer().writeInt(temp.op.get(i).getParam(0));

            }
            m.writer().writeInt(0); // time use
            m.writer().writeByte(0);
            m.writer().writeByte(0);
            m.writer().writeByte(0);
            conn.addmsg(m);
            m.cleanup();
            if (temp.tierStar < 15) {
                m = new Message(-105);
                m.writer().writeByte(5);
                m.writer().writeByte(3);
                m.writer().writeUTF("Thành công, xin chúc mừng :)");
                m.writer().writeShort(id);
                conn.addmsg(m);
                m.cleanup();
            }
        } catch (Exception eee) {
            System.out.println("Lỗi: " + eee.getMessage());
            eee.printStackTrace();
        }
    }

    public static void nangmdthan(Session conn, Message m) {
        try {
            if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                Service.send_notice_box(conn, "Chậm thôi!");
                return;
            }
            conn.p.time_speed_rebuild = System.currentTimeMillis() + 1000;
            byte type = m.reader().readByte();
            short id = -1;
            byte cat = -1;
            try {
                id = m.reader().readShort();
                cat = m.reader().readByte();
            } catch (IOException e) {
            }
            switch (type) {
                case 0: {
                    if (cat != 3) {
                        Service.send_notice_box(conn, "Trang bị không phù hợp!");
                        return;
                    }
                    if (id >= conn.p.item.bag3.length) {
                        return;
                    }
                    Item3 item = conn.p.item.bag3[id];
                    if (item != null) {
                        if (item.type == 16) {
                            if (item.tier < 100) {
                                Message m_send = new Message(-105);
                                m_send.writer().writeByte(4);
                                m_send.writer().writeByte(5);
                                conn.addmsg(m_send);
                                m_send.cleanup();
                            } else {
                                Service.send_notice_box(conn, "Trang bị đã nâng tối đa");
                            }
                        } else {
                            Service.send_notice_box(conn, "Vật phẩm không phù hợp");
                        }
                    } else {
                        Service.send_notice_box(conn, "Vui lòng chọn Mề Đay để Nâng Cấp");
                    }
                    break;
                }
                case 4: {
                    if (cat != 3) {
                        return;
                    }
                    if (id >= conn.p.item.bag3.length) {
                        return;
                    }
                    Item3 item = conn.p.item.bag3[id];
                    if (item.tier >= 100){
                        Service.send_notice_box(conn, "Trang bị đã đạt cấp tối đa!");
                        return;
                    }

                    long vang;
                    int ngoc;
                    int coin;
                    if (item.tier <= 15){
                        vang = 100_000;
                        ngoc = 100;
                        coin = 1_000;
                    }else if (item.tier <= 30){
                        vang = 1_000_000;
                        ngoc = 1_000;
                        coin = 10_000;
                    }else if (item.tier <= 50){
                        vang = 10_000_000;
                        ngoc = 10_000;
                        coin = 100_000;
                    }else if (item.tier <= 80){
                        vang = 100_000_000;
                        ngoc = 100_000;
                        coin = 1_000_000;
                    }else {
                        vang = 1_000_000_000;
                        ngoc = 1_000_000;
                        coin = 10_000_000;
                    }
                    if (conn.p.get_vang() < vang || conn.p.get_ngoc() < ngoc || conn.p.checkcoin() < coin){
                        if (conn.p.get_vang() < vang){
                            Service.send_notice_box(conn, "Không đủ vàng");
                        }else if (conn.p.get_ngoc() < ngoc){
                            Service.send_notice_box(conn, "Không đủ ngọc");
                        }else {
                            Service.send_notice_box(conn,"Không đủ coin");
                        }
                        return;
                    }else {
                        conn.p.update_vang(-vang);
                        conn.p.update_ngoc(-ngoc);
                        conn.p.update_coin(-coin);
                    }
                    if (item != null && item.type == 16 && item.tier < 100) {
                        His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                        hisc.coin_change = coin;
                        hisc.coin_last = conn.p.checkcoin();
                        hisc.Logger = "(TRỪ COIN) từ nâng do2";
                        hisc.Flus();


                        item = medal.Upgare_Medal(item);
                        item.color = 5;
                        item.tier++;
                        item.UpdateName();


                        m.cleanup();
                        m = new Message(-105);
                        m.writer().writeByte(3);
                        m.writer().writeByte(3);
                        m.writer().writeUTF("Thành công!");
                        // Record information about the upgraded equipment
                        ItemTemplate3 temp = ItemTemplate3.item.get(item.id);
                        m.writer().writeByte(3);
                        m.writer().writeUTF(item.name);
                        m.writer().writeByte(temp.getClazz());
                        m.writer().writeShort(temp.getId());
                        m.writer().writeByte(temp.getType());
                        m.writer().writeShort(temp.getIcon());
                        m.writer().writeByte(item.tier); // tier
                        m.writer().writeShort(1); // level required
                        m.writer().writeByte(item.color); // color
                        m.writer().writeByte(0); // can sell
                        m.writer().writeByte(0); // can trade
                        m.writer().writeByte(item.op.size());
                        for (int i = 0; i < item.op.size(); i++) {
                            m.writer().writeByte(item.op.get(i).id);
                            m.writer().writeInt(item.op.get(i).getParam(item.tier));
                        }
                        m.writer().writeInt(0); // time use
                        m.writer().writeByte(0);
                        m.writer().writeByte(0);
                        m.writer().writeByte(0);
                        conn.addmsg(m);
                        m.cleanup();

                        // Update character inventory
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);
                        conn.p.item.char_inventory(3);

                        // Send upgrade result notice
                        m = new Message(-105);
                        m.writer().writeByte(5);
                        m.writer().writeByte(3);
                        m.writer().writeUTF("Thành công!");
                        m.writer().writeShort(id);
                        conn.addmsg(m);
                        m.cleanup();
                    } else {
                        Service.send_notice_box(conn, "Trang Bị đã đạt cấp tối đa!");
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void nangtb2(Session conn, Message m) {
        try {
            if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                Service.send_notice_box(conn, "Chậm thôi!");
                return;
            }
            conn.p.time_speed_rebuild = System.currentTimeMillis() + 1000;
            byte type = m.reader().readByte();
            short id = -1;
            byte cat = -1;
            try {
                id = m.reader().readShort();
                cat = m.reader().readByte();
            } catch (IOException e) {
            }
            switch (type) {
                case 0: {
                    if (cat != 3) {
                        Service.send_notice_box(conn, "Trang bị không phù hợp!");
                        return;
                    }
                    if (id >= conn.p.item.bag3.length) {
                        return;
                    }
                    Item3 item = conn.p.item.bag3[id];
                    if (item != null) {
                        if ((item.type >= 21 && item.type <= 28) || item.type == 55 || item.type == 102) {
                            if (item.tier < 100) {
                                Message m_send = new Message(-105);
                                m_send.writer().writeByte(4);
                                m_send.writer().writeByte(5);
                                m_send.writer().writeShort(493);
                                if (conn.version >= 270) {
                                    m_send.writer().writeShort(1);
                                } else {
                                    m_send.writer().writeByte(1);
                                }
                                conn.addmsg(m_send);
                                m_send.cleanup();
                            } else {
                                Service.send_notice_box(conn, "Trang bị đã nâng tối đa");
                            }
                        } else {
                            Service.send_notice_box(conn, "Vật phẩm không phù hợp");
                        }
                    } else {
                        Service.send_notice_box(conn, "Vui lòng chọn Trang bị 2 để Nâng Cấp");
                    }
                    break;
                }
                case 4: {
                    if (cat != 3) {
                        return;
                    }
                    if (id >= conn.p.item.bag3.length) {
                        return;
                    }
                    Item3 item = conn.p.item.bag3[id];
                    long vang_req = 10000000;
                    int ngoc_req = 10000;
                    int coin_req = 100000;
                    if (conn.p.get_vang() < vang_req){
                        Service.send_notice_box(conn,"Không đủ "+vang_req+ "vàng");
                        return;
                    }
                    if (conn.p.get_ngoc() < ngoc_req){
                        Service.send_notice_box(conn,"Không đủ "+ngoc_req+ "ngọc");
                        return;
                    }
                    if (conn.p.checkcoin()< coin_req){
                        Service.send_notice_box(conn,"Không đủ" + coin_req + "coin");
                        return;
                    }
                    if (item != null && ((item.type >= 21 && item.type <= 28) || item.type == 55 || item.type == 102) && item.tier < 100) {
                        for (int i = 0; i < 5; i++) {
                            if (i < conn.p.NLtb2.length) {
                                if (conn.p.item.total_item_by_id(7, conn.p.NLtb2[i]) < 1 && (conn.ac_admin < 4 || !Manager.BuffAdmin)) {
                                    Service.send_notice_box(conn, "Không đủ " + ItemTemplate7.item.get(conn.p.NLtb2[i]).getName() + "!");
                                    return;
                                }
                            }
                        }
                        for (int i = 0; i < 5; i++) {
                            if (i < conn.p.NLtb2.length) {
                                conn.p.item.remove(7, conn.p.NLtb2[i], 1);
                            }
                        }
                        conn.p.update_vang(-vang_req);
                        conn.p.update_ngoc(-ngoc_req);
                        conn.p.update_coin(-coin_req);
                        His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                        hisc.coin_change = coin_req;
                        hisc.coin_last = conn.p.checkcoin();
                        hisc.Logger = "(TRỪ COIN) từ nâng do3";
                        hisc.Flus();
                        //Log.gI().add_log(conn.p.name, "trừ "+coin_req+" coin từ nâng do3");

                        item.tier++;
                        item.islock = true;
                        item.UpdateName();
                        conn.p.setnltb2();
                        for (int i = 0; i < item.op.size(); i++) {
                            Option op = item.op.get(i);
                            if (op.id >= 0 && op.id < 7) {
                                op.setParam(op.getParam(0) + 150);
                            }else if (op.id >= 8 && op.id <= 13) {
                                op.setParam((int) (op.getParam(0) * 1.005));
                            }else if (op.id >= 23 && op.id <= 26) {
                                op.setParam(op.getParam(0));
                            }else if (op.id >= 27 && op.id <= 36) {
                                op.setParam((int) (op.getParam(0) * 1.002));
                            } else if (op.id >= 39 && op.id <= 99 && op.id != 37 && op.id != 38) {
                                op.setParam(op.getParam(0) + 1);
                            }
                        }
                        m.cleanup();
                        m = new Message(-105);
                        m.writer().writeByte(3);
                        m.writer().writeByte(3);
                        m.writer().writeUTF("Thành công!");
                        ItemTemplate3 temp = ItemTemplate3.item.get(item.id);
                        m.writer().writeByte(3);
                        m.writer().writeUTF(item.name);
                        m.writer().writeByte(temp.getClazz());
                        m.writer().writeShort(temp.getId());
                        m.writer().writeByte(temp.getType());
                        m.writer().writeShort(temp.getIcon());
                        m.writer().writeByte(item.tier); // tier
                        m.writer().writeShort(1); // level required
                        m.writer().writeByte(item.color); // color
                        m.writer().writeByte(0); // can sell
                        m.writer().writeByte(0); // can trade
                        m.writer().writeByte(item.op.size());
                        for (int i = 0; i < item.op.size(); i++) {
                            m.writer().writeByte(item.op.get(i).id);
                            m.writer().writeInt(item.op.get(i).getParam(item.tier));
                        }
                        m.writer().writeInt(0); // time use
                        m.writer().writeByte(0);
                        m.writer().writeByte(0);
                        m.writer().writeByte(0);
                        conn.addmsg(m);
                        m.cleanup();
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);
                        conn.p.item.char_inventory(3);
                        m = new Message(-105);
                        m.writer().writeByte(5);
                        m.writer().writeByte(3);
                        m.writer().writeUTF("Thành công!");
                        m.writer().writeShort(id);
                        conn.addmsg(m);
                        m.cleanup();
                    } else {
                        Service.send_notice_box(conn, "Trang Bị đã đạt cấp tối đa!");
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void nangtb1(Session conn, Message m) {
        try {
            if (conn.p.time_speed_rebuild > System.currentTimeMillis()) {
                Service.send_notice_box(conn, "Chậm thôi!");
                return;
            }
            conn.p.time_speed_rebuild = System.currentTimeMillis() + 1000;
            byte type = m.reader().readByte();
            short id = -1;
            byte cat = -1;
            try {
                id = m.reader().readShort();
                cat = m.reader().readByte();
            } catch (IOException e) {
            }
            switch (type) {
                case 0: {
                    if (cat != 3) {
                        Service.send_notice_box(conn, "Trang bị không phù hợp!");
                        return;
                    }
                    if (id >= conn.p.item.bag3.length) {
                        return;
                    }
                    Item3 item = conn.p.item.bag3[id];
                    if (item.type == 15 || item.type == 7){
                        Service.send_notice_box(conn,"Không thể cường hóa cánh và giáp");
                        return;
                    }
                    if(item.tier >= 100) {
                        Service.send_notice_box(conn,"Bạn đã nâng cấp trang bị tối đa");
                        return;
                    }
                    if ((item.id >= 4656 && item.id <= 4675) && item.tierStar < 9){
                        Service.send_notice_box(conn, "Phải nâng cấp lên cấp 9 đã");
                        return;
                    }
                    if (item != null) {
                        if (item.type >= 0 && item.type <= 11) {
                            if (item.tier < 100) {
                                Message m_send = new Message(-105);
                                m_send.writer().writeByte(4);
                                m_send.writer().writeByte(5);
                                conn.addmsg(m_send);
                                m_send.cleanup();
                            } else {
                                Service.send_notice_box(conn, "Trang bị đã nâng tối đa");
                            }
                        } else {
                            Service.send_notice_box(conn, "Vật phẩm không phù hợp");
                        }
                    } else {
                        Service.send_notice_box(conn, "Vui lòng chọn Trang bị 1 để Nâng Cấp");
                    }
                    break;
                }
                case 4: {
                    if (cat != 3) {
                        return;
                    }
                    if (id >= conn.p.item.bag3.length) {
                        return;
                    }
                    Item3 item = conn.p.item.bag3[id];
                    if(item.tier >= 100) {
                        Service.send_notice_box(conn,"Bạn đã nâng cấp trang bị tối đa");
                        return;
                    }
                    if (item.type == 15 || item.type == 7){
                        Service.send_notice_box(conn,"Không thể cường hóa cánh và giáp");
                        return;
                    }
                    if ((item.id >= 4656 && item.id <= 4675) && item.tierStar < 9){
                        Service.send_notice_box(conn, "Phải nâng cấp lên cấp 9 đã");
                        return;
                    }
                    long vang;
                    int ngoc;
                    int coin;
                    if (item.tier <= 15){
                        vang = 100_000;//1tr5
                        ngoc = 100;//1k5
                        coin = 1_000;//15k
                    }else if (item.tier <= 30){
                        vang = 1_000_000;//15tr
                        ngoc = 1_000;//15k
                        coin = 10_000;//150k
                    }else if (item.tier <= 50){
                        vang = 10_000_000;// 300tr
                        ngoc = 10_000;// 300k
                        coin = 100_000;// 3tr
                    }else if (item.tier <= 80){
                        vang = 100_000_000;
                        ngoc = 100_000;
                        coin = 1_000_000;
                    }else {
                        vang = 1_000_000_000;
                        ngoc = 1_000_000;
                        coin = 10_000_000;
                    }

                    if (conn.p.get_vang() < vang || conn.p.get_ngoc() < ngoc || conn.p.checkcoin() < coin){
                        if (conn.p.get_vang() < vang){
                            Service.send_notice_box(conn, "Không đủ vàng");
                        }else if (conn.p.get_ngoc() < ngoc){
                            Service.send_notice_box(conn, "Không đủ ngọc");
                        }else {
                            Service.send_notice_box(conn,"Không đủ coin");
                        }
                        return;
                    }else {
                        conn.p.update_vang(-vang);
                        conn.p.update_ngoc(-ngoc);
                        conn.p.update_coin(-coin);
                    }
                    His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                    hisc.coin_change = (int) coin;
                    hisc.coin_last = conn.p.checkcoin();
                    hisc.Logger = "(TRỪ COIN) từ nâng tb1";
                    hisc.Flus();
                    if (item != null && (item.type >= 0 && item.type <= 11) && item.tier < 100) {
                        item.tier++;
                        item.color = 5;
                        item.islock = true;
                        item.UpdateName();
                        for (int i = 0; i < item.op.size(); i++) {
                            Option op = item.op.get(i);
                            if (op.id >= 0 && op.id <= 99 && op.id != 37 && op.id != 38) {
                                if (op.id >= 0 && op.id < 7) {
                                    op.setParam(op.getParam(0) + 300);
                                }else if (op.id >= 7 && op.id <= 13) {
                                    op.setParam((int) (op.getParam(0) * 1.005));
                                }else if (op.id >= 23 && op.id <= 26) {
                                    op.setParam(op.getParam(0)+1);
                                }else if (op.id >= 27 && op.id <= 36) {
                                    op.setParam((int) (op.getParam(0) * 1.002));
                                } else if (op.id == 14 || op.id == 15){
                                    op.setParam(op.getParam(0) + 1);
                                } else if (!(op.id >= 33 && op.id <= 36)) {
                                    op.setParam(op.getParam(0)+ 10);
                                } else {
                                    op.setParam(op.getParam(0));
                                }
                            }
                        }
                        m.cleanup();
                        m = new Message(-105);
                        m.writer().writeByte(3);
                        m.writer().writeByte(3);
                        m.writer().writeUTF("Thành công!");
                        ItemTemplate3 temp = ItemTemplate3.item.get(item.id);
                        m.writer().writeByte(3);
                        m.writer().writeUTF(item.name);
                        m.writer().writeByte(temp.getClazz());
                        m.writer().writeShort(temp.getId());
                        m.writer().writeByte(temp.getType());
                        m.writer().writeShort(temp.getIcon());
                        m.writer().writeByte(item.tier); // tier
                        m.writer().writeShort(1); // level required
                        m.writer().writeByte(item.color); // color
                        m.writer().writeByte(0); // can sell
                        m.writer().writeByte(0); // can trade
                        m.writer().writeByte(item.op.size());
                        for (int i = 0; i < item.op.size(); i++) {
                            m.writer().writeByte(item.op.get(i).id);
                            m.writer().writeInt(item.op.get(i).getParam(item.tier));
                        }
                        m.writer().writeInt(0); // time use
                        m.writer().writeByte(0);
                        m.writer().writeByte(0);
                        m.writer().writeByte(0);
                        conn.addmsg(m);
                        m.cleanup();
                        conn.p.item.char_inventory(4);
                        conn.p.item.char_inventory(7);
                        conn.p.item.char_inventory(3);
                        m = new Message(-105);
                        m.writer().writeByte(5);
                        m.writer().writeByte(3);
                        m.writer().writeUTF("Thành công!");
                        m.writer().writeShort(id);
                        conn.addmsg(m);
                        m.cleanup();
                    } else {
                        Service.send_notice_box(conn, "Trang Bị đã đạt cấp tối đa!");
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}