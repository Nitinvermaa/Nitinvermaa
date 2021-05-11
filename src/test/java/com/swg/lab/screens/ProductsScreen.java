package com.swg.lab.screens;


import com.swg.framework.api.ScreenPattern;
import com.swg.framework.api.control.Button;
import com.swg.framework.api.control.Label;
import com.swg.framework.api.control.TextField;

/**
 * UI extraction for Landing Screen.
 */
public abstract class ProductsScreen extends ScreenPattern {

    /**
     * Constructor
     */
    public ProductsScreen() {
        super();
    }


    /*
     * Holds Products label
     */
    public abstract Label getProductsLabel();

    /*
     * Holds First product Add to cart btn
     */
    public abstract Button getFirstProdAddToCartBtn();

    /*
     * Holds Add product to cart by name Add btn
     */
    public abstract Button getAddProdByNameToCartBtn(String prodName);

    /*
     * Holds First product Name
     */
    public abstract Label getFirstProdNameLabel();

    /*
     * Holds First product Remove BTN
     */
    public abstract Button getFirstProdRemoveBTN();
}
