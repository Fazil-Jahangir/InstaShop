package com.example.instashop.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.instashop.Product;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper
{
    private static final String DB_NAME = "test_database.db";
    private static int DB_VER = 1;

    public Database(Context context)
    {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Product> getCarts()
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID", "Item_id", "Name", "Tax", "Cost", "Image", "Discount", "Quantity"};
        String sqlTable = "OrderDetails";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Product> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do
            {
                result.add(new Product(c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("Item_id")),
                        c.getString(c.getColumnIndex("Name")),
                        c.getString(c.getColumnIndex("Tax")),
                        c.getString(c.getColumnIndex("Cost")),
                        c.getString(c.getColumnIndex("Image")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Quantity"))
                ));
            }
            while(c.moveToNext());
        }
        return result;
    }

    public void addToCart(Product p)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetails(Item_id, Name, Tax, Cost, Image, Discount, Quantity) VALUES ('%s','%s','%s','%s','%s','%s','%s');",
                p.getItem_id(),
                p.getName(),
                p.getTax(),
                p.getCost(),
                p.getImage(),
                p.getDiscount(),
                p.getQuantity());
        db.execSQL(query);
    }

    public void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetails");
        db.execSQL(query);
    }

    public void updateCart(Product p)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetails SET Quantity= %s WHERE ID = %d", p.getQuantity(), p.getID());
        db.execSQL(query);
    }
}
