package ru.zuma.photohack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.core.util.Consumer;
import androidx.fragment.app.DialogFragment;

public class FireMissilesDialogFragment extends DialogFragment {
    private Bitmap bitmap;
    private Runnable onSubmit;

    public void setBitmapDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setOnSubmit(Runnable onSubmit) {
        this.onSubmit = onSubmit;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.dialog_submit_filter, null);
        ((ImageView)view.findViewById(R.id.iv_submit_filter)).setImageBitmap(bitmap);
        builder.setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        onSubmit.run();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO
                    }
                });;

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
