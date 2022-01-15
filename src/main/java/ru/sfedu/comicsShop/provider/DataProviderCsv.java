package ru.sfedu.comicsShop.provider;

import com.opencsv.bean.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.comicsShop.Constants;
import ru.sfedu.comicsShop.model.*;
import ru.sfedu.comicsShop.utils.HistoryContent;
import ru.sfedu.comicsShop.utils.ReturnDiscount;
import ru.sfedu.comicsShop.utils.ReturnOrder;
import ru.sfedu.comicsShop.utils.Status;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sfedu.comicsShop.Constants.DEFAULT_ACTOR;
import static ru.sfedu.comicsShop.utils.ConfigurationUtil.getConfigurationEntry;
import static ru.sfedu.comicsShop.utils.HistoryUtil.saveHistory;

public class DataProviderCsv implements IDataProvider{
    private static final Logger log = LogManager.getLogger(DataProviderCsv.class.getName());

    private <T> Status beanToCsv(List<T> objects, String className, String path) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            Writer writer = new FileWriter(getConfigurationEntry(path));
            StatefulBeanToCsvBuilder<T> builder = new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<T> beanWriter = builder.build();
            beanWriter.write(objects);
            writer.close();
            HistoryContent historyContent = createHistoryContent(className, methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return Status.SUCCESS;
        }catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    public static <T> List<T> csvToBean(Class<T> tClass, String path) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            FileReader fileReader = new FileReader(getConfigurationEntry(path));
            List<T> objects = new CsvToBeanBuilder<T>(fileReader)
                    .withType(tClass)
                    .withSeparator(',')
                    .build()
                    .parse();
            fileReader.close();
            HistoryContent historyContent = createHistoryContent(tClass.getSimpleName(), methodName, objects, Status.SUCCESS);
            saveHistory(historyContent);
            return objects;
        }catch (Exception e){
            log.error(e);
            HistoryContent historyContent = createHistoryContent(tClass.getSimpleName(), methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return new ArrayList<>();
        }
    }


    private static <T> HistoryContent createHistoryContent(String className, String methodName, T object, Status status) {
        return new HistoryContent(new Date().getTime(), className, new Date().toString(), DEFAULT_ACTOR, methodName, object, status);
    }


    private static <T> String getPath(Class<T> tClass) {
        String path = switch (tClass.getSimpleName()) {
            case "Item" -> Constants.ITEM_CSV;
            case "User" -> Constants.USER_CSV;
            case "Cart" -> Constants.CART_CSV;
            case "GiftCertificate" -> Constants.GIFT_CERTIFICATE_CSV;
            case "PromoCode" -> Constants.PROMO_CODE_CSV;
            case "Order" -> Constants.ORDER_CSV;
            default -> "";
        };
        return path;
    }


    @Override
    public Status saveItem(Item object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getItemById(object.getId()).isEmpty()
                && object.getPrice() > 0
                && !object.getName().isEmpty()){
            String path = getPath(Item.class);
            List<Item> objects = csvToBean(Item.class, path);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
        List<String> itemIdsString =  new ArrayList<String>(Arrays.asList(order.getItemIds().split(" ")));
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
            final long id = object.getId();

            //удалить все записи удаляемого айтема из корзины
            String pathCart = getPath(Cart.class);
            List<Cart> carts;
            Optional<Cart> cart;
            while((csvToBean(Cart.class, pathCart).stream().anyMatch(o -> o.getItemId() == id))){
                carts = csvToBean(Cart.class, pathCart);
                cart = (carts.stream().filter(o -> o.getItemId() == id).findFirst());
                deleteCart(cart.get());
            }
            //удалить все записи удаляемого айтема из заказов
            String pathOrder = getPath(Order.class);
            List<Order> orders;
            Optional<Order> order;
            while((csvToBean(Order.class, pathOrder).stream().anyMatch(o -> isItemInOrder(id, o)))){
                orders = csvToBean(Order.class, pathOrder);
                order = (orders.stream().filter(o -> isItemInOrder(id, o)).findFirst());
                deleteOrder(order.get());
            }

            String path = getPath(Item.class);
            List<Item> objects = csvToBean(Item.class, path);
            objects.removeIf(o -> o.getId() == id);
            Status status = beanToCsv(objects, className, path);
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
    public Optional<Item> getItemById(long id) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = Item.class.getSimpleName();
        String path = getPath(Item.class);
        List<Item> objects = csvToBean(Item.class, path);
        Optional<Item> object = objects.stream().filter(o -> o.getId() == id).findFirst();
        if (object.isPresent()){
            saveHistory( createHistoryContent(className, methodName, object.get(), Status.SUCCESS));
        }else{
            saveHistory(createHistoryContent(className, methodName, null, Status.FAULT));
        }
        return object;
    }

    @Override
    public Status updateItem(Item object) {
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = object.getClass().getSimpleName();
        if (getItemById(object.getId()).isPresent()
                && object.getPrice() > 0
                && !object.getName().isEmpty()){
            final long id = object.getId();
            String path = getPath(Item.class);
            List<Item> objects = csvToBean(Item.class, path);
            objects.removeIf(o -> o.getId() == id);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            String path = getPath(User.class);
            List<User> objects = csvToBean(User.class, path);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            final long id = object.getId();

            //удалить все записи удаляемого юзера из корзины
            String pathCart = getPath(Cart.class);
            List<Cart> carts;
            Optional<Cart> cart;
            while((csvToBean(Cart.class, pathCart).stream().anyMatch(o -> o.getUserId() == id))){
                carts = csvToBean(Cart.class, pathCart);
                cart = (carts.stream().filter(o -> o.getUserId() == id).findFirst());
                deleteCart(cart.get());
            }

            //удалить все записи удаляемого юзера из заказов
            String pathOrder = getPath(Order.class);
            List<Order> orders;
            Optional<Order> order;
            while((csvToBean(Order.class, pathOrder).stream().anyMatch(o -> o.getUserId() == id))){
                orders = csvToBean(Order.class, pathOrder);
                order = (orders.stream().filter(o -> o.getUserId() == id).findFirst());
                deleteOrder(order.get());
            }

            //удалить все подарочные сертификаты удаляемого юзера
            String pathGiftCertificate = getPath(GiftCertificate.class);
            List<GiftCertificate> giftCertificates;
            Optional<GiftCertificate> giftCertificate;
            while((csvToBean(GiftCertificate.class, pathGiftCertificate).stream().anyMatch(o -> o.getUserId() == id))){
                giftCertificates = csvToBean(GiftCertificate.class, pathGiftCertificate);
                giftCertificate = (giftCertificates.stream().filter(o -> o.getUserId() == id).findFirst());
                deleteGiftCertificate(giftCertificate.get());
            }


            String path = getPath(User.class);
            List<User> objects = csvToBean(User.class, path);
            objects.removeIf(o -> o.getId() == id);
            Status status = beanToCsv(objects, className, path);
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
        String path = getPath(User.class);
        List<User> objects = csvToBean(User.class, path);
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
            final long id = object.getId();
            String path = getPath(User.class);
            List<User> objects = csvToBean(User.class, path);
            objects.removeIf(o -> o.getId() == id);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            String path = getPath(Cart.class);
            List<Cart> objects = csvToBean(Cart.class, path);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            final long id = object.getId();
            String path = getPath(Cart.class);
            List<Cart> objects = csvToBean(Cart.class, path);
            objects.removeIf(o -> o.getId() == id);
            Status status = beanToCsv(objects, className, path);
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
        String path = getPath(Cart.class);
        List<Cart> objects = csvToBean(Cart.class, path);
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
            final long id = object.getId();
            String path = getPath(Cart.class);
            List<Cart> objects = csvToBean(Cart.class, path);
            objects.removeIf(o -> o.getId() == id);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
        List<GiftCertificate> giftCertificates = csvToBean(GiftCertificate.class, getPath(GiftCertificate.class));
        GiftCertificate giftCertificate = giftCertificates.stream().filter(o -> o.getName().equals(name)).findFirst().orElse(null);
        List<PromoCode> promoCodes = csvToBean(PromoCode.class, getPath(PromoCode.class));
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
            String path = getPath(GiftCertificate.class);
            List<GiftCertificate> objects = csvToBean(GiftCertificate.class, path);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            final long id = object.getId();


            //в заказах убрать айди удаляемых сертификатов и поменять значение цены без скидки на значение цены со скидкой
            String pathOrder = getPath(Order.class);
            List<Order> orders;
            Optional<Order> order;
            while((csvToBean(Order.class, pathOrder).stream()
                    .anyMatch(o -> o.getDiscountCodeId() != null && o.getDiscountCodeId() == id))){
                orders = csvToBean(Order.class, pathOrder);
                order = (orders.stream().filter(o -> o.getDiscountCodeId() == id).findFirst());
                order.get().setDiscountCodeId(null);
                order.get().setDiscountTotalPrice(order.get().getTotalPrice());
            }


            String path = getPath(GiftCertificate.class);
            List<GiftCertificate> objects = csvToBean(GiftCertificate.class, path);
            objects.removeIf(o -> o.getId() == id);
            Status status = beanToCsv(objects, className, path);
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
        String path = getPath(GiftCertificate.class);
        List<GiftCertificate> objects = csvToBean(GiftCertificate.class, path);
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
        String path = getPath(GiftCertificate.class);
        List<GiftCertificate> objects = csvToBean(GiftCertificate.class, path);
        if (getGiftCertificateById(object.getId()).isPresent()
                //имя промокода либо уникально либо такое же как и было
                && (object.getName().equals(objects.stream().filter(o -> o.getId() == id).findFirst().get().getName())
                || isUniqueCode(object.getName()))
                && !object.getName().isEmpty()
                && getUserById(object.getUserId()).isPresent()
                && object.getDiscountTotal() > 0){
            objects.removeIf(o -> o.getId() == id);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
        if (getGiftCertificateById(object.getId()).isEmpty()
                && getPromoCodeById(object.getId()).isEmpty()
                && isUniqueCode(object.getName())
                && !object.getName().isEmpty()
                && object.getMinTotalPrice() > 0
                && object.getDiscountPercent() > 0
                && object.getDiscountPercent() < 100){
            String path = getPath(PromoCode.class);
            List<PromoCode> objects = csvToBean(PromoCode.class, path);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            final long id = object.getId();


            //в заказах убрать айди удаляемых сертификатов и поменять значение цены со скидкой
            String pathOrder = getPath(Order.class);
            List<Order> orders;
            Optional<Order> order;
            while((csvToBean(Order.class, pathOrder).stream()
                    .anyMatch(o -> o.getDiscountCodeId() != null && o.getDiscountCodeId() == id))){
                orders = csvToBean(Order.class, pathOrder);
                order = (orders.stream().filter(o -> o.getDiscountCodeId() == id).findFirst());
                order.get().setDiscountCodeId(null);
                order.get().setDiscountTotalPrice(order.get().getTotalPrice());
            }


            String path = getPath(PromoCode.class);
            List<PromoCode> objects = csvToBean(PromoCode.class, path);
            objects.removeIf(o -> o.getId() == id);
            Status status = beanToCsv(objects, className, path);
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
        String path = getPath(PromoCode.class);
        List<PromoCode> objects = csvToBean(PromoCode.class, path);
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
        String path = getPath(PromoCode.class);
        List<PromoCode> objects = csvToBean(PromoCode.class, path);
        if  (getPromoCodeById(object.getId()).isPresent()
               // && isUniqueCode(object.getName())
                && (object.getName().equals(objects.stream().filter(o -> o.getId() == id).findFirst().get().getName())
                || isUniqueCode(object.getName()))
                && !object.getName().isEmpty()
                && object.getMinTotalPrice() > 0
                && object.getDiscountPercent() > 0
                && object.getDiscountPercent() < 100
            ){
            objects.removeIf(o -> o.getId() == id);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
        List<String> itemIdsString =  new ArrayList<String>(Arrays.asList(object.getItemIds().split(" ")));
        List<Integer> itemIds = itemIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<String> itemAmountsString =  new ArrayList<String>(Arrays.asList(object.getItemAmounts().split(" ")));
        List<Integer> itemAmounts = itemAmountsString.stream().map(Integer::valueOf).collect(Collectors.toList());

        if (getOrderById(object.getId()).isEmpty()
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
            String path = getPath(Order.class);
            List<Order> objects = csvToBean(Order.class, path);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
            final long id = object.getId();
            String path = getPath(Order.class);
            List<Order> objects = csvToBean(Order.class, path);
            objects.removeIf(o -> o.getId() == id);
            Status status = beanToCsv(objects, className, path);
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
        String path = getPath(Order.class);
        List<Order> objects = csvToBean(Order.class, path);
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

        List<String> itemIdsString =  new ArrayList<String>(Arrays.asList(object.getItemIds().split(" ")));
        List<Integer> itemIds = itemIdsString.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<String> itemAmountsString =  new ArrayList<String>(Arrays.asList(object.getItemAmounts().split(" ")));
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
            final long id = object.getId();
            String path = getPath(Order.class);
            List<Order> objects = csvToBean(Order.class, path);
            objects.removeIf(o -> o.getId() == id);
            objects.add(object);
            Status status = beanToCsv(objects, className, path);
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
        String pathCart = getPath(Cart.class);
        List<Cart> carts;
        Optional<Cart> cart;
        if((csvToBean(Cart.class, pathCart).stream()
                .anyMatch(o -> o.getItemId() == itemId && o.getUserId() == userId))){
            carts = csvToBean(Cart.class, pathCart);
            cart = (carts.stream().filter(o -> o.getItemId() == itemId && o.getUserId() == userId).findFirst());
            return cart;
        }else{
            return Optional.empty();
        }
    }


    //если amount > 0 и указанный товар уже лежит в корзине у указанного пользователя поменять amount
    //если amount > 0 и указанный товар еще не лежит в корзине у указанного пользователя создать новую запись
    //если amount == 0 и указанный товар уже лежит в корзине у указанного пользователя удалить
    //иначе ничего не делать и вернуть статус FAULT
    @Override
    public Status editCart(long userId, long itemId, long amount) {
        if (amount == 0){
            return deleteRecord(userId, itemId);
        }else{
            Optional<Cart> cart = getCartByUserIdAndItemId(userId, itemId);
            if (cart.isEmpty()){
                return createRecord(userId, itemId, amount);
            } else {
                return editRecord(cart.get().getId(), userId, itemId, amount);
            }
        }


//
//
//        Optional<Cart> cart = getCartByUserIdAndItemId(userId, itemId);
//        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
//        final String className = cart.getClass().getSimpleName();
//        Status status;
//        if (cart.isPresent()){
//            if(amount > 0){
//                cart.get().setItemAmount(amount);
//                updateCart(cart.get());
//                status = Status.SUCCESS;
//            }
//            else if(amount == 0){
//                status = deleteCart(cart.get());
//            }
//            else {
//                status = Status.FAULT;
//            }
//            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
//            saveHistory(historyContent);
//        }else{
//            final UUID uid = UUID.randomUUID();
//            long id = uid.getMostSignificantBits() % 1000000000;
//            Cart newCart = new Cart(id, userId, itemId, amount);
//            status = saveCart(newCart);
//            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
//            saveHistory(historyContent);
//        }
//        return status;
    }

    @Override
    public Status deleteRecord(long userId, long itemId){
        Optional<Cart> cart = getCartByUserIdAndItemId(userId, itemId);
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final String className = cart.getClass().getSimpleName();
        if (cart.isPresent()){
            Status status = deleteCart(cart.get());
            HistoryContent historyContent = createHistoryContent(className, methodName, cart.get(), status);
            saveHistory(historyContent);
            return status;
        } else {
            HistoryContent historyContent = createHistoryContent(className, methodName, null, Status.FAULT);
            saveHistory(historyContent);
            return Status.FAULT;
        }
    }

    @Override
    public Status createRecord(long userId, long itemId, long amount){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        final UUID uid = UUID.randomUUID();
        long id = uid.getMostSignificantBits() % 1000000000;
        Cart cart = new Cart(id, userId, itemId, amount);
        Status status = saveCart(cart);
        final String className = cart.getClass().getSimpleName();
        HistoryContent historyContent = createHistoryContent(className, methodName, cart, status);
        saveHistory(historyContent);
        return status;
    }

    @Override
    public Status editRecord(long cartId, long userId, long itemId, long amount){
        final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        Cart cart = new Cart(cartId, userId, itemId, amount);
        Status status = updateCart(cart);
        final String className = cart.getClass().getSimpleName();
        HistoryContent historyContent = createHistoryContent(className, methodName, cart, status);
        saveHistory(historyContent);
        return status;
    }


    @Override
    public Status emptyCart(long userId) {
        Status status = Status.FAULT;
        String pathCart = getPath(Cart.class);
        List<Cart> carts;
        carts = csvToBean(Cart.class, pathCart)
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

        String pathCart = getPath(Cart.class);
        List<Cart> carts;
        carts = csvToBean(Cart.class, pathCart)
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
        String pathCart = getPath(Cart.class);
        List<Cart> carts;
        carts = csvToBean(Cart.class, pathCart)
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

        String pathPromoCode = getPath(PromoCode.class);

        Optional<PromoCode> promoCode = csvToBean(PromoCode.class, pathPromoCode)
                .stream()
                .filter(o->o.getName().equals(discountCode))
                .findFirst();


        String pathGiftCertificate = getPath(GiftCertificate.class);
        Optional<GiftCertificate> giftCertificate = csvToBean(GiftCertificate.class, pathGiftCertificate)
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
