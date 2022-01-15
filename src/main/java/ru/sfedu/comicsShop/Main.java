package ru.sfedu.comicsShop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.comicsShop.model.*;
import ru.sfedu.comicsShop.provider.DataProviderCsv;
import ru.sfedu.comicsShop.provider.DataProviderSql;
import ru.sfedu.comicsShop.provider.DataProviderXml;
import ru.sfedu.comicsShop.provider.IDataProvider;
import ru.sfedu.comicsShop.utils.ReturnOrder;
import ru.sfedu.comicsShop.utils.Status;

import java.util.Arrays;
import java.util.List;

public class Main {
    private static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args){
        log.info(args[4]);
        try {
            List<String> listArgs = Arrays.asList(args);
            if (listArgs.size() == 0) {
                log.error("Empty input.");
            } else {

                String nameProvider = listArgs.get(4);
                log.info(nameProvider);
                IDataProvider provider = getDataProvider(nameProvider);
                if (provider != null){
                    Status status = run(provider, listArgs);
                    if (status.equals(Status.FAULT)) log.error("Run error");
                } else{
                    log.error("Wrong data provider.");
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private static IDataProvider getDataProvider(String nameProvider) {
        IDataProvider provider = switch (nameProvider) {
            case "CSV" -> new DataProviderCsv();
            case "XML" -> new DataProviderXml();
            case "JSON" -> new DataProviderSql();
            default -> null;
        };
     //   if (provider == null){log.error("No such data provider")}
        return provider;
    }


    public static Status run(IDataProvider provider, List<String> args) {
        switch (args.get(5)) {
            case Constants.CLI_SAVE_ITEM: {
                try {
//                    Float f = (Float.parseFloat(args.get(4)));
//                    float ff = f.floatValue();
                    Item item = new Item(Long.parseLong(args.get(6)), args.get(7), Float.parseFloat(args.get(8)));
                    Status status = provider.saveItem(item);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_DELETE_ITEM:{
                try {
                    Item item = new Item(Long.parseLong(args.get(6)), args.get(7), Float.parseFloat(args.get(8)));
                    Status status = provider.deleteItem(item);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_UPDATE_ITEM:{
                try {
                    Item item = new Item(Long.parseLong(args.get(6)), args.get(7), Float.parseFloat(args.get(8)));
                    Status status = provider.updateItem(item);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_GET_ITEM:{
                try {
                    Item item = provider.getItemById(Long.parseLong(args.get(6))).get();
                    log.info(Status.SUCCESS + " " + args.get(5));
                    log.info(item.toString());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_SAVE_USER: {
                try {
                    User user = new User(Long.parseLong(args.get(6)), args.get(7), args.get(8), args.get(9));
                    Status status = provider.saveUser(user);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_DELETE_USER:{
                try {
                    User user = new User(Long.parseLong(args.get(6)), args.get(7), args.get(8), args.get(9));
                    Status status = provider.deleteUser(user);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_UPDATE_USER:{
                try {
                    User user = new User(Long.parseLong(args.get(6)), args.get(7), args.get(8), args.get(9));
                    Status status = provider.updateUser(user);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_GET_USER:{
                try {
                    User user = provider.getUserById(Long.parseLong(args.get(6))).get();
                    log.info(Status.SUCCESS + " " + args.get(5));
                    log.info(user.toString());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_SAVE_CART: {
                try {
                    Cart cart = new Cart(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)),
                            Long.parseLong(args.get(8)), Long.parseLong(args.get(9)));
                    Status status = provider.saveCart(cart);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_DELETE_CART:{
                try {
                    Cart cart = new Cart(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)),
                            Long.parseLong(args.get(8)), Long.parseLong(args.get(9)));
                    Status status = provider.deleteCart(cart);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_UPDATE_CART:{
                try {
                    Cart cart = new Cart(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)),
                            Long.parseLong(args.get(8)), Long.parseLong(args.get(9)));
                    Status status = provider.updateCart(cart);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_GET_CART:{
                try {
                    Cart cart = provider.getCartById(Long.parseLong(args.get(6))).get();
                    log.info(Status.SUCCESS + " " + args.get(5));
                    log.info(cart.toString());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(1));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_SAVE_GIFT_CERTIFICATE: {
                try {
                    GiftCertificate giftCertificate = new GiftCertificate(Long.parseLong(args.get(6)), args.get(7),
                            Boolean.parseBoolean(args.get(8)), Long.parseLong(args.get(9)), Float.parseFloat(args.get(10)));
                    Status status = provider.saveGiftCertificate(giftCertificate);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_DELETE_GIFT_CERTIFICATE:{
                try {
                    GiftCertificate giftCertificate = new GiftCertificate(Long.parseLong(args.get(6)), args.get(7),
                            Boolean.parseBoolean(args.get(8)), Long.parseLong(args.get(9)), Float.parseFloat(args.get(10)));
                    Status status = provider.deleteGiftCertificate(giftCertificate);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_UPDATE_GIFT_CERTIFICATE:{
                try {
                    GiftCertificate giftCertificate = new GiftCertificate(Long.parseLong(args.get(6)), args.get(7),
                            Boolean.parseBoolean(args.get(8)), Long.parseLong(args.get(9)), Float.parseFloat(args.get(10)));
                    Status status = provider.updateGiftCertificate(giftCertificate);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_GET_GIFT_CERTIFICATE:{
                try {
                    GiftCertificate giftCertificate = provider.getGiftCertificateById(Long.parseLong(args.get(6))).get();
                    log.info(Status.SUCCESS + " " + args.get(5));
                    log.info(giftCertificate.toString());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_SAVE_PROMO_CODE: {
                try {
                    PromoCode promoCode = new PromoCode(Long.parseLong(args.get(6)), args.get(7),
                            Boolean.parseBoolean(args.get(8)), Float.parseFloat(args.get(9)), Long.parseLong(args.get(10)));
                    Status status = provider.savePromoCode(promoCode);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_DELETE_PROMO_CODE:{
                try {
                    PromoCode promoCode = new PromoCode(Long.parseLong(args.get(6)), args.get(7),
                            Boolean.parseBoolean(args.get(8)), Float.parseFloat(args.get(9)), Long.parseLong(args.get(10)));
                    Status status = provider.deletePromoCode(promoCode);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_UPDATE_PROMO_CODE:{
                try {
                    PromoCode promoCode = new PromoCode(Long.parseLong(args.get(6)), args.get(7),
                            Boolean.parseBoolean(args.get(8)), Float.parseFloat(args.get(9)), Long.parseLong(args.get(10)));
                    Status status = provider.updatePromoCode(promoCode);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_GET_PROMO_CODE:{
                try {
                    PromoCode promoCode = provider.getPromoCodeById(Long.parseLong(args.get(6))).get();
                    log.info(Status.SUCCESS + " " + args.get(5));
                    log.info(promoCode.toString());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_SAVE_ORDER: {
                try {
                    Order order;
                    if (!args.get(10).toString().equals("null")){
                        order = new Order(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)), args.get(8),
                                Float.parseFloat(args.get(9)), Long.parseLong(args.get(10)),
                                Float.parseFloat(args.get(11)), args.get(12), args.get(13));
                    } else{
                        order = new Order(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)), args.get(8),
                                Float.parseFloat(args.get(9)), null,
                                Float.parseFloat(args.get(11)), args.get(12), args.get(13));
                    }
                    Status status = provider.saveOrder(order);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_DELETE_ORDER:{
                try {
                    Order order = new Order(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)), args.get(8),
                            Float.parseFloat(args.get(9)), Long.parseLong(args.get(10)),
                            Float.parseFloat(args.get(11)), args.get(12), args.get(13));
                    Status status = provider.deleteOrder(order);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_UPDATE_ORDER:{
                try {
                    Order order;
                    if (!args.get(10).toString().equals("null")){
                        order = new Order(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)), args.get(8),
                                Float.parseFloat(args.get(9)), Long.parseLong(args.get(10)),
                                Float.parseFloat(args.get(11)), args.get(12), args.get(13));
                    } else{
                        order = new Order(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)), args.get(8),
                                Float.parseFloat(args.get(9)), null,
                                Float.parseFloat(args.get(11)), args.get(12), args.get(13));
                    }
                    Status status = provider.updateOrder(order);
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_GET_ORDER:{
                try {
                    Order order = provider.getOrderById(Long.parseLong(args.get(6))).get();
                    log.info(Status.SUCCESS + " " + args.get(5));
                    log.info(order.toString());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_EDIT_CART:{
                try {
                    Status status = provider.editCart(Long.parseLong(args.get(6)), Long.parseLong(args.get(7)), Long.parseLong(args.get(8)));
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_EMPTY_CART:{
                try {
                    Status status = provider.emptyCart(Long.parseLong(args.get(6)));
                    log.info(status + " " + args.get(5));
                    return status;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }
            case Constants.CLI_MAKE_ORDER:{
                try {
                    ReturnOrder returnOrder = null;
                    if (args.size() == 8){
                        returnOrder = provider.makeOrder(Long.parseLong(args.get(6)), args.get(7));
                    }else if (args.size() == 9){
                        returnOrder = provider.makeOrder(Long.parseLong(args.get(6)), args.get(7), args.get(8));
                    }
                    log.info(returnOrder.getOrderStatus() + " " + args.get(5));
                    log.info(returnOrder.getOrderId());
                    return Status.SUCCESS;
                } catch (Exception ex) {
                    log.error(ex + " " + args.get(5));
                    return Status.FAULT;
                }
            }

            default: return Status.FAULT;
            }
    }

}
