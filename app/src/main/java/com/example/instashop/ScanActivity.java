package com.example.instashop;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends AppCompatActivity
{
    SurfaceView surfaceView;
    TextView textViewBarCodeValue;
    BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "null";
    List<String> storeBarcodeItems = new ArrayList<>();
    private static String TAG = "AHHH";
    private final int splash_delay = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initComponents();
        //reference = FirebaseDatabase.getInstance().getReference().child("Products").child("096619756803");
        //reference.keepSynced(true);
        //temp = getIntent().getStringExtra("itemid");
        //addDataOnMatch();
        storeBarcodeItems.add("096619756803");
        storeBarcodeItems.add("09661975681");
        storeBarcodeItems.add("09661975682");
        storeBarcodeItems.add("09661975683");
        storeBarcodeItems.add("09661975684");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                Toast.makeText(this, "Begin Scanning!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cart:
                Toast.makeText(this, "Cart selected", Toast.LENGTH_SHORT).show();
                onPause();
                Intent intent = new Intent(ScanActivity.this, CartActivity.class);
                intent.putExtra("BARCODE_VALUE", "nomatch");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initComponents() {
        textViewBarCodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources()
    {
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamera();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "Scanner Closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCode = detections.getDetectedItems();
                if (barCode.size() > 0) {
                    setBarCode(barCode);
                }
            }
        });
    }

    private void openCamera(){
        try {
            if (ActivityCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(ScanActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBarCode(final SparseArray<Barcode> barCode)
    {
        textViewBarCodeValue.post(new Runnable()
        {
            public void run()
            {
                intentData = barCode.valueAt(0).displayValue;
                textViewBarCodeValue.setText(intentData);
                textViewBarCodeValue.setText(intentData);
                copyToClipBoard(intentData);
                //teacher(intentData);
                barcodeCheck(intentData);
            }
        });
    }

    private void teacher(String temp)
    {
        Intent intent = new Intent(getBaseContext(), CartActivity.class);
        intent.putExtra("BARCODE_VALUE", temp);
        startActivity(intent);
        onPause();
    }

    private void barcodeCheck(String temp)
    {
        if(storeBarcodeItems.contains(temp))
        {
            TAG = temp;
            System.out.println("ASDF: " + TAG);
            teacher(temp);
        }
    }


    private void barcodeChecker(String temp)
    {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference();
        postRef.child("Products").child(temp).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    TAG = dataSnapshot.toString();
                    System.out.println("ASDF: " + TAG);
                    teacher(temp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource != null)
        {
            cameraSource.release();
            cameraSource = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void copyToClipBoard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Barcode Scanner", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                finish();
            else
                openCamera();
        }else
            finish();
    }

    /*
    public void addDataOnMatch()
    {
        reference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Product p = dataSnapshot.getValue(Product.class);
                assert p != null;
                p.setQuantity("1");
                TAG = p.getItem_id();
                setDBase(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

             }
        });
    }


    public void setDBase(Product p)
    {
        Toast.makeText(getApplicationContext(), "Found! Item Added!", Toast.LENGTH_SHORT).show();
        //pClass.setQuantity("1");
        TAG = p.getItem_id();
        System.out.println("ITEM ID: " + TAG);
        System.out.println("Name: " + p.getName());
        System.out.println("Tax: " + p.getTax());
        System.out.println("Cost: " + p.getCost());
        System.out.println("Image: " + p.getImage());
        System.out.println("Discount: " + p.getDiscount());
        System.out.println("Quantity: " + p.getQuantity());
        //new Database(getBaseContext()).addToCart(p);

        new Database(getBaseContext()).addToCart(new Product(
                p.getItem_id(),
                p.getName(),
                p.getTax(),
                p.getCost(),
                p.getImage(),
                p.getDiscount(),
                p.getQuantity()
        ));
    }
    */
}

