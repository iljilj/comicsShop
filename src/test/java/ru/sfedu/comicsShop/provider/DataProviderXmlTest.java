package ru.sfedu.comicsShop.provider;

import org.junit.After;
import org.junit.Test;
import ru.sfedu.comicsShop.Constants;
import ru.sfedu.comicsShop.TestBase;
import ru.sfedu.comicsShop.utils.Status;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.sfedu.comicsShop.utils.ConfigurationUtil.getConfigurationEntry;

public class DataProviderXmlTest extends TestBase {

    DataProviderXml provider = new DataProviderXml();

    @After
    public void cleanUpFiles() throws IOException {
        String[] paths = new String[]{Constants.ITEM_XML,Constants.USER_XML, Constants.CART_XML,
                Constants.GIFT_CERTIFICATE_XML, Constants.PROMO_CODE_XML, Constants.ORDER_XML};
        Writer writer;
        for(String path : paths){
            new FileWriter(getConfigurationEntry(path), false).close();
        }
    }


    @Test
    public void testItem(){

        assertEquals(provider.saveItem(item1WrongPrice()), Status.FAULT);
        assertEquals(provider.saveItem(item1WrongName()), Status.FAULT);
        assertEquals(provider.saveItem(item1()), Status.SUCCESS);
        assertEquals(provider.getItemById(item1().getId()).get(), item1());
        assertEquals(provider.saveItem(item1Alter()), Status.FAULT);
        assertEquals(provider.updateItem(item1Alter()), Status.SUCCESS);
        assertEquals(provider.deleteItem(item1Alter()), Status.SUCCESS);
    }


    @Test
    public void testUser(){

        assertEquals(provider.saveUser(user1WrongFirstName()), Status.FAULT);
        assertEquals(provider.saveUser(user1WrongSecondName()), Status.FAULT);
        assertEquals(provider.saveUser(user1WrongPhoneNumber()), Status.FAULT);
        assertEquals(provider.saveUser(user1()), Status.SUCCESS);
        assertEquals(provider.getUserById(user1().getId()).get(), user1());
        assertEquals(provider.saveUser(user1Alter()), Status.FAULT);
        assertEquals(provider.updateUser(user1Alter()), Status.SUCCESS);
        assertEquals(provider.deleteUser(user1Alter()), Status.SUCCESS);
    }

    @Test
    public void testCart(){

        assertEquals(provider.saveUser(user1()), Status.SUCCESS);
        assertEquals(provider.saveItem(item1()), Status.SUCCESS);

        assertEquals(provider.saveCart(cart1WrongItemId()), Status.FAULT);
        assertEquals(provider.saveCart(cart1WrongUserId()), Status.FAULT);
        assertEquals(provider.saveCart(cart1WrongItemAmount()), Status.FAULT);
        assertEquals(provider.saveCart(cart1()), Status.SUCCESS);
        assertEquals(provider.getCartById(cart1().getId()).get(), cart1());
        assertEquals(provider.saveCart(cart1Alter()), Status.FAULT);
        assertEquals(provider.updateCart(cart1Alter()), Status.SUCCESS);
        assertEquals(provider.deleteCart(cart1Alter()), Status.SUCCESS);

    }

    @Test
    public void testGiftCertificate(){

        assertEquals(provider.saveUser(user1()), Status.SUCCESS);

        assertEquals(provider.saveGiftCertificate(giftCertificate1WrongDiscount()), Status.FAULT);
        assertEquals(provider.saveGiftCertificate(giftCertificate1WrongUserId()), Status.FAULT);
        assertEquals(provider.saveGiftCertificate(giftCertificate1WrongNameEmpty()), Status.FAULT);
        assertEquals(provider.saveGiftCertificate(giftCertificate1()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate1WrongNameNotUnique()), Status.FAULT);
        assertEquals(provider.getGiftCertificateById(giftCertificate1().getId()).get(), giftCertificate1());
        assertEquals(provider.saveGiftCertificate(giftCertificate1Alter()), Status.FAULT);
        assertEquals(provider.updateGiftCertificate(giftCertificate1Alter()), Status.SUCCESS);
        assertEquals(provider.deleteGiftCertificate(giftCertificate1Alter()), Status.SUCCESS);

    }


