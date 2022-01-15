package ru.sfedu.comicsShop;

import ru.sfedu.comicsShop.model.*;

abstract public class TestBase {
    protected static Item item1(){return new Item(1, "Kakegurui vol. 3", 250);}
    protected static Item item1Alter(){return new Item(1, "Kakegurui vol. 3", 200);}
    protected static Item item1WrongPrice(){return new Item(1, "Kakegurui vol. 3", -250);}
    protected static Item item1WrongName(){return new Item(1, "", 250);}


//    protected static Item item2(){return new Item(2, "Naruto vol. 72", 210);}
//    protected static Item item3(){return new Item(3, "Death Note vol. 5", 300);}
//    protected static Item item4(){return new Item(4, "Fullmetal Alchemist  vol. 2", 180);}

    protected static User user1(){return new User(1, "Egor", "Egorov", "+79814881488");}
    protected static User user1Alter(){return new User(1, "Egor", "Egorov", "+79884201620");}
    protected static User user1WrongFirstName(){return new User(1, "", "Egorov", "+79814881488");}
    protected static User user1WrongSecondName(){return new User(1, "Egor", "", "+79814881488");}
    protected static User user1WrongPhoneNumber(){return new User(1, "Egor", "Egorov", "");}

//    protected static User user2(){return new User(2, "Muha", "Utkin", "+79884201620");}
//    protected static User user3(){return new User(3, "Ivan", "Ivanov", "+79696969669");}

    protected static Cart cart1(){return new Cart(1, 1, 1, 2);}
    protected static Cart cart1Alter(){return new Cart(1, 1, 1, 5);}
    protected static Cart cart1WrongUserId(){return new Cart(1, 2, 1, 2);}
    protected static Cart cart1WrongItemId(){return new Cart(1, 1, 2, 2);}
    protected static Cart cart1WrongItemAmount(){return new Cart(1, 1, 1, -2);}


    protected static GiftCertificate giftCertificate1(){return new GiftCertificate(1, "HOPHOP", true, 1, 200);}
    protected static GiftCertificate giftCertificate1Alter(){return new GiftCertificate(1, "HOPHOP", false, 1, 200);}
    protected static GiftCertificate giftCertificate1WrongNameNotUnique(){return new GiftCertificate(2, "HOPHOP", true, 1, 200);}
    protected static GiftCertificate giftCertificate1WrongNameEmpty(){return new GiftCertificate(1, "", true, 1, 200);}
    protected static GiftCertificate giftCertificate1WrongUserId(){return new GiftCertificate(1, "HOPHOP", true, 5, 200);}
    protected static GiftCertificate giftCertificate1WrongDiscount(){return new GiftCertificate(1, "HOPHOP", true, 1, -200);}


    protected static PromoCode promoCode1(){return new PromoCode(2, "MAY10", true, 1000, 10);}
    protected static PromoCode promoCode1Alter(){return new PromoCode(2, "MAY10", true, 1100, 10);}
    protected static PromoCode promoCode1WrongNameNotUnigue(){return new PromoCode(3, "MAY10", true, 1000, 10);}
    protected static PromoCode promoCode1WrongNameEmpty(){return new PromoCode(2, "", true, 1000, 10);}
    protected static PromoCode promoCode1WrongMinPrice(){return new PromoCode(2, "MAY10", true, -1000, 10);}
    protected static PromoCode promoCode1WrongDiscountPercentSmall(){return new PromoCode(2, "MAY10", true, 1000, -10);}
    protected static PromoCode promoCode1WrongDiscountPercentBig(){return new PromoCode(2, "MAY10", true, 1000, 110);}


    protected static Item item2(){return new Item(2, "Naruto vol. 72", 210);}
    protected static Item item3(){return new Item(3, "Death Note vol. 5", 300);}
    protected static Item item4(){return new Item(4, "Fullmetal Alchemist  vol. 2", 180);}
    protected static GiftCertificate giftCertificate2(){return new GiftCertificate(2, "HOPHOP", true, 2, 200);}
    protected static PromoCode promoCode2(){return new PromoCode(4, "MAY10", true, 1000, 10);}
    protected static User user2(){return new User(2, "Muha", "Utkin", "+79884201620");}


