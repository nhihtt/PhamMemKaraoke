package tonhi.com.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import tonhi.com.model.BaiHat;
import tonhi.com.phammemkaraoke.MainActivity;
import tonhi.com.phammemkaraoke.R;

public class BaiHatAdapter extends ArrayAdapter<BaiHat> {
    Activity context;
    int resource;
    public BaiHatAdapter(@NonNull Activity context, int resource) {

        super(context, resource);
        this.context=context;
        this.resource=resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=this.context.getLayoutInflater().inflate(this.resource,null);
        TextView txtMa=view.findViewById(R.id.txtMa);
        TextView txtTen=view.findViewById(R.id.txtTen);
        TextView txtCasi=view.findViewById(R.id.txtCasi);
        ImageView imgLike =view.findViewById(R.id.imgLike);
        ImageView imgDisLike = view.findViewById(R.id.imgDislike);
        final BaiHat bh=getItem(position);
        txtMa.setText(bh.getMa());
        txtTen.setText(bh.getTen());
        txtCasi.setText(bh.getCasi());
        if(bh.getThich()==1){
            imgLike.setVisibility(View.INVISIBLE);
            imgDisLike.setVisibility(View.VISIBLE);
        }
        else{
            imgLike.setVisibility(View.VISIBLE);
            imgDisLike.setVisibility(View.INVISIBLE);
        }
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLike(bh);
            }
        });
        imgDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDislike(bh);
            }
        });
        return view;
    }

    private void xuLyDislike(BaiHat bh) {
        ContentValues values=new ContentValues();
        values.put("YEUTHICH",0);

        int kq= MainActivity.database.update(
                MainActivity.TableName,
                values,
                "MABH=?",
                new String[]{bh.getMa()});
        if(kq>0)
            Toast.makeText(context,"Đã gỡ bỏ bài hát["+bh.getTen()+"] khỏi danh saacsh yêu thích thành công",Toast.LENGTH_LONG).show();
    }

    private void xuLyLike(BaiHat bh) {
        ContentValues values=new ContentValues();
        values.put("YEUTHICH",1);

       int kq= MainActivity.database.update(
               MainActivity.TableName,
               values,
               "MABH=?", new String[]{bh.getMa()});
       if(kq>0)
           Toast.makeText(context,"Đã thêm bài hát["+bh.getTen()+"] vào danh saacsh yêu thích thành công",Toast.LENGTH_LONG).show();



    }
}
