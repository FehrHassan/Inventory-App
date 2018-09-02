package com.fehr.nanodegree.inventoryapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModifyQuantityDialogFragment extends DialogFragment {
    int mQuantity;
    EditText QuantityEditText;

    public static interface QuantityListener {
        void onFinishModifyQuantityDialog(String quantity);
    }

    private QuantityListener mListener;

    static ModifyQuantityDialogFragment newInstance(int quantity) {
        ModifyQuantityDialogFragment f = new ModifyQuantityDialogFragment();
        Bundle args = new Bundle();
        args.putInt("quantity", quantity);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        try {
            mListener = (QuantityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement QuantityListener");
        }
        mQuantity = getArguments().getInt("quantity");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_modify_quantity, null);
        QuantityEditText = (EditText) view.findViewById(R.id.edit_text_quantity);
        QuantityEditText.setText(Integer.toString(mQuantity));
        builder.setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String quantity = getString(R.string.default_product_quantity);
                        if (!TextUtils.isEmpty(QuantityEditText.getText().toString().trim()))
                            quantity = QuantityEditText.getText().toString().trim();
                        mListener.onFinishModifyQuantityDialog(quantity);
                    }
                })
                .setNegativeButton(R.string.cancel, null);
        Button increaseButton = (Button) view.findViewById(R.id.increase_btn);
        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(QuantityEditText.getText().toString().trim());
                quantity++;
                QuantityEditText.setText(String.valueOf(quantity));
            }
        });
        Button decreaseButton = (Button) view.findViewById(R.id.decrease_btn);
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(QuantityEditText.getText().toString().trim());
                if (quantity > 0)
                    quantity--;
                else
                    Toast.makeText(getActivity(), getString(R.string.zero_quantity), Toast.LENGTH_SHORT).show();
                QuantityEditText.setText(String.valueOf(quantity));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }
}