    protected static Order order1(){return new Order(1, 2, "Rostov Hophop h123", 890, 2L, 690, "3 2 4", "2 1 1");}
    protected static Order order2(){return new Order(2, 2, "Rostov bla bla", 890, 4L, 690, "3 2 4", "2 1 1");}

    protected static Order order1Alter(){return new Order(1, 2, "Moscow jhgjh", 890, 2L, 690, "3 2 4", "2 1 1");}

    protected static Order order1WrongUserId(){return new Order(1, 3, "Rostov Hophop h123", 890, 2L, 690, "3 2 4", "2 1 1");}
    protected static Order order1WrongAddress(){return new Order(1, 2, "", 890, 2L, 690, "3 2 4", "2 1 1");}
    protected static Order order1WrongTotalPrice(){return new Order(1, 2, "Rostov Hophop h123", -890, 2L, 690, "3 2 4", "2 1 1");}
    protected static Order order1WrongDiscountCodeId(){return new Order(1, 2, "Rostov Hophop h123", 890, 5L, 690, "3 2 4", "2 1 1");}
    protected static Order order1WrongDiscountTotalPrice(){return new Order(1, 2, "Rostov Hophop h123", 890, 2L, -690, "3 2 4", "2 1 1");}
    protected static Order order1WrongItemAmountsNegative(){return new Order(1, 2, "Rostov Hophop h123", 890, 2L, 690, "3 2 4", "2 -1 1");}
    protected static Order order1WrongItemIdsNotExist(){return new Order(1, 2, "Rostov Hophop h123", 890, 2L, 690, "3 2 9", "2 1 1");}
    protected static Order order1WrongItemIdsEmpty(){return new Order(1, 2, "Rostov Hophop h123", 890, 2L, 690, "", "");}
    protected static Order order1WrongItemsAndAmountsDifferentSize(){return new Order(1, 2, "Rostov Hophop h123", 890, 2L, 690, "3 2", "2 1 1");}



    protected static User user11(){return new User(3, "Aaa", "Aaaaa", "+79880000000");}
    protected static User user12(){return new User(4, "Bbb", "Bbbbb", "+79881111111");}
    protected static Item item11(){return new Item(2, "Naruto vol. 72", 210);}
    protected static Item item12(){return new Item(3, "Death Note vol. 5", 300);}
    protected static Cart cart11(){return new Cart(1, 3, 2, 2);}
    protected static Cart cart12(){return new Cart(2, 3, 3, 1);}


    protected static PromoCode promoCode4(){return new PromoCode(5, "AAA", true, 1000, 10);}
    protected static PromoCode promoCode5(){return new PromoCode(6, "BBB", false, 1000, 10);}
    protected static PromoCode promoCode6(){return new PromoCode(7, "CCC", true, 2000, 20);}

    protected static User user21(){return new User(1, "Aaa", "Aaaaa", "+79880000000");}
    protected static User user22(){return new User(2, "Bbb", "Bbbbb", "+79881111111");}
    protected static GiftCertificate giftCertificate3(){return new GiftCertificate(1, "AAAA", true, 1, 200);}
    protected static GiftCertificate giftCertificate4(){return new GiftCertificate(2, "BBBB", false, 1, 200);}
    protected static GiftCertificate giftCertificate5(){return new GiftCertificate(3, "CCCC", false, 2, 200);}


    protected static User user31(){return new User(1, "Aaa", "Aaaaa", "+79880000000");}
    protected static User user32(){return new User(2, "Bbb", "Bbbbb", "+79881111111");}
    protected static Item item31(){return new Item(1, "Naruto vol. 72", 210);}
    protected static Item item32(){return new Item(2, "Death Note vol. 5", 300);}
    protected static Cart cart31(){return new Cart(1, 1, 1, 2);}
    protected static Cart cart32(){return new Cart(2, 1, 2, 1);}




}
