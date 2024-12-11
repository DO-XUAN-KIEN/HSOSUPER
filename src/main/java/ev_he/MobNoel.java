/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ev_he;

import client.Player;
import core.Manager;
import core.Util;
import io.Message;
import io.Session;
import java.io.IOException;
import map.Map;

/**
 *
 * @author chien
 */
public class MobNoel {
    public long timeUpdate;
    public short index;
    public String name ="Cây ngũ quả";
    public String nameOwner="";
    public Player Owner;
    public boolean isRemove;
    public Map map;
    public short x,y;

    public MobNoel(Map map, short idx){
        timeUpdate = System.currentTimeMillis();
        this.map = map;
        this.index = idx;
        x= (short)(Util.random(map.mapW)*24);
        y= (short)(Util.random(map.mapH)*24);
//        for(Player p:map.players){
//            if(p!=null && p.conn!= null && p.conn.connected && Math.abs(x - p.x) < 300 && Math.abs(y - p.y) < 300)
//            {
//                try{
//                    //SendMob(p.conn);
//                }catch(Exception e){}
//            }
//        }
        map.mobnoel.add(this);
    }

    public void SendMob(Session conn)throws IOException{
        if(!conn.p.isShowMobEvents)return;
        Message m = new Message(4);
        m.writer().writeByte(1);
        m.writer().writeShort(129);
        m.writer().writeShort(index);
        m.writer().writeShort(x);
        m.writer().writeShort(y);
        m.writer().writeByte(-1);
        conn.addmsg(m);
        m.cleanup();
        m = new Message(7);
        m.writer().writeShort(index);
        m.writer().writeByte(40);
        m.writer().writeShort(x);
        m.writer().writeShort(y);
        m.writer().writeInt(1);
        m.writer().writeInt(1);
        m.writer().writeByte(0);
        m.writer().writeInt(-2);
        m.writer().writeShort(-1);

        m.writer().writeByte(1);
        m.writer().writeByte(1);
        m.writer().writeByte(0);
        m.writer().writeUTF(nameOwner);
        m.writer().writeLong(-11111);
        m.writer().writeByte(0);
        conn.addmsg(m);
        m.cleanup();

    }

    public void SendEffMob(Session conn)throws IOException{
        if(!conn.p.isShowMobEvents)return;
        Message m = new Message(-49);
        m.writer().writeByte(0);
        m.writer().writeShort(Manager.gI().msg_eff_109.length);
        m.writer().write(Manager.gI().msg_eff_109);

        m.writer().writeByte(0);
        m.writer().writeByte(1);
        m.writer().writeByte(109);

        m.writer().writeShort(index);
        m.writer().writeByte(1);//tem mob
        m.writer().writeByte(0);
        m.writer().writeShort(8000);
        m.writer().writeByte(0);
        conn.addmsg(m);
        m.cleanup();
    }

    public void MobLeave()throws IOException{
        Message m2 = new Message(17);
        m2.writer().writeShort(Owner == null ? -1: Owner.index);
        m2.writer().writeShort(index);
        for(Player p : map.players){
            if(p!=null && p.conn != null && p.conn.connected)
            {
                p.conn.addmsg(m2);
                if(p.other_mob_inside != null)
                    p.other_mob_inside.remove((int)index);
            }
        }
        m2.cleanup();
        map.mobnoel.remove(this);
        Event_8.entrys.remove(this);
    }
    public void setOwner(Player p)throws IOException{
        if(p==null)return;
        nameOwner = p.name;
        Owner = p;
        MobLeave();
        //updateMobInsiders();
    }

    public void mobMove(){
        try{
            timeUpdate = System.currentTimeMillis();
            x= (short)(Util.random(map.mapW)*24);
            y= (short)(Util.random(map.mapH)*24);
            Message m2 = new Message(17);
            m2.writer().writeShort(Owner == null ? -1: Owner.index);
            m2.writer().writeShort(index);
            for(Player p: map.players){
                if(p.other_mob_inside != null && p.other_mob_inside.containsKey((int)index))
                {
                    p.conn.addmsg(m2);
                    p.other_mob_inside.remove((int)index);
                }
            }
            m2.cleanup();
        }catch(Exception e){}
    }

    public void updateMobInsiders(){
        try{
            Message m = new Message(7);
            m.writer().writeShort(index);
            m.writer().writeByte(40);
            m.writer().writeShort(x);
            m.writer().writeShort(y);
            m.writer().writeInt(1);
            m.writer().writeInt(1);
            m.writer().writeByte(0);
            m.writer().writeInt(1);
            m.writer().writeShort(-1);

            m.writer().writeByte(1);
            m.writer().writeByte(1);
            m.writer().writeByte(0);
            m.writer().writeUTF(nameOwner);
            m.writer().writeLong(-11111);
            m.writer().writeByte(0);
            for(Player p : map.players){
                if(p!=null && p.conn!= null && p.conn.connected && Math.abs(x - p.x) < 300 && Math.abs(y - p.y) < 300)
                    p.conn.addmsg(m);
            }
            m.cleanup();
        }catch(IOException e){}
    }

    public void update(){
        try{
            long time = System.currentTimeMillis();
            if(Owner != null)
                MobLeave();
            else if(time - timeUpdate > 1000 * 60 * 4)
                mobMove();
        }catch(Exception e){}
    }
}
