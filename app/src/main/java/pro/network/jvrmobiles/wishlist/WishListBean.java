package pro.network.jvrmobiles.wishlist;

import java.util.ArrayList;

import pro.network.jvrmobiles.product.ProductBean;

public class WishListBean {
    ArrayList<ProductBean> data;

    public WishListBean(ArrayList<ProductBean> data) {
        this.data = data;
    }


    public ArrayList<ProductBean> getData() {
        return data;
    }

    public void setData(ArrayList<ProductBean> data) {
        this.data = data;
    }
}