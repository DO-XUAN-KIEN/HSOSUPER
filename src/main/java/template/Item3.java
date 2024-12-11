package template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item3 {

    public short id;
    public byte clazz;
    public byte type;
    public short level;
    public short icon;
    public byte color;
    public byte part;
    public boolean islock;
    public String name;
    public byte tier;
    public byte tierStar;
    public List<Option> op;
    public List<Option> opMedal;
    public long time_use;
    public long expiry_date;

    public Item3() {
    }

    public Item3(Item3 Origin) {
        this.id = Origin.id;
        this.clazz = Origin.clazz;
        this.type = Origin.type;
        this.level = Origin.level;
        this.icon = Origin.icon;
        this.color = Origin.color;
        this.part = Origin.part;
        this.islock = Origin.islock;
        this.name = Origin.name;
        this.tier = Origin.tier;
        this.tierStar = Origin.tierStar;
        this.op = new ArrayList<>();
        if (Origin.op != null) {
            this.op.addAll(Origin.op);
        }
        this.opMedal = new ArrayList<>();
        if (Origin.opMedal != null) {
            this.opMedal.addAll(Origin.opMedal);
        }
        this.time_use = Origin.time_use;
        this.expiry_date = Origin.expiry_date;
    }

    public Option GetNextOPMedal() {
        if (this.opMedal == null || this.opMedal.size() < 1) {
            this.opMedal = null;
            return null;
        }
        return this.opMedal.remove(0);
    }

    public void UpdateName() {
        name = ItemTemplate3.item.get(id).getName();
        if (islock) {
            name += " [Khóa]";
        }
        if (tierStar > 0 && (id >= 4587 && id <= 4590) || (id >= 4831 && id <= 4873) || (id >= 4878 && id <= 4886 && id != 4879)){
            name += "[Siêu cấp " + tierStar +"]";
        } else if (tierStar > 0) {
            name += " [Cấp " + tierStar + "]";
        }
    }

    public boolean isTT() {
        return (id >= 3732 && id <= 3736) || id >= 3807 && id <= 3811 || id >= 3897 && id <= 3901 || id >= 4656 && id <= 4675|| (id >= 4831 && id <= 4873);
    }

    public void UpdateOption() {
        int[] opAo = {-123, -122, -121, -120, -119};
        int[] opNon = {-114, -125, -117};
        int[] opVK = {-128, -125, 99, -85, -83, -81};
        int[] opNhan = {-103, -88, -116, 99, -85, -83, -81};
        int[] opDayChuyen = {-88, -117, -115, -92};
        int[] opGang = {-90, -115, -92};
        int[] opGiay = {-116, -115, -92};
        Integer[] opAoOld = {-111, -110, -109, -108, -107};
        Integer[] opNonOld = {-102, -113, -105};
        Integer[] opVKOld = {-101, -113, -86, -84, -82, -80};
        Integer[] opNhanOld = {-91, -87, -104, -86, -84, -82, -80};
        Integer[] opDayChuyenOld = {-87, -105, -103, -91};;
        Integer[] opGangOld = {-89, -103, -91};
        Integer[] opGiayOld = {-104, -103, -91};

        if (type == 0 || type == 1) {
            List<Integer> list = Arrays.asList(opAoOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opAo[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 2) {
            List<Integer> list = Arrays.asList(opNonOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opNon[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 3) {
            List<Integer> list = Arrays.asList(opGangOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opGang[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 4) {
            List<Integer> list = Arrays.asList(opNhanOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opNhan[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 5) {
            List<Integer> list = Arrays.asList(opDayChuyenOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opDayChuyen[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 6) {
            List<Integer> list = Arrays.asList(opGiayOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opGiay[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type > 7) {
            List<Integer> list = Arrays.asList(opVKOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opVK[list.indexOf((int) op.get(i).id)];
                }
            }
        }

    }

    public void ReUpdateOption() {
        Integer[] opAoOld = {-123, -122, -121, -120, -119};
        Integer[] opNonOld = {-114, -125, -117};
        Integer[] opVKOld = {-128, -125, 99, -85, -83, -81};
        Integer[] opNhanOld = {-103, -88, -116, 99, -85, -83, -81};
        Integer[] opDayChuyenOld = {-88, -117, -115, -92};
        Integer[] opGangOld = {-90, -115, -92};
        Integer[] opGiayOld = {-116, -115, -92};
        int[] opAo = {-111, -110, -109, -108, -107};
        int[] opNon = {-102, -113, -105};
        int[] opVK = {-101, -113, -86, -84, -82, -80};
        int[] opNhan = {-91, -87, -104, -86, -84, -82, -80};
        int[] opDayChuyen = {-87, -105, -103, -91};;
        int[] opGang = {-89, -103, -91};
        int[] opGiay = {-104, -103, -91};
        if (type == 0 || type == 1) {
            List<Integer> list = Arrays.asList(opAoOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opAo[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 2) {
            List<Integer> list = Arrays.asList(opNonOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opNon[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 3) {
            List<Integer> list = Arrays.asList(opGangOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opGang[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 4) {
            List<Integer> list = Arrays.asList(opNhanOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opNhan[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 5) {
            List<Integer> list = Arrays.asList(opDayChuyenOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opDayChuyen[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type == 6) {
            List<Integer> list = Arrays.asList(opGiayOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opGiay[list.indexOf((int) op.get(i).id)];
                }
            }
        } else if (type > 7) {
            List<Integer> list = Arrays.asList(opVKOld);
            for (int i = 0; i < op.size(); i++) {
                if (list.contains((int) op.get(i).id)) {
                    op.get(i).id = (byte) opVK[list.indexOf((int) op.get(i).id)];
                }
            }
        }
    }

    public boolean hasOption(int id) {
        for (Option o : op) {
            if (o.id == id) {
                return true;
            }
        }
        return false;
    }
    
    public int getParamOption(int id) {
        for (Option o : op) {
            if (o.id == id) {
                return o.param;
            }
        }
        return 0;
    }
}
