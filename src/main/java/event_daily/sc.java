package event_daily;

import client.Player;
import core.Util;
import io.Session;

public class sc extends Player{// mề đay

    public static short[] id = new short[] {486,487,488,489}; // id item
    public static int[] sl = new int[] {10, 10, 10, 10}; // sl

    public sc(Session conn, int id) {
        super(conn, id);
    }

    public static class sc1{//nl Xanh c3
        public static short[] id = new short[] {316, 317, 318, 319, 320, 321, 322, 323, 324, 325}; // id item
        public static int[] sl = new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10}; // sl
    }
    public static class sc2{// Nl Vàng C3
        public static short[] id = new short[] {326, 327, 328, 329, 330, 331, 332, 333, 334, 335}; // id item
        public static int[] sl = new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10}; // sl
    }
    public static class sc3{ // NL Tím C3
        public static short[] id = new short[] {336, 337, 338, 339, 340, 341, 342, 343, 344, 345}; // id item
        public static int[] sl = new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10}; // sl
    }
    public static class sc4{// Đá Nâng Cấp
        public static short[] id = new short[] {464,465,466,467,468}; // id item
        public static int[] sl = new int[] {10, 10, 10,10,10}; // sl

    }

    public static void ran_sc(Player p){

        p.sc_ran[0] =  Util.random(4);
        p.sc_ran[1] =  Util.random(10);
        p.sc_ran[2] = Util.random(10);
        p.sc_ran[3] =  Util.random(10);
        p.sc_ran[4] = Util.random(5);

        return;
    }

}