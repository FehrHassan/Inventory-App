package com.fehr.nanodegree.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fehr.nanodegree.inventoryapp.data.ProductContract.ProductEntry;

public class ProductCursorAdapter extends CursorAdapter {

    private Context mContext;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_product, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mContext = context;

        viewHolder holder = new viewHolder(view);

        final String name = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_IMAGE));
        final Integer quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        final Float price = cursor.getFloat(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        holder.tvName.setText(name);
        holder.tvQuantity.setText(Integer.toString(quantity));
        holder.tvPrice.setText(Float.toString(price));
        if (imagePath != null) {
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.ivImage.setImageURI(Uri.parse(imagePath));
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }

        Button sellButton = (Button) view.findViewById(R.id.btn_sell);
        Object obj = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        sellButton.setTag(obj);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    Object obj = view.getTag();
                    String st = obj.toString();
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_NAME, name);
                    values.put(ProductEntry.COLUMN_PRODUCT_IMAGE, imagePath);
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity >= 1 ? quantity - 1 : 0);
                    values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);

                    Uri currentPetUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, Integer.parseInt(st));

                    int rowsAffected = mContext.getContentResolver().update(currentPetUri, values, null, null);
                    if (rowsAffected == 0 || quantity == 0) {
                        Toast.makeText(mContext, mContext.getString(R.string.sell_product_failed), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    static class viewHolder {
        private TextView tvName;
        private ImageView ivImage;
        private TextView tvQuantity;
        private TextView tvPrice;

        viewHolder(View v) {
            tvName = (TextView) v.findViewById(R.id.name);
            ivImage = (ImageView) v.findViewById(R.id.image);
            tvQuantity = (TextView) v.findViewById(R.id.textview_quantity);
            tvPrice = (TextView) v.findViewById(R.id.textview_price);
        }

    }
}
