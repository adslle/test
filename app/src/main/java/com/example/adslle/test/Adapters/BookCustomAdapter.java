package com.example.adslle.test.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adslle.test.MainActivity;
import com.example.adslle.test.R;
import com.example.adslle.test.Requests.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ADSLLE on 27/07/2016.
 */
public class BookCustomAdapter  extends BaseAdapter{
    Context context;
    JSONArray jsonArray;
    private static LayoutInflater inflater=null;
    public BookCustomAdapter(MainActivity mainActivity, JSONArray jsonArray) {
        // TODO Auto-generated constructor stub

        context=mainActivity;
        this.jsonArray=jsonArray;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.book_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        String title=null;
        String image=null;
        try {
            title=((JSONObject)jsonArray.get(position)).getString("title");

            image=((JSONObject)jsonArray.get(position)).getString("image_url");
            if(image.equals("null")){
                image= Request.BASE_URL+"img/books/"+((JSONObject)jsonArray.get(position)).getString("image");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.tv.setText(title);
        Log.e("Image "+position,image);

        URL url = null;
        try {
            url = new URL(image);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            holder.img.setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    String title=((JSONObject)jsonArray.get(position)).getString("title");
                    Toast.makeText(context, "You Clicked "+title, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return rowView;
    }

}