package BossHDL;

import client.Player;
import core.Manager;
import core.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import map.LeaveItemMap;
import map.Mob_in_map;
import template.Mob;
import map.Map;
/**
 *
 * @author chien
 */
public class BossManager {
    public static final CopyOnWriteArrayList<Mob_in_map> entrys = new CopyOnWriteArrayList<>();
    public static Map map;
    public static byte random(){
        List<Integer> excludedNumbers = Arrays.asList(1, 17, 33, 34, 35);
        int ran;
        do {
            ran = Util.nextInt(46);
        } while (excludedNumbers.contains(ran));
        return (byte) ran;
    }

    public static byte GetIdMap(int idboss){
        switch (idboss) {
            //case = mod - Return = map
            case 103: return  7;
            case 104: return 15;
            case 101: return 25;
            case 84: return 37;
            case 105: return 45;
            
            case 83: return  51;
            case 106: return 62;
            case 149: return 76;
            case 155: return 79;
            case 174: return 26;
            case 173: return 113;
            case 195: return 112;
            case 196: return 115;
            case 197: return 114;
            case 186: return 109;
            case 187: return 110;
            case 188: return 111;
            case 190: return 11;
            case 178: return 36;
            case 23: return 3;
            case 51: return 3;
            case 52: return 3;
            case 53: return 3;
            case 79: return 3;
            default: throw new AssertionError();
        }
    }
    private static short[] GetSite(int idboss){
        switch (idboss) {
            //mod + vị trí
            case 103: return  new short[]{432,512};
            case 104: return new short[]{ 530,213};
            case 101: return new short[]{ 204,284};
            case 84: return new short[]{ 160,224};
            case 105: return new short[]{ 816,1064};
            
            case 83: return  new short[]{ 320,1520};
            case 106: return new short[]{ 468,498};
            case 149: return new short[]{ 204,762};
            case 155: return new short[]{ 534,732};
            case 174: return new short[]{ 550,250};
            case 173: return new short[]{ 450,432};
            case 195: return new short[]{ 450,432};
            case 196: return new short[]{ 450,432};
            case 197: return new short[]{ 450,432};
            case 186: return new short[]{ 450,432};
            case 187: return new short[]{ 450,432};
            case 188: return new short[]{ 450,432};
            case 178: return new short[]{ 450,384};
            case 190: return new short[]{ 280,440};
            case 23: return new short[]{ 510,835};
            case 51: return new short[]{ 510,929};
            case 52: return new short[]{ 622,901};
            case 53: return new short[]{ 573,880};
            case 79: return new short[]{ 629,909};
            default:
                throw new AssertionError();
        }
    }
    public static void init(){
        int idx = 10_000;
        int[] ids = new int[]{101 , 84 , 83 ,103 ,104 ,105 , 106, 149 , 155, 174, 173, 195, 196, 197, 186, 187, 188, 178, 190, 23, 51, 52, 53, 79};
        for(int id : ids){
            for(int i=0; i<5;i++){
//                if(id == 174){
//                    if(i == 1 || i == 4 || Manager.gI().event != 2) continue;
//                }
                if(id == 178){
                    if(i == 1 || i == 4) continue;
                }
                if (id == 190){
                    if(Manager.gI().event != 7) continue;
                }
                if (id == 23 || id == 51 || id == 52 || id == 53 || id == 79){
                    if (Manager.gI().event != 12) continue;
                }
                Mob m = Mob.entrys.get(id);
                Mob_in_map temp = new Mob_in_map();
                temp.template = m;
                temp.x = GetSite(id)[0];
                temp.y = GetSite(id)[1];
                temp.level = id == 178? 150: m.level;
                temp.Set_isBoss(true);
                temp.hp = temp.Set_hpMax(id == 178? (600_000_000 + (i*200_000_000)) : m.hpmax );
                if(id == 178)
                    temp.timeBossRecive = 1000 * 60 * 60 * 6;
                else
                    temp.timeBossRecive = 1000 * 60 * 60 * 8;
                temp.map_id = GetIdMap(id);
                temp.zone_id = (byte)i;
                temp.index = idx++;
                entrys.add(temp);
                //map[i].Boss_entrys.add(temp);
            }
        }
    }
    
    public static String GetInfoBoss(int idMob){
        String s = null;
        //long currentTimeMillis = System.currentTimeMillis();
        
        long time = System.currentTimeMillis();
        for(Mob_in_map mob: entrys){
            if(mob.template.mob_id == idMob){
                if(s == null)
                {
                    Map[] m = Map.get_map_by_id(mob.map_id);
                    if(m == null)continue;
                    s = "Map: "+m[0].name;
                }
                s+="\nkhu: "+ (mob.zone_id+1) + (mob.time_back > time ? (" Hồi sinh vào lúc: "+Helps._Time.ConvertTime(mob.time_back) +""):" Còn sống");
            }
        }
        return s;
    }
    public static long lastBossSpawnTime = System.currentTimeMillis();
    private static int lastMapId = -1; //

    public static void Update() {
        try {
            long time = System.currentTimeMillis();
            for(Mob_in_map mob : entrys){
                if((!mob.is_boss_active || mob.isDie) && time > mob.time_back){
                    Map[] map = Map.get_map_by_id(mob.map_id);
                    if(map == null || map.length <= mob.zone_id)continue;
                    mob.isDie = false;
                    mob.is_boss_active = true;
                    mob.hp = mob.get_HpMax();
                    if(map[mob.zone_id].Boss_entrys.contains(mob))
                        map[mob.zone_id].Boss_entrys.remove(mob);
                    map[mob.zone_id].Boss_entrys.add(mob);
//                    if (mob.template.mob_id == 193) {
//                        Manager.gI().chatKTGprocess(""+mob.template.name+" đã xuất hiện tại "+map[mob.zone_id].name);
//                        System.out.println(map[mob.zone_id].name+" "+mob.zone_id+1);
//                    }
                    //System.out.println("Boss "+mob.template.name+" Acctive "+map[mob.zone_id].name+ " khu "+(mob.zone_id+1));
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }
    
    
    
    
    public static void DropItemBossEvent(Map map, Mob_in_map mob, Player p)throws IOException{
        if(Manager.gI().event == 2){
            int[] it4 = new int[]{48,49,50,51,52,5,26,131,132,24,10};
            int[] it7 = new int[]{346,349,33,34,12,13};
            
            for(int i=0; i< 15; i++){
                LeaveItemMap.leave_vang(map,mob, -1);
            }
            for(int i=0; i<40; i++){
                int ran = Util.random(100);
                if(ran < 10)
                    LeaveItemMap.leave_item_by_type7(map, (short)Util.random(it7),p, mob.index, p.index);
                if(ran < 40)
                    LeaveItemMap.leave_item_by_type4(map, (short)Util.random(it4),p, mob.index, -1);
                else
                    LeaveItemMap.leave_item_by_type7(map, (short)Util.random(417,464),p, mob.index, -1);
            }
        }
    }
    public static void callBossToMap(int map_id, int boss_id, int x, int y, int hp, int level) {
        Mob m = Mob.entrys.get(boss_id);
        Mob_in_map temp = new Mob_in_map();
        temp.template = m;
        temp.x = (short) x;
        temp.y = (short) y;
        temp.level = (short) level;
        temp.color_name = 5;
        temp.Set_isBoss(true);
        temp.hp = temp.Set_hpMax(hp);
        temp.map_id = (short) map_id;
        temp.zone_id = 0;
        temp.index = 10_000 + entrys.size();
        entrys.add(temp);
    }
}