    @Test
    public void testPromoCode(){

        assertEquals(provider.savePromoCode(promoCode1WrongMinPrice()), Status.FAULT);
        assertEquals(provider.savePromoCode(promoCode1WrongDiscountPercentBig()), Status.FAULT);
        assertEquals(provider.savePromoCode(promoCode1WrongDiscountPercentSmall()), Status.FAULT);
        assertEquals(provider.savePromoCode(promoCode1WrongNameEmpty()), Status.FAULT);
        assertEquals(provider.savePromoCode(promoCode1()), Status.SUCCESS);
        assertEquals(provider.getPromoCodeById(promoCode1().getId()).get(), promoCode1());
        assertEquals(provider.savePromoCode(promoCode1WrongNameNotUnigue()), Status.FAULT);
        assertEquals(provider.savePromoCode(promoCode1Alter()), Status.FAULT);
        assertEquals(provider.updatePromoCode(promoCode1Alter()), Status.SUCCESS);
        assertEquals(provider.deletePromoCode(promoCode1Alter()), Status.SUCCESS);
    }

    @Test
    public void testOrder(){

        assertEquals(provider.saveUser(user2()), Status.SUCCESS);
        assertEquals(provider.saveItem(item2()), Status.SUCCESS);
        assertEquals(provider.saveItem(item3()), Status.SUCCESS);
        assertEquals(provider.saveItem(item4()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate2()), Status.SUCCESS);
        assertEquals(provider.savePromoCode(promoCode2()), Status.SUCCESS);

        assertEquals(provider.saveOrder(order1WrongUserId()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongAddress()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongTotalPrice()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongDiscountCodeId()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongDiscountTotalPrice()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongItemAmountsNegative()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongItemIdsNotExist()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongItemIdsEmpty()), Status.FAULT);
        assertEquals(provider.saveOrder(order1WrongItemsAndAmountsDifferentSize()), Status.FAULT);

        assertEquals(provider.saveOrder(order1()), Status.SUCCESS);
        assertEquals(provider.saveOrder(order2()), Status.SUCCESS);
        assertEquals(provider.getOrderById(order1().getId()).get(), order1());
        assertEquals(provider.saveOrder(order1Alter()), Status.FAULT);
        assertEquals(provider.updateOrder(order1Alter()), Status.SUCCESS);
        assertEquals(provider.deleteOrder(order1Alter()), Status.SUCCESS);
        assertEquals(provider.deleteOrder(order2()), Status.SUCCESS);

    }

    @Test
    public void testCountPrice() {
        assertEquals(provider.saveUser(user11()), Status.SUCCESS);
        assertEquals(provider.saveUser(user12()), Status.SUCCESS);
        assertEquals(provider.saveItem(item11()), Status.SUCCESS);
        assertEquals(provider.saveItem(item12()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart11()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart12()), Status.SUCCESS);

        assertEquals(provider.countPrice(user11().getId()), 720);
        assertEquals(provider.countPrice(user12().getId()), 0); //не положивщий ничего в корзину
        assertEquals(provider.countPrice(10), 0); //не существующий пользователь
    }

    @Test
    public void testEditCart(){
        assertEquals(provider.saveUser(user11()), Status.SUCCESS);
        assertEquals(provider.saveUser(user12()), Status.SUCCESS);
        assertEquals(provider.saveItem(item11()), Status.SUCCESS);
        assertEquals(provider.saveItem(item12()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart11()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart12()), Status.SUCCESS);

        assertEquals(provider.getCartById(cart12().getId()).get().getItemAmount(), 1);
        assertEquals(provider.editCart(3, 3, 10), Status.SUCCESS);
        assertEquals(provider.getCartById(cart12().getId()).get().getItemAmount(), 10);
        assertEquals(provider.editCart(3, 3, 0), Status.SUCCESS);
        assertEquals(provider.getCartById(cart12().getId()), Optional.empty());
        assertEquals(provider.editCart(3, 3, 1), Status.SUCCESS);
        assertEquals(provider.getCartByUserIdAndItemId(3, 3).get().getItemAmount(), 1);

        assertEquals(provider.editCart(9, 3, 0), Status.FAULT);
        assertEquals(provider.editCart(3, 3, -10), Status.FAULT);
    }


