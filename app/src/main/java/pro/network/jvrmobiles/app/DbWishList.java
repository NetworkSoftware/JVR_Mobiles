package pro.network.jvrmobiles.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import pro.network.jvrmobiles.product.ProductListBean;


/**
 * Created by ravi on 15/03/18.
 */

public class DbWishList extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "jvrmobiles_Mobiles_wish";


    public DbWishList(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create Mainbeans table
        db.execSQL(ProductListBean.CREATE_TABLE_WISH);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("ALTER TABLE "+Mainbean.TABLE_NAME_WISH+" ADD COLUMN "+Mainbean.COLUMN_INCLUDE_GST+" TEXT");
        db.execSQL("DROP TABLE IF EXISTS " + ProductListBean.TABLE_NAME_WISH);
        // Create tables again
        onCreate(db);
    }

    public boolean isInWishList(String id, String userid) {
        //onCreate(this.getWritableDatabase());
        if (userid.length() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            String qry = "Select *  from " + ProductListBean.TABLE_NAME_WISH +
                    " where " + ProductListBean.COLUMN_PRO_ID + " = " + id + " AND " + ProductListBean.USER_ID + " = '" + userid+"'";
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } else {
            return false;
        }
    }

    public long insertWishList(ProductListBean mainbean, String userId) {
        if (userId.length() > 0) {
            if (isInWishList(mainbean.id, userId)) {
                mainbean.setQty("1");
                updateWishList(mainbean, userId);
            } else {
                // get writable database as we want to write data
                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                // `id` and `timestamp` will be inserted automatically.
                // no need to add them
                values.put(ProductListBean.COLUMN_PRO_ID, mainbean.getId());
                values.put(ProductListBean.USER_ID, userId);
                values.put(ProductListBean.COLUMN_WISH, mainbean.getWish());
                values.put(ProductListBean.COLUMN_BRAND, mainbean.getBrand());
                values.put(ProductListBean.COLUMN_NAME, mainbean.getName());
                values.put(ProductListBean.COLUMN_ROM, mainbean.getRom());
                values.put(ProductListBean.COLUMN_RAM, mainbean.getRam());
                values.put(ProductListBean.COLUMN_PRICE, mainbean.getPrice());
                values.put(ProductListBean.COLUMN_MODEL, mainbean.getModel());
                values.put(ProductListBean.COLUMN_IMAGE, mainbean.getImage());
                values.put(ProductListBean.COLUMN_DESCRIPTION, mainbean.getDescription());
                values.put(ProductListBean.COLUMN_QTY, mainbean.getQty());
                values.put(ProductListBean.COLUMN_STOCKUPDATE, mainbean.getStock_update());


                // insert row
                long id = db.insert(ProductListBean.TABLE_NAME_WISH, null, values);

                // close db connection
                db.close();
            }


            // return newly inserted row id
            return 1;
        } else {
            return 0;

        }
    }


    public int getWishListCount(String userId) {
        if(userId==null || userId.length()<=0){
            return 0;
        }
        String countQuery = "SELECT  * FROM " + ProductListBean.TABLE_NAME_WISH + " WHERE " + ProductListBean.USER_ID + " = '" + userId+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateWishList(ProductListBean mainbean, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductListBean.COLUMN_PRO_ID, mainbean.getId());
        values.put(ProductListBean.USER_ID, userId);
        values.put(ProductListBean.COLUMN_WISH, mainbean.getWish());
        values.put(ProductListBean.COLUMN_BRAND, mainbean.getBrand());
        values.put(ProductListBean.COLUMN_NAME, mainbean.getName());
        values.put(ProductListBean.COLUMN_ROM, mainbean.getRom());
        values.put(ProductListBean.COLUMN_RAM, mainbean.getRam());
        values.put(ProductListBean.COLUMN_PRICE, mainbean.getPrice());
        values.put(ProductListBean.COLUMN_MODEL, mainbean.getModel());
        values.put(ProductListBean.COLUMN_IMAGE, mainbean.getImage());
        values.put(ProductListBean.COLUMN_DESCRIPTION, mainbean.getDescription());
        values.put(ProductListBean.COLUMN_QTY, mainbean.getQty());


        // updating row
        return db.update(ProductListBean.TABLE_NAME_WISH, values, ProductListBean.COLUMN_PRO_ID + " = ? AND " + ProductListBean.USER_ID + " = ?",
                new String[]{mainbean.getId(), userId});
    }

    public void deleteWishList(ProductListBean productListBean, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductListBean.TABLE_NAME_WISH, productListBean.COLUMN_PRO_ID + " = ? AND " + ProductListBean.USER_ID + " = ?",
                new String[]{productListBean.getId(), userid});
        db.close();
    }

    public void deleteAllWishList(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductListBean.TABLE_NAME_WISH, ProductListBean.USER_ID + " = ?",
                new String[]{userid});
        db.close();
    }

    public ArrayList<ProductListBean> getAllWishList(String userid) {
        if (userid.length() <= 0) {
            return new ArrayList<>();
        }
        ArrayList<ProductListBean> productListBeans = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + ProductListBean.TABLE_NAME_WISH
                + " WHERE " + ProductListBean.USER_ID + " = '" + userid+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ProductListBean productListBean = new ProductListBean();
                productListBean.setId(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_PRO_ID)));
                productListBean.setUserId(cursor.getString(cursor.getColumnIndex(ProductListBean.USER_ID)));
                productListBean.setWish(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_WISH)));
                productListBean.setBrand(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_BRAND)));
                productListBean.setName(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_NAME)));
                productListBean.setRom(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_ROM)));
                productListBean.setRam(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_RAM)));
                productListBean.setPrice(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_PRICE)));
                productListBean.setModel(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_MODEL)));
                productListBean.setImage(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_IMAGE)));
                productListBean.setDescription(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_DESCRIPTION)));
                productListBean.setQty(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_QTY)));
                productListBean.setStock_update(cursor.getString(cursor.getColumnIndex(ProductListBean.COLUMN_STOCKUPDATE)));
                productListBeans.add(productListBean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return productListBeans;
    }

}
