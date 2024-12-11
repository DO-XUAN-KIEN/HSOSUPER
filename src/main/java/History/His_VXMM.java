/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package History;

import core.SQL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author chien
 */
public class His_VXMM {
    public String namePWin="";
    public int moneyJoin;
    public long moneyround;
    private byte typeMoney;
    public long lastMoney;
    public long affterMoney;
    public String Logger="";
    
    public His_VXMM(byte type){
        this.typeMoney = type;
    }
    
    public void Flus(){
        String query
                = "INSERT INTO `history_vxmm` (`name_player`, `monney_join`, `money_round` , `type_monney`, `last_monney`"
                + ", `monney`, `logger`) VALUES ('"
                + this.namePWin + "', '" + this.moneyJoin + "', '" + this.moneyround + "', '" + this.typeMoney + "', '" + this.lastMoney + 
                "', '" + this.affterMoney + "', '" + Logger+ "')";
        try ( Connection connection = SQL.gI().getConnection();  Statement statement = connection.createStatement();) {
            if (statement.executeUpdate(query) > 0) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
