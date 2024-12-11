/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gamble;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import client.Player;
import core.Log;
import core.Manager;
import core.Service;
import core.Util;
import io.Message;
/**
 *
 * @author chien
 */
public class VXKC implements Runnable{
    public Thread mainloopThread;
    private boolean running;
    private boolean started;
    private short time;
    private String last_winner = "Chưa có";
    private long vang_win = 0;
    private int vang_join = 0;
    private HashMap<Player, Integer> list_playerHashMap = new HashMap<>();
    public VXKC() {
        time = 120;
        started = false;
        mainloopThread = new Thread(this);
        mainloopThread.start();
    }
    public void send_in4(Player p) throws IOException {
            Message m = new Message(-32);
            m.writer().writeShort(p.index);
            m.writer().writeByte(87);
            String text = "Vòng xoay Ngọc\r\n" + "Thời gian\r\n" + get_time() + "\r\n" + " ngọc tổng: \r\n"
                  + Util.number_format(get_total_vang()) + "\r\n" + " Ngọc tham gia: \r\n"
                  + Util.number_format((list_playerHashMap.containsKey(p)) ? list_playerHashMap.get(p) : 0) + "\r\n"
                  + "Tỷ lệ thắng: " + get_percent(p) + "%\r\n" + "Số người hiện tại: " + get_join() + "\r\n"
                  + "Người vừa chiến thắng: " + get_last_winner() + "\r\n" + "Số ngọc ăn được: " + get_vang_win() + "\r\n"
                  + "Số ngọc tham gia: " + get_vang_join();
            m.writer().writeUTF(text);
            p.conn.addmsg(m);
            m.cleanup();
    }

    private String get_percent(Player p) {
            if (list_playerHashMap.containsKey(p)) {
                    float percent = ((float) list_playerHashMap.get(p) * 100) / get_total_vang();
                    return String.format("%.3f", percent);
            }
            return "0.0";
    }

    private int get_join() {
            return list_playerHashMap.size();
    }

    private long get_total_vang() {
            int total = 0;
            for (Map.Entry<Player, Integer> player : list_playerHashMap.entrySet()) {
                    total += player.getValue();
            }
            return total;
    }

    private String get_vang_join() {
            if (vang_join > 0) {
                    return Util.number_format(vang_join);
            }
            return "chưa có";
    }

    private String get_vang_win() {
            if (vang_win > 0) {
                    return Util.number_format(vang_win);
            }
            return "chưa có";
    }

    private String get_last_winner() {
            return last_winner;
    }

    private String get_time() {
            if (started) {
                    if (time <= 120 && time > 60) {
                            if (time > 60 && time <= 69) {
                                    return "01:0" + (time - 60);
                            } else {
                                    return "01:" + (time - 60);
                            }
                    } else if (time >= 0 && time <= 60) {
                            if (time > 0 && time <= 9) {
                                    return "00:0" + time;
                            } else {
                                    return "00:" + time;
                            }
                    }
            } else {
                    return "02:00";
            }
            return "0";
    }

    @Override
    public void run() {
            running = true;
            long time1 = 0;
            long time2 = 0;
            while (running) {
                    try {
                            time1 = System.currentTimeMillis();
                            update();
                            time2 = System.currentTimeMillis();
                            long time3 = (1000L - (time2 - time1));
                            if (time3 > 0) {
                                    Thread.sleep(time3);
                            }
                    } catch (InterruptedException e) {
                    }
            }
    }

    private synchronized void update() {
            if (started) {
                    time--;
                    if (time <= 0) {
                            try {
                                    notice_winner();
                            } catch (IOException e) {
                                    e.printStackTrace();
                            }
                    }
            }
    }

    private synchronized void notice_winner() throws IOException {
            int index = -1;
            int dem = 0;
            for (Map.Entry<Player, Integer> player : list_playerHashMap.entrySet()) {
                    long percent = (((long) list_playerHashMap.get(player.getKey())) * 100L) / get_total_vang();
                    if (percent > Util.random(250)) {
                            index = dem;
                    }
                    if (index != -1) {
                            break;
                    }
                    dem++;
            }
            if (index == -1) {
                    index = Util.random(0, list_playerHashMap.size()); // random win :v
            }
            dem = 0;
            for (Map.Entry<Player, Integer> player : list_playerHashMap.entrySet()) {
                if (dem == index) {
                    Player p0 = player.getKey();
                    if (p0 != null && p0.map != null) {
                            synchronized (p0.map) {
                                    last_winner = player.getKey().name;
                                    vang_join = player.getValue();
                                    vang_win = (get_total_vang() * 8L) / 10L;
                                    Manager.gI().chatKTGprocess(last_winner + " đã thắng " + Util.number_format(vang_win)
                                          + " ngọc khi tham gia vòng xoay may mắn");
                                    player.getKey().update_ngoc(vang_win);
                                    Log.gI().add_log(player.getKey().name, "VXKC ăn được " + Util.number_format(vang_win) + " ngọc");
                                    player.getKey().item.char_inventory(5);
                            }
                    } else {
                            Manager.gI().chatKTGprocess("Người thắng cuộc đã offline nên kết quả bị hủy kakaka :v");
                    }
                    break;
                }
                dem++;
            }
            refresh();
    }

    public synchronized void refresh() {
            started = false;
            this.list_playerHashMap.clear();
            time = 120;
    }

    public void close() {
            running = false;
            mainloopThread.interrupt();
            mainloopThread = null;
    }

    public synchronized void join_vxmm(Player p, int vang_join_vxmm) throws IOException {
            if (time > 10) {
                if(Manager.isLockVX)
                {
                    Service.send_notice_box(p.conn, "Tôi cần nghỉ ngơi, hãy quay lại sau!");
                    return;
                }
                if(p.conn.ac_admin > 10 || p.conn.status!=0)
                {
                    Service.send_notice_box(p.conn, "Bạn không thể tham gia!");
                    return;
                }
                if(list_playerHashMap.containsKey(p))
                {
                    Service.send_notice_box(p.conn, "Bạn chỉ có thể tham gia 1 lần");
                    return;
                }
                if (list_playerHashMap.containsKey(p) && (list_playerHashMap.get(p) + vang_join_vxmm) > 50_000) {
                        Service.send_notice_box(p.conn, "Chỉ có thể tham gia tối đa 50k ngọc");
                        return;
                }
                if ((get_total_vang() + vang_join_vxmm) > 50_000_000) {
                        Service.send_notice_box(p.conn, "Tổng số ngọc trong vòng xoay tối đa chỉ 50tr");
                        return;
                }
                p.update_ngoc(-vang_join_vxmm);
                Log.gI().add_log(p.name, "VXKC chơi " + Util.number_format(vang_join_vxmm) + " ngọc");
                p.item.char_inventory(5);
                Service.send_notice_box(p.conn, "tham gia " + Util.number_format(vang_join_vxmm) + " ngọc thành công");
                if (!list_playerHashMap.containsKey(p)) {
                        list_playerHashMap.put(p, vang_join_vxmm);
                } else {
                        int add = list_playerHashMap.get(p) + vang_join_vxmm;
                        list_playerHashMap.replace(p, list_playerHashMap.get(p), add);
                }
                if (list_playerHashMap.size() > 1 && !started) {
                        this.start_rotate();
                }
            } else {
                    Service.send_notice_box(p.conn, "Không thể thêm khi chỉ còn 10s nữa");
            }
    }

    private synchronized void start_rotate() throws IOException {
        Manager.gI().chatKTGprocess("Vòng xoay ngọc bắt đầu xoay!!");
        started = true;
    }
}
