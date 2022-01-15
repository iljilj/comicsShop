package ru.sfedu.comicsShop.provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.comicsShop.model.*;
import ru.sfedu.comicsShop.utils.*;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

import static ru.sfedu.comicsShop.Constants.*;
import static ru.sfedu.comicsShop.Constants.DEFAULT_ACTOR;
import static ru.sfedu.comicsShop.utils.HistoryUtil.saveHistory;

public class DataProviderSql implements IDataProvider{
    private static final Logger log = LogManager.getLogger(DataProviderSql.class.getName());
    private static final Connection connection = SqlUtil.connectToMySql();

    private static <T> HistoryContent createHistoryContent(String className, String methodName, T object, Status status) {
        return new HistoryContent(new java.util.Date().getTime(), className, new Date().toString(), DEFAULT_ACTOR, methodName, object, status);
    }

    public List<Item> selectItem(){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_SQL+Item.class.getSimpleName()).executeQuery();
            List<Item> objects = new ArrayList<>();
            while(resultSet.next()) {
                objects.add(new Item(resultSet.getLong(1), resultSet.getString(2),
                        resultSet.getFloat(3)));
            }
            HistoryContent historyContent = createHistoryContent(Item.class.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        } catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(Item.class.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }

    public List<User> selectUser(){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_SQL+User.class.getSimpleName()).executeQuery();
            List<User> objects = new ArrayList<>();
            while(resultSet.next()) {
                objects.add(new User(resultSet.getLong(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getString(4)));
            }
            HistoryContent historyContent = createHistoryContent(User.class.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        } catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(User.class.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }

    public List<Cart> selectCart(){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_SQL+Cart.class.getSimpleName()).executeQuery();
            List<Cart> objects = new ArrayList<>();
            while(resultSet.next()) {
                objects.add(new Cart(resultSet.getLong(1), resultSet.getLong(2),
                        resultSet.getLong(3), resultSet.getLong(4)));
            }
            HistoryContent historyContent = createHistoryContent(Cart.class.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        } catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(Cart.class.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }

    public List<Order> selectOrder(){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_SQL+Order.class.getSimpleName()+"_").executeQuery();
            List<Order> objects = new ArrayList<>();
            while(resultSet.next()) {
                objects.add(new Order(resultSet.getLong(1), resultSet.getLong(2),
                        resultSet.getString(3), resultSet.getFloat(4),
                        resultSet.getLong(5), resultSet.getFloat(6),
                        resultSet.getString(7), resultSet.getString(8)));
            }
            HistoryContent historyContent = createHistoryContent(Order.class.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        } catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(Order.class.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }

    public static List<PromoCode> selectPromoCode(){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_SQL+PromoCode.class.getSimpleName()).executeQuery();
            List<PromoCode> objects = new ArrayList<>();
            while(resultSet.next()) {
                objects.add(new PromoCode(resultSet.getLong(1), resultSet.getString(2),
                        resultSet.getBoolean(3), resultSet.getFloat(4), resultSet.getLong(5)));
            }
            HistoryContent historyContent = createHistoryContent(PromoCode.class.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        } catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(PromoCode.class.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }

    public static List<GiftCertificate> selectGiftCertificate(){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ResultSet resultSet = connection.prepareStatement(SELECT_SQL+GiftCertificate.class.getSimpleName()).executeQuery();
            List<GiftCertificate> objects = new ArrayList<>();
            while(resultSet.next()) {
                objects.add(new GiftCertificate(resultSet.getLong(1), resultSet.getString(2),
                        resultSet.getBoolean(3), resultSet.getLong(4),  resultSet.getFloat(5)));
            }
            HistoryContent historyContent = createHistoryContent(GiftCertificate.class.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        } catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(GiftCertificate.class.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }

    @Override
    public Status saveItem(Item object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getItemById(object.getId()).isEmpty()
                && object.getPrice() > 0
                && !object.getName().isEmpty()){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("insert into item value (" +
                        object.getId() + ", '" +
                        object.getName() + "', " +
                        object.getPrice() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }


    //возвращает тру если айтим есть в заказе
    private static Boolean isItemInOrder(long itemId, Order order){
        List<String> itemIdsString =  new ArrayList<>(Arrays.asList(order.getItemIds().split(" ")));
        List<Integer> itemIds = itemIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        for(long i : itemIds){
            if(i == itemId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Status deleteItem(Item object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getItemById(object.getId()).isPresent()){
            Status status;
            final long id = object.getId();

            //удалить все записи удаляемого айтема из корзины
            List<Cart> carts;
            Optional<Cart> cart;
            while(selectCart().stream().anyMatch(o -> o.getItemId() == id)){
                carts = selectCart();
                cart = (carts.stream().filter(o -> o.getItemId() == id).findFirst());
                deleteCart(cart.get());
            }

            //удалить все записи удаляемого айтема из заказов
            List<Order> orders;
            Optional<Order> order;
            while(selectOrder().stream().anyMatch(o -> isItemInOrder(id, o))){
                orders = selectOrder();
                order = (orders.stream().filter(o -> isItemInOrder(id, o)).findFirst());
                deleteOrder(order.get());
            }
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_ITEM_SQL+object.getId());
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }


    @Override
    public Optional<Item> getItemById(long id){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = Item.class.getSimpleName();
        List<Item> objects = selectItem();
        Optional<Item> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updateItem(Item object){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getItemById(object.getId()).isPresent()
                && object.getPrice() > 0
                && !object.getName().isEmpty()){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_ITEM_SQL+object.getId());
                statement.executeUpdate("insert into item value (" +
                        object.getId() + ", '" +
                        object.getName() + "', " +
                        object.getPrice() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status saveUser(User object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getUserById(object.getId()).isEmpty()
                && !object.getFirstName().isEmpty()
                && !object.getSecondName().isEmpty()
                && !object.getPhoneNumber().isEmpty()){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("insert into user value (" +
                        object.getId() + ", '" +
                        object.getFirstName() + "', '" +
                        object.getSecondName() + "', '" +
                        object.getPhoneNumber() + "');");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status deleteUser(User object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getUserById(object.getId()).isPresent()){
            Status status;
            final long id = object.getId();

            //удалить все записи удаляемого юзера из корзины
            List<Cart> carts;
            Optional<Cart> cart;
            while(selectCart().stream().anyMatch(o -> o.getUserId() == id)){
                carts = selectCart();
                cart = (carts.stream().filter(o -> o.getUserId() == id).findFirst());
                deleteCart(cart.get());
            }

            //удалить все записи удаляемого юзера из заказов
            List<Order> orders;
            Optional<Order> order;
            while(selectOrder().stream().anyMatch(o -> o.getUserId() == id)){
                orders = selectOrder();
                order = (orders.stream().filter(o -> o.getUserId() == id).findFirst());
                deleteOrder(order.get());
            }

            //удалить все подарочные сертификаты удаляемого юзера
            List<GiftCertificate> giftCertificates;
            Optional<GiftCertificate> giftCertificate;
            while(selectGiftCertificate().stream().anyMatch(o -> o.getUserId() == id)){
                giftCertificates = selectGiftCertificate();
                giftCertificate = (giftCertificates.stream().filter(o -> o.getUserId() == id).findFirst());
                deleteGiftCertificate(giftCertificate.get());
            }


            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_USER_SQL+object.getId());
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Optional<User> getUserById(long id) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = User.class.getSimpleName();
        List<User> objects = selectUser();
        Optional<User> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updateUser(User object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getUserById(object.getId()).isPresent()
                && !object.getFirstName().isEmpty()
                && !object.getSecondName().isEmpty()
                && !object.getPhoneNumber().isEmpty()){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_USER_SQL+object.getId());
                statement.executeUpdate("insert into user value (" +
                        object.getId() + ", '" +
                        object.getFirstName() + "', '" +
                        object.getSecondName() + "', '" +
                        object.getPhoneNumber() + "');");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            System.out.println(1);
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status saveCart(Cart object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getCartById(object.getId()).isEmpty()
                && getUserById(object.getUserId()).isPresent()
                && getItemById(object.getItemId()).isPresent()
                && object.getItemAmount() > 0){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("insert into cart value (" +
                        object.getId() + ", " +
                        object.getUserId() + ", " +
                        object.getItemId() + ", " +
                        object.getItemAmount() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status deleteCart(Cart object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getCartById(object.getId()).isPresent()){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_CART_SQL+object.getId());
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Optional<Cart> getCartById(long id) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = Cart.class.getSimpleName();
        List<Cart> objects = selectCart();
        Optional<Cart> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updateCart(Cart object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getCartById(object.getId()).isPresent()
                && getUserById(object.getUserId()).isPresent()
                && getItemById(object.getItemId()).isPresent()
                && object.getItemAmount() > 0){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_CART_SQL+object.getId());
                statement.executeUpdate("insert into cart value (" +
                        object.getId() + ", " +
                        object.getUserId() + ", " +
                        object.getItemId() + ", " +
                        object.getItemAmount() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    //проверяет что подобный код не встречентся не в сертификатах не в промокодах
    private static Boolean isUniqueCode(String name){
        List<GiftCertificate> giftCertificates = selectGiftCertificate();
        GiftCertificate giftCertificate = giftCertificates.stream().filter(o -> o.getName().equals(name)).findFirst().orElse(null);
        List<PromoCode> promoCodes = selectPromoCode();
        PromoCode promoCode = promoCodes.stream().filter(o -> o.getName().equals(name)).findFirst().orElse(null);
        return (giftCertificate == null) && (promoCode == null);
    }

    @Override
    public Status saveGiftCertificate(GiftCertificate object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getGiftCertificateById(object.getId()).isEmpty()
                && getPromoCodeById(object.getId()).isEmpty()
                && isUniqueCode(object.getName())
                && !object.getName().isEmpty()
                && getUserById(object.getUserId()).isPresent()
                && object.getDiscountTotal() > 0){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("insert into giftCertificate value (" +
                        object.getId() + ", '" +
                        object.getName() + "', " +
                        object.getCurrentlyAvailable() + ", " +
                        object.getUserId() + ", " +
                        object.getDiscountTotal() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status deleteGiftCertificate(GiftCertificate object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getGiftCertificateById(object.getId()).isPresent()){
            Status status;
            final long id = object.getId();
            //в заказах убрать айди удаляемых сертификатов и поменять значение цены без скидки на значение цены со скидкой
            List<Order> orders;
            Optional<Order> order;
            while(selectOrder().stream()
                    .anyMatch(o -> o.getDiscountCodeId() != null && o.getDiscountCodeId() == id)){
                orders = selectOrder();
                order = (orders.stream().filter(o -> o.getDiscountCodeId() == id).findFirst());
                order.get().setDiscountCodeId(null);
                order.get().setDiscountTotalPrice(order.get().getTotalPrice());
            }

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_GIFT_CERTIFICATE_SQL+object.getId());
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Optional<GiftCertificate> getGiftCertificateById(long id) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = GiftCertificate.class.getSimpleName();
        List<GiftCertificate> objects = selectGiftCertificate();
        Optional<GiftCertificate> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updateGiftCertificate(GiftCertificate object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        final long id = object.getId();
        List<GiftCertificate> objects = selectGiftCertificate();
        if (getGiftCertificateById(object.getId()).isPresent()
                //имя промокода либо уникально либо такое же как и было
                && (object.getName().equals(objects.stream().filter(o -> o.getId() == id).findFirst().get().getName())
                || isUniqueCode(object.getName()))
                && !object.getName().isEmpty()
                && getUserById(object.getUserId()).isPresent()
                && object.getDiscountTotal() > 0){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_GIFT_CERTIFICATE_SQL+object.getId());
                statement.executeUpdate("insert into giftCertificate value (" +
                        object.getId() + ", '" +
                        object.getName() + "', " +
                        object.getCurrentlyAvailable() + ", " +
                        object.getUserId() + ", " +
                        object.getDiscountTotal() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status savePromoCode(PromoCode object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if  (getGiftCertificateById(object.getId()).isEmpty()
                && getPromoCodeById(object.getId()).isEmpty()
                && isUniqueCode(object.getName())
                && !object.getName().isEmpty()
                && object.getMinTotalPrice() > 0
                && object.getDiscountPercent() > 0
                && object.getDiscountPercent() < 100){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("insert into PromoCode value (" +
                        object.getId() + ", '" +
                        object.getName() + "', " +
                        object.getCurrentlyAvailable() + ", " +
                        object.getMinTotalPrice() + ", " +
                        object.getDiscountPercent() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status deletePromoCode(PromoCode object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getPromoCodeById(object.getId()).isPresent()){
            Status status;
            final long id = object.getId();

            //в заказах убрать айди удаляемых сертификатов и поменять значение цены со скидкой
            List<Order> orders;
            Optional<Order> order;
            while(selectOrder().stream()
                    .anyMatch(o -> o.getDiscountCodeId() != null && o.getDiscountCodeId() == id)){
                orders = selectOrder();
                order = (orders.stream().filter(o -> o.getDiscountCodeId() == id).findFirst());
                order.get().setDiscountCodeId(null);
                order.get().setDiscountTotalPrice(order.get().getTotalPrice());
            }

            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_PROMO_CODE_SQL+object.getId());
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Optional<PromoCode> getPromoCodeById(long id) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = PromoCode.class.getSimpleName();
        List<PromoCode> objects = selectPromoCode();
        Optional<PromoCode> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updatePromoCode(PromoCode object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        final long id = object.getId();
        List<PromoCode> objects = selectPromoCode();
        if  (getPromoCodeById(object.getId()).isPresent()
                // && isUniqueCode(object.getName())
                && (object.getName().equals(objects.stream().filter(o -> o.getId() == id).findFirst().get().getName())
                || isUniqueCode(object.getName()))
                && !object.getName().isEmpty()
                && object.getMinTotalPrice() > 0
                && object.getDiscountPercent() > 0
                && object.getDiscountPercent() < 100
        ){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_PROMO_CODE_SQL+object.getId());
                statement.executeUpdate("insert into PromoCode value (" +
                        object.getId() + ", '" +
                        object.getName() + "', " +
                        object.getCurrentlyAvailable() + ", " +
                        object.getMinTotalPrice() + ", " +
                        object.getDiscountPercent() + ");");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status saveOrder(Order object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();

        if(object.getItemIds().isEmpty()
                ||object.getItemAmounts().isEmpty()){
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
        List<String> itemIdsString =  new ArrayList<>(Arrays.asList(object.getItemIds().split(" ")));
        List<Integer> itemIds = itemIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<String> itemAmountsString =  new ArrayList<>(Arrays.asList(object.getItemAmounts().split(" ")));
        List<Integer> itemAmounts = itemAmountsString.stream().map(Integer::valueOf).collect(Collectors.toList());

        if  (getOrderById(object.getId()).isEmpty()
                && getUserById(object.getUserId()).isPresent()
                && !object.getAddress().isEmpty()
                && object.getTotalPrice() > 0
                && object.getDiscountTotalPrice() >= 0 //цена со скидкой неотрицательна
                && (object.getDiscountCodeId()==null  //скидочный код либо не указан
                || getPromoCodeById(object.getDiscountCodeId()).isPresent()  //либо существует в табличке с промокодами
                || getGiftCertificateById(object.getDiscountCodeId()).isPresent()) //или скидочными сертификатами
                && itemIds.size() != 0 //колво товаров не 0
                && itemAmounts.size()==itemIds.size()  //одинаковое колво товаров и их количеств
                && (itemAmounts.stream().filter(s -> s <= 0).findFirst()).isEmpty() //все значения колва товаров положительны
                && (itemIds.stream().filter(s -> getItemById(s).isEmpty()).findFirst()).isEmpty() //все айтемы существуют
        ){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate("insert into Order_ value (" +
                        object.getId() + ", " +
                        object.getUserId() + ", '" +
                        object.getAddress() + "', " +
                        object.getTotalPrice() + ", " +
                        object.getDiscountCodeId() + ", " +
                        object.getDiscountTotalPrice() + ", '" +
                        object.getItemIds() + "', '" +
                        object.getItemAmounts() + "');");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status deleteOrder(Order object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getOrderById(object.getId()).isPresent()){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_ORDER_SQL+object.getId());
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Optional<Order> getOrderById(long id) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = Order.class.getSimpleName();
        List<Order> objects = selectOrder();
        Optional<Order> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updateOrder(Order object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();

        if(object.getItemIds().isEmpty()
                ||object.getItemAmounts().isEmpty()){
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }

        List<String> itemIdsString =  new ArrayList<>(Arrays.asList(object.getItemIds().split(" ")));
        List<Integer> itemIds = itemIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<String> itemAmountsString =  new ArrayList<>(Arrays.asList(object.getItemAmounts().split(" ")));
        List<Integer> itemAmounts = itemAmountsString.stream().map(Integer::valueOf).collect(Collectors.toList());

        if (getOrderById(object.getId()).isPresent()
                && getUserById(object.getUserId()).isPresent()
                && !object.getAddress().isEmpty()
                && object.getTotalPrice() > 0
                && object.getDiscountTotalPrice() >= 0 //цена со скидкой неотрицательна
                && (object.getDiscountCodeId()==null  //скидочный код либо не указан
                || getPromoCodeById(object.getDiscountCodeId()).isPresent()  //либо существует в табличке с промокодами
                || getGiftCertificateById(object.getDiscountCodeId()).isPresent()) //или скидочными сертификатами
                && itemIds.size() != 0 //колво товаров не 0
                && itemAmounts.size()==itemIds.size()  //одинаковое колво товаров и их количеств
                && (itemAmounts.stream().filter(s -> s <= 0).findFirst()).isEmpty() //все значения колва товаров положительны
                && (itemIds.stream().filter(s -> getItemById(s).isEmpty()).findFirst()).isEmpty() //все айтемы существуют
        ){
            Status status;
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(DELETE_ORDER_SQL+object.getId());
                statement.executeUpdate("insert into Order_ value (" +
                        object.getId() + ", " +
                        object.getUserId() + ", '" +
                        object.getAddress() + "', " +
                        object.getTotalPrice() + ", " +
                        object.getDiscountCodeId() + ", " +
                        object.getDiscountTotalPrice() + ", '" +
                        object.getItemIds() + "', '" +
                        object.getItemAmounts() + "');");
                status = Status.SUCCESS;
            } catch (Exception e){
                log.error(e);
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, object, status);
            saveHistory(historyContent);
            return status;
        }else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    public Optional<Cart> getCartByUserIdAndItemId(long userId, long itemId){

        try{
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM CART WHERE userId = "+userId+" AND itemId = "+itemId).executeQuery();
            resultSet.next();
            return Optional.of(new Cart(resultSet.getLong(1), resultSet.getLong(2),
                    resultSet.getLong(3), resultSet.getLong(4)));
        } catch (Exception e) {
            log.error(e);
            return Optional.empty();
        }
    }

    @Override
    public Status editCart(long userId, long itemId, long amount) {
        Optional<Cart> cart = getCartByUserIdAndItemId(userId, itemId);
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = cart.getClass().getSimpleName();
        Status status;

        if (cart.isPresent()){
            System.out.println("cart is present");
            if(amount > 0){
                System.out.println("amount is positive");
                System.out.println(cart);
                cart.get().setItemAmount(amount);
                System.out.println(cart);
                System.out.println(selectCart());
                status = updateCart(cart.get());
                System.out.println(selectCart());
                //status = Status.SUCCESS;
            }
            else if(amount == 0){
                status = deleteCart(cart.get());
            }
            else {
                status = Status.FAULT;
            }
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
        }else{
            final UUID uid = UUID.randomUUID();
            long id = uid.getMostSignificantBits() % 1000000000;
            Cart newCart = new Cart(id, userId, itemId, amount);
            status = saveCart(newCart);
            System.out.println(newCart);
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
        }
        return status;
    }

    @Override
    public Status emptyCart(long userId) {
        Status status = Status.FAULT;
        List<Cart> carts;
        carts = selectCart()
                .stream()
                .filter(o->o.getUserId()==userId)
                .collect(Collectors.toList());
        for(Cart cart : carts){
            status = deleteCart(cart);
        }
        return status;
    }

    @Override
    public ReturnOrder makeOrder(long userId, String address, String discountCode) {
        float totalPrice = countPrice(userId);
        Long discountCodeId = null;
        float discountTotalPrice = totalPrice;
        if (discountCode!=null && !discountCode.isEmpty()){
            ReturnDiscount discount = makeDiscount(userId,discountCode,totalPrice);
            discountCodeId = discount.getDiscountId();
            discountTotalPrice = discount.getDiscountPrice();
        }

        List<String> items = new ArrayList<>();
        List<String> amounts = new ArrayList<>();

        List<Cart> carts;
        carts = selectCart()
                .stream()
                .filter(o->o.getUserId()==userId)
                .collect(Collectors.toList());
        for(Cart cart : carts){
            items.add(String.valueOf(cart.getItemAmount()));
            amounts.add(String.valueOf(cart.getItemId()));
        }
        String itemsString = String.join(" ", items);
        String amountsString = String.join(" ", items);

        final UUID uid = UUID.randomUUID();
        long id = uid.getMostSignificantBits() % 1000000000;

        Order order = new Order(id, userId, address, totalPrice, discountCodeId,
                discountTotalPrice, itemsString, amountsString);

        if (saveOrder(order).equals(Status.SUCCESS)){
            return new ReturnOrder(id, Status.SUCCESS);
        } else{
            return new ReturnOrder(null, Status.FAULT);
        }
    }

    @Override
    public ReturnOrder makeOrder(long userId, String address) {
        return makeOrder(userId, address, null);
    }

    @Override
    public float countPrice(long userId) {
        float price = 0;
        List<Cart> carts;
        carts =selectCart()
                .stream()
                .filter(o->o.getUserId()==userId)
                .collect(Collectors.toList());
        Item item;
        for(Cart cart : carts){
            item = getItemById(cart.getItemId()).get();
            price += item.getPrice() * cart.getItemAmount();
        }
        return price;
    }

    @Override
    public ReturnDiscount makeDiscount(long userId, String discountCode, float price) {
        Status status = Status.FAULT;

        Optional<PromoCode> promoCode = selectPromoCode()
                .stream()
                .filter(o->o.getName().equals(discountCode))
                .findFirst();


        Optional<GiftCertificate> giftCertificate = selectGiftCertificate()
                .stream()
                .filter(o->o.getName().equals(discountCode))
                .findFirst();

        Long discountCodeId;
        float newPrice;
        if (promoCode.isPresent()){
            newPrice = enterPromoCode(promoCode.get().getId(), price);
            if (newPrice != price){
                status = Status.SUCCESS;
                discountCodeId = promoCode.get().getId();
            }else {
                discountCodeId = null;
            }
            return new ReturnDiscount(discountCodeId, newPrice, status);
        } else if(giftCertificate.isPresent()){
            newPrice = enterGiftCertificate(giftCertificate.get().getId(), userId, price);
            if (newPrice != price){
                status = Status.SUCCESS;
                discountCodeId = giftCertificate.get().getId();
            } else{
                discountCodeId = null;
            }
            return new ReturnDiscount(discountCodeId, newPrice, status);
        }else{
            return new ReturnDiscount(null, price, Status.FAULT);
        }
    }

    @Override
    public float enterPromoCode(long discountCodeId, float price) {
        PromoCode promoCode = getPromoCodeById(discountCodeId).get();
        if(promoCode.getMinTotalPrice()<=price && promoCode.getCurrentlyAvailable()){
            price *= (((float)100 - (promoCode.getDiscountPercent())) / (float)100);
        }
        return price;
    }

    @Override
    public float enterGiftCertificate(long discountCodeId, long userId, float price) {
        GiftCertificate giftCertificate = getGiftCertificateById(discountCodeId).get();
        if(giftCertificate.getUserId()==userId && giftCertificate.getCurrentlyAvailable()){
            price -= giftCertificate.getDiscountTotal();
            if(price < 0){price = 0;}
            giftCertificate.setCurrentlyAvailable(false);
        }
        return price;
    }


}
