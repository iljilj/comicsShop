package ru.sfedu.comicsShop.provider;

import ru.sfedu.comicsShop.model.*;
import ru.sfedu.comicsShop.utils.ReturnDiscount;
import ru.sfedu.comicsShop.utils.ReturnOrder;
import ru.sfedu.comicsShop.utils.Status;

import java.util.Optional;

public interface IDataProvider {
    Status saveItem(Item item);
    Status deleteItem(Item item);
    Optional<Item> getItemById(long id);
    Status updateItem(Item item);

    Status saveUser(User user);
    Status deleteUser(User user);
    Optional<User> getUserById(long id);
    Status updateUser(User user);

    Status saveCart(Cart cart);
    Status deleteCart(Cart cart);
    Optional<Cart> getCartById(long id);
    Status updateCart(Cart cart);

    Status saveGiftCertificate(GiftCertificate giftCertificate);
    Status deleteGiftCertificate(GiftCertificate giftCertificate);
    Optional<GiftCertificate> getGiftCertificateById(long id);
    Status updateGiftCertificate(GiftCertificate giftCertificate);

    Status savePromoCode(PromoCode promoCode);
    Status deletePromoCode(PromoCode promoCode);
    Optional<PromoCode> getPromoCodeById(long id);
    Status updatePromoCode(PromoCode promoCode);

    Status saveOrder(Order order);
    Status deleteOrder(Order order);
    Optional<Order> getOrderById(long id);
    Status updateOrder(Order order);

    Status editCart(long userId, long itemId, long amount);
    Status emptyCart(long userId);
    Status deleteRecord(long userId, long itemId);
    Status createRecord(long userId, long itemId, long amount);
    Status editRecord(long cartId, long userId, long itemId, long amount);

    ReturnOrder makeOrder(long userId, String address, String discountCode);
    ReturnOrder makeOrder(long userId, String address); //discountCode = null или мб ""

    float countPrice(long userId); //вернет цену без скидки

    ReturnDiscount makeDiscount(long userId, String discountCode, float price); //айди кода, новая цена (со скидкой)
    float enterPromoCode(long discountCodeId, float price); //вернет цену со скидкой
    float enterGiftCertificate(long discountCodeId, long userId, float price); //вернет цену со скидкой

}
