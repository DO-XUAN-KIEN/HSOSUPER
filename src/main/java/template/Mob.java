package template;

import java.util.ArrayList;
import java.util.List;
import map.Map;

public class Mob {
	public static final List<Mob> entrys = new ArrayList<>();
	public short mob_id;
	public String name;
	public short level;
	public int hpmax;
	public byte typemove;
          public Map map;
        public boolean is_boss;
}
