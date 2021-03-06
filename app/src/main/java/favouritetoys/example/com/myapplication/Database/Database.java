package favouritetoys.example.com.myapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import favouritetoys.example.com.myapplication.Model.Order;

/**
 * Created by Divya Gupta on 16-04-2018.
 */

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "EatIt.db";
    private static final int DB_VERSION = 1;


    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"ProductName", "ProductId", "Quantity", "Price", "Discount"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        final List<Order> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                ));
            } while (c.moveToNext());
        }
        return result;

    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProductId,ProductName,Quantity,Price,Discount) VALUES('%s','%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount());

        db.execSQL(query);

    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);

    }

    public void addToFavourites(String foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favourites (FoodId) VALUES ('%s'); ", foodID);
        db.execSQL(query);
    }

    public void removeFromFavourites(String foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM  Favourites WHERE FoodId ='%s';", foodID);
        db.execSQL(query);
    }

    public boolean isFavourite(String foodID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favourites WHERE FoodId='%s';", foodID);
        Cursor c = db.rawQuery(query, null);
        if (c.getCount() <= 0) {
            c.close();
            return false;
        }
        c.close();
        return true;

    }
}
