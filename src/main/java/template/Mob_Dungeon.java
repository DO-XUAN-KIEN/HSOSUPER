package template;

import client.Player;
import core.Util;

import java.io.IOException;
import map.Dungeon;
import map.Map;

public class Mob_Dungeon extends MainObject {

    private Dungeon dungeon;
    public int from_gate;
    public boolean is_atk;
    

    public Mob_Dungeon(Dungeon map, int index, Mob mob) {
        dungeon= map;
        this.index = (short) index;
        this.template = mob;
        is_atk = false;
        isDie = false;
        color_name = 0;
    }
    @Override
    public boolean isMobDungeon() {
        return true;
    }
    @Override
    public void SetDie(Map map, MainObject mainAtk) throws IOException {
        if (this.hp <= 0) {
            this.hp = 0;
            // mob die
            if (!this.isDie) {
                this.isDie = true;
                // send p outside
                if(30>Util.random(0,100))
                    Dungeon.leave_item_by_type7(map, (short)Util.random(417,464), (Player)mainAtk, this.index);
                if(5>Util.random(0,100))
                    Dungeon.leave_item_by_type7(map, Medal_Material.m_blue[Util.random(Medal_Material.m_blue.length)], (Player)mainAtk, this.index);
            }
            ((Player)mainAtk).point_active[1] += (dungeon.wave / 5);
            dungeon.num_mob--;
            if (dungeon.num_mob == 0) {
                dungeon.state = 1;
            }
        }
    }
}
