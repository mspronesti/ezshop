package it.polito.ezshop.data;

import java.lang.reflect.Proxy;

public class EZShopControllerFactory {
    static EZShopController create() {
        EZShopControllerImpl controller = new EZShopControllerImpl();
        Class<EZShopController> clazz = EZShopController.class;
        return (EZShopController) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{ clazz },
                new EZShopControllerImpl.ActionHandler(controller)
        );
    }
}
