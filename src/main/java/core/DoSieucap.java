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
                            int[] values = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
                            int tierStar = temp.tierStar >= 0 && temp.tierStar < values.length ? values[temp.tierStar] : temp.tierStar;
                            for (int i = conn.p.TypeItemStarCreate * 5; i < conn.p.TypeItemStarCreate * 5 + 5; i++) {
                                m_send.writer().writeShort(conn.p.NLdothan[i]); // id ở player
                                if (conn.version >= 270) {
                                    m_send.writer().writeShort(tierStar);
                                } else {
                                    m_send.writer().writeByte(tierStar);
                                }
                            }
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

    public static short[] ti_le_nang_do = new short[]{1000, 800, 600, 500, 400, 300, 200, 100, 80, 70, 50, 30, 20, 1, 1};

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
            int[] values = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
            int tierStar = temp.tierStar >= 0 && temp.tierStar < values.length ? values[temp.tierStar] : temp.tierStar;
            for (int i = conn.p.TypeItemStarCreate * 5; i < conn.p.TypeItemStarCreate * 5 + 5; i++) {
                if (conn.p.item.total_item_by_id(7, conn.p.NLdothan[i]) < tierStar && conn.ac_admin < 20) {
                    Service.send_notice_box(conn, "Thiếu " + ItemTemplate7.item.get(conn.p.NLdothan[i]).getName());
                    return;
                }
            }
            if (temp.tierStar >= ti_le_nang_do.length) {
                Service.send_notice_box(conn, "Trang bị đã đạt cấp tối đa!");
                return;
            }
            long vang_req = (temp.tierStar + 1) * 10_000_000;
            int coin_req = (temp.tierStar + 1) * 20_000;
            if (conn.p.get_vang() < vang_req){
                Service.send_notice_box(conn,"Không đủ "+vang_req+ "vàng");
                return;
            }
            if (conn.p.checkcoin()< coin_req){
                Service.send_notice_box(conn,"Không đủ" + coin_req + "coin");
                return;
            }
            conn.p.update_vang(-vang_req);
            conn.p.update_coin(-coin_req);
            His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
            hisc.coin_change = coin_req;
            hisc.coin_last = conn.p.checkcoin();
            hisc.Logger = "(TRỪ COIN) từ nâng do1";
            hisc.Flus();
            //Log.gI().add_log(conn.p.name, "trừ "+coin_req+" coin từ nâng do1");
            boolean suc;
            if (temp.tierStar < 10) {
                suc = Util.random(15111) < ti_le_nang_do[temp.tierStar] || (conn.ac_admin > 111 && Manager.BuffAdmin) || conn.p.mm_tt >= 70;
            }else {
                suc = Util.random(22222) < ti_le_nang_do[temp.tierStar] || (conn.ac_admin > 111 && Manager.BuffAdmin) || conn.p.mm_tt >= 100;
            }
            if (suc) {
                List<Option> ops = ItemStar.GetOpsItemStarUpgrade(temp.clazz, temp.type, temp.id, temp.tierStar + 1, temp.op);
                if ((ops == null || ops.isEmpty()) && (temp.id >= 4831 && temp.id <= 4873)) {
                    Service.send_notice_box(conn, "Lỗi không tìm thấy chỉ số, hãy chụp lại chỉ số và báo ngay cho ad \"Nhắn riêng\"");
                    return;
                }
                conn.p.mm_tt = 0;
                temp.tierStar++;
                temp.UpdateName();
                conn.p.setnldothan();
                for (int i = 0; i < temp.op.size(); i++) {
                    Option op = temp.op.get(i);
                    if (op.id >= 0 && op.id <= 99) {
                        if (op.id >= 0 && op.id <= 7){
                            op.setParam(op.getParam(1));
                        } else if (op.id == 37 || op.id == 38) {
                            op.setParam(3);
                        } else {
                            op.setParam(op.getParam(4));
                        }
                    }
                    if (op != null && op.id >= -128 && op.id <= -80 || (op.id == 99)) {
                        op.setParam(op.getParam(0) + 100);
                    }
                }
            } else {
                if (index == 0) {
                    for (Option o : temp.op) {
                        if (o.id == (byte) 129) {
                            o.param -= 100;
                        }
                    }
                }
            }
            // xóa nl
            for (int i = conn.p.TypeItemStarCreate * 5; i < conn.p.TypeItemStarCreate * 5 + 5; i++) {
                conn.p.item.remove(7, conn.p.NLdothan[i], tierStar);
            }
