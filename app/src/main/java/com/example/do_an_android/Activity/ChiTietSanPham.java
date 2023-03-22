package com.example.do_an_android.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.do_an_android.R;
import com.example.do_an_android.Util.DownloadImageTask;
import com.example.do_an_android.Util.Server;
import com.example.do_an_android.Util.VolleySingleton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ChiTietSanPham extends AppCompatActivity {
    int id;
    ImageView hinh;
    Toolbar toolbar_back_chitiet;
    TextView tenSP,giaSP,manhinhSP,hdhSP,camerasauSP,cameratruocSP,chipSP,ramSP,bonhotrongSP,simSP,pinsacSP,txtdatmua;
    private static final String FILE_NAME = "myFile";
    SharedPreferences sharedPreferences;
    private  static int iduser_share,idorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        anhxa();
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        iduser_share = sharedPreferences.getInt("iduser", 0);




        txtdatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent it=new Intent(ChiTietSanPham.this, TrangChu.class);
                startActivity(it);

            }
        });
        toolbar_back_chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(), DanhSachSP.class));
            }
        });
        Intent intent = getIntent();
         id = intent.getIntExtra("idproduct",-1);
        getData();

        getIdOrder(iduser_share);
        idorder = sharedPreferences.getInt("idorder", 0);

        txtdatmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertProduct();

            }

        });

    }
    private static int kiemtra=0;
    private void anhxa(){

        toolbar_back_chitiet = findViewById(R.id.toolbar_back_chitiet);
        tenSP = findViewById(R.id.ten_san_pham_chitiet);
        giaSP = findViewById(R.id.gia_SP_chitiet);
        manhinhSP = findViewById(R.id.txt_manhinh_chitiet);
        hdhSP= findViewById(R.id.txt_hdh_chitiet);
        camerasauSP= findViewById(R.id.txt_camerasau_chitiet);
        cameratruocSP= findViewById(R.id.txt_cameratruoc_chitiet);
        chipSP= findViewById(R.id.txt_chip_chitiet);
        ramSP= findViewById(R.id.txt_ram_chitiet);
        bonhotrongSP= findViewById(R.id.txt_bonhotrong_chitiet);
        simSP= findViewById(R.id.txt_sim_chitiet);
        pinsacSP= findViewById(R.id.txt_pinsac_chitiet);
        txtdatmua=findViewById(R.id.txt_datmua);
        hinh = findViewById(R.id.img_sanpham);
    }//
    private static int giasp;
    private void getData() {
//        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getproductbyid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int idproduct = jsonObject.getInt("idproduct");
                    String nameproduct = jsonObject.getString("nameproduct");
                    int price  =  jsonObject.getInt("price");
                    giasp=price;
                    String manhinh = jsonObject.getString("manhinh");
                    String hdh = jsonObject.getString("hdh");
                    String camerasau = jsonObject.getString("camerasau");
                    String cameratruoc = jsonObject.getString("cameratruoc");
                    String chip = jsonObject.getString("chip");
                    String ram = jsonObject.getString("ram");
                    String bnt = jsonObject.getString("bnt");
                    String sim = jsonObject.getString("sim");
                    String pinsac = jsonObject.getString("pinsac");
                    int sum = jsonObject.getInt("sum");

                    String hinhanh = jsonObject.getString("hinh");
                   // Uri imgUri=Uri.parse(hinhanh);
                    Glide.with(getApplicationContext()).load(hinhanh).into(hinh);
                    tenSP.setText(nameproduct);
                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    giaSP.setText("Giá: "+ formatter.format(price)  + "VNĐ");
                    manhinhSP.setText(manhinh);
                    hdhSP.setText(hdh);
                    camerasauSP.setText(camerasau);
                    cameratruocSP.setText(cameratruoc);
                    chipSP.setText(chip);
                    ramSP.setText(ram);
                    bonhotrongSP.setText(bnt);
                    simSP.setText(sim);
                    pinsacSP.setText(pinsac);

//

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Lỗi " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idproduct", String.valueOf(id));

                return param;
            }
        };
//        requestQueue.add(stringRequest);
        VolleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
    }
    private static String idget;
    private void getIdOrder(int iduser){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.insert, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int _idorder = jsonObject.getInt("id");
//                    Toast.makeText(getApplicationContext(), "id:"+idorder, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
                    editor.putInt("idorder",_idorder);
                    editor.apply();
                    idget=String.valueOf(_idorder);
                    if(idorder!=_idorder){
                        finish();
                        startActivity(new Intent(getApplicationContext(),TrangChu.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Lỗi 1" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("iduser", String.valueOf(iduser));

                return param;
            }
        };
        requestQueue.add(stringRequest);
//        VolleySingleton.getInstance(this).getRequestQueue().add(stringRequest);
//        return idget;
    }
    private  void insertProduct(){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.insertItemToCart, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Done")){
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), GioHang.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Lỗi 2 " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idorder", String.valueOf(idorder));
                param.put("priceproduct", String.valueOf(giasp));
                param.put("idproduct", String.valueOf(id));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(),TrangChu.class));
    }

    public void trovetrangchu(View view) {
        startActivity(new Intent(getApplicationContext(),TrangChu.class));
    }
}