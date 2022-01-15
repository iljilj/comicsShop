package ru.sfedu.comicsShop;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Constants {
    public static final String MONGO_COLLECTION = "mongoCollection";
    public static final String MONGO_URI = "mongoURI";
    public static final String ENV_PROP_KEY = "environment";//?
    public static final String ENV_PROP_VALUE = "src/main/resources/environment.properties";//?
    public static final String MONGO_DATABASE = "mongoDatabase";
    public static final String DEFAULT_ACTOR = "system";


    //CSV
    public static final String USER_CSV = "data.csv.user";
    public static final String ITEM_CSV = "data.csv.item";
    public static final String PROMO_CODE_CSV = "data.csv.promoCode";
    public static final String GIFT_CERTIFICATE_CSV = "data.csv.giftCertificate";
    public static final String CART_CSV = "data.csv.cart";
    public static final String ORDER_CSV = "data.csv.order";


    //XML
    public static final String USER_XML = "data.xml.user";
    public static final String ITEM_XML = "data.xml.item";
    public static final String PROMO_CODE_XML = "data.xml.promoCode";
    public static final String GIFT_CERTIFICATE_XML = "data.xml.giftCertificate";
    public static final String CART_XML = "data.xml.cart";
    public static final String ORDER_XML = "data.xml.order";













//
//    public static final String INSERT_USER_QUERY = "INSERT INTO USER " +
//            "(id, firstName, secondName, phoneNumber) VALUES ('%s','%s','%s','%s')";
//    public static final String INSERT_ORDER_QUERY = "INSERT INTO ORDER " +
//            "(id, useId, address, totalPrice, discountCodeId, discountTotalPrice, itemIds, itemAmounts) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s')";
//    public static final String INSERT_ITEM_QUERY = "INSERT INTO ITEM " +
//            "(id, name, price) VALUES ('%s','%s','%s')";
//    public static final String INSERT_PROMO_CODE_QUERY = "INSERT INTO PROMO_CODE " +
//            "(id, name, currentlyAvailable, minTotalPrice, discountPercent) VALUES ('%s','%s','%s','%s','%s')";
//    public static final String INSERT_GIFT_CERTIFICATE_QUERY = "INSERT INTO GIFT_CERTIFICATE " +
//            "(id, name, currentlyAvailable, userId, discountTotal) VALUES ('%s','%s','%s','%s','%s')";





    //SQL

    public static final String DELETE_ITEM_SQL = "delete from item where id = ";
    public static final String DELETE_ORDER_SQL = "delete from order_ where id = ";
    public static final String DELETE_USER_SQL = "delete from user where id = ";
    public static final String DELETE_CART_SQL = "delete from cart where id = ";
    public static final String DELETE_PROMO_CODE_SQL = "delete from promoCode where id = ";
    public static final String DELETE_GIFT_CERTIFICATE_SQL = "delete from giftCertificate where id = ";

    public static final String SELECT_SQL = "select * from ";


    public static final String URL_SQL_CONNECTION = "mysqlURL";
    public static final String USER_SQL_CONNECTION = "mysqlUsername";
    public static final String PASSWORD_SQL_CONNECTION ="mysqlPassword";

    public static final String CREATE_DATABASE_SQL_CONNECTION = "CREATE DATABASE ComicsShop";

    public static final String CREATE_ITEM_TABLE_SQL_CONNECTION =
            "CREATE TABLE Item(id int, name varchar(100), price float)";
    public static final String CREATE_USER_TABLE_SQL_CONNECTION =
            "CREATE TABLE User(id int, firstName varchar(100), secondName varchar(100), phoneNumber varchar(100))";
    public static final String CREATE_CART_TABLE_SQL_CONNECTION =
            "CREATE TABLE Cart(id int, userId int, itemId int, itemAmount int)";
    public static final String CREATE_ORDER_TABLE_SQL_CONNECTION =
            "CREATE TABLE Order_(id int, userId int, address varchar(100), totalPrice float, discountCodeId int, discountTotalPrice float, itemIds varchar(100), itemAmounts varchar(100))";
    public static final String CREATE_PROMO_CODE_TABLE_SQL_CONNECTION =
            "CREATE TABLE PromoCode(id int, name varchar(100), currentlyAvailable BOOLEAN, minTotalPrice float, discountPercent int)";
    public static final String CREATE_GIFT_CERTIFICATE_TABLE_SQL_CONNECTION =
            "CREATE TABLE GiftCertificate(id int, name varchar(100), currentlyAvailable BOOLEAN, userId int, discountTotal float)";





    //CLI
    public static final String CLI_SAVE_ITEM = "SAVE_ITEM";
    public static final String CLI_DELETE_ITEM = "DELETE_ITEM";
    public static final String CLI_GET_ITEM = "GET_ITEM";
    public static final String CLI_UPDATE_ITEM = "UPDATE_ITEM";

    public static final String CLI_SAVE_ORDER = "SAVE_ORDER";
    public static final String CLI_DELETE_ORDER = "DELETE_ORDER";
    public static final String CLI_GET_ORDER = "GET_ORDER";
    public static final String CLI_UPDATE_ORDER = "UPDATE_ORDER";

    public static final String CLI_SAVE_USER = "SAVE_USER";
    public static final String CLI_DELETE_USER = "DELETE_USER";
    public static final String CLI_GET_USER = "GET_USER";
    public static final String CLI_UPDATE_USER = "UPDATE_USER";

    public static final String CLI_SAVE_CART = "SAVE_CART";
    public static final String CLI_DELETE_CART = "DELETE_CART";
    public static final String CLI_GET_CART = "GET_CART";
    public static final String CLI_UPDATE_CART = "UPDATE_CART";

    public static final String CLI_SAVE_PROMO_CODE = "SAVE_PROMO_CODE";
    public static final String CLI_DELETE_PROMO_CODE = "DELETE_PROMO_CODE";
    public static final String CLI_GET_PROMO_CODE = "GET_PROMO_CODE";
    public static final String CLI_UPDATE_PROMO_CODE = "UPDATE_PROMO_CODE";

    public static final String CLI_SAVE_GIFT_CERTIFICATE = "SAVE_GIFT_CERTIFICATE";
    public static final String CLI_DELETE_GIFT_CERTIFICATE = "DELETE_GIFT_CERTIFICATE";
    public static final String CLI_GET_GIFT_CERTIFICATE = "GET_GIFT_CERTIFICATE";
    public static final String CLI_UPDATE_GIFT_CERTIFICATE = "UPDATE_GIFT_CERTIFICATE";

    public static final String CLI_EDIT_CART = "EDIT_CART";
    public static final String CLI_EMPTY_CART = "EMPTY_CART";
    public static final String CLI_MAKE_ORDER = "MAKE_ORDER";


}