//            if (suc == true) {
//                conn.p.item.remove(7, 471, 5);
//            } else {
//                conn.p.item.remove(7, 471, 2);
//            }
            if (suc && (temp.tierStar + 1) % 3 == 0) {
                conn.p.ChangeNL_dothan(conn.p.TypeItemStarCreate);
            }
            conn.p.item.char_inventory(4);
            conn.p.item.char_inventory(7);
            conn.p.item.char_inventory(3);
            Message m = new Message(-105);
            m.writer().writeByte(3);
            if (suc) {
                m.writer().writeByte(3);
                m.writer().writeUTF("Thành công!");
            } else {
                m.writer().writeByte(4);
                m.writer().writeUTF("Thất bại!");
            }
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
                if (suc) {
                    m.writer().writeByte(3);
                    m.writer().writeUTF("Thành công, xin chúc mừng :)");
                } else {
                    conn.p.mm_tt += 1;
                    m.writer().writeByte(4);
                    m.writer().writeUTF("Thất bại rồi :(");
                }
                m.writer().writeShort(id);
                conn.addmsg(m);
                m.cleanup();
            }
        } catch (Exception eee) {
            System.out.println("Lỗi: " + eee.getMessage());
            eee.printStackTrace();
        }
    }

    public static short[] ti_le_nang_md = new short[]{20000, 800, 600, 500, 400, 300, 200, 100, 80, 70, 50, 30, 20, 1, 1,1};
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
                    if (item == null || item.tier < 15 || item.color < 4) {
                        Service.send_notice_box(conn, "Mề Đay Cam + 15 trở Lên!");
                        return;
                    }
                    if (item != null) {
                        if (item.type == 16) {
                            if (item.tier < 30) {
                                Message m_send = new Message(-105);
                                m_send.writer().writeByte(4);
                                m_send.writer().writeByte(5);
                                int[] values = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
                                int tier = item.tierStar >= 0 && item.tierStar < values.length ? values[item.tierStar] : item.tierStar;
                                for (short nl : conn.p.NLdothan) {
                                    m_send.writer().writeShort(nl);
                                    if (conn.version >= 270) {
                                        m_send.writer().writeShort(tier);
                                    } else {
                                        m_send.writer().writeByte(tier);
                                    }
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

                    if (item == null || item.tier < 15 || item.color < 4) {
                        Service.send_notice_box(conn, "Mề Đay Cam + 15 trở Lên!");
                        return;
                    }
                    if (item.tierStar >= 15){
                        Service.send_notice_box(conn, "Trang bị đã đạt cấp tối đa!");
                        return;
                    }
                    long vang_req = (item.tierStar + 1) * 10_000_000;
                    int coin_req = (item.tierStar + 1) * 20_000;
                    if (conn.p.get_vang() < vang_req){
                        Service.send_notice_box(conn,"Không đủ "+vang_req+ "vàng");
                        return;
                    }
                    if (conn.p.checkcoin()< coin_req){
                        Service.send_notice_box(conn,"Không đủ" + coin_req + "coin");
                        return;
                    }
                    if (item != null && item.type == 16 && item.tier < 30) {
                        int[] values = {10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40};
                        int tier = item.tierStar >= 0 && item.tierStar < values.length ? values[item.tierStar] : item.tierStar;
                        for (int i = 0; i < 5; i++) {
                            if (i < conn.p.NLdothan.length) {
                                if (conn.p.item.total_item_by_id(7, conn.p.NLdothan[i]) < tier && (conn.ac_admin < 4 || !Manager.BuffAdmin)) {
                                    Service.send_notice_box(conn, "Không đủ " + ItemTemplate7.item.get(conn.p.NLdothan[i]).getName() + "!");
                                    return;
                                }
                            }
                        }
                        for (int i = 0; i < 5; i++) {
                            if (i < conn.p.NLdothan.length) {
                                conn.p.item.remove(7, conn.p.NLdothan[i], tier);
                            }
                        }
                        conn.p.update_coin(-coin_req);
                        conn.p.update_vang(-vang_req);
                        His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                        hisc.coin_change = coin_req;
                        hisc.coin_last = conn.p.checkcoin();
                        hisc.Logger = "(TRỪ COIN) từ nâng do2";
                        hisc.Flus();
                        //Log.gI().add_log(conn.p.name, "trừ "+coin_req+" coin từ nâng do2");
                        boolean suc;
                        if (item.tierStar < 10) {
                            suc = Util.random(15111) < ti_le_nang_md[item.tier - 15] || (conn.ac_admin > 111 && Manager.BuffAdmin) || conn.p.mm_md >= 70;
                        }else {
                            suc = Util.random(22222) < ti_le_nang_md[item.tier - 15] || (conn.ac_admin > 111 && Manager.BuffAdmin) || conn.p.mm_md >= 100;
                        }
                        if (suc) {
                            conn.p.mm_md = 0;
                            item = medal.Upgare_Medal(item);
                            item.color = 5;
                            item.tier++;
                            item.tierStar++;
                            item.icon = 13218;
                            item.UpdateName();
                            conn.p.setnldothan();
                            if(item.tier == 16){
                                item.op.add(new Option(96, 20, item.id));
                            }
                        }
                        m.cleanup();
                        m = new Message(-105);
                        m.writer().writeByte(3);
                        if (suc) {
                            m.writer().writeByte(3);
                            m.writer().writeUTF("Thành công!");
                        } else {
                            m.writer().writeByte(4);
                            m.writer().writeUTF("Thất bại!");
                        }
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
                        if (suc) {
                            m.writer().writeByte(3);
                            m.writer().writeUTF("Thành công!");
                        } else {
                            conn.p.mm_md += 1;
                            m.writer().writeByte(4);
                            m.writer().writeUTF("Thất bại!");
                        }
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
                                    m_send.writer().writeShort(100);
                                } else {
                                    m_send.writer().writeByte(100);
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
                    long vang_req = (item.tier + 1) * 5_000_000;
                    int coin_req = (item.tier + 1) * 20_000;
                    if (conn.p.get_vang() < vang_req){
                        Service.send_notice_box(conn,"Không đủ "+vang_req+ "vàng");
                        return;
                    }
                    if (conn.p.checkcoin()< coin_req){
                        Service.send_notice_box(conn,"Không đủ" + coin_req + "coin");
                        return;
                    }
                    if (item != null && ((item.type >= 21 && item.type <= 28) || item.type == 55 || item.type == 102) && item.tier < 100) {
                        for (int i = 0; i < 5; i++) {
                            if (i < conn.p.NLtb2.length) {
                                if (conn.p.item.total_item_by_id(7, conn.p.NLtb2[i]) < 100 && (conn.ac_admin < 4 || !Manager.BuffAdmin)) {
                                    Service.send_notice_box(conn, "Không đủ " + ItemTemplate7.item.get(conn.p.NLtb2[i]).getName() + "!");
                                    return;
                                }
                            }
                        }
                        for (int i = 0; i < 5; i++) {
                            if (i < conn.p.NLtb2.length) {
                                conn.p.item.remove(7, conn.p.NLtb2[i], 100);
                            }
                        }
                        conn.p.update_vang(-vang_req);
                        conn.p.update_coin(-coin_req);
                        His_COIN hisc = new His_COIN(conn.user ,conn.p.name);
                        hisc.coin_change = coin_req;
                        hisc.coin_last = conn.p.checkcoin();
                        hisc.Logger = "(TRỪ COIN) từ nâng do3";
                        hisc.Flus();
                        //Log.gI().add_log(conn.p.name, "trừ "+coin_req+" coin từ nâng do3");
                        int ran = Util.random(1000);
                        boolean suc =(item.tier >= 0 && item.tier < 5) && ran > 200 ||
                                (item.tier >= 5 && item.tier < 10) && ran > 300 ||
                                (item.tier >= 10 && item.tier < 15) && ran > 400 ||
                                (item.tier >= 15 && item.tier < 20) && ran > 500 ||
                                (item.tier >= 20 && item.tier < 25) && ran > 600 ||
                                (item.tier >= 25 && item.tier < 30) && ran > 700 ||
                                (item.tier >= 30 && item.tier < 35) && ran > 720 ||
                                (item.tier >= 35 && item.tier < 40) && ran > 740 ||
                                (item.tier >= 40 && item.tier < 45) && ran > 760 ||
                                (item.tier >= 45 && item.tier < 50) && ran > 780 ||
                                (item.tier >= 50 && item.tier < 55) && ran > 800 ||
                                (item.tier >= 55 && item.tier < 60) && ran > 850 ||
                                (item.tier >= 60 && item.tier < 65) && ran > 890 ||
                                (item.tier >= 65 && item.tier < 70) && ran > 920 ||
                                (item.tier >= 70 && item.tier < 75) && ran > 940 ||
                                (item.tier >= 75 && item.tier < 80) && ran > 970 ||
                                (item.tier >= 80 && item.tier < 85) && ran > 990 ||
                                (item.tier >= 85 && item.tier < 90) && ran > 990 ||
                                (item.tier >= 90 && item.tier < 95) && ran > 995 ||
                                (item.tier >= 95 && item.tier < 1000) && ran > 999;
                        if (conn.ac_admin > 111 && Manager.BuffAdmin){
                            suc = true;
                        }
//                        if (item.tier >= 15 && item.tier <= 30) {
//                            suc = Util.random(11000) < ti_le_nang_md[item.tier - 15] || (conn.ac_admin > 3 && Manager.BuffAdmin);
//                        } else {
//                            suc = ti_le_nang_md[item.tier] > Util.random(11000) || (conn.ac_admin > 3 && Manager.BuffAdmin);
//                        }
                        if (suc) {
                            item.tier++;
                            item.islock = true;
                            item.UpdateName();
                            conn.p.setnltb2();
                            for (int i = 0; i < item.op.size(); i++) {
                                Option op = item.op.get(i);
                                if (op.id >= 0 && op.id <= 7){
                                    op.setParam(op.getParam(0)+100);
                                }
                                else if (op.id >= 8 && op.id <= 99 && op.id != 37 && op.id != 38) {
                                    op.setParam(op.getParam(1));
                                }
                            }
                        }
                        m.cleanup();
                        m = new Message(-105);
                        m.writer().writeByte(3);
                        if (suc) {
                            m.writer().writeByte(3);
                            m.writer().writeUTF("Thành công!");
                        } else {
                            m.writer().writeByte(4);
                            m.writer().writeUTF("Thất bại!");
                        }
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
                        if (suc == true) {
                            m.writer().writeByte(3);
                            m.writer().writeUTF("Thành công!");
                        } else {
                            m.writer().writeByte(4);
                            m.writer().writeUTF("Thất bại!");
                        }
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