package pro.network.jvrmobiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import nl.joery.animatedbottombar.AnimatedBottomBar;
import nl.joery.animatedbottombar.AnimatedBottomBar.OnTabSelectListener;
import pro.network.jvrmobiles.about.AboutusActivity;
import pro.network.jvrmobiles.app.AppConfig;
import pro.network.jvrmobiles.app.DatabaseHelperJVR;
import pro.network.jvrmobiles.app.DbWishList;
import pro.network.jvrmobiles.cart.CartActivity;
import pro.network.jvrmobiles.orders.MyOrderPage;
import pro.network.jvrmobiles.web.WebActivity;
import pro.network.jvrmobiles.web.WebFragment;
import pro.network.jvrmobiles.wishlist.WishListActivity;

import static android.content.ContentValues.TAG;
import static pro.network.jvrmobiles.app.AppConfig.mypreference;

public class MainActivity extends AppCompatActivity implements CartActivity.OnCartItemChange {

   // private ChipNavigationBar chipNavigationBar;
   AnimatedBottomBar animatedBottomBar;
    private Fragment fragment = null;
    ImageView search;
    SharedPreferences sharedpreferences;
    TextView basketCount, wishListCount;
    FragmentManager fragmentmanager;
    ImageView wishlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jvr);
        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        // chipNavigationBar = findViewById(R.id.chipNavigation);
        animatedBottomBar = findViewById(R.id.bottom_nav);
        if (savedInstanceState == null) {
            animatedBottomBar.selectTabById(R.id.navigation_home, true);
            fragmentmanager = getSupportFragmentManager();
            HomeActivity homeFragment = new HomeActivity();
            fragmentmanager.beginTransaction().replace(R.id.container, homeFragment)
                    .commit();
        }
        animatedBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {
//
            }

            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch (newTab.getId()) {
                    case R.id.navigation_home:
                        fragment = new HomeActivity();
                        break;
                    case R.id.myorders:
                        fragment = new MyOrderPage();
                        break;
                    case R.id.profile:
                        fragment = new AboutusActivity();
                        break;
                    case R.id.map:
                        fragment = new WebFragment();
                        break;
                }

                if (fragment != null) {
                    fragmentmanager = getSupportFragmentManager();
                    fragmentmanager.beginTransaction().replace(R.id.container, fragment)
                            .commit();
                } else {
                    Log.e(TAG, "Error in creating Fragment");
                }
            }
        });



        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AllProductActivity.class);
                intent.putExtra("isSearch", true);
                startActivity(intent);
            }
        });
        ImageView navigation_notifications123 = findViewById(R.id.profile);
        navigation_notifications123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
        ImageView wishlist = findViewById(R.id.wishlist);
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WishListActivity.class));
            }
        });
        //    chipNavigationBar.setItemSelected(R.id.navigation_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeActivity()).commit();
        basketCount = findViewById(R.id.basketCount);
        wishListCount = findViewById(R.id.wishListCount);
        updateCart();
        updateWishList();


    }
    private void updateCart() {
        DatabaseHelperJVR helperYalu = new DatabaseHelperJVR(this);
        if (basketCount != null) {
            if (helperYalu.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                basketCount.setVisibility(View.GONE);
            } else {
                basketCount.setVisibility(View.VISIBLE);
            }
            basketCount.setText(helperYalu.getCartCountYalu(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }
    }

    private void updateWishList() {
        DbWishList dbWishList = new DbWishList(this);
        if (wishListCount != null) {
            if (dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, "")) == 0) {
                wishListCount.setVisibility(View.GONE);
            } else {
                wishListCount.setVisibility(View.VISIBLE);
            }
            wishListCount.setText(dbWishList.getWishListCount(sharedpreferences.getString(AppConfig.userId, "")) + "");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWishList();
        updateCart();
    }

    @Override
    public void onCartChange() {
        updateCart();
        updateWishList();
    }
}