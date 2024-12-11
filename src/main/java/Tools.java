//
//import Helps.CheckItem;
//import core.SQL;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONValue;
//
//public class Tools {
//
//    public static void main(String[] args) {
//        check();
//    }
//
//    private static void check() {
//        String url = "jdbc:mysql://localhost:3306/hso";
//        String user = "root";
//        String pass = "uw1kIYuqRcRS6@SgX@";
//        Connection connection = null;
//
//        String query = "SELECT `name`, `item3`, `itembox3` FROM `player`;";
//        try {
//            connection = DriverManager.getConnection(url, user, pass);
//            Statement ps = connection.createStatement();
//            ResultSet rs = ps.executeQuery(query);
//            while (rs.next()) {
//                String name = rs.getString("name");
//                int count_me_day = 0;
//                int count_tinh_tu = 0;
//
//                JSONArray jsar = (JSONArray) JSONValue.parse(rs.getString("item3"));
//                for (int i = 0; i < jsar.size(); i++) {
//                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                    short id = Short.parseShort(jsar2.get(0).toString());
//                    if (CheckItem.isMeDay(id)) {
//                        count_me_day++;
//                    }
//                    if (CheckItem.isTT(id)) {
//                        count_tinh_tu++;
//                    }
//                }
//                jsar.clear();
//
//                jsar = (JSONArray) JSONValue.parse(rs.getString("itembox3"));
//                for (int i = 0; i < jsar.size(); i++) {
//                    JSONArray jsar2 = (JSONArray) JSONValue.parse(jsar.get(i).toString());
//                    short id = Short.parseShort(jsar2.get(0).toString());
//                    if (CheckItem.isMeDay(id)) {
//                        count_me_day++;
//                    }
//                    if (CheckItem.isTT(id)) {
//                        count_tinh_tu++;
//                    }
//                }
//                jsar.clear();
//                if (count_me_day > 0 || count_tinh_tu > 0) {
//                    System.out.println(name + " co " + count_me_day + " meday, " + count_tinh_tu + " do tinhtu");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
