//package ru.sfedu.comicsShop;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import ru.sfedu.comicsShop.model.Item;
//import ru.sfedu.comicsShop.provider.DataProviderCsv;
//import ru.sfedu.comicsShop.utils.HistoryContent;
//import ru.sfedu.comicsShop.utils.Status;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static java.lang.Thread.currentThread;
//import static ru.sfedu.comicsShop.Constants.DEFAULT_ACTOR;
//import static ru.sfedu.comicsShop.Constants.*;
//import static ru.sfedu.comicsShop.utils.HistoryUtil.saveHistory;
//
//public class bbb {
//
//
//
//
//    private static Connection connection;
//
//    static {
//        try {
//            connection = DriverManager.getConnection(URL_SQL_CONNECTION, USER_SQL_CONNECTION, PASSWORD_SQL_CONNECTION);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<Item> selectItem(){
//        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
//        try {
//            ResultSet resultSet = connection.prepareStatement(SELECT_ITEM).executeQuery();
//            List<Item> objects = new ArrayList<>();
//            while(resultSet.next()) {
//                objects.add(new Item(resultSet.getLong(1), resultSet.getString(2),
//                        resultSet.getFloat(3)));
//            }
//            HistoryContent historyContent = createHistoryContent(Item.class.getSimpleName(), methodName, objects, Status.SUCCESS);
//            saveHistory(historyContent);
//            return objects;
//        } catch (Exception e){
//            log.error(e);
//            HistoryContent historyContent = createHistoryContent(Item.class.getSimpleName(), methodName, null, Status.FAULT);
//            saveHistory(historyContent);
//            return new ArrayList<>();
//        }
//    }
//
//    private static <T> HistoryContent createHistoryContent(String className, String methodName, T object, Status status) {
//        return new HistoryContent(new java.util.Date().getTime(), className, new Date().toString(), DEFAULT_ACTOR, methodName, object, status);
//    }
//
//
//
//    private static final Logger log = LogManager.getLogger(bbb.class.getName());
//
//    public static final String CREATE_TABLE_ITEM = "CREATE TABLE Item(" +
//            "id int," +
//            "name varchar(255)," +
//            "price float)";
//    public static final Item item = new Item(1, "hhh", 345);
//
//    public static final String UPDATE_TABLE = "insert into item value (" +
//            item.getId() + ", '" +
//            item.getName() + "', " +
//            item.getPrice() + ");";
//
//    public static final String DELETE = "delete from item where id = " +
//            item.getId();
//
//    public static final String SELECT_ITEM = "select * from item";
//
//
//
//
//
//    public static void main(String[] args) {
//
//
//
//        String URL = "jdbc:mysql://localhost:3306/comicsShop";
//        String USER = "admin";
//        String PASSWORD ="admin";
//
//
//
//        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//
//            ResultSet resultSet = connection.prepareStatement(SELECT_ITEM).executeQuery();
//            List<Item> beans = new ArrayList<>();
//            while(resultSet.next()) {
//                beans.add(new Item(resultSet.getLong(1), resultSet.getString(2),
//                        resultSet.getFloat(3)));
//            }
//            System.out.println(beans);
//
//
//
//
//            Statement statement = connection.createStatement();
//            //   statement.executeUpdate(CREATE_TABLE_ITEM);
//            statement.executeUpdate(UPDATE_TABLE);
//            // statement.executeUpdate(DELETE);
//        ////    statement.executeUpdate(SELECT);
//        } catch (Exception e) {
//            log.error(e);
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//}