    @Test
    public void testEmptyCart(){
        assertEquals(provider.saveUser(user11()), Status.SUCCESS);
        assertEquals(provider.saveUser(user12()), Status.SUCCESS);
        assertEquals(provider.saveItem(item11()), Status.SUCCESS);
        assertEquals(provider.saveItem(item12()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart11()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart12()), Status.SUCCESS);

        assertTrue(provider.getCartById(cart11().getId()).isPresent());
        assertEquals(provider.emptyCart(cart11().getUserId()), Status.SUCCESS);
        assertFalse(provider.getCartById(cart11().getId()).isPresent());
        assertEquals(provider.emptyCart(cart12().getUserId()), Status.FAULT);
    }

    @Test
    public void testEnterPromoCode(){

        assertEquals(provider.savePromoCode(promoCode4()), Status.SUCCESS);
        assertEquals(provider.savePromoCode(promoCode5()), Status.SUCCESS);
        assertEquals(provider.savePromoCode(promoCode6()), Status.SUCCESS);

        assertEquals(provider.enterPromoCode(5, 1000), 900);
        assertEquals(provider.enterPromoCode(6, 1000), 1000);
        assertEquals(provider.enterPromoCode(7, 1000), 1000);
    }

    @Test
    public void testEnterGiftCertificate(){

        assertEquals(provider.saveUser(user21()), Status.SUCCESS);
        assertEquals(provider.saveUser(user22()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate3()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate4()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate5()), Status.SUCCESS);

        assertEquals(provider.enterGiftCertificate(1, 1, 1000), 800);
        assertEquals(provider.enterGiftCertificate(2, 1, 1000), 1000);
        assertEquals(provider.enterGiftCertificate(3, 1, 1000), 1000);

    }

    @Test
    public void testMakeDiscount(){

        assertEquals(provider.saveUser(user21()), Status.SUCCESS);
        assertEquals(provider.saveUser(user22()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate3()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate4()), Status.SUCCESS);
        assertEquals(provider.saveGiftCertificate(giftCertificate5()), Status.SUCCESS);

        assertEquals(provider.savePromoCode(promoCode4()), Status.SUCCESS);
        assertEquals(provider.savePromoCode(promoCode5()), Status.SUCCESS);
        assertEquals(provider.savePromoCode(promoCode6()), Status.SUCCESS);

        assertEquals(provider.makeDiscount(1, "AAA", 1000).getDiscountId(), 5);
        assertEquals(provider.makeDiscount(1, "AAA", 1000).getDiscountPrice(), 900);
        assertEquals(provider.makeDiscount(1, "AAA", 1000).getDiscountStatus(), Status.SUCCESS);

        Long n = null;
        assertEquals(provider.makeDiscount(1, "AAA", 900).getDiscountId(), n);
        assertEquals(provider.makeDiscount(1, "AAA", 900).getDiscountPrice(), 900);
        assertEquals(provider.makeDiscount(1, "AAA", 900).getDiscountStatus(),  Status.FAULT);

        assertEquals(provider.makeDiscount(1, "AAAA", 1000).getDiscountStatus(), Status.SUCCESS);
        assertEquals(provider.updateGiftCertificate(giftCertificate3()), Status.SUCCESS);
        assertEquals(provider.makeDiscount(1, "AAAA", 1000).getDiscountPrice(), 800);
        assertEquals(provider.updateGiftCertificate(giftCertificate3()), Status.SUCCESS);
        assertEquals(provider.makeDiscount(1, "AAAA", 1000).getDiscountId(), 1);

        assertEquals(provider.makeDiscount(1, "BBBB", 1000).getDiscountId(), n);
        assertEquals(provider.makeDiscount(1, "BBBB", 1000).getDiscountPrice(), 1000);
        assertEquals(provider.makeDiscount(1, "BBBB", 1000).getDiscountStatus(), Status.FAULT);

    }

    @Test
    public void testMakeOrder(){
        assertEquals(provider.saveUser(user31()), Status.SUCCESS);
        assertEquals(provider.saveUser(user32()), Status.SUCCESS);
        assertEquals(provider.saveItem(item31()), Status.SUCCESS);
        assertEquals(provider.saveItem(item32()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart31()), Status.SUCCESS);
        assertEquals(provider.saveCart(cart32()), Status.SUCCESS);

        Long n = null;
        assertEquals(provider.makeOrder(1, "Moscow balbla").getOrderStatus(), Status.SUCCESS);
        assertEquals(provider.makeOrder(2, "Moscow balbla").getOrderStatus(), Status.FAULT);
        assertEquals(provider.makeOrder(2, "Moscow balbla").getOrderId(), n);
    }
}
