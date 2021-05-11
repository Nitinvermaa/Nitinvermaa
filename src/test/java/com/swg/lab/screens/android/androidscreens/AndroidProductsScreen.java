package com.swg.lab.screens.android.androidscreens;


import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.api.control.TextField;
import com.swg.framework.enums.ElementType;
import com.swg.lab.screens.ProductsScreen;
import static com.swg.lab.screens.android.locators.Locators.ProductLocators.*;

/**
 * UI extraction for Products Screen.
 */
public class AndroidProductsScreen extends ProductsScreen {

    /*
     * Holds Products label
     */
    @Override
    public Label getProductsLabel() {
        return getCachedControl(Label.class, PRODUCTS_LABEL, ElementType.byXpath);
    }

    /*
     * Holds First product Add to cart btn
     */
    @Override
    public Button getFirstProdAddToCartBtn() {
        return getCachedControl(Button.class, FIRST_PRODUCT_ADD_TO_CART_BTN, ElementType.byXpath);
    }

    /*
     * Holds Add product to cart by name Add btn
     */
    @Override
    public Button getAddProdByNameToCartBtn(String prodName) {
        return getCachedControl(Button.class, "//*[@text='"+prodName+"']/../android.view.ViewGroup[@content-desc='test-ADD TO CART']", ElementType.byXpath);
    }

    /*
     * Holds First product Name
     */
    @Override
    public Label getFirstProdNameLabel() {
        return getCachedControl(Label.class, FIRST_PRODUCT_NAME, ElementType.byXpath);
    }

    /*
     * Holds First product Remove BTN
     */
    @Override
    public Button getFirstProdRemoveBTN() {
        return getCachedControl(Button.class, FIRST_PRODUCT_REMOVE_BTN, ElementType.byXpath);
    }




}
